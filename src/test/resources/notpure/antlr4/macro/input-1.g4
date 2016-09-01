grammar Hello;

// parser rules
r: 'Hello World';

// lexer rules
ID : [a-z]+;
WS : [ \t\r\n]+ -> skip;