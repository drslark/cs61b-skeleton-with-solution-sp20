package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GitCommands {

    static final File CWD = new File(System.getProperty("user.dir"));
    static final File GITLET = new File(CWD, ".gitlet");
    static final File COMMITS = new File(GITLET, "commits");
    static final File COMMIT_LOG = new File(COMMITS, "commit_log.txt");
    static final File STAGING_AREA = new File(GITLET, "staging_area");
    static final File REMOVALS = new File(GITLET, "removals");
    static File staged_files = new File(STAGING_AREA, "staged_files.txt");

    static HashMap<String, File> commits_log;
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
        COMMIT_LOG.createNewFile();
        STAGING_AREA.mkdir();
        REMOVALS.mkdir();
        staged_files.createNewFile();

        Commit initialCommit = new Commit("initial commit", new Date(0), null);
        Utils.writeContents(COMMIT_LOG, initialCommit.hash());
        File initCommit = new File(COMMITS, initialCommit.hash());
        initCommit.createNewFile();
        Utils.writeObject(initCommit, initialCommit);

        branches = new HashMap<String, Pointer>();
        head = new Pointer("head", null);
        branches.put("master", new Pointer("master", initialCommit));
        Commit currentMaster = branches.get("master").getCurrentCommit();
        head.setCurrentCommit(currentMaster);

    }

}
