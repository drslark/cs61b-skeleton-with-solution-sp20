package gitlet;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Blob implements Serializable {

    private String _name;

    private byte[] _contents;

    public Blob(String fileName, File file) {
        _name = fileName;
        _contents = Utils.serialize(file);
    }

    public String getName() {
        return _name;
    }

    public byte[] getContents() {
        return _contents;
    }

    public void makeBlobFile() throws IOException {
        File blobFile = Utils.join(GitCommands.BLOBS, hash());
        blobFile.createNewFile();
        Utils.writeObject(blobFile, this);
    }

    public static Blob readAsBlob(File fileName) {
        return Utils.readObject(fileName, Blob.class);
    }

    public String hash() {
        return Utils.sha1((Object) Utils.serialize(this));
    }
}
