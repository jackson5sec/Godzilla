/*      */ package javassist;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.lang.invoke.MethodHandles;
/*      */ import java.net.URL;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.util.proxy.DefineClassHelper;
/*      */ import javassist.util.proxy.DefinePackageHelper;
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
/*      */ public class ClassPool
/*      */ {
/*      */   public boolean childFirstLookup = false;
/*      */   public static boolean doPruning = false;
/*      */   private int compressCount;
/*      */   private static final int COMPRESS_THRESHOLD = 100;
/*      */   public static boolean releaseUnmodifiedClassFile = true;
/*      */   public static boolean cacheOpenedJarFile = true;
/*      */   protected ClassPoolTail source;
/*      */   protected ClassPool parent;
/*      */   protected Hashtable classes;
/*  131 */   private Hashtable cflow = null;
/*      */ 
/*      */   
/*      */   private static final int INIT_HASH_SIZE = 191;
/*      */ 
/*      */   
/*      */   private ArrayList importedPackages;
/*      */ 
/*      */   
/*      */   public ClassPool() {
/*  141 */     this((ClassPool)null);
/*      */   }
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
/*      */   public ClassPool(boolean useDefaultPath) {
/*  154 */     this((ClassPool)null);
/*  155 */     if (useDefaultPath) {
/*  156 */       appendSystemPath();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPool(ClassPool parent) {
/*  167 */     this.classes = new Hashtable<>(191);
/*  168 */     this.source = new ClassPoolTail();
/*  169 */     this.parent = parent;
/*  170 */     if (parent == null) {
/*  171 */       CtClass[] pt = CtClass.primitiveTypes;
/*  172 */       for (int i = 0; i < pt.length; i++) {
/*  173 */         this.classes.put(pt[i].getName(), pt[i]);
/*      */       }
/*      */     } 
/*  176 */     this.cflow = null;
/*  177 */     this.compressCount = 0;
/*  178 */     clearImportedPackages();
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static synchronized ClassPool getDefault() {
/*  207 */     if (defaultPool == null) {
/*  208 */       defaultPool = new ClassPool(null);
/*  209 */       defaultPool.appendSystemPath();
/*      */     } 
/*      */     
/*  212 */     return defaultPool;
/*      */   }
/*      */   
/*  215 */   private static ClassPool defaultPool = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtClass getCached(String classname) {
/*  225 */     return (CtClass)this.classes.get(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cacheCtClass(String classname, CtClass c, boolean dynamic) {
/*  236 */     this.classes.put(classname, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtClass removeCached(String classname) {
/*  247 */     return (CtClass)this.classes.remove(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  254 */     return this.source.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void compress() {
/*  262 */     if (this.compressCount++ > 100) {
/*  263 */       this.compressCount = 0;
/*  264 */       Enumeration<CtClass> e = this.classes.elements();
/*  265 */       while (e.hasMoreElements()) {
/*  266 */         ((CtClass)e.nextElement()).compress();
/*      */       }
/*      */     } 
/*      */   }
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
/*      */   public void importPackage(String packageName) {
/*  288 */     this.importedPackages.add(packageName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearImportedPackages() {
/*  299 */     this.importedPackages = new ArrayList();
/*  300 */     this.importedPackages.add("java.lang");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<String> getImportedPackages() {
/*  310 */     return this.importedPackages.iterator();
/*      */   }
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
/*      */   public void recordInvalidClassName(String name) {}
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
/*      */   void recordCflow(String name, String cname, String fname) {
/*  340 */     if (this.cflow == null) {
/*  341 */       this.cflow = new Hashtable<>();
/*      */     }
/*  343 */     this.cflow.put(name, new Object[] { cname, fname });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object[] lookupCflow(String name) {
/*  352 */     if (this.cflow == null) {
/*  353 */       this.cflow = new Hashtable<>();
/*      */     }
/*  355 */     return (Object[])this.cflow.get(name);
/*      */   }
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
/*      */   public CtClass getAndRename(String orgName, String newName) throws NotFoundException {
/*  379 */     CtClass clazz = get0(orgName, false);
/*  380 */     if (clazz == null) {
/*  381 */       throw new NotFoundException(orgName);
/*      */     }
/*  383 */     if (clazz instanceof CtClassType) {
/*  384 */       ((CtClassType)clazz).setClassPool(this);
/*      */     }
/*  386 */     clazz.setName(newName);
/*      */     
/*  388 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized void classNameChanged(String oldname, CtClass clazz) {
/*  397 */     CtClass c = getCached(oldname);
/*  398 */     if (c == clazz) {
/*  399 */       removeCached(oldname);
/*      */     }
/*  401 */     String newName = clazz.getName();
/*  402 */     checkNotFrozen(newName);
/*  403 */     cacheCtClass(newName, clazz, false);
/*      */   }
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
/*      */   public CtClass get(String classname) throws NotFoundException {
/*      */     CtClass clazz;
/*  424 */     if (classname == null) {
/*  425 */       clazz = null;
/*      */     } else {
/*  427 */       clazz = get0(classname, true);
/*      */     } 
/*  429 */     if (clazz == null) {
/*  430 */       throw new NotFoundException(classname);
/*      */     }
/*  432 */     clazz.incGetCounter();
/*  433 */     return clazz;
/*      */   }
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
/*      */   public CtClass getOrNull(String classname) {
/*  452 */     CtClass clazz = null;
/*  453 */     if (classname == null) {
/*  454 */       clazz = null;
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/*  461 */         clazz = get0(classname, true);
/*      */       }
/*  463 */       catch (NotFoundException notFoundException) {}
/*      */     } 
/*  465 */     if (clazz != null) {
/*  466 */       clazz.incGetCounter();
/*      */     }
/*  468 */     return clazz;
/*      */   }
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
/*      */   public CtClass getCtClass(String classname) throws NotFoundException {
/*  492 */     if (classname.charAt(0) == '[') {
/*  493 */       return Descriptor.toCtClass(classname, this);
/*      */     }
/*  495 */     return get(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized CtClass get0(String classname, boolean useCache) throws NotFoundException {
/*  505 */     CtClass clazz = null;
/*  506 */     if (useCache) {
/*  507 */       clazz = getCached(classname);
/*  508 */       if (clazz != null) {
/*  509 */         return clazz;
/*      */       }
/*      */     } 
/*  512 */     if (!this.childFirstLookup && this.parent != null) {
/*  513 */       clazz = this.parent.get0(classname, useCache);
/*  514 */       if (clazz != null) {
/*  515 */         return clazz;
/*      */       }
/*      */     } 
/*  518 */     clazz = createCtClass(classname, useCache);
/*  519 */     if (clazz != null) {
/*      */       
/*  521 */       if (useCache) {
/*  522 */         cacheCtClass(clazz.getName(), clazz, false);
/*      */       }
/*  524 */       return clazz;
/*      */     } 
/*      */     
/*  527 */     if (this.childFirstLookup && this.parent != null) {
/*  528 */       clazz = this.parent.get0(classname, useCache);
/*      */     }
/*  530 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtClass createCtClass(String classname, boolean useCache) {
/*  542 */     if (classname.charAt(0) == '[') {
/*  543 */       classname = Descriptor.toClassName(classname);
/*      */     }
/*  545 */     if (classname.endsWith("[]")) {
/*  546 */       String base = classname.substring(0, classname.indexOf('['));
/*  547 */       if ((!useCache || getCached(base) == null) && find(base) == null) {
/*  548 */         return null;
/*      */       }
/*  550 */       return new CtArray(classname, this);
/*      */     } 
/*      */     
/*  553 */     if (find(classname) == null) {
/*  554 */       return null;
/*      */     }
/*  556 */     return new CtClassType(classname, this);
/*      */   }
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
/*      */   public URL find(String classname) {
/*  569 */     return this.source.find(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void checkNotFrozen(String classname) throws RuntimeException {
/*  581 */     CtClass clazz = getCached(classname);
/*  582 */     if (clazz == null) {
/*  583 */       if (!this.childFirstLookup && this.parent != null) {
/*      */         try {
/*  585 */           clazz = this.parent.get0(classname, true);
/*      */         }
/*  587 */         catch (NotFoundException notFoundException) {}
/*  588 */         if (clazz != null) {
/*  589 */           throw new RuntimeException(classname + " is in a parent ClassPool.  Use the parent.");
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*  594 */     else if (clazz.isFrozen()) {
/*  595 */       throw new RuntimeException(classname + ": frozen class (cannot edit)");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CtClass checkNotExists(String classname) {
/*  606 */     CtClass clazz = getCached(classname);
/*  607 */     if (clazz == null && 
/*  608 */       !this.childFirstLookup && this.parent != null) {
/*      */       try {
/*  610 */         clazz = this.parent.get0(classname, true);
/*      */       }
/*  612 */       catch (NotFoundException notFoundException) {}
/*      */     }
/*      */     
/*  615 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   InputStream openClassfile(String classname) throws NotFoundException {
/*  621 */     return this.source.openClassfile(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void writeClassfile(String classname, OutputStream out) throws NotFoundException, IOException, CannotCompileException {
/*  627 */     this.source.writeClassfile(classname, out);
/*      */   }
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
/*      */   public CtClass[] get(String[] classnames) throws NotFoundException {
/*  642 */     if (classnames == null) {
/*  643 */       return new CtClass[0];
/*      */     }
/*  645 */     int num = classnames.length;
/*  646 */     CtClass[] result = new CtClass[num];
/*  647 */     for (int i = 0; i < num; i++) {
/*  648 */       result[i] = get(classnames[i]);
/*      */     }
/*  650 */     return result;
/*      */   }
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
/*      */   public CtMethod getMethod(String classname, String methodname) throws NotFoundException {
/*  663 */     CtClass c = get(classname);
/*  664 */     return c.getDeclaredMethod(methodname);
/*      */   }
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
/*      */   public CtClass makeClass(InputStream classfile) throws IOException, RuntimeException {
/*  685 */     return makeClass(classfile, true);
/*      */   }
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
/*      */   public CtClass makeClass(InputStream classfile, boolean ifNotFrozen) throws IOException, RuntimeException {
/*  705 */     compress();
/*  706 */     classfile = new BufferedInputStream(classfile);
/*  707 */     CtClass clazz = new CtClassType(classfile, this);
/*  708 */     clazz.checkModify();
/*  709 */     String classname = clazz.getName();
/*  710 */     if (ifNotFrozen) {
/*  711 */       checkNotFrozen(classname);
/*      */     }
/*  713 */     cacheCtClass(classname, clazz, true);
/*  714 */     return clazz;
/*      */   }
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
/*      */   public CtClass makeClass(ClassFile classfile) throws RuntimeException {
/*  734 */     return makeClass(classfile, true);
/*      */   }
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
/*      */   public CtClass makeClass(ClassFile classfile, boolean ifNotFrozen) throws RuntimeException {
/*  754 */     compress();
/*  755 */     CtClass clazz = new CtClassType(classfile, this);
/*  756 */     clazz.checkModify();
/*  757 */     String classname = clazz.getName();
/*  758 */     if (ifNotFrozen) {
/*  759 */       checkNotFrozen(classname);
/*      */     }
/*  761 */     cacheCtClass(classname, clazz, true);
/*  762 */     return clazz;
/*      */   }
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
/*      */   public CtClass makeClassIfNew(InputStream classfile) throws IOException, RuntimeException {
/*  783 */     compress();
/*  784 */     classfile = new BufferedInputStream(classfile);
/*  785 */     CtClass clazz = new CtClassType(classfile, this);
/*  786 */     clazz.checkModify();
/*  787 */     String classname = clazz.getName();
/*  788 */     CtClass found = checkNotExists(classname);
/*  789 */     if (found != null) {
/*  790 */       return found;
/*      */     }
/*  792 */     cacheCtClass(classname, clazz, true);
/*  793 */     return clazz;
/*      */   }
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
/*      */   public CtClass makeClass(String classname) throws RuntimeException {
/*  814 */     return makeClass(classname, (CtClass)null);
/*      */   }
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
/*      */   public synchronized CtClass makeClass(String classname, CtClass superclass) throws RuntimeException {
/*  837 */     checkNotFrozen(classname);
/*  838 */     CtClass clazz = new CtNewClass(classname, this, false, superclass);
/*  839 */     cacheCtClass(classname, clazz, true);
/*  840 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized CtClass makeNestedClass(String classname) {
/*  851 */     checkNotFrozen(classname);
/*  852 */     CtClass clazz = new CtNewClass(classname, this, false, null);
/*  853 */     cacheCtClass(classname, clazz, true);
/*  854 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeInterface(String name) throws RuntimeException {
/*  866 */     return makeInterface(name, null);
/*      */   }
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
/*      */   public synchronized CtClass makeInterface(String name, CtClass superclass) throws RuntimeException {
/*  881 */     checkNotFrozen(name);
/*  882 */     CtClass clazz = new CtNewClass(name, this, true, superclass);
/*  883 */     cacheCtClass(name, clazz, true);
/*  884 */     return clazz;
/*      */   }
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
/*      */   public CtClass makeAnnotation(String name) throws RuntimeException {
/*      */     try {
/*  899 */       CtClass cc = makeInterface(name, get("java.lang.annotation.Annotation"));
/*  900 */       cc.setModifiers(cc.getModifiers() | 0x2000);
/*  901 */       return cc;
/*      */     }
/*  903 */     catch (NotFoundException e) {
/*      */       
/*  905 */       throw new RuntimeException(e.getMessage(), e);
/*      */     } 
/*      */   }
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
/*      */   public ClassPath appendSystemPath() {
/*  920 */     return this.source.appendSystemPath();
/*      */   }
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
/*      */   public ClassPath insertClassPath(ClassPath cp) {
/*  933 */     return this.source.insertClassPath(cp);
/*      */   }
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
/*      */   public ClassPath appendClassPath(ClassPath cp) {
/*  946 */     return this.source.appendClassPath(cp);
/*      */   }
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
/*      */   public ClassPath insertClassPath(String pathname) throws NotFoundException {
/*  964 */     return this.source.insertClassPath(pathname);
/*      */   }
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
/*      */   public ClassPath appendClassPath(String pathname) throws NotFoundException {
/*  982 */     return this.source.appendClassPath(pathname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeClassPath(ClassPath cp) {
/*  991 */     this.source.removeClassPath(cp);
/*      */   }
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
/*      */   public void appendPathList(String pathlist) throws NotFoundException {
/* 1007 */     char sep = File.pathSeparatorChar;
/* 1008 */     int i = 0;
/*      */     while (true) {
/* 1010 */       int j = pathlist.indexOf(sep, i);
/* 1011 */       if (j < 0) {
/* 1012 */         appendClassPath(pathlist.substring(i));
/*      */         
/*      */         break;
/*      */       } 
/* 1016 */       appendClassPath(pathlist.substring(i, j));
/* 1017 */       i = j + 1;
/*      */     } 
/*      */   }
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
/*      */   public Class toClass(CtClass clazz) throws CannotCompileException {
/* 1056 */     return toClass(clazz, getClassLoader());
/*      */   }
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
/*      */   public ClassLoader getClassLoader() {
/* 1070 */     return getContextClassLoader();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static ClassLoader getContextClassLoader() {
/* 1078 */     return Thread.currentThread().getContextClassLoader();
/*      */   }
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
/*      */   public Class toClass(CtClass ct, ClassLoader loader) throws CannotCompileException {
/* 1098 */     return toClass(ct, null, loader, null);
/*      */   }
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
/*      */   public Class toClass(CtClass ct, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/* 1136 */     return toClass(ct, null, loader, domain);
/*      */   }
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
/*      */   public Class<?> toClass(CtClass ct, Class<?> neighbor) throws CannotCompileException {
/*      */     try {
/* 1158 */       return DefineClassHelper.toClass(neighbor, ct
/* 1159 */           .toBytecode());
/*      */     }
/* 1161 */     catch (IOException e) {
/* 1162 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
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
/*      */   public Class<?> toClass(CtClass ct, MethodHandles.Lookup lookup) throws CannotCompileException {
/*      */     try {
/* 1184 */       return DefineClassHelper.toClass(lookup, ct
/* 1185 */           .toBytecode());
/*      */     }
/* 1187 */     catch (IOException e) {
/* 1188 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class toClass(CtClass ct, Class<?> neighbor, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/*      */     try {
/* 1240 */       return DefineClassHelper.toClass(ct.getName(), neighbor, loader, domain, ct
/* 1241 */           .toBytecode());
/*      */     }
/* 1243 */     catch (IOException e) {
/* 1244 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
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
/*      */ 
/*      */   
/*      */   public void makePackage(ClassLoader loader, String name) throws CannotCompileException {
/* 1271 */     DefinePackageHelper.definePackage(name, loader);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\ClassPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */