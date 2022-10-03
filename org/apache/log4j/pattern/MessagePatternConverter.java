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
/*    */ public final class MessagePatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/* 32 */   private static final MessagePatternConverter INSTANCE = new MessagePatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private MessagePatternConverter() {
/* 39 */     super("Message", "message");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MessagePatternConverter newInstance(String[] options) {
/* 49 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/* 56 */     toAppendTo.append(event.getRenderedMessage());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\pattern\MessagePatternConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */