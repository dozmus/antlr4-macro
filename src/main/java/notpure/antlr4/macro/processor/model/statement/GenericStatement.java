package notpure.antlr4.macro.processor.model.statement;

/**
 * A generic ANTLR4 statement.
 */
public final class GenericStatement extends Statement {

    private final String identifierName;
    private final String identifierValue;
    private final StatementType statementType;

    public GenericStatement(String identifierName, String identifierValue, StatementType statementType) {
        super(GenericStatement.class.getSimpleName(), identifierName + ":" + identifierValue);
        this.identifierName = identifierName;
        this.identifierValue = identifierValue;
        this.statementType = statementType;
    }

    public String getIdentifierName() {
        return identifierName;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GenericStatement))
            return false;
        GenericStatement other = (GenericStatement)obj;
        return other.getIdentifierName().equals(identifierName)
                && other.getIdentifierValue().equals(identifierValue)
                && other.getName().equals(getName())
                && other.getStatementType().equals(statementType);
    }
}
