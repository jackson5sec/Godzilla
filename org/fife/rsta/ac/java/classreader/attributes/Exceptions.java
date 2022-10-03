/*    */ package org.fife.rsta.ac.java.classreader.attributes;
/*    */ 
/*    */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*    */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*    */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantClassInfo;
/*    */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantPoolInfo;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Exceptions
/*    */   extends AttributeInfo
/*    */ {
/*    */   private MethodInfo mi;
/*    */   private int[] exceptionIndexTable;
/*    */   
/*    */   public Exceptions(MethodInfo mi, int[] exceptionIndexTable) {
/* 44 */     super(mi.getClassFile());
/* 45 */     this.exceptionIndexTable = exceptionIndexTable;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getException(int index) {
/* 56 */     ClassFile cf = getClassFile();
/* 57 */     ConstantPoolInfo cpi = cf.getConstantPoolInfo(this.exceptionIndexTable[index]);
/*    */     
/* 59 */     ConstantClassInfo cci = (ConstantClassInfo)cpi;
/* 60 */     int nameIndex = cci.getNameIndex();
/* 61 */     String name = cf.getUtf8ValueFromConstantPool(nameIndex);
/* 62 */     return name.replace('/', '.');
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getExceptionCount() {
/* 72 */     return (this.exceptionIndexTable == null) ? 0 : this.exceptionIndexTable.length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodInfo getMethodInfo() {
/* 82 */     return this.mi;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\attributes\Exceptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */