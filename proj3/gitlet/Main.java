package gitlet;

import java.io.IOException;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Amit Bhat
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        if (args[0].equals("init")) {
            GitCommands.init();
            System.exit(0);
        }

        if (!GitCommands.GITLET.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        processFirstHalfCommands(args);
    }

    /** Helper function for processing Gitlet commands. Takes in arguments
     *  ARGS. */
    public static void processFirstHalfCommands(String... args)
            throws IOException {
        switch (args[0]) {
        case "add":
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.add(args[1]);
            break;
        case "rm":
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.rm(args[1]);
            break;
        case "commit":
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.commit(args[1]);
            break;
        case "log":
            if (args.length != 1) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.log();
            break;
        case "global-log":
            if (args.length != 1) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.globalLog();
            break;
        case "find":
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.find(args[1]);
            break;
        case "status":
            if (args.length != 1) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.status();
            break;
        default:
            processSecondHalfCommands(args);
            break;
        }
    }

    /** Helper function for processing remaining Gitlet commands.
     * Takes in arguments ARGS. */
    public static void processSecondHalfCommands(String... args)
            throws IOException {
        switch (args[0]) {
        case "checkout":
            if (args[1].equals("--") && args.length == 3) {
                GitCommands.checkout1(args[2]);
            } else if (args.length == 4
                    && args[1].matches("[a-f0-9]+")
                    && args[2].equals("--")) {
                GitCommands.checkout2(args[1], args[3]);
            } else if (args.length == 2) {
                GitCommands.checkout3(args[1]);
            } else {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            break;
        case "branch":
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.branch(args[1]);
            break;
        case "rm-branch":
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.rmBranch(args[1]);
            break;
        case "reset":
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.reset(args[1]);
            break;
        case "merge":
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            }
            GitCommands.merge(args[1]);
            break;
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }
}
