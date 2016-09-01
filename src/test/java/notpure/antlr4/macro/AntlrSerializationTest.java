package notpure.antlr4.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static notpure.antlr4.macro.model.lang.ExpressionType.*;
import static notpure.antlr4.macro.model.lang.ExpressionValueType.*;

/**
 * Created by pure on 29/08/2016.
 */
public final class AntlrSerializationTest {

    private static void assertEquals(String antlr4, Expression expr) {
        Assert.assertEquals(antlr4, expr.toAntlr4String());
    }

    @Test
    public void testSerialization() {
        // Grammar name
        String[] grammarTypes = {null, "parser", "lexer"};

        for (String type : grammarTypes) {
            String grammarPart = type == null ? "" : " " + type;
            assertEquals("grammar" + grammarPart + " Hello;", new Expression(GRAMMAR_NAME, type, new ExpressionValue(RAW, "Hello")));
            assertEquals("grammar" + grammarPart + " ;", new Expression(GRAMMAR_NAME, type, new ExpressionValue(RAW, "")));
            assertEquals("grammar" + grammarPart + " 332;", new Expression(GRAMMAR_NAME, type, new ExpressionValue(RAW, "332")));
        }

        // Single-line comments
        assertEquals("//Hello", new Expression(SINGLE_LINE_COMMENT, null, new ExpressionValue(RAW, "Hello")));
        assertEquals("// Hello", new Expression(SINGLE_LINE_COMMENT, null, new ExpressionValue(RAW, " Hello")));
        assertEquals("//", new Expression(SINGLE_LINE_COMMENT, null, new ExpressionValue(RAW, "")));

        // Multi-line comments
        assertEquals("/*Hello*/", new Expression(MULTI_LINE_COMMENT, null, new ExpressionValue(RAW, "Hello")));
        assertEquals("/* Hello */", new Expression(MULTI_LINE_COMMENT, null, new ExpressionValue(RAW, " Hello ")));
        assertEquals("/**/", new Expression(MULTI_LINE_COMMENT, null, new ExpressionValue(RAW, "")));

        // Lexer/parser rules
        assertEquals("MY_RULE: 'HELLO';", new Expression(LEXER_RULE, "MY_RULE", new ExpressionValue(STRING, "HELLO")));
        assertEquals("myRule: 'HELLO';", new Expression(PARSER_RULE, "myRule", new ExpressionValue(STRING, "HELLO")));

        List<ExpressionValue> values = new ArrayList<>();
        values.add(new ExpressionValue(STRING, "HELLO"));
        values.add(new ExpressionValue(ALTERNATOR, "|"));
        values.add(new ExpressionValue(RULE_REFERENCE, "OTHER_RULE"));
        assertEquals("myRule: 'HELLO' | OTHER_RULE;", new Expression(PARSER_RULE, "myRule", values));
    }
}
