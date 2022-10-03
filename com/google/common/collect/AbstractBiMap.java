/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class AbstractBiMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private transient Map<K, V> delegate;
/*     */   @RetainedWith
/*     */   transient AbstractBiMap<V, K> inverse;
/*     */   private transient Set<K> keySet;
/*     */   private transient Set<V> valueSet;
/*     */   private transient Set<Map.Entry<K, V>> entrySet;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   AbstractBiMap(Map<K, V> forward, Map<V, K> backward) {
/*  59 */     setDelegates(forward, backward);
/*     */   }
/*     */ 
/*     */   
/*     */   private AbstractBiMap(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/*  64 */     this.delegate = backward;
/*  65 */     this.inverse = forward;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<K, V> delegate() {
/*  70 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   K checkKey(K key) {
/*  76 */     return key;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   V checkValue(V value) {
/*  82 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setDelegates(Map<K, V> forward, Map<V, K> backward) {
/*  90 */     Preconditions.checkState((this.delegate == null));
/*  91 */     Preconditions.checkState((this.inverse == null));
/*  92 */     Preconditions.checkArgument(forward.isEmpty());
/*  93 */     Preconditions.checkArgument(backward.isEmpty());
/*  94 */     Preconditions.checkArgument((forward != backward));
/*  95 */     this.delegate = forward;
/*  96 */     this.inverse = makeInverse(backward);
/*     */   }
/*     */   
/*     */   AbstractBiMap<V, K> makeInverse(Map<V, K> backward) {
/* 100 */     return new Inverse<>(backward, this);
/*     */   }
/*     */   
/*     */   void setInverse(AbstractBiMap<V, K> inverse) {
/* 104 */     this.inverse = inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 111 */     return this.inverse.containsKey(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, V value) {
/* 119 */     return putInBothMaps(key, value, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(K key, V value) {
/* 125 */     return putInBothMaps(key, value, true);
/*     */   }
/*     */   
/*     */   private V putInBothMaps(K key, V value, boolean force) {
/* 129 */     checkKey(key);
/* 130 */     checkValue(value);
/* 131 */     boolean containedKey = containsKey(key);
/* 132 */     if (containedKey && Objects.equal(value, get(key))) {
/* 133 */       return value;
/*     */     }
/* 135 */     if (force) {
/* 136 */       inverse().remove(value);
/*     */     } else {
/* 138 */       Preconditions.checkArgument(!containsValue(value), "value already present: %s", value);
/*     */     } 
/* 140 */     V oldValue = this.delegate.put(key, value);
/* 141 */     updateInverseMap(key, containedKey, oldValue, value);
/* 142 */     return oldValue;
/*     */   }
/*     */   
/*     */   private void updateInverseMap(K key, boolean containedKey, V oldValue, V newValue) {
/* 146 */     if (containedKey) {
/* 147 */       removeFromInverseMap(oldValue);
/*     */     }
/* 149 */     this.inverse.delegate.put((K)newValue, (V)key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object key) {
/* 155 */     return containsKey(key) ? removeFromBothMaps(key) : null;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private V removeFromBothMaps(Object key) {
/* 160 */     V oldValue = this.delegate.remove(key);
/* 161 */     removeFromInverseMap(oldValue);
/* 162 */     return oldValue;
/*     */   }
/*     */   
/*     */   private void removeFromInverseMap(V oldValue) {
/* 166 */     this.inverse.delegate.remove(oldValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 173 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 174 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 180 */     this.delegate.replaceAll(function);
/* 181 */     this.inverse.delegate.clear();
/* 182 */     Map.Entry<K, V> broken = null;
/* 183 */     Iterator<Map.Entry<K, V>> itr = this.delegate.entrySet().iterator();
/* 184 */     while (itr.hasNext()) {
/* 185 */       Map.Entry<K, V> entry = itr.next();
/* 186 */       K k = entry.getKey();
/* 187 */       V v = entry.getValue();
/* 188 */       K conflict = (K)this.inverse.delegate.putIfAbsent((K)v, (V)k);
/* 189 */       if (conflict != null) {
/* 190 */         broken = entry;
/*     */ 
/*     */         
/* 193 */         itr.remove();
/*     */       } 
/*     */     } 
/* 196 */     if (broken != null) {
/* 197 */       throw new IllegalArgumentException("value already present: " + broken.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 203 */     this.delegate.clear();
/* 204 */     this.inverse.delegate.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 211 */     return this.inverse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 218 */     Set<K> result = this.keySet;
/* 219 */     return (result == null) ? (this.keySet = new KeySet()) : result;
/*     */   }
/*     */   
/*     */   private class KeySet extends ForwardingSet<K> {
/*     */     private KeySet() {}
/*     */     
/*     */     protected Set<K> delegate() {
/* 226 */       return AbstractBiMap.this.delegate.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 231 */       AbstractBiMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object key) {
/* 236 */       if (!contains(key)) {
/* 237 */         return false;
/*     */       }
/* 239 */       AbstractBiMap.this.removeFromBothMaps(key);
/* 240 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> keysToRemove) {
/* 245 */       return standardRemoveAll(keysToRemove);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> keysToRetain) {
/* 250 */       return standardRetainAll(keysToRetain);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 255 */       return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
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
/*     */   public Set<V> values() {
/* 267 */     Set<V> result = this.valueSet;
/* 268 */     return (result == null) ? (this.valueSet = new ValueSet()) : result;
/*     */   }
/*     */   
/*     */   private class ValueSet
/*     */     extends ForwardingSet<V> {
/* 273 */     final Set<V> valuesDelegate = AbstractBiMap.this.inverse.keySet();
/*     */ 
/*     */     
/*     */     protected Set<V> delegate() {
/* 277 */       return this.valuesDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 282 */       return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 287 */       return standardToArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 292 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 297 */       return standardToString();
/*     */     }
/*     */ 
/*     */     
/*     */     private ValueSet() {}
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 305 */     Set<Map.Entry<K, V>> result = this.entrySet;
/* 306 */     return (result == null) ? (this.entrySet = new EntrySet()) : result;
/*     */   }
/*     */   
/*     */   class BiMapEntry extends ForwardingMapEntry<K, V> {
/*     */     private final Map.Entry<K, V> delegate;
/*     */     
/*     */     BiMapEntry(Map.Entry<K, V> delegate) {
/* 313 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Map.Entry<K, V> delegate() {
/* 318 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 323 */       AbstractBiMap.this.checkValue(value);
/*     */       
/* 325 */       Preconditions.checkState(AbstractBiMap.this.entrySet().contains(this), "entry no longer in map");
/*     */       
/* 327 */       if (Objects.equal(value, getValue())) {
/* 328 */         return value;
/*     */       }
/* 330 */       Preconditions.checkArgument(!AbstractBiMap.this.containsValue(value), "value already present: %s", value);
/* 331 */       V oldValue = this.delegate.setValue(value);
/* 332 */       Preconditions.checkState(Objects.equal(value, AbstractBiMap.this.get(getKey())), "entry no longer in map");
/* 333 */       AbstractBiMap.this.updateInverseMap(getKey(), true, oldValue, value);
/* 334 */       return oldValue;
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entrySetIterator() {
/* 339 */     final Iterator<Map.Entry<K, V>> iterator = this.delegate.entrySet().iterator();
/* 340 */     return new Iterator<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> entry;
/*     */         
/*     */         public boolean hasNext() {
/* 345 */           return iterator.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 350 */           this.entry = iterator.next();
/* 351 */           return new AbstractBiMap.BiMapEntry(this.entry);
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 356 */           CollectPreconditions.checkRemove((this.entry != null));
/* 357 */           V value = this.entry.getValue();
/* 358 */           iterator.remove();
/* 359 */           AbstractBiMap.this.removeFromInverseMap(value);
/* 360 */           this.entry = null;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private class EntrySet
/*     */     extends ForwardingSet<Map.Entry<K, V>> {
/* 367 */     final Set<Map.Entry<K, V>> esDelegate = AbstractBiMap.this.delegate.entrySet();
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<K, V>> delegate() {
/* 371 */       return this.esDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 376 */       AbstractBiMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object object) {
/* 381 */       if (!this.esDelegate.contains(object)) {
/* 382 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 386 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 387 */       AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 393 */       this.esDelegate.remove(entry);
/* 394 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 399 */       return AbstractBiMap.this.entrySetIterator();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 406 */       return standardToArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 411 */       return (T[])standardToArray((Object[])array);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 416 */       return Maps.containsEntryImpl(delegate(), o);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 421 */       return standardContainsAll(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 426 */       return standardRemoveAll(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 431 */       return standardRetainAll(c);
/*     */     }
/*     */     
/*     */     private EntrySet() {} }
/*     */   
/*     */   static class Inverse<K, V> extends AbstractBiMap<K, V> {
/*     */     Inverse(Map<K, V> backward, AbstractBiMap<V, K> forward) {
/* 438 */       super(backward, forward);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     K checkKey(K key) {
/* 452 */       return this.inverse.checkValue(key);
/*     */     }
/*     */ 
/*     */     
/*     */     V checkValue(V value) {
/* 457 */       return this.inverse.checkKey(value);
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     private void writeObject(ObjectOutputStream stream) throws IOException {
/* 463 */       stream.defaultWriteObject();
/* 464 */       stream.writeObject(inverse());
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 470 */       stream.defaultReadObject();
/* 471 */       setInverse((AbstractBiMap<V, K>)stream.readObject());
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     Object readResolve() {
/* 476 */       return inverse().inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */