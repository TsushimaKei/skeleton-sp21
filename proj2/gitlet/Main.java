package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args[0] == null) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                // TODO: handle the `init` command

            case "add":
                Repository.add(args[1]);
                // TODO: handle the `add [filename]` command

            // TODO: FILL THE REST IN
            case "commit":
                Repository.commit(args[1]);


            case "rm":
                Repository.rm(args[1]);
            case "global-log":
                Repository.globallog();
        }

    }
}
