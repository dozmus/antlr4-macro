package notpure.antlr4.macro.processor.util;

import notpure.antlr4.macro.processor.model.token.Token;
import notpure.antlr4.macro.processor.model.token.TokenDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for {@link Token}, to be used in tests.
 */
public final class TokenHelper {

    public static List<Token> getTokens(TokenDefinition def, String input) {
        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            tokens.add(new Token(def.name(), def.isLiteral() ? def.getGroup() : c + ""));
        }
        return tokens;
    }

    public static List<Token> getLiteralTokens(TokenDefinition... defs) {
        List<Token> tokens = new ArrayList<>();

        for (TokenDefinition def : defs) {
            tokens.add(new Token(def));
        }
        return tokens;
    }

    public static List<Token> getLineTerminatorTokens() {
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token(TokenDefinition.CARRIAGE_RETURN));
        tokens.add(new Token(TokenDefinition.NEW_LINE));
        return tokens;
    }
}
