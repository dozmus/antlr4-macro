package notpure.antlr4.macro.processor.parser.impl;

import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.TokenIterator;

/**
 * Created by pure on 28/07/2016.
 */
public final class ParserRuleParser extends GrammarRuleParser {

    public ParserRuleParser() {
        super(ExpressionType.PARSER_RULE);
    }

    @Override
    public boolean validate(TokenIterator it) {
        return it.remaining() > 0 && it.peek().getValue() != null && Character.isLowerCase(it.peek().getValue().charAt(0));
    }
}
