/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
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
/*     */ @GwtCompatible
/*     */ public abstract class ImmutableTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction) {
/*  66 */     Preconditions.checkNotNull(rowFunction, "rowFunction");
/*  67 */     Preconditions.checkNotNull(columnFunction, "columnFunction");
/*  68 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  69 */     return Collector.of(() -> new Builder<>(), (builder, t) -> builder.put(rowFunction.apply(t), columnFunction.apply(t), valueFunction.apply(t)), (b1, b2) -> b1.combine(b2), b -> b.build(), new Collector.Characteristics[0]);
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
/*     */   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  94 */     Preconditions.checkNotNull(rowFunction, "rowFunction");
/*  95 */     Preconditions.checkNotNull(columnFunction, "columnFunction");
/*  96 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  97 */     Preconditions.checkNotNull(mergeFunction, "mergeFunction");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     return Collector.of(() -> new CollectorState<>(), (state, input) -> state.put(rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction), (s1, s2) -> s1.combine(s2, mergeFunction), state -> state.toTable(), new Collector.Characteristics[0]);
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
/*     */   private static final class CollectorState<R, C, V>
/*     */   {
/* 119 */     final List<ImmutableTable.MutableCell<R, C, V>> insertionOrder = new ArrayList<>();
/* 120 */     final Table<R, C, ImmutableTable.MutableCell<R, C, V>> table = HashBasedTable.create();
/*     */     
/*     */     void put(R row, C column, V value, BinaryOperator<V> merger) {
/* 123 */       ImmutableTable.MutableCell<R, C, V> oldCell = this.table.get(row, column);
/* 124 */       if (oldCell == null) {
/* 125 */         ImmutableTable.MutableCell<R, C, V> cell = new ImmutableTable.MutableCell<>(row, column, value);
/* 126 */         this.insertionOrder.add(cell);
/* 127 */         this.table.put(row, column, cell);
/*     */       } else {
/* 129 */         oldCell.merge(value, merger);
/*     */       } 
/*     */     }
/*     */     
/*     */     CollectorState<R, C, V> combine(CollectorState<R, C, V> other, BinaryOperator<V> merger) {
/* 134 */       for (ImmutableTable.MutableCell<R, C, V> cell : other.insertionOrder) {
/* 135 */         put(cell.getRowKey(), cell.getColumnKey(), cell.getValue(), merger);
/*     */       }
/* 137 */       return this;
/*     */     }
/*     */     
/*     */     ImmutableTable<R, C, V> toTable() {
/* 141 */       return ImmutableTable.copyOf((Iterable)this.insertionOrder);
/*     */     }
/*     */     
/*     */     private CollectorState() {}
/*     */   }
/*     */   
/*     */   private static final class MutableCell<R, C, V> extends Tables.AbstractCell<R, C, V> {
/*     */     private final R row;
/*     */     
/*     */     MutableCell(R row, C column, V value) {
/* 151 */       this.row = (R)Preconditions.checkNotNull(row, "row");
/* 152 */       this.column = (C)Preconditions.checkNotNull(column, "column");
/* 153 */       this.value = (V)Preconditions.checkNotNull(value, "value");
/*     */     }
/*     */     private final C column;
/*     */     
/*     */     public R getRowKey() {
/* 158 */       return this.row;
/*     */     }
/*     */     private V value;
/*     */     
/*     */     public C getColumnKey() {
/* 163 */       return this.column;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 168 */       return this.value;
/*     */     }
/*     */     
/*     */     void merge(V value, BinaryOperator<V> mergeFunction) {
/* 172 */       Preconditions.checkNotNull(value, "value");
/* 173 */       this.value = (V)Preconditions.checkNotNull(mergeFunction.apply(this.value, value), "mergeFunction.apply");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> ImmutableTable<R, C, V> of() {
/* 180 */     return (ImmutableTable)SparseImmutableTable.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <R, C, V> ImmutableTable<R, C, V> of(R rowKey, C columnKey, V value) {
/* 185 */     return new SingletonImmutableTable<>(rowKey, columnKey, value);
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
/*     */   public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
/* 203 */     if (table instanceof ImmutableTable) {
/*     */       
/* 205 */       ImmutableTable<R, C, V> parameterizedTable = (ImmutableTable)table;
/* 206 */       return parameterizedTable;
/*     */     } 
/* 208 */     return copyOf(table.cellSet());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <R, C, V> ImmutableTable<R, C, V> copyOf(Iterable<? extends Table.Cell<? extends R, ? extends C, ? extends V>> cells) {
/* 214 */     Builder<R, C, V> builder = builder();
/* 215 */     for (Table.Cell<? extends R, ? extends C, ? extends V> cell : cells) {
/* 216 */       builder.put(cell);
/*     */     }
/* 218 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> Builder<R, C, V> builder() {
/* 226 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> Table.Cell<R, C, V> cellOf(R rowKey, C columnKey, V value) {
/* 234 */     return Tables.immutableCell(
/* 235 */         (R)Preconditions.checkNotNull(rowKey, "rowKey"), 
/* 236 */         (C)Preconditions.checkNotNull(columnKey, "columnKey"), 
/* 237 */         (V)Preconditions.checkNotNull(value, "value"));
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
/*     */   public static final class Builder<R, C, V>
/*     */   {
/* 267 */     private final List<Table.Cell<R, C, V>> cells = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */     
/*     */     private Comparator<? super R> rowComparator;
/*     */ 
/*     */     
/*     */     private Comparator<? super C> columnComparator;
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> orderRowsBy(Comparator<? super R> rowComparator) {
/* 280 */       this.rowComparator = (Comparator<? super R>)Preconditions.checkNotNull(rowComparator, "rowComparator");
/* 281 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> orderColumnsBy(Comparator<? super C> columnComparator) {
/* 287 */       this.columnComparator = (Comparator<? super C>)Preconditions.checkNotNull(columnComparator, "columnComparator");
/* 288 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> put(R rowKey, C columnKey, V value) {
/* 297 */       this.cells.add(ImmutableTable.cellOf(rowKey, columnKey, value));
/* 298 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> put(Table.Cell<? extends R, ? extends C, ? extends V> cell) {
/* 307 */       if (cell instanceof Tables.ImmutableCell) {
/* 308 */         Preconditions.checkNotNull(cell.getRowKey(), "row");
/* 309 */         Preconditions.checkNotNull(cell.getColumnKey(), "column");
/* 310 */         Preconditions.checkNotNull(cell.getValue(), "value");
/*     */         
/* 312 */         Table.Cell<? extends R, ? extends C, ? extends V> cell1 = cell;
/* 313 */         this.cells.add(cell1);
/*     */       } else {
/* 315 */         put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*     */       } 
/* 317 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 328 */       for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
/* 329 */         put(cell);
/*     */       }
/* 331 */       return this;
/*     */     }
/*     */     
/*     */     Builder<R, C, V> combine(Builder<R, C, V> other) {
/* 335 */       this.cells.addAll(other.cells);
/* 336 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableTable<R, C, V> build() {
/* 345 */       int size = this.cells.size();
/* 346 */       switch (size) {
/*     */         case 0:
/* 348 */           return ImmutableTable.of();
/*     */         case 1:
/* 350 */           return new SingletonImmutableTable<>(Iterables.<Table.Cell<R, C, V>>getOnlyElement(this.cells));
/*     */       } 
/* 352 */       return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Table.Cell<R, C, V>> cellSet() {
/* 361 */     return (ImmutableSet<Table.Cell<R, C, V>>)super.cellSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() {
/* 369 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   final Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 374 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 379 */     return (ImmutableCollection<V>)super.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Iterator<V> valuesIterator() {
/* 387 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, V> column(C columnKey) {
/* 397 */     Preconditions.checkNotNull(columnKey, "columnKey");
/* 398 */     return (ImmutableMap<R, V>)MoreObjects.firstNonNull(
/* 399 */         columnMap().get(columnKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<C> columnKeySet() {
/* 404 */     return columnMap().keySet();
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
/*     */   public ImmutableMap<C, V> row(R rowKey) {
/* 423 */     Preconditions.checkNotNull(rowKey, "rowKey");
/* 424 */     return (ImmutableMap<C, V>)MoreObjects.firstNonNull(
/* 425 */         rowMap().get(rowKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<R> rowKeySet() {
/* 430 */     return rowMap().keySet();
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
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/* 444 */     return (get(rowKey, columnKey) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 449 */     return values().contains(value);
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
/*     */   public final void clear() {
/* 461 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public final V put(R rowKey, C columnKey, V value) {
/* 474 */     throw new UnsupportedOperationException();
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
/*     */   public final void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 486 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public final V remove(Object rowKey, Object columnKey) {
/* 499 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     private final Object[] rowKeys;
/*     */ 
/*     */     
/*     */     private final Object[] columnKeys;
/*     */ 
/*     */     
/*     */     private final Object[] cellValues;
/*     */     
/*     */     private final int[] cellRowIndices;
/*     */     
/*     */     private final int[] cellColumnIndices;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     private SerializedForm(Object[] rowKeys, Object[] columnKeys, Object[] cellValues, int[] cellRowIndices, int[] cellColumnIndices) {
/* 523 */       this.rowKeys = rowKeys;
/* 524 */       this.columnKeys = columnKeys;
/* 525 */       this.cellValues = cellValues;
/* 526 */       this.cellRowIndices = cellRowIndices;
/* 527 */       this.cellColumnIndices = cellColumnIndices;
/*     */     }
/*     */ 
/*     */     
/*     */     static SerializedForm create(ImmutableTable<?, ?, ?> table, int[] cellRowIndices, int[] cellColumnIndices) {
/* 532 */       return new SerializedForm(table
/* 533 */           .rowKeySet().toArray(), table
/* 534 */           .columnKeySet().toArray(), table
/* 535 */           .values().toArray(), cellRowIndices, cellColumnIndices);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 541 */       if (this.cellValues.length == 0) {
/* 542 */         return ImmutableTable.of();
/*     */       }
/* 544 */       if (this.cellValues.length == 1) {
/* 545 */         return ImmutableTable.of(this.rowKeys[0], this.columnKeys[0], this.cellValues[0]);
/*     */       }
/* 547 */       ImmutableList.Builder<Table.Cell<Object, Object, Object>> cellListBuilder = new ImmutableList.Builder<>(this.cellValues.length);
/*     */       
/* 549 */       for (int i = 0; i < this.cellValues.length; i++) {
/* 550 */         cellListBuilder.add(
/* 551 */             ImmutableTable.cellOf(this.rowKeys[this.cellRowIndices[i]], this.columnKeys[this.cellColumnIndices[i]], this.cellValues[i]));
/*     */       }
/* 553 */       return RegularImmutableTable.forOrderedComponents(cellListBuilder
/* 554 */           .build(), ImmutableSet.copyOf(this.rowKeys), ImmutableSet.copyOf(this.columnKeys));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final Object writeReplace() {
/* 561 */     return createSerializedForm();
/*     */   }
/*     */   
/*     */   abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();
/*     */   
/*     */   abstract ImmutableCollection<V> createValues();
/*     */   
/*     */   public abstract ImmutableMap<C, Map<R, V>> columnMap();
/*     */   
/*     */   public abstract ImmutableMap<R, Map<C, V>> rowMap();
/*     */   
/*     */   abstract SerializedForm createSerializedForm();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */