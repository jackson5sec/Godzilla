/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ @GwtIncompatible
/*     */ public final class ConcurrentHashMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final transient ConcurrentMap<E, AtomicInteger> countMap;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   private static class FieldSettersHolder
/*     */   {
/*  78 */     static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ConcurrentHashMultiset<E> create() {
/*  89 */     return new ConcurrentHashMultiset<>(new ConcurrentHashMap<>());
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
/*     */   public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements) {
/* 101 */     ConcurrentHashMultiset<E> multiset = create();
/* 102 */     Iterables.addAll(multiset, elements);
/* 103 */     return multiset;
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
/*     */   
/*     */   @Beta
/*     */   public static <E> ConcurrentHashMultiset<E> create(ConcurrentMap<E, AtomicInteger> countMap) {
/* 122 */     return new ConcurrentHashMultiset<>(countMap);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> countMap) {
/* 127 */     Preconditions.checkArgument(countMap.isEmpty(), "the backing map (%s) must be empty", countMap);
/* 128 */     this.countMap = countMap;
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
/*     */   public int count(Object element) {
/* 141 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 142 */     return (existingCounter == null) ? 0 : existingCounter.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 153 */     long sum = 0L;
/* 154 */     for (AtomicInteger value : this.countMap.values()) {
/* 155 */       sum += value.get();
/*     */     }
/* 157 */     return Ints.saturatedCast(sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 167 */     return snapshot().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 172 */     return snapshot().toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<E> snapshot() {
/* 180 */     List<E> list = Lists.newArrayListWithExpectedSize(size());
/* 181 */     for (Multiset.Entry<E> entry : (Iterable<Multiset.Entry<E>>)entrySet()) {
/* 182 */       E element = entry.getElement();
/* 183 */       for (int i = entry.getCount(); i > 0; i--) {
/* 184 */         list.add(element);
/*     */       }
/*     */     } 
/* 187 */     return list;
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
/*     */   @CanIgnoreReturnValue
/*     */   public int add(E element, int occurrences) {
/*     */     AtomicInteger existingCounter, newCounter;
/* 204 */     Preconditions.checkNotNull(element);
/* 205 */     if (occurrences == 0) {
/* 206 */       return count(element);
/*     */     }
/* 208 */     CollectPreconditions.checkPositive(occurrences, "occurences");
/*     */     
/*     */     do {
/* 211 */       existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 212 */       if (existingCounter == null) {
/* 213 */         existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(occurrences));
/* 214 */         if (existingCounter == null) {
/* 215 */           return 0;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       while (true) {
/* 221 */         int oldValue = existingCounter.get();
/* 222 */         if (oldValue != 0) {
/*     */           try {
/* 224 */             int newValue = IntMath.checkedAdd(oldValue, occurrences);
/* 225 */             if (existingCounter.compareAndSet(oldValue, newValue))
/*     */             {
/* 227 */               return oldValue;
/*     */             }
/* 229 */           } catch (ArithmeticException overflow) {
/* 230 */             throw new IllegalArgumentException("Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/* 237 */       newCounter = new AtomicInteger(occurrences);
/* 238 */     } while (this.countMap.putIfAbsent(element, newCounter) != null && 
/* 239 */       !this.countMap.replace(element, existingCounter, newCounter));
/* 240 */     return 0;
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
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(Object element, int occurrences) {
/* 271 */     if (occurrences == 0) {
/* 272 */       return count(element);
/*     */     }
/* 274 */     CollectPreconditions.checkPositive(occurrences, "occurences");
/*     */     
/* 276 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 277 */     if (existingCounter == null) {
/* 278 */       return 0;
/*     */     }
/*     */     while (true) {
/* 281 */       int oldValue = existingCounter.get();
/* 282 */       if (oldValue != 0) {
/* 283 */         int newValue = Math.max(0, oldValue - occurrences);
/* 284 */         if (existingCounter.compareAndSet(oldValue, newValue)) {
/* 285 */           if (newValue == 0)
/*     */           {
/*     */             
/* 288 */             this.countMap.remove(element, existingCounter);
/*     */           }
/* 290 */           return oldValue;
/*     */         }  continue;
/*     */       }  break;
/* 293 */     }  return 0;
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
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeExactly(Object element, int occurrences) {
/* 312 */     if (occurrences == 0) {
/* 313 */       return true;
/*     */     }
/* 315 */     CollectPreconditions.checkPositive(occurrences, "occurences");
/*     */     
/* 317 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 318 */     if (existingCounter == null) {
/* 319 */       return false;
/*     */     }
/*     */     while (true) {
/* 322 */       int oldValue = existingCounter.get();
/* 323 */       if (oldValue < occurrences) {
/* 324 */         return false;
/*     */       }
/* 326 */       int newValue = oldValue - occurrences;
/* 327 */       if (existingCounter.compareAndSet(oldValue, newValue)) {
/* 328 */         if (newValue == 0)
/*     */         {
/*     */           
/* 331 */           this.countMap.remove(element, existingCounter);
/*     */         }
/* 333 */         return true;
/*     */       } 
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
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/* 348 */     Preconditions.checkNotNull(element);
/* 349 */     CollectPreconditions.checkNonnegative(count, "count");
/*     */     label26: while (true) {
/* 351 */       AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 352 */       if (existingCounter == null) {
/* 353 */         if (count == 0) {
/* 354 */           return 0;
/*     */         }
/* 356 */         existingCounter = this.countMap.putIfAbsent(element, new AtomicInteger(count));
/* 357 */         if (existingCounter == null) {
/* 358 */           return 0;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 365 */         int oldValue = existingCounter.get();
/* 366 */         if (oldValue == 0) {
/* 367 */           if (count == 0) {
/* 368 */             return 0;
/*     */           }
/* 370 */           AtomicInteger newCounter = new AtomicInteger(count);
/* 371 */           if (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap
/* 372 */             .replace(element, existingCounter, newCounter)) {
/* 373 */             return 0;
/*     */           }
/*     */           
/*     */           continue label26;
/*     */         } 
/* 378 */         if (existingCounter.compareAndSet(oldValue, count)) {
/* 379 */           if (count == 0)
/*     */           {
/*     */             
/* 382 */             this.countMap.remove(element, existingCounter);
/*     */           }
/* 384 */           return oldValue;
/*     */         } 
/*     */       } 
/*     */       break;
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
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean setCount(E element, int expectedOldCount, int newCount) {
/* 404 */     Preconditions.checkNotNull(element);
/* 405 */     CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
/* 406 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*     */     
/* 408 */     AtomicInteger existingCounter = Maps.<AtomicInteger>safeGet(this.countMap, element);
/* 409 */     if (existingCounter == null) {
/* 410 */       if (expectedOldCount != 0)
/* 411 */         return false; 
/* 412 */       if (newCount == 0) {
/* 413 */         return true;
/*     */       }
/*     */       
/* 416 */       return (this.countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null);
/*     */     } 
/*     */     
/* 419 */     int oldValue = existingCounter.get();
/* 420 */     if (oldValue == expectedOldCount) {
/* 421 */       if (oldValue == 0) {
/* 422 */         if (newCount == 0) {
/*     */           
/* 424 */           this.countMap.remove(element, existingCounter);
/* 425 */           return true;
/*     */         } 
/* 427 */         AtomicInteger newCounter = new AtomicInteger(newCount);
/* 428 */         return (this.countMap.putIfAbsent(element, newCounter) == null || this.countMap
/* 429 */           .replace(element, existingCounter, newCounter));
/*     */       } 
/*     */       
/* 432 */       if (existingCounter.compareAndSet(oldValue, newCount)) {
/* 433 */         if (newCount == 0)
/*     */         {
/*     */           
/* 436 */           this.countMap.remove(element, existingCounter);
/*     */         }
/* 438 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 442 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<E> createElementSet() {
/* 449 */     final Set<E> delegate = this.countMap.keySet();
/* 450 */     return new ForwardingSet<E>()
/*     */       {
/*     */         protected Set<E> delegate() {
/* 453 */           return delegate;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(Object object) {
/* 458 */           return (object != null && Collections2.safeContains(delegate, object));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean containsAll(Collection<?> collection) {
/* 463 */           return standardContainsAll(collection);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(Object object) {
/* 468 */           return (object != null && Collections2.safeRemove(delegate, object));
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean removeAll(Collection<?> c) {
/* 473 */           return standardRemoveAll(c);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<E> elementIterator() {
/* 480 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Set<Multiset.Entry<E>> createEntrySet() {
/* 487 */     return new EntrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 492 */     return this.countMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 497 */     return this.countMap.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 504 */     final Iterator<Multiset.Entry<E>> readOnlyIterator = new AbstractIterator<Multiset.Entry<E>>()
/*     */       {
/* 506 */         private final Iterator<Map.Entry<E, AtomicInteger>> mapEntries = ConcurrentHashMultiset.this
/* 507 */           .countMap.entrySet().iterator();
/*     */ 
/*     */         
/*     */         protected Multiset.Entry<E> computeNext() {
/*     */           while (true) {
/* 512 */             if (!this.mapEntries.hasNext()) {
/* 513 */               return endOfData();
/*     */             }
/* 515 */             Map.Entry<E, AtomicInteger> mapEntry = this.mapEntries.next();
/* 516 */             int count = ((AtomicInteger)mapEntry.getValue()).get();
/* 517 */             if (count != 0) {
/* 518 */               return Multisets.immutableEntry(mapEntry.getKey(), count);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */     
/* 524 */     return new ForwardingIterator<Multiset.Entry<E>>()
/*     */       {
/*     */         private Multiset.Entry<E> last;
/*     */         
/*     */         protected Iterator<Multiset.Entry<E>> delegate() {
/* 529 */           return readOnlyIterator;
/*     */         }
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 534 */           this.last = super.next();
/* 535 */           return this.last;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 540 */           CollectPreconditions.checkRemove((this.last != null));
/* 541 */           ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
/* 542 */           this.last = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 549 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 554 */     this.countMap.clear();
/*     */   }
/*     */   
/*     */   private class EntrySet extends AbstractMultiset<E>.EntrySet {
/*     */     private EntrySet() {}
/*     */     
/*     */     ConcurrentHashMultiset<E> multiset() {
/* 561 */       return ConcurrentHashMultiset.this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 571 */       return snapshot().toArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 576 */       return snapshot().toArray(array);
/*     */     }
/*     */     
/*     */     private List<Multiset.Entry<E>> snapshot() {
/* 580 */       List<Multiset.Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
/*     */       
/* 582 */       Iterators.addAll(list, iterator());
/* 583 */       return list;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 589 */     stream.defaultWriteObject();
/* 590 */     stream.writeObject(this.countMap);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 594 */     stream.defaultReadObject();
/*     */ 
/*     */     
/* 597 */     ConcurrentMap<E, Integer> deserializedCountMap = (ConcurrentMap<E, Integer>)stream.readObject();
/* 598 */     FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, deserializedCountMap);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ConcurrentHashMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */