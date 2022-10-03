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
/*    */ public class ConstantMethodTypeInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private int descriptorIndex;
/*    */   
/*    */   public ConstantMethodTypeInfo(int descriptorIndex) {
/* 29 */     super(16);
/* 30 */     this.descriptorIndex = descriptorIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDescriptorIndex() {
/* 35 */     return this.descriptorIndex;
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
/* 46 */     return "[ConstantMethodTypeInfo: bootstrapMethodAttrIndex=" + 
/* 47 */       getDescriptorIndex() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantMethodTypeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */