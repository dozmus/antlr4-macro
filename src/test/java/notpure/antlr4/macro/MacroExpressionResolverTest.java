package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.processor.macro.MacroExpressionResolver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * A set of tests for {@link MacroExpressionResolver}.
 */
public final class MacroExpressionResolverTest {

    /**
     * Tests resolving a macro which requires none.
     */
    private static void assertResolveOfNoOperation(Expression input) {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(input);

        List<Expression> outputExpressions;

        try {
            outputExpressions = MacroExpressionResolver.resolve(expressionList);
        } catch (Exception ignored) {
            fail("Exception occurred while resolving valid macro expression.");
            return; // to suppress compile error
        }

        assertEquals(1, outputExpressions.size());
        assertEquals(input, outputExpressions.get(0));
    }

    private static void assertResolveOfCyclicReference(Expression input) throws Exception {
        List<Expression> expressionList = new ArrayList<>();
        expressionList.add(input);
        MacroExpressionResolver.resolve(expressionList);
    }

    private static void assertResolveOfCyclicReference(List<Expression> input) throws Exception {
        List<Expression> expressionList = input.stream().collect(Collectors.toList());
        MacroExpressionResolver.resolve(expressionList);
    }

    @Test
    public void testSimpleMacroExpressionResolving() {
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "hello",
                new ExpressionValue(ExpressionValueType.STRING, "world")));
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "hello",
                new ExpressionValue(ExpressionValueType.STRING, "")));
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "hello",
                new ExpressionValue(ExpressionValueType.REGEX_GROUP, "[a-z]+")));
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "hello",
                new ExpressionValue(ExpressionValueType.STRING, "world")));
    }

    @Test
    public void testComplexMacroExpressionResolving() {
        List<ExpressionValue> values = new ArrayList<>();
        values.add(new ExpressionValue(ExpressionValueType.STRING, "hello"));
        values.add(new ExpressionValue(ExpressionValueType.ALTERNATOR, "|"));
        values.add(new ExpressionValue(ExpressionValueType.STRING, "world"));
        assertResolveOfNoOperation(new Expression(ExpressionType.MACRO_RULE, "HELLO_WORLD", values));
    }

    @Test(expected = Exception.class)
    public void testCyclicMacroExpressionResolving1() throws Exception {
        assertResolveOfCyclicReference(new Expression(ExpressionType.MACRO_RULE, "HELLO_WORLD",
                new ExpressionValue(ExpressionValueType.RULE_REFERENCE, "HELLO_WORLD")));
    }

    @Test(expected = Exception.class)
    public void testCyclicMacroExpressionResolving2() throws Exception {
        List<ExpressionValue> values = new ArrayList<>();
        values.add(new ExpressionValue(ExpressionValueType.STRING, "hello"));
        values.add(new ExpressionValue(ExpressionValueType.ALTERNATOR, "|"));
        values.add(new ExpressionValue(ExpressionValueType.STRING, "world"));
        values.add(new ExpressionValue(ExpressionValueType.ALTERNATOR, "|"));
        values.add(new ExpressionValue(ExpressionValueType.RULE_REFERENCE, "HELLO_WORLD"));
        assertResolveOfCyclicReference(new Expression(ExpressionType.MACRO_RULE, "HELLO_WORLD", values));
    }

    @Test(expected = Exception.class)
    public void testCyclicMacroExpressionResolving3() throws Exception {
        List<Expression> values = new ArrayList<>();
        values.add(new Expression(ExpressionType.MACRO_RULE, "HELLO_WORLD",
                new ExpressionValue(ExpressionValueType.RULE_REFERENCE, "BYE_WORLD")));
        values.add(new Expression(ExpressionType.MACRO_RULE, "BYE_WORLD",
                new ExpressionValue(ExpressionValueType.RULE_REFERENCE, "HELLO_WORLD")));
        assertResolveOfCyclicReference(values);
    }
}
