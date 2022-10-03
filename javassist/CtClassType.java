/*      */ package javassist;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javassist.bytecode.AccessFlag;
/*      */ import javassist.bytecode.AnnotationsAttribute;
/*      */ import javassist.bytecode.AttributeInfo;
/*      */ import javassist.bytecode.BadBytecode;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.CodeAttribute;
/*      */ import javassist.bytecode.CodeIterator;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.ConstantAttribute;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.EnclosingMethodAttribute;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.InnerClassesAttribute;
/*      */ import javassist.bytecode.MethodInfo;
/*      */ import javassist.bytecode.ParameterAnnotationsAttribute;
/*      */ import javassist.bytecode.SignatureAttribute;
/*      */ import javassist.bytecode.annotation.Annotation;
/*      */ import javassist.bytecode.annotation.AnnotationImpl;
/*      */ import javassist.compiler.AccessorMaker;
/*      */ import javassist.compiler.CompileError;
/*      */ import javassist.compiler.Javac;
/*      */ import javassist.expr.ExprEditor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class CtClassType
/*      */   extends CtClass
/*      */ {
/*      */   ClassPool classPool;
/*      */   boolean wasChanged;
/*      */   private boolean wasFrozen;
/*      */   boolean wasPruned;
/*      */   boolean gcConstPool;
/*      */   ClassFile classfile;
/*      */   byte[] rawClassfile;
/*      */   private Reference<CtMember.Cache> memberCache;
/*      */   private AccessorMaker accessors;
/*      */   private FieldInitLink fieldInitializers;
/*      */   private Map<CtMethod, String> hiddenMethods;
/*      */   private int uniqueNumberSeed;
/*   78 */   private boolean doPruning = ClassPool.doPruning;
/*      */   private int getCount;
/*      */   private static final int GET_THRESHOLD = 2;
/*      */   
/*      */   CtClassType(String name, ClassPool cp) {
/*   83 */     super(name);
/*   84 */     this.classPool = cp;
/*   85 */     this.wasChanged = this.wasFrozen = this.wasPruned = this.gcConstPool = false;
/*   86 */     this.classfile = null;
/*   87 */     this.rawClassfile = null;
/*   88 */     this.memberCache = null;
/*   89 */     this.accessors = null;
/*   90 */     this.fieldInitializers = null;
/*   91 */     this.hiddenMethods = null;
/*   92 */     this.uniqueNumberSeed = 0;
/*   93 */     this.getCount = 0;
/*      */   }
/*      */   
/*      */   CtClassType(InputStream ins, ClassPool cp) throws IOException {
/*   97 */     this((String)null, cp);
/*   98 */     this.classfile = new ClassFile(new DataInputStream(ins));
/*   99 */     this.qualifiedName = this.classfile.getName();
/*      */   }
/*      */   
/*      */   CtClassType(ClassFile cf, ClassPool cp) {
/*  103 */     this((String)null, cp);
/*  104 */     this.classfile = cf;
/*  105 */     this.qualifiedName = this.classfile.getName();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void extendToString(StringBuffer buffer) {
/*  110 */     if (this.wasChanged) {
/*  111 */       buffer.append("changed ");
/*      */     }
/*  113 */     if (this.wasFrozen) {
/*  114 */       buffer.append("frozen ");
/*      */     }
/*  116 */     if (this.wasPruned) {
/*  117 */       buffer.append("pruned ");
/*      */     }
/*  119 */     buffer.append(Modifier.toString(getModifiers()));
/*  120 */     buffer.append(" class ");
/*  121 */     buffer.append(getName());
/*      */     
/*      */     try {
/*  124 */       CtClass ext = getSuperclass();
/*  125 */       if (ext != null) {
/*  126 */         String name = ext.getName();
/*  127 */         if (!name.equals("java.lang.Object")) {
/*  128 */           buffer.append(" extends " + ext.getName());
/*      */         }
/*      */       } 
/*  131 */     } catch (NotFoundException e) {
/*  132 */       buffer.append(" extends ??");
/*      */     } 
/*      */     
/*      */     try {
/*  136 */       CtClass[] intf = getInterfaces();
/*  137 */       if (intf.length > 0) {
/*  138 */         buffer.append(" implements ");
/*      */       }
/*  140 */       for (int i = 0; i < intf.length; i++) {
/*  141 */         buffer.append(intf[i].getName());
/*  142 */         buffer.append(", ");
/*      */       }
/*      */     
/*  145 */     } catch (NotFoundException e) {
/*  146 */       buffer.append(" extends ??");
/*      */     } 
/*      */     
/*  149 */     CtMember.Cache memCache = getMembers();
/*  150 */     exToString(buffer, " fields=", memCache
/*  151 */         .fieldHead(), memCache.lastField());
/*  152 */     exToString(buffer, " constructors=", memCache
/*  153 */         .consHead(), memCache.lastCons());
/*  154 */     exToString(buffer, " methods=", memCache
/*  155 */         .methodHead(), memCache.lastMethod());
/*      */   }
/*      */ 
/*      */   
/*      */   private void exToString(StringBuffer buffer, String msg, CtMember head, CtMember tail) {
/*  160 */     buffer.append(msg);
/*  161 */     while (head != tail) {
/*  162 */       head = head.next();
/*  163 */       buffer.append(head);
/*  164 */       buffer.append(", ");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public AccessorMaker getAccessorMaker() {
/*  170 */     if (this.accessors == null) {
/*  171 */       this.accessors = new AccessorMaker(this);
/*      */     }
/*  173 */     return this.accessors;
/*      */   }
/*      */ 
/*      */   
/*      */   public ClassFile getClassFile2() {
/*  178 */     return getClassFile3(true);
/*      */   }
/*      */   
/*      */   public ClassFile getClassFile3(boolean doCompress) {
/*      */     byte[] rcfile;
/*  183 */     ClassFile cfile = this.classfile;
/*  184 */     if (cfile != null) {
/*  185 */       return cfile;
/*      */     }
/*  187 */     if (doCompress) {
/*  188 */       this.classPool.compress();
/*      */     }
/*      */     
/*  191 */     synchronized (this) {
/*      */       
/*  193 */       cfile = this.classfile;
/*  194 */       if (cfile != null) {
/*  195 */         return cfile;
/*      */       }
/*  197 */       rcfile = this.rawClassfile;
/*      */     } 
/*      */     
/*  200 */     if (rcfile != null) {
/*      */       ClassFile cf;
/*      */       try {
/*  203 */         cf = new ClassFile(new DataInputStream(new ByteArrayInputStream(rcfile)));
/*      */       }
/*  205 */       catch (IOException e) {
/*  206 */         throw new RuntimeException(e.toString(), e);
/*      */       } 
/*  208 */       this.getCount = 2;
/*  209 */       synchronized (this) {
/*  210 */         this.rawClassfile = null;
/*  211 */         return setClassFile(cf);
/*      */       } 
/*      */     } 
/*      */     
/*  215 */     InputStream inputStream = null;
/*      */     try {
/*  217 */       inputStream = this.classPool.openClassfile(getName());
/*  218 */       if (inputStream == null) {
/*  219 */         throw new NotFoundException(getName());
/*      */       }
/*  221 */       inputStream = new BufferedInputStream(inputStream);
/*  222 */       ClassFile cf = new ClassFile(new DataInputStream(inputStream));
/*  223 */       if (!cf.getName().equals(this.qualifiedName)) {
/*  224 */         throw new RuntimeException("cannot find " + this.qualifiedName + ": " + cf
/*  225 */             .getName() + " found in " + this.qualifiedName
/*  226 */             .replace('.', '/') + ".class");
/*      */       }
/*  228 */       return setClassFile(cf);
/*      */     }
/*  230 */     catch (NotFoundException e) {
/*  231 */       throw new RuntimeException(e.toString(), e);
/*      */     }
/*  233 */     catch (IOException e) {
/*  234 */       throw new RuntimeException(e.toString(), e);
/*      */     } finally {
/*      */       
/*  237 */       if (inputStream != null) {
/*      */         try {
/*  239 */           inputStream.close();
/*      */         }
/*  241 */         catch (IOException iOException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void incGetCounter() {
/*  251 */     this.getCount++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void compress() {
/*  260 */     if (this.getCount < 2)
/*  261 */       if (!isModified() && ClassPool.releaseUnmodifiedClassFile) {
/*  262 */         removeClassFile();
/*  263 */       } else if (isFrozen() && !this.wasPruned) {
/*  264 */         saveClassFile();
/*      */       }  
/*  266 */     this.getCount = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void saveClassFile() {
/*  276 */     if (this.classfile == null || hasMemberCache() != null) {
/*      */       return;
/*      */     }
/*  279 */     ByteArrayOutputStream barray = new ByteArrayOutputStream();
/*  280 */     DataOutputStream out = new DataOutputStream(barray);
/*      */     try {
/*  282 */       this.classfile.write(out);
/*  283 */       barray.close();
/*  284 */       this.rawClassfile = barray.toByteArray();
/*  285 */       this.classfile = null;
/*      */     }
/*  287 */     catch (IOException iOException) {}
/*      */   }
/*      */   
/*      */   private synchronized void removeClassFile() {
/*  291 */     if (this.classfile != null && !isModified() && hasMemberCache() == null) {
/*  292 */       this.classfile = null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized ClassFile setClassFile(ClassFile cf) {
/*  299 */     if (this.classfile == null) {
/*  300 */       this.classfile = cf;
/*      */     }
/*  302 */     return this.classfile;
/*      */   }
/*      */   
/*      */   public ClassPool getClassPool() {
/*  306 */     return this.classPool;
/*      */   } void setClassPool(ClassPool cp) {
/*  308 */     this.classPool = cp;
/*      */   }
/*      */   
/*      */   public URL getURL() throws NotFoundException {
/*  312 */     URL url = this.classPool.find(getName());
/*  313 */     if (url == null)
/*  314 */       throw new NotFoundException(getName()); 
/*  315 */     return url;
/*      */   }
/*      */   
/*      */   public boolean isModified() {
/*  319 */     return this.wasChanged;
/*      */   }
/*      */   public boolean isFrozen() {
/*  322 */     return this.wasFrozen;
/*      */   }
/*      */   public void freeze() {
/*  325 */     this.wasFrozen = true;
/*      */   }
/*      */   
/*      */   void checkModify() throws RuntimeException {
/*  329 */     if (isFrozen()) {
/*  330 */       String msg = getName() + " class is frozen";
/*  331 */       if (this.wasPruned) {
/*  332 */         msg = msg + " and pruned";
/*      */       }
/*  334 */       throw new RuntimeException(msg);
/*      */     } 
/*      */     
/*  337 */     this.wasChanged = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void defrost() {
/*  342 */     checkPruned("defrost");
/*  343 */     this.wasFrozen = false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean subtypeOf(CtClass clazz) throws NotFoundException {
/*  349 */     String cname = clazz.getName();
/*  350 */     if (this == clazz || getName().equals(cname)) {
/*  351 */       return true;
/*      */     }
/*  353 */     ClassFile file = getClassFile2();
/*  354 */     String supername = file.getSuperclass();
/*  355 */     if (supername != null && supername.equals(cname)) {
/*  356 */       return true;
/*      */     }
/*  358 */     String[] ifs = file.getInterfaces();
/*  359 */     int num = ifs.length; int i;
/*  360 */     for (i = 0; i < num; i++) {
/*  361 */       if (ifs[i].equals(cname))
/*  362 */         return true; 
/*      */     } 
/*  364 */     if (supername != null && this.classPool.get(supername).subtypeOf(clazz)) {
/*  365 */       return true;
/*      */     }
/*  367 */     for (i = 0; i < num; i++) {
/*  368 */       if (this.classPool.get(ifs[i]).subtypeOf(clazz))
/*  369 */         return true; 
/*      */     } 
/*  371 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setName(String name) throws RuntimeException {
/*  376 */     String oldname = getName();
/*  377 */     if (name.equals(oldname)) {
/*      */       return;
/*      */     }
/*      */     
/*  381 */     this.classPool.checkNotFrozen(name);
/*  382 */     ClassFile cf = getClassFile2();
/*  383 */     super.setName(name);
/*  384 */     cf.setName(name);
/*  385 */     nameReplaced();
/*  386 */     this.classPool.classNameChanged(oldname, this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getGenericSignature() {
/*  392 */     SignatureAttribute sa = (SignatureAttribute)getClassFile2().getAttribute("Signature");
/*  393 */     return (sa == null) ? null : sa.getSignature();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setGenericSignature(String sig) {
/*  398 */     ClassFile cf = getClassFile();
/*  399 */     SignatureAttribute sa = new SignatureAttribute(cf.getConstPool(), sig);
/*  400 */     cf.addAttribute((AttributeInfo)sa);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void replaceClassName(ClassMap classnames) throws RuntimeException {
/*  407 */     String oldClassName = getName();
/*      */     
/*  409 */     String newClassName = classnames.get(Descriptor.toJvmName(oldClassName));
/*  410 */     if (newClassName != null) {
/*  411 */       newClassName = Descriptor.toJavaName(newClassName);
/*      */       
/*  413 */       this.classPool.checkNotFrozen(newClassName);
/*      */     } 
/*      */     
/*  416 */     super.replaceClassName(classnames);
/*  417 */     ClassFile cf = getClassFile2();
/*  418 */     cf.renameClass(classnames);
/*  419 */     nameReplaced();
/*      */     
/*  421 */     if (newClassName != null) {
/*  422 */       super.setName(newClassName);
/*  423 */       this.classPool.classNameChanged(oldClassName, this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void replaceClassName(String oldname, String newname) throws RuntimeException {
/*  431 */     String thisname = getName();
/*  432 */     if (thisname.equals(oldname)) {
/*  433 */       setName(newname);
/*      */     } else {
/*  435 */       super.replaceClassName(oldname, newname);
/*  436 */       getClassFile2().renameClass(oldname, newname);
/*  437 */       nameReplaced();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInterface() {
/*  443 */     return Modifier.isInterface(getModifiers());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAnnotation() {
/*  448 */     return Modifier.isAnnotation(getModifiers());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEnum() {
/*  453 */     return Modifier.isEnum(getModifiers());
/*      */   }
/*      */ 
/*      */   
/*      */   public int getModifiers() {
/*  458 */     ClassFile cf = getClassFile2();
/*  459 */     int acc = cf.getAccessFlags();
/*  460 */     acc = AccessFlag.clear(acc, 32);
/*  461 */     int inner = cf.getInnerAccessFlags();
/*  462 */     if (inner != -1) {
/*  463 */       if ((inner & 0x8) != 0)
/*  464 */         acc |= 0x8; 
/*  465 */       if ((inner & 0x1) != 0) {
/*  466 */         acc |= 0x1;
/*      */       } else {
/*  468 */         acc &= 0xFFFFFFFE;
/*  469 */         if ((inner & 0x4) != 0) {
/*  470 */           acc |= 0x4;
/*  471 */         } else if ((inner & 0x2) != 0) {
/*  472 */           acc |= 0x2;
/*      */         } 
/*      */       } 
/*  475 */     }  return AccessFlag.toModifier(acc);
/*      */   }
/*      */ 
/*      */   
/*      */   public CtClass[] getNestedClasses() throws NotFoundException {
/*  480 */     ClassFile cf = getClassFile2();
/*      */     
/*  482 */     InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
/*  483 */     if (ica == null) {
/*  484 */       return new CtClass[0];
/*      */     }
/*  486 */     String thisName = cf.getName() + "$";
/*  487 */     int n = ica.tableLength();
/*  488 */     List<CtClass> list = new ArrayList<>(n);
/*  489 */     for (int i = 0; i < n; i++) {
/*  490 */       String name = ica.innerClass(i);
/*  491 */       if (name != null && 
/*  492 */         name.startsWith(thisName))
/*      */       {
/*  494 */         if (name.lastIndexOf('$') < thisName.length()) {
/*  495 */           list.add(this.classPool.get(name));
/*      */         }
/*      */       }
/*      */     } 
/*  499 */     return list.<CtClass>toArray(new CtClass[list.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setModifiers(int mod) {
/*  504 */     checkModify();
/*  505 */     updateInnerEntry(mod, getName(), this, true);
/*  506 */     ClassFile cf = getClassFile2();
/*  507 */     cf.setAccessFlags(AccessFlag.of(mod & 0xFFFFFFF7));
/*      */   }
/*      */   
/*      */   private static void updateInnerEntry(int newMod, String name, CtClass clazz, boolean outer) {
/*  511 */     ClassFile cf = clazz.getClassFile2();
/*      */     
/*  513 */     InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
/*  514 */     if (ica != null) {
/*      */ 
/*      */ 
/*      */       
/*  518 */       int mod = newMod & 0xFFFFFFF7;
/*  519 */       int i = ica.find(name);
/*  520 */       if (i >= 0) {
/*  521 */         int isStatic = ica.accessFlags(i) & 0x8;
/*  522 */         if (isStatic != 0 || !Modifier.isStatic(newMod)) {
/*  523 */           clazz.checkModify();
/*  524 */           ica.setAccessFlags(i, AccessFlag.of(mod) | isStatic);
/*  525 */           String outName = ica.outerClass(i);
/*  526 */           if (outName != null && outer) {
/*      */             try {
/*  528 */               CtClass parent = clazz.getClassPool().get(outName);
/*  529 */               updateInnerEntry(mod, name, parent, false);
/*      */             }
/*  531 */             catch (NotFoundException e) {
/*  532 */               throw new RuntimeException("cannot find the declaring class: " + outName);
/*      */             } 
/*      */           }
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  541 */     if (Modifier.isStatic(newMod)) {
/*  542 */       throw new RuntimeException("cannot change " + Descriptor.toJavaName(name) + " into a static class");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasAnnotation(String annotationName) {
/*  548 */     ClassFile cf = getClassFile2();
/*      */     
/*  550 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  552 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
/*  553 */     return hasAnnotationType(annotationName, getClassPool(), ainfo, ainfo2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   static boolean hasAnnotationType(Class<?> clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) {
/*  564 */     return hasAnnotationType(clz.getName(), cp, a1, a2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean hasAnnotationType(String annotationTypeName, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) {
/*      */     Annotation[] anno1;
/*      */     Annotation[] anno2;
/*  573 */     if (a1 == null) {
/*  574 */       anno1 = null;
/*      */     } else {
/*  576 */       anno1 = a1.getAnnotations();
/*      */     } 
/*  578 */     if (a2 == null) {
/*  579 */       anno2 = null;
/*      */     } else {
/*  581 */       anno2 = a2.getAnnotations();
/*      */     } 
/*  583 */     if (anno1 != null)
/*  584 */       for (int i = 0; i < anno1.length; i++) {
/*  585 */         if (anno1[i].getTypeName().equals(annotationTypeName))
/*  586 */           return true; 
/*      */       }  
/*  588 */     if (anno2 != null)
/*  589 */       for (int i = 0; i < anno2.length; i++) {
/*  590 */         if (anno2[i].getTypeName().equals(annotationTypeName))
/*  591 */           return true; 
/*      */       }  
/*  593 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getAnnotation(Class<?> clz) throws ClassNotFoundException {
/*  598 */     ClassFile cf = getClassFile2();
/*      */     
/*  600 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  602 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
/*  603 */     return getAnnotationType(clz, getClassPool(), ainfo, ainfo2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object getAnnotationType(Class<?> clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
/*      */     Annotation[] anno1, anno2;
/*  612 */     if (a1 == null) {
/*  613 */       anno1 = null;
/*      */     } else {
/*  615 */       anno1 = a1.getAnnotations();
/*      */     } 
/*  617 */     if (a2 == null) {
/*  618 */       anno2 = null;
/*      */     } else {
/*  620 */       anno2 = a2.getAnnotations();
/*      */     } 
/*  622 */     String typeName = clz.getName();
/*  623 */     if (anno1 != null)
/*  624 */       for (int i = 0; i < anno1.length; i++) {
/*  625 */         if (anno1[i].getTypeName().equals(typeName))
/*  626 */           return toAnnoType(anno1[i], cp); 
/*      */       }  
/*  628 */     if (anno2 != null)
/*  629 */       for (int i = 0; i < anno2.length; i++) {
/*  630 */         if (anno2[i].getTypeName().equals(typeName))
/*  631 */           return toAnnoType(anno2[i], cp); 
/*      */       }  
/*  633 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object[] getAnnotations() throws ClassNotFoundException {
/*  638 */     return getAnnotations(false);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object[] getAvailableAnnotations() {
/*      */     try {
/*  644 */       return getAnnotations(true);
/*      */     }
/*  646 */     catch (ClassNotFoundException e) {
/*  647 */       throw new RuntimeException("Unexpected exception ", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
/*  654 */     ClassFile cf = getClassFile2();
/*      */     
/*  656 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  658 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
/*  659 */     return toAnnotationType(ignoreNotFound, getClassPool(), ainfo, ainfo2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object[] toAnnotationType(boolean ignoreNotFound, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
/*      */     Annotation[] anno1, anno2;
/*      */     int size1, size2;
/*  669 */     if (a1 == null) {
/*  670 */       anno1 = null;
/*  671 */       size1 = 0;
/*      */     } else {
/*      */       
/*  674 */       anno1 = a1.getAnnotations();
/*  675 */       size1 = anno1.length;
/*      */     } 
/*      */     
/*  678 */     if (a2 == null) {
/*  679 */       anno2 = null;
/*  680 */       size2 = 0;
/*      */     } else {
/*      */       
/*  683 */       anno2 = a2.getAnnotations();
/*  684 */       size2 = anno2.length;
/*      */     } 
/*      */     
/*  687 */     if (!ignoreNotFound) {
/*  688 */       Object[] result = new Object[size1 + size2];
/*  689 */       for (int m = 0; m < size1; m++) {
/*  690 */         result[m] = toAnnoType(anno1[m], cp);
/*      */       }
/*  692 */       for (int k = 0; k < size2; k++) {
/*  693 */         result[k + size1] = toAnnoType(anno2[k], cp);
/*      */       }
/*  695 */       return result;
/*      */     } 
/*  697 */     List<Object> annotations = new ArrayList();
/*  698 */     for (int i = 0; i < size1; i++) {
/*      */       try {
/*  700 */         annotations.add(toAnnoType(anno1[i], cp));
/*      */       }
/*  702 */       catch (ClassNotFoundException classNotFoundException) {}
/*  703 */     }  for (int j = 0; j < size2; j++) {
/*      */       try {
/*  705 */         annotations.add(toAnnoType(anno2[j], cp));
/*      */       }
/*  707 */       catch (ClassNotFoundException classNotFoundException) {}
/*      */     } 
/*  709 */     return annotations.toArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object[][] toAnnotationType(boolean ignoreNotFound, ClassPool cp, ParameterAnnotationsAttribute a1, ParameterAnnotationsAttribute a2, MethodInfo minfo) throws ClassNotFoundException {
/*  718 */     int numParameters = 0;
/*  719 */     if (a1 != null) {
/*  720 */       numParameters = a1.numParameters();
/*  721 */     } else if (a2 != null) {
/*  722 */       numParameters = a2.numParameters();
/*      */     } else {
/*  724 */       numParameters = Descriptor.numOfParameters(minfo.getDescriptor());
/*      */     } 
/*  726 */     Object[][] result = new Object[numParameters][];
/*  727 */     for (int i = 0; i < numParameters; i++) {
/*      */       Annotation[] anno1; Annotation[] anno2;
/*      */       int size1;
/*      */       int size2;
/*  731 */       if (a1 == null) {
/*  732 */         anno1 = null;
/*  733 */         size1 = 0;
/*      */       } else {
/*      */         
/*  736 */         anno1 = a1.getAnnotations()[i];
/*  737 */         size1 = anno1.length;
/*      */       } 
/*      */       
/*  740 */       if (a2 == null) {
/*  741 */         anno2 = null;
/*  742 */         size2 = 0;
/*      */       } else {
/*      */         
/*  745 */         anno2 = a2.getAnnotations()[i];
/*  746 */         size2 = anno2.length;
/*      */       } 
/*      */       
/*  749 */       if (!ignoreNotFound) {
/*  750 */         result[i] = new Object[size1 + size2]; int j;
/*  751 */         for (j = 0; j < size1; j++) {
/*  752 */           result[i][j] = toAnnoType(anno1[j], cp);
/*      */         }
/*  754 */         for (j = 0; j < size2; j++) {
/*  755 */           result[i][j + size1] = toAnnoType(anno2[j], cp);
/*      */         }
/*      */       } else {
/*  758 */         List<Object> annotations = new ArrayList(); int j;
/*  759 */         for (j = 0; j < size1; j++) {
/*      */           try {
/*  761 */             annotations.add(toAnnoType(anno1[j], cp));
/*      */           }
/*  763 */           catch (ClassNotFoundException classNotFoundException) {}
/*      */         } 
/*  765 */         for (j = 0; j < size2; j++) {
/*      */           try {
/*  767 */             annotations.add(toAnnoType(anno2[j], cp));
/*      */           }
/*  769 */           catch (ClassNotFoundException classNotFoundException) {}
/*      */         } 
/*      */         
/*  772 */         result[i] = annotations.toArray();
/*      */       } 
/*      */     } 
/*      */     
/*  776 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object toAnnoType(Annotation anno, ClassPool cp) throws ClassNotFoundException {
/*      */     try {
/*  783 */       ClassLoader cl = cp.getClassLoader();
/*  784 */       return anno.toAnnotationType(cl, cp);
/*      */     }
/*  786 */     catch (ClassNotFoundException e) {
/*  787 */       ClassLoader cl2 = cp.getClass().getClassLoader();
/*      */       try {
/*  789 */         return anno.toAnnotationType(cl2, cp);
/*      */       }
/*  791 */       catch (ClassNotFoundException e2) {
/*      */         try {
/*  793 */           Class<?> clazz = cp.get(anno.getTypeName()).toClass();
/*  794 */           return AnnotationImpl.make(clazz
/*  795 */               .getClassLoader(), clazz, cp, anno);
/*      */         
/*      */         }
/*  798 */         catch (Throwable e3) {
/*  799 */           throw new ClassNotFoundException(anno.getTypeName());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean subclassOf(CtClass superclass) {
/*  807 */     if (superclass == null) {
/*  808 */       return false;
/*      */     }
/*  810 */     String superName = superclass.getName();
/*  811 */     CtClass curr = this;
/*      */     try {
/*  813 */       while (curr != null) {
/*  814 */         if (curr.getName().equals(superName)) {
/*  815 */           return true;
/*      */         }
/*  817 */         curr = curr.getSuperclass();
/*      */       }
/*      */     
/*  820 */     } catch (Exception exception) {}
/*  821 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtClass getSuperclass() throws NotFoundException {
/*  826 */     String supername = getClassFile2().getSuperclass();
/*  827 */     if (supername == null)
/*  828 */       return null; 
/*  829 */     return this.classPool.get(supername);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSuperclass(CtClass clazz) throws CannotCompileException {
/*  834 */     checkModify();
/*  835 */     if (isInterface()) {
/*  836 */       addInterface(clazz);
/*      */     } else {
/*  838 */       getClassFile2().setSuperclass(clazz.getName());
/*      */     } 
/*      */   }
/*      */   
/*      */   public CtClass[] getInterfaces() throws NotFoundException {
/*  843 */     String[] ifs = getClassFile2().getInterfaces();
/*  844 */     int num = ifs.length;
/*  845 */     CtClass[] ifc = new CtClass[num];
/*  846 */     for (int i = 0; i < num; i++) {
/*  847 */       ifc[i] = this.classPool.get(ifs[i]);
/*      */     }
/*  849 */     return ifc;
/*      */   }
/*      */   
/*      */   public void setInterfaces(CtClass[] list) {
/*      */     String[] ifs;
/*  854 */     checkModify();
/*      */     
/*  856 */     if (list == null) {
/*  857 */       ifs = new String[0];
/*      */     } else {
/*  859 */       int num = list.length;
/*  860 */       ifs = new String[num];
/*  861 */       for (int i = 0; i < num; i++) {
/*  862 */         ifs[i] = list[i].getName();
/*      */       }
/*      */     } 
/*  865 */     getClassFile2().setInterfaces(ifs);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addInterface(CtClass anInterface) {
/*  870 */     checkModify();
/*  871 */     if (anInterface != null) {
/*  872 */       getClassFile2().addInterface(anInterface.getName());
/*      */     }
/*      */   }
/*      */   
/*      */   public CtClass getDeclaringClass() throws NotFoundException {
/*  877 */     ClassFile cf = getClassFile2();
/*  878 */     InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
/*      */     
/*  880 */     if (ica == null) {
/*  881 */       return null;
/*      */     }
/*  883 */     String name = getName();
/*  884 */     int n = ica.tableLength();
/*  885 */     for (int i = 0; i < n; i++) {
/*  886 */       if (name.equals(ica.innerClass(i))) {
/*  887 */         String outName = ica.outerClass(i);
/*  888 */         if (outName != null) {
/*  889 */           return this.classPool.get(outName);
/*      */         }
/*      */ 
/*      */         
/*  893 */         EnclosingMethodAttribute ema = (EnclosingMethodAttribute)cf.getAttribute("EnclosingMethod");
/*      */         
/*  895 */         if (ema != null) {
/*  896 */           return this.classPool.get(ema.className());
/*      */         }
/*      */       } 
/*      */     } 
/*  900 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CtBehavior getEnclosingBehavior() throws NotFoundException {
/*  906 */     ClassFile cf = getClassFile2();
/*      */     
/*  908 */     EnclosingMethodAttribute ema = (EnclosingMethodAttribute)cf.getAttribute("EnclosingMethod");
/*      */     
/*  910 */     if (ema == null)
/*  911 */       return null; 
/*  912 */     CtClass enc = this.classPool.get(ema.className());
/*  913 */     String name = ema.methodName();
/*  914 */     if ("<init>".equals(name))
/*  915 */       return enc.getConstructor(ema.methodDescriptor()); 
/*  916 */     if ("<clinit>".equals(name)) {
/*  917 */       return enc.getClassInitializer();
/*      */     }
/*  919 */     return enc.getMethod(name, ema.methodDescriptor());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeNestedClass(String name, boolean isStatic) {
/*  925 */     if (!isStatic) {
/*  926 */       throw new RuntimeException("sorry, only nested static class is supported");
/*      */     }
/*      */     
/*  929 */     checkModify();
/*  930 */     CtClass c = this.classPool.makeNestedClass(getName() + "$" + name);
/*  931 */     ClassFile cf = getClassFile2();
/*  932 */     ClassFile cf2 = c.getClassFile2();
/*  933 */     InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
/*      */     
/*  935 */     if (ica == null) {
/*  936 */       ica = new InnerClassesAttribute(cf.getConstPool());
/*  937 */       cf.addAttribute((AttributeInfo)ica);
/*      */     } 
/*      */     
/*  940 */     ica.append(c.getName(), getName(), name, cf2
/*  941 */         .getAccessFlags() & 0xFFFFFFDF | 0x8);
/*  942 */     cf2.addAttribute(ica.copy(cf2.getConstPool(), null));
/*  943 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void nameReplaced() {
/*  949 */     CtMember.Cache cache = hasMemberCache();
/*  950 */     if (cache != null) {
/*  951 */       CtMember mth = cache.methodHead();
/*  952 */       CtMember tail = cache.lastMethod();
/*  953 */       while (mth != tail) {
/*  954 */         mth = mth.next();
/*  955 */         mth.nameReplaced();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtMember.Cache hasMemberCache() {
/*  964 */     if (this.memberCache != null)
/*  965 */       return this.memberCache.get(); 
/*  966 */     return null;
/*      */   }
/*      */   
/*      */   protected synchronized CtMember.Cache getMembers() {
/*  970 */     CtMember.Cache cache = null;
/*  971 */     if (this.memberCache == null || (
/*  972 */       cache = this.memberCache.get()) == null) {
/*  973 */       cache = new CtMember.Cache(this);
/*  974 */       makeFieldCache(cache);
/*  975 */       makeBehaviorCache(cache);
/*  976 */       this.memberCache = new WeakReference<>(cache);
/*      */     } 
/*      */     
/*  979 */     return cache;
/*      */   }
/*      */   
/*      */   private void makeFieldCache(CtMember.Cache cache) {
/*  983 */     List<FieldInfo> fields = getClassFile3(false).getFields();
/*  984 */     for (FieldInfo finfo : fields)
/*  985 */       cache.addField(new CtField(finfo, this)); 
/*      */   }
/*      */   
/*      */   private void makeBehaviorCache(CtMember.Cache cache) {
/*  989 */     List<MethodInfo> methods = getClassFile3(false).getMethods();
/*  990 */     for (MethodInfo minfo : methods) {
/*  991 */       if (minfo.isMethod()) {
/*  992 */         cache.addMethod(new CtMethod(minfo, this)); continue;
/*      */       } 
/*  994 */       cache.addConstructor(new CtConstructor(minfo, this));
/*      */     } 
/*      */   }
/*      */   
/*      */   public CtField[] getFields() {
/*  999 */     List<CtMember> alist = new ArrayList<>();
/* 1000 */     getFields(alist, this);
/* 1001 */     return alist.<CtField>toArray(new CtField[alist.size()]);
/*      */   }
/*      */   
/*      */   private static void getFields(List<CtMember> alist, CtClass cc) {
/* 1005 */     if (cc == null) {
/*      */       return;
/*      */     }
/*      */     try {
/* 1009 */       getFields(alist, cc.getSuperclass());
/*      */     }
/* 1011 */     catch (NotFoundException notFoundException) {}
/*      */     
/*      */     try {
/* 1014 */       CtClass[] ifs = cc.getInterfaces();
/* 1015 */       for (CtClass ctc : ifs) {
/* 1016 */         getFields(alist, ctc);
/*      */       }
/* 1018 */     } catch (NotFoundException notFoundException) {}
/*      */     
/* 1020 */     CtMember.Cache memCache = ((CtClassType)cc).getMembers();
/* 1021 */     CtMember field = memCache.fieldHead();
/* 1022 */     CtMember tail = memCache.lastField();
/* 1023 */     while (field != tail) {
/* 1024 */       field = field.next();
/* 1025 */       if (!Modifier.isPrivate(field.getModifiers())) {
/* 1026 */         alist.add(field);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public CtField getField(String name, String desc) throws NotFoundException {
/* 1032 */     CtField f = getField2(name, desc);
/* 1033 */     return checkGetField(f, name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private CtField checkGetField(CtField f, String name, String desc) throws NotFoundException {
/* 1039 */     if (f == null) {
/* 1040 */       String msg = "field: " + name;
/* 1041 */       if (desc != null) {
/* 1042 */         msg = msg + " type " + desc;
/*      */       }
/* 1044 */       throw new NotFoundException(msg + " in " + getName());
/*      */     } 
/* 1046 */     return f;
/*      */   }
/*      */ 
/*      */   
/*      */   CtField getField2(String name, String desc) {
/* 1051 */     CtField df = getDeclaredField2(name, desc);
/* 1052 */     if (df != null) {
/* 1053 */       return df;
/*      */     }
/*      */     try {
/* 1056 */       CtClass[] ifs = getInterfaces();
/* 1057 */       for (CtClass ctc : ifs) {
/* 1058 */         CtField f = ctc.getField2(name, desc);
/* 1059 */         if (f != null) {
/* 1060 */           return f;
/*      */         }
/*      */       } 
/* 1063 */       CtClass s = getSuperclass();
/* 1064 */       if (s != null) {
/* 1065 */         return s.getField2(name, desc);
/*      */       }
/* 1067 */     } catch (NotFoundException notFoundException) {}
/* 1068 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtField[] getDeclaredFields() {
/* 1073 */     CtMember.Cache memCache = getMembers();
/* 1074 */     CtMember field = memCache.fieldHead();
/* 1075 */     CtMember tail = memCache.lastField();
/* 1076 */     int num = CtMember.Cache.count(field, tail);
/* 1077 */     CtField[] cfs = new CtField[num];
/* 1078 */     int i = 0;
/* 1079 */     while (field != tail) {
/* 1080 */       field = field.next();
/* 1081 */       cfs[i++] = (CtField)field;
/*      */     } 
/*      */     
/* 1084 */     return cfs;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtField getDeclaredField(String name) throws NotFoundException {
/* 1089 */     return getDeclaredField(name, (String)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public CtField getDeclaredField(String name, String desc) throws NotFoundException {
/* 1094 */     CtField f = getDeclaredField2(name, desc);
/* 1095 */     return checkGetField(f, name, desc);
/*      */   }
/*      */   
/*      */   private CtField getDeclaredField2(String name, String desc) {
/* 1099 */     CtMember.Cache memCache = getMembers();
/* 1100 */     CtMember field = memCache.fieldHead();
/* 1101 */     CtMember tail = memCache.lastField();
/* 1102 */     while (field != tail) {
/* 1103 */       field = field.next();
/* 1104 */       if (field.getName().equals(name) && (desc == null || desc
/* 1105 */         .equals(field.getSignature()))) {
/* 1106 */         return (CtField)field;
/*      */       }
/*      */     } 
/* 1109 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtBehavior[] getDeclaredBehaviors() {
/* 1114 */     CtMember.Cache memCache = getMembers();
/* 1115 */     CtMember cons = memCache.consHead();
/* 1116 */     CtMember consTail = memCache.lastCons();
/* 1117 */     int cnum = CtMember.Cache.count(cons, consTail);
/* 1118 */     CtMember mth = memCache.methodHead();
/* 1119 */     CtMember mthTail = memCache.lastMethod();
/* 1120 */     int mnum = CtMember.Cache.count(mth, mthTail);
/*      */     
/* 1122 */     CtBehavior[] cb = new CtBehavior[cnum + mnum];
/* 1123 */     int i = 0;
/* 1124 */     while (cons != consTail) {
/* 1125 */       cons = cons.next();
/* 1126 */       cb[i++] = (CtBehavior)cons;
/*      */     } 
/*      */     
/* 1129 */     while (mth != mthTail) {
/* 1130 */       mth = mth.next();
/* 1131 */       cb[i++] = (CtBehavior)mth;
/*      */     } 
/*      */     
/* 1134 */     return cb;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtConstructor[] getConstructors() {
/* 1139 */     CtMember.Cache memCache = getMembers();
/* 1140 */     CtMember cons = memCache.consHead();
/* 1141 */     CtMember consTail = memCache.lastCons();
/*      */     
/* 1143 */     int n = 0;
/* 1144 */     CtMember mem = cons;
/* 1145 */     while (mem != consTail) {
/* 1146 */       mem = mem.next();
/* 1147 */       if (isPubCons((CtConstructor)mem)) {
/* 1148 */         n++;
/*      */       }
/*      */     } 
/* 1151 */     CtConstructor[] result = new CtConstructor[n];
/* 1152 */     int i = 0;
/* 1153 */     mem = cons;
/* 1154 */     while (mem != consTail) {
/* 1155 */       mem = mem.next();
/* 1156 */       CtConstructor cc = (CtConstructor)mem;
/* 1157 */       if (isPubCons(cc)) {
/* 1158 */         result[i++] = cc;
/*      */       }
/*      */     } 
/* 1161 */     return result;
/*      */   }
/*      */   
/*      */   private static boolean isPubCons(CtConstructor cons) {
/* 1165 */     return (!Modifier.isPrivate(cons.getModifiers()) && cons
/* 1166 */       .isConstructor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtConstructor getConstructor(String desc) throws NotFoundException {
/* 1173 */     CtMember.Cache memCache = getMembers();
/* 1174 */     CtMember cons = memCache.consHead();
/* 1175 */     CtMember consTail = memCache.lastCons();
/*      */     
/* 1177 */     while (cons != consTail) {
/* 1178 */       cons = cons.next();
/* 1179 */       CtConstructor cc = (CtConstructor)cons;
/* 1180 */       if (cc.getMethodInfo2().getDescriptor().equals(desc) && cc
/* 1181 */         .isConstructor()) {
/* 1182 */         return cc;
/*      */       }
/*      */     } 
/* 1185 */     return super.getConstructor(desc);
/*      */   }
/*      */ 
/*      */   
/*      */   public CtConstructor[] getDeclaredConstructors() {
/* 1190 */     CtMember.Cache memCache = getMembers();
/* 1191 */     CtMember cons = memCache.consHead();
/* 1192 */     CtMember consTail = memCache.lastCons();
/*      */     
/* 1194 */     int n = 0;
/* 1195 */     CtMember mem = cons;
/* 1196 */     while (mem != consTail) {
/* 1197 */       mem = mem.next();
/* 1198 */       CtConstructor cc = (CtConstructor)mem;
/* 1199 */       if (cc.isConstructor()) {
/* 1200 */         n++;
/*      */       }
/*      */     } 
/* 1203 */     CtConstructor[] result = new CtConstructor[n];
/* 1204 */     int i = 0;
/* 1205 */     mem = cons;
/* 1206 */     while (mem != consTail) {
/* 1207 */       mem = mem.next();
/* 1208 */       CtConstructor cc = (CtConstructor)mem;
/* 1209 */       if (cc.isConstructor()) {
/* 1210 */         result[i++] = cc;
/*      */       }
/*      */     } 
/* 1213 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtConstructor getClassInitializer() {
/* 1218 */     CtMember.Cache memCache = getMembers();
/* 1219 */     CtMember cons = memCache.consHead();
/* 1220 */     CtMember consTail = memCache.lastCons();
/*      */     
/* 1222 */     while (cons != consTail) {
/* 1223 */       cons = cons.next();
/* 1224 */       CtConstructor cc = (CtConstructor)cons;
/* 1225 */       if (cc.isClassInitializer()) {
/* 1226 */         return cc;
/*      */       }
/*      */     } 
/* 1229 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtMethod[] getMethods() {
/* 1234 */     Map<String, CtMember> h = new HashMap<>();
/* 1235 */     getMethods0(h, this);
/* 1236 */     return (CtMethod[])h.values().toArray((Object[])new CtMethod[h.size()]);
/*      */   }
/*      */   
/*      */   private static void getMethods0(Map<String, CtMember> h, CtClass cc) {
/*      */     try {
/* 1241 */       CtClass[] ifs = cc.getInterfaces();
/* 1242 */       for (CtClass ctc : ifs) {
/* 1243 */         getMethods0(h, ctc);
/*      */       }
/* 1245 */     } catch (NotFoundException notFoundException) {}
/*      */     
/*      */     try {
/* 1248 */       CtClass s = cc.getSuperclass();
/* 1249 */       if (s != null) {
/* 1250 */         getMethods0(h, s);
/*      */       }
/* 1252 */     } catch (NotFoundException notFoundException) {}
/*      */     
/* 1254 */     if (cc instanceof CtClassType) {
/* 1255 */       CtMember.Cache memCache = ((CtClassType)cc).getMembers();
/* 1256 */       CtMember mth = memCache.methodHead();
/* 1257 */       CtMember mthTail = memCache.lastMethod();
/*      */       
/* 1259 */       while (mth != mthTail) {
/* 1260 */         mth = mth.next();
/* 1261 */         if (!Modifier.isPrivate(mth.getModifiers())) {
/* 1262 */           h.put(((CtMethod)mth).getStringRep(), mth);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CtMethod getMethod(String name, String desc) throws NotFoundException {
/* 1271 */     CtMethod m = getMethod0(this, name, desc);
/* 1272 */     if (m != null)
/* 1273 */       return m; 
/* 1274 */     throw new NotFoundException(name + "(..) is not found in " + 
/* 1275 */         getName());
/*      */   }
/*      */ 
/*      */   
/*      */   private static CtMethod getMethod0(CtClass cc, String name, String desc) {
/* 1280 */     if (cc instanceof CtClassType) {
/* 1281 */       CtMember.Cache memCache = ((CtClassType)cc).getMembers();
/* 1282 */       CtMember mth = memCache.methodHead();
/* 1283 */       CtMember mthTail = memCache.lastMethod();
/*      */       
/* 1285 */       while (mth != mthTail) {
/* 1286 */         mth = mth.next();
/* 1287 */         if (mth.getName().equals(name) && ((CtMethod)mth)
/* 1288 */           .getMethodInfo2().getDescriptor().equals(desc)) {
/* 1289 */           return (CtMethod)mth;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     try {
/* 1294 */       CtClass s = cc.getSuperclass();
/* 1295 */       if (s != null) {
/* 1296 */         CtMethod m = getMethod0(s, name, desc);
/* 1297 */         if (m != null) {
/* 1298 */           return m;
/*      */         }
/*      */       } 
/* 1301 */     } catch (NotFoundException notFoundException) {}
/*      */     
/*      */     try {
/* 1304 */       CtClass[] ifs = cc.getInterfaces();
/* 1305 */       for (CtClass ctc : ifs) {
/* 1306 */         CtMethod m = getMethod0(ctc, name, desc);
/* 1307 */         if (m != null) {
/* 1308 */           return m;
/*      */         }
/*      */       } 
/* 1311 */     } catch (NotFoundException notFoundException) {}
/* 1312 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtMethod[] getDeclaredMethods() {
/* 1317 */     CtMember.Cache memCache = getMembers();
/* 1318 */     CtMember mth = memCache.methodHead();
/* 1319 */     CtMember mthTail = memCache.lastMethod();
/* 1320 */     List<CtMember> methods = new ArrayList<>();
/* 1321 */     while (mth != mthTail) {
/* 1322 */       mth = mth.next();
/* 1323 */       methods.add(mth);
/*      */     } 
/*      */     
/* 1326 */     return methods.<CtMethod>toArray(new CtMethod[methods.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public CtMethod[] getDeclaredMethods(String name) throws NotFoundException {
/* 1331 */     CtMember.Cache memCache = getMembers();
/* 1332 */     CtMember mth = memCache.methodHead();
/* 1333 */     CtMember mthTail = memCache.lastMethod();
/* 1334 */     List<CtMember> methods = new ArrayList<>();
/* 1335 */     while (mth != mthTail) {
/* 1336 */       mth = mth.next();
/* 1337 */       if (mth.getName().equals(name)) {
/* 1338 */         methods.add(mth);
/*      */       }
/*      */     } 
/* 1341 */     return methods.<CtMethod>toArray(new CtMethod[methods.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   public CtMethod getDeclaredMethod(String name) throws NotFoundException {
/* 1346 */     CtMember.Cache memCache = getMembers();
/* 1347 */     CtMember mth = memCache.methodHead();
/* 1348 */     CtMember mthTail = memCache.lastMethod();
/* 1349 */     while (mth != mthTail) {
/* 1350 */       mth = mth.next();
/* 1351 */       if (mth.getName().equals(name)) {
/* 1352 */         return (CtMethod)mth;
/*      */       }
/*      */     } 
/* 1355 */     throw new NotFoundException(name + "(..) is not found in " + 
/* 1356 */         getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtMethod getDeclaredMethod(String name, CtClass[] params) throws NotFoundException {
/* 1363 */     String desc = Descriptor.ofParameters(params);
/* 1364 */     CtMember.Cache memCache = getMembers();
/* 1365 */     CtMember mth = memCache.methodHead();
/* 1366 */     CtMember mthTail = memCache.lastMethod();
/*      */     
/* 1368 */     while (mth != mthTail) {
/* 1369 */       mth = mth.next();
/* 1370 */       if (mth.getName().equals(name) && ((CtMethod)mth)
/* 1371 */         .getMethodInfo2().getDescriptor().startsWith(desc)) {
/* 1372 */         return (CtMethod)mth;
/*      */       }
/*      */     } 
/* 1375 */     throw new NotFoundException(name + "(..) is not found in " + 
/* 1376 */         getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addField(CtField f, String init) throws CannotCompileException {
/* 1383 */     addField(f, CtField.Initializer.byExpr(init));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addField(CtField f, CtField.Initializer init) throws CannotCompileException {
/* 1390 */     checkModify();
/* 1391 */     if (f.getDeclaringClass() != this) {
/* 1392 */       throw new CannotCompileException("cannot add");
/*      */     }
/* 1394 */     if (init == null) {
/* 1395 */       init = f.getInit();
/*      */     }
/* 1397 */     if (init != null) {
/* 1398 */       init.check(f.getSignature());
/* 1399 */       int mod = f.getModifiers();
/* 1400 */       if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
/*      */         try {
/* 1402 */           ConstPool cp = getClassFile2().getConstPool();
/* 1403 */           int index = init.getConstantValue(cp, f.getType());
/* 1404 */           if (index != 0) {
/* 1405 */             f.getFieldInfo2().addAttribute((AttributeInfo)new ConstantAttribute(cp, index));
/* 1406 */             init = null;
/*      */           }
/*      */         
/* 1409 */         } catch (NotFoundException notFoundException) {}
/*      */       }
/*      */     } 
/* 1412 */     getMembers().addField(f);
/* 1413 */     getClassFile2().addField(f.getFieldInfo2());
/*      */     
/* 1415 */     if (init != null) {
/* 1416 */       FieldInitLink fil = new FieldInitLink(f, init);
/* 1417 */       FieldInitLink link = this.fieldInitializers;
/* 1418 */       if (link == null) {
/* 1419 */         this.fieldInitializers = fil;
/*      */       } else {
/* 1421 */         while (link.next != null) {
/* 1422 */           link = link.next;
/*      */         }
/* 1424 */         link.next = fil;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeField(CtField f) throws NotFoundException {
/* 1431 */     checkModify();
/* 1432 */     FieldInfo fi = f.getFieldInfo2();
/* 1433 */     ClassFile cf = getClassFile2();
/* 1434 */     if (cf.getFields().remove(fi)) {
/* 1435 */       getMembers().remove(f);
/* 1436 */       this.gcConstPool = true;
/*      */     } else {
/*      */       
/* 1439 */       throw new NotFoundException(f.toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CtConstructor makeClassInitializer() throws CannotCompileException {
/* 1446 */     CtConstructor clinit = getClassInitializer();
/* 1447 */     if (clinit != null) {
/* 1448 */       return clinit;
/*      */     }
/* 1450 */     checkModify();
/* 1451 */     ClassFile cf = getClassFile2();
/* 1452 */     Bytecode code = new Bytecode(cf.getConstPool(), 0, 0);
/* 1453 */     modifyClassConstructor(cf, code, 0, 0);
/* 1454 */     return getClassInitializer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConstructor(CtConstructor c) throws CannotCompileException {
/* 1461 */     checkModify();
/* 1462 */     if (c.getDeclaringClass() != this) {
/* 1463 */       throw new CannotCompileException("cannot add");
/*      */     }
/* 1465 */     getMembers().addConstructor(c);
/* 1466 */     getClassFile2().addMethod(c.getMethodInfo2());
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeConstructor(CtConstructor m) throws NotFoundException {
/* 1471 */     checkModify();
/* 1472 */     MethodInfo mi = m.getMethodInfo2();
/* 1473 */     ClassFile cf = getClassFile2();
/* 1474 */     if (cf.getMethods().remove(mi)) {
/* 1475 */       getMembers().remove(m);
/* 1476 */       this.gcConstPool = true;
/*      */     } else {
/*      */       
/* 1479 */       throw new NotFoundException(m.toString());
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addMethod(CtMethod m) throws CannotCompileException {
/* 1484 */     checkModify();
/* 1485 */     if (m.getDeclaringClass() != this) {
/* 1486 */       throw new CannotCompileException("bad declaring class");
/*      */     }
/* 1488 */     int mod = m.getModifiers();
/* 1489 */     if ((getModifiers() & 0x200) != 0) {
/* 1490 */       if (Modifier.isProtected(mod) || Modifier.isPrivate(mod)) {
/* 1491 */         throw new CannotCompileException("an interface method must be public: " + m
/* 1492 */             .toString());
/*      */       }
/* 1494 */       m.setModifiers(mod | 0x1);
/*      */     } 
/*      */     
/* 1497 */     getMembers().addMethod(m);
/* 1498 */     getClassFile2().addMethod(m.getMethodInfo2());
/* 1499 */     if ((mod & 0x400) != 0) {
/* 1500 */       setModifiers(getModifiers() | 0x400);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeMethod(CtMethod m) throws NotFoundException {
/* 1506 */     checkModify();
/* 1507 */     MethodInfo mi = m.getMethodInfo2();
/* 1508 */     ClassFile cf = getClassFile2();
/* 1509 */     if (cf.getMethods().remove(mi)) {
/* 1510 */       getMembers().remove(m);
/* 1511 */       this.gcConstPool = true;
/*      */     } else {
/*      */       
/* 1514 */       throw new NotFoundException(m.toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getAttribute(String name) {
/* 1520 */     AttributeInfo ai = getClassFile2().getAttribute(name);
/* 1521 */     if (ai == null)
/* 1522 */       return null; 
/* 1523 */     return ai.get();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAttribute(String name, byte[] data) {
/* 1529 */     checkModify();
/* 1530 */     ClassFile cf = getClassFile2();
/* 1531 */     cf.addAttribute(new AttributeInfo(cf.getConstPool(), name, data));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void instrument(CodeConverter converter) throws CannotCompileException {
/* 1538 */     checkModify();
/* 1539 */     ClassFile cf = getClassFile2();
/* 1540 */     ConstPool cp = cf.getConstPool();
/* 1541 */     List<MethodInfo> methods = cf.getMethods();
/* 1542 */     for (MethodInfo minfo : (MethodInfo[])methods.<MethodInfo>toArray(new MethodInfo[methods.size()])) {
/* 1543 */       converter.doit(this, minfo, cp);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void instrument(ExprEditor editor) throws CannotCompileException {
/* 1550 */     checkModify();
/* 1551 */     ClassFile cf = getClassFile2();
/* 1552 */     List<MethodInfo> methods = cf.getMethods();
/* 1553 */     for (MethodInfo minfo : (MethodInfo[])methods.<MethodInfo>toArray(new MethodInfo[methods.size()])) {
/* 1554 */       editor.doit(this, minfo);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void prune() {
/* 1564 */     if (this.wasPruned) {
/*      */       return;
/*      */     }
/* 1567 */     this.wasPruned = this.wasFrozen = true;
/* 1568 */     getClassFile2().prune();
/*      */   }
/*      */   
/*      */   public void rebuildClassFile() {
/* 1572 */     this.gcConstPool = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void toBytecode(DataOutputStream out) throws CannotCompileException, IOException {
/*      */     try {
/* 1579 */       if (isModified()) {
/* 1580 */         checkPruned("toBytecode");
/* 1581 */         ClassFile cf = getClassFile2();
/* 1582 */         if (this.gcConstPool) {
/* 1583 */           cf.compact();
/* 1584 */           this.gcConstPool = false;
/*      */         } 
/*      */         
/* 1587 */         modifyClassConstructor(cf);
/* 1588 */         modifyConstructors(cf);
/* 1589 */         if (debugDump != null) {
/* 1590 */           dumpClassFile(cf);
/*      */         }
/* 1592 */         cf.write(out);
/* 1593 */         out.flush();
/* 1594 */         this.fieldInitializers = null;
/* 1595 */         if (this.doPruning) {
/*      */           
/* 1597 */           cf.prune();
/* 1598 */           this.wasPruned = true;
/*      */         } 
/*      */       } else {
/*      */         
/* 1602 */         this.classPool.writeClassfile(getName(), out);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1607 */       this.getCount = 0;
/* 1608 */       this.wasFrozen = true;
/*      */     }
/* 1610 */     catch (NotFoundException e) {
/* 1611 */       throw new CannotCompileException(e);
/*      */     }
/* 1613 */     catch (IOException e) {
/* 1614 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void dumpClassFile(ClassFile cf) throws IOException {
/* 1620 */     DataOutputStream dump = makeFileOutput(debugDump);
/*      */     try {
/* 1622 */       cf.write(dump);
/*      */     } finally {
/*      */       
/* 1625 */       dump.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkPruned(String method) {
/* 1633 */     if (this.wasPruned) {
/* 1634 */       throw new RuntimeException(method + "(): " + getName() + " was pruned.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean stopPruning(boolean stop) {
/* 1641 */     boolean prev = !this.doPruning;
/* 1642 */     this.doPruning = !stop;
/* 1643 */     return prev;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void modifyClassConstructor(ClassFile cf) throws CannotCompileException, NotFoundException {
/* 1649 */     if (this.fieldInitializers == null) {
/*      */       return;
/*      */     }
/* 1652 */     Bytecode code = new Bytecode(cf.getConstPool(), 0, 0);
/* 1653 */     Javac jv = new Javac(code, this);
/* 1654 */     int stacksize = 0;
/* 1655 */     boolean doInit = false;
/* 1656 */     for (FieldInitLink fi = this.fieldInitializers; fi != null; fi = fi.next) {
/* 1657 */       CtField f = fi.field;
/* 1658 */       if (Modifier.isStatic(f.getModifiers())) {
/* 1659 */         doInit = true;
/* 1660 */         int s = fi.init.compileIfStatic(f.getType(), f.getName(), code, jv);
/*      */         
/* 1662 */         if (stacksize < s) {
/* 1663 */           stacksize = s;
/*      */         }
/*      */       } 
/*      */     } 
/* 1667 */     if (doInit) {
/* 1668 */       modifyClassConstructor(cf, code, stacksize, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void modifyClassConstructor(ClassFile cf, Bytecode code, int stacksize, int localsize) throws CannotCompileException {
/* 1675 */     MethodInfo m = cf.getStaticInitializer();
/* 1676 */     if (m == null) {
/* 1677 */       code.add(177);
/* 1678 */       code.setMaxStack(stacksize);
/* 1679 */       code.setMaxLocals(localsize);
/* 1680 */       m = new MethodInfo(cf.getConstPool(), "<clinit>", "()V");
/* 1681 */       m.setAccessFlags(8);
/* 1682 */       m.setCodeAttribute(code.toCodeAttribute());
/* 1683 */       cf.addMethod(m);
/* 1684 */       CtMember.Cache cache = hasMemberCache();
/* 1685 */       if (cache != null) {
/* 1686 */         cache.addConstructor(new CtConstructor(m, this));
/*      */       }
/*      */     } else {
/* 1689 */       CodeAttribute codeAttr = m.getCodeAttribute();
/* 1690 */       if (codeAttr == null) {
/* 1691 */         throw new CannotCompileException("empty <clinit>");
/*      */       }
/*      */       try {
/* 1694 */         CodeIterator it = codeAttr.iterator();
/* 1695 */         int pos = it.insertEx(code.get());
/* 1696 */         it.insert(code.getExceptionTable(), pos);
/* 1697 */         int maxstack = codeAttr.getMaxStack();
/* 1698 */         if (maxstack < stacksize) {
/* 1699 */           codeAttr.setMaxStack(stacksize);
/*      */         }
/* 1701 */         int maxlocals = codeAttr.getMaxLocals();
/* 1702 */         if (maxlocals < localsize) {
/* 1703 */           codeAttr.setMaxLocals(localsize);
/*      */         }
/* 1705 */       } catch (BadBytecode e) {
/* 1706 */         throw new CannotCompileException(e);
/*      */       } 
/*      */     } 
/*      */     
/*      */     try {
/* 1711 */       m.rebuildStackMapIf6(this.classPool, cf);
/*      */     }
/* 1713 */     catch (BadBytecode e) {
/* 1714 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void modifyConstructors(ClassFile cf) throws CannotCompileException, NotFoundException {
/* 1721 */     if (this.fieldInitializers == null) {
/*      */       return;
/*      */     }
/* 1724 */     ConstPool cp = cf.getConstPool();
/* 1725 */     List<MethodInfo> methods = cf.getMethods();
/* 1726 */     for (MethodInfo minfo : methods) {
/* 1727 */       if (minfo.isConstructor()) {
/* 1728 */         CodeAttribute codeAttr = minfo.getCodeAttribute();
/* 1729 */         if (codeAttr != null) {
/*      */           
/*      */           try {
/* 1732 */             Bytecode init = new Bytecode(cp, 0, codeAttr.getMaxLocals());
/*      */             
/* 1734 */             CtClass[] params = Descriptor.getParameterTypes(minfo
/* 1735 */                 .getDescriptor(), this.classPool);
/*      */             
/* 1737 */             int stacksize = makeFieldInitializer(init, params);
/* 1738 */             insertAuxInitializer(codeAttr, init, stacksize);
/* 1739 */             minfo.rebuildStackMapIf6(this.classPool, cf);
/*      */           }
/* 1741 */           catch (BadBytecode e) {
/* 1742 */             throw new CannotCompileException(e);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void insertAuxInitializer(CodeAttribute codeAttr, Bytecode initializer, int stacksize) throws BadBytecode {
/* 1753 */     CodeIterator it = codeAttr.iterator();
/* 1754 */     int index = it.skipSuperConstructor();
/* 1755 */     if (index < 0) {
/* 1756 */       index = it.skipThisConstructor();
/* 1757 */       if (index >= 0) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1763 */     int pos = it.insertEx(initializer.get());
/* 1764 */     it.insert(initializer.getExceptionTable(), pos);
/* 1765 */     int maxstack = codeAttr.getMaxStack();
/* 1766 */     if (maxstack < stacksize) {
/* 1767 */       codeAttr.setMaxStack(stacksize);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private int makeFieldInitializer(Bytecode code, CtClass[] parameters) throws CannotCompileException, NotFoundException {
/* 1773 */     int stacksize = 0;
/* 1774 */     Javac jv = new Javac(code, this);
/*      */     try {
/* 1776 */       jv.recordParams(parameters, false);
/*      */     }
/* 1778 */     catch (CompileError e) {
/* 1779 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/* 1782 */     for (FieldInitLink fi = this.fieldInitializers; fi != null; fi = fi.next) {
/* 1783 */       CtField f = fi.field;
/* 1784 */       if (!Modifier.isStatic(f.getModifiers())) {
/* 1785 */         int s = fi.init.compile(f.getType(), f.getName(), code, parameters, jv);
/*      */         
/* 1787 */         if (stacksize < s) {
/* 1788 */           stacksize = s;
/*      */         }
/*      */       } 
/*      */     } 
/* 1792 */     return stacksize;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Map<CtMethod, String> getHiddenMethods() {
/* 1798 */     if (this.hiddenMethods == null) {
/* 1799 */       this.hiddenMethods = new Hashtable<>();
/*      */     }
/* 1801 */     return this.hiddenMethods;
/*      */   }
/*      */   int getUniqueNumber() {
/* 1804 */     return this.uniqueNumberSeed++;
/*      */   }
/*      */   
/*      */   public String makeUniqueName(String prefix) {
/* 1808 */     Map<Object, CtClassType> table = new HashMap<>();
/* 1809 */     makeMemberList(table);
/* 1810 */     Set<Object> keys = table.keySet();
/* 1811 */     String[] methods = new String[keys.size()];
/* 1812 */     keys.toArray(methods);
/*      */     
/* 1814 */     if (notFindInArray(prefix, methods)) {
/* 1815 */       return prefix;
/*      */     }
/* 1817 */     int i = 100;
/*      */     
/*      */     while (true) {
/* 1820 */       if (i > 999) {
/* 1821 */         throw new RuntimeException("too many unique name");
/*      */       }
/* 1823 */       String name = prefix + i++;
/* 1824 */       if (notFindInArray(name, methods))
/* 1825 */         return name; 
/*      */     } 
/*      */   }
/*      */   private static boolean notFindInArray(String prefix, String[] values) {
/* 1829 */     int len = values.length;
/* 1830 */     for (int i = 0; i < len; i++) {
/* 1831 */       if (values[i].startsWith(prefix))
/* 1832 */         return false; 
/*      */     } 
/* 1834 */     return true;
/*      */   }
/*      */   
/*      */   private void makeMemberList(Map<Object, CtClassType> table) {
/* 1838 */     int mod = getModifiers();
/* 1839 */     if (Modifier.isAbstract(mod) || Modifier.isInterface(mod)) {
/*      */       try {
/* 1841 */         CtClass[] ifs = getInterfaces();
/* 1842 */         for (CtClass ic : ifs) {
/* 1843 */           if (ic != null && ic instanceof CtClassType)
/* 1844 */             ((CtClassType)ic).makeMemberList(table); 
/*      */         } 
/* 1846 */       } catch (NotFoundException notFoundException) {}
/*      */     }
/*      */     try {
/* 1849 */       CtClass s = getSuperclass();
/* 1850 */       if (s != null && s instanceof CtClassType) {
/* 1851 */         ((CtClassType)s).makeMemberList(table);
/*      */       }
/* 1853 */     } catch (NotFoundException notFoundException) {}
/*      */     
/* 1855 */     List<MethodInfo> methods = getClassFile2().getMethods();
/* 1856 */     for (MethodInfo minfo : methods) {
/* 1857 */       table.put(minfo.getName(), this);
/*      */     }
/* 1859 */     List<FieldInfo> fields = getClassFile2().getFields();
/* 1860 */     for (FieldInfo finfo : fields)
/* 1861 */       table.put(finfo.getName(), this); 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtClassType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */