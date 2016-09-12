package notpure.antlr4.macro.processor.parser.expr;

import notpure.antlr4.macro.model.lang.Expression;
import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.lang.ExpressionValue;
import notpure.antlr4.macro.model.lang.ExpressionValueType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.parser.ExpressionParser;
import notpure.antlr4.macro.model.parser.ParserException;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;

/**
 * Parses {@link ExpressionType#GRAMMAR_NAME}.
 */
public final class GrammarNameParser implements ExpressionParser<Expression> {

    private static final String GRAMMAR_TEXT = "grammar";
    private static final String PARSER_TEXT = "parser";
    private static final String LEXER_TEXT = "lexer";
    private static final TokenParserIterator.TokenTarget TARGET_TOKEN = new TokenParserIterator.TokenTarget(
            new Token[]{new Token(TokenDefinition.SEMICOLON)}, false);

    @Override
    public Expression parse(TokenParserIterator it) throws ParserException {
        // Deduce grammar type
        String grammarType = null;

        if (it.hasNext(GRAMMAR_TEXT)) {
            it.skip(GRAMMAR_TEXT.length()); // skip 'grammar'
        }
        else if (it.hasNext(PARSER_TEXT)) {
            grammarType = PARSER_TEXT;
            it.skip(PARSER_TEXT.length()); // skip 'parser'
        }
        else if (it.hasNext(LEXER_TEXT)) {
            grammarType = LEXER_TEXT;
            it.skip(LEXER_TEXT.length()); // skip 'lexer'
        }
        else
            throw new RuntimeException("GrammarNameParser failed parsing header.");

        // Skip initial space
        if (it.skipAllWhitespace() == 0) {
            String gname = "'" + grammarType + "'";
            throw new ParserException(getClass(), "Expected whitespace between "
                    + (grammarType == null ? "'grammar'" : gname) + " and "
                    + (grammarType == null ? "its name" : gname) + ".");
        }

        // Handle grammar name part, for parser/lexer grammar types
        if (grammarType != null) {
            if (it.hasNext(GRAMMAR_TEXT)) {
                it.skip(GRAMMAR_TEXT.length()); // skip 'grammar'
            }

            if (it.skipAllWhitespace() == 0) {
                throw new ParserException(getClass(), "Expected whitespace between 'grammar' and its name.");
            }
        }

        // Parse value
        String value;

        try {
            value = it.aggregateValues(TARGET_TOKEN, true, true);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new ParserException(getClass(), "Expected semi-colon after grammar name.");
        }

        if (value.trim().length() == 0) {
            throw new ParserException(getClass(), "Expected value after grammar name.");
        }
        it.skip(TokenDefinition.SEMICOLON);
        return new Expression(ExpressionType.GRAMMAR_NAME, grammarType, new ExpressionValue(ExpressionValueType.RAW, value));
    }

    @Override
    public boolean validate(TokenParserIterator it) {
        return it.hasNext(GRAMMAR_TEXT) || it.hasNext(PARSER_TEXT) || it.hasNext(LEXER_TEXT);
    }
}
