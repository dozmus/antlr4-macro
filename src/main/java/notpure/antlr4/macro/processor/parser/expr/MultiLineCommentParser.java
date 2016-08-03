package notpure.antlr4.macro.processor.parser.expr;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.model.parser.ExpressionParser;
import notpure.antlr4.macro.model.parser.ParserException;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;

/**
 * Parses {@link ExpressionType#MULTI_LINE_COMMENT}.
 */
public final class MultiLineCommentParser implements ExpressionParser<Expression> {

    private static final TokenParserIterator.TokenTarget TARGET_TOKEN = new TokenParserIterator.TokenTarget(
            new Token[]{new Token(TokenDefinition.ASTERISK), new Token(TokenDefinition.FORWARD_SLASH)}, true);

    @Override
    public Expression parse(TokenParserIterator it) throws ParserException {
        it.skip(2); // skip '/*'
        String value;

        try {
            value = it.aggregateValues(TARGET_TOKEN, false, false);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException(getClass(), "Failed to parse multi-line comment, did not find ending symbol '*/'.");
        }
        it.skip(2); // skip '*/'
        return new Expression(ExpressionType.MULTI_LINE_COMMENT, new ExpressionValue(ExpressionValueType.RAW, value));
    }

    @Override
    public boolean validate(TokenParserIterator it) {
        return it.remaining() >= 2
                && it.peek().nameEquals(TokenDefinition.FORWARD_SLASH)
                && it.peek(1).nameEquals(TokenDefinition.ASTERISK);
    }
}
