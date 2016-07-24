package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.impl.*;
import notpure.antlr4.macro.processor.model.statement.Statement;
import notpure.antlr4.macro.processor.model.statement.StatementType;
import notpure.antlr4.macro.processor.model.token.Token;
import notpure.antlr4.macro.processor.model.token.TokenDefinition;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.processor.util.TokenHelper.getLineTerminatorTokens;
import static notpure.antlr4.macro.processor.util.TokenHelper.getLiteralTokens;
import static notpure.antlr4.macro.processor.util.TokenHelper.getTokens;
import static org.junit.Assert.assertEquals;

/**
 * A set of tests targeting both {@link SimpleLexer} and {@link SimpleParser}.
 */
public final class CombinedTest {

    @Test
    public void combinedTestOfSampleInputFile1() {
        // Store expected tokens
        List<Token> eo = new ArrayList<>();
        eo.addAll(getTokens(TokenDefinition.LETTER, "grammar"));
        eo.add(new Token(TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "Hello"));
        eo.add(new Token(TokenDefinition.SEMICOLON));
        eo.addAll(getLineTerminatorTokens());
        eo.addAll(getLineTerminatorTokens());

        eo.addAll(getLiteralTokens(TokenDefinition.FORWARD_SLASH, TokenDefinition.FORWARD_SLASH, TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "parser"));
        eo.add(new Token(TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "rules"));
        eo.addAll(getLineTerminatorTokens());

        eo.addAll(getTokens(TokenDefinition.LETTER, "r"));
        eo.addAll(getLiteralTokens(TokenDefinition.COLON, TokenDefinition.SPACE, TokenDefinition.SINGLE_QUOTE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "hello"));
        eo.addAll(getLiteralTokens(TokenDefinition.SINGLE_QUOTE, TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "ID"));
        eo.add(new Token(TokenDefinition.SEMICOLON));
        eo.addAll(getLineTerminatorTokens());
        eo.addAll(getLineTerminatorTokens());

        eo.addAll(getLiteralTokens(TokenDefinition.FORWARD_SLASH, TokenDefinition.FORWARD_SLASH, TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "lexer"));
        eo.add(new Token(TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "rules"));
        eo.addAll(getLineTerminatorTokens());

        eo.addAll(getTokens(TokenDefinition.LETTER, "ID"));
        eo.addAll(getLiteralTokens(TokenDefinition.SPACE, TokenDefinition.COLON, TokenDefinition.SPACE,
                TokenDefinition.LEFT_SQUARE_BRACKET));
        eo.addAll(getTokens(TokenDefinition.LETTER, "a"));
        eo.add(new Token(TokenDefinition.HYPHEN));
        eo.addAll(getTokens(TokenDefinition.LETTER, "z"));
        eo.addAll(getLiteralTokens(TokenDefinition.RIGHT_SQUARE_BRACKET, TokenDefinition.PLUS, TokenDefinition.SEMICOLON));
        eo.addAll(getLineTerminatorTokens());

        eo.addAll(getTokens(TokenDefinition.LETTER, "WS"));
        eo.addAll(getLiteralTokens(TokenDefinition.SPACE, TokenDefinition.COLON, TokenDefinition.SPACE,
                TokenDefinition.LEFT_SQUARE_BRACKET, TokenDefinition.SPACE));
        eo.add(new Token(TokenDefinition.BACK_SLASH));
        eo.addAll(getTokens(TokenDefinition.LETTER, "t"));
        eo.add(new Token(TokenDefinition.BACK_SLASH));
        eo.addAll(getTokens(TokenDefinition.LETTER, "r"));
        eo.add(new Token(TokenDefinition.BACK_SLASH));
        eo.addAll(getTokens(TokenDefinition.LETTER, "n"));
        eo.addAll(getLiteralTokens(TokenDefinition.RIGHT_SQUARE_BRACKET, TokenDefinition.PLUS, TokenDefinition.SPACE,
                TokenDefinition.HYPHEN, TokenDefinition.GREATER_THAN, TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "skip"));
        eo.add(new Token(TokenDefinition.SEMICOLON));
        eo.add(new Token(TokenDefinition.EOF));

        // Tokenize file input
        List<Token> actualOutput = new ArrayList<>();

        try (InputStream inputStream = new FileInputStream("src\\test\\resources\\notpure\\antlr4\\macro\\processor\\lexer-input-1.mg4")) {
            actualOutput = new SimpleLexer().tokenize(inputStream).getTokens();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Compare outputs
        assertEquals(eo.size(), actualOutput.size());

        // Iterate over generated tokens
        for (int i = 0; i < eo.size(); i++) {
            Token expectedToken = eo.get(i);
            Token actualToken = actualOutput.get(i);
            assertEquals(expectedToken, actualToken);
        }

        // Store expected statements
        List<Statement> expectedStatements = new ArrayList<>();
        expectedStatements.add(new Statement(StatementType.GRAMMAR_NAME, "Hello"));
        expectedStatements.add(new Statement(StatementType.SINGLE_LINE_COMMENT, "parser rules"));
        expectedStatements.add(new Statement(StatementType.PARSER_RULE, "r", "'hello' ID"));
        expectedStatements.add(new Statement(StatementType.SINGLE_LINE_COMMENT, "lexer rules"));
        expectedStatements.add(new Statement(StatementType.LEXER_RULE, "ID", "[a-z]+"));
        expectedStatements.add(new Statement(StatementType.LEXER_RULE, "WS", "[ \\t\\r\\n]+ -> skip"));

        // Store actual statements
        List<Statement> actualStatements = new SimpleParser().parse(actualOutput).getStatements();

        // Compare outputs
        assertEquals(expectedStatements.size(), actualStatements.size());

        // Iterate over generated statements
        for (int i = 0; i < expectedStatements.size(); i++) {
            Statement expectedStatement = expectedStatements.get(i);
            Statement actualStatement = actualStatements.get(i);
            assertEquals(expectedStatement, actualStatement);
        }
    }
}
