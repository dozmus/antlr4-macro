package com.purecs.antlr4.macro.lang.inbuilt;

import com.purecs.antlr4.macro.lang.AbstractMacroRule;

import java.util.Arrays;

/**
 * An arbitrarily-delimited list, of at least one element.
 */
public class ArbitraryDelimitedList extends AbstractMacroRule {

    public ArbitraryDelimitedList() {
        super("list", "A (B A)*", Arrays.asList("A", "B"));
    }
}
