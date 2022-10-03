/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.LinkedHashMap;
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
/*     */ @Immutable(containerOf = {"R", "C", "V"})
/*     */ @GwtCompatible
/*     */ final class SparseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*  27 */   static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(
/*     */       
/*  29 */       ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
/*     */ 
/*     */   
/*     */   private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
/*     */ 
/*     */   
/*     */   private final int[] cellRowIndices;
/*     */ 
/*     */   
/*     */   private final int[] cellColumnInRowIndices;
/*     */ 
/*     */ 
/*     */   
/*     */   SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
/*  47 */     Map<R, Integer> rowIndex = Maps.indexMap(rowSpace);
/*  48 */     Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
/*  49 */     for (UnmodifiableIterator<R> unmodifiableIterator = rowSpace.iterator(); unmodifiableIterator.hasNext(); ) { R row = unmodifiableIterator.next();
/*  50 */       rows.put(row, new LinkedHashMap<>()); }
/*     */     
/*  52 */     Map<C, Map<R, V>> columns = Maps.newLinkedHashMap();
/*  53 */     for (UnmodifiableIterator<C> unmodifiableIterator1 = columnSpace.iterator(); unmodifiableIterator1.hasNext(); ) { C col = unmodifiableIterator1.next();
/*  54 */       columns.put(col, new LinkedHashMap<>()); }
/*     */     
/*  56 */     int[] cellRowIndices = new int[cellList.size()];
/*  57 */     int[] cellColumnInRowIndices = new int[cellList.size()];
/*  58 */     for (int i = 0; i < cellList.size(); i++) {
/*  59 */       Table.Cell<R, C, V> cell = cellList.get(i);
/*  60 */       R rowKey = cell.getRowKey();
/*  61 */       C columnKey = cell.getColumnKey();
/*  62 */       V value = cell.getValue();
/*     */       
/*  64 */       cellRowIndices[i] = ((Integer)rowIndex.get(rowKey)).intValue();
/*  65 */       Map<C, V> thisRow = rows.get(rowKey);
/*  66 */       cellColumnInRowIndices[i] = thisRow.size();
/*  67 */       V oldValue = thisRow.put(columnKey, value);
/*  68 */       checkNoDuplicate(rowKey, columnKey, oldValue, value);
/*  69 */       ((Map<R, V>)columns.get(columnKey)).put(rowKey, value);
/*     */     } 
/*  71 */     this.cellRowIndices = cellRowIndices;
/*  72 */     this.cellColumnInRowIndices = cellColumnInRowIndices;
/*     */     
/*  74 */     ImmutableMap.Builder<R, ImmutableMap<C, V>> rowBuilder = new ImmutableMap.Builder<>(rows.size());
/*  75 */     for (Map.Entry<R, Map<C, V>> row : rows.entrySet()) {
/*  76 */       rowBuilder.put(row.getKey(), ImmutableMap.copyOf(row.getValue()));
/*     */     }
/*  78 */     this.rowMap = rowBuilder.build();
/*     */ 
/*     */     
/*  81 */     ImmutableMap.Builder<C, ImmutableMap<R, V>> columnBuilder = new ImmutableMap.Builder<>(columns.size());
/*  82 */     for (Map.Entry<C, Map<R, V>> col : columns.entrySet()) {
/*  83 */       columnBuilder.put(col.getKey(), ImmutableMap.copyOf(col.getValue()));
/*     */     }
/*  85 */     this.columnMap = columnBuilder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<C, Map<R, V>> columnMap() {
/*  91 */     ImmutableMap<C, ImmutableMap<R, V>> columnMap = this.columnMap;
/*  92 */     return ImmutableMap.copyOf((Map)columnMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, Map<C, V>> rowMap() {
/*  98 */     ImmutableMap<R, ImmutableMap<C, V>> rowMap = this.rowMap;
/*  99 */     return ImmutableMap.copyOf((Map)rowMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 104 */     return this.cellRowIndices.length;
/*     */   }
/*     */ 
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index) {
/* 109 */     int rowIndex = this.cellRowIndices[index];
/* 110 */     Map.Entry<R, ImmutableMap<C, V>> rowEntry = this.rowMap.entrySet().asList().get(rowIndex);
/* 111 */     ImmutableMap<C, V> row = rowEntry.getValue();
/* 112 */     int columnIndex = this.cellColumnInRowIndices[index];
/* 113 */     Map.Entry<C, V> colEntry = row.entrySet().asList().get(columnIndex);
/* 114 */     return cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   V getValue(int index) {
/* 119 */     int rowIndex = this.cellRowIndices[index];
/* 120 */     ImmutableMap<C, V> row = this.rowMap.values().asList().get(rowIndex);
/* 121 */     int columnIndex = this.cellColumnInRowIndices[index];
/* 122 */     return row.values().asList().get(columnIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableTable.SerializedForm createSerializedForm() {
/* 127 */     Map<C, Integer> columnKeyToIndex = Maps.indexMap(columnKeySet());
/* 128 */     int[] cellColumnIndices = new int[cellSet().size()];
/* 129 */     int i = 0;
/* 130 */     for (UnmodifiableIterator<Table.Cell<R, C, V>> unmodifiableIterator = cellSet().iterator(); unmodifiableIterator.hasNext(); ) { Table.Cell<R, C, V> cell = unmodifiableIterator.next();
/* 131 */       cellColumnIndices[i++] = ((Integer)columnKeyToIndex.get(cell.getColumnKey())).intValue(); }
/*     */     
/* 133 */     return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, cellColumnIndices);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SparseImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */