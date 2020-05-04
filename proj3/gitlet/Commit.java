package gitlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Commit implements Serializable {

    private Date commitTime;

    private String _message;

    private Map<String, String> _files;

    private String _firstParent;

    private String _secondParent;

    public Commit(String message, Date time, String firstParent, String secondParent) {
        _message = message;
        commitTime = time;
        _firstParent = firstParent;
        _secondParent = secondParent;
        _files = new HashMap<>();
    }

    public Commit(String message, Date time, String firstParent) {
        this(message, time, firstParent, null);
    }

    public Commit copy(String message) {
        Commit copy = new Commit(message, new Date(), _firstParent, _secondParent);
        copy._files = new HashMap<>();
        for (String s : _files.keySet()) {
            copy._files.put(s, _files.get(s));
        }
        return copy;
    }

    public void setFirstParent(Commit commit) {
        _firstParent = commit._firstParent;
    }

    public void addStagedFiles() {
        Stage staged_additions = Stage.readFileAsStage(GitCommands.additions);
        for (String s : staged_additions.getKeys()) {
            _files.put(s, staged_additions.get(s));
        }
    }

    public void removeStagedFiles() {
        Stage staged_removals = Stage.readFileAsStage(GitCommands.removals);
        for (String s : staged_removals.getKeys()) {
            _files.remove(s);
        }
    }

    public Blob getBlob(String fileName) {
        String blobHash = _files.get(fileName);
        File blobFile = Utils.join(GitCommands.BLOBS, blobHash);
        return Blob.readAsBlob(blobFile);
    }

    public String getMessage() {
        return _message;
    }

    public Map<String, String> getFiles() {
        return _files;
    }


    public String getFirstParent() {
        return _firstParent;
    }

    public String getSecondParent() {
        return _secondParent;
    }

    public void makeCommitFile() throws IOException {
        File commitFile = Utils.join(GitCommands.COMMITS, hash());
        commitFile.createNewFile();
        Utils.writeObject(commitFile, this);
    }

    public static Commit readAsCommit(File file) {
        return Utils.readObject(file, Commit.class);
    }

    public boolean contains(Blob blob) {
        return _files.containsValue(blob.hash());
    }

    public boolean contains(String fileName) {
        return _files.containsKey(fileName);
    }

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

}
