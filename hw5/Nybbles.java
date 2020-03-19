/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author Amit Bhat
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    /** Return an array of size N. 
    * DON'T CHANGE THIS.*/
    public Nybbles(int N) {
        _data = new int[(N + 7) / 8];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            int num =  k / 8;
            int x = _data[num] >> (4 * (k % 8));
            x = x & 15;
            while (x >= 8) {
                x -= 16;
            }
            return x;
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
            if (val < 0) {
                val = (val & 15);
            }
            int index = k / 8;
            int j = _data[index] >>> (4 * (k % 8) + 4) << 4;
            j += val;
            j = j << (4 * (k % 8));
            int i = _data[index] << (32 - 4 * (k % 8)) >>> (32 - 4 * (k % 8));
            _data[index] = i + j;

        }
    }

    /** DON'T CHANGE OR ADD TO THESE. */

    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
