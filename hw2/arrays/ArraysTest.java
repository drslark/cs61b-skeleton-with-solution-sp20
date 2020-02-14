package arrays;

import lists.IntList;
import lists.IntListList;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests for the Arrays class.
 *  @Amit Bhat
 */

public class ArraysTest {

    @Test
    public void testCatenate() {

        int[] a = new int[0];
        int[] b = {1, 2, 3};
        int[] c = {4, 5, 6, 7};
        int[] d = {-3, -2, -1, 0};

        int[] bc = {1, 2, 3, 4, 5, 6, 7};
        int[] db = {-3, -2, -1, 0, 1, 2, 3};
        int[] ca = {4, 5, 6, 7};
        int[] ad = {-3, -2, -1, 0};

        assertArrayEquals(bc, Arrays.catenate(b, c));
        assertArrayEquals(db, Arrays.catenate(d, b));
        assertArrayEquals(ca, Arrays.catenate(c, a));
        assertArrayEquals(ad, Arrays.catenate(a, d));
    }

    @Test
    public void testRemove() {

        int[] a = {-4, 3, 4, 5, 8, 1, 9, 2, 7};
        int[] b = {1, 2, 3};
        int[] c = {4, 5, 6, 7};
        int[] d = {-3, -2, -1, 0};

        int[] a1 = {-4, 3, 4, 5, 7};
        int[] b1 = {1, 2};
        int[] c1 = {4, 7};
        int[] d1 = new int[0];

        assertArrayEquals(a1, Arrays.remove(a, 4, 4));
        assertArrayEquals(b1, Arrays.remove(b, 2, 1));
        assertArrayEquals(c1, Arrays.remove(c, 1, 2));
        assertArrayEquals(d1, Arrays.remove(d, 0, 4));
    }

    @Test
    public void testNaturalRuns() {

        int[] a = new int[] { 1, 3, 4, 4, 5, 2, 9, 12 };
        int[][] a1 = new int[][] { {1, 3, 4}, {4, 5}, {2, 9, 12} };

        int[] b = new int[] { -1, 0, 3, -3, 5, 0, 9, 12 };
        int[][] b1 =  new int[][] { {-1, 0, 3}, {-3, 5}, {0, 9, 12} };

        int[] c = new int[] { -1, -4, 0, 3, 9, -3 };
        int[][] c1 = new int[][] { {-1}, {-4, 0, 3, 9}, {-3} };

        assertArrayEquals(a1, Arrays.naturalRuns(a));
        assertArrayEquals(b1, Arrays.naturalRuns(b));
        assertArrayEquals(c1, Arrays.naturalRuns(c));

    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
