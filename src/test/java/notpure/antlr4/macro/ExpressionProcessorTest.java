package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.macro.MissingMacroRuleException;
import notpure.antlr4.macro.processor.ExpressionProcessor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static notpure.antlr4.macro.model.lang.ExpressionValueType.*;
import static notpure.antlr4.macro.model.lang.ExpressionValueType.RULE_REFERENCE;
import static notpure.antlr4.macro.model.lang.ExpressionValueType.STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * A set of tests for {@link ExpressionProcessor}.
 */
public final class ExpressionProcessorTest {

    /**
     * Tests resolving an expression for which no resolving is required.
     */
    private static void assertResolveOfNoOperation(Expression input) {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(input);

        List<Expression> outputExpressions;

        try {
            outputExpressions = ExpressionProcessor.resolveMacros(expressionList);
        } catch (Exception ignored) {
            fail("Exception occurred while resolving valid macro expression.");
            return;
        }

        assertEquals(1, outputExpressions.size());
        assertEquals(input, outputExpressions.get(0));
    }

    private static void assertResolveOfCyclicReference(Expression... inputExpressions) throws Exception {
        // Create list of expressions
        List<Expression> expressionList = new ArrayList<>();
        Collections.addAll(expressionList, inputExpressions);

        // Attempt to resolve macro definitions
        ExpressionProcessor.resolveMacros(expressionList);
    }

    @Test
    public void testSimpleMacroExpressionResolving() {
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "hello",
                new ExpressionValue(STRING, "world")));
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "hello",
                new ExpressionValue(STRING, "")));
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "hello",
                new ExpressionValue(REGEX_GROUP, "[a-z]+")));
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "hello",
                new ExpressionValue(STRING, "world")));
    }

    @Test
    public void testComplexMacroExpressionResolving() {
        List<ExpressionValue> values = new ArrayList<>();
        values.add(new ExpressionValue(STRING, "hello"));
        values.add(new ExpressionValue(ALTERNATOR, "|"));
        values.add(new ExpressionValue(STRING, "world"));
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "#HELLO_WORLD", values));
    }

    @Test(expected = Exception.class)
    public void testCyclicMacroExpressionResolving1() throws Exception {
        assertResolveOfCyclicReference(new Expression(ExpressionType.MACRO_RULE, "#HELLO_WORLD",
                new ExpressionValue(RULE_REFERENCE, "#HELLO_WORLD")));
    }

    @Test(expected = Exception.class)
    public void testCyclicMacroExpressionResolving2() throws Exception {
        List<ExpressionValue> values = new ArrayList<>();
        values.add(new ExpressionValue(STRING, "hello"));
        values.add(new ExpressionValue(ALTERNATOR, "|"));
        values.add(new ExpressionValue(STRING, "world"));
        values.add(new ExpressionValue(ALTERNATOR, "|"));
        values.add(new ExpressionValue(RULE_REFERENCE, "#HELLO_WORLD"));
        assertResolveOfCyclicReference(new Expression(ExpressionType.MACRO_RULE, "#HELLO_WORLD", values));
    }

    @Test(expected = Exception.class)
    public void testCyclicMacroExpressionResolving3() throws Exception {
        Expression expr1 = new Expression(ExpressionType.MACRO_RULE, "HELLO_WORLD",
                new ExpressionValue(RULE_REFERENCE, "#BYE_WORLD"));
        Expression expr2 = new Expression(ExpressionType.MACRO_RULE, "BYE_WORLD",
                new ExpressionValue(RULE_REFERENCE, "#HELLO_WORLD"));
        assertResolveOfCyclicReference(expr1, expr2);
    }

    @Test
    public void testValidMacroRuleApplication() throws Exception {
        Expression expr1 = new Expression(ExpressionType.MACRO_RULE, "FNAME", new ExpressionValue(STRING, "John"));
        Expression expr2 = new Expression(ExpressionType.LEXER_RULE, "NAME", new ExpressionValue(RULE_REFERENCE, "#FNAME"));
        Expression expectedOutputExpr = new Expression(ExpressionType.LEXER_RULE, "NAME", new ExpressionValue(STRING, "John"));

        List<Expression> macros = new ArrayList<>();
        macros.add(expr1);

        List<Expression> expressions = new ArrayList<>();
        expressions.add(expr2);

        List<Expression> outputExpressions = ExpressionProcessor.applyMacros(expressions, macros);
        assertEquals(1, outputExpressions.size());
        assertEquals(expectedOutputExpr, outputExpressions.get(0));
    }

    @Test
    public void testComplexValidMacroRuleApplication() throws Exception {
        List<ExpressionValue> values = new ArrayList<>();
        values.add(new ExpressionValue(RULE_REFERENCE, "#FNAME"));
        values.add(new ExpressionValue(RULE_REFERENCE, "#LNAME"));

        Expression expr1 = new Expression(ExpressionType.MACRO_RULE, "FNAME", new ExpressionValue(STRING, "John"));
        Expression expr2 = new Expression(ExpressionType.MACRO_RULE, "LNAME", new ExpressionValue(STRING, "Doe"));
        Expression expr3 = new Expression(ExpressionType.LEXER_RULE, "NAME", values);

        List<ExpressionValue> expectedValues = new ArrayList<>();
        expectedValues.add(new ExpressionValue(STRING, "John"));
        expectedValues.add(new ExpressionValue(STRING, "Doe"));
        Expression expectedOutputExpr = new Expression(ExpressionType.LEXER_RULE, "NAME", expectedValues);

        List<Expression> macros = new ArrayList<>();
        macros.add(expr1);
        macros.add(expr2);
        List<Expression> expressions = new ArrayList<>();
        expressions.add(expr3);

        List<Expression> outputExpressions = ExpressionProcessor.applyMacros(expressions, macros);
        assertEquals(1, outputExpressions.size());
        assertEquals(expectedOutputExpr, outputExpressions.get(0));
    }

    @Test(expected = MissingMacroRuleException.class)
    public void testMissingMacroRuleApplication() throws Exception {
        Expression expr = new Expression(ExpressionType.LEXER_RULE, "NAME", new ExpressionValue(RULE_REFERENCE, "#FNAME"));

        List<Expression> macros = new ArrayList<>();
        List<Expression> expressions = new ArrayList<>();
        expressions.add(expr);

        ExpressionProcessor.applyMacros(expressions, macros);
    }
}
