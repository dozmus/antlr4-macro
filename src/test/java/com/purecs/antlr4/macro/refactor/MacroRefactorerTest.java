package com.purecs.antlr4.macro.refactor;

import com.purecs.antlr4.macro.lang.MacroUse;
import com.purecs.antlr4.macro.lang.RedefinedMacroRuleException;
import com.purecs.antlr4.macro.util.FilePosition;
import com.purecs.antlr4.macro.parse.Parser;
import com.purecs.antlr4.macro.parse.ParserFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MacroRefactorerTest {

    private static final Path SAMPLES_DIRECTORY = Paths.get("src/test/resources/grammars/");
    private static final Path BAD_SAMPLES_DIRECTORY = Paths.get("src/test/resources/badgrammars/");

    @Test
    public void testRemoveMacroRules() throws IOException {
        Path file = Paths.get(SAMPLES_DIRECTORY.toString(), "Hello.mg4");

        // Read file
        String content = new String(Files.readAllBytes(file));

        // Parse file
        Parser parser = ParserFactory.parse(file);

        // Old indices
        List<FilePosition> initialFilePositions = new ArrayList<>();
        List<List<FilePosition>> initialFileArgumentPositions = new ArrayList<>();

        List<MacroUse> m = parser.getMacroUses();

        for (int i = 0; i < m.size(); i++) {
            MacroUse use = m.get(i);
            // Entire rule
            initialFilePositions.add((FilePosition) use.getFilePosition().clone());

            // Arguments
            initialFileArgumentPositions.add(new ArrayList<>());

            for (FilePosition fp : use.getArgumentFilePositions()) {
                initialFileArgumentPositions.get(i).add((FilePosition) fp.clone());
            }
        }

        // Generate output
        String actual = new MacroRefactorer().removeMacroRules(content, parser.getMacros(), parser.getMacroUses());

        // Expected output
        String expected = "grammar Hello;\r\n"
                + "\r\n"
                + "// macro rules\r\n"
                + "\r\n"
                + "\r\n"
                + "\r\n"
                + "\r\n"
                + "// parser rules\r\n"
                + "r1: 'Hello';\r\n"
                + "r2: #HELLO_WORLD;\r\n"
                + "r3: #HELLO_WORLD '!';\r\n"
                + "r4: #DUMMY('hello');\r\n"
                + "r5: #DUMMY(r1, r2);\r\n"
                + "r6: #DUMMY(r2 'hello');\r\n"
                + "r7: 'Hello' #DUMMY(r1 'World');\r\n"
                + "\r\n"
                + "// lexer rules\r\n"
                + "L1: 'Hello';\r\n"
                + "L2: #HELLO_WORLD;\r\n"
                + "L3: #HELLO_WORLD '!';\r\n"
                + "L4: #DUMMY('hello');\r\n"
                + "L5: #DUMMY(L1, L2);\r\n"
                + "L6: #DUMMY(L2 'hello');\r\n"
                + "L7: 'Hello' #DUMMY(L1 'World');\r\n"
                + "\r\n"
                + "ID: [a-z]+;\r\n"
                + "WS: [ \\t\\r\\n]+ -> skip;\r\n";

        // Assert output
        assertEquals(expected, actual);

        // Expected macro use indices
        int deltaIdx = 59; // all the macro rules to remove are at the start, so we can do a simple idx comparison
        // TODO ^ is value correct?

        // Assert macro use indices
        List<MacroUse> macroUses = parser.getMacroUses();

        // Sort in correct order so the test works
        macroUses.sort(Comparator.comparingInt(r -> r.getFilePosition().getStartIdx()));

        for (int i = 0; i < macroUses.size(); i++) {
            MacroUse use = macroUses.get(i);

            // Compare entire rule
            Assert.assertEquals(initialFilePositions.get(i).subtract(deltaIdx), use.getFilePosition());

            // Compare arguments
            List<FilePosition> argumentFilePositions = use.getArgumentFilePositions();

            for (int j = 0; j < argumentFilePositions.size(); j++) {
                FilePosition fp = argumentFilePositions.get(j);
                List<FilePosition> list = initialFileArgumentPositions.get(i);

                if (list.size() > 0) {
                    assertEquals(list.get(j).subtract(deltaIdx), fp);
                }
            }
        }
    }

    @Test
    public void applyRefactorsHelloGrammar() {
        // Expected output
        String expected = "grammar Hello;\r\n"
                + "\r\n"
                + "// macro rules\r\n"
                + "\r\n"
                + "\r\n"
                + "\r\n"
                + "\r\n"
                + "// parser rules\r\n"
                + "r1: 'Hello';\r\n"
                + "r2: 'Hello World';\r\n"
                + "r3: 'Hello World' '!';\r\n"
                + "r4: 'hello';\r\n"
                + "r5: r1 r2;\r\n"
                + "r6: r2 'hello';\r\n"
                + "r7: 'Hello' r1 'World';\r\n"
                + "\r\n"
                + "// lexer rules\r\n"
                + "L1: 'Hello';\r\n"
                + "L2: 'Hello World';\r\n"
                + "L3: 'Hello World' '!';\r\n"
                + "L4: 'hello';\r\n"
                + "L5: L1 L2;\r\n"
                + "L6: L2 'hello';\r\n"
                + "L7: 'Hello' L1 'World';\r\n"
                + "\r\n"
                + "ID: [a-z]+;\r\n"
                + "WS: [ \\t\\r\\n]+ -> skip;\r\n";

        // Assert output
        assertRefactorResult(expected, "Hello.mg4");
    }

    @Test
    public void applyRefactorsInbuiltFunctions() {
        // Expected output
        String expected = "grammar InbuiltFunctions;\r\n"
                + "\r\n"
                + "// parser rules\r\n"
                + "r1: 'hello';\r\n"
                + "r2: 'hello' 'world!';\r\n"
                + "r3: 'HELLO';\r\n"
                + "r4: 'HELLO' 'WORLD!';\r\n"
                + "r5: 'Player' NUMBER (',' 'Player' NUMBER)*;\r\n"
                + "r6: 'Player' NUMBER ('|' 'Player' NUMBER)*;\r\n"
                + "\r\n"
                + "// lexer rules\r\n"
                + "L1: 'hello';\r\n"
                + "L2: 'hello' 'world!';\r\n"
                + "L3: 'HELLO';\r\n"
                + "L4: 'HELLO' 'WORLD!';\r\n"
                + "L5: 'Player' [0-9]+ (',' 'Player' [0-9]+)*;\r\n"
                + "L6: 'Player' [0-9]+ ('|' 'Player' [0-9]+)*;\r\n"
                + "\r\n"
                + "NUMBER: [0-9]+;\r\n";

        // Assert output
        assertRefactorResult(expected, "InbuiltFunctions.mg4");
    }

    @Test(expected = RedefinedMacroRuleException.class)
    public void testRedefineMacroRule1() {
        assertRefactorResult(null, "RedefineMacroRule1.mg4", BAD_SAMPLES_DIRECTORY);
    }

    @Test(expected = RedefinedMacroRuleException.class)
    public void testRedefineMacroRule2() {
        assertRefactorResult(null, "RedefineMacroRule2.mg4", BAD_SAMPLES_DIRECTORY);
    }

    private static void assertRefactorResult(String expected, String fileName) {
        assertRefactorResult(expected, fileName, SAMPLES_DIRECTORY);
    }

    private static void assertRefactorResult(String expected, String fileName, Path directory) {
        try {
            Path file = Paths.get(directory.toString(), fileName);

            // Read file
            String content = new String(Files.readAllBytes(file));

            // Parse file
            Parser parser = ParserFactory.parse(file);

            // Generate output
            String actual = new MacroRefactorer().refactor(content, parser.getMacros(), parser.getMacroUses());

            // Assert output
            assertEquals(expected, actual);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
}
