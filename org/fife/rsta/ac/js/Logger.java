/*    */ package org.fife.rsta.ac.js;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Logger
/*    */ {
/* 10 */   private static boolean DEBUG = Boolean.parseBoolean(System.getProperty("javascript.debug"));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void log(String msg) {
/* 19 */     if (DEBUG) {
/* 20 */       System.out.println(msg);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final void logError(String msg) {
/* 25 */     System.err.println(msg);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\Logger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */