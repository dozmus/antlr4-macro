antlr4-macro
============

A simple macro implementation for ANTLR4. This project is not yet
ready for public consumption.

## Usage
You should add a pre-build event in your IDE of choice to run the
antlr4-macro artifact.

## How it works
You write your grammars in ANTLR4 syntax, however now you can also add
C-like macros to your code, such as `#MY_MACRO: 'my macro';` and then
use `#MY_MACRO` elsewhere in your code as an identifier. This is
currently only supported for combined grammars.

A user-configured pre-build event should then call the executable
and thus parse your macro grammar files and outputs valid ANTLR4 grammar
files.