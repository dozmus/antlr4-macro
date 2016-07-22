package notpure.antlr4.macro.processor.model;

import notpure.antlr4.macro.processor.model.token.Token;
import notpure.antlr4.macro.processor.util.FileHelper;

import java.io.InputStream;
import java.util.List;

/**
 * A Lexer - converts {@link InputStream} into a {@link List} of {@link Token}.
 */
public abstract class Lexer {

    public Lexer tokenize(String input) {
        return tokenize(FileHelper.stringStream(input));
    }

    public abstract Lexer tokenize(InputStream inputStream);

    public abstract List<Token> getTokens();
}
