/*     */ package org.springframework.util.comparator;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class InvertibleComparator<T>
/*     */   implements Comparator<T>, Serializable
/*     */ {
/*     */   private final Comparator<T> comparator;
/*     */   private boolean ascending = true;
/*     */   
/*     */   public InvertibleComparator(Comparator<T> comparator) {
/*  52 */     Assert.notNull(comparator, "Comparator must not be null");
/*  53 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InvertibleComparator(Comparator<T> comparator, boolean ascending) {
/*  63 */     Assert.notNull(comparator, "Comparator must not be null");
/*  64 */     this.comparator = comparator;
/*  65 */     setAscending(ascending);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAscending(boolean ascending) {
/*  73 */     this.ascending = ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAscending() {
/*  80 */     return this.ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invertOrder() {
/*  88 */     this.ascending = !this.ascending;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(T o1, T o2) {
/*  94 */     int result = this.comparator.compare(o1, o2);
/*  95 */     if (result != 0) {
/*     */       
/*  97 */       if (!this.ascending) {
/*  98 */         if (Integer.MIN_VALUE == result) {
/*  99 */           result = Integer.MAX_VALUE;
/*     */         } else {
/*     */           
/* 102 */           result *= -1;
/*     */         } 
/*     */       }
/* 105 */       return result;
/*     */     } 
/* 107 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 113 */     if (this == other) {
/* 114 */       return true;
/*     */     }
/* 116 */     if (!(other instanceof InvertibleComparator)) {
/* 117 */       return false;
/*     */     }
/* 119 */     InvertibleComparator<T> otherComp = (InvertibleComparator<T>)other;
/* 120 */     return (this.comparator.equals(otherComp.comparator) && this.ascending == otherComp.ascending);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 125 */     return this.comparator.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     return "InvertibleComparator: [" + this.comparator + "]; ascending=" + this.ascending;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\comparator\InvertibleComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */