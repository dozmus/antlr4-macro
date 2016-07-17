package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.token.Token;
import notpure.antlr4.macro.processor.token.TokenDefinition;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.processor.TokenHelper.*;
import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link SimpleLexer}.
 */
public final class SimpleLexerTest {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleLexerTest.class);

    /**
     * Tests each lexer token definition once.
     */
    @Test
    public void generalLexerTestOfAllTokenDefinitions() {
        LOGGER.info("Now running: generalLexerTestOfAllTokenDefinitions()");

        List<Token> expectedOutput = new ArrayList<>();

        // Generate sample input
        String inputString = "";

        for (TokenDefinition def : TokenDefinition.values()) {
            if (def == TokenDefinition.DIGIT) {
                inputString += "0";
                expectedOutput.add(new Token(def.name(), "0"));
            } else if (def == TokenDefinition.LETTER) {
                inputString += "a";
                inputString += "B";
                expectedOutput.add(new Token(def.name(), "a"));
                expectedOutput.add(new Token(def.name(), "B"));
            } else { // general definitions
                String letter = def.getRegex().substring(1); // substring literal escape
                inputString += letter;
                expectedOutput.add(new Token(def.name(), letter));
            }
        }

        // Wrap string in stream
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));

        // Tokenize input
        List<Token> actualOutput = SimpleLexer.tokenize(inputStream);

        // Compare outputs
        assertEquals("actualOutput#size() != expectedOutput#size()", expectedOutput.size(), actualOutput.size());

        // Iterate over generated tokens
        for (int i = 0; i < expectedOutput.size(); i++) {
            Token eToken = expectedOutput.get(i);
            Token aToken = actualOutput.get(i);

            assertEquals("eToken != aToken", eToken, aToken);
        }
    }

    @Test
    public void lexerTestOfSampleInputFile1() {
        LOGGER.info("Now running: lexerTestOfSampleInputFile1()");

        // Store expected tokens
        List<Token> eo = new ArrayList<>();
        eo.addAll(getTokens(TokenDefinition.LETTER, "grammar"));
        eo.add(getLiteralToken(TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "Hello"));
        eo.add(getLiteralToken(TokenDefinition.SEMICOLON));
        eo.addAll(getLineTerminator());
        eo.addAll(getLineTerminator());

        eo.addAll(getLiteralTokens(TokenDefinition.FORWARD_SLASH, TokenDefinition.FORWARD_SLASH, TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "parser"));
        eo.add(getLiteralToken(TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "rules"));
        eo.addAll(getLineTerminator());

        eo.addAll(getTokens(TokenDefinition.LETTER, "r"));
        eo.addAll(getLiteralTokens(TokenDefinition.COLON, TokenDefinition.SPACE, TokenDefinition.SINGLE_QUOTE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "hello"));
        eo.addAll(getLiteralTokens(TokenDefinition.SINGLE_QUOTE, TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "ID"));
        eo.add(getLiteralToken(TokenDefinition.SEMICOLON));
        eo.addAll(getLineTerminator());
        eo.addAll(getLineTerminator());

        eo.addAll(getLiteralTokens(TokenDefinition.FORWARD_SLASH, TokenDefinition.FORWARD_SLASH, TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "lexer"));
        eo.add(getLiteralToken(TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "rules"));
        eo.addAll(getLineTerminator());

        eo.addAll(getTokens(TokenDefinition.LETTER, "ID"));
        eo.addAll(getLiteralTokens(TokenDefinition.SPACE, TokenDefinition.COLON, TokenDefinition.SPACE,
                TokenDefinition.LEFT_SQUARE_BRACKET));
        eo.addAll(getTokens(TokenDefinition.LETTER, "a"));
        eo.add(getLiteralToken(TokenDefinition.HYPHEN));
        eo.addAll(getTokens(TokenDefinition.LETTER, "z"));
        eo.addAll(getLiteralTokens(TokenDefinition.RIGHT_SQUARE_BRACKET, TokenDefinition.PLUS, TokenDefinition.SEMICOLON));
        eo.addAll(getLineTerminator());

        eo.addAll(getTokens(TokenDefinition.LETTER, "WS"));
        eo.addAll(getLiteralTokens(TokenDefinition.SPACE, TokenDefinition.COLON, TokenDefinition.SPACE,
                TokenDefinition.LEFT_SQUARE_BRACKET, TokenDefinition.SPACE));
        eo.add(getLiteralToken(TokenDefinition.BACK_SLASH));
        eo.addAll(getTokens(TokenDefinition.LETTER, "t"));
        eo.add(getLiteralToken(TokenDefinition.BACK_SLASH));
        eo.addAll(getTokens(TokenDefinition.LETTER, "r"));
        eo.add(getLiteralToken(TokenDefinition.BACK_SLASH));
        eo.addAll(getTokens(TokenDefinition.LETTER, "n"));
        eo.addAll(getLiteralTokens(TokenDefinition.RIGHT_SQUARE_BRACKET, TokenDefinition.PLUS, TokenDefinition.SPACE,
                TokenDefinition.HYPHEN, TokenDefinition.GREATER_THAN, TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "skip"));
        eo.add(getLiteralToken(TokenDefinition.SEMICOLON));

        // Tokenize file input
        List<Token> actualOutput = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream("src\\test\\resources\\notpure\\antlr4\\macro\\processor\\lexer-input-1.mg4")) {
            actualOutput = SimpleLexer.tokenize(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Compare outputs
        assertEquals("actualOutput#size() != expectedOutput#size()", eo.size(), actualOutput.size());

        // Iterate over generated tokens
        for (int i = 0; i < eo.size(); i++) {
            Token eToken = eo.get(i);
            Token aToken = actualOutput.get(i);

            assertEquals("eToken != aToken", eToken, aToken);
        }
    }


}
