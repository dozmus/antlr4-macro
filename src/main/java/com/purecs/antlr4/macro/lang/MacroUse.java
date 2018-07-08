package com.purecs.antlr4.macro.lang;

import com.purecs.antlr4.macro.util.FilePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MacroUse {

    private final String identifier;
    private final FilePosition filePosition;
    private final List<FilePosition> argumentFilePositions;

    public MacroUse(String identifier, FilePosition filePosition) {
        this(identifier, filePosition, new ArrayList<>());
    }

    public MacroUse(String identifier, FilePosition filePosition, List<FilePosition> argumentFilePositions) {
        this.identifier = identifier;
        this.filePosition = filePosition;
        this.argumentFilePositions = argumentFilePositions;
    }

    public String getIdentifier() {
        return identifier;
    }

    public FilePosition getFilePosition() {
        return filePosition;
    }

    public List<FilePosition> getArgumentFilePositions() {
        return argumentFilePositions;
    }

    public int getEndIndex() {
        return argumentFilePositions.size() == 0 ? filePosition.getEndIdx()
                : argumentFilePositions.get(argumentFilePositions.size() - 1).getEndIdx();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MacroUse{");
        sb.append("identifier='").append(identifier).append('\'');
        sb.append(", filePosition=").append(filePosition);
        sb.append(", argumentFilePositions=").append(argumentFilePositions);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Object clone() {
        List<FilePosition> clonedArgumentFilePositions = argumentFilePositions
                .stream()
                .map(fp -> (FilePosition)fp.clone())
                .collect(Collectors.toList());
        return new MacroUse(identifier, (FilePosition)filePosition.clone(), clonedArgumentFilePositions);
    }
}
