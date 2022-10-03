/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class ArrayTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   private final ImmutableList<R> rowList;
/*     */   private final ImmutableList<C> columnList;
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final V[][] array;
/*     */   private transient ColumnMap columnMap;
/*     */   private transient RowMap rowMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/*  95 */     return new ArrayTable<>(rowKeys, columnKeys);
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
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, V> table) {
/* 123 */     return (table instanceof ArrayTable) ? new ArrayTable<>((ArrayTable<R, C, V>)table) : new ArrayTable<>(table);
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
/*     */   private ArrayTable(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/* 137 */     this.rowList = ImmutableList.copyOf(rowKeys);
/* 138 */     this.columnList = ImmutableList.copyOf(columnKeys);
/* 139 */     Preconditions.checkArgument((this.rowList.isEmpty() == this.columnList.isEmpty()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     this.rowKeyToIndex = Maps.indexMap(this.rowList);
/* 148 */     this.columnKeyToIndex = Maps.indexMap(this.columnList);
/*     */ 
/*     */     
/* 151 */     V[][] tmpArray = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/* 152 */     this.array = tmpArray;
/*     */     
/* 154 */     eraseAll();
/*     */   }
/*     */   
/*     */   private ArrayTable(Table<R, C, V> table) {
/* 158 */     this(table.rowKeySet(), table.columnKeySet());
/* 159 */     putAll(table);
/*     */   }
/*     */   
/*     */   private ArrayTable(ArrayTable<R, C, V> table) {
/* 163 */     this.rowList = table.rowList;
/* 164 */     this.columnList = table.columnList;
/* 165 */     this.rowKeyToIndex = table.rowKeyToIndex;
/* 166 */     this.columnKeyToIndex = table.columnKeyToIndex;
/*     */     
/* 168 */     V[][] copy = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/* 169 */     this.array = copy;
/* 170 */     for (int i = 0; i < this.rowList.size(); i++)
/* 171 */       System.arraycopy(table.array[i], 0, copy[i], 0, (table.array[i]).length); 
/*     */   }
/*     */   
/*     */   private static abstract class ArrayMap<K, V>
/*     */     extends Maps.IteratorBasedAbstractMap<K, V> {
/*     */     private final ImmutableMap<K, Integer> keyIndex;
/*     */     
/*     */     private ArrayMap(ImmutableMap<K, Integer> keyIndex) {
/* 179 */       this.keyIndex = keyIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> keySet() {
/* 184 */       return this.keyIndex.keySet();
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/* 188 */       return this.keyIndex.keySet().asList().get(index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 199 */       return this.keyIndex.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 204 */       return this.keyIndex.isEmpty();
/*     */     }
/*     */     
/*     */     Map.Entry<K, V> getEntry(final int index) {
/* 208 */       Preconditions.checkElementIndex(index, size());
/* 209 */       return new AbstractMapEntry<K, V>()
/*     */         {
/*     */           public K getKey() {
/* 212 */             return (K)ArrayTable.ArrayMap.this.getKey(index);
/*     */           }
/*     */ 
/*     */           
/*     */           public V getValue() {
/* 217 */             return (V)ArrayTable.ArrayMap.this.getValue(index);
/*     */           }
/*     */ 
/*     */           
/*     */           public V setValue(V value) {
/* 222 */             return (V)ArrayTable.ArrayMap.this.setValue(index, value);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 229 */       return new AbstractIndexedListIterator<Map.Entry<K, V>>(size())
/*     */         {
/*     */           protected Map.Entry<K, V> get(int index) {
/* 232 */             return ArrayTable.ArrayMap.this.getEntry(index);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 239 */       return CollectSpliterators.indexed(size(), 16, this::getEntry);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 246 */       return this.keyIndex.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 251 */       Integer index = this.keyIndex.get(key);
/* 252 */       if (index == null) {
/* 253 */         return null;
/*     */       }
/* 255 */       return getValue(index.intValue());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V put(K key, V value) {
/* 261 */       Integer index = this.keyIndex.get(key);
/* 262 */       if (index == null) {
/* 263 */         throw new IllegalArgumentException(
/* 264 */             getKeyRole() + " " + key + " not in " + this.keyIndex.keySet());
/*     */       }
/* 266 */       return setValue(index.intValue(), value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 271 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     abstract String getKeyRole();
/*     */     abstract V getValue(int param1Int);
/*     */     public void clear() {
/* 276 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     abstract V setValue(int param1Int, V param1V);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableList<R> rowKeyList() {
/* 285 */     return this.rowList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<C> columnKeyList() {
/* 293 */     return this.columnList;
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
/*     */   public V at(int rowIndex, int columnIndex) {
/* 310 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 311 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 312 */     return this.array[rowIndex][columnIndex];
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
/*     */   public V set(int rowIndex, int columnIndex, V value) {
/* 331 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 332 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 333 */     V oldValue = this.array[rowIndex][columnIndex];
/* 334 */     this.array[rowIndex][columnIndex] = value;
/* 335 */     return oldValue;
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
/*     */   @GwtIncompatible
/*     */   public V[][] toArray(Class<V> valueClass) {
/* 350 */     V[][] copy = (V[][])Array.newInstance(valueClass, new int[] { this.rowList.size(), this.columnList.size() });
/* 351 */     for (int i = 0; i < this.rowList.size(); i++) {
/* 352 */       System.arraycopy(this.array[i], 0, copy[i], 0, (this.array[i]).length);
/*     */     }
/* 354 */     return copy;
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
/*     */   public void clear() {
/* 366 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void eraseAll() {
/* 371 */     for (V[] row : this.array) {
/* 372 */       Arrays.fill((Object[])row, (Object)null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/* 382 */     return (containsRow(rowKey) && containsColumn(columnKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsColumn(Object columnKey) {
/* 391 */     return this.columnKeyToIndex.containsKey(columnKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsRow(Object rowKey) {
/* 400 */     return this.rowKeyToIndex.containsKey(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 405 */     for (V[] row : this.array) {
/* 406 */       for (V element : row) {
/* 407 */         if (Objects.equal(value, element)) {
/* 408 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 412 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object rowKey, Object columnKey) {
/* 417 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 418 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 419 */     return (rowIndex == null || columnIndex == null) ? null : at(rowIndex.intValue(), columnIndex.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 427 */     return (this.rowList.isEmpty() || this.columnList.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, V value) {
/* 439 */     Preconditions.checkNotNull(rowKey);
/* 440 */     Preconditions.checkNotNull(columnKey);
/* 441 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 442 */     Preconditions.checkArgument((rowIndex != null), "Row %s not in %s", rowKey, this.rowList);
/* 443 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 444 */     Preconditions.checkArgument((columnIndex != null), "Column %s not in %s", columnKey, this.columnList);
/* 445 */     return set(rowIndex.intValue(), columnIndex.intValue(), value);
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
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 465 */     super.putAll(table);
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
/*     */   public V remove(Object rowKey, Object columnKey) {
/* 478 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public V erase(Object rowKey, Object columnKey) {
/* 496 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 497 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 498 */     if (rowIndex == null || columnIndex == null) {
/* 499 */       return null;
/*     */     }
/* 501 */     return set(rowIndex.intValue(), columnIndex.intValue(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 508 */     return this.rowList.size() * this.columnList.size();
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
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 524 */     return super.cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 529 */     return new AbstractIndexedListIterator<Table.Cell<R, C, V>>(size())
/*     */       {
/*     */         protected Table.Cell<R, C, V> get(int index) {
/* 532 */           return ArrayTable.this.getCell(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 539 */     return CollectSpliterators.indexed(
/* 540 */         size(), 273, this::getCell);
/*     */   }
/*     */   
/*     */   private Table.Cell<R, C, V> getCell(final int index) {
/* 544 */     return new Tables.AbstractCell<R, C, V>() {
/* 545 */         final int rowIndex = index / ArrayTable.this.columnList.size();
/* 546 */         final int columnIndex = index % ArrayTable.this.columnList.size();
/*     */ 
/*     */         
/*     */         public R getRowKey() {
/* 550 */           return (R)ArrayTable.this.rowList.get(this.rowIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         public C getColumnKey() {
/* 555 */           return (C)ArrayTable.this.columnList.get(this.columnIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         public V getValue() {
/* 560 */           return (V)ArrayTable.this.at(this.rowIndex, this.columnIndex);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private V getValue(int index) {
/* 566 */     int rowIndex = index / this.columnList.size();
/* 567 */     int columnIndex = index % this.columnList.size();
/* 568 */     return at(rowIndex, columnIndex);
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
/*     */   public Map<R, V> column(C columnKey) {
/* 584 */     Preconditions.checkNotNull(columnKey);
/* 585 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 586 */     return (columnIndex == null) ? ImmutableMap.<R, V>of() : new Column(columnIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Column extends ArrayMap<R, V> {
/*     */     final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 593 */       super(ArrayTable.this.rowKeyToIndex);
/* 594 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 599 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int index) {
/* 604 */       return (V)ArrayTable.this.at(index, this.columnIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     V setValue(int index, V newValue) {
/* 609 */       return (V)ArrayTable.this.set(index, this.columnIndex, newValue);
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
/*     */   public ImmutableSet<C> columnKeySet() {
/* 621 */     return this.columnKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 628 */     ColumnMap map = this.columnMap;
/* 629 */     return (map == null) ? (this.columnMap = new ColumnMap()) : map;
/*     */   }
/*     */   
/*     */   private class ColumnMap
/*     */     extends ArrayMap<C, Map<R, V>> {
/*     */     private ColumnMap() {
/* 635 */       super(ArrayTable.this.columnKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 640 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> getValue(int index) {
/* 645 */       return new ArrayTable.Column(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> setValue(int index, Map<R, V> newValue) {
/* 650 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> put(C key, Map<R, V> value) {
/* 655 */       throw new UnsupportedOperationException();
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
/*     */   public Map<C, V> row(R rowKey) {
/* 672 */     Preconditions.checkNotNull(rowKey);
/* 673 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 674 */     return (rowIndex == null) ? ImmutableMap.<C, V>of() : new Row(rowIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Row extends ArrayMap<C, V> {
/*     */     final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 681 */       super(ArrayTable.this.columnKeyToIndex);
/* 682 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 687 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int index) {
/* 692 */       return (V)ArrayTable.this.at(this.rowIndex, index);
/*     */     }
/*     */ 
/*     */     
/*     */     V setValue(int index, V newValue) {
/* 697 */       return (V)ArrayTable.this.set(this.rowIndex, index, newValue);
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
/*     */   public ImmutableSet<R> rowKeySet() {
/* 709 */     return this.rowKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 716 */     RowMap map = this.rowMap;
/* 717 */     return (map == null) ? (this.rowMap = new RowMap()) : map;
/*     */   }
/*     */   
/*     */   private class RowMap
/*     */     extends ArrayMap<R, Map<C, V>> {
/*     */     private RowMap() {
/* 723 */       super(ArrayTable.this.rowKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 728 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> getValue(int index) {
/* 733 */       return new ArrayTable.Row(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> setValue(int index, Map<C, V> newValue) {
/* 738 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> put(R key, Map<C, V> value) {
/* 743 */       throw new UnsupportedOperationException();
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
/*     */   public Collection<V> values() {
/* 758 */     return super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<V> valuesIterator() {
/* 763 */     return new AbstractIndexedListIterator<V>(size())
/*     */       {
/*     */         protected V get(int index) {
/* 766 */           return ArrayTable.this.getValue(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<V> valuesSpliterator() {
/* 773 */     return CollectSpliterators.indexed(size(), 16, this::getValue);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ArrayTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */