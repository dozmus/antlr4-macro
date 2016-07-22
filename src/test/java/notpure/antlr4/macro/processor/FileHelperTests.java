package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.util.FileHelper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link FileHelper}.
 */
public final class FileHelperTests {

    /**
     * Tests {@link FileHelper#parseFileName(String)}.
     */
    @Test
    public void testParseFileName() {
        assertEquals("C:/ANTLR4/antlr4-macro", FileHelper.parseFileName("C:/ANTLR4/antlr4-macro.jar"));
        assertEquals("/ANTLR4/antlr4-macro", FileHelper.parseFileName("/ANTLR4/antlr4-macro.jar"));
        assertEquals("C:/ANTLR4/antlr4.macro", FileHelper.parseFileName("C:/ANTLR4/antlr4.macro.jar"));
        assertEquals("/ANTLR4/antlr4.macro", FileHelper.parseFileName("/ANTLR4/antlr4.macro.jar"));
        assertEquals("antlr4macro", FileHelper.parseFileName("antlr4macro"));
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
