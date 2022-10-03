/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class TreeMultimap<K, V>
/*     */   extends AbstractSortedKeySortedSetMultimap<K, V>
/*     */ {
/*     */   private transient Comparator<? super K> keyComparator;
/*     */   private transient Comparator<? super V> valueComparator;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
/*  83 */     return new TreeMultimap<>(Ordering.natural(), Ordering.natural());
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
/*     */   public static <K, V> TreeMultimap<K, V> create(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
/*  95 */     return new TreeMultimap<>((Comparator<? super K>)Preconditions.checkNotNull(keyComparator), (Comparator<? super V>)Preconditions.checkNotNull(valueComparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 106 */     return new TreeMultimap<>(Ordering.natural(), Ordering.natural(), multimap);
/*     */   }
/*     */   
/*     */   TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
/* 110 */     super(new TreeMap<>(keyComparator));
/* 111 */     this.keyComparator = keyComparator;
/* 112 */     this.valueComparator = valueComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator, Multimap<? extends K, ? extends V> multimap) {
/* 119 */     this(keyComparator, valueComparator);
/* 120 */     putAll(multimap);
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 125 */     return createMaybeNavigableAsMap();
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
/*     */   SortedSet<V> createCollection() {
/* 137 */     return new TreeSet<>(this.valueComparator);
/*     */   }
/*     */ 
/*     */   
/*     */   Collection<V> createCollection(K key) {
/* 142 */     if (key == null) {
/* 143 */       keyComparator().compare(key, key);
/*     */     }
/* 145 */     return super.createCollection(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Comparator<? super K> keyComparator() {
/* 155 */     return this.keyComparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super V> valueComparator() {
/* 160 */     return this.valueComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public NavigableSet<V> get(K key) {
/* 167 */     return (NavigableSet<V>)super.get(key);
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
/*     */   public NavigableSet<K> keySet() {
/* 181 */     return (NavigableSet<K>)super.keySet();
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
/*     */   public NavigableMap<K, Collection<V>> asMap() {
/* 195 */     return (NavigableMap<K, Collection<V>>)super.asMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 204 */     stream.defaultWriteObject();
/* 205 */     stream.writeObject(keyComparator());
/* 206 */     stream.writeObject(valueComparator());
/* 207 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 213 */     stream.defaultReadObject();
/* 214 */     this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(stream.readObject());
/* 215 */     this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(stream.readObject());
/* 216 */     setMap(new TreeMap<>(this.keyComparator));
/* 217 */     Serialization.populateMultimap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\TreeMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */