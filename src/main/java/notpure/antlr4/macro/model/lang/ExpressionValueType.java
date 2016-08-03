package notpure.antlr4.macro.model.lang;

/**
 * The type of a {@link ExpressionValueType}.
 */
public enum ExpressionValueType {
    /**
     * A raw message, such as a grammar name.
     */
    RAW,
    /**
     * A rule reference, in form `{ident}={rule}` or `{rule}`.
     */
    RULE_REFERENCE,
    /**
     * A string in the format, `'{string}'` or `"{string}"`.
     */
    STRING,
    /**
     * 'OR' in a grammar rule's value, i.e. `|`.
     */
    ALTERNATOR
}
