/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ObjectArrays
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static <T> T[] newArray(Class<T> type, int length) {
/*  49 */     return (T[])Array.newInstance(type, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T[] newArray(T[] reference, int length) {
/*  59 */     return Platform.newArray(reference, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <T> T[] concat(T[] first, T[] second, Class<T> type) {
/*  71 */     T[] result = newArray(type, first.length + second.length);
/*  72 */     System.arraycopy(first, 0, result, 0, first.length);
/*  73 */     System.arraycopy(second, 0, result, first.length, second.length);
/*  74 */     return result;
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
/*     */   public static <T> T[] concat(T element, T[] array) {
/*  86 */     T[] result = newArray(array, array.length + 1);
/*  87 */     result[0] = element;
/*  88 */     System.arraycopy(array, 0, result, 1, array.length);
/*  89 */     return result;
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
/*     */   public static <T> T[] concat(T[] array, T element) {
/* 101 */     T[] result = Arrays.copyOf(array, array.length + 1);
/* 102 */     result[array.length] = element;
/* 103 */     return result;
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
/*     */   static <T> T[] toArrayImpl(Collection<?> c, T[] array) {
/* 128 */     int size = c.size();
/* 129 */     if (array.length < size) {
/* 130 */       array = newArray(array, size);
/*     */     }
/* 132 */     fillArray(c, (Object[])array);
/* 133 */     if (array.length > size) {
/* 134 */       array[size] = null;
/*     */     }
/* 136 */     return array;
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
/*     */   static <T> T[] toArrayImpl(Object[] src, int offset, int len, T[] dst) {
/* 151 */     Preconditions.checkPositionIndexes(offset, offset + len, src.length);
/* 152 */     if (dst.length < len) {
/* 153 */       dst = newArray(dst, len);
/* 154 */     } else if (dst.length > len) {
/* 155 */       dst[len] = null;
/*     */     } 
/* 157 */     System.arraycopy(src, offset, dst, 0, len);
/* 158 */     return dst;
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
/*     */   static Object[] toArrayImpl(Collection<?> c) {
/* 174 */     return fillArray(c, new Object[c.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object[] copyAsObjectArray(Object[] elements, int offset, int length) {
/* 182 */     Preconditions.checkPositionIndexes(offset, offset + length, elements.length);
/* 183 */     if (length == 0) {
/* 184 */       return new Object[0];
/*     */     }
/* 186 */     Object[] result = new Object[length];
/* 187 */     System.arraycopy(elements, offset, result, 0, length);
/* 188 */     return result;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private static Object[] fillArray(Iterable<?> elements, Object[] array) {
/* 193 */     int i = 0;
/* 194 */     for (Object element : elements) {
/* 195 */       array[i++] = element;
/*     */     }
/* 197 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   static void swap(Object[] array, int i, int j) {
/* 202 */     Object temp = array[i];
/* 203 */     array[i] = array[j];
/* 204 */     array[j] = temp;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object[] checkElementsNotNull(Object... array) {
/* 209 */     return checkElementsNotNull(array, array.length);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object[] checkElementsNotNull(Object[] array, int length) {
/* 214 */     for (int i = 0; i < length; i++) {
/* 215 */       checkElementNotNull(array[i], i);
/*     */     }
/* 217 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object checkElementNotNull(Object element, int index) {
/* 224 */     if (element == null) {
/* 225 */       throw new NullPointerException("at index " + index);
/*     */     }
/* 227 */     return element;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ObjectArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */