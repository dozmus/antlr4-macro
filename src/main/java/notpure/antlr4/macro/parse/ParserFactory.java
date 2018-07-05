package notpure.antlr4.macro.parse;

import java.io.IOException;
import java.nio.file.Path;

public class ParserFactory {

    public static Parser create(Path file) {
        if (file.getFileName().toString().endsWith(".mg4")) {
            return new Antlr4Parser();
        }
        throw new IllegalArgumentException("unsupported file");
    }

    public static Parser parse(Path file) throws IOException {
        Parser parser = create(file);
        parser.parse(file);
        return parser;
    }
}
