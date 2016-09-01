package notpure.antlr4.macro.model.lang;

import notpure.antlr4.macro.util.CollectionHelper;
import notpure.antlr4.macro.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A parsed expression.
 */
public final class Expression implements Antlr4Serializable {

    private final String identifier;
    private final List<ExpressionValue> values;
    private final ExpressionType type;

    public Expression(ExpressionType type, String identifier, List<ExpressionValue> values) {
        this.identifier = identifier;
        this.values = values;
        this.type = type;
    }

    public Expression(ExpressionType type, String identifier, ExpressionValue value) {
        this(type, identifier, new ArrayList<>());
        values.add(value);
    }

    public Expression(ExpressionType type, List<ExpressionValue> values) {
        this(type, null, values);
    }

    public Expression(ExpressionType type, ExpressionValue value) {
        this(type, null, new ArrayList<>());
        values.add(value);
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<ExpressionValue> getValues() {
        return values;
    }

    public ExpressionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.name() + "(" + (identifier == null ? "" : identifier + "=")
                + CollectionHelper.toString("ExpressionValue[]", values) + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return Optional.ofNullable(obj)
                .filter($ -> $ instanceof Expression)
                .map($ -> (Expression)$)
                .filter($ -> $.getType() == type)
                .filter($ -> ExpressionValue.equals($.getValues(), values))
                .filter($ -> Objects.equals($.getIdentifier(), identifier))
                .isPresent();
    }

    @Override
    public String toAntlr4String() {
        switch (type) {
            case GRAMMAR_NAME:
                return String.format("grammar %s;", getValues().get(0).getValue());
            case SINGLE_LINE_COMMENT:
                return String.format("//%s", getValues().get(0).getValue());
            case MULTI_LINE_COMMENT:
                return String.format("/*%s*/", getValues().get(0).getValue());
            case LEXER_RULE:
            case PARSER_RULE:
                return String.format("%s: %s;", getIdentifier(), CollectionHelper.toAntlr4String(getValues()));
            case MACRO_RULE:
            default:
                throw new RuntimeException("toAntlr4String() is invalid for ExprType.MACRO_RULE");
        }
    }
}
