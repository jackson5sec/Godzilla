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
/*    */ public class SequenceNumberPatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/* 33 */   private static final SequenceNumberPatternConverter INSTANCE = new SequenceNumberPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private SequenceNumberPatternConverter() {
/* 40 */     super("Sequence Number", "sn");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SequenceNumberPatternConverter newInstance(String[] options) {
/* 50 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/* 57 */     toAppendTo.append("0");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\pattern\SequenceNumberPatternConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */