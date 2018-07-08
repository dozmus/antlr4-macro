package com.purecs.antlr4.macro;

import com.purecs.antlr4.macro.parse.Parser;
import com.purecs.antlr4.macro.parse.ParserFactory;
import com.purecs.antlr4.macro.refactor.MacroRefactorer;
import com.purecs.antlr4.macro.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
     * Processes the given macro file, into one with the specified name.
     */
    public void processFile() {
        try {
            System.out.println("Processing file: " + inputFileName);

            // Read file
            Path input = Paths.get(inputFileName);
            String content = new String(Files.readAllBytes(input));

            // Parse file
            Parser parser = ParserFactory.parse(input);

            // Generate output
            String code = new MacroRefactorer().refactor(content, parser.getMacros(), parser.getMacroUses());

            // Write output
            writeOutput(code);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            LOGGER.error("Failed to process file '{}' because '{}'", inputFileName, e.getMessage());
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
