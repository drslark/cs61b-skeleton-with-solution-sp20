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
    static File removals = Utils.join(STAGING_AREA, "removals.txt");
    static File additions = Utils.join(STAGING_AREA, "additions.txt");

    static Pointer head;
    static HashMap<String, Pointer> branches;

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
        STAGING_AREA.mkdir();
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

        branches = new HashMap<String, Pointer>();
        head = new Pointer("head", null);
        branches.put("master", new Pointer("master", initialCommit));
        Commit currentMaster = branches.get("master").getCurrentCommit();
        head.setCurrentCommit(currentMaster);
    }

    public static void add(String fileName) {
        File currFile = Utils.join(CWD, fileName);
        if (!currFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        Commit current = head.getCurrentCommit();
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
            additionStage.addBlob(fileBlob);
            fileBlob.makeBlobFile();
        }
        additionStage.writeStageToFile(additions);
        removalStage.writeStageToFile(removals);
    }

    public static void commit() {
    }

}
