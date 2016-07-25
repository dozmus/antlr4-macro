package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.model.Lexer;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.util.CharStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple lexer.
 */
public final class SimpleLexer extends Lexer {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLexer.class);

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
            String value = in.next();

            // Try to map it to a definition
            tryTokenize(value);
        }

        // Add end of file token
        tryTokenize(null);
        return this;
    }

    /**
     * Attempts to match the provided value with a {@link TokenDefinition}, if this is successful the new token
     * is added to {@link SimpleLexer#tokens}.
     * @param value
     */
    private void tryTokenize(String value) {
        if (value == null) {
            getTokens().add(new Token(TokenDefinition.EOF));
            LOGGER.info("Current value: '{}' has been mapped to '{}'", "null", TokenDefinition.EOF.name());
            return;
        } else {
            for (TokenDefinition def : TokenDefinition.values()) {
                if (def.matches(value)) {
                    LOGGER.info("Current value: '{}' has been mapped to '{}'", value.trim(), def.name());
                    getTokens().add(new Token(def.name(), value));
                    return;
                }
            }
        }
        LOGGER.info("Current value: {}", value);
    }
}
