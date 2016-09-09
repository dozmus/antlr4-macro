package notpure.antlr4.macro.processor.cmd;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.processor.macro.MacroExpressionResolver;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pure on 09/09/2016.
 */
public final class ResolveMacroExpressions implements Command {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ResolveMacroExpressions.class);

    @Override
    public boolean execute(Context context) throws Exception {
        if (!context.containsKey("resolved-macro-expressions")) {
            List<Expression> expressions = (List<Expression>)context.get("expressions");
            List<Expression> macroExpressions = expressions.stream()
                    .filter(expr -> expr.getType() == ExpressionType.MACRO_RULE)
                    .collect(Collectors.toList());

            // Debug output
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Resolving macro definitions (expr-len={}, macro-expr-len={})", expressions.size(),
                        macroExpressions.size());
            }

            // Resolve macro expressions
            try {
                macroExpressions = MacroExpressionResolver.resolve(macroExpressions);
                context.put("resolved-macro-expressions", macroExpressions);
            } catch (Exception ex) {
                String msg = "Error resolving macro definition: " + ex.getMessage();
                System.out.println(msg);
                LOGGER.warn(msg);
            }
        }
        return false;
    }
}
