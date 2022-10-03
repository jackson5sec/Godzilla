/*     */ package org.springframework.util.function;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SingletonSupplier<T>
/*     */   implements Supplier<T>
/*     */ {
/*     */   @Nullable
/*     */   private final Supplier<? extends T> instanceSupplier;
/*     */   @Nullable
/*     */   private final Supplier<? extends T> defaultSupplier;
/*     */   @Nullable
/*     */   private volatile T singletonInstance;
/*     */   
/*     */   public SingletonSupplier(@Nullable T instance, Supplier<? extends T> defaultSupplier) {
/*  56 */     this.instanceSupplier = null;
/*  57 */     this.defaultSupplier = defaultSupplier;
/*  58 */     this.singletonInstance = instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SingletonSupplier(@Nullable Supplier<? extends T> instanceSupplier, Supplier<? extends T> defaultSupplier) {
/*  68 */     this.instanceSupplier = instanceSupplier;
/*  69 */     this.defaultSupplier = defaultSupplier;
/*     */   }
/*     */   
/*     */   private SingletonSupplier(Supplier<? extends T> supplier) {
/*  73 */     this.instanceSupplier = supplier;
/*  74 */     this.defaultSupplier = null;
/*     */   }
/*     */   
/*     */   private SingletonSupplier(T singletonInstance) {
/*  78 */     this.instanceSupplier = null;
/*  79 */     this.defaultSupplier = null;
/*  80 */     this.singletonInstance = singletonInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T get() {
/*  91 */     T instance = this.singletonInstance;
/*  92 */     if (instance == null) {
/*  93 */       synchronized (this) {
/*  94 */         instance = this.singletonInstance;
/*  95 */         if (instance == null) {
/*  96 */           if (this.instanceSupplier != null) {
/*  97 */             instance = this.instanceSupplier.get();
/*     */           }
/*  99 */           if (instance == null && this.defaultSupplier != null) {
/* 100 */             instance = this.defaultSupplier.get();
/*     */           }
/* 102 */           this.singletonInstance = instance;
/*     */         } 
/*     */       } 
/*     */     }
/* 106 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T obtain() {
/* 115 */     T instance = get();
/* 116 */     Assert.state((instance != null), "No instance from Supplier");
/* 117 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> SingletonSupplier<T> of(T instance) {
/* 127 */     return new SingletonSupplier<>(instance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static <T> SingletonSupplier<T> ofNullable(@Nullable T instance) {
/* 137 */     return (instance != null) ? new SingletonSupplier<>(instance) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> SingletonSupplier<T> of(Supplier<T> supplier) {
/* 146 */     return new SingletonSupplier<>(supplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static <T> SingletonSupplier<T> ofNullable(@Nullable Supplier<T> supplier) {
/* 156 */     return (supplier != null) ? new SingletonSupplier<>(supplier) : null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\function\SingletonSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */