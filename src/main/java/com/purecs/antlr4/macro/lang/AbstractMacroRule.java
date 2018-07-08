package com.purecs.antlr4.macro.lang;

import com.purecs.antlr4.macro.parse.Antlr4MacroRuleContentParser;

import java.util.List;
import java.util.Objects;

public abstract class AbstractMacroRule {

    private final String identifier;
    private final String content;
    private final List<String> parameters;

    public AbstractMacroRule(String identifier, String content, List<String> parameters) {
        this.identifier = identifier;
        this.content = content;
        this.parameters = parameters;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getContent() {
        return content;
    }

    public List<String> getParameters() {
        return parameters;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMacroRule that = (AbstractMacroRule) o;
        return Objects.equals(identifier, that.identifier)
                && Objects.equals(content, that.content)
                && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, content, parameters);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractMacroRule{");
        sb.append("identifier='").append(identifier).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", parameters=").append(parameters);
        sb.append('}');
        return sb.toString();
    }
}
