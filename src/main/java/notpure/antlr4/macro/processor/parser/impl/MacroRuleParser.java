package notpure.antlr4.macro.processor.parser.impl;

import notpure.antlr4.macro.model.lang.ExpressionType;
import notpure.antlr4.macro.model.token.TokenDefinition;
import notpure.antlr4.macro.model.token.TokenIterator;

/**
 * Created by pure on 28/07/2016.
 */
public final class MacroRuleParser extends GrammarRuleParser {

    public MacroRuleParser() {
        super(ExpressionType.MACRO_RULE);
    }

    @Override
    public boolean validate(TokenIterator it) {
        return it.hasNext() && it.peek().nameEquals(TokenDefinition.HASH);
    }
}
