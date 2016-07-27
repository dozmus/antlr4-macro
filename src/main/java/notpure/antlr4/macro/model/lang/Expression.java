package notpure.antlr4.macro.model.lang;

/**
 * A parsed expression.
 */
public final class Expression {

    private final String identifier;
    private final String value;
    private final ExpressionType type;

    public Expression(ExpressionType type, String identifier, String value) {
        this.identifier = identifier;
        this.value = value;
        this.type = type;
    }

    public Expression(ExpressionType type, String value) {
        this(type, null, value);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getValue() {
        return value;
    }

    public ExpressionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.name() + "(" + (identifier == null ? "" : identifier + "=") + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Expression))
            return false;
        Expression other = (Expression) obj;
        return other.getType() == type && other.getValue().equals(value)
                && (other.getIdentifier() == null && identifier == null
                || other.getIdentifier() != null && identifier != null && other.getIdentifier().equals(identifier));
    }
}
