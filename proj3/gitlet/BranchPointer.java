package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class BranchPointer implements Serializable {

    private String name;

    private boolean _isCurrentBranch;

    private String currentCommit;

    public BranchPointer(String name, String commit, boolean current) {
        this.name = name;
        currentCommit = commit;
        _isCurrentBranch = current;
    }

    public BranchPointer() {
        name = "";
        currentCommit = "";
        _isCurrentBranch = false;
    }

    public void setCurrentCommit(String commit) {
        currentCommit = commit;
    }

    public boolean isCurrentBranch() {
        return _isCurrentBranch;
    }

    public String getName() {
        return name;
    }

    public String getCurrentCommit() {
        return currentCommit;
    }

    public void writeBranchToFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        Utils.writeObject(file, this);
    }

    public static BranchPointer readFileAsBranch(File file) {
        return Utils.readObject(file, BranchPointer.class);
    }


}
