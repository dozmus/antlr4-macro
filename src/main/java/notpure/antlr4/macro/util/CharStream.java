package notpure.antlr4.macro.util;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;

/**
 * A stream of characters, at its core a {@link Scanner} wrapper. The values are returned as {@link String} of length 1.
 */
public final class CharStream implements Iterator<Character> {

    private final Scanner scanner;

    public CharStream(InputStream inputStream) {
        scanner = new Scanner(inputStream, "UTF-8");
        scanner.useDelimiter("");
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNext();
    }

    @Override
    public Character next() {
        return scanner.next().charAt(0);
    }
}
