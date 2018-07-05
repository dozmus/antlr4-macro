// Source: https://github.com/antlr/grammars-v4/blob/master/antlr4/examples/Hello.g4
// define a grammar called Hello
grammar Hello;
r   : 'hello' ID;
ID  : [a-z]+ ;
WS  : [ \t\r\n]+ -> skip ;
