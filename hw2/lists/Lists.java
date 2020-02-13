package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @Amit Bhat
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
        k = m = L;
        int last = L.head;
        L = L.tail;
        int numRun = 1;

        while (L != null) {
            if (L.head > last) {
                numRun += 1;
            }
            else {
                for (; numRun > 0; numRun -= 1) {
                    k = k.tail;
                }
                k.tail = null;
                pointer.head = m;
                pointer = pointer.tail;
                k = m = L;
            }
            last = L.head;
            L = L.tail;
        }
        return newList;
    }
}
