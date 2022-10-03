/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable(containerOf = {"R", "C", "V"})
/*     */ @GwtCompatible
/*     */ final class DenseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
/*     */   private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
/*     */   private final int[] rowCounts;
/*     */   private final int[] columnCounts;
/*     */   private final V[][] values;
/*     */   private final int[] cellRowIndices;
/*     */   private final int[] cellColumnIndices;
/*     */   
/*     */   DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
/*  55 */     V[][] array = (V[][])new Object[rowSpace.size()][columnSpace.size()];
/*  56 */     this.values = array;
/*  57 */     this.rowKeyToIndex = Maps.indexMap(rowSpace);
/*  58 */     this.columnKeyToIndex = Maps.indexMap(columnSpace);
/*  59 */     this.rowCounts = new int[this.rowKeyToIndex.size()];
/*  60 */     this.columnCounts = new int[this.columnKeyToIndex.size()];
/*  61 */     int[] cellRowIndices = new int[cellList.size()];
/*  62 */     int[] cellColumnIndices = new int[cellList.size()];
/*  63 */     for (int i = 0; i < cellList.size(); i++) {
/*  64 */       Table.Cell<R, C, V> cell = cellList.get(i);
/*  65 */       R rowKey = cell.getRowKey();
/*  66 */       C columnKey = cell.getColumnKey();
/*  67 */       int rowIndex = ((Integer)this.rowKeyToIndex.get(rowKey)).intValue();
/*  68 */       int columnIndex = ((Integer)this.columnKeyToIndex.get(columnKey)).intValue();
/*  69 */       V existingValue = this.values[rowIndex][columnIndex];
/*  70 */       checkNoDuplicate(rowKey, columnKey, existingValue, cell.getValue());
/*  71 */       this.values[rowIndex][columnIndex] = cell.getValue();
/*  72 */       this.rowCounts[rowIndex] = this.rowCounts[rowIndex] + 1;
/*  73 */       this.columnCounts[columnIndex] = this.columnCounts[columnIndex] + 1;
/*  74 */       cellRowIndices[i] = rowIndex;
/*  75 */       cellColumnIndices[i] = columnIndex;
/*     */     } 
/*  77 */     this.cellRowIndices = cellRowIndices;
/*  78 */     this.cellColumnIndices = cellColumnIndices;
/*  79 */     this.rowMap = new RowMap();
/*  80 */     this.columnMap = new ColumnMap();
/*     */   }
/*     */   
/*     */   private static abstract class ImmutableArrayMap<K, V>
/*     */     extends ImmutableMap.IteratorBasedImmutableMap<K, V> {
/*     */     private final int size;
/*     */     
/*     */     ImmutableArrayMap(int size) {
/*  88 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract ImmutableMap<K, Integer> keyToIndex();
/*     */     
/*     */     private boolean isFull() {
/*  95 */       return (this.size == keyToIndex().size());
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/*  99 */       return keyToIndex().keySet().asList().get(index);
/*     */     }
/*     */ 
/*     */     
/*     */     abstract V getValue(int param1Int);
/*     */     
/*     */     ImmutableSet<K> createKeySet() {
/* 106 */       return isFull() ? keyToIndex().keySet() : super.createKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 111 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 116 */       Integer keyIndex = keyToIndex().get(key);
/* 117 */       return (keyIndex == null) ? null : getValue(keyIndex.intValue());
/*     */     }
/*     */ 
/*     */     
/*     */     UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/* 122 */       return new AbstractIterator<Map.Entry<K, V>>() {
/* 123 */           private int index = -1;
/* 124 */           private final int maxIndex = DenseImmutableTable.ImmutableArrayMap.this.keyToIndex().size();
/*     */ 
/*     */           
/*     */           protected Map.Entry<K, V> computeNext() {
/* 128 */             this.index++; for (; this.index < this.maxIndex; this.index++) {
/* 129 */               V value = (V)DenseImmutableTable.ImmutableArrayMap.this.getValue(this.index);
/* 130 */               if (value != null) {
/* 131 */                 return Maps.immutableEntry((K)DenseImmutableTable.ImmutableArrayMap.this.getKey(this.index), value);
/*     */               }
/*     */             } 
/* 134 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Row extends ImmutableArrayMap<C, V> {
/*     */     private final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 144 */       super(DenseImmutableTable.this.rowCounts[rowIndex]);
/* 145 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex() {
/* 150 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int keyIndex) {
/* 155 */       return (V)DenseImmutableTable.this.values[this.rowIndex][keyIndex];
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 160 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Column extends ImmutableArrayMap<R, V> {
/*     */     private final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 168 */       super(DenseImmutableTable.this.columnCounts[columnIndex]);
/* 169 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex() {
/* 174 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int keyIndex) {
/* 179 */       return (V)DenseImmutableTable.this.values[keyIndex][this.columnIndex];
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 184 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class RowMap
/*     */     extends ImmutableArrayMap<R, ImmutableMap<C, V>> {
/*     */     private RowMap() {
/* 191 */       super(DenseImmutableTable.this.rowCounts.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex() {
/* 196 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, V> getValue(int keyIndex) {
/* 201 */       return new DenseImmutableTable.Row(keyIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 206 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ColumnMap
/*     */     extends ImmutableArrayMap<C, ImmutableMap<R, V>> {
/*     */     private ColumnMap() {
/* 213 */       super(DenseImmutableTable.this.columnCounts.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex() {
/* 218 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, V> getValue(int keyIndex) {
/* 223 */       return new DenseImmutableTable.Column(keyIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 228 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<C, Map<R, V>> columnMap() {
/* 235 */     ImmutableMap<C, ImmutableMap<R, V>> columnMap = this.columnMap;
/* 236 */     return ImmutableMap.copyOf((Map)columnMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, Map<C, V>> rowMap() {
/* 242 */     ImmutableMap<R, ImmutableMap<C, V>> rowMap = this.rowMap;
/* 243 */     return ImmutableMap.copyOf((Map)rowMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object rowKey, Object columnKey) {
/* 248 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 249 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 250 */     return (rowIndex == null || columnIndex == null) ? null : this.values[rowIndex.intValue()][columnIndex.intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 255 */     return this.cellRowIndices.length;
/*     */   }
/*     */ 
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index) {
/* 260 */     int rowIndex = this.cellRowIndices[index];
/* 261 */     int columnIndex = this.cellColumnIndices[index];
/* 262 */     R rowKey = rowKeySet().asList().get(rowIndex);
/* 263 */     C columnKey = columnKeySet().asList().get(columnIndex);
/* 264 */     V value = this.values[rowIndex][columnIndex];
/* 265 */     return cellOf(rowKey, columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   V getValue(int index) {
/* 270 */     return this.values[this.cellRowIndices[index]][this.cellColumnIndices[index]];
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableTable.SerializedForm createSerializedForm() {
/* 275 */     return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, this.cellColumnIndices);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\DenseImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */