/** An Adder is a function that stores the sum of
 *  all values passed into its apply function.
 * @author Amit Bhat
 */
public class Sum implements IntUnaryFunction {

    /** The sum of all arguments passed into apply. */
    private int _sum;

    /** A new Sum whose sum is 0. */
    public Sum() {
        _sum = 0;
    }

    /** Returns the value of _sum. */
    public int getSum() {
        return _sum;
    }

    @Override
    public int apply(int x) {
        _sum += x;
        return x;
    }
}
