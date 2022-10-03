/*    */ package org.springframework.expression.spel;
/*    */ 
/*    */ import org.springframework.expression.EvaluationException;
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
/*    */ 
/*    */ 
/*    */ public class SpelEvaluationException
/*    */   extends EvaluationException
/*    */ {
/*    */   private final SpelMessage message;
/*    */   private final Object[] inserts;
/*    */   
/*    */   public SpelEvaluationException(SpelMessage message, Object... inserts) {
/* 39 */     super(message.formatMessage(inserts));
/* 40 */     this.message = message;
/* 41 */     this.inserts = inserts;
/*    */   }
/*    */   
/*    */   public SpelEvaluationException(int position, SpelMessage message, Object... inserts) {
/* 45 */     super(position, message.formatMessage(inserts));
/* 46 */     this.message = message;
/* 47 */     this.inserts = inserts;
/*    */   }
/*    */   
/*    */   public SpelEvaluationException(int position, Throwable cause, SpelMessage message, Object... inserts) {
/* 51 */     super(position, message.formatMessage(inserts), cause);
/* 52 */     this.message = message;
/* 53 */     this.inserts = inserts;
/*    */   }
/*    */   
/*    */   public SpelEvaluationException(Throwable cause, SpelMessage message, Object... inserts) {
/* 57 */     super(message.formatMessage(inserts), cause);
/* 58 */     this.message = message;
/* 59 */     this.inserts = inserts;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPosition(int position) {
/* 67 */     this.position = position;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SpelMessage getMessageCode() {
/* 74 */     return this.message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object[] getInserts() {
/* 81 */     return this.inserts;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\SpelEvaluationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */