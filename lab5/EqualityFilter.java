/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _colName = colName;
        _match = match;

    }

    @Override
    protected boolean keep() {
        boolean keep = false;
        int index = this.headerList().indexOf(_colName);
        String item = candidateNext().getValue(index);
        if (item.equals(_match)) {
            keep = true;
        }
        return keep;
    }

    private String _colName;
    private String _match;
}
