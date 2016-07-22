package notpure.antlr4.macro.processor.util;

import java.io.InputStream;
import java.util.Scanner;

/**
 * A stream of characters, at its core a {@link Scanner} wrapper. The values are returned as {@link String} of length 1.
 */
public final class CharStream {

    private final Scanner scanner;

    public CharStream(InputStream inputStream) {
        scanner = new Scanner(inputStream);
        scanner.useDelimiter("");
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public String next() {
        return scanner.next();
    }
}
