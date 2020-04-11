import java.util.HashMap;

/** HW #7, Two-sum problem.
 * @author Amit Bhat
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        HashMap<Integer, Boolean> dict = new HashMap<Integer, Boolean>();

        for (int x : B) {
            dict.put(x, true);
        }

        for (int x : A) {
            if (dict.containsKey(m - x)) {
                return dict.get(m-x);
            }
        }
        return false;
    }

}
