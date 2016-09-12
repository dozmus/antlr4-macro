package notpure.antlr4.macro.processor.cmd;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.processor.parser.SimpleParser;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by pure on 09/09/2016.
 */
public final class Parse implements Command {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(Parse.class);

    @Override
    public boolean execute(Context context) throws Exception {
        if (!context.containsKey("expressions")) {
            List<Token> tokens = (List<Token>)context.get("tokens");

            // Check tokens list
            if (tokens == null) {
                throw new Exception("Command context missing input file tokens.");
            }

            // Debug output
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Parsing tokens into expressions (token-len={})", tokens.size());
            }

            // Process tokens into expressions
            SimpleParser parser = new SimpleParser();
            parser.parse(tokens);
            List<Expression> expressions = parser.getExpressions();
            context.put("expressions", expressions);
        }
        return false;
    }
}
