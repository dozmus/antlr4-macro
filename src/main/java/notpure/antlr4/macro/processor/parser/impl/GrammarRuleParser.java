package notpure.antlr4.macro.processor.parser.impl;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.token.TokenIterator;
import notpure.antlr4.macro.processor.parser.ExpressionParser;
import notpure.antlr4.macro.processor.parser.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A master class for all grammar rules.
 */
public abstract class GrammarRuleParser implements ExpressionParser {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarRuleParser.class);
    private static final TokenIterator.TokenTarget RULE_IDENTIFIER_TARGET_TOKEN = new TokenIterator.TokenTarget(
            new Token[]{new Token(TokenDefinition.COLON)}, false);
    private static final TokenIterator.TokenTarget RULE_VALUE_TARGET_TOKEN = new TokenIterator.TokenTarget(
            new Token[]{new Token(TokenDefinition.SEMICOLON)}, false);

    private final ExpressionType type;

    public GrammarRuleParser(ExpressionType type) {
        this.type = type;
    }

    @Override
    public Expression parse(TokenIterator it) throws ParserException {
        // Skip hash if macro rule
        if (type == ExpressionType.MACRO_RULE)
            it.skip(TokenDefinition.HASH); // skip '#'

        // Parse identifier
        String identifier;

        try {
            identifier = it.aggregateValues(RULE_IDENTIFIER_TARGET_TOKEN, true, true);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException(getClass(), "Expected colon after grammar rule identifier of type " + type);
        }
        it.skipAllWhitespace();
        it.skip(TokenDefinition.COLON);
        it.skipAllWhitespace();

        // Parse value
        String value;

        try {
            value = it.aggregateValues(RULE_VALUE_TARGET_TOKEN, true, true);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException(getClass(), "Expected semi-colon after grammar rule value of type " + type);
        }
        it.skipAllWhitespace();
        it.skip(TokenDefinition.SEMICOLON);

        // Construct expression
        Expression expr = new Expression(type, identifier, value);
        LOGGER.info("Parsed {}", expr);
        return expr;
    }

    @Override
    public abstract boolean validate(TokenIterator it);
}
