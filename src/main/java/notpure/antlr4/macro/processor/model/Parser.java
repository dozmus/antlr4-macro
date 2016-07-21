package notpure.antlr4.macro.processor.model;

import notpure.antlr4.macro.processor.model.statement.Statement;
import notpure.antlr4.macro.processor.model.token.Token;

import java.util.List;

/**
 * A Parser - converts a {@link List} of {@link Token} into a {@link List} of {@link Statement}.
 */
public interface Parser {

    Parser parse(List<Token> tokens);

    List<Statement> getStatements();
}
