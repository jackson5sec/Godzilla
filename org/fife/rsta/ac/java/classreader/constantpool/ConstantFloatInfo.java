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
/*    */ public class ConstantFloatInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private int bytes;
/*    */   
/*    */   public ConstantFloatInfo(int bytes) {
/* 31 */     super(4);
/* 32 */     this.bytes = bytes;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getBytes() {
/* 37 */     return this.bytes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getFloatValue() {
/* 47 */     return Float.intBitsToFloat(this.bytes);
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
/* 58 */     return "[ConstantFloatInfo: value=" + 
/* 59 */       getFloatValue() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantFloatInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */