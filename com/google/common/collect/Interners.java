/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class Interners
/*     */ {
/*     */   public static class InternerBuilder
/*     */   {
/*  44 */     private final MapMaker mapMaker = new MapMaker();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean strong = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InternerBuilder strong() {
/*  55 */       this.strong = true;
/*  56 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible("java.lang.ref.WeakReference")
/*     */     public InternerBuilder weak() {
/*  66 */       this.strong = false;
/*  67 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InternerBuilder concurrencyLevel(int concurrencyLevel) {
/*  76 */       this.mapMaker.concurrencyLevel(concurrencyLevel);
/*  77 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Interner<E> build() {
/*  81 */       if (!this.strong) {
/*  82 */         this.mapMaker.weakKeys();
/*     */       }
/*  84 */       return new Interners.InternerImpl<>(this.mapMaker);
/*     */     }
/*     */     
/*     */     private InternerBuilder() {} }
/*     */   
/*     */   public static InternerBuilder newBuilder() {
/*  90 */     return new InternerBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Interner<E> newStrongInterner() {
/*  99 */     return newBuilder().strong().build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/*     */   public static <E> Interner<E> newWeakInterner() {
/* 110 */     return newBuilder().weak().build();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class InternerImpl<E> implements Interner<E> {
/*     */     @VisibleForTesting
/*     */     final MapMakerInternalMap<E, MapMaker.Dummy, ?, ?> map;
/*     */     
/*     */     private InternerImpl(MapMaker mapMaker) {
/* 119 */       this
/* 120 */         .map = MapMakerInternalMap.createWithDummyValues(mapMaker.keyEquivalence(Equivalence.equals()));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E intern(E sample) {
/*     */       while (true) {
/* 127 */         MapMakerInternalMap.InternalEntry<E, MapMaker.Dummy, ?> entry = (MapMakerInternalMap.InternalEntry<E, MapMaker.Dummy, ?>)this.map.getEntry(sample);
/* 128 */         if (entry != null) {
/* 129 */           E canonical = entry.getKey();
/* 130 */           if (canonical != null) {
/* 131 */             return canonical;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 136 */         MapMaker.Dummy sneaky = this.map.putIfAbsent(sample, MapMaker.Dummy.VALUE);
/* 137 */         if (sneaky == null) {
/* 138 */           return sample;
/*     */         }
/*     */       } 
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
/*     */   public static <E> Function<E, E> asFunction(Interner<E> interner) {
/* 157 */     return new InternerFunction<>((Interner<E>)Preconditions.checkNotNull(interner));
/*     */   }
/*     */   
/*     */   private static class InternerFunction<E>
/*     */     implements Function<E, E> {
/*     */     private final Interner<E> interner;
/*     */     
/*     */     public InternerFunction(Interner<E> interner) {
/* 165 */       this.interner = interner;
/*     */     }
/*     */ 
/*     */     
/*     */     public E apply(E input) {
/* 170 */       return this.interner.intern(input);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 175 */       return this.interner.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 180 */       if (other instanceof InternerFunction) {
/* 181 */         InternerFunction<?> that = (InternerFunction)other;
/* 182 */         return this.interner.equals(that.interner);
/*     */       } 
/*     */       
/* 185 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Interners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */