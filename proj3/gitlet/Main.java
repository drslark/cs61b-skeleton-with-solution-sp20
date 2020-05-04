package gitlet;

import java.io.IOException;

import static gitlet.GitCommands.init;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Amit Bhat
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        if (args[0].equals("init")) {
            init();
        }
    }

}
