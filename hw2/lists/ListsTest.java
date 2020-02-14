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
                IntListList.list(new int[][] { {1, 3, 4}, {4, 5}, {2, 9, 12} });
        IntList aList =
                IntList.list(new int[] { 1, 3, 4, 4, 5, 2, 9, 12 });

        IntListList bListList =
                IntListList.list(new int[][] {
                        {-1, 0, 3},
                        {-3, 5},
                        {-8, 9, 12} });
        IntList bList =
                IntList.list(new int[] { -1, 0, 3, -3, 5, -8, 9, 12 });

        IntListList cListList =
                IntListList.list(new int[][] { {-1}, {-4, 0, 3, 9}, {-3} });
        IntList cList =
                IntList.list(new int[] { -1, -4, 0, 3, 9, -3 });

        assertEquals(aListList, Lists.naturalRuns(aList));
        assertEquals(bListList, Lists.naturalRuns(bList));
        assertEquals(cListList, Lists.naturalRuns(cList));

    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
