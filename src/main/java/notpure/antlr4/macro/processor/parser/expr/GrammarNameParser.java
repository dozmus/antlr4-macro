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
 * Parses {@link ExpressionType#GRAMMAR_NAME}.
 */
public final class GrammarNameParser implements ExpressionParser<Expression> {

    private static final String GRAMMAR_TEXT = "grammar";
    private static final TokenParserIterator.TokenTarget TARGET_TOKEN = new TokenParserIterator.TokenTarget(
            new Token[]{new Token(TokenDefinition.SEMICOLON)}, false);

    @Override
    public Expression parse(TokenParserIterator it) throws ParserException {
        it.skip(GRAMMAR_TEXT.length()); // skip 'grammar'

        // Skip white space in between identifier and value, need at least one
        if (it.skipAllWhitespace() == 0) {
            throw new ParserException(getClass(), "Expected whitespace between 'grammar' and its name.");
        }

        // Parse value
        String value;

        try {
            value = it.aggregateValues(TARGET_TOKEN, true, true);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException(getClass(), "Expected semi-colon after grammar name.");
        }

        if (value.trim().length() == 0) {
            throw new ParserException(getClass(), "Expected value after grammar name.");
        }
        it.skip(TokenDefinition.SEMICOLON);
        return new Expression(ExpressionType.GRAMMAR_NAME, new ExpressionValue(ExpressionValueType.RAW, value));
    }

    @Override
    public boolean validate(TokenParserIterator it) {
        return it.hasNext(GRAMMAR_TEXT);
    }
}
