package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.parser.ParserExceptionListener;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.processor.Lexer;
import notpure.antlr4.macro.processor.parser.SimpleParser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.model.lang.ExpressionType.*;
import static notpure.antlr4.macro.model.lang.ExpressionValueType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A set of tests for {@link SimpleParser}. These tests rely on {@link LexerTest} passing.
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
        SimpleParser parser = new SimpleParser(new ParserExceptionListener.ParserExceptionSilent());
        parser.parse(tokens(input));
        assertTrue(parser.isErrorOccurred());
    }

    /**
     * Asserts a combined grammar name definition.
     */
    private static void assertGrammarNameDef(String input, String name) {
        assertGrammarNameDef(input, null, name);
    }

    /**
     * Asserts a specific grammar name definition.
     * @param input The raw string input to parse.
     * @param grammarType Empty string if combined, or 'parser' or 'lexer'.
     * @param name The grammar name.
     */
    private static void assertGrammarNameDef(String input, String grammarType, String name) {
        assertSingleStatement(input, new Expression(GRAMMAR_NAME, grammarType, new ExpressionValue(RAW, name)));
    }

    /**
     * Generates a list of {@link Expression} from the given input.
     */
    private static List<Expression> statements(String input) {
        List<Token> tokens = tokens(input);
        SimpleParser parser = new SimpleParser();
        parser.parse(tokens);
        return parser.getExpressions();
    }

    /**
     * Generates a list of {@link Token} from the given input.
     */
    private static List<Token> tokens(String input) {
        return Lexer.tokenize(input);
    }

    @Test
    public void parserTestOfMacroRuleDefinitions() {
        final ExpressionType type = MACRO_RULE;
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
        final ExpressionType type = PARSER_RULE;
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
        final ExpressionType type = LEXER_RULE;
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
    public void parserTestOfGrammarNameDefinitions() {
        // Combined grammars
        assertGrammarNameDef("grammar myGrammar;", "myGrammar");
        assertGrammarNameDef("grammar myGrammar2;", "myGrammar2");
        assertGrammarNameDef("grammar 2;", "2");
        assertGrammarNameDef("grammar m;", "m");
        assertGrammarNameDef("grammar\r\nmyGrammar;", "myGrammar");
        assertGrammarNameDef("grammar\nmyGrammar;", "myGrammar");
        assertGrammarNameDef("grammar  \r\n  m;", "m");
        assertGrammarNameDef("grammar  \n  m;", "m");

        // Lexer/parser grammars
        String[] types = {"lexer", "parser"};

        for (String t : types) {
            assertGrammarNameDef(t + " grammar myGrammar;", t, "myGrammar");
            assertGrammarNameDef(t + " grammar myGrammar2;", t, "myGrammar2");
            assertGrammarNameDef(t + " grammar 2;", t, "2");
            assertGrammarNameDef(t + " grammar m;", t, "m");
            assertGrammarNameDef(t + " grammar\r\nmyGrammar;", t, "myGrammar");
            assertGrammarNameDef(t + " grammar\nmyGrammar;", t, "myGrammar");
            assertGrammarNameDef(t + "\r\ngrammar\r\nmyGrammar;", t, "myGrammar");
            assertGrammarNameDef(t + "\n\ngrammar\nmyGrammar;", t, "myGrammar");
            assertGrammarNameDef(t + "  \r\n  grammar  m;", t, "m");
            assertGrammarNameDef(t + " \n grammar \n  m;", t, "m");
            assertGrammarNameDef(t + " \r\n \r\n grammar  \r\n  m;", t, "m");
            assertGrammarNameDef(t + "   \n   \n   grammar  \n  \n  m;", t, "m");
        }
    }

    @Test
    public void parserTestOfMultiLineComment() {
        final ExpressionType type = MULTI_LINE_COMMENT;
        assertSingleStatement("/*my comment*/", new Expression(type, new ExpressionValue(RAW, "my comment")));
        assertSingleStatement("/* my comment */", new Expression(type, new ExpressionValue(RAW, " my comment ")));
        assertSingleStatement("/*/* my comment */", new Expression(type, new ExpressionValue(RAW, "/* my comment ")));
        assertSingleStatement("/*\r\nmy\r\ncomment\r\n*/", new Expression(type, new ExpressionValue(RAW, "\r\nmy\r\ncomment\r\n")));
        assertSingleStatement("/**/", new Expression(type, new ExpressionValue(RAW, "")));
    }

    @Test
    public void parserTestOfRepetitionOperators() {
        List<ExpressionValue> values = new ArrayList<>();
        values.add(new ExpressionValue(STRING, "hello"));
        values.add(new ExpressionValue(PLUS, "+"));
        Expression expectedExpression = new Expression(PARSER_RULE, "myRule", values);
        assertSingleStatement("myRule: 'hello'+;", expectedExpression);

        values.clear();
        values.add(new ExpressionValue(STRING, "hello"));
        values.add(new ExpressionValue(STAR, "*"));
        expectedExpression = new Expression(PARSER_RULE, "myRule", values);
        assertSingleStatement("myRule: 'hello'*;", expectedExpression);

        values.clear();
        values.add(new ExpressionValue(STRING, "hello"));
        values.add(new ExpressionValue(QUESTION_MARK, "?"));
        expectedExpression = new Expression(PARSER_RULE, "myRule", values);
        assertSingleStatement("myRule: 'hello'?;", expectedExpression);

        values.clear();
        values.add(new ExpressionValue(REGEX_GROUP, "[A-Z]"));
        values.add(new ExpressionValue(PLUS, "+"));
        expectedExpression = new Expression(PARSER_RULE, "myRule", values);
        assertSingleStatement("myRule: [A-Z]+;", expectedExpression);

        values.clear();
        values.add(new ExpressionValue(REGEX_GROUP, "[A-Z]"));
        values.add(new ExpressionValue(STAR, "*"));
        expectedExpression = new Expression(PARSER_RULE, "myRule", values);
        assertSingleStatement("myRule: [A-Z]*;", expectedExpression);

        values.clear();
        values.add(new ExpressionValue(REGEX_GROUP, "[A-Z]"));
        values.add(new ExpressionValue(QUESTION_MARK, "?"));
        expectedExpression = new Expression(PARSER_RULE, "myRule", values);
        assertSingleStatement("myRule: [A-Z]?;", expectedExpression);
    }

    @Test
    public void parserTestOfInlineElements() {
        // Statements
        final Expression slComExpr1 = new Expression(SINGLE_LINE_COMMENT, new ExpressionValue(RAW, "comment"));
        final Expression slComExpr2 = new Expression(SINGLE_LINE_COMMENT, new ExpressionValue(RAW, " comment"));

        final Expression mlComExpr1 = new Expression(MULTI_LINE_COMMENT, new ExpressionValue(RAW, "comment"));
        final Expression mlComExpr2 = new Expression(MULTI_LINE_COMMENT, new ExpressionValue(RAW, " comment"));
        final Expression mlComExpr3 = new Expression(MULTI_LINE_COMMENT, new ExpressionValue(RAW, "comment "));
        final Expression mlComExpr4 = new Expression(MULTI_LINE_COMMENT, new ExpressionValue(RAW, " comment "));

        final Expression grmExpr = new Expression(GRAMMAR_NAME, new ExpressionValue(RAW, "HelloWorld"));
        final Expression prsrExpr = new Expression(PARSER_RULE, "hello", new ExpressionValue(RULE_REFERENCE, "WORLD"));
        final Expression lxrExpr = new Expression(LEXER_RULE, "HELLO", new ExpressionValue(RULE_REFERENCE, "WORLD"));

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
