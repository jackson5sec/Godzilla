/*    */ package com.google.common.base;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class Ticker
/*    */ {
/*    */   public abstract long read();
/*    */   
/*    */   public static Ticker systemTicker() {
/* 44 */     return SYSTEM_TICKER;
/*    */   }
/*    */   
/* 47 */   private static final Ticker SYSTEM_TICKER = new Ticker()
/*    */     {
/*    */       public long read()
/*    */       {
/* 51 */         return Platform.systemNanoTime();
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Ticker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */