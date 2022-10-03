/*    */ package org.fife.rsta.ac.java.classreader.attributes;
/*    */ 
/*    */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*    */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantDoubleInfo;
/*    */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantFloatInfo;
/*    */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantIntegerInfo;
/*    */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantLongInfo;
/*    */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantPoolInfo;
/*    */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantStringInfo;
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
/*    */ public class ConstantValue
/*    */   extends AttributeInfo
/*    */ {
/*    */   private int constantValueIndex;
/*    */   
/*    */   public ConstantValue(ClassFile cf, int constantValueIndex) {
/* 41 */     super(cf);
/* 42 */     this.constantValueIndex = constantValueIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getConstantValueIndex() {
/* 53 */     return this.constantValueIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getConstantValueAsString() {
/* 64 */     ClassFile cf = getClassFile();
/* 65 */     ConstantPoolInfo cpi = cf.getConstantPoolInfo(getConstantValueIndex());
/*    */     
/* 67 */     if (cpi instanceof ConstantDoubleInfo) {
/* 68 */       ConstantDoubleInfo cdi = (ConstantDoubleInfo)cpi;
/* 69 */       double value = cdi.getDoubleValue();
/* 70 */       return Double.toString(value);
/*    */     } 
/* 72 */     if (cpi instanceof ConstantFloatInfo) {
/* 73 */       ConstantFloatInfo cfi = (ConstantFloatInfo)cpi;
/* 74 */       float value = cfi.getFloatValue();
/* 75 */       return Float.toString(value);
/*    */     } 
/* 77 */     if (cpi instanceof ConstantIntegerInfo) {
/* 78 */       ConstantIntegerInfo cii = (ConstantIntegerInfo)cpi;
/* 79 */       int value = cii.getIntValue();
/* 80 */       return Integer.toString(value);
/*    */     } 
/* 82 */     if (cpi instanceof ConstantLongInfo) {
/* 83 */       ConstantLongInfo cli = (ConstantLongInfo)cpi;
/* 84 */       long value = cli.getLongValue();
/* 85 */       return Long.toString(value);
/*    */     } 
/* 87 */     if (cpi instanceof ConstantStringInfo) {
/* 88 */       ConstantStringInfo csi = (ConstantStringInfo)cpi;
/* 89 */       return csi.getStringValue();
/*    */     } 
/*    */     
/* 92 */     return "INVALID_CONSTANT_TYPE_" + cpi.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\attributes\ConstantValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */