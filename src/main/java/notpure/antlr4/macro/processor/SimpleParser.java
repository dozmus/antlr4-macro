package notpure.antlr4.macro.processor;

import notpure.antlr4.macro.processor.statement.MacroStatement;
import notpure.antlr4.macro.processor.statement.Statement;
import notpure.antlr4.macro.processor.token.Token;
import notpure.antlr4.macro.processor.token.TokenDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple parser.
 */
public final class SimpleParser {

    /**
     * Logger instance.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleParser.class);

    /**
     * Parses the tokens into more complicated ones.
     */
    public static List<Statement> parse(List<Token> tokens) {
        ArrayList<Statement> statements = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            // TODO impl
            switch (token.getValue()) {
                case "\"":
                    break;
                case "'":
                    break;
                case "#":
                    // Parse identifier
                    ParsedToken pair1 = seekToken(tokens, i + 1,
                            new Token(TokenDefinition.COLON.name(), TokenDefinition.COLON.getRegex().substring(1)), "MacroIdentifier", true);
                    LOGGER.info("Parsed identifier for macro def: {} (nextIdx={})", pair1.getToken(), pair1.getNextIdx());
                    i = pair1.getNextIdx();

//                    i = skipWhiteSpace(i, tokens);

                    // Parse value
                    ParsedToken pair2 = seekToken(tokens, i, new Token[] {
                            new Token(TokenDefinition.SEMICOLON.name(), TokenDefinition.SEMICOLON.getRegex().substring(1))
                    }, "MacroValue", true);
                    LOGGER.info("Parsed identifier for macro value: {} (nextIdx={})", pair2.getToken(), pair2.getNextIdx());
                    i = pair2.getNextIdx();

                    // Add to parsed tokens list and continue
                    MacroStatement macroStatement = new MacroStatement(pair1.getToken().getValue(), pair2.getToken().getValue());
                    statements.add(macroStatement);
                    LOGGER.info("Parsed macro statement: {}={}", macroStatement.getIdentifierName(), macroStatement.getIdentifierValue());
                    break;
            }
        }
        return statements;
    }

    private static int skipWhiteSpace(int currentIdx, List<Token> tokens) {
        for (int i = currentIdx; i < tokens.size(); i++) {
            if (!tokens.get(i).getName().equals(TokenDefinition.SPACE.name())) {
                return i;
            }
        }
        return -1;
    }

    private static ParsedToken seekToken(List<Token> tokens, int currentIdx, Token target, String tokenName, boolean trimOutput) {
        return seekToken(tokens, currentIdx, new Token[] { target }, tokenName, trimOutput);
    }

    private static ParsedToken seekToken(List<Token> tokens, int currentIdx, Token[] targets, String tokenName, boolean trimOutput) {
        String outputValue = "";

        for (int i = currentIdx; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (!tokenArrayContainsTarget(targets, token)) {
                outputValue += token.getValue();
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
