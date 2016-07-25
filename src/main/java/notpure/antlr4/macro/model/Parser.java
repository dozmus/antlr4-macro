package notpure.antlr4.macro.model;

import notpure.antlr4.macro.model.lang.Statement;
import notpure.antlr4.macro.model.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * A Parser - parses a {@link List} of {@link Token} into a {@link List} of {@link Statement}.
 */
public abstract class Parser {

    private final List<Statement> statements = new ArrayList<>();

    public abstract Parser parse(List<Token> tokens);

    public List<Statement> getStatements() {
        return statements;
    }
}
