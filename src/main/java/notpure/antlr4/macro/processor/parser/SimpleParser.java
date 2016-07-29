package notpure.antlr4.macro.processor.parser;

import notpure.antlr4.macro.model.Parser;
import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.processor.parser.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses tokens into statements.
 */
public final class SimpleParser extends Parser {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GrammarRuleParser.class);
    private final List<ExpressionParser> parsers = new ArrayList<>();
    private boolean errorParsing;

    public SimpleParser() {
        initParsers();
    }

    @Override
    public Parser parse(List<Token> tokens) {
        if (tokens == null)
            throw new IllegalArgumentException("Token list is null.");

        // Parse tokens
        TokenParserIterator it = new TokenParserIterator(tokens);

        while (it.hasNext()) {
            ExpressionParser parser = getParser(it);

            if (parser != null) {
                Expression expr;

                try {
                     expr = parser.parse(it);
                } catch (ParserException e) {
                    LOGGER.error("Error parsing: {}", e.getMessage());
                    e.printStackTrace();
                    errorParsing = true;
                    return this;
                }

                if (expr != null)
                    getExpressions().add(expr);
            } else {
                it.next(); // consume unhandled token
            }
        }
        return this;
    }

    private void initParsers() {
        parsers.add(new MultiLineCommentParser());
        parsers.add(new SingleLineCommentParser());
        parsers.add(new GrammarNameParser());
        parsers.add(new LexerRuleParser());
        parsers.add(new ParserRuleParser());
        parsers.add(new MacroRuleParser());
    }

    private ExpressionParser getParser(TokenParserIterator it) {
        for (ExpressionParser parser : parsers) {
            if (parser.validate(it)) {
                return parser;
            }
        }
        return null;
    }

    public boolean errorOccurredParsing() {
        return errorParsing;
    }
}
