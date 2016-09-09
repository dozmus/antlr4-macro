package notpure.antlr4.macro.processor.lexer;

import notpure.antlr4.macro.model.lexer.Lexer;
import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.util.CharStream;
import notpure.antlr4.macro.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

/**
 * A simple lexer.
 */
public final class SimpleLexer extends Lexer {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLexer.class);
    /**
     * Null character.
     */
    private static final char NULL_CHAR = Character.MIN_VALUE;
    private int currentLineNo = 0;
    private int currentColNo = 0;

    /**
     * Tokenizes the input stream into simple tokens.
     */
    @Override
    public Lexer tokenize(InputStream inputStream) {
        if (inputStream == null)
            throw new IllegalArgumentException("InputStream is null.");

        CharStream in = new CharStream(inputStream);

        // Process input
        while (in.hasNext()) {
            // Get next string
            char value = in.next();

            // Try to map it to a definition
            tryTokenize(value);
        }

        // Add end of file token
        tryTokenize(NULL_CHAR);
        return this;
    }

    /**
     * Attempts to match the provided value with a {@link TokenDefinition}, if this is successful the new token
     * is added to {@link SimpleLexer#tokens}. If the value is {@link SimpleLexer#NULL_CHAR} then
     * {@link TokenDefinition#EOF} is added to {@link SimpleLexer#tokens} instead.
     * @param value The character to try to parse.
     */
    private void tryTokenize(char value) {
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

        // Add token
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Current value: '{}' has been mapped to '{}'", StringHelper.escape(value), def.name());
        }
        getTokens().add(new Token(def.name(), val, currentLineNo, currentColNo));

        // Update lineNo/colNo
        currentColNo++;

        if (def == TokenDefinition.NEW_LINE) {
            currentLineNo++;
            currentColNo = 0;
        }
    }
}
