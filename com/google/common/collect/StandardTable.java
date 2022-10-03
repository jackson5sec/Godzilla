/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class StandardTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   @GwtTransient
/*     */   final Map<R, Map<C, V>> backingMap;
/*     */   @GwtTransient
/*     */   final Supplier<? extends Map<C, V>> factory;
/*     */   private transient Set<C> columnKeySet;
/*     */   private transient Map<R, Map<C, V>> rowMap;
/*     */   private transient ColumnMap columnMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/*  73 */     this.backingMap = backingMap;
/*  74 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/*  81 */     return (rowKey != null && columnKey != null && super.contains(rowKey, columnKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(Object columnKey) {
/*  86 */     if (columnKey == null) {
/*  87 */       return false;
/*     */     }
/*  89 */     for (Map<C, V> map : this.backingMap.values()) {
/*  90 */       if (Maps.safeContainsKey(map, columnKey)) {
/*  91 */         return true;
/*     */       }
/*     */     } 
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsRow(Object rowKey) {
/*  99 */     return (rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 104 */     return (value != null && super.containsValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object rowKey, Object columnKey) {
/* 109 */     return (rowKey == null || columnKey == null) ? null : super.get(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 114 */     return this.backingMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 119 */     int size = 0;
/* 120 */     for (Map<C, V> map : this.backingMap.values()) {
/* 121 */       size += map.size();
/*     */     }
/* 123 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 130 */     this.backingMap.clear();
/*     */   }
/*     */   
/*     */   private Map<C, V> getOrCreate(R rowKey) {
/* 134 */     Map<C, V> map = this.backingMap.get(rowKey);
/* 135 */     if (map == null) {
/* 136 */       map = (Map<C, V>)this.factory.get();
/* 137 */       this.backingMap.put(rowKey, map);
/*     */     } 
/* 139 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, V value) {
/* 145 */     Preconditions.checkNotNull(rowKey);
/* 146 */     Preconditions.checkNotNull(columnKey);
/* 147 */     Preconditions.checkNotNull(value);
/* 148 */     return getOrCreate(rowKey).put(columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object rowKey, Object columnKey) {
/* 154 */     if (rowKey == null || columnKey == null) {
/* 155 */       return null;
/*     */     }
/* 157 */     Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
/* 158 */     if (map == null) {
/* 159 */       return null;
/*     */     }
/* 161 */     V value = map.remove(columnKey);
/* 162 */     if (map.isEmpty()) {
/* 163 */       this.backingMap.remove(rowKey);
/*     */     }
/* 165 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private Map<R, V> removeColumn(Object column) {
/* 170 */     Map<R, V> output = new LinkedHashMap<>();
/* 171 */     Iterator<Map.Entry<R, Map<C, V>>> iterator = this.backingMap.entrySet().iterator();
/* 172 */     while (iterator.hasNext()) {
/* 173 */       Map.Entry<R, Map<C, V>> entry = iterator.next();
/* 174 */       V value = (V)((Map)entry.getValue()).remove(column);
/* 175 */       if (value != null) {
/* 176 */         output.put(entry.getKey(), value);
/* 177 */         if (((Map)entry.getValue()).isEmpty()) {
/* 178 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */     } 
/* 182 */     return output;
/*     */   }
/*     */   
/*     */   private boolean containsMapping(Object rowKey, Object columnKey, Object value) {
/* 186 */     return (value != null && value.equals(get(rowKey, columnKey)));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
/* 191 */     if (containsMapping(rowKey, columnKey, value)) {
/* 192 */       remove(rowKey, columnKey);
/* 193 */       return true;
/*     */     } 
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class TableSet<T>
/*     */     extends Sets.ImprovedAbstractSet<T>
/*     */   {
/*     */     private TableSet() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 208 */       return StandardTable.this.backingMap.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 213 */       StandardTable.this.backingMap.clear();
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
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 228 */     return super.cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 233 */     return new CellIterator();
/*     */   }
/*     */   
/*     */   private class CellIterator implements Iterator<Table.Cell<R, C, V>> {
/* 237 */     final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */     Map.Entry<R, Map<C, V>> rowEntry;
/* 239 */     Iterator<Map.Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 243 */       return (this.rowIterator.hasNext() || this.columnIterator.hasNext());
/*     */     }
/*     */ 
/*     */     
/*     */     public Table.Cell<R, C, V> next() {
/* 248 */       if (!this.columnIterator.hasNext()) {
/* 249 */         this.rowEntry = this.rowIterator.next();
/* 250 */         this.columnIterator = ((Map<C, V>)this.rowEntry.getValue()).entrySet().iterator();
/*     */       } 
/* 252 */       Map.Entry<C, V> columnEntry = this.columnIterator.next();
/* 253 */       return Tables.immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 258 */       this.columnIterator.remove();
/* 259 */       if (((Map)this.rowEntry.getValue()).isEmpty()) {
/* 260 */         this.rowIterator.remove();
/* 261 */         this.rowEntry = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private CellIterator() {} }
/*     */   
/*     */   Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 268 */     return CollectSpliterators.flatMap(this.backingMap
/* 269 */         .entrySet().spliterator(), rowEntry -> CollectSpliterators.map(((Map)rowEntry.getValue()).entrySet().spliterator(), ()), 65, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 277 */         size());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<C, V> row(R rowKey) {
/* 282 */     return new Row(rowKey);
/*     */   }
/*     */   
/*     */   class Row extends Maps.IteratorBasedAbstractMap<C, V> { final R rowKey;
/*     */     Map<C, V> backingRowMap;
/*     */     
/*     */     Row(R rowKey) {
/* 289 */       this.rowKey = (R)Preconditions.checkNotNull(rowKey);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Map<C, V> backingRowMap() {
/* 295 */       return (this.backingRowMap == null || (this.backingRowMap.isEmpty() && StandardTable.this.backingMap.containsKey(this.rowKey))) ? (this
/* 296 */         .backingRowMap = computeBackingRowMap()) : this.backingRowMap;
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> computeBackingRowMap() {
/* 301 */       return (Map<C, V>)StandardTable.this.backingMap.get(this.rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     void maintainEmptyInvariant() {
/* 306 */       if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
/* 307 */         StandardTable.this.backingMap.remove(this.rowKey);
/* 308 */         this.backingRowMap = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 314 */       Map<C, V> backingRowMap = backingRowMap();
/* 315 */       return (key != null && backingRowMap != null && Maps.safeContainsKey(backingRowMap, key));
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 320 */       Map<C, V> backingRowMap = backingRowMap();
/* 321 */       return (key != null && backingRowMap != null) ? Maps.<V>safeGet(backingRowMap, key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(C key, V value) {
/* 326 */       Preconditions.checkNotNull(key);
/* 327 */       Preconditions.checkNotNull(value);
/* 328 */       if (this.backingRowMap != null && !this.backingRowMap.isEmpty()) {
/* 329 */         return this.backingRowMap.put(key, value);
/*     */       }
/* 331 */       return StandardTable.this.put(this.rowKey, key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 336 */       Map<C, V> backingRowMap = backingRowMap();
/* 337 */       if (backingRowMap == null) {
/* 338 */         return null;
/*     */       }
/* 340 */       V result = Maps.safeRemove(backingRowMap, key);
/* 341 */       maintainEmptyInvariant();
/* 342 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 347 */       Map<C, V> backingRowMap = backingRowMap();
/* 348 */       if (backingRowMap != null) {
/* 349 */         backingRowMap.clear();
/*     */       }
/* 351 */       maintainEmptyInvariant();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 356 */       Map<C, V> map = backingRowMap();
/* 357 */       return (map == null) ? 0 : map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<C, V>> entryIterator() {
/* 362 */       Map<C, V> map = backingRowMap();
/* 363 */       if (map == null) {
/* 364 */         return Iterators.emptyModifiableIterator();
/*     */       }
/* 366 */       final Iterator<Map.Entry<C, V>> iterator = map.entrySet().iterator();
/* 367 */       return new Iterator<Map.Entry<C, V>>()
/*     */         {
/*     */           public boolean hasNext() {
/* 370 */             return iterator.hasNext();
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<C, V> next() {
/* 375 */             return StandardTable.Row.this.wrapEntry(iterator.next());
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 380 */             iterator.remove();
/* 381 */             StandardTable.Row.this.maintainEmptyInvariant();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Map.Entry<C, V>> entrySpliterator() {
/* 388 */       Map<C, V> map = backingRowMap();
/* 389 */       if (map == null) {
/* 390 */         return Spliterators.emptySpliterator();
/*     */       }
/* 392 */       return CollectSpliterators.map(map.entrySet().spliterator(), this::wrapEntry);
/*     */     }
/*     */     
/*     */     Map.Entry<C, V> wrapEntry(final Map.Entry<C, V> entry) {
/* 396 */       return new ForwardingMapEntry<C, V>()
/*     */         {
/*     */           protected Map.Entry<C, V> delegate() {
/* 399 */             return entry;
/*     */           }
/*     */ 
/*     */           
/*     */           public V setValue(V value) {
/* 404 */             return super.setValue((V)Preconditions.checkNotNull(value));
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public boolean equals(Object object) {
/* 410 */             return standardEquals(object);
/*     */           }
/*     */         };
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, V> column(C columnKey) {
/* 423 */     return new Column(columnKey);
/*     */   }
/*     */   
/*     */   private class Column extends Maps.ViewCachingAbstractMap<R, V> {
/*     */     final C columnKey;
/*     */     
/*     */     Column(C columnKey) {
/* 430 */       this.columnKey = (C)Preconditions.checkNotNull(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(R key, V value) {
/* 435 */       return StandardTable.this.put(key, this.columnKey, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 440 */       return (V)StandardTable.this.get(key, this.columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 445 */       return StandardTable.this.contains(key, this.columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 450 */       return (V)StandardTable.this.remove(key, this.columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     boolean removeFromColumnIf(Predicate<? super Map.Entry<R, V>> predicate) {
/* 456 */       boolean changed = false;
/* 457 */       Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/* 458 */       while (iterator.hasNext()) {
/* 459 */         Map.Entry<R, Map<C, V>> entry = iterator.next();
/* 460 */         Map<C, V> map = entry.getValue();
/* 461 */         V value = map.get(this.columnKey);
/* 462 */         if (value != null && predicate.apply(Maps.immutableEntry(entry.getKey(), value))) {
/* 463 */           map.remove(this.columnKey);
/* 464 */           changed = true;
/* 465 */           if (map.isEmpty()) {
/* 466 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 470 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     Set<Map.Entry<R, V>> createEntrySet() {
/* 475 */       return new EntrySet();
/*     */     }
/*     */     
/*     */     private class EntrySet extends Sets.ImprovedAbstractSet<Map.Entry<R, V>> {
/*     */       private EntrySet() {}
/*     */       
/*     */       public Iterator<Map.Entry<R, V>> iterator() {
/* 482 */         return new StandardTable.Column.EntrySetIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 487 */         int size = 0;
/* 488 */         for (Map<C, V> map : (Iterable<Map<C, V>>)StandardTable.this.backingMap.values()) {
/* 489 */           if (map.containsKey(StandardTable.Column.this.columnKey)) {
/* 490 */             size++;
/*     */           }
/*     */         } 
/* 493 */         return size;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isEmpty() {
/* 498 */         return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
/*     */       }
/*     */ 
/*     */       
/*     */       public void clear() {
/* 503 */         StandardTable.Column.this.removeFromColumnIf(Predicates.alwaysTrue());
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object o) {
/* 508 */         if (o instanceof Map.Entry) {
/* 509 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 510 */           return StandardTable.this.containsMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 512 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 517 */         if (obj instanceof Map.Entry) {
/* 518 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 519 */           return StandardTable.this.removeMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 521 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 526 */         return StandardTable.Column.this.removeFromColumnIf(Predicates.not(Predicates.in(c)));
/*     */       }
/*     */     }
/*     */     
/*     */     private class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
/* 531 */       final Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */ 
/*     */       
/*     */       protected Map.Entry<R, V> computeNext() {
/* 535 */         while (this.iterator.hasNext())
/* 536 */         { final Map.Entry<R, Map<C, V>> entry = this.iterator.next();
/* 537 */           if (((Map)entry.getValue()).containsKey(StandardTable.Column.this.columnKey))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 555 */             return new EntryImpl(); }  }  class EntryImpl extends AbstractMapEntry<R, V> {
/*     */           public R getKey() { return (R)entry.getKey(); }
/*     */           public V getValue() { return (V)((Map)entry.getValue()).get(StandardTable.Column.this.columnKey); } public V setValue(V value) { return ((Map<C, V>)entry.getValue()).put(StandardTable.Column.this.columnKey, (V)Preconditions.checkNotNull(value)); }
/* 558 */         }; return endOfData();
/*     */       }
/*     */       
/*     */       private EntrySetIterator() {} }
/*     */     
/*     */     Set<R> createKeySet() {
/* 564 */       return new KeySet();
/*     */     }
/*     */     
/*     */     private class KeySet
/*     */       extends Maps.KeySet<R, V> {
/*     */       KeySet() {
/* 570 */         super(StandardTable.Column.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 575 */         return StandardTable.this.contains(obj, StandardTable.Column.this.columnKey);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 580 */         return (StandardTable.this.remove(obj, StandardTable.Column.this.columnKey) != null);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 585 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<V> createValues() {
/* 591 */       return new Values();
/*     */     }
/*     */     
/*     */     private class Values
/*     */       extends Maps.Values<R, V> {
/*     */       Values() {
/* 597 */         super(StandardTable.Column.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 602 */         return (obj != null && StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.equalTo(obj))));
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 607 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.in(c)));
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 612 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<R> rowKeySet() {
/* 619 */     return rowMap().keySet();
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
/*     */   public Set<C> columnKeySet() {
/* 634 */     Set<C> result = this.columnKeySet;
/* 635 */     return (result == null) ? (this.columnKeySet = new ColumnKeySet()) : result;
/*     */   }
/*     */   
/*     */   private class ColumnKeySet extends TableSet<C> {
/*     */     private ColumnKeySet() {}
/*     */     
/*     */     public Iterator<C> iterator() {
/* 642 */       return StandardTable.this.createColumnKeyIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 647 */       return Iterators.size(iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 652 */       if (obj == null) {
/* 653 */         return false;
/*     */       }
/* 655 */       boolean changed = false;
/* 656 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 657 */       while (iterator.hasNext()) {
/* 658 */         Map<C, V> map = iterator.next();
/* 659 */         if (map.keySet().remove(obj)) {
/* 660 */           changed = true;
/* 661 */           if (map.isEmpty()) {
/* 662 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 666 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 671 */       Preconditions.checkNotNull(c);
/* 672 */       boolean changed = false;
/* 673 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 674 */       while (iterator.hasNext()) {
/* 675 */         Map<C, V> map = iterator.next();
/*     */ 
/*     */         
/* 678 */         if (Iterators.removeAll(map.keySet().iterator(), c)) {
/* 679 */           changed = true;
/* 680 */           if (map.isEmpty()) {
/* 681 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 685 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 690 */       Preconditions.checkNotNull(c);
/* 691 */       boolean changed = false;
/* 692 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 693 */       while (iterator.hasNext()) {
/* 694 */         Map<C, V> map = iterator.next();
/* 695 */         if (map.keySet().retainAll(c)) {
/* 696 */           changed = true;
/* 697 */           if (map.isEmpty()) {
/* 698 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 702 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 707 */       return StandardTable.this.containsColumn(obj);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<C> createColumnKeyIterator() {
/* 713 */     return new ColumnKeyIterator();
/*     */   }
/*     */   
/*     */   private class ColumnKeyIterator
/*     */     extends AbstractIterator<C>
/*     */   {
/* 719 */     final Map<C, V> seen = (Map<C, V>)StandardTable.this.factory.get();
/* 720 */     final Iterator<Map<C, V>> mapIterator = StandardTable.this.backingMap.values().iterator();
/* 721 */     Iterator<Map.Entry<C, V>> entryIterator = Iterators.emptyIterator();
/*     */ 
/*     */     
/*     */     protected C computeNext() {
/*     */       while (true) {
/* 726 */         while (this.entryIterator.hasNext()) {
/* 727 */           Map.Entry<C, V> entry = this.entryIterator.next();
/* 728 */           if (!this.seen.containsKey(entry.getKey())) {
/* 729 */             this.seen.put(entry.getKey(), entry.getValue());
/* 730 */             return entry.getKey();
/*     */           } 
/* 732 */         }  if (this.mapIterator.hasNext()) {
/* 733 */           this.entryIterator = ((Map<C, V>)this.mapIterator.next()).entrySet().iterator(); continue;
/*     */         }  break;
/* 735 */       }  return endOfData();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ColumnKeyIterator() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 749 */     return super.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 756 */     Map<R, Map<C, V>> result = this.rowMap;
/* 757 */     return (result == null) ? (this.rowMap = createRowMap()) : result;
/*     */   }
/*     */   
/*     */   Map<R, Map<C, V>> createRowMap() {
/* 761 */     return new RowMap();
/*     */   }
/*     */   
/*     */   class RowMap
/*     */     extends Maps.ViewCachingAbstractMap<R, Map<C, V>>
/*     */   {
/*     */     public boolean containsKey(Object key) {
/* 768 */       return StandardTable.this.containsRow(key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<C, V> get(Object key) {
/* 775 */       return StandardTable.this.containsRow(key) ? StandardTable.this.row(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> remove(Object key) {
/* 780 */       return (key == null) ? null : (Map<C, V>)StandardTable.this.backingMap.remove(key);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() {
/* 785 */       return new EntrySet();
/*     */     }
/*     */     
/*     */     class EntrySet
/*     */       extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>>
/*     */     {
/*     */       public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
/* 792 */         return Maps.asMapEntryIterator(StandardTable.this.backingMap
/* 793 */             .keySet(), new Function<R, Map<C, V>>()
/*     */             {
/*     */               public Map<C, V> apply(R rowKey)
/*     */               {
/* 797 */                 return StandardTable.this.row(rowKey);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 804 */         return StandardTable.this.backingMap.size();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 809 */         if (obj instanceof Map.Entry) {
/* 810 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 811 */           return (entry.getKey() != null && entry
/* 812 */             .getValue() instanceof Map && 
/* 813 */             Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry));
/*     */         } 
/* 815 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 820 */         if (obj instanceof Map.Entry) {
/* 821 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 822 */           return (entry.getKey() != null && entry
/* 823 */             .getValue() instanceof Map && StandardTable.this.backingMap
/* 824 */             .entrySet().remove(entry));
/*     */         } 
/* 826 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 835 */     ColumnMap result = this.columnMap;
/* 836 */     return (result == null) ? (this.columnMap = new ColumnMap()) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   private class ColumnMap
/*     */     extends Maps.ViewCachingAbstractMap<C, Map<R, V>>
/*     */   {
/*     */     private ColumnMap() {}
/*     */     
/*     */     public Map<R, V> get(Object key) {
/* 846 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.column(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 851 */       return StandardTable.this.containsColumn(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> remove(Object key) {
/* 856 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<C, Map<R, V>>> createEntrySet() {
/* 861 */       return new ColumnMapEntrySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> keySet() {
/* 866 */       return StandardTable.this.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<Map<R, V>> createValues() {
/* 871 */       return new ColumnMapValues();
/*     */     }
/*     */     
/*     */     class ColumnMapEntrySet
/*     */       extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>>
/*     */     {
/*     */       public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
/* 878 */         return Maps.asMapEntryIterator(StandardTable.this
/* 879 */             .columnKeySet(), new Function<C, Map<R, V>>()
/*     */             {
/*     */               public Map<R, V> apply(C columnKey)
/*     */               {
/* 883 */                 return StandardTable.this.column(columnKey);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 890 */         return StandardTable.this.columnKeySet().size();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 895 */         if (obj instanceof Map.Entry) {
/* 896 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 897 */           if (StandardTable.this.containsColumn(entry.getKey())) {
/*     */ 
/*     */ 
/*     */             
/* 901 */             C columnKey = (C)entry.getKey();
/* 902 */             return StandardTable.ColumnMap.this.get(columnKey).equals(entry.getValue());
/*     */           } 
/*     */         } 
/* 905 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 910 */         if (contains(obj)) {
/* 911 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 912 */           StandardTable.this.removeColumn(entry.getKey());
/* 913 */           return true;
/*     */         } 
/* 915 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 926 */         Preconditions.checkNotNull(c);
/* 927 */         return Sets.removeAllImpl(this, c.iterator());
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 932 */         Preconditions.checkNotNull(c);
/* 933 */         boolean changed = false;
/* 934 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 935 */           if (!c.contains(Maps.immutableEntry(columnKey, StandardTable.this.column(columnKey)))) {
/* 936 */             StandardTable.this.removeColumn(columnKey);
/* 937 */             changed = true;
/*     */           } 
/*     */         } 
/* 940 */         return changed;
/*     */       }
/*     */     }
/*     */     
/*     */     private class ColumnMapValues
/*     */       extends Maps.Values<C, Map<R, V>> {
/*     */       ColumnMapValues() {
/* 947 */         super(StandardTable.ColumnMap.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 952 */         for (Map.Entry<C, Map<R, V>> entry : StandardTable.ColumnMap.this.entrySet()) {
/* 953 */           if (((Map)entry.getValue()).equals(obj)) {
/* 954 */             StandardTable.this.removeColumn(entry.getKey());
/* 955 */             return true;
/*     */           } 
/*     */         } 
/* 958 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 963 */         Preconditions.checkNotNull(c);
/* 964 */         boolean changed = false;
/* 965 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 966 */           if (c.contains(StandardTable.this.column(columnKey))) {
/* 967 */             StandardTable.this.removeColumn(columnKey);
/* 968 */             changed = true;
/*     */           } 
/*     */         } 
/* 971 */         return changed;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 976 */         Preconditions.checkNotNull(c);
/* 977 */         boolean changed = false;
/* 978 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 979 */           if (!c.contains(StandardTable.this.column(columnKey))) {
/* 980 */             StandardTable.this.removeColumn(columnKey);
/* 981 */             changed = true;
/*     */           } 
/*     */         } 
/* 984 */         return changed;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\StandardTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */