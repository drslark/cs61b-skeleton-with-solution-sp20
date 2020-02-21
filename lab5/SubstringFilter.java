/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _colName = colName;
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
        boolean keep = false;
        int index = this.headerList().indexOf(_colName);
        String item = candidateNext().getValue(index);
        if (item.contains(_subStr)) {
            keep = true;
        }
        return keep;
    }

    private String _colName;
    private String _subStr;
}
