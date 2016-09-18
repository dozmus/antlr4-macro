antlr4-macro
============

A macro pre-processor for ANTLR4. **This project is not yet ready for
production-level consumption.**

## License
This project is licensed under the [MIT license](LICENSE).

## Usage
You should add pre-build event(s) in your IDE of choice to run the
antlr4-macro artifact.  

You can specify either target file, i.e. `-i my-grammar.mg4` or the
entire working directory, i.e. `-i .`.  
You may also enable recursive
traversal of the working directory, i.e. `-i . -r`.  
When traversing the directory, only files with extension `mg4` will be
picked up.  

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