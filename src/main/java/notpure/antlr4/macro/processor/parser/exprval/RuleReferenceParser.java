package notpure.antlr4.macro.processor.parser.exprval;

import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.model.parser.ExpressionParser;
import notpure.antlr4.macro.model.parser.ParserException;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;

/**
 * Created by pure on 02/08/2016.
 */
public final class RuleReferenceParser implements ExpressionParser<ExpressionValue> {

    private static final TokenParserIterator.TokenTarget RULE_REF_TARGET_TOKEN = new TokenParserIterator.TokenTarget(
            new Token[]{ new Token(TokenDefinition.SPACE), new Token(TokenDefinition.CARRIAGE_RETURN),
                    new Token(TokenDefinition.NEW_LINE), new Token(TokenDefinition.SEMICOLON)}, false);

    @Override
    public ExpressionValue parse(TokenParserIterator it) throws ParserException {
        // Parse value
        String value;

        try {
            value = it.aggregateValues(RULE_REF_TARGET_TOKEN, true, true);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException(getClass(), "Expected termination after rule reference");
        }

        // Parse further
        if (value.contains("=")) {
            String[] frag = value.split("=");
            return new ExpressionValue(ExpressionValueType.RULE_REFERENCE, frag[0], frag[1]);
        } else {
            return new ExpressionValue(ExpressionValueType.RULE_REFERENCE, value);
        }
    }

    @Override
    public boolean validate(TokenParserIterator it) {
        return it.hasNext() && (it.peek().nameEquals(TokenDefinition.LETTER)
                || it.peek().nameEquals(TokenDefinition.HASH));
    }
}
