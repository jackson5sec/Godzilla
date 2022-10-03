/*    */ package org.apache.log4j.spi;
/*    */ 
/*    */ import org.apache.log4j.Level;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.apache.log4j.helpers.LogLog;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RootLogger
/*    */   extends Logger
/*    */ {
/*    */   public RootLogger(Level level) {
/* 45 */     super("root");
/* 46 */     setLevel(level);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Level getChainedLevel() {
/* 54 */     return this.level;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void setLevel(Level level) {
/* 63 */     if (level == null) {
/* 64 */       LogLog.error("You have tried to set a null level to root.", new Throwable());
/*    */     } else {
/*    */       
/* 67 */       this.level = level;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\spi\RootLogger.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */