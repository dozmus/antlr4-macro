package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.token.Token;
import notpure.antlr4.macro.processor.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The master processor for macro files.
 */
public final class MacroFileProcessor {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(MacroFileProcessor.class);

    /**
     * Processes all macro files in the given directory.
     */
    public static void processDirectory(String directory) {
        // Aggregate target files
        ArrayList<String> fileNames = new ArrayList<>();
        FileHelper.getFileNames(fileNames, Paths.get(directory), ".mg4");

        // Process all files
        fileNames.forEach(MacroFileProcessor::processFile);
    }

    /**
     * Processes the given macro file.
     */
    public static void processFile(String inFileName) {
        String outFileName = FileHelper.parseFileName(inFileName) + ".g4";
        processFile(inFileName, outFileName);
    }

    /**
     * Processes the given macro file, into one with the specified name.
     */
    public static void processFile(String inFileName, String outFileName) {
        // Tokenize
        List<Token> tokens;

        try (InputStream inputStream = new FileInputStream(inFileName)) {
            tokens = Lexer.tokenize(inputStream);
        } catch (FileNotFoundException e) {
            LOGGER.warn("File not found: '{}'", inFileName);
            return;
        } catch (IOException e) {
            LOGGER.warn("IOException occurred while processing file: '{}'", inFileName);
            return;
        }

        // Parser
        List<Token> parsedTokens = Parser.parse(tokens);

        // Write to file
        // TODO impl
    }
}
