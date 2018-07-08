package com.purecs.antlr4.macro.refactor;

import com.purecs.antlr4.macro.lang.MacroUse;
import com.purecs.antlr4.macro.util.FilePosition;
import com.purecs.antlr4.macro.lang.MacroRule;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MacroRefactorer {

    /**
     * Returns the first rule to accept the argument use in the order of rules.
     */
    private static MacroRule rule(MacroUse use, List<MacroRule> rules) {
        for (MacroRule rule : rules) {
            if (rule.accepts(use)) {
                return rule;
            }
        }
        throw new IllegalArgumentException("no matching rule found.");
    }

    public String refactor(String content, List<MacroRule> rules, List<MacroUse> uses) {
        // Remove all macro rules
        content = removeMacroRules(content, rules, uses);

        // Refactor
        content = applyRefactors(content, rules, uses);
        return content;
    }

    /**
     * Returns the argument String with the argument macro rules applied to the argument macro uses.
     */
    private String applyRefactors(final String content, List<MacroRule> rules, List<MacroUse> uses) {
        String out = content;

        // Sort uses in reverse order => no need to update indices
        uses.sort(Comparator.comparingInt(r -> -r.getFilePosition().getStartIdx()));

        for (MacroUse use : uses) {
            // Find corresponding rule
            MacroRule rule = rule(use, rules);

            // Find argument values
            List<String> args = use.getArgumentFilePositions()
                    .stream()
                    .map(fp -> content.substring(fp.getStartIdx(), fp.getEndIdx()))
                    .collect(Collectors.toList());

            // Apply change
            String s = rule.apply(args);
            out = out.substring(0, use.getFilePosition().getStartIdx()) + s + out.substring(use.getEndIndex() + 1);

            System.out.println(rule + "(" + args + ") => " + s);
            System.out.println("content=" + rule.getContent());

            // TODO test
        }
        return out;
    }

    /**
     * Returns the argument String, with any argument rules in it removed.
     * It also adjusts the indices of the macro uses, so that they remain consistent.
     */
    public String removeMacroRules(String content, List<MacroRule> rules, List<MacroUse> uses) {
        // TODO make it work for files w no macro rules + add test
        // Sort rules in reverse order => smaller scope of updating indices
        rules.sort(Comparator.comparingInt(r -> -r.getFilePosition().getStartIdx()));

        // Apply removals
        StringBuilder out = new StringBuilder();
        int endIdx = content.length();

        for (MacroRule rule : rules) {
            // Remove rule
            int removedLength = rule.getFilePosition().length();
            String mod = content.substring(rule.getFilePosition().getEndIdx(), endIdx);
            out.insert(0, mod);

            // Update end idx
            endIdx = rule.getFilePosition().getStartIdx();

            // Adjust indices of uses coming after this rule
            for (MacroUse use : uses) {
                FilePosition fp = use.getFilePosition();

                if (fp.getStartIdx() > rule.getFilePosition().getEndIdx()) {
                    // Entirety
                    fp.setStartIdx(fp.getStartIdx() - removedLength);
                    fp.setEndIdx(fp.getEndIdx() - removedLength);

                    // Arguments
                    for (FilePosition arg : use.getArgumentFilePositions()) {
                        arg.setStartIdx(arg.getStartIdx() - removedLength);
                        arg.setEndIdx(arg.getEndIdx() - removedLength);
                    }
                }
            }
        }

        // Add initial part of content, if the file did not begin with a macro rule
        FilePosition initialRule = rules.get(rules.size() - 1).getFilePosition();
        out.insert(0, content.substring(0, initialRule.getStartIdx()));

        // Return output
        return out.toString();
    }
}
