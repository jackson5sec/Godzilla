/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class RegularImmutableTable<R, C, V>
/*     */   extends ImmutableTable<R, C, V>
/*     */ {
/*     */   final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
/*  42 */     return isEmpty() ? ImmutableSet.<Table.Cell<R, C, V>>of() : new CellSet();
/*     */   }
/*     */   
/*     */   private final class CellSet extends IndexedImmutableSet<Table.Cell<R, C, V>> {
/*     */     private CellSet() {}
/*     */     
/*     */     public int size() {
/*  49 */       return RegularImmutableTable.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Table.Cell<R, C, V> get(int index) {
/*  54 */       return RegularImmutableTable.this.getCell(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/*  59 */       if (object instanceof Table.Cell) {
/*  60 */         Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)object;
/*  61 */         Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
/*  62 */         return (value != null && value.equals(cell.getValue()));
/*     */       } 
/*  64 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/*  69 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final ImmutableCollection<V> createValues() {
/*  77 */     return isEmpty() ? ImmutableList.<V>of() : new Values();
/*     */   }
/*     */   
/*     */   private final class Values extends ImmutableList<V> {
/*     */     private Values() {}
/*     */     
/*     */     public int size() {
/*  84 */       return RegularImmutableTable.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(int index) {
/*  89 */       return (V)RegularImmutableTable.this.getValue(index);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/*  94 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, final Comparator<? super R> rowComparator, final Comparator<? super C> columnComparator) {
/* 102 */     Preconditions.checkNotNull(cells);
/* 103 */     if (rowComparator != null || columnComparator != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 111 */       Comparator<Table.Cell<R, C, V>> comparator = new Comparator<Table.Cell<R, C, V>>()
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2)
/*     */           {
/* 118 */             int rowCompare = (rowComparator == null) ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
/* 119 */             if (rowCompare != 0) {
/* 120 */               return rowCompare;
/*     */             }
/* 122 */             return (columnComparator == null) ? 0 : columnComparator
/*     */               
/* 124 */               .compare(cell1.getColumnKey(), cell2.getColumnKey());
/*     */           }
/*     */         };
/* 127 */       Collections.sort(cells, comparator);
/*     */     } 
/* 129 */     return forCellsInternal(cells, rowComparator, columnComparator);
/*     */   }
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
/* 133 */     return forCellsInternal(cells, (Comparator<? super R>)null, (Comparator<? super C>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
/* 140 */     Set<R> rowSpaceBuilder = new LinkedHashSet<>();
/* 141 */     Set<C> columnSpaceBuilder = new LinkedHashSet<>();
/* 142 */     ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
/* 143 */     for (Table.Cell<R, C, V> cell : cells) {
/* 144 */       rowSpaceBuilder.add(cell.getRowKey());
/* 145 */       columnSpaceBuilder.add(cell.getColumnKey());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     ImmutableSet<R> rowSpace = (rowComparator == null) ? ImmutableSet.<R>copyOf(rowSpaceBuilder) : ImmutableSet.<R>copyOf(ImmutableList.sortedCopyOf(rowComparator, rowSpaceBuilder));
/*     */ 
/*     */ 
/*     */     
/* 155 */     ImmutableSet<C> columnSpace = (columnComparator == null) ? ImmutableSet.<C>copyOf(columnSpaceBuilder) : ImmutableSet.<C>copyOf(ImmutableList.sortedCopyOf(columnComparator, columnSpaceBuilder));
/*     */     
/* 157 */     return forOrderedComponents(cellList, rowSpace, columnSpace);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
/* 167 */     return (cellList.size() > rowSpace.size() * columnSpace.size() / 2L) ? new DenseImmutableTable<>(cellList, rowSpace, columnSpace) : new SparseImmutableTable<>(cellList, rowSpace, columnSpace);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void checkNoDuplicate(R rowKey, C columnKey, V existingValue, V newValue) {
/* 178 */     Preconditions.checkArgument((existingValue == null), "Duplicate key: (row=%s, column=%s), values: [%s, %s].", rowKey, columnKey, newValue, existingValue);
/*     */   }
/*     */   
/*     */   abstract Table.Cell<R, C, V> getCell(int paramInt);
/*     */   
/*     */   abstract V getValue(int paramInt);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\RegularImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */