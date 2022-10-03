/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Tables
/*     */ {
/*     */   @Beta
/*     */   public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, Supplier<I> tableSupplier) {
/*  71 */     return toTable(rowFunction, columnFunction, valueFunction, (v1, v2) -> { throw new IllegalStateException("Conflicting values " + v1 + " and " + v2); }tableSupplier);
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
/*     */   public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction, Supplier<I> tableSupplier) {
/* 101 */     Preconditions.checkNotNull(rowFunction);
/* 102 */     Preconditions.checkNotNull(columnFunction);
/* 103 */     Preconditions.checkNotNull(valueFunction);
/* 104 */     Preconditions.checkNotNull(mergeFunction);
/* 105 */     Preconditions.checkNotNull(tableSupplier);
/* 106 */     return (Collector)Collector.of(tableSupplier, (table, input) -> merge(table, rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction), (table1, table2) -> { for (Table.Cell<R, C, V> cell2 : (Iterable<Table.Cell<R, C, V>>)table2.cellSet()) merge(table1, cell2.getRowKey(), cell2.getColumnKey(), cell2.getValue(), mergeFunction);  return table1; }new Collector.Characteristics[0]);
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
/*     */   private static <R, C, V> void merge(Table<R, C, V> table, R row, C column, V value, BinaryOperator<V> mergeFunction) {
/* 125 */     Preconditions.checkNotNull(value);
/* 126 */     V oldValue = table.get(row, column);
/* 127 */     if (oldValue == null) {
/* 128 */       table.put(row, column, value);
/*     */     } else {
/* 130 */       V newValue = mergeFunction.apply(oldValue, value);
/* 131 */       if (newValue == null) {
/* 132 */         table.remove(row, column);
/*     */       } else {
/* 134 */         table.put(row, column, newValue);
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
/*     */ 
/*     */   
/*     */   public static <R, C, V> Table.Cell<R, C, V> immutableCell(R rowKey, C columnKey, V value) {
/* 150 */     return new ImmutableCell<>(rowKey, columnKey, value);
/*     */   }
/*     */   
/*     */   static final class ImmutableCell<R, C, V> extends AbstractCell<R, C, V> implements Serializable { private final R rowKey;
/*     */     private final C columnKey;
/*     */     private final V value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ImmutableCell(R rowKey, C columnKey, V value) {
/* 159 */       this.rowKey = rowKey;
/* 160 */       this.columnKey = columnKey;
/* 161 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public R getRowKey() {
/* 166 */       return this.rowKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public C getColumnKey() {
/* 171 */       return this.columnKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 176 */       return this.value;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class AbstractCell<R, C, V>
/*     */     implements Table.Cell<R, C, V>
/*     */   {
/*     */     public boolean equals(Object obj) {
/* 188 */       if (obj == this) {
/* 189 */         return true;
/*     */       }
/* 191 */       if (obj instanceof Table.Cell) {
/* 192 */         Table.Cell<?, ?, ?> other = (Table.Cell<?, ?, ?>)obj;
/* 193 */         return (Objects.equal(getRowKey(), other.getRowKey()) && 
/* 194 */           Objects.equal(getColumnKey(), other.getColumnKey()) && 
/* 195 */           Objects.equal(getValue(), other.getValue()));
/*     */       } 
/* 197 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 202 */       return Objects.hashCode(new Object[] { getRowKey(), getColumnKey(), getValue() });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 207 */       return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue();
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
/*     */   
/*     */   public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) {
/* 224 */     return (table instanceof TransposeTable) ? ((TransposeTable)table).original : new TransposeTable<>(table);
/*     */   }
/*     */   
/*     */   private static class TransposeTable<C, R, V>
/*     */     extends AbstractTable<C, R, V>
/*     */   {
/*     */     final Table<R, C, V> original;
/*     */     
/*     */     TransposeTable(Table<R, C, V> original) {
/* 233 */       this.original = (Table<R, C, V>)Preconditions.checkNotNull(original);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 238 */       this.original.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> column(R columnKey) {
/* 243 */       return this.original.row(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> columnKeySet() {
/* 248 */       return this.original.rowKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> columnMap() {
/* 253 */       return this.original.rowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object rowKey, Object columnKey) {
/* 258 */       return this.original.contains(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsColumn(Object columnKey) {
/* 263 */       return this.original.containsRow(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsRow(Object rowKey) {
/* 268 */       return this.original.containsColumn(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsValue(Object value) {
/* 273 */       return this.original.containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object rowKey, Object columnKey) {
/* 278 */       return this.original.get(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(C rowKey, R columnKey, V value) {
/* 283 */       return this.original.put(columnKey, rowKey, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends C, ? extends R, ? extends V> table) {
/* 288 */       this.original.putAll(Tables.transpose(table));
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object rowKey, Object columnKey) {
/* 293 */       return this.original.remove(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> row(C rowKey) {
/* 298 */       return this.original.column(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> rowKeySet() {
/* 303 */       return this.original.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> rowMap() {
/* 308 */       return this.original.columnMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 313 */       return this.original.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 318 */       return this.original.values();
/*     */     }
/*     */ 
/*     */     
/* 322 */     private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>()
/*     */       {
/*     */         public Table.Cell<?, ?, ?> apply(Table.Cell<?, ?, ?> cell)
/*     */         {
/* 326 */           return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<C, R, V>> cellIterator() {
/* 333 */       return (Iterator)Iterators.transform(this.original.cellSet().iterator(), TRANSPOSE_CELL);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Spliterator<Table.Cell<C, R, V>> cellSpliterator() {
/* 339 */       return (Spliterator)CollectSpliterators.map(this.original.cellSet().spliterator(), (Function<? super Table.Cell<?, ?, ?>, ? extends Table.Cell<?, ?, ?>>)TRANSPOSE_CELL);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/* 384 */     Preconditions.checkArgument(backingMap.isEmpty());
/* 385 */     Preconditions.checkNotNull(factory);
/*     */     
/* 387 */     return new StandardTable<>(backingMap, factory);
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
/*     */   @Beta
/*     */   public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 414 */     return new TransformedTable<>(fromTable, function);
/*     */   }
/*     */   
/*     */   private static class TransformedTable<R, C, V1, V2> extends AbstractTable<R, C, V2> {
/*     */     final Table<R, C, V1> fromTable;
/*     */     final Function<? super V1, V2> function;
/*     */     
/*     */     TransformedTable(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 422 */       this.fromTable = (Table<R, C, V1>)Preconditions.checkNotNull(fromTable);
/* 423 */       this.function = (Function<? super V1, V2>)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object rowKey, Object columnKey) {
/* 428 */       return this.fromTable.contains(rowKey, columnKey);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V2 get(Object rowKey, Object columnKey) {
/* 435 */       return contains(rowKey, columnKey) ? (V2)this.function.apply(this.fromTable.get(rowKey, columnKey)) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 440 */       return this.fromTable.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 445 */       this.fromTable.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public V2 put(R rowKey, C columnKey, V2 value) {
/* 450 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V2> table) {
/* 455 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public V2 remove(Object rowKey, Object columnKey) {
/* 460 */       return contains(rowKey, columnKey) ? (V2)this.function
/* 461 */         .apply(this.fromTable.remove(rowKey, columnKey)) : null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<C, V2> row(R rowKey) {
/* 467 */       return Maps.transformValues(this.fromTable.row(rowKey), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V2> column(C columnKey) {
/* 472 */       return Maps.transformValues(this.fromTable.column(columnKey), this.function);
/*     */     }
/*     */     
/*     */     Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
/* 476 */       return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>()
/*     */         {
/*     */           public Table.Cell<R, C, V2> apply(Table.Cell<R, C, V1> cell) {
/* 479 */             return Tables.immutableCell(cell
/* 480 */                 .getRowKey(), cell.getColumnKey(), (V2)Tables.TransformedTable.this.function.apply(cell.getValue()));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<R, C, V2>> cellIterator() {
/* 487 */       return Iterators.transform(this.fromTable.cellSet().iterator(), cellFunction());
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Table.Cell<R, C, V2>> cellSpliterator() {
/* 492 */       return CollectSpliterators.map(this.fromTable.cellSet().spliterator(), (Function<? super Table.Cell<R, C, V1>, ? extends Table.Cell<R, C, V2>>)cellFunction());
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 497 */       return this.fromTable.rowKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 502 */       return this.fromTable.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<V2> createValues() {
/* 507 */       return Collections2.transform(this.fromTable.values(), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V2>> rowMap() {
/* 512 */       Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>()
/*     */         {
/*     */           public Map<C, V2> apply(Map<C, V1> row)
/*     */           {
/* 516 */             return Maps.transformValues(row, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 519 */       return Maps.transformValues(this.fromTable.rowMap(), rowFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V2>> columnMap() {
/* 524 */       Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>()
/*     */         {
/*     */           public Map<R, V2> apply(Map<R, V1> column)
/*     */           {
/* 528 */             return Maps.transformValues(column, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 531 */       return Maps.transformValues(this.fromTable.columnMap(), columnFunction);
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
/*     */ 
/*     */   
/*     */   public static <R, C, V> Table<R, C, V> unmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) {
/* 549 */     return new UnmodifiableTable<>(table);
/*     */   }
/*     */   
/*     */   private static class UnmodifiableTable<R, C, V> extends ForwardingTable<R, C, V> implements Serializable {
/*     */     final Table<? extends R, ? extends C, ? extends V> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     UnmodifiableTable(Table<? extends R, ? extends C, ? extends V> delegate) {
/* 557 */       this.delegate = (Table<? extends R, ? extends C, ? extends V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Table<R, C, V> delegate() {
/* 563 */       return (Table)this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Table.Cell<R, C, V>> cellSet() {
/* 568 */       return Collections.unmodifiableSet(super.cellSet());
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 573 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> column(C columnKey) {
/* 578 */       return Collections.unmodifiableMap(super.column(columnKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 583 */       return Collections.unmodifiableSet(super.columnKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> columnMap() {
/* 588 */       Function<Map<R, V>, Map<R, V>> wrapper = Tables.unmodifiableWrapper();
/* 589 */       return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(R rowKey, C columnKey, V value) {
/* 594 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 599 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object rowKey, Object columnKey) {
/* 604 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> row(R rowKey) {
/* 609 */       return Collections.unmodifiableMap(super.row(rowKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 614 */       return Collections.unmodifiableSet(super.rowKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> rowMap() {
/* 619 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 620 */       return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 625 */       return Collections.unmodifiableCollection(super.values());
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
/*     */   public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(RowSortedTable<R, ? extends C, ? extends V> table) {
/* 651 */     return new UnmodifiableRowSortedMap<>(table);
/*     */   }
/*     */   
/*     */   static final class UnmodifiableRowSortedMap<R, C, V> extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public UnmodifiableRowSortedMap(RowSortedTable<R, ? extends C, ? extends V> delegate) {
/* 658 */       super(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RowSortedTable<R, C, V> delegate() {
/* 663 */       return (RowSortedTable<R, C, V>)super.delegate();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> rowMap() {
/* 668 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 669 */       return Collections.unmodifiableSortedMap(Maps.transformValues(delegate().rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<R> rowKeySet() {
/* 674 */       return Collections.unmodifiableSortedSet(delegate().rowKeySet());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
/* 682 */     return (Function)UNMODIFIABLE_WRAPPER;
/*     */   }
/*     */   
/* 685 */   private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>()
/*     */     {
/*     */       public Map<Object, Object> apply(Map<Object, Object> input)
/*     */       {
/* 689 */         return Collections.unmodifiableMap(input);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> Table<R, C, V> synchronizedTable(Table<R, C, V> table) {
/* 723 */     return Synchronized.table(table, null);
/*     */   }
/*     */   
/*     */   static boolean equalsImpl(Table<?, ?, ?> table, Object obj) {
/* 727 */     if (obj == table)
/* 728 */       return true; 
/* 729 */     if (obj instanceof Table) {
/* 730 */       Table<?, ?, ?> that = (Table<?, ?, ?>)obj;
/* 731 */       return table.cellSet().equals(that.cellSet());
/*     */     } 
/* 733 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Tables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */