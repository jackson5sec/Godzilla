/*    */ package org.springframework.util.comparator;
/*    */ 
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
/*    */ 
/*    */ public abstract class Comparators
/*    */ {
/*    */   public static <T> Comparator<T> comparable() {
/* 36 */     return ComparableComparator.INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Comparator<T> nullsLow() {
/* 46 */     return NullSafeComparator.NULLS_LOW;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Comparator<T> nullsLow(Comparator<T> comparator) {
/* 55 */     return new NullSafeComparator<>(comparator, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Comparator<T> nullsHigh() {
/* 65 */     return NullSafeComparator.NULLS_HIGH;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Comparator<T> nullsHigh(Comparator<T> comparator) {
/* 74 */     return new NullSafeComparator<>(comparator, false);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\comparator\Comparators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */