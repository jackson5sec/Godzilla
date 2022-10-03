/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
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
/*    */ @GwtIncompatible
/*    */ abstract class AbstractRangeSet<C extends Comparable>
/*    */   implements RangeSet<C>
/*    */ {
/*    */   public boolean contains(C value) {
/* 31 */     return (rangeContaining(value) != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract Range<C> rangeContaining(C paramC);
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 39 */     return asRanges().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(Range<C> range) {
/* 44 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(Range<C> range) {
/* 49 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 54 */     remove((Range)Range.all());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean enclosesAll(RangeSet<C> other) {
/* 59 */     return enclosesAll(other.asRanges());
/*    */   }
/*    */ 
/*    */   
/*    */   public void addAll(RangeSet<C> other) {
/* 64 */     addAll(other.asRanges());
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeAll(RangeSet<C> other) {
/* 69 */     removeAll(other.asRanges());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean intersects(Range<C> otherRange) {
/* 74 */     return !subRangeSet(otherRange).isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract boolean encloses(Range<C> paramRange);
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 82 */     if (obj == this)
/* 83 */       return true; 
/* 84 */     if (obj instanceof RangeSet) {
/* 85 */       RangeSet<?> other = (RangeSet)obj;
/* 86 */       return asRanges().equals(other.asRanges());
/*    */     } 
/* 88 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 93 */     return asRanges().hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 98 */     return asRanges().toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\AbstractRangeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */