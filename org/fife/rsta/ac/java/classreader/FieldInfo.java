/*     */ package org.fife.rsta.ac.java.classreader;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.AttributeInfo;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.ConstantValue;
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
/*     */ public class FieldInfo
/*     */   extends MemberInfo
/*     */ {
/*     */   private int nameIndex;
/*     */   private int descriptorIndex;
/*     */   private List<AttributeInfo> attributes;
/*     */   public static final String CONSTANT_VALUE = "ConstantValue";
/*     */   
/*     */   public FieldInfo(ClassFile cf, int accessFlags, int nameIndex, int descriptorIndex) {
/*  55 */     super(cf, accessFlags);
/*  56 */     this.nameIndex = nameIndex;
/*  57 */     this.descriptorIndex = descriptorIndex;
/*  58 */     this.attributes = new ArrayList<>(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(AttributeInfo info) {
/*  68 */     this.attributes.add(info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo getAttribute(int index) {
/*  79 */     return this.attributes.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeCount() {
/*  89 */     return this.attributes.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConstantValueAsString() {
/*  94 */     ConstantValue cv = getConstantValueAttributeInfo();
/*  95 */     return (cv == null) ? null : cv.getConstantValueAsString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ConstantValue getConstantValueAttributeInfo() {
/* 106 */     for (int i = 0; i < getAttributeCount(); i++) {
/* 107 */       AttributeInfo ai = this.attributes.get(i);
/* 108 */       if (ai instanceof ConstantValue) {
/* 109 */         return (ConstantValue)ai;
/*     */       }
/*     */     } 
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptor() {
/* 123 */     return this.cf.getUtf8ValueFromConstantPool(this.descriptorIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 132 */     return this.cf.getUtf8ValueFromConstantPool(this.nameIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNameIndex() {
/* 143 */     return this.nameIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeString(boolean qualified) {
/*     */     String clazz;
/* 154 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 156 */     String descriptor = getDescriptor();
/* 157 */     int braceCount = descriptor.lastIndexOf('[') + 1;
/*     */     
/* 159 */     switch (descriptor.charAt(braceCount)) {
/*     */ 
/*     */       
/*     */       case 'B':
/* 163 */         sb.append("byte");
/*     */         break;
/*     */       case 'C':
/* 166 */         sb.append("char");
/*     */         break;
/*     */       case 'D':
/* 169 */         sb.append("double");
/*     */         break;
/*     */       case 'F':
/* 172 */         sb.append("float");
/*     */         break;
/*     */       case 'I':
/* 175 */         sb.append("int");
/*     */         break;
/*     */       case 'J':
/* 178 */         sb.append("long");
/*     */         break;
/*     */       case 'S':
/* 181 */         sb.append("short");
/*     */         break;
/*     */       case 'Z':
/* 184 */         sb.append("boolean");
/*     */         break;
/*     */ 
/*     */       
/*     */       case 'L':
/* 189 */         clazz = descriptor.substring(braceCount + 1, descriptor
/* 190 */             .length() - 1);
/* 191 */         if (qualified) {
/* 192 */           clazz = clazz.replace('/', '.');
/*     */         } else {
/*     */           
/* 195 */           clazz = clazz.substring(clazz.lastIndexOf('/') + 1);
/*     */         } 
/* 197 */         sb.append(clazz);
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/* 202 */         sb.append("UNSUPPORTED_TYPE_").append(descriptor);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 207 */     for (int i = 0; i < braceCount; i++) {
/* 208 */       sb.append("[]");
/*     */     }
/*     */     
/* 211 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstant() {
/* 217 */     return (getConstantValueAttributeInfo() != null);
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
/*     */   public static FieldInfo read(ClassFile cf, DataInputStream in) throws IOException {
/* 234 */     FieldInfo info = new FieldInfo(cf, in.readUnsignedShort(), in.readUnsignedShort(), in.readUnsignedShort());
/* 235 */     int attrCount = in.readUnsignedShort();
/* 236 */     for (int i = 0; i < attrCount; i++) {
/* 237 */       AttributeInfo ai = info.readAttribute(in);
/* 238 */       if (ai != null) {
/* 239 */         info.addAttribute(ai);
/*     */       }
/*     */     } 
/* 242 */     return info;
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
/*     */   private AttributeInfo readAttribute(DataInputStream in) throws IOException {
/*     */     AttributeInfo ai;
/* 258 */     int attributeNameIndex = in.readUnsignedShort();
/* 259 */     int attributeLength = in.readInt();
/*     */     
/* 261 */     String attrName = this.cf.getUtf8ValueFromConstantPool(attributeNameIndex);
/*     */     
/* 263 */     if ("ConstantValue".equals(attrName)) {
/* 264 */       int constantValueIndex = in.readUnsignedShort();
/* 265 */       ConstantValue cv = new ConstantValue(this.cf, constantValueIndex);
/* 266 */       ConstantValue constantValue1 = cv;
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 271 */       ai = readAttribute(in, attrName, attributeLength);
/*     */     } 
/*     */     
/* 274 */     return ai;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\FieldInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */