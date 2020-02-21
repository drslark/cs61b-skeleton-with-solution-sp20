/** An Adder is a function that stores the sum of
 *  all values passed into its apply function.
 * @author Amit Bhat
 */
public class Sum implements IntUnaryFunction {
    private int _sum;

    public Sum() {
        _sum = 0;
    }

    public int getSum() {
        return _sum;
    }

    @Override
    public int apply(int x) {
        _sum += x;
        return x;
    }
}
