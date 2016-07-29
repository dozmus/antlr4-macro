package notpure.antlr4.macro.model.token;

import notpure.antlr4.macro.util.ArrayHelper;

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
        this.tokens = new Token[tokens.size()];
        this.tokens = tokens.toArray(this.tokens);
    }

    private static boolean isWhiteSpace(Token token) {
        return isNewLine(token) || token.nameEquals(TokenDefinition.SPACE);
    }

    private static boolean isNewLine(Token token) {
        return token.nameEquals(TokenDefinition.CARRIAGE_RETURN) || token.nameEquals(TokenDefinition.NEW_LINE);
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

    public boolean hasNext(String value) {
        if (value == null || remaining() < value.length())
            return false;

        String aggregatedValue = "";

        for (int i = currentIdx; i < currentIdx + value.length(); i++) {
            aggregatedValue += tokens[i].getValue();
        }
        return aggregatedValue.equals(value);
    }

    public int skipAllWhitespace() {
        int amt = 0;

        while (hasNext()) {
            if (isWhiteSpace(peek())) {
                next();
                amt++;
            } else {
                return amt;
            }
        }
        return amt;
    }

    public String aggregateValues(TokenTarget target, boolean omitNewLines, boolean trimOutput) {
        String value = "";

        while (hasNext()) {
            // Check if time to stop
            if (anyMatch(target) || consecutiveMatch(target)) {
                return trimOutput ? value.trim() : value;
            } else { // else aggregate
                Token token = next();

                if (omitNewLines && !isNewLine(token))
                    value += token.getValue();
                else if (!omitNewLines)
                    value += token.getValue();
            }
        }
        return null; // TODO throw exception
    }

    private boolean anyMatch(TokenTarget target) {
        return !target.isConsecutiveMatch() && ArrayHelper.arrayContains(target.getTokens(), peek());
    }

    private boolean consecutiveMatch(TokenTarget target) {
        if (!target.isConsecutiveMatch() || remaining() < target.getTokens().length)
            return false;

        for (int i = 0; i < target.getTokens().length; i++) {
            if (!peek(i).equals(target.getTokens()[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * TokenTarget for {@link TokenIterator#aggregateValues(TokenTarget, boolean, boolean)}.
     */
    public static final class TokenTarget {

        private Token[] tokens;
        private boolean consecutiveMatch;

        public TokenTarget(Token[] tokens, boolean consecutiveMatch) {
            this.tokens = tokens;
            this.consecutiveMatch = consecutiveMatch;
        }

        public TokenTarget(Token token, boolean consecutiveMatch) {
            this(new Token[]{token}, consecutiveMatch);
        }

        public Token[] getTokens() {
            return tokens;
        }

        public boolean isConsecutiveMatch() {
            return consecutiveMatch;
        }
    }
}
