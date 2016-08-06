package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import org.junit.Test;

import static notpure.antlr4.macro.model.lang.ExpressionValueType.RAW;
import static notpure.antlr4.macro.model.lang.ExpressionValueType.RULE_REFERENCE;
import static org.junit.Assert.*;

/**
 * A set of tests for {@link Expression}.
 */
public final class ExpressionTest {

    @Test
    public void statementEqualsTest() {
        final Expression expression1 = new Expression(ExpressionType.GRAMMAR_NAME, new ExpressionValue(RAW, "hello"));
        final Expression expression2 = new Expression(ExpressionType.GRAMMAR_NAME, "raw", new ExpressionValue(RAW, "hello"));
        final Expression expression3 = new Expression(ExpressionType.LEXER_RULE, "H", new ExpressionValue(RULE_REFERENCE, "R"));

        // Statement with identifier=null
        assertTrue(expression1.equals(new Expression(ExpressionType.GRAMMAR_NAME, new ExpressionValue(RAW, "hello"))));
        assertFalse(expression1.equals(expression2));
        assertFalse(expression2.equals(expression1));
        assertFalse(expression1.equals(null));

        // Statement with identifier!=null
        assertTrue(expression3.equals(new Expression(ExpressionType.LEXER_RULE, "H", new ExpressionValue(RULE_REFERENCE, "R"))));
        assertFalse(new Expression(ExpressionType.LEXER_RULE, new ExpressionValue(RULE_REFERENCE, "H")).equals(expression2));
        assertFalse(expression3.equals(new Expression(ExpressionType.LEXER_RULE, new ExpressionValue(RULE_REFERENCE, "R"))));
        assertFalse(expression3.equals(new Expression(ExpressionType.LEXER_RULE, new ExpressionValue(RULE_REFERENCE, "H=RR"))));
        assertFalse(expression3.equals(null));
    }

    @Test
    public void statementToStringTest() {
        final Expression expression1 = new Expression(ExpressionType.GRAMMAR_NAME, new ExpressionValue(RAW, "hello"));
        final Expression expression2 = new Expression(ExpressionType.LEXER_RULE, "H", new ExpressionValue(RULE_REFERENCE, "R"));

        assertEquals("GRAMMAR_NAME(ExpressionValue[] { RAW(hello) })", expression1.toString());
        assertEquals("LEXER_RULE(H=ExpressionValue[] { RULE_REFERENCE(R) })", expression2.toString());
    }
}
