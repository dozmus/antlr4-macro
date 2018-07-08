package com.purecs.antlr4.macro.parse;

import antlr4.ANTLRv4Lexer;
import antlr4.ANTLRv4Parser;
import antlr4.ANTLRv4ParserBaseListener;
import com.purecs.antlr4.macro.lang.MacroRule;
import com.purecs.antlr4.macro.lang.MacroUse;
import com.purecs.antlr4.macro.util.FilePosition;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Antlr4Parser extends ANTLRv4ParserBaseListener implements Parser {

    // TODO throw exception if undefined macro encountered

    private final static Logger LOGGER = LoggerFactory.getLogger(Antlr4Parser.class);
    /**
     * Macros are stored here in the order that they are encountered in the code.
     * A macro must be defined before its use.
     */
    private final List<MacroRule> macros = new ArrayList<>();
    /**
     * Macros uses are stored here in the order that they are encountered in the code.
     */
    private final List<MacroUse> macroUses = new ArrayList<>();
    private CommonTokenStream tokens;

    @Override
    public void enterStandAloneMacroRule(ANTLRv4Parser.StandAloneMacroRuleContext ctx) {
        // Parse rule
        MacroRule rule = parseMacroRule(ctx.POUND(), ctx.COLON(), ctx.SEMI(), ctx.identifier(),
                ctx.macroRuleContent().labeledAltOrLexerAlt(), new ArrayList<>());
        macros.add(rule);
        LOGGER.debug("Parsed macro: {}", rule);

        // Continue traversal
        super.enterStandAloneMacroRule(ctx);
    }

    @Override
    public void enterArgsMacroRule(ANTLRv4Parser.ArgsMacroRuleContext ctx) {
        // Parse rule
        MacroRule rule = parseMacroRule(ctx.POUND(), ctx.COLON(), ctx.SEMI(), ctx.identifier(),
                ctx.macroRuleContent().labeledAltOrLexerAlt(), ctx.identifierList().identifier());
        macros.add(rule);
        LOGGER.debug("Parsed macro: {}", rule);

        // Continue traversal
        super.enterArgsMacroRule(ctx);
    }

    /**
     * Parse macro use in parser rule.
     */
    @Override
    public void enterElement(ANTLRv4Parser.ElementContext ctx) {
        // Parse rule usage
        if (ctx.POUND() != null) {
            MacroUse use = parseMacroUse(ctx.POUND(), ctx.identifier(),
                    ctx.LPAREN() == null ? new ArrayList<>() : ctx.alternativeList().alternative());
            macroUses.add(use);
            LOGGER.debug("Parsed macro use (in parser rule): {}", use);
        }

        // Continue traversal
        super.enterElement(ctx);
    }

    /**
     * Parse macro use in lexer rule.
     */
    @Override
    public void enterLexerElement(ANTLRv4Parser.LexerElementContext ctx) {
        // Parse rule usage
        if (ctx.POUND() != null) {
            MacroUse use = parseMacroUse(ctx.POUND(), ctx.identifier(),
                    ctx.LPAREN() == null ? new ArrayList<>() : ctx.lexerAltMacroArgsList().lexerAlt());
            macroUses.add(use);
            LOGGER.debug("Parsed macro use (in lexer rule): {}", use);
        }

        // Continue traversal
        super.enterLexerElement(ctx);
    }

    private MacroUse parseMacroUse(TerminalNode pound, ANTLRv4Parser.IdentifierContext identifierCtx,
            List<? extends ParserRuleContext> args) {
        int startIdx = pound.getSymbol().getStartIndex();
        int endIdx = identifierEndIndex(identifierCtx);
        FilePosition filePosition = new FilePosition(startIdx, endIdx);

        String identifier = identifierCtx.getText();
        List<FilePosition> arguments;

        if (args.size() == 0) {
            arguments = new ArrayList<>();
        } else {
            arguments = args
                    .stream()
                    .map(a -> new FilePosition(a.start.getStartIndex(), a.stop.getStopIndex() + 1))
                    .collect(Collectors.toList());
        }
        return new MacroUse(identifier, filePosition, arguments);
    }

    private MacroRule parseMacroRule(TerminalNode pound, TerminalNode colon, TerminalNode semiColon,
            ANTLRv4Parser.IdentifierContext identifierCtx, List<ANTLRv4Parser.LabeledAltOrLexerAltContext> body,
            List<ANTLRv4Parser.IdentifierContext> parameterCtxs) {
        int startIdx = pound.getSymbol().getStartIndex();
        int endIdx = semiColon.getSymbol().getStopIndex() + 1; // +1 to include the end
        FilePosition filePosition = new FilePosition(startIdx, endIdx);
        String identifier = identifierCtx.getText();

        // Parameters
        List<String> parameters = parameterCtxs
                .stream()
                .map(RuleContext::getText)
                .collect(Collectors.toList());

        // Content
        String content = tokens.getTokens()
                .stream()
                .filter(t -> t.getChannel() == ANTLRv4Lexer.WHITESPACE_CHANNEL || t.getChannel() == 0)
                .filter(t -> t.getStartIndex() > colon.getSymbol().getStopIndex()
                        && t.getStopIndex() < semiColon.getSymbol().getStartIndex())
                .sorted(Comparator.comparingInt(Token::getStartIndex))
                .map(Token::getText)
                .collect(Collectors.joining())
                .trim();
        return new MacroRule(identifier, content, filePosition, parameters);
    }

    private static int identifierEndIndex(ANTLRv4Parser.IdentifierContext ctx) {
        return ctx.RULE_REF() == null
                ? ctx.TOKEN_REF().getSymbol().getStopIndex()
                : ctx.RULE_REF().getSymbol().getStopIndex();
    }

    @Override
    public void parse(Path file) throws IOException {
        ANTLRv4Lexer lexer = new ANTLRv4Lexer(CharStreams.fromPath(file));
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);

        tokens = new CommonTokenStream(lexer);
        ANTLRv4Parser parser = new ANTLRv4Parser(tokens);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);

        // Generate the code tree for it
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, parser.grammarSpec());
    }

    public List<MacroRule> getMacros() {
        return macros;
    }

    public List<MacroUse> getMacroUses() {
        return macroUses;
    }
}
