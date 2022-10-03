/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingTable<R, C, V>
/*     */   extends ForwardingObject
/*     */   implements Table<R, C, V>
/*     */ {
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/*  43 */     return delegate().cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  48 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<R, V> column(C columnKey) {
/*  53 */     return delegate().column(columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<C> columnKeySet() {
/*  58 */     return delegate().columnKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/*  63 */     return delegate().columnMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/*  68 */     return delegate().contains(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(Object columnKey) {
/*  73 */     return delegate().containsColumn(columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsRow(Object rowKey) {
/*  78 */     return delegate().containsRow(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  83 */     return delegate().containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object rowKey, Object columnKey) {
/*  88 */     return delegate().get(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  93 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, V value) {
/*  99 */     return delegate().put(rowKey, columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 104 */     delegate().putAll(table);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object rowKey, Object columnKey) {
/* 110 */     return delegate().remove(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<C, V> row(R rowKey) {
/* 115 */     return delegate().row(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<R> rowKeySet() {
/* 120 */     return delegate().rowKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 125 */     return delegate().rowMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 130 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 135 */     return delegate().values();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 140 */     return (obj == this || delegate().equals(obj));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 145 */     return delegate().hashCode();
/*     */   }
/*     */   
/*     */   protected abstract Table<R, C, V> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */