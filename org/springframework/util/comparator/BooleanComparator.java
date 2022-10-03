/*    */ package org.springframework.util.comparator;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class BooleanComparator
/*    */   implements Comparator<Boolean>, Serializable
/*    */ {
/* 38 */   public static final BooleanComparator TRUE_LOW = new BooleanComparator(true);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public static final BooleanComparator TRUE_HIGH = new BooleanComparator(false);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean trueLow;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BooleanComparator(boolean trueLow) {
/* 61 */     this.trueLow = trueLow;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(Boolean v1, Boolean v2) {
/* 67 */     return ((v1.booleanValue() ^ v2.booleanValue()) != 0) ? (((v1.booleanValue() ^ this.trueLow) != 0) ? 1 : -1) : 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@Nullable Object other) {
/* 73 */     return (this == other || (other instanceof BooleanComparator && this.trueLow == ((BooleanComparator)other).trueLow));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 79 */     return getClass().hashCode() * (this.trueLow ? -1 : 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 84 */     return "BooleanComparator: " + (this.trueLow ? "true low" : "true high");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\comparator\BooleanComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */