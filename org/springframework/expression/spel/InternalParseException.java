/*    */ package org.springframework.expression.spel;
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
/*    */ public class InternalParseException
/*    */   extends RuntimeException
/*    */ {
/*    */   public InternalParseException(SpelParseException cause) {
/* 30 */     super((Throwable)cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public SpelParseException getCause() {
/* 35 */     return (SpelParseException)super.getCause();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\InternalParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */