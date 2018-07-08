package com.purecs.antlr4.macro.lang.inbuilt;

import com.purecs.antlr4.macro.lang.AbstractMacroRule;

import java.util.Collections;

/**
 * A comma-delimited list, of at least one element.
 */
public class List1 extends AbstractMacroRule {

    public List1() {
        super("list", "A (',' A)*", Collections.singletonList("A"));
    }
}
