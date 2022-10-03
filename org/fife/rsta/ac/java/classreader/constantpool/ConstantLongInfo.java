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
/*    */ public class ConstantLongInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private int highBytes;
/*    */   private int lowBytes;
/*    */   
/*    */   public ConstantLongInfo(int highBytes, int lowBytes) {
/* 33 */     super(5);
/* 34 */     this.highBytes = highBytes;
/* 35 */     this.lowBytes = lowBytes;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHighBytes() {
/* 40 */     return this.highBytes;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLongValue() {
/* 45 */     return (this.highBytes << 32L) + this.lowBytes;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLowBytes() {
/* 50 */     return this.lowBytes;
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
/* 61 */     return "[ConstantLongInfo: value=" + 
/* 62 */       getLongValue() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantLongInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */