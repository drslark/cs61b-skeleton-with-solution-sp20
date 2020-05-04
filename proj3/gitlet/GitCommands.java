package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    //static String head_branch;
    static String head_commit;

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

        BranchPointer master = new BranchPointer("master", initialCommit, true);
        File masterFile = Utils.join(BRANCHES, "master");
        master.writeBranchToFile(masterFile);

        head_commit = initialCommit.hash();
        Utils.writeContents(HEAD, head_commit);

    }

    public static void add(String fileName) throws IOException {
        File currFile = Utils.join(CWD, fileName);
        if (!currFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        head_commit = Utils.readContentsAsString(HEAD);
        Commit current = Commit.readAsCommit(Utils.join(COMMITS, head_commit));
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

    /*
    public static void commit() {
        Commit newCommit = head.getCurrentCommit().copy();
        newCommit.addStagedFiles();
        head.setCurrentCommit(newCommit);

        Stage staged_additions = Stage.readFileAsStage(additions);
        staged_additions.removeAll();
        staged_additions.writeStageToFile(additions);
    }
    */

    public static void rm(String fileName) {
        Stage additionStage = Stage.readFileAsStage(additions);
        Stage removalStage = Stage.readFileAsStage(removals);

        head_commit = Utils.readContentsAsString(HEAD);
        Commit latestCommit = Commit.readAsCommit(Utils.join(COMMITS, head_commit));

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

}
