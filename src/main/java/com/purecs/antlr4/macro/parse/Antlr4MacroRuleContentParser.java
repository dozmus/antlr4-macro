package com.purecs.antlr4.macro.parse;

public class Antlr4MacroRuleContentParser {

    public static String apply(String out, String parameter, String argument) {
        String pattern1 = "^p$".replaceAll("p", parameter);
        String pattern2 = "\\sp\\s".replaceAll("p", parameter);
        String pattern3 = "^p\\s".replaceAll("p", parameter);
        String pattern4 = "\\sp$".replaceAll("p", parameter);

        String pattern5 = "\\(p\\s".replaceAll("p", parameter);
        String pattern6 = "\\[p\\s".replaceAll("p", parameter);
        String pattern7 = "\\sp\\)".replaceAll("p", parameter);
        String pattern8 = "\\sp\\]".replaceAll("p", parameter);

        out = out.replaceAll(pattern1, argument);
        out = out.replaceAll(pattern2, " " + argument + " ");
        out = out.replaceAll(pattern3, argument + " ");
        out = out.replaceAll(pattern4, " " + argument);

        out = out.replaceAll(pattern5, "(" + argument + " ");
        out = out.replaceAll(pattern6, "[" + argument + " ");
        out = out.replaceAll(pattern7, " " + argument + ")");
        out = out.replaceAll(pattern8, " " + argument + "]");
        return out;
    }
}
