/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
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
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public final class Runnables
/*    */ {
/* 29 */   private static final Runnable EMPTY_RUNNABLE = new Runnable()
/*    */     {
/*    */       public void run() {}
/*    */     };
/*    */ 
/*    */ 
/*    */   
/*    */   public static Runnable doNothing() {
/* 37 */     return EMPTY_RUNNABLE;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\Runnables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */