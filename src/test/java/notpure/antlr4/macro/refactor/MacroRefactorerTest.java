package notpure.antlr4.macro.refactor;

import notpure.antlr4.macro.lang.MacroUse;
import notpure.antlr4.macro.parse.Parser;
import notpure.antlr4.macro.parse.ParserFactory;
import notpure.antlr4.macro.util.FilePosition;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MacroRefactorerTest {

    private static final Path SAMPLES_DIRECTORY = Paths.get("src/test/resources/grammars/");

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
            assertEquals(initialFilePositions.get(i).subtract(deltaIdx), use.getFilePosition());

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
    public void applyRefactors() throws IOException {
        Path file = Paths.get(SAMPLES_DIRECTORY.toString(), "Hello.mg4");

        // Read file
        String content = new String(Files.readAllBytes(file));

        // Parse file
        Parser parser = ParserFactory.parse(file);

        // Generate output
        String actual = new MacroRefactorer().refactor(content, parser.getMacros(), parser.getMacroUses());

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
        assertEquals(expected, actual);
    }
}
