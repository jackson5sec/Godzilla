/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class ImmutableMapValues<K, V>
/*     */   extends ImmutableCollection<V>
/*     */ {
/*     */   private final ImmutableMap<K, V> map;
/*     */   
/*     */   ImmutableMapValues(ImmutableMap<K, V> map) {
/*  40 */     this.map = map;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  45 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<V> iterator() {
/*  50 */     return new UnmodifiableIterator<V>() {
/*  51 */         final UnmodifiableIterator<Map.Entry<K, V>> entryItr = ImmutableMapValues.this.map.entrySet().iterator();
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/*  55 */           return this.entryItr.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public V next() {
/*  60 */           return (V)((Map.Entry)this.entryItr.next()).getValue();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<V> spliterator() {
/*  67 */     return CollectSpliterators.map(this.map.entrySet().spliterator(), Map.Entry::getValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  72 */     return (object != null && Iterators.contains(iterator(), object));
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableList<V> asList() {
/*  82 */     final ImmutableList<Map.Entry<K, V>> entryList = this.map.entrySet().asList();
/*  83 */     return new ImmutableAsList<V>()
/*     */       {
/*     */         public V get(int index) {
/*  86 */           return (V)((Map.Entry)entryList.get(index)).getValue();
/*     */         }
/*     */ 
/*     */         
/*     */         ImmutableCollection<V> delegateCollection() {
/*  91 */           return ImmutableMapValues.this;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public void forEach(Consumer<? super V> action) {
/*  99 */     Preconditions.checkNotNull(action);
/* 100 */     this.map.forEach((k, v) -> action.accept(v));
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 106 */     return new SerializedForm<>(this.map);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class SerializedForm<V> implements Serializable {
/*     */     final ImmutableMap<?, V> map;
/*     */     
/*     */     SerializedForm(ImmutableMap<?, V> map) {
/* 114 */       this.map = map;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     Object readResolve() {
/* 118 */       return this.map.values();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableMapValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */