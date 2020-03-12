package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Amit Bhat
 */
public class MachineTest {

    private ArrayList<Rotor> allRotors;

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setAllRotors(HashMap<String, String> rotors) {
        allRotors = new ArrayList<>();

        for (String x : rotors.keySet()) {
            Rotor r = new MovingRotor(x,
                    new Permutation(rotors.get(x), UPPER), "");
            allRotors.add(r);
        }
    }

    @Test
    public void testConstructor() {
        setAllRotors(NAVALA);
        Machine m = new Machine(UPPER, 5, 3, allRotors);
        assertEquals(5, m.numRotors());
        assertEquals(3, m.numPawls());
        HashMap<String, Rotor> rotors = m.allRotors();
        assertEquals(allRotors.get(2), rotors.get(allRotors.get(2).name()));
        assertEquals(allRotors.get(5), rotors.get(allRotors.get(5).name()));
    }

    @Test
    public void testInsertRotors() {
        setAllRotors(NAVALA);
        Machine m = new Machine(UPPER, 5, 3, allRotors);
        String[] slots = {"B", "Beta", "I", "II", "III"};
        m.insertRotors(slots);
        Rotor[] slotRotors = m.slotRotors();
        assertEquals(slots[0], slotRotors[0].name());
        assertEquals(slots[4], slotRotors[4].name());
        assertEquals(0, slotRotors[0].setting());
    }

    @Test
    public void testSetRotors() {
        setAllRotors(NAVALA);
        Machine m = new Machine(UPPER, 5, 3, allRotors);
        String[] slots = {"B", "Beta", "I", "II", "III"};
        String settings = "DZQI";
        m.insertRotors(slots);
        m.setRotors(settings);
        Rotor[] slotRotors = m.slotRotors();
        assertEquals(3, slotRotors[1].setting());
        assertEquals(25, slotRotors[2].setting());
        assertEquals(16, slotRotors[3].setting());
        assertEquals(8, slotRotors[4].setting());
    }

    @Test
    public void testConvertChar() {
        setAllRotors(NAVALA);
        Machine m = new Machine(UPPER, 5, 3, allRotors);
        String[] slots = {"B", "Beta", "III", "IV", "I"};
        String settings = "AXLE";
        m.insertRotors(slots);
        m.setRotors(settings);
        m.setPlugboard(new Permutation("(YF) (ZH)", UPPER));
        assertEquals(UPPER.toInt('Z'), m.convert(UPPER.toInt('Y')));
    }

    @Test
    public void testConvertStr() {
        setAllRotors(NAVALA);

        Machine m1 = new Machine(UPPER, 5, 3, allRotors);
        String[] slots1 = {"B", "Beta", "I", "II", "III"};
        String settings1 = "AAAA";
        m1.insertRotors(slots1);
        m1.setRotors(settings1);
        m1.setPlugboard(new Permutation("(AQ) (EP)", UPPER));
        assertEquals("IHBDQ", m1.convert("HELLO"));

        Machine m2 = new Machine(UPPER, 5, 3, allRotors);
        String[] slots2 = {"B", "Beta", "I", "II", "III"};
        String settings2 = "AAAA";
        m2.insertRotors(slots2);
        m2.setRotors(settings2);
        m2.setPlugboard(new Permutation("(AQ) (EP)", UPPER));
        assertEquals("IHBDQ QMTQZ", m2.convert("HELLO WORLD"));

        Machine m3 = new Machine(UPPER, 5, 3, allRotors);
        String[] slots3 = {"B", "Beta", "I", "II", "III"};
        String settings3 = "AAAA";
        m3.insertRotors(slots3);
        m3.setRotors(settings3);
        m3.setPlugboard(new Permutation("(AQ) (EP)", UPPER));
        assertEquals("HELLO WORLD", m3.convert("IHBDQ QMTQZ"));
    }

}
