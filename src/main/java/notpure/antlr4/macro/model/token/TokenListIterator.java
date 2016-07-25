package notpure.antlr4.macro.model.token;

import java.util.List;

/**
 * A simple token list iterator, to be used while parsing tokens.
 */
public final class TokenListIterator {

    private final List<Token> tokens;
    private int currentIndex = -1;

    public TokenListIterator(List<Token> tokens) {
        if (tokens == null)
            throw new IllegalArgumentException("Token list is null.");
        this.tokens = tokens;
    }

    public boolean hasNext() {
        return currentIndex + 1 < tokens.size();
    }

    public Token next() {
        return tokens.get(++currentIndex);
    }

    public Token peek(int offset) {
        if (currentIndex + offset < tokens.size()) {
            return tokens.get(currentIndex + offset);
        } else {
            throw new ArrayIndexOutOfBoundsException("The offset provided was too large.");
        }
    }

    public Token peek() {
        return peek(1);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
