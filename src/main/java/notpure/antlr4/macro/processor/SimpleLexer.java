package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.token.Token;
import notpure.antlr4.macro.processor.token.TokenDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple lexer.
 */
public final class SimpleLexer {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleLexer.class);

    /**
     * Tokenizes the input stream into simple tokens.
     */
    public static List<Token> tokenize(InputStream inputStream) {
        ArrayList<Token> tokens = new ArrayList<>();
        Scanner in = new Scanner(inputStream);
        in.useDelimiter(""); // to get 1 character at a time

        // Process input
        while (in.hasNext()) {
            // Get next string
            String value = in.next();

            // Try to map it to a definition
            tryTokenize(value, tokens);
        }
        return tokens;
    }

    private static void tryTokenize(String value, List<Token> tokens) {
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
