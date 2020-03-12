package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import java.util.NoSuchElementException;
import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Amit Bhat
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        _config.close();

        if (!_input.hasNext("\\*")) {
            throw new EnigmaException("input doesn't start"
                    + "with a setting");
        }

        String msg = "";
        while (_input.hasNextLine()) {
            String nxt = _input.nextLine();
            if (nxt.equals("")) {
                _output.println();
            } else if (nxt.contains("*")) {
                nxt = nxt.substring(1);
                setUp(m, nxt);
            } else {
                msg = m.convert(nxt);
                printMessageLine(msg);
            }
        }
        _input.close();
        _output.close();
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            _alphabet = new Alphabet(_config.nextLine());
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            _config.nextLine();
            while (_config.hasNextLine()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String posAndNotch = _config.next();
            String cycle = _config.nextLine();
            while (_config.hasNext("\\(([A-Z]||[a-z]||\\_||\\.)+"
                    + "\\)")) {
                cycle = cycle + _config.nextLine();
            }
            String notches = "";

            if (posAndNotch.length() > 1) {
                for (int i = 1; i < posAndNotch.length(); i += 1) {
                    notches = notches + posAndNotch.charAt(i);
                }
            }

            if (posAndNotch.charAt(0) == 'R') {
                return new Reflector(name,
                        new Permutation(cycle, _alphabet));
            } else if (posAndNotch.charAt(0) == 'N') {
                return new FixedRotor(name,
                        new Permutation(cycle, _alphabet));
            } else {
                return new MovingRotor(name,
                        new Permutation(cycle, _alphabet), notches);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        try {
            Scanner set = new Scanner(settings);
            String[] slots = new String[M.numRotors()];
            for (int i = 0; i < M.numRotors(); i += 1) {
                String newRotor = set.next();
                List<String> check = Arrays.asList(slots);
                if (check.contains(newRotor)) {
                    throw new EnigmaException("rotor already in list");
                }
                slots[i] = newRotor;
            }

            String positions = set.next();
            String plug = "";
            if (set.hasNext()) {
                plug = set.nextLine();
            }
            M.insertRotors(slots);
            M.setRotors(positions);
            M.setPlugboard(new Permutation(plug, _alphabet));
        } catch (NoSuchElementException excp) {
            throw error("bad setting description");
        }

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.equals("")) {
            _output.println("\n");
        } else {
            int total = 0;
            for (int i = 0; i < msg.length() / 5; i += 1) {
                String out = "";
                for (int j = 0; j < 5; j += 1, total += 1) {
                    out = out + msg.charAt(total);
                }
                _output.print(out + " ");
            }

            String last = "";
            for (; total < msg.length(); total += 1) {
                last = last + msg.charAt(total);
            }
            _output.println(last);
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
