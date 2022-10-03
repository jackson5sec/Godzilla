/*    */ package org.fife.ui.autocomplete;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class SortByRelevanceComparator
/*    */   implements Comparator<Completion>, Serializable
/*    */ {
/*    */   public int compare(Completion c1, Completion c2) {
/* 28 */     int rel1 = c1.getRelevance();
/* 29 */     int rel2 = c2.getRelevance();
/* 30 */     int diff = rel2 - rel1;
/* 31 */     return (diff == 0) ? c1.compareTo(c2) : diff;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\SortByRelevanceComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */