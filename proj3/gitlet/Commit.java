package gitlet;

import java.io.FileNotFoundException;
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

    public Commit(String message, Date time, String firstParent, String secondParent)
            throws FileNotFoundException {
        _message = message;
        commitTime = time;
        firstParent = firstParent;
        secondParent = secondParent;
        _files = new HashMap<>();


    }

    public Commit(String message, Date time, String firstParent)
            throws FileNotFoundException {
        this(message, time, firstParent, null);
    }

    public Commit copy() throws FileNotFoundException {
        Commit copy = new Commit(_message, commitTime, _firstParent, _secondParent);
        copy._files = new HashMap<>();
        for (String s : _files.keySet()) {
            copy._files.put(s, _files.get(s));
        }
        return copy;
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

    public boolean contains(Blob blob) {
        return _files.containsValue(blob.hash());
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
