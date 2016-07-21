package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.impl.SimpleLexer;
import notpure.antlr4.macro.processor.impl.SimpleParser;
import notpure.antlr4.macro.processor.model.statement.GenericStatement;
import notpure.antlr4.macro.processor.model.statement.Statement;
import notpure.antlr4.macro.processor.model.token.Token;
import notpure.antlr4.macro.processor.model.token.TokenDefinition;
import notpure.antlr4.macro.processor.util.FileHelper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.processor.TokenHelper.*;
import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link SimpleParser}. These tests rely on {@link SimpleLexerTest} passing.
 */
public final class SimpleParserTest {

    @Test
    public void parserTestOfMacroRuleDefinitions() {
        // Create array, try parse and compare
        List<Token> tokens = new SimpleLexer().tokenize(FileHelper.stringString("#HELLO290woRld:'HELLO';")).getTokens();
        List<Statement> output = new SimpleParser().parse(tokens).getStatements();
        assertEquals(1, output.size());
        assertEquals(GenericStatement.class.getSimpleName(), output.get(0).getName());
        assertEquals("HELLO290woRld:'HELLO'", output.get(0).getValue());

        // Create array, try parse and compare
        tokens = new SimpleLexer().tokenize(FileHelper.stringString("#HELLO290woRld  :'HELLO';")).getTokens();
        output = new SimpleParser().parse(tokens).getStatements();
        assertEquals(1, output.size());
        assertEquals(GenericStatement.class.getSimpleName(), output.get(0).getName());
        assertEquals("HELLO290woRld:'HELLO'", output.get(0).getValue());

        // Create array, try parse and compare
        tokens = new SimpleLexer().tokenize(FileHelper.stringString("#HELLO290woRld  : 'HELLO' ;")).getTokens();
        output = new SimpleParser().parse(tokens).getStatements();
        assertEquals(1, output.size());
        assertEquals(GenericStatement.class.getSimpleName(), output.get(0).getName());
        assertEquals("HELLO290woRld:'HELLO'", output.get(0).getValue());

        // Create array, try parse and compare
        tokens = new SimpleLexer().tokenize(FileHelper.stringString("#HELLO290woRld  :'HELLO\r\n|WORLD';")).getTokens();
        output = new SimpleParser().parse(tokens).getStatements();
        assertEquals(1, output.size());
        assertEquals(GenericStatement.class.getSimpleName(), output.get(0).getName());
        assertEquals("HELLO290woRld:'HELLO|WORLD'", output.get(0).getValue());
    }

    @Test
    public void parserTestOfParserRuleDefinitions() {
        // TODO add
    }

    @Test
    public void parserTestOfLexerRuleDefinitions() {
        // TODO add
    }

    @Test
    public void parserTestOfFileHeaderDefinitions() {
        // TODO add
    }
}
