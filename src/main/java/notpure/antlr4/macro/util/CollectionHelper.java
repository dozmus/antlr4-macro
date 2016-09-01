package notpure.antlr4.macro.util;

import notpure.antlr4.macro.model.lang.ExpressionValue;

import java.util.List;

/**
 * Collection utility methods.
 */
public final class CollectionHelper {

    /**
     * Linear-search implementation, uses {@link CollectionHelper#search(Object[], Object)}.
     * @return True if target is included in the source array.
     */
    public static <T> boolean arrayContains(T[] src, T target) {
        return search(src, target) != -1;
    }

    /**
     * Linear-search implementation.
     */
    public static <T> int search(T[] src, T target) {
        for (int i = 0; i < src.length; i++) {
            if (src[i].equals(target))
                return i;
        }
        return -1;
    }

    /**
     * Constructs a String representation of an array and its values.
     */
    public static <T> String toString(String arrayName, T[] targets) {
        final int n = targets.length;
        StringBuilder sb = new StringBuilder();
        sb.append(arrayName).append(" { ");

        for (int i = 0; i < n; i++) {
            sb.append(targets[i].toString());

            if (i + 1 < n)
                sb.append(",");
            sb.append(" ");
        }
        return sb.append("}").toString();
    }

    /**
     * Constructs a String representation of an array and its values.
     */
    public static <T> String toString(T[] targets) {
        return toString(targets.getClass().getSimpleName(), targets);
    }

    /**
     * Constructs a String representation of a {@link List} and its values.
     */
    public static <T> String toString(List<T> values) {
        return toString(values.toArray());
    }

    /**
     * Constructs a String representation of a {@link List} and its values.
     */
    public static <T> String toString(String arrayName, List<T> values) {
        return toString(arrayName, values.toArray());
    }

    public static String toAntlr4String(List<ExpressionValue> values) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i).toAntlr4String());

            if (i + 1 < values.size())
                sb.append(" ");
        }
        return sb.toString();
    }
}
