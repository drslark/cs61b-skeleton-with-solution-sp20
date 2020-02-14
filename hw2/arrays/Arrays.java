package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Amit Bhat
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int[] newArr = new int[A.length + B.length];

        for (int i = 0; i < A.length; i += 1) {
            newArr[i] = A[i];
        }
        for (int i = 0; i < B.length; i += 1) {
            newArr[i + A.length] = B[i];
        }

        return newArr;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int[] newArr = new int[A.length - len];

        int j = 0;
        for (int i = 0; i < A.length; i += 1) {
            if (!(start <= i && i < start + len)) {
                newArr[j] = A[i];
                j += 1;
            }
        }
        return newArr;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int runs = 0;
        for (int i = 1; i < A.length; i += 1) {
            if (A[i] <= A[i - 1]) {
                runs += 1;
            }
            if (i + 1 == A.length) {
                runs += 1;
            }
        }

        int[][] newArr = new int[runs][1];

        int last = A[0];
        int k, j;
        k = j = 0;
        int lenRun = 1;

        for (int i = 1; i < A.length; last = A[i], i += 1) {
            if (A[i] > last) {
                lenRun += 1;
            } else {
                newArr[j] = Utils.subarray(A, k, lenRun);
                j += 1;
                k += lenRun;
                lenRun = 1;
            }
        }
        newArr[j] = Utils.subarray(A, k, lenRun);

        return newArr;
    }
}
