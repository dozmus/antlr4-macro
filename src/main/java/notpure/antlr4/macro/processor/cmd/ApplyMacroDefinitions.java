package notpure.antlr4.macro.processor.cmd;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.processor.macro.MacroExpressionProcessor;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by pure on 09/09/2016.
 */
public final class ApplyMacroDefinitions implements Command {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplyMacroDefinitions.class);

    @Override
    public boolean execute(Context context) throws Exception {
        if (!context.containsKey("output-expressions")) {
            List<Expression> expressions = (List<Expression>)context.get("expressions");
            List<Expression> macroExpressions = (List<Expression>)context.get("resolved-macro-expressions");

            // Debug output
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Applying macro definitions (expr-len={},macro-expr-len={})", expressions.size(),
                        macroExpressions.size());
            }

            // Apply macro definitions
            try {
                expressions = MacroExpressionProcessor.process(expressions, macroExpressions);
                context.put("output-expressions", expressions);
            } catch (Exception ex) {
                LOGGER.warn("Error applying macro definitions: '{}'", ex.getMessage());
            }
        }
        return false;
    }
}
