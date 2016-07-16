package notpure.antlr4.macro.processor.token;

import java.util.regex.Pattern;

/**
 * Definitions for the {@link notpure.antlr4.macro.processor.Lexer}.
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
    LPAREN("("),
    RPAREN(")"),
    ALTERNATOR("|"),
    QUESTION_MARK("?"),
    ASTERISKS("*"),
    PLUS("+"),
    EQUALS("="),
    BACK_SLASH("\\"),
    FORWARD_SLASH("/"),
    HYPHEN("-"),
    LT("<"),
    GT(">"),
    LSQPAREN("["),
    RSQPAREN("]"),
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
