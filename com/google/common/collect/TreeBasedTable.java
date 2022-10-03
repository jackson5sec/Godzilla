/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ public class TreeBasedTable<R, C, V>
/*     */   extends StandardRowSortedTable<R, C, V>
/*     */ {
/*     */   private final Comparator<? super C> columnComparator;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static class Factory<C, V>
/*     */     implements Supplier<TreeMap<C, V>>, Serializable
/*     */   {
/*     */     final Comparator<? super C> comparator;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     Factory(Comparator<? super C> comparator) {
/*  76 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     public TreeMap<C, V> get() {
/*  81 */       return new TreeMap<>(this.comparator);
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
/*     */   public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
/*  96 */     return new TreeBasedTable<>(Ordering.natural(), Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
/* 107 */     Preconditions.checkNotNull(rowComparator);
/* 108 */     Preconditions.checkNotNull(columnComparator);
/* 109 */     return new TreeBasedTable<>(rowComparator, columnComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> table) {
/* 118 */     TreeBasedTable<R, C, V> result = new TreeBasedTable<>(table.rowComparator(), table.columnComparator());
/* 119 */     result.putAll(table);
/* 120 */     return result;
/*     */   }
/*     */   
/*     */   TreeBasedTable(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
/* 124 */     super(new TreeMap<>(rowComparator), (Supplier)new Factory<>(columnComparator));
/* 125 */     this.columnComparator = columnComparator;
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
/*     */   @Deprecated
/*     */   public Comparator<? super R> rowComparator() {
/* 138 */     return rowKeySet().comparator();
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
/*     */   @Deprecated
/*     */   public Comparator<? super C> columnComparator() {
/* 151 */     return this.columnComparator;
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
/*     */   public SortedMap<C, V> row(R rowKey) {
/* 168 */     return new TreeRow(rowKey);
/*     */   }
/*     */   
/*     */   private class TreeRow
/*     */     extends StandardTable<R, C, V>.Row implements SortedMap<C, V> {
/*     */     final C lowerBound;
/*     */     
/*     */     TreeRow(R rowKey) {
/* 176 */       this(rowKey, null, null);
/*     */     }
/*     */     final C upperBound; transient SortedMap<C, V> wholeRow;
/*     */     TreeRow(R rowKey, C lowerBound, C upperBound) {
/* 180 */       super(rowKey);
/* 181 */       this.lowerBound = lowerBound;
/* 182 */       this.upperBound = upperBound;
/* 183 */       Preconditions.checkArgument((lowerBound == null || upperBound == null || 
/* 184 */           compare(lowerBound, upperBound) <= 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<C> keySet() {
/* 189 */       return new Maps.SortedKeySet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super C> comparator() {
/* 194 */       return TreeBasedTable.this.columnComparator();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     int compare(Object a, Object b) {
/* 200 */       Comparator<Object> cmp = (Comparator)comparator();
/* 201 */       return cmp.compare(a, b);
/*     */     }
/*     */     
/*     */     boolean rangeContains(Object o) {
/* 205 */       return (o != null && (this.lowerBound == null || 
/* 206 */         compare(this.lowerBound, o) <= 0) && (this.upperBound == null || 
/* 207 */         compare(this.upperBound, o) > 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> subMap(C fromKey, C toKey) {
/* 212 */       Preconditions.checkArgument((rangeContains(Preconditions.checkNotNull(fromKey)) && rangeContains(Preconditions.checkNotNull(toKey))));
/* 213 */       return new TreeRow(this.rowKey, fromKey, toKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> headMap(C toKey) {
/* 218 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(toKey)));
/* 219 */       return new TreeRow(this.rowKey, this.lowerBound, toKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> tailMap(C fromKey) {
/* 224 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(fromKey)));
/* 225 */       return new TreeRow(this.rowKey, fromKey, this.upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     public C firstKey() {
/* 230 */       SortedMap<C, V> backing = backingRowMap();
/* 231 */       if (backing == null) {
/* 232 */         throw new NoSuchElementException();
/*     */       }
/* 234 */       return backingRowMap().firstKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public C lastKey() {
/* 239 */       SortedMap<C, V> backing = backingRowMap();
/* 240 */       if (backing == null) {
/* 241 */         throw new NoSuchElementException();
/*     */       }
/* 243 */       return backingRowMap().lastKey();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SortedMap<C, V> wholeRow() {
/* 253 */       if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey))) {
/* 254 */         this.wholeRow = (SortedMap<C, V>)TreeBasedTable.this.backingMap.get(this.rowKey);
/*     */       }
/* 256 */       return this.wholeRow;
/*     */     }
/*     */ 
/*     */     
/*     */     SortedMap<C, V> backingRowMap() {
/* 261 */       return (SortedMap<C, V>)super.backingRowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     SortedMap<C, V> computeBackingRowMap() {
/* 266 */       SortedMap<C, V> map = wholeRow();
/* 267 */       if (map != null) {
/* 268 */         if (this.lowerBound != null) {
/* 269 */           map = map.tailMap(this.lowerBound);
/*     */         }
/* 271 */         if (this.upperBound != null) {
/* 272 */           map = map.headMap(this.upperBound);
/*     */         }
/* 274 */         return map;
/*     */       } 
/* 276 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     void maintainEmptyInvariant() {
/* 281 */       if (wholeRow() != null && this.wholeRow.isEmpty()) {
/* 282 */         TreeBasedTable.this.backingMap.remove(this.rowKey);
/* 283 */         this.wholeRow = null;
/* 284 */         this.backingRowMap = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 290 */       return (rangeContains(key) && super.containsKey(key));
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(C key, V value) {
/* 295 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(key)));
/* 296 */       return super.put(key, value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<R> rowKeySet() {
/* 304 */     return super.rowKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<R, Map<C, V>> rowMap() {
/* 309 */     return super.rowMap();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<C> createColumnKeyIterator() {
/* 315 */     final Comparator<? super C> comparator = columnComparator();
/*     */ 
/*     */     
/* 318 */     final Iterator<C> merged = Iterators.mergeSorted(
/* 319 */         Iterables.transform(this.backingMap
/* 320 */           .values(), new Function<Map<C, V>, Iterator<C>>()
/*     */           {
/*     */             public Iterator<C> apply(Map<C, V> input)
/*     */             {
/* 324 */               return input.keySet().iterator();
/*     */             }
/*     */           }), comparator);
/*     */ 
/*     */     
/* 329 */     return new AbstractIterator<C>()
/*     */       {
/*     */         C lastValue;
/*     */         
/*     */         protected C computeNext() {
/* 334 */           while (merged.hasNext()) {
/* 335 */             C next = merged.next();
/* 336 */             boolean duplicate = (this.lastValue != null && comparator.compare(next, this.lastValue) == 0);
/*     */ 
/*     */             
/* 339 */             if (!duplicate) {
/* 340 */               this.lastValue = next;
/* 341 */               return this.lastValue;
/*     */             } 
/*     */           } 
/*     */           
/* 345 */           this.lastValue = null;
/* 346 */           return endOfData();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\TreeBasedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */