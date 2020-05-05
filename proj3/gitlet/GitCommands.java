package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GitCommands {

    static final File CWD = new File(System.getProperty("user.dir"));
    static final File GITLET = Utils.join(CWD, ".gitlet");
    static final File COMMITS = Utils.join(GITLET, "commits");
    static final File BLOBS = Utils.join(GITLET, "blobs");
    static final File STAGING_AREA = Utils.join(GITLET, "staging_area");
    static final File HEAD = Utils.join(GITLET, "head.txt");
    static final File BRANCHES = Utils.join(GITLET, "branches");
    static File removals = Utils.join(STAGING_AREA, "removals.txt");
    static File additions = Utils.join(STAGING_AREA, "additions.txt");

    //static String current_branch;
    static String head_pointer;

    /** Creates a new Gitlet version-control system in the current directory,
     *  with current branch master and an initial commit.
     */
    public static void init() throws IOException {
        if (GITLET.exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        }

        GITLET.mkdir();
        COMMITS.mkdir();
        BLOBS.mkdir();
        STAGING_AREA.mkdir();
        HEAD.createNewFile();
        BRANCHES.mkdir();
        removals.createNewFile();
        additions.createNewFile();
        Stage staged_additions = new Stage("Additions");
        Stage staged_removals = new Stage("Removals");
        staged_additions.writeStageToFile(additions);
        staged_removals.writeStageToFile(removals);

        Commit initialCommit = new Commit("initial commit", new Date(0), null);
        File initCommit = Utils.join(COMMITS, initialCommit.hash());
        initCommit.createNewFile();
        Utils.writeObject(initCommit, initialCommit);

        BranchPointer master = new BranchPointer("master", initialCommit.hash(), true);
        File masterFile = Utils.join(BRANCHES, "master");
        master.writeBranchToFile(masterFile);

        head_pointer = master.getName();
        Utils.writeContents(HEAD, head_pointer);

    }

    public static void add(String fileName) throws IOException {
        File currFile = Utils.join(CWD, fileName);
        if (!currFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        head_pointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch
                (Utils.join(BRANCHES, head_pointer));
        Commit current = Commit.readAsCommit(
                Utils.join(COMMITS, currBranch.getCurrentCommit()));
        Blob fileBlob = new Blob(fileName, currFile);
        Stage additionStage = Stage.readFileAsStage(additions);
        Stage removalStage = Stage.readFileAsStage(removals);

        if (current.contains(fileBlob)) {
            if (additionStage.contains(fileBlob)) {
                additionStage.remove(fileBlob);
            }
        } else {
            if (removalStage.contains(fileBlob)) {
                removalStage.remove(fileBlob);
            }
            additionStage.add(fileBlob);
            fileBlob.makeBlobFile();
        }
        additionStage.writeStageToFile(additions);
        removalStage.writeStageToFile(removals);
    }


    public static void commit(String message) throws IOException {
        Stage additionStage = Stage.readFileAsStage(additions);
        Stage removalStage = Stage.readFileAsStage(removals);
        if (additionStage.isEmpty() && removalStage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        } else if(message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        head_pointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch
                (Utils.join(BRANCHES, head_pointer));
        Commit latestCommit = Commit.readAsCommit(
                Utils.join(COMMITS, currBranch.getCurrentCommit()));
        Commit newCommit = latestCommit.copy(message);
        newCommit.setFirstParent(latestCommit);
        newCommit.addStagedFiles();
        newCommit.removeStagedFiles();

        BranchPointer currentBranch = new BranchPointer();
        for (File f : Objects.requireNonNull(BRANCHES.listFiles())) {
            currentBranch = BranchPointer.readFileAsBranch(f);
            if (currentBranch.isCurrentBranch()) {
                break;
            }
        }
        currentBranch.setCurrentCommit(newCommit.hash());

        additionStage.removeAll();
        removalStage.removeAll();
        additionStage.writeStageToFile(additions);
        removalStage.writeStageToFile(removals);
        currentBranch.writeBranchToFile(Utils.join(BRANCHES, currentBranch.getName()));
        newCommit.makeCommitFile();
    }


    public static void rm(String fileName) {
        Stage additionStage = Stage.readFileAsStage(additions);
        Stage removalStage = Stage.readFileAsStage(removals);

        head_pointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch
                (Utils.join(BRANCHES, head_pointer));
        Commit latestCommit = Commit.readAsCommit(
                Utils.join(COMMITS, currBranch.getCurrentCommit()));

        if (additionStage.contains(fileName)) {
            additionStage.remove(fileName);
        } else if (latestCommit.contains(fileName)) {
            Blob currBlob = latestCommit.getBlob(fileName);
            removalStage.add(currBlob);
            File cwdFile = Utils.join(CWD, fileName);
            if (cwdFile.exists()) {
                Utils.restrictedDelete(cwdFile);
            }
        } else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        additionStage.writeStageToFile(additions);
        removalStage.writeStageToFile(removals);
    }

    public static void log() {
        head_pointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch
                (Utils.join(BRANCHES, head_pointer));
        String current = currBranch.getCurrentCommit();

        while (current != null) {
            Commit currentCommit = Commit.readAsCommit(Utils.join(COMMITS, current));
            currentCommit.displayCommit();
            current = currentCommit.getFirstParent();
        }
    }

    public static void globalLog() {
        for (File f : Objects.requireNonNull(COMMITS.listFiles())) {
            Commit currentCommit = Commit.readAsCommit(f);
            currentCommit.displayCommit();
        }
    }

    public static void find(String message) {
        ArrayList<String> ids = new ArrayList<>();

        for (File f : Objects.requireNonNull(COMMITS.listFiles())) {
            Commit currentCommit = Commit.readAsCommit(f);
            if (currentCommit.getMessage().equals(message)) {
                ids.add(currentCommit.hash());
            }
        }

        if (ids.isEmpty()) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }

        for (String s : ids) {
            System.out.println(s);
        }
    }

    public static void status() {
        Stage additionStage = Stage.readFileAsStage(additions);
        Stage removalStage = Stage.readFileAsStage(removals);

        System.out.println("=== Branches ===");
        for (File f : Objects.requireNonNull(BRANCHES.listFiles())) {
            BranchPointer currentBranch = BranchPointer.readFileAsBranch(f);
            String branchName = currentBranch.getName();
            if (currentBranch.isCurrentBranch()) {
                branchName = "*" + branchName;
            }
            System.out.println(branchName);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (String s : additionStage.getKeys()) {
            System.out.println(s);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String s : removalStage.getKeys()) {
            System.out.println(s);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===\n");
        System.out.println("=== Untracked Files ===\n");

    }

    public static void checkout(String arg1, String arg2, int commandType)
            throws IOException {

        if (commandType == 1) {
            head_pointer = Utils.readContentsAsString(HEAD);
            BranchPointer currBranch = BranchPointer.readFileAsBranch
                    (Utils.join(BRANCHES, head_pointer));
            Commit latestCommit = Commit.readAsCommit(
                    Utils.join(COMMITS, currBranch.getCurrentCommit()));

            if (!latestCommit.contains(arg1)) {
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            }

            checkoutHelper(latestCommit, arg1);
        } else if (commandType == 2) {
            if (arg1.length() < 40) {
                for (File f : Objects.requireNonNull(COMMITS.listFiles())) {
                    if (f.getName().substring(0, arg1.length())
                            .equals(arg1)) {
                        arg1 = f.getName();
                    }
                }
            }

            File commitFile = Utils.join(COMMITS, arg1);
            if (!commitFile.exists()) {
                System.out.println("No commit with that id exists.");
                System.exit(0);
            }

            Commit latestCommit = Commit.readAsCommit(commitFile);
            if (!latestCommit.contains(arg2)) {
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            }

            checkoutHelper(latestCommit, arg2);
        } else {
            File branchFile = Utils.join(BRANCHES, arg1);
            if (!branchFile.exists()) {
                System.out.println("No such branch exists.");
                System.exit(0);
            }

            head_pointer = Utils.readContentsAsString(HEAD);
            BranchPointer newBranch = BranchPointer.readFileAsBranch(branchFile);
            BranchPointer currBranch = BranchPointer.readFileAsBranch(
                    Utils.join(BRANCHES, head_pointer));
            if (newBranch.isCurrentBranch()) {
                System.out.println("No need to checkout the current branch.");
                System.exit(0);
            }

            Stage additionStage = Stage.readFileAsStage(additions);
            Stage removalStage = Stage.readFileAsStage(removals);
            additionStage.removeAll();
            removalStage.removeAll();
            additionStage.writeStageToFile(additions);
            removalStage.writeStageToFile(removals);

            Commit checkedOutCommit =
                    Commit.readAsCommit(Utils.join(COMMITS, newBranch.getCurrentCommit()));
            Commit currentCommit =
                    Commit.readAsCommit(Utils.join(COMMITS, currBranch.getCurrentCommit()));

            for (String s : checkedOutCommit.getFiles().keySet()) {
                File checkedFile = Utils.join(CWD, s);
                if (checkedFile.exists()) {
                    Blob checkedFileAsBlob = new Blob(s, checkedFile);
                    File blobFile = Utils.join(BLOBS, checkedFileAsBlob.hash());
                    if (!blobFile.exists()) {
                        System.out.println("There is an untracked file in the way; "
                                + "delete it, or add and commit it first.");
                        System.exit(0);
                    }
                }
                checkoutHelper(checkedOutCommit, s);
            }

            for (String s : currentCommit.getFiles().keySet() ) {
                if (!checkedOutCommit.contains(s)
                    && Utils.join(CWD, s).exists()) {
                    Utils.restrictedDelete(Utils.join(CWD, s));
                }
            }

            currBranch.setCurrentBranch(false);
            newBranch.setCurrentBranch(true);
            head_pointer = newBranch.getName();
            newBranch.writeBranchToFile(Utils.join(BRANCHES, newBranch.getName()));
            currBranch.writeBranchToFile(Utils.join(BRANCHES, currBranch.getName()));
            Utils.writeContents(HEAD, head_pointer);

        }

    }

    public static void checkoutHelper(Commit commit, String fileName)
            throws IOException {
        Blob wantedBlob = commit.getBlob(fileName);
        byte[] contents = wantedBlob.getContents();

        File file = Utils.join(CWD, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        Utils.writeContents(file, contents);

    }

    public static void branch(String branchName) throws IOException {
        File file = Utils.join(BRANCHES, branchName);
        if (file.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }

        head_pointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch
                (Utils.join(BRANCHES, head_pointer));
        BranchPointer newBranch = new BranchPointer(branchName,
                currBranch.getCurrentCommit(), false);
        newBranch.writeBranchToFile(file);

    }
}
