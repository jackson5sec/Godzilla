/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javassist.ClassPool;
/*     */ import javassist.bytecode.stackmap.MapMaker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ {
/*     */   ConstPool constPool;
/*     */   int accessFlags;
/*     */   int name;
/*     */   String cachedName;
/*     */   int descriptor;
/*     */   List<AttributeInfo> attribute;
/*     */   public static boolean doPreverify = false;
/*     */   public static final String nameInit = "<init>";
/*     */   public static final String nameClinit = "<clinit>";
/*     */   
/*     */   private MethodInfo(ConstPool cp) {
/*  83 */     this.constPool = cp;
/*  84 */     this.attribute = null;
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
/*     */   public MethodInfo(ConstPool cp, String methodname, String desc) {
/* 100 */     this(cp);
/* 101 */     this.accessFlags = 0;
/* 102 */     this.name = cp.addUtf8Info(methodname);
/* 103 */     this.cachedName = methodname;
/* 104 */     this.descriptor = this.constPool.addUtf8Info(desc);
/*     */   }
/*     */   
/*     */   MethodInfo(ConstPool cp, DataInputStream in) throws IOException {
/* 108 */     this(cp);
/* 109 */     read(in);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo(ConstPool cp, String methodname, MethodInfo src, Map<String, String> classnameMap) throws BadBytecode {
/* 134 */     this(cp);
/* 135 */     read(src, methodname, classnameMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return getName() + " " + getDescriptor();
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
/*     */   void compact(ConstPool cp) {
/* 155 */     this.name = cp.addUtf8Info(getName());
/* 156 */     this.descriptor = cp.addUtf8Info(getDescriptor());
/* 157 */     this.attribute = AttributeInfo.copyAll(this.attribute, cp);
/* 158 */     this.constPool = cp;
/*     */   }
/*     */   
/*     */   void prune(ConstPool cp) {
/* 162 */     List<AttributeInfo> newAttributes = new ArrayList<>();
/*     */ 
/*     */     
/* 165 */     AttributeInfo invisibleAnnotations = getAttribute("RuntimeInvisibleAnnotations");
/* 166 */     if (invisibleAnnotations != null) {
/* 167 */       invisibleAnnotations = invisibleAnnotations.copy(cp, null);
/* 168 */       newAttributes.add(invisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 172 */     AttributeInfo visibleAnnotations = getAttribute("RuntimeVisibleAnnotations");
/* 173 */     if (visibleAnnotations != null) {
/* 174 */       visibleAnnotations = visibleAnnotations.copy(cp, null);
/* 175 */       newAttributes.add(visibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 179 */     AttributeInfo parameterInvisibleAnnotations = getAttribute("RuntimeInvisibleParameterAnnotations");
/* 180 */     if (parameterInvisibleAnnotations != null) {
/* 181 */       parameterInvisibleAnnotations = parameterInvisibleAnnotations.copy(cp, null);
/* 182 */       newAttributes.add(parameterInvisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 186 */     AttributeInfo parameterVisibleAnnotations = getAttribute("RuntimeVisibleParameterAnnotations");
/* 187 */     if (parameterVisibleAnnotations != null) {
/* 188 */       parameterVisibleAnnotations = parameterVisibleAnnotations.copy(cp, null);
/* 189 */       newAttributes.add(parameterVisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 193 */     AnnotationDefaultAttribute defaultAttribute = (AnnotationDefaultAttribute)getAttribute("AnnotationDefault");
/* 194 */     if (defaultAttribute != null) {
/* 195 */       newAttributes.add(defaultAttribute);
/*     */     }
/* 197 */     ExceptionsAttribute ea = getExceptionsAttribute();
/* 198 */     if (ea != null) {
/* 199 */       newAttributes.add(ea);
/*     */     }
/*     */     
/* 202 */     AttributeInfo signature = getAttribute("Signature");
/* 203 */     if (signature != null) {
/* 204 */       signature = signature.copy(cp, null);
/* 205 */       newAttributes.add(signature);
/*     */     } 
/*     */     
/* 208 */     this.attribute = newAttributes;
/* 209 */     this.name = cp.addUtf8Info(getName());
/* 210 */     this.descriptor = cp.addUtf8Info(getDescriptor());
/* 211 */     this.constPool = cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 218 */     if (this.cachedName == null) {
/* 219 */       this.cachedName = this.constPool.getUtf8Info(this.name);
/*     */     }
/* 221 */     return this.cachedName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String newName) {
/* 228 */     this.name = this.constPool.addUtf8Info(newName);
/* 229 */     this.cachedName = newName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMethod() {
/* 237 */     String n = getName();
/* 238 */     return (!n.equals("<init>") && !n.equals("<clinit>"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/* 245 */     return this.constPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 252 */     return getName().equals("<init>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStaticInitializer() {
/* 259 */     return getName().equals("<clinit>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccessFlags() {
/* 268 */     return this.accessFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessFlags(int acc) {
/* 277 */     this.accessFlags = acc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptor() {
/* 286 */     return this.constPool.getUtf8Info(this.descriptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescriptor(String desc) {
/* 295 */     if (!desc.equals(getDescriptor())) {
/* 296 */       this.descriptor = this.constPool.addUtf8Info(desc);
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
/*     */   public List<AttributeInfo> getAttributes() {
/* 310 */     if (this.attribute == null) {
/* 311 */       this.attribute = new ArrayList<>();
/*     */     }
/* 313 */     return this.attribute;
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
/*     */   public AttributeInfo getAttribute(String name) {
/* 330 */     return AttributeInfo.lookup(this.attribute, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo removeAttribute(String name) {
/* 341 */     return AttributeInfo.remove(this.attribute, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(AttributeInfo info) {
/* 351 */     if (this.attribute == null) {
/* 352 */       this.attribute = new ArrayList<>();
/*     */     }
/* 354 */     AttributeInfo.remove(this.attribute, info.getName());
/* 355 */     this.attribute.add(info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionsAttribute getExceptionsAttribute() {
/* 364 */     AttributeInfo info = AttributeInfo.lookup(this.attribute, "Exceptions");
/*     */     
/* 366 */     return (ExceptionsAttribute)info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeAttribute getCodeAttribute() {
/* 375 */     AttributeInfo info = AttributeInfo.lookup(this.attribute, "Code");
/* 376 */     return (CodeAttribute)info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeExceptionsAttribute() {
/* 383 */     AttributeInfo.remove(this.attribute, "Exceptions");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionsAttribute(ExceptionsAttribute cattr) {
/* 394 */     removeExceptionsAttribute();
/* 395 */     if (this.attribute == null) {
/* 396 */       this.attribute = new ArrayList<>();
/*     */     }
/* 398 */     this.attribute.add(cattr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCodeAttribute() {
/* 405 */     AttributeInfo.remove(this.attribute, "Code");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCodeAttribute(CodeAttribute cattr) {
/* 416 */     removeCodeAttribute();
/* 417 */     if (this.attribute == null) {
/* 418 */       this.attribute = new ArrayList<>();
/*     */     }
/* 420 */     this.attribute.add(cattr);
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
/*     */   public void rebuildStackMapIf6(ClassPool pool, ClassFile cf) throws BadBytecode {
/* 439 */     if (cf.getMajorVersion() >= 50) {
/* 440 */       rebuildStackMap(pool);
/*     */     }
/* 442 */     if (doPreverify) {
/* 443 */       rebuildStackMapForME(pool);
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
/*     */   public void rebuildStackMap(ClassPool pool) throws BadBytecode {
/* 456 */     CodeAttribute ca = getCodeAttribute();
/* 457 */     if (ca != null) {
/* 458 */       StackMapTable smt = MapMaker.make(pool, this);
/* 459 */       ca.setAttribute(smt);
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
/*     */   public void rebuildStackMapForME(ClassPool pool) throws BadBytecode {
/* 473 */     CodeAttribute ca = getCodeAttribute();
/* 474 */     if (ca != null) {
/* 475 */       StackMap sm = MapMaker.make2(pool, this);
/* 476 */       ca.setAttribute(sm);
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
/*     */   public int getLineNumber(int pos) {
/* 490 */     CodeAttribute ca = getCodeAttribute();
/* 491 */     if (ca == null) {
/* 492 */       return -1;
/*     */     }
/*     */     
/* 495 */     LineNumberAttribute ainfo = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
/* 496 */     if (ainfo == null) {
/* 497 */       return -1;
/*     */     }
/* 499 */     return ainfo.toLineNumber(pos);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuperclass(String superclass) throws BadBytecode {
/* 524 */     if (!isConstructor()) {
/*     */       return;
/*     */     }
/* 527 */     CodeAttribute ca = getCodeAttribute();
/* 528 */     byte[] code = ca.getCode();
/* 529 */     CodeIterator iterator = ca.iterator();
/* 530 */     int pos = iterator.skipSuperConstructor();
/* 531 */     if (pos >= 0) {
/* 532 */       ConstPool cp = this.constPool;
/* 533 */       int mref = ByteArray.readU16bit(code, pos + 1);
/* 534 */       int nt = cp.getMethodrefNameAndType(mref);
/* 535 */       int sc = cp.addClassInfo(superclass);
/* 536 */       int mref2 = cp.addMethodrefInfo(sc, nt);
/* 537 */       ByteArray.write16bit(mref2, code, pos + 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void read(MethodInfo src, String methodname, Map<String, String> classnames) {
/* 542 */     ConstPool destCp = this.constPool;
/* 543 */     this.accessFlags = src.accessFlags;
/* 544 */     this.name = destCp.addUtf8Info(methodname);
/* 545 */     this.cachedName = methodname;
/* 546 */     ConstPool srcCp = src.constPool;
/* 547 */     String desc = srcCp.getUtf8Info(src.descriptor);
/* 548 */     String desc2 = Descriptor.rename(desc, classnames);
/* 549 */     this.descriptor = destCp.addUtf8Info(desc2);
/*     */     
/* 551 */     this.attribute = new ArrayList<>();
/* 552 */     ExceptionsAttribute eattr = src.getExceptionsAttribute();
/* 553 */     if (eattr != null) {
/* 554 */       this.attribute.add(eattr.copy(destCp, classnames));
/*     */     }
/* 556 */     CodeAttribute cattr = src.getCodeAttribute();
/* 557 */     if (cattr != null)
/* 558 */       this.attribute.add(cattr.copy(destCp, classnames)); 
/*     */   }
/*     */   
/*     */   private void read(DataInputStream in) throws IOException {
/* 562 */     this.accessFlags = in.readUnsignedShort();
/* 563 */     this.name = in.readUnsignedShort();
/* 564 */     this.descriptor = in.readUnsignedShort();
/* 565 */     int n = in.readUnsignedShort();
/* 566 */     this.attribute = new ArrayList<>();
/* 567 */     for (int i = 0; i < n; i++)
/* 568 */       this.attribute.add(AttributeInfo.read(this.constPool, in)); 
/*     */   }
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 572 */     out.writeShort(this.accessFlags);
/* 573 */     out.writeShort(this.name);
/* 574 */     out.writeShort(this.descriptor);
/*     */     
/* 576 */     if (this.attribute == null) {
/* 577 */       out.writeShort(0);
/*     */     } else {
/* 579 */       out.writeShort(this.attribute.size());
/* 580 */       AttributeInfo.writeAll(this.attribute, out);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\MethodInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */