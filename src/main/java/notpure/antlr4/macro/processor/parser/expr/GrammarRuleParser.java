package notpure.antlr4.macro.processor.parser.expr;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.model.parser.ExpressionParser;
import notpure.antlr4.macro.model.parser.ParserException;
import notpure.antlr4.macro.processor.parser.ExpressionValueParser;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A master class for all grammar rules.
 */
public abstract class GrammarRuleParser implements ExpressionParser<Expression> {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarRuleParser.class);
    private static final TokenParserIterator.TokenTarget RULE_IDENTIFIER_TARGET_TOKEN = new TokenParserIterator.TokenTarget(
            new Token[]{new Token(TokenDefinition.COLON)}, false);

    private final ExpressionType type;

    public GrammarRuleParser(ExpressionType type) {
        this.type = type;
    }

    @Override
    public Expression parse(TokenParserIterator it) throws ParserException {
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
        List<ExpressionValue> values = ExpressionValueParser.parse(it);

        if (values == null || values.size() == 0) {
            throw new ParserException(getClass(), "Expected value(s) after grammar rule identifier of type " + type);
        }
        it.skip(TokenDefinition.SEMICOLON);

        // Construct expression
        Expression expr = new Expression(type, identifier, values);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Parsed: {}", expr);
        }
        return expr;
    }

    @Override
    public abstract boolean validate(TokenParserIterator it);

    /**
     * Parses {@link ExpressionType#PARSER_RULE}.
     */
    public static final class ParserRuleParser extends GrammarRuleParser {

        public ParserRuleParser() {
            super(ExpressionType.PARSER_RULE);
        }

        @Override
        public boolean validate(TokenParserIterator it) {
            return it.hasNext() && it.peek().getValue() != null && Character.isLowerCase(it.peek().getValue().charAt(0));
        }
    }

    /**
     * Parses {@link ExpressionType#MACRO_RULE}.
     */
    public static final class MacroRuleParser extends GrammarRuleParser {

        public MacroRuleParser() {
            super(ExpressionType.MACRO_RULE);
        }

        @Override
        public boolean validate(TokenParserIterator it) {
            return it.hasNext() && it.peek().nameEquals(TokenDefinition.HASH);
        }
    }

    /**
     * Parses {@link ExpressionType#LEXER_RULE}.
     */
    public static final class LexerRuleParser extends GrammarRuleParser {

        public LexerRuleParser() {
            super(ExpressionType.LEXER_RULE);
        }

        @Override
        public boolean validate(TokenParserIterator it) {
            return it.hasNext() && it.peek().getValue() != null && Character.isUpperCase(it.peek().getValue().charAt(0));
        }
    }
}
