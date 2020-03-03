package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author Amit Bhat
 */
public abstract class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    abstract Permutation getNewPermutation(String cycles, Alphabet alphabet);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet(String chars);

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    abstract Alphabet getNewAlphabet();

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void testAlphabet() {
        Alphabet a = getNewAlphabet("XYZ");
        Permutation perm = getNewPermutation("", getNewAlphabet("XYZ"));
        assertEquals(a, perm.alphabet());
    }

    @Test
    public void testSize() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals(a.size(), 3);
        assertEquals(b.size(), 26);
        assertEquals(c.size(), 6);
    }

    @Test
    public void testPermuteInt() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        assertEquals(a.permute(0), 0);
        assertEquals(a.permute(1), 1);
        assertEquals(a.permute(-3), 0);
        assertEquals(a.permute(14), 2);

        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        assertEquals(b.permute(0), 23);
        assertEquals(b.permute(23), 16);
        assertEquals(b.permute(-10), 22);
        assertEquals(b.permute(67), 15);

        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals(c.permute(0), 3);
        assertEquals(c.permute(5), 5);
        assertEquals(c.permute(2), 1);
        assertEquals(c.permute(4), 0);
        assertEquals(c.permute(-3), 4);
        assertEquals(c.permute(17), 0);

        Permutation d = getNewPermutation("(F)", getNewAlphabet("F"));
        assertEquals(d.permute(0), 0);
    }

    @Test
    public void testPermuteChar() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        assertEquals(a.permute('X'), 'X');
        assertEquals(a.permute('Z'), 'Z');

        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        assertEquals(b.permute('A'), 'X');
        assertEquals(b.permute('W'), 'A');
        assertEquals(b.permute('B'), 'B');

        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals(c.permute('A'), 'D');
        assertEquals(c.permute('B'), 'C');
        assertEquals(c.permute('F'), 'F');
        assertEquals(c.permute('F'), 'A');

        Permutation d = getNewPermutation("(F)", getNewAlphabet("F"));
        assertEquals(d.permute('F'), 'F');
    }

    @Test
    public void testInvertInt() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        assertEquals(a.invert(0), 0);
        assertEquals(a.invert(1), 1);
        assertEquals(a.invert(-3), 0);
        assertEquals(a.invert(14), 2);

        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        assertEquals(b.invert(23), 0);
        assertEquals(b.invert(16), 23);
        assertEquals(b.invert(-26), 22);
        assertEquals(b.invert(67), 15);

        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals(c.invert(3), 0);
        assertEquals(c.invert(5), 5);
        assertEquals(c.invert(2), 1);
        assertEquals(c.invert(3), 0);
        assertEquals(c.invert(-3), 0);
        assertEquals(c.invert(17), 3);

        Permutation d = getNewPermutation("(F)", getNewAlphabet("F"));
        assertEquals(d.invert(0), 0);
    }

    @Test
    public void testInvertChar() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        assertEquals(a.invert('X'), 'X');
        assertEquals(a.invert('Z'), 'Z');

        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        assertEquals(b.invert('X'), 'A');
        assertEquals(b.invert('A'), 'W');
        assertEquals(b.invert('B'), 'B');

        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals(c.invert('D'), 'A');
        assertEquals(c.invert('B'), 'C');
        assertEquals(c.invert('F'), 'F');
        assertEquals(c.invert('E'), 'D');

        Permutation d = getNewPermutation("(F)", getNewAlphabet("F"));
        assertEquals(d.invert('F'), 'F');
    }

    @Test
    public void testDerangement() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        Permutation c = getNewPermutation("(DEA) (BCF)", getNewAlphabet("ABCDEF"));
        Permutation d = getNewPermutation("(F)", getNewAlphabet("F"));
        assertFalse(a.derangement());
        assertFalse(b.derangement());
        assertTrue(c.derangement());
        assertFalse(d.derangement());
    }

    @Test (expected = EnigmaException.class)
    public void testNotClosedLeft() {
        Permutation a = getNewPermutation("DEA)", getNewAlphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testNotClosedRight() {
        Permutation a = getNewPermutation("(DEA", getNewAlphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testNotClosedEither() {
        Permutation a = getNewPermutation("DEA", getNewAlphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testLargerCycle() {
        Permutation a = getNewPermutation("(DEA) (BCFG)", getNewAlphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testNoCycle() {
        Permutation a = getNewPermutation("()", getNewAlphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testRepeatedPerm() {
        Permutation a = getNewPermutation("(AA)", getNewAlphabet("ABCDEF"));
    }

    @Test (expected = EnigmaException.class)
    public void testPermOutOfAlphabet() {
        Permutation a = getNewPermutation("(CBA)", getNewAlphabet("ABCDEF"));
        a.permute('Z');
    }

    @Test (expected = EnigmaException.class)
    public void testInvertOutOfAlphabet() {
        Permutation a = getNewPermutation("(CBA)", getNewAlphabet("ABCDEF"));
        a.invert('Z');
    }

}
