/*     */ package javassist.tools.reflect;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CodeConverter;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtField;
/*     */ import javassist.CtMethod;
/*     */ import javassist.CtNewMethod;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.Translator;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Reflection
/*     */   implements Translator
/*     */ {
/*     */   static final String classobjectField = "_classobject";
/*     */   static final String classobjectAccessor = "_getClass";
/*     */   static final String metaobjectField = "_metaobject";
/*     */   static final String metaobjectGetter = "_getMetaobject";
/*     */   static final String metaobjectSetter = "_setMetaobject";
/*     */   static final String readPrefix = "_r_";
/*     */   static final String writePrefix = "_w_";
/*     */   static final String metaobjectClassName = "javassist.tools.reflect.Metaobject";
/*     */   static final String classMetaobjectClassName = "javassist.tools.reflect.ClassMetaobject";
/*     */   protected CtMethod trapMethod;
/*     */   protected CtMethod trapStaticMethod;
/*     */   protected CtMethod trapRead;
/*     */   protected CtMethod trapWrite;
/*     */   protected CtClass[] readParam;
/*     */   protected ClassPool classPool;
/*     */   protected CodeConverter converter;
/*     */   
/*     */   private boolean isExcluded(String name) {
/*  97 */     return (name.startsWith("_m_") || name
/*  98 */       .equals("_getClass") || name
/*  99 */       .equals("_setMetaobject") || name
/* 100 */       .equals("_getMetaobject") || name
/* 101 */       .startsWith("_r_") || name
/* 102 */       .startsWith("_w_"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reflection() {
/* 109 */     this.classPool = null;
/* 110 */     this.converter = new CodeConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(ClassPool pool) throws NotFoundException {
/* 118 */     this.classPool = pool;
/* 119 */     String msg = "javassist.tools.reflect.Sample is not found or broken.";
/*     */     
/*     */     try {
/* 122 */       CtClass c = this.classPool.get("javassist.tools.reflect.Sample");
/* 123 */       rebuildClassFile(c.getClassFile());
/* 124 */       this.trapMethod = c.getDeclaredMethod("trap");
/* 125 */       this.trapStaticMethod = c.getDeclaredMethod("trapStatic");
/* 126 */       this.trapRead = c.getDeclaredMethod("trapRead");
/* 127 */       this.trapWrite = c.getDeclaredMethod("trapWrite");
/* 128 */       this
/* 129 */         .readParam = new CtClass[] { this.classPool.get("java.lang.Object") };
/*     */     }
/* 131 */     catch (NotFoundException e) {
/* 132 */       throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
/* 133 */     } catch (BadBytecode e) {
/* 134 */       throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
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
/*     */   public void onLoad(ClassPool pool, String classname) throws CannotCompileException, NotFoundException {
/* 146 */     CtClass clazz = pool.get(classname);
/* 147 */     clazz.instrument(this.converter);
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
/*     */   public boolean makeReflective(String classname, String metaobject, String metaclass) throws CannotCompileException, NotFoundException {
/* 167 */     return makeReflective(this.classPool.get(classname), this.classPool
/* 168 */         .get(metaobject), this.classPool
/* 169 */         .get(metaclass));
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
/*     */   public boolean makeReflective(Class<?> clazz, Class<?> metaobject, Class<?> metaclass) throws CannotCompileException, NotFoundException {
/* 193 */     return makeReflective(clazz.getName(), metaobject.getName(), metaclass
/* 194 */         .getName());
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
/*     */   
/*     */   public boolean makeReflective(CtClass clazz, CtClass metaobject, CtClass metaclass) throws CannotCompileException, CannotReflectException, NotFoundException {
/* 220 */     if (clazz.isInterface()) {
/* 221 */       throw new CannotReflectException("Cannot reflect an interface: " + clazz
/* 222 */           .getName());
/*     */     }
/* 224 */     if (clazz.subclassOf(this.classPool.get("javassist.tools.reflect.ClassMetaobject"))) {
/* 225 */       throw new CannotReflectException("Cannot reflect a subclass of ClassMetaobject: " + clazz
/*     */           
/* 227 */           .getName());
/*     */     }
/* 229 */     if (clazz.subclassOf(this.classPool.get("javassist.tools.reflect.Metaobject"))) {
/* 230 */       throw new CannotReflectException("Cannot reflect a subclass of Metaobject: " + clazz
/*     */           
/* 232 */           .getName());
/*     */     }
/* 234 */     registerReflectiveClass(clazz);
/* 235 */     return modifyClassfile(clazz, metaobject, metaclass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerReflectiveClass(CtClass clazz) {
/* 243 */     CtField[] fs = clazz.getDeclaredFields();
/* 244 */     for (int i = 0; i < fs.length; i++) {
/* 245 */       CtField f = fs[i];
/* 246 */       int mod = f.getModifiers();
/* 247 */       if ((mod & 0x1) != 0 && (mod & 0x10) == 0) {
/* 248 */         String name = f.getName();
/* 249 */         this.converter.replaceFieldRead(f, clazz, "_r_" + name);
/* 250 */         this.converter.replaceFieldWrite(f, clazz, "_w_" + name);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean modifyClassfile(CtClass clazz, CtClass metaobject, CtClass metaclass) throws CannotCompileException, NotFoundException {
/* 259 */     if (clazz.getAttribute("Reflective") != null)
/* 260 */       return false; 
/* 261 */     clazz.setAttribute("Reflective", new byte[0]);
/*     */     
/* 263 */     CtClass mlevel = this.classPool.get("javassist.tools.reflect.Metalevel");
/* 264 */     boolean addMeta = !clazz.subtypeOf(mlevel);
/* 265 */     if (addMeta) {
/* 266 */       clazz.addInterface(mlevel);
/*     */     }
/* 268 */     processMethods(clazz, addMeta);
/* 269 */     processFields(clazz);
/*     */ 
/*     */     
/* 272 */     if (addMeta) {
/* 273 */       CtField ctField = new CtField(this.classPool.get("javassist.tools.reflect.Metaobject"), "_metaobject", clazz);
/*     */       
/* 275 */       ctField.setModifiers(4);
/* 276 */       clazz.addField(ctField, CtField.Initializer.byNewWithParams(metaobject));
/*     */       
/* 278 */       clazz.addMethod(CtNewMethod.getter("_getMetaobject", ctField));
/* 279 */       clazz.addMethod(CtNewMethod.setter("_setMetaobject", ctField));
/*     */     } 
/*     */     
/* 282 */     CtField f = new CtField(this.classPool.get("javassist.tools.reflect.ClassMetaobject"), "_classobject", clazz);
/*     */     
/* 284 */     f.setModifiers(10);
/* 285 */     clazz.addField(f, CtField.Initializer.byNew(metaclass, new String[] { clazz
/* 286 */             .getName() }));
/*     */     
/* 288 */     clazz.addMethod(CtNewMethod.getter("_getClass", f));
/* 289 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processMethods(CtClass clazz, boolean dontSearch) throws CannotCompileException, NotFoundException {
/* 295 */     CtMethod[] ms = clazz.getMethods();
/* 296 */     for (int i = 0; i < ms.length; i++) {
/* 297 */       CtMethod m = ms[i];
/* 298 */       int mod = m.getModifiers();
/* 299 */       if (Modifier.isPublic(mod) && !Modifier.isAbstract(mod)) {
/* 300 */         processMethods0(mod, clazz, m, i, dontSearch);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processMethods0(int mod, CtClass clazz, CtMethod m, int identifier, boolean dontSearch) throws CannotCompileException, NotFoundException {
/*     */     CtMethod body, m2;
/* 309 */     String name = m.getName();
/*     */     
/* 311 */     if (isExcluded(name)) {
/*     */       return;
/*     */     }
/*     */     
/* 315 */     if (m.getDeclaringClass() == clazz) {
/* 316 */       if (Modifier.isNative(mod)) {
/*     */         return;
/*     */       }
/* 319 */       m2 = m;
/* 320 */       if (Modifier.isFinal(mod)) {
/* 321 */         mod &= 0xFFFFFFEF;
/* 322 */         m2.setModifiers(mod);
/*     */       } 
/*     */     } else {
/*     */       
/* 326 */       if (Modifier.isFinal(mod)) {
/*     */         return;
/*     */       }
/* 329 */       mod &= 0xFFFFFEFF;
/* 330 */       m2 = CtNewMethod.delegator(findOriginal(m, dontSearch), clazz);
/* 331 */       m2.setModifiers(mod);
/* 332 */       clazz.addMethod(m2);
/*     */     } 
/*     */     
/* 335 */     m2.setName("_m_" + identifier + "_" + name);
/*     */ 
/*     */     
/* 338 */     if (Modifier.isStatic(mod)) {
/* 339 */       body = this.trapStaticMethod;
/*     */     } else {
/* 341 */       body = this.trapMethod;
/*     */     } 
/*     */     
/* 344 */     CtMethod wmethod = CtNewMethod.wrapped(m.getReturnType(), name, m
/* 345 */         .getParameterTypes(), m.getExceptionTypes(), body, 
/* 346 */         CtMethod.ConstParameter.integer(identifier), clazz);
/*     */     
/* 348 */     wmethod.setModifiers(mod);
/* 349 */     clazz.addMethod(wmethod);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CtMethod findOriginal(CtMethod m, boolean dontSearch) throws NotFoundException {
/* 355 */     if (dontSearch) {
/* 356 */       return m;
/*     */     }
/* 358 */     String name = m.getName();
/* 359 */     CtMethod[] ms = m.getDeclaringClass().getDeclaredMethods();
/* 360 */     for (int i = 0; i < ms.length; i++) {
/* 361 */       String orgName = ms[i].getName();
/* 362 */       if (orgName.endsWith(name) && orgName
/* 363 */         .startsWith("_m_") && ms[i]
/* 364 */         .getSignature().equals(m.getSignature())) {
/* 365 */         return ms[i];
/*     */       }
/*     */     } 
/* 368 */     return m;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processFields(CtClass clazz) throws CannotCompileException, NotFoundException {
/* 374 */     CtField[] fs = clazz.getDeclaredFields();
/* 375 */     for (int i = 0; i < fs.length; i++) {
/* 376 */       CtField f = fs[i];
/* 377 */       int mod = f.getModifiers();
/* 378 */       if ((mod & 0x1) != 0 && (mod & 0x10) == 0) {
/* 379 */         mod |= 0x8;
/* 380 */         String name = f.getName();
/* 381 */         CtClass ftype = f.getType();
/*     */         
/* 383 */         CtMethod wmethod = CtNewMethod.wrapped(ftype, "_r_" + name, this.readParam, null, this.trapRead, 
/*     */             
/* 385 */             CtMethod.ConstParameter.string(name), clazz);
/*     */         
/* 387 */         wmethod.setModifiers(mod);
/* 388 */         clazz.addMethod(wmethod);
/* 389 */         CtClass[] writeParam = new CtClass[2];
/* 390 */         writeParam[0] = this.classPool.get("java.lang.Object");
/* 391 */         writeParam[1] = ftype;
/* 392 */         wmethod = CtNewMethod.wrapped(CtClass.voidType, "_w_" + name, writeParam, null, this.trapWrite, 
/*     */ 
/*     */             
/* 395 */             CtMethod.ConstParameter.string(name), clazz);
/* 396 */         wmethod.setModifiers(mod);
/* 397 */         clazz.addMethod(wmethod);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rebuildClassFile(ClassFile cf) throws BadBytecode {
/* 403 */     if (ClassFile.MAJOR_VERSION < 50) {
/*     */       return;
/*     */     }
/* 406 */     for (MethodInfo mi : cf.getMethods())
/* 407 */       mi.rebuildStackMap(this.classPool); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\reflect\Reflection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */