package notpure.antlr4.macro.processor.cmd;

import notpure.antlr4.macro.model.lang.Antlr4Serializable;
import notpure.antlr4.macro.model.lang.Expression;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by pure on 09/09/2016.
 */
public final class GenerateAntlrCode implements Command {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(GenerateAntlrCode.class);

    @Override
    public boolean execute(Context context) throws Exception {
        if (!context.containsKey("antlr-code")) {
            List<Expression> expressions = (List<Expression>)context.get("output-expressions");

            // Debug output
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Generating antlr code (output-expr-len={})", expressions.size());
            }

            // Generate and put code
            String antlrCode = Antlr4Serializable.serializeAll(expressions);
            context.put("antlr-code", antlrCode);
        }
        return false;
    }
}
