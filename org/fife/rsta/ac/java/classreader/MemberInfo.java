/*     */ package org.fife.rsta.ac.java.classreader;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.AttributeInfo;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.Signature;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.UnsupportedAttribute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MemberInfo
/*     */ {
/*     */   protected ClassFile cf;
/*     */   private int accessFlags;
/*     */   private boolean deprecated;
/*     */   public static final String DEPRECATED = "Deprecated";
/*     */   public static final String SIGNATURE = "Signature";
/*     */   public static final String RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";
/*     */   
/*     */   protected MemberInfo(ClassFile cf, int accessFlags) {
/*  68 */     this.cf = cf;
/*  69 */     this.accessFlags = accessFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccessFlags() {
/*  80 */     return this.accessFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile getClassFile() {
/*  90 */     return this.cf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 108 */     return this.deprecated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getDescriptor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 126 */     return ((getAccessFlags() & 0x10) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 136 */     return ((getAccessFlags() & 0x8) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AttributeInfo readAttribute(DataInputStream in, String attrName, int attrLength) throws IOException {
/*     */     UnsupportedAttribute unsupportedAttribute;
/* 155 */     AttributeInfo ai = null;
/*     */     
/* 157 */     if ("Deprecated".equals(attrName)) {
/*     */       
/* 159 */       this.deprecated = true;
/*     */     
/*     */     }
/* 162 */     else if ("Signature".equals(attrName)) {
/*     */       
/* 164 */       int signatureIndex = in.readUnsignedShort();
/* 165 */       String typeSig = this.cf.getUtf8ValueFromConstantPool(signatureIndex);
/* 166 */       Signature signature = new Signature(this.cf, typeSig);
/*     */     
/*     */     }
/* 169 */     else if ("RuntimeVisibleAnnotations".equals(attrName)) {
/*     */ 
/*     */       
/* 172 */       Util.skipBytes(in, attrLength);
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 179 */       unsupportedAttribute = AttributeInfo.readUnsupportedAttribute(this.cf, in, attrName, attrLength);
/*     */     } 
/*     */ 
/*     */     
/* 183 */     return (AttributeInfo)unsupportedAttribute;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\MemberInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */