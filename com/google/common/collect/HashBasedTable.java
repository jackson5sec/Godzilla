/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ public class HashBasedTable<R, C, V>
/*     */   extends StandardTable<R, C, V>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static class Factory<C, V>
/*     */     implements Supplier<Map<C, V>>, Serializable
/*     */   {
/*     */     final int expectedSize;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     Factory(int expectedSize) {
/*  57 */       this.expectedSize = expectedSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> get() {
/*  62 */       return Maps.newLinkedHashMapWithExpectedSize(this.expectedSize);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> HashBasedTable<R, C, V> create() {
/*  70 */     return new HashBasedTable<>(new LinkedHashMap<>(), new Factory<>(0));
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
/*     */   public static <R, C, V> HashBasedTable<R, C, V> create(int expectedRows, int expectedCellsPerRow) {
/*  83 */     CollectPreconditions.checkNonnegative(expectedCellsPerRow, "expectedCellsPerRow");
/*  84 */     Map<R, Map<C, V>> backingMap = Maps.newLinkedHashMapWithExpectedSize(expectedRows);
/*  85 */     return new HashBasedTable<>(backingMap, new Factory<>(expectedCellsPerRow));
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
/*     */   public static <R, C, V> HashBasedTable<R, C, V> create(Table<? extends R, ? extends C, ? extends V> table) {
/*  97 */     HashBasedTable<R, C, V> result = create();
/*  98 */     result.putAll(table);
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   HashBasedTable(Map<R, Map<C, V>> backingMap, Factory<C, V> factory) {
/* 103 */     super(backingMap, factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/* 110 */     return super.contains(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(Object columnKey) {
/* 115 */     return super.containsColumn(columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsRow(Object rowKey) {
/* 120 */     return super.containsRow(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 125 */     return super.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object rowKey, Object columnKey) {
/* 130 */     return super.get(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 135 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object rowKey, Object columnKey) {
/* 141 */     return super.remove(rowKey, columnKey);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\HashBasedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */