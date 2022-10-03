/*    */ package org.apache.log4j.pattern;
/*    */ 
/*    */ import org.apache.log4j.Layout;
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
/*    */ public final class LineSeparatorPatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/* 34 */   private static final LineSeparatorPatternConverter INSTANCE = new LineSeparatorPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final String lineSep;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private LineSeparatorPatternConverter() {
/* 46 */     super("Line Sep", "lineSep");
/* 47 */     this.lineSep = Layout.LINE_SEP;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static LineSeparatorPatternConverter newInstance(String[] options) {
/* 57 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/* 64 */     toAppendTo.append(this.lineSep);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(Object obj, StringBuffer toAppendTo) {
/* 71 */     toAppendTo.append(this.lineSep);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\pattern\LineSeparatorPatternConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */