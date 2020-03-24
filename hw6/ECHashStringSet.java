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
        int newBinCount = binCount * 2;
        StringList[] newBins = new StringList[newBinCount];

        for (StringList x : bins) {
            for (String s : x) {
                int binIndex = (s.hashCode() & 0x7fffffff) % binCount;
                if (newBins[binIndex] == null) {
                    newBins[binIndex] = new StringList();
                }
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
            if (x != null && x.contains(s)) {
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
            if (!(x == null)) {
                allStrings.addAll(x);
            }

        }
        return allStrings;
    }
}
