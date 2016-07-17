package notpure.antlr4.macro.processor.model.token;

import notpure.antlr4.macro.processor.impl.SimpleLexer;

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
    private final String group;

    TokenDefinition(String group, boolean literal) {
        this.literal = literal;
        this.group = group;
        this.pattern = Pattern.compile(literal ? "\\" + group : group);
    }

    TokenDefinition(String regex) {
        this(regex, true);
    }

    public boolean matches(String input) {
        return pattern.matcher(input).matches(); // XXX optimisation: use equals() if tokendef is a literal
    }

    public String getRegex() {
        return pattern.pattern();
    }

    public String getGroup() {
        return group;
    }

    public boolean isLiteral() {
        return literal;
    }
}
