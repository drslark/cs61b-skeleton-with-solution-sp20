/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _colName1 = colName1;
        _colName2 = colName2;
    }

    @Override
    protected boolean keep() {
        boolean keep = false;
        int index1 = this.headerList().indexOf(_colName1);
        int index2 = this.headerList().indexOf(_colName2);
        String item1 = candidateNext().getValue(index1);
        String item2 = candidateNext().getValue(index2);
        if (item1.equals(item2)) {
            keep = true;
        }
        return keep;
    }

    private String _colName1;
    private String _colName2;
}
