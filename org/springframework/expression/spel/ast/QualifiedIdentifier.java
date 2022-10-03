/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ 
/*    */ public class QualifiedIdentifier
/*    */   extends SpelNodeImpl
/*    */ {
/*    */   @Nullable
/*    */   private TypedValue value;
/*    */   
/*    */   public QualifiedIdentifier(int startPos, int endPos, SpelNodeImpl... operands) {
/* 40 */     super(startPos, endPos, operands);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 47 */     if (this.value == null) {
/* 48 */       StringBuilder sb = new StringBuilder();
/* 49 */       for (int i = 0; i < getChildCount(); i++) {
/* 50 */         Object value = this.children[i].getValueInternal(state).getValue();
/* 51 */         if (i > 0 && (value == null || !value.toString().startsWith("$"))) {
/* 52 */           sb.append('.');
/*    */         }
/* 54 */         sb.append(value);
/*    */       } 
/* 56 */       this.value = new TypedValue(sb.toString());
/*    */     } 
/* 58 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toStringAST() {
/* 63 */     StringBuilder sb = new StringBuilder();
/* 64 */     if (this.value != null) {
/* 65 */       sb.append(this.value.getValue());
/*    */     } else {
/*    */       
/* 68 */       for (int i = 0; i < getChildCount(); i++) {
/* 69 */         if (i > 0) {
/* 70 */           sb.append('.');
/*    */         }
/* 72 */         sb.append(getChild(i).toStringAST());
/*    */       } 
/*    */     } 
/* 75 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\QualifiedIdentifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */