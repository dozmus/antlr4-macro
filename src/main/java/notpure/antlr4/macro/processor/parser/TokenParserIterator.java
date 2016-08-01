package notpure.antlr4.macro.processor.parser;

import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.model.lexer.token.TokenIterator;
import notpure.antlr4.macro.util.ArrayHelper;

import java.util.List;

/**
 * A specialized {@link TokenIterator} with functionality for parsing.
 */
public final class TokenParserIterator extends TokenIterator {

    public TokenParserIterator(Token[] tokens) {
        super(tokens);
    }

    public TokenParserIterator(List<Token> tokens) {
        super(tokens);
    }

    private static boolean isWhiteSpace(Token token) {
        return isNewLine(token) || token.nameEquals(TokenDefinition.SPACE);
    }

    private static boolean isNewLine(Token token) {
        return token.nameEquals(TokenDefinition.CARRIAGE_RETURN) || token.nameEquals(TokenDefinition.NEW_LINE);
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

    public String aggregateValues(TokenTarget target, boolean omitNewLines, boolean trimOutput)
            throws ArrayIndexOutOfBoundsException {
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
        throw new ArrayIndexOutOfBoundsException("Could not find token target.");
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

    public boolean skip(TokenDefinition semicolon) {
        if (peek().nameEquals(semicolon)) {
            next();
            return true;
        }
        return false;
    }

    /**
     * TokenTarget for {@link TokenParserIterator#aggregateValues(TokenTarget, boolean, boolean)}.
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
