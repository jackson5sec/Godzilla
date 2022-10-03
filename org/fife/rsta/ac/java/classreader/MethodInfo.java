/*     */ package org.fife.rsta.ac.java.classreader;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.AttributeInfo;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.Code;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.Exceptions;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.Signature;
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
/*     */ public class MethodInfo
/*     */   extends MemberInfo
/*     */   implements AccessFlags
/*     */ {
/*     */   private int nameIndex;
/*     */   private int descriptorIndex;
/*     */   private Signature signatureAttr;
/*     */   private Code codeAttr;
/*     */   private List<AttributeInfo> attributes;
/*     */   private String[] paramTypes;
/*     */   private String returnType;
/*     */   private String nameAndParameters;
/*     */   private static final String SPECIAL_NAME_CONSTRUCTOR = "<init>";
/*     */   public static final String CODE = "Code";
/*     */   public static final String EXCEPTIONS = "Exceptions";
/*     */   
/*     */   public MethodInfo(ClassFile cf, int accessFlags, int nameIndex, int descriptorIndex) {
/*  95 */     super(cf, accessFlags);
/*  96 */     this.nameIndex = nameIndex;
/*  97 */     this.descriptorIndex = descriptorIndex;
/*  98 */     this.attributes = new ArrayList<>(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAttribute(AttributeInfo info) {
/* 108 */     this.attributes.add(info);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void appendParamDescriptors(StringBuilder sb) {
/* 114 */     String[] paramTypes = getParameterTypes();
/* 115 */     for (int i = 0; i < paramTypes.length; i++) {
/* 116 */       sb.append(paramTypes[i]).append(" param").append(i);
/* 117 */       if (i < paramTypes.length - 1) {
/* 118 */         sb.append(", ");
/*     */       }
/*     */     } 
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
/*     */   void clearParamTypeInfo() {
/* 137 */     this.paramTypes = null;
/* 138 */     this.returnType = null;
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
/*     */   private String[] createParamTypes() {
/* 154 */     String[] types = createParamTypesFromTypeSignature();
/* 155 */     if (types == null) {
/* 156 */       types = createParamTypesFromDescriptor(true);
/*     */     }
/* 158 */     return types;
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
/*     */   private String[] createParamTypesFromDescriptor(boolean qualified) {
/* 172 */     String descriptor = getDescriptor();
/* 173 */     int rparen = descriptor.indexOf(')');
/* 174 */     String paramDescriptors = descriptor.substring(1, rparen);
/*     */ 
/*     */     
/* 177 */     List<String> paramTypeList = new ArrayList<>();
/*     */ 
/*     */     
/* 180 */     while (paramDescriptors.length() > 0) {
/*     */       String type, clazz, temp;
/*     */ 
/*     */ 
/*     */       
/* 185 */       int braceCount = -1;
/* 186 */       while (paramDescriptors.charAt(++braceCount) == '[');
/* 187 */       int pos = braceCount;
/*     */       
/* 189 */       switch (paramDescriptors.charAt(pos)) {
/*     */ 
/*     */         
/*     */         case 'B':
/* 193 */           type = "byte";
/* 194 */           pos++;
/*     */           break;
/*     */         case 'C':
/* 197 */           type = "char";
/* 198 */           pos++;
/*     */           break;
/*     */         case 'D':
/* 201 */           type = "double";
/* 202 */           pos++;
/*     */           break;
/*     */         case 'F':
/* 205 */           type = "float";
/* 206 */           pos++;
/*     */           break;
/*     */         case 'I':
/* 209 */           type = "int";
/* 210 */           pos++;
/*     */           break;
/*     */         case 'J':
/* 213 */           type = "long";
/* 214 */           pos++;
/*     */           break;
/*     */         case 'S':
/* 217 */           type = "short";
/* 218 */           pos++;
/*     */           break;
/*     */         case 'Z':
/* 221 */           type = "boolean";
/* 222 */           pos++;
/*     */           break;
/*     */ 
/*     */         
/*     */         case 'L':
/* 227 */           clazz = paramDescriptors.substring(pos + 1, paramDescriptors
/* 228 */               .indexOf(';'));
/* 229 */           type = qualified ? clazz.replace('/', '.') : clazz.substring(clazz.lastIndexOf('/') + 1);
/* 230 */           pos += clazz.length() + 2;
/*     */           break;
/*     */ 
/*     */         
/*     */         default:
/* 235 */           temp = "INVALID_TYPE_" + paramDescriptors;
/* 236 */           type = temp;
/* 237 */           pos += paramDescriptors.length();
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 242 */       for (int i = 0; i < braceCount; i++) {
/* 243 */         type = type + "[]";
/*     */       }
/* 245 */       paramTypeList.add(type);
/*     */       
/* 247 */       paramDescriptors = paramDescriptors.substring(pos);
/*     */     } 
/*     */ 
/*     */     
/* 251 */     String[] types = new String[paramTypeList.size()];
/* 252 */     types = paramTypeList.<String>toArray(types);
/* 253 */     return types;
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
/*     */   private String[] createParamTypesFromTypeSignature() {
/* 270 */     String[] params = null;
/*     */     
/* 272 */     if (this.signatureAttr != null) {
/*     */       
/* 274 */       List<String> paramTypes = this.signatureAttr.getMethodParamTypes(this, this.cf, false);
/* 275 */       if (paramTypes != null) {
/* 276 */         params = new String[paramTypes.size()];
/* 277 */         params = paramTypes.<String>toArray(params);
/*     */       } 
/*     */     } 
/*     */     
/* 281 */     return params;
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
/*     */   public AttributeInfo getAttribute(int index) {
/* 293 */     return this.attributes.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeCount() {
/* 303 */     return this.attributes.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptor() {
/* 312 */     return this.cf.getUtf8ValueFromConstantPool(this.descriptorIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 321 */     String name = this.cf.getUtf8ValueFromConstantPool(this.nameIndex);
/* 322 */     if ("<init>".equals(name)) {
/* 323 */       name = this.cf.getClassName(false);
/*     */     }
/* 325 */     return name;
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
/*     */   public String getNameAndParameters() {
/* 337 */     if (this.nameAndParameters == null) {
/*     */       
/* 339 */       StringBuilder sb = new StringBuilder(getName());
/*     */       
/* 341 */       sb.append('(');
/* 342 */       int paramCount = getParameterCount();
/* 343 */       for (int i = 0; i < paramCount; i++) {
/* 344 */         sb.append(getParameterType(i, false));
/* 345 */         if (i < paramCount - 1) {
/* 346 */           sb.append(", ");
/*     */         }
/*     */       } 
/* 349 */       sb.append(')');
/*     */       
/* 351 */       this.nameAndParameters = sb.toString();
/*     */     } 
/*     */ 
/*     */     
/* 355 */     return this.nameAndParameters;
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
/*     */   public int getParameterCount() {
/* 368 */     if (this.paramTypes == null) {
/* 369 */       this.paramTypes = createParamTypes();
/*     */     }
/* 371 */     return this.paramTypes.length;
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
/* 384 */     if (index >= 0 && index < getParameterCount() && 
/* 385 */       this.codeAttr != null) {
/* 386 */       return this.codeAttr.getParameterName(index);
/*     */     }
/*     */     
/* 389 */     return null;
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
/*     */   public String getParameterType(int index, boolean fullyQualified) {
/* 406 */     if (this.paramTypes == null) {
/* 407 */       this.paramTypes = createParamTypes();
/*     */     }
/* 409 */     String type = this.paramTypes[index];
/* 410 */     if (!fullyQualified) {
/* 411 */       int dot = type.lastIndexOf('.');
/* 412 */       if (dot > -1) {
/* 413 */         type = type.substring(dot + 1);
/*     */       }
/*     */     } 
/* 416 */     return type;
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
/*     */   public String[] getParameterTypes() {
/* 430 */     if (this.paramTypes == null) {
/* 431 */       this.paramTypes = createParamTypes();
/*     */     }
/* 433 */     return (String[])this.paramTypes.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReturnTypeString(boolean fullyQualified) {
/* 442 */     if (this.returnType == null) {
/* 443 */       this.returnType = getReturnTypeStringFromTypeSignature(fullyQualified);
/* 444 */       if (this.returnType == null) {
/* 445 */         this.returnType = getReturnTypeStringFromDescriptor(fullyQualified);
/*     */       }
/*     */     } 
/* 448 */     if (!fullyQualified)
/*     */     {
/* 450 */       if (this.returnType != null && this.returnType.contains(".")) {
/* 451 */         return this.returnType.substring(this.returnType.lastIndexOf(".") + 1, this.returnType.length());
/*     */       }
/*     */     }
/* 454 */     return this.returnType;
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
/*     */   private String getReturnTypeStringFromDescriptor(boolean qualified) {
/* 474 */     String clazz, descriptor = getDescriptor();
/* 475 */     int rparen = descriptor.indexOf(')');
/* 476 */     descriptor = descriptor.substring(rparen + 1);
/* 477 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 479 */     int braceCount = descriptor.lastIndexOf('[') + 1;
/*     */     
/* 481 */     switch (descriptor.charAt(braceCount)) {
/*     */ 
/*     */       
/*     */       case 'B':
/* 485 */         sb.append("byte");
/*     */         break;
/*     */       case 'C':
/* 488 */         sb.append("char");
/*     */         break;
/*     */       case 'D':
/* 491 */         sb.append("double");
/*     */         break;
/*     */       case 'F':
/* 494 */         sb.append("float");
/*     */         break;
/*     */       case 'I':
/* 497 */         sb.append("int");
/*     */         break;
/*     */       case 'J':
/* 500 */         sb.append("long");
/*     */         break;
/*     */       case 'S':
/* 503 */         sb.append("short");
/*     */         break;
/*     */       case 'Z':
/* 506 */         sb.append("boolean");
/*     */         break;
/*     */       case 'V':
/* 509 */         sb.append("void");
/*     */         break;
/*     */ 
/*     */       
/*     */       case 'L':
/* 514 */         clazz = descriptor.substring(braceCount + 1, descriptor.length() - 1);
/* 515 */         clazz = qualified ? clazz.replace('/', '.') : clazz.substring(clazz.lastIndexOf('/') + 1);
/* 516 */         sb.append(clazz);
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/* 521 */         sb.append("UNSUPPORTED_TYPE_").append(descriptor);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 526 */     for (int i = 0; i < braceCount; i++) {
/* 527 */       sb.append("[]");
/*     */     }
/*     */     
/* 530 */     return sb.toString();
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
/*     */   private String getReturnTypeStringFromTypeSignature(boolean qualified) {
/* 544 */     String retType = null;
/* 545 */     if (this.signatureAttr != null) {
/* 546 */       retType = this.signatureAttr.getMethodReturnType(this, this.cf, qualified);
/*     */     }
/*     */     
/* 549 */     return retType;
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
/*     */   public String getSignature() {
/* 562 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*     */     
/* 565 */     if (!isConstructor()) {
/* 566 */       sb.append(getReturnTypeString(false));
/* 567 */       sb.append(' ');
/*     */     } 
/*     */ 
/*     */     
/* 571 */     sb.append(getName());
/* 572 */     sb.append('(');
/* 573 */     appendParamDescriptors(sb);
/* 574 */     sb.append(')');
/*     */ 
/*     */     
/* 577 */     for (AttributeInfo ai : this.attributes) {
/* 578 */       if (ai instanceof Exceptions) {
/* 579 */         sb.append(" throws ");
/* 580 */         Exceptions ex = (Exceptions)ai;
/* 581 */         for (int j = 0; j < ex.getExceptionCount(); j++) {
/* 582 */           sb.append(ex.getException(j));
/* 583 */           if (j < ex.getExceptionCount() - 1) {
/* 584 */             sb.append(", ");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 590 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 601 */     return ((getAccessFlags() & 0x400) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 611 */     String name = this.cf.getUtf8ValueFromConstantPool(this.nameIndex);
/* 612 */     return "<init>".equals(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNative() {
/* 622 */     return ((getAccessFlags() & 0x100) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/* 633 */     return ((getAccessFlags() & 0x8) > 0);
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
/*     */   public static MethodInfo read(ClassFile cf, DataInputStream in) throws IOException {
/* 647 */     int accessFlags = in.readUnsignedShort();
/* 648 */     int nameIndex = in.readUnsignedShort();
/* 649 */     int descriptorIndex = in.readUnsignedShort();
/* 650 */     MethodInfo mi = new MethodInfo(cf, accessFlags, nameIndex, descriptorIndex);
/*     */     
/* 652 */     int attrCount = in.readUnsignedShort();
/* 653 */     for (int j = 0; j < attrCount; j++) {
/* 654 */       AttributeInfo ai = mi.readAttribute(in);
/* 655 */       if (ai instanceof Signature) {
/* 656 */         mi.signatureAttr = (Signature)ai;
/*     */       }
/* 658 */       else if (ai instanceof Code) {
/* 659 */         mi.codeAttr = (Code)ai;
/*     */       }
/* 661 */       else if (ai != null) {
/* 662 */         mi.addAttribute(ai);
/*     */       } 
/*     */     } 
/* 665 */     return mi;
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
/* 681 */     int attributeNameIndex = in.readUnsignedShort();
/* 682 */     int attributeLength = in.readInt();
/*     */     
/* 684 */     String attrName = this.cf.getUtf8ValueFromConstantPool(attributeNameIndex);
/*     */     
/* 686 */     if ("Code".equals(attrName)) {
/* 687 */       Code code = Code.read(this, in);
/*     */     
/*     */     }
/* 690 */     else if ("Exceptions".equals(attrName)) {
/* 691 */       int exceptionCount = in.readUnsignedShort();
/* 692 */       int[] exceptionIndexTable = null;
/* 693 */       if (exceptionCount > 0) {
/* 694 */         exceptionIndexTable = new int[exceptionCount];
/* 695 */         for (int i = 0; i < exceptionCount; i++) {
/* 696 */           exceptionIndexTable[i] = in.readUnsignedShort();
/*     */         }
/*     */       } 
/* 699 */       Exceptions e = new Exceptions(this, exceptionIndexTable);
/* 700 */       Exceptions exceptions1 = e;
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 707 */       ai = readAttribute(in, attrName, attributeLength);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 713 */     return ai;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\MethodInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */