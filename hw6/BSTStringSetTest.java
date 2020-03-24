import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author Amit Bhat
 */
public class BSTStringSetTest  {
    // FIXME: Add your own tests for your BST StringSet

    @Test
    public void testContains() {
        BSTStringSet a = new BSTStringSet();
        a.put("Michael");
        a.put("Dwight");
        a.put("Stanley");
        a.put("Angela");
        a.put("Jim");
        a.put("Pam");
        a.put("Angela");

        assertTrue(a.contains("Michael"));
        assertTrue(a.contains("Dwight"));
        assertTrue(a.contains("Stanley"));
        assertFalse(a.contains("Oscar"));
    }

    @Test
    public void testAsList() {
        BSTStringSet a = new BSTStringSet();
        a.put("Michael");
        a.put("Dwight");
        a.put("Stanley");
        a.put("Angela");
        a.put("Jim");
        a.put("Pam");
        List b = a.asList();

        assertEquals("Angela", b.get(0));
        assertEquals("Stanley", b.get(5));
        assertEquals("Jim", b.get(2));
        assertEquals("Michael", b.get(3));
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(BSTStringSetTest.class));
    }

}
