package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author Amit Bhat
 */
class Lists {

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        IntListList newList = new IntListList(null, null);
        IntListList pointer = newList;

        IntList m, k;
        k = L;
        m = L;

        while (k.tail != null) {
            if (k.head >= k.tail.head) {
                m = k.tail;
                k.tail = null;
                pointer.tail = new IntListList(L, null);
                pointer = pointer.tail;
                L = k = m;
            } else {
                k = k.tail;
            }
        }
        pointer.tail = new IntListList(L, null);
        pointer = pointer.tail;

        return newList.tail;
    }
}
