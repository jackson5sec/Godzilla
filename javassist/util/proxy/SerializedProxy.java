/*     */ package javassist.util.proxy;
/*     */ 
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SerializedProxy
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String superClass;
/*     */   private String[] interfaces;
/*     */   private byte[] filterSignature;
/*     */   private MethodHandler handler;
/*     */   
/*     */   SerializedProxy(Class<?> proxy, byte[] sig, MethodHandler h) {
/*  43 */     this.filterSignature = sig;
/*  44 */     this.handler = h;
/*  45 */     this.superClass = proxy.getSuperclass().getName();
/*  46 */     Class<?>[] infs = proxy.getInterfaces();
/*  47 */     int n = infs.length;
/*  48 */     this.interfaces = new String[n - 1];
/*  49 */     String setterInf = ProxyObject.class.getName();
/*  50 */     String setterInf2 = Proxy.class.getName();
/*  51 */     for (int i = 0; i < n; i++) {
/*  52 */       String name = infs[i].getName();
/*  53 */       if (!name.equals(setterInf) && !name.equals(setterInf2)) {
/*  54 */         this.interfaces[i] = name;
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
/*     */   protected Class<?> loadClass(final String className) throws ClassNotFoundException {
/*     */     try {
/*  67 */       return AccessController.<Class<?>>doPrivileged(new PrivilegedExceptionAction<Class<?>>()
/*     */           {
/*     */             public Class<?> run() throws Exception {
/*  70 */               ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  71 */               return Class.forName(className, true, cl);
/*     */             }
/*     */           });
/*     */     }
/*  75 */     catch (PrivilegedActionException pae) {
/*  76 */       throw new RuntimeException("cannot load the class: " + className, pae.getException());
/*     */     } 
/*     */   }
/*     */   
/*     */   Object readResolve() throws ObjectStreamException {
/*     */     try {
/*  82 */       int n = this.interfaces.length;
/*  83 */       Class<?>[] infs = new Class[n];
/*  84 */       for (int i = 0; i < n; i++) {
/*  85 */         infs[i] = loadClass(this.interfaces[i]);
/*     */       }
/*  87 */       ProxyFactory f = new ProxyFactory();
/*  88 */       f.setSuperclass(loadClass(this.superClass));
/*  89 */       f.setInterfaces(infs);
/*  90 */       Proxy proxy = f.createClass(this.filterSignature).getConstructor(new Class[0]).newInstance(new Object[0]);
/*  91 */       proxy.setHandler(this.handler);
/*  92 */       return proxy;
/*     */     }
/*  94 */     catch (NoSuchMethodException e) {
/*  95 */       throw new InvalidClassException(e.getMessage());
/*     */     }
/*  97 */     catch (InvocationTargetException e) {
/*  98 */       throw new InvalidClassException(e.getMessage());
/*     */     }
/* 100 */     catch (ClassNotFoundException e) {
/* 101 */       throw new InvalidClassException(e.getMessage());
/*     */     }
/* 103 */     catch (InstantiationException e2) {
/* 104 */       throw new InvalidObjectException(e2.getMessage());
/*     */     }
/* 106 */     catch (IllegalAccessException e3) {
/* 107 */       throw new InvalidClassException(e3.getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\SerializedProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */