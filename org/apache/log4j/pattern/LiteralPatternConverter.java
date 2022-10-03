/*    */ package org.apache.log4j.pattern;
/*    */ 
/*    */ import org.apache.log4j.spi.LoggingEvent;
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
/*    */ 
/*    */ 
/*    */ public final class LiteralPatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/*    */   private final String literal;
/*    */   
/*    */   public LiteralPatternConverter(String literal) {
/* 40 */     super("Literal", "literal");
/* 41 */     this.literal = literal;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/* 48 */     toAppendTo.append(this.literal);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(Object obj, StringBuffer toAppendTo) {
/* 55 */     toAppendTo.append(this.literal);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\pattern\LiteralPatternConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */