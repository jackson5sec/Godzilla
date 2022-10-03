/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*    */ public final class SettableFuture<V>
/*    */   extends AbstractFuture.TrustedFuture<V>
/*    */ {
/*    */   public static <V> SettableFuture<V> create() {
/* 42 */     return new SettableFuture<>();
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public boolean set(V value) {
/* 48 */     return super.set(value);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public boolean setException(Throwable throwable) {
/* 54 */     return super.setException(throwable);
/*    */   }
/*    */ 
/*    */   
/*    */   @Beta
/*    */   @CanIgnoreReturnValue
/*    */   public boolean setFuture(ListenableFuture<? extends V> future) {
/* 61 */     return super.setFuture(future);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\SettableFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */