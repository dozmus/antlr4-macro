package notpure.antlr4.macro.model.lang;

import notpure.antlr4.macro.Main;

import java.util.List;

/**
 * Created by pure on 07/08/2016.
 */
public interface Antlr4Serializable {

    /**
     * Serializes and aggregates all of the input list's values and returns them. Space is inserted between each value.
     */
    static String toAntlr4String(List<ExpressionValue> values) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < values.size(); i++) {
            ExpressionValue val = values.get(i);
            sb.append(val.toAntlr4String());

            if (i + 1 < values.size()) {
                ExpressionValue nextVal = values.get(i + 1);

                if (val.getType() != ExpressionValueType.REGEX_GROUP
                        || (nextVal.getType() != ExpressionValueType.PLUS
                        && nextVal.getType() != ExpressionValueType.STAR)) {
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Serializes and aggregates all of the input {@link Expression}'s and returns the value. CRLF is inserted
     * between each expression.
     */
    static String serializeAll(List<Expression> expressions) {
        StringBuilder sb = new StringBuilder();

        expressions.stream()
                .filter(expr -> {
                    // filter comments if optimizing
                    return !Main.CommandLineFlags.minify
                            || expr.getType() != ExpressionType.SINGLE_LINE_COMMENT
                            && expr.getType() != ExpressionType.MULTI_LINE_COMMENT;
                })
                .forEach(expr -> {
                    sb.append(expr.toAntlr4String());

                    if (!Main.CommandLineFlags.minify) {
                        sb.append("\r\n");
                    }
                });
        return sb.toString();
    }

    /**
     * ANTLR4 code representation of this object.
     */
    String toAntlr4String();
}
