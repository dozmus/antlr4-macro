package notpure.antlr4.macro.model.macro;

/**
 * Thrown if no macro rule is defined for the given macro identifier.
 */
public final class MissingMacroRuleException extends Exception {

    public MissingMacroRuleException(String macroIdentifier) {
        super("Unable to locate macro rule for macro identifier `" + macroIdentifier + "`.");
    }
}
