package notpure.antlr4.macro.processor.macro;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.macro.MacroResolverException;
import notpure.antlr4.macro.model.macro.MacroResolverException.CyclicMacroRuleReferenceException;
import notpure.antlr4.macro.model.macro.MacroResolverException.MissingMacroRuleReferenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Takes a list of macro expressions and replaces its expression values with their expanded equivalent.
 */
public final class MacroExpressionResolver {

    public static List<Expression> resolve(List<Expression> inputExpressions) throws MacroResolverException {
        List<Expression> outputExpressions = new ArrayList<>();

        for (Expression expr : inputExpressions) {
            outputExpressions.add(resolve(inputExpressions, expr));
        }
        return outputExpressions;
    }

    private static Expression resolve(List<Expression> inputExpressions, Expression expr) throws MacroResolverException {
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
                    } else { // resolve
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
}
