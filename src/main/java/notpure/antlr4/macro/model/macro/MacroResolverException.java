package notpure.antlr4.macro.model.macro;

/**
 * Thrown if an issue occurs when resolving a macro expression.
 */
public class MacroResolverException extends Exception {

    public MacroResolverException(String message) {
        super(message);
    }

    /**
     * Thrown if a macro rule is cyclic.
     */
    public static final class CyclicMacroRuleReferenceException extends MacroResolverException {

        public CyclicMacroRuleReferenceException(String identifier) {
            super("Cyclic reference in rule: " + identifier);
        }
    }

    /**
     * Thrown if a macro rule contains an identifier which is missing.
     */
    public static final class MissingMacroRuleReferenceException extends MacroResolverException {

        public MissingMacroRuleReferenceException(String parentRule, String identifier) {
            super("Reference `" + identifier + "` not found in rule: " + parentRule);
        }
    }
}
