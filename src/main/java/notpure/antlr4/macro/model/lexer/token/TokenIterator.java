package notpure.antlr4.macro.model.lexer.token;

import java.util.Iterator;
import java.util.List;

/**
 * A specialized iterator for {@link Token}.
 */
public class TokenIterator implements Iterator<Token> {

    protected Token[] tokens;
    protected int currentIdx;

    public TokenIterator(Token[] tokens) {
        this.tokens = tokens;
    }

    public TokenIterator(List<Token> tokens) {
        this.tokens = tokens.toArray(new Token[0]);
    }

    public int getCurrentIdx() {
        return currentIdx;
    }

    @Override
    public boolean hasNext() {
        return currentIdx < tokens.length;
    }

    @Override
    public Token next() {
        return tokens[currentIdx++];
    }

    public Token peek(int offset) {
        return tokens[currentIdx + offset];
    }

    public Token peek() {
        return peek(0);
    }

    public void skip(int offset) {
        currentIdx += offset;
    }

    public int size() {
        return tokens.length;
    }

    public int remaining() {
        return size() - currentIdx;
    }
}
