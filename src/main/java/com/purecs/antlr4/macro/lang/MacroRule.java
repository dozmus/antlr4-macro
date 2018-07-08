package com.purecs.antlr4.macro.lang;

import com.purecs.antlr4.macro.util.FilePosition;

import java.util.ArrayList;
import java.util.List;

public class MacroRule extends AbstractMacroRule {

    private final FilePosition filePosition;

    public MacroRule(String identifier, String content, FilePosition filePosition) {
        this(identifier, content, filePosition, new ArrayList<>());
    }

    public MacroRule(String identifier, String content, FilePosition filePosition, List<String> parameters) {
        super(identifier, content, parameters);
        this.filePosition = filePosition;
    }

    public FilePosition getFilePosition() {
        return filePosition;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MacroRule{");
        sb.append("filePosition=").append(filePosition).append(",");
        sb.append("super=").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
