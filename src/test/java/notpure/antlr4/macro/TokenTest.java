package notpure.antlr4.macro;

import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link notpure.antlr4.macro.model.token.Token}.
 */
public final class TokenTest {

    @Test(expected = IllegalArgumentException.class)
    public void testTokenConstructorForRegexTokenDefinitionParameter() {
        new Token(TokenDefinition.DIGIT);
    }

    @Test
    public void testTokenConstructorForTokenDefinitionParameters() {
        // Literal type
        Token token1 = new Token(TokenDefinition.ASTERISK);
        assertEquals(token1.getName(), TokenDefinition.ASTERISK.name());
        assertEquals(token1.getValue(), TokenDefinition.ASTERISK.getValue());

        Token token2 = new Token(TokenDefinition.NEW_LINE);
        assertEquals(token2.getName(), TokenDefinition.NEW_LINE.name());
        assertEquals(token2.getValue(), TokenDefinition.NEW_LINE.getValue());

        // Special type
        Token token3 = new Token(TokenDefinition.EOF);
        assertEquals(token3.getName(), TokenDefinition.EOF.name());
        assertEquals(token3.getValue(), TokenDefinition.EOF.getValue());
    }

    @Test
    public void testArrayContains() {
        // TODO add
    }

    @Test
    public void testArrayToString() {
        // TODO add
    }
}
