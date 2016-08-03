package notpure.antlr4.macro.util;

import notpure.antlr4.macro.model.lang.ExpressionValue;

import java.util.List;

/**
 * Array utility methods.
 */
public final class ArrayHelper {

    /**
     * Linear-search implementation, uses {@link ArrayHelper#find(Object[], Object)}.
     * @return True if target is included in the source array.
     */
    public static <T> boolean arrayContains(T[] src, T target) {
        return find(src, target) != -1;
    }

    /**
     * Linear-search implementation.
     */
    public static <T> int find(T[] src, T target) {
        for (int i = 0; i < src.length; i++) {
            if (src[i].equals(target))
                return i;
        }
        return -1;
    }

    /**
     * Constructs a String representation of an array and its values.
     */
    public static <T> String toString(T[] targets) {
        final int n = targets.length;
        StringBuilder sb = new StringBuilder();
        sb.append(targets.getClass().getSimpleName()).append(" { ");

        for (int i = 0; i < n; i++) {
            sb.append(targets[i].toString());

            if (i + 1 < n)
                sb.append(",");
            sb.append(" ");
        }
        return sb.append("}").toString();
    }

    /**
     * Constructs a String representation of a {@link List} and its values.
     */
    public static String toString(List<ExpressionValue> values) {
        return toString(values.toArray());
    }
}
