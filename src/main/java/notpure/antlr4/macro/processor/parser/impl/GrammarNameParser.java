package notpure.antlr4.macro.processor.parser.impl;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.token.TokenIterator;
import notpure.antlr4.macro.processor.parser.ExpressionParser;
import notpure.antlr4.macro.processor.parser.ParserException;

/**
 * Created by pure on 28/07/2016.
 */
public final class GrammarNameParser implements ExpressionParser {

    private static final String GRAMMAR_TEXT = "grammar";
    private static final TokenIterator.TokenTarget TARGET_TOKEN = new TokenIterator.TokenTarget(
            new Token[]{new Token(TokenDefinition.SEMICOLON)}, false);

    @Override
    public Expression parse(TokenIterator it) throws ParserException {
        it.skip(GRAMMAR_TEXT.length()); // skip 'grammar'

        // Skip white space in between, need at least one
        if (it.skipAllWhitespace() == 0) {
            throw new ParserException(getClass(), "Expected whitespace between 'grammar' and its name.");
        }
        String value;

        try {
            value = it.aggregateValues(TARGET_TOKEN, true, true);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException(getClass(), "Expected semi-colon after grammar name.");
        }

        if (!it.skip(TokenDefinition.SEMICOLON)) {
            throw new ParserException(getClass(), "Expected semi-colon after grammar name.");
        }
        return new Expression(ExpressionType.GRAMMAR_NAME, value);
    }

    @Override
    public boolean validate(TokenIterator it) {
        return it.hasNext(GRAMMAR_TEXT);
    }
}
