package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/** Represents a Staging Area that can be serialized.
 *  @author Amit Bhat
 */
public class Stage implements Serializable {

    /** Name of Stage. */
    private String _name;

    /** Files currently in Stage. */
    private HashMap<String, String> _files;

    /** Instantiates a Stage object with name NAME.*/
    public Stage(String name) {
        _name = name;
        _files = new HashMap<>();
    }

    /** Adds a Blob BLOB to this Stage's files. */
    public void add(Blob blob) {
        _files.put(blob.getName(), blob.hash());
    }

    /** Returns the name of the file representing the Blob
     *  with name S. */
    public String get(String s) {
        return _files.get(s);
    }

    /** Returns the names of  all the Blobs in Stage. */
    public Set<String> getKeys() {
        return _files.keySet();
    }
    /** Returns the file names of the files that represent
     *  all the Blobs in this Stage. */
    public Collection<String> getValues() {
        return _files.values();
    }

    /** Returns true if this Stage contains the Blob with name FILENAME. */
    public boolean contains(String fileName) {
        return _files.containsKey(fileName);
    }

    /** Returns true if this Stage contains Blob BLOB. */
    public boolean contains(Blob blob) {
        return _files.containsValue(blob.hash());
    }

    /** Returns true if this Stage contains no files. */
    public boolean isEmpty() {
        return _files.isEmpty();
    }

    /** Removes Blob BLOB from this Stage. */
    public void remove(Blob blob) {
        _files.remove(blob.getName());
    }

    /** Removes Blob with name FILENAME from this Stage. */
    public void remove(String fileName) {
        _files.remove(fileName);
    }

    /** Removes all Blobs from this Stage. */
    public void removeAll() {
        _files.clear();
    }

    /** Writes thisStage to a file with path FILE. */
    public void writeStageToFile(File file) {
        Utils.writeObject(file, this);
    }

    /** Takes in file with path FILE and returns the
     *  Stage serialized into that file. */
    public static Stage readFileAsStage(File file) {
        return Utils.readObject(file, Stage.class);
    }

}
