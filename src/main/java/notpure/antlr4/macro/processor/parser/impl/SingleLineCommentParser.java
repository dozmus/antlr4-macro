package notpure.antlr4.macro.processor.parser.impl;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.token.TokenIterator;
import notpure.antlr4.macro.model.token.TokenTarget;
import notpure.antlr4.macro.processor.parser.ExpressionParser;

/**
 * Created by pure on 28/07/2016.
 */
public final class SingleLineCommentParser implements ExpressionParser {

    private static final TokenTarget TARGET_TOKEN = new TokenTarget(
            new Token[]{new Token(TokenDefinition.EOF), new Token(TokenDefinition.NEW_LINE)}, false);

    @Override
    public Expression parse(TokenIterator it) {
        it.skip(2); // skip '//'
        String value = it.aggregateValues(TARGET_TOKEN, true, true);
        return new Expression(ExpressionType.SINGLE_LINE_COMMENT, value);
    }

    @Override
    public boolean validate(TokenIterator it) {
        return it.remaining() >= 2
                && it.peek().nameEquals(TokenDefinition.FORWARD_SLASH)
                && it.peek(1).nameEquals(TokenDefinition.FORWARD_SLASH);
    }
}
