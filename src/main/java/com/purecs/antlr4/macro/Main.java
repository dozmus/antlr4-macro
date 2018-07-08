package com.purecs.antlr4.macro;

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

            if (line.hasOption("minify")) {
                CommandLineFlags.minify = true;
            }

            if (line.hasOption("recursive")) {
                CommandLineFlags.recursive = true;
            }

            if (line.hasOption("help")) {
                new HelpFormatter().printHelp("antlr4-macro", options);
            }

            if (line.hasOption("i")) {
                String targetInput = line.getOptionValue("i");

                // Process input
                if (targetInput == null) {
                    System.out.println("Missing target input argument for: -i,--input <arg>");
                } else {
                    if (targetInput.equals(".")) { // Process working directory
                        MacroFileProcessor.processDirectory(System.getProperty("user.dir"));
                    } else { // Process single file
                        // Get file name
                        String inFileName = args[1];

                        // Process file
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
        options.addOption("minify", false, "minify output grammar");
        options.addOption("i", "input", true, "processes the given file(s)");
        options.addOption("r", "recursive", false, "processes the given directory recursively");
        return options;
    }


    /**
     * Command-line parsed flags.
     */
    public static final class CommandLineFlags {

        /**
         * If output grammars should be minified.
         */
        public static boolean minify;
        /**
         * If the target directory is to be processed recursively.
         */
        public static boolean recursive;
    }
}
