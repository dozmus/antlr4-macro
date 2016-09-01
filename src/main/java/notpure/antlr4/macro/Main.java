package notpure.antlr4.macro;

/**
 * Application entry-point.
 */
public final class Main {

    public static void main(String[] args) {
        // Check argument count
        if (args.length == 0) {
            printUsage();
            return;
        }

        // Execute command
        switch (args[0]) {
            case "-r": // Recursively process all files in the working directory
                MacroFileProcessor.processDirectory(System.getProperty("user.dir"));
                break;
            case "-i": // Process a single file
                if (args.length != 2) {
                    printUsage();
                } else {
                    // Get file name
                    String inFileName = args[1];

                    // Process files
                    MacroFileProcessor.processFile(inFileName);
                }
                break;
            case "-help":
            case "-usage":
            default:
                printUsage();
                break;
        }
    }

    /**
     * Prints program usage.
     */
    private static void printUsage() {
        System.out.println("Invalid usage, try:");
        System.out.println("antlr4-macros.jar -i <input-file> | to process the target file");
        System.out.println("antlr4-macros.jar -r | to process all .mg4 files in the current directory recursively");
        System.out.println("antlr4-macros.jar -usage | to display this message");
    }
}
