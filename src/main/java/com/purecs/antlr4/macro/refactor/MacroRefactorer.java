package com.purecs.antlr4.macro.refactor;

import com.purecs.antlr4.macro.lang.AbstractMacroRule;
import com.purecs.antlr4.macro.lang.MacroUse;
import com.purecs.antlr4.macro.lang.inbuilt.*;
import com.purecs.antlr4.macro.util.FilePosition;
import com.purecs.antlr4.macro.lang.MacroRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MacroRefactorer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MacroRefactorer.class);
    private static final List<AbstractMacroRule> inbuiltRules = Arrays.asList(new Upper(), new Lower(),
            new CommaDelimitedList(), new ArbitraryDelimitedList());

    /**
     * Returns the first rule to accept the argument use in the order of rules.
     * Rules defined in the current file are prioritized over the in-built ones.
     */
    private static AbstractMacroRule rule(MacroUse use, List<? extends AbstractMacroRule> rules) {
        for (AbstractMacroRule rule : rules) {
            if (rule.accepts(use)) {
                return rule;
            }
        }

        for (AbstractMacroRule rule : inbuiltRules) {
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
            AbstractMacroRule rule = rule(use, rules);

            // Find argument values
            List<String> args = use.getArgumentFilePositions()
                    .stream()
                    .map(fp -> content.substring(fp.getStartIdx(), fp.getEndIdx()))
                    .collect(Collectors.toList());

            // Apply change
            String s = rule.apply(args);
            out = out.substring(0, use.getFilePosition().getStartIdx()) + s + out.substring(use.getEndIndex() + 1);

            LOGGER.debug("{} args={} => {}", rule, args, s);
        }
        return out;
    }

    /**
     * Returns the argument String, with any argument rules in it removed.
     * It also adjusts the indices of the macro uses, so that they remain consistent.
     */
    public String removeMacroRules(String content, List<MacroRule> rules, List<MacroUse> uses) {
        // Check if no macro rule
        if (rules.size() == 0)
            return content;

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
