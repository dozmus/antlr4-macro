package notpure.antlr4.macro.util;

/**
 * Created by pure on 29/07/2016.
 */
public final class StringHelper {

    /**
     * Formats the given char for printing.
     * @return
     */
    public static String toPretty(char c) {
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
