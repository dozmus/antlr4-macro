package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.macro.MacroResolverException;
import notpure.antlr4.macro.model.macro.MacroResolverException.CyclicMacroRuleReferenceException;
import notpure.antlr4.macro.model.macro.MacroResolverException.MissingMacroRuleReferenceException;
import notpure.antlr4.macro.model.macro.MissingMacroRuleException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Takes a list of macro expressions and replaces its expression values with their expanded equivalent.
 */
public final class ExpressionProcessor {

    public static List<Expression> resolveMacros(List<Expression> inputExpressions) throws MacroResolverException {
        List<Expression> outputExpressions = new ArrayList<>();

        for (Expression expr : inputExpressions) {
            outputExpressions.add(resolveMacros(inputExpressions, expr));
        }
        return outputExpressions;
    }

    private static Expression resolveMacros(List<Expression> inputExpressions, Expression expr) throws MacroResolverException {
        boolean resolved = true;

        while (resolved) {
            resolved = false;

            for (int i = 0; i < expr.getValues().size(); i++) {
                ExpressionValue val = expr.getValues().get(i);

                if (val.getType() == ExpressionValueType.RULE_REFERENCE && val.getValue().startsWith("#")) {
                    String reference = val.getValue();

                    // Check if cyclic reference
                    if (reference.equals(expr.getIdentifier())) {
                        throw new CyclicMacroRuleReferenceException(expr.getIdentifier());
                    } else { // resolveMacros
                        // Get value
                        List<ExpressionValue> resolvedValues = getExpressionValue(inputExpressions, reference);

                        // Insert into list
                        if (resolvedValues == null) {
                            throw new MissingMacroRuleReferenceException(expr.getIdentifier(), reference);
                        } else {
                            expr.getValues().remove(i);

                            for (int j = 0; j < resolvedValues.size(); j++) {
                                expr.getValues().add(i + j, resolvedValues.get(j));
                            }
                            resolved = true;
                            break;
                        }
                    }
                }
            }
        }
        return expr;
    }

    private static List<ExpressionValue> getExpressionValue(List<Expression> expressions, String reference) {
        Optional<Expression> expression = expressions.stream()
                .filter(expr -> expr.getIdentifier() != null)
                .filter(expr -> expr.getIdentifier().startsWith("#"))
                .filter(expr -> expr.getIdentifier().equals(reference))
                .findFirst();
        return expression.isPresent() ? expression.get().getValues() : null;
    }

    public static List<Expression> applyMacros(List<Expression> inputExpressions, List<Expression> expandedMacroExpressions)
            throws MissingMacroRuleException {
        List<Expression> outputExpressions = new ArrayList<>();

        for (Expression expr : inputExpressions) {
            if (expr.getType() == ExpressionType.LEXER_RULE || expr.getType() == ExpressionType.PARSER_RULE) {
                outputExpressions.add(applyMacros(expandedMacroExpressions, expr));
            } else if (expr.getType() == ExpressionType.SINGLE_LINE_COMMENT) {
                String val = expr.getValues().get(0).getValue();

                if (!val.startsWith("/")) { // Ignore 'macro' comments
                    outputExpressions.add(expr);
                }
            } else if (expr.getType() != ExpressionType.MACRO_RULE) { // Ignore macro rules
                outputExpressions.add(expr);
            }
        }
        return outputExpressions;
    }

    private static Expression applyMacros(List<Expression> expandedMacroExpressions, Expression expr)
            throws MissingMacroRuleException {
        boolean resolved = true;

        while (resolved) {
            resolved = false;

            for (int i = 0; i < expr.getValues().size(); i++) {
                ExpressionValue val = expr.getValues().get(i);

                if (val.getType() == ExpressionValueType.RULE_REFERENCE && val.getValue().startsWith("#")) {
                    String reference = val.getValue().substring(1);
                    Expression macroExpr = getMacroExpr(expandedMacroExpressions, reference);

                    if (macroExpr != null) {
                        expr.getValues().remove(i);

                        for (int j = 0; j < macroExpr.getValues().size(); j++) {
                            expr.getValues().add(i + j, macroExpr.getValues().get(j));
                        }
                        resolved = true;
                    } else {
                        throw new MissingMacroRuleException(reference);
                    }
                }
            }
        }
        return expr;
    }

    private static Expression getMacroExpr(List<Expression> expandedMacroExpressions, String ref) {
        for (Expression expr : expandedMacroExpressions)
            if (expr.getIdentifier().equals(ref))
                return expr;
        return null;
    }
}
