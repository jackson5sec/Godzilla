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
/*    */ 
/*    */ 
/*    */ class ConstantInterfaceMethodrefInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private int classIndex;
/*    */   private int nameAndTypeIndex;
/*    */   
/*    */   public ConstantInterfaceMethodrefInfo(int classIndex, int nameAndTypeIndex) {
/* 36 */     super(11);
/* 37 */     this.classIndex = classIndex;
/* 38 */     this.nameAndTypeIndex = nameAndTypeIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getClassIndex() {
/* 43 */     return this.classIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getNameAndTypeIndex() {
/* 48 */     return this.nameAndTypeIndex;
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
/* 59 */     return "[ConstantInterfaceMethodrefInfo: classIndex=" + 
/* 60 */       getClassIndex() + "; nameAndTypeIndex=" + 
/* 61 */       getNameAndTypeIndex() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantInterfaceMethodrefInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */