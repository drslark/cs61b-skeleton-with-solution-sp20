package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/** Represents a Branch that can be serialized.
 *  @author Amit Bhat
 */
public class BranchPointer implements Serializable {

    /** Name of BranchPointer. */
    private String _name;

    /** Denotes whether this BranchPointer is
     *  the current branch or not. */
    private boolean _isCurrentBranch;

    /** UID of Commit that BranchPointer is
     *  currently pointing to. */
    private String currentCommit;

    /** Instantiates a BranchPointer object with name NAME and
     * UID COMMIT. CURRENT denotes the state of the BranchPointer
     * in regards to current branch. */
    public BranchPointer(String name, String commit, boolean current) {
        _name = name;
        currentCommit = commit;
        _isCurrentBranch = current;
    }
    /** Instantiates a dummy BranchPointer object. */
    public BranchPointer() {
        _name = "";
        currentCommit = "";
        _isCurrentBranch = false;
    }

    /** Sets this BranchPointer's current commit to COMMIT. */
    public void setCurrentCommit(String commit) {
        currentCommit = commit;
    }

    /** Sets this BranchPointer's isCurrentBranch indicator
     *  to CURR. */
    public void setCurrentBranch(boolean curr) {
        _isCurrentBranch = curr;
    }

    /** Returns true if this BranchPointer is the current branch. */
    public boolean isCurrentBranch() {
        return _isCurrentBranch;
    }

    /** Returns this BranchPointer's name. */
    public String getName() {
        return _name;
    }

    /** Returns this BranchPointer's current commit's UID. */
    public String getCurrentCommit() {
        return currentCommit;
    }

    /** Writes this BranchPointer to a file with path FILE, with
     *  the name of the file being this BranchPointers's name. */
    public void writeBranchToFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        Utils.writeObject(file, this);
    }

    /** Takes in file with path FILE and returns the
     *  BranchPointer serialized into that file. */
    public static BranchPointer readFileAsBranch(File file) {
        return Utils.readObject(file, BranchPointer.class);
    }

    /** Takes in string NAME and returns the
     *  BranchPointer with that name. */
    public static BranchPointer readFileAsBranch(String name) {
        return Utils.readObject(Utils.join(
                GitCommands.BRANCHES, name), BranchPointer.class);
    }

}
