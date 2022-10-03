/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractTable<R, C, V>
/*     */   implements Table<R, C, V>
/*     */ {
/*     */   private transient Set<Table.Cell<R, C, V>> cellSet;
/*     */   private transient Collection<V> values;
/*     */   
/*     */   public boolean containsRow(Object rowKey) {
/*  40 */     return Maps.safeContainsKey(rowMap(), rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(Object columnKey) {
/*  45 */     return Maps.safeContainsKey(columnMap(), columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<R> rowKeySet() {
/*  50 */     return rowMap().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<C> columnKeySet() {
/*  55 */     return columnMap().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  60 */     for (Map<C, V> row : rowMap().values()) {
/*  61 */       if (row.containsValue(value)) {
/*  62 */         return true;
/*     */       }
/*     */     } 
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/*  70 */     Map<C, V> row = Maps.<Map<C, V>>safeGet(rowMap(), rowKey);
/*  71 */     return (row != null && Maps.safeContainsKey(row, columnKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object rowKey, Object columnKey) {
/*  76 */     Map<C, V> row = Maps.<Map<C, V>>safeGet(rowMap(), rowKey);
/*  77 */     return (row == null) ? null : Maps.<V>safeGet(row, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  82 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  87 */     Iterators.clear(cellSet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object rowKey, Object columnKey) {
/*  93 */     Map<C, V> row = Maps.<Map<C, V>>safeGet(rowMap(), rowKey);
/*  94 */     return (row == null) ? null : Maps.<V>safeRemove(row, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, V value) {
/* 100 */     return row(rowKey).put(columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 105 */     for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
/* 106 */       put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 114 */     Set<Table.Cell<R, C, V>> result = this.cellSet;
/* 115 */     return (result == null) ? (this.cellSet = createCellSet()) : result;
/*     */   }
/*     */   
/*     */   Set<Table.Cell<R, C, V>> createCellSet() {
/* 119 */     return new CellSet();
/*     */   }
/*     */   
/*     */   abstract Iterator<Table.Cell<R, C, V>> cellIterator();
/*     */   
/*     */   abstract Spliterator<Table.Cell<R, C, V>> cellSpliterator();
/*     */   
/*     */   class CellSet
/*     */     extends AbstractSet<Table.Cell<R, C, V>>
/*     */   {
/*     */     public boolean contains(Object o) {
/* 130 */       if (o instanceof Table.Cell) {
/* 131 */         Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)o;
/* 132 */         Map<C, V> row = Maps.<Map<C, V>>safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
/* 133 */         return (row != null && 
/* 134 */           Collections2.safeContains(row
/* 135 */             .entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue())));
/*     */       } 
/* 137 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 142 */       if (o instanceof Table.Cell) {
/* 143 */         Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>)o;
/* 144 */         Map<C, V> row = Maps.<Map<C, V>>safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
/* 145 */         return (row != null && 
/* 146 */           Collections2.safeRemove(row
/* 147 */             .entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue())));
/*     */       } 
/* 149 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 154 */       AbstractTable.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Table.Cell<R, C, V>> iterator() {
/* 159 */       return AbstractTable.this.cellIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Table.Cell<R, C, V>> spliterator() {
/* 164 */       return AbstractTable.this.cellSpliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 169 */       return AbstractTable.this.size();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 177 */     Collection<V> result = this.values;
/* 178 */     return (result == null) ? (this.values = createValues()) : result;
/*     */   }
/*     */   
/*     */   Collection<V> createValues() {
/* 182 */     return new Values();
/*     */   }
/*     */   
/*     */   Iterator<V> valuesIterator() {
/* 186 */     return new TransformedIterator<Table.Cell<R, C, V>, V>(cellSet().iterator())
/*     */       {
/*     */         V transform(Table.Cell<R, C, V> cell) {
/* 189 */           return cell.getValue();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   Spliterator<V> valuesSpliterator() {
/* 195 */     return CollectSpliterators.map(cellSpliterator(), Table.Cell::getValue);
/*     */   }
/*     */   
/*     */   class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/*     */     public Iterator<V> iterator() {
/* 202 */       return AbstractTable.this.valuesIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<V> spliterator() {
/* 207 */       return AbstractTable.this.valuesSpliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 212 */       return AbstractTable.this.containsValue(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 217 */       AbstractTable.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 222 */       return AbstractTable.this.size();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 228 */     return Tables.equalsImpl(this, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 233 */     return cellSet().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 239 */     return rowMap().toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */