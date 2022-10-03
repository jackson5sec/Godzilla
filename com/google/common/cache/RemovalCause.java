/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ public enum RemovalCause
/*    */ {
/* 35 */   EXPLICIT
/*    */   {
/*    */     boolean wasEvicted() {
/* 38 */       return false;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   REPLACED
/*    */   {
/*    */     boolean wasEvicted() {
/* 51 */       return false;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   COLLECTED
/*    */   {
/*    */     boolean wasEvicted() {
/* 63 */       return true;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   EXPIRED
/*    */   {
/*    */     boolean wasEvicted() {
/* 74 */       return true;
/*    */     }
/*    */   },
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   SIZE
/*    */   {
/*    */     boolean wasEvicted() {
/* 85 */       return true;
/*    */     }
/*    */   };
/*    */   
/*    */   abstract boolean wasEvicted();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\RemovalCause.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */