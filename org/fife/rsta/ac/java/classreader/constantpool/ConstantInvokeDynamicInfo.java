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
/*    */ public class ConstantInvokeDynamicInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private int bootstrapMethodAttrIndex;
/*    */   private int nameAndTypeIndex;
/*    */   
/*    */   public ConstantInvokeDynamicInfo(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
/* 33 */     super(18);
/* 34 */     this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
/* 35 */     this.nameAndTypeIndex = nameAndTypeIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getBootstrapMethodAttrIndex() {
/* 40 */     return this.bootstrapMethodAttrIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getNameAndTypeIndex() {
/* 45 */     return this.nameAndTypeIndex;
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
/* 56 */     return "[ConstantInvokeDynamicInfo: bootstrapMethodAttrIndex=" + 
/* 57 */       getBootstrapMethodAttrIndex() + "; nameAndTypeIndex=" + 
/* 58 */       getNameAndTypeIndex() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantInvokeDynamicInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */