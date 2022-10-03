/*     */ package javassist.compiler;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtField;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.compiler.ast.ASTList;
/*     */ import javassist.compiler.ast.ASTree;
/*     */ import javassist.compiler.ast.Declarator;
/*     */ import javassist.compiler.ast.Keyword;
/*     */ import javassist.compiler.ast.Symbol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemberResolver
/*     */   implements TokenId
/*     */ {
/*     */   private ClassPool classPool;
/*     */   private static final int YES = 0;
/*     */   private static final int NO = -1;
/*     */   private static final String INVALID = "<invalid>";
/*     */   
/*     */   public ClassPool getClassPool() {
/*     */     return this.classPool;
/*     */   }
/*     */   
/*     */   private static void fatal() throws CompileError {
/*     */     throw new CompileError("fatal");
/*     */   }
/*     */   
/*     */   public static class Method
/*     */   {
/*     */     public CtClass declaring;
/*     */     public MethodInfo info;
/*     */     public int notmatch;
/*     */     
/*     */     public Method(CtClass c, MethodInfo i, int n) {
/*     */       this.declaring = c;
/*     */       this.info = i;
/*     */       this.notmatch = n;
/*     */     }
/*     */     
/*     */     public boolean isStatic() {
/*     */       int acc = this.info.getAccessFlags();
/*     */       return ((acc & 0x8) != 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public Method lookupMethod(CtClass clazz, CtClass currentClass, MethodInfo current, String methodName, int[] argTypes, int[] argDims, String[] argClassNames) throws CompileError {
/*     */     Method maybe = null;
/*     */     if (current != null && clazz == currentClass && current.getName().equals(methodName)) {
/*     */       int res = compareSignature(current.getDescriptor(), argTypes, argDims, argClassNames);
/*     */       if (res != -1) {
/*     */         Method r = new Method(clazz, current, res);
/*     */         if (res == 0)
/*     */           return r; 
/*     */         maybe = r;
/*     */       } 
/*     */     } 
/*     */     Method m = lookupMethod(clazz, methodName, argTypes, argDims, argClassNames, (maybe != null));
/*     */     if (m != null)
/*     */       return m; 
/*     */     return maybe;
/*     */   }
/*     */   
/*     */   private Method lookupMethod(CtClass clazz, String methodName, int[] argTypes, int[] argDims, String[] argClassNames, boolean onlyExact) throws CompileError {
/*     */     Method maybe = null;
/*     */     ClassFile cf = clazz.getClassFile2();
/*     */     if (cf != null) {
/*     */       List<MethodInfo> list = cf.getMethods();
/*     */       for (MethodInfo minfo : list) {
/*     */         if (minfo.getName().equals(methodName) && (minfo.getAccessFlags() & 0x40) == 0) {
/*     */           int res = compareSignature(minfo.getDescriptor(), argTypes, argDims, argClassNames);
/*     */           if (res != -1) {
/*     */             Method r = new Method(clazz, minfo, res);
/*     */             if (res == 0)
/*     */               return r; 
/*     */             if (maybe == null || maybe.notmatch > res)
/*     */               maybe = r; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     if (onlyExact) {
/*     */       maybe = null;
/*     */     } else if (maybe != null) {
/*     */       return maybe;
/*     */     } 
/*     */     int mod = clazz.getModifiers();
/*     */     boolean isIntf = Modifier.isInterface(mod);
/*     */     try {
/*     */       if (!isIntf) {
/*     */         CtClass pclazz = clazz.getSuperclass();
/*     */         if (pclazz != null) {
/*     */           Method r = lookupMethod(pclazz, methodName, argTypes, argDims, argClassNames, onlyExact);
/*     */           if (r != null)
/*     */             return r; 
/*     */         } 
/*     */       } 
/*     */     } catch (NotFoundException notFoundException) {}
/*     */     try {
/*     */       CtClass[] ifs = clazz.getInterfaces();
/*     */       for (CtClass intf : ifs) {
/*     */         Method r = lookupMethod(intf, methodName, argTypes, argDims, argClassNames, onlyExact);
/*     */         if (r != null)
/*     */           return r; 
/*     */       } 
/*     */       if (isIntf) {
/*     */         CtClass pclazz = clazz.getSuperclass();
/*     */         if (pclazz != null) {
/*     */           Method r = lookupMethod(pclazz, methodName, argTypes, argDims, argClassNames, onlyExact);
/*     */           if (r != null)
/*     */             return r; 
/*     */         } 
/*     */       } 
/*     */     } catch (NotFoundException notFoundException) {}
/*     */     return maybe;
/*     */   }
/*     */   
/*     */   private int compareSignature(String desc, int[] argTypes, int[] argDims, String[] argClassNames) throws CompileError {
/*     */     int result = 0;
/*     */     int i = 1;
/*     */     int nArgs = argTypes.length;
/*     */     if (nArgs != Descriptor.numOfParameters(desc))
/*     */       return -1; 
/*     */     int len = desc.length();
/*     */     for (int n = 0; i < len; n++) {
/*     */       char c = desc.charAt(i++);
/*     */       if (c == ')')
/*     */         return (n == nArgs) ? result : -1; 
/*     */       if (n >= nArgs)
/*     */         return -1; 
/*     */       int dim = 0;
/*     */       while (c == '[') {
/*     */         dim++;
/*     */         c = desc.charAt(i++);
/*     */       } 
/*     */       if (argTypes[n] == 412) {
/*     */         if (dim == 0 && c != 'L')
/*     */           return -1; 
/*     */         if (c == 'L')
/*     */           i = desc.indexOf(';', i) + 1; 
/*     */       } else if (argDims[n] != dim) {
/*     */         if (dim != 0 || c != 'L' || !desc.startsWith("java/lang/Object;", i))
/*     */           return -1; 
/*     */         i = desc.indexOf(';', i) + 1;
/*     */         result++;
/*     */         if (i <= 0)
/*     */           return -1; 
/*     */       } else if (c == 'L') {
/*     */         int j = desc.indexOf(';', i);
/*     */         if (j < 0 || argTypes[n] != 307)
/*     */           return -1; 
/*     */         String cname = desc.substring(i, j);
/*     */         if (!cname.equals(argClassNames[n])) {
/*     */           CtClass clazz = lookupClassByJvmName(argClassNames[n]);
/*     */           try {
/*     */             if (clazz.subtypeOf(lookupClassByJvmName(cname))) {
/*     */               result++;
/*     */             } else {
/*     */               return -1;
/*     */             } 
/*     */           } catch (NotFoundException e) {
/*     */             result++;
/*     */           } 
/*     */         } 
/*     */         i = j + 1;
/*     */       } else {
/*     */         int t = descToType(c);
/*     */         int at = argTypes[n];
/*     */         if (t != at)
/*     */           if (t == 324 && (at == 334 || at == 303 || at == 306)) {
/*     */             result++;
/*     */           } else {
/*     */             return -1;
/*     */           }  
/*     */       } 
/*     */     } 
/*     */     return -1;
/*     */   }
/*     */   
/*     */   public CtField lookupFieldByJvmName2(String jvmClassName, Symbol fieldSym, ASTree expr) throws NoFieldException {
/*     */     String field = fieldSym.get();
/*     */     CtClass cc = null;
/*     */     try {
/*     */       cc = lookupClass(jvmToJavaName(jvmClassName), true);
/*     */     } catch (CompileError e) {
/*     */       throw new NoFieldException(jvmClassName + "/" + field, expr);
/*     */     } 
/*     */     try {
/*     */       return cc.getField(field);
/*     */     } catch (NotFoundException e) {
/*     */       jvmClassName = javaToJvmName(cc.getName());
/*     */       throw new NoFieldException(jvmClassName + "$" + field, expr);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CtField lookupFieldByJvmName(String jvmClassName, Symbol fieldName) throws CompileError {
/*     */     return lookupField(jvmToJavaName(jvmClassName), fieldName);
/*     */   }
/*     */   
/*     */   public CtField lookupField(String className, Symbol fieldName) throws CompileError {
/*     */     CtClass cc = lookupClass(className, false);
/*     */     try {
/*     */       return cc.getField(fieldName.get());
/*     */     } catch (NotFoundException notFoundException) {
/*     */       throw new CompileError("no such field: " + fieldName.get());
/*     */     } 
/*     */   }
/*     */   
/*     */   public CtClass lookupClassByName(ASTList name) throws CompileError {
/*     */     return lookupClass(Declarator.astToClassName(name, '.'), false);
/*     */   }
/*     */   
/*     */   public CtClass lookupClassByJvmName(String jvmName) throws CompileError {
/*     */     return lookupClass(jvmToJavaName(jvmName), false);
/*     */   }
/*     */   
/*     */   public CtClass lookupClass(Declarator decl) throws CompileError {
/*     */     return lookupClass(decl.getType(), decl.getArrayDim(), decl.getClassName());
/*     */   }
/*     */   
/*     */   public CtClass lookupClass(int type, int dim, String classname) throws CompileError {
/*     */     String cname = "";
/*     */     if (type == 307) {
/*     */       CtClass clazz = lookupClassByJvmName(classname);
/*     */       if (dim > 0) {
/*     */         cname = clazz.getName();
/*     */       } else {
/*     */         return clazz;
/*     */       } 
/*     */     } else {
/*     */       cname = getTypeName(type);
/*     */     } 
/*     */     while (dim-- > 0)
/*     */       cname = cname + "[]"; 
/*     */     return lookupClass(cname, false);
/*     */   }
/*     */   
/*     */   static String getTypeName(int type) throws CompileError {
/*     */     String cname = "";
/*     */     switch (type) {
/*     */       case 301:
/*     */         cname = "boolean";
/*     */         return cname;
/*     */       case 306:
/*     */         cname = "char";
/*     */         return cname;
/*     */       case 303:
/*     */         cname = "byte";
/*     */         return cname;
/*     */       case 334:
/*     */         cname = "short";
/*     */         return cname;
/*     */       case 324:
/*     */         cname = "int";
/*     */         return cname;
/*     */       case 326:
/*     */         cname = "long";
/*     */         return cname;
/*     */       case 317:
/*     */         cname = "float";
/*     */         return cname;
/*     */       case 312:
/*     */         cname = "double";
/*     */         return cname;
/*     */       case 344:
/*     */         cname = "void";
/*     */         return cname;
/*     */     } 
/*     */     fatal();
/*     */     return cname;
/*     */   }
/*     */   
/*     */   public CtClass lookupClass(String name, boolean notCheckInner) throws CompileError {
/*     */     Map<String, String> cache = getInvalidNames();
/*     */     String found = cache.get(name);
/*     */     if (found == "<invalid>")
/*     */       throw new CompileError("no such class: " + name); 
/*     */     if (found != null)
/*     */       try {
/*     */         return this.classPool.get(found);
/*     */       } catch (NotFoundException notFoundException) {} 
/*     */     CtClass cc = null;
/*     */     try {
/*     */       cc = lookupClass0(name, notCheckInner);
/*     */     } catch (NotFoundException e) {
/*     */       cc = searchImports(name);
/*     */     } 
/*     */     cache.put(name, cc.getName());
/*     */     return cc;
/*     */   }
/*     */   
/*     */   private static Map<ClassPool, Reference<Map<String, String>>> invalidNamesMap = new WeakHashMap<>();
/*     */   private Map<String, String> invalidNames;
/*     */   
/*     */   public MemberResolver(ClassPool cp) {
/* 432 */     this.invalidNames = null;
/*     */     this.classPool = cp;
/*     */   } public static int getInvalidMapSize() {
/* 435 */     return invalidNamesMap.size();
/*     */   }
/*     */   private Map<String, String> getInvalidNames() {
/* 438 */     Map<String, String> ht = this.invalidNames;
/* 439 */     if (ht == null) {
/* 440 */       synchronized (MemberResolver.class) {
/* 441 */         Reference<Map<String, String>> ref = invalidNamesMap.get(this.classPool);
/* 442 */         if (ref != null) {
/* 443 */           ht = ref.get();
/*     */         }
/* 445 */         if (ht == null) {
/* 446 */           ht = new Hashtable<>();
/* 447 */           invalidNamesMap.put(this.classPool, new WeakReference<>(ht));
/*     */         } 
/*     */       } 
/*     */       
/* 451 */       this.invalidNames = ht;
/*     */     } 
/*     */     
/* 454 */     return ht;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass searchImports(String orgName) throws CompileError {
/* 460 */     if (orgName.indexOf('.') < 0) {
/* 461 */       Iterator<String> it = this.classPool.getImportedPackages();
/* 462 */       while (it.hasNext()) {
/* 463 */         String pac = it.next();
/* 464 */         String fqName = pac.replaceAll("\\.$", "") + "." + orgName;
/*     */         try {
/* 466 */           return this.classPool.get(fqName);
/*     */         }
/* 468 */         catch (NotFoundException e) {
/*     */           try {
/* 470 */             if (pac.endsWith("." + orgName)) {
/* 471 */               return this.classPool.get(pac);
/*     */             }
/* 473 */           } catch (NotFoundException notFoundException) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 478 */     getInvalidNames().put(orgName, "<invalid>");
/* 479 */     throw new CompileError("no such class: " + orgName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass lookupClass0(String classname, boolean notCheckInner) throws NotFoundException {
/* 485 */     CtClass cc = null;
/*     */     while (true) {
/*     */       try {
/* 488 */         cc = this.classPool.get(classname);
/*     */       }
/* 490 */       catch (NotFoundException e) {
/* 491 */         int i = classname.lastIndexOf('.');
/* 492 */         if (notCheckInner || i < 0)
/* 493 */           throw e; 
/* 494 */         StringBuffer sbuf = new StringBuffer(classname);
/* 495 */         sbuf.setCharAt(i, '$');
/* 496 */         classname = sbuf.toString();
/*     */       } 
/* 498 */       if (cc != null) {
/* 499 */         return cc;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String resolveClassName(ASTList name) throws CompileError {
/* 508 */     if (name == null)
/* 509 */       return null; 
/* 510 */     return javaToJvmName(lookupClassByName(name).getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String resolveJvmClassName(String jvmName) throws CompileError {
/* 517 */     if (jvmName == null)
/* 518 */       return null; 
/* 519 */     return javaToJvmName(lookupClassByJvmName(jvmName).getName());
/*     */   }
/*     */   
/*     */   public static CtClass getSuperclass(CtClass c) throws CompileError {
/*     */     try {
/* 524 */       CtClass sc = c.getSuperclass();
/* 525 */       if (sc != null) {
/* 526 */         return sc;
/*     */       }
/* 528 */     } catch (NotFoundException notFoundException) {}
/* 529 */     throw new CompileError("cannot find the super class of " + c
/* 530 */         .getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static CtClass getSuperInterface(CtClass c, String interfaceName) throws CompileError {
/*     */     try {
/* 537 */       CtClass[] intfs = c.getInterfaces();
/* 538 */       for (int i = 0; i < intfs.length; i++)
/* 539 */       { if (intfs[i].getName().equals(interfaceName))
/* 540 */           return intfs[i];  } 
/* 541 */     } catch (NotFoundException notFoundException) {}
/* 542 */     throw new CompileError("cannot find the super interface " + interfaceName + " of " + c
/* 543 */         .getName());
/*     */   }
/*     */   
/*     */   public static String javaToJvmName(String classname) {
/* 547 */     return classname.replace('.', '/');
/*     */   }
/*     */   
/*     */   public static String jvmToJavaName(String classname) {
/* 551 */     return classname.replace('/', '.');
/*     */   }
/*     */   
/*     */   public static int descToType(char c) throws CompileError {
/* 555 */     switch (c) {
/*     */       case 'Z':
/* 557 */         return 301;
/*     */       case 'C':
/* 559 */         return 306;
/*     */       case 'B':
/* 561 */         return 303;
/*     */       case 'S':
/* 563 */         return 334;
/*     */       case 'I':
/* 565 */         return 324;
/*     */       case 'J':
/* 567 */         return 326;
/*     */       case 'F':
/* 569 */         return 317;
/*     */       case 'D':
/* 571 */         return 312;
/*     */       case 'V':
/* 573 */         return 344;
/*     */       case 'L':
/*     */       case '[':
/* 576 */         return 307;
/*     */     } 
/* 578 */     fatal();
/* 579 */     return 344;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getModifiers(ASTList mods) {
/* 584 */     int m = 0;
/* 585 */     while (mods != null) {
/* 586 */       Keyword k = (Keyword)mods.head();
/* 587 */       mods = mods.tail();
/* 588 */       switch (k.get()) {
/*     */         case 335:
/* 590 */           m |= 0x8;
/*     */         
/*     */         case 315:
/* 593 */           m |= 0x10;
/*     */         
/*     */         case 338:
/* 596 */           m |= 0x20;
/*     */         
/*     */         case 300:
/* 599 */           m |= 0x400;
/*     */         
/*     */         case 332:
/* 602 */           m |= 0x1;
/*     */         
/*     */         case 331:
/* 605 */           m |= 0x4;
/*     */         
/*     */         case 330:
/* 608 */           m |= 0x2;
/*     */         
/*     */         case 345:
/* 611 */           m |= 0x40;
/*     */         
/*     */         case 342:
/* 614 */           m |= 0x80;
/*     */         
/*     */         case 347:
/* 617 */           m |= 0x800;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 622 */     return m;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\MemberResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */