package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Stage implements Serializable {

    private String _name;

    private HashMap<String, String> _files;

    public Stage (String name) {
        _name = name;
        _files = new HashMap<>();
    }


    public void add(Blob file) {
        _files.put(file.getName(), file.hash());
    }

    public String get(String s) {
        return _files.get(s);
    }

    public Set<String> getKeys() {
        return _files.keySet();
    }

    public Collection<String> getValues() {
        return _files.values();
    }

    public boolean contains(String fileName) {
        return _files.containsKey(fileName);
    }

    public boolean contains(Blob blob) {
        return _files.containsValue(blob.hash());
    }

    public void remove(Blob blob) {
        _files.remove(blob.getName());
    }

    public void remove(String fileName) {
        _files.remove(fileName);
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
