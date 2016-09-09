package notpure.antlr4.macro;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Application entry-point.
 */
public final class Main {

    /**
     * Project URL.
     */
    private static final String PROJECT_URL = "https://github.com/PureCS/antlr4-macro";
    /**
     * Project version.
     */
    private static final String PROJECT_VERSION = "v1.0.0-beta.1";

    public static void main(String[] args) {
        try {
            Options options = commandLineOptions();
            CommandLine line = new DefaultParser().parse(options, args);

            if (line.hasOption("url")) {
                System.out.println("Project url: " + PROJECT_URL);
            }

            if (line.hasOption("version")) {
                System.out.println("Project version: " + PROJECT_VERSION);
            }

            if (line.hasOption("optimize")) {
                CommandLineFlags.optimize = true;
            }

            if (line.hasOption("help")) {
                new HelpFormatter().printHelp("antlr4-macro", options);
            }

            if (line.hasOption("i")) {
                String targetInput = line.getOptionValue("i");

                if (targetInput == null) {
                    System.out.println("Missing target input argument for: -i,--input <arg>");
                } else {
                    if (targetInput.equals("*")) { // Recursively process all files in the working directory
                        MacroFileProcessor.processDirectory(System.getProperty("user.dir"));
                    } else { // Process single file
                        // Get file name
                        String inFileName = args[1];

                        // Process files
                        MacroFileProcessor.processFile(inFileName);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Command-line options.
     */
    private static Options commandLineOptions() {
        Options options = new Options();
        options.addOption("help", false, "prints this message");
        options.addOption("url", false, "prints the project url");
        options.addOption("version", false, "prints the version information");
        options.addOption("optimize", false, "optimize output grammar");
        options.addOption("i", "input", true, "processes the given file(s)");
        return options;
    }


    /**
     * Command-line parsed flags.
     */
    public static class CommandLineFlags {

        /**
         * If output grammars should be optimized.
         */
        public static boolean optimize;
    }
}
