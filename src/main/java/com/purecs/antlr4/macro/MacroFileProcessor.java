package com.purecs.antlr4.macro;

import com.purecs.antlr4.macro.parse.Parser;
import com.purecs.antlr4.macro.parse.ParserFactory;
import com.purecs.antlr4.macro.refactor.MacroRefactorer;
import com.purecs.antlr4.macro.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public final class MacroFileProcessor {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(MacroFileProcessor.class);
    private final String inputFileName;
    private final String outputFileName;

    /**
     * @throws NullPointerException if either argument is null
     */
    public MacroFileProcessor(String inputFileName, String outputFileName) {
        this.inputFileName = Objects.requireNonNull(inputFileName);
        this.outputFileName = Objects.requireNonNull(outputFileName);
    }

    /**
     * Processes all macro files in the given directory. Recursive traversal can be toggled using
     * {@link Main.CommandLineFlags#recursive}.
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
        try {
            MacroFileProcessor mfp = new MacroFileProcessor(inFileName, outFileName);
            System.out.println("Processing file: " + inFileName);

            // Read file
            Path input = Paths.get(inFileName);
            String content = new String(Files.readAllBytes(input));

            // Parse file
            Parser parser = ParserFactory.parse(input);

            // Generate output
            String code = new MacroRefactorer().refactor(content, parser.getMacros(), parser.getMacroUses());

            // Write output
            mfp.writeOutput(code);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            LOGGER.error("Failed to process file '{}' because '{}'", inFileName, e.getMessage());
        }
    }

    private void writeOutput(String code) throws Exception {
        // Debug output
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Writing output to file '{}' -> '{}'", inputFileName, outputFileName);
        }

        // Write output
        Path output = Paths.get(outputFileName);
        FileHelper.writeOutput(output, code);
    }
}
