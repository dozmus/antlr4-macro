package notpure.antlr4.macro.model.macro;

/**
 * Created by pure on 01/09/2016.
 */
public final class MissingMacroRuleException extends Exception {

    public MissingMacroRuleException(String reference) {
        super("Unable to locate macro rule for macro identifier `" + reference + "`.");
    }
}
