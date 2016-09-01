package notpure.antlr4.macro.util;

/**
 * A set of utility methods yielding String outputs.
 */
public final class StringHelper {

    /**
     * Formats the given char for printing by escaping escape sequences, i.e. '\n' -> "\\n".
     */
    public static String escape(char c) {
        switch (c) {
            case '\n':
                return "\\n";
            case '\r':
                return "\\r";
            case '\t':
                return "\\t";
            default:
                return c + "";
        }
    }
}
