/*    */ package org.apache.log4j.pattern;
/*    */ 
/*    */ import org.apache.log4j.spi.LocationInfo;
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
/*    */ public final class ClassNamePatternConverter
/*    */   extends NamePatternConverter
/*    */ {
/*    */   private ClassNamePatternConverter(String[] options) {
/* 36 */     super("Class Name", "class name", options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ClassNamePatternConverter newInstance(String[] options) {
/* 46 */     return new ClassNamePatternConverter(options);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer toAppendTo) {
/* 55 */     int initialLength = toAppendTo.length();
/* 56 */     LocationInfo li = event.getLocationInformation();
/*    */     
/* 58 */     if (li == null) {
/* 59 */       toAppendTo.append("?");
/*    */     } else {
/* 61 */       toAppendTo.append(li.getClassName());
/*    */     } 
/*    */     
/* 64 */     abbreviate(initialLength, toAppendTo);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\pattern\ClassNamePatternConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */