/*     */ package org.fife.rsta.ac.java.classreader;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.AttributeInfo;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.Signature;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.SourceFile;
/*     */ import org.fife.rsta.ac.java.classreader.attributes.UnsupportedAttribute;
/*     */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantClassInfo;
/*     */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantPoolInfo;
/*     */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantPoolInfoFactory;
/*     */ import org.fife.rsta.ac.java.classreader.constantpool.ConstantUtf8Info;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassFile
/*     */   implements AccessFlags
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private int minorVersion;
/*     */   private int majorVersion;
/*     */   private ConstantPoolInfo[] constantPool;
/*     */   private int accessFlags;
/*     */   private int thisClass;
/*     */   private int superClass;
/*     */   int[] interfaces;
/*     */   private FieldInfo[] fields;
/*     */   private MethodInfo[] methods;
/*     */   private boolean deprecated;
/*     */   private AttributeInfo[] attributes;
/*     */   private List<String> paramTypes;
/*     */   private Map<String, String> typeMap;
/*     */   public static final String DEPRECATED = "Deprecated";
/*     */   public static final String ENCLOSING_METHOD = "EnclosingMethod";
/*     */   public static final String INNER_CLASSES = "InnerClasses";
/*     */   public static final String RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";
/*     */   public static final String SIGNATURE = "Signature";
/*     */   public static final String SOURCE_FILE = "SourceFile";
/*     */   public static final String BOOTSTRAP_METHODS = "BootstrapMethods";
/* 120 */   private static final byte[] HEADER = new byte[] { -54, -2, -70, -66 };
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile(File classFile) throws IOException {
/* 125 */     try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(classFile)))) {
/*     */       
/* 127 */       init(in);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassFile(DataInputStream in) throws IOException {
/* 133 */     init(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void debugPrint(String text) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccessFlags() {
/* 151 */     return this.accessFlags;
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
/* 163 */     return this.attributes[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeCount() {
/* 174 */     return (this.attributes == null) ? 0 : this.attributes.length;
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
/*     */   public String getClassName(boolean fullyQualified) {
/* 186 */     return getClassNameFromConstantPool(this.thisClass, fullyQualified);
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
/*     */   protected String getClassNameFromConstantPool(int cpIndex, boolean fullyQualified) {
/* 203 */     ConstantPoolInfo cpi = getConstantPoolInfo(cpIndex);
/*     */     
/* 205 */     if (cpi instanceof ConstantClassInfo) {
/* 206 */       ConstantClassInfo cci = (ConstantClassInfo)cpi;
/* 207 */       int index = cci.getNameIndex();
/* 208 */       ConstantUtf8Info cui = (ConstantUtf8Info)getConstantPoolInfo(index);
/* 209 */       String className = cui.getRepresentedString(false);
/* 210 */       if (fullyQualified) {
/* 211 */         className = className.replace('/', '.');
/*     */       } else {
/*     */         
/* 214 */         className = className.substring(className.lastIndexOf('/') + 1);
/*     */       } 
/* 216 */       return className.replace('$', '.');
/*     */     } 
/*     */ 
/*     */     
/* 220 */     throw new InternalError("Expected ConstantClassInfo, found " + cpi
/* 221 */         .getClass().toString());
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
/*     */   public int getConstantPoolCount() {
/* 233 */     return this.constantPool.length + 1;
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
/*     */   public ConstantPoolInfo getConstantPoolInfo(int index) {
/* 250 */     return (index != 0) ? this.constantPool[index - 1] : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFieldCount() {
/* 261 */     return (this.fields == null) ? 0 : this.fields.length;
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
/*     */   public FieldInfo getFieldInfo(int index) {
/* 274 */     return this.fields[index];
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
/*     */   public FieldInfo getFieldInfoByName(String name) {
/* 287 */     for (int i = 0; i < getFieldCount(); i++) {
/* 288 */       if (name.equals(this.fields[i].getName())) {
/* 289 */         return this.fields[i];
/*     */       }
/*     */     } 
/* 292 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getImplementedInterfaceCount() {
/* 303 */     return (this.interfaces == null) ? 0 : this.interfaces.length;
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
/*     */   public String getImplementedInterfaceName(int index, boolean fullyQualified) {
/* 319 */     return getClassNameFromConstantPool(this.interfaces[index], fullyQualified);
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
/*     */   public int getMethodCount() {
/* 331 */     return (this.methods == null) ? 0 : this.methods.length;
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
/*     */   public MethodInfo getMethodInfo(int index) {
/* 344 */     return this.methods[index];
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
/*     */   public List<MethodInfo> getMethodInfoByName(String name) {
/* 357 */     return getMethodInfoByName(name, -1);
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
/*     */   public List<MethodInfo> getMethodInfoByName(String name, int argCount) {
/* 374 */     List<MethodInfo> methods = null;
/* 375 */     for (int i = 0; i < getMethodCount(); i++) {
/* 376 */       MethodInfo info = this.methods[i];
/* 377 */       if (name.equals(info.getName()) && (
/* 378 */         argCount < 0 || argCount == info.getParameterCount())) {
/* 379 */         if (methods == null) {
/* 380 */           methods = new ArrayList<>(1);
/*     */         }
/* 382 */         methods.add(info);
/*     */       } 
/*     */     } 
/*     */     
/* 386 */     return methods;
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
/*     */   public String getPackageName() {
/* 398 */     String className = getClassName(true);
/* 399 */     int dot = className.lastIndexOf('.');
/* 400 */     return (dot == -1) ? null : className.substring(0, dot);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getParamTypes() {
/* 405 */     return this.paramTypes;
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
/*     */   public String getSuperClassName(boolean fullyQualified) {
/* 423 */     if (this.superClass == 0) {
/* 424 */       return null;
/*     */     }
/* 426 */     return getClassNameFromConstantPool(this.superClass, fullyQualified);
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
/*     */   public String getTypeArgument(String typeParam) {
/* 447 */     return (this.typeMap == null) ? "Object" : this.typeMap.get(typeParam);
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
/*     */   public String getUtf8ValueFromConstantPool(int index) {
/* 461 */     ConstantPoolInfo cpi = getConstantPoolInfo(index);
/* 462 */     ConstantUtf8Info cui = (ConstantUtf8Info)cpi;
/* 463 */     return cui.getRepresentedString(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersionString() {
/* 474 */     return this.majorVersion + "." + this.minorVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(DataInputStream in) throws IOException {
/* 485 */     readHeader(in);
/* 486 */     readVersion(in);
/* 487 */     readConstantPoolInfos(in);
/* 488 */     readAccessFlags(in);
/* 489 */     readThisClass(in);
/* 490 */     readSuperClass(in);
/* 491 */     readInterfaces(in);
/* 492 */     readFields(in);
/* 493 */     readMethods(in);
/* 494 */     readAttributes(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeprecated() {
/* 504 */     return this.deprecated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readAccessFlags(DataInputStream in) throws IOException {
/* 515 */     this.accessFlags = in.readUnsignedShort();
/* 516 */     debugPrint("Access flags: " + this.accessFlags);
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
/*     */   private AttributeInfo readAttribute(DataInputStream in) throws IOException {
/*     */     UnsupportedAttribute unsupportedAttribute;
/* 529 */     AttributeInfo ai = null;
/*     */     
/* 531 */     int attributeNameIndex = in.readUnsignedShort();
/* 532 */     int attributeLength = in.readInt();
/*     */     
/* 534 */     String attrName = getUtf8ValueFromConstantPool(attributeNameIndex);
/* 535 */     debugPrint("Found class attribute: " + attrName);
/*     */     
/* 537 */     if ("SourceFile".equals(attrName)) {
/* 538 */       int sourceFileIndex = in.readUnsignedShort();
/* 539 */       SourceFile sf = new SourceFile(this, sourceFileIndex);
/* 540 */       SourceFile sourceFile1 = sf;
/*     */     
/*     */     }
/* 543 */     else if ("BootstrapMethods".equals(attrName)) {
/*     */ 
/*     */       
/* 546 */       Util.skipBytes(in, attributeLength);
/*     */ 
/*     */     
/*     */     }
/* 550 */     else if ("Signature".equals(attrName)) {
/* 551 */       int signatureIndex = in.readUnsignedShort();
/* 552 */       String sig = getUtf8ValueFromConstantPool(signatureIndex);
/*     */       
/* 554 */       Signature signature = new Signature(this, sig);
/* 555 */       this.paramTypes = signature.getClassParamTypes();
/*     */     
/*     */     }
/* 558 */     else if ("InnerClasses".equals(attrName)) {
/*     */ 
/*     */       
/* 561 */       Util.skipBytes(in, attributeLength);
/*     */ 
/*     */     
/*     */     }
/* 565 */     else if ("EnclosingMethod".equals(attrName)) {
/*     */ 
/*     */       
/* 568 */       Util.skipBytes(in, attributeLength);
/*     */ 
/*     */     
/*     */     }
/* 572 */     else if ("Deprecated".equals(attrName)) {
/*     */       
/* 574 */       this.deprecated = true;
/*     */     
/*     */     }
/* 577 */     else if ("RuntimeVisibleAnnotations".equals(attrName)) {
/*     */ 
/*     */       
/* 580 */       Util.skipBytes(in, attributeLength);
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 587 */       debugPrint("Unsupported class attribute: " + attrName);
/* 588 */       unsupportedAttribute = AttributeInfo.readUnsupportedAttribute(this, in, attrName, attributeLength);
/*     */     } 
/*     */ 
/*     */     
/* 592 */     return (AttributeInfo)unsupportedAttribute;
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
/*     */   private void readAttributes(DataInputStream in) throws IOException {
/* 604 */     int attributeCount = in.readUnsignedShort();
/* 605 */     if (attributeCount > 0) {
/* 606 */       this.attributes = new AttributeInfo[attributeCount];
/* 607 */       for (int i = 0; i < attributeCount; i++) {
/* 608 */         this.attributes[i] = readAttribute(in);
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
/*     */   private void readConstantPoolInfos(DataInputStream in) throws IOException {
/* 625 */     int constantPoolCount = in.readUnsignedShort() - 1;
/* 626 */     debugPrint("Constant pool count: " + constantPoolCount);
/*     */     
/* 628 */     this.constantPool = new ConstantPoolInfo[constantPoolCount];
/*     */     
/* 630 */     for (int i = 0; i < constantPoolCount; i++) {
/* 631 */       ConstantPoolInfo cpi = ConstantPoolInfoFactory.readConstantPoolInfo(this, in);
/* 632 */       this.constantPool[i] = cpi;
/*     */ 
/*     */       
/* 635 */       if (cpi instanceof org.fife.rsta.ac.java.classreader.constantpool.ConstantLongInfo || cpi instanceof org.fife.rsta.ac.java.classreader.constantpool.ConstantDoubleInfo)
/*     */       {
/* 637 */         i++;
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
/*     */   private void readFields(DataInputStream in) throws IOException {
/* 651 */     int fieldCount = in.readUnsignedShort();
/* 652 */     if (fieldCount > 0) {
/* 653 */       this.fields = new FieldInfo[fieldCount];
/* 654 */       for (int i = 0; i < fieldCount; i++) {
/* 655 */         this.fields[i] = FieldInfo.read(this, in);
/*     */       }
/*     */     } 
/* 658 */     debugPrint("fieldCount: " + fieldCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readHeader(DataInputStream in) throws IOException {
/* 669 */     for (byte b1 : HEADER) {
/* 670 */       byte b = in.readByte();
/* 671 */       if (b != b1) {
/* 672 */         throw new IOException("\"CAFEBABE\" header not found");
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
/*     */   private void readInterfaces(DataInputStream in) throws IOException {
/* 686 */     int interfaceCount = in.readUnsignedShort();
/* 687 */     if (interfaceCount > 0) {
/* 688 */       this.interfaces = new int[interfaceCount];
/* 689 */       for (int i = 0; i < interfaceCount; i++) {
/* 690 */         this.interfaces[i] = in.readUnsignedShort();
/*     */       }
/*     */     } 
/* 693 */     debugPrint("interfaceCount: " + interfaceCount);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readMethods(DataInputStream in) throws IOException {
/* 698 */     int methodCount = in.readUnsignedShort();
/* 699 */     if (methodCount > 0) {
/* 700 */       this.methods = new MethodInfo[methodCount];
/* 701 */       for (int i = 0; i < methodCount; i++) {
/* 702 */         this.methods[i] = MethodInfo.read(this, in);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readSuperClass(DataInputStream in) throws IOException {
/* 709 */     this.superClass = in.readUnsignedShort();
/* 710 */     ConstantPoolInfo cpi = getConstantPoolInfo(this.superClass);
/* 711 */     debugPrint("superClass: " + cpi);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readThisClass(DataInputStream in) throws IOException {
/* 716 */     this.thisClass = in.readUnsignedShort();
/* 717 */     ConstantPoolInfo cpi = getConstantPoolInfo(this.thisClass);
/* 718 */     debugPrint("thisClass: " + cpi);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readVersion(DataInputStream in) throws IOException {
/* 729 */     this.minorVersion = in.readUnsignedShort();
/* 730 */     this.majorVersion = in.readUnsignedShort();
/* 731 */     debugPrint("Class file version: " + getVersionString());
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
/*     */   public void setTypeParamsToTypeArgs(Map<String, String> typeMap) {
/* 747 */     this.typeMap = typeMap;
/* 748 */     for (int i = 0; i < getMethodCount(); i++) {
/* 749 */       getMethodInfo(i).clearParamTypeInfo();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 756 */     return "[ClassFile: accessFlags=" + this.accessFlags + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\classreader\ClassFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */