package com.purecs.antlr4.macro.lang.inbuilt;

import com.purecs.antlr4.macro.lang.AbstractMacroRule;

import java.util.Collections;
import java.util.List;

public class Lower extends AbstractMacroRule {

    public Lower() {
        super("lower", "A", Collections.singletonList("A"));
    }

    @Override
    public String apply(List<String> arguments) {
        return super.apply(arguments).toLowerCase();
    }
}
