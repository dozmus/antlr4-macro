package notpure.antlr4.macro.model.parser;

/**
 * A parser exception.
 */
public final class ParserException extends Exception {

    public ParserException(Class clazz, String message) {
        super(clazz.getSimpleName() + ": " + message);
    }
}
