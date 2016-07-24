package notpure.antlr4.macro.model.token;

import notpure.antlr4.macro.processor.SimpleLexer;

import java.util.regex.Pattern;

/**
 * Definitions for the {@link SimpleLexer}.
 */
public enum TokenDefinition {
    LETTER("[A-Za-z]", ValueType.REGEX),
    DIGIT("[0-9]", ValueType.REGEX),
    HASH("#"),
    SEMICOLON(";"),
    COLON(":"),
    SINGLE_QUOTE("'"),
    DOUBLE_QUOTE("\""),
    UNDERSCORE("_"),
    LEFT_BRACKET("("),
    RIGHT_BRACKET(")"),
    VERTICAL_LINE("|"),
    QUESTION_MARK("?"),
    ASTERISK("*"),
    PLUS("+"),
    EQUALS("="),
    BACK_SLASH("\\"),
    FORWARD_SLASH("/"),
    MODULO("%"),
    EXCLAMATION_MARK("!"),
    AND("&"),
    AT("@"),
    CARET("^"),
    TILDE("~"),
    DOLLAR("$"),
    POUND("£"),
    HYPHEN("-"),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LEFT_SQUARE_BRACKET("["),
    RIGHT_SQUARE_BRACKET("]"),
    SPACE(" "),
    CARRIAGE_RETURN("\r"),
    NEW_LINE("\n"),
    /**
     * End of file, unique token.
     */
    EOF;

    private final String value;
    private final ValueType valueType;
    private Pattern pattern;

    TokenDefinition(String value, ValueType valueType) {
        this.value = value;
        this.valueType = valueType;

        if (valueType == ValueType.REGEX)
            this.pattern = Pattern.compile(value);
    }

    TokenDefinition(String value) {
        this(value, ValueType.LITERAL);
    }

    TokenDefinition() {
        this(null, ValueType.SPECIAL);
    }

    public boolean matches(String input) {
        switch (valueType) {
            case LITERAL:
                return input.equals(value);
            case REGEX:
                return pattern.matcher(input).matches();
            default:
            case SPECIAL:
                return false;
        }
    }

    public String getValue() {
        return value;
    }

    public ValueType getValueType() {
        return valueType;
    }

    /**
     * Value type for {@link TokenDefinition}.
     */
    public enum ValueType {

        /**
         * The value is literal.
         */
        LITERAL,
        /**
         * The value is a RegEx.
         */
        REGEX,
        /**
         * The value is special in that {@link TokenDefinition#matches(String)} will always return false for it.
         */
        SPECIAL
    }
}
