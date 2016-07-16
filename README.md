antlr4-macro
============

A simple macro implementation for ANTLR4.

## Usage
You should add a pre-build event in your IDE of choice to run the antlr4-macro artifact.

## How it works
You write your grammars in ANTLR4 syntax, however you can also add
C-like macros to your code, such as `#MY_MACRO: 'my macro';` and then
use `#MY_MACRO` elsewhere in your code as an identifier. This is
currently only supported for combined grammars.

A user-configurated pre-build event then parses these files and
outputs valid ANTLR4 grammars.