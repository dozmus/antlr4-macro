antlr4-macro
============

A macro pre-processor for ANTLR4.

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
C-like macros to your code, such as `#MY_MACRO: 'my macro';` and
`#MACRO2(A, B): A ' ' B;`.

The scope of the macro rules is the entire current file.

A user-configured pre-build event should then call the executable
and thus parse your macro grammar files and outputs valid ANTLR4 grammar
files.

## Example
Input:
```
grammar HelloWorld;

// my macro definitions
#HELLO: 'Hello';
#WORLD: 'World';

// parser rules
helloWorldList1: list(HELLOWORLD1);
helloWorldList2: list(HELLOWORLD2, ' ');

// lexer rules
HELLOWORLD1: #HELLO #WORLD;
HELLOWORLD2: lower('Hello') upper('World');
```

Rough output:
```
grammar HelloWorld;

// parser rules
helloWorldList1: HELLOWORLD1 (',' HELLOWORLD1)*;
helloWorldList2: HELLOWORLD2 (' ' HELLOWORLD2)*;

// lexer rules
HELLOWORLD1: 'Hello' 'World';
HELLOWORLD2: 'hello' 'WORLD';
```

## Run-time details
There are four in-built macros: lower, upper, arbitrary-delimited list, and comma-delimited list.

You can redefine an in-built macro, however you cannot redefine your own macros.
This will result in the `RedefinedMacroRuleException` being thrown.

## License
This project is licensed under the [MIT license](LICENSE).
