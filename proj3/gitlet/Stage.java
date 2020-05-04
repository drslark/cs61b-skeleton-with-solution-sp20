package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

public class Stage implements Serializable {

    private String _name;

    private HashMap<String, String> _files;

    public Stage (String name) {
        _name = name;
    }

    public void addBlob(Blob file) {
        _files.put(file.getName(), file.hash());
    }

    public boolean contains(Blob blob) {
        return _files.containsValue(blob.hash());
    }

    public void remove(Blob blob) {
        _files.remove(blob.getName());
    }

    public void removeAll() {
        _files.clear();
    }

    public byte[] serialize() {
        return Utils.serialize(this);
    }

    public void writeStageToFile(File file) {
        Utils.writeObject(file, this);
    }

    public static Stage readFileAsStage(File file) {
        return Utils.readObject(file, Stage.class);
    }

    public String hash() {
        return Utils.sha1((Object) Utils.serialize(this));
    }

}
