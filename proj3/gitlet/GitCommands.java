package gitlet;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.TreeSet;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Set;
import java.util.Date;

/** Commands for Gitlet.
 *  @author Amit Bhat
 */
public class GitCommands {

    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main Gitlet repo folder. */
    static final File GITLET = Utils.join(CWD, ".gitlet");

    /** Folder containing serialized Commit objects. */
    static final File COMMITS = Utils.join(GITLET, "commits");

    /** Folder containing serialized Blob objects. */
    static final File BLOBS = Utils.join(GITLET, "blobs");

    /** Folder containing serialized Blob objects. */
    static final File STAGING_AREA = Utils.join(GITLET, "staging_area");

    /** Folder containing serialized BranchPointer objects. */
    static final File BRANCHES = Utils.join(GITLET, "branches");

    /** Text file containing the name of branch that the HEAD pointer is on. */
    static final File HEAD = Utils.join(GITLET, "head.txt");

    /** Text file containing the serialized Stage object
     *  representing removals. */
    static final File REMOVALS = Utils.join(STAGING_AREA, "removals.txt");

    /** Text file containing the serialized Stage object
     *  representing additions. */
    static final File ADDITIONS = Utils.join(STAGING_AREA, "additions.txt");

    /** Static variable used to denote HEAD. */
    private static String headPointer;

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
        REMOVALS.createNewFile();
        ADDITIONS.createNewFile();
        Stage stagedAdditions = new Stage("Additions");
        Stage stagedRemovals = new Stage("Removals");
        stagedAdditions.writeStageToFile(ADDITIONS);
        stagedRemovals.writeStageToFile(REMOVALS);

        Commit initialCommit = new Commit("initial commit", new Date(0), null);
        File initCommit = Utils.join(COMMITS, initialCommit.hash());
        initCommit.createNewFile();
        Utils.writeObject(initCommit, initialCommit);

        BranchPointer master = new BranchPointer("master",
                initialCommit.hash(), true);
        File masterFile = Utils.join(BRANCHES, "master");
        master.writeBranchToFile(masterFile);

