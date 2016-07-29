package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.processor.SimpleLexer;
import notpure.antlr4.macro.processor.parser.SimpleParser;
import org.junit.Test;

import java.util.List;

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
    private static void assertDoubleStatement(String input, Expression expectedExpression1, Expression expectedExpression2) {
        // Generate statements
        List<Expression> output = statements(input);

        // Assert values
        assertEquals(2, output.size());
        assertEquals(expectedExpression1, output.get(0));
        assertEquals(expectedExpression2, output.get(1));
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
        final Expression expression1 = new Expression(type, "HELLO_WORLD", "'HELLO'");
        assertSingleStatement("#P:w;", new Expression(type, "P", "w"));
        assertSingleStatement("#HELLO290woRld:'HELLO';", new Expression(type, "HELLO290woRld", "'HELLO'"));
        assertSingleStatement("#HELLO:HELLO;", new Expression(type, "HELLO", "HELLO"));
        assertSingleStatement("#HELLO_WORLD  :'HELLO';", expression1);
        assertSingleStatement("#HELLO_WORLD  : 'HELLO' ;", expression1);
        assertSingleStatement("#HELLO_WORLD : 'HELLO' ;", expression1);
        assertSingleStatement("#HELLO_WORLD: 'HELLO' ;", expression1);
        assertSingleStatement("#hELLO290woRld : 'HELLO' ;", new Expression(type, "hELLO290woRld", "'HELLO'"));
        assertSingleStatement("#hELLO290woRld: 'HELLO' ;", new Expression(type, "hELLO290woRld", "'HELLO'"));
        assertSingleStatement("#HELLO290woRld\r\n: \r\n'HELLO' ;", new Expression(type, "HELLO290woRld", "'HELLO'"));
        assertSingleStatement("#HELLO290woRld  : 'HELLO' ;", new Expression(type, "HELLO290woRld", "'HELLO'"));
        assertSingleStatement("#HELLO290woRld  :'HELLO\r\n|WORLD';", new Expression(type, "HELLO290woRld", "'HELLO|WORLD'"));
        assertSingleStatement("#HELLO290woRld  : \r\nHELLO\r\n|WORLD;", new Expression(type, "HELLO290woRld", "HELLO|WORLD"));
    }

    @Test
    public void parserTestOfParserRuleDefinitions() {
        final ExpressionType type = ExpressionType.PARSER_RULE;
        final Expression expression1 = new Expression(type, "helloWorld", "'HELLO'");
        assertSingleStatement("p:w;", new Expression(type, "p", "w"));
        assertSingleStatement("hello2903:'HELLO';", new Expression(type, "hello2903", "'HELLO'"));
        assertSingleStatement("hello:HELLO;", new Expression(type, "hello", "HELLO"));
        assertSingleStatement("helloWorld  :'HELLO';", expression1);
        assertSingleStatement("helloWorld  : 'HELLO' ;", expression1);
        assertSingleStatement("helloWorld : 'HELLO' ;", expression1);
        assertSingleStatement("helloWorld: 'HELLO' ;", expression1);
        assertSingleStatement("helloWorld\r\n: \r\n'HELLO' ;", expression1);
        assertSingleStatement("helloWorld  : 'HELLO' ;", expression1);
        assertSingleStatement("helloWorld  :'HELLO\r\n|WORLD';", new Expression(type, "helloWorld", "'HELLO|WORLD'"));
        assertSingleStatement("helloWorld  :HELLO\r\n|WORLD;", new Expression(type, "helloWorld", "HELLO|WORLD"));
    }

    @Test
    public void parserTestOfLexerRuleDefinitions() {
        final ExpressionType type = ExpressionType.LEXER_RULE;
        final Expression expression1 = new Expression(type, "HELLOWORLD", "'HELLO'");
        assertSingleStatement("P:w;", new Expression(type, "P", "w"));
        assertSingleStatement("HELLO290woRld:'HELLO';", new Expression(type, "HELLO290woRld", "'HELLO'"));
        assertSingleStatement("HELLO:HELLO;", new Expression(type, "HELLO", "HELLO"));
        assertSingleStatement("HELLOWORLD  :'HELLO';", expression1);
        assertSingleStatement("HELLOWORLD  : 'HELLO' ;", expression1);
        assertSingleStatement("HELLOWORLD : 'HELLO' ;", expression1);
        assertSingleStatement("HELLOWORLD: 'HELLO' ;", expression1);
        assertSingleStatement("HELLOWORLD\r\n: \r\n'HELLO' ;", expression1);
        assertSingleStatement("HELLOWORLD  : 'HELLO' ;", expression1);
        assertSingleStatement("HELLOWORLD  :'HELLO\r\n|WORLD';", new Expression(type, "HELLOWORLD", "'HELLO|WORLD'"));
        assertSingleStatement("HELLOWORLD  :HELLO\r\n|WORLD;", new Expression(type, "HELLOWORLD", "HELLO|WORLD"));
    }

    @Test
    public void parserTestOfFileHeaderDefinitions() {
        final ExpressionType type = ExpressionType.GRAMMAR_NAME;
        assertSingleStatement("grammar myGrammar;", new Expression(type, "myGrammar"));
        assertSingleStatement("grammar myGrammar2;", new Expression(type, "myGrammar2"));
        assertSingleStatement("grammar 2;", new Expression(type, "2"));
        assertSingleStatement("grammar m;", new Expression(type, "m"));
        assertSingleStatement("grammar\r\nmyGrammar;", new Expression(type, "myGrammar"));
        assertSingleStatement("grammar\nmyGrammar;", new Expression(type, "myGrammar"));
        assertSingleStatement("grammar  \r\n  m;", new Expression(type, "m"));
        assertSingleStatement("grammar  \n  m;", new Expression(type, "m"));
    }

    @Test
    public void parserTestOfMultiLineComment() {
        final ExpressionType type = ExpressionType.MULTI_LINE_COMMENT;
        assertSingleStatement("/*my comment*/", new Expression(type, "my comment"));
        assertSingleStatement("/* my comment */", new Expression(type, "my comment"));
        assertSingleStatement("/*/* my comment */", new Expression(type, "/* my comment"));
        assertSingleStatement("/*\r\nmy\r\ncomment\r\n*/", new Expression(type, "mycomment"));
        assertSingleStatement("/**/", new Expression(type, ""));
    }

    @Test
    public void parserTestOfInlineElements() {
        // Statements
        final Expression mlComExpr = new Expression(ExpressionType.MULTI_LINE_COMMENT, "comment");
        final Expression slComExpr = new Expression(ExpressionType.SINGLE_LINE_COMMENT, "comment");
        final Expression grmExpr = new Expression(ExpressionType.GRAMMAR_NAME, "HelloWorld");
        final Expression prsrExpr = new Expression(ExpressionType.PARSER_RULE, "hello", "WORLD");
        final Expression lxrExpr = new Expression(ExpressionType.LEXER_RULE, "HELLO", "WORLD");

        // Tests
        assertDoubleStatement("grammar HelloWorld;//comment", grmExpr, slComExpr);
        assertDoubleStatement("grammar HelloWorld;// comment", grmExpr, slComExpr);
        assertDoubleStatement("grammar HelloWorld; //comment", grmExpr, slComExpr);
        assertDoubleStatement("grammar HelloWorld; // comment", grmExpr, slComExpr);

        assertDoubleStatement("grammar HelloWorld;/*comment*/", grmExpr, mlComExpr);
        assertDoubleStatement("grammar HelloWorld;/* comment*/", grmExpr, mlComExpr);
        assertDoubleStatement("grammar HelloWorld;/*comment */", grmExpr, mlComExpr);
        assertDoubleStatement("grammar HelloWorld; /*comment*/", grmExpr, mlComExpr);
        assertDoubleStatement("grammar HelloWorld; /* comment*/", grmExpr, mlComExpr);
        assertDoubleStatement("grammar HelloWorld; /*comment */", grmExpr, mlComExpr);
        assertDoubleStatement("grammar HelloWorld; /* comment */", grmExpr, mlComExpr);

        assertDoubleStatement("hello: WORLD;//comment", prsrExpr, slComExpr);
        assertDoubleStatement("hello: WORLD;// comment", prsrExpr, slComExpr);
        assertDoubleStatement("hello: WORLD; //comment", prsrExpr, slComExpr);
        assertDoubleStatement("hello: WORLD; // comment", prsrExpr, slComExpr);

        assertDoubleStatement("hello: WORLD;/*comment*/", prsrExpr, mlComExpr);
        assertDoubleStatement("hello: WORLD;/* comment*/", prsrExpr, mlComExpr);
        assertDoubleStatement("hello: WORLD;/*comment */", prsrExpr, mlComExpr);
        assertDoubleStatement("hello: WORLD; /*comment*/", prsrExpr, mlComExpr);
        assertDoubleStatement("hello: WORLD; /* comment*/", prsrExpr, mlComExpr);
        assertDoubleStatement("hello: WORLD; /*comment */", prsrExpr, mlComExpr);
        assertDoubleStatement("hello: WORLD; /* comment */", prsrExpr, mlComExpr);

        assertDoubleStatement("/*comment*/hello: WORLD;", mlComExpr, prsrExpr);
        assertDoubleStatement("/* comment*/hello: WORLD;", mlComExpr, prsrExpr);
        assertDoubleStatement("/*comment */hello: WORLD;", mlComExpr, prsrExpr);
        assertDoubleStatement("/*comment*/hello: WORLD;", mlComExpr, prsrExpr);
        assertDoubleStatement("/* comment*/hello: WORLD;", mlComExpr, prsrExpr);
        assertDoubleStatement("/*comment */hello: WORLD;", mlComExpr, prsrExpr);
        assertDoubleStatement("/* comment */hello: WORLD;", mlComExpr, prsrExpr);

        assertDoubleStatement("HELLO: WORLD;//comment", lxrExpr, slComExpr);
        assertDoubleStatement("HELLO: WORLD;// comment", lxrExpr, slComExpr);
        assertDoubleStatement("HELLO: WORLD; //comment", lxrExpr, slComExpr);
        assertDoubleStatement("HELLO: WORLD; // comment", lxrExpr, slComExpr);

        assertDoubleStatement("HELLO: WORLD;/*comment*/", lxrExpr, mlComExpr);
        assertDoubleStatement("HELLO: WORLD;/* comment*/", lxrExpr, mlComExpr);
        assertDoubleStatement("HELLO: WORLD;/*comment */", lxrExpr, mlComExpr);
        assertDoubleStatement("HELLO: WORLD; /*comment*/", lxrExpr, mlComExpr);
        assertDoubleStatement("HELLO: WORLD; /* comment*/", lxrExpr, mlComExpr);
        assertDoubleStatement("HELLO: WORLD; /*comment */", lxrExpr, mlComExpr);
        assertDoubleStatement("HELLO: WORLD; /* comment */", lxrExpr, mlComExpr);

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

        assertDoubleStatement("/* comment *//* comment */", mlComExpr, mlComExpr);
        assertDoubleStatement("/* comment*//*comment*/", mlComExpr, mlComExpr);
        assertDoubleStatement("/*comment *//*comment*/", mlComExpr, mlComExpr);
        assertDoubleStatement("/*comment*//* comment*/", mlComExpr, mlComExpr);
        assertDoubleStatement("/*comment*//*comment */", mlComExpr, mlComExpr);
        assertDoubleStatement("/*comment*/ /*comment*/", mlComExpr, mlComExpr);
        assertDoubleStatement("/* comment */ /* comment */", mlComExpr, mlComExpr);
        assertDoubleStatement("/* comment*/ /*comment*/", mlComExpr, mlComExpr);
        assertDoubleStatement("/*comment */ /*comment*/", mlComExpr, mlComExpr);
        assertDoubleStatement("/*comment*/ /* comment*/", mlComExpr, mlComExpr);
        assertDoubleStatement("/*comment*/ /*comment */", mlComExpr, mlComExpr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parserTestOfNullInput() {
        new SimpleParser().parse(null);
    }

    @Test
    public void parserTestOfInvalidGrammarNameParsing1() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("grammar myGrammar"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidGrammarNameParsing2() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("grammarmyGrammar;"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidGrammarNameParsing3() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("grammar ;"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidLexerRuleParsing1() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("HELLO WORLD;"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidLexerRuleParsing2() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("HELLO:WORLD"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidLexerRuleParsing3() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("HELLO:;"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidMacroRuleParsing1() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("#HELLO WORLD;"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidMacroRuleParsing2() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("#HELLO:WORLD"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidMacroRuleParsing3() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("#HELLO:;"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidParserRuleParsing1() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("hello world;"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidParserRuleParsing2() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("hello:world"));
        assertTrue(sp.errorOccurredParsing());
    }

    @Test
    public void parserTestOfInvalidParserRuleParsing3() {
        SimpleParser sp = (SimpleParser)new SimpleParser().parse(tokens("hello:;"));
        assertTrue(sp.errorOccurredParsing());
    }
}
