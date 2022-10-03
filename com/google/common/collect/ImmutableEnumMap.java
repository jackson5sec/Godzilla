/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Spliterator;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class ImmutableEnumMap<K extends Enum<K>, V>
/*     */   extends ImmutableMap.IteratorBasedImmutableMap<K, V>
/*     */ {
/*     */   private final transient EnumMap<K, V> delegate;
/*     */   
/*     */   static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> map) {
/*     */     Map.Entry<K, V> entry;
/*  38 */     switch (map.size()) {
/*     */       case 0:
/*  40 */         return ImmutableMap.of();
/*     */       case 1:
/*  42 */         entry = Iterables.<Map.Entry<K, V>>getOnlyElement(map.entrySet());
/*  43 */         return ImmutableMap.of(entry.getKey(), entry.getValue());
/*     */     } 
/*  45 */     return new ImmutableEnumMap<>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableEnumMap(EnumMap<K, V> delegate) {
/*  52 */     this.delegate = delegate;
/*  53 */     Preconditions.checkArgument(!delegate.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<K> keyIterator() {
/*  58 */     return Iterators.unmodifiableIterator(this.delegate.keySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<K> keySpliterator() {
/*  63 */     return this.delegate.keySet().spliterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  68 */     return this.delegate.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  73 */     return this.delegate.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/*  78 */     return this.delegate.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object<K, V> object) {
/*  83 */     if (object == this) {
/*  84 */       return true;
/*     */     }
/*  86 */     if (object instanceof ImmutableEnumMap) {
/*  87 */       object = (Object<K, V>)((ImmutableEnumMap)object).delegate;
/*     */     }
/*  89 */     return this.delegate.equals(object);
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/*  94 */     return Maps.unmodifiableEntryIterator(this.delegate.entrySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/*  99 */     return CollectSpliterators.map(this.delegate.entrySet().spliterator(), Maps::unmodifiableEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 104 */     this.delegate.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 115 */     return new EnumSerializedForm<>(this.delegate);
/*     */   }
/*     */   
/*     */   private static class EnumSerializedForm<K extends Enum<K>, V>
/*     */     implements Serializable
/*     */   {
/*     */     final EnumMap<K, V> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EnumSerializedForm(EnumMap<K, V> delegate) {
/* 125 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 129 */       return new ImmutableEnumMap<>(this.delegate);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableEnumMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */