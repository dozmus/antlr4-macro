package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.model.Parser;
import notpure.antlr4.macro.model.lang.Statement;
import notpure.antlr4.macro.model.lang.StatementType;
import notpure.antlr4.macro.model.token.Token;
import notpure.antlr4.macro.model.token.TokenDefinition;
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
                StatementType.SINGLE_LINE_COMMENT.name(), true);
        LOGGER.info("Parsed single line comment: {} [nextIdx={}]", pair.getToken(), pair.getNextIdx());
        statements.add(new Statement(StatementType.SINGLE_LINE_COMMENT, pair.getToken().getValue()));
        return pair.getNextIdx();
    }

    private int parseFileHeaderStatement(List<Token> tokens, int idx) {
        // Parse file header, in form 'grammar {name};'.
        ParsedToken pair = seekToken(tokens, idx, new Token(TokenDefinition.SEMICOLON), StatementType.GRAMMAR_NAME.name(), true);
        LOGGER.info("Parsed grammar name: {} [nextIdx={}]", pair.getToken(), pair.getNextIdx());
        statements.add(new Statement(StatementType.GRAMMAR_NAME, pair.getToken().getValue()));
        return pair.getNextIdx();
    }

    private int parseStatement(List<Token> tokens, int idx, int offset, StatementType type) {
        // Parse identifier
        ParsedToken identifier = seekToken(tokens, idx + offset,
                new Token(TokenDefinition.COLON), type.name() + "_Identifier", true);
        LOGGER.info("Parsed identifier for {}: {} [nextIdx={}]", type, identifier.getToken(), identifier.getNextIdx());
        idx = identifier.getNextIdx();

        // Parse value
        ParsedToken value = seekToken(tokens, idx, new Token(TokenDefinition.SEMICOLON), type.name() + "_Value", true);
        LOGGER.info("Parsed value for {}: {} [nextIdx={}]", type, value.getToken(), value.getNextIdx());
        idx = value.getNextIdx();

        // Add to statements list and continue
        Statement statement = fromParsedToken(identifier, value, type);
        statements.add(statement);
        LOGGER.info("Parsed statement {}: {}={}", type, statement.getIdentifier(), statement.getValue());
        return idx;
    }

    private static Statement fromParsedToken(ParsedToken identifier, ParsedToken value, StatementType type) {
        return new Statement(type, identifier.getToken().getValue(), value.getToken().getValue());
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
