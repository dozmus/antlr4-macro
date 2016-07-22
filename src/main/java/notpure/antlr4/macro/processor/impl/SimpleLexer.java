package notpure.antlr4.macro.processor.impl;

import notpure.antlr4.macro.processor.model.Lexer;
import notpure.antlr4.macro.processor.model.token.Token;
import notpure.antlr4.macro.processor.model.token.TokenDefinition;
import notpure.antlr4.macro.processor.util.CharStream;
import notpure.antlr4.macro.processor.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple lexer.
 */
public final class SimpleLexer extends Lexer {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLexer.class);
    private final List<Token> tokens = new ArrayList<>();

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
        return this;
    }

    @Override
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Attempts to match the provided value with a {@link TokenDefinition}, if this is successful the new token
     * is added to {@link SimpleLexer#tokens}.
     * @param value
     */
    private void tryTokenize(String value) {
        for (TokenDefinition def : TokenDefinition.values()) {
            if (def.matches(value)) {
                LOGGER.info("Current value: '{}' has been mapped to '{}'", value.trim(), def.name());
                tokens.add(new Token(def.name(), value));
                return;
            }
        }
        LOGGER.info("Current value: {}", value);
    }
}
