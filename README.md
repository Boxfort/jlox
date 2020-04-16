# Jlox - Java Lox Interpreter

Implementation of Lox in Java following the book [Crafting Interpreters](https://craftinginterpreters.com/) by Bob Nystrom.

## Grammar

```
program        -> statement* EOF ;

declaration    -> varDecl | statement ;

varDecl        -> "var" IDENTIFIER ( "=" expression )? ";" ;

statement      -> exprStmt | printStmt ;

exprStmt       -> expression ";" ;
printStmt      -> "print" expression ";" ;

expression     -> ternary ("," ternary)* ;
ternary        -> equality ("?" equality ":" equality)* ;
equality       -> comparison ( ( "!=" | "==" ) comparison )* ;
comparison     -> addition ( ( ">" | ">=" | "<" | "<=" ) addition )* ;
addition       -> multiplication ( ( "-" | "+" ) multiplication)* ;
multiplication -> unary ( ( "/" | "*" ) unary)* ;
unary          -> ( "!" | "-" ) unary | primary ;
primary        -> NUMBER | STRING | "false" | "true" | "nil" | "(" expression ")" | IDENTIFIER ;
```
