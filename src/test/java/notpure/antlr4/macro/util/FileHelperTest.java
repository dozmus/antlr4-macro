package notpure.antlr4.macro.util;

import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FileHelper}.
 */
public final class FileHelperTest {

    /**
     * Creates a Scanner using {@link FileHelper#stringStream(String)} and ensures that the input matches the output.
     */
    private static void assertStringStreamOutput(String input) {
        // Generate output
        StringBuilder actualOutput = new StringBuilder();

        try (Scanner in = new Scanner(FileHelper.stringStream(input))) {
            in.useDelimiter("");

            // Read all
            while (in.hasNext()) {
                actualOutput.append(in.next());
            }
        }

        // Compare results
        assertEquals(input, actualOutput.toString());
    }

    /**
     * Parses file name using {@link FileHelper#parseFileName(String)} and then compares this value to the expected
     * output.
     */
    private static void assertParseFileName(String expectedOutput, String input) {
        assertEquals(expectedOutput, FileHelper.parseFileName(input));
    }

    /**
     * Tests {@link FileHelper#parseFileName(String)}.
     */
    @Test
    public void testParseFileName() {
        assertParseFileName("C:/ANTLR4/antlr4-macro", "C:/ANTLR4/antlr4-macro.jar");
        assertParseFileName("/ANTLR4/antlr4-macro", "/ANTLR4/antlr4-macro.jar");
        assertParseFileName("ANTLR4/antlr4-macro", "ANTLR4/antlr4-macro.jar");
        assertParseFileName("C:/ANTLR4/antlr4.macro", "C:/ANTLR4/antlr4.macro.jar");
        assertParseFileName("/ANTLR4/antlr4.macro", "/ANTLR4/antlr4.macro.jar");
        assertParseFileName("ANTLR4/antlr4.macro", "ANTLR4/antlr4.macro.jar");
        assertParseFileName("antlr4macro", "antlr4macro");
    }

    /**
     * Tests {@link FileHelper#stringStream(String)}.
     */
    @Test
    public void testStringStream() {
        assertStringStreamOutput("hello world");
        assertStringStreamOutput("hello100world");
        assertStringStreamOutput("hello\r\nworld");
        assertStringStreamOutput("hello100world;!!093");
        assertStringStreamOutput("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileNameForEmptyString() {
        FileHelper.parseFileName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFileNameForNullReference() {
        FileHelper.parseFileName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringStreamForNullReference() {
        FileHelper.stringStream(null);
    }
}
