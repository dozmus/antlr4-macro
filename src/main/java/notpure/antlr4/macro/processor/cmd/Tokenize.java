package notpure.antlr4.macro.processor.cmd;

import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.processor.Lexer;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by pure on 09/09/2016.
 */
public final class Tokenize implements Command {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(Tokenize.class);

    @Override
    public boolean execute(Context context) throws Exception {
        if (!context.containsKey("tokens")) {
            String inputFileName = (String)context.get("input-file-name");

            // Check input file name
            if (inputFileName == null) {
                throw new Exception("Command context missing input file name.");
            }

            // Process file
            final List<Token> tokens;

            try (InputStream inputStream = new FileInputStream(inputFileName)) {
                Lexer lexer = new Lexer();
                lexer.tokenize(inputStream);
                tokens = lexer.getTokens();
                context.put("tokens", tokens);
            } catch (FileNotFoundException e) {
                LOGGER.warn("File not found: '{}'", inputFileName);
                return true;
            } catch (IOException e) {
                LOGGER.warn("IOException occurred while processing file: '{}'", inputFileName);
                return true;
            }
        }
        return false;
    }
}
