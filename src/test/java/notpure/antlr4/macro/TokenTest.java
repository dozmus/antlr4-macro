package notpure.antlr4.macro;

import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.util.CollectionHelper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A set of tests for {@link Token}.
 */
public final class TokenTest {

    /**
     * Constructs a new {@link Token} using the provided definition, and then compares its name and values.
     */
    private static void assertConstructedTokenEquals(TokenDefinition def) {
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
        assertConstructedTokenEquals(TokenDefinition.ASTERISK);
        assertConstructedTokenEquals(TokenDefinition.NEW_LINE);

        // Special type
        assertConstructedTokenEquals(TokenDefinition.EOF);
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

        // Test contained values
        for (Token token : tokens) {
            assertTrue(CollectionHelper.arrayContains(tokens, token));
        }

        // Test un-contained values
        assertFalse(CollectionHelper.arrayContains(tokens, new Token(TokenDefinition.SEMICOLON)));
        assertFalse(CollectionHelper.arrayContains(tokens, new Token(TokenDefinition.NEW_LINE)));
        assertFalse(CollectionHelper.arrayContains(tokens, new Token(TokenDefinition.EOF)));
        assertFalse(CollectionHelper.arrayContains(tokens, new Token(TokenDefinition.LEFT_BRACKET)));
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
        String actualValue = CollectionHelper.toString(tokens);
        assertEquals(expectedValue, actualValue);
    }
}
