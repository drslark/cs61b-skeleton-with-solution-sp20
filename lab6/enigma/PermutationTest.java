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
        Permutation perm = getNewPermutation("", a);
        assertEquals(a, perm.alphabet());
    }

    @Test
    public void testSize() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals(3, a.size());
        assertEquals(26, b.size());
        assertEquals(6, c.size());
    }

    @Test
    public void testPermuteInt() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        assertEquals(0, a.permute(0));
        assertEquals(1, a.permute(1));
        assertEquals(0, a.permute(-3));
        assertEquals(2, a.permute(11));

        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        assertEquals(23, b.permute(0));
        assertEquals(16, b.permute(23));
        assertEquals(22, b.permute(-10));
        assertEquals(15, b.permute(67));

        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals(3, c.permute(0));
        assertEquals(5, c.permute(5));
        assertEquals(1, c.permute(2));
        assertEquals(0, c.permute(4));
        assertEquals(4, c.permute(-3));
        assertEquals(5, c.permute(17));

        Permutation d = getNewPermutation("(F)", getNewAlphabet("F"));
        assertEquals(0, d.permute(0));
    }

    @Test
    public void testPermuteChar() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        assertEquals('X', a.permute('X'));
        assertEquals('Z', a.permute('Z'));

        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        assertEquals('X', b.permute('A'));
        assertEquals('A', b.permute('W'));
        assertEquals('B', b.permute('B'));

        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals('D', c.permute('A'));
        assertEquals('C', c.permute('B'));
        assertEquals('F', c.permute('F'));
        assertEquals('A', c.permute('E'));

        Permutation d = getNewPermutation("(F)", getNewAlphabet("F"));
        assertEquals('F', d.permute('F'));
    }

    @Test
    public void testInvertInt() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        assertEquals(0, a.invert(0));
        assertEquals(1, a.invert(1));
        assertEquals(0, a.invert(-3));
        assertEquals(2, a.invert(14));


        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        assertEquals(0, b.invert(23));
        assertEquals(23, b.invert(16));
        assertEquals(22, b.invert(-26));
        assertEquals(15, b.invert(67));

        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals(0, c.invert(3));
        assertEquals(5, c.invert(5));
        assertEquals(1, c.invert(2));
        assertEquals(0, c.invert(-3));
        assertEquals(5, c.invert(17));

        Permutation d = getNewPermutation("(F)", getNewAlphabet("F"));
        assertEquals(0, d.invert(0));
    }

    @Test
    public void testInvertChar() {
        Permutation a = getNewPermutation("", getNewAlphabet("XYZ"));
        assertEquals('X', a.invert('X'));
        assertEquals('Z', a.invert('Z'));

        Permutation b = getNewPermutation("(AXQW)", getNewAlphabet());
        assertEquals('A', b.invert('X'));
        assertEquals('B', b.invert('B'));
        assertEquals('W', b.invert('A'));

        Permutation c = getNewPermutation("(DEA) (BC) (F)", getNewAlphabet("ABCDEF"));
        assertEquals('A', c.invert('D'));
        assertEquals('C', c.invert('B'));
        assertEquals('F', c.invert('F'));
        assertEquals('D', c.invert('E'));

        Permutation d = getNewPermutation("(F)", getNewAlphabet("F"));
        assertEquals('F', d.invert('F'));
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
