package notpure.antlr4.macro.processor.parser.exprval;

import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.parser.ExpressionParser;
import notpure.antlr4.macro.model.parser.ParserException;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;

/**
 * Parses {@link ExpressionValueType#ALTERNATOR}.
 */
public final class AlternatorParser implements ExpressionParser<ExpressionValue> {

    @Override
    public ExpressionValue parse(TokenParserIterator it) throws ParserException {
        return new ExpressionValue(ExpressionValueType.ALTERNATOR, it.next().getValue());
    }

    @Override
    public boolean validate(TokenParserIterator it) {
        return it.hasNext() && it.peek().nameEquals(TokenDefinition.VERTICAL_LINE);
    }
}
