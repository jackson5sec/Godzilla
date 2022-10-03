/*     */ package javassist.scopedpool;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class SoftValueHashMap<K, V>
/*     */   implements Map<K, V>
/*     */ {
/*     */   private Map<K, SoftValueRef<K, V>> hash;
/*     */   
/*     */   private static class SoftValueRef<K, V>
/*     */     extends SoftReference<V>
/*     */   {
/*     */     public K key;
/*     */     
/*     */     private SoftValueRef(K key, V val, ReferenceQueue<V> q) {
/*  42 */       super(val, q);
/*  43 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     private static <K, V> SoftValueRef<K, V> create(K key, V val, ReferenceQueue<V> q) {
/*  48 */       if (val == null) {
/*  49 */         return null;
/*     */       }
/*  51 */       return new SoftValueRef<>(key, val, q);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/*  61 */     processQueue();
/*  62 */     Set<Map.Entry<K, V>> ret = new HashSet<>();
/*  63 */     for (Map.Entry<K, SoftValueRef<K, V>> e : this.hash.entrySet())
/*  64 */       ret.add(new AbstractMap.SimpleImmutableEntry<>(e
/*  65 */             .getKey(), (V)((SoftValueRef)e.getValue()).get())); 
/*  66 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private ReferenceQueue<V> queue = new ReferenceQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processQueue() {
/*  81 */     if (!this.hash.isEmpty()) {
/*  82 */       Object<? extends V> ref; while ((ref = (Object<? extends V>)this.queue.poll()) != null) {
/*  83 */         if (ref instanceof SoftValueRef) {
/*     */           
/*  85 */           SoftValueRef que = (SoftValueRef)ref;
/*  86 */           if (ref == this.hash.get(que.key))
/*     */           {
/*  88 */             this.hash.remove(que.key);
/*     */           }
/*     */         } 
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoftValueHashMap(int initialCapacity, float loadFactor) {
/* 109 */     this.hash = new ConcurrentHashMap<>(initialCapacity, loadFactor);
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
/*     */   public SoftValueHashMap(int initialCapacity) {
/* 123 */     this.hash = new ConcurrentHashMap<>(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoftValueHashMap() {
/* 131 */     this.hash = new ConcurrentHashMap<>();
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
/*     */   public SoftValueHashMap(Map<K, V> t) {
/* 144 */     this(Math.max(2 * t.size(), 11), 0.75F);
/* 145 */     putAll(t);
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
/*     */   public int size() {
/* 158 */     processQueue();
/* 159 */     return this.hash.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 167 */     processQueue();
/* 168 */     return this.hash.isEmpty();
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
/*     */   public boolean containsKey(Object key) {
/* 180 */     processQueue();
/* 181 */     return this.hash.containsKey(key);
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
/*     */   public V get(Object key) {
/* 196 */     processQueue();
/* 197 */     return valueOrNull(this.hash.get(key));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 217 */     processQueue();
/* 218 */     return valueOrNull(this.hash.put(key, SoftValueRef.create(key, value, this.queue)));
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
/*     */   public V remove(Object key) {
/* 233 */     processQueue();
/* 234 */     return valueOrNull(this.hash.remove(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 242 */     processQueue();
/* 243 */     this.hash.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object arg0) {
/* 253 */     processQueue();
/* 254 */     if (null == arg0) {
/* 255 */       return false;
/*     */     }
/* 257 */     for (SoftValueRef<K, V> e : this.hash.values()) {
/* 258 */       if (null != e && arg0.equals(e.get()))
/* 259 */         return true; 
/* 260 */     }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 266 */     processQueue();
/* 267 */     return this.hash.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> arg0) {
/* 273 */     processQueue();
/* 274 */     for (K key : arg0.keySet()) {
/* 275 */       put(key, arg0.get(key));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 281 */     processQueue();
/* 282 */     List<V> ret = new ArrayList<>();
/* 283 */     for (SoftValueRef<K, V> e : this.hash.values())
/* 284 */       ret.add(e.get()); 
/* 285 */     return ret;
/*     */   }
/*     */   
/*     */   private V valueOrNull(SoftValueRef<K, V> rtn) {
/* 289 */     if (null == rtn)
/* 290 */       return null; 
/* 291 */     return rtn.get();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\scopedpool\SoftValueHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */