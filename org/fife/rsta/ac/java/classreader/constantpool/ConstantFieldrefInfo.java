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
/*    */ 
/*    */ public class ConstantFieldrefInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private int classIndex;
/*    */   private int nameAndTypeIndex;
/*    */   
/*    */   public ConstantFieldrefInfo(int classIndex, int nameAndTypeIndex) {
/* 34 */     super(9);
/* 35 */     this.classIndex = classIndex;
/* 36 */     this.nameAndTypeIndex = nameAndTypeIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getClassIndex() {
/* 41 */     return this.classIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getNameAndTypeIndex() {
/* 46 */     return this.nameAndTypeIndex;
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
/* 57 */     return "[ConstantFieldrefInfo: classIndex=" + 
/* 58 */       getClassIndex() + "; nameAndTypeIndex=" + 
/* 59 */       getNameAndTypeIndex() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantFieldrefInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */