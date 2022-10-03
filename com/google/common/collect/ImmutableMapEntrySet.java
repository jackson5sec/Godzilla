/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.j2objc.annotations.Weak;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class ImmutableMapEntrySet<K, V>
/*     */   extends ImmutableSet<Map.Entry<K, V>>
/*     */ {
/*     */   abstract ImmutableMap<K, V> map();
/*     */   
/*     */   static final class RegularEntrySet<K, V>
/*     */     extends ImmutableMapEntrySet<K, V>
/*     */   {
/*     */     @Weak
/*     */     private final transient ImmutableMap<K, V> map;
/*     */     private final transient ImmutableList<Map.Entry<K, V>> entries;
/*     */     
/*     */     RegularEntrySet(ImmutableMap<K, V> map, Map.Entry<K, V>[] entries) {
/*  41 */       this(map, ImmutableList.asImmutableList((Object[])entries));
/*     */     }
/*     */     
/*     */     RegularEntrySet(ImmutableMap<K, V> map, ImmutableList<Map.Entry<K, V>> entries) {
/*  45 */       this.map = map;
/*  46 */       this.entries = entries;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<K, V> map() {
/*  51 */       return this.map;
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible("not used in GWT")
/*     */     int copyIntoArray(Object[] dst, int offset) {
/*  57 */       return this.entries.copyIntoArray(dst, offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/*  62 */       return this.entries.iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Map.Entry<K, V>> spliterator() {
/*  67 */       return this.entries.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/*  72 */       this.entries.forEach(action);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableList<Map.Entry<K, V>> createAsList() {
/*  77 */       return new RegularImmutableAsList<>(this, this.entries);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  87 */     return map().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/*  92 */     if (object instanceof Map.Entry) {
/*  93 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/*  94 */       V value = map().get(entry.getKey());
/*  95 */       return (value != null && value.equals(entry.getValue()));
/*     */     } 
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 102 */     return map().isPartialView();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   boolean isHashCodeFast() {
/* 108 */     return map().isHashCodeFast();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     return map().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 119 */     return new EntrySetSerializedForm<>(map());
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class EntrySetSerializedForm<K, V> implements Serializable {
/*     */     final ImmutableMap<K, V> map;
/*     */     
/*     */     EntrySetSerializedForm(ImmutableMap<K, V> map) {
/* 127 */       this.map = map;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     Object readResolve() {
/* 131 */       return this.map.entrySet();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableMapEntrySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */