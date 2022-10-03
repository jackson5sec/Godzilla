/*    */ package org.fife.rsta.ac.java.classreader.attributes;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*    */ import org.fife.rsta.ac.java.classreader.Util;
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
/*    */ public abstract class AttributeInfo
/*    */ {
/*    */   private ClassFile cf;
/*    */   public int attributeNameIndex;
/*    */   
/*    */   protected AttributeInfo(ClassFile cf) {
/* 26 */     this.cf = cf;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassFile getClassFile() {
/* 31 */     return this.cf;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 41 */     return this.cf.getUtf8ValueFromConstantPool(this.attributeNameIndex);
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
/*    */   public static UnsupportedAttribute readUnsupportedAttribute(ClassFile cf, DataInputStream in, String attrName, int attrLength) throws IOException {
/* 65 */     Util.skipBytes(in, attrLength);
/* 66 */     return new UnsupportedAttribute(cf, attrName);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\attributes\AttributeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */