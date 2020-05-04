package gitlet;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Commit implements Serializable {

    private Date commitTime;

    private String logMessage;

    private Map<String, String> files;

    private Commit firstParent;

    private Commit secondParent;

    public Commit(String message, Date time, Commit first, Commit second)
            throws FileNotFoundException {
        logMessage = message;
        commitTime = time;
        firstParent = first;
        secondParent = second;
        Scanner input = new Scanner(GitCommands.staged_files);
        files = new HashMap<String, String>();
        while (input.hasNext()) {
            String fileName = input.next();
            String reference = input.nextLine();
            files.put(fileName, reference);
        }

    }

    public Commit(String message, Date time, Commit firstParent)
            throws FileNotFoundException {
        this(message, time, firstParent, null);
    }

    public String getMessage() {
        return logMessage;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public String getFirstParent() {
        return Utils.sha1(firstParent);
    }

    public Commit getFirstParentString() {
        return firstParent;
    }

    public String getSecondParent() {
        return Utils.sha1(secondParent);
    }

    public Commit getSecondParentString() {
        return secondParent;
    }

    public String hash() {
        return Utils.sha1((Object) Utils.serialize(this));
    }

    @Override
    public String toString() {
        String out = hash()  + " "
                + commitTime + " " + logMessage
                + " " + getFirstParentString();
        if (secondParent != null) {
            out = out + " " + getSecondParentString();
        }
        return out;
    }

}
