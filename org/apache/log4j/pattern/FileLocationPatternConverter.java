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
/*    */ public final class FileLocationPatternConverter
/*    */   extends LoggingEventPatternConverter
/*    */ {
/* 34 */   private static final FileLocationPatternConverter INSTANCE = new FileLocationPatternConverter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private FileLocationPatternConverter() {
/* 41 */     super("File Location", "file");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static FileLocationPatternConverter newInstance(String[] options) {
/* 51 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void format(LoggingEvent event, StringBuffer output) {
/* 58 */     LocationInfo locationInfo = event.getLocationInformation();
/*    */     
/* 60 */     if (locationInfo != null)
/* 61 */       output.append(locationInfo.getFileName()); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\pattern\FileLocationPatternConverter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */