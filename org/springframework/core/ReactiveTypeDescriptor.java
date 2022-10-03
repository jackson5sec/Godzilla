/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ReactiveTypeDescriptor
/*     */ {
/*     */   private final Class<?> reactiveType;
/*     */   private final boolean multiValue;
/*     */   private final boolean noValue;
/*     */   @Nullable
/*     */   private final Supplier<?> emptyValueSupplier;
/*     */   private final boolean deferred;
/*     */   
/*     */   private ReactiveTypeDescriptor(Class<?> reactiveType, boolean multiValue, boolean noValue, @Nullable Supplier<?> emptySupplier) {
/*  48 */     this(reactiveType, multiValue, noValue, emptySupplier, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ReactiveTypeDescriptor(Class<?> reactiveType, boolean multiValue, boolean noValue, @Nullable Supplier<?> emptySupplier, boolean deferred) {
/*  54 */     Assert.notNull(reactiveType, "'reactiveType' must not be null");
/*  55 */     this.reactiveType = reactiveType;
/*  56 */     this.multiValue = multiValue;
/*  57 */     this.noValue = noValue;
/*  58 */     this.emptyValueSupplier = emptySupplier;
/*  59 */     this.deferred = deferred;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getReactiveType() {
/*  67 */     return this.reactiveType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMultiValue() {
/*  77 */     return this.multiValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNoValue() {
/*  85 */     return this.noValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsEmpty() {
/*  92 */     return (this.emptyValueSupplier != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getEmptyValue() {
/* 100 */     Assert.state((this.emptyValueSupplier != null), "Empty values not supported");
/* 101 */     return this.emptyValueSupplier.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeferred() {
/* 111 */     return this.deferred;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 117 */     if (this == other) {
/* 118 */       return true;
/*     */     }
/* 120 */     if (other == null || getClass() != other.getClass()) {
/* 121 */       return false;
/*     */     }
/* 123 */     return this.reactiveType.equals(((ReactiveTypeDescriptor)other).reactiveType);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     return this.reactiveType.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReactiveTypeDescriptor multiValue(Class<?> type, Supplier<?> emptySupplier) {
/* 138 */     return new ReactiveTypeDescriptor(type, true, false, emptySupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReactiveTypeDescriptor singleOptionalValue(Class<?> type, Supplier<?> emptySupplier) {
/* 147 */     return new ReactiveTypeDescriptor(type, false, false, emptySupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReactiveTypeDescriptor singleRequiredValue(Class<?> type) {
/* 155 */     return new ReactiveTypeDescriptor(type, false, false, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReactiveTypeDescriptor noValue(Class<?> type, Supplier<?> emptySupplier) {
/* 164 */     return new ReactiveTypeDescriptor(type, false, true, emptySupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReactiveTypeDescriptor nonDeferredAsyncValue(Class<?> type, Supplier<?> emptySupplier) {
/* 175 */     return new ReactiveTypeDescriptor(type, false, false, emptySupplier, false);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\ReactiveTypeDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */