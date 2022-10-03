/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ @GwtIncompatible
/*    */ public final class RemovalListeners
/*    */ {
/*    */   public static <K, V> RemovalListener<K, V> asynchronous(final RemovalListener<K, V> listener, final Executor executor) {
/* 42 */     Preconditions.checkNotNull(listener);
/* 43 */     Preconditions.checkNotNull(executor);
/* 44 */     return new RemovalListener<K, V>()
/*    */       {
/*    */         public void onRemoval(final RemovalNotification<K, V> notification) {
/* 47 */           executor.execute(new Runnable()
/*    */               {
/*    */                 public void run()
/*    */                 {
/* 51 */                   listener.onRemoval(notification);
/*    */                 }
/*    */               });
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\RemovalListeners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */