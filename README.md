antlr4-macro
============

A macro pre-processor for ANTLR4.

## Usage
You should add pre-build event(s) in your IDE of choice to run the
antlr4-macro artifact, and thus output standard ANTLR4 grammar files.  

You can specify either target file, i.e. `-i my-grammar.mg4` or the
entire working directory, i.e. `-i .`.  
You may also enable recursive traversal using `-r`.  
When traversing the directory, only files with extension `mg4` will be
processed.  

More usage information available at `java -jar antlr4-macro.jar -help`

## What is it?
C-like macros for your ANTLR4 grammar code.  
e.g. `#MY_MACRO: 'my macro';` and `#MACRO2(A, B): A ' ' B;`.

Note: The scope of a macro rule is the entire file it is in.

## In-built macro rules
| Name | Arguments | Output (ANTLR4 grammar code) |
|---|---|---|
| `lower` | Anything | Its entire argument in lowercase. |
| `upper` | Anything | Its entire argument in uppercase. |
| `list` | Anything | `Anything (',' Anything)*` |
| `list` | Anything, Delimiter | `Anything (Delimeter Anything)*` |

Note: You can redefine an in-built macro, however you cannot redefine your own macros.
This will result in the `RedefinedMacroRuleException` being thrown.

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

## License
This project is licensed under the [MIT license](LICENSE).
