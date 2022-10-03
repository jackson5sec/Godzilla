/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Converter;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BinaryOperator;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Maps
/*      */ {
/*      */   private enum EntryFunction
/*      */     implements Function<Map.Entry<?, ?>, Object>
/*      */   {
/*   93 */     KEY
/*      */     {
/*      */       public Object apply(Map.Entry<?, ?> entry) {
/*   96 */         return entry.getKey();
/*      */       }
/*      */     },
/*   99 */     VALUE
/*      */     {
/*      */       public Object apply(Map.Entry<?, ?> entry) {
/*  102 */         return entry.getValue();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
/*  109 */     return EntryFunction.KEY;
/*      */   }
/*      */ 
/*      */   
/*      */   static <V> Function<Map.Entry<?, V>, V> valueFunction() {
/*  114 */     return EntryFunction.VALUE;
/*      */   }
/*      */   
/*      */   static <K, V> Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> entryIterator) {
/*  118 */     return new TransformedIterator<Map.Entry<K, V>, K>(entryIterator)
/*      */       {
/*      */         K transform(Map.Entry<K, V> entry) {
/*  121 */           return entry.getKey();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static <K, V> Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> entryIterator) {
/*  127 */     return new TransformedIterator<Map.Entry<K, V>, V>(entryIterator)
/*      */       {
/*      */         V transform(Map.Entry<K, V> entry) {
/*  130 */           return entry.getValue();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> map) {
/*  149 */     if (map instanceof ImmutableEnumMap) {
/*      */       
/*  151 */       ImmutableEnumMap<K, V> result = (ImmutableEnumMap)map;
/*  152 */       return result;
/*      */     } 
/*  154 */     Iterator<? extends Map.Entry<K, ? extends V>> entryItr = map.entrySet().iterator();
/*  155 */     if (!entryItr.hasNext()) {
/*  156 */       return ImmutableMap.of();
/*      */     }
/*  158 */     Map.Entry<K, ? extends V> entry1 = entryItr.next();
/*  159 */     Enum<K> enum_ = (Enum)entry1.getKey();
/*  160 */     V value1 = entry1.getValue();
/*  161 */     CollectPreconditions.checkEntryNotNull(enum_, value1);
/*  162 */     Class<K> clazz = enum_.getDeclaringClass();
/*  163 */     EnumMap<K, V> enumMap = new EnumMap<>(clazz);
/*  164 */     enumMap.put((K)enum_, value1);
/*  165 */     while (entryItr.hasNext()) {
/*  166 */       Map.Entry<K, ? extends V> entry = entryItr.next();
/*  167 */       Enum enum_1 = (Enum)entry.getKey();
/*  168 */       V value = entry.getValue();
/*  169 */       CollectPreconditions.checkEntryNotNull(enum_1, value);
/*  170 */       enumMap.put((K)enum_1, value);
/*      */     } 
/*  172 */     return ImmutableEnumMap.asImmutable(enumMap);
/*      */   }
/*      */   
/*      */   private static class Accumulator<K extends Enum<K>, V> {
/*      */     private final BinaryOperator<V> mergeFunction;
/*  177 */     private EnumMap<K, V> map = null;
/*      */     
/*      */     Accumulator(BinaryOperator<V> mergeFunction) {
/*  180 */       this.mergeFunction = mergeFunction;
/*      */     }
/*      */     
/*      */     void put(K key, V value) {
/*  184 */       if (this.map == null) {
/*  185 */         this.map = new EnumMap<>(key.getDeclaringClass());
/*      */       }
/*  187 */       this.map.merge(key, value, this.mergeFunction);
/*      */     }
/*      */     
/*      */     Accumulator<K, V> combine(Accumulator<K, V> other) {
/*  191 */       if (this.map == null)
/*  192 */         return other; 
/*  193 */       if (other.map == null) {
/*  194 */         return this;
/*      */       }
/*  196 */       other.map.forEach(this::put);
/*  197 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableMap<K, V> toImmutableMap() {
/*  202 */       return (this.map == null) ? ImmutableMap.<K, V>of() : ImmutableEnumMap.<K, V>asImmutable(this.map);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  223 */     Preconditions.checkNotNull(keyFunction);
/*  224 */     Preconditions.checkNotNull(valueFunction);
/*  225 */     return Collector.of(() -> new Accumulator<>(()), (accum, t) -> { Enum enum_ = (Enum)Preconditions.checkNotNull(keyFunction.apply(t), "Null key for input %s", t); V newValue = (V)Preconditions.checkNotNull(valueFunction.apply(t), "Null value for input %s", t); accum.put(enum_, newValue); }Accumulator::combine, Accumulator::toImmutableMap, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  256 */     Preconditions.checkNotNull(keyFunction);
/*  257 */     Preconditions.checkNotNull(valueFunction);
/*  258 */     Preconditions.checkNotNull(mergeFunction);
/*      */     
/*  260 */     return Collector.of(() -> new Accumulator<>(mergeFunction), (accum, t) -> { Enum enum_ = (Enum)Preconditions.checkNotNull(keyFunction.apply(t), "Null key for input %s", t); V newValue = (V)Preconditions.checkNotNull(valueFunction.apply(t), "Null value for input %s", t); accum.put(enum_, newValue); }Accumulator::combine, Accumulator::toImmutableMap, new Collector.Characteristics[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> HashMap<K, V> newHashMap() {
/*  285 */     return new HashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
/*  303 */     return new HashMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
/*  318 */     return new HashMap<>(capacity(expectedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int capacity(int expectedSize) {
/*  326 */     if (expectedSize < 3) {
/*  327 */       CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/*  328 */       return expectedSize + 1;
/*      */     } 
/*  330 */     if (expectedSize < 1073741824)
/*      */     {
/*      */ 
/*      */       
/*  334 */       return (int)(expectedSize / 0.75F + 1.0F);
/*      */     }
/*  336 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
/*  351 */     return new LinkedHashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
/*  368 */     return new LinkedHashMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
/*  384 */     return new LinkedHashMap<>(capacity(expectedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
/*  393 */     return new ConcurrentHashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
/*  409 */     return new TreeMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
/*  429 */     return new TreeMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <C, K extends C, V> TreeMap<K, V> newTreeMap(Comparator<C> comparator) {
/*  451 */     return new TreeMap<>(comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
/*  461 */     return new EnumMap<>((Class<K>)Preconditions.checkNotNull(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
/*  477 */     return new EnumMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
/*  490 */     return new IdentityHashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  511 */     if (left instanceof SortedMap) {
/*  512 */       SortedMap<K, ? extends V> sortedLeft = (SortedMap)left;
/*  513 */       return difference(sortedLeft, right);
/*      */     } 
/*  515 */     return difference(left, right, Equivalence.equals());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence) {
/*  536 */     Preconditions.checkNotNull(valueEquivalence);
/*      */     
/*  538 */     Map<K, V> onlyOnLeft = newLinkedHashMap();
/*  539 */     Map<K, V> onlyOnRight = new LinkedHashMap<>(right);
/*  540 */     Map<K, V> onBoth = newLinkedHashMap();
/*  541 */     Map<K, MapDifference.ValueDifference<V>> differences = newLinkedHashMap();
/*  542 */     doDifference(left, right, valueEquivalence, onlyOnLeft, onlyOnRight, onBoth, differences);
/*  543 */     return new MapDifferenceImpl<>(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  565 */     Preconditions.checkNotNull(left);
/*  566 */     Preconditions.checkNotNull(right);
/*  567 */     Comparator<? super K> comparator = orNaturalOrder(left.comparator());
/*  568 */     SortedMap<K, V> onlyOnLeft = newTreeMap(comparator);
/*  569 */     SortedMap<K, V> onlyOnRight = newTreeMap(comparator);
/*  570 */     onlyOnRight.putAll(right);
/*  571 */     SortedMap<K, V> onBoth = newTreeMap(comparator);
/*  572 */     SortedMap<K, MapDifference.ValueDifference<V>> differences = newTreeMap(comparator);
/*  573 */     doDifference(left, right, Equivalence.equals(), onlyOnLeft, onlyOnRight, onBoth, differences);
/*  574 */     return new SortedMapDifferenceImpl<>(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> void doDifference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  585 */     for (Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
/*  586 */       K leftKey = entry.getKey();
/*  587 */       V leftValue = entry.getValue();
/*  588 */       if (right.containsKey(leftKey)) {
/*  589 */         V rightValue = onlyOnRight.remove(leftKey);
/*  590 */         if (valueEquivalence.equivalent(leftValue, rightValue)) {
/*  591 */           onBoth.put(leftKey, leftValue); continue;
/*      */         } 
/*  593 */         differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
/*      */         continue;
/*      */       } 
/*  596 */       onlyOnLeft.put(leftKey, leftValue);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> unmodifiableMap(Map<K, ? extends V> map) {
/*  602 */     if (map instanceof SortedMap) {
/*  603 */       return Collections.unmodifiableSortedMap((SortedMap<K, ? extends V>)map);
/*      */     }
/*  605 */     return Collections.unmodifiableMap(map);
/*      */   }
/*      */ 
/*      */   
/*      */   static class MapDifferenceImpl<K, V>
/*      */     implements MapDifference<K, V>
/*      */   {
/*      */     final Map<K, V> onlyOnLeft;
/*      */     
/*      */     final Map<K, V> onlyOnRight;
/*      */     
/*      */     final Map<K, V> onBoth;
/*      */     final Map<K, MapDifference.ValueDifference<V>> differences;
/*      */     
/*      */     MapDifferenceImpl(Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  620 */       this.onlyOnLeft = Maps.unmodifiableMap(onlyOnLeft);
/*  621 */       this.onlyOnRight = Maps.unmodifiableMap(onlyOnRight);
/*  622 */       this.onBoth = Maps.unmodifiableMap(onBoth);
/*  623 */       this.differences = (Map)Maps.unmodifiableMap((Map)differences);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean areEqual() {
/*  628 */       return (this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesOnlyOnLeft() {
/*  633 */       return this.onlyOnLeft;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesOnlyOnRight() {
/*  638 */       return this.onlyOnRight;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesInCommon() {
/*  643 */       return this.onBoth;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
/*  648 */       return this.differences;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  653 */       if (object == this) {
/*  654 */         return true;
/*      */       }
/*  656 */       if (object instanceof MapDifference) {
/*  657 */         MapDifference<?, ?> other = (MapDifference<?, ?>)object;
/*  658 */         return (entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && 
/*  659 */           entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && 
/*  660 */           entriesInCommon().equals(other.entriesInCommon()) && 
/*  661 */           entriesDiffering().equals(other.entriesDiffering()));
/*      */       } 
/*  663 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  668 */       return Objects.hashCode(new Object[] {
/*  669 */             entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering()
/*      */           });
/*      */     }
/*      */     
/*      */     public String toString() {
/*  674 */       if (areEqual()) {
/*  675 */         return "equal";
/*      */       }
/*      */       
/*  678 */       StringBuilder result = new StringBuilder("not equal");
/*  679 */       if (!this.onlyOnLeft.isEmpty()) {
/*  680 */         result.append(": only on left=").append(this.onlyOnLeft);
/*      */       }
/*  682 */       if (!this.onlyOnRight.isEmpty()) {
/*  683 */         result.append(": only on right=").append(this.onlyOnRight);
/*      */       }
/*  685 */       if (!this.differences.isEmpty()) {
/*  686 */         result.append(": value differences=").append(this.differences);
/*      */       }
/*  688 */       return result.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   static class ValueDifferenceImpl<V> implements MapDifference.ValueDifference<V> {
/*      */     private final V left;
/*      */     private final V right;
/*      */     
/*      */     static <V> MapDifference.ValueDifference<V> create(V left, V right) {
/*  697 */       return new ValueDifferenceImpl<>(left, right);
/*      */     }
/*      */     
/*      */     private ValueDifferenceImpl(V left, V right) {
/*  701 */       this.left = left;
/*  702 */       this.right = right;
/*      */     }
/*      */ 
/*      */     
/*      */     public V leftValue() {
/*  707 */       return this.left;
/*      */     }
/*      */ 
/*      */     
/*      */     public V rightValue() {
/*  712 */       return this.right;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  717 */       if (object instanceof MapDifference.ValueDifference) {
/*  718 */         MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference)object;
/*  719 */         return (Objects.equal(this.left, that.leftValue()) && 
/*  720 */           Objects.equal(this.right, that.rightValue()));
/*      */       } 
/*  722 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  727 */       return Objects.hashCode(new Object[] { this.left, this.right });
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  732 */       return "(" + this.left + ", " + this.right + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class SortedMapDifferenceImpl<K, V>
/*      */     extends MapDifferenceImpl<K, V>
/*      */     implements SortedMapDifference<K, V>
/*      */   {
/*      */     SortedMapDifferenceImpl(SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, MapDifference.ValueDifference<V>> differences) {
/*  743 */       super(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
/*  748 */       return (SortedMap<K, MapDifference.ValueDifference<V>>)super.entriesDiffering();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesInCommon() {
/*  753 */       return (SortedMap<K, V>)super.entriesInCommon();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesOnlyOnLeft() {
/*  758 */       return (SortedMap<K, V>)super.entriesOnlyOnLeft();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesOnlyOnRight() {
/*  763 */       return (SortedMap<K, V>)super.entriesOnlyOnRight();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Comparator<? super E> orNaturalOrder(Comparator<? super E> comparator) {
/*  774 */     if (comparator != null) {
/*  775 */       return comparator;
/*      */     }
/*  777 */     return Ordering.natural();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
/*  805 */     return new AsMapView<>(set, function);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> asMap(SortedSet<K> set, Function<? super K, V> function) {
/*  832 */     return new SortedAsMapView<>(set, function);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> set, Function<? super K, V> function) {
/*  861 */     return new NavigableAsMapView<>(set, function);
/*      */   }
/*      */   
/*      */   private static class AsMapView<K, V>
/*      */     extends ViewCachingAbstractMap<K, V> {
/*      */     private final Set<K> set;
/*      */     final Function<? super K, V> function;
/*      */     
/*      */     Set<K> backingSet() {
/*  870 */       return this.set;
/*      */     }
/*      */     
/*      */     AsMapView(Set<K> set, Function<? super K, V> function) {
/*  874 */       this.set = (Set<K>)Preconditions.checkNotNull(set);
/*  875 */       this.function = (Function<? super K, V>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> createKeySet() {
/*  880 */       return Maps.removeOnlySet(backingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/*  885 */       return Collections2.transform(this.set, this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  890 */       return backingSet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  895 */       return backingSet().contains(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/*  900 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getOrDefault(Object key, V defaultValue) {
/*  905 */       if (Collections2.safeContains(backingSet(), key)) {
/*      */         
/*  907 */         K k = (K)key;
/*  908 */         return (V)this.function.apply(k);
/*      */       } 
/*  910 */       return defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/*  916 */       if (backingSet().remove(key)) {
/*      */         
/*  918 */         K k = (K)key;
/*  919 */         return (V)this.function.apply(k);
/*      */       } 
/*  921 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  927 */       backingSet().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/*      */       class EntrySetImpl
/*      */         extends Maps.EntrySet<K, V>
/*      */       {
/*      */         Map<K, V> map() {
/*  936 */           return Maps.AsMapView.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Map.Entry<K, V>> iterator() {
/*  941 */           return Maps.asMapEntryIterator(Maps.AsMapView.this.backingSet(), Maps.AsMapView.this.function);
/*      */         }
/*      */       };
/*  944 */       return new EntrySetImpl();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/*  949 */       Preconditions.checkNotNull(action);
/*      */       
/*  951 */       backingSet().forEach(k -> action.accept(k, this.function.apply(k)));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(Set<K> set, final Function<? super K, V> function) {
/*  957 */     return new TransformedIterator<K, Map.Entry<K, V>>(set.iterator())
/*      */       {
/*      */         Map.Entry<K, V> transform(K key) {
/*  960 */           return Maps.immutableEntry(key, (V)function.apply(key));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static class SortedAsMapView<K, V>
/*      */     extends AsMapView<K, V> implements SortedMap<K, V> {
/*      */     SortedAsMapView(SortedSet<K> set, Function<? super K, V> function) {
/*  968 */       super(set, function);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> backingSet() {
/*  973 */       return (SortedSet<K>)super.backingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/*  978 */       return backingSet().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/*  983 */       return Maps.removeOnlySortedSet(backingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/*  988 */       return Maps.asMap(backingSet().subSet(fromKey, toKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/*  993 */       return Maps.asMap(backingSet().headSet(toKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/*  998 */       return Maps.asMap(backingSet().tailSet(fromKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 1003 */       return backingSet().first();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 1008 */       return backingSet().last();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static final class NavigableAsMapView<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableSet<K> set;
/*      */     
/*      */     private final Function<? super K, V> function;
/*      */ 
/*      */     
/*      */     NavigableAsMapView(NavigableSet<K> ks, Function<? super K, V> vFunction) {
/* 1023 */       this.set = (NavigableSet<K>)Preconditions.checkNotNull(ks);
/* 1024 */       this.function = (Function<? super K, V>)Preconditions.checkNotNull(vFunction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1030 */       return Maps.asMap(this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 1035 */       return Maps.asMap(this.set.headSet(toKey, inclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 1040 */       return Maps.asMap(this.set.tailSet(fromKey, inclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1045 */       return this.set.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 1050 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getOrDefault(Object key, V defaultValue) {
/* 1055 */       if (Collections2.safeContains(this.set, key)) {
/*      */         
/* 1057 */         K k = (K)key;
/* 1058 */         return (V)this.function.apply(k);
/*      */       } 
/* 1060 */       return defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1066 */       this.set.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 1071 */       return Maps.asMapEntryIterator(this.set, this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 1076 */       return CollectSpliterators.map(this.set.spliterator(), e -> Maps.immutableEntry(e, this.function.apply(e)));
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1081 */       this.set.forEach(k -> action.accept(k, this.function.apply(k)));
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> descendingEntryIterator() {
/* 1086 */       return descendingMap().entrySet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1091 */       return Maps.removeOnlyNavigableSet(this.set);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1096 */       return this.set.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 1101 */       return Maps.asMap(this.set.descendingSet(), this.function);
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> Set<E> removeOnlySet(final Set<E> set) {
/* 1106 */     return new ForwardingSet<E>()
/*      */       {
/*      */         protected Set<E> delegate() {
/* 1109 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(E element) {
/* 1114 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1119 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
/* 1125 */     return new ForwardingSortedSet<E>()
/*      */       {
/*      */         protected SortedSet<E> delegate() {
/* 1128 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(E element) {
/* 1133 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1138 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> headSet(E toElement) {
/* 1143 */           return Maps.removeOnlySortedSet(super.headSet(toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1148 */           return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> tailSet(E fromElement) {
/* 1153 */           return Maps.removeOnlySortedSet(super.tailSet(fromElement));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
/* 1160 */     return new ForwardingNavigableSet<E>()
/*      */       {
/*      */         protected NavigableSet<E> delegate() {
/* 1163 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(E element) {
/* 1168 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1173 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> headSet(E toElement) {
/* 1178 */           return Maps.removeOnlySortedSet(super.headSet(toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1183 */           return Maps.removeOnlyNavigableSet(super.headSet(toElement, inclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1188 */           return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1194 */           return Maps.removeOnlyNavigableSet(super
/* 1195 */               .subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> tailSet(E fromElement) {
/* 1200 */           return Maps.removeOnlySortedSet(super.tailSet(fromElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1205 */           return Maps.removeOnlyNavigableSet(super.tailSet(fromElement, inclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> descendingSet() {
/* 1210 */           return Maps.removeOnlyNavigableSet(super.descendingSet());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) {
/* 1233 */     return toMap(keys.iterator(), valueFunction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
/* 1251 */     Preconditions.checkNotNull(valueFunction);
/*      */     
/* 1253 */     Map<K, V> builder = newLinkedHashMap();
/* 1254 */     while (keys.hasNext()) {
/* 1255 */       K key = keys.next();
/* 1256 */       builder.put(key, (V)valueFunction.apply(key));
/*      */     } 
/* 1258 */     return ImmutableMap.copyOf(builder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) {
/* 1293 */     return uniqueIndex(values.iterator(), keyFunction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1328 */     Preconditions.checkNotNull(keyFunction);
/* 1329 */     ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
/* 1330 */     while (values.hasNext()) {
/* 1331 */       V value = values.next();
/* 1332 */       builder.put((K)keyFunction.apply(value), value);
/*      */     } 
/*      */     try {
/* 1335 */       return builder.build();
/* 1336 */     } catch (IllegalArgumentException duplicateKeys) {
/* 1337 */       throw new IllegalArgumentException(duplicateKeys
/* 1338 */           .getMessage() + ". To index multiple values under a key, use Multimaps.index.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static ImmutableMap<String, String> fromProperties(Properties properties) {
/* 1355 */     ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/*      */     
/* 1357 */     for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
/* 1358 */       String key = (String)e.nextElement();
/* 1359 */       builder.put(key, properties.getProperty(key));
/*      */     } 
/*      */     
/* 1362 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <K, V> Map.Entry<K, V> immutableEntry(K key, V value) {
/* 1379 */     return new ImmutableEntry<>(key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> entrySet) {
/* 1391 */     return new UnmodifiableEntrySet<>(Collections.unmodifiableSet(entrySet));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
/* 1404 */     Preconditions.checkNotNull(entry);
/* 1405 */     return new AbstractMapEntry<K, V>()
/*      */       {
/*      */         public K getKey() {
/* 1408 */           return (K)entry.getKey();
/*      */         }
/*      */ 
/*      */         
/*      */         public V getValue() {
/* 1413 */           return (V)entry.getValue();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> UnmodifiableIterator<Map.Entry<K, V>> unmodifiableEntryIterator(final Iterator<Map.Entry<K, V>> entryIterator) {
/* 1420 */     return new UnmodifiableIterator<Map.Entry<K, V>>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1423 */           return entryIterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public Map.Entry<K, V> next() {
/* 1428 */           return Maps.unmodifiableEntry(entryIterator.next());
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntries<K, V>
/*      */     extends ForwardingCollection<Map.Entry<K, V>> {
/*      */     private final Collection<Map.Entry<K, V>> entries;
/*      */     
/*      */     UnmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/* 1438 */       this.entries = entries;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<Map.Entry<K, V>> delegate() {
/* 1443 */       return this.entries;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1448 */       return Maps.unmodifiableEntryIterator(this.entries.iterator());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 1455 */       return standardToArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 1460 */       return (T[])standardToArray((Object[])array);
/*      */     }
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntrySet<K, V>
/*      */     extends UnmodifiableEntries<K, V>
/*      */     implements Set<Map.Entry<K, V>> {
/*      */     UnmodifiableEntrySet(Set<Map.Entry<K, V>> entries) {
/* 1468 */       super(entries);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1475 */       return Sets.equalsImpl(this, object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1480 */       return Sets.hashCodeImpl(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <A, B> Converter<A, B> asConverter(BiMap<A, B> bimap) {
/* 1495 */     return new BiMapConverter<>(bimap);
/*      */   }
/*      */   
/*      */   private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable { private final BiMap<A, B> bimap;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     BiMapConverter(BiMap<A, B> bimap) {
/* 1502 */       this.bimap = (BiMap<A, B>)Preconditions.checkNotNull(bimap);
/*      */     }
/*      */ 
/*      */     
/*      */     protected B doForward(A a) {
/* 1507 */       return convert(this.bimap, a);
/*      */     }
/*      */ 
/*      */     
/*      */     protected A doBackward(B b) {
/* 1512 */       return convert(this.bimap.inverse(), b);
/*      */     }
/*      */     
/*      */     private static <X, Y> Y convert(BiMap<X, Y> bimap, X input) {
/* 1516 */       Y output = bimap.get(input);
/* 1517 */       Preconditions.checkArgument((output != null), "No non-null mapping present for input: %s", input);
/* 1518 */       return output;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 1523 */       if (object instanceof BiMapConverter) {
/* 1524 */         BiMapConverter<?, ?> that = (BiMapConverter<?, ?>)object;
/* 1525 */         return this.bimap.equals(that.bimap);
/*      */       } 
/* 1527 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1532 */       return this.bimap.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1538 */       return "Maps.asConverter(" + this.bimap + ")";
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
/* 1574 */     return Synchronized.biMap(bimap, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) {
/* 1589 */     return new UnmodifiableBiMap<>(bimap, null);
/*      */   }
/*      */   
/*      */   private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
/*      */     final Map<K, V> unmodifiableMap;
/*      */     final BiMap<? extends K, ? extends V> delegate;
/*      */     @RetainedWith
/*      */     BiMap<V, K> inverse;
/*      */     transient Set<V> values;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, BiMap<V, K> inverse) {
/* 1601 */       this.unmodifiableMap = Collections.unmodifiableMap(delegate);
/* 1602 */       this.delegate = delegate;
/* 1603 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Map<K, V> delegate() {
/* 1608 */       return this.unmodifiableMap;
/*      */     }
/*      */ 
/*      */     
/*      */     public V forcePut(K key, V value) {
/* 1613 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1618 */       BiMap<V, K> result = this.inverse;
/* 1619 */       return (result == null) ? (this
/* 1620 */         .inverse = new UnmodifiableBiMap(this.delegate.inverse(), this)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 1626 */       Set<V> result = this.values;
/* 1627 */       return (result == null) ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1668 */     return transformEntries(fromMap, asEntryTransformer(function));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1709 */     return transformEntries(fromMap, asEntryTransformer(function));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1753 */     return transformEntries(fromMap, asEntryTransformer(function));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1805 */     return new TransformedEntriesMap<>(fromMap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1857 */     return new TransformedEntriesSortedMap<>(fromMap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1911 */     return new TransformedEntriesNavigableMap<>(fromMap, transformer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
/* 1945 */     Preconditions.checkNotNull(function);
/* 1946 */     return new EntryTransformer<K, V1, V2>()
/*      */       {
/*      */         public V2 transformEntry(K key, V1 value) {
/* 1949 */           return (V2)function.apply(value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> transformer, final K key) {
/* 1956 */     Preconditions.checkNotNull(transformer);
/* 1957 */     return new Function<V1, V2>()
/*      */       {
/*      */         public V2 apply(V1 v1) {
/* 1960 */           return transformer.transformEntry(key, v1);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1968 */     Preconditions.checkNotNull(transformer);
/* 1969 */     return new Function<Map.Entry<K, V1>, V2>()
/*      */       {
/*      */         public V2 apply(Map.Entry<K, V1> entry) {
/* 1972 */           return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> transformer, final Map.Entry<K, V1> entry) {
/* 1980 */     Preconditions.checkNotNull(transformer);
/* 1981 */     Preconditions.checkNotNull(entry);
/* 1982 */     return new AbstractMapEntry<K, V2>()
/*      */       {
/*      */         public K getKey() {
/* 1985 */           return (K)entry.getKey();
/*      */         }
/*      */ 
/*      */         
/*      */         public V2 getValue() {
/* 1990 */           return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1998 */     Preconditions.checkNotNull(transformer);
/* 1999 */     return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>()
/*      */       {
/*      */         public Map.Entry<K, V2> apply(Map.Entry<K, V1> entry) {
/* 2002 */           return Maps.transformEntry(transformer, entry);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static class TransformedEntriesMap<K, V1, V2>
/*      */     extends IteratorBasedAbstractMap<K, V2> {
/*      */     final Map<K, V1> fromMap;
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMap(Map<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2013 */       this.fromMap = (Map<K, V1>)Preconditions.checkNotNull(fromMap);
/* 2014 */       this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2019 */       return this.fromMap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 2024 */       return this.fromMap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V2 get(Object key) {
/* 2029 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V2 getOrDefault(Object key, V2 defaultValue) {
/* 2036 */       V1 value = this.fromMap.get(key);
/* 2037 */       return (value != null || this.fromMap.containsKey(key)) ? this.transformer
/* 2038 */         .transformEntry((K)key, value) : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V2 remove(Object key) {
/* 2046 */       return this.fromMap.containsKey(key) ? this.transformer
/* 2047 */         .transformEntry((K)key, this.fromMap.remove(key)) : null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2053 */       this.fromMap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 2058 */       return this.fromMap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V2>> entryIterator() {
/* 2063 */       return Iterators.transform(this.fromMap
/* 2064 */           .entrySet().iterator(), Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     Spliterator<Map.Entry<K, V2>> entrySpliterator() {
/* 2069 */       return CollectSpliterators.map(this.fromMap
/* 2070 */           .entrySet().spliterator(), (Function<?, ? extends Map.Entry<K, V2>>)Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V2> action) {
/* 2075 */       Preconditions.checkNotNull(action);
/*      */       
/* 2077 */       this.fromMap.forEach((k, v1) -> action.accept(k, this.transformer.transformEntry((K)k, (V1)v1)));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> values() {
/* 2082 */       return new Maps.Values<>(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static class TransformedEntriesSortedMap<K, V1, V2>
/*      */     extends TransformedEntriesMap<K, V1, V2>
/*      */     implements SortedMap<K, V2> {
/*      */     protected SortedMap<K, V1> fromMap() {
/* 2090 */       return (SortedMap<K, V1>)this.fromMap;
/*      */     }
/*      */ 
/*      */     
/*      */     TransformedEntriesSortedMap(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2095 */       super(fromMap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 2100 */       return fromMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 2105 */       return fromMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> headMap(K toKey) {
/* 2110 */       return Maps.transformEntries(fromMap().headMap(toKey), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 2115 */       return fromMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> subMap(K fromKey, K toKey) {
/* 2120 */       return Maps.transformEntries(fromMap().subMap(fromKey, toKey), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> tailMap(K fromKey) {
/* 2125 */       return Maps.transformEntries(fromMap().tailMap(fromKey), this.transformer);
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class TransformedEntriesNavigableMap<K, V1, V2>
/*      */     extends TransformedEntriesSortedMap<K, V1, V2>
/*      */     implements NavigableMap<K, V2>
/*      */   {
/*      */     TransformedEntriesNavigableMap(NavigableMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2135 */       super(fromMap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> ceilingEntry(K key) {
/* 2140 */       return transformEntry(fromMap().ceilingEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 2145 */       return fromMap().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 2150 */       return fromMap().descendingKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> descendingMap() {
/* 2155 */       return Maps.transformEntries(fromMap().descendingMap(), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> firstEntry() {
/* 2160 */       return transformEntry(fromMap().firstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> floorEntry(K key) {
/* 2165 */       return transformEntry(fromMap().floorEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 2170 */       return fromMap().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> headMap(K toKey) {
/* 2175 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> headMap(K toKey, boolean inclusive) {
/* 2180 */       return Maps.transformEntries(fromMap().headMap(toKey, inclusive), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> higherEntry(K key) {
/* 2185 */       return transformEntry(fromMap().higherEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 2190 */       return fromMap().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> lastEntry() {
/* 2195 */       return transformEntry(fromMap().lastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> lowerEntry(K key) {
/* 2200 */       return transformEntry(fromMap().lowerEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 2205 */       return fromMap().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 2210 */       return fromMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> pollFirstEntry() {
/* 2215 */       return transformEntry(fromMap().pollFirstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> pollLastEntry() {
/* 2220 */       return transformEntry(fromMap().pollLastEntry());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 2226 */       return Maps.transformEntries(
/* 2227 */           fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> subMap(K fromKey, K toKey) {
/* 2232 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> tailMap(K fromKey) {
/* 2237 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> tailMap(K fromKey, boolean inclusive) {
/* 2242 */       return Maps.transformEntries(fromMap().tailMap(fromKey, inclusive), this.transformer);
/*      */     }
/*      */     
/*      */     private Map.Entry<K, V2> transformEntry(Map.Entry<K, V1> entry) {
/* 2246 */       return (entry == null) ? null : Maps.<V2, K, V1>transformEntry(this.transformer, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     protected NavigableMap<K, V1> fromMap() {
/* 2251 */       return (NavigableMap<K, V1>)super.fromMap();
/*      */     }
/*      */   }
/*      */   
/*      */   static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> keyPredicate) {
/* 2256 */     return Predicates.compose(keyPredicate, keyFunction());
/*      */   }
/*      */   
/*      */   static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> valuePredicate) {
/* 2260 */     return Predicates.compose(valuePredicate, valueFunction());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2288 */     Preconditions.checkNotNull(keyPredicate);
/* 2289 */     Predicate<Map.Entry<K, ?>> entryPredicate = keyPredicateOnEntries(keyPredicate);
/* 2290 */     return (unfiltered instanceof AbstractFilteredMap) ? 
/* 2291 */       filterFiltered((AbstractFilteredMap<K, V>)unfiltered, (Predicate)entryPredicate) : new FilteredKeyMap<>(
/* 2292 */         (Map<K, V>)Preconditions.checkNotNull(unfiltered), keyPredicate, (Predicate)entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2325 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2359 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2388 */     Preconditions.checkNotNull(keyPredicate);
/* 2389 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2417 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2448 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2480 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2512 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2541 */     Preconditions.checkNotNull(entryPredicate);
/* 2542 */     return (unfiltered instanceof AbstractFilteredMap) ? 
/* 2543 */       filterFiltered((AbstractFilteredMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryMap<>(
/* 2544 */         (Map<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2575 */     Preconditions.checkNotNull(entryPredicate);
/* 2576 */     return (unfiltered instanceof FilteredEntrySortedMap) ? 
/* 2577 */       filterFiltered((FilteredEntrySortedMap<K, V>)unfiltered, entryPredicate) : new FilteredEntrySortedMap<>(
/* 2578 */         (SortedMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2610 */     Preconditions.checkNotNull(entryPredicate);
/* 2611 */     return (unfiltered instanceof FilteredEntryNavigableMap) ? 
/* 2612 */       filterFiltered((FilteredEntryNavigableMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryNavigableMap<>(
/* 2613 */         (NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2645 */     Preconditions.checkNotNull(unfiltered);
/* 2646 */     Preconditions.checkNotNull(entryPredicate);
/* 2647 */     return (unfiltered instanceof FilteredEntryBiMap) ? 
/* 2648 */       filterFiltered((FilteredEntryBiMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryBiMap<>(unfiltered, entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2658 */     return new FilteredEntryMap<>(map.unfiltered, 
/* 2659 */         Predicates.and(map.predicate, entryPredicate));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2668 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/* 2669 */     return new FilteredEntrySortedMap<>(map.sortedMap(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2680 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.entryPredicate, entryPredicate);
/* 2681 */     return new FilteredEntryNavigableMap<>(map.unfiltered, predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2690 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/* 2691 */     return new FilteredEntryBiMap<>(map.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   private static abstract class AbstractFilteredMap<K, V> extends ViewCachingAbstractMap<K, V> {
/*      */     final Map<K, V> unfiltered;
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2699 */       this.unfiltered = unfiltered;
/* 2700 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean apply(Object key, V value) {
/* 2707 */       K k = (K)key;
/* 2708 */       return this.predicate.apply(Maps.immutableEntry(k, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/* 2713 */       Preconditions.checkArgument(apply(key, value));
/* 2714 */       return this.unfiltered.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 2719 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 2720 */         Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
/*      */       }
/* 2722 */       this.unfiltered.putAll(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 2727 */       return (this.unfiltered.containsKey(key) && apply(key, this.unfiltered.get(key)));
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 2732 */       V value = this.unfiltered.get(key);
/* 2733 */       return (value != null && apply(key, value)) ? value : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2738 */       return entrySet().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/* 2743 */       return containsKey(key) ? this.unfiltered.remove(key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/* 2748 */       return new Maps.FilteredMapValues<>(this, this.unfiltered, this.predicate);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class FilteredMapValues<K, V>
/*      */     extends Values<K, V> {
/*      */     final Map<K, V> unfiltered;
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     FilteredMapValues(Map<K, V> filteredMap, Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2758 */       super(filteredMap);
/* 2759 */       this.unfiltered = unfiltered;
/* 2760 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2765 */       Iterator<Map.Entry<K, V>> entryItr = this.unfiltered.entrySet().iterator();
/* 2766 */       while (entryItr.hasNext()) {
/* 2767 */         Map.Entry<K, V> entry = entryItr.next();
/* 2768 */         if (this.predicate.apply(entry) && Objects.equal(entry.getValue(), o)) {
/* 2769 */           entryItr.remove();
/* 2770 */           return true;
/*      */         } 
/*      */       } 
/* 2773 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> collection) {
/* 2778 */       Iterator<Map.Entry<K, V>> entryItr = this.unfiltered.entrySet().iterator();
/* 2779 */       boolean result = false;
/* 2780 */       while (entryItr.hasNext()) {
/* 2781 */         Map.Entry<K, V> entry = entryItr.next();
/* 2782 */         if (this.predicate.apply(entry) && collection.contains(entry.getValue())) {
/* 2783 */           entryItr.remove();
/* 2784 */           result = true;
/*      */         } 
/*      */       } 
/* 2787 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> collection) {
/* 2792 */       Iterator<Map.Entry<K, V>> entryItr = this.unfiltered.entrySet().iterator();
/* 2793 */       boolean result = false;
/* 2794 */       while (entryItr.hasNext()) {
/* 2795 */         Map.Entry<K, V> entry = entryItr.next();
/* 2796 */         if (this.predicate.apply(entry) && !collection.contains(entry.getValue())) {
/* 2797 */           entryItr.remove();
/* 2798 */           result = true;
/*      */         } 
/*      */       } 
/* 2801 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2807 */       return Lists.<V>newArrayList(iterator()).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 2812 */       return (T[])Lists.<V>newArrayList(iterator()).toArray((Object[])array);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FilteredKeyMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Predicate<? super K> keyPredicate;
/*      */     
/*      */     FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2823 */       super(unfiltered, entryPredicate);
/* 2824 */       this.keyPredicate = keyPredicate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 2829 */       return Sets.filter(this.unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 2834 */       return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 2842 */       return (this.unfiltered.containsKey(key) && this.keyPredicate.apply(key));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class FilteredEntryMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Set<Map.Entry<K, V>> filteredEntrySet;
/*      */ 
/*      */     
/*      */     FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2854 */       super(unfiltered, entryPredicate);
/* 2855 */       this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 2860 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
/*      */       private EntrySet() {}
/*      */       
/*      */       protected Set<Map.Entry<K, V>> delegate() {
/* 2867 */         return Maps.FilteredEntryMap.this.filteredEntrySet;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, V>> iterator() {
/* 2872 */         return new TransformedIterator<Map.Entry<K, V>, Map.Entry<K, V>>(Maps.FilteredEntryMap.this.filteredEntrySet.iterator())
/*      */           {
/*      */             Map.Entry<K, V> transform(final Map.Entry<K, V> entry) {
/* 2875 */               return new ForwardingMapEntry<K, V>()
/*      */                 {
/*      */                   protected Map.Entry<K, V> delegate() {
/* 2878 */                     return entry;
/*      */                   }
/*      */ 
/*      */                   
/*      */                   public V setValue(V newValue) {
/* 2883 */                     Preconditions.checkArgument(Maps.FilteredEntryMap.this.apply(getKey(), newValue));
/* 2884 */                     return super.setValue(newValue);
/*      */                   }
/*      */                 };
/*      */             }
/*      */           };
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 2894 */       return new KeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     static <K, V> boolean removeAllKeys(Map<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate, Collection<?> keyCollection) {
/* 2899 */       Iterator<Map.Entry<K, V>> entryItr = map.entrySet().iterator();
/* 2900 */       boolean result = false;
/* 2901 */       while (entryItr.hasNext()) {
/* 2902 */         Map.Entry<K, V> entry = entryItr.next();
/* 2903 */         if (entryPredicate.apply(entry) && keyCollection.contains(entry.getKey())) {
/* 2904 */           entryItr.remove();
/* 2905 */           result = true;
/*      */         } 
/*      */       } 
/* 2908 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     static <K, V> boolean retainAllKeys(Map<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate, Collection<?> keyCollection) {
/* 2913 */       Iterator<Map.Entry<K, V>> entryItr = map.entrySet().iterator();
/* 2914 */       boolean result = false;
/* 2915 */       while (entryItr.hasNext()) {
/* 2916 */         Map.Entry<K, V> entry = entryItr.next();
/* 2917 */         if (entryPredicate.apply(entry) && !keyCollection.contains(entry.getKey())) {
/* 2918 */           entryItr.remove();
/* 2919 */           result = true;
/*      */         } 
/*      */       } 
/* 2922 */       return result;
/*      */     }
/*      */     
/*      */     class KeySet
/*      */       extends Maps.KeySet<K, V> {
/*      */       KeySet() {
/* 2928 */         super(Maps.FilteredEntryMap.this);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 2933 */         if (Maps.FilteredEntryMap.this.containsKey(o)) {
/* 2934 */           Maps.FilteredEntryMap.this.unfiltered.remove(o);
/* 2935 */           return true;
/*      */         } 
/* 2937 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean removeAll(Collection<?> collection) {
/* 2942 */         return Maps.FilteredEntryMap.removeAllKeys(Maps.FilteredEntryMap.this.unfiltered, Maps.FilteredEntryMap.this.predicate, collection);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean retainAll(Collection<?> collection) {
/* 2947 */         return Maps.FilteredEntryMap.retainAllKeys(Maps.FilteredEntryMap.this.unfiltered, Maps.FilteredEntryMap.this.predicate, collection);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public Object[] toArray() {
/* 2953 */         return Lists.<K>newArrayList(iterator()).toArray();
/*      */       }
/*      */ 
/*      */       
/*      */       public <T> T[] toArray(T[] array) {
/* 2958 */         return (T[])Lists.<K>newArrayList(iterator()).toArray((Object[])array);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FilteredEntrySortedMap<K, V>
/*      */     extends FilteredEntryMap<K, V>
/*      */     implements SortedMap<K, V>
/*      */   {
/*      */     FilteredEntrySortedMap(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2968 */       super(unfiltered, entryPredicate);
/*      */     }
/*      */     
/*      */     SortedMap<K, V> sortedMap() {
/* 2972 */       return (SortedMap<K, V>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 2977 */       return (SortedSet<K>)super.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> createKeySet() {
/* 2982 */       return new SortedKeySet();
/*      */     }
/*      */     
/*      */     class SortedKeySet
/*      */       extends Maps.FilteredEntryMap<K, V>.KeySet
/*      */       implements SortedSet<K> {
/*      */       public Comparator<? super K> comparator() {
/* 2989 */         return Maps.FilteredEntrySortedMap.this.sortedMap().comparator();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> subSet(K fromElement, K toElement) {
/* 2994 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.subMap(fromElement, toElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> headSet(K toElement) {
/* 2999 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.headMap(toElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> tailSet(K fromElement) {
/* 3004 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.tailMap(fromElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public K first() {
/* 3009 */         return (K)Maps.FilteredEntrySortedMap.this.firstKey();
/*      */       }
/*      */ 
/*      */       
/*      */       public K last() {
/* 3014 */         return (K)Maps.FilteredEntrySortedMap.this.lastKey();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3020 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 3026 */       return keySet().iterator().next();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 3031 */       SortedMap<K, V> headMap = sortedMap();
/*      */       
/*      */       while (true) {
/* 3034 */         K key = headMap.lastKey();
/* 3035 */         if (apply(key, this.unfiltered.get(key))) {
/* 3036 */           return key;
/*      */         }
/* 3038 */         headMap = sortedMap().headMap(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 3044 */       return new FilteredEntrySortedMap(sortedMap().headMap(toKey), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 3049 */       return new FilteredEntrySortedMap(sortedMap().subMap(fromKey, toKey), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 3054 */       return new FilteredEntrySortedMap(sortedMap().tailMap(fromKey), this.predicate);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class FilteredEntryNavigableMap<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableMap<K, V> unfiltered;
/*      */     
/*      */     private final Predicate<? super Map.Entry<K, V>> entryPredicate;
/*      */     
/*      */     private final Map<K, V> filteredDelegate;
/*      */ 
/*      */     
/*      */     FilteredEntryNavigableMap(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3072 */       this.unfiltered = (NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered);
/* 3073 */       this.entryPredicate = entryPredicate;
/* 3074 */       this.filteredDelegate = new Maps.FilteredEntryMap<>(unfiltered, entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3079 */       return this.unfiltered.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 3084 */       return new Maps.NavigableKeySet<K, V>(this)
/*      */         {
/*      */           public boolean removeAll(Collection<?> collection) {
/* 3087 */             return Maps.FilteredEntryMap.removeAllKeys(Maps.FilteredEntryNavigableMap.this.unfiltered, Maps.FilteredEntryNavigableMap.this.entryPredicate, collection);
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean retainAll(Collection<?> collection) {
/* 3092 */             return Maps.FilteredEntryMap.retainAllKeys(Maps.FilteredEntryNavigableMap.this.unfiltered, Maps.FilteredEntryNavigableMap.this.entryPredicate, collection);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 3099 */       return new Maps.FilteredMapValues<>(this, this.unfiltered, this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 3104 */       return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> descendingEntryIterator() {
/* 3109 */       return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3114 */       return this.filteredDelegate.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3119 */       return !Iterables.any(this.unfiltered.entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 3124 */       return this.filteredDelegate.get(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 3129 */       return this.filteredDelegate.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/* 3134 */       return this.filteredDelegate.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/* 3139 */       return this.filteredDelegate.remove(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> m) {
/* 3144 */       this.filteredDelegate.putAll(m);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3149 */       this.filteredDelegate.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3154 */       return this.filteredDelegate.entrySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 3159 */       return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 3164 */       return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 3169 */       return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 3175 */       return Maps.filterEntries(this.unfiltered
/* 3176 */           .subMap(fromKey, fromInclusive, toKey, toInclusive), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 3181 */       return Maps.filterEntries(this.unfiltered.headMap(toKey, inclusive), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 3186 */       return Maps.filterEntries(this.unfiltered.tailMap(fromKey, inclusive), this.entryPredicate);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class FilteredEntryBiMap<K, V>
/*      */     extends FilteredEntryMap<K, V> implements BiMap<K, V> {
/*      */     @RetainedWith
/*      */     private final BiMap<V, K> inverse;
/*      */     
/*      */     private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> forwardPredicate) {
/* 3196 */       return new Predicate<Map.Entry<V, K>>()
/*      */         {
/*      */           public boolean apply(Map.Entry<V, K> input) {
/* 3199 */             return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate) {
/* 3205 */       super(delegate, predicate);
/* 3206 */       this
/* 3207 */         .inverse = new FilteredEntryBiMap(delegate.inverse(), inversePredicate(predicate), this);
/*      */     }
/*      */ 
/*      */     
/*      */     private FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate, BiMap<V, K> inverse) {
/* 3212 */       super(delegate, predicate);
/* 3213 */       this.inverse = inverse;
/*      */     }
/*      */     
/*      */     BiMap<K, V> unfiltered() {
/* 3217 */       return (BiMap<K, V>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     public V forcePut(K key, V value) {
/* 3222 */       Preconditions.checkArgument(apply(key, value));
/* 3223 */       return unfiltered().forcePut(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 3228 */       unfiltered()
/* 3229 */         .replaceAll((key, value) -> this.predicate.apply(Maps.immutableEntry(key, value)) ? function.apply(key, value) : value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 3238 */       return this.inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 3243 */       return this.inverse.keySet();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, ? extends V> map) {
/* 3268 */     Preconditions.checkNotNull(map);
/* 3269 */     if (map instanceof UnmodifiableNavigableMap)
/*      */     {
/* 3271 */       return (NavigableMap)map;
/*      */     }
/*      */     
/* 3274 */     return new UnmodifiableNavigableMap<>(map);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Map.Entry<K, V> unmodifiableOrNull(Map.Entry<K, ? extends V> entry) {
/* 3280 */     return (entry == null) ? null : unmodifiableEntry(entry);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
/*      */     private final NavigableMap<K, ? extends V> delegate;
/*      */     private transient UnmodifiableNavigableMap<K, V> descendingMap;
/*      */     
/*      */     UnmodifiableNavigableMap(NavigableMap<K, ? extends V> delegate) {
/* 3289 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     UnmodifiableNavigableMap(NavigableMap<K, ? extends V> delegate, UnmodifiableNavigableMap<K, V> descendingMap) {
/* 3294 */       this.delegate = delegate;
/* 3295 */       this.descendingMap = descendingMap;
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedMap<K, V> delegate() {
/* 3300 */       return Collections.unmodifiableSortedMap(this.delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 3305 */       return Maps.unmodifiableOrNull(this.delegate.lowerEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 3310 */       return this.delegate.lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 3315 */       return Maps.unmodifiableOrNull(this.delegate.floorEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 3320 */       return this.delegate.floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 3325 */       return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 3330 */       return this.delegate.ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 3335 */       return Maps.unmodifiableOrNull(this.delegate.higherEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 3340 */       return this.delegate.higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> firstEntry() {
/* 3345 */       return Maps.unmodifiableOrNull(this.delegate.firstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lastEntry() {
/* 3350 */       return Maps.unmodifiableOrNull(this.delegate.lastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public final Map.Entry<K, V> pollFirstEntry() {
/* 3355 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public final Map.Entry<K, V> pollLastEntry() {
/* 3360 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 3367 */       UnmodifiableNavigableMap<K, V> result = this.descendingMap;
/* 3368 */       return (result == null) ? (this
/* 3369 */         .descendingMap = new UnmodifiableNavigableMap(this.delegate.descendingMap(), this)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 3375 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 3380 */       return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 3385 */       return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 3390 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 3396 */       return Maps.unmodifiableNavigableMap(this.delegate
/* 3397 */           .subMap(fromKey, fromInclusive, toKey, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 3402 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 3407 */       return Maps.unmodifiableNavigableMap(this.delegate.headMap(toKey, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 3412 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 3417 */       return Maps.unmodifiableNavigableMap(this.delegate.tailMap(fromKey, inclusive));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
/* 3473 */     return Synchronized.navigableMap(navigableMap);
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtCompatible
/*      */   static abstract class ViewCachingAbstractMap<K, V>
/*      */     extends AbstractMap<K, V>
/*      */   {
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */     
/*      */     private transient Set<K> keySet;
/*      */     
/*      */     private transient Collection<V> values;
/*      */ 
/*      */     
/*      */     abstract Set<Map.Entry<K, V>> createEntrySet();
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3492 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 3493 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 3500 */       Set<K> result = this.keySet;
/* 3501 */       return (result == null) ? (this.keySet = createKeySet()) : result;
/*      */     }
/*      */     
/*      */     Set<K> createKeySet() {
/* 3505 */       return new Maps.KeySet<>(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 3512 */       Collection<V> result = this.values;
/* 3513 */       return (result == null) ? (this.values = createValues()) : result;
/*      */     }
/*      */     
/*      */     Collection<V> createValues() {
/* 3517 */       return new Maps.Values<>(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class IteratorBasedAbstractMap<K, V>
/*      */     extends AbstractMap<K, V> {
/*      */     public abstract int size();
/*      */     
/*      */     abstract Iterator<Map.Entry<K, V>> entryIterator();
/*      */     
/*      */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 3528 */       return Spliterators.spliterator(
/* 3529 */           entryIterator(), size(), 65);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3534 */       return new Maps.EntrySet<K, V>()
/*      */         {
/*      */           Map<K, V> map() {
/* 3537 */             return Maps.IteratorBasedAbstractMap.this;
/*      */           }
/*      */ 
/*      */           
/*      */           public Iterator<Map.Entry<K, V>> iterator() {
/* 3542 */             return Maps.IteratorBasedAbstractMap.this.entryIterator();
/*      */           }
/*      */ 
/*      */           
/*      */           public Spliterator<Map.Entry<K, V>> spliterator() {
/* 3547 */             return Maps.IteratorBasedAbstractMap.this.entrySpliterator();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 3552 */             Maps.IteratorBasedAbstractMap.this.forEachEntry(action);
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     void forEachEntry(Consumer<? super Map.Entry<K, V>> action) {
/* 3558 */       entryIterator().forEachRemaining(action);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3563 */       Iterators.clear(entryIterator());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V> V safeGet(Map<?, V> map, Object key) {
/* 3572 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3574 */       return map.get(key);
/* 3575 */     } catch (ClassCastException|NullPointerException e) {
/* 3576 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean safeContainsKey(Map<?, ?> map, Object key) {
/* 3585 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3587 */       return map.containsKey(key);
/* 3588 */     } catch (ClassCastException|NullPointerException e) {
/* 3589 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V> V safeRemove(Map<?, V> map, Object key) {
/* 3598 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3600 */       return map.remove(key);
/* 3601 */     } catch (ClassCastException|NullPointerException e) {
/* 3602 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean containsKeyImpl(Map<?, ?> map, Object key) {
/* 3608 */     return Iterators.contains(keyIterator(map.entrySet().iterator()), key);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean containsValueImpl(Map<?, ?> map, Object value) {
/* 3613 */     return Iterators.contains(valueIterator(map.entrySet().iterator()), value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/* 3629 */     if (!(o instanceof Map.Entry)) {
/* 3630 */       return false;
/*      */     }
/* 3632 */     return c.contains(unmodifiableEntry((Map.Entry<?, ?>)o));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/* 3647 */     if (!(o instanceof Map.Entry)) {
/* 3648 */       return false;
/*      */     }
/* 3650 */     return c.remove(unmodifiableEntry((Map.Entry<?, ?>)o));
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Map<?, ?> map, Object object) {
/* 3655 */     if (map == object)
/* 3656 */       return true; 
/* 3657 */     if (object instanceof Map) {
/* 3658 */       Map<?, ?> o = (Map<?, ?>)object;
/* 3659 */       return map.entrySet().equals(o.entrySet());
/*      */     } 
/* 3661 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   static String toStringImpl(Map<?, ?> map) {
/* 3666 */     StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
/* 3667 */     boolean first = true;
/* 3668 */     for (Map.Entry<?, ?> entry : map.entrySet()) {
/* 3669 */       if (!first) {
/* 3670 */         sb.append(", ");
/*      */       }
/* 3672 */       first = false;
/* 3673 */       sb.append(entry.getKey()).append('=').append(entry.getValue());
/*      */     } 
/* 3675 */     return sb.append('}').toString();
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void putAllImpl(Map<K, V> self, Map<? extends K, ? extends V> map) {
/* 3680 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
/* 3681 */       self.put(entry.getKey(), entry.getValue()); 
/*      */   }
/*      */   
/*      */   static class KeySet<K, V> extends Sets.ImprovedAbstractSet<K> {
/*      */     @Weak
/*      */     final Map<K, V> map;
/*      */     
/*      */     KeySet(Map<K, V> map) {
/* 3689 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */     
/*      */     Map<K, V> map() {
/* 3693 */       return this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 3698 */       return Maps.keyIterator(map().entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> action) {
/* 3703 */       Preconditions.checkNotNull(action);
/*      */       
/* 3705 */       this.map.forEach((k, v) -> action.accept(k));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3710 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3715 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3720 */       return map().containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3725 */       if (contains(o)) {
/* 3726 */         map().remove(o);
/* 3727 */         return true;
/*      */       } 
/* 3729 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3734 */       map().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static <K> K keyOrNull(Map.Entry<K, ?> entry) {
/* 3739 */     return (entry == null) ? null : entry.getKey();
/*      */   }
/*      */   
/*      */   static <V> V valueOrNull(Map.Entry<?, V> entry) {
/* 3743 */     return (entry == null) ? null : entry.getValue();
/*      */   }
/*      */   
/*      */   static class SortedKeySet<K, V> extends KeySet<K, V> implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, V> map) {
/* 3748 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedMap<K, V> map() {
/* 3753 */       return (SortedMap<K, V>)super.map();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3758 */       return map().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 3763 */       return new SortedKeySet(map().subMap(fromElement, toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 3768 */       return new SortedKeySet(map().headMap(toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 3773 */       return new SortedKeySet(map().tailMap(fromElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public K first() {
/* 3778 */       return map().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K last() {
/* 3783 */       return map().lastKey();
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class NavigableKeySet<K, V> extends SortedKeySet<K, V> implements NavigableSet<K> {
/*      */     NavigableKeySet(NavigableMap<K, V> map) {
/* 3790 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, V> map() {
/* 3795 */       return (NavigableMap<K, V>)this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public K lower(K e) {
/* 3800 */       return map().lowerKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floor(K e) {
/* 3805 */       return map().floorKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceiling(K e) {
/* 3810 */       return map().ceilingKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higher(K e) {
/* 3815 */       return map().higherKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollFirst() {
/* 3820 */       return Maps.keyOrNull(map().pollFirstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollLast() {
/* 3825 */       return Maps.keyOrNull(map().pollLastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingSet() {
/* 3830 */       return map().descendingKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> descendingIterator() {
/* 3835 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
/* 3841 */       return map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 3846 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement, boolean inclusive) {
/* 3851 */       return map().headMap(toElement, inclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 3856 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
/* 3861 */       return map().tailMap(fromElement, inclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 3866 */       return tailSet(fromElement, true);
/*      */     } }
/*      */   
/*      */   static class Values<K, V> extends AbstractCollection<V> {
/*      */     @Weak
/*      */     final Map<K, V> map;
/*      */     
/*      */     Values(Map<K, V> map) {
/* 3874 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */     
/*      */     final Map<K, V> map() {
/* 3878 */       return this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 3883 */       return Maps.valueIterator(map().entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super V> action) {
/* 3888 */       Preconditions.checkNotNull(action);
/*      */       
/* 3890 */       this.map.forEach((k, v) -> action.accept(v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*      */       try {
/* 3896 */         return super.remove(o);
/* 3897 */       } catch (UnsupportedOperationException e) {
/* 3898 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 3899 */           if (Objects.equal(o, entry.getValue())) {
/* 3900 */             map().remove(entry.getKey());
/* 3901 */             return true;
/*      */           } 
/*      */         } 
/* 3904 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 3911 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 3912 */       } catch (UnsupportedOperationException e) {
/* 3913 */         Set<K> toRemove = Sets.newHashSet();
/* 3914 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 3915 */           if (c.contains(entry.getValue())) {
/* 3916 */             toRemove.add(entry.getKey());
/*      */           }
/*      */         } 
/* 3919 */         return map().keySet().removeAll(toRemove);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 3926 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 3927 */       } catch (UnsupportedOperationException e) {
/* 3928 */         Set<K> toRetain = Sets.newHashSet();
/* 3929 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 3930 */           if (c.contains(entry.getValue())) {
/* 3931 */             toRetain.add(entry.getKey());
/*      */           }
/*      */         } 
/* 3934 */         return map().keySet().retainAll(toRetain);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3940 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3945 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3950 */       return map().containsValue(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3955 */       map().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<K, V>
/*      */     extends Sets.ImprovedAbstractSet<Map.Entry<K, V>> {
/*      */     abstract Map<K, V> map();
/*      */     
/*      */     public int size() {
/* 3964 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3969 */       map().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3974 */       if (o instanceof Map.Entry) {
/* 3975 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 3976 */         Object key = entry.getKey();
/* 3977 */         V value = Maps.safeGet(map(), key);
/* 3978 */         return (Objects.equal(value, entry.getValue()) && (value != null || map().containsKey(key)));
/*      */       } 
/* 3980 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3985 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3990 */       if (contains(o)) {
/* 3991 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 3992 */         return map().keySet().remove(entry.getKey());
/*      */       } 
/* 3994 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 4000 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 4001 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 4003 */         return Sets.removeAllImpl(this, c.iterator());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 4010 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 4011 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 4013 */         Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
/* 4014 */         for (Object o : c) {
/* 4015 */           if (contains(o)) {
/* 4016 */             Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 4017 */             keys.add(entry.getKey());
/*      */           } 
/*      */         } 
/* 4020 */         return map().keySet().retainAll(keys);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static abstract class DescendingMap<K, V>
/*      */     extends ForwardingMap<K, V> implements NavigableMap<K, V> {
/*      */     private transient Comparator<? super K> comparator;
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */     private transient NavigableSet<K> navigableKeySet;
/*      */     
/*      */     protected final Map<K, V> delegate() {
/* 4033 */       return forward();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 4041 */       Comparator<? super K> result = this.comparator;
/* 4042 */       if (result == null) {
/* 4043 */         Comparator<? super K> forwardCmp = forward().comparator();
/* 4044 */         if (forwardCmp == null) {
/* 4045 */           forwardCmp = Ordering.natural();
/*      */         }
/* 4047 */         result = this.comparator = reverse(forwardCmp);
/*      */       } 
/* 4049 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     private static <T> Ordering<T> reverse(Comparator<T> forward) {
/* 4054 */       return Ordering.<T>from(forward).reverse();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 4059 */       return forward().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 4064 */       return forward().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 4069 */       return forward().higherEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 4074 */       return forward().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 4079 */       return forward().ceilingEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 4084 */       return forward().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 4089 */       return forward().floorEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 4094 */       return forward().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 4099 */       return forward().lowerEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 4104 */       return forward().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> firstEntry() {
/* 4109 */       return forward().lastEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lastEntry() {
/* 4114 */       return forward().firstEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 4119 */       return forward().pollLastEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 4124 */       return forward().pollFirstEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 4129 */       return forward();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 4136 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 4137 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<Map.Entry<K, V>> createEntrySet() {
/*      */       class EntrySetImpl
/*      */         extends Maps.EntrySet<K, V>
/*      */       {
/*      */         Map<K, V> map() {
/* 4147 */           return Maps.DescendingMap.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Map.Entry<K, V>> iterator() {
/* 4152 */           return Maps.DescendingMap.this.entryIterator();
/*      */         }
/*      */       };
/* 4155 */       return new EntrySetImpl();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 4160 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 4167 */       NavigableSet<K> result = this.navigableKeySet;
/* 4168 */       return (result == null) ? (this.navigableKeySet = new Maps.NavigableKeySet<>(this)) : result;
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 4173 */       return forward().navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 4179 */       return forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 4184 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 4189 */       return forward().tailMap(toKey, inclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 4194 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 4199 */       return forward().headMap(fromKey, inclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 4204 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 4209 */       return new Maps.Values<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4214 */       return standardToString();
/*      */     }
/*      */     abstract NavigableMap<K, V> forward();
/*      */     abstract Iterator<Map.Entry<K, V>> entryIterator(); }
/*      */   
/*      */   static <E> ImmutableMap<E, Integer> indexMap(Collection<E> list) {
/* 4220 */     ImmutableMap.Builder<E, Integer> builder = new ImmutableMap.Builder<>(list.size());
/* 4221 */     int i = 0;
/* 4222 */     for (E e : list) {
/* 4223 */       builder.put(e, Integer.valueOf(i++));
/*      */     }
/* 4225 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <K extends Comparable<? super K>, V> NavigableMap<K, V> subMap(NavigableMap<K, V> map, Range<K> range) {
/* 4248 */     if (map.comparator() != null && map
/* 4249 */       .comparator() != Ordering.natural() && range
/* 4250 */       .hasLowerBound() && range
/* 4251 */       .hasUpperBound()) {
/* 4252 */       Preconditions.checkArgument(
/* 4253 */           (map.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0), "map is using a custom comparator which is inconsistent with the natural ordering.");
/*      */     }
/*      */     
/* 4256 */     if (range.hasLowerBound() && range.hasUpperBound())
/* 4257 */       return map.subMap(range
/* 4258 */           .lowerEndpoint(), 
/* 4259 */           (range.lowerBoundType() == BoundType.CLOSED), range
/* 4260 */           .upperEndpoint(), 
/* 4261 */           (range.upperBoundType() == BoundType.CLOSED)); 
/* 4262 */     if (range.hasLowerBound())
/* 4263 */       return map.tailMap(range.lowerEndpoint(), (range.lowerBoundType() == BoundType.CLOSED)); 
/* 4264 */     if (range.hasUpperBound()) {
/* 4265 */       return map.headMap(range.upperEndpoint(), (range.upperBoundType() == BoundType.CLOSED));
/*      */     }
/* 4267 */     return (NavigableMap<K, V>)Preconditions.checkNotNull(map);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface EntryTransformer<K, V1, V2> {
/*      */     V2 transformEntry(K param1K, V1 param1V1);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Maps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */