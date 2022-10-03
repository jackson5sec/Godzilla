/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EnumMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private transient Class<K> keyType;
/*     */   private transient Class<V> valueType;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> keyType, Class<V> valueType) {
/*  53 */     return new EnumBiMap<>(keyType, valueType);
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
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> map) {
/*  66 */     EnumBiMap<K, V> bimap = create(inferKeyType(map), inferValueType(map));
/*  67 */     bimap.putAll(map);
/*  68 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumBiMap(Class<K> keyType, Class<V> valueType) {
/*  72 */     super(new EnumMap<>(keyType), (Map)new EnumMap<>(valueType));
/*  73 */     this.keyType = keyType;
/*  74 */     this.valueType = valueType;
/*     */   }
/*     */   
/*     */   static <K extends Enum<K>> Class<K> inferKeyType(Map<K, ?> map) {
/*  78 */     if (map instanceof EnumBiMap) {
/*  79 */       return ((EnumBiMap)map).keyType();
/*     */     }
/*  81 */     if (map instanceof EnumHashBiMap) {
/*  82 */       return ((EnumHashBiMap)map).keyType();
/*     */     }
/*  84 */     Preconditions.checkArgument(!map.isEmpty());
/*  85 */     return ((Enum<K>)map.keySet().iterator().next()).getDeclaringClass();
/*     */   }
/*     */   
/*     */   private static <V extends Enum<V>> Class<V> inferValueType(Map<?, V> map) {
/*  89 */     if (map instanceof EnumBiMap) {
/*  90 */       return ((EnumBiMap)map).valueType;
/*     */     }
/*  92 */     Preconditions.checkArgument(!map.isEmpty());
/*  93 */     return ((Enum<V>)map.values().iterator().next()).getDeclaringClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<K> keyType() {
/*  98 */     return this.keyType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<V> valueType() {
/* 103 */     return this.valueType;
/*     */   }
/*     */ 
/*     */   
/*     */   K checkKey(K key) {
/* 108 */     return (K)Preconditions.checkNotNull(key);
/*     */   }
/*     */ 
/*     */   
/*     */   V checkValue(V value) {
/* 113 */     return (V)Preconditions.checkNotNull(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 122 */     stream.defaultWriteObject();
/* 123 */     stream.writeObject(this.keyType);
/* 124 */     stream.writeObject(this.valueType);
/* 125 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 131 */     stream.defaultReadObject();
/* 132 */     this.keyType = (Class<K>)stream.readObject();
/* 133 */     this.valueType = (Class<V>)stream.readObject();
/* 134 */     setDelegates(new EnumMap<>(this.keyType), (Map)new EnumMap<>(this.valueType));
/* 135 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\EnumBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */