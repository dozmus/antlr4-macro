package com.purecs.antlr4.macro.lang;

import com.purecs.antlr4.macro.parse.Antlr4MacroRuleContentParser;
import com.purecs.antlr4.macro.util.FilePosition;

import java.util.ArrayList;
import java.util.List;

public class MacroRule {

    private final String identifier;
    private final String content;
    private final FilePosition filePosition;
    private final List<String> parameters;

    public MacroRule(String identifier, String content, FilePosition filePosition) {
        this(identifier, content, filePosition, new ArrayList<>());
    }

    public MacroRule(String identifier, String content, FilePosition filePosition, List<String> parameters) {
        this.identifier = identifier;
        this.content = content;
        this.filePosition = filePosition;
        this.parameters = parameters;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getContent() {
        return content;
    }

    public FilePosition getFilePosition() {
        return filePosition;
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MacroRule{");
        sb.append("identifier='").append(identifier).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", filePosition=").append(filePosition);
        sb.append(", parameters=").append(parameters);
        sb.append('}');
        return sb.toString();
    }

    public boolean accepts(MacroUse use) {
        return use.getIdentifier().equals(identifier) && use.getArgumentFilePositions().size() == parameters.size();
    }

    public String apply(List<String> arguments) {
        String out = content;

        for (int i = 0; i < arguments.size(); i++) {
            String argument = arguments.get(i);
            String parameter = parameters.get(i);
            out = Antlr4MacroRuleContentParser.apply(out, parameter, argument);
        }
        return out;
    }
}
