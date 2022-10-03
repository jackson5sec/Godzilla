/*    */ package org.fife.rsta.ac.java.classreader.constantpool;
/*    */ 
/*    */ import org.fife.rsta.ac.java.classreader.ClassFile;
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
/*    */ public class ConstantStringInfo
/*    */   extends ConstantPoolInfo
/*    */ {
/*    */   private ClassFile cf;
/*    */   private int stringIndex;
/*    */   
/*    */   public ConstantStringInfo(ClassFile cf, int stringIndex) {
/* 34 */     super(8);
/* 35 */     this.cf = cf;
/* 36 */     this.stringIndex = stringIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getStringIndex() {
/* 41 */     return this.stringIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getStringValue() {
/* 51 */     return '"' + this.cf.getUtf8ValueFromConstantPool(getStringIndex()) + '"';
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "[ConstantStringInfo: stringIndex=" + 
/* 64 */       getStringIndex() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\constantpool\ConstantStringInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */