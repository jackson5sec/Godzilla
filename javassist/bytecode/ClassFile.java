/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import javassist.CannotCompileException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassFile
/*     */ {
/*     */   int major;
/*     */   int minor;
/*     */   ConstPool constPool;
/*     */   int thisClass;
/*     */   int accessFlags;
/*     */   int superClass;
/*     */   int[] interfaces;
/*     */   List<FieldInfo> fields;
/*     */   List<MethodInfo> methods;
/*     */   List<AttributeInfo> attributes;
/*     */   String thisclassname;
/*     */   String[] cachedInterfaces;
/*     */   String cachedSuperclass;
/*     */   public static final int JAVA_1 = 45;
/*     */   public static final int JAVA_2 = 46;
/*     */   public static final int JAVA_3 = 47;
/*     */   public static final int JAVA_4 = 48;
/*     */   public static final int JAVA_5 = 49;
/*     */   public static final int JAVA_6 = 50;
/*     */   public static final int JAVA_7 = 51;
/*     */   public static final int JAVA_8 = 52;
/*     */   public static final int JAVA_9 = 53;
/*     */   public static final int JAVA_10 = 54;
/*     */   public static final int JAVA_11 = 55;
/*     */   public static final int MAJOR_VERSION;
/*     */   
/*     */   static {
/* 160 */     int ver = 47;
/*     */     try {
/* 162 */       Class.forName("java.lang.StringBuilder");
/* 163 */       ver = 49;
/* 164 */       Class.forName("java.util.zip.DeflaterInputStream");
/* 165 */       ver = 50;
/* 166 */       Class.forName("java.lang.invoke.CallSite", false, ClassLoader.getSystemClassLoader());
/* 167 */       ver = 51;
/* 168 */       Class.forName("java.util.function.Function");
/* 169 */       ver = 52;
/* 170 */       Class.forName("java.lang.Module");
/* 171 */       ver = 53;
/* 172 */       List.class.getMethod("copyOf", new Class[] { Collection.class });
/* 173 */       ver = 54;
/* 174 */       Class.forName("java.util.Optional").getMethod("isEmpty", new Class[0]);
/* 175 */       ver = 55;
/*     */     }
/* 177 */     catch (Throwable throwable) {}
/* 178 */     MAJOR_VERSION = ver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile(DataInputStream in) throws IOException {
/* 185 */     read(in);
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
/*     */   public ClassFile(boolean isInterface, String classname, String superclass) {
/* 199 */     this.major = MAJOR_VERSION;
/* 200 */     this.minor = 0;
/* 201 */     this.constPool = new ConstPool(classname);
/* 202 */     this.thisClass = this.constPool.getThisClassInfo();
/* 203 */     if (isInterface) {
/* 204 */       this.accessFlags = 1536;
/*     */     } else {
/* 206 */       this.accessFlags = 32;
/*     */     } 
/* 208 */     initSuperclass(superclass);
/* 209 */     this.interfaces = null;
/* 210 */     this.fields = new ArrayList<>();
/* 211 */     this.methods = new ArrayList<>();
/* 212 */     this.thisclassname = classname;
/*     */     
/* 214 */     this.attributes = new ArrayList<>();
/* 215 */     this.attributes.add(new SourceFileAttribute(this.constPool, 
/* 216 */           getSourcefileName(this.thisclassname)));
/*     */   }
/*     */   
/*     */   private void initSuperclass(String superclass) {
/* 220 */     if (superclass != null) {
/* 221 */       this.superClass = this.constPool.addClassInfo(superclass);
/* 222 */       this.cachedSuperclass = superclass;
/*     */     } else {
/*     */       
/* 225 */       this.superClass = this.constPool.addClassInfo("java.lang.Object");
/* 226 */       this.cachedSuperclass = "java.lang.Object";
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String getSourcefileName(String qname) {
/* 231 */     return qname.replaceAll("^.*\\.", "") + ".java";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void compact() {
/* 240 */     ConstPool cp = compact0();
/* 241 */     for (MethodInfo minfo : this.methods) {
/* 242 */       minfo.compact(cp);
/*     */     }
/* 244 */     for (FieldInfo finfo : this.fields) {
/* 245 */       finfo.compact(cp);
/*     */     }
/* 247 */     this.attributes = AttributeInfo.copyAll(this.attributes, cp);
/* 248 */     this.constPool = cp;
/*     */   }
/*     */   
/*     */   private ConstPool compact0() {
/* 252 */     ConstPool cp = new ConstPool(this.thisclassname);
/* 253 */     this.thisClass = cp.getThisClassInfo();
/* 254 */     String sc = getSuperclass();
/* 255 */     if (sc != null) {
/* 256 */       this.superClass = cp.addClassInfo(getSuperclass());
/*     */     }
/* 258 */     if (this.interfaces != null)
/* 259 */       for (int i = 0; i < this.interfaces.length; i++) {
/* 260 */         this.interfaces[i] = cp
/* 261 */           .addClassInfo(this.constPool.getClassInfo(this.interfaces[i]));
/*     */       } 
/* 263 */     return cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prune() {
/* 273 */     ConstPool cp = compact0();
/* 274 */     List<AttributeInfo> newAttributes = new ArrayList<>();
/*     */     
/* 276 */     AttributeInfo invisibleAnnotations = getAttribute("RuntimeInvisibleAnnotations");
/* 277 */     if (invisibleAnnotations != null) {
/* 278 */       invisibleAnnotations = invisibleAnnotations.copy(cp, null);
/* 279 */       newAttributes.add(invisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 283 */     AttributeInfo visibleAnnotations = getAttribute("RuntimeVisibleAnnotations");
/* 284 */     if (visibleAnnotations != null) {
/* 285 */       visibleAnnotations = visibleAnnotations.copy(cp, null);
/* 286 */       newAttributes.add(visibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 290 */     AttributeInfo signature = getAttribute("Signature");
/* 291 */     if (signature != null) {
/* 292 */       signature = signature.copy(cp, null);
/* 293 */       newAttributes.add(signature);
/*     */     } 
/*     */     
/* 296 */     for (MethodInfo minfo : this.methods) {
/* 297 */       minfo.prune(cp);
/*     */     }
/* 299 */     for (FieldInfo finfo : this.fields) {
/* 300 */       finfo.prune(cp);
/*     */     }
/* 302 */     this.attributes = newAttributes;
/* 303 */     this.constPool = cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/* 310 */     return this.constPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/* 317 */     return ((this.accessFlags & 0x200) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 324 */     return ((this.accessFlags & 0x10) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 331 */     return ((this.accessFlags & 0x400) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccessFlags() {
/* 340 */     return this.accessFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessFlags(int acc) {
/* 349 */     if ((acc & 0x200) == 0) {
/* 350 */       acc |= 0x20;
/*     */     }
/* 352 */     this.accessFlags = acc;
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
/*     */   public int getInnerAccessFlags() {
/* 365 */     InnerClassesAttribute ica = (InnerClassesAttribute)getAttribute("InnerClasses");
/* 366 */     if (ica == null) {
/* 367 */       return -1;
/*     */     }
/* 369 */     String name = getName();
/* 370 */     int n = ica.tableLength();
/* 371 */     for (int i = 0; i < n; i++) {
/* 372 */       if (name.equals(ica.innerClass(i)))
/* 373 */         return ica.accessFlags(i); 
/*     */     } 
/* 375 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 382 */     return this.thisclassname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 390 */     renameClass(this.thisclassname, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSuperclass() {
/* 397 */     if (this.cachedSuperclass == null) {
/* 398 */       this.cachedSuperclass = this.constPool.getClassInfo(this.superClass);
/*     */     }
/* 400 */     return this.cachedSuperclass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSuperclassId() {
/* 408 */     return this.superClass;
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
/*     */   public void setSuperclass(String superclass) throws CannotCompileException {
/* 420 */     if (superclass == null) {
/* 421 */       superclass = "java.lang.Object";
/*     */     }
/*     */     try {
/* 424 */       this.superClass = this.constPool.addClassInfo(superclass);
/* 425 */       for (MethodInfo minfo : this.methods) {
/* 426 */         minfo.setSuperclass(superclass);
/*     */       }
/* 428 */     } catch (BadBytecode e) {
/* 429 */       throw new CannotCompileException(e);
/*     */     } 
/* 431 */     this.cachedSuperclass = superclass;
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
/*     */   public final void renameClass(String oldname, String newname) {
/* 449 */     if (oldname.equals(newname)) {
/*     */       return;
/*     */     }
/* 452 */     if (oldname.equals(this.thisclassname)) {
/* 453 */       this.thisclassname = newname;
/*     */     }
/* 455 */     oldname = Descriptor.toJvmName(oldname);
/* 456 */     newname = Descriptor.toJvmName(newname);
/* 457 */     this.constPool.renameClass(oldname, newname);
/*     */     
/* 459 */     AttributeInfo.renameClass(this.attributes, oldname, newname);
/* 460 */     for (MethodInfo minfo : this.methods) {
/* 461 */       String desc = minfo.getDescriptor();
/* 462 */       minfo.setDescriptor(Descriptor.rename(desc, oldname, newname));
/* 463 */       AttributeInfo.renameClass(minfo.getAttributes(), oldname, newname);
/*     */     } 
/*     */     
/* 466 */     for (FieldInfo finfo : this.fields) {
/* 467 */       String desc = finfo.getDescriptor();
/* 468 */       finfo.setDescriptor(Descriptor.rename(desc, oldname, newname));
/* 469 */       AttributeInfo.renameClass(finfo.getAttributes(), oldname, newname);
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
/*     */   public final void renameClass(Map<String, String> classnames) {
/* 483 */     String jvmNewThisName = classnames.get(
/* 484 */         Descriptor.toJvmName(this.thisclassname));
/* 485 */     if (jvmNewThisName != null) {
/* 486 */       this.thisclassname = Descriptor.toJavaName(jvmNewThisName);
/*     */     }
/* 488 */     this.constPool.renameClass(classnames);
/*     */     
/* 490 */     AttributeInfo.renameClass(this.attributes, classnames);
/* 491 */     for (MethodInfo minfo : this.methods) {
/* 492 */       String desc = minfo.getDescriptor();
/* 493 */       minfo.setDescriptor(Descriptor.rename(desc, classnames));
/* 494 */       AttributeInfo.renameClass(minfo.getAttributes(), classnames);
/*     */     } 
/*     */     
/* 497 */     for (FieldInfo finfo : this.fields) {
/* 498 */       String desc = finfo.getDescriptor();
/* 499 */       finfo.setDescriptor(Descriptor.rename(desc, classnames));
/* 500 */       AttributeInfo.renameClass(finfo.getAttributes(), classnames);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void getRefClasses(Map<String, String> classnames) {
/* 509 */     this.constPool.renameClass(classnames);
/*     */     
/* 511 */     AttributeInfo.getRefClasses(this.attributes, classnames);
/* 512 */     for (MethodInfo minfo : this.methods) {
/* 513 */       String desc = minfo.getDescriptor();
/* 514 */       Descriptor.rename(desc, classnames);
/* 515 */       AttributeInfo.getRefClasses(minfo.getAttributes(), classnames);
/*     */     } 
/*     */     
/* 518 */     for (FieldInfo finfo : this.fields) {
/* 519 */       String desc = finfo.getDescriptor();
/* 520 */       Descriptor.rename(desc, classnames);
/* 521 */       AttributeInfo.getRefClasses(finfo.getAttributes(), classnames);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getInterfaces() {
/* 530 */     if (this.cachedInterfaces != null) {
/* 531 */       return this.cachedInterfaces;
/*     */     }
/* 533 */     String[] rtn = null;
/* 534 */     if (this.interfaces == null) {
/* 535 */       rtn = new String[0];
/*     */     } else {
/* 537 */       String[] list = new String[this.interfaces.length];
/* 538 */       for (int i = 0; i < this.interfaces.length; i++) {
/* 539 */         list[i] = this.constPool.getClassInfo(this.interfaces[i]);
/*     */       }
/* 541 */       rtn = list;
/*     */     } 
/*     */     
/* 544 */     this.cachedInterfaces = rtn;
/* 545 */     return rtn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterfaces(String[] nameList) {
/* 555 */     this.cachedInterfaces = null;
/* 556 */     if (nameList != null) {
/* 557 */       this.interfaces = new int[nameList.length];
/* 558 */       for (int i = 0; i < nameList.length; i++) {
/* 559 */         this.interfaces[i] = this.constPool.addClassInfo(nameList[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInterface(String name) {
/* 567 */     this.cachedInterfaces = null;
/* 568 */     int info = this.constPool.addClassInfo(name);
/* 569 */     if (this.interfaces == null) {
/* 570 */       this.interfaces = new int[1];
/* 571 */       this.interfaces[0] = info;
/*     */     } else {
/*     */       
/* 574 */       int n = this.interfaces.length;
/* 575 */       int[] newarray = new int[n + 1];
/* 576 */       System.arraycopy(this.interfaces, 0, newarray, 0, n);
/* 577 */       newarray[n] = info;
/* 578 */       this.interfaces = newarray;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FieldInfo> getFields() {
/* 589 */     return this.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addField(FieldInfo finfo) throws DuplicateMemberException {
/* 598 */     testExistingField(finfo.getName(), finfo.getDescriptor());
/* 599 */     this.fields.add(finfo);
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
/*     */   public final void addField2(FieldInfo finfo) {
/* 611 */     this.fields.add(finfo);
/*     */   }
/*     */ 
/*     */   
/*     */   private void testExistingField(String name, String descriptor) throws DuplicateMemberException {
/* 616 */     for (FieldInfo minfo : this.fields) {
/* 617 */       if (minfo.getName().equals(name)) {
/* 618 */         throw new DuplicateMemberException("duplicate field: " + name);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MethodInfo> getMethods() {
/* 628 */     return this.methods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getMethod(String name) {
/* 638 */     for (MethodInfo minfo : this.methods) {
/* 639 */       if (minfo.getName().equals(name))
/* 640 */         return minfo; 
/* 641 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getStaticInitializer() {
/* 649 */     return getMethod("<clinit>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMethod(MethodInfo minfo) throws DuplicateMemberException {
/* 660 */     testExistingMethod(minfo);
/* 661 */     this.methods.add(minfo);
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
/*     */   public final void addMethod2(MethodInfo minfo) {
/* 673 */     this.methods.add(minfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void testExistingMethod(MethodInfo newMinfo) throws DuplicateMemberException {
/* 679 */     String name = newMinfo.getName();
/* 680 */     String descriptor = newMinfo.getDescriptor();
/* 681 */     ListIterator<MethodInfo> it = this.methods.listIterator(0);
/* 682 */     while (it.hasNext()) {
/* 683 */       if (isDuplicated(newMinfo, name, descriptor, it.next(), it)) {
/* 684 */         throw new DuplicateMemberException("duplicate method: " + name + " in " + 
/* 685 */             getName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDuplicated(MethodInfo newMethod, String newName, String newDesc, MethodInfo minfo, ListIterator<MethodInfo> it) {
/* 692 */     if (!minfo.getName().equals(newName)) {
/* 693 */       return false;
/*     */     }
/* 695 */     String desc = minfo.getDescriptor();
/* 696 */     if (!Descriptor.eqParamTypes(desc, newDesc)) {
/* 697 */       return false;
/*     */     }
/* 699 */     if (desc.equals(newDesc)) {
/* 700 */       if (notBridgeMethod(minfo)) {
/* 701 */         return true;
/*     */       }
/*     */       
/* 704 */       it.remove();
/* 705 */       return false;
/*     */     } 
/* 707 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean notBridgeMethod(MethodInfo minfo) {
/* 714 */     return ((minfo.getAccessFlags() & 0x40) == 0);
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
/*     */   public List<AttributeInfo> getAttributes() {
/* 728 */     return this.attributes;
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
/* 745 */     for (AttributeInfo ai : this.attributes) {
/* 746 */       if (ai.getName().equals(name))
/* 747 */         return ai; 
/* 748 */     }  return null;
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
/* 759 */     return AttributeInfo.remove(this.attributes, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(AttributeInfo info) {
/* 769 */     AttributeInfo.remove(this.attributes, info.getName());
/* 770 */     this.attributes.add(info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSourceFile() {
/* 780 */     SourceFileAttribute sf = (SourceFileAttribute)getAttribute("SourceFile");
/* 781 */     if (sf == null)
/* 782 */       return null; 
/* 783 */     return sf.getFileName();
/*     */   }
/*     */ 
/*     */   
/*     */   private void read(DataInputStream in) throws IOException {
/* 788 */     int magic = in.readInt();
/* 789 */     if (magic != -889275714) {
/* 790 */       throw new IOException("bad magic number: " + Integer.toHexString(magic));
/*     */     }
/* 792 */     this.minor = in.readUnsignedShort();
/* 793 */     this.major = in.readUnsignedShort();
/* 794 */     this.constPool = new ConstPool(in);
/* 795 */     this.accessFlags = in.readUnsignedShort();
/* 796 */     this.thisClass = in.readUnsignedShort();
/* 797 */     this.constPool.setThisClassInfo(this.thisClass);
/* 798 */     this.superClass = in.readUnsignedShort();
/* 799 */     int n = in.readUnsignedShort();
/* 800 */     if (n == 0) {
/* 801 */       this.interfaces = null;
/*     */     } else {
/* 803 */       this.interfaces = new int[n];
/* 804 */       for (int j = 0; j < n; j++) {
/* 805 */         this.interfaces[j] = in.readUnsignedShort();
/*     */       }
/*     */     } 
/* 808 */     ConstPool cp = this.constPool;
/* 809 */     n = in.readUnsignedShort();
/* 810 */     this.fields = new ArrayList<>(); int i;
/* 811 */     for (i = 0; i < n; i++) {
/* 812 */       addField2(new FieldInfo(cp, in));
/*     */     }
/* 814 */     n = in.readUnsignedShort();
/* 815 */     this.methods = new ArrayList<>();
/* 816 */     for (i = 0; i < n; i++) {
/* 817 */       addMethod2(new MethodInfo(cp, in));
/*     */     }
/* 819 */     this.attributes = new ArrayList<>();
/* 820 */     n = in.readUnsignedShort();
/* 821 */     for (i = 0; i < n; i++) {
/* 822 */       addAttribute(AttributeInfo.read(cp, in));
/*     */     }
/* 824 */     this.thisclassname = this.constPool.getClassInfo(this.thisClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutputStream out) throws IOException {
/* 833 */     out.writeInt(-889275714);
/* 834 */     out.writeShort(this.minor);
/* 835 */     out.writeShort(this.major);
/* 836 */     this.constPool.write(out);
/* 837 */     out.writeShort(this.accessFlags);
/* 838 */     out.writeShort(this.thisClass);
/* 839 */     out.writeShort(this.superClass);
/*     */     
/* 841 */     if (this.interfaces == null) {
/* 842 */       n = 0;
/*     */     } else {
/* 844 */       n = this.interfaces.length;
/*     */     } 
/* 846 */     out.writeShort(n); int i;
/* 847 */     for (i = 0; i < n; i++) {
/* 848 */       out.writeShort(this.interfaces[i]);
/*     */     }
/* 850 */     int n = this.fields.size();
/* 851 */     out.writeShort(n);
/* 852 */     for (i = 0; i < n; i++) {
/* 853 */       FieldInfo finfo = this.fields.get(i);
/* 854 */       finfo.write(out);
/*     */     } 
/*     */     
/* 857 */     out.writeShort(this.methods.size());
/* 858 */     for (MethodInfo minfo : this.methods) {
/* 859 */       minfo.write(out);
/*     */     }
/* 861 */     out.writeShort(this.attributes.size());
/* 862 */     AttributeInfo.writeAll(this.attributes, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMajorVersion() {
/* 871 */     return this.major;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMajorVersion(int major) {
/* 881 */     this.major = major;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinorVersion() {
/* 890 */     return this.minor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinorVersion(int minor) {
/* 900 */     this.minor = minor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersionToJava5() {
/* 911 */     this.major = 49;
/* 912 */     this.minor = 0;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\ClassFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */