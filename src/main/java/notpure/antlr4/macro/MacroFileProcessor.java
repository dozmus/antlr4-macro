package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Antlr4Serializable;
import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.macro.MacroResolverException;
import notpure.antlr4.macro.model.macro.MissingMacroRuleException;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.processor.ExpressionProcessor;
import notpure.antlr4.macro.processor.Lexer;
import notpure.antlr4.macro.processor.parser.SimpleParser;
import notpure.antlr4.macro.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The master processor for macro files.
 */
public final class MacroFileProcessor {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(MacroFileProcessor.class);
    private final String inputFileName;
    private final String outputFileName;

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
        try {
            MacroFileProcessor mfp = new MacroFileProcessor(inFileName, outFileName);
            System.out.println("Processing file: " + inFileName);

            // Tokenize and parse input file
            mfp.tokenize();
            mfp.parse();

            // Resolve and apply macro definitions
            mfp.resolveMacroDefinitions();
            mfp.applyMacroDefinitions();

            // Generate and write antlr code
            mfp.generateAntlrCode();
            mfp.writeOutput();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            LOGGER.error("Failed to process file '{}' because '{}'", inFileName, e.getMessage());
        }
    }

    public MacroFileProcessor(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    private List<Token> tokens;
    private List<Expression> expressions;
    private List<Expression> macroExpressions;

    public void setOutputExpressions(List<Expression> outputExpressions) {
        this.outputExpressions = outputExpressions;
    }

    private List<Expression> outputExpressions;

    public String getAntlrCode() {
        return antlrCode;
    }

    private String antlrCode;

    private void tokenize() throws IOException {
        try (InputStream inputStream = new FileInputStream(inputFileName)) {
            tokens = Lexer.tokenize(inputStream);
        }
    }

    private void parse() throws IllegalStateException {
        // Check tokens list
        if (tokens == null) {
            throw new IllegalStateException("Tokens list is null");
        }

        // Debug output
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Parsing tokens into expressions (token-len={})", tokens.size());
        }

        // Process tokens into expressions
        SimpleParser parser = new SimpleParser();
        parser.parse(tokens);
        expressions = parser.getExpressions();
    }

    private void resolveMacroDefinitions() throws MacroResolverException {
        // Filter macro rules
        macroExpressions = expressions.stream()
                .filter(expr -> expr.getType() == ExpressionType.MACRO_RULE)
                .collect(Collectors.toList());

        // Debug output
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resolving macro definitions (expr-len={}, macro-expr-len={})", expressions.size(),
                    macroExpressions.size());
        }

        // Resolve macro expressions
        macroExpressions = ExpressionProcessor.resolveMacros(macroExpressions);
    }

    private void applyMacroDefinitions() throws MissingMacroRuleException {
        // Debug output
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Applying macro definitions (expr-len={},macro-expr-len={})", expressions.size(),
                    macroExpressions.size());
        }

        // Apply macro definitions
        outputExpressions = ExpressionProcessor.applyMacros(expressions, macroExpressions);
    }

    public void generateAntlrCode() {
        // Debug output
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Generating antlr code (output-expr-len={})", outputExpressions.size());
        }

        // Generate and put code
        antlrCode = Antlr4Serializable.serializeAll(outputExpressions);
    }

    private void writeOutput() throws Exception {
        // Debug output
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Writing output to file '{}'->'{}'", inputFileName, outputFileName);
        }

        // Write output
        if (inputFileName == null) {
            throw new IllegalArgumentException("inputFileName is null");
        }

        // Check output file name
        if (outputFileName == null) {
            throw new IllegalArgumentException("outputFileName is null");
        }
        writeOutput(inputFileName, outputFileName, antlrCode);
    }

    /**
     * Attempts to overwrite the content of the input file.
     * @param antlr4Code The content to write.
     */
    public static void writeOutput(String inFileName, String outFileName, String antlr4Code) throws IOException {
        File file = new File(outFileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(antlr4Code);
            writer.flush();
        }
    }
}
