/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable = true)
/*    */ final class CompoundOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final Comparator<? super T>[] comparators;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   CompoundOrdering(Comparator<? super T> primary, Comparator<? super T> secondary) {
/* 30 */     this.comparators = (Comparator<? super T>[])new Comparator[] { primary, secondary };
/*    */   }
/*    */   
/*    */   CompoundOrdering(Iterable<? extends Comparator<? super T>> comparators) {
/* 34 */     this.comparators = Iterables.<Comparator<? super T>>toArray(comparators, (Comparator<? super T>[])new Comparator[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(T left, T right) {
/* 39 */     for (int i = 0; i < this.comparators.length; i++) {
/* 40 */       int result = this.comparators[i].compare(left, right);
/* 41 */       if (result != 0) {
/* 42 */         return result;
/*    */       }
/*    */     } 
/* 45 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 50 */     if (object == this) {
/* 51 */       return true;
/*    */     }
/* 53 */     if (object instanceof CompoundOrdering) {
/* 54 */       CompoundOrdering<?> that = (CompoundOrdering)object;
/* 55 */       return Arrays.equals((Object[])this.comparators, (Object[])that.comparators);
/*    */     } 
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     return Arrays.hashCode((Object[])this.comparators);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return "Ordering.compound(" + Arrays.toString((Object[])this.comparators) + ")";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\CompoundOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */