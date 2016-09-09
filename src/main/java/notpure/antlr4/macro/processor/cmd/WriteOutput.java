package notpure.antlr4.macro.processor.cmd;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by pure on 09/09/2016.
 */
public final class WriteOutput implements Command {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(WriteOutput.class);

    /**
     * Attempts to overwrite the content of the input file.
     * @param antlr4Code The content to write.
     * @return True if successful, false otherwise.
     */
    public static boolean writeOutput(String inFileName, String outFileName, String antlr4Code) {
        File file = new File(outFileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Error occurred while creating file: '{}'", inFileName);
                return false;
            }
        }

        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(antlr4Code);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("IOException occurred while processing file: '{}'", inFileName);
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(Context context) throws Exception {
        if (!context.containsKey("wrote-output") || !((boolean) context.get("wrote-output"))) {
            String inputFileName = (String)context.get("input-file-name");
            String outputFileName = (String)context.get("output-file-name");
            String antlrCode = (String)context.get("antlr-code");

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Writing output to file '{}'->'{}'", inputFileName, outputFileName);
            }
            writeOutput(inputFileName, outputFileName, antlrCode);
            context.put("wrote-output", true);
        }
        return false;
    }
}
