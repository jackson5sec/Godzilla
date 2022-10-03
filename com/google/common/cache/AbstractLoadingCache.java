/*    */ package com.google.common.cache;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ExecutionException;
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
/*    */ @GwtIncompatible
/*    */ public abstract class AbstractLoadingCache<K, V>
/*    */   extends AbstractCache<K, V>
/*    */   implements LoadingCache<K, V>
/*    */ {
/*    */   public V getUnchecked(K key) {
/*    */     try {
/* 50 */       return get(key);
/* 51 */     } catch (ExecutionException e) {
/* 52 */       throw new UncheckedExecutionException(e.getCause());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 58 */     Map<K, V> result = Maps.newLinkedHashMap();
/* 59 */     for (K key : keys) {
/* 60 */       if (!result.containsKey(key)) {
/* 61 */         result.put(key, get(key));
/*    */       }
/*    */     } 
/* 64 */     return ImmutableMap.copyOf(result);
/*    */   }
/*    */ 
/*    */   
/*    */   public final V apply(K key) {
/* 69 */     return getUnchecked(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public void refresh(K key) {
/* 74 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\cache\AbstractLoadingCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */