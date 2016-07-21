package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.impl.SimpleLexer;
import notpure.antlr4.macro.processor.model.token.Token;
import notpure.antlr4.macro.processor.model.token.TokenDefinition;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link SimpleLexer}.
 */
public final class SimpleLexerTest {

    /**
     * Tests each lexer token definition once.
     */
    @Test
    public void generalLexerTestOfAllTokenDefinitions() {
        List<Token> expectedOutput = new ArrayList<>();

        // Generate sample input
        String inputString = "";

        for (TokenDefinition def : TokenDefinition.values()) {
            if (def == TokenDefinition.DIGIT) {
                inputString += "0";
                expectedOutput.add(new Token(def.name(), "0"));
            } else if (def == TokenDefinition.LETTER) {
                inputString += "a";
                inputString += "B";
                expectedOutput.add(new Token(def.name(), "a"));
                expectedOutput.add(new Token(def.name(), "B"));
            } else if (def.isLiteral()) { // general definitions
                String letter = def.getGroup();
                inputString += letter;
                expectedOutput.add(new Token(def.name(), letter));
            }
        }

        // Wrap string in stream
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));

        // Tokenize input
        List<Token> actualOutput = new SimpleLexer().tokenize(inputStream).getTokens();

        // Compare outputs
        assertEquals(expectedOutput.size(), actualOutput.size());

        // Iterate over generated tokens
        for (int i = 0; i < expectedOutput.size(); i++) {
            Token eToken = expectedOutput.get(i);
            Token aToken = actualOutput.get(i);

            assertEquals(eToken, aToken);
        }
    }
}
