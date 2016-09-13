package notpure.antlr4.macro.processor.parser.exprval;

import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.parser.ExpressionParser;
import notpure.antlr4.macro.model.parser.ParserException;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;

/**
 * Created by pure on 04/08/2016.
 */
public final class RegexGroupParser implements ExpressionParser<ExpressionValue> {

    private static final TokenParserIterator.TokenTarget TARGET_TOKEN = new TokenParserIterator.TokenTarget(
            new Token[]{new Token(TokenDefinition.RIGHT_SQUARE_BRACKET)}, false);

    @Override
    public ExpressionValue parse(TokenParserIterator it) throws ParserException {
        it.skip(1); // skip '['
        String value = "[";

        try {
            value += it.aggregateValues(TARGET_TOKEN, false, false);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException(getClass(), "Failed to parse regex group, did not find ending symbol `]`.");
        }
        it.skip(1); // skip ']'
        value += "]";
        return new ExpressionValue(ExpressionValueType.REGEX_GROUP, value);
    }

    @Override
    public boolean validate(TokenParserIterator it) {
        return it.remaining() > 1 && it.peek().nameEquals(TokenDefinition.LEFT_SQUARE_BRACKET);
    }
}