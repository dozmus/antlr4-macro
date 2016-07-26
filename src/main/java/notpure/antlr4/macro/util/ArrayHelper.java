package notpure.antlr4.macro.util;

import notpure.antlr4.macro.model.token.Token;

/**
 * Array utility methods.
 */
public final class ArrayHelper {

    /**
     * Linear-search implementation.
     */
    public static <T> boolean arrayContains(T[] src, T target) {
        for (T t : src)
            if (t.equals(target))
                return true;
        return false;
    }

    /**
     * Constructs a String representation of a {@link Token} array and its values.
     * @param targets
     * @return
     */
    public static String toString(Token[] targets) {
        final int n = targets.length;
        StringBuilder sb = new StringBuilder();
        sb.append("Token[] { ");

        for (int i = 0; i < n; i++) {
            sb.append(targets[i].toString());

            if (i + 1 < n)
                sb.append(",");
            sb.append(" ");
        }

        return sb.append("}").toString();
    }
}
