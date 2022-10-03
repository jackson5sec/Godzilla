/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.concurrent.Executor;
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
/*    */ enum DirectExecutor
/*    */   implements Executor
/*    */ {
/* 26 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public void execute(Runnable command) {
/* 30 */     command.run();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 35 */     return "MoreExecutors.directExecutor()";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\DirectExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */