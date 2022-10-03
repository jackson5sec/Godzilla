/*     */ package org.fife.rsta.ac.java.classreader.attributes;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.classreader.ClassFile;
/*     */ import org.fife.rsta.ac.java.classreader.ExceptionTableEntry;
/*     */ import org.fife.rsta.ac.java.classreader.MethodInfo;
/*     */ import org.fife.rsta.ac.java.classreader.Util;
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
/*     */ public class Code
/*     */   extends AttributeInfo
/*     */ {
/*     */   private MethodInfo mi;
/*     */   private int maxStack;
/*     */   private int maxLocals;
/*     */   private int codeLength;
/*     */   private ExceptionTableEntry[] exceptionTable;
/*     */   private String[] paramNames;
/*     */   private List<AttributeInfo> attributes;
/*     */   private static final String LINE_NUMBER_TABLE = "LineNumberTable";
/*     */   private static final String LOCAL_VARIABLE_TABLE = "LocalVariableTable";
/*     */   private static final String LOCAL_VARIABLE_TYPE_TABLE = "LocalVariableTypeTable";
/*     */   private static final String STACK_MAP_TABLE = "StackMapTable";
/*     */   
/*     */   public Code(MethodInfo mi) {
/*  99 */     super(mi.getClassFile());
/* 100 */     this.mi = mi;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCodeLength() {
/* 121 */     return this.codeLength;
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
/*     */   public int getMaxLocals() {
/* 137 */     return this.maxLocals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxStack() {
/* 146 */     return this.maxStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getMethodInfo() {
/* 156 */     return this.mi;
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
/*     */   public String getParameterName(int index) {
/* 169 */     return (this.paramNames == null) ? null : this.paramNames[index];
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
/*     */   public static Code read(MethodInfo mi, DataInputStream in) throws IOException {
/* 184 */     Code code = new Code(mi);
/* 185 */     code.maxStack = in.readUnsignedShort();
/* 186 */     code.maxLocals = in.readUnsignedShort();
/* 187 */     code.codeLength = in.readInt();
/* 188 */     Util.skipBytes(in, code.codeLength);
/*     */     
/* 190 */     int exceptionTableLength = in.readUnsignedShort();
/* 191 */     if (exceptionTableLength > 0) {
/* 192 */       code.exceptionTable = new ExceptionTableEntry[exceptionTableLength];
/* 193 */       for (int i = 0; i < exceptionTableLength; i++) {
/* 194 */         ExceptionTableEntry ete = ExceptionTableEntry.read(mi
/* 195 */             .getClassFile(), in);
/* 196 */         code.exceptionTable[i] = ete;
/*     */       } 
/*     */     } 
/*     */     
/* 200 */     int attrCount = in.readUnsignedShort();
/* 201 */     if (attrCount > 0) {
/* 202 */       code.attributes = new ArrayList<>(1);
/* 203 */       for (int i = 0; i < attrCount; i++) {
/* 204 */         AttributeInfo ai = code.readAttribute(in);
/* 205 */         if (ai != null) {
/* 206 */           code.attributes.add(ai);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     return code;
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
/* 226 */     AttributeInfo ai = null;
/* 227 */     ClassFile cf = this.mi.getClassFile();
/*     */     
/* 229 */     int attributeNameIndex = in.readUnsignedShort();
/* 230 */     int attributeLength = in.readInt();
/*     */     
/* 232 */     String attrName = cf.getUtf8ValueFromConstantPool(attributeNameIndex);
/*     */ 
/*     */     
/* 235 */     if ("LineNumberTable".equals(attrName)) {
/*     */ 
/*     */       
/* 238 */       Util.skipBytes(in, attributeLength);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 244 */     else if ("LocalVariableTable".equals(attrName)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 252 */       int paramCount = this.mi.getParameterCount();
/* 253 */       this.paramNames = new String[paramCount];
/* 254 */       boolean isStatic = this.mi.isStatic();
/*     */       
/* 256 */       int localVariableTableLength = in.readUnsignedShort();
/* 257 */       for (int i = 0; i < localVariableTableLength; i++)
/*     */       {
/* 259 */         in.readUnsignedShort();
/* 260 */         in.readUnsignedShort();
/* 261 */         int nameIndex = in.readUnsignedShort();
/* 262 */         in.readUnsignedShort();
/*     */ 
/*     */ 
/*     */         
/* 266 */         int index = in.readUnsignedShort();
/* 267 */         int adjustedIndex = isStatic ? index : (index - 1);
/*     */         
/* 269 */         if (adjustedIndex >= 0 && adjustedIndex < this.paramNames.length) {
/* 270 */           String name = cf.getUtf8ValueFromConstantPool(nameIndex);
/*     */ 
/*     */ 
/*     */           
/* 274 */           this.paramNames[adjustedIndex] = name;
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 282 */     else if ("LocalVariableTypeTable".equals(attrName)) {
/* 283 */       Util.skipBytes(in, attributeLength);
/*     */ 
/*     */     
/*     */     }
/* 287 */     else if ("StackMapTable".equals(attrName)) {
/* 288 */       Util.skipBytes(in, attributeLength);
/*     */     }
/*     */     else {
/*     */       
/* 292 */       System.out.println("Unsupported Code attribute: " + attrName);
/* 293 */       ai = AttributeInfo.readUnsupportedAttribute(cf, in, attrName, attributeLength);
/*     */     } 
/*     */ 
/*     */     
/* 297 */     return ai;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\attributes\Code.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */