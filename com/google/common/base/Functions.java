/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Functions
/*     */ {
/*     */   public static Function<Object, String> toStringFunction() {
/*  60 */     return ToStringFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum ToStringFunction
/*     */     implements Function<Object, String> {
/*  65 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public String apply(Object o) {
/*  69 */       Preconditions.checkNotNull(o);
/*  70 */       return o.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  75 */       return "Functions.toStringFunction()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Function<E, E> identity() {
/*  83 */     return IdentityFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum IdentityFunction
/*     */     implements Function<Object, Object> {
/*  88 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public Object apply(Object o) {
/*  92 */       return o;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  97 */       return "Functions.identity()";
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
/*     */   public static <K, V> Function<K, V> forMap(Map<K, V> map) {
/* 115 */     return new FunctionForMapNoDefault<>(map);
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
/*     */   public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, V defaultValue) {
/* 132 */     return new ForMapWithDefault<>(map, defaultValue);
/*     */   }
/*     */   
/*     */   private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
/*     */     final Map<K, V> map;
/*     */     
/*     */     FunctionForMapNoDefault(Map<K, V> map) {
/* 139 */       this.map = Preconditions.<Map<K, V>>checkNotNull(map);
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public V apply(K key) {
/* 144 */       V result = this.map.get(key);
/* 145 */       Preconditions.checkArgument((result != null || this.map.containsKey(key)), "Key '%s' not present in map", key);
/* 146 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 151 */       if (o instanceof FunctionForMapNoDefault) {
/* 152 */         FunctionForMapNoDefault<?, ?> that = (FunctionForMapNoDefault<?, ?>)o;
/* 153 */         return this.map.equals(that.map);
/*     */       } 
/* 155 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 160 */       return this.map.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 165 */       return "Functions.forMap(" + this.map + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ForMapWithDefault<K, V>
/*     */     implements Function<K, V>, Serializable {
/*     */     final Map<K, ? extends V> map;
/*     */     final V defaultValue;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ForMapWithDefault(Map<K, ? extends V> map, V defaultValue) {
/* 176 */       this.map = Preconditions.<Map<K, ? extends V>>checkNotNull(map);
/* 177 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public V apply(K key) {
/* 182 */       V result = this.map.get(key);
/* 183 */       return (result != null || this.map.containsKey(key)) ? result : this.defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 188 */       if (o instanceof ForMapWithDefault) {
/* 189 */         ForMapWithDefault<?, ?> that = (ForMapWithDefault<?, ?>)o;
/* 190 */         return (this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue));
/*     */       } 
/* 192 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 197 */       return Objects.hashCode(new Object[] { this.map, this.defaultValue });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 203 */       return "Functions.forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")";
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
/*     */   public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
/* 222 */     return new FunctionComposition<>(g, f);
/*     */   }
/*     */   
/*     */   private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable { private final Function<B, C> g;
/*     */     private final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
/* 230 */       this.g = Preconditions.<Function<B, C>>checkNotNull(g);
/* 231 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */ 
/*     */     
/*     */     public C apply(A a) {
/* 236 */       return this.g.apply(this.f.apply(a));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 241 */       if (obj instanceof FunctionComposition) {
/* 242 */         FunctionComposition<?, ?, ?> that = (FunctionComposition<?, ?, ?>)obj;
/* 243 */         return (this.f.equals(that.f) && this.g.equals(that.g));
/*     */       } 
/* 245 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 250 */       return this.f.hashCode() ^ this.g.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 256 */       return this.g + "(" + this.f + ")";
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
/*     */   public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
/* 271 */     return new PredicateFunction<>(predicate);
/*     */   }
/*     */   
/*     */   private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
/*     */     private final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private PredicateFunction(Predicate<T> predicate) {
/* 279 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean apply(T t) {
/* 284 */       return Boolean.valueOf(this.predicate.apply(t));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 289 */       if (obj instanceof PredicateFunction) {
/* 290 */         PredicateFunction<?> that = (PredicateFunction)obj;
/* 291 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 293 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 298 */       return this.predicate.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 303 */       return "Functions.forPredicate(" + this.predicate + ")";
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
/*     */   public static <E> Function<Object, E> constant(E value) {
/* 318 */     return new ConstantFunction<>(value);
/*     */   }
/*     */   
/*     */   private static class ConstantFunction<E> implements Function<Object, E>, Serializable { private final E value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public ConstantFunction(E value) {
/* 325 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public E apply(Object from) {
/* 330 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 335 */       if (obj instanceof ConstantFunction) {
/* 336 */         ConstantFunction<?> that = (ConstantFunction)obj;
/* 337 */         return Objects.equal(this.value, that.value);
/*     */       } 
/* 339 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 344 */       return (this.value == null) ? 0 : this.value.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 349 */       return "Functions.constant(" + this.value + ")";
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
/*     */   public static <T> Function<Object, T> forSupplier(Supplier<T> supplier) {
/* 363 */     return new SupplierFunction<>(supplier);
/*     */   }
/*     */   
/*     */   private static class SupplierFunction<T>
/*     */     implements Function<Object, T>, Serializable {
/*     */     private final Supplier<T> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private SupplierFunction(Supplier<T> supplier) {
/* 372 */       this.supplier = Preconditions.<Supplier<T>>checkNotNull(supplier);
/*     */     }
/*     */ 
/*     */     
/*     */     public T apply(Object input) {
/* 377 */       return this.supplier.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 382 */       if (obj instanceof SupplierFunction) {
/* 383 */         SupplierFunction<?> that = (SupplierFunction)obj;
/* 384 */         return this.supplier.equals(that.supplier);
/*     */       } 
/* 386 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 391 */       return this.supplier.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 396 */       return "Functions.forSupplier(" + this.supplier + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Functions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */