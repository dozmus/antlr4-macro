package notpure.antlr4.macro.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
     * Gets all files recursively in target directory, with given file extension.
     *
     * @param ext The file extension to look for, e.g. '.g4'.
     * @return List of full file names.
     */
    public static List<String> getFileNames(List<String> fileNames, Path dir, String ext) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            // Iterate over paths
            for (Path path : stream) {
                if (path.toFile().isDirectory()) {
                    getFileNames(fileNames, path, ext);
                } else {
                    String fileName = path.toAbsolutePath().toString();

                    if (fileName.endsWith(ext))
                        fileNames.add(path.toAbsolutePath().toString());
                }
            }
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
}
