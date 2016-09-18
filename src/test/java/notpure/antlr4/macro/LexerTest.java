package notpure.antlr4.macro;

import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.processor.Lexer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link Lexer}.
 */
public final class LexerTest {

    /**
     * Asserts that the given token's line number and column number matches the provided ones.
     */
    private static void assertTokenLineAndColEquals(Token token, int lineNo, int colNo) {
        assertEquals(lineNo, token.getLineNo());
        assertEquals(colNo, token.getColNo());
    }

    /**
     * Tests each lexer token definition once.
     */
    @Test
    public void generalLexerTestOfAllTokenDefinitions() {
        List<Token> expectedOutput = new ArrayList<>();

        // Generate sample input
        String inputString = "";

        for (TokenDefinition def : TokenDefinition.values()) {
            if (def == TokenDefinition.DIGIT) { // Digit definition
                inputString += "0";
                expectedOutput.add(new Token(def.name(), "0"));
            } else if (def == TokenDefinition.LETTER) { // Letter definition
                inputString += "a";
                inputString += "B";
                expectedOutput.add(new Token(def.name(), "a"));
                expectedOutput.add(new Token(def.name(), "B"));
            } else if (def.getValueType() == TokenDefinition.ValueType.LITERAL) { // general literal definitions
                String letter = def.getValue();
                inputString += letter;
                expectedOutput.add(new Token(def.name(), letter));
            }
        }

        // Tokenize input
        List<Token> actualOutput = Lexer.tokenize(inputString);

        // Compare outputs
        assertEquals(expectedOutput.size() + 1, actualOutput.size());

        // Iterate over generated tokens
        for (int i = 0; i < expectedOutput.size(); i++) {
            Token expectedToken = expectedOutput.get(i);
            Token actualToken = actualOutput.get(i);
            assertEquals(expectedToken, actualToken);
        }
    }

    /**
     * Tests the line and col numbers given to each token in the input.
     */
    @Test
    public void lexerTestOfLineAndColNoAssignments() {
        // Tokenize input
        List<Token> actualOutput = Lexer.tokenize("my friend\r\nhello!");

        // Check output
        assertEquals(17 + 1, actualOutput.size());

        for (int i = 0; i < 9; i++)
            assertTokenLineAndColEquals(actualOutput.get(i), 0, i);

        assertTokenLineAndColEquals(actualOutput.get(9), 0, 9);
        assertTokenLineAndColEquals(actualOutput.get(10), 0, 10);

        for (int i = 11; i < 17; i++)
            assertTokenLineAndColEquals(actualOutput.get(i), 1, i - 11);
    }
}
