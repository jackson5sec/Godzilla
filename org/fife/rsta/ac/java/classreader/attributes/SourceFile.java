/*    */ package org.fife.rsta.ac.java.classreader.attributes;
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
/*    */ public class SourceFile
/*    */   extends AttributeInfo
/*    */ {
/*    */   private int sourceFileIndex;
/*    */   
/*    */   public SourceFile(ClassFile cf, int sourceFileIndex) {
/* 44 */     super(cf);
/* 45 */     this.sourceFileIndex = sourceFileIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSourceFileName() {
/* 56 */     return getClassFile().getUtf8ValueFromConstantPool(this.sourceFileIndex);
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
/* 68 */     return "[SourceFile: file=" + 
/* 69 */       getSourceFileName() + "]";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\attributes\SourceFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */