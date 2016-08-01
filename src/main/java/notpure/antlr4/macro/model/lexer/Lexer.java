package notpure.antlr4.macro.model.lexer;

import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.util.FileHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A Lexer - converts {@link InputStream} into a {@link List} of {@link Token}.
 */
public abstract class Lexer {

    private final List<Token> tokens = new ArrayList<>();

    public Lexer tokenize(String input) {
        return tokenize(FileHelper.stringStream(input));
    }

    public abstract Lexer tokenize(InputStream inputStream);

    public List<Token> getTokens() {
        return tokens;
    }
}
