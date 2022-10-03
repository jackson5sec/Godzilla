/*     */ package javassist.tools.rmi;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtConstructor;
/*     */ import javassist.CtField;
/*     */ import javassist.CtMethod;
/*     */ import javassist.CtNewConstructor;
/*     */ import javassist.CtNewMethod;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.Translator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StubGenerator
/*     */   implements Translator
/*     */ {
/*     */   private static final String fieldImporter = "importer";
/*     */   private static final String fieldObjectId = "objectId";
/*     */   private static final String accessorObjectId = "_getObjectId";
/*     */   private static final String sampleClass = "javassist.tools.rmi.Sample";
/*     */   private ClassPool classPool;
/*  74 */   private Map<String, CtClass> proxyClasses = new Hashtable<>();
/*     */   
/*     */   private CtMethod forwardMethod;
/*     */   
/*     */   private CtMethod forwardStaticMethod;
/*     */   
/*     */   private CtClass[] proxyConstructorParamTypes;
/*     */   private CtClass[] interfacesForProxy;
/*     */   private CtClass[] exceptionForProxy;
/*     */   
/*     */   public void start(ClassPool pool) throws NotFoundException {
/*  85 */     this.classPool = pool;
/*  86 */     CtClass c = pool.get("javassist.tools.rmi.Sample");
/*  87 */     this.forwardMethod = c.getDeclaredMethod("forward");
/*  88 */     this.forwardStaticMethod = c.getDeclaredMethod("forwardStatic");
/*     */     
/*  90 */     this
/*  91 */       .proxyConstructorParamTypes = pool.get(new String[] { "javassist.tools.rmi.ObjectImporter", "int" });
/*     */     
/*  93 */     this
/*  94 */       .interfacesForProxy = pool.get(new String[] { "java.io.Serializable", "javassist.tools.rmi.Proxy" });
/*     */     
/*  96 */     this
/*  97 */       .exceptionForProxy = new CtClass[] { pool.get("javassist.tools.rmi.RemoteException") };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onLoad(ClassPool pool, String classname) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProxyClass(String name) {
/* 115 */     return (this.proxyClasses.get(name) != null);
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
/*     */   public synchronized boolean makeProxyClass(Class<?> clazz) throws CannotCompileException, NotFoundException {
/* 130 */     String classname = clazz.getName();
/* 131 */     if (this.proxyClasses.get(classname) != null)
/* 132 */       return false; 
/* 133 */     CtClass ctclazz = produceProxyClass(this.classPool.get(classname), clazz);
/*     */     
/* 135 */     this.proxyClasses.put(classname, ctclazz);
/* 136 */     modifySuperclass(ctclazz);
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass produceProxyClass(CtClass orgclass, Class<?> orgRtClass) throws CannotCompileException, NotFoundException {
/* 143 */     int modify = orgclass.getModifiers();
/* 144 */     if (Modifier.isAbstract(modify) || Modifier.isNative(modify) || 
/* 145 */       !Modifier.isPublic(modify)) {
/* 146 */       throw new CannotCompileException(orgclass.getName() + " must be public, non-native, and non-abstract.");
/*     */     }
/*     */     
/* 149 */     CtClass proxy = this.classPool.makeClass(orgclass.getName(), orgclass
/* 150 */         .getSuperclass());
/*     */     
/* 152 */     proxy.setInterfaces(this.interfacesForProxy);
/*     */ 
/*     */     
/* 155 */     CtField f = new CtField(this.classPool.get("javassist.tools.rmi.ObjectImporter"), "importer", proxy);
/*     */     
/* 157 */     f.setModifiers(2);
/* 158 */     proxy.addField(f, CtField.Initializer.byParameter(0));
/*     */     
/* 160 */     f = new CtField(CtClass.intType, "objectId", proxy);
/* 161 */     f.setModifiers(2);
/* 162 */     proxy.addField(f, CtField.Initializer.byParameter(1));
/*     */     
/* 164 */     proxy.addMethod(CtNewMethod.getter("_getObjectId", f));
/*     */     
/* 166 */     proxy.addConstructor(CtNewConstructor.defaultConstructor(proxy));
/*     */     
/* 168 */     CtConstructor cons = CtNewConstructor.skeleton(this.proxyConstructorParamTypes, null, proxy);
/*     */     
/* 170 */     proxy.addConstructor(cons);
/*     */     
/*     */     try {
/* 173 */       addMethods(proxy, orgRtClass.getMethods());
/* 174 */       return proxy;
/*     */     }
/* 176 */     catch (SecurityException e) {
/* 177 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private CtClass toCtClass(Class<?> rtclass) throws NotFoundException {
/*     */     String name;
/* 183 */     if (!rtclass.isArray())
/* 184 */     { name = rtclass.getName(); }
/*     */     else
/* 186 */     { StringBuffer sbuf = new StringBuffer();
/*     */       while (true)
/* 188 */       { sbuf.append("[]");
/* 189 */         rtclass = rtclass.getComponentType();
/* 190 */         if (!rtclass.isArray())
/* 191 */         { sbuf.insert(0, rtclass.getName());
/* 192 */           name = sbuf.toString();
/*     */ 
/*     */           
/* 195 */           return this.classPool.get(name); }  }  }  return this.classPool.get(name);
/*     */   }
/*     */   
/*     */   private CtClass[] toCtClass(Class<?>[] rtclasses) throws NotFoundException {
/* 199 */     int n = rtclasses.length;
/* 200 */     CtClass[] ctclasses = new CtClass[n];
/* 201 */     for (int i = 0; i < n; i++) {
/* 202 */       ctclasses[i] = toCtClass(rtclasses[i]);
/*     */     }
/* 204 */     return ctclasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addMethods(CtClass proxy, Method[] ms) throws CannotCompileException, NotFoundException {
/* 214 */     for (int i = 0; i < ms.length; i++) {
/* 215 */       Method m = ms[i];
/* 216 */       int mod = m.getModifiers();
/* 217 */       if (m.getDeclaringClass() != Object.class && 
/* 218 */         !Modifier.isFinal(mod)) {
/* 219 */         if (Modifier.isPublic(mod)) {
/*     */           CtMethod body;
/* 221 */           if (Modifier.isStatic(mod)) {
/* 222 */             body = this.forwardStaticMethod;
/*     */           } else {
/* 224 */             body = this.forwardMethod;
/*     */           } 
/*     */           
/* 227 */           CtMethod wmethod = CtNewMethod.wrapped(toCtClass(m.getReturnType()), m
/* 228 */               .getName(), 
/* 229 */               toCtClass(m.getParameterTypes()), this.exceptionForProxy, body, 
/*     */ 
/*     */               
/* 232 */               CtMethod.ConstParameter.integer(i), proxy);
/*     */           
/* 234 */           wmethod.setModifiers(mod);
/* 235 */           proxy.addMethod(wmethod);
/*     */         }
/* 237 */         else if (!Modifier.isProtected(mod) && 
/* 238 */           !Modifier.isPrivate(mod)) {
/*     */           
/* 240 */           throw new CannotCompileException("the methods must be public, protected, or private.");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void modifySuperclass(CtClass orgclass) throws CannotCompileException, NotFoundException {
/* 252 */     for (;; orgclass = superclazz) {
/* 253 */       CtClass superclazz = orgclass.getSuperclass();
/* 254 */       if (superclazz == null) {
/*     */         break;
/*     */       }
/*     */       try {
/* 258 */         superclazz.getDeclaredConstructor(null);
/*     */         
/*     */         break;
/* 261 */       } catch (NotFoundException notFoundException) {
/*     */ 
/*     */         
/* 264 */         superclazz.addConstructor(
/* 265 */             CtNewConstructor.defaultConstructor(superclazz));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\rmi\StubGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */