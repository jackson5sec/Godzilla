/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class LinkedCaseInsensitiveMap<V>
/*     */   implements Map<String, V>, Serializable, Cloneable
/*     */ {
/*     */   private final LinkedHashMap<String, V> targetMap;
/*     */   private final HashMap<String, String> caseInsensitiveKeys;
/*     */   private final Locale locale;
/*     */   @Nullable
/*     */   private volatile transient Set<String> keySet;
/*     */   @Nullable
/*     */   private volatile transient Collection<V> values;
/*     */   @Nullable
/*     */   private volatile transient Set<Map.Entry<String, V>> entrySet;
/*     */   
/*     */   public LinkedCaseInsensitiveMap() {
/*  74 */     this((Locale)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedCaseInsensitiveMap(@Nullable Locale locale) {
/*  84 */     this(12, locale);
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
/*     */   public LinkedCaseInsensitiveMap(int expectedSize) {
/*  98 */     this(expectedSize, null);
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
/*     */   public LinkedCaseInsensitiveMap(int expectedSize, @Nullable Locale locale) {
/* 113 */     this.targetMap = new LinkedHashMap<String, V>((int)(expectedSize / 0.75F), 0.75F)
/*     */       {
/*     */         public boolean containsKey(Object key)
/*     */         {
/* 117 */           return LinkedCaseInsensitiveMap.this.containsKey(key);
/*     */         }
/*     */         
/*     */         protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
/* 121 */           boolean doRemove = LinkedCaseInsensitiveMap.this.removeEldestEntry(eldest);
/* 122 */           if (doRemove) {
/* 123 */             LinkedCaseInsensitiveMap.this.removeCaseInsensitiveKey(eldest.getKey());
/*     */           }
/* 125 */           return doRemove;
/*     */         }
/*     */       };
/* 128 */     this.caseInsensitiveKeys = CollectionUtils.newHashMap(expectedSize);
/* 129 */     this.locale = (locale != null) ? locale : Locale.getDefault();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LinkedCaseInsensitiveMap(LinkedCaseInsensitiveMap<V> other) {
/* 137 */     this.targetMap = (LinkedHashMap<String, V>)other.targetMap.clone();
/* 138 */     this.caseInsensitiveKeys = (HashMap<String, String>)other.caseInsensitiveKeys.clone();
/* 139 */     this.locale = other.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 147 */     return this.targetMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 152 */     return this.targetMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 157 */     return (key instanceof String && this.caseInsensitiveKeys.containsKey(convertKey((String)key)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 162 */     return this.targetMap.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(Object key) {
/* 168 */     if (key instanceof String) {
/* 169 */       String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String)key));
/* 170 */       if (caseInsensitiveKey != null) {
/* 171 */         return this.targetMap.get(caseInsensitiveKey);
/*     */       }
/*     */     } 
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V getOrDefault(Object key, V defaultValue) {
/* 180 */     if (key instanceof String) {
/* 181 */       String caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String)key));
/* 182 */       if (caseInsensitiveKey != null) {
/* 183 */         return this.targetMap.get(caseInsensitiveKey);
/*     */       }
/*     */     } 
/* 186 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V put(String key, @Nullable V value) {
/* 192 */     String oldKey = this.caseInsensitiveKeys.put(convertKey(key), key);
/* 193 */     V oldKeyValue = null;
/* 194 */     if (oldKey != null && !oldKey.equals(key)) {
/* 195 */       oldKeyValue = this.targetMap.remove(oldKey);
/*     */     }
/* 197 */     V oldValue = this.targetMap.put(key, value);
/* 198 */     return (oldKeyValue != null) ? oldKeyValue : oldValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends V> map) {
/* 203 */     if (map.isEmpty()) {
/*     */       return;
/*     */     }
/* 206 */     map.forEach(this::put);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V putIfAbsent(String key, @Nullable V value) {
/* 212 */     String oldKey = this.caseInsensitiveKeys.putIfAbsent(convertKey(key), key);
/* 213 */     if (oldKey != null) {
/* 214 */       V oldKeyValue = this.targetMap.get(oldKey);
/* 215 */       if (oldKeyValue != null) {
/* 216 */         return oldKeyValue;
/*     */       }
/*     */       
/* 219 */       key = oldKey;
/*     */     } 
/*     */     
/* 222 */     return this.targetMap.putIfAbsent(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V computeIfAbsent(String key, Function<? super String, ? extends V> mappingFunction) {
/* 228 */     String oldKey = this.caseInsensitiveKeys.putIfAbsent(convertKey(key), key);
/* 229 */     if (oldKey != null) {
/* 230 */       V oldKeyValue = this.targetMap.get(oldKey);
/* 231 */       if (oldKeyValue != null) {
/* 232 */         return oldKeyValue;
/*     */       }
/*     */       
/* 235 */       key = oldKey;
/*     */     } 
/*     */     
/* 238 */     return this.targetMap.computeIfAbsent(key, mappingFunction);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V remove(Object key) {
/* 244 */     if (key instanceof String) {
/* 245 */       String caseInsensitiveKey = removeCaseInsensitiveKey((String)key);
/* 246 */       if (caseInsensitiveKey != null) {
/* 247 */         return this.targetMap.remove(caseInsensitiveKey);
/*     */       }
/*     */     } 
/* 250 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 255 */     this.caseInsensitiveKeys.clear();
/* 256 */     this.targetMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 261 */     Set<String> keySet = this.keySet;
/* 262 */     if (keySet == null) {
/* 263 */       keySet = new KeySet(this.targetMap.keySet());
/* 264 */       this.keySet = keySet;
/*     */     } 
/* 266 */     return keySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 271 */     Collection<V> values = this.values;
/* 272 */     if (values == null) {
/* 273 */       values = new Values(this.targetMap.values());
/* 274 */       this.values = values;
/*     */     } 
/* 276 */     return values;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, V>> entrySet() {
/* 281 */     Set<Map.Entry<String, V>> entrySet = this.entrySet;
/* 282 */     if (entrySet == null) {
/* 283 */       entrySet = new EntrySet(this.targetMap.entrySet());
/* 284 */       this.entrySet = entrySet;
/*     */     } 
/* 286 */     return entrySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public LinkedCaseInsensitiveMap<V> clone() {
/* 291 */     return new LinkedCaseInsensitiveMap(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 296 */     return (this == other || this.targetMap.equals(other));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 301 */     return this.targetMap.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 306 */     return this.targetMap.toString();
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
/*     */   public Locale getLocale() {
/* 320 */     return this.locale;
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
/*     */   protected String convertKey(String key) {
/* 332 */     return key.toLowerCase(getLocale());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
/* 342 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String removeCaseInsensitiveKey(String key) {
/* 347 */     return this.caseInsensitiveKeys.remove(convertKey(key));
/*     */   }
/*     */   
/*     */   private class KeySet
/*     */     extends AbstractSet<String>
/*     */   {
/*     */     private final Set<String> delegate;
/*     */     
/*     */     KeySet(Set<String> delegate) {
/* 356 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 361 */       return this.delegate.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 366 */       return this.delegate.contains(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<String> iterator() {
/* 371 */       return new LinkedCaseInsensitiveMap.KeySetIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 376 */       return (LinkedCaseInsensitiveMap.this.remove(o) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 381 */       LinkedCaseInsensitiveMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<String> spliterator() {
/* 386 */       return this.delegate.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super String> action) {
/* 391 */       this.delegate.forEach(action);
/*     */     }
/*     */   }
/*     */   
/*     */   private class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/*     */     private final Collection<V> delegate;
/*     */     
/*     */     Values(Collection<V> delegate) {
/* 401 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 406 */       return this.delegate.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 411 */       return this.delegate.contains(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 416 */       return new LinkedCaseInsensitiveMap.ValuesIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 421 */       LinkedCaseInsensitiveMap.this.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<V> spliterator() {
/* 426 */       return this.delegate.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super V> action) {
/* 431 */       this.delegate.forEach(action);
/*     */     }
/*     */   }
/*     */   
/*     */   private class EntrySet
/*     */     extends AbstractSet<Map.Entry<String, V>>
/*     */   {
/*     */     private final Set<Map.Entry<String, V>> delegate;
/*     */     
/*     */     public EntrySet(Set<Map.Entry<String, V>> delegate) {
/* 441 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 446 */       return this.delegate.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 451 */       return this.delegate.contains(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<String, V>> iterator() {
/* 456 */       return new LinkedCaseInsensitiveMap.EntrySetIterator();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(Object o) {
/* 462 */       if (this.delegate.remove(o)) {
/* 463 */         LinkedCaseInsensitiveMap.this.removeCaseInsensitiveKey((String)((Map.Entry)o).getKey());
/* 464 */         return true;
/*     */       } 
/* 466 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 471 */       this.delegate.clear();
/* 472 */       LinkedCaseInsensitiveMap.this.caseInsensitiveKeys.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Map.Entry<String, V>> spliterator() {
/* 477 */       return this.delegate.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super Map.Entry<String, V>> action) {
/* 482 */       this.delegate.forEach(action);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class EntryIterator<T>
/*     */     implements Iterator<T>
/*     */   {
/* 495 */     private final Iterator<Map.Entry<String, V>> delegate = LinkedCaseInsensitiveMap.this.targetMap.entrySet().iterator();
/*     */ 
/*     */     
/*     */     protected Map.Entry<String, V> nextEntry() {
/* 499 */       Map.Entry<String, V> entry = this.delegate.next();
/* 500 */       this.last = entry;
/* 501 */       return entry;
/*     */     }
/*     */     @Nullable
/*     */     private Map.Entry<String, V> last;
/*     */     public boolean hasNext() {
/* 506 */       return this.delegate.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 511 */       this.delegate.remove();
/* 512 */       if (this.last != null) {
/* 513 */         LinkedCaseInsensitiveMap.this.removeCaseInsensitiveKey(this.last.getKey());
/* 514 */         this.last = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class KeySetIterator
/*     */     extends EntryIterator<String> {
/*     */     private KeySetIterator() {}
/*     */     
/*     */     public String next() {
/* 524 */       return (String)nextEntry().getKey();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ValuesIterator
/*     */     extends EntryIterator<V> {
/*     */     private ValuesIterator() {}
/*     */     
/*     */     public V next() {
/* 533 */       return (V)nextEntry().getValue();
/*     */     }
/*     */   }
/*     */   
/*     */   private class EntrySetIterator
/*     */     extends EntryIterator<Map.Entry<String, V>> {
/*     */     private EntrySetIterator() {}
/*     */     
/*     */     public Map.Entry<String, V> next() {
/* 542 */       return nextEntry();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\LinkedCaseInsensitiveMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */