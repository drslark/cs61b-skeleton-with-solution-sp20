package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Amit Bhat
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        addCycle(cycles);
    }


    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles = new HashMap<Character, Character>();
        for (int i = 0; i < cycle.length();) {
            if (cycle.charAt(i) == '(') {
                if (cycle.charAt(i + 1) == ')') {
                    throw new EnigmaException("Empty cycle");
                }
                i += 1;
                char first = cycle.charAt(i);
                while (cycle.charAt(i) != ')') {
                    char key = cycle.charAt(i);
                    if (_cycles.containsKey(key)) {
                        throw new EnigmaException("Cycle element "
                                + "already in cycle");
                    } else if (i + 1 == cycle.length()
                            || cycle.charAt(i + 1) == ' ') {
                        throw new EnigmaException("Cycle has no "
                                + "right closing");
                    } else if (cycle.charAt(i + 1) == ')') {
                        _cycles.put(cycle.charAt(i), first);
                    } else {
                        char val = cycle.charAt(i + 1);
                        _cycles.put(key, val);
                    }
                    i += 1;
                }

            } else if (_alphabet.contains(cycle.charAt(i))) {
                throw new EnigmaException("Cycle has no "
                        + "left closing");
            }
            else {
                i += 1;
            }
        }
        if (_alphabet.size() < _cycles.size()) {
            throw new EnigmaException("Cycle element"
                    + " not in alphabet");
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
         p = wrap(p);
         char ch = _alphabet.toChar(p);
         ch = permute(ch);
         return _alphabet.toInt(ch);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        char ch = _alphabet.toChar(c);
        ch = invert(ch);
        return _alphabet.toInt(ch);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw new EnigmaException("Permuted value not in alphabet");
        }
        return _cycles.getOrDefault(p, p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw new EnigmaException("Inverted value not in alphabet");
        }

        char inv = c;
        if (_cycles.containsValue(c)) {
            for (char x :_cycles.keySet()) {
                if (_cycles.get(x) == c) {
                    inv = x;
                }
            }
        }
        return inv;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return the cycles used to initialize this Permutation. */
     HashMap<Character, Character> getCycles() {
     return _cycles;
     }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i += 1) {
            if (permute(i) == i) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private HashMap<Character, Character> _cycles;

}
