/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class SingletonImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*     */   final transient K singleKey;
/*     */   final transient V singleValue;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   transient ImmutableBiMap<V, K> inverse;
/*     */   
/*     */   SingletonImmutableBiMap(K singleKey, V singleValue) {
/*  42 */     CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
/*  43 */     this.singleKey = singleKey;
/*  44 */     this.singleValue = singleValue;
/*     */   }
/*     */   
/*     */   private SingletonImmutableBiMap(K singleKey, V singleValue, ImmutableBiMap<V, K> inverse) {
/*  48 */     this.singleKey = singleKey;
/*  49 */     this.singleValue = singleValue;
/*  50 */     this.inverse = inverse;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  55 */     return this.singleKey.equals(key) ? this.singleValue : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  60 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/*  65 */     ((BiConsumer<K, V>)Preconditions.checkNotNull(action)).accept(this.singleKey, this.singleValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  70 */     return this.singleKey.equals(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  75 */     return this.singleValue.equals(value);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*  85 */     return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/*  90 */     return ImmutableSet.of(this.singleKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/*  98 */     ImmutableBiMap<V, K> result = this.inverse;
/*  99 */     if (result == null) {
/* 100 */       return this.inverse = new SingletonImmutableBiMap((K)this.singleValue, (V)this.singleKey, this);
/*     */     }
/* 102 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SingletonImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */