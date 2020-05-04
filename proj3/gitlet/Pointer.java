package gitlet;

public class Pointer {

    private String name;

    private Commit currentCommit;

    public Pointer(String name, Commit commit) {
        this.name = name;
        currentCommit = commit;
    }

    public void setCurrentCommit(Commit commit) {
        currentCommit = commit;
    }

    public String getName() {
        return name;
    }

    public Commit getCurrentCommit() {
        return currentCommit;
    }
}
