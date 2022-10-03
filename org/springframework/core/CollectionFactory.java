/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CollectionFactory
/*     */ {
/*  58 */   private static final Set<Class<?>> approximableCollectionTypes = new HashSet<>();
/*     */   
/*  60 */   private static final Set<Class<?>> approximableMapTypes = new HashSet<>();
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  65 */     approximableCollectionTypes.add(Collection.class);
/*  66 */     approximableCollectionTypes.add(List.class);
/*  67 */     approximableCollectionTypes.add(Set.class);
/*  68 */     approximableCollectionTypes.add(SortedSet.class);
/*  69 */     approximableCollectionTypes.add(NavigableSet.class);
/*  70 */     approximableMapTypes.add(Map.class);
/*  71 */     approximableMapTypes.add(SortedMap.class);
/*  72 */     approximableMapTypes.add(NavigableMap.class);
/*     */ 
/*     */     
/*  75 */     approximableCollectionTypes.add(ArrayList.class);
/*  76 */     approximableCollectionTypes.add(LinkedList.class);
/*  77 */     approximableCollectionTypes.add(HashSet.class);
/*  78 */     approximableCollectionTypes.add(LinkedHashSet.class);
/*  79 */     approximableCollectionTypes.add(TreeSet.class);
/*  80 */     approximableCollectionTypes.add(EnumSet.class);
/*  81 */     approximableMapTypes.add(HashMap.class);
/*  82 */     approximableMapTypes.add(LinkedHashMap.class);
/*  83 */     approximableMapTypes.add(TreeMap.class);
/*  84 */     approximableMapTypes.add(EnumMap.class);
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
/*     */   public static boolean isApproximableCollectionType(@Nullable Class<?> collectionType) {
/*  99 */     return (collectionType != null && approximableCollectionTypes.contains(collectionType));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Collection<E> createApproximateCollection(@Nullable Object collection, int capacity) {
/* 124 */     if (collection instanceof LinkedList) {
/* 125 */       return new LinkedList<>();
/*     */     }
/* 127 */     if (collection instanceof List) {
/* 128 */       return new ArrayList<>(capacity);
/*     */     }
/* 130 */     if (collection instanceof EnumSet) {
/*     */       
/* 132 */       Collection<E> enumSet = (Collection)EnumSet.copyOf((EnumSet<Enum>)collection);
/* 133 */       enumSet.clear();
/* 134 */       return enumSet;
/*     */     } 
/* 136 */     if (collection instanceof SortedSet) {
/* 137 */       return new TreeSet<>(((SortedSet<E>)collection).comparator());
/*     */     }
/*     */     
/* 140 */     return new LinkedHashSet<>(capacity);
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
/*     */   public static <E> Collection<E> createCollection(Class<?> collectionType, int capacity) {
/* 155 */     return createCollection(collectionType, null, capacity);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Collection<E> createCollection(Class<?> collectionType, @Nullable Class<?> elementType, int capacity) {
/* 183 */     Assert.notNull(collectionType, "Collection type must not be null");
/* 184 */     if (collectionType.isInterface()) {
/* 185 */       if (Set.class == collectionType || Collection.class == collectionType) {
/* 186 */         return new LinkedHashSet<>(capacity);
/*     */       }
/* 188 */       if (List.class == collectionType) {
/* 189 */         return new ArrayList<>(capacity);
/*     */       }
/* 191 */       if (SortedSet.class == collectionType || NavigableSet.class == collectionType) {
/* 192 */         return new TreeSet<>();
/*     */       }
/*     */       
/* 195 */       throw new IllegalArgumentException("Unsupported Collection interface: " + collectionType.getName());
/*     */     } 
/*     */     
/* 198 */     if (EnumSet.class.isAssignableFrom(collectionType)) {
/* 199 */       Assert.notNull(elementType, "Cannot create EnumSet for unknown element type");
/*     */       
/* 201 */       return (Collection)EnumSet.noneOf(asEnumType(elementType));
/*     */     } 
/*     */     
/* 204 */     if (!Collection.class.isAssignableFrom(collectionType)) {
/* 205 */       throw new IllegalArgumentException("Unsupported Collection type: " + collectionType.getName());
/*     */     }
/*     */     try {
/* 208 */       return ReflectionUtils.accessibleConstructor(collectionType, new Class[0]).newInstance(new Object[0]);
/*     */     }
/* 210 */     catch (Throwable ex) {
/* 211 */       throw new IllegalArgumentException("Could not instantiate Collection type: " + collectionType
/* 212 */           .getName(), ex);
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
/*     */   public static boolean isApproximableMapType(@Nullable Class<?> mapType) {
/* 224 */     return (mapType != null && approximableMapTypes.contains(mapType));
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
/*     */ 
/*     */   
/*     */   public static <K, V> Map<K, V> createApproximateMap(@Nullable Object map, int capacity) {
/* 246 */     if (map instanceof EnumMap) {
/* 247 */       EnumMap<Enum, Object> enumMap = new EnumMap<>((EnumMap<Enum, ?>)map);
/* 248 */       enumMap.clear();
/* 249 */       return (Map)enumMap;
/*     */     } 
/* 251 */     if (map instanceof SortedMap) {
/* 252 */       return new TreeMap<>(((SortedMap)map).comparator());
/*     */     }
/*     */     
/* 255 */     return new LinkedHashMap<>(capacity);
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
/*     */   public static <K, V> Map<K, V> createMap(Class<?> mapType, int capacity) {
/* 270 */     return createMap(mapType, null, capacity);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Map<K, V> createMap(Class<?> mapType, @Nullable Class<?> keyType, int capacity) {
/* 299 */     Assert.notNull(mapType, "Map type must not be null");
/* 300 */     if (mapType.isInterface()) {
/* 301 */       if (Map.class == mapType) {
/* 302 */         return new LinkedHashMap<>(capacity);
/*     */       }
/* 304 */       if (SortedMap.class == mapType || NavigableMap.class == mapType) {
/* 305 */         return new TreeMap<>();
/*     */       }
/* 307 */       if (MultiValueMap.class == mapType) {
/* 308 */         return (Map<K, V>)new LinkedMultiValueMap();
/*     */       }
/*     */       
/* 311 */       throw new IllegalArgumentException("Unsupported Map interface: " + mapType.getName());
/*     */     } 
/*     */     
/* 314 */     if (EnumMap.class == mapType) {
/* 315 */       Assert.notNull(keyType, "Cannot create EnumMap for unknown key type");
/* 316 */       return (Map)new EnumMap<>(asEnumType(keyType));
/*     */     } 
/*     */     
/* 319 */     if (!Map.class.isAssignableFrom(mapType)) {
/* 320 */       throw new IllegalArgumentException("Unsupported Map type: " + mapType.getName());
/*     */     }
/*     */     try {
/* 323 */       return ReflectionUtils.accessibleConstructor(mapType, new Class[0]).newInstance(new Object[0]);
/*     */     }
/* 325 */     catch (Throwable ex) {
/* 326 */       throw new IllegalArgumentException("Could not instantiate Map type: " + mapType.getName(), ex);
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
/*     */   public static Properties createStringAdaptingProperties() {
/* 343 */     return new SortedProperties(false)
/*     */       {
/*     */         @Nullable
/*     */         public String getProperty(String key) {
/* 347 */           Object value = get(key);
/* 348 */           return (value != null) ? value.toString() : null;
/*     */         }
/*     */       };
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
/*     */   public static Properties createSortedProperties(boolean omitComments) {
/* 368 */     return new SortedProperties(omitComments);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Properties createSortedProperties(Properties properties, boolean omitComments) {
/* 391 */     return new SortedProperties(properties, omitComments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<? extends Enum> asEnumType(Class<?> enumType) {
/* 402 */     Assert.notNull(enumType, "Enum type must not be null");
/* 403 */     if (!Enum.class.isAssignableFrom(enumType)) {
/* 404 */       throw new IllegalArgumentException("Supplied type is not an enum: " + enumType.getName());
/*     */     }
/* 406 */     return enumType.asSubclass(Enum.class);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\CollectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */