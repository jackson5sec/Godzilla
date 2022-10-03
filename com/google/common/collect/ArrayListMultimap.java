/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class ArrayListMultimap<K, V>
/*     */   extends ArrayListMultimapGwtSerializationDependencies<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 3;
/*     */   @VisibleForTesting
/*     */   transient int expectedValuesPerKey;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ArrayListMultimap<K, V> create() {
/*  76 */     return new ArrayListMultimap<>();
/*     */   }
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
/*     */   public static <K, V> ArrayListMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/*  92 */     return new ArrayListMultimap<>(expectedKeys, expectedValuesPerKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ArrayListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 104 */     return new ArrayListMultimap<>(multimap);
/*     */   }
/*     */   
/*     */   private ArrayListMultimap() {
/* 108 */     this(12, 3);
/*     */   }
/*     */   
/*     */   private ArrayListMultimap(int expectedKeys, int expectedValuesPerKey) {
/* 112 */     super(Platform.newHashMapWithExpectedSize(expectedKeys));
/* 113 */     CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 114 */     this.expectedValuesPerKey = expectedValuesPerKey;
/*     */   }
/*     */   
/*     */   private ArrayListMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 118 */     this(multimap
/* 119 */         .keySet().size(), (multimap instanceof ArrayListMultimap) ? ((ArrayListMultimap)multimap).expectedValuesPerKey : 3);
/*     */ 
/*     */ 
/*     */     
/* 123 */     putAll(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<V> createCollection() {
/* 131 */     return new ArrayList<>(this.expectedValuesPerKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void trimToSize() {
/* 143 */     for (Collection<V> collection : backingMap().values()) {
/* 144 */       ArrayList<V> arrayList = (ArrayList<V>)collection;
/* 145 */       arrayList.trimToSize();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 155 */     stream.defaultWriteObject();
/* 156 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 161 */     stream.defaultReadObject();
/* 162 */     this.expectedValuesPerKey = 3;
/* 163 */     int distinctKeys = Serialization.readCount(stream);
/* 164 */     Map<K, Collection<V>> map = Maps.newHashMap();
/* 165 */     setMap(map);
/* 166 */     Serialization.populateMultimap(this, stream, distinctKeys);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ArrayListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */