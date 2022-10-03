/*     */ package javassist.util.proxy;
/*     */ 
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import javassist.CannotCompileException;
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
/*     */ public class DefinePackageHelper
/*     */ {
/*     */   private static abstract class Helper
/*     */   {
/*     */     private Helper() {}
/*     */     
/*     */     abstract Package definePackage(ClassLoader param1ClassLoader, String param1String1, String param1String2, String param1String3, String param1String4, String param1String5, String param1String6, String param1String7, URL param1URL) throws IllegalArgumentException;
/*     */   }
/*     */   
/*     */   private static class Java9
/*     */     extends Helper
/*     */   {
/*     */     private Java9() {}
/*     */     
/*     */     Package definePackage(ClassLoader loader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
/*  50 */       throw new RuntimeException("define package has been disabled for jigsaw");
/*     */     } }
/*     */   private static class Java7 extends Helper { private final SecurityActions stack; private final MethodHandle definePackage;
/*     */     
/*     */     private Java7() {
/*  55 */       this.stack = SecurityActions.stack;
/*  56 */       this.definePackage = getDefinePackageMethodHandle();
/*     */     }
/*     */     private MethodHandle getDefinePackageMethodHandle() {
/*  59 */       if (this.stack.getCallerClass() != getClass())
/*  60 */         throw new IllegalAccessError("Access denied for caller."); 
/*     */       try {
/*  62 */         return SecurityActions.getMethodHandle(ClassLoader.class, "definePackage", new Class[] { String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class });
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*  67 */       catch (NoSuchMethodException e) {
/*  68 */         throw new RuntimeException("cannot initialize", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Package definePackage(ClassLoader loader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
/*  78 */       if (this.stack.getCallerClass() != DefinePackageHelper.class)
/*  79 */         throw new IllegalAccessError("Access denied for caller."); 
/*     */       try {
/*  81 */         return (Package)this.definePackage.invokeWithArguments(new Object[] { loader, name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase });
/*     */       }
/*  83 */       catch (Throwable e) {
/*  84 */         if (e instanceof IllegalArgumentException) throw (IllegalArgumentException)e; 
/*  85 */         if (e instanceof RuntimeException) throw (RuntimeException)e;
/*     */         
/*  87 */         return null;
/*     */       } 
/*     */     } }
/*     */   private static class JavaOther extends Helper { private final SecurityActions stack;
/*     */     private JavaOther() {
/*  92 */       this.stack = SecurityActions.stack;
/*  93 */       this.definePackage = getDefinePackageMethod();
/*     */     } private final Method definePackage;
/*     */     private Method getDefinePackageMethod() {
/*  96 */       if (this.stack.getCallerClass() != getClass())
/*  97 */         throw new IllegalAccessError("Access denied for caller."); 
/*     */       try {
/*  99 */         return SecurityActions.getDeclaredMethod(ClassLoader.class, "definePackage", new Class[] { String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class });
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 104 */       catch (NoSuchMethodException e) {
/* 105 */         throw new RuntimeException("cannot initialize", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Package definePackage(ClassLoader loader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
/* 115 */       if (this.stack.getCallerClass() != DefinePackageHelper.class)
/* 116 */         throw new IllegalAccessError("Access denied for caller."); 
/*     */       try {
/* 118 */         this.definePackage.setAccessible(true);
/* 119 */         return (Package)this.definePackage.invoke(loader, new Object[] { name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase });
/*     */ 
/*     */       
/*     */       }
/* 123 */       catch (Throwable e) {
/* 124 */         if (e instanceof InvocationTargetException) {
/* 125 */           Throwable t = ((InvocationTargetException)e).getTargetException();
/* 126 */           if (t instanceof IllegalArgumentException)
/* 127 */             throw (IllegalArgumentException)t; 
/*     */         } 
/* 129 */         if (e instanceof RuntimeException) throw (RuntimeException)e;
/*     */         
/* 131 */         return null;
/*     */       } 
/*     */     } }
/*     */ 
/*     */   
/* 136 */   private static final Helper privileged = (ClassFile.MAJOR_VERSION >= 53) ? 
/* 137 */     new Java9() : ((ClassFile.MAJOR_VERSION >= 51) ? 
/* 138 */     new Java7() : new JavaOther());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void definePackage(String className, ClassLoader loader) throws CannotCompileException {
/*     */     try {
/* 165 */       privileged.definePackage(loader, className, null, null, null, null, null, null, null);
/*     */     
/*     */     }
/* 168 */     catch (IllegalArgumentException e) {
/*     */ 
/*     */       
/*     */       return;
/*     */     }
/* 173 */     catch (Exception e) {
/* 174 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\DefinePackageHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */