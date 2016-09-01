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
     * A RegEx Group in the format `[...]`, which can end with a plus or astericks.
     */
    REGEX_GROUP,
    /**
     * 'OR' in a grammar rule's value, i.e. `|`.
     */
    ALTERNATOR,
    /**
     * 'Pipeline' in a grammar rule's value, i.e. `->`.
     */
    PIPELINE,
    /**
     * The output to redirect input to, i.e. in `a -> skip` this would be `skip`.
     */
    OUTPUT_REDIRECT
}
