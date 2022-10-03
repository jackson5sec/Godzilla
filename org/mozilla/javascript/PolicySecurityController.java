/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.SecureClassLoader;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import org.mozilla.classfile.ClassFileWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PolicySecurityController
/*     */   extends SecurityController
/*     */ {
/*  35 */   private static final byte[] secureCallerImplBytecode = loadBytecode();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers = new WeakHashMap<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>>();
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getStaticSecurityDomainClassInternal() {
/*  47 */     return CodeSource.class;
/*     */   }
/*     */   
/*     */   private static class Loader
/*     */     extends SecureClassLoader
/*     */     implements GeneratedClassLoader
/*     */   {
/*     */     private final CodeSource codeSource;
/*     */     
/*     */     Loader(ClassLoader parent, CodeSource codeSource) {
/*  57 */       super(parent);
/*  58 */       this.codeSource = codeSource;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> defineClass(String name, byte[] data) {
/*  63 */       return defineClass(name, data, 0, data.length, this.codeSource);
/*     */     }
/*     */ 
/*     */     
/*     */     public void linkClass(Class<?> cl) {
/*  68 */       resolveClass(cl);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneratedClassLoader createClassLoader(final ClassLoader parent, final Object securityDomain) {
/*  76 */     return AccessController.<Loader>doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           
/*     */           public Object run()
/*     */           {
/*  81 */             return new PolicySecurityController.Loader(parent, (CodeSource)securityDomain);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getDynamicSecurityDomain(Object securityDomain) {
/*  91 */     return securityDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object callWithDomain(Object securityDomain, final Context cx, Callable callable, Scriptable scope, Scriptable thisObj, Object[] args) {
/*     */     Map<ClassLoader, SoftReference<SecureCaller>> classLoaderMap;
/*     */     SecureCaller caller;
/* 101 */     final ClassLoader classLoader = AccessController.<ClassLoader>doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/* 104 */             return cx.getApplicationClassLoader();
/*     */           }
/*     */         });
/* 107 */     final CodeSource codeSource = (CodeSource)securityDomain;
/*     */     
/* 109 */     synchronized (callers) {
/* 110 */       classLoaderMap = callers.get(codeSource);
/* 111 */       if (classLoaderMap == null) {
/* 112 */         classLoaderMap = new WeakHashMap<ClassLoader, SoftReference<SecureCaller>>();
/* 113 */         callers.put(codeSource, classLoaderMap);
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     synchronized (classLoaderMap) {
/* 118 */       SoftReference<SecureCaller> ref = classLoaderMap.get(classLoader);
/* 119 */       if (ref != null) {
/* 120 */         caller = ref.get();
/*     */       } else {
/* 122 */         caller = null;
/*     */       } 
/* 124 */       if (caller == null) {
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */           
/* 130 */           caller = AccessController.<SecureCaller>doPrivileged(new PrivilegedExceptionAction()
/*     */               {
/*     */                 
/*     */                 public Object run() throws Exception
/*     */                 {
/* 135 */                   PolicySecurityController.Loader loader = new PolicySecurityController.Loader(classLoader, codeSource);
/*     */                   
/* 137 */                   Class<?> c = loader.defineClass(PolicySecurityController.SecureCaller.class.getName() + "Impl", PolicySecurityController.secureCallerImplBytecode);
/*     */ 
/*     */                   
/* 140 */                   return c.newInstance();
/*     */                 }
/*     */               });
/* 143 */           classLoaderMap.put(classLoader, new SoftReference<SecureCaller>(caller));
/*     */         }
/* 145 */         catch (PrivilegedActionException ex) {
/*     */           
/* 147 */           throw new UndeclaredThrowableException(ex.getCause());
/*     */         } 
/*     */       }
/*     */     } 
/* 151 */     return caller.call(callable, cx, scope, thisObj, args);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class SecureCaller
/*     */   {
/*     */     public abstract Object call(Callable param1Callable, Context param1Context, Scriptable param1Scriptable1, Scriptable param1Scriptable2, Object[] param1ArrayOfObject);
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] loadBytecode() {
/* 163 */     String secureCallerClassName = SecureCaller.class.getName();
/* 164 */     ClassFileWriter cfw = new ClassFileWriter(secureCallerClassName + "Impl", secureCallerClassName, "<generated>");
/*     */ 
/*     */     
/* 167 */     cfw.startMethod("<init>", "()V", (short)1);
/* 168 */     cfw.addALoad(0);
/* 169 */     cfw.addInvoke(183, secureCallerClassName, "<init>", "()V");
/*     */     
/* 171 */     cfw.add(177);
/* 172 */     cfw.stopMethod((short)1);
/* 173 */     String callableCallSig = "Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     cfw.startMethod("call", "(Lorg/mozilla/javascript/Callable;" + callableCallSig, (short)17);
/*     */ 
/*     */ 
/*     */     
/* 183 */     for (int i = 1; i < 6; i++) {
/* 184 */       cfw.addALoad(i);
/*     */     }
/* 186 */     cfw.addInvoke(185, "org/mozilla/javascript/Callable", "call", "(" + callableCallSig);
/*     */ 
/*     */     
/* 189 */     cfw.add(176);
/* 190 */     cfw.stopMethod((short)6);
/* 191 */     return cfw.toByteArray();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\PolicySecurityController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */