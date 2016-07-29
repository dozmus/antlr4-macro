package notpure.antlr4.macro;

import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.token.TokenIterator;
import notpure.antlr4.macro.util.TokenHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link TokenIterator}.
 */
public final class TokenIteratorTest {

    @Test
    public void listInputTest() {
        String input = "hello";
        List<Token> tokens = new ArrayList<>(TokenHelper.getTokens(TokenDefinition.LETTER, input));
        TokenIterator it = new TokenIterator(tokens);

        assertEquals(input.length(), it.size());
        assertEquals(input.length(), it.remaining());
        assertEquals(0, it.getCurrentIdx());
        assertEquals(true, it.hasNext());

        assertEquals(new Token(TokenDefinition.LETTER.name(), "h"), it.peek());
        assertEquals(new Token(TokenDefinition.LETTER.name(), "e"), it.peek(1));
        assertEquals(0, it.getCurrentIdx());
        assertEquals(true, it.hasNext());

        assertEquals(new Token(TokenDefinition.LETTER.name(), "h"), it.next());
        assertEquals(true, it.hasNext());
        assertEquals(1, it.getCurrentIdx());
        assertEquals(4, it.remaining());

        assertEquals(new Token(TokenDefinition.LETTER.name(), "e"), it.next());
        assertEquals(true, it.hasNext());
        assertEquals(2, it.getCurrentIdx());
        assertEquals(3, it.remaining());

        assertEquals(new Token(TokenDefinition.LETTER.name(), "l"), it.next());
        assertEquals(true, it.hasNext());
        assertEquals(3, it.getCurrentIdx());
        assertEquals(2, it.remaining());

        assertEquals(new Token(TokenDefinition.LETTER.name(), "l"), it.next());
        assertEquals(true, it.hasNext());
        assertEquals(4, it.getCurrentIdx());
        assertEquals(1, it.remaining());

        assertEquals(new Token(TokenDefinition.LETTER.name(), "o"), it.next());
        assertEquals(false, it.hasNext());
        assertEquals(5, it.getCurrentIdx());
        assertEquals(0, it.remaining());
    }

    @Test
    public void hasNextStringInputTest() {
        String input = "hello";
        List<Token> tokens = new ArrayList<>(TokenHelper.getTokens(TokenDefinition.LETTER, input));
        TokenIterator it = new TokenIterator(tokens);

        assertEquals(input.length(), it.size());
        assertEquals(true, it.hasNext(input));
        assertEquals(false, it.hasNext("hellO"));
        assertEquals(false, it.hasNext("helloo"));
        assertEquals(false, it.hasNext(null));
    }
}
