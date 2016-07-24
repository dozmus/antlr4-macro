package notpure.antlr4.macro.processor.model.statement;

/**
 * The type of a {@link Statement}.
 */
public enum StatementType {
    GRAMMAR_NAME,
    SINGLE_LINE_COMMENT,
    MULTI_LINE_COMMENT,
    MACRO_RULE,
    PARSER_RULE,
    LEXER_RULE
}
