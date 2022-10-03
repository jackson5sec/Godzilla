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
/*    */ public class ConstantNameAndTypeInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private int nameIndex;
/*    */   private int descriptorIndex;
/*    */   
/*    */   public ConstantNameAndTypeInfo(int nameIndex, int descriptorIndex) {
/* 33 */     super(12);
/* 34 */     this.nameIndex = nameIndex;
/* 35 */     this.descriptorIndex = descriptorIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDescriptorIndex() {
/* 40 */     return this.descriptorIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getNameIndex() {
/* 45 */     return this.nameIndex;
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
/* 56 */     return "[ConstantNameAndTypeInfo: descriptorIndex=" + 
/* 57 */       getDescriptorIndex() + "; nameIndex=" + 
/* 58 */       getNameIndex() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantNameAndTypeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */