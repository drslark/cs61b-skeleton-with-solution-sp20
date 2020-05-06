package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Represents a Commit that can be serialized.
 *  @author Amit Bhat
 */
public class Commit implements Serializable {

    /** Large value to indicate that path between two commits doesn't
     *  exist. */
    static final int DIST_OF_BAD_PATH = Integer.MAX_VALUE - 100;

    /** Date of Commit. */
    private Date commitTime;

    /** Message of Commit. */
    private String _message;

    /** Files tracked by Commit. */
    private HashMap<String, String> _files;

    /** Parent of Commit. */
    private String _firstParent;

    /** Second parent of Commit.
     *  Only in Merge cases. */
    private String _secondParent;

    /** Instantiates a Commit object with message MESSAGE, time TIME,
     * parent FIRSTPARENT, and second parent SECONDPARENT. */
    public Commit(String message, Date time,
                  String firstParent, String secondParent) {
        _message = message;
        commitTime = time;
        _firstParent = firstParent;
        _secondParent = secondParent;
        _files = new HashMap<>();
    }

    /** Instantiates a Commit object with message MESSAGE, time TIME,
     * parent FIRSTPARENT. */
    public Commit(String message, Date time, String firstParent) {
        this(message, time, firstParent, null);
    }

    /** Returns a Commit object that is a copy of this Commit but
     *  with message MESSAGE. */
    public Commit copy(String message) {
        Commit copy = new Commit(message, new Date(),
                _firstParent);
        copy._files = new HashMap<>();
        for (String s : _files.keySet()) {
            copy._files.put(s, _files.get(s));
        }
        return copy;
    }

    /** Sets first parent to UID of COMMIT. */
    public void setFirstParent(Commit commit) {
        _firstParent = commit.hash();
    }

    /** Sets second parent to UID of COMMIT. */
    public void setSecondParent(Commit commit) {
        _secondParent = commit.hash();
    }

    /** Adds all staged files into files. */
    public void addStagedFiles() {
        Stage stagedAdditions = Stage.readFileAsStage(GitCommands.ADDITIONS);
        for (String s : stagedAdditions.getKeys()) {
            _files.put(s, stagedAdditions.get(s));
        }
    }

    /** Removes all staged removal files from files. */
    public void removeStagedFiles() {
        Stage stagedRemovals = Stage.readFileAsStage(GitCommands.REMOVALS);
        for (String s : stagedRemovals.getKeys()) {
            _files.remove(s);
        }
    }

    /** Returns a set containing all the names of files in
     *  this Commit. */
    public Set<String> getNames() {
        return _files.keySet();
    }

    /** Returns the UID of Blob with name NAME. */
    public String getUID(String name) {
        return _files.get(name);
    }

    /** Returns the Blob with name FILENAME. */
    public Blob getBlob(String fileName) {
        String blobHash = _files.get(fileName);
        File blobFile = Utils.join(GitCommands.BLOBS, blobHash);
        return Blob.readAsBlob(blobFile);
    }

    /** Returns this Commit's message. */
    public String getMessage() {
        return _message;
    }

    /** Returns the files of this Commit. */
    public Map<String, String> getFiles() {
        return _files;
    }

    /** Returns the Date object of this Commit. */
    public Date getDate() {
        return commitTime;
    }

    /** Returns the UID of this Commit's first parent. */
    public String getFirstParent() {
        return _firstParent;
    }

    /** Returns the UID of this Commit's second parent. */
    public String getSecondParent() {
        return _secondParent;
    }

    /** Returns distance between COMMIT1 and one of its ancestors on
     *  the same branch, COMMIT2. */
    public static int distance(String commit1, String commit2) {
        if (commit1 == null || commit2 == null) {
            return DIST_OF_BAD_PATH;
        }
        if (commit1.equals(commit2)) {
            return 0;
        } else {
            Commit currCommit = Commit.readAsCommit(Utils.join(
                    GitCommands.COMMITS, commit1));
            commit1 = currCommit.getFirstParent();
            int firstParent = 1 + distance(commit1, commit2);
            int secondParent = DIST_OF_BAD_PATH;
            if (currCommit._secondParent != null) {
                secondParent = 1 + distance(currCommit._secondParent, commit2);
            }
            return Integer.min(firstParent, secondParent);
        }
    }

    /** Creates and writes this Commit to a file, with
     *  the name of the file being this Commit's UID. */
    public void makeCommitFile() throws IOException {
        File commitFile = Utils.join(GitCommands.COMMITS, hash());
        commitFile.createNewFile();
        Utils.writeObject(commitFile, this);
    }

    /** Returns a String of the contents of the serialized Blob
     *  with name NAME. */
    public String readBlobAsString(String name) {
        String blobID = _files.get(name);
        File blobFile = Utils.join(GitCommands.BLOBS, blobID);
        if (!blobFile.exists()) {
            return "";
        }
        return Utils.readContentsAsString(blobFile);
    }

    /** Takes in file with path FILE and returns the
     *  Commit serialized into that file. */
    public static Commit readAsCommit(File file) {
        return Utils.readObject(file, Commit.class);
    }

    /** Takes in string ID and returns the
     *  Commit with that UID. */
    public static Commit readAsCommit(String id) {
        return Utils.readObject(
                Utils.join(GitCommands.COMMITS, id), Commit.class);
    }

    /** Returns true if this Commit contains Blob BLOB. */
    public boolean contains(Blob blob) {
        return _files.containsValue(blob.hash());
    }

    /** Returns true if this Commit contains a Blob with name
     *  FILENAME. */
    public boolean contains(String fileName) {
        return _files.containsKey(fileName);
    }

    /** Returns the SHA-1 UID of this Commit. */
    public String hash() {
        return Utils.sha1((Object) Utils.serialize(this));
    }

    @Override
    public String toString() {
        String out = hash()  + " "
                + commitTime + " " + _message
                + " " + _firstParent;
        if (_secondParent != null) {
            out = out + " " + _secondParent;
        }
        return out;
    }

    /** Displays this Commit in the format used by gitCommands.log. */
    public void displayCommit() {
        String separator = "===", merge = "";
        String header, date, message;

        header = "commit " + hash();

        if (getSecondParent() != null) {
            merge = "Merge: " + getFirstParent().substring(0, 7)
                    + " " + getSecondParent().substring(0, 7);
        }

        Date d = getDate();
        date = "Date: " + String.format("%ta %tb %td %tT %tY %tz",
                d, d, d, d, d, d);
        message = getMessage();

        System.out.println(separator);
        System.out.println(header);
        if (!merge.equals("")) {
            System.out.println(merge);
        }
        System.out.println(date);
        System.out.println(message + "\n");
    }

}
