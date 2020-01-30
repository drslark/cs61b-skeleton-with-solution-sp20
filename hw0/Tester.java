import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Tests for hw0. 
 *  @author Amit Bhat
 */
public class Tester {

    /* Feel free to add your own tests.  For now, you can just follow
     * the pattern you see here. jav We'll look into the details of JUnit
     * testing later.
     *
     * To actually run the tests, just use
     *      java Tester 
     * (after first compiling your files).
     *
     * DON'T put your HW0 solutions here!  Put them in a separate
     * class and figure out how to call them from here.  You'll have
     * to modify the calls to max, threeSum, and threeSumDistinct to
     * get them to work, but it's all good practice! */

    @Test
    public void maxTest() {
        assertEquals(10, Solutions.max(new int[] { 0, -5, 2, 4, 10 }));
        assertEquals(6, Solutions.max(new int[] { 0, 5, -2, 6, 1 }));
        assertEquals(5, Solutions.max(new int[] { 2, 3, -1, 5, -8 }));
        assertEquals(0, Solutions.max(new int[] { 0, -5, -3, -10, -7 }));
        assertEquals(-1, Solutions.max(new int[] { -2, -5, -10, -1, -3 }));
    }

    @Test
    public void threeSumTest() {
        assertTrue(Solutions.threeSum(new int[] { -6, 3, 10, 200 }));
        assertTrue(Solutions.threeSum(new int[] { 5, 4, -1, -3, -8 }));
        assertTrue(Solutions.threeSum(new int[] { 1, 0, -10, 3, 6 }));
        assertFalse(Solutions.threeSum(new int[] { 1, 2, 3, 4 }));
        assertFalse(Solutions.threeSum(new int[] { -5, 5, 4, 3, -10 }));
        assertFalse(Solutions.threeSum(new int[] { -8, 1, 2, 9 }));
        
    }

    @Test
    public void threeSumDistinctTest() {
        assertTrue(Solutions.threeSum_Distinct(new int[] { -100, 3, 50, 50 }));
        assertTrue(Solutions.threeSum_Distinct(new int[] { -10, 3, 10, 0 }));
        assertTrue(Solutions.threeSum_Distinct(new int[] { -3, 2, 1, 0 }));
        assertFalse(Solutions.threeSum_Distinct(new int[] { 1, 2, 3, 4 }));
        assertFalse(Solutions.threeSum_Distinct(new int[] { -5, 5, 4, 3, -10 }));
        assertFalse(Solutions.threeSum_Distinct(new int[] { -8, 1, 2, 9 }));
    }

    public static void main(String[] unused) {
        textui.runClasses(Tester.class);
    }

}
