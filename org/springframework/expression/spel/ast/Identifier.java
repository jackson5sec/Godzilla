/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Identifier
/*    */   extends SpelNodeImpl
/*    */ {
/*    */   private final TypedValue id;
/*    */   
/*    */   public Identifier(String payload, int startPos, int endPos) {
/* 35 */     super(startPos, endPos, new SpelNodeImpl[0]);
/* 36 */     this.id = new TypedValue(payload);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toStringAST() {
/* 42 */     return String.valueOf(this.id.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public TypedValue getValueInternal(ExpressionState state) {
/* 47 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\Identifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */