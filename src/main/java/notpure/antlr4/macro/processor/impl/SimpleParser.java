package notpure.antlr4.macro.processor.impl;

import notpure.antlr4.macro.processor.model.Parser;
import notpure.antlr4.macro.processor.model.statement.MacroStatement;
import notpure.antlr4.macro.processor.model.statement.Statement;
import notpure.antlr4.macro.processor.model.token.Token;
import notpure.antlr4.macro.processor.model.token.TokenDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple parser.
 */
public final class SimpleParser implements Parser {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleParser.class);
    private final List<Statement> statements = new ArrayList<>();

    /**
     * Parses the tokens into more complicated ones.
     */
    @Override
    public Parser parse(List<Token> tokens) {
        for (int idx = 0; idx < tokens.size(); idx++) {
            Token token = tokens.get(idx);

            // TODO impl/finish
            if (token.getValue().equals("#")) { // macro rule
                // Parse identifier
                ParsedToken pair1 = seekToken(tokens, idx + 1,
                        new Token(TokenDefinition.COLON), "MacroRuleIdentifier", true);
                LOGGER.info("Parsed identifier for macro def: {} (nextIdx={})", pair1.getToken(), pair1.getNextIdx());
                idx = pair1.getNextIdx();

                // Parse value
                ParsedToken pair2 = seekToken(tokens, idx, new Token[] {
                        new Token(TokenDefinition.SEMICOLON)
                }, "MacroRuleValue", true);
                LOGGER.info("Parsed identifier for macro value: {} (nextIdx={})", pair2.getToken(), pair2.getNextIdx());
                idx = pair2.getNextIdx();

                // Add to parsed tokens list and continue
                MacroStatement macroStatement = new MacroStatement(pair1.getToken().getValue(), pair2.getToken().getValue());
                statements.add(macroStatement);
                LOGGER.info("Parsed macro statement: {}={}", macroStatement.getIdentifierName(), macroStatement.getIdentifierValue());
            } else if (token.getValue().length() == 1) {
                char c = token.getValue().charAt(0);

                if (Character.isLowerCase(c)) { // parser rule
                    // ...
                } else if (Character.isUpperCase(c)) { // lexer rule
                    // ...
                }
            }
        }
        return this;
    }

    @Override
    public List<Statement> getStatements() {
        return statements;
    }

    private static ParsedToken seekToken(List<Token> tokens, int currentIdx, Token target, String tokenName, boolean trimOutput) {
        return seekToken(tokens, currentIdx, new Token[] { target }, tokenName, trimOutput);
    }

    private static ParsedToken seekToken(List<Token> tokens, int currentIdx, Token[] targets, String tokenName, boolean trimOutput) {
        String outputValue = "";

        for (int i = currentIdx; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (!tokenArrayContainsTarget(targets, token)) {
                outputValue += token.getValue().equals("\n") || token.getValue().equals("\r") ? "" : token.getValue();
            } else {
                return new ParsedToken(i + 1, new Token(tokenName, trimOutput ? outputValue.trim() : outputValue));
            }
        }
        return new ParsedToken(-1, null);
    }

    private static boolean tokenArrayContainsTarget(Token[] src, Token target) {
        // TODO extract into a helper or the token class
        for (Token t : src) {
            if (t.equals(target))
                return true;
        }
        return false;
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
