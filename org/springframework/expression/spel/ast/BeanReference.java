/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.expression.AccessException;
/*    */ import org.springframework.expression.BeanResolver;
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ import org.springframework.expression.spel.SpelEvaluationException;
/*    */ import org.springframework.expression.spel.SpelMessage;
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
/*    */ public class BeanReference
/*    */   extends SpelNodeImpl
/*    */ {
/*    */   private static final String FACTORY_BEAN_PREFIX = "&";
/*    */   private final String beanName;
/*    */   
/*    */   public BeanReference(int startPos, int endPos, String beanName) {
/* 41 */     super(startPos, endPos, new SpelNodeImpl[0]);
/* 42 */     this.beanName = beanName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
/* 48 */     BeanResolver beanResolver = state.getEvaluationContext().getBeanResolver();
/* 49 */     if (beanResolver == null) {
/* 50 */       throw new SpelEvaluationException(
/* 51 */           getStartPosition(), SpelMessage.NO_BEAN_RESOLVER_REGISTERED, new Object[] { this.beanName });
/*    */     }
/*    */     
/*    */     try {
/* 55 */       return new TypedValue(beanResolver.resolve(state.getEvaluationContext(), this.beanName));
/*    */     }
/* 57 */     catch (AccessException ex) {
/* 58 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_BEAN_RESOLUTION, new Object[] { this.beanName, ex
/* 59 */             .getMessage() });
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toStringAST() {
/* 65 */     StringBuilder sb = new StringBuilder();
/* 66 */     if (!this.beanName.startsWith("&")) {
/* 67 */       sb.append('@');
/*    */     }
/* 69 */     if (!this.beanName.contains(".")) {
/* 70 */       sb.append(this.beanName);
/*    */     } else {
/*    */       
/* 73 */       sb.append('\'').append(this.beanName).append('\'');
/*    */     } 
/* 75 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\ast\BeanReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */