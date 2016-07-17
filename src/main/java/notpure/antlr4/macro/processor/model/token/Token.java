package notpure.antlr4.macro.processor.model.token;

/**
 * A token.
 */
public final class Token {

    private final String name;
    private final String value;

    public Token(TokenDefinition def) {
        if (!def.isLiteral()) {
            throw new IllegalArgumentException("TokenDefinition passed to Token constructor is not a literal and hence invalid.");
        }
        this.name = def.name();
        this.value = def.getRegex().substring(1);
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
                    && other.getValue().equals(value);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Token(" + name + "='" + value.trim() + "')";
    }
}
