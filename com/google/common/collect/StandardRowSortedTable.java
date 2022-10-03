/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class StandardRowSortedTable<R, C, V>
/*     */   extends StandardTable<R, C, V>
/*     */   implements RowSortedTable<R, C, V>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   StandardRowSortedTable(SortedMap<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/*  58 */     super(backingMap, factory);
/*     */   }
/*     */   
/*     */   private SortedMap<R, Map<C, V>> sortedBackingMap() {
/*  62 */     return (SortedMap<R, Map<C, V>>)this.backingMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<R> rowKeySet() {
/*  73 */     return (SortedSet<R>)rowMap().keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMap<R, Map<C, V>> rowMap() {
/*  84 */     return (SortedMap<R, Map<C, V>>)super.rowMap();
/*     */   }
/*     */ 
/*     */   
/*     */   SortedMap<R, Map<C, V>> createRowMap() {
/*  89 */     return new RowSortedMap();
/*     */   }
/*     */   
/*     */   private class RowSortedMap extends StandardTable<R, C, V>.RowMap implements SortedMap<R, Map<C, V>> {
/*     */     private RowSortedMap() {}
/*     */     
/*     */     public SortedSet<R> keySet() {
/*  96 */       return (SortedSet<R>)super.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     SortedSet<R> createKeySet() {
/* 101 */       return new Maps.SortedKeySet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super R> comparator() {
/* 106 */       return StandardRowSortedTable.this.sortedBackingMap().comparator();
/*     */     }
/*     */ 
/*     */     
/*     */     public R firstKey() {
/* 111 */       return (R)StandardRowSortedTable.this.sortedBackingMap().firstKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public R lastKey() {
/* 116 */       return (R)StandardRowSortedTable.this.sortedBackingMap().lastKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> headMap(R toKey) {
/* 121 */       Preconditions.checkNotNull(toKey);
/* 122 */       return (new StandardRowSortedTable<>(StandardRowSortedTable.this.sortedBackingMap().headMap(toKey), StandardRowSortedTable.this.factory))
/* 123 */         .rowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> subMap(R fromKey, R toKey) {
/* 128 */       Preconditions.checkNotNull(fromKey);
/* 129 */       Preconditions.checkNotNull(toKey);
/* 130 */       return (new StandardRowSortedTable<>(StandardRowSortedTable.this.sortedBackingMap().subMap(fromKey, toKey), StandardRowSortedTable.this.factory))
/* 131 */         .rowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> tailMap(R fromKey) {
/* 136 */       Preconditions.checkNotNull(fromKey);
/* 137 */       return (new StandardRowSortedTable<>(StandardRowSortedTable.this.sortedBackingMap().tailMap(fromKey), StandardRowSortedTable.this.factory))
/* 138 */         .rowMap();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\StandardRowSortedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */