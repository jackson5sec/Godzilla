/*    */ package org.springframework.expression.spel;
/*    */ 
/*    */ import org.springframework.expression.ParseException;
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
/*    */ 
/*    */ 
/*    */ public class SpelParseException
/*    */   extends ParseException
/*    */ {
/*    */   private final SpelMessage message;
/*    */   private final Object[] inserts;
/*    */   
/*    */   public SpelParseException(@Nullable String expressionString, int position, SpelMessage message, Object... inserts) {
/* 40 */     super(expressionString, position, message.formatMessage(inserts));
/* 41 */     this.message = message;
/* 42 */     this.inserts = inserts;
/*    */   }
/*    */   
/*    */   public SpelParseException(int position, SpelMessage message, Object... inserts) {
/* 46 */     super(position, message.formatMessage(inserts));
/* 47 */     this.message = message;
/* 48 */     this.inserts = inserts;
/*    */   }
/*    */   
/*    */   public SpelParseException(int position, Throwable cause, SpelMessage message, Object... inserts) {
/* 52 */     super(position, message.formatMessage(inserts), cause);
/* 53 */     this.message = message;
/* 54 */     this.inserts = inserts;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SpelMessage getMessageCode() {
/* 62 */     return this.message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object[] getInserts() {
/* 69 */     return this.inserts;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\SpelParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */