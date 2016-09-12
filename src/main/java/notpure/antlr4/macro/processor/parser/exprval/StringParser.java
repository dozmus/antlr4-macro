package notpure.antlr4.macro.processor.parser.exprval;

import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.parser.ExpressionParser;
import notpure.antlr4.macro.model.parser.ParserException;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;

/**
 * Created by pure on 02/08/2016.
 */
public final class StringParser implements ExpressionParser<ExpressionValue> {

    @Override
    public ExpressionValue parse(TokenParserIterator it) throws ParserException {
        String quoteName = it.next().getName();
        String value;

        try {
            value = it.aggregateValues(new TokenParserIterator.TokenTarget(
                    new Token(TokenDefinition.forName(quoteName))), false, false);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException(getClass(), "Expected string termination");
        }
        it.skip(1); // skip end of quote token
        return new ExpressionValue(ExpressionValueType.STRING, value);
    }

    @Override
    public boolean validate(TokenParserIterator it) {
        return it.remaining() >= 2 && (it.peek().nameEquals(TokenDefinition.SINGLE_QUOTE)
                || it.peek().nameEquals(TokenDefinition.DOUBLE_QUOTE));
    }
}
