package notpure.antlr4.macro.model.lang;

import java.util.List;

/**
 * The value of an {@link Expression}.
 */
public final class ExpressionValue {

    private final ExpressionValueType type;
    private final String identifier;
    private final String value;

    public ExpressionValue(ExpressionValueType type, String value) {
        this(type, null, value);
    }

    public ExpressionValue(ExpressionValueType type, String identifier, String value) {
        this.type = type;
        this.identifier = identifier;
        this.value = value;
    }

    public ExpressionValueType getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type.name() + "(" + (identifier == null ? "" : identifier + "=") + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ExpressionValue) {
            ExpressionValue other = (ExpressionValue)obj;
            return other.getType() == type
                    // comparing value
                    && other.getValue() != null && value != null && other.getValue().equals(value)
                    // comparing identifier
                    && (other.getIdentifier() == null && identifier == null
                    || other.getIdentifier() != null && identifier != null && other.getIdentifier().equals(identifier));
        }
        return false;
    }

    public static boolean equals(List<ExpressionValue> v1, List<ExpressionValue> v2) {
        // compare size
        if (v1.size() != v2.size())
            return false;

        // compare elements
        for (int i = 0; i < v1.size(); i++) {
            if (!v1.get(i).equals(v2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
