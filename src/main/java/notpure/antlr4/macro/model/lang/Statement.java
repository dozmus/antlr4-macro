package notpure.antlr4.macro.model.lang;

/**
 * A parsed statement.
 */
public final class Statement {

    private final String identifier;
    private final String value;
    private final StatementType type;

    public Statement(StatementType type, String identifier, String value) {
        this.identifier = identifier;
        this.value = value;
        this.type = type;
    }

    public Statement(StatementType type, String value) {
        this(type, null, value);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getValue() {
        return value;
    }

    public StatementType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.name() + "(" + (identifier == null ? "" : identifier + "=") + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Statement))
            return false;
        Statement other = (Statement) obj;
        return other.getType() == type && other.getValue().equals(value)
                && (other.getIdentifier() == null && identifier == null
                || other.getIdentifier() != null && identifier != null && other.getIdentifier().equals(identifier));
    }
}
