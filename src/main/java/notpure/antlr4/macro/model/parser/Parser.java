package notpure.antlr4.macro.model.parser;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lexer.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * A Parser - parses a {@link List} of {@link Token} into a {@link List} of {@link Expression}.
 */
public abstract class Parser {

    private final List<Expression> expressions = new ArrayList<>();

    public abstract Parser parse(List<Token> tokens);

    public List<Expression> getExpressions() {
        return expressions;
    }
}
