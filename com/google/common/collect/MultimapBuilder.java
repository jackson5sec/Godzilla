/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
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
/*     */ @GwtCompatible
/*     */ public abstract class MultimapBuilder<K0, V0>
/*     */ {
/*     */   private static final int DEFAULT_EXPECTED_KEYS = 8;
/*     */   
/*     */   private MultimapBuilder() {}
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys() {
/*  76 */     return hashKeys(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultimapBuilderWithKeys<Object> hashKeys(final int expectedKeys) {
/*  86 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/*  87 */     return new MultimapBuilderWithKeys()
/*     */       {
/*     */         <K, V> Map<K, Collection<V>> createMap() {
/*  90 */           return Platform.newHashMapWithExpectedSize(expectedKeys);
/*     */         }
/*     */       };
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
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys() {
/* 104 */     return linkedHashKeys(8);
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
/*     */   public static MultimapBuilderWithKeys<Object> linkedHashKeys(final int expectedKeys) {
/* 117 */     CollectPreconditions.checkNonnegative(expectedKeys, "expectedKeys");
/* 118 */     return new MultimapBuilderWithKeys()
/*     */       {
/*     */         <K, V> Map<K, Collection<V>> createMap() {
/* 121 */           return Platform.newLinkedHashMapWithExpectedSize(expectedKeys);
/*     */         }
/*     */       };
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
/*     */   public static MultimapBuilderWithKeys<Comparable> treeKeys() {
/* 138 */     return treeKeys(Ordering.natural());
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
/*     */   
/*     */   public static <K0> MultimapBuilderWithKeys<K0> treeKeys(final Comparator<K0> comparator) {
/* 155 */     Preconditions.checkNotNull(comparator);
/* 156 */     return new MultimapBuilderWithKeys<K0>()
/*     */       {
/*     */         <K extends K0, V> Map<K, Collection<V>> createMap() {
/* 159 */           return new TreeMap<>(comparator);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K0 extends Enum<K0>> MultimapBuilderWithKeys<K0> enumKeys(final Class<K0> keyClass) {
/* 171 */     Preconditions.checkNotNull(keyClass);
/* 172 */     return new MultimapBuilderWithKeys<K0>()
/*     */       {
/*     */ 
/*     */         
/*     */         <K extends K0, V> Map<K, Collection<V>> createMap()
/*     */         {
/* 178 */           return (Map)new EnumMap<>(keyClass);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class ArrayListSupplier<V> implements Supplier<List<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     ArrayListSupplier(int expectedValuesPerKey) {
/* 187 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> get() {
/* 192 */       return new ArrayList<>(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private enum LinkedListSupplier implements Supplier<List<Object>> {
/* 197 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public static <V> Supplier<List<V>> instance() {
/* 202 */       Supplier<List<V>> result = INSTANCE;
/* 203 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Object> get() {
/* 208 */       return new LinkedList();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     HashSetSupplier(int expectedValuesPerKey) {
/* 216 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> get() {
/* 221 */       return Platform.newHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LinkedHashSetSupplier<V> implements Supplier<Set<V>>, Serializable {
/*     */     private final int expectedValuesPerKey;
/*     */     
/*     */     LinkedHashSetSupplier(int expectedValuesPerKey) {
/* 229 */       this.expectedValuesPerKey = CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> get() {
/* 234 */       return Platform.newLinkedHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TreeSetSupplier<V> implements Supplier<SortedSet<V>>, Serializable {
/*     */     private final Comparator<? super V> comparator;
/*     */     
/*     */     TreeSetSupplier(Comparator<? super V> comparator) {
/* 242 */       this.comparator = (Comparator<? super V>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<V> get() {
/* 247 */       return new TreeSet<>(this.comparator);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EnumSetSupplier<V extends Enum<V>>
/*     */     implements Supplier<Set<V>>, Serializable {
/*     */     private final Class<V> clazz;
/*     */     
/*     */     EnumSetSupplier(Class<V> clazz) {
/* 256 */       this.clazz = (Class<V>)Preconditions.checkNotNull(clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> get() {
/* 261 */       return EnumSet.noneOf(this.clazz);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class MultimapBuilderWithKeys<K0>
/*     */   {
/*     */     private static final int DEFAULT_EXPECTED_VALUES_PER_KEY = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract <K extends K0, V> Map<K, Collection<V>> createMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues() {
/* 282 */       return arrayListValues(2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> arrayListValues(final int expectedValuesPerKey) {
/* 292 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 293 */       return new MultimapBuilder.ListMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> ListMultimap<K, V> build() {
/* 296 */             return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 297 */                 .createMap(), new MultimapBuilder.ArrayListSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.ListMultimapBuilder<K0, Object> linkedListValues() {
/* 305 */       return new MultimapBuilder.ListMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> ListMultimap<K, V> build() {
/* 308 */             return Multimaps.newListMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 309 */                 .createMap(), (Supplier)MultimapBuilder.LinkedListSupplier.instance());
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues() {
/* 316 */       return hashSetValues(2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> hashSetValues(final int expectedValuesPerKey) {
/* 326 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 327 */       return new MultimapBuilder.SetMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> SetMultimap<K, V> build() {
/* 330 */             return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 331 */                 .createMap(), new MultimapBuilder.HashSetSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues() {
/* 339 */       return linkedHashSetValues(2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SetMultimapBuilder<K0, Object> linkedHashSetValues(final int expectedValuesPerKey) {
/* 349 */       CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 350 */       return new MultimapBuilder.SetMultimapBuilder<K0, Object>()
/*     */         {
/*     */           public <K extends K0, V> SetMultimap<K, V> build() {
/* 353 */             return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 354 */                 .createMap(), new MultimapBuilder.LinkedHashSetSupplier(expectedValuesPerKey));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultimapBuilder.SortedSetMultimapBuilder<K0, Comparable> treeSetValues() {
/* 363 */       return treeSetValues(Ordering.natural());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <V0> MultimapBuilder.SortedSetMultimapBuilder<K0, V0> treeSetValues(final Comparator<V0> comparator) {
/* 373 */       Preconditions.checkNotNull(comparator, "comparator");
/* 374 */       return new MultimapBuilder.SortedSetMultimapBuilder<K0, V0>()
/*     */         {
/*     */           public <K extends K0, V extends V0> SortedSetMultimap<K, V> build() {
/* 377 */             return Multimaps.newSortedSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this
/* 378 */                 .createMap(), new MultimapBuilder.TreeSetSupplier(comparator));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <V0 extends Enum<V0>> MultimapBuilder.SetMultimapBuilder<K0, V0> enumSetValues(final Class<V0> valueClass) {
/* 386 */       Preconditions.checkNotNull(valueClass, "valueClass");
/* 387 */       return new MultimapBuilder.SetMultimapBuilder<K0, V0>()
/*     */         {
/*     */ 
/*     */           
/*     */           public <K extends K0, V extends V0> SetMultimap<K, V> build()
/*     */           {
/* 393 */             Supplier<Set<V>> factory = (Supplier)new MultimapBuilder.EnumSetSupplier<>(valueClass);
/* 394 */             return Multimaps.newSetMultimap(MultimapBuilder.MultimapBuilderWithKeys.this.createMap(), factory);
/*     */           }
/*     */         };
/*     */     }
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
/*     */   public <K extends K0, V extends V0> Multimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 409 */     Multimap<K, V> result = build();
/* 410 */     result.putAll(multimap);
/* 411 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract <K extends K0, V extends V0> Multimap<K, V> build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class ListMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     public <K extends K0, V extends V0> ListMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 428 */       return (ListMultimap<K, V>)super.<K, V>build(multimap);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract <K extends K0, V extends V0> ListMultimap<K, V> build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class SetMultimapBuilder<K0, V0>
/*     */     extends MultimapBuilder<K0, V0>
/*     */   {
/*     */     public <K extends K0, V extends V0> SetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 446 */       return (SetMultimap<K, V>)super.<K, V>build(multimap);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract <K extends K0, V extends V0> SetMultimap<K, V> build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class SortedSetMultimapBuilder<K0, V0>
/*     */     extends SetMultimapBuilder<K0, V0>
/*     */   {
/*     */     public <K extends K0, V extends V0> SortedSetMultimap<K, V> build(Multimap<? extends K, ? extends V> multimap) {
/* 464 */       return (SortedSetMultimap<K, V>)super.<K, V>build(multimap);
/*     */     }
/*     */     
/*     */     public abstract <K extends K0, V extends V0> SortedSetMultimap<K, V> build();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\MultimapBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */