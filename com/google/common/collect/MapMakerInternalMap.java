/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtIncompatible
/*      */ class MapMakerInternalMap<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>, S extends MapMakerInternalMap.Segment<K, V, E, S>>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>, Serializable
/*      */ {
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int CONTAINS_VALUE_RETRIES = 3;
/*      */   static final int DRAIN_THRESHOLD = 63;
/*      */   static final int DRAIN_MAX = 16;
/*      */   static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
/*      */   final transient int segmentMask;
/*      */   final transient int segmentShift;
/*      */   final transient Segment<K, V, E, S>[] segments;
/*      */   final int concurrencyLevel;
/*      */   final Equivalence<Object> keyEquivalence;
/*      */   final transient InternalEntryHelper<K, V, E, S> entryHelper;
/*      */   
/*      */   private MapMakerInternalMap(MapMaker builder, InternalEntryHelper<K, V, E, S> entryHelper) {
/*  162 */     this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
/*      */     
/*  164 */     this.keyEquivalence = builder.getKeyEquivalence();
/*  165 */     this.entryHelper = entryHelper;
/*      */     
/*  167 */     int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
/*      */ 
/*      */ 
/*      */     
/*  171 */     int segmentShift = 0;
/*  172 */     int segmentCount = 1;
/*  173 */     while (segmentCount < this.concurrencyLevel) {
/*  174 */       segmentShift++;
/*  175 */       segmentCount <<= 1;
/*      */     } 
/*  177 */     this.segmentShift = 32 - segmentShift;
/*  178 */     this.segmentMask = segmentCount - 1;
/*      */     
/*  180 */     this.segments = newSegmentArray(segmentCount);
/*      */     
/*  182 */     int segmentCapacity = initialCapacity / segmentCount;
/*  183 */     if (segmentCapacity * segmentCount < initialCapacity) {
/*  184 */       segmentCapacity++;
/*      */     }
/*      */     
/*  187 */     int segmentSize = 1;
/*  188 */     while (segmentSize < segmentCapacity) {
/*  189 */       segmentSize <<= 1;
/*      */     }
/*      */     
/*  192 */     for (int i = 0; i < this.segments.length; i++) {
/*  193 */       this.segments[i] = createSegment(segmentSize, -1);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker builder) {
/*  200 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  201 */       .getValueStrength() == Strength.STRONG) {
/*  202 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)StrongKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  204 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  205 */       .getValueStrength() == Strength.WEAK) {
/*  206 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)StrongKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  208 */     if (builder.getKeyStrength() == Strength.WEAK && builder
/*  209 */       .getValueStrength() == Strength.STRONG) {
/*  210 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)WeakKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  212 */     if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.WEAK) {
/*  213 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)WeakKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  215 */     throw new AssertionError();
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
/*      */   static <K> MapMakerInternalMap<K, MapMaker.Dummy, ? extends InternalEntry<K, MapMaker.Dummy, ?>, ?> createWithDummyValues(MapMaker builder) {
/*  231 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  232 */       .getValueStrength() == Strength.STRONG) {
/*  233 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)StrongKeyDummyValueEntry.Helper.instance());
/*      */     }
/*  235 */     if (builder.getKeyStrength() == Strength.WEAK && builder
/*  236 */       .getValueStrength() == Strength.STRONG) {
/*  237 */       return new MapMakerInternalMap<>(builder, (InternalEntryHelper)WeakKeyDummyValueEntry.Helper.instance());
/*      */     }
/*  239 */     if (builder.getValueStrength() == Strength.WEAK) {
/*  240 */       throw new IllegalArgumentException("Map cannot have both weak and dummy values");
/*      */     }
/*  242 */     throw new AssertionError();
/*      */   }
/*      */   
/*      */   enum Strength {
/*  246 */     STRONG
/*      */     {
/*      */       Equivalence<Object> defaultEquivalence() {
/*  249 */         return Equivalence.equals();
/*      */       }
/*      */     },
/*      */     
/*  253 */     WEAK
/*      */     {
/*      */       Equivalence<Object> defaultEquivalence() {
/*  256 */         return Equivalence.identity();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract Equivalence<Object> defaultEquivalence();
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
/*      */   static abstract class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>>
/*      */     implements InternalEntry<K, V, E>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int hash;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final E next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractStrongKeyEntry(K key, int hash, E next) {
/*  346 */       this.key = key;
/*  347 */       this.hash = hash;
/*  348 */       this.next = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  353 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  358 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getNext() {
/*  363 */       return this.next;
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
/*      */   static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() {
/*  386 */     return (WeakValueReference)UNSET_WEAK_VALUE_REFERENCE;
/*      */   }
/*      */   
/*      */   static final class StrongKeyStrongValueEntry<K, V>
/*      */     extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>>
/*      */     implements StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>>
/*      */   {
/*  393 */     private volatile V value = null;
/*      */     
/*      */     StrongKeyStrongValueEntry(K key, int hash, StrongKeyStrongValueEntry<K, V> next) {
/*  396 */       super(key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  401 */       return this.value;
/*      */     }
/*      */     
/*      */     void setValue(V value) {
/*  405 */       this.value = value;
/*      */     }
/*      */     
/*      */     StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueEntry<K, V> newNext) {
/*  409 */       StrongKeyStrongValueEntry<K, V> newEntry = new StrongKeyStrongValueEntry(this.key, this.hash, newNext);
/*      */       
/*  411 */       newEntry.value = this.value;
/*  412 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>>
/*      */     {
/*  419 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  423 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  428 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  433 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  443 */         return new MapMakerInternalMap.StrongKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext) {
/*  451 */         return entry.copy(newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value) {
/*  459 */         entry.setValue(value);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next)
/*      */       {
/*  468 */         return new MapMakerInternalMap.StrongKeyStrongValueEntry<>(key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next) { return new MapMakerInternalMap.StrongKeyStrongValueEntry<>(key, hash, next); } static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.StrongKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext) {
/*      */       return entry.copy(newNext);
/*      */     } public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value);
/*      */     } }
/*      */    static final class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>> {
/*  478 */     private volatile MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */     
/*      */     StrongKeyWeakValueEntry(K key, int hash, StrongKeyWeakValueEntry<K, V> next) {
/*  481 */       super(key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  486 */       return this.valueReference.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clearValue() {
/*  491 */       this.valueReference.clear();
/*      */     }
/*      */     
/*      */     void setValue(V value, ReferenceQueue<V> queueForValues) {
/*  495 */       MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = this.valueReference;
/*  496 */       this.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(queueForValues, value, this);
/*  497 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueEntry<K, V> copy(ReferenceQueue<V> queueForValues, StrongKeyWeakValueEntry<K, V> newNext) {
/*  502 */       StrongKeyWeakValueEntry<K, V> newEntry = new StrongKeyWeakValueEntry(this.key, this.hash, newNext);
/*  503 */       newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
/*  504 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
/*  509 */       return this.valueReference;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>>
/*      */     {
/*  516 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  520 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  525 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  530 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  539 */         return new MapMakerInternalMap.StrongKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext) {
/*  547 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  548 */           return null;
/*      */         }
/*  550 */         return entry.copy(segment.queueForValues, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value) {
/*  556 */         entry.setValue(value, segment.queueForValues);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next)
/*      */       {
/*  565 */         return new MapMakerInternalMap.StrongKeyWeakValueEntry<>(key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next) { return new MapMakerInternalMap.StrongKeyWeakValueEntry<>(key, hash, next); } public MapMakerInternalMap.Strength valueStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.StrongKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext) {
/*      */       if (MapMakerInternalMap.Segment.isCollected(entry))
/*      */         return null; 
/*      */       return entry.copy(segment.queueForValues, newNext);
/*      */     } public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value, segment.queueForValues);
/*      */     } }
/*      */   static final class StrongKeyDummyValueEntry<K> extends AbstractStrongKeyEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> implements StrongValueEntry<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>> { StrongKeyDummyValueEntry(K key, int hash, StrongKeyDummyValueEntry<K> next) {
/*  575 */       super(key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.Dummy getValue() {
/*  580 */       return MapMaker.Dummy.VALUE;
/*      */     }
/*      */     
/*      */     void setValue(MapMaker.Dummy value) {}
/*      */     
/*      */     StrongKeyDummyValueEntry<K> copy(StrongKeyDummyValueEntry<K> newNext) {
/*  586 */       return new StrongKeyDummyValueEntry(this.key, this.hash, newNext);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final class Helper<K>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>>
/*      */     {
/*  596 */       private static final Helper<?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K> Helper<K> instance() {
/*  600 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  605 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  610 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/*  619 */         return new MapMakerInternalMap.StrongKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueEntry<K> copy(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMakerInternalMap.StrongKeyDummyValueEntry<K> newNext) {
/*  627 */         return entry.copy(newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, K key, int hash, MapMakerInternalMap.StrongKeyDummyValueEntry<K> next)
/*      */       {
/*  640 */         return new MapMakerInternalMap.StrongKeyDummyValueEntry<>(key, hash, next); } } } static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> { private static final Helper<?> INSTANCE = new Helper(); static <K> Helper<K> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.StrongKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, K key, int hash, MapMakerInternalMap.StrongKeyDummyValueEntry<K> next) { return new MapMakerInternalMap.StrongKeyDummyValueEntry<>(key, hash, next); }
/*      */      public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.StrongKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, MapMakerInternalMap.StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.StrongKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */     public MapMakerInternalMap.StrongKeyDummyValueEntry<K> copy(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMakerInternalMap.StrongKeyDummyValueEntry<K> newNext) {
/*      */       return entry.copy(newNext);
/*      */     }
/*      */     public void setValue(MapMakerInternalMap.StrongKeyDummyValueSegment<K> segment, MapMakerInternalMap.StrongKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {} }
/*      */   static abstract class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E> { final int hash; final E next;
/*      */     AbstractWeakKeyEntry(ReferenceQueue<K> queue, K key, int hash, E next) {
/*  652 */       super(key, queue);
/*  653 */       this.hash = hash;
/*  654 */       this.next = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  659 */       return get();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  664 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getNext() {
/*  669 */       return this.next;
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class WeakKeyDummyValueEntry<K>
/*      */     extends AbstractWeakKeyEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>>
/*      */     implements StrongValueEntry<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>>
/*      */   {
/*      */     WeakKeyDummyValueEntry(ReferenceQueue<K> queue, K key, int hash, WeakKeyDummyValueEntry<K> next) {
/*  679 */       super(queue, key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMaker.Dummy getValue() {
/*  684 */       return MapMaker.Dummy.VALUE;
/*      */     }
/*      */ 
/*      */     
/*      */     void setValue(MapMaker.Dummy value) {}
/*      */     
/*      */     WeakKeyDummyValueEntry<K> copy(ReferenceQueue<K> queueForKeys, WeakKeyDummyValueEntry<K> newNext) {
/*  691 */       return new WeakKeyDummyValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static final class Helper<K>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>>
/*      */     {
/*  701 */       private static final Helper<?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K> Helper<K> instance() {
/*  705 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  710 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  715 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/*  723 */         return new MapMakerInternalMap.WeakKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyDummyValueEntry<K> copy(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMakerInternalMap.WeakKeyDummyValueEntry<K> newNext) {
/*  731 */         if (entry.getKey() == null)
/*      */         {
/*  733 */           return null;
/*      */         }
/*  735 */         return entry.copy(segment.queueForKeys, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, K key, int hash, MapMakerInternalMap.WeakKeyDummyValueEntry<K> next)
/*      */       {
/*  748 */         return new MapMakerInternalMap.WeakKeyDummyValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K> implements InternalEntryHelper<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> { private static final Helper<?> INSTANCE = new Helper(); static <K> Helper<K> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyDummyValueEntry<K> newEntry(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, K key, int hash, MapMakerInternalMap.WeakKeyDummyValueEntry<K> next) { return new MapMakerInternalMap.WeakKeyDummyValueEntry<>(segment.queueForKeys, key, hash, next); }
/*      */      public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.WeakKeyDummyValueSegment<K> newSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, MapMakerInternalMap.WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.WeakKeyDummyValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.WeakKeyDummyValueEntry<K> copy(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMakerInternalMap.WeakKeyDummyValueEntry<K> newNext) {
/*      */       if (entry.getKey() == null)
/*      */         return null; 
/*      */       return entry.copy(segment.queueForKeys, newNext);
/*  757 */     } public void setValue(MapMakerInternalMap.WeakKeyDummyValueSegment<K> segment, MapMakerInternalMap.WeakKeyDummyValueEntry<K> entry, MapMaker.Dummy value) {} } static final class WeakKeyStrongValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>> implements StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>> { private volatile V value = null;
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash, WeakKeyStrongValueEntry<K, V> next) {
/*  761 */       super(queue, key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  766 */       return this.value;
/*      */     }
/*      */     
/*      */     void setValue(V value) {
/*  770 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, WeakKeyStrongValueEntry<K, V> newNext) {
/*  776 */       WeakKeyStrongValueEntry<K, V> newEntry = new WeakKeyStrongValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*  777 */       newEntry.setValue(this.value);
/*  778 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>>
/*      */     {
/*  785 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  789 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  794 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  799 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  808 */         return new MapMakerInternalMap.WeakKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext) {
/*  816 */         if (entry.getKey() == null)
/*      */         {
/*  818 */           return null;
/*      */         }
/*  820 */         return entry.copy(segment.queueForKeys, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value) {
/*  826 */         entry.setValue(value);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next)
/*      */       {
/*  835 */         return new MapMakerInternalMap.WeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next) { return new MapMakerInternalMap.WeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash, next); } public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.WeakKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext) {
/*      */       if (entry.getKey() == null)
/*      */         return null; 
/*      */       return entry.copy(segment.queueForKeys, newNext);
/*      */     } public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value);
/*  845 */     } } static final class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>> { private volatile MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash, WeakKeyWeakValueEntry<K, V> next) {
/*  849 */       super(queue, key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  854 */       return this.valueReference.get();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, ReferenceQueue<V> queueForValues, WeakKeyWeakValueEntry<K, V> newNext) {
/*  862 */       WeakKeyWeakValueEntry<K, V> newEntry = new WeakKeyWeakValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*  863 */       newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
/*  864 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clearValue() {
/*  869 */       this.valueReference.clear();
/*      */     }
/*      */     
/*      */     void setValue(V value, ReferenceQueue<V> queueForValues) {
/*  873 */       MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = this.valueReference;
/*  874 */       this.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(queueForValues, value, this);
/*  875 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
/*  880 */       return this.valueReference;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>>
/*      */     {
/*  887 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  891 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  896 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  901 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  909 */         return new MapMakerInternalMap.WeakKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext) {
/*  917 */         if (entry.getKey() == null)
/*      */         {
/*  919 */           return null;
/*      */         }
/*  921 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  922 */           return null;
/*      */         }
/*  924 */         return entry.copy(segment.queueForKeys, segment.queueForValues, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value) {
/*  930 */         entry.setValue(value, segment.queueForValues);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next)
/*      */       {
/*  939 */         return new MapMakerInternalMap.WeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next) { return new MapMakerInternalMap.WeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash, next); }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.WEAK;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.WeakKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext) {
/*      */       if (entry.getKey() == null) {
/*      */         return null;
/*      */       }
/*      */       if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*      */         return null;
/*      */       }
/*      */       return entry.copy(segment.queueForKeys, segment.queueForValues, newNext);
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value, segment.queueForValues);
/*      */     } }
/*      */ 
/*      */   
/*      */   static final class DummyInternalEntry
/*      */     implements InternalEntry<Object, Object, DummyInternalEntry>
/*      */   {
/*      */     private DummyInternalEntry() {
/*  973 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public DummyInternalEntry getNext() {
/*  978 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  983 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getKey() {
/*  988 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getValue() {
/*  993 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1001 */   static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference<Object, Object, DummyInternalEntry>()
/*      */     {
/*      */       public MapMakerInternalMap.DummyInternalEntry getEntry()
/*      */       {
/* 1005 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public void clear() {}
/*      */ 
/*      */       
/*      */       public Object get() {
/* 1013 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakValueReference<Object, Object, MapMakerInternalMap.DummyInternalEntry> copyFor(ReferenceQueue<Object> queue, MapMakerInternalMap.DummyInternalEntry entry) {
/* 1019 */         return this;
/*      */       }
/*      */     };
/*      */   transient Set<K> keySet; transient Collection<V> values; transient Set<Map.Entry<K, V>> entrySet;
/*      */   private static final long serialVersionUID = 5L;
/*      */   
/*      */   static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E> { @Weak
/*      */     final E entry;
/*      */     
/*      */     WeakValueReferenceImpl(ReferenceQueue<V> queue, V referent, E entry) {
/* 1029 */       super(referent, queue);
/* 1030 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getEntry() {
/* 1035 */       return this.entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> queue, E entry) {
/* 1040 */       return new WeakValueReferenceImpl(queue, get(), entry);
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
/*      */   static int rehash(int h) {
/* 1056 */     h += h << 15 ^ 0xFFFFCD7D;
/* 1057 */     h ^= h >>> 10;
/* 1058 */     h += h << 3;
/* 1059 */     h ^= h >>> 6;
/* 1060 */     h += (h << 2) + (h << 14);
/* 1061 */     return h ^ h >>> 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   E copyEntry(E original, E newNext) {
/* 1070 */     int hash = original.getHash();
/* 1071 */     return segmentFor(hash).copyEntry(original, newNext);
/*      */   }
/*      */   
/*      */   int hash(Object key) {
/* 1075 */     int h = this.keyEquivalence.hash(key);
/* 1076 */     return rehash(h);
/*      */   }
/*      */   
/*      */   void reclaimValue(WeakValueReference<K, V, E> valueReference) {
/* 1080 */     E entry = valueReference.getEntry();
/* 1081 */     int hash = entry.getHash();
/* 1082 */     segmentFor(hash).reclaimValue((K)entry.getKey(), hash, valueReference);
/*      */   }
/*      */   
/*      */   void reclaimKey(E entry) {
/* 1086 */     int hash = entry.getHash();
/* 1087 */     segmentFor(hash).reclaimKey(entry, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   boolean isLiveForTesting(InternalEntry<K, V, ?> entry) {
/* 1096 */     return (segmentFor(entry.getHash()).getLiveValueForTesting(entry) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Segment<K, V, E, S> segmentFor(int hash) {
/* 1107 */     return this.segments[hash >>> this.segmentShift & this.segmentMask];
/*      */   }
/*      */   
/*      */   Segment<K, V, E, S> createSegment(int initialCapacity, int maxSegmentSize) {
/* 1111 */     return (Segment<K, V, E, S>)this.entryHelper.newSegment(this, initialCapacity, maxSegmentSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   V getLiveValue(E entry) {
/* 1119 */     if (entry.getKey() == null) {
/* 1120 */       return null;
/*      */     }
/* 1122 */     V value = (V)entry.getValue();
/* 1123 */     if (value == null) {
/* 1124 */       return null;
/*      */     }
/* 1126 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   final Segment<K, V, E, S>[] newSegmentArray(int ssize) {
/* 1131 */     return (Segment<K, V, E, S>[])new Segment[ssize];
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
/*      */   static abstract class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>
/*      */     extends ReentrantLock
/*      */   {
/*      */     @Weak
/*      */     final MapMakerInternalMap<K, V, E, S> map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile int count;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int modCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int threshold;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile AtomicReferenceArray<E> table;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int maxSegmentSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1205 */     final AtomicInteger readCount = new AtomicInteger();
/*      */     
/*      */     Segment(MapMakerInternalMap<K, V, E, S> map, int initialCapacity, int maxSegmentSize) {
/* 1208 */       this.map = map;
/* 1209 */       this.maxSegmentSize = maxSegmentSize;
/* 1210 */       initTable(newEntryArray(initialCapacity));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract S self();
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void maybeDrainReferenceQueues() {}
/*      */ 
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {}
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue(E entry, V value) {
/* 1230 */       this.map.entryHelper.setValue(self(), entry, value);
/*      */     }
/*      */ 
/*      */     
/*      */     E copyEntry(E original, E newNext) {
/* 1235 */       return this.map.entryHelper.copy(self(), original, newNext);
/*      */     }
/*      */     
/*      */     AtomicReferenceArray<E> newEntryArray(int size) {
/* 1239 */       return new AtomicReferenceArray<>(size);
/*      */     }
/*      */     
/*      */     void initTable(AtomicReferenceArray<E> newTable) {
/* 1243 */       this.threshold = newTable.length() * 3 / 4;
/* 1244 */       if (this.threshold == this.maxSegmentSize)
/*      */       {
/* 1246 */         this.threshold++;
/*      */       }
/* 1248 */       this.table = newTable;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract E castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> param1InternalEntry);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 1264 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 1269 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1274 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value) {
/* 1283 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 1293 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setTableEntryForTesting(int i, MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1300 */       this.table.set(i, castForTesting(entry));
/*      */     }
/*      */ 
/*      */     
/*      */     E copyForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, MapMakerInternalMap.InternalEntry<K, V, ?> newNext) {
/* 1305 */       return this.map.entryHelper.copy(self(), castForTesting(entry), castForTesting(newNext));
/*      */     }
/*      */ 
/*      */     
/*      */     void setValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value) {
/* 1310 */       this.map.entryHelper.setValue(self(), castForTesting(entry), value);
/*      */     }
/*      */ 
/*      */     
/*      */     E newEntryForTesting(K key, int hash, MapMakerInternalMap.InternalEntry<K, V, ?> next) {
/* 1315 */       return this.map.entryHelper.newEntry(self(), key, hash, castForTesting(next));
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean removeTableEntryForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1321 */       return removeEntryForTesting(castForTesting(entry));
/*      */     }
/*      */ 
/*      */     
/*      */     E removeFromChainForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> first, MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1326 */       return removeFromChain(castForTesting(first), castForTesting(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V getLiveValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1334 */       return getLiveValue(castForTesting(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryDrainReferenceQueues() {
/* 1341 */       if (tryLock()) {
/*      */         try {
/* 1343 */           maybeDrainReferenceQueues();
/*      */         } finally {
/* 1345 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainKeyReferenceQueue(ReferenceQueue<K> keyReferenceQueue) {
/* 1353 */       int i = 0; Reference<? extends K> ref;
/* 1354 */       while ((ref = keyReferenceQueue.poll()) != null) {
/*      */         
/* 1356 */         MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)ref;
/* 1357 */         this.map.reclaimKey((E)internalEntry);
/* 1358 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainValueReferenceQueue(ReferenceQueue<V> valueReferenceQueue) {
/* 1367 */       int i = 0; Reference<? extends V> ref;
/* 1368 */       while ((ref = valueReferenceQueue.poll()) != null) {
/*      */         
/* 1370 */         MapMakerInternalMap.WeakValueReference<K, V, E> valueReference = (MapMakerInternalMap.WeakValueReference)ref;
/* 1371 */         this.map.reclaimValue(valueReference);
/* 1372 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     <T> void clearReferenceQueue(ReferenceQueue<T> referenceQueue) {
/* 1379 */       while (referenceQueue.poll() != null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     E getFirst(int hash) {
/* 1385 */       AtomicReferenceArray<E> table = this.table;
/* 1386 */       return table.get(hash & table.length() - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     E getEntry(Object key, int hash) {
/* 1392 */       if (this.count != 0) {
/* 1393 */         for (E e = getFirst(hash); e != null; e = (E)e.getNext()) {
/* 1394 */           if (e.getHash() == hash) {
/*      */ 
/*      */ 
/*      */             
/* 1398 */             K entryKey = (K)e.getKey();
/* 1399 */             if (entryKey == null) {
/* 1400 */               tryDrainReferenceQueues();
/*      */ 
/*      */             
/*      */             }
/* 1404 */             else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 1405 */               return e;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/* 1410 */       return null;
/*      */     }
/*      */     
/*      */     E getLiveEntry(Object key, int hash) {
/* 1414 */       return getEntry(key, hash);
/*      */     }
/*      */     
/*      */     V get(Object key, int hash) {
/*      */       try {
/* 1419 */         E e = getLiveEntry(key, hash);
/* 1420 */         if (e == null) {
/* 1421 */           return null;
/*      */         }
/*      */         
/* 1424 */         V value = (V)e.getValue();
/* 1425 */         if (value == null) {
/* 1426 */           tryDrainReferenceQueues();
/*      */         }
/* 1428 */         return value;
/*      */       } finally {
/* 1430 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*      */       try {
/* 1436 */         if (this.count != 0) {
/* 1437 */           E e = getLiveEntry(key, hash);
/* 1438 */           return (e != null && e.getValue() != null);
/*      */         } 
/*      */         
/* 1441 */         return false;
/*      */       } finally {
/* 1443 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @VisibleForTesting
/*      */     boolean containsValue(Object value) {
/*      */       try {
/* 1454 */         if (this.count != 0) {
/* 1455 */           AtomicReferenceArray<E> table = this.table;
/* 1456 */           int length = table.length();
/* 1457 */           for (int i = 0; i < length; i++) {
/* 1458 */             for (MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)table.get(i); internalEntry != null; internalEntry = (MapMakerInternalMap.InternalEntry)internalEntry.getNext()) {
/* 1459 */               V entryValue = getLiveValue((E)internalEntry);
/* 1460 */               if (entryValue != null)
/*      */               {
/*      */                 
/* 1463 */                 if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1464 */                   return true;
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/* 1470 */         return false;
/*      */       } finally {
/* 1472 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent) {
/* 1477 */       lock();
/*      */       try {
/* 1479 */         preWriteCleanup();
/*      */         
/* 1481 */         int newCount = this.count + 1;
/* 1482 */         if (newCount > this.threshold) {
/* 1483 */           expand();
/* 1484 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 1487 */         AtomicReferenceArray<E> table = this.table;
/* 1488 */         int index = hash & table.length() - 1;
/* 1489 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */ 
/*      */         
/* 1492 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1493 */           K entryKey = (K)internalEntry2.getKey();
/* 1494 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1496 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1499 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1501 */             if (entryValue == null) {
/* 1502 */               this.modCount++;
/* 1503 */               setValue((E)internalEntry2, value);
/* 1504 */               newCount = this.count;
/* 1505 */               this.count = newCount;
/* 1506 */               return null;
/* 1507 */             }  if (onlyIfAbsent)
/*      */             {
/*      */ 
/*      */               
/* 1511 */               return entryValue;
/*      */             }
/*      */             
/* 1514 */             this.modCount++;
/* 1515 */             setValue((E)internalEntry2, value);
/* 1516 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1522 */         this.modCount++;
/* 1523 */         E newEntry = this.map.entryHelper.newEntry(self(), key, hash, (E)internalEntry1);
/* 1524 */         setValue(newEntry, value);
/* 1525 */         table.set(index, newEntry);
/* 1526 */         this.count = newCount;
/* 1527 */         return null;
/*      */       } finally {
/* 1529 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expand() {
/* 1536 */       AtomicReferenceArray<E> oldTable = this.table;
/* 1537 */       int oldCapacity = oldTable.length();
/* 1538 */       if (oldCapacity >= 1073741824) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1552 */       int newCount = this.count;
/* 1553 */       AtomicReferenceArray<E> newTable = newEntryArray(oldCapacity << 1);
/* 1554 */       this.threshold = newTable.length() * 3 / 4;
/* 1555 */       int newMask = newTable.length() - 1;
/* 1556 */       for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
/*      */ 
/*      */         
/* 1559 */         MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)oldTable.get(oldIndex);
/*      */         
/* 1561 */         if (internalEntry != null) {
/* 1562 */           E next = (E)internalEntry.getNext();
/* 1563 */           int headIndex = internalEntry.getHash() & newMask;
/*      */ 
/*      */           
/* 1566 */           if (next == null) {
/* 1567 */             newTable.set(headIndex, (E)internalEntry);
/*      */           } else {
/*      */             E e1;
/*      */ 
/*      */             
/* 1572 */             MapMakerInternalMap.InternalEntry internalEntry1 = internalEntry;
/* 1573 */             int tailIndex = headIndex;
/* 1574 */             for (E e = next; e != null; e = (E)e.getNext()) {
/* 1575 */               int newIndex = e.getHash() & newMask;
/* 1576 */               if (newIndex != tailIndex) {
/*      */                 
/* 1578 */                 tailIndex = newIndex;
/* 1579 */                 e1 = e;
/*      */               } 
/*      */             } 
/* 1582 */             newTable.set(tailIndex, e1);
/*      */ 
/*      */             
/* 1585 */             for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry; internalEntry2 != e1; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1586 */               int newIndex = internalEntry2.getHash() & newMask;
/* 1587 */               MapMakerInternalMap.InternalEntry internalEntry3 = (MapMakerInternalMap.InternalEntry)newTable.get(newIndex);
/* 1588 */               E newFirst = copyEntry((E)internalEntry2, (E)internalEntry3);
/* 1589 */               if (newFirst != null) {
/* 1590 */                 newTable.set(newIndex, newFirst);
/*      */               } else {
/* 1592 */                 newCount--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1598 */       this.table = newTable;
/* 1599 */       this.count = newCount;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/* 1603 */       lock();
/*      */       try {
/* 1605 */         preWriteCleanup();
/*      */         
/* 1607 */         AtomicReferenceArray<E> table = this.table;
/* 1608 */         int index = hash & table.length() - 1;
/* 1609 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1611 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1612 */           K entryKey = (K)internalEntry2.getKey();
/* 1613 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1615 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1618 */             V entryValue = (V)internalEntry2.getValue();
/* 1619 */             if (entryValue == null) {
/* 1620 */               if (isCollected(internalEntry2)) {
/* 1621 */                 int newCount = this.count - 1;
/* 1622 */                 this.modCount++;
/* 1623 */                 E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1624 */                 newCount = this.count - 1;
/* 1625 */                 table.set(index, newFirst);
/* 1626 */                 this.count = newCount;
/*      */               } 
/* 1628 */               return false;
/*      */             } 
/*      */             
/* 1631 */             if (this.map.valueEquivalence().equivalent(oldValue, entryValue)) {
/* 1632 */               this.modCount++;
/* 1633 */               setValue((E)internalEntry2, newValue);
/* 1634 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 1638 */             return false;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1643 */         return false;
/*      */       } finally {
/* 1645 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     V replace(K key, int hash, V newValue) {
/* 1650 */       lock();
/*      */       try {
/* 1652 */         preWriteCleanup();
/*      */         
/* 1654 */         AtomicReferenceArray<E> table = this.table;
/* 1655 */         int index = hash & table.length() - 1;
/* 1656 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         MapMakerInternalMap.InternalEntry internalEntry2;
/* 1658 */         for (internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1659 */           K entryKey = (K)internalEntry2.getKey();
/* 1660 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1662 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1665 */             V entryValue = (V)internalEntry2.getValue();
/* 1666 */             if (entryValue == null) {
/* 1667 */               if (isCollected(internalEntry2)) {
/* 1668 */                 int newCount = this.count - 1;
/* 1669 */                 this.modCount++;
/* 1670 */                 E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1671 */                 newCount = this.count - 1;
/* 1672 */                 table.set(index, newFirst);
/* 1673 */                 this.count = newCount;
/*      */               } 
/* 1675 */               return null;
/*      */             } 
/*      */             
/* 1678 */             this.modCount++;
/* 1679 */             setValue((E)internalEntry2, newValue);
/* 1680 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 1684 */         internalEntry2 = null; return (V)internalEntry2;
/*      */       } finally {
/* 1686 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     V remove(Object key, int hash) {
/* 1692 */       lock();
/*      */       try {
/* 1694 */         preWriteCleanup();
/*      */         
/* 1696 */         int newCount = this.count - 1;
/* 1697 */         AtomicReferenceArray<E> table = this.table;
/* 1698 */         int index = hash & table.length() - 1;
/* 1699 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         MapMakerInternalMap.InternalEntry internalEntry2;
/* 1701 */         for (internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1702 */           K entryKey = (K)internalEntry2.getKey();
/* 1703 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1705 */             .equivalent(key, entryKey)) {
/* 1706 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1708 */             if (entryValue == null)
/*      */             {
/* 1710 */               if (!isCollected(internalEntry2))
/*      */               {
/*      */                 
/* 1713 */                 return null;
/*      */               }
/*      */             }
/* 1716 */             this.modCount++;
/* 1717 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1718 */             newCount = this.count - 1;
/* 1719 */             table.set(index, newFirst);
/* 1720 */             this.count = newCount;
/* 1721 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 1725 */         internalEntry2 = null; return (V)internalEntry2;
/*      */       } finally {
/* 1727 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean remove(Object key, int hash, Object value) {
/* 1732 */       lock();
/*      */       try {
/* 1734 */         preWriteCleanup();
/*      */         
/* 1736 */         int newCount = this.count - 1;
/* 1737 */         AtomicReferenceArray<E> table = this.table;
/* 1738 */         int index = hash & table.length() - 1;
/* 1739 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1741 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1742 */           K entryKey = (K)internalEntry2.getKey();
/* 1743 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1745 */             .equivalent(key, entryKey)) {
/* 1746 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1748 */             boolean explicitRemoval = false;
/* 1749 */             if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1750 */               explicitRemoval = true;
/* 1751 */             } else if (!isCollected(internalEntry2)) {
/*      */ 
/*      */               
/* 1754 */               return false;
/*      */             } 
/*      */             
/* 1757 */             this.modCount++;
/* 1758 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1759 */             newCount = this.count - 1;
/* 1760 */             table.set(index, newFirst);
/* 1761 */             this.count = newCount;
/* 1762 */             return explicitRemoval;
/*      */           } 
/*      */         } 
/*      */         
/* 1766 */         return false;
/*      */       } finally {
/* 1768 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     void clear() {
/* 1773 */       if (this.count != 0) {
/* 1774 */         lock();
/*      */         try {
/* 1776 */           AtomicReferenceArray<E> table = this.table;
/* 1777 */           for (int i = 0; i < table.length(); i++) {
/* 1778 */             table.set(i, null);
/*      */           }
/* 1780 */           maybeClearReferenceQueues();
/* 1781 */           this.readCount.set(0);
/*      */           
/* 1783 */           this.modCount++;
/* 1784 */           this.count = 0;
/*      */         } finally {
/* 1786 */           unlock();
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     E removeFromChain(E first, E entry) {
/* 1805 */       int newCount = this.count;
/* 1806 */       E newFirst = (E)entry.getNext();
/* 1807 */       for (E e = first; e != entry; e = (E)e.getNext()) {
/* 1808 */         E next = copyEntry(e, newFirst);
/* 1809 */         if (next != null) {
/* 1810 */           newFirst = next;
/*      */         } else {
/* 1812 */           newCount--;
/*      */         } 
/*      */       } 
/* 1815 */       this.count = newCount;
/* 1816 */       return newFirst;
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimKey(E entry, int hash) {
/* 1822 */       lock();
/*      */       try {
/* 1824 */         int newCount = this.count - 1;
/* 1825 */         AtomicReferenceArray<E> table = this.table;
/* 1826 */         int index = hash & table.length() - 1;
/* 1827 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1829 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1830 */           if (internalEntry2 == entry) {
/* 1831 */             this.modCount++;
/* 1832 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1833 */             newCount = this.count - 1;
/* 1834 */             table.set(index, newFirst);
/* 1835 */             this.count = newCount;
/* 1836 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/* 1840 */         return false;
/*      */       } finally {
/* 1842 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimValue(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, E> valueReference) {
/* 1849 */       lock();
/*      */       try {
/* 1851 */         int newCount = this.count - 1;
/* 1852 */         AtomicReferenceArray<E> table = this.table;
/* 1853 */         int index = hash & table.length() - 1;
/* 1854 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1856 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1857 */           K entryKey = (K)internalEntry2.getKey();
/* 1858 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1860 */             .equivalent(key, entryKey)) {
/* 1861 */             MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry<K, V, E>)internalEntry2).getValueReference();
/* 1862 */             if (v == valueReference) {
/* 1863 */               this.modCount++;
/* 1864 */               E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1865 */               newCount = this.count - 1;
/* 1866 */               table.set(index, newFirst);
/* 1867 */               this.count = newCount;
/* 1868 */               return true;
/*      */             } 
/* 1870 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 1874 */         return false;
/*      */       } finally {
/* 1876 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean clearValueForTesting(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 1886 */       lock();
/*      */       try {
/* 1888 */         AtomicReferenceArray<E> table = this.table;
/* 1889 */         int index = hash & table.length() - 1;
/* 1890 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1892 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1893 */           K entryKey = (K)internalEntry2.getKey();
/* 1894 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1896 */             .equivalent(key, entryKey)) {
/* 1897 */             MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry<K, V, E>)internalEntry2).getValueReference();
/* 1898 */             if (v == valueReference) {
/* 1899 */               E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1900 */               table.set(index, newFirst);
/* 1901 */               return true;
/*      */             } 
/* 1903 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 1907 */         return false;
/*      */       } finally {
/* 1909 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     boolean removeEntryForTesting(E entry) {
/* 1915 */       int hash = entry.getHash();
/* 1916 */       int newCount = this.count - 1;
/* 1917 */       AtomicReferenceArray<E> table = this.table;
/* 1918 */       int index = hash & table.length() - 1;
/* 1919 */       MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */       
/* 1921 */       for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1922 */         if (internalEntry2 == entry) {
/* 1923 */           this.modCount++;
/* 1924 */           E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1925 */           newCount = this.count - 1;
/* 1926 */           table.set(index, newFirst);
/* 1927 */           this.count = newCount;
/* 1928 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 1932 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static <K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> boolean isCollected(E entry) {
/* 1940 */       return (entry.getValue() == null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V getLiveValue(E entry) {
/* 1949 */       if (entry.getKey() == null) {
/* 1950 */         tryDrainReferenceQueues();
/* 1951 */         return null;
/*      */       } 
/* 1953 */       V value = (V)entry.getValue();
/* 1954 */       if (value == null) {
/* 1955 */         tryDrainReferenceQueues();
/* 1956 */         return null;
/*      */       } 
/*      */       
/* 1959 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postReadCleanup() {
/* 1968 */       if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
/* 1969 */         runCleanup();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void preWriteCleanup() {
/* 1979 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     void runCleanup() {
/* 1983 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     void runLockedCleanup() {
/* 1987 */       if (tryLock()) {
/*      */         try {
/* 1989 */           maybeDrainReferenceQueues();
/* 1990 */           this.readCount.set(0);
/*      */         } finally {
/* 1992 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class StrongKeyStrongValueSegment<K, V>
/*      */     extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>>
/*      */   {
/*      */     StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 2007 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyStrongValueSegment<K, V> self() {
/* 2012 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2018 */       return (MapMakerInternalMap.StrongKeyStrongValueEntry)entry;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class StrongKeyWeakValueSegment<K, V>
/*      */     extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>>
/*      */   {
/* 2025 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 2032 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueSegment<K, V> self() {
/* 2037 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 2042 */       return this.queueForValues;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2048 */       return (MapMakerInternalMap.StrongKeyWeakValueEntry)entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e) {
/* 2054 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value) {
/* 2060 */       return new MapMakerInternalMap.WeakValueReferenceImpl<>(this.queueForValues, value, castForTesting(e));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 2067 */       MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 2069 */       MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> weakValueReference = valueReference;
/*      */       
/* 2071 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/* 2072 */       entry.valueReference = weakValueReference;
/* 2073 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2078 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2083 */       clearReferenceQueue(this.queueForValues);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class StrongKeyDummyValueSegment<K>
/*      */     extends Segment<K, MapMaker.Dummy, StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>>
/*      */   {
/*      */     StrongKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.StrongKeyDummyValueEntry<K>, StrongKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/* 2095 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyDummyValueSegment<K> self() {
/* 2100 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.StrongKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap.InternalEntry<K, MapMaker.Dummy, ?> entry) {
/* 2106 */       return (MapMakerInternalMap.StrongKeyDummyValueEntry)entry;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyStrongValueSegment<K, V>
/*      */     extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>>
/*      */   {
/* 2113 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 2120 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueSegment<K, V> self() {
/* 2125 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 2130 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2136 */       return (MapMakerInternalMap.WeakKeyStrongValueEntry)entry;
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2141 */       drainKeyReferenceQueue(this.queueForKeys);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2146 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyWeakValueSegment<K, V>
/*      */     extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>>
/*      */   {
/* 2153 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/* 2154 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 2160 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueSegment<K, V> self() {
/* 2165 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 2170 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 2175 */       return this.queueForValues;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 2181 */       return (MapMakerInternalMap.WeakKeyWeakValueEntry)entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e) {
/* 2187 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value) {
/* 2193 */       return new MapMakerInternalMap.WeakValueReferenceImpl<>(this.queueForValues, value, castForTesting(e));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 2200 */       MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 2202 */       MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> weakValueReference = valueReference;
/*      */       
/* 2204 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/* 2205 */       entry.valueReference = weakValueReference;
/* 2206 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2211 */       drainKeyReferenceQueue(this.queueForKeys);
/* 2212 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2217 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyDummyValueSegment<K>
/*      */     extends Segment<K, MapMaker.Dummy, WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>>
/*      */   {
/* 2224 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyDummyValueSegment(MapMakerInternalMap<K, MapMaker.Dummy, MapMakerInternalMap.WeakKeyDummyValueEntry<K>, WeakKeyDummyValueSegment<K>> map, int initialCapacity, int maxSegmentSize) {
/* 2230 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyDummyValueSegment<K> self() {
/* 2235 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 2240 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyDummyValueEntry<K> castForTesting(MapMakerInternalMap.InternalEntry<K, MapMaker.Dummy, ?> entry) {
/* 2246 */       return (MapMakerInternalMap.WeakKeyDummyValueEntry)entry;
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2251 */       drainKeyReferenceQueue(this.queueForKeys);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2256 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class CleanupMapTask implements Runnable {
/*      */     final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;
/*      */     
/*      */     public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> map) {
/* 2264 */       this.mapReference = new WeakReference<>(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/* 2269 */       MapMakerInternalMap<?, ?, ?, ?> map = this.mapReference.get();
/* 2270 */       if (map == null) {
/* 2271 */         throw new CancellationException();
/*      */       }
/*      */       
/* 2274 */       for (MapMakerInternalMap.Segment<?, ?, ?, ?> segment : map.segments) {
/* 2275 */         segment.runCleanup();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength keyStrength() {
/* 2282 */     return this.entryHelper.keyStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength valueStrength() {
/* 2287 */     return this.entryHelper.valueStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Equivalence<Object> valueEquivalence() {
/* 2292 */     return this.entryHelper.valueStrength().defaultEquivalence();
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
/*      */   public boolean isEmpty() {
/* 2306 */     long sum = 0L;
/* 2307 */     Segment<K, V, E, S>[] segments = this.segments; int i;
/* 2308 */     for (i = 0; i < segments.length; i++) {
/* 2309 */       if ((segments[i]).count != 0) {
/* 2310 */         return false;
/*      */       }
/* 2312 */       sum += (segments[i]).modCount;
/*      */     } 
/*      */     
/* 2315 */     if (sum != 0L) {
/* 2316 */       for (i = 0; i < segments.length; i++) {
/* 2317 */         if ((segments[i]).count != 0) {
/* 2318 */           return false;
/*      */         }
/* 2320 */         sum -= (segments[i]).modCount;
/*      */       } 
/* 2322 */       if (sum != 0L) {
/* 2323 */         return false;
/*      */       }
/*      */     } 
/* 2326 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/* 2331 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2332 */     long sum = 0L;
/* 2333 */     for (int i = 0; i < segments.length; i++) {
/* 2334 */       sum += (segments[i]).count;
/*      */     }
/* 2336 */     return Ints.saturatedCast(sum);
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(Object key) {
/* 2341 */     if (key == null) {
/* 2342 */       return null;
/*      */     }
/* 2344 */     int hash = hash(key);
/* 2345 */     return segmentFor(hash).get(key, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   E getEntry(Object key) {
/* 2353 */     if (key == null) {
/* 2354 */       return null;
/*      */     }
/* 2356 */     int hash = hash(key);
/* 2357 */     return segmentFor(hash).getEntry(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object key) {
/* 2362 */     if (key == null) {
/* 2363 */       return false;
/*      */     }
/* 2365 */     int hash = hash(key);
/* 2366 */     return segmentFor(hash).containsKey(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(Object value) {
/* 2371 */     if (value == null) {
/* 2372 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2380 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2381 */     long last = -1L;
/* 2382 */     for (int i = 0; i < 3; i++) {
/* 2383 */       long sum = 0L;
/* 2384 */       for (Segment<K, V, E, S> segment : segments) {
/*      */         
/* 2386 */         int unused = segment.count;
/*      */         
/* 2388 */         AtomicReferenceArray<E> table = segment.table;
/* 2389 */         for (int j = 0; j < table.length(); j++) {
/* 2390 */           for (InternalEntry internalEntry = (InternalEntry)table.get(j); internalEntry != null; internalEntry = (InternalEntry)internalEntry.getNext()) {
/* 2391 */             V v = segment.getLiveValue((E)internalEntry);
/* 2392 */             if (v != null && valueEquivalence().equivalent(value, v)) {
/* 2393 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 2397 */         sum += segment.modCount;
/*      */       } 
/* 2399 */       if (sum == last) {
/*      */         break;
/*      */       }
/* 2402 */       last = sum;
/*      */     } 
/* 2404 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V put(K key, V value) {
/* 2410 */     Preconditions.checkNotNull(key);
/* 2411 */     Preconditions.checkNotNull(value);
/* 2412 */     int hash = hash(key);
/* 2413 */     return segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V putIfAbsent(K key, V value) {
/* 2419 */     Preconditions.checkNotNull(key);
/* 2420 */     Preconditions.checkNotNull(value);
/* 2421 */     int hash = hash(key);
/* 2422 */     return segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/* 2427 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 2428 */       put(e.getKey(), e.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V remove(Object key) {
/* 2435 */     if (key == null) {
/* 2436 */       return null;
/*      */     }
/* 2438 */     int hash = hash(key);
/* 2439 */     return segmentFor(hash).remove(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean remove(Object key, Object value) {
/* 2445 */     if (key == null || value == null) {
/* 2446 */       return false;
/*      */     }
/* 2448 */     int hash = hash(key);
/* 2449 */     return segmentFor(hash).remove(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean replace(K key, V oldValue, V newValue) {
/* 2455 */     Preconditions.checkNotNull(key);
/* 2456 */     Preconditions.checkNotNull(newValue);
/* 2457 */     if (oldValue == null) {
/* 2458 */       return false;
/*      */     }
/* 2460 */     int hash = hash(key);
/* 2461 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V replace(K key, V value) {
/* 2467 */     Preconditions.checkNotNull(key);
/* 2468 */     Preconditions.checkNotNull(value);
/* 2469 */     int hash = hash(key);
/* 2470 */     return segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 2475 */     for (Segment<K, V, E, S> segment : this.segments) {
/* 2476 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/* 2484 */     Set<K> ks = this.keySet;
/* 2485 */     return (ks != null) ? ks : (this.keySet = new KeySet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 2492 */     Collection<V> vs = this.values;
/* 2493 */     return (vs != null) ? vs : (this.values = new Values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/* 2500 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 2501 */     return (es != null) ? es : (this.entrySet = new EntrySet());
/*      */   }
/*      */ 
/*      */   
/*      */   abstract class HashIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     MapMakerInternalMap.Segment<K, V, E, S> currentSegment;
/*      */     AtomicReferenceArray<E> currentTable;
/*      */     E nextEntry;
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;
/*      */     
/*      */     HashIterator() {
/* 2517 */       this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
/* 2518 */       this.nextTableIndex = -1;
/* 2519 */       advance();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract T next();
/*      */     
/*      */     final void advance() {
/* 2526 */       this.nextExternal = null;
/*      */       
/* 2528 */       if (nextInChain()) {
/*      */         return;
/*      */       }
/*      */       
/* 2532 */       if (nextInTable()) {
/*      */         return;
/*      */       }
/*      */       
/* 2536 */       while (this.nextSegmentIndex >= 0) {
/* 2537 */         this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
/* 2538 */         if (this.currentSegment.count != 0) {
/* 2539 */           this.currentTable = this.currentSegment.table;
/* 2540 */           this.nextTableIndex = this.currentTable.length() - 1;
/* 2541 */           if (nextInTable()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInChain() {
/* 2550 */       if (this.nextEntry != null) {
/* 2551 */         for (this.nextEntry = (E)this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = (E)this.nextEntry.getNext()) {
/* 2552 */           if (advanceTo(this.nextEntry)) {
/* 2553 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/* 2557 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean nextInTable() {
/* 2562 */       while (this.nextTableIndex >= 0) {
/* 2563 */         if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) != null && (
/* 2564 */           advanceTo(this.nextEntry) || nextInChain())) {
/* 2565 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 2569 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean advanceTo(E entry) {
/*      */       try {
/* 2578 */         K key = (K)entry.getKey();
/* 2579 */         V value = (V)MapMakerInternalMap.this.getLiveValue(entry);
/* 2580 */         if (value != null) {
/* 2581 */           this.nextExternal = new MapMakerInternalMap.WriteThroughEntry(key, value);
/* 2582 */           return true;
/*      */         } 
/*      */         
/* 2585 */         return false;
/*      */       } finally {
/*      */         
/* 2588 */         this.currentSegment.postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 2594 */       return (this.nextExternal != null);
/*      */     }
/*      */     
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
/* 2598 */       if (this.nextExternal == null) {
/* 2599 */         throw new NoSuchElementException();
/*      */       }
/* 2601 */       this.lastReturned = this.nextExternal;
/* 2602 */       advance();
/* 2603 */       return this.lastReturned;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 2608 */       CollectPreconditions.checkRemove((this.lastReturned != null));
/* 2609 */       MapMakerInternalMap.this.remove(this.lastReturned.getKey());
/* 2610 */       this.lastReturned = null;
/*      */     }
/*      */   }
/*      */   
/*      */   final class KeyIterator
/*      */     extends HashIterator<K>
/*      */   {
/*      */     public K next() {
/* 2618 */       return nextEntry().getKey();
/*      */     }
/*      */   }
/*      */   
/*      */   final class ValueIterator
/*      */     extends HashIterator<V>
/*      */   {
/*      */     public V next() {
/* 2626 */       return nextEntry().getValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class WriteThroughEntry
/*      */     extends AbstractMapEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */     
/*      */     V value;
/*      */     
/*      */     WriteThroughEntry(K key, V value) {
/* 2639 */       this.key = key;
/* 2640 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 2645 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 2650 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/* 2656 */       if (object instanceof Map.Entry) {
/* 2657 */         Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 2658 */         return (this.key.equals(that.getKey()) && this.value.equals(that.getValue()));
/*      */       } 
/* 2660 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2666 */       return this.key.hashCode() ^ this.value.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V newValue) {
/* 2671 */       V oldValue = (V)MapMakerInternalMap.this.put(this.key, newValue);
/* 2672 */       this.value = newValue;
/* 2673 */       return oldValue;
/*      */     }
/*      */   }
/*      */   
/*      */   final class EntryIterator
/*      */     extends HashIterator<Map.Entry<K, V>>
/*      */   {
/*      */     public Map.Entry<K, V> next() {
/* 2681 */       return nextEntry();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class KeySet
/*      */     extends SafeToArraySet<K>
/*      */   {
/*      */     public Iterator<K> iterator() {
/* 2690 */       return new MapMakerInternalMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2695 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2700 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2705 */       return MapMakerInternalMap.this.containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2710 */       return (MapMakerInternalMap.this.remove(o) != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2715 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class Values
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     public Iterator<V> iterator() {
/* 2724 */       return new MapMakerInternalMap.ValueIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2729 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2734 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2739 */       return MapMakerInternalMap.this.containsValue(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2744 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2752 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/* 2757 */       return (T[])MapMakerInternalMap.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class EntrySet
/*      */     extends SafeToArraySet<Map.Entry<K, V>>
/*      */   {
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 2766 */       return new MapMakerInternalMap.EntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2771 */       if (!(o instanceof Map.Entry)) {
/* 2772 */         return false;
/*      */       }
/* 2774 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 2775 */       Object key = e.getKey();
/* 2776 */       if (key == null) {
/* 2777 */         return false;
/*      */       }
/* 2779 */       V v = (V)MapMakerInternalMap.this.get(key);
/*      */       
/* 2781 */       return (v != null && MapMakerInternalMap.this.valueEquivalence().equivalent(e.getValue(), v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2786 */       if (!(o instanceof Map.Entry)) {
/* 2787 */         return false;
/*      */       }
/* 2789 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 2790 */       Object key = e.getKey();
/* 2791 */       return (key != null && MapMakerInternalMap.this.remove(key, e.getValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2796 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2801 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2806 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract class SafeToArraySet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SafeToArraySet() {}
/*      */     
/*      */     public Object[] toArray() {
/* 2816 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/* 2821 */       return (T[])MapMakerInternalMap.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> ArrayList<E> toArrayList(Collection<E> c) {
/* 2827 */     ArrayList<E> result = new ArrayList<>(c.size());
/* 2828 */     Iterators.addAll(result, c.iterator());
/* 2829 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object writeReplace() {
/* 2837 */     return new SerializationProxy<>(this.entryHelper
/* 2838 */         .keyStrength(), this.entryHelper
/* 2839 */         .valueStrength(), this.keyEquivalence, this.entryHelper
/*      */         
/* 2841 */         .valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractSerializationProxy<K, V>
/*      */     extends ForwardingConcurrentMap<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */ 
/*      */     
/*      */     final MapMakerInternalMap.Strength keyStrength;
/*      */ 
/*      */     
/*      */     final MapMakerInternalMap.Strength valueStrength;
/*      */ 
/*      */     
/*      */     final Equivalence<Object> keyEquivalence;
/*      */     
/*      */     final Equivalence<Object> valueEquivalence;
/*      */     
/*      */     final int concurrencyLevel;
/*      */     
/*      */     transient ConcurrentMap<K, V> delegate;
/*      */ 
/*      */     
/*      */     AbstractSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
/* 2869 */       this.keyStrength = keyStrength;
/* 2870 */       this.valueStrength = valueStrength;
/* 2871 */       this.keyEquivalence = keyEquivalence;
/* 2872 */       this.valueEquivalence = valueEquivalence;
/* 2873 */       this.concurrencyLevel = concurrencyLevel;
/* 2874 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected ConcurrentMap<K, V> delegate() {
/* 2879 */       return this.delegate;
/*      */     }
/*      */     
/*      */     void writeMapTo(ObjectOutputStream out) throws IOException {
/* 2883 */       out.writeInt(this.delegate.size());
/* 2884 */       for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
/* 2885 */         out.writeObject(entry.getKey());
/* 2886 */         out.writeObject(entry.getValue());
/*      */       } 
/* 2888 */       out.writeObject(null);
/*      */     }
/*      */ 
/*      */     
/*      */     MapMaker readMapMaker(ObjectInputStream in) throws IOException {
/* 2893 */       int size = in.readInt();
/* 2894 */       return (new MapMaker())
/* 2895 */         .initialCapacity(size)
/* 2896 */         .setKeyStrength(this.keyStrength)
/* 2897 */         .setValueStrength(this.valueStrength)
/* 2898 */         .keyEquivalence(this.keyEquivalence)
/* 2899 */         .concurrencyLevel(this.concurrencyLevel);
/*      */     }
/*      */ 
/*      */     
/*      */     void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*      */       while (true) {
/* 2905 */         K key = (K)in.readObject();
/* 2906 */         if (key == null) {
/*      */           break;
/*      */         }
/* 2909 */         V value = (V)in.readObject();
/* 2910 */         this.delegate.put(key, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SerializationProxy<K, V>
/*      */     extends AbstractSerializationProxy<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
/* 2929 */       super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, concurrencyLevel, delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream out) throws IOException {
/* 2934 */       out.defaultWriteObject();
/* 2935 */       writeMapTo(out);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 2939 */       in.defaultReadObject();
/* 2940 */       MapMaker mapMaker = readMapMaker(in);
/* 2941 */       this.delegate = mapMaker.makeMap();
/* 2942 */       readEntries(in);
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 2946 */       return this.delegate;
/*      */     }
/*      */   }
/*      */   
/*      */   static interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>> {
/*      */     V get();
/*      */     
/*      */     E getEntry();
/*      */     
/*      */     void clear();
/*      */     
/*      */     WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> param1ReferenceQueue, E param1E);
/*      */   }
/*      */   
/*      */   static interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> getValueReference();
/*      */     
/*      */     void clearValue();
/*      */   }
/*      */   
/*      */   static interface StrongValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {}
/*      */   
/*      */   static interface InternalEntry<K, V, E extends InternalEntry<K, V, E>> {
/*      */     E getNext();
/*      */     
/*      */     int getHash();
/*      */     
/*      */     K getKey();
/*      */     
/*      */     V getValue();
/*      */   }
/*      */   
/*      */   static interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> {
/*      */     MapMakerInternalMap.Strength keyStrength();
/*      */     
/*      */     MapMakerInternalMap.Strength valueStrength();
/*      */     
/*      */     S newSegment(MapMakerInternalMap<K, V, E, S> param1MapMakerInternalMap, int param1Int1, int param1Int2);
/*      */     
/*      */     E newEntry(S param1S, K param1K, int param1Int, E param1E);
/*      */     
/*      */     E copy(S param1S, E param1E1, E param1E2);
/*      */     
/*      */     void setValue(S param1S, E param1E, V param1V);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\MapMakerInternalMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */