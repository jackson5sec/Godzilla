/*    */ package org.fife.rsta.ac.java.classreader.constantpool;
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
/*    */ public class ConstantDoubleInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private int highBytes;
/*    */   private int lowBytes;
/*    */   
/*    */   public ConstantDoubleInfo(int highBytes, int lowBytes) {
/* 33 */     super(6);
/* 34 */     this.highBytes = highBytes;
/* 35 */     this.lowBytes = lowBytes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getDoubleValue() {
/* 45 */     long bits = (this.highBytes << 32L) + this.lowBytes;
/* 46 */     return Double.longBitsToDouble(bits);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHighBytes() {
/* 51 */     return this.highBytes;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLowBytes() {
/* 56 */     return this.lowBytes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return "[ConstantDoubleInfo: value=" + 
/* 68 */       getDoubleValue() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantDoubleInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */