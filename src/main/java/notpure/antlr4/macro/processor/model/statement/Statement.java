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

    @Override
    public String toString() {
        return name + "(" + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Statement && obj.toString().equals(toString());
    }
}
