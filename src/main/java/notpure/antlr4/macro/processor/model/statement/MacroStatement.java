package notpure.antlr4.macro.processor.model.statement;

/**
 * A macro statement.
 */
public final class MacroStatement extends Statement {

    private final String identifierName;
    private final String identifierValue;

    public MacroStatement(String identifierName, String identifierValue) {
        super(MacroStatement.class.getSimpleName(), identifierName + "=" + identifierValue);
        this.identifierName = identifierName;
        this.identifierValue = identifierValue;
    }

    public String getIdentifierName() {
        return identifierName;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }
}
