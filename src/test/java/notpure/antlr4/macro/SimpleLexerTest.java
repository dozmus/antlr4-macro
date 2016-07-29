package notpure.antlr4.macro;

import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.processor.SimpleLexer;
import org.junit.Test;

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
            } else if (def.getValueType() == TokenDefinition.ValueType.LITERAL) { // literal definitions
                String letter = def.getValue();
                inputString += letter;
                expectedOutput.add(new Token(def.name(), letter));
            }
        }

        // Tokenize input
        List<Token> actualOutput = new SimpleLexer().tokenize(inputString).getTokens();

        // Compare outputs
        assertEquals(expectedOutput.size() + 1, actualOutput.size());

        // Iterate over generated tokens
        for (int i = 0; i < expectedOutput.size(); i++) {
            Token expectedToken = expectedOutput.get(i);
            Token actualToken = actualOutput.get(i);
            assertEquals(expectedToken, actualToken);
        }
    }
}
