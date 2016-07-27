package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.model.Parser;
import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.util.ArrayHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A simple LL(*) parser.
 */
public final class SimpleParser extends Parser {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleParser.class);
    /**
     * The grammar name text.
     */
    private static final String GRAMMAR_NAME_TEXT = "grammar";

    /**
     * Parses the tokens into more complicated ones.
     */
    @Override
    public Parser parse(List<Token> tokens) {
        if (tokens == null)
            throw new IllegalArgumentException("Token list is null.");

        for (int idx = 0; idx < tokens.size(); idx++) {
            Token token = tokens.get(idx);
            boolean hasNext = idx + 1 < tokens.size();
            Token nextToken = hasNext ? tokens.get(idx + 1) : null;

            if (token.nameEquals(TokenDefinition.EOF))
                continue;

            // TODO impl/finish
            final char c = token.getValue().charAt(0);
            LOGGER.info("Current char: '{}', hasNext: {}, nextValue: {}", c, hasNext,
                    nextToken == null ? "null" : nextToken.getValue());

            if (c == '/' && hasNext && nextToken.getValue().equals("/")) { // single-line comment
                idx += 2; // skip '//'
                idx = parseSingleLineComment(tokens, idx);
            } else if (c == '/' && hasNext && nextToken.getValue().equals("*")) { // multi-line comment
                idx += 2; // skip '/*'
                idx = parseMultiLineComment(tokens, idx);
            } else if (c == '#') { // macro rule
                idx = parseStatement(tokens, idx, 1, ExpressionType.MACRO_RULE);
            } else if (Character.isLowerCase(c)) {
                if (seekString(tokens, idx, GRAMMAR_NAME_TEXT)) { // grammar header file
                    idx += GRAMMAR_NAME_TEXT.length(); // skip header text
                    idx = parseFileHeaderStatement(tokens, idx) - 1;
                } else { // parser rule
                    idx = parseStatement(tokens, idx, (idx > 4 ? -4 : 0), ExpressionType.PARSER_RULE) - 1;
                }
            } else if (Character.isUpperCase(c)) { // lexer rule
                idx = parseStatement(tokens, idx, (idx > 1 ? -1 : 0), ExpressionType.LEXER_RULE) - 1;
            }
        }
        return this;
    }

    private int parseSingleLineComment(List<Token> tokens, int idx) {
        return parseCustomStatement(tokens, idx,
                new TokenTarget(new Token[] { new Token(TokenDefinition.NEW_LINE),
                        new Token(TokenDefinition.EOF) }, false), ExpressionType.SINGLE_LINE_COMMENT);
    }

    private int parseMultiLineComment(List<Token> tokens, int idx) {
        return parseCustomStatement(tokens, idx,
                new TokenTarget(new Token[] { new Token(TokenDefinition.ASTERISK),
                        new Token(TokenDefinition.FORWARD_SLASH) }, true), ExpressionType.MULTI_LINE_COMMENT);
    }

    private int parseFileHeaderStatement(List<Token> tokens, int idx) {
        return parseCustomStatement(tokens, idx, new TokenTarget(new Token(TokenDefinition.SEMICOLON), false),
                ExpressionType.GRAMMAR_NAME);
    }

    private int parseCustomStatement(List<Token> tokens, int idx, TokenTarget tokenTarget, ExpressionType expressionType) {
        ParsedToken pair = seekToken(tokens, idx, tokenTarget, expressionType.name(), true);
        LOGGER.info("Parsed {}: {} [nextIdx={}]", expressionType, pair.getToken(), pair.getNextIdx());
        getExpressions().add(new Expression(expressionType, pair.getToken().getValue()));
        return pair.getNextIdx();
    }

    private int parseStatement(List<Token> tokens, int idx, int offset, ExpressionType type) {
        // Parse identifier
        ParsedToken identifier = seekToken(tokens, idx + offset,
                new TokenTarget(new Token(TokenDefinition.COLON), false), type.name() + "_Identifier", true);
        LOGGER.info("Parsed identifier for {}: {} [nextIdx={}]", type, identifier.getToken(), identifier.getNextIdx());
        idx = identifier.getNextIdx();

        // Parse value
        ParsedToken value = seekToken(tokens, idx, new TokenTarget(
                new Token(TokenDefinition.SEMICOLON), false), type.name() + "_Value", true);
        LOGGER.info("Parsed value for {}: {} [nextIdx={}]", type, value.getToken(), value.getNextIdx());
        idx = value.getNextIdx();

        // Add to statements list and continue
        Expression expression = fromParsedToken(identifier, value, type);
        getExpressions().add(expression);
        LOGGER.info("Parsed statement {}: {}={}", type, expression.getIdentifier(), expression.getValue());
        return idx;
    }

    private static Expression fromParsedToken(ParsedToken identifier, ParsedToken value, ExpressionType type) {
        return new Expression(type, identifier.getToken().getValue(), value.getToken().getValue());
    }

    private static boolean seekString(List<Token> tokens, int currentIdx, String target) {
        String outputValue = "";

        for (int i = currentIdx; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (outputValue.length() > target.length()) {
                return false;
            } else if (outputValue.length() == target.length()) {
                return outputValue.equals(target);
            } else {
                if (token.nameEquals(TokenDefinition.LETTER) || token.nameEquals(TokenDefinition.DIGIT)) {
                    outputValue += token.getValue();
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private static ParsedToken seekToken(List<Token> tokens, int currentIdx, TokenTarget target, String tokenName,
                                         boolean trimOutput) {
        String outputValue = "";
        Token[] targets = target.getTokens();

        for (int idx = currentIdx; idx < tokens.size(); idx++) {
            Token token = tokens.get(idx);
            String value = token.getValue();

            // TODO make sure this is all right
            if (!target.isConsecutive() && !ArrayHelper.arrayContains(targets, token)) {
                if (value != null)
                    outputValue += value.equals("\n") || value.equals("\r") ? "" : value;
            } else if (target.isConsecutive() && !tokensContainsConsecutively(tokens, targets, idx)) {
                if (value != null)
                    outputValue += value.equals("\n") || value.equals("\r") ? "" : value;
            } else {
                return new ParsedToken(new Token(tokenName, trimOutput ? outputValue.trim() : outputValue), idx + 1);
            }
        }

        // Throw exception
        String seekType = target.isConsecutive() ? "consecutive" : "any of";
        throw new IndexOutOfBoundsException("Unable to find " + seekType + " target token"
                + (targets.length > 1 ? "s" : "") + ": " + ArrayHelper.toString(targets) + " while seeking for " + tokenName);
    }

    private static boolean tokensContainsConsecutively(List<Token> tokens, Token[] targets, int idx) {
        for (int i = idx; i < tokens.size() - targets.length; i++) {
            for (int offset = 0; offset < targets.length; offset++) {
                if (!tokens.get(i + offset).equals(targets[offset])) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * A parsed token.
     */
    private static class ParsedToken {

        private final int nextIdx;
        private final Token token;

        public ParsedToken(Token token, int nextIdx) {
            this.token = token;
            this.nextIdx = nextIdx;
        }

        public int getNextIdx() {
            return nextIdx;
        }

        public Token getToken() {
            return token;
        }
    }

    /**
     * A parameter for {@link SimpleParser#seekToken(List, int, TokenTarget, String, boolean)}.
     */
    private static class TokenTarget {

        private final Token[] tokens;
        private final boolean consecutive;

        public TokenTarget(Token token, boolean consecutive) {
            this(new Token[] { token }, consecutive);
        }

        public TokenTarget(Token[] tokens, boolean consecutive) {
            this.tokens = tokens;
            this.consecutive = consecutive;
        }

        public Token[] getTokens() {
            return tokens;
        }

        public boolean isConsecutive() {
            return consecutive;
        }
    }
}
