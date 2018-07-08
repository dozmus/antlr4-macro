package com.purecs.antlr4.macro.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * File utility functions.
 */
public final class FileHelper {

    /**
     * Gets all files in target directory, with the given file extension. Returns an empty array list if nothing is
     * found or if an {@link IOException} occurs.
     *
     * @param ext The file extension to look for, e.g. '.g4'.
     * @param recursive If the folder should be recursively traversed.
     * @return List of full file names.
     */
    public static List<String> getFileNames(List<String> fileNames, Path dir, String ext, boolean recursive) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            // Iterate over paths
            stream.forEach(p -> {
                if (p.toFile().isDirectory() && recursive) { // Directory
                    getFileNames(fileNames, p, ext, true);
                } else { // File
                    String fileName = p.toAbsolutePath().toString();

                    if (fileName.endsWith(ext))
                        fileNames.add(fileName);
                }
            });
        } catch (IOException e) {
            return new ArrayList<>();
        }
        return fileNames;
    }

    /**
     * Parses the file name, out of a fully qualified file name.
     * e.g. 'my/dir/file.txt' -> 'my/dir/file'.
     *
     * @throws IllegalArgumentException If fileName is null or empty.
     */
    public static String parseFileName(String fileName) {
        if (fileName == null || fileName.isEmpty())
            throw new IllegalArgumentException("parseFileName method invoked with null or empty input.");
        int lastDot = fileName.lastIndexOf('.');
        return lastDot == -1 ? fileName : fileName.substring(0, lastDot);
    }

    /**
     * Creates a {@link InputStream} for the provided {@link String}.
     *
     * @throws IllegalArgumentException If input is null.
     */
    public static InputStream stringStream(String input) {
        if (input == null)
            throw new IllegalArgumentException("stringStream method invoked with null input.");
        return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Attempts to overwrite the content of the argument file.
     *
     * @param code The content to write.
     */
    public static void writeOutput(Path file, String code) throws IOException {
        if (!Files.exists(file)) {
            Files.createFile(file);
        }

        try (Writer writer = new BufferedWriter(new FileWriter(file.toFile()))) {
            writer.write(code);
            writer.flush();
        }
    }
}
