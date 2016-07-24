package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Statement;
import notpure.antlr4.macro.model.lang.StatementType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * A set of tests for {@link notpure.antlr4.macro.model.lang.Statement}.
 */
public final class StatementTest {

    @Test
    public void statementEqualsTest() {
        // Statement with identifier=null
        final Statement statement1 = new Statement(StatementType.GRAMMAR_NAME, "hello");

        assertTrue(statement1.equals(new Statement(StatementType.GRAMMAR_NAME, "hello")));
        assertFalse(statement1.equals(new Statement(StatementType.GRAMMAR_NAME, "raw", "hello")));
        assertFalse(new Statement(StatementType.GRAMMAR_NAME, "raw", "hello").equals(statement1));
        assertFalse(statement1.equals(null));

        // Statement with identifier!=null
        final Statement statement2 = new Statement(StatementType.LEXER_RULE, "H", "R");

        assertTrue(statement2.equals(new Statement(StatementType.LEXER_RULE, "H", "R")));
        assertFalse(new Statement(StatementType.LEXER_RULE, "H").equals(statement2));
        assertFalse(statement2.equals(new Statement(StatementType.LEXER_RULE, "H")));
        assertFalse(statement2.equals(new Statement(StatementType.LEXER_RULE, "H=R")));
        assertFalse(statement2.equals(null));
    }

    @Test
    public void statementToStringTest() {
        final Statement statement1 = new Statement(StatementType.GRAMMAR_NAME, "hello");
        final Statement statement2 = new Statement(StatementType.LEXER_RULE, "H", "R");

        assertEquals("GRAMMAR_NAME(hello)", statement1.toString());
        assertEquals("LEXER_RULE(H=R)", statement2.toString());
    }
}
