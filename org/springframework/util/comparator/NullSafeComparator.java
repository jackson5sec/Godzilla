/*     */ package org.springframework.util.comparator;
/*     */ 
/*     */ import java.util.Comparator;
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
/*     */ public class NullSafeComparator<T>
/*     */   implements Comparator<T>
/*     */ {
/*  42 */   public static final NullSafeComparator NULLS_LOW = new NullSafeComparator(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public static final NullSafeComparator NULLS_HIGH = new NullSafeComparator(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Comparator<T> nonNullComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean nullsLow;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NullSafeComparator(boolean nullsLow) {
/*  74 */     this.nonNullComparator = ComparableComparator.INSTANCE;
/*  75 */     this.nullsLow = nullsLow;
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
/*     */   public NullSafeComparator(Comparator<T> comparator, boolean nullsLow) {
/*  88 */     Assert.notNull(comparator, "Non-null Comparator is required");
/*  89 */     this.nonNullComparator = comparator;
/*  90 */     this.nullsLow = nullsLow;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(@Nullable T o1, @Nullable T o2) {
/*  96 */     if (o1 == o2) {
/*  97 */       return 0;
/*     */     }
/*  99 */     if (o1 == null) {
/* 100 */       return this.nullsLow ? -1 : 1;
/*     */     }
/* 102 */     if (o2 == null) {
/* 103 */       return this.nullsLow ? 1 : -1;
/*     */     }
/* 105 */     return this.nonNullComparator.compare(o1, o2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 112 */     if (this == other) {
/* 113 */       return true;
/*     */     }
/* 115 */     if (!(other instanceof NullSafeComparator)) {
/* 116 */       return false;
/*     */     }
/* 118 */     NullSafeComparator<T> otherComp = (NullSafeComparator<T>)other;
/* 119 */     return (this.nonNullComparator.equals(otherComp.nonNullComparator) && this.nullsLow == otherComp.nullsLow);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 124 */     return this.nonNullComparator.hashCode() * (this.nullsLow ? -1 : 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     return "NullSafeComparator: non-null comparator [" + this.nonNullComparator + "]; " + (this.nullsLow ? "nulls low" : "nulls high");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\comparator\NullSafeComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */