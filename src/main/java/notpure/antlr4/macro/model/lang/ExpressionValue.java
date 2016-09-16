package notpure.antlr4.macro.model.lang;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The value of an {@link Expression}.
 */
public final class ExpressionValue implements Antlr4Serializable {

    private final ExpressionValueType type;
    private final String identifier;
    private final String value;

    public ExpressionValue(ExpressionValueType type, String value) {
        this(type, null, value);
    }

    public ExpressionValue(ExpressionValueType type, String identifier, String value) {
        this.type = type;
        this.identifier = identifier;
        this.value = value;
    }

    public ExpressionValueType getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type.name() + "(" + (identifier == null ? "" : identifier + "=") + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return Optional.ofNullable(obj)
                .filter($ -> $ instanceof ExpressionValue)
                .map($ -> (ExpressionValue)$)
                .filter($ -> $.getValue() != null && value != null && $.getValue().equals(value))
                .filter($ -> Objects.equals($.getIdentifier(), identifier))
                .isPresent();
    }

    public static boolean equals(List<ExpressionValue> v1, List<ExpressionValue> v2) {
        // compare size
        if (v1.size() != v2.size())
            return false;

        // compare elements
        for (int i = 0; i < v1.size(); i++) {
            if (!v1.get(i).equals(v2.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toAntlr4String() {
        switch (type) {
            case RAW:
            case RULE_REFERENCE:
            case REGEX_GROUP:
                return identifier == null ? getValue() : identifier + "=" + getValue();
            case PIPELINE:
            case OUTPUT_REDIRECT:
            case ALTERNATOR:
            case PLUS:
            case STAR:
            case QUESTION_MARK:
                return getValue();
            case STRING:
                return identifier == null ? String.format("'%s'", getValue())
                        : String.format("%s='%s'", identifier, getValue());
            default:
                return null;
        }
    }
}
