import java.util.ArrayList;
import java.util.List;

/** A set of String values.
 *  @author Amit Bhat
 */
class ECHashStringSet implements StringSet {
    private StringList[] bins;
    private int binCount;
    private int numItems;

    private static class StringList extends ArrayList<String> {}

    ECHashStringSet() {
        bins = new StringList[4];
        for (int i = 0; i < bins.length; i += 1) {
            bins[i] = new StringList();
        }
        binCount = 4;
        numItems = 0;
    }


    @Override
    public void put(String s) {
        if (numItems / binCount >= 5) {
            resize();
        }
        if (!contains(s)) {
            int binIndex = (s.hashCode() & 0x7fffffff) % binCount;
            if (bins[binIndex] == null) {
                bins[binIndex] = new StringList();
            }
            bins[binIndex].add(s);
            numItems += 1;
        }
    }

    private void resize() {
        int newBinCount = binCount * 5;
        StringList[] newBins = new StringList[newBinCount];
        for (int i = 0; i < newBins.length; i += 1) {
            newBins[i] = new StringList();
        }

        for (StringList x : bins) {
            for (String s : x) {
                int binIndex = (s.hashCode() & 0x7fffffff) % binCount;
                newBins[binIndex].add(s);
            }
        }
        binCount = newBinCount;
        bins = newBins;
    }


    @Override
    public boolean contains(String s) {
        boolean contain = false;
        for (StringList x : bins) {
            if (x.contains(s)) {
                contain = true;
                break;
            }
        }
        return contain;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> allStrings = new ArrayList<String>();
        for (StringList x : bins) {
            allStrings.addAll(x);
        }
        return allStrings;
    }
}
