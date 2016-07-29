package notpure.antlr4.macro.processor.parser.impl;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.token.TokenIterator;
import notpure.antlr4.macro.model.token.x.TokenTarget;
import notpure.antlr4.macro.processor.parser.ExpressionParser;

/**
 * Created by pure on 28/07/2016.
 */
public final class GrammarNameParser implements ExpressionParser {

    private static final TokenTarget TARGET_TOKEN = new TokenTarget(
            new Token[] { new Token(TokenDefinition.SEMICOLON) }, false);

    @Override
    public Expression parse(TokenIterator it) {
        it.skip(7); // skip 'grammar'

        // skip white space in between
        if (it.skipAllWhitespace() == 0) {
            // TODO throw error
        }
        String value = it.aggregateValues(TARGET_TOKEN, true, true);
        it.skip(1); // skip ';'
        return new Expression(ExpressionType.GRAMMAR_NAME, value);
    }

    @Override
    public boolean validate(TokenIterator it) {
        return it.hasNext("grammar");
    }
}
