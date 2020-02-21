/** A EmptyList is an empty sequence of integers.
 * @author Amit Bhat
 */
public class EmptyList extends WeirdList {

    /** Creates a new EmptyList with head HEAD and tail TAIL. */
    public EmptyList(int head, WeirdList tail) {
        super(head, tail);
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public WeirdList map(IntUnaryFunction func) {
        return this;
    }
}
