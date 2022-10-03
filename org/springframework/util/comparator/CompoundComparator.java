/*     */ package org.springframework.util.comparator;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class CompoundComparator<T>
/*     */   implements Comparator<T>, Serializable
/*     */ {
/*     */   private final List<InvertibleComparator> comparators;
/*     */   
/*     */   public CompoundComparator() {
/*  57 */     this.comparators = new ArrayList<>();
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
/*     */   public CompoundComparator(Comparator... comparators) {
/*  69 */     Assert.notNull(comparators, "Comparators must not be null");
/*  70 */     this.comparators = new ArrayList<>(comparators.length);
/*  71 */     for (Comparator<? extends T> comparator : comparators) {
/*  72 */       addComparator(comparator);
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
/*     */   public void addComparator(Comparator<? extends T> comparator) {
/*  86 */     if (comparator instanceof InvertibleComparator) {
/*  87 */       this.comparators.add((InvertibleComparator)comparator);
/*     */     } else {
/*     */       
/*  90 */       this.comparators.add(new InvertibleComparator<>(comparator));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComparator(Comparator<? extends T> comparator, boolean ascending) {
/* 101 */     this.comparators.add(new InvertibleComparator<>(comparator, ascending));
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
/*     */   public void setComparator(int index, Comparator<? extends T> comparator) {
/* 114 */     if (comparator instanceof InvertibleComparator) {
/* 115 */       this.comparators.set(index, (InvertibleComparator)comparator);
/*     */     } else {
/*     */       
/* 118 */       this.comparators.set(index, new InvertibleComparator<>(comparator));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComparator(int index, Comparator<T> comparator, boolean ascending) {
/* 129 */     this.comparators.set(index, new InvertibleComparator<>(comparator, ascending));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invertOrder() {
/* 137 */     for (InvertibleComparator comparator : this.comparators) {
/* 138 */       comparator.invertOrder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invertOrder(int index) {
/* 147 */     ((InvertibleComparator)this.comparators.get(index)).invertOrder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAscendingOrder(int index) {
/* 155 */     ((InvertibleComparator)this.comparators.get(index)).setAscending(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescendingOrder(int index) {
/* 163 */     ((InvertibleComparator)this.comparators.get(index)).setAscending(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getComparatorCount() {
/* 170 */     return this.comparators.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(T o1, T o2) {
/* 177 */     Assert.state(!this.comparators.isEmpty(), "No sort definitions have been added to this CompoundComparator to compare");
/*     */     
/* 179 */     for (InvertibleComparator<T> comparator : this.comparators) {
/* 180 */       int result = comparator.compare(o1, o2);
/* 181 */       if (result != 0) {
/* 182 */         return result;
/*     */       }
/*     */     } 
/* 185 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 192 */     return (this == other || (other instanceof CompoundComparator && this.comparators
/* 193 */       .equals(((CompoundComparator)other).comparators)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 198 */     return this.comparators.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 203 */     return "CompoundComparator: " + this.comparators;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\comparator\CompoundComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */