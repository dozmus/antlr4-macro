package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.statement.Statement;
import notpure.antlr4.macro.processor.token.Token;
import notpure.antlr4.macro.processor.token.TokenDefinition;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.processor.TokenHelper.*;
import static org.junit.Assert.assertEquals;

/**
 * A set of tests for {@link SimpleParser}.
 */
public final class SimpleParserTest {

    @Test
    public void parserTestOfStringLiterals() {
        /*
        // Create array
        List<Token> tokens = new ArrayList<>();
        tokens.add(getLiteralToken(TokenDefinition.SINGLE_QUOTE));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "hello"));
        tokens.addAll(getTokens(TokenDefinition.DIGIT, "23390"));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "world"));
        tokens.add(getLiteralToken(TokenDefinition.SINGLE_QUOTE));

        // Try parse and compare
        List<Token> output = Parser.parse(tokens);
        assertEquals("output#size() != 1", output.size(), 1);
        assertEquals("output#get(0)#getName() != \"String\"", output.get(0).getName(), "String");
        assertEquals("output#get(0)#getValue() != \"hello23390world\"", output.get(0).getValue(), "\"hello23390world\"");

        // Create array
        tokens = new ArrayList<>();
        tokens.add(getLiteralToken(TokenDefinition.DOUBLE_QUOTE));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "hello"));
        tokens.addAll(getTokens(TokenDefinition.DIGIT, "23390"));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "world"));
        tokens.add(getLiteralToken(TokenDefinition.DOUBLE_QUOTE));

        // Try parse and compare
        output = Parser.parse(tokens);
        assertEquals("output#size() != 1", output.size(), 1);
        assertEquals("output#get(0)#getName() != \"String\"", output.get(0).getName(), "String");
        assertEquals("output#get(0)#getValue() != \"hello23390world\"", output.get(0).getValue(), "\"hello23390world\"");
        */
    }

    @Test
    public void parserTestOfMacroDefinitions() {
        // Create array
        List<Token> tokens = new ArrayList<>();
        tokens.add(getLiteralToken(TokenDefinition.HASH));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getTokens(TokenDefinition.DIGIT, "290"));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "woRld"));
        tokens.addAll(getLiteralTokens(TokenDefinition.COLON, TokenDefinition.SINGLE_QUOTE));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getLiteralTokens(TokenDefinition.SINGLE_QUOTE, TokenDefinition.SEMICOLON));

        // Try parse and compare
        List<Statement> output = SimpleParser.parse(tokens);
        assertEquals("output#size() != 1", 1, output.size());
        assertEquals("'MacroStatement' != output.get(0).getName()", "MacroStatement", output.get(0).getName());
        assertEquals("'MacroStatement' != output.get(0).getValue()", "HELLO290woRld='HELLO'", output.get(0).getValue());

        // Create array
        tokens = new ArrayList<>();
        tokens.add(getLiteralToken(TokenDefinition.HASH));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getTokens(TokenDefinition.DIGIT, "290"));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "woRld"));
        tokens.addAll(getLiteralTokens(TokenDefinition.SPACE));
        tokens.addAll(getLiteralTokens(TokenDefinition.SPACE));
        tokens.addAll(getLiteralTokens(TokenDefinition.COLON, TokenDefinition.SINGLE_QUOTE));
        tokens.addAll(getTokens(TokenDefinition.LETTER, "HELLO"));
        tokens.addAll(getLiteralTokens(TokenDefinition.SINGLE_QUOTE, TokenDefinition.SEMICOLON));

        // Try parse and compare
        output = SimpleParser.parse(tokens);
        assertEquals("output#size() != 1", 1, output.size());
        assertEquals("'MacroStatement' != output.get(0).getName()", "MacroStatement", output.get(0).getName());
        assertEquals("'MacroStatement' != output.get(0).getValue()", "HELLO290woRld='HELLO'", output.get(0).getValue());
    }
}
