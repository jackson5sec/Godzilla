/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Enums
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static Field getField(Enum<?> enumValue) {
/*  50 */     Class<?> clazz = enumValue.getDeclaringClass();
/*     */     try {
/*  52 */       return clazz.getDeclaredField(enumValue.name());
/*  53 */     } catch (NoSuchFieldException impossible) {
/*  54 */       throw new AssertionError(impossible);
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
/*     */   public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> enumClass, String value) {
/*  67 */     Preconditions.checkNotNull(enumClass);
/*  68 */     Preconditions.checkNotNull(value);
/*  69 */     return Platform.getEnumIfPresent(enumClass, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*  74 */   private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> enumConstantCache = new WeakHashMap<>();
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(Class<T> enumClass) {
/*  79 */     Map<String, WeakReference<? extends Enum<?>>> result = new HashMap<>();
/*  80 */     for (Enum<?> enum_ : EnumSet.<T>allOf(enumClass)) {
/*  81 */       result.put(enum_.name(), new WeakReference<>(enum_));
/*     */     }
/*  83 */     enumConstantCache.put(enumClass, result);
/*  84 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> getEnumConstants(Class<T> enumClass) {
/*  90 */     synchronized (enumConstantCache) {
/*  91 */       Map<String, WeakReference<? extends Enum<?>>> constants = enumConstantCache.get(enumClass);
/*  92 */       if (constants == null) {
/*  93 */         constants = populateCache(enumClass);
/*     */       }
/*  95 */       return constants;
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
/*     */   public static <T extends Enum<T>> Converter<String, T> stringConverter(Class<T> enumClass) {
/* 108 */     return new StringConverter<>(enumClass);
/*     */   }
/*     */   
/*     */   private static final class StringConverter<T extends Enum<T>>
/*     */     extends Converter<String, T> implements Serializable {
/*     */     private final Class<T> enumClass;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(Class<T> enumClass) {
/* 117 */       this.enumClass = Preconditions.<Class<T>>checkNotNull(enumClass);
/*     */     }
/*     */ 
/*     */     
/*     */     protected T doForward(String value) {
/* 122 */       return Enum.valueOf(this.enumClass, value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(T enumValue) {
/* 127 */       return enumValue.name();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 132 */       if (object instanceof StringConverter) {
/* 133 */         StringConverter<?> that = (StringConverter)object;
/* 134 */         return this.enumClass.equals(that.enumClass);
/*     */       } 
/* 136 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 141 */       return this.enumClass.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 146 */       return "Enums.stringConverter(" + this.enumClass.getName() + ".class)";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Enums.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */