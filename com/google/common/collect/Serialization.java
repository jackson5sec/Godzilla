/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ final class Serialization
/*     */ {
/*     */   static int readCount(ObjectInputStream stream) throws IOException {
/*  47 */     return stream.readInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void writeMap(Map<K, V> map, ObjectOutputStream stream) throws IOException {
/*  58 */     stream.writeInt(map.size());
/*  59 */     for (Map.Entry<K, V> entry : map.entrySet()) {
/*  60 */       stream.writeObject(entry.getKey());
/*  61 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  71 */     int size = stream.readInt();
/*  72 */     populateMap(map, stream, size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream, int size) throws IOException, ClassNotFoundException {
/*  81 */     for (int i = 0; i < size; i++) {
/*     */       
/*  83 */       K key = (K)stream.readObject();
/*     */       
/*  85 */       V value = (V)stream.readObject();
/*  86 */       map.put(key, value);
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
/*     */   static <E> void writeMultiset(Multiset<E> multiset, ObjectOutputStream stream) throws IOException {
/*  99 */     int entryCount = multiset.entrySet().size();
/* 100 */     stream.writeInt(entryCount);
/* 101 */     for (Multiset.Entry<E> entry : multiset.entrySet()) {
/* 102 */       stream.writeObject(entry.getElement());
/* 103 */       stream.writeInt(entry.getCount());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 113 */     int distinctElements = stream.readInt();
/* 114 */     populateMultiset(multiset, stream, distinctElements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream, int distinctElements) throws IOException, ClassNotFoundException {
/* 125 */     for (int i = 0; i < distinctElements; i++) {
/*     */       
/* 127 */       E element = (E)stream.readObject();
/* 128 */       int count = stream.readInt();
/* 129 */       multiset.add(element, count);
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
/*     */   static <K, V> void writeMultimap(Multimap<K, V> multimap, ObjectOutputStream stream) throws IOException {
/* 143 */     stream.writeInt(multimap.asMap().size());
/* 144 */     for (Map.Entry<K, Collection<V>> entry : (Iterable<Map.Entry<K, Collection<V>>>)multimap.asMap().entrySet()) {
/* 145 */       stream.writeObject(entry.getKey());
/* 146 */       stream.writeInt(((Collection)entry.getValue()).size());
/* 147 */       for (V value : entry.getValue()) {
/* 148 */         stream.writeObject(value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 159 */     int distinctKeys = stream.readInt();
/* 160 */     populateMultimap(multimap, stream, distinctKeys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream, int distinctKeys) throws IOException, ClassNotFoundException {
/* 171 */     for (int i = 0; i < distinctKeys; i++) {
/*     */       
/* 173 */       K key = (K)stream.readObject();
/* 174 */       Collection<V> values = multimap.get(key);
/* 175 */       int valueCount = stream.readInt();
/* 176 */       for (int j = 0; j < valueCount; j++) {
/*     */         
/* 178 */         V value = (V)stream.readObject();
/* 179 */         values.add(value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> FieldSetter<T> getFieldSetter(Class<T> clazz, String fieldName) {
/*     */     try {
/* 187 */       Field field = clazz.getDeclaredField(fieldName);
/* 188 */       return new FieldSetter<>(field);
/* 189 */     } catch (NoSuchFieldException e) {
/* 190 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class FieldSetter<T>
/*     */   {
/*     */     private final Field field;
/*     */     
/*     */     private FieldSetter(Field field) {
/* 199 */       this.field = field;
/* 200 */       field.setAccessible(true);
/*     */     }
/*     */     
/*     */     void set(T instance, Object value) {
/*     */       try {
/* 205 */         this.field.set(instance, value);
/* 206 */       } catch (IllegalAccessException impossible) {
/* 207 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */     
/*     */     void set(T instance, int value) {
/*     */       try {
/* 213 */         this.field.set(instance, Integer.valueOf(value));
/* 214 */       } catch (IllegalAccessException impossible) {
/* 215 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Serialization.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */