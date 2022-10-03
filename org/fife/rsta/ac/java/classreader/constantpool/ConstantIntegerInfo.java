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
/*    */ public class ConstantIntegerInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private long bytes;
/*    */   
/*    */   public ConstantIntegerInfo(long bytes) {
/* 29 */     super(8);
/* 30 */     this.bytes = bytes;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getBytes() {
/* 35 */     return this.bytes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getIntValue() {
/* 45 */     return (int)this.bytes;
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
/* 56 */     return "[ConstantIntegerInfo: bytes=" + 
/* 57 */       getBytes() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantIntegerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */