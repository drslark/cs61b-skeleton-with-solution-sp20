package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.HashMap;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Amit Bhat
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testAlphabet() {
        Alphabet a = new Alphabet("XYZ");
        Permutation perm = new Permutation("", a);
        assertEquals(a, perm.alphabet());
    }

    @Test
    public void TestCycles() {
        Permutation c = new Permutation("(DEA) (BC) (F)", new Alphabet("ABCDEF"));
        HashMap<Character, Character> cycles = c.get_cycles();
        assertEquals((Character) 'E', cycles.get('D'));
        assertEquals((Character) 'A', cycles.get('E'));
        assertEquals((Character) 'D', cycles.get('A'));
        assertEquals((Character) 'B', cycles.get('C'));
        assertEquals((Character) 'C', cycles.get('B'));
        assertEquals((Character) 'F', cycles.get('F'));
    }

    @Test
    public void testSize() {
        Permutation a = new Permutation("", new Alphabet("XYZ"));
        Permutation b = new Permutation("(AXQW)", new Alphabet());
        Permutation c = new Permutation("(DEA) (BC) (F)", new Alphabet("ABCDEF"));
        assertEquals(3, a.size());
        assertEquals(26, b.size());
        assertEquals(6, c.size());
    }

    @Test
    public void testPermuteInt() {
        Permutation a = new Permutation("", new Alphabet("XYZ"));
        assertEquals(0, a.permute(0));
        assertEquals(1, a.permute(1));
        assertEquals(0, a.permute(-3));
        assertEquals(2, a.permute(11));

        Permutation b = new Permutation("(AXQW)", new Alphabet());
        assertEquals(23, b.permute(0));
        assertEquals(16, b.permute(23));
        assertEquals(22, b.permute(-10));
        assertEquals(15, b.permute(67));

        Permutation c = new Permutation("(DEA) (BC) (F)", new Alphabet("ABCDEF"));
        assertEquals(3, c.permute(0));
        assertEquals(5, c.permute(5));
        assertEquals(1, c.permute(2));
        assertEquals(0, c.permute(4));
        assertEquals(4, c.permute(-3));
        assertEquals(5, c.permute(17));

        Permutation d = new Permutation("(F)", new Alphabet("F"));
        assertEquals(0, d.permute(0));
    }

    @Test
    public void testPermuteChar() {
        Permutation a = new Permutation("", new Alphabet("XYZ"));
        assertEquals('X', a.permute('X'));
        assertEquals('Z', a.permute('Z'));

        Permutation b = new Permutation("(AXQW)", new Alphabet());
        assertEquals('X', b.permute('A'));
        assertEquals('A', b.permute('W'));
        assertEquals('B', b.permute('B'));

        Permutation c = new Permutation("(DEA) (BC) (F)", new Alphabet("ABCDEF"));
        assertEquals('D', c.permute('A'));
        assertEquals('C', c.permute('B'));
        assertEquals('F', c.permute('F'));
        assertEquals('A', c.permute('E'));

        Permutation d = new Permutation("(F)", new Alphabet("F"));
        assertEquals('F', d.permute('F'));
    }

    @Test
    public void testInvertInt() {
        Permutation a = new Permutation("", new Alphabet("XYZ"));
        assertEquals(0, a.invert(0));
        assertEquals(1, a.invert(1));
        assertEquals(0, a.invert(-3));
        assertEquals(2, a.invert(14));


        Permutation b = new Permutation("(AXQW)", new Alphabet());
        assertEquals(0, b.invert(23));
        assertEquals(23, b.invert(16));
        assertEquals(22, b.invert(-26));
        assertEquals(15, b.invert(67));

        Permutation c = new Permutation("(DEA) (BC) (F)", new Alphabet("ABCDEF"));
        assertEquals(0, c.invert(3));
        assertEquals(5, c.invert(5));
        assertEquals(1, c.invert(2));
        assertEquals(0, c.invert(-3));
        assertEquals(5, c.invert(17));

        Permutation d = new Permutation("(F)", new Alphabet("F"));
        assertEquals(0, d.invert(0));
    }

    @Test
    public void testInvertChar() {
        Permutation a = new Permutation("", new Alphabet("XYZ"));
        assertEquals('X', a.invert('X'));
        assertEquals('Z', a.invert('Z'));

        Permutation b = new Permutation("(AXQW)", new Alphabet());
        assertEquals('A', b.invert('X'));
        assertEquals('B', b.invert('B'));
        assertEquals('W', b.invert('A'));

        Permutation c = new Permutation("(DEA) (BC) (F)", new Alphabet("ABCDEF"));
        assertEquals('A', c.invert('D'));
        assertEquals('C', c.invert('B'));
        assertEquals('F', c.invert('F'));
        assertEquals('D', c.invert('E'));

        Permutation d = new Permutation("(F)", new Alphabet("F"));
        assertEquals('F', d.invert('F'));
    }

    @Test
    public void testDerangement() {
        Permutation a = new Permutation("", new Alphabet("XYZ"));
        Permutation b = new Permutation("(AXQW)", new Alphabet());
        Permutation c = new Permutation("(DEA) (BCF)", new Alphabet("ABCDEF"));
        Permutation d = new Permutation("(F)", new Alphabet("F"));
        assertFalse(a.derangement());
        assertFalse(b.derangement());
        assertTrue(c.derangement());
        assertFalse(d.derangement());
    }

    @Test (expected = EnigmaException.class)
    public void testNotClosedLeft() {
        Permutation a = new Permutation("DEA)", new Alphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testNotClosedRight() {
        Permutation a = new Permutation("(DEA", new Alphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testNotClosedEither() {
        Permutation a = new Permutation("DEA", new Alphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testLargerCycle() {
        Permutation a = new Permutation("(DEA) (BCFG)", new Alphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testNoCycle() {
        Permutation a = new Permutation("()", new Alphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testRepeatedPerm() {
        Permutation a = new Permutation("(AA)", new Alphabet("ABCDEF"));
    }


    @Test (expected = EnigmaException.class)
    public void testPermOutOfAlphabet() {
        Permutation a = new Permutation("(CBA)", new Alphabet("ABCDEF"));
        a.permute('Z');
    }

    @Test (expected = EnigmaException.class)
    public void testInvertOutOfAlphabet() {
        Permutation a = new Permutation("(CBA)", new Alphabet("ABCDEF"));
        a.invert('Z');
    }

}
