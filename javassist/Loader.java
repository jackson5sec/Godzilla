/*     */ package javassist;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.URL;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Vector;
/*     */ import javassist.bytecode.ClassFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Loader
/*     */   extends ClassLoader
/*     */ {
/*     */   private HashMap<String, ClassLoader> notDefinedHere;
/*     */   private Vector<String> notDefinedPackages;
/*     */   private ClassPool source;
/*     */   private Translator translator;
/*     */   private ProtectionDomain domain;
/*     */   
/*     */   public static class Simple
/*     */     extends ClassLoader
/*     */   {
/*     */     public Simple() {}
/*     */     
/*     */     public Simple(ClassLoader parent) {
/* 170 */       super(parent);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> invokeDefineClass(CtClass cc) throws IOException, CannotCompileException {
/* 178 */       byte[] code = cc.toBytecode();
/* 179 */       return defineClass(cc.getName(), code, 0, code.length);
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
/*     */   public boolean doDelegation = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Loader() {
/* 206 */     this((ClassPool)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Loader(ClassPool cp) {
/* 215 */     init(cp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Loader(ClassLoader parent, ClassPool cp) {
/* 226 */     super(parent);
/* 227 */     init(cp);
/*     */   }
/*     */   
/*     */   private void init(ClassPool cp) {
/* 231 */     this.notDefinedHere = new HashMap<>();
/* 232 */     this.notDefinedPackages = new Vector<>();
/* 233 */     this.source = cp;
/* 234 */     this.translator = null;
/* 235 */     this.domain = null;
/* 236 */     delegateLoadingOf("javassist.Loader");
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
/*     */   public void delegateLoadingOf(String classname) {
/* 248 */     if (classname.endsWith(".")) {
/* 249 */       this.notDefinedPackages.addElement(classname);
/*     */     } else {
/* 251 */       this.notDefinedHere.put(classname, this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDomain(ProtectionDomain d) {
/* 261 */     this.domain = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassPool(ClassPool cp) {
/* 268 */     this.source = cp;
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
/*     */   public void addTranslator(ClassPool cp, Translator t) throws NotFoundException, CannotCompileException {
/* 282 */     this.source = cp;
/* 283 */     this.translator = t;
/* 284 */     t.start(cp);
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
/*     */   public static void main(String[] args) throws Throwable {
/* 301 */     Loader cl = new Loader();
/* 302 */     cl.run(args);
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
/*     */   public void run(String[] args) throws Throwable {
/* 315 */     if (args.length >= 1) {
/* 316 */       run(args[0], Arrays.<String>copyOfRange(args, 1, args.length));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run(String classname, String[] args) throws Throwable {
/* 326 */     Class<?> c = loadClass(classname);
/*     */     try {
/* 328 */       c.getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });
/*     */ 
/*     */     
/*     */     }
/* 332 */     catch (InvocationTargetException e) {
/* 333 */       throw e.getTargetException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> loadClass(String name, boolean resolve) throws ClassFormatError, ClassNotFoundException {
/* 343 */     name = name.intern();
/* 344 */     synchronized (name) {
/* 345 */       Class<?> c = findLoadedClass(name);
/* 346 */       if (c == null) {
/* 347 */         c = loadClassByDelegation(name);
/*     */       }
/* 349 */       if (c == null) {
/* 350 */         c = findClass(name);
/*     */       }
/* 352 */       if (c == null) {
/* 353 */         c = delegateToParent(name);
/*     */       }
/* 355 */       if (resolve) {
/* 356 */         resolveClass(c);
/*     */       }
/* 358 */       return c;
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
/*     */   protected Class<?> findClass(String name) throws ClassNotFoundException {
/*     */     byte[] classfile;
/*     */     try {
/* 378 */       if (this.source != null) {
/* 379 */         if (this.translator != null) {
/* 380 */           this.translator.onLoad(this.source, name);
/*     */         }
/*     */         try {
/* 383 */           classfile = this.source.get(name).toBytecode();
/*     */         }
/* 385 */         catch (NotFoundException e) {
/* 386 */           return null;
/*     */         } 
/*     */       } else {
/*     */         
/* 390 */         String jarname = "/" + name.replace('.', '/') + ".class";
/* 391 */         InputStream in = getClass().getResourceAsStream(jarname);
/* 392 */         if (in == null) {
/* 393 */           return null;
/*     */         }
/* 395 */         classfile = ClassPoolTail.readStream(in);
/*     */       }
/*     */     
/* 398 */     } catch (Exception e) {
/* 399 */       throw new ClassNotFoundException("caught an exception while obtaining a class file for " + name, e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 404 */     int i = name.lastIndexOf('.');
/* 405 */     if (i != -1) {
/* 406 */       String pname = name.substring(0, i);
/* 407 */       if (isDefinedPackage(pname)) {
/*     */         try {
/* 409 */           definePackage(pname, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (URL)null);
/*     */         
/*     */         }
/* 412 */         catch (IllegalArgumentException illegalArgumentException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 418 */     if (this.domain == null)
/* 419 */       return defineClass(name, classfile, 0, classfile.length); 
/* 420 */     return defineClass(name, classfile, 0, classfile.length, this.domain);
/*     */   }
/*     */   
/*     */   private boolean isDefinedPackage(String name) {
/* 424 */     if (ClassFile.MAJOR_VERSION >= 53) {
/* 425 */       return (getDefinedPackage(name) == null);
/*     */     }
/* 427 */     return (getPackage(name) == null);
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
/*     */   protected Class<?> loadClassByDelegation(String name) throws ClassNotFoundException {
/* 443 */     Class<?> c = null;
/* 444 */     if (this.doDelegation && (
/* 445 */       name.startsWith("java.") || name
/* 446 */       .startsWith("javax.") || name
/* 447 */       .startsWith("sun.") || name
/* 448 */       .startsWith("com.sun.") || name
/* 449 */       .startsWith("org.w3c.") || name
/* 450 */       .startsWith("org.xml.") || 
/* 451 */       notDelegated(name))) {
/* 452 */       c = delegateToParent(name);
/*     */     }
/* 454 */     return c;
/*     */   }
/*     */   
/*     */   private boolean notDelegated(String name) {
/* 458 */     if (this.notDefinedHere.containsKey(name)) {
/* 459 */       return true;
/*     */     }
/* 461 */     for (String pack : this.notDefinedPackages) {
/* 462 */       if (name.startsWith(pack))
/* 463 */         return true; 
/*     */     } 
/* 465 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> delegateToParent(String classname) throws ClassNotFoundException {
/* 471 */     ClassLoader cl = getParent();
/* 472 */     if (cl != null)
/* 473 */       return cl.loadClass(classname); 
/* 474 */     return findSystemClass(classname);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\Loader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */