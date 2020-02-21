/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _colName = colName;
        _ref = ref;
    }

    @Override
    protected boolean keep() {
        boolean keep = false;
        int index = this.headerList().indexOf(_colName);
        String item = candidateNext().getValue(index);
        if (item.compareTo(_ref) > 0) {
            keep = true;
        }
        return keep;
    }

    private String _colName;
    private String _ref;
}
