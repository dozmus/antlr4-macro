package notpure.antlr4.macro.model.token.x;

import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenIterator;

/**
 * TokenTarget for {@link TokenIterator#aggregateValues(TokenTarget, boolean, boolean)}.
 */
public final class TokenTarget {

    private Token[] tokens;
    private boolean consecutiveMatch;

    public TokenTarget(Token[] tokens, boolean consecutiveMatch) {
        this.tokens = tokens;
        this.consecutiveMatch = consecutiveMatch;
    }

    public TokenTarget(Token token, boolean consecutiveMatch) {
        this(new Token[] { token }, consecutiveMatch);
    }

    public Token[] getTokens() {
        return tokens;
    }

    public boolean isConsecutiveMatch() {
        return consecutiveMatch;
    }
}
