package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.impl.SimpleLexer;
import notpure.antlr4.macro.processor.impl.SimpleParser;
import notpure.antlr4.macro.processor.model.statement.GenericStatement;
import notpure.antlr4.macro.processor.model.statement.Statement;
import notpure.antlr4.macro.processor.model.statement.StatementType;
import notpure.antlr4.macro.processor.model.token.Token;
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
        assertSingleStatement("#P:w;", "P:w", type);
        assertSingleStatement("#HELLO290woRld:'HELLO';", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("#HELLO:HELLO;", "HELLO:HELLO", type);
        assertSingleStatement("#HELLO290woRld  :'HELLO';", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("#HELLO290woRld  : 'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("#HELLO290woRld : 'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("#HELLO290woRld: 'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("#hELLO290woRld : 'HELLO' ;", "hELLO290woRld:'HELLO'", type);
        assertSingleStatement("#hELLO290woRld: 'HELLO' ;", "hELLO290woRld:'HELLO'", type);
        assertSingleStatement("#HELLO290woRld\r\n: \r\n'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("#HELLO290woRld  : 'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("#HELLO290woRld  :'HELLO\r\n|WORLD';", "HELLO290woRld:'HELLO|WORLD'", type);
    }

    @Test
    public void parserTestOfParserRuleDefinitions() {
        final StatementType type = StatementType.PARSER_RULE;
        assertSingleStatement("p:w;", "p:w", type);
        assertSingleStatement("hELLO290woRld:'HELLO';", "hELLO290woRld:'HELLO'", type);
        assertSingleStatement("hELLO:HELLO;", "hELLO:HELLO", type);
        assertSingleStatement("hELLO290woRld  :'HELLO';", "hELLO290woRld:'HELLO'", type);
        assertSingleStatement("hELLO290woRld  : 'HELLO' ;", "hELLO290woRld:'HELLO'", type);
        assertSingleStatement("hELLO290woRld : 'HELLO' ;", "hELLO290woRld:'HELLO'", type);
        assertSingleStatement("hELLO290woRld: 'HELLO' ;", "hELLO290woRld:'HELLO'", type);
        assertSingleStatement("helloWorld\r\n: \r\n'HELLO' ;", "helloWorld:'HELLO'", type);
        assertSingleStatement("hELLO290woRld  : 'HELLO' ;", "hELLO290woRld:'HELLO'", type);
        assertSingleStatement("hELLO290woRld  :'HELLO\r\n|WORLD';", "hELLO290woRld:'HELLO|WORLD'", type);
    }

    @Test
    public void parserTestOfLexerRuleDefinitions() {
        final StatementType type = StatementType.LEXER_RULE;
        assertSingleStatement("P:w;", "P:w", type);
        assertSingleStatement("HELLO290woRld:'HELLO';", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("HELLO:HELLO;", "HELLO:HELLO", type);
        assertSingleStatement("HELLO290woRld  :'HELLO';", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("HELLO290woRld  : 'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("HELLO290woRld : 'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("HELLO290woRld: 'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("HELLO290woRld\r\n: \r\n'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("HELLO290woRld  : 'HELLO' ;", "HELLO290woRld:'HELLO'", type);
        assertSingleStatement("HELLO290woRld  :'HELLO\r\n|WORLD';", "HELLO290woRld:'HELLO|WORLD'", type);
    }

    @Test
    public void parserTestOfFileHeaderDefinitions() {
        assertSingleGrammarStatement("grammar myGrammar;", "myGrammar");
        assertSingleGrammarStatement("grammar myGrammar2;", "myGrammar2");
        assertSingleGrammarStatement("grammar 2;", "2");
    }

    /**
     * Parses the input value into a list of {@link Statement} and ensures that the size is 1, the type
     * of the only element is Statement, the name is 'FileHeader' and the value is as specified.
     */
    private static void assertSingleGrammarStatement(String input, String expectedValue) {
        // Generate statements
        List<Statement> output = statements(input);

        // Assert values
        assertEquals(1, output.size());
        assertEquals(Statement.class, output.get(0).getClass());
        assertEquals("FileHeader", output.get(0).getName());
        assertEquals(expectedValue, output.get(0).getValue());
    }

    /**
     * Parses the input value into a list of {@link Statement} and ensures that the size is 1, the type
     * of the only element is GenericStatement, the {@link StatementType} is as specified and the value is as specified.
     */
    private static void assertSingleStatement(String input, String expectedValue, StatementType expectedType) {
        // Generate statements
        List<Statement> output = statements(input);

        // Assert values
        assertEquals(1, output.size());
        assertEquals(GenericStatement.class, output.get(0).getClass());
        assertEquals(expectedType, ((GenericStatement)output.get(0)).getStatementType());
        assertEquals(expectedValue, output.get(0).getValue());
    }

    /**
     * Generates a list of {@link Statement} from the given input.
     */
    private static List<Statement> statements(String input) {
        List<Token> tokens = new SimpleLexer().tokenize(input).getTokens();
        return new SimpleParser().parse(tokens).getStatements();
    }
}
