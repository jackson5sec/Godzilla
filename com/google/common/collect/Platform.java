/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class Platform
/*     */ {
/*     */   static <K, V> Map<K, V> newHashMapWithExpectedSize(int expectedSize) {
/*  34 */     return Maps.newHashMapWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> Map<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
/*  42 */     return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> Set<E> newHashSetWithExpectedSize(int expectedSize) {
/*  47 */     return Sets.newHashSetWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Set<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
/*  55 */     return Sets.newLinkedHashSetWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> Map<K, V> preservesInsertionOrderOnPutsMap() {
/*  63 */     return Maps.newLinkedHashMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Set<E> preservesInsertionOrderOnAddsSet() {
/*  71 */     return Sets.newLinkedHashSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> T[] newArray(T[] reference, int length) {
/*  81 */     Class<?> type = reference.getClass().getComponentType();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     T[] result = (T[])Array.newInstance(type, length);
/*  87 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> T[] copy(Object[] source, int from, int to, T[] arrayOfType) {
/*  92 */     return Arrays.copyOfRange(source, from, to, (Class)arrayOfType.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MapMaker tryWeakKeys(MapMaker mapMaker) {
/* 101 */     return mapMaker.weakKeys();
/*     */   }
/*     */   
/*     */   static int reduceIterationsIfGwt(int iterations) {
/* 105 */     return iterations;
/*     */   }
/*     */   
/*     */   static int reduceExponentIfGwt(int exponent) {
/* 109 */     return exponent;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */