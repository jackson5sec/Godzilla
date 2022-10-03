/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.beans.Introspector;
/*      */ import java.io.Closeable;
/*      */ import java.io.Externalizable;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.StringJoiner;
/*      */ import org.springframework.lang.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class ClassUtils
/*      */ {
/*      */   public static final String ARRAY_SUFFIX = "[]";
/*      */   private static final String INTERNAL_ARRAY_PREFIX = "[";
/*      */   private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
/*   69 */   private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char PACKAGE_SEPARATOR = '.';
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char PATH_SEPARATOR = '/';
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char NESTED_CLASS_SEPARATOR = '$';
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String CGLIB_CLASS_SEPARATOR = "$$";
/*      */ 
/*      */   
/*      */   public static final String CLASS_FILE_SUFFIX = ".class";
/*      */ 
/*      */   
/*   91 */   private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(9);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   97 */   private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(9);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  103 */   private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<>(32);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  109 */   private static final Map<String, Class<?>> commonClassCache = new HashMap<>(64);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final Set<Class<?>> javaLanguageInterfaces;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  120 */   private static final Map<Method, Method> interfaceMethodCache = new ConcurrentReferenceHashMap<>(256);
/*      */ 
/*      */   
/*      */   static {
/*  124 */     primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
/*  125 */     primitiveWrapperTypeMap.put(Byte.class, byte.class);
/*  126 */     primitiveWrapperTypeMap.put(Character.class, char.class);
/*  127 */     primitiveWrapperTypeMap.put(Double.class, double.class);
/*  128 */     primitiveWrapperTypeMap.put(Float.class, float.class);
/*  129 */     primitiveWrapperTypeMap.put(Integer.class, int.class);
/*  130 */     primitiveWrapperTypeMap.put(Long.class, long.class);
/*  131 */     primitiveWrapperTypeMap.put(Short.class, short.class);
/*  132 */     primitiveWrapperTypeMap.put(Void.class, void.class);
/*      */ 
/*      */     
/*  135 */     for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
/*  136 */       primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
/*  137 */       registerCommonClasses(new Class[] { entry.getKey() });
/*      */     } 
/*      */     
/*  140 */     Set<Class<?>> primitiveTypes = new HashSet<>(32);
/*  141 */     primitiveTypes.addAll(primitiveWrapperTypeMap.values());
/*  142 */     Collections.addAll(primitiveTypes, new Class[] { boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class });
/*      */     
/*  144 */     for (Class<?> primitiveType : primitiveTypes) {
/*  145 */       primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
/*      */     }
/*      */     
/*  148 */     registerCommonClasses(new Class[] { Boolean[].class, Byte[].class, Character[].class, Double[].class, Float[].class, Integer[].class, Long[].class, Short[].class });
/*      */     
/*  150 */     registerCommonClasses(new Class[] { Number.class, Number[].class, String.class, String[].class, Class.class, Class[].class, Object.class, Object[].class });
/*      */     
/*  152 */     registerCommonClasses(new Class[] { Throwable.class, Exception.class, RuntimeException.class, Error.class, StackTraceElement.class, StackTraceElement[].class });
/*      */     
/*  154 */     registerCommonClasses(new Class[] { Enum.class, Iterable.class, Iterator.class, Enumeration.class, Collection.class, List.class, Set.class, Map.class, Map.Entry.class, Optional.class });
/*      */ 
/*      */     
/*  157 */     Class<?>[] javaLanguageInterfaceArray = new Class[] { Serializable.class, Externalizable.class, Closeable.class, AutoCloseable.class, Cloneable.class, Comparable.class };
/*      */     
/*  159 */     registerCommonClasses(javaLanguageInterfaceArray);
/*  160 */     javaLanguageInterfaces = new HashSet<>(Arrays.asList(javaLanguageInterfaceArray));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void registerCommonClasses(Class<?>... commonClasses) {
/*  168 */     for (Class<?> clazz : commonClasses) {
/*  169 */       commonClassCache.put(clazz.getName(), clazz);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static ClassLoader getDefaultClassLoader() {
/*  189 */     ClassLoader cl = null;
/*      */     try {
/*  191 */       cl = Thread.currentThread().getContextClassLoader();
/*      */     }
/*  193 */     catch (Throwable throwable) {}
/*      */ 
/*      */     
/*  196 */     if (cl == null) {
/*      */       
/*  198 */       cl = ClassUtils.class.getClassLoader();
/*  199 */       if (cl == null) {
/*      */         
/*      */         try {
/*  202 */           cl = ClassLoader.getSystemClassLoader();
/*      */         }
/*  204 */         catch (Throwable throwable) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  209 */     return cl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static ClassLoader overrideThreadContextClassLoader(@Nullable ClassLoader classLoaderToUse) {
/*  221 */     Thread currentThread = Thread.currentThread();
/*  222 */     ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
/*  223 */     if (classLoaderToUse != null && !classLoaderToUse.equals(threadContextClassLoader)) {
/*  224 */       currentThread.setContextClassLoader(classLoaderToUse);
/*  225 */       return threadContextClassLoader;
/*      */     } 
/*      */     
/*  228 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> forName(String name, @Nullable ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
/*  248 */     Assert.notNull(name, "Name must not be null");
/*      */     
/*  250 */     Class<?> clazz = resolvePrimitiveClassName(name);
/*  251 */     if (clazz == null) {
/*  252 */       clazz = commonClassCache.get(name);
/*      */     }
/*  254 */     if (clazz != null) {
/*  255 */       return clazz;
/*      */     }
/*      */ 
/*      */     
/*  259 */     if (name.endsWith("[]")) {
/*  260 */       String elementClassName = name.substring(0, name.length() - "[]".length());
/*  261 */       Class<?> elementClass = forName(elementClassName, classLoader);
/*  262 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     } 
/*      */ 
/*      */     
/*  266 */     if (name.startsWith("[L") && name.endsWith(";")) {
/*  267 */       String elementName = name.substring("[L".length(), name.length() - 1);
/*  268 */       Class<?> elementClass = forName(elementName, classLoader);
/*  269 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     } 
/*      */ 
/*      */     
/*  273 */     if (name.startsWith("[")) {
/*  274 */       String elementName = name.substring("[".length());
/*  275 */       Class<?> elementClass = forName(elementName, classLoader);
/*  276 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     } 
/*      */     
/*  279 */     ClassLoader clToUse = classLoader;
/*  280 */     if (clToUse == null) {
/*  281 */       clToUse = getDefaultClassLoader();
/*      */     }
/*      */     try {
/*  284 */       return Class.forName(name, false, clToUse);
/*      */     }
/*  286 */     catch (ClassNotFoundException ex) {
/*  287 */       int lastDotIndex = name.lastIndexOf('.');
/*  288 */       if (lastDotIndex != -1) {
/*      */         
/*  290 */         String nestedClassName = name.substring(0, lastDotIndex) + '$' + name.substring(lastDotIndex + 1);
/*      */         try {
/*  292 */           return Class.forName(nestedClassName, false, clToUse);
/*      */         }
/*  294 */         catch (ClassNotFoundException classNotFoundException) {}
/*      */       } 
/*      */ 
/*      */       
/*  298 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> resolveClassName(String className, @Nullable ClassLoader classLoader) throws IllegalArgumentException {
/*      */     try {
/*  324 */       return forName(className, classLoader);
/*      */     }
/*  326 */     catch (IllegalAccessError err) {
/*  327 */       throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" + className + "]: " + err
/*  328 */           .getMessage(), err);
/*      */     }
/*  330 */     catch (LinkageError err) {
/*  331 */       throw new IllegalArgumentException("Unresolvable class definition for class [" + className + "]", err);
/*      */     }
/*  333 */     catch (ClassNotFoundException ex) {
/*  334 */       throw new IllegalArgumentException("Could not find class [" + className + "]", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPresent(String className, @Nullable ClassLoader classLoader) {
/*      */     try {
/*  354 */       forName(className, classLoader);
/*  355 */       return true;
/*      */     }
/*  357 */     catch (IllegalAccessError err) {
/*  358 */       throw new IllegalStateException("Readability mismatch in inheritance hierarchy of class [" + className + "]: " + err
/*  359 */           .getMessage(), err);
/*      */     }
/*  361 */     catch (Throwable ex) {
/*      */       
/*  363 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isVisible(Class<?> clazz, @Nullable ClassLoader classLoader) {
/*  374 */     if (classLoader == null) {
/*  375 */       return true;
/*      */     }
/*      */     try {
/*  378 */       if (clazz.getClassLoader() == classLoader) {
/*  379 */         return true;
/*      */       }
/*      */     }
/*  382 */     catch (SecurityException securityException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  387 */     return isLoadable(clazz, classLoader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isCacheSafe(Class<?> clazz, @Nullable ClassLoader classLoader) {
/*  398 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/*  400 */       ClassLoader target = clazz.getClassLoader();
/*      */       
/*  402 */       if (target == classLoader || target == null) {
/*  403 */         return true;
/*      */       }
/*  405 */       if (classLoader == null) {
/*  406 */         return false;
/*      */       }
/*      */       
/*  409 */       ClassLoader current = classLoader;
/*  410 */       while (current != null) {
/*  411 */         current = current.getParent();
/*  412 */         if (current == target) {
/*  413 */           return true;
/*      */         }
/*      */       } 
/*      */       
/*  417 */       while (target != null) {
/*  418 */         target = target.getParent();
/*  419 */         if (target == classLoader) {
/*  420 */           return false;
/*      */         }
/*      */       }
/*      */     
/*  424 */     } catch (SecurityException securityException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  430 */     return (classLoader != null && isLoadable(clazz, classLoader));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isLoadable(Class<?> clazz, ClassLoader classLoader) {
/*      */     try {
/*  441 */       return (clazz == classLoader.loadClass(clazz.getName()));
/*      */     
/*      */     }
/*  444 */     catch (ClassNotFoundException ex) {
/*      */       
/*  446 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Class<?> resolvePrimitiveClassName(@Nullable String name) {
/*  462 */     Class<?> result = null;
/*      */ 
/*      */     
/*  465 */     if (name != null && name.length() <= 7)
/*      */     {
/*  467 */       result = primitiveTypeNameMap.get(name);
/*      */     }
/*  469 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveWrapper(Class<?> clazz) {
/*  480 */     Assert.notNull(clazz, "Class must not be null");
/*  481 */     return primitiveWrapperTypeMap.containsKey(clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
/*  494 */     Assert.notNull(clazz, "Class must not be null");
/*  495 */     return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveArray(Class<?> clazz) {
/*  505 */     Assert.notNull(clazz, "Class must not be null");
/*  506 */     return (clazz.isArray() && clazz.getComponentType().isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
/*  516 */     Assert.notNull(clazz, "Class must not be null");
/*  517 */     return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> resolvePrimitiveIfNecessary(Class<?> clazz) {
/*  527 */     Assert.notNull(clazz, "Class must not be null");
/*  528 */     return (clazz.isPrimitive() && clazz != void.class) ? primitiveTypeToWrapperMap.get(clazz) : clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
/*  541 */     Assert.notNull(lhsType, "Left-hand side type must not be null");
/*  542 */     Assert.notNull(rhsType, "Right-hand side type must not be null");
/*  543 */     if (lhsType.isAssignableFrom(rhsType)) {
/*  544 */       return true;
/*      */     }
/*  546 */     if (lhsType.isPrimitive()) {
/*  547 */       Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
/*  548 */       return (lhsType == resolvedPrimitive);
/*      */     } 
/*      */     
/*  551 */     Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
/*  552 */     return (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isAssignableValue(Class<?> type, @Nullable Object value) {
/*  565 */     Assert.notNull(type, "Type must not be null");
/*  566 */     return (value != null) ? isAssignable(type, value.getClass()) : (!type.isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String convertResourcePathToClassName(String resourcePath) {
/*  575 */     Assert.notNull(resourcePath, "Resource path must not be null");
/*  576 */     return resourcePath.replace('/', '.');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String convertClassNameToResourcePath(String className) {
/*  585 */     Assert.notNull(className, "Class name must not be null");
/*  586 */     return className.replace('.', '/');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String addResourcePathToPackagePath(Class<?> clazz, String resourceName) {
/*  606 */     Assert.notNull(resourceName, "Resource name must not be null");
/*  607 */     if (!resourceName.startsWith("/")) {
/*  608 */       return classPackageAsResourcePath(clazz) + '/' + resourceName;
/*      */     }
/*  610 */     return classPackageAsResourcePath(clazz) + resourceName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String classPackageAsResourcePath(@Nullable Class<?> clazz) {
/*  628 */     if (clazz == null) {
/*  629 */       return "";
/*      */     }
/*  631 */     String className = clazz.getName();
/*  632 */     int packageEndIndex = className.lastIndexOf('.');
/*  633 */     if (packageEndIndex == -1) {
/*  634 */       return "";
/*      */     }
/*  636 */     String packageName = className.substring(0, packageEndIndex);
/*  637 */     return packageName.replace('.', '/');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String classNamesToString(Class<?>... classes) {
/*  650 */     return classNamesToString(Arrays.asList(classes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String classNamesToString(@Nullable Collection<Class<?>> classes) {
/*  663 */     if (CollectionUtils.isEmpty(classes)) {
/*  664 */       return "[]";
/*      */     }
/*  666 */     StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
/*  667 */     for (Class<?> clazz : classes) {
/*  668 */       stringJoiner.add(clazz.getName());
/*      */     }
/*  670 */     return stringJoiner.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] toClassArray(@Nullable Collection<Class<?>> collection) {
/*  682 */     return !CollectionUtils.isEmpty(collection) ? (Class[])collection.<Class<?>[]>toArray((Class<?>[][])EMPTY_CLASS_ARRAY) : EMPTY_CLASS_ARRAY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] getAllInterfaces(Object instance) {
/*  692 */     Assert.notNull(instance, "Instance must not be null");
/*  693 */     return getAllInterfacesForClass(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
/*  704 */     return getAllInterfacesForClass(clazz, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, @Nullable ClassLoader classLoader) {
/*  717 */     return toClassArray(getAllInterfacesForClassAsSet(clazz, classLoader));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Class<?>> getAllInterfacesAsSet(Object instance) {
/*  727 */     Assert.notNull(instance, "Instance must not be null");
/*  728 */     return getAllInterfacesForClassAsSet(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz) {
/*  739 */     return getAllInterfacesForClassAsSet(clazz, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, @Nullable ClassLoader classLoader) {
/*  752 */     Assert.notNull(clazz, "Class must not be null");
/*  753 */     if (clazz.isInterface() && isVisible(clazz, classLoader)) {
/*  754 */       return Collections.singleton(clazz);
/*      */     }
/*  756 */     Set<Class<?>> interfaces = new LinkedHashSet<>();
/*  757 */     Class<?> current = clazz;
/*  758 */     while (current != null) {
/*  759 */       Class<?>[] ifcs = current.getInterfaces();
/*  760 */       for (Class<?> ifc : ifcs) {
/*  761 */         if (isVisible(ifc, classLoader)) {
/*  762 */           interfaces.add(ifc);
/*      */         }
/*      */       } 
/*  765 */       current = current.getSuperclass();
/*      */     } 
/*  767 */     return interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> createCompositeInterface(Class<?>[] interfaces, @Nullable ClassLoader classLoader) {
/*  783 */     Assert.notEmpty((Object[])interfaces, "Interface array must not be empty");
/*  784 */     return Proxy.getProxyClass(classLoader, interfaces);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Class<?> determineCommonAncestor(@Nullable Class<?> clazz1, @Nullable Class<?> clazz2) {
/*  798 */     if (clazz1 == null) {
/*  799 */       return clazz2;
/*      */     }
/*  801 */     if (clazz2 == null) {
/*  802 */       return clazz1;
/*      */     }
/*  804 */     if (clazz1.isAssignableFrom(clazz2)) {
/*  805 */       return clazz1;
/*      */     }
/*  807 */     if (clazz2.isAssignableFrom(clazz1)) {
/*  808 */       return clazz2;
/*      */     }
/*  810 */     Class<?> ancestor = clazz1;
/*      */     while (true) {
/*  812 */       ancestor = ancestor.getSuperclass();
/*  813 */       if (ancestor == null || Object.class == ancestor) {
/*  814 */         return null;
/*      */       }
/*      */       
/*  817 */       if (ancestor.isAssignableFrom(clazz2)) {
/*  818 */         return ancestor;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isJavaLanguageInterface(Class<?> ifc) {
/*  831 */     return javaLanguageInterfaces.contains(ifc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isInnerClass(Class<?> clazz) {
/*  842 */     return (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isCglibProxy(Object object) {
/*  854 */     return isCglibProxyClass(object.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isCglibProxyClass(@Nullable Class<?> clazz) {
/*  865 */     return (clazz != null && isCglibProxyClassName(clazz.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isCglibProxyClassName(@Nullable String className) {
/*  875 */     return (className != null && className.contains("$$"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getUserClass(Object instance) {
/*  886 */     Assert.notNull(instance, "Instance must not be null");
/*  887 */     return getUserClass(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Class<?> getUserClass(Class<?> clazz) {
/*  897 */     if (clazz.getName().contains("$$")) {
/*  898 */       Class<?> superclass = clazz.getSuperclass();
/*  899 */       if (superclass != null && superclass != Object.class) {
/*  900 */         return superclass;
/*      */       }
/*      */     } 
/*  903 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String getDescriptiveType(@Nullable Object value) {
/*  915 */     if (value == null) {
/*  916 */       return null;
/*      */     }
/*  918 */     Class<?> clazz = value.getClass();
/*  919 */     if (Proxy.isProxyClass(clazz)) {
/*  920 */       String prefix = clazz.getName() + " implementing ";
/*  921 */       StringJoiner result = new StringJoiner(",", prefix, "");
/*  922 */       for (Class<?> ifc : clazz.getInterfaces()) {
/*  923 */         result.add(ifc.getName());
/*      */       }
/*  925 */       return result.toString();
/*      */     } 
/*      */     
/*  928 */     return clazz.getTypeName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean matchesTypeName(Class<?> clazz, @Nullable String typeName) {
/*  938 */     return (typeName != null && (typeName
/*  939 */       .equals(clazz.getTypeName()) || typeName.equals(clazz.getSimpleName())));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortName(String className) {
/*  949 */     Assert.hasLength(className, "Class name must not be empty");
/*  950 */     int lastDotIndex = className.lastIndexOf('.');
/*  951 */     int nameEndIndex = className.indexOf("$$");
/*  952 */     if (nameEndIndex == -1) {
/*  953 */       nameEndIndex = className.length();
/*      */     }
/*  955 */     String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
/*  956 */     shortName = shortName.replace('$', '.');
/*  957 */     return shortName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortName(Class<?> clazz) {
/*  966 */     return getShortName(getQualifiedName(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getShortNameAsProperty(Class<?> clazz) {
/*  977 */     String shortName = getShortName(clazz);
/*  978 */     int dotIndex = shortName.lastIndexOf('.');
/*  979 */     shortName = (dotIndex != -1) ? shortName.substring(dotIndex + 1) : shortName;
/*  980 */     return Introspector.decapitalize(shortName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getClassFileName(Class<?> clazz) {
/*  990 */     Assert.notNull(clazz, "Class must not be null");
/*  991 */     String className = clazz.getName();
/*  992 */     int lastDotIndex = className.lastIndexOf('.');
/*  993 */     return className.substring(lastDotIndex + 1) + ".class";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(Class<?> clazz) {
/* 1004 */     Assert.notNull(clazz, "Class must not be null");
/* 1005 */     return getPackageName(clazz.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPackageName(String fqClassName) {
/* 1016 */     Assert.notNull(fqClassName, "Class name must not be null");
/* 1017 */     int lastDotIndex = fqClassName.lastIndexOf('.');
/* 1018 */     return (lastDotIndex != -1) ? fqClassName.substring(0, lastDotIndex) : "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getQualifiedName(Class<?> clazz) {
/* 1028 */     Assert.notNull(clazz, "Class must not be null");
/* 1029 */     return clazz.getTypeName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getQualifiedMethodName(Method method) {
/* 1039 */     return getQualifiedMethodName(method, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getQualifiedMethodName(Method method, @Nullable Class<?> clazz) {
/* 1052 */     Assert.notNull(method, "Method must not be null");
/* 1053 */     return ((clazz != null) ? clazz : method.getDeclaringClass()).getName() + '.' + method.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasConstructor(Class<?> clazz, Class<?>... paramTypes) {
/* 1065 */     return (getConstructorIfAvailable(clazz, paramTypes) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <T> Constructor<T> getConstructorIfAvailable(Class<T> clazz, Class<?>... paramTypes) {
/* 1079 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/* 1081 */       return clazz.getConstructor(paramTypes);
/*      */     }
/* 1083 */     catch (NoSuchMethodException ex) {
/* 1084 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasMethod(Class<?> clazz, Method method) {
/* 1096 */     Assert.notNull(clazz, "Class must not be null");
/* 1097 */     Assert.notNull(method, "Method must not be null");
/* 1098 */     if (clazz == method.getDeclaringClass()) {
/* 1099 */       return true;
/*      */     }
/* 1101 */     String methodName = method.getName();
/* 1102 */     Class<?>[] paramTypes = method.getParameterTypes();
/* 1103 */     return (getMethodOrNull(clazz, methodName, paramTypes) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
/* 1116 */     return (getMethodIfAvailable(clazz, methodName, paramTypes) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getMethod(Class<?> clazz, String methodName, @Nullable Class<?>... paramTypes) {
/* 1134 */     Assert.notNull(clazz, "Class must not be null");
/* 1135 */     Assert.notNull(methodName, "Method name must not be null");
/* 1136 */     if (paramTypes != null) {
/*      */       try {
/* 1138 */         return clazz.getMethod(methodName, paramTypes);
/*      */       }
/* 1140 */       catch (NoSuchMethodException ex) {
/* 1141 */         throw new IllegalStateException("Expected method not found: " + ex);
/*      */       } 
/*      */     }
/*      */     
/* 1145 */     Set<Method> candidates = findMethodCandidatesByName(clazz, methodName);
/* 1146 */     if (candidates.size() == 1) {
/* 1147 */       return candidates.iterator().next();
/*      */     }
/* 1149 */     if (candidates.isEmpty()) {
/* 1150 */       throw new IllegalStateException("Expected method not found: " + clazz.getName() + '.' + methodName);
/*      */     }
/*      */     
/* 1153 */     throw new IllegalStateException("No unique method found: " + clazz.getName() + '.' + methodName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Method getMethodIfAvailable(Class<?> clazz, String methodName, @Nullable Class<?>... paramTypes) {
/* 1173 */     Assert.notNull(clazz, "Class must not be null");
/* 1174 */     Assert.notNull(methodName, "Method name must not be null");
/* 1175 */     if (paramTypes != null) {
/* 1176 */       return getMethodOrNull(clazz, methodName, paramTypes);
/*      */     }
/*      */     
/* 1179 */     Set<Method> candidates = findMethodCandidatesByName(clazz, methodName);
/* 1180 */     if (candidates.size() == 1) {
/* 1181 */       return candidates.iterator().next();
/*      */     }
/* 1183 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getMethodCountForName(Class<?> clazz, String methodName) {
/* 1195 */     Assert.notNull(clazz, "Class must not be null");
/* 1196 */     Assert.notNull(methodName, "Method name must not be null");
/* 1197 */     int count = 0;
/* 1198 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/* 1199 */     for (Method method : declaredMethods) {
/* 1200 */       if (methodName.equals(method.getName())) {
/* 1201 */         count++;
/*      */       }
/*      */     } 
/* 1204 */     Class<?>[] ifcs = clazz.getInterfaces();
/* 1205 */     for (Class<?> ifc : ifcs) {
/* 1206 */       count += getMethodCountForName(ifc, methodName);
/*      */     }
/* 1208 */     if (clazz.getSuperclass() != null) {
/* 1209 */       count += getMethodCountForName(clazz.getSuperclass(), methodName);
/*      */     }
/* 1211 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasAtLeastOneMethodWithName(Class<?> clazz, String methodName) {
/* 1223 */     Assert.notNull(clazz, "Class must not be null");
/* 1224 */     Assert.notNull(methodName, "Method name must not be null");
/* 1225 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/* 1226 */     for (Method method : declaredMethods) {
/* 1227 */       if (method.getName().equals(methodName)) {
/* 1228 */         return true;
/*      */       }
/*      */     } 
/* 1231 */     Class<?>[] ifcs = clazz.getInterfaces();
/* 1232 */     for (Class<?> ifc : ifcs) {
/* 1233 */       if (hasAtLeastOneMethodWithName(ifc, methodName)) {
/* 1234 */         return true;
/*      */       }
/*      */     } 
/* 1237 */     return (clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getMostSpecificMethod(Method method, @Nullable Class<?> targetClass) {
/* 1262 */     if (targetClass != null && targetClass != method.getDeclaringClass() && isOverridable(method, targetClass)) {
/*      */       try {
/* 1264 */         if (Modifier.isPublic(method.getModifiers())) {
/*      */           try {
/* 1266 */             return targetClass.getMethod(method.getName(), method.getParameterTypes());
/*      */           }
/* 1268 */           catch (NoSuchMethodException ex) {
/* 1269 */             return method;
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 1274 */         Method specificMethod = ReflectionUtils.findMethod(targetClass, method.getName(), method.getParameterTypes());
/* 1275 */         return (specificMethod != null) ? specificMethod : method;
/*      */       
/*      */       }
/* 1278 */       catch (SecurityException securityException) {}
/*      */     }
/*      */ 
/*      */     
/* 1282 */     return method;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Method getInterfaceMethodIfPossible(Method method) {
/* 1295 */     if (!Modifier.isPublic(method.getModifiers()) || method.getDeclaringClass().isInterface()) {
/* 1296 */       return method;
/*      */     }
/* 1298 */     return interfaceMethodCache.computeIfAbsent(method, key -> {
/*      */           Class<?> current = key.getDeclaringClass();
/*      */           
/*      */           while (current != null && current != Object.class) {
/*      */             Class<?>[] ifcs = current.getInterfaces();
/*      */             for (Class<?> ifc : ifcs) {
/*      */               try {
/*      */                 return ifc.getMethod(key.getName(), key.getParameterTypes());
/* 1306 */               } catch (NoSuchMethodException noSuchMethodException) {}
/*      */             } 
/*      */             current = current.getSuperclass();
/*      */           } 
/*      */           return key;
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isUserLevelMethod(Method method) {
/* 1328 */     Assert.notNull(method, "Method must not be null");
/* 1329 */     return (method.isBridge() || (!method.isSynthetic() && !isGroovyObjectMethod(method)));
/*      */   }
/*      */   
/*      */   private static boolean isGroovyObjectMethod(Method method) {
/* 1333 */     return method.getDeclaringClass().getName().equals("groovy.lang.GroovyObject");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isOverridable(Method method, @Nullable Class<?> targetClass) {
/* 1342 */     if (Modifier.isPrivate(method.getModifiers())) {
/* 1343 */       return false;
/*      */     }
/* 1345 */     if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
/* 1346 */       return true;
/*      */     }
/* 1348 */     return (targetClass == null || 
/* 1349 */       getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Method getStaticMethod(Class<?> clazz, String methodName, Class<?>... args) {
/* 1362 */     Assert.notNull(clazz, "Class must not be null");
/* 1363 */     Assert.notNull(methodName, "Method name must not be null");
/*      */     try {
/* 1365 */       Method method = clazz.getMethod(methodName, args);
/* 1366 */       return Modifier.isStatic(method.getModifiers()) ? method : null;
/*      */     }
/* 1368 */     catch (NoSuchMethodException ex) {
/* 1369 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static Method getMethodOrNull(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
/*      */     try {
/* 1377 */       return clazz.getMethod(methodName, paramTypes);
/*      */     }
/* 1379 */     catch (NoSuchMethodException ex) {
/* 1380 */       return null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static Set<Method> findMethodCandidatesByName(Class<?> clazz, String methodName) {
/* 1385 */     Set<Method> candidates = new HashSet<>(1);
/* 1386 */     Method[] methods = clazz.getMethods();
/* 1387 */     for (Method method : methods) {
/* 1388 */       if (methodName.equals(method.getName())) {
/* 1389 */         candidates.add(method);
/*      */       }
/*      */     } 
/* 1392 */     return candidates;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\ClassUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */