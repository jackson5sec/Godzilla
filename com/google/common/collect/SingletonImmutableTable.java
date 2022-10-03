/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ class SingletonImmutableTable<R, C, V>
/*    */   extends ImmutableTable<R, C, V>
/*    */ {
/*    */   final R singleRowKey;
/*    */   final C singleColumnKey;
/*    */   final V singleValue;
/*    */   
/*    */   SingletonImmutableTable(R rowKey, C columnKey, V value) {
/* 36 */     this.singleRowKey = (R)Preconditions.checkNotNull(rowKey);
/* 37 */     this.singleColumnKey = (C)Preconditions.checkNotNull(columnKey);
/* 38 */     this.singleValue = (V)Preconditions.checkNotNull(value);
/*    */   }
/*    */   
/*    */   SingletonImmutableTable(Table.Cell<R, C, V> cell) {
/* 42 */     this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableMap<R, V> column(C columnKey) {
/* 47 */     Preconditions.checkNotNull(columnKey);
/* 48 */     return containsColumn(columnKey) ? 
/* 49 */       ImmutableMap.<R, V>of(this.singleRowKey, this.singleValue) : 
/* 50 */       ImmutableMap.<R, V>of();
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableMap<C, Map<R, V>> columnMap() {
/* 55 */     return ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableMap<R, Map<C, V>> rowMap() {
/* 60 */     return ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 65 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
/* 70 */     return ImmutableSet.of(cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableCollection<V> createValues() {
/* 75 */     return ImmutableSet.of(this.singleValue);
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableTable.SerializedForm createSerializedForm() {
/* 80 */     return ImmutableTable.SerializedForm.create(this, new int[] { 0 }, new int[] { 0 });
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\SingletonImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */