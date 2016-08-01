package notpure.antlr4.macro.model.parser;

/**
 * A parser exeception.
 */
public final class ParserException extends Exception {

    public ParserException(Class<? extends ExpressionParser> clazz, String message) {
        super(clazz.getSimpleName() + ": " + message);
    }
}
