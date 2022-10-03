/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class HashMultimap<K, V>
/*     */   extends HashMultimapGwtSerializationDependencies<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 2;
/*     */   @VisibleForTesting
/*  50 */   transient int expectedValuesPerKey = 2;
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashMultimap<K, V> create() {
/*  59 */     return new HashMultimap<>();
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
/*     */   public static <K, V> HashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/*  75 */     return new HashMultimap<>(expectedKeys, expectedValuesPerKey);
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
/*     */   public static <K, V> HashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/*  89 */     return new HashMultimap<>(multimap);
/*     */   }
/*     */   
/*     */   private HashMultimap() {
/*  93 */     this(12, 2);
/*     */   }
/*     */   
/*     */   private HashMultimap(int expectedKeys, int expectedValuesPerKey) {
/*  97 */     super(Platform.newHashMapWithExpectedSize(expectedKeys));
/*  98 */     Preconditions.checkArgument((expectedValuesPerKey >= 0));
/*  99 */     this.expectedValuesPerKey = expectedValuesPerKey;
/*     */   }
/*     */   
/*     */   private HashMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 103 */     super(Platform.newHashMapWithExpectedSize(multimap.keySet().size()));
/* 104 */     putAll(multimap);
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
/*     */   Set<V> createCollection() {
/* 116 */     return Platform.newHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 125 */     stream.defaultWriteObject();
/* 126 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 131 */     stream.defaultReadObject();
/* 132 */     this.expectedValuesPerKey = 2;
/* 133 */     int distinctKeys = Serialization.readCount(stream);
/* 134 */     Map<K, Collection<V>> map = Platform.newHashMapWithExpectedSize(12);
/* 135 */     setMap(map);
/* 136 */     Serialization.populateMultimap(this, stream, distinctKeys);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\HashMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */