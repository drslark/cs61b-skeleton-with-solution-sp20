package gitlet;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/** Represents a Blob that can be serialized.
 *  @author Amit Bhat
 */
public class Blob implements Serializable {

    /** Name of Blob. */
    private String _name;

    /** Serialized contents of Blob. */
    private byte[] _contents;

    /** Instantiates a Blob object with name FILENAME
     *  and representing file given by path FILE. */
    public Blob(String fileName, File file) {
        _name = fileName;
        _contents = Utils.readContents(file);
    }

    /** Return this Blob's name. */
    public String getName() {
        return _name;
    }

    /** Return this Blob's contents. */
    public byte[] getContents() {
        return _contents;
    }

    /** Return this Blob's contents as a String. */
    public String getContentsAsString() {
        return new String(_contents, StandardCharsets.UTF_8);
    }

    /** Creates and writes this Blob to a file, with
     *  the name of the file being this Blob's UID. */
    public void makeBlobFile() throws IOException {
        File blobFile = Utils.join(GitCommands.BLOBS, hash());
        blobFile.createNewFile();
        Utils.writeObject(blobFile, this);
    }

    /** Takes in file with path FILE and returns the
     *  Blob serialized into that file. */
    public static Blob readAsBlob(File file) {
        return Utils.readObject(file, Blob.class);
    }

    /** Returns the SHA-1 hash of this Blob. */
    public String hash() {
        return Utils.sha1((Object) Utils.serialize(this));
    }
}
