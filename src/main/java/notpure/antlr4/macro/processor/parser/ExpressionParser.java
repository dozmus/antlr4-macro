package notpure.antlr4.macro.processor.parser;

import notpure.antlr4.macro.model.lang.Expression;

/**
 * Parses one expression.
 */
public interface ExpressionParser {

    Expression parse(TokenParserIterator it) throws ParserException;

    boolean validate(TokenParserIterator it);
}
