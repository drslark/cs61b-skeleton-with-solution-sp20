/** An Adder is a function that takes in a value N
 *  and adds to anything it is applied to.
 * @author Amit Bhat
 */
public class Adder implements IntUnaryFunction {

    private int add;
    public Adder(int n) {
        add = n;
    }

    @Override
    public int apply(int x) {
        return x + add;
    }
}
