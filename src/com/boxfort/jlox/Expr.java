package com.boxfort.jlox;

abstract class Expr {
   static class Literal extends Expr {
      final Token literal;

      Literal(Token literal) {
         this.literal = literal;
      }
   }

   static class Unary extends Expr {
      final Expr right;
      final Token operator;

      Unary(Token operator, Expr right) {
         this.operator = operator;
         this.right = right ;
      }
   }

   static class Binary extends Expr {
      final Expr left;
      final Expr right;
      final Token operator;

      Binary(Expr left, Token operator, Expr right) {
         this.left = left;
         this.operator = operator;
         this.right = right ;
      }
   }

   static class Grouping extends Expr {
      final Expr expr;

      Grouping(Expr expr) {
         this.expr = expr;
      }
   }
}
