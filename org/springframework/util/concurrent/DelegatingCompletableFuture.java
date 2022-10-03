/*    */ package org.springframework.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Future;
/*    */ import org.springframework.util.Assert;
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
/*    */ class DelegatingCompletableFuture<T>
/*    */   extends CompletableFuture<T>
/*    */ {
/*    */   private final Future<T> delegate;
/*    */   
/*    */   public DelegatingCompletableFuture(Future<T> delegate) {
/* 38 */     Assert.notNull(delegate, "Delegate must not be null");
/* 39 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 45 */     boolean result = this.delegate.cancel(mayInterruptIfRunning);
/* 46 */     super.cancel(mayInterruptIfRunning);
/* 47 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\concurrent\DelegatingCompletableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */