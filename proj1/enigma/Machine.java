package enigma;

import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Amit Bhat
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new HashMap<String, Rotor>();
        Object[] rotors = allRotors.toArray();
        for (Object x : rotors) {
            Rotor y = (Rotor) x;
            _allRotors.put(y.name(), y);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return all the available rotors I have. */
    HashMap<String, Rotor> allRotors() {
        return _allRotors;
    }

    /** Return all the rotors in my slots. */
    Rotor[] slotRotors() {
        return _slotRotors;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (numRotors() != rotors.length) {
            throw new EnigmaException("Number of rotors given does "
                    + "not agree with number of slots available");
        }

        _slotRotors = new Rotor[_numRotors];
        for (int i = 0; i < rotors.length; i += 1) {
            _slotRotors[i] = _allRotors.get(rotors[i]);
            _slotRotors[i].set(0);
        }

        if (!_slotRotors[0].reflecting()) {
            throw new EnigmaException("first rotor "
                    + "doesn't reflect");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("incorrect number of settings");
        }
        for (int i = 0; i < setting.length(); i += 1) {
            _slotRotors[i + 1].set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = new FixedRotor("Plugboard", plugboard);
    }


    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRings(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("incorrect number of rings");
        }
        for (int i = 0; i < setting.length(); i += 1) {
            _slotRotors[i + 1].setRing(setting.charAt(i));
        }
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean lastMoved = false;
        for (int i = 1; i < _slotRotors.length; i += 1) {
            if (i + 1 == _slotRotors.length) {
                _slotRotors[i].advance();
            } else if (_slotRotors[i].rotates()
                    && (_slotRotors[i + 1].atNotch()
                    || lastMoved)) {
                _slotRotors[i].advance();
                lastMoved = true;
            } else {
                lastMoved = false;
            }
        }

        c = _plugBoard.convertForward(c);
        for (int i = _slotRotors.length - 1; i > 0; i -= 1) {
            Rotor current = _slotRotors[i];
            c = current.convertForward(c);
        }
        c = _slotRotors[0].convertForward(c);
        for (int i = 1; i < _slotRotors.length; i += 1) {
            Rotor current = _slotRotors[i];
            c = current.convertBackward(c);
        }
        c = _plugBoard.convertBackward(c);

        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String converted = "";
        for (int i = 0; i < msg.length(); i += 1) {
            char currentChar = msg.charAt(i);
            char newChar; int newInt;
            if (currentChar == ' ') {
                continue;
            } else if (!_alphabet.contains(currentChar)) {
                throw new EnigmaException("Letters not"
                        + "in alphabet");
            }
            newInt = convert(_alphabet.toInt(currentChar));
            newChar = _alphabet.toChar(newInt);
            converted = converted + newChar;
        }
        return converted;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors in this machine. */
    private int _numRotors;

    /** Number of pawls in this machine. */
    private int _pawls;

    /** All the available rotors in this machine. */
    private HashMap<String, Rotor> _allRotors;

    /** The rotors that are actually used in the slots
     *  of the machine. */
    private Rotor[] _slotRotors;

    /** The plugboard for this particular machine. */
    private FixedRotor _plugBoard;

}
