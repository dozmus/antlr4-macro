package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.util.ArrayHelper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A set of tests for {@link Token}.
 */
public final class TokenTest {

    private static void assertToken(TokenDefinition def) {
        Token token = new Token(def);
        assertEquals(token.getName(), def.name());
        assertEquals(token.getValue(), def.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokenConstructorForRegexTokenDefinitionParameter() {
        new Token(TokenDefinition.DIGIT);
    }

    @Test
    public void testTokenConstructorForTokenDefinitionParameters() {
        // Literal type
        assertToken(TokenDefinition.ASTERISK);
        assertToken(TokenDefinition.NEW_LINE);

        // Special type
        assertToken(TokenDefinition.EOF);
    }

    @Test
    public void testArrayContains() {
        Token[] tokens = new Token[]{
                new Token(TokenDefinition.ASTERISK),
                new Token(TokenDefinition.BACK_SLASH),
                new Token(TokenDefinition.AMPERSAND),
                new Token(TokenDefinition.COMMERCIAL_AT),
                new Token(TokenDefinition.CARET),
                new Token(TokenDefinition.CARRIAGE_RETURN),
                new Token(TokenDefinition.COLON)
        };

        // Test valid values
        for (Token token : tokens) {
            assertTrue(ArrayHelper.arrayContains(tokens, token));
        }

        // Test invalid values
        assertFalse(ArrayHelper.arrayContains(tokens, new Token(TokenDefinition.SEMICOLON)));
        assertFalse(ArrayHelper.arrayContains(tokens, new Token(TokenDefinition.NEW_LINE)));
        assertFalse(ArrayHelper.arrayContains(tokens, new Token(TokenDefinition.EOF)));
        assertFalse(ArrayHelper.arrayContains(tokens, new Token(TokenDefinition.LEFT_BRACKET)));
    }

    @Test
    public void testArrayToString() {
        Token[] tokens = new Token[]{
                new Token(TokenDefinition.ASTERISK),
                new Token(TokenDefinition.BACK_SLASH),
                new Token(TokenDefinition.AMPERSAND),
                new Token(TokenDefinition.COMMERCIAL_AT),
                new Token(TokenDefinition.CARET),
                new Token(TokenDefinition.CARRIAGE_RETURN),
                new Token(TokenDefinition.COLON)
        };

        String expectedValue = "Token[] { Token(ASTERISK='*'), Token(BACK_SLASH='\\'), Token(AMPERSAND='&'),"
        + " Token(COMMERCIAL_AT='@'), Token(CARET='^'), Token(CARRIAGE_RETURN=''), Token(COLON=':') }";
        assertEquals(expectedValue, ArrayHelper.toString(tokens));
    }
}
