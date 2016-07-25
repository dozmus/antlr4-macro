package notpure.antlr4.macro;

import notpure.antlr4.macro.processor.SimpleLexer;
import notpure.antlr4.macro.processor.SimpleParser;
import notpure.antlr4.macro.model.lang.Statement;
import notpure.antlr4.macro.model.lang.StatementType;
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
        final StatementType type = StatementType.MACRO_RULE;
        final Statement statement1 = new Statement(type, "HELLO_WORLD", "'HELLO'");
        assertSingleStatement("#P:w;", new Statement(type, "P", "w"));
        assertSingleStatement("#HELLO290woRld:'HELLO';", new Statement(type, "HELLO290woRld", "'HELLO'"));
        assertSingleStatement("#HELLO:HELLO;", new Statement(type, "HELLO", "HELLO"));
        assertSingleStatement("#HELLO_WORLD  :'HELLO';", statement1);
        assertSingleStatement("#HELLO_WORLD  : 'HELLO' ;", statement1);
        assertSingleStatement("#HELLO_WORLD : 'HELLO' ;", statement1);
        assertSingleStatement("#HELLO_WORLD: 'HELLO' ;", statement1);
        assertSingleStatement("#hELLO290woRld : 'HELLO' ;", new Statement(type, "hELLO290woRld", "'HELLO'"));
        assertSingleStatement("#hELLO290woRld: 'HELLO' ;", new Statement(type, "hELLO290woRld", "'HELLO'"));
        assertSingleStatement("#HELLO290woRld\r\n: \r\n'HELLO' ;", new Statement(type, "HELLO290woRld", "'HELLO'"));
        assertSingleStatement("#HELLO290woRld  : 'HELLO' ;", new Statement(type, "HELLO290woRld", "'HELLO'"));
        assertSingleStatement("#HELLO290woRld  :'HELLO\r\n|WORLD';", new Statement(type, "HELLO290woRld", "'HELLO|WORLD'"));
        assertSingleStatement("#HELLO290woRld  : \r\nHELLO\r\n|WORLD;", new Statement(type, "HELLO290woRld", "HELLO|WORLD"));
    }

    @Test
    public void parserTestOfParserRuleDefinitions() {
        final StatementType type = StatementType.PARSER_RULE;
        final Statement statement1 = new Statement(type, "helloWorld", "'HELLO'");
        assertSingleStatement("p:w;", new Statement(type, "p", "w"));
        assertSingleStatement("hello2903:'HELLO';", new Statement(type, "hello2903", "'HELLO'"));
        assertSingleStatement("hello:HELLO;", new Statement(type, "hello", "HELLO"));
        assertSingleStatement("helloWorld  :'HELLO';", statement1);
        assertSingleStatement("helloWorld  : 'HELLO' ;", statement1);
        assertSingleStatement("helloWorld : 'HELLO' ;", statement1);
        assertSingleStatement("helloWorld: 'HELLO' ;", statement1);
        assertSingleStatement("helloWorld\r\n: \r\n'HELLO' ;", statement1);
        assertSingleStatement("helloWorld  : 'HELLO' ;", statement1);
        assertSingleStatement("helloWorld  :'HELLO\r\n|WORLD';", new Statement(type, "helloWorld", "'HELLO|WORLD'"));
        assertSingleStatement("helloWorld  :HELLO\r\n|WORLD;", new Statement(type, "helloWorld", "HELLO|WORLD"));
    }

    @Test
    public void parserTestOfLexerRuleDefinitions() {
        final StatementType type = StatementType.LEXER_RULE;
        final Statement statement1 = new Statement(type, "HELLOWORLD", "'HELLO'");
        assertSingleStatement("P:w;", new Statement(type, "P", "w"));
        assertSingleStatement("HELLO290woRld:'HELLO';", new Statement(type, "HELLO290woRld", "'HELLO'"));
        assertSingleStatement("HELLO:HELLO;", new Statement(type, "HELLO", "HELLO"));
        assertSingleStatement("HELLOWORLD  :'HELLO';", statement1);
        assertSingleStatement("HELLOWORLD  : 'HELLO' ;", statement1);
        assertSingleStatement("HELLOWORLD : 'HELLO' ;",statement1);
        assertSingleStatement("HELLOWORLD: 'HELLO' ;", statement1);
        assertSingleStatement("HELLOWORLD\r\n: \r\n'HELLO' ;", statement1);
        assertSingleStatement("HELLOWORLD  : 'HELLO' ;", statement1);
        assertSingleStatement("HELLOWORLD  :'HELLO\r\n|WORLD';", new Statement(type, "HELLOWORLD", "'HELLO|WORLD'"));
        assertSingleStatement("HELLOWORLD  :HELLO\r\n|WORLD;", new Statement(type, "HELLOWORLD", "HELLO|WORLD"));
    }

    @Test
    public void parserTestOfFileHeaderDefinitions() {
        final StatementType type = StatementType.GRAMMAR_NAME;
        assertSingleStatement("grammar myGrammar;", new Statement(type, "myGrammar"));
        assertSingleStatement("grammar myGrammar2;", new Statement(type, "myGrammar2"));
        assertSingleStatement("grammar 2;", new Statement(type, "2"));
        assertSingleStatement("grammar m;", new Statement(type, "m"));
    }

    @Test
    public void parserTestOfMultiLineComment() {
        final StatementType type = StatementType.MULTI_LINE_COMMENT;
        assertSingleStatement("/*my comment*/", new Statement(type, "my comment"));
        assertSingleStatement("/* my comment */", new Statement(type, "my comment"));
        assertSingleStatement("/*/* my comment */", new Statement(type, "/* my comment"));
        assertSingleStatement("/*\r\nmy\r\ncomment\r\n*/", new Statement(type, "mycomment"));
        assertSingleStatement("/**/", new Statement(type, ""));
    }

    @Test
    public void parserTestOfInlineElements() {
        // TODO impl
    }

    @Test(expected = IllegalArgumentException.class)
    public void parserTestOfNullInput() {
        new SimpleParser().parse(null);
    }

    /**
     * Parses the input value into a list of {@link Statement} and ensures that the size is 1 and the only element
     * matches the expectedStatement.
     */
    private static void assertSingleStatement(String input, Statement expectedStatement) {
        // Generate statements
        List<Statement> output = statements(input);

        // Assert values
        assertEquals(1, output.size());
        assertEquals(expectedStatement, output.get(0));
    }

    /**
     * Generates a list of {@link Statement} from the given input.
     */
    private static List<Statement> statements(String input) {
        List<Token> tokens = new SimpleLexer().tokenize(input).getTokens();
        return new SimpleParser().parse(tokens).getStatements();
    }
}
