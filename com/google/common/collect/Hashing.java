/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class Hashing
/*    */ {
/*    */   private static final long C1 = -862048943L;
/*    */   private static final long C2 = 461845907L;
/*    */   private static final int MAX_TABLE_SIZE = 1073741824;
/*    */   
/*    */   static int smear(int hashCode) {
/* 50 */     return (int)(461845907L * Integer.rotateLeft((int)(hashCode * -862048943L), 15));
/*    */   }
/*    */   
/*    */   static int smearedHash(Object o) {
/* 54 */     return smear((o == null) ? 0 : o.hashCode());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static int closedTableSize(int expectedEntries, double loadFactor) {
/* 62 */     expectedEntries = Math.max(expectedEntries, 2);
/* 63 */     int tableSize = Integer.highestOneBit(expectedEntries);
/*    */     
/* 65 */     if (expectedEntries > (int)(loadFactor * tableSize)) {
/* 66 */       tableSize <<= 1;
/* 67 */       return (tableSize > 0) ? tableSize : 1073741824;
/*    */     } 
/* 69 */     return tableSize;
/*    */   }
/*    */   
/*    */   static boolean needsResizing(int size, int tableSize, double loadFactor) {
/* 73 */     return (size > loadFactor * tableSize && tableSize < 1073741824);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\Hashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */