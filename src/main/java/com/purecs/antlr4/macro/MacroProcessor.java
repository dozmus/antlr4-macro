package com.purecs.antlr4.macro;

import com.purecs.antlr4.macro.util.FileHelper;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MacroProcessor {

    private final Main.CLIContext context;

    public MacroProcessor(Main.CLIContext context) {
        this.context = context;
    }

    /**
     * Processes all macro files in the given directory. Recursive traversal can be toggled using
     * {@link Main.CLIContext#recursive}.
     */
    public void processDirectory(String directory) {
        // Aggregate target files
        ArrayList<String> fileNames = new ArrayList<>();
        FileHelper.getFileNames(fileNames, Paths.get(directory), ".mg4", context.isRecursive());

        // Process all files
        fileNames.forEach(this::processFile);
    }

    /**
     * Processes the given macro file.
     */
    public void processFile(String inFileName) {
        // Attempt to resolve directory of input if necessary
        if (!new File(inFileName).exists()) {
            File baseDir = new File(System.getProperty("user.dir"));
            inFileName = new File(baseDir, inFileName).getPath();
        }

        // Process input file
        String outFileName = FileHelper.parseFileName(inFileName) + ".g4";
        MacroFileProcessor processor = new MacroFileProcessor(inFileName, outFileName);
        processor.processFile();
    }
}
