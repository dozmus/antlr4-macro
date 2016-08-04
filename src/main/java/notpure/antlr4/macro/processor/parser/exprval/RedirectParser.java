package notpure.antlr4.macro.processor.parser.exprval;

import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.model.parser.ExpressionParser;
import notpure.antlr4.macro.model.parser.ParserException;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;
/**
 * Parses {@link ExpressionValueType#REDIRECT}.
 */
public final class RedirectParser implements ExpressionParser<ExpressionValue> {

    @Override
    public ExpressionValue parse(TokenParserIterator it) throws ParserException {
        it.skip(2); // skip '->'
        return new ExpressionValue(ExpressionValueType.REDIRECT, "->");
    }

    @Override
    public boolean validate(TokenParserIterator it) {
        return it.remaining() > 1 && it.peek().nameEquals(TokenDefinition.HYPHEN)
                && it.peek(1).nameEquals(TokenDefinition.GREATER_THAN);
    }
}
