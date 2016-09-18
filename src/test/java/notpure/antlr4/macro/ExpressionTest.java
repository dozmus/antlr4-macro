package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.model.lang.ExpressionType.*;
import static notpure.antlr4.macro.model.lang.ExpressionValueType.*;
import static org.junit.Assert.*;

/**
 * A set of tests for {@link Expression}.
 */
public final class ExpressionTest {

    /**
     * Calls {@link Expression#toAntlr4String()} on the given expression and compares the output to expected.
     */
    private static void assertToAntlr4String(String expected, Expression expr) {
        assertEquals(expected, expr.toAntlr4String());
    }

    /**
     * Tests {@link Expression#equals(Object)}.
     */
    @Test
    public void equalsTest() {
        final Expression expr1 = new Expression(GRAMMAR_NAME, new ExpressionValue(RAW, "hello"));
        final Expression expr2 = new Expression(GRAMMAR_NAME, "raw", new ExpressionValue(RAW, "hello"));
        final Expression expr3 = new Expression(LEXER_RULE, "H", new ExpressionValue(RULE_REFERENCE, "R"));

        // Expressions with identifier=null
        assertTrue(expr1.equals(new Expression(GRAMMAR_NAME, new ExpressionValue(RAW, "hello"))));
        assertFalse(expr1.equals(expr2));
        assertFalse(expr2.equals(expr1));
        assertFalse(expr1.equals(null));

        // Expressions with identifier!=null
        assertTrue(expr3.equals(new Expression(LEXER_RULE, "H", new ExpressionValue(RULE_REFERENCE, "R"))));
        assertFalse(new Expression(LEXER_RULE, new ExpressionValue(RULE_REFERENCE, "H")).equals(expr2));
        assertFalse(expr3.equals(new Expression(LEXER_RULE, new ExpressionValue(RULE_REFERENCE, "R"))));
        assertFalse(expr3.equals(new Expression(LEXER_RULE, new ExpressionValue(RULE_REFERENCE, "H=RR"))));
        assertFalse(expr3.equals(null));
    }

    /**
     * Tests {@link Expression#toString()}.
     */
    @Test
    public void toStringTest() {
        final Expression expr1 = new Expression(GRAMMAR_NAME, new ExpressionValue(RAW, "hello"));
        final Expression expr2 = new Expression(LEXER_RULE, "H", new ExpressionValue(RULE_REFERENCE, "R"));

        assertEquals("GRAMMAR_NAME(ExpressionValue[] { RAW(hello) })", expr1.toString());
        assertEquals("LEXER_RULE(H=ExpressionValue[] { RULE_REFERENCE(R) })", expr2.toString());
    }

    /**
     * Tests {@link Expression#toAntlr4String()}.
     */
    @Test
    public void toAntlr4StringTest() {
        // Simple expressions (1 expression value)
        assertToAntlr4String("grammar hello;", new Expression(GRAMMAR_NAME, new ExpressionValue(RAW, "hello")));

        // Rules
        assertToAntlr4String("H: R;", new Expression(LEXER_RULE, "H", new ExpressionValue(RULE_REFERENCE, "R")));
        assertToAntlr4String("X: 'Hello';", new Expression(LEXER_RULE, "X", new ExpressionValue(STRING, "Hello")));
        assertToAntlr4String("h: R;", new Expression(PARSER_RULE, "h", new ExpressionValue(RULE_REFERENCE, "R")));
        assertToAntlr4String("x: 'Hello';", new Expression(PARSER_RULE, "x", new ExpressionValue(STRING, "Hello")));

        // Comments
        assertToAntlr4String("//Hello", new Expression(SINGLE_LINE_COMMENT, new ExpressionValue(RAW, "Hello")));
        assertToAntlr4String("// Hello", new Expression(SINGLE_LINE_COMMENT, new ExpressionValue(RAW, " Hello")));
        assertToAntlr4String("/*Hello*/", new Expression(MULTI_LINE_COMMENT, new ExpressionValue(RAW, "Hello")));
        assertToAntlr4String("/* Hello */", new Expression(MULTI_LINE_COMMENT, new ExpressionValue(RAW, " Hello ")));

        // Complex expressions (>1 expression value)
        List<ExpressionValue> valueList = new ArrayList<>();
        valueList.add(new ExpressionValue(STRING, "Hello"));
        valueList.add(new ExpressionValue(ALTERNATOR, "|"));
        valueList.add(new ExpressionValue(RULE_REFERENCE, "WORLD"));

        assertToAntlr4String("X: 'Hello' | WORLD;", new Expression(LEXER_RULE, "X", valueList));
        assertToAntlr4String("myRule: 'Hello' | WORLD;", new Expression(PARSER_RULE, "myRule", valueList));
    }

    /**
     * Tests {@link Expression#toAntlr4String()} with invalid input that should cause a {@link RuntimeException}.
     */
    @Test(expected = RuntimeException.class)
    public void toAntlr4StringTestForMacroRule() {
        assertToAntlr4String("---", new Expression(MACRO_RULE, "HI", new ExpressionValue(STRING, "Hello")));
    }
}
