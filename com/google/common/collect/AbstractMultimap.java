/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AbstractMultimap<K, V>
/*     */   implements Multimap<K, V>
/*     */ {
/*     */   private transient Collection<Map.Entry<K, V>> entries;
/*     */   private transient Set<K> keySet;
/*     */   private transient Multiset<K> keys;
/*     */   private transient Collection<V> values;
/*     */   private transient Map<K, Collection<V>> asMap;
/*     */   
/*     */   public boolean isEmpty() {
/*  44 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  49 */     for (Collection<V> collection : asMap().values()) {
/*  50 */       if (collection.contains(value)) {
/*  51 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  55 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsEntry(Object key, Object value) {
/*  60 */     Collection<V> collection = asMap().get(key);
/*  61 */     return (collection != null && collection.contains(value));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(Object key, Object value) {
/*  67 */     Collection<V> collection = asMap().get(key);
/*  68 */     return (collection != null && collection.remove(value));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(K key, V value) {
/*  74 */     return get(key).add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/*  80 */     Preconditions.checkNotNull(values);
/*     */ 
/*     */     
/*  83 */     if (values instanceof Collection) {
/*  84 */       Collection<? extends V> valueCollection = (Collection<? extends V>)values;
/*  85 */       return (!valueCollection.isEmpty() && get(key).addAll(valueCollection));
/*     */     } 
/*  87 */     Iterator<? extends V> valueItr = values.iterator();
/*  88 */     return (valueItr.hasNext() && Iterators.addAll(get(key), valueItr));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  95 */     boolean changed = false;
/*  96 */     for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
/*  97 */       changed |= put(entry.getKey(), entry.getValue());
/*     */     }
/*  99 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/* 105 */     Preconditions.checkNotNull(values);
/* 106 */     Collection<V> result = removeAll(key);
/* 107 */     putAll(key, values);
/* 108 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries() {
/* 115 */     Collection<Map.Entry<K, V>> result = this.entries;
/* 116 */     return (result == null) ? (this.entries = createEntries()) : result;
/*     */   }
/*     */   
/*     */   abstract Collection<Map.Entry<K, V>> createEntries();
/*     */   
/*     */   abstract Iterator<Map.Entry<K, V>> entryIterator();
/*     */   
/*     */   class Entries extends Multimaps.Entries<K, V> {
/*     */     Multimap<K, V> multimap() {
/* 125 */       return AbstractMultimap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 130 */       return AbstractMultimap.this.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Map.Entry<K, V>> spliterator() {
/* 135 */       return AbstractMultimap.this.entrySpliterator();
/*     */     }
/*     */   }
/*     */   
/*     */   class EntrySet
/*     */     extends Entries
/*     */     implements Set<Map.Entry<K, V>> {
/*     */     public int hashCode() {
/* 143 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 148 */       return Sets.equalsImpl(this, obj);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 155 */     return Spliterators.spliterator(
/* 156 */         entryIterator(), size(), (this instanceof SetMultimap) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 163 */     Set<K> result = this.keySet;
/* 164 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Set<K> createKeySet();
/*     */ 
/*     */   
/*     */   public Multiset<K> keys() {
/* 173 */     Multiset<K> result = this.keys;
/* 174 */     return (result == null) ? (this.keys = createKeys()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Multiset<K> createKeys();
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 183 */     Collection<V> result = this.values;
/* 184 */     return (result == null) ? (this.values = createValues()) : result;
/*     */   }
/*     */   
/*     */   abstract Collection<V> createValues();
/*     */   
/*     */   class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/*     */     public Iterator<V> iterator() {
/* 193 */       return AbstractMultimap.this.valueIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<V> spliterator() {
/* 198 */       return AbstractMultimap.this.valueSpliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 203 */       return AbstractMultimap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 208 */       return AbstractMultimap.this.containsValue(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 213 */       AbstractMultimap.this.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<V> valueIterator() {
/* 218 */     return Maps.valueIterator(entries().iterator());
/*     */   }
/*     */   
/*     */   Spliterator<V> valueSpliterator() {
/* 222 */     return Spliterators.spliterator(valueIterator(), size(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 229 */     Map<K, Collection<V>> result = this.asMap;
/* 230 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Map<K, Collection<V>> createAsMap();
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 239 */     return Multimaps.equalsImpl(this, object);
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
/*     */   public int hashCode() {
/* 252 */     return asMap().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 263 */     return asMap().toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */