package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.parser.ParserExceptionListener;
import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lexer.token.Token;
import notpure.antlr4.macro.processor.lexer.SimpleLexer;
import notpure.antlr4.macro.processor.parser.SimpleParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.model.lang.ExpressionValueType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A set of tests for {@link SimpleParser}. These tests rely on {@link SimpleLexerTest} passing.
 */
public final class SimpleParserTest {

    /**
     * Parses the input value into a list of {@link Expression} and ensures that the size is 1 and the only element
     * matches the expectedStatement.
     */
    private static void assertSingleStatement(String input, Expression expectedExpression) {
        // Generate statements
        List<Expression> output = statements(input);

        // Assert values
        assertEquals(1, output.size());
        assertEquals(expectedExpression, output.get(0));
    }

    /**
     * Parses the input value into a list of {@link Expression} and ensures that the size is 1 and the only element
     * matches the expectedStatement.
     */
    private static void assertDoubleStatement(String input, Expression expectedExpr1, Expression expectedExpr2) {
        // Generate statements
        List<Expression> output = statements(input);

        // Assert values
        assertEquals(2, output.size());
        assertEquals(expectedExpr1, output.get(0));
        assertEquals(expectedExpr2, output.get(1));
    }

    /**
     * Parses the input value into a list of {@link Expression} and ensures that {@link SimpleParser#isErrorOccurred()}
     * is true.
     */
    private static void assertParsingError(String input) {
        SimpleParser sp = (SimpleParser)new SimpleParser(new ParserExceptionListener.ParserExceptionNop())
                .parse(tokens("grammar myGrammar"));
        assertTrue(sp.isErrorOccurred());
    }

    /**
     * Generates a list of {@link Expression} from the given input.
     */
    private static List<Expression> statements(String input) {
        List<Token> tokens = tokens(input);
        return new SimpleParser().parse(tokens).getExpressions();
    }

    /**
     * Generates a list of {@link Token} from the given input.
     */
    private static List<Token> tokens(String input) {
        return new SimpleLexer().tokenize(input).getTokens();
    }

    @Test
    public void parserTestOfMacroRuleDefinitions() {
        final ExpressionType type = ExpressionType.MACRO_RULE;
        final ExpressionValue helloString = new ExpressionValue(STRING, "HELLO");
        final Expression expr1 = new Expression(type, "HELLO_WORLD", helloString);
        final Expression expr2 = new Expression(type, "hELLO290woRld", helloString);
        final Expression expr3 = new Expression(type, "HELLO290woRld", helloString);

        assertSingleStatement("#P:w;", new Expression(type, "P", new ExpressionValue(RULE_REFERENCE, "w")));
        assertSingleStatement("#HELLO290woRld:'HELLO';", new Expression(type, "HELLO290woRld", helloString));
        assertSingleStatement("#HELLO:HELLO;",
                new Expression(type, "HELLO", new ExpressionValue(RULE_REFERENCE, "HELLO")));
        assertSingleStatement("#HELLO_WORLD  :'HELLO';", expr1);
        assertSingleStatement("#HELLO_WORLD  : 'HELLO' ;", expr1);
        assertSingleStatement("#HELLO_WORLD : 'HELLO' ;", expr1);
        assertSingleStatement("#HELLO_WORLD: 'HELLO' ;", expr1);
        assertSingleStatement("#hELLO290woRld : 'HELLO' ;", expr2);
        assertSingleStatement("#hELLO290woRld: 'HELLO' ;", expr2);
        assertSingleStatement("#HELLO290woRld\r\n: \r\n'HELLO' ;", expr3);
        assertSingleStatement("#HELLO290woRld  : 'HELLO' ;", expr3);
        assertSingleStatement("#HELLO290woRld  :'HELLO\r\n|WORLD';",
                new Expression(type, "HELLO290woRld", new ExpressionValue(STRING, "HELLO\r\n|WORLD")));

        // UTF-8 in string
        assertSingleStatement("#HELLO:'HǺLLO';",
                new Expression(type, "HELLO", new ExpressionValue(STRING, "HǺLLO")));

        // Compound values
        final List<ExpressionValue> expressionValues = new ArrayList<>();
        expressionValues.add(new ExpressionValue(RULE_REFERENCE, "HELLO"));
        expressionValues.add(new ExpressionValue(ALTERNATOR, "|"));
        expressionValues.add(new ExpressionValue(RULE_REFERENCE, "WORLD"));
        assertSingleStatement("#HELLO290woRld  : \r\nHELLO\r\n|WORLD;",
                new Expression(type, "HELLO290woRld", expressionValues));
    }

    @Test
    public void parserTestOfParserRuleDefinitions() {
        final ExpressionType type = ExpressionType.PARSER_RULE;
        final ExpressionValue helloString = new ExpressionValue(STRING, "HELLO");
        final Expression expr1 = new Expression(type, "helloWorld", new ExpressionValue(STRING, "HELLO"));

        assertSingleStatement("p:w;", new Expression(type, "p", new ExpressionValue(RULE_REFERENCE, "w")));
        assertSingleStatement("hello2903:'HELLO';", new Expression(type, "hello2903", helloString));
        assertSingleStatement("hello:HELLO;",
                new Expression(type, "hello", new ExpressionValue(RULE_REFERENCE, "HELLO")));
        assertSingleStatement("helloWorld  :'HELLO';", expr1);
        assertSingleStatement("helloWorld  : 'HELLO' ;", expr1);
        assertSingleStatement("helloWorld : 'HELLO' ;", expr1);
        assertSingleStatement("helloWorld: 'HELLO' ;", expr1);
        assertSingleStatement("helloWorld\r\n: \r\n'HELLO' ;", expr1);
        assertSingleStatement("helloWorld  : 'HELLO' ;", expr1);
        assertSingleStatement("helloWorld  :'HELLO\r\n|WORLD';",
                new Expression(type, "helloWorld", new ExpressionValue(STRING, "HELLO\r\n|WORLD")));

        // UTF-8 in string
        assertSingleStatement("hello:'HǺLLO';",
                new Expression(type, "hello", new ExpressionValue(STRING, "HǺLLO")));

        // Compound values
        final List<ExpressionValue> expressionValues = new ArrayList<>();
        expressionValues.add(new ExpressionValue(RULE_REFERENCE, "HELLO"));
        expressionValues.add(new ExpressionValue(ALTERNATOR, "|"));
        expressionValues.add(new ExpressionValue(RULE_REFERENCE, "WORLD"));
        assertSingleStatement("helloWorld  :HELLO\r\n|WORLD;", new Expression(type, "helloWorld", expressionValues));
    }

    @Test
    public void parserTestOfLexerRuleDefinitions() {
        final ExpressionType type = ExpressionType.LEXER_RULE;
        final ExpressionValue helloString = new ExpressionValue(STRING, "HELLO");
        final Expression expr1 = new Expression(type, "HELLOWORLD", helloString);

        assertSingleStatement("P:w;", new Expression(type, "P", new ExpressionValue(RULE_REFERENCE, "w")));
        assertSingleStatement("HELLO290woRld:'HELLO';", new Expression(type, "HELLO290woRld", helloString));
        assertSingleStatement("HELLO:HELLO;",
                new Expression(type, "HELLO", new ExpressionValue(RULE_REFERENCE, "HELLO")));
        assertSingleStatement("HELLOWORLD  :'HELLO';", expr1);
        assertSingleStatement("HELLOWORLD  : 'HELLO' ;", expr1);
        assertSingleStatement("HELLOWORLD : 'HELLO' ;", expr1);
        assertSingleStatement("HELLOWORLD: 'HELLO' ;", expr1);
        assertSingleStatement("HELLOWORLD\r\n: \r\n'HELLO' ;", expr1);
        assertSingleStatement("HELLOWORLD  : 'HELLO' ;", expr1);
        assertSingleStatement("HELLOWORLD  :'HELLO\r\n|WORLD';",
                new Expression(type, "HELLOWORLD", new ExpressionValue(STRING, "HELLO\r\n|WORLD")));


        // UTF-8 in string
        assertSingleStatement("HELLO:'HǺLLO';",
                new Expression(type, "HELLO", new ExpressionValue(STRING, "HǺLLO")));

        // Compound values
        final List<ExpressionValue> expressionValues = new ArrayList<>();
        expressionValues.add(new ExpressionValue(RULE_REFERENCE, "HELLO"));
        expressionValues.add(new ExpressionValue(ALTERNATOR, "|"));
        expressionValues.add(new ExpressionValue(RULE_REFERENCE, "WORLD"));
        assertSingleStatement("HELLOWORLD  :HELLO\r\n|WORLD;", new Expression(type, "HELLOWORLD", expressionValues));
    }

    @Test
    public void parserTestOfFileHeaderDefinitions() {
        final ExpressionType type = ExpressionType.GRAMMAR_NAME;
        assertSingleStatement("grammar myGrammar;", new Expression(type, new ExpressionValue(RAW, "myGrammar")));
        assertSingleStatement("grammar myGrammar2;", new Expression(type, new ExpressionValue(RAW, "myGrammar2")));
        assertSingleStatement("grammar 2;", new Expression(type, new ExpressionValue(RAW, "2")));
        assertSingleStatement("grammar m;", new Expression(type, new ExpressionValue(RAW, "m")));
        assertSingleStatement("grammar\r\nmyGrammar;", new Expression(type, new ExpressionValue(RAW, "myGrammar")));
        assertSingleStatement("grammar\nmyGrammar;", new Expression(type, new ExpressionValue(RAW, "myGrammar")));
        assertSingleStatement("grammar  \r\n  m;", new Expression(type, new ExpressionValue(RAW, "m")));
        assertSingleStatement("grammar  \n  m;", new Expression(type, new ExpressionValue(RAW, "m")));
    }

    @Test
    public void parserTestOfMultiLineComment() {
        final ExpressionType type = ExpressionType.MULTI_LINE_COMMENT;
        assertSingleStatement("/*my comment*/", new Expression(type, new ExpressionValue(RAW, "my comment")));
        assertSingleStatement("/* my comment */", new Expression(type, new ExpressionValue(RAW, " my comment ")));
        assertSingleStatement("/*/* my comment */", new Expression(type, new ExpressionValue(RAW, "/* my comment ")));
        assertSingleStatement("/*\r\nmy\r\ncomment\r\n*/", new Expression(type, new ExpressionValue(RAW, "\r\nmy\r\ncomment\r\n")));
        assertSingleStatement("/**/", new Expression(type, new ExpressionValue(RAW, "")));
    }

