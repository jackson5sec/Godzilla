/*     */ package javassist.util.proxy;
/*     */ 
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ class SecurityActions
/*     */   extends SecurityManager
/*     */ {
/*  38 */   public static final SecurityActions stack = new SecurityActions();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getCallerClass() {
/*  54 */     return getClassContext()[2];
/*     */   }
/*     */ 
/*     */   
/*     */   static Method[] getDeclaredMethods(final Class<?> clazz) {
/*  59 */     if (System.getSecurityManager() == null) {
/*  60 */       return clazz.getDeclaredMethods();
/*     */     }
/*  62 */     return AccessController.<Method[]>doPrivileged((PrivilegedAction)new PrivilegedAction<Method[]>()
/*     */         {
/*     */           public Method[] run() {
/*  65 */             return clazz.getDeclaredMethods();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Constructor<?>[] getDeclaredConstructors(final Class<?> clazz) {
/*  73 */     if (System.getSecurityManager() == null) {
/*  74 */       return clazz.getDeclaredConstructors();
/*     */     }
/*  76 */     return (Constructor<?>[])AccessController.<Constructor[]>doPrivileged((PrivilegedAction)new PrivilegedAction<Constructor<?>[]>()
/*     */         {
/*     */           public Constructor<?>[] run() {
/*  79 */             return clazz.getDeclaredConstructors();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MethodHandle getMethodHandle(final Class<?> clazz, final String name, final Class<?>[] params) throws NoSuchMethodException {
/*     */     try {
/*  89 */       return AccessController.<MethodHandle>doPrivileged(new PrivilegedExceptionAction<MethodHandle>()
/*     */           {
/*     */             public MethodHandle run() throws IllegalAccessException, NoSuchMethodException, SecurityException
/*     */             {
/*  93 */               Method rmet = clazz.getDeclaredMethod(name, params);
/*  94 */               rmet.setAccessible(true);
/*  95 */               MethodHandle meth = MethodHandles.lookup().unreflect(rmet);
/*  96 */               rmet.setAccessible(false);
/*  97 */               return meth;
/*     */             }
/*     */           });
/*     */     }
/* 101 */     catch (PrivilegedActionException e) {
/* 102 */       if (e.getCause() instanceof NoSuchMethodException)
/* 103 */         throw (NoSuchMethodException)e.getCause(); 
/* 104 */       throw new RuntimeException(e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Method getDeclaredMethod(final Class<?> clazz, final String name, final Class<?>[] types) throws NoSuchMethodException {
/* 111 */     if (System.getSecurityManager() == null) {
/* 112 */       return clazz.getDeclaredMethod(name, types);
/*     */     }
/*     */     try {
/* 115 */       return AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>()
/*     */           {
/*     */             public Method run() throws Exception {
/* 118 */               return clazz.getDeclaredMethod(name, types);
/*     */             }
/*     */           });
/*     */     }
/* 122 */     catch (PrivilegedActionException e) {
/* 123 */       if (e.getCause() instanceof NoSuchMethodException) {
/* 124 */         throw (NoSuchMethodException)e.getCause();
/*     */       }
/* 126 */       throw new RuntimeException(e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Constructor<?> getDeclaredConstructor(final Class<?> clazz, final Class<?>[] types) throws NoSuchMethodException {
/* 135 */     if (System.getSecurityManager() == null) {
/* 136 */       return clazz.getDeclaredConstructor(types);
/*     */     }
/*     */     try {
/* 139 */       return AccessController.<Constructor>doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<Constructor<?>>()
/*     */           {
/*     */             public Constructor<?> run() throws Exception {
/* 142 */               return clazz.getDeclaredConstructor(types);
/*     */             }
/*     */           });
/*     */     }
/* 146 */     catch (PrivilegedActionException e) {
/* 147 */       if (e.getCause() instanceof NoSuchMethodException) {
/* 148 */         throw (NoSuchMethodException)e.getCause();
/*     */       }
/* 150 */       throw new RuntimeException(e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void setAccessible(final AccessibleObject ao, final boolean accessible) {
/* 158 */     if (System.getSecurityManager() == null) {
/* 159 */       ao.setAccessible(accessible);
/*     */     } else {
/* 161 */       AccessController.doPrivileged(new PrivilegedAction<Void>() {
/*     */             public Void run() {
/* 163 */               ao.setAccessible(accessible);
/* 164 */               return null;
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void set(final Field fld, final Object target, final Object value) throws IllegalAccessException {
/* 173 */     if (System.getSecurityManager() == null) {
/* 174 */       fld.set(target, value);
/*     */     } else {
/*     */       try {
/* 177 */         AccessController.doPrivileged(new PrivilegedExceptionAction<Void>()
/*     */             {
/*     */               public Void run() throws Exception {
/* 180 */                 fld.set(target, value);
/* 181 */                 return null;
/*     */               }
/*     */             });
/*     */       }
/* 185 */       catch (PrivilegedActionException e) {
/* 186 */         if (e.getCause() instanceof NoSuchMethodException)
/* 187 */           throw (IllegalAccessException)e.getCause(); 
/* 188 */         throw new RuntimeException(e.getCause());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static TheUnsafe getSunMiscUnsafeAnonymously() throws ClassNotFoundException {
/*     */     try {
/* 196 */       return AccessController.<TheUnsafe>doPrivileged(new PrivilegedExceptionAction<TheUnsafe>()
/*     */           {
/*     */             public SecurityActions.TheUnsafe run() throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
/*     */             {
/* 200 */               Class<?> unsafe = Class.forName("sun.misc.Unsafe");
/* 201 */               Field theUnsafe = unsafe.getDeclaredField("theUnsafe");
/* 202 */               theUnsafe.setAccessible(true);
/* 203 */               Objects.requireNonNull(SecurityActions.stack); SecurityActions.TheUnsafe usf = new SecurityActions.TheUnsafe(unsafe, theUnsafe.get((Object)null));
/* 204 */               theUnsafe.setAccessible(false);
/* 205 */               SecurityActions.disableWarning(usf);
/* 206 */               return usf;
/*     */             }
/*     */           });
/*     */     }
/* 210 */     catch (PrivilegedActionException e) {
/* 211 */       if (e.getCause() instanceof ClassNotFoundException)
/* 212 */         throw (ClassNotFoundException)e.getCause(); 
/* 213 */       if (e.getCause() instanceof NoSuchFieldException)
/* 214 */         throw new ClassNotFoundException("No such instance.", e.getCause()); 
/* 215 */       if (e.getCause() instanceof IllegalAccessException || e
/* 216 */         .getCause() instanceof IllegalAccessException || e
/* 217 */         .getCause() instanceof SecurityException)
/* 218 */         throw new ClassNotFoundException("Security denied access.", e.getCause()); 
/* 219 */       throw new RuntimeException(e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class TheUnsafe
/*     */   {
/*     */     final Class<?> unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final Object theUnsafe;
/*     */ 
/*     */ 
/*     */     
/* 238 */     final Map<String, List<Method>> methods = new HashMap<>();
/*     */ 
/*     */ 
/*     */     
/*     */     TheUnsafe(Class<?> c, Object o) {
/* 243 */       this.unsafe = c;
/* 244 */       this.theUnsafe = o;
/* 245 */       for (Method m : this.unsafe.getDeclaredMethods()) {
/* 246 */         if (!this.methods.containsKey(m.getName())) {
/* 247 */           this.methods.put(m.getName(), Collections.singletonList(m));
/*     */         } else {
/*     */           
/* 250 */           if (((List)this.methods.get(m.getName())).size() == 1)
/* 251 */             this.methods.put(m.getName(), new ArrayList<>(this.methods
/* 252 */                   .get(m.getName()))); 
/* 253 */           ((List<Method>)this.methods.get(m.getName())).add(m);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private Method getM(String name, Object[] o) {
/* 259 */       return ((List<Method>)this.methods.get(name)).get(0);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object call(String name, Object... args) {
/*     */       
/* 265 */       try { return getM(name, args).invoke(this.theUnsafe, args); }
/* 266 */       catch (Throwable t) { t.printStackTrace();
/* 267 */         return null; }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void disableWarning(TheUnsafe tu) {
/*     */     try {
/* 278 */       if (ClassFile.MAJOR_VERSION < 53)
/*     */         return; 
/* 280 */       Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
/* 281 */       Field logger = cls.getDeclaredField("logger");
/* 282 */       tu.call("putObjectVolatile", new Object[] { cls, tu.call("staticFieldOffset", new Object[] { logger }), null });
/* 283 */     } catch (Exception exception) {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassis\\util\proxy\SecurityActions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */