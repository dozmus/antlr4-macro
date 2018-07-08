package com.purecs.antlr4.macro.parse;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Antlr4GrammarTest {

    private static final Path SAMPLES_DIRECTORY = Paths.get("src/test/resources/grammars/");

    @Test
    public void test() throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(SAMPLES_DIRECTORY)) {
            for (Path path : stream) {
                new Antlr4Parser().parse(path);
            }
        }
    }
}
