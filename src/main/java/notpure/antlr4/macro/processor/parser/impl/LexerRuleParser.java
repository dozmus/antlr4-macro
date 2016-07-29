package notpure.antlr4.macro.processor.parser.impl;

import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.processor.parser.TokenParserIterator;

/**
 * Created by pure on 28/07/2016.
 */
public final class LexerRuleParser extends GrammarRuleParser {

    public LexerRuleParser() {
        super(ExpressionType.LEXER_RULE);
    }

    @Override
    public boolean validate(TokenParserIterator it) {
        return it.hasNext() && it.peek().getValue() != null && Character.isUpperCase(it.peek().getValue().charAt(0));
    }
}
