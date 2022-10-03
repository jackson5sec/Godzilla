/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Comparators
/*     */ {
/*     */   @Beta
/*     */   public static <T, S extends T> Comparator<Iterable<S>> lexicographical(Comparator<T> comparator) {
/*  65 */     return new LexicographicalOrdering<>((Comparator<? super S>)Preconditions.checkNotNull(comparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <T> boolean isInOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
/*  75 */     Preconditions.checkNotNull(comparator);
/*  76 */     Iterator<? extends T> it = iterable.iterator();
/*  77 */     if (it.hasNext()) {
/*  78 */       T prev = it.next();
/*  79 */       while (it.hasNext()) {
/*  80 */         T next = it.next();
/*  81 */         if (comparator.compare(prev, next) > 0) {
/*  82 */           return false;
/*     */         }
/*  84 */         prev = next;
/*     */       } 
/*     */     } 
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <T> boolean isInStrictOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
/*  98 */     Preconditions.checkNotNull(comparator);
/*  99 */     Iterator<? extends T> it = iterable.iterator();
/* 100 */     if (it.hasNext()) {
/* 101 */       T prev = it.next();
/* 102 */       while (it.hasNext()) {
/* 103 */         T next = it.next();
/* 104 */         if (comparator.compare(prev, next) >= 0) {
/* 105 */           return false;
/*     */         }
/* 107 */         prev = next;
/*     */       } 
/*     */     } 
/* 110 */     return true;
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
/*     */   public static <T> Collector<T, ?, List<T>> least(int k, Comparator<? super T> comparator) {
/* 134 */     CollectPreconditions.checkNonnegative(k, "k");
/* 135 */     Preconditions.checkNotNull(comparator);
/* 136 */     return Collector.of(() -> TopKSelector.least(k, comparator), TopKSelector::offer, TopKSelector::combine, TopKSelector::topK, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*     */   public static <T> Collector<T, ?, List<T>> greatest(int k, Comparator<? super T> comparator) {
/* 165 */     return least(k, comparator.reversed());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <T> Comparator<Optional<T>> emptiesFirst(Comparator<? super T> valueComparator) {
/* 177 */     Preconditions.checkNotNull(valueComparator);
/* 178 */     return Comparator.comparing(o -> o.orElse(null), Comparator.nullsFirst(valueComparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <T> Comparator<Optional<T>> emptiesLast(Comparator<? super T> valueComparator) {
/* 190 */     Preconditions.checkNotNull(valueComparator);
/* 191 */     return Comparator.comparing(o -> o.orElse(null), Comparator.nullsLast(valueComparator));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Comparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */