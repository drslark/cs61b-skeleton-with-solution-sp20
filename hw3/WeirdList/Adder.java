/** An Adder is a function that takes in a value N
 *  and adds to anything it is applied to.
 * @author Amit Bhat
 */
public class Adder implements IntUnaryFunction {

    /** The number being added to every argument to apply. */
    private int _add;

    /** A new Adder whose add value is N. */
    public Adder(int n) {
        _add = n;
    }

    @Override
    public int apply(int x) {
        return x + _add;
    }
}
