/*    */ package com.google.common.escape;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class Platform
/*    */ {
/*    */   static char[] charBufferFromThreadLocal() {
/* 30 */     return DEST_TL.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>()
/*    */     {
/*    */       protected char[] initialValue()
/*    */       {
/* 42 */         return new char[1024];
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\escape\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */