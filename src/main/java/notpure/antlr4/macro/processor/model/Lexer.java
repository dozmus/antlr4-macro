package notpure.antlr4.macro.processor.model;

import notpure.antlr4.macro.processor.model.token.Token;

import java.io.InputStream;
import java.util.List;

/**
 * Created by pure on 17/07/2016.
 */
public interface Lexer {

    Lexer tokenize(InputStream inputStream);

    List<Token> getTokens();
}
