package notpure.antlr4.macro.processor.token;

import notpure.antlr4.macro.processor.SimpleLexer;

import java.util.regex.Pattern;

/**
 * Definitions for the {@link SimpleLexer}.
 */
public enum TokenDefinition {
    LETTER("[A-Za-z]", false),
    DIGIT("[0-9]", false),
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
    HYPHEN("-"),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LEFT_SQUARE_BRACKET("["),
    RIGHT_SQUARE_BRACKET("]"),
    SPACE(" "),
    CARRIAGE_RETURN("\r"),
    NEW_LINE("\n");

    private final boolean literal;
    private final Pattern pattern;

    TokenDefinition(String regex, boolean literal) {
        this.literal = literal;
        this.pattern = Pattern.compile(literal ? "\\" + regex : regex);
    }

    TokenDefinition(String regex) {
        this(regex, true);
    }

    public boolean matches(String input) {
        return pattern.matcher(input).matches();
    }

    public String getRegex() {
        return pattern.pattern();
    }

    public boolean isLiteral() {
        return literal;
    }
}
