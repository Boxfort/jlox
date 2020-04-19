# Jlox - Java Lox Interpreter

Implementation of Lox in Java following the book [Crafting Interpreters](https://craftinginterpreters.com/) by Bob Nystrom.

## Grammar

```
program        -> statement* EOF ;

declaration    -> varDecl | statement ;

varDecl        -> "var" IDENTIFIER ( "=" expression )? ";" ;

statement      -> exprStmt | ifStmt | printStmt | whileStmt | block;

exprStmt       -> expression ";" ;
ifStmt         -> "if" "(" expression ")" statement ( "else" statement )? ;
printStmt      -> "print" expression ";" ;
whileStmt      -> "while "(" expression ")" statement ;
block          -> "{" declaration* "}" ;

expression     -> assignment ;
assignment     -> IDENTIFIER "=" assignment | logic_or ;
logic_or       -> logic_and ( "or" logic_and )* ;
logic_and      -> chain ( "and" chain )* ;
chain          -> ternary ("," ternary)* ;
ternary        -> equality ("?" equality ":" equality)* ;
equality       -> comparison ( ( "!=" | "==" ) comparison )* ;
comparison     -> addition ( ( ">" | ">=" | "<" | "<=" ) addition )* ;
addition       -> multiplication ( ( "-" | "+" ) multiplication)* ;
multiplication -> unary ( ( "/" | "*" ) unary)* ;
unary          -> ( "!" | "-" ) unary | primary ;
primary        -> NUMBER | STRING | "false" | "true" | "nil" | "(" expression ")" | IDENTIFIER ;
```
