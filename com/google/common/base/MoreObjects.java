/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class MoreObjects
/*     */ {
/*     */   public static <T> T firstNonNull(T first, T second) {
/*  55 */     if (first != null) {
/*  56 */       return first;
/*     */     }
/*  58 */     if (second != null) {
/*  59 */       return second;
/*     */     }
/*  61 */     throw new NullPointerException("Both parameters are null");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ToStringHelper toStringHelper(Object self) {
/* 105 */     return new ToStringHelper(self.getClass().getSimpleName());
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
/*     */   public static ToStringHelper toStringHelper(Class<?> clazz) {
/* 119 */     return new ToStringHelper(clazz.getSimpleName());
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
/*     */   public static ToStringHelper toStringHelper(String className) {
/* 131 */     return new ToStringHelper(className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ToStringHelper
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/* 142 */     private final ValueHolder holderHead = new ValueHolder();
/* 143 */     private ValueHolder holderTail = this.holderHead;
/*     */     
/*     */     private boolean omitNullValues = false;
/*     */     
/*     */     private ToStringHelper(String className) {
/* 148 */       this.className = Preconditions.<String>checkNotNull(className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper omitNullValues() {
/* 160 */       this.omitNullValues = true;
/* 161 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, Object value) {
/* 171 */       return addHolder(name, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, boolean value) {
/* 181 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, char value) {
/* 191 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, double value) {
/* 201 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, float value) {
/* 211 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, int value) {
/* 221 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, long value) {
/* 231 */       return addHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(Object value) {
/* 242 */       return addHolder(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(boolean value) {
/* 255 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(char value) {
/* 268 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(double value) {
/* 281 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(float value) {
/* 294 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(int value) {
/* 307 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(long value) {
/* 320 */       return addHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 334 */       boolean omitNullValuesSnapshot = this.omitNullValues;
/* 335 */       String nextSeparator = "";
/* 336 */       StringBuilder builder = (new StringBuilder(32)).append(this.className).append('{');
/* 337 */       ValueHolder valueHolder = this.holderHead.next;
/* 338 */       for (; valueHolder != null; 
/* 339 */         valueHolder = valueHolder.next) {
/* 340 */         Object value = valueHolder.value;
/* 341 */         if (!omitNullValuesSnapshot || value != null) {
/* 342 */           builder.append(nextSeparator);
/* 343 */           nextSeparator = ", ";
/*     */           
/* 345 */           if (valueHolder.name != null) {
/* 346 */             builder.append(valueHolder.name).append('=');
/*     */           }
/* 348 */           if (value != null && value.getClass().isArray()) {
/* 349 */             Object[] objectArray = { value };
/* 350 */             String arrayString = Arrays.deepToString(objectArray);
/* 351 */             builder.append(arrayString, 1, arrayString.length() - 1);
/*     */           } else {
/* 353 */             builder.append(value);
/*     */           } 
/*     */         } 
/*     */       } 
/* 357 */       return builder.append('}').toString();
/*     */     }
/*     */     
/*     */     private ValueHolder addHolder() {
/* 361 */       ValueHolder valueHolder = new ValueHolder();
/* 362 */       this.holderTail = this.holderTail.next = valueHolder;
/* 363 */       return valueHolder;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(Object value) {
/* 367 */       ValueHolder valueHolder = addHolder();
/* 368 */       valueHolder.value = value;
/* 369 */       return this;
/*     */     }
/*     */     
/*     */     private ToStringHelper addHolder(String name, Object value) {
/* 373 */       ValueHolder valueHolder = addHolder();
/* 374 */       valueHolder.value = value;
/* 375 */       valueHolder.name = Preconditions.<String>checkNotNull(name);
/* 376 */       return this;
/*     */     }
/*     */     
/*     */     private static final class ValueHolder {
/*     */       String name;
/*     */       Object value;
/*     */       ValueHolder next;
/*     */       
/*     */       private ValueHolder() {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\MoreObjects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */