package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class BranchPointer implements Serializable {

    private String name;

    private boolean _isCurrentBranch;

    private Commit currentCommit;

    public BranchPointer(String name, Commit commit, boolean current) {
        this.name = name;
        currentCommit = commit;
        _isCurrentBranch = current;
    }

    public void setCurrentCommit(Commit commit) {
        currentCommit = commit;
    }

    public boolean isCurrentBranch() {
        return _isCurrentBranch;
    }

    public String getName() {
        return name;
    }

    public Commit getCurrentCommit() {
        return currentCommit;
    }

    public void writeBranchToFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        Utils.writeObject(file, this);
    }

    public static Stage readFileAsBranch(File file) {
        return Utils.readObject(file, Stage.class);
    }


}
