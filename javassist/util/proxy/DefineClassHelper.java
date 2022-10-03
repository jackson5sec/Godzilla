/*     */ package javassist.util.proxy;
/*     */ 
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.List;
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
/*     */ public class DefineClassHelper
/*     */ {
/*     */   private static abstract class Helper
/*     */   {
/*     */     private Helper() {}
/*     */     
/*     */     abstract Class<?> defineClass(String param1String, byte[] param1ArrayOfbyte, int param1Int1, int param1Int2, Class<?> param1Class, ClassLoader param1ClassLoader, ProtectionDomain param1ProtectionDomain) throws ClassFormatError, CannotCompileException;
/*     */   }
/*     */   
/*     */   private static class Java11
/*     */     extends JavaOther
/*     */   {
/*     */     private Java11() {}
/*     */     
/*     */     Class<?> defineClass(String name, byte[] bcode, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
/*  47 */       if (neighbor != null) {
/*  48 */         return DefineClassHelper.toClass(neighbor, bcode);
/*     */       }
/*     */ 
/*     */       
/*  52 */       return super.defineClass(name, bcode, off, len, neighbor, loader, protectionDomain);
/*     */     } }
/*     */   
/*     */   private static class Java9 extends Helper { private final Object stack;
/*     */     private final Method getCallerClass;
/*     */     
/*     */     final class ReferencedUnsafe {
/*     */       private final SecurityActions.TheUnsafe sunMiscUnsafeTheUnsafe;
/*     */       private final MethodHandle defineClass;
/*     */       
/*     */       ReferencedUnsafe(SecurityActions.TheUnsafe usf, MethodHandle meth) {
/*  63 */         this.sunMiscUnsafeTheUnsafe = usf;
/*  64 */         this.defineClass = meth;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
/*     */         try {
/*  72 */           if (DefineClassHelper.Java9.this.getCallerClass.invoke(DefineClassHelper.Java9.this.stack, new Object[0]) != DefineClassHelper.Java9.class)
/*  73 */             throw new IllegalAccessError("Access denied for caller."); 
/*  74 */         } catch (Exception e) {
/*  75 */           throw new RuntimeException("cannot initialize", e);
/*     */         } 
/*     */         try {
/*  78 */           return (Class)this.defineClass.invokeWithArguments(new Object[] { this.sunMiscUnsafeTheUnsafe.theUnsafe, name, b, 
/*     */                 
/*  80 */                 Integer.valueOf(off), Integer.valueOf(len), loader, protectionDomain });
/*  81 */         } catch (Throwable e) {
/*  82 */           if (e instanceof RuntimeException) throw (RuntimeException)e; 
/*  83 */           if (e instanceof ClassFormatError) throw (ClassFormatError)e; 
/*  84 */           throw new ClassFormatError(e.getMessage());
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  91 */     private final ReferencedUnsafe sunMiscUnsafe = getReferencedUnsafe();
/*     */     
/*     */     Java9() {
/*  94 */       Class<?> stackWalkerClass = null;
/*     */       try {
/*  96 */         stackWalkerClass = Class.forName("java.lang.StackWalker");
/*  97 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */       
/* 100 */       if (stackWalkerClass != null) {
/*     */         try {
/* 102 */           Class<?> optionClass = Class.forName("java.lang.StackWalker$Option");
/* 103 */           this
/*     */             
/* 105 */             .stack = stackWalkerClass.getMethod("getInstance", new Class[] { optionClass }).invoke(null, new Object[] { optionClass.getEnumConstants()[0] });
/* 106 */           this.getCallerClass = stackWalkerClass.getMethod("getCallerClass", new Class[0]);
/* 107 */         } catch (Throwable e) {
/* 108 */           throw new RuntimeException("cannot initialize", e);
/*     */         } 
/*     */       } else {
/* 111 */         this.stack = null;
/* 112 */         this.getCallerClass = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private final ReferencedUnsafe getReferencedUnsafe() {
/*     */       try {
/* 118 */         if (DefineClassHelper.privileged != null && this.getCallerClass.invoke(this.stack, new Object[0]) != getClass())
/* 119 */           throw new IllegalAccessError("Access denied for caller."); 
/* 120 */       } catch (Exception e) {
/* 121 */         throw new RuntimeException("cannot initialize", e);
/*     */       } 
/*     */       try {
/* 124 */         SecurityActions.TheUnsafe usf = SecurityActions.getSunMiscUnsafeAnonymously();
/* 125 */         List<Method> defineClassMethod = usf.methods.get("defineClass");
/*     */         
/* 127 */         if (null == defineClassMethod)
/* 128 */           return null; 
/* 129 */         MethodHandle meth = MethodHandles.lookup().unreflect(defineClassMethod.get(0));
/* 130 */         return new ReferencedUnsafe(usf, meth);
/* 131 */       } catch (Throwable e) {
/* 132 */         throw new RuntimeException("cannot initialize", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
/*     */       try {
/* 142 */         if (this.getCallerClass.invoke(this.stack, new Object[0]) != DefineClassHelper.class)
/* 143 */           throw new IllegalAccessError("Access denied for caller."); 
/* 144 */       } catch (Exception e) {
/* 145 */         throw new RuntimeException("cannot initialize", e);
/*     */       } 
/* 147 */       return this.sunMiscUnsafe.defineClass(name, b, off, len, loader, protectionDomain);
/*     */     } }
/*     */   private static class Java7 extends Helper { private final SecurityActions stack;
/*     */     private final MethodHandle defineClass;
/*     */     
/*     */     private Java7() {
/* 153 */       this.stack = SecurityActions.stack;
/* 154 */       this.defineClass = getDefineClassMethodHandle();
/*     */     } private final MethodHandle getDefineClassMethodHandle() {
/* 156 */       if (DefineClassHelper.privileged != null && this.stack.getCallerClass() != getClass())
/* 157 */         throw new IllegalAccessError("Access denied for caller."); 
/*     */       try {
/* 159 */         return SecurityActions.getMethodHandle(ClassLoader.class, "defineClass", new Class[] { String.class, byte[].class, int.class, int.class, ProtectionDomain.class });
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 164 */       catch (NoSuchMethodException e) {
/* 165 */         throw new RuntimeException("cannot initialize", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
/* 174 */       if (this.stack.getCallerClass() != DefineClassHelper.class)
/* 175 */         throw new IllegalAccessError("Access denied for caller."); 
/*     */       try {
/* 177 */         return (Class)this.defineClass.invokeWithArguments(new Object[] { loader, name, b, 
/* 178 */               Integer.valueOf(off), Integer.valueOf(len), protectionDomain });
/* 179 */       } catch (Throwable e) {
/* 180 */         if (e instanceof RuntimeException) throw (RuntimeException)e; 
/* 181 */         if (e instanceof ClassFormatError) throw (ClassFormatError)e; 
/* 182 */         throw new ClassFormatError(e.getMessage());
/*     */       } 
/*     */     } }
/*     */   private static class JavaOther extends Helper { private final Method defineClass;
/*     */     
/*     */     private JavaOther() {
/* 188 */       this.defineClass = getDefineClassMethod();
/* 189 */       this.stack = SecurityActions.stack;
/*     */     } private final SecurityActions stack;
/*     */     private final Method getDefineClassMethod() {
/* 192 */       if (DefineClassHelper.privileged != null && this.stack.getCallerClass() != getClass())
/* 193 */         throw new IllegalAccessError("Access denied for caller."); 
/*     */       try {
/* 195 */         return SecurityActions.getDeclaredMethod(ClassLoader.class, "defineClass", new Class[] { String.class, byte[].class, int.class, int.class, ProtectionDomain.class });
/*     */ 
/*     */       
/*     */       }
/* 199 */       catch (NoSuchMethodException e) {
/* 200 */         throw new RuntimeException("cannot initialize", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
/* 209 */       Class<?> klass = this.stack.getCallerClass();
/* 210 */       if (klass != DefineClassHelper.class && klass != getClass())
/* 211 */         throw new IllegalAccessError("Access denied for caller."); 
/*     */       try {
/* 213 */         SecurityActions.setAccessible(this.defineClass, true);
/* 214 */         return (Class)this.defineClass.invoke(loader, new Object[] { name, b, 
/* 215 */               Integer.valueOf(off), Integer.valueOf(len), protectionDomain });
/*     */       }
/* 217 */       catch (Throwable e) {
/* 218 */         if (e instanceof ClassFormatError) throw (ClassFormatError)e; 
/* 219 */         if (e instanceof RuntimeException) throw (RuntimeException)e; 
/* 220 */         throw new CannotCompileException(e);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 227 */   private static final Helper privileged = (ClassFile.MAJOR_VERSION > 54) ? 
/* 228 */     new Java11() : (
/* 229 */     (ClassFile.MAJOR_VERSION >= 53) ? 
/* 230 */     new Java9() : (
/* 231 */     (ClassFile.MAJOR_VERSION >= 51) ? new Java7() : new JavaOther()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> toClass(String className, Class<?> neighbor, ClassLoader loader, ProtectionDomain domain, byte[] bcode) throws CannotCompileException {
/*     */     try {
/* 260 */       return privileged.defineClass(className, bcode, 0, bcode.length, neighbor, loader, domain);
/*     */     
/*     */     }
/* 263 */     catch (RuntimeException e) {
/* 264 */       throw e;
/*     */     }
/* 266 */     catch (CannotCompileException e) {
/* 267 */       throw e;
/*     */     }
/* 269 */     catch (ClassFormatError e) {
/* 270 */       Throwable t = e.getCause();
/* 271 */       throw new CannotCompileException((t == null) ? e : t);
/*     */     }
/* 273 */     catch (Exception e) {
/* 274 */       throw new CannotCompileException(e);
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
/*     */   public static Class<?> toClass(Class<?> neighbor, byte[] bcode) throws CannotCompileException {
/*     */     try {
/* 292 */       DefineClassHelper.class.getModule().addReads(neighbor.getModule());
/* 293 */       MethodHandles.Lookup lookup = MethodHandles.lookup();
/* 294 */       MethodHandles.Lookup prvlookup = MethodHandles.privateLookupIn(neighbor, lookup);
/* 295 */       return prvlookup.defineClass(bcode);
/* 296 */     } catch (IllegalAccessException|IllegalArgumentException e) {
/* 297 */       throw new CannotCompileException(e.getMessage() + ": " + neighbor.getName() + " has no permission to define the class");
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
/*     */   public static Class<?> toClass(MethodHandles.Lookup lookup, byte[] bcode) throws CannotCompileException {
/*     */     try {
/* 314 */       return lookup.defineClass(bcode);
/* 315 */     } catch (IllegalAccessException|IllegalArgumentException e) {
/* 316 */       throw new CannotCompileException(e.getMessage());
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
/*     */   static Class<?> toPublicClass(String className, byte[] bcode) throws CannotCompileException {
/*     */     try {
/* 329 */       MethodHandles.Lookup lookup = MethodHandles.lookup();
/* 330 */       lookup = lookup.dropLookupMode(2);
/* 331 */       return lookup.defineClass(bcode);
/*     */     }
/* 333 */     catch (Throwable t) {
/* 334 */       throw new CannotCompileException(t);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\DefineClassHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */