package notpure.antlr4.macro;

import notpure.antlr4.macro.processor.cmd.*;
import notpure.antlr4.macro.util.FileHelper;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The master processor for macro files.
 */
public final class MacroFileProcessor {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(MacroFileProcessor.class);

    /**
     * Processes all macro files in the given directory. Recursive traversal can be toggled using
     * {@link notpure.antlr4.macro.Main.CommandLineFlags#recursive}.
     */
    public static void processDirectory(String directory) {
        // Aggregate target files
        ArrayList<String> fileNames = new ArrayList<>();
        FileHelper.getFileNames(fileNames, Paths.get(directory), ".mg4", Main.CommandLineFlags.recursive);

        // Process all files
        fileNames.forEach(MacroFileProcessor::processFile);
    }

    /**
     * Processes the given macro file.
     */
    public static void processFile(String inFileName) {
        // Attempt to resolve directory of input if necessary
        if (!new File(inFileName).exists()) {
            File baseDir = new File(System.getProperty("user.dir"));
            inFileName = new File(baseDir, inFileName).getPath();
        }

        // Process input file
        String outFileName = FileHelper.parseFileName(inFileName) + ".g4";
        processFile(inFileName, outFileName);
    }

    /**
     * Processes the given macro file, into one with the specified name.
     */
    public static void processFile(String inFileName, String outFileName) {
        Context context = new ContextBase();

        // Put initial values
        context.put("input-file-name", inFileName);
        context.put("output-file-name", outFileName);

        // Execute commands
        System.out.println("Processing file: " + inFileName);

        try {
            new Tokenize().execute(context);
            new Parse().execute(context);
            new ResolveMacroExpressions().execute(context);
            new ApplyMacroDefinitions().execute(context);
            new GenerateAntlrCode().execute(context);
            new WriteOutput().execute(context);
        } catch (Exception e) {
            System.out.println("Error processing file: " + e.getMessage());
            LOGGER.error("Failed to process file '{}' because '{}'", inFileName, e.getMessage());
        }
    }
}
