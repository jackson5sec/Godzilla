/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.io.Serializable;
/*     */ import java.util.function.BiPredicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Equivalence<T>
/*     */   implements BiPredicate<T, T>
/*     */ {
/*     */   public final boolean equivalent(T a, T b) {
/*  59 */     if (a == b) {
/*  60 */       return true;
/*     */     }
/*  62 */     if (a == null || b == null) {
/*  63 */       return false;
/*     */     }
/*  65 */     return doEquivalent(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean test(T t, T u) {
/*  76 */     return equivalent(t, u);
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
/*     */   @ForOverride
/*     */   protected abstract boolean doEquivalent(T paramT1, T paramT2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hash(T t) {
/* 109 */     if (t == null) {
/* 110 */       return 0;
/*     */     }
/* 112 */     return doHash(t);
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
/*     */   @ForOverride
/*     */   protected abstract int doHash(T paramT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) {
/* 151 */     return new FunctionalEquivalence<>(function, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <S extends T> Wrapper<S> wrap(S reference) {
/* 162 */     return new Wrapper<>(this, reference);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Wrapper<T>
/*     */     implements Serializable
/*     */   {
/*     */     private final Equivalence<? super T> equivalence;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final T reference;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Wrapper(Equivalence<? super T> equivalence, T reference) {
/* 190 */       this.equivalence = Preconditions.<Equivalence<? super T>>checkNotNull(equivalence);
/* 191 */       this.reference = reference;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() {
/* 196 */       return this.reference;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 206 */       if (obj == this) {
/* 207 */         return true;
/*     */       }
/* 209 */       if (obj instanceof Wrapper) {
/* 210 */         Wrapper<?> that = (Wrapper)obj;
/*     */         
/* 212 */         if (this.equivalence.equals(that.equivalence)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 218 */           Equivalence<Object> equivalence = (Equivalence)this.equivalence;
/* 219 */           return equivalence.equivalent(this.reference, that.reference);
/*     */         } 
/*     */       } 
/* 222 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 228 */       return this.equivalence.hash(this.reference);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 237 */       return this.equivalence + ".wrap(" + this.reference + ")";
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public final <S extends T> Equivalence<Iterable<S>> pairwise() {
/* 258 */     return new PairwiseEquivalence<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Predicate<T> equivalentTo(T target) {
/* 268 */     return new EquivalentToPredicate<>(this, target);
/*     */   }
/*     */   
/*     */   private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {
/*     */     private final Equivalence<T> equivalence;
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EquivalentToPredicate(Equivalence<T> equivalence, T target) {
/* 277 */       this.equivalence = Preconditions.<Equivalence<T>>checkNotNull(equivalence);
/* 278 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(T input) {
/* 283 */       return this.equivalence.equivalent(input, this.target);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 288 */       if (this == obj) {
/* 289 */         return true;
/*     */       }
/* 291 */       if (obj instanceof EquivalentToPredicate) {
/* 292 */         EquivalentToPredicate<?> that = (EquivalentToPredicate)obj;
/* 293 */         return (this.equivalence.equals(that.equivalence) && Objects.equal(this.target, that.target));
/*     */       } 
/* 295 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 300 */       return Objects.hashCode(new Object[] { this.equivalence, this.target });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 305 */       return this.equivalence + ".equivalentTo(" + this.target + ")";
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
/*     */   public static Equivalence<Object> equals() {
/* 322 */     return Equals.INSTANCE;
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
/*     */   public static Equivalence<Object> identity() {
/* 334 */     return Identity.INSTANCE;
/*     */   }
/*     */   
/*     */   static final class Equals
/*     */     extends Equivalence<Object> implements Serializable {
/* 339 */     static final Equals INSTANCE = new Equals();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 343 */       return a.equals(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int doHash(Object o) {
/* 348 */       return o.hashCode();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 352 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Identity
/*     */     extends Equivalence<Object>
/*     */     implements Serializable
/*     */   {
/* 360 */     static final Identity INSTANCE = new Identity();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 364 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int doHash(Object o) {
/* 369 */       return System.identityHashCode(o);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 373 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Equivalence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */