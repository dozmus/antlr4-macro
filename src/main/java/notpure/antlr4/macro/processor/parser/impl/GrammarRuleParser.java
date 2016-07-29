package notpure.antlr4.macro.processor.parser.impl;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.token.TokenIterator;
import notpure.antlr4.macro.processor.parser.ExpressionParser;
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
    public Expression parse(TokenIterator it) {
        // skip hash if macro rule
        if (type == ExpressionType.MACRO_RULE)
            it.skip(1); // skip '#'

        String identifier = it.aggregateValues(RULE_IDENTIFIER_TARGET_TOKEN, true, true);
        it.skipAllWhitespace();
        it.skip(1); // skip ':'
        it.skipAllWhitespace();
        String value = it.aggregateValues(RULE_VALUE_TARGET_TOKEN, true, true);
        it.skipAllWhitespace();
        it.skip(1); // skip ';'

        Expression expr = new Expression(type, identifier, value);
        LOGGER.info("Parsed {}", expr);
        return expr;
    }

    @Override
    public abstract boolean validate(TokenIterator it);
}