        headPointer = master.getName();
        Utils.writeContents(HEAD, headPointer);

    }

    /** Adds the file with name FILENAME to staging area in advance of
     *  being committed. */
    public static void add(String fileName) throws IOException {
        File currFile = Utils.join(CWD, fileName);
        if (!currFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        headPointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch(headPointer);
        Commit current = Commit.readAsCommit(currBranch.getCurrentCommit());
        Blob fileBlob = new Blob(fileName, currFile);
        Stage additionStage = Stage.readFileAsStage(ADDITIONS);
        Stage removalStage = Stage.readFileAsStage(REMOVALS);

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
        additionStage.writeStageToFile(ADDITIONS);
        removalStage.writeStageToFile(REMOVALS);
    }

    /** Commits the changes found in the staged additions and
     *  staged removals folders. Creates Commit object with
     *  MESSAGE. */
    public static void commit(String message) throws IOException {
        Stage additionStage = Stage.readFileAsStage(ADDITIONS);
        Stage removalStage = Stage.readFileAsStage(REMOVALS);
        if (additionStage.isEmpty() && removalStage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        } else if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        headPointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch(headPointer);
        Commit latestCommit = Commit.readAsCommit(
                currBranch.getCurrentCommit());
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
        additionStage.writeStageToFile(ADDITIONS);
        removalStage.writeStageToFile(REMOVALS);
        currentBranch.writeBranchToFile(
                Utils.join(BRANCHES, currentBranch.getName()));
        newCommit.makeCommitFile();
    }

    /** Unstage the file with name FILENAME if it is currently staged for
     *  addition. If the file is tracked in the current commit,
     *  stage it for removal and remove the file from the working
     *  directory if user has not already done so. */
    public static void rm(String fileName) {
        Stage additionStage = Stage.readFileAsStage(ADDITIONS);
        Stage removalStage = Stage.readFileAsStage(REMOVALS);

        headPointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch(headPointer);
        Commit latestCommit = Commit.readAsCommit(
                currBranch.getCurrentCommit());

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
        additionStage.writeStageToFile(ADDITIONS);
        removalStage.writeStageToFile(REMOVALS);
    }

    /** Displays information about each commit in the current branch. */
    public static void log() {
        headPointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch(headPointer);
        String current = currBranch.getCurrentCommit();

        while (current != null) {
            Commit currentCommit = Commit.readAsCommit(current);
            currentCommit.displayCommit();
            current = currentCommit.getFirstParent();
        }
    }

    /** Displays information about each commit in the current branch. */
    public static void globalLog() {
        for (File f : Objects.requireNonNull(COMMITS.listFiles())) {
            Commit currentCommit = Commit.readAsCommit(f);
            currentCommit.displayCommit();
        }
    }

    /** Print the UIDs of all the commits with message MESSAGE. */
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

    /** Displays what branches currently exist and what files
     *  have been staged for addition or removal.
     *  Indicates the current branch with a *. */
    public static void status() {
        Stage additionStage = Stage.readFileAsStage(ADDITIONS);
        Stage removalStage = Stage.readFileAsStage(REMOVALS);

        System.out.println("=== Branches ===");
        Set<String> sorter = new TreeSet<String>();

        for (File f : Objects.requireNonNull(BRANCHES.listFiles())) {
            BranchPointer currentBranch = BranchPointer.readFileAsBranch(f);
            String branchName = currentBranch.getName();
            sorter.add(branchName);
        }
        for (String f : sorter) {
            BranchPointer temp = BranchPointer.readFileAsBranch(f);
            if (temp.isCurrentBranch()) {
                f = "*" + f;
            }
            System.out.println(f);
        }
        System.out.println();
        sorter.clear();

        sorter.addAll(additionStage.getKeys());
        System.out.println("=== Staged Files ===");
        for (String s : sorter) {
            System.out.println(s);
        }
        System.out.println();
        sorter.clear();

        sorter.addAll(removalStage.getKeys());
        System.out.println("=== Removed Files ===");
        for (String s : sorter) {
            System.out.println(s);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===\n");
        System.out.println("=== Untracked Files ===\n");
    }

    /** Takes the version of the file with name ARG1
     *  as it exists in the head commit, the front of the current
     *  branch, and puts it in the working directory. */
    public static void checkout1(String arg1)
            throws IOException {
        headPointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch(headPointer);
        Commit latestCommit = Commit.readAsCommit(
                currBranch.getCurrentCommit());

        if (!latestCommit.contains(arg1)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }

        checkoutHelper(latestCommit, arg1);
    }

    /** checkout [commit id] -- [file name]: Takes the version of the file
     *  with name ARG2 as it exists in the commit with UID ARG1, and
     *  puts it in the working directory. */
    public static void checkout2(String arg1, String arg2)
            throws IOException {
        arg1 = shortUID(arg1);

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
    }

    /** Takes all files in the commit at the head of
     *  the branch with name ARG1, and puts them in the working directory.
     *  Also, at the end of this command, the given branch will
     *  be the current branch (HEAD). Any files that are tracked in the
     *  current branch but are not present in the checked-out branch are
     *  deleted. The staging area is cleared. */
    public static void checkout3(String arg1)
            throws IOException {
        File branchFile = Utils.join(BRANCHES, arg1);
        if (!branchFile.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        headPointer = Utils.readContentsAsString(HEAD);
        BranchPointer newBranch = BranchPointer.readFileAsBranch(branchFile);
        BranchPointer currBranch = BranchPointer.readFileAsBranch(headPointer);
        if (newBranch.isCurrentBranch()) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        resetHelper(currBranch.getCurrentCommit(),
                newBranch.getCurrentCommit());

        currBranch.setCurrentBranch(false);
        newBranch.setCurrentBranch(true);
        headPointer = newBranch.getName();
        newBranch.writeBranchToFile(Utils.join(BRANCHES, newBranch.getName()));
        currBranch.writeBranchToFile(Utils.join(BRANCHES,
                currBranch.getName()));
        Utils.writeContents(HEAD, headPointer);
    }

    /** Helper function for reset and checkout3 that handles insertion and
     *  deletion of files in the working directory as needed.
     *  Takes in the current commit's UID, CURRENT, and the checked out commit's
     *  UID, CHECKEDOUT. */
    public static void resetHelper(String current, String checkedOut)
            throws IOException {
        Commit checkedOutCommit = Commit.readAsCommit(checkedOut);
        Commit currentCommit = Commit.readAsCommit(current);

        Stage additionStage = Stage.readFileAsStage(ADDITIONS);
        Stage removalStage = Stage.readFileAsStage(REMOVALS);
        additionStage.removeAll();
        removalStage.removeAll();
        additionStage.writeStageToFile(ADDITIONS);
        removalStage.writeStageToFile(REMOVALS);

        for (String s : checkedOutCommit.getNames()) {
            unTrackedFileChecker(s);
            checkoutHelper(checkedOutCommit, s);
        }

        for (String s : currentCommit.getFiles().keySet()) {
            if (!checkedOutCommit.contains(s) && Utils.join(CWD, s).exists()) {
                Utils.restrictedDelete(Utils.join(CWD, s));
            }
        }
    }

    /** Makes sure the file with name NAME in the
     *  Current Working Directory is not untracked. */
    public static void unTrackedFileChecker(String name) {
        File checkedFile = Utils.join(CWD, name);
        if (checkedFile.exists()) {
            Blob checkedFileAsBlob = new Blob(name, checkedFile);
            File blobFile = Utils.join(BLOBS, checkedFileAsBlob.hash());
            if (!blobFile.exists()) {
                System.out.println("There is an untracked file in the "
                        + "way; delete it, or add and commit it first.");
                System.exit(0);
            }
        }
    }


    /** Helper function for checkout that adds the file from COMMIT
     *  with name FILENAME to the Current Working Directory. */
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

    /** Takes in a string COMMITID less than 40 characters representing a
     *  Commit UID and returns the full UID of the Commit. */
    public static String shortUID(String commitId) {
        if (commitId.length() < Utils.UID_LENGTH) {
            for (File f : Objects.requireNonNull(COMMITS.listFiles())) {
                if (f.getName().substring(0, commitId.length())
                        .equals(commitId)) {
                    commitId = f.getName();
                }
            }
        }
        return commitId;
    }

    /** Creates a new branch with name BRANCHNAME, and points it
     *  at the current head node. */
    public static void branch(String branchName) throws IOException {
        File file = Utils.join(BRANCHES, branchName);
        if (file.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }

        headPointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch(headPointer);
        BranchPointer newBranch = new BranchPointer(branchName,
                currBranch.getCurrentCommit(), false);
        newBranch.writeBranchToFile(file);
    }

    /** Deletes the branch with name BRANCHNAME. */
    public static void rmBranch(String branchName) {

        File branchFile = Utils.join(BRANCHES, branchName);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        BranchPointer branch = BranchPointer.readFileAsBranch(branchFile);
        if (branch.isCurrentBranch()) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        branchFile.delete();
    }

    /** Checks out all the files tracked by the commit with
     *  ID COMMITID. */
    public static void reset(String commitId)
            throws IOException {
        commitId = shortUID(commitId);
        File commitFile = Utils.join(COMMITS, commitId);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        headPointer = Utils.readContentsAsString(HEAD);
        BranchPointer currBranch = BranchPointer.readFileAsBranch(headPointer);

        resetHelper(currBranch.getCurrentCommit(), commitId);
        currBranch.setCurrentCommit(commitId);
        currBranch.writeBranchToFile(Utils.join(BRANCHES, headPointer));
    }

    /** Returns the split point between the commits CURRENT and CHECKEDOUT
     *  with minimum distance from the current branch head. */
    public static String findSplitPoint(String current, String checkedOut) {
        if (current == null || checkedOut == null) {
            return null;
        }
        if (current.equals(checkedOut)) {
            return current;
        }
        Commit currCommit = Commit.readAsCommit(current);
        Commit newCommit = Commit.readAsCommit(checkedOut);
        String firstCurrPath = findSplitPoint(
                currCommit.getFirstParent(), checkedOut);
        String firstCheckedPath = findSplitPoint(current,
                newCommit.getFirstParent());
        String secondCurrPath = findSplitPoint(
                currCommit.getSecondParent(), checkedOut);
        String secondCheckedPath = findSplitPoint(current,
                newCommit.getSecondParent());

        int dist1 = Commit.distance(current, firstCurrPath),
                dist2 = Commit.distance(current, firstCheckedPath),
                dist3 = Commit.distance(current, secondCurrPath),
                dist4 = Commit.distance(current, secondCheckedPath);
        int minDist = Integer.min(Integer.min(
                Integer.min(dist1, dist2), dist3), dist4);

        if (minDist == dist1) {
            return firstCurrPath;
        } else if (minDist == dist2) {
            return firstCheckedPath;
        } else if (minDist == dist3) {
            return secondCurrPath;
        } else {
            return secondCheckedPath;
        }
    }

    /** Merges files from the branch with name BRANCHNAME into the
     *  current branch. */
    public static void merge(String branchName) throws IOException {
        Stage additionStage = Stage.readFileAsStage(ADDITIONS);
        Stage removalStage = Stage.readFileAsStage(REMOVALS);
        if (!additionStage.isEmpty() || !removalStage.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        headPointer = Utils.readContentsAsString(HEAD);
        File branchFile = Utils.join(BRANCHES, branchName);
        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (headPointer.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        BranchPointer checkedBranch = BranchPointer.
                readFileAsBranch(branchFile);
        BranchPointer currBranch = BranchPointer.readFileAsBranch(headPointer);
        String splitPoint = findSplitPoint(currBranch.getCurrentCommit(),
                checkedBranch.getCurrentCommit());
        if (splitPoint.equals(checkedBranch.getCurrentCommit())) {
            System.out.println("Given branch is an ancestor of the "
                    + "current branch.");
        } else if (splitPoint.equals(currBranch.getCurrentCommit())) {
            GitCommands.checkout3(branchName);
            System.out.println("Current branch fast-forwarded.");
        } else {
            Commit checked = Commit.readAsCommit(
                    checkedBranch.getCurrentCommit());
            Commit current = Commit.readAsCommit(currBranch.getCurrentCommit());
            Commit split = Commit.readAsCommit(splitPoint);
            for (String name : checked.getNames()) {
                unTrackedFileChecker(name);
                if (!split.contains(name) && !current.contains(name)) {
                    GitCommands.checkout2(
                            checkedBranch.getCurrentCommit(), name);
                    GitCommands.add(name);
                }
            }
            for (String name : current.getNames()) {
                if (checked.contains(name) && split.contains(name)) {
                    if (!(checked.getUID(name).equals(split.getUID(name)))
                        && split.getUID(name).equals(current.getUID(name))) {
                        GitCommands.checkout2(
                                checkedBranch.getCurrentCommit(), name);
                        GitCommands.add(name);
                    }
                } else if (split.contains(name)) {
                    if (split.getUID(name).equals(current.getUID(name))) {
                        GitCommands.rm(name);
                        Utils.join(BLOBS, name).delete();
                    }
                }
            }
            conflictHandler(checked, current, split);
            mergeCommit(branchName);
        }
    }

    /** Returns true if file NAME has a merge conflict when comparing Commits
     *  CHECKED, CURRENT, and SPLIT. */
    public static boolean conflictChecker(Commit checked, Commit current,
                                       Commit split, String name) {

        boolean diffFiles = (checked.contains(name) && current.contains(name)
                && split.contains(name))
                && !checked.getUID(name).equals(split.getUID(name))
                && !current.getUID(name).equals(split.getUID(name))
                && !current.getUID(name).equals((checked.getUID(name)));
        boolean oneEmpty = split.contains(name)
                && ((checked.contains(name) && !current.contains(name)
                && !checked.getUID(name).equals(split.getUID(name)))
                || (!checked.contains(name) && current.contains(name)
                && !current.getUID(name).equals(split.getUID(name))));
        boolean notInSplit = !split.contains(name)
                && checked.contains(name) && current.contains(name)
                && !checked.getUID(name).equals(current.getUID(name));

        return diffFiles || oneEmpty || notInSplit;
    }

    /** Helper function that handles merge conflict cases in the Commits
     *  CHECKED, CURRENT, and SPLIT. */
    public static void conflictHandler(Commit checked, Commit current,
                                          Commit split) throws IOException {
        boolean anyConflict = false;
        for (String name : current.getNames()) {
            boolean conflict = conflictChecker(checked, current, split, name);
            if (conflict) {
                String curr = "", check = "";
                if (current.contains(name) && checked.contains(name)) {
                    curr = current.getBlob(name).getContentsAsString();
                    check = checked.getBlob(name).getContentsAsString();
                } else if (checked.contains(name)) {
                    check = checked.getBlob(name).getContentsAsString();
                } else if (current.contains(name)) {
                    curr = current.getBlob(name).getContentsAsString();
                }
                String newContents = "<<<<<<< HEAD\n"
                        + curr + "=======\n" + check + ">>>>>>>\n";
                if (!Utils.join(CWD, name).exists()) {
                    Utils.join(CWD, name).createNewFile();
                }
                PrintWriter writer = new PrintWriter(
                        Utils.join(CWD, name), StandardCharsets.UTF_8);
                writer.print(newContents);
                writer.close();
                GitCommands.add(name);
                anyConflict = true;
            }
        }
        for (String name : checked.getNames()) {
            boolean conflict = conflictChecker(checked, current, split, name);
            if (conflict) {
                String curr = "", check = "";
                if (current.contains(name) && checked.contains(name)) {
                    curr = current.getBlob(name).getContentsAsString();
                    check = checked.getBlob(name).getContentsAsString();
                } else if (checked.contains(name)) {
                    check = checked.getBlob(name).getContentsAsString();
                } else if (current.contains(name)) {
                    curr = current.getBlob(name).getContentsAsString();
                }
                String newContents = "<<<<<<< HEAD\n"
                        + curr + "=======\n" + check + ">>>>>>>\n";
                if (!Utils.join(CWD, name).exists()) {
                    Utils.join(CWD, name).createNewFile();
                }
                PrintWriter writer = new PrintWriter(
                        Utils.join(CWD, name), StandardCharsets.UTF_8);
                writer.print(newContents);
                writer.close();
                GitCommands.add(name);
                anyConflict = true;
            }
        }
        if (anyConflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** Helper method for merge; executes a merge style commit, with
     *  the second parent being the branch BRANCHNAME. */
    public static void mergeCommit(String branchName) throws IOException {
        BranchPointer checkedBranch =
                BranchPointer.readFileAsBranch(branchName);
        Commit checked = Commit.readAsCommit(checkedBranch.getCurrentCommit());
        GitCommands.commit("Merged " + branchName
                + " into " + headPointer + ".");
        BranchPointer curr = BranchPointer.readFileAsBranch(headPointer);
        Commit newCommit = Commit.readAsCommit(curr.getCurrentCommit());
        Utils.join(COMMITS, curr.getCurrentCommit()).delete();
        newCommit.setSecondParent(checked);
        newCommit.makeCommitFile();
        curr.setCurrentCommit(newCommit.hash());
        curr.writeBranchToFile(Utils.join(BRANCHES, headPointer));
    }
}
