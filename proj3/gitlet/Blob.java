package gitlet;
import java.io.File;
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

    public void makeBlobFile() {
        File blobFile = Utils.join(GitCommands.BLOBS, hash());
        Utils.writeObject(blobFile, this);
    }

    public String hash() {
        return Utils.sha1((Object) Utils.serialize(this));
    }
}
