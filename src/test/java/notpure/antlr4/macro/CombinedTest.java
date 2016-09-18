package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.processor.cmd.GenerateAntlrCode;
import notpure.antlr4.macro.processor.Lexer;
import notpure.antlr4.macro.processor.ExpressionProcessor;
import notpure.antlr4.macro.processor.parser.SimpleParser;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
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
import static org.junit.Assert.assertTrue;

/**
 * A set of tests targeting a mixture of modules.
 * and {@link ExpressionProcessor}.
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
            actualOutput = Lexer.tokenize(inputStream);
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
        expressionValues2.add(new ExpressionValue(ExpressionValueType.REGEX_GROUP, "[ \\t\\r\\n]"));
        expressionValues2.add(new ExpressionValue(ExpressionValueType.PLUS, "+"));
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
                new ExpressionValue(ExpressionValueType.REGEX_GROUP, "[a-z]"),
                new ExpressionValue(ExpressionValueType.PLUS, "+")));
        expectedExpressions.add(new Expression(ExpressionType.LEXER_RULE, "WS", expressionValues2));

        // Store actual statements
        SimpleParser parser = new SimpleParser();
        parser.parse(actualOutput);
        List<Expression> actualExpressions = parser.getExpressions();

        // Compare outputs
        assertEquals(expectedExpressions.size(), actualExpressions.size());

        // Iterate over generated statements
        for (int i = 0; i < expectedExpressions.size(); i++) {
            Expression expectedExpression = expectedExpressions.get(i);
            Expression actualExpression = actualExpressions.get(i);
            assertEquals(expectedExpression, actualExpression);
        }

        // Testing minified output
        Main.CommandLineFlags.minify = true;
        Context ctx = new ContextBase();
        ctx.put("output-expressions", actualExpressions);
        GenerateAntlrCode gac = new GenerateAntlrCode();
        gac.execute(ctx);

        String expected = "grammar Hello;"
                + "r: 'hello' ID;"
                + "ID: [a-z]+;"
                + "WS: [ \\t\\r\\n]+ -> skip;";
        assertTrue(ctx.containsKey("antlr-code"));
        assertEquals(expected, ctx.get("antlr-code"));
        Main.CommandLineFlags.minify = false;
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
            actualOutput = Lexer.tokenize(inputStream);
        }

        // Store expected statements
        List<ExpressionValue> expressionValues1 = new ArrayList<>();
        expressionValues1.add(new ExpressionValue(ExpressionValueType.REGEX_GROUP, "[ \\t\\r\\n]"));
        expressionValues1.add(new ExpressionValue(ExpressionValueType.PLUS, "+"));
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
                new ExpressionValue(ExpressionValueType.REGEX_GROUP, "[a-z]"),
                new ExpressionValue(ExpressionValueType.PLUS, "+")));
        expectedExpressions.add(new Expression(ExpressionType.LEXER_RULE, "WS", expressionValues1));

        // Store actual statements
        SimpleParser parser = new SimpleParser();
        parser.parse(actualOutput);
        List<Expression> outputExpressions = parser.getExpressions();

        // Compare outputs
        assertEquals(expectedExpressions.size(), outputExpressions.size());

        // Iterate over generated statements
        for (int i = 0; i < expectedExpressions.size(); i++) {
            Expression expectedExpression = expectedExpressions.get(i);
            Expression actualExpression = outputExpressions.get(i);
            assertEquals(expectedExpression, actualExpression);
        }

        // MacroExpressionResolver
        List<Expression> macroExpressions = outputExpressions.stream()
                .filter(expr -> expr.getType() == ExpressionType.MACRO_RULE)
                .collect(Collectors.toList());
        List<Expression> resolvedMacroExpr = ExpressionProcessor.resolveMacros(macroExpressions);

        assertEquals(1, resolvedMacroExpr.size());
        assertEquals(new Expression(ExpressionType.MACRO_RULE, "HELLO_WORLD",
                new ExpressionValue(ExpressionValueType.STRING, "Hello World")), resolvedMacroExpr.get(0));

        // MacroExpressionProcessor
        outputExpressions = ExpressionProcessor.applyMacros(outputExpressions, resolvedMacroExpr);

        Context ctx = new ContextBase();
        ctx.put("output-expressions", outputExpressions);
        GenerateAntlrCode gac = new GenerateAntlrCode();
        gac.execute(ctx);

        // Generate antlr file contents and compare to expected input
        String expected = "grammar Hello;\r\n"
                + "// macro rules\r\n"
                + "// parser rules\r\n"
                + "r: 'Hello World';\r\n"
                + "// lexer rules\r\n"
                + "ID: [a-z]+;\r\n"
                + "WS: [ \\t\\r\\n]+ -> skip;\r\n";
        assertTrue(ctx.containsKey("antlr-code"));
        assertEquals(expected, ctx.get("antlr-code"));

        // Testing minified output
        Main.CommandLineFlags.minify = true;
        ctx = new ContextBase();
        ctx.put("output-expressions", outputExpressions);
        gac = new GenerateAntlrCode();
        gac.execute(ctx);

        expected = "grammar Hello;"
                + "r: 'Hello World';"
                + "ID: [a-z]+;"
                + "WS: [ \\t\\r\\n]+ -> skip;";
        assertTrue(ctx.containsKey("antlr-code"));
        assertEquals(expected, ctx.get("antlr-code"));
        Main.CommandLineFlags.minify = false;
    }
}
