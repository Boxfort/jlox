# Jlox - Java Lox Interpreter

Implementation of Lox in Java following the book [Crafting Interpreters](https://craftinginterpreters.com/) by Bob Nystrom.

## Grammar

```
program        -> statement* EOF ;

declaration    -> varDecl | statement ;

varDecl        -> "var" IDENTIFIER ( "=" expression )? ";" ;

statement      -> exprStmt | ifStmt | printStmt | block;

exprStmt       -> expression ";" ;
ifStmt         -> "if" "(" expression ")" statement ( "else" statement )? ;
printStmt      -> "print" expression ";" ;
block          -> "{" declaration* "}" ;

expression     -> assignment ;
assignment     -> IDENTIFIER "=" assignment | chain ;
chain          -> ternary ("," ternary)* ;
ternary        -> equality ("?" equality ":" equality)* ;
equality       -> comparison ( ( "!=" | "==" ) comparison )* ;
comparison     -> addition ( ( ">" | ">=" | "<" | "<=" ) addition )* ;
addition       -> multiplication ( ( "-" | "+" ) multiplication)* ;
multiplication -> unary ( ( "/" | "*" ) unary)* ;
unary          -> ( "!" | "-" ) unary | primary ;
primary        -> NUMBER | STRING | "false" | "true" | "nil" | "(" expression ")" | IDENTIFIER ;
```
