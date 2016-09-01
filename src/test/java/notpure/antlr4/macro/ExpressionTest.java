package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.model.lang.ExpressionValueType.*;
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

    @Test
    public void statementToAntlr4StringTest() {
        final Expression expression1 = new Expression(ExpressionType.GRAMMAR_NAME, new ExpressionValue(RAW, "hello"));
        final Expression expression2 = new Expression(ExpressionType.LEXER_RULE, "H", new ExpressionValue(RULE_REFERENCE, "R"));
        final Expression expression3 = new Expression(ExpressionType.LEXER_RULE, "X", new ExpressionValue(STRING, "Hello"));
        final Expression expression4 = new Expression(ExpressionType.PARSER_RULE, "h", new ExpressionValue(RULE_REFERENCE, "R"));
        final Expression expression5 = new Expression(ExpressionType.PARSER_RULE, "x", new ExpressionValue(STRING, "Hello"));
        final Expression expression6 = new Expression(ExpressionType.SINGLE_LINE_COMMENT, new ExpressionValue(STRING, "Hello"));
        final Expression expression7 = new Expression(ExpressionType.SINGLE_LINE_COMMENT, new ExpressionValue(STRING, " Hello"));
        final Expression expression8 = new Expression(ExpressionType.MULTI_LINE_COMMENT, new ExpressionValue(STRING, "Hello"));
        final Expression expression9 = new Expression(ExpressionType.MULTI_LINE_COMMENT, new ExpressionValue(STRING, " Hello "));

        List<ExpressionValue> valueList = new ArrayList<>();
        valueList.add(new ExpressionValue(STRING, "Hello"));
        valueList.add(new ExpressionValue(ALTERNATOR, "|"));
        valueList.add(new ExpressionValue(RULE_REFERENCE, "WORLD"));
        final Expression expression10 = new Expression(ExpressionType.LEXER_RULE, "X", valueList);
        final Expression expression11 = new Expression(ExpressionType.PARSER_RULE, "myRule", valueList);

        assertEquals("grammar hello;", expression1.toAntlr4String());
        assertEquals("H: R;", expression2.toAntlr4String());
        assertEquals("X: 'Hello';", expression3.toAntlr4String());
        assertEquals("h: R;", expression4.toAntlr4String());
        assertEquals("x: 'Hello';", expression5.toAntlr4String());
        assertEquals("//Hello", expression6.toAntlr4String());
        assertEquals("// Hello", expression7.toAntlr4String());
        assertEquals("/*Hello*/", expression8.toAntlr4String());
        assertEquals("/* Hello */", expression9.toAntlr4String());
        assertEquals("X: 'Hello' | WORLD;", expression10.toAntlr4String());
        assertEquals("myRule: 'Hello' | WORLD;", expression11.toAntlr4String());
    }
}
