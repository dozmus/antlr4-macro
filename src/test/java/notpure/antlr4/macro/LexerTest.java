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
    public void lexerTestOfLineAndColNo() {
        // Tokenize input
        List<Token> actualOutput = Lexer.tokenize("my friend\r\nhello!");

        // Check output
        assertEquals(17 + 1, actualOutput.size());

        for (int i = 0; i < 9; i++)
            assertTokenLineAndColEquals(0, i, actualOutput.get(i));

        assertTokenLineAndColEquals(0, 9, actualOutput.get(9));
        assertTokenLineAndColEquals(0, 10, actualOutput.get(10));

        for (int i = 11; i < 17; i++)
            assertTokenLineAndColEquals(1, i - 11, actualOutput.get(i));
    }

    private static void assertTokenLineAndColEquals(int lineNo, int colNo, Token actualToken) {
        assertEquals(lineNo, actualToken.getLineNo());
        assertEquals(colNo, actualToken.getColNo());
    }
}
