/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.AbstractMap;
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
/*    */ public final class RemovalNotification<K, V>
/*    */   extends AbstractMap.SimpleImmutableEntry<K, V>
/*    */ {
/*    */   private final RemovalCause cause;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public static <K, V> RemovalNotification<K, V> create(K key, V value, RemovalCause cause) {
/* 47 */     return new RemovalNotification<>(key, value, cause);
/*    */   }
/*    */   
/*    */   private RemovalNotification(K key, V value, RemovalCause cause) {
/* 51 */     super(key, value);
/* 52 */     this.cause = (RemovalCause)Preconditions.checkNotNull(cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public RemovalCause getCause() {
/* 57 */     return this.cause;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean wasEvicted() {
/* 65 */     return this.cause.wasEvicted();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\RemovalNotification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */