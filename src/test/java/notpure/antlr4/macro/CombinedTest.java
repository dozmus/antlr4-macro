package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.processor.lexer.SimpleLexer;
import notpure.antlr4.macro.processor.parser.SimpleParser;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.model.lang.ExpressionValueType.RAW;
import static notpure.antlr4.macro.util.TokenHelper.*;
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

        try (InputStream inputStream = new FileInputStream("src\\test\\resources\\notpure\\antlr4\\macro\\lexer-input-1.mg4")) {
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
        List<ExpressionValue> expressionValues1 = new ArrayList<>();
        expressionValues1.add(new ExpressionValue(ExpressionValueType.STRING, "hello"));
        expressionValues1.add(new ExpressionValue(ExpressionValueType.RULE_REFERENCE, "ID"));

        List<ExpressionValue> expressionValues2 = new ArrayList<>();
        expressionValues2.add(new ExpressionValue(ExpressionValueType.REGEX_GROUP, "[ \\t\\r\\n]+"));
        expressionValues2.add(new ExpressionValue(ExpressionValueType.REDIRECT, "->"));
        expressionValues2.add(new ExpressionValue(ExpressionValueType.OUTPUT_REDIRECT, "skip"));

        List<Expression> expectedExpressions = new ArrayList<>();
        expectedExpressions.add(new Expression(ExpressionType.GRAMMAR_NAME,
                new ExpressionValue(RAW, "Hello")));
        expectedExpressions.add(new Expression(ExpressionType.SINGLE_LINE_COMMENT,
                new ExpressionValue(RAW, " parser rules")));
        expectedExpressions.add(new Expression(ExpressionType.PARSER_RULE, "r", expressionValues1));
        expectedExpressions.add(new Expression(ExpressionType.SINGLE_LINE_COMMENT,
                new ExpressionValue(RAW, " lexer rules")));
        expectedExpressions.add(new Expression(ExpressionType.LEXER_RULE, "ID",
                new ExpressionValue(ExpressionValueType.REGEX_GROUP, "[a-z]+")));
        expectedExpressions.add(new Expression(ExpressionType.LEXER_RULE, "WS", expressionValues2));

        // Store actual statements
        List<Expression> actualExpressions = new SimpleParser().parse(actualOutput).getExpressions();

        // Compare outputs
        assertEquals(expectedExpressions.size(), actualExpressions.size());

        // Iterate over generated statements
        for (int i = 0; i < expectedExpressions.size(); i++) {
            Expression expectedExpression = expectedExpressions.get(i);
            Expression actualExpression = actualExpressions.get(i);
            assertEquals(expectedExpression, actualExpression);
        }
    }
}
