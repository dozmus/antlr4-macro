package notpure.antlr4.macro.model.macro;

/**
 * Created by pure on 01/09/2016.
 */
public class MacroResolverException extends Exception {

    public MacroResolverException(String message) {
        super(message);
    }

    public static final class CyclicMacroRuleReferenceException extends MacroResolverException {

        public CyclicMacroRuleReferenceException(String identifier) {
            super("Cyclic reference in rule: " + identifier);
        }
    }

    public static final class MissingMacroRuleReferenceException extends MacroResolverException {

        public MissingMacroRuleReferenceException(String identifier, String reference) {
            super("Reference `" + reference + "` not found in rule: " + identifier);
        }
    }
}
