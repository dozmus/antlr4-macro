package notpure.antlr4.macro.processor.parser;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.token.TokenIterator;

/**
 * Parses one expression.
 */
public interface ExpressionParser {

    Expression parse(TokenIterator it) throws ParserException;

    boolean validate(TokenIterator it);
}
