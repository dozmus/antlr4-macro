package notpure.antlr4.macro.model.parser;

import notpure.antlr4.macro.processor.parser.SimpleParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by pure on 01/08/2016.
 */
public interface ParserExceptionListener {

    /**
     * An event called when {@link SimpleParser#parse(List)} catches an exception.
     * @param source
     * @param ex
     * @throws ParserException
     */
    void parserExceptionOccurred(SimpleParser source, ParserException ex);

    /**
     * A {@link ParserExceptionListener} that prints the error to the CLI and marks than an error has occurred.
     */
    class ParserExceptionLogger implements ParserExceptionListener {

        /**
         * Logger instance.
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(ParserExceptionLogger.class);

        public void parserExceptionOccurred(SimpleParser source, ParserException ex) {
            LOGGER.error("Error parsing: {}", ex.getMessage());
            ex.printStackTrace();
            source.setErrorOccurred(true);
        }
    }

    /**
     * A {@link ParserExceptionListener} that does nothing except mark that an error has occurred.
     */
    class ParserExceptionNop implements ParserExceptionListener {

        public void parserExceptionOccurred(SimpleParser source, ParserException ex) {
            source.setErrorOccurred(true);
        }
    }
}
