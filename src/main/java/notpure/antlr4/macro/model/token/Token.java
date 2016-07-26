package notpure.antlr4.macro.model.token;

/**
 * A token.
 */
public final class Token {

    private final String name;
    private final String value;

    public Token(TokenDefinition def) {
        if (def.getValueType() == TokenDefinition.ValueType.REGEX) {
            throw new IllegalArgumentException("TokenDefinition has ValueType.REGEX and hence is invalid.");
        }
        this.name = def.name();
        this.value = def.getValue();
    }

    public Token(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            Token other = (Token) obj;
            return other.getName().equals(name)
                    && (value == null && other.getValue() == null
                    || value != null && other.getValue() != null && other.getValue().equals(value));
        }
        return false;
    }

    public boolean nameEquals(TokenDefinition def) {
        return def.name().equals(name);
    }

    @Override
    public String toString() {
        return "Token(" + name + "='" + value.trim() + "')";
    }
}
