package notpure.antlr4.macro.processor.util;

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
 * File helper utility functions.
 */
public final class FileHelper {

    /**
     * Gets all files recursively in target directory, with given file extension.
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
     * @param in
     * @return
     */
    public static String parseFileName(String in) {
        int lastDot = in.lastIndexOf('.');
        return in.substring(0, lastDot);
    }

    public static InputStream stringString(String s) {
        return new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
    }
}
