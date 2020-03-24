import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Amit Bhat
 */
class ECHashStringSet implements StringSet {
    private LinkedList<String>[] bins;
    private int binCount;
    private int numItems;

    private static class StringList extends ArrayList<String> {}

    ECHashStringSet() {
        bins = new LinkedList[5];
        for (int i = 0; i < bins.length; i += 1) {
            bins[i] = new LinkedList<>();
        }
        binCount = 5;
        numItems = 0;
    }


    @Override
    public void put(String s) {
        if (numItems / binCount >= 5) {
            resize();
        }
        if (!contains(s)) {
            int binIndex = hash(s);
            bins[binIndex].add(s);
            numItems += 1;
        }
    }

    public int hash(String s) {
        return (s.hashCode() & 0x7fffffff) % binCount;
    }

    private void resize() {
        binCount = binCount * 5;
        List<String> oldBins = asList();

        bins = new LinkedList[binCount];
        for (int i = 0; i < bins.length; i += 1) {
            bins[i] = new LinkedList<>();
        }

        for (String s : oldBins) {
            int binIndex = hash(s);
            bins[binIndex].add(s);
        }
    }

    @Override
    public boolean contains(String s) {
        boolean contained = false;
        if (bins[hash(s)] != null && bins[hash(s)].contains(s)) {
            contained = true;
        }
        return contained;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> allStrings = new ArrayList<String>();
        for (LinkedList x : bins) {
            allStrings.addAll(x);
        }
        return allStrings;
    }
}
