/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Suppliers
/*     */ {
/*     */   public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
/*  46 */     return new SupplierComposition<>(function, supplier);
/*     */   }
/*     */   
/*     */   private static class SupplierComposition<F, T>
/*     */     implements Supplier<T>, Serializable {
/*     */     final Function<? super F, T> function;
/*     */     
/*     */     SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
/*  54 */       this.function = Preconditions.<Function<? super F, T>>checkNotNull(function);
/*  55 */       this.supplier = Preconditions.<Supplier<F>>checkNotNull(supplier);
/*     */     }
/*     */     final Supplier<F> supplier; private static final long serialVersionUID = 0L;
/*     */     
/*     */     public T get() {
/*  60 */       return this.function.apply(this.supplier.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/*  65 */       if (obj instanceof SupplierComposition) {
/*  66 */         SupplierComposition<?, ?> that = (SupplierComposition<?, ?>)obj;
/*  67 */         return (this.function.equals(that.function) && this.supplier.equals(that.supplier));
/*     */       } 
/*  69 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  74 */       return Objects.hashCode(new Object[] { this.function, this.supplier });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  79 */       return "Suppliers.compose(" + this.function + ", " + this.supplier + ")";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> memoize(Supplier<T> delegate) {
/* 102 */     if (delegate instanceof NonSerializableMemoizingSupplier || delegate instanceof MemoizingSupplier)
/*     */     {
/* 104 */       return delegate;
/*     */     }
/* 106 */     return (delegate instanceof Serializable) ? new MemoizingSupplier<>(delegate) : new NonSerializableMemoizingSupplier<>(delegate);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class MemoizingSupplier<T>
/*     */     implements Supplier<T>, Serializable
/*     */   {
/*     */     final Supplier<T> delegate;
/*     */     volatile transient boolean initialized;
/*     */     transient T value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     MemoizingSupplier(Supplier<T> delegate) {
/* 120 */       this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public T get() {
/* 126 */       if (!this.initialized) {
/* 127 */         synchronized (this) {
/* 128 */           if (!this.initialized) {
/* 129 */             T t = this.delegate.get();
/* 130 */             this.value = t;
/* 131 */             this.initialized = true;
/* 132 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/* 136 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 141 */       return "Suppliers.memoize(" + (this.initialized ? ("<supplier that returned " + this.value + ">") : (String)this.delegate) + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class NonSerializableMemoizingSupplier<T>
/*     */     implements Supplier<T>
/*     */   {
/*     */     volatile Supplier<T> delegate;
/*     */     
/*     */     volatile boolean initialized;
/*     */     
/*     */     T value;
/*     */ 
/*     */     
/*     */     NonSerializableMemoizingSupplier(Supplier<T> delegate) {
/* 158 */       this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public T get() {
/* 164 */       if (!this.initialized) {
/* 165 */         synchronized (this) {
/* 166 */           if (!this.initialized) {
/* 167 */             T t = this.delegate.get();
/* 168 */             this.value = t;
/* 169 */             this.initialized = true;
/*     */             
/* 171 */             this.delegate = null;
/* 172 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/* 176 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 181 */       Supplier<T> delegate = this.delegate;
/* 182 */       return "Suppliers.memoize(" + ((delegate == null) ? ("<supplier that returned " + this.value + ">") : (String)delegate) + ")";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
/* 212 */     return new ExpiringMemoizingSupplier<>(delegate, duration, unit);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ExpiringMemoizingSupplier<T>
/*     */     implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     final long durationNanos;
/*     */     volatile transient T value;
/*     */     volatile transient long expirationNanos;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
/* 225 */       this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
/* 226 */       this.durationNanos = unit.toNanos(duration);
/* 227 */       Preconditions.checkArgument((duration > 0L), "duration (%s %s) must be > 0", duration, unit);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T get() {
/* 238 */       long nanos = this.expirationNanos;
/* 239 */       long now = Platform.systemNanoTime();
/* 240 */       if (nanos == 0L || now - nanos >= 0L) {
/* 241 */         synchronized (this) {
/* 242 */           if (nanos == this.expirationNanos) {
/* 243 */             T t = this.delegate.get();
/* 244 */             this.value = t;
/* 245 */             nanos = now + this.durationNanos;
/*     */ 
/*     */             
/* 248 */             this.expirationNanos = (nanos == 0L) ? 1L : nanos;
/* 249 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/* 253 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 260 */       return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> ofInstance(T instance) {
/* 268 */     return new SupplierOfInstance<>(instance);
/*     */   }
/*     */   
/*     */   private static class SupplierOfInstance<T> implements Supplier<T>, Serializable { final T instance;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SupplierOfInstance(T instance) {
/* 275 */       this.instance = instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() {
/* 280 */       return this.instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 285 */       if (obj instanceof SupplierOfInstance) {
/* 286 */         SupplierOfInstance<?> that = (SupplierOfInstance)obj;
/* 287 */         return Objects.equal(this.instance, that.instance);
/*     */       } 
/* 289 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 294 */       return Objects.hashCode(new Object[] { this.instance });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 299 */       return "Suppliers.ofInstance(" + this.instance + ")";
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
/* 310 */     return new ThreadSafeSupplier<>(delegate);
/*     */   }
/*     */   
/*     */   private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable { final Supplier<T> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ThreadSafeSupplier(Supplier<T> delegate) {
/* 317 */       this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() {
/* 322 */       synchronized (this.delegate) {
/* 323 */         return this.delegate.get();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 329 */       return "Suppliers.synchronizedSupplier(" + this.delegate + ")";
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Function<Supplier<T>, T> supplierFunction() {
/* 345 */     SupplierFunction<T> sf = SupplierFunctionImpl.INSTANCE;
/* 346 */     return sf;
/*     */   }
/*     */   
/*     */   private static interface SupplierFunction<T> extends Function<Supplier<T>, T> {}
/*     */   
/*     */   private enum SupplierFunctionImpl implements SupplierFunction<Object> {
/* 352 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     public Object apply(Supplier<Object> input) {
/* 357 */       return input.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 362 */       return "Suppliers.supplierFunction()";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Suppliers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */