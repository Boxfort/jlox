package com.boxfort.jlox;

import java.util.List;
import java.util.function.Function;

import static com.boxfort.jlox.TokenType.*;

public class Parser {
    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
    }

    // expression -> ternary ("," ternary)* ;
    private Expr expression() {
        binaryMissingLeftOperand();
        Expr expr = ternary();

        while(match(COMMA)) {
            Token operator = previous();
            Expr right = ternary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // binary -> (","|"-"|"+"|"/"|"*"|"="|"=="|"!="|">"|">="|"<"|"<=") expression
    private void binaryMissingLeftOperand() {
        while(match(COMMA, MINUS, PLUS, SLASH, STAR, EQUAL,
                 EQUAL_EQUAL, BANG_EQUAL, GREATER,
                 GREATER_EQUAL, LESS, LESS_EQUAL))
        {
            Token operator = previous();
            expression();
            throw error(operator, "Missing left hand operand.");
        }

        return;
    }

    // ternary -> equality ("?" equality ":" equality)* ;
    private Expr ternary() {
        Expr expr = equality();

        while(match(QUESTION_MARK)) {
            Expr left = equality();
            consume(COLON, "Expect ':' in ternary expression.");
            Expr right = equality();
            expr = new Expr.Ternary(expr, left, right);
        }

        return expr;
    }

    // equality -> comparison ( ( "!=" | "==" ) comparison )* ;
    private Expr equality() {
        Expr expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // comparison -> addition ( ( ">" | ">=" | "<" | "<=" ) addition )* ;
    private Expr comparison() {
        Expr expr = addition();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = addition();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // addition -> multiplication ( ( "-" | "+" ) multiplication)* ;
    private Expr addition() {
        Expr expr = multiplication();

        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = multiplication();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // multiplication -> unary ( ( "/" | "*" ) unary)* ;
    private Expr multiplication() {
        Expr expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // unary -> ( "!" | "-" ) unary | primary ;
    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    // primary -> NUMBER | STRING | "false" | "true" | "nil" | "(" expression ")" ;
    private Expr primary() {
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NIL)) return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    // This checks to see if the current token is any of the
    // given types and returns true. Otherwise return false.
    // Consumes token on match.
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw error(peek(), message);
    }

    // Returns true if the current token is of the given type.
    // Does not consume token.
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    // Consumes the current token and returns it.
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    // Return the current token without consuming it.
    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        Jlox.error(token, message);
        return new ParseError();
    }

    // Discards tokens until a statement boundary is found.
    // This is used after catching a parsing error so that
    // parsing may continue.
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            advance();
        }
    }
}
