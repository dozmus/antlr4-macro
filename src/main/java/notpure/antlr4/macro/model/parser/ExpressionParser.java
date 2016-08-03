package notpure.antlr4.macro.model.parser;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;

/**
 * Parses one {@link Expression} or {@link ExpressionValue}.
 */
public interface ExpressionParser<T> {

    T parse(TokenParserIterator it) throws ParserException;

    boolean validate(TokenParserIterator it);
}
