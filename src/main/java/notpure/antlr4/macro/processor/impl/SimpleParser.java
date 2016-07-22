package notpure.antlr4.macro.processor.impl;

import notpure.antlr4.macro.processor.model.Parser;
import notpure.antlr4.macro.processor.model.statement.GenericStatement;
import notpure.antlr4.macro.processor.model.statement.Statement;
import notpure.antlr4.macro.processor.model.statement.StatementType;
import notpure.antlr4.macro.processor.model.token.Token;
import notpure.antlr4.macro.processor.model.token.TokenDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple LL(*) parser.
 */
public final class SimpleParser implements Parser {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleParser.class);
    private static final String FILE_HEADER_TEXT = "grammar";
    private final List<Statement> statements = new ArrayList<>();

    /**
     * Parses the tokens into more complicated ones.
     */
    @Override
    public Parser parse(List<Token> tokens) {
        if (tokens == null)
            throw new IllegalArgumentException("Token list is null.");

        int n = tokens.size();

        for (int idx = 0; idx < n; idx++) {
            Token token = tokens.get(idx);

            // TODO impl/finish
            if (token.getValue().length() != 1)
                continue;

            char c = token.getValue().charAt(0);

            if (c == '/' && idx + 1 < n && tokens.get(idx + 1).getValue().length() == 1
                    && tokens.get(idx + 1).getValue().equals("/")) { // single line comment
                idx += 2; // skip '//'
                idx = parseSingleLineComment(tokens, idx);
            } else if (c == '#') { // macro rule
                idx = parseStatement(tokens, idx, 1, StatementType.MACRO_RULE);
            } else if (Character.isLowerCase(c)) {
                if (seekString(tokens, idx, FILE_HEADER_TEXT)) { // grammar header file
                    idx += FILE_HEADER_TEXT.length(); // skip header text
                    idx = parseFileHeaderStatement(tokens, idx);
                } else { // parser rule
                    idx = parseStatement(tokens, idx, (idx > 4 ? -4 : 0), StatementType.PARSER_RULE);
                }
            } else if (Character.isUpperCase(c)) { // lexer rule
                idx = parseStatement(tokens, idx, (idx > 1 ? -1 : 0), StatementType.LEXER_RULE);
            }
        }
        return this;
    }

    private int parseSingleLineComment(List<Token> tokens, int idx) {
        ParsedToken pair = seekToken(tokens, idx,
                new Token[] { new Token(TokenDefinition.NEW_LINE), new Token(TokenDefinition.EOF) },
                "SingleLineComment", true);
        LOGGER.info("Parsed single line comment: {} [nextIdx={}]", pair.getToken(), pair.getNextIdx());
        statements.add(new Statement("SingleLineComment", pair.getToken().getValue()));
        return pair.getNextIdx();
    }

    private int parseFileHeaderStatement(List<Token> tokens, int idx) {
        // Parse file header, in form 'grammar {name};'.
        ParsedToken pair = seekToken(tokens, idx, new Token(TokenDefinition.SEMICOLON), "FileHeader", true);
        LOGGER.info("Parsed file header: {} [nextIdx={}]", pair.getToken(), pair.getNextIdx());
        statements.add(new Statement("FileHeader", pair.getToken().getValue()));
        return pair.getNextIdx();
    }

    private int parseStatement(List<Token> tokens, int idx, int offset, StatementType type) {
        String statementName = type.toString();

        // Parse identifier
        ParsedToken pair1 = seekToken(tokens, idx + offset,
                new Token(TokenDefinition.COLON), statementName + "_Identifier", true);
        LOGGER.info("Parsed identifier for {}: {} [nextIdx={}]", statementName, pair1.getToken(), pair1.getNextIdx());
        idx = pair1.getNextIdx();

        // Parse value
        ParsedToken pair2 = seekToken(tokens, idx, new Token(TokenDefinition.SEMICOLON), statementName + "_Value", true);
        LOGGER.info("Parsed value for {}: {} [nextIdx={}]", statementName, pair2.getToken(), pair2.getNextIdx());
        idx = pair2.getNextIdx();

        // Add to statements list and continue
        GenericStatement statement = fromParsedToken(pair1, pair2, type);
        statements.add(statement);
        LOGGER.info("Parsed statement {}: {}={}", statementName, statement.getIdentifierName(), statement.getIdentifierValue());
        return idx;
    }

    private static GenericStatement fromParsedToken(ParsedToken pair1, ParsedToken pair2, StatementType type) {
        return new GenericStatement(pair1.getToken().getValue(), pair2.getToken().getValue(), type);
    }

    @Override
    public List<Statement> getStatements() {
        return statements;
    }

    private static boolean seekString(List<Token> tokens, int currentIdx, String target) {
        String outputValue = "";

        for (int i = currentIdx; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (outputValue.length() > target.length()) {
                return false;
            } else if (outputValue.length() == target.length()) {
                return outputValue.equals(target);
            } else {
                if (token.nameEquals(TokenDefinition.LETTER) || token.nameEquals(TokenDefinition.DIGIT)) {
                    outputValue += token.getValue();
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private static ParsedToken seekToken(List<Token> tokens, int currentIdx, Token target, String tokenName,
                                         boolean trimOutput) {
        return seekToken(tokens, currentIdx, new Token[] { target }, tokenName, trimOutput);
    }

    private static ParsedToken seekToken(List<Token> tokens, int currentIdx, Token[] targets, String tokenName,
                                         boolean trimOutput) {
        String outputValue = "";

        for (int i = currentIdx; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (!Token.arrayContains(targets, token)) {
                outputValue += token.getValue().equals("\n") || token.getValue().equals("\r") ? "" : token.getValue();
            } else {
                return new ParsedToken(i + 1, new Token(tokenName, trimOutput ? outputValue.trim() : outputValue));
            }
        }
        throw new IndexOutOfBoundsException("Unable to find target token(s): " + Token.toString(targets));
    }


    /**
     * A parsed token.
     */
    private static class ParsedToken {

        private final int nextIdx;
        private final Token token;

        public ParsedToken(int nextIdx, Token token) {
            this.nextIdx = nextIdx;
            this.token = token;
        }

        public int getNextIdx() {
            return nextIdx;
        }

        public Token getToken() {
            return token;
        }
    }
}
