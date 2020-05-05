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
        case "checkout":
            if (args[1].equals("--") && args.length == 3) {
                GitCommands.checkout(args[2], "", 1);
            } else if (args[1].matches("[a-f0-9]+") && (args[2].equals("--"))
                    && args.length == 4) {
                GitCommands.checkout(args[1], args[3], 2);
            } else if (args.length == 2){
                GitCommands.checkout(args[1], "", 3);
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
        default:
            System.out.println("No command with that name exists.");
            System.exit(0);
        }

    }

}