    @Test
    public void parserTestOfInlineElements() {
        // Statements
        final Expression slComExpr1 = new Expression(ExpressionType.SINGLE_LINE_COMMENT, new ExpressionValue(RAW, "comment"));
        final Expression slComExpr2 = new Expression(ExpressionType.SINGLE_LINE_COMMENT, new ExpressionValue(RAW, " comment"));

        final Expression mlComExpr1 = new Expression(ExpressionType.MULTI_LINE_COMMENT, new ExpressionValue(RAW, "comment"));
        final Expression mlComExpr2 = new Expression(ExpressionType.MULTI_LINE_COMMENT, new ExpressionValue(RAW, " comment"));
        final Expression mlComExpr3 = new Expression(ExpressionType.MULTI_LINE_COMMENT, new ExpressionValue(RAW, "comment "));
        final Expression mlComExpr4 = new Expression(ExpressionType.MULTI_LINE_COMMENT, new ExpressionValue(RAW, " comment "));

        final Expression grmExpr = new Expression(ExpressionType.GRAMMAR_NAME, new ExpressionValue(RAW, "HelloWorld"));
        final Expression prsrExpr = new Expression(ExpressionType.PARSER_RULE, "hello", new ExpressionValue(RULE_REFERENCE, "WORLD"));
        final Expression lxrExpr = new Expression(ExpressionType.LEXER_RULE, "HELLO", new ExpressionValue(RULE_REFERENCE, "WORLD"));

        // Tests
        assertDoubleStatement("grammar HelloWorld;//comment", grmExpr, slComExpr1);
        assertDoubleStatement("grammar HelloWorld;// comment", grmExpr, slComExpr2);
        assertDoubleStatement("grammar HelloWorld; //comment", grmExpr, slComExpr1);
        assertDoubleStatement("grammar HelloWorld; // comment", grmExpr, slComExpr2);

        assertDoubleStatement("grammar HelloWorld;/*comment*/", grmExpr, mlComExpr1);
        assertDoubleStatement("grammar HelloWorld;/* comment*/", grmExpr, mlComExpr2);
        assertDoubleStatement("grammar HelloWorld;/*comment */", grmExpr, mlComExpr3);
        assertDoubleStatement("grammar HelloWorld;/* comment */", grmExpr, mlComExpr4);
        assertDoubleStatement("grammar HelloWorld; /*comment*/", grmExpr, mlComExpr1);
        assertDoubleStatement("grammar HelloWorld; /* comment*/", grmExpr, mlComExpr2);
        assertDoubleStatement("grammar HelloWorld; /*comment */", grmExpr, mlComExpr3);
        assertDoubleStatement("grammar HelloWorld; /* comment */", grmExpr, mlComExpr4);

        assertDoubleStatement("hello: WORLD;//comment", prsrExpr, slComExpr1);
        assertDoubleStatement("hello: WORLD;// comment", prsrExpr, slComExpr2);
        assertDoubleStatement("hello: WORLD; //comment", prsrExpr, slComExpr1);
        assertDoubleStatement("hello: WORLD; // comment", prsrExpr, slComExpr2);

        assertDoubleStatement("hello: WORLD;/*comment*/", prsrExpr, mlComExpr1);
        assertDoubleStatement("hello: WORLD;/* comment*/", prsrExpr, mlComExpr2);
        assertDoubleStatement("hello: WORLD;/*comment */", prsrExpr, mlComExpr3);
        assertDoubleStatement("hello: WORLD;/* comment */", prsrExpr, mlComExpr4);
        assertDoubleStatement("hello: WORLD; /*comment*/", prsrExpr, mlComExpr1);
        assertDoubleStatement("hello: WORLD; /* comment*/", prsrExpr, mlComExpr2);
        assertDoubleStatement("hello: WORLD; /*comment */", prsrExpr, mlComExpr3);
        assertDoubleStatement("hello: WORLD; /* comment */", prsrExpr, mlComExpr4);

        assertDoubleStatement("/*comment*/hello: WORLD;", mlComExpr1, prsrExpr);
        assertDoubleStatement("/* comment*/hello: WORLD;", mlComExpr2, prsrExpr);
        assertDoubleStatement("/*comment */hello: WORLD;", mlComExpr3, prsrExpr);
        assertDoubleStatement("/* comment */hello: WORLD;", mlComExpr4, prsrExpr);
        assertDoubleStatement("/*comment*/ hello: WORLD;", mlComExpr1, prsrExpr);
        assertDoubleStatement("/* comment*/ hello: WORLD;", mlComExpr2, prsrExpr);
        assertDoubleStatement("/*comment */ hello: WORLD;", mlComExpr3, prsrExpr);
        assertDoubleStatement("/* comment */ hello: WORLD;", mlComExpr4, prsrExpr);

        assertDoubleStatement("HELLO: WORLD;//comment", lxrExpr, slComExpr1);
        assertDoubleStatement("HELLO: WORLD;// comment", lxrExpr, slComExpr2);
        assertDoubleStatement("HELLO: WORLD; //comment", lxrExpr, slComExpr1);
        assertDoubleStatement("HELLO: WORLD; // comment", lxrExpr, slComExpr2);

        assertDoubleStatement("HELLO: WORLD;/*comment*/", lxrExpr, mlComExpr1);
        assertDoubleStatement("HELLO: WORLD;/* comment*/", lxrExpr, mlComExpr2);
        assertDoubleStatement("HELLO: WORLD;/*comment */", lxrExpr, mlComExpr3);
        assertDoubleStatement("HELLO: WORLD;/* comment */", lxrExpr, mlComExpr4);
        assertDoubleStatement("HELLO: WORLD; /*comment*/", lxrExpr, mlComExpr1);
        assertDoubleStatement("HELLO: WORLD; /* comment*/", lxrExpr, mlComExpr2);
        assertDoubleStatement("HELLO: WORLD; /*comment */", lxrExpr, mlComExpr3);
        assertDoubleStatement("HELLO: WORLD; /* comment */", lxrExpr, mlComExpr4);

        assertDoubleStatement("HELLO: WORLD; HELLO: WORLD;", lxrExpr, lxrExpr);
        assertDoubleStatement("HELLO:WORLD; HELLO: WORLD;", lxrExpr, lxrExpr);
        assertDoubleStatement("HELLO: WORLD; HELLO:WORLD;", lxrExpr, lxrExpr);
        assertDoubleStatement("HELLO:WORLD; HELLO:WORLD;", lxrExpr, lxrExpr);
        assertDoubleStatement("HELLO:WORLD;HELLO:WORLD;", lxrExpr, lxrExpr);

        assertDoubleStatement("HELLO: WORLD; hello: WORLD;", lxrExpr, prsrExpr);
        assertDoubleStatement("HELLO:WORLD; hello: WORLD;", lxrExpr, prsrExpr);
        assertDoubleStatement("HELLO: WORLD; hello:WORLD;", lxrExpr, prsrExpr);
        assertDoubleStatement("HELLO:WORLD;hello: WORLD;", lxrExpr, prsrExpr);
        assertDoubleStatement("HELLO: WORLD;hello:WORLD;", lxrExpr, prsrExpr);
        assertDoubleStatement("HELLO:WORLD;hello:WORLD;", lxrExpr, prsrExpr);

        assertDoubleStatement("hello: WORLD; HELLO: WORLD;", prsrExpr, lxrExpr);
        assertDoubleStatement("hello:WORLD; HELLO: WORLD;", prsrExpr, lxrExpr);
        assertDoubleStatement("hello: WORLD; HELLO:WORLD;", prsrExpr, lxrExpr);
        assertDoubleStatement("hello:WORLD;HELLO: WORLD;", prsrExpr, lxrExpr);
        assertDoubleStatement("hello: WORLD;HELLO:WORLD;", prsrExpr, lxrExpr);
        assertDoubleStatement("hello:WORLD;HELLO:WORLD;", prsrExpr, lxrExpr);

        assertDoubleStatement("/* comment *//* comment */", mlComExpr4, mlComExpr4);
        assertDoubleStatement("/* comment*//*comment*/", mlComExpr2, mlComExpr1);
        assertDoubleStatement("/*comment *//*comment*/", mlComExpr3, mlComExpr1);
        assertDoubleStatement("/*comment*//* comment*/", mlComExpr1, mlComExpr2);
        assertDoubleStatement("/*comment*//*comment */", mlComExpr1, mlComExpr3);
        assertDoubleStatement("/*comment*/ /*comment*/", mlComExpr1, mlComExpr1);
        assertDoubleStatement("/* comment */ /* comment */", mlComExpr4, mlComExpr4);
        assertDoubleStatement("/* comment*/ /*comment*/", mlComExpr2, mlComExpr1);
        assertDoubleStatement("/*comment */ /*comment*/", mlComExpr3, mlComExpr1);
        assertDoubleStatement("/*comment*/ /* comment*/", mlComExpr1, mlComExpr2);
        assertDoubleStatement("/*comment*/ /*comment */", mlComExpr1, mlComExpr3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parserTestOfNullInput() {
        new SimpleParser().parse(null);
    }

    @Test
    public void parserTestOfInvalidGrammarNameParsing() {
        assertParsingError("grammar myGrammar");
        assertParsingError("grammarmyGrammar;");
        assertParsingError("grammar ;");
    }

    @Test
    public void parserTestOfInvalidLexerRuleParsing() {
        assertParsingError("HELLO WORLD;");
        assertParsingError("HELLO:WORLD");
        assertParsingError("HELLO:;");
    }

    @Test
    public void parserTestOfInvalidMacroRuleParsing() {
        assertParsingError("#HELLO WORLD;");
        assertParsingError("#HELLO:WORLD");
        assertParsingError("#HELLO:;");
    }

    @Test
    public void parserTestOfInvalidParserRuleParsing() {
        assertParsingError("hello world;");
        assertParsingError("hello:world");
        assertParsingError("hello:;");
    }
}
