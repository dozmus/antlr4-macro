package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A set of tests for {@link Expression}.
 */
public final class ExpressionTest {

    @Test
    public void statementEqualsTest() {
        // Statement with identifier=null
        final Expression expression1 = new Expression(ExpressionType.GRAMMAR_NAME, "hello");

        assertTrue(expression1.equals(new Expression(ExpressionType.GRAMMAR_NAME, "hello")));
        assertFalse(expression1.equals(new Expression(ExpressionType.GRAMMAR_NAME, "raw", "hello")));
        assertFalse(new Expression(ExpressionType.GRAMMAR_NAME, "raw", "hello").equals(expression1));
        assertFalse(expression1.equals(null));

        // Statement with identifier!=null
        final Expression expression2 = new Expression(ExpressionType.LEXER_RULE, "H", "R");

        assertTrue(expression2.equals(new Expression(ExpressionType.LEXER_RULE, "H", "R")));
        assertFalse(new Expression(ExpressionType.LEXER_RULE, "H").equals(expression2));
        assertFalse(expression2.equals(new Expression(ExpressionType.LEXER_RULE, "H")));
        assertFalse(expression2.equals(new Expression(ExpressionType.LEXER_RULE, "H=R")));
        assertFalse(expression2.equals(null));
    }

    @Test
    public void statementToStringTest() {
        final Expression expression1 = new Expression(ExpressionType.GRAMMAR_NAME, "hello");
        final Expression expression2 = new Expression(ExpressionType.LEXER_RULE, "H", "R");

        assertEquals("GRAMMAR_NAME(hello)", expression1.toString());
        assertEquals("LEXER_RULE(H=R)", expression2.toString());
    }
}
