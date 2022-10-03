/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingMap<K, V>
/*     */   extends ForwardingObject
/*     */   implements Map<K, V>
/*     */ {
/*     */   public int size() {
/*  69 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  74 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object object) {
/*  80 */     return delegate().remove(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  85 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  90 */     return delegate().containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  95 */     return delegate().containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 100 */     return delegate().get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, V value) {
/* 106 */     return delegate().put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 111 */     delegate().putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 116 */     return delegate().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 121 */     return delegate().values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 126 */     return delegate().entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 131 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 136 */     return delegate().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void standardPutAll(Map<? extends K, ? extends V> map) {
/* 147 */     Maps.putAllImpl(this, map);
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
/*     */   @Beta
/*     */   protected V standardRemove(Object key) {
/* 162 */     Iterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
/* 163 */     while (entryIterator.hasNext()) {
/* 164 */       Map.Entry<K, V> entry = entryIterator.next();
/* 165 */       if (Objects.equal(entry.getKey(), key)) {
/* 166 */         V value = entry.getValue();
/* 167 */         entryIterator.remove();
/* 168 */         return value;
/*     */       } 
/*     */     } 
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void standardClear() {
/* 182 */     Iterators.clear(entrySet().iterator());
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
/*     */   @Beta
/*     */   protected class StandardKeySet
/*     */     extends Maps.KeySet<K, V>
/*     */   {
/*     */     public StandardKeySet() {
/* 198 */       super(ForwardingMap.this);
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
/*     */   @Beta
/*     */   protected boolean standardContainsKey(Object key) {
/* 211 */     return Maps.containsKeyImpl(this, key);
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
/*     */   @Beta
/*     */   protected class StandardValues
/*     */     extends Maps.Values<K, V>
/*     */   {
/*     */     public StandardValues() {
/* 227 */       super(ForwardingMap.this);
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
/*     */   protected boolean standardContainsValue(Object value) {
/* 239 */     return Maps.containsValueImpl(this, value);
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
/*     */   @Beta
/*     */   protected abstract class StandardEntrySet
/*     */     extends Maps.EntrySet<K, V>
/*     */   {
/*     */     Map<K, V> map() {
/* 258 */       return ForwardingMap.this;
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
/*     */   protected boolean standardIsEmpty() {
/* 270 */     return !entrySet().iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardEquals(Object object) {
/* 281 */     return Maps.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardHashCode() {
/* 292 */     return Sets.hashCodeImpl(entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String standardToString() {
/* 303 */     return Maps.toStringImpl(this);
/*     */   }
/*     */   
/*     */   protected abstract Map<K, V> delegate();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */