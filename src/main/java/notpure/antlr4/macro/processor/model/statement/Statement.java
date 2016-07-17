package notpure.antlr4.macro.processor.model.statement;

/**
 * A parsed statement.
 */
public class Statement {

    private final String name;
    private final String value;

    public Statement(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
