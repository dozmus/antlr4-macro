package notpure.antlr4.macro.model.token;

import java.util.Iterator;
import java.util.List;

/**
 * A specialized iterator for {@link Token}.
 */
public final class TokenIterator implements Iterator<Token> {

    private Token[] tokens;
    private int currentIdx;

    public TokenIterator(Token[] tokens) {
        this.tokens = tokens;
    }

    public TokenIterator(List<Token> tokens) {
        this.tokens = new Token[tokens.size()]; // needed?
        this.tokens = tokens.toArray(this.tokens);
    }

    public int getCurrentIdx() {
        return currentIdx;
    }

    public void setCurrentIdx(int currentIdx) {
        this.currentIdx = currentIdx;
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

    public int size() {
        return tokens.length;
    }
}
