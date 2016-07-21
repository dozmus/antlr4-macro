package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.impl.*;
import notpure.antlr4.macro.processor.model.statement.GenericStatement;
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

import static notpure.antlr4.macro.processor.TokenHelper.getLineTerminatorTokens;
import static notpure.antlr4.macro.processor.TokenHelper.getLiteralTokens;
import static notpure.antlr4.macro.processor.TokenHelper.getTokens;
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
            Token eToken = eo.get(i);
            Token aToken = actualOutput.get(i);

            assertEquals(eToken, aToken);
        }

        // Store expected statements
        List<Statement> expectedStatements = new ArrayList<>();
        expectedStatements.add(new Statement("FileHeader", "Hello"));
        expectedStatements.add(new Statement("SingleLineComment", "parser rules"));
        expectedStatements.add(new GenericStatement("r", "'hello' ID", StatementType.PARSER_RULE));
        expectedStatements.add(new Statement("SingleLineComment", "lexer rules"));
        expectedStatements.add(new GenericStatement("ID", "[a-z]+", StatementType.LEXER_RULE));
        expectedStatements.add(new GenericStatement("WS", "[ \\t\\r\\n]+ -> skip", StatementType.LEXER_RULE));

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
