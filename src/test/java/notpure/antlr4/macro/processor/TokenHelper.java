package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.token.Token;
import notpure.antlr4.macro.processor.token.TokenDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for {@link Token} to be used in tests.
 */
public class TokenHelper {

    public static List<Token> getTokens(TokenDefinition def, String input) {
        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            tokens.add(new Token(def.name(), def.isLiteral() ? def.getRegex().substring(1) : c + ""));
        }
        return tokens;
    }

    public static List<Token> getLiteralTokens(TokenDefinition... defs) {
        List<Token> tokens = new ArrayList<>();

        for (TokenDefinition def : defs) {
            tokens.add(getLiteralToken(def));
        }
        return tokens;
    }

    public static Token getLiteralToken(TokenDefinition def) {
        return def.isLiteral() ? new Token(def.name(), def.getRegex().substring(1)) : null;
    }

    public static List<Token> getLineTerminator() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(getLiteralToken(TokenDefinition.CARRIAGE_RETURN));
        tokens.add(getLiteralToken(TokenDefinition.NEW_LINE));
        return tokens;
    }
}
