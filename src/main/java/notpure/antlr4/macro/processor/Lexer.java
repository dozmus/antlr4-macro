package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.util.CharStream;
import notpure.antlr4.macro.util.FileHelper;
import notpure.antlr4.macro.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A lexer.
 */
public final class Lexer {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Lexer.class);
    /**
     * Null character.
     */
    private static final char NULL_CHAR = Character.MIN_VALUE;
    private final List<Token> tokens = new ArrayList<>();
    private int currentLineNo = 0;
    private int currentColNo = 0;

    /**
     * Tokenizes the input stream into tokens.
     */
    public static List<Token> tokenize(InputStream inputStream) {
        if (inputStream == null)
            throw new IllegalArgumentException("InputStream is null.");

        Lexer lexer = new Lexer();
        CharStream in = new CharStream(inputStream);

        // Process input
        while (in.hasNext()) {
            // Get next string
            char value = in.next();

            // Try to map it to a definition
            lexer.tokenize(value);
        }

        // Add end of file token
        lexer.tokenize(NULL_CHAR);
        return lexer.getTokens();
    }

    /**
     * Tokenizes the input String into tokens.
     */
    public static List<Token> tokenize(String input) {
        return tokenize(FileHelper.stringStream(input));
    }

    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Attempts to match the provided value with a {@link TokenDefinition}, if this is successful the new token
     * is added to {@link Lexer#tokens}. If the value is {@link Lexer#NULL_CHAR} then
     * {@link TokenDefinition#EOF} is added to {@link Lexer#tokens} instead.
     *
     * @param value The character to try to parse.
     */
    private void tokenize(char value) {
        // Attempt to tokenize EOF
        if (value == NULL_CHAR) {
            getTokens().add(new Token(TokenDefinition.EOF));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Current value: '{}' has been mapped to '{}'", "null", TokenDefinition.EOF.name());
            }
            return;
        }

        // Attempt to tokenize char
        String val = value + "";

        Optional<TokenDefinition> possibleDef = Arrays.stream(TokenDefinition.values())
                .filter(d -> d.matches(val))
                .findFirst();
        TokenDefinition def = possibleDef.isPresent() ? possibleDef.get() : TokenDefinition.UNKNOWN;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Current value: '{}' has been mapped to '{}'", StringHelper.escape(value), def.name());
        }

        // Add token
        getTokens().add(new Token(def.name(), val, currentLineNo, currentColNo));

        // Update lineNo/colNo
        currentColNo++;

        if (def == TokenDefinition.NEW_LINE) {
            currentLineNo++;
            currentColNo = 0;
        }
    }
}
