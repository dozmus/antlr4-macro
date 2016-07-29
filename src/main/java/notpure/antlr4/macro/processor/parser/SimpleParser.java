package notpure.antlr4.macro.processor.parser;

import notpure.antlr4.macro.model.Parser;
import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenIterator;
import notpure.antlr4.macro.processor.parser.impl.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses tokens into statements.
 */
public final class SimpleParser extends Parser {

    private final List<ExpressionParser> parsers = new ArrayList<>();

    public SimpleParser() {
        initParsers();
    }

    @Override
    public Parser parse(List<Token> tokens) {
        if (tokens == null)
            throw new IllegalArgumentException("Token list is null.");

        // Parse tokens
        TokenIterator it = new TokenIterator(tokens);

        while (it.hasNext()) {
            ExpressionParser parser = getParser(it);

            if (parser != null) {
                Expression expr = parser.parse(it);

                if (expr != null)
                    getExpressions().add(expr);
            } else {
                it.next(); // consume unhandled token
            }
        }
        return this;
    }

    private void initParsers() {
        parsers.add(new GrammarNameParser());
        parsers.add(new SingleLineCommentParser());
        parsers.add(new MultiLineCommentParser());
        parsers.add(new LexerRuleParser());
        parsers.add(new ParserRuleParser());
        parsers.add(new MacroRuleParser());
    }

    private ExpressionParser getParser(TokenIterator it) {
        for (ExpressionParser parser : parsers) {
            if (parser.validate(it)) {
                return parser;
            }
        }
        return null;
    }
}
