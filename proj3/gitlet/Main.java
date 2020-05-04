package gitlet;

import java.io.IOException;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Amit Bhat
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        switch (args[0]) {
        case "init":
            GitCommands.init();
            break;
        case "add":
            GitCommands.add(args[1]);
            break;
        case "rm":
            GitCommands.rm(args[1]);
            break;
        case "commit":
            GitCommands.commit(args[1]);
            break;
        }
    }

}
