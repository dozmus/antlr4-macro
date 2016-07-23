package notpure.antlr4.macro.processor.model.token;

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

    public static boolean arrayContains(Token[] src, Token target) {
        // TODO extract into an array helper class
        for (Token t : src) {
            if (t.equals(target))
                return true;
        }
        return false;
    }

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
                    && (value == null || other.getValue().equals(value));
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
