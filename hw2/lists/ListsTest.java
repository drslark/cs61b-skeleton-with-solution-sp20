package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit testing for the Lists class, specifically the NaturalRuns method.
 *
 *  @Amit Bhat
 */

public class ListsTest {

    @Test
    public void testNaturalRuns() {

        IntListList aListList =
                new IntListList(new int[][] { {1, 3, 4}, {4, 5}, {6, 9, 12} });
        IntList aList =
                new IntList(new int[] { 1, 3, 4, 4, 5, 6, 9, 12 });

        IntListList bListList =
                new IntListList(new int[][] {
                        {-1, 0, 3},
                        {-3, 5},
                        {6, 9, 12} });
        IntList bList =
                new IntList(new int[] { -1, 0, 3, -3, 5, 6, 9, 12 });

        IntListList cListList =
                new IntListList(new int[][] { {-1}, {-4, 0, 3, 9}, {-3} });
        IntList cList =
                new IntList(new int[] { -1, -4, 0, 3, 9, -3 });

        assertEquals(aListList, Lists.naturalRuns(aList));
        assertEquals(bListList, Lists.naturalRuns(bList));
        assertEquals(cListList, Lists.naturalRuns(cList));

    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
