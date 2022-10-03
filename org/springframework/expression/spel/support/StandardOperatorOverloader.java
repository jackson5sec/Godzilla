/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.Operation;
/*    */ import org.springframework.expression.OperatorOverloader;
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
/*    */ public class StandardOperatorOverloader
/*    */   implements OperatorOverloader
/*    */ {
/*    */   public boolean overridesOperation(Operation operation, @Nullable Object leftOperand, @Nullable Object rightOperand) throws EvaluationException {
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object operate(Operation operation, @Nullable Object leftOperand, @Nullable Object rightOperand) throws EvaluationException {
/* 43 */     throw new EvaluationException("No operation overloaded by default");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\StandardOperatorOverloader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */