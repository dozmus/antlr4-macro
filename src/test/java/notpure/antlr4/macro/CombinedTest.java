package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.model.lexer.token.TokenDefinition;
import notpure.antlr4.macro.processor.lexer.SimpleLexer;
import notpure.antlr4.macro.processor.macro.MacroExpressionProcessor;
import notpure.antlr4.macro.processor.macro.MacroExpressionResolver;
import notpure.antlr4.macro.processor.parser.SimpleParser;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static notpure.antlr4.macro.model.lang.ExpressionValueType.RAW;
import static notpure.antlr4.macro.model.lang.ExpressionValueType.STRING;
import static notpure.antlr4.macro.util.TokenHelper.*;
import static org.junit.Assert.assertEquals;

/**
 * A set of tests targeting a mixture of {@link SimpleLexer}, {@link SimpleParser}, {@link MacroExpressionProcessor}
 * and {@link MacroExpressionResolver}.
 */
public final class CombinedTest {

    @Test
    public void lexerParserTestOfSampleInputFile1() throws Exception {
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
        List<Token> actualOutput;

        try (InputStream inputStream = new FileInputStream("src\\test\\resources\\notpure\\antlr4\\macro\\lexer-parser-input-1.mg4")) {
            actualOutput = new SimpleLexer().tokenize(inputStream).getTokens();
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
        expressionValues2.add(new ExpressionValue(ExpressionValueType.PIPELINE, "->"));
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

        // TODO test optimized antlr output
    }

    @Test
    public void combinedTestOfSampleInputFile1() throws Exception {
        // Store expected tokens
        List<Token> eo = new ArrayList<>();
        eo.addAll(getTokens(TokenDefinition.LETTER, "grammar"));
        eo.add(new Token(TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "Hello"));
        eo.add(new Token(TokenDefinition.SEMICOLON));
        eo.addAll(getLineTerminatorTokens());
        eo.addAll(getLineTerminatorTokens());

        eo.addAll(getLiteralTokens(TokenDefinition.HASH));
        eo.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        eo.addAll(getLiteralTokens(TokenDefinition.UNDERSCORE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "WORLD"));
        eo.addAll(getLiteralTokens(TokenDefinition.COLON, TokenDefinition.SPACE, TokenDefinition.DOUBLE_QUOTE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "Hello"));
        eo.addAll(getLiteralTokens(TokenDefinition.SPACE));
        eo.addAll(getTokens(TokenDefinition.LETTER, "World"));
        eo.addAll(getLiteralTokens(TokenDefinition.DOUBLE_QUOTE));
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
        List<Token> actualOutput;

        try (InputStream inputStream = new FileInputStream("src\\test\\resources\\notpure\\antlr4\\macro\\input-1.mg4")) {
            actualOutput = new SimpleLexer().tokenize(inputStream).getTokens();
        }

        // Store expected statements
        List<ExpressionValue> expressionValues1 = new ArrayList<>();
        expressionValues1.add(new ExpressionValue(ExpressionValueType.REGEX_GROUP, "[ \\t\\r\\n]+"));
        expressionValues1.add(new ExpressionValue(ExpressionValueType.PIPELINE, "->"));
        expressionValues1.add(new ExpressionValue(ExpressionValueType.OUTPUT_REDIRECT, "skip"));

        List<Expression> expectedExpressions = new ArrayList<>();
        expectedExpressions.add(new Expression(ExpressionType.GRAMMAR_NAME,
                new ExpressionValue(RAW, "Hello")));
        expectedExpressions.add(new Expression(ExpressionType.SINGLE_LINE_COMMENT,
                new ExpressionValue(RAW, " macro rules")));
        expectedExpressions.add(new Expression(ExpressionType.MACRO_RULE, "HELLO_WORLD",
                new ExpressionValue(STRING, "Hello World")));
        expectedExpressions.add(new Expression(ExpressionType.SINGLE_LINE_COMMENT,
                new ExpressionValue(RAW, " parser rules")));
        expectedExpressions.add(new Expression(ExpressionType.PARSER_RULE, "r",
                new ExpressionValue(ExpressionValueType.RULE_REFERENCE, "#HELLO_WORLD")));
        expectedExpressions.add(new Expression(ExpressionType.SINGLE_LINE_COMMENT,
                new ExpressionValue(RAW, " lexer rules")));
        expectedExpressions.add(new Expression(ExpressionType.LEXER_RULE, "ID",
                new ExpressionValue(ExpressionValueType.REGEX_GROUP, "[a-z]+")));
        expectedExpressions.add(new Expression(ExpressionType.LEXER_RULE, "WS", expressionValues1));

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

        // MacroExpressionResolver
        List<Expression> macroExpressions = actualExpressions.stream()
                .filter(expr -> expr.getType() == ExpressionType.MACRO_RULE)
                .collect(Collectors.toList());
        List<Expression> resolvedMacroExpr = MacroExpressionResolver.resolve(macroExpressions);

        assertEquals(1, resolvedMacroExpr.size());
        assertEquals(new Expression(ExpressionType.MACRO_RULE, "HELLO_WORLD",
                new ExpressionValue(ExpressionValueType.STRING, "Hello World")), resolvedMacroExpr.get(0));

        // MacroExpressionProcessor
        actualExpressions = MacroExpressionProcessor.process(actualExpressions, resolvedMacroExpr);
        StringBuilder outputAntlr = new StringBuilder();

        actualExpressions.forEach(expr -> {
            outputAntlr.append(expr.toAntlr4String());
            outputAntlr.append("\r\n");
        });

        // Generate antlr file contents and compare to expected input
        String expected = "grammar Hello;\r\n"
                + "// macro rules\r\n"
                + "// parser rules\r\n"
                + "r: 'Hello World';\r\n"
                + "// lexer rules\r\n"
                + "ID: [a-z]+;\r\n"
                + "WS: [ \\t\\r\\n]+ -> skip;\r\n";
        assertEquals(expected, outputAntlr.toString());

        // TODO test optimized antlr output
    }
}
