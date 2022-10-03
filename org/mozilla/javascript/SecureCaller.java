/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.SecureClassLoader;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SecureCaller
/*     */ {
/*  27 */   private static final byte[] secureCallerImplBytecode = loadBytecode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  34 */   private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers = new WeakHashMap<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object callSecurely(final CodeSource codeSource, Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*     */     Map<ClassLoader, SoftReference<SecureCaller>> classLoaderMap;
/*     */     SecureCaller caller;
/*  47 */     final Thread thread = Thread.currentThread();
/*     */ 
/*     */     
/*  50 */     final ClassLoader classLoader = AccessController.<ClassLoader>doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/*  53 */             return thread.getContextClassLoader();
/*     */           }
/*     */         });
/*     */     
/*  57 */     synchronized (callers) {
/*     */       
/*  59 */       classLoaderMap = callers.get(codeSource);
/*  60 */       if (classLoaderMap == null) {
/*     */         
/*  62 */         classLoaderMap = new WeakHashMap<ClassLoader, SoftReference<SecureCaller>>();
/*  63 */         callers.put(codeSource, classLoaderMap);
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     synchronized (classLoaderMap) {
/*     */       
/*  69 */       SoftReference<SecureCaller> ref = classLoaderMap.get(classLoader);
/*  70 */       if (ref != null) {
/*  71 */         caller = ref.get();
/*     */       } else {
/*  73 */         caller = null;
/*     */       } 
/*  75 */       if (caller == null) {
/*     */         
/*     */         try {
/*     */ 
/*     */           
/*  80 */           caller = AccessController.<SecureCaller>doPrivileged(new PrivilegedExceptionAction()
/*     */               {
/*     */                 public Object run() throws Exception
/*     */                 {
/*     */                   ClassLoader effectiveClassLoader;
/*     */                   
/*  86 */                   Class<?> thisClass = getClass();
/*  87 */                   if (classLoader.loadClass(thisClass.getName()) != thisClass) {
/*  88 */                     effectiveClassLoader = thisClass.getClassLoader();
/*     */                   } else {
/*  90 */                     effectiveClassLoader = classLoader;
/*     */                   } 
/*  92 */                   SecureCaller.SecureClassLoaderImpl secCl = new SecureCaller.SecureClassLoaderImpl(effectiveClassLoader);
/*     */                   
/*  94 */                   Class<?> c = secCl.defineAndLinkClass(SecureCaller.class.getName() + "Impl", SecureCaller.secureCallerImplBytecode, codeSource);
/*     */ 
/*     */                   
/*  97 */                   return c.newInstance();
/*     */                 }
/*     */               });
/* 100 */           classLoaderMap.put(classLoader, new SoftReference<SecureCaller>(caller));
/*     */         }
/* 102 */         catch (PrivilegedActionException ex) {
/*     */           
/* 104 */           throw new UndeclaredThrowableException(ex.getCause());
/*     */         } 
/*     */       }
/*     */     } 
/* 108 */     return caller.call(callable, cx, scope, thisObj, args);
/*     */   }
/*     */   
/*     */   private static class SecureClassLoaderImpl
/*     */     extends SecureClassLoader
/*     */   {
/*     */     SecureClassLoaderImpl(ClassLoader parent) {
/* 115 */       super(parent);
/*     */     }
/*     */ 
/*     */     
/*     */     Class<?> defineAndLinkClass(String name, byte[] bytes, CodeSource cs) {
/* 120 */       Class<?> cl = defineClass(name, bytes, 0, bytes.length, cs);
/* 121 */       resolveClass(cl);
/* 122 */       return cl;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] loadBytecode() {
/* 128 */     return AccessController.<byte[]>doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run()
/*     */           {
/* 132 */             return SecureCaller.loadBytecodePrivileged();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] loadBytecodePrivileged() {
/* 139 */     URL url = SecureCaller.class.getResource("SecureCallerImpl.clazz");
/*     */     
/*     */     try {
/* 142 */       InputStream in = url.openStream();
/*     */       
/*     */       try {
/* 145 */         ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*     */         
/*     */         while (true) {
/* 148 */           int r = in.read();
/* 149 */           if (r == -1)
/*     */           {
/* 151 */             return bout.toByteArray();
/*     */           }
/* 153 */           bout.write(r);
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 158 */         in.close();
/*     */       }
/*     */     
/* 161 */     } catch (IOException e) {
/*     */       
/* 163 */       throw new UndeclaredThrowableException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract Object call(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\SecureCaller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */