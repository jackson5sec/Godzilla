/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectUtils
/*     */ {
/*     */   static {
/*     */     Method privateLookupIn, lookupDefineClass, classLoaderDefineClass;
/*     */   }
/*     */   
/*  53 */   private static final Map primitives = new HashMap<>(8);
/*     */   
/*  55 */   private static final Map transforms = new HashMap<>(8);
/*     */   
/*  57 */   private static final ClassLoader defaultLoader = ReflectUtils.class.getClassLoader();
/*     */ 
/*     */   
/*     */   private static final Method privateLookupInMethod;
/*     */   
/*     */   private static final Method lookupDefineClassMethod;
/*     */   
/*     */   private static final Method classLoaderDefineClassMethod;
/*     */   
/*     */   private static final Throwable THROWABLE;
/*     */   
/*     */   private static final ProtectionDomain PROTECTION_DOMAIN;
/*     */   
/*  70 */   private static final List<Method> OBJECT_METHODS = new ArrayList<>();
/*     */   
/*     */   private static final String[] CGLIB_PACKAGES;
/*     */ 
/*     */   
/*     */   static {
/*  76 */     Throwable throwable = null;
/*     */     try {
/*  78 */       privateLookupIn = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
/*     */             public Object run() throws Exception {
/*     */               try {
/*  81 */                 return MethodHandles.class.getMethod("privateLookupIn", new Class[] { Class.class, MethodHandles.Lookup.class });
/*     */               }
/*  83 */               catch (NoSuchMethodException ex) {
/*  84 */                 return null;
/*     */               } 
/*     */             }
/*     */           });
/*  88 */       lookupDefineClass = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
/*     */             public Object run() throws Exception {
/*     */               try {
/*  91 */                 return MethodHandles.Lookup.class.getMethod("defineClass", new Class[] { byte[].class });
/*     */               }
/*  93 */               catch (NoSuchMethodException ex) {
/*  94 */                 return null;
/*     */               } 
/*     */             }
/*     */           });
/*  98 */       classLoaderDefineClass = AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
/*     */             public Object run() throws Exception {
/* 100 */               return ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class, ProtectionDomain.class });
/*     */             }
/*     */           });
/*     */     
/*     */     }
/* 105 */     catch (Throwable t) {
/* 106 */       privateLookupIn = null;
/* 107 */       lookupDefineClass = null;
/* 108 */       classLoaderDefineClass = null;
/* 109 */       throwable = t;
/*     */     } 
/*     */     
/* 112 */     privateLookupInMethod = privateLookupIn;
/* 113 */     lookupDefineClassMethod = lookupDefineClass;
/* 114 */     classLoaderDefineClassMethod = classLoaderDefineClass;
/* 115 */     THROWABLE = throwable;
/* 116 */     PROTECTION_DOMAIN = getProtectionDomain(ReflectUtils.class);
/*     */     
/* 118 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Object run() {
/* 120 */             Method[] methods = Object.class.getDeclaredMethods();
/* 121 */             for (Method method : methods) {
/* 122 */               if (!"finalize".equals(method.getName()) && (method
/* 123 */                 .getModifiers() & 0x18) <= 0)
/*     */               {
/*     */                 
/* 126 */                 ReflectUtils.OBJECT_METHODS.add(method); } 
/*     */             } 
/* 128 */             return null;
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 134 */     CGLIB_PACKAGES = new String[] { "java.lang" };
/*     */ 
/*     */     
/* 137 */     primitives.put("byte", byte.class);
/* 138 */     primitives.put("char", char.class);
/* 139 */     primitives.put("double", double.class);
/* 140 */     primitives.put("float", float.class);
/* 141 */     primitives.put("int", int.class);
/* 142 */     primitives.put("long", long.class);
/* 143 */     primitives.put("short", short.class);
/* 144 */     primitives.put("boolean", boolean.class);
/*     */     
/* 146 */     transforms.put("byte", "B");
/* 147 */     transforms.put("char", "C");
/* 148 */     transforms.put("double", "D");
/* 149 */     transforms.put("float", "F");
/* 150 */     transforms.put("int", "I");
/* 151 */     transforms.put("long", "J");
/* 152 */     transforms.put("short", "S");
/* 153 */     transforms.put("boolean", "Z");
/*     */   }
/*     */   
/*     */   public static ProtectionDomain getProtectionDomain(final Class source) {
/* 157 */     if (source == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     return AccessController.<ProtectionDomain>doPrivileged(new PrivilegedAction<ProtectionDomain>() {
/*     */           public Object run() {
/* 162 */             return source.getProtectionDomain();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static Type[] getExceptionTypes(Member member) {
/* 168 */     if (member instanceof Method) {
/* 169 */       return TypeUtils.getTypes(((Method)member).getExceptionTypes());
/*     */     }
/* 171 */     if (member instanceof Constructor) {
/* 172 */       return TypeUtils.getTypes(((Constructor)member).getExceptionTypes());
/*     */     }
/*     */     
/* 175 */     throw new IllegalArgumentException("Cannot get exception types of a field");
/*     */   }
/*     */ 
/*     */   
/*     */   public static Signature getSignature(Member member) {
/* 180 */     if (member instanceof Method) {
/* 181 */       return new Signature(member.getName(), Type.getMethodDescriptor((Method)member));
/*     */     }
/* 183 */     if (member instanceof Constructor) {
/* 184 */       Type[] types = TypeUtils.getTypes(((Constructor)member).getParameterTypes());
/* 185 */       return new Signature("<init>", 
/* 186 */           Type.getMethodDescriptor(Type.VOID_TYPE, types));
/*     */     } 
/*     */ 
/*     */     
/* 190 */     throw new IllegalArgumentException("Cannot get signature of a field");
/*     */   }
/*     */ 
/*     */   
/*     */   public static Constructor findConstructor(String desc) {
/* 195 */     return findConstructor(desc, defaultLoader);
/*     */   }
/*     */   
/*     */   public static Constructor findConstructor(String desc, ClassLoader loader) {
/*     */     try {
/* 200 */       int lparen = desc.indexOf('(');
/* 201 */       String className = desc.substring(0, lparen).trim();
/* 202 */       return getClass(className, loader).getConstructor(parseTypes(desc, loader));
/*     */     }
/* 204 */     catch (ClassNotFoundException|NoSuchMethodException ex) {
/* 205 */       throw new CodeGenerationException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Method findMethod(String desc) {
/* 210 */     return findMethod(desc, defaultLoader);
/*     */   }
/*     */   
/*     */   public static Method findMethod(String desc, ClassLoader loader) {
/*     */     try {
/* 215 */       int lparen = desc.indexOf('(');
/* 216 */       int dot = desc.lastIndexOf('.', lparen);
/* 217 */       String className = desc.substring(0, dot).trim();
/* 218 */       String methodName = desc.substring(dot + 1, lparen).trim();
/* 219 */       return getClass(className, loader).getDeclaredMethod(methodName, parseTypes(desc, loader));
/*     */     }
/* 221 */     catch (ClassNotFoundException|NoSuchMethodException ex) {
/* 222 */       throw new CodeGenerationException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Class[] parseTypes(String desc, ClassLoader loader) throws ClassNotFoundException {
/* 227 */     int lparen = desc.indexOf('(');
/* 228 */     int rparen = desc.indexOf(')', lparen);
/* 229 */     List<String> params = new ArrayList();
/* 230 */     int start = lparen + 1;
/*     */     while (true) {
/* 232 */       int comma = desc.indexOf(',', start);
/* 233 */       if (comma < 0) {
/*     */         break;
/*     */       }
/* 236 */       params.add(desc.substring(start, comma).trim());
/* 237 */       start = comma + 1;
/*     */     } 
/* 239 */     if (start < rparen) {
/* 240 */       params.add(desc.substring(start, rparen).trim());
/*     */     }
/* 242 */     Class[] types = new Class[params.size()];
/* 243 */     for (int i = 0; i < types.length; i++) {
/* 244 */       types[i] = getClass(params.get(i), loader);
/*     */     }
/* 246 */     return types;
/*     */   }
/*     */   
/*     */   private static Class getClass(String className, ClassLoader loader) throws ClassNotFoundException {
/* 250 */     return getClass(className, loader, CGLIB_PACKAGES);
/*     */   }
/*     */   
/*     */   private static Class getClass(String className, ClassLoader loader, String[] packages) throws ClassNotFoundException {
/* 254 */     String save = className;
/* 255 */     int dimensions = 0;
/* 256 */     int index = 0;
/* 257 */     while ((index = className.indexOf("[]", index) + 1) > 0) {
/* 258 */       dimensions++;
/*     */     }
/* 260 */     StringBuilder brackets = new StringBuilder(className.length() - dimensions);
/* 261 */     for (int i = 0; i < dimensions; i++) {
/* 262 */       brackets.append('[');
/*     */     }
/* 264 */     className = className.substring(0, className.length() - 2 * dimensions);
/*     */     
/* 266 */     String prefix = (dimensions > 0) ? (brackets + "L") : "";
/* 267 */     String suffix = (dimensions > 0) ? ";" : "";
/*     */     try {
/* 269 */       return Class.forName(prefix + className + suffix, false, loader);
/*     */     }
/* 271 */     catch (ClassNotFoundException classNotFoundException) {
/*     */       
/* 273 */       for (int j = 0; j < packages.length; j++) {
/*     */         try {
/* 275 */           return Class.forName(prefix + packages[j] + '.' + className + suffix, false, loader);
/*     */         }
/* 277 */         catch (ClassNotFoundException classNotFoundException1) {}
/*     */       } 
/*     */       
/* 280 */       if (dimensions == 0) {
/* 281 */         Class c = (Class)primitives.get(className);
/* 282 */         if (c != null) {
/* 283 */           return c;
/*     */         }
/*     */       } else {
/*     */         
/* 287 */         String transform = (String)transforms.get(className);
/* 288 */         if (transform != null) {
/*     */           try {
/* 290 */             return Class.forName(brackets + transform, false, loader);
/*     */           }
/* 292 */           catch (ClassNotFoundException classNotFoundException1) {}
/*     */         }
/*     */       } 
/*     */       
/* 296 */       throw new ClassNotFoundException(save);
/*     */     } 
/*     */   }
/*     */   public static Object newInstance(Class type) {
/* 300 */     return newInstance(type, Constants.EMPTY_CLASS_ARRAY, null);
/*     */   }
/*     */   
/*     */   public static Object newInstance(Class type, Class[] parameterTypes, Object[] args) {
/* 304 */     return newInstance(getConstructor(type, parameterTypes), args);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object newInstance(Constructor cstruct, Object[] args) {
/* 309 */     boolean flag = cstruct.isAccessible();
/*     */     try {
/* 311 */       if (!flag) {
/* 312 */         cstruct.setAccessible(true);
/*     */       }
/* 314 */       Object result = cstruct.newInstance(args);
/* 315 */       return result;
/*     */     }
/* 317 */     catch (InstantiationException e) {
/* 318 */       throw new CodeGenerationException(e);
/*     */     }
/* 320 */     catch (IllegalAccessException e) {
/* 321 */       throw new CodeGenerationException(e);
/*     */     }
/* 323 */     catch (InvocationTargetException e) {
/* 324 */       throw new CodeGenerationException(e.getTargetException());
/*     */     } finally {
/*     */       
/* 327 */       if (!flag) {
/* 328 */         cstruct.setAccessible(flag);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Constructor getConstructor(Class type, Class[] parameterTypes) {
/*     */     try {
/* 335 */       Constructor constructor = type.getDeclaredConstructor(parameterTypes);
/* 336 */       if (System.getSecurityManager() != null) {
/* 337 */         AccessController.doPrivileged(() -> {
/*     */               constructor.setAccessible(true);
/*     */               
/*     */               return null;
/*     */             });
/*     */       } else {
/* 343 */         constructor.setAccessible(true);
/*     */       } 
/* 345 */       return constructor;
/*     */     }
/* 347 */     catch (NoSuchMethodException e) {
/* 348 */       throw new CodeGenerationException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String[] getNames(Class[] classes) {
/* 353 */     if (classes == null)
/* 354 */       return null; 
/* 355 */     String[] names = new String[classes.length];
/* 356 */     for (int i = 0; i < names.length; i++) {
/* 357 */       names[i] = classes[i].getName();
/*     */     }
/* 359 */     return names;
/*     */   }
/*     */   
/*     */   public static Class[] getClasses(Object[] objects) {
/* 363 */     Class[] classes = new Class[objects.length];
/* 364 */     for (int i = 0; i < objects.length; i++) {
/* 365 */       classes[i] = objects[i].getClass();
/*     */     }
/* 367 */     return classes;
/*     */   }
/*     */   
/*     */   public static Method findNewInstance(Class iface) {
/* 371 */     Method m = findInterfaceMethod(iface);
/* 372 */     if (!m.getName().equals("newInstance")) {
/* 373 */       throw new IllegalArgumentException(iface + " missing newInstance method");
/*     */     }
/* 375 */     return m;
/*     */   }
/*     */   
/*     */   public static Method[] getPropertyMethods(PropertyDescriptor[] properties, boolean read, boolean write) {
/* 379 */     Set<Method> methods = new HashSet();
/* 380 */     for (int i = 0; i < properties.length; i++) {
/* 381 */       PropertyDescriptor pd = properties[i];
/* 382 */       if (read) {
/* 383 */         methods.add(pd.getReadMethod());
/*     */       }
/* 385 */       if (write) {
/* 386 */         methods.add(pd.getWriteMethod());
/*     */       }
/*     */     } 
/* 389 */     methods.remove(null);
/* 390 */     return methods.<Method>toArray(new Method[methods.size()]);
/*     */   }
/*     */   
/*     */   public static PropertyDescriptor[] getBeanProperties(Class type) {
/* 394 */     return getPropertiesHelper(type, true, true);
/*     */   }
/*     */   
/*     */   public static PropertyDescriptor[] getBeanGetters(Class type) {
/* 398 */     return getPropertiesHelper(type, true, false);
/*     */   }
/*     */   
/*     */   public static PropertyDescriptor[] getBeanSetters(Class type) {
/* 402 */     return getPropertiesHelper(type, false, true);
/*     */   }
/*     */   
/*     */   private static PropertyDescriptor[] getPropertiesHelper(Class<?> type, boolean read, boolean write) {
/*     */     try {
/* 407 */       BeanInfo info = Introspector.getBeanInfo(type, Object.class);
/* 408 */       PropertyDescriptor[] all = info.getPropertyDescriptors();
/* 409 */       if (read && write) {
/* 410 */         return all;
/*     */       }
/* 412 */       List<PropertyDescriptor> properties = new ArrayList(all.length);
/* 413 */       for (int i = 0; i < all.length; i++) {
/* 414 */         PropertyDescriptor pd = all[i];
/* 415 */         if ((read && pd.getReadMethod() != null) || (write && pd
/* 416 */           .getWriteMethod() != null)) {
/* 417 */           properties.add(pd);
/*     */         }
/*     */       } 
/* 420 */       return properties.<PropertyDescriptor>toArray(new PropertyDescriptor[properties.size()]);
/*     */     }
/* 422 */     catch (IntrospectionException e) {
/* 423 */       throw new CodeGenerationException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findDeclaredMethod(Class type, String methodName, Class[] parameterTypes) throws NoSuchMethodException {
/* 431 */     Class cl = type;
/* 432 */     while (cl != null) {
/*     */       try {
/* 434 */         return cl.getDeclaredMethod(methodName, parameterTypes);
/*     */       }
/* 436 */       catch (NoSuchMethodException e) {
/* 437 */         cl = cl.getSuperclass();
/*     */       } 
/*     */     } 
/* 440 */     throw new NoSuchMethodException(methodName);
/*     */   }
/*     */   
/*     */   public static List addAllMethods(Class<Object> type, List<Method> list) {
/* 444 */     if (type == Object.class) {
/* 445 */       list.addAll(OBJECT_METHODS);
/*     */     } else {
/*     */       
/* 448 */       list.addAll(Arrays.asList(type.getDeclaredMethods()));
/*     */     } 
/* 450 */     Class<? super Object> superclass = type.getSuperclass();
/* 451 */     if (superclass != null) {
/* 452 */       addAllMethods(superclass, list);
/*     */     }
/* 454 */     Class[] interfaces = type.getInterfaces();
/* 455 */     for (int i = 0; i < interfaces.length; i++) {
/* 456 */       addAllMethods(interfaces[i], list);
/*     */     }
/*     */     
/* 459 */     return list;
/*     */   }
/*     */   
/*     */   public static List addAllInterfaces(Class type, List list) {
/* 463 */     Class superclass = type.getSuperclass();
/* 464 */     if (superclass != null) {
/* 465 */       list.addAll(Arrays.asList(type.getInterfaces()));
/* 466 */       addAllInterfaces(superclass, list);
/*     */     } 
/* 468 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method findInterfaceMethod(Class iface) {
/* 473 */     if (!iface.isInterface()) {
/* 474 */       throw new IllegalArgumentException(iface + " is not an interface");
/*     */     }
/* 476 */     Method[] methods = iface.getDeclaredMethods();
/* 477 */     if (methods.length != 1) {
/* 478 */       throw new IllegalArgumentException("expecting exactly 1 method in " + iface);
/*     */     }
/* 480 */     return methods[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
/* 485 */     return defineClass(className, b, loader, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain) throws Exception {
/* 491 */     return defineClass(className, b, loader, protectionDomain, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain, Class<?> contextClass) throws Exception {
/* 498 */     Class c = null;
/* 499 */     Throwable t = THROWABLE;
/*     */ 
/*     */     
/* 502 */     if (contextClass != null && contextClass.getClassLoader() == loader && privateLookupInMethod != null && lookupDefineClassMethod != null) {
/*     */       
/*     */       try {
/*     */         
/* 506 */         MethodHandles.Lookup lookup = (MethodHandles.Lookup)privateLookupInMethod.invoke((Object)null, new Object[] { contextClass, MethodHandles.lookup() });
/* 507 */         c = (Class)lookupDefineClassMethod.invoke(lookup, new Object[] { b });
/*     */       }
/* 509 */       catch (InvocationTargetException ex) {
/* 510 */         Throwable target = ex.getTargetException();
/* 511 */         if (target.getClass() != LinkageError.class && target.getClass() != IllegalArgumentException.class) {
/* 512 */           throw new CodeGenerationException(target);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 517 */         t = target;
/*     */       }
/* 519 */       catch (Throwable ex) {
/* 520 */         throw new CodeGenerationException(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 525 */     if (c == null) {
/* 526 */       if (protectionDomain == null) {
/* 527 */         protectionDomain = PROTECTION_DOMAIN;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 532 */         Method publicDefineClass = loader.getClass().getMethod("publicDefineClass", new Class[] { String.class, byte[].class, ProtectionDomain.class });
/*     */         
/* 534 */         c = (Class)publicDefineClass.invoke(loader, new Object[] { className, b, protectionDomain });
/*     */       }
/* 536 */       catch (InvocationTargetException ex) {
/* 537 */         if (!(ex.getTargetException() instanceof UnsupportedOperationException)) {
/* 538 */           throw new CodeGenerationException(ex.getTargetException());
/*     */         }
/*     */         
/* 541 */         t = ex.getTargetException();
/*     */       }
/* 543 */       catch (Throwable ex) {
/*     */         
/* 545 */         t = ex;
/*     */       } 
/*     */ 
/*     */       
/* 549 */       if (c == null && classLoaderDefineClassMethod != null) {
/* 550 */         Object[] args = { className, b, Integer.valueOf(0), Integer.valueOf(b.length), protectionDomain };
/*     */         try {
/* 552 */           if (!classLoaderDefineClassMethod.isAccessible()) {
/* 553 */             classLoaderDefineClassMethod.setAccessible(true);
/*     */           }
/* 555 */           c = (Class)classLoaderDefineClassMethod.invoke(loader, args);
/*     */         }
/* 557 */         catch (InvocationTargetException ex) {
/* 558 */           throw new CodeGenerationException(ex.getTargetException());
/*     */         }
/* 560 */         catch (Throwable ex) {
/*     */ 
/*     */           
/* 563 */           if (!ex.getClass().getName().endsWith("InaccessibleObjectException")) {
/* 564 */             throw new CodeGenerationException(ex);
/*     */           }
/* 566 */           t = ex;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 572 */     if (c == null && contextClass != null && contextClass.getClassLoader() != loader && privateLookupInMethod != null && lookupDefineClassMethod != null) {
/*     */       
/*     */       try {
/*     */         
/* 576 */         MethodHandles.Lookup lookup = (MethodHandles.Lookup)privateLookupInMethod.invoke((Object)null, new Object[] { contextClass, MethodHandles.lookup() });
/* 577 */         c = (Class)lookupDefineClassMethod.invoke(lookup, new Object[] { b });
/*     */       }
/* 579 */       catch (InvocationTargetException ex) {
/* 580 */         throw new CodeGenerationException(ex.getTargetException());
/*     */       }
/* 582 */       catch (Throwable ex) {
/* 583 */         throw new CodeGenerationException(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 588 */     if (c == null) {
/* 589 */       throw new CodeGenerationException(t);
/*     */     }
/*     */ 
/*     */     
/* 593 */     Class.forName(className, true, loader);
/* 594 */     return c;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int findPackageProtected(Class[] classes) {
/* 599 */     for (int i = 0; i < classes.length; i++) {
/* 600 */       if (!Modifier.isPublic(classes[i].getModifiers())) {
/* 601 */         return i;
/*     */       }
/*     */     } 
/* 604 */     return 0;
/*     */   }
/*     */   
/*     */   public static MethodInfo getMethodInfo(final Member member, final int modifiers) {
/* 608 */     final Signature sig = getSignature(member);
/* 609 */     return new MethodInfo() {
/*     */         private ClassInfo ci;
/*     */         
/*     */         public ClassInfo getClassInfo() {
/* 613 */           if (this.ci == null)
/* 614 */             this.ci = ReflectUtils.getClassInfo(member.getDeclaringClass()); 
/* 615 */           return this.ci;
/*     */         }
/*     */         
/*     */         public int getModifiers() {
/* 619 */           return modifiers;
/*     */         }
/*     */         
/*     */         public Signature getSignature() {
/* 623 */           return sig;
/*     */         }
/*     */         
/*     */         public Type[] getExceptionTypes() {
/* 627 */           return ReflectUtils.getExceptionTypes(member);
/*     */         }
/*     */         
/*     */         public Attribute getAttribute() {
/* 631 */           return null;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static MethodInfo getMethodInfo(Member member) {
/* 637 */     return getMethodInfo(member, member.getModifiers());
/*     */   }
/*     */   
/*     */   public static ClassInfo getClassInfo(final Class clazz) {
/* 641 */     final Type type = Type.getType(clazz);
/* 642 */     final Type sc = (clazz.getSuperclass() == null) ? null : Type.getType(clazz.getSuperclass());
/* 643 */     return new ClassInfo() {
/*     */         public Type getType() {
/* 645 */           return type;
/*     */         }
/*     */         public Type getSuperType() {
/* 648 */           return sc;
/*     */         }
/*     */         public Type[] getInterfaces() {
/* 651 */           return TypeUtils.getTypes(clazz.getInterfaces());
/*     */         }
/*     */         public int getModifiers() {
/* 654 */           return clazz.getModifiers();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Method[] findMethods(String[] namesAndDescriptors, Method[] methods) {
/* 661 */     Map<Object, Object> map = new HashMap<>();
/* 662 */     for (int i = 0; i < methods.length; i++) {
/* 663 */       Method method = methods[i];
/* 664 */       map.put(method.getName() + Type.getMethodDescriptor(method), method);
/*     */     } 
/* 666 */     Method[] result = new Method[namesAndDescriptors.length / 2];
/* 667 */     for (int j = 0; j < result.length; j++) {
/* 668 */       result[j] = (Method)map.get(namesAndDescriptors[j * 2] + namesAndDescriptors[j * 2 + 1]);
/* 669 */       if (result[j] == null);
/*     */     } 
/*     */ 
/*     */     
/* 673 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\ReflectUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */