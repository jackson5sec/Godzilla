/*     */ package org.springframework.cglib.proxy;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.cglib.core.AbstractClassGenerator;
/*     */ import org.springframework.cglib.core.CodeGenerationException;
/*     */ import org.springframework.cglib.core.GeneratorStrategy;
/*     */ import org.springframework.cglib.core.NamingPolicy;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.reflect.FastClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodProxy
/*     */ {
/*     */   private Signature sig1;
/*     */   private Signature sig2;
/*     */   private CreateInfo createInfo;
/*  45 */   private final Object initLock = new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile FastClassInfo fastClassInfo;
/*     */ 
/*     */ 
/*     */   
/*     */   public static MethodProxy create(Class c1, Class c2, String desc, String name1, String name2) {
/*  54 */     MethodProxy proxy = new MethodProxy();
/*  55 */     proxy.sig1 = new Signature(name1, desc);
/*  56 */     proxy.sig2 = new Signature(name2, desc);
/*  57 */     proxy.createInfo = new CreateInfo(c1, c2);
/*  58 */     return proxy;
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
/*     */   private void init() {
/*  70 */     if (this.fastClassInfo == null) {
/*  71 */       synchronized (this.initLock) {
/*  72 */         if (this.fastClassInfo == null) {
/*  73 */           CreateInfo ci = this.createInfo;
/*     */           
/*  75 */           FastClassInfo fci = new FastClassInfo();
/*  76 */           fci.f1 = helper(ci, ci.c1);
/*  77 */           fci.f2 = helper(ci, ci.c2);
/*  78 */           fci.i1 = fci.f1.getIndex(this.sig1);
/*  79 */           fci.i2 = fci.f2.getIndex(this.sig2);
/*  80 */           this.fastClassInfo = fci;
/*  81 */           this.createInfo = null;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class FastClassInfo
/*     */   {
/*     */     FastClass f1;
/*     */     
/*     */     FastClass f2;
/*     */     
/*     */     int i1;
/*     */     
/*     */     int i2;
/*     */ 
/*     */     
/*     */     private FastClassInfo() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class CreateInfo
/*     */   {
/*     */     Class c1;
/*     */     
/*     */     Class c2;
/*     */     NamingPolicy namingPolicy;
/*     */     GeneratorStrategy strategy;
/*     */     boolean attemptLoad;
/*     */     
/*     */     public CreateInfo(Class c1, Class c2) {
/* 113 */       this.c1 = c1;
/* 114 */       this.c2 = c2;
/* 115 */       AbstractClassGenerator fromEnhancer = AbstractClassGenerator.getCurrent();
/* 116 */       if (fromEnhancer != null) {
/* 117 */         this.namingPolicy = fromEnhancer.getNamingPolicy();
/* 118 */         this.strategy = fromEnhancer.getStrategy();
/* 119 */         this.attemptLoad = fromEnhancer.getAttemptLoad();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static FastClass helper(CreateInfo ci, Class type) {
/* 126 */     FastClass.Generator g = new FastClass.Generator();
/* 127 */     g.setType(type);
/*     */     
/* 129 */     g.setContextClass(type);
/*     */     
/* 131 */     g.setClassLoader(ci.c2.getClassLoader());
/* 132 */     g.setNamingPolicy(ci.namingPolicy);
/* 133 */     g.setStrategy(ci.strategy);
/* 134 */     g.setAttemptLoad(ci.attemptLoad);
/* 135 */     return g.create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Signature getSignature() {
/* 145 */     return this.sig1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSuperName() {
/* 155 */     return this.sig2.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSuperIndex() {
/* 166 */     init();
/* 167 */     return this.fastClassInfo.i2;
/*     */   }
/*     */ 
/*     */   
/*     */   FastClass getFastClass() {
/* 172 */     init();
/* 173 */     return this.fastClassInfo.f1;
/*     */   }
/*     */ 
/*     */   
/*     */   FastClass getSuperFastClass() {
/* 178 */     init();
/* 179 */     return this.fastClassInfo.f2;
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
/*     */   public static MethodProxy find(Class type, Signature sig) {
/*     */     try {
/* 192 */       Method m = type.getDeclaredMethod("CGLIB$findMethodProxy", MethodInterceptorGenerator.FIND_PROXY_TYPES);
/*     */       
/* 194 */       return (MethodProxy)m.invoke(null, new Object[] { sig });
/*     */     }
/* 196 */     catch (NoSuchMethodException ex) {
/* 197 */       throw new IllegalArgumentException("Class " + type + " does not use a MethodInterceptor");
/*     */     }
/* 199 */     catch (IllegalAccessException|InvocationTargetException ex) {
/* 200 */       throw new CodeGenerationException(ex);
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
/*     */   public Object invoke(Object obj, Object[] args) throws Throwable {
/*     */     try {
/* 216 */       init();
/* 217 */       FastClassInfo fci = this.fastClassInfo;
/* 218 */       return fci.f1.invoke(fci.i1, obj, args);
/*     */     }
/* 220 */     catch (InvocationTargetException ex) {
/* 221 */       throw ex.getTargetException();
/*     */     }
/* 223 */     catch (IllegalArgumentException ex) {
/* 224 */       if (this.fastClassInfo.i1 < 0)
/* 225 */         throw new IllegalArgumentException("Protected method: " + this.sig1); 
/* 226 */       throw ex;
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
/*     */   public Object invokeSuper(Object obj, Object[] args) throws Throwable {
/*     */     try {
/* 242 */       init();
/* 243 */       FastClassInfo fci = this.fastClassInfo;
/* 244 */       return fci.f2.invoke(fci.i2, obj, args);
/*     */     }
/* 246 */     catch (InvocationTargetException e) {
/* 247 */       throw e.getTargetException();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\MethodProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */