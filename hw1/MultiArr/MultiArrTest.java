import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] test1 = {
                {1, 2, 3, 4},
                {1, 5, 12, 7},
                {2, 2, 9, 4},
                {8, 10, 3, 9}
        };
        int[][] test2 = {
                {-2},
                {-5, -1, -9},
                {-3, -4, -6},
                {-12, -10, -6, -23}
        };
        int[][] test3 = {
                {1, 2, 3},
                {5, 6, 6, 17, 8},
                {4, 5},
                {9}
        };
        assertEquals(12, MultiArr.maxValue(test1));
        assertEquals(-1, MultiArr.maxValue(test2));
        assertEquals(17, MultiArr.maxValue(test3));
    }

    @Test
    public void testAllRowSums() {
        int[][] test1 = {
                {1, 2, 3, 4},
                {1, 5, 12, 7},
                {2, 2, 9, 4},
                {8, 10, 3, 9}
        };
        int[][] test2 = {
                {-2},
                {-5, -1, -9},
                {-3, -4, -6},
                {-12, -10, -6, -23}
        };
        int[][] test3 = {
                {1, 2, 3},
                {5, 6, 6, 17, 8},
                {4, 5}
        };
        int[][] test4 = {
                {-2},
                {5, -1, -9},
                {-3, 4, 7},
                {-12, 10, -6, -23}
        };
        int[] result1 = {10, 25, 17, 30};
        int[] result2 = {-2, -15, -13, -51};
        int[] result3 = {6, 42, 9};
        int[] result4 = {-2, -5, 8, -31};
        assertArrayEquals(result1, MultiArr.allRowSums(test1));
        assertArrayEquals(result2, MultiArr.allRowSums(test2));
        assertArrayEquals(result3, MultiArr.allRowSums(test3));
        assertArrayEquals(result4, MultiArr.allRowSums(test4));

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
