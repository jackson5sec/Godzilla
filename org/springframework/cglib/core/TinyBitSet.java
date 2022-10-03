/*    */ package org.springframework.cglib.core;
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
/*    */ @Deprecated
/*    */ public class TinyBitSet
/*    */ {
/* 20 */   private static int[] T = new int[256];
/* 21 */   private int value = 0;
/*    */   
/*    */   private static int gcount(int x) {
/* 24 */     int c = 0;
/* 25 */     while (x != 0) {
/* 26 */       c++;
/* 27 */       x &= x - 1;
/*    */     } 
/* 29 */     return c;
/*    */   }
/*    */   
/*    */   static {
/* 33 */     for (int j = 0; j < 256; j++) {
/* 34 */       T[j] = gcount(j);
/*    */     }
/*    */   }
/*    */   
/*    */   private static int topbit(int i) {
/*    */     int j;
/* 40 */     for (j = 0; i != 0; i ^= j) {
/* 41 */       j = i & -i;
/*    */     }
/* 43 */     return j;
/*    */   }
/*    */   
/*    */   private static int log2(int i) {
/* 47 */     int j = 0;
/* 48 */     for (j = 0; i != 0; i >>= 1) {
/* 49 */       j++;
/*    */     }
/* 51 */     return j;
/*    */   }
/*    */   
/*    */   public int length() {
/* 55 */     return log2(topbit(this.value));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int cardinality() {
/* 64 */     int w = this.value;
/* 65 */     int c = 0;
/* 66 */     while (w != 0) {
/* 67 */       c += T[w & 0xFF];
/* 68 */       w >>= 8;
/*    */     } 
/* 70 */     return c;
/*    */   }
/*    */   
/*    */   public boolean get(int index) {
/* 74 */     return ((this.value & 1 << index) != 0);
/*    */   }
/*    */   
/*    */   public void set(int index) {
/* 78 */     this.value |= 1 << index;
/*    */   }
/*    */   
/*    */   public void clear(int index) {
/* 82 */     this.value &= 1 << index ^ 0xFFFFFFFF;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\TinyBitSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */