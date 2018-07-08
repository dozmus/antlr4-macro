package com.purecs.antlr4.macro.lang.inbuilt;

import com.purecs.antlr4.macro.lang.AbstractMacroRule;

import java.util.Collections;
import java.util.List;

public class Upper extends AbstractMacroRule {

    public Upper() {
        super("upper", "A", Collections.singletonList("A"));
    }

    @Override
    public String apply(List<String> arguments) {
        return super.apply(arguments).toUpperCase();
    }
}
