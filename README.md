antlr4-macro
============

A macro pre-processor for ANTLR4. **This project is not yet ready for
production-level consumption.**

## License
This project is licensed under the [MIT license](LICENSE).

## Usage
You should add pre-build event(s) in your IDE of choice to run the
antlr4-macro artifact.  
More usage information available at `java -jar antlr4-macro.jar -help`

## How it works
You write your grammars in ANTLR4 syntax, however now you can also add
C-like macros to your code, such as `#MY_MACRO: 'my macro';` and then
use `#MY_MACRO` elsewhere in your code as an identifier. The scope
of the macro rules is the entire current file.

A user-configured pre-build event should then call the executable
and thus parse your macro grammar files and outputs valid ANTLR4 grammar
files.

## Example
Input:
```
grammar HelloWorld;

/// my macro definitions, this comment will not appear in the output
#HELLO: 'Hello';
#WORLD: 'World';

// parser rules
mySingleRule: HELLOWORLD;

// lexer rules
HELLOWORLD: #HELLO #WORLD;
```

Rough output:
```
grammar HelloWorld;

// parser rules
mySingleRule: HELLOWORLD;

// lexer rules
HELLOWORLD: 'Hello' 'World';
```