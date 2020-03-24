import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a EC Hash String Set.
 * @author Amit Bhat
 */
public class ECHashStringSetTest  {
    // FIXME: Add your own tests for your ECHashStringSetTest

    @Test
    public void testContains() {
        ECHashStringSet a = new ECHashStringSet();
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

        assertTrue(b.contains("Michael"));
        assertTrue(b.contains("Dwight"));
        assertTrue(b.contains("Stanley"));
        assertFalse(b.contains("Oscar"));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ECHashStringSetTest.class));
    }
}
