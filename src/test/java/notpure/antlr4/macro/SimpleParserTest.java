package notpure.antlr4.macro;

import notpure.antlr4.macro.processor.SimpleLexer;
import notpure.antlr4.macro.processor.SimpleParser;
import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.Token;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link SimpleParser}. These tests rely on {@link SimpleLexerTest} passing.
 */
public final class SimpleParserTest {

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
        final Expression mlCommentExpression = new Expression(ExpressionType.MULTI_LINE_COMMENT, "comment");
        final Expression slCommentExpression = new Expression(ExpressionType.SINGLE_LINE_COMMENT, "comment");
        final Expression grammarExpression = new Expression(ExpressionType.GRAMMAR_NAME, "HelloWorld");
        final Expression parserExpression = new Expression(ExpressionType.PARSER_RULE, "hello", "WORLD");
        final Expression lexerExpression = new Expression(ExpressionType.LEXER_RULE, "HELLO", "WORLD");

        // Tests
        assertDoubleStatement("grammar HelloWorld;//comment", grammarExpression, slCommentExpression);
        assertDoubleStatement("grammar HelloWorld;// comment", grammarExpression, slCommentExpression);
        assertDoubleStatement("grammar HelloWorld; //comment", grammarExpression, slCommentExpression);
        assertDoubleStatement("grammar HelloWorld; // comment", grammarExpression, slCommentExpression);

        assertDoubleStatement("grammar HelloWorld;/*comment*/", grammarExpression, mlCommentExpression);
        assertDoubleStatement("grammar HelloWorld;/* comment*/", grammarExpression, mlCommentExpression);
        assertDoubleStatement("grammar HelloWorld;/*comment */", grammarExpression, mlCommentExpression);
        assertDoubleStatement("grammar HelloWorld; /*comment*/", grammarExpression, mlCommentExpression);
        assertDoubleStatement("grammar HelloWorld; /* comment*/", grammarExpression, mlCommentExpression);
        assertDoubleStatement("grammar HelloWorld; /*comment */", grammarExpression, mlCommentExpression);
        assertDoubleStatement("grammar HelloWorld; /* comment */", grammarExpression, mlCommentExpression);

        assertDoubleStatement("hello: WORLD;//comment", parserExpression, slCommentExpression);
        assertDoubleStatement("hello: WORLD;// comment", parserExpression, slCommentExpression);
        assertDoubleStatement("hello: WORLD; //comment", parserExpression, slCommentExpression);
        assertDoubleStatement("hello: WORLD; // comment", parserExpression, slCommentExpression);

        assertDoubleStatement("hello: WORLD;/*comment*/", parserExpression, mlCommentExpression);
        assertDoubleStatement("hello: WORLD;/* comment*/", parserExpression, mlCommentExpression);
        assertDoubleStatement("hello: WORLD;/*comment */", parserExpression, mlCommentExpression);
        assertDoubleStatement("hello: WORLD; /*comment*/", parserExpression, mlCommentExpression);
        assertDoubleStatement("hello: WORLD; /* comment*/", parserExpression, mlCommentExpression);
        assertDoubleStatement("hello: WORLD; /*comment */", parserExpression, mlCommentExpression);
        assertDoubleStatement("hello: WORLD; /* comment */", parserExpression, mlCommentExpression);

        assertDoubleStatement("HELLO: WORLD;//comment", lexerExpression, slCommentExpression);
        assertDoubleStatement("HELLO: WORLD;// comment", lexerExpression, slCommentExpression);
        assertDoubleStatement("HELLO: WORLD; //comment", lexerExpression, slCommentExpression);
        assertDoubleStatement("HELLO: WORLD; // comment", lexerExpression, slCommentExpression);

        assertDoubleStatement("HELLO: WORLD;/*comment*/", lexerExpression, mlCommentExpression);
        assertDoubleStatement("HELLO: WORLD;/* comment*/", lexerExpression, mlCommentExpression);
        assertDoubleStatement("HELLO: WORLD;/*comment */", lexerExpression, mlCommentExpression);
        assertDoubleStatement("HELLO: WORLD; /*comment*/", lexerExpression, mlCommentExpression);
        assertDoubleStatement("HELLO: WORLD; /* comment*/", lexerExpression, mlCommentExpression);
        assertDoubleStatement("HELLO: WORLD; /*comment */", lexerExpression, mlCommentExpression);
        assertDoubleStatement("HELLO: WORLD; /* comment */", lexerExpression, mlCommentExpression);

        // TODO fix code to work for tests below.
        assertDoubleStatement("HELLO: WORLD; HELLO: WORLD;", lexerExpression, lexerExpression);
        assertDoubleStatement("HELLO:WORLD; HELLO: WORLD;", lexerExpression, lexerExpression);
        assertDoubleStatement("HELLO: WORLD; HELLO:WORLD;", lexerExpression, lexerExpression);
        assertDoubleStatement("HELLO:WORLD; HELLO:WORLD;", lexerExpression, lexerExpression);
//        assertDoubleStatement("HELLO:WORLD;HELLO:WORLD;", lexerStatement, lexerStatement);

//        assertDoubleStatement("HELLO: WORLD; hello: WORLD;", lexerStatement, parserStatement);
//        assertDoubleStatement("HELLO:WORLD; hello: WORLD;", lexerStatement, parserStatement);
//        assertDoubleStatement("HELLO: WORLD; hello:WORLD;", lexerStatement, parserStatement);
//        assertDoubleStatement("HELLO:WORLD;hello: WORLD;", lexerStatement, parserStatement);
//        assertDoubleStatement("HELLO: WORLD;hello:WORLD;", lexerStatement, parserStatement);
//        assertDoubleStatement("HELLO:WORLD;hello:WORLD;", lexerStatement, parserStatement);

        assertDoubleStatement("hello: WORLD; HELLO: WORLD;", parserExpression, lexerExpression);
        assertDoubleStatement("hello:WORLD; HELLO: WORLD;", parserExpression, lexerExpression);
        assertDoubleStatement("hello: WORLD; HELLO:WORLD;", parserExpression, lexerExpression);
//        assertDoubleStatement("hello:WORLD;HELLO: WORLD;", parserStatement, lexerStatement);
//        assertDoubleStatement("hello: WORLD;HELLO:WORLD;", parserStatement, lexerStatement);
//        assertDoubleStatement("hello:WORLD;HELLO:WORLD;", parserStatement, lexerStatement);

//        assertDoubleStatement("/* comment *//* comment */", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/* comment*//*comment*/", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/*comment *//*comment*/", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/*comment*//* comment*/", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/*comment*//*comment */", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/*comment*/ /*comment*/", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/* comment */ /* comment */", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/* comment*/ /*comment*/", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/*comment */ /*comment*/", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/*comment*/ /* comment*/", mlCommentStatement, mlCommentStatement);
//        assertDoubleStatement("/*comment*/ /*comment */", mlCommentStatement, mlCommentStatement);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parserTestOfNullInput() {
        new SimpleParser().parse(null);
    }

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
        List<Token> tokens = new SimpleLexer().tokenize(input).getTokens();
        return new SimpleParser().parse(tokens).getExpressions();
    }
}
