/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ public abstract class CollectionUtils
/*     */ {
/*     */   static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*     */   
/*     */   public static boolean isEmpty(@Nullable Collection<?> collection) {
/*  61 */     return (collection == null || collection.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(@Nullable Map<?, ?> map) {
/*  71 */     return (map == null || map.isEmpty());
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
/*     */   public static <K, V> HashMap<K, V> newHashMap(int expectedSize) {
/*  88 */     return new HashMap<>((int)(expectedSize / 0.75F), 0.75F);
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
/*     */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int expectedSize) {
/* 105 */     return new LinkedHashMap<>((int)(expectedSize / 0.75F), 0.75F);
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
/*     */   public static List<?> arrayToList(@Nullable Object source) {
/* 121 */     return Arrays.asList(ObjectUtils.toObjectArray(source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> void mergeArrayIntoCollection(@Nullable Object array, Collection<E> collection) {
/* 131 */     Object[] arr = ObjectUtils.toObjectArray(array);
/* 132 */     for (Object elem : arr) {
/* 133 */       collection.add((E)elem);
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
/*     */   public static <K, V> void mergePropertiesIntoMap(@Nullable Properties props, Map<K, V> map) {
/* 147 */     if (props != null) {
/* 148 */       for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements(); ) {
/* 149 */         String key = (String)en.nextElement();
/* 150 */         Object value = props.get(key);
/* 151 */         if (value == null)
/*     */         {
/* 153 */           value = props.getProperty(key);
/*     */         }
/* 155 */         map.put((K)key, (V)value);
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
/*     */   public static boolean contains(@Nullable Iterator<?> iterator, Object element) {
/* 168 */     if (iterator != null) {
/* 169 */       while (iterator.hasNext()) {
/* 170 */         Object candidate = iterator.next();
/* 171 */         if (ObjectUtils.nullSafeEquals(candidate, element)) {
/* 172 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(@Nullable Enumeration<?> enumeration, Object element) {
/* 186 */     if (enumeration != null) {
/* 187 */       while (enumeration.hasMoreElements()) {
/* 188 */         Object candidate = enumeration.nextElement();
/* 189 */         if (ObjectUtils.nullSafeEquals(candidate, element)) {
/* 190 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 194 */     return false;
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
/*     */   public static boolean containsInstance(@Nullable Collection<?> collection, Object element) {
/* 206 */     if (collection != null) {
/* 207 */       for (Object candidate : collection) {
/* 208 */         if (candidate == element) {
/* 209 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 213 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
/* 224 */     return (findFirstMatch(source, candidates) != null);
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
/*     */   @Nullable
/*     */   public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
/* 239 */     if (isEmpty(source) || isEmpty(candidates)) {
/* 240 */       return null;
/*     */     }
/* 242 */     for (E candidate : candidates) {
/* 243 */       if (source.contains(candidate)) {
/* 244 */         return candidate;
/*     */       }
/*     */     } 
/* 247 */     return null;
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
/*     */   @Nullable
/*     */   public static <T> T findValueOfType(Collection<?> collection, @Nullable Class<T> type) {
/* 260 */     if (isEmpty(collection)) {
/* 261 */       return null;
/*     */     }
/* 263 */     T value = null;
/* 264 */     for (Object element : collection) {
/* 265 */       if (type == null || type.isInstance(element)) {
/* 266 */         if (value != null)
/*     */         {
/* 268 */           return null;
/*     */         }
/* 270 */         value = (T)element;
/*     */       } 
/*     */     } 
/* 273 */     return value;
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
/*     */   @Nullable
/*     */   public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
/* 287 */     if (isEmpty(collection) || ObjectUtils.isEmpty((Object[])types)) {
/* 288 */       return null;
/*     */     }
/* 290 */     for (Class<?> type : types) {
/* 291 */       Object value = findValueOfType(collection, type);
/* 292 */       if (value != null) {
/* 293 */         return value;
/*     */       }
/*     */     } 
/* 296 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasUniqueObject(Collection<?> collection) {
/* 306 */     if (isEmpty(collection)) {
/* 307 */       return false;
/*     */     }
/* 309 */     boolean hasCandidate = false;
/* 310 */     Object candidate = null;
/* 311 */     for (Object elem : collection) {
/* 312 */       if (!hasCandidate) {
/* 313 */         hasCandidate = true;
/* 314 */         candidate = elem; continue;
/*     */       } 
/* 316 */       if (candidate != elem) {
/* 317 */         return false;
/*     */       }
/*     */     } 
/* 320 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Class<?> findCommonElementType(Collection<?> collection) {
/* 331 */     if (isEmpty(collection)) {
/* 332 */       return null;
/*     */     }
/* 334 */     Class<?> candidate = null;
/* 335 */     for (Object val : collection) {
/* 336 */       if (val != null) {
/* 337 */         if (candidate == null) {
/* 338 */           candidate = val.getClass(); continue;
/*     */         } 
/* 340 */         if (candidate != val.getClass()) {
/* 341 */           return null;
/*     */         }
/*     */       } 
/*     */     } 
/* 345 */     return candidate;
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
/*     */   @Nullable
/*     */   public static <T> T firstElement(@Nullable Set<T> set) {
/* 360 */     if (isEmpty(set)) {
/* 361 */       return null;
/*     */     }
/* 363 */     if (set instanceof SortedSet) {
/* 364 */       return ((SortedSet<T>)set).first();
/*     */     }
/*     */     
/* 367 */     Iterator<T> it = set.iterator();
/* 368 */     T first = null;
/* 369 */     if (it.hasNext()) {
/* 370 */       first = it.next();
/*     */     }
/* 372 */     return first;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static <T> T firstElement(@Nullable List<T> list) {
/* 383 */     if (isEmpty(list)) {
/* 384 */       return null;
/*     */     }
/* 386 */     return list.get(0);
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
/*     */   @Nullable
/*     */   public static <T> T lastElement(@Nullable Set<T> set) {
/* 401 */     if (isEmpty(set)) {
/* 402 */       return null;
/*     */     }
/* 404 */     if (set instanceof SortedSet) {
/* 405 */       return ((SortedSet<T>)set).last();
/*     */     }
/*     */ 
/*     */     
/* 409 */     Iterator<T> it = set.iterator();
/* 410 */     T last = null;
/* 411 */     while (it.hasNext()) {
/* 412 */       last = it.next();
/*     */     }
/* 414 */     return last;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static <T> T lastElement(@Nullable List<T> list) {
/* 425 */     if (isEmpty(list)) {
/* 426 */       return null;
/*     */     }
/* 428 */     return list.get(list.size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
/* 437 */     ArrayList<A> elements = new ArrayList<>();
/* 438 */     while (enumeration.hasMoreElements()) {
/* 439 */       elements.add((A)enumeration.nextElement());
/*     */     }
/* 441 */     return elements.toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Iterator<E> toIterator(@Nullable Enumeration<E> enumeration) {
/* 450 */     return (enumeration != null) ? new EnumerationIterator<>(enumeration) : Collections.<E>emptyIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> targetMap) {
/* 460 */     return new MultiValueMapAdapter<>(targetMap);
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
/*     */   public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap(MultiValueMap<? extends K, ? extends V> targetMap) {
/* 473 */     Assert.notNull(targetMap, "'targetMap' must not be null");
/* 474 */     Map<K, List<V>> result = newLinkedHashMap(targetMap.size());
/* 475 */     targetMap.forEach((key, value) -> {
/*     */           List<? extends V> values = Collections.unmodifiableList(value);
/*     */           result.put(key, values);
/*     */         });
/* 479 */     Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap(result);
/* 480 */     return toMultiValueMap(unmodifiableMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class EnumerationIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private final Enumeration<E> enumeration;
/*     */ 
/*     */     
/*     */     public EnumerationIterator(Enumeration<E> enumeration) {
/* 492 */       this.enumeration = enumeration;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 497 */       return this.enumeration.hasMoreElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 502 */       return this.enumeration.nextElement();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() throws UnsupportedOperationException {
/* 507 */       throw new UnsupportedOperationException("Not supported");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\CollectionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */