package notpure.antlr4.macro.processor.model;

import notpure.antlr4.macro.processor.model.statement.Statement;
import notpure.antlr4.macro.processor.model.token.Token;

import java.util.List;

/**
 * Created by pure on 17/07/2016.
 */
public interface Parser {

    Parser parse(List<Token> tokens);

    List<Statement> getStatements();
}
