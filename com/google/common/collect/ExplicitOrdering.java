/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class ExplicitOrdering<T>
/*    */   extends Ordering<T>
/*    */   implements Serializable
/*    */ {
/*    */   final ImmutableMap<T, Integer> rankMap;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ExplicitOrdering(List<T> valuesInOrder) {
/* 30 */     this(Maps.indexMap(valuesInOrder));
/*    */   }
/*    */   
/*    */   ExplicitOrdering(ImmutableMap<T, Integer> rankMap) {
/* 34 */     this.rankMap = rankMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(T left, T right) {
/* 39 */     return rank(left) - rank(right);
/*    */   }
/*    */   
/*    */   private int rank(T value) {
/* 43 */     Integer rank = this.rankMap.get(value);
/* 44 */     if (rank == null) {
/* 45 */       throw new Ordering.IncomparableValueException(value);
/*    */     }
/* 47 */     return rank.intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 52 */     if (object instanceof ExplicitOrdering) {
/* 53 */       ExplicitOrdering<?> that = (ExplicitOrdering)object;
/* 54 */       return this.rankMap.equals(that.rankMap);
/*    */     } 
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 61 */     return this.rankMap.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     return "Ordering.explicit(" + this.rankMap.keySet() + ")";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ExplicitOrdering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */