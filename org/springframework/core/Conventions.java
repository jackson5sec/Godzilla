/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public final class Conventions
/*     */ {
/*     */   private static final String PLURAL_SUFFIX = "List";
/*     */   
/*     */   public static String getVariableName(Object value) {
/*     */     Class<?> valueClass;
/*  65 */     Assert.notNull(value, "Value must not be null");
/*     */     
/*  67 */     boolean pluralize = false;
/*     */     
/*  69 */     if (value.getClass().isArray()) {
/*  70 */       valueClass = value.getClass().getComponentType();
/*  71 */       pluralize = true;
/*     */     }
/*  73 */     else if (value instanceof Collection) {
/*  74 */       Collection<?> collection = (Collection)value;
/*  75 */       if (collection.isEmpty()) {
/*  76 */         throw new IllegalArgumentException("Cannot generate variable name for an empty Collection");
/*     */       }
/*     */       
/*  79 */       Object valueToCheck = peekAhead(collection);
/*  80 */       valueClass = getClassForValue(valueToCheck);
/*  81 */       pluralize = true;
/*     */     } else {
/*     */       
/*  84 */       valueClass = getClassForValue(value);
/*     */     } 
/*     */     
/*  87 */     String name = ClassUtils.getShortNameAsProperty(valueClass);
/*  88 */     return pluralize ? pluralize(name) : name;
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
/*     */   public static String getVariableNameForParameter(MethodParameter parameter) {
/*     */     Class<?> valueClass;
/* 102 */     Assert.notNull(parameter, "MethodParameter must not be null");
/*     */     
/* 104 */     boolean pluralize = false;
/* 105 */     String reactiveSuffix = "";
/*     */     
/* 107 */     if (parameter.getParameterType().isArray()) {
/* 108 */       valueClass = parameter.getParameterType().getComponentType();
/* 109 */       pluralize = true;
/*     */     }
/* 111 */     else if (Collection.class.isAssignableFrom(parameter.getParameterType())) {
/* 112 */       valueClass = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0]);
/* 113 */       if (valueClass == null) {
/* 114 */         throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection parameter type");
/*     */       }
/*     */       
/* 117 */       pluralize = true;
/*     */     } else {
/*     */       
/* 120 */       valueClass = parameter.getParameterType();
/* 121 */       ReactiveAdapter adapter = ReactiveAdapterRegistry.getSharedInstance().getAdapter(valueClass);
/* 122 */       if (adapter != null && !adapter.getDescriptor().isNoValue()) {
/* 123 */         reactiveSuffix = ClassUtils.getShortName(valueClass);
/* 124 */         valueClass = parameter.nested().getNestedParameterType();
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     String name = ClassUtils.getShortNameAsProperty(valueClass);
/* 129 */     return pluralize ? pluralize(name) : (name + reactiveSuffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getVariableNameForReturnType(Method method) {
/* 139 */     return getVariableNameForReturnType(method, method.getReturnType(), null);
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
/*     */   public static String getVariableNameForReturnType(Method method, @Nullable Object value) {
/* 152 */     return getVariableNameForReturnType(method, method.getReturnType(), value);
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
/*     */   
/*     */   public static String getVariableNameForReturnType(Method method, Class<?> resolvedType, @Nullable Object value) {
/*     */     Class<?> valueClass;
/* 170 */     Assert.notNull(method, "Method must not be null");
/*     */     
/* 172 */     if (Object.class == resolvedType) {
/* 173 */       if (value == null) {
/* 174 */         throw new IllegalArgumentException("Cannot generate variable name for an Object return type with null value");
/*     */       }
/*     */       
/* 177 */       return getVariableName(value);
/*     */     } 
/*     */ 
/*     */     
/* 181 */     boolean pluralize = false;
/* 182 */     String reactiveSuffix = "";
/*     */     
/* 184 */     if (resolvedType.isArray()) {
/* 185 */       valueClass = resolvedType.getComponentType();
/* 186 */       pluralize = true;
/*     */     }
/* 188 */     else if (Collection.class.isAssignableFrom(resolvedType)) {
/* 189 */       valueClass = ResolvableType.forMethodReturnType(method).asCollection().resolveGeneric(new int[0]);
/* 190 */       if (valueClass == null) {
/* 191 */         if (!(value instanceof Collection)) {
/* 192 */           throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection return type and a non-Collection value");
/*     */         }
/*     */         
/* 195 */         Collection<?> collection = (Collection)value;
/* 196 */         if (collection.isEmpty()) {
/* 197 */           throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection return type and an empty Collection value");
/*     */         }
/*     */         
/* 200 */         Object valueToCheck = peekAhead(collection);
/* 201 */         valueClass = getClassForValue(valueToCheck);
/*     */       } 
/* 203 */       pluralize = true;
/*     */     } else {
/*     */       
/* 206 */       valueClass = resolvedType;
/* 207 */       ReactiveAdapter adapter = ReactiveAdapterRegistry.getSharedInstance().getAdapter(valueClass);
/* 208 */       if (adapter != null && !adapter.getDescriptor().isNoValue()) {
/* 209 */         reactiveSuffix = ClassUtils.getShortName(valueClass);
/* 210 */         valueClass = ResolvableType.forMethodReturnType(method).getGeneric(new int[0]).toClass();
/*     */       } 
/*     */     } 
/*     */     
/* 214 */     String name = ClassUtils.getShortNameAsProperty(valueClass);
/* 215 */     return pluralize ? pluralize(name) : (name + reactiveSuffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String attributeNameToPropertyName(String attributeName) {
/* 224 */     Assert.notNull(attributeName, "'attributeName' must not be null");
/* 225 */     if (!attributeName.contains("-")) {
/* 226 */       return attributeName;
/*     */     }
/* 228 */     char[] result = new char[attributeName.length() - 1];
/* 229 */     int currPos = 0;
/* 230 */     boolean upperCaseNext = false;
/* 231 */     for (int i = 0; i < attributeName.length(); i++) {
/* 232 */       char c = attributeName.charAt(i);
/* 233 */       if (c == '-') {
/* 234 */         upperCaseNext = true;
/*     */       }
/* 236 */       else if (upperCaseNext) {
/* 237 */         result[currPos++] = Character.toUpperCase(c);
/* 238 */         upperCaseNext = false;
/*     */       } else {
/*     */         
/* 241 */         result[currPos++] = c;
/*     */       } 
/*     */     } 
/* 244 */     return new String(result, 0, currPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getQualifiedAttributeName(Class<?> enclosingClass, String attributeName) {
/* 253 */     Assert.notNull(enclosingClass, "'enclosingClass' must not be null");
/* 254 */     Assert.notNull(attributeName, "'attributeName' must not be null");
/* 255 */     return enclosingClass.getName() + '.' + attributeName;
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
/*     */   private static Class<?> getClassForValue(Object value) {
/* 268 */     Class<?> valueClass = value.getClass();
/* 269 */     if (Proxy.isProxyClass(valueClass)) {
/* 270 */       Class<?>[] ifcs = valueClass.getInterfaces();
/* 271 */       for (Class<?> ifc : ifcs) {
/* 272 */         if (!ClassUtils.isJavaLanguageInterface(ifc)) {
/* 273 */           return ifc;
/*     */         }
/*     */       }
/*     */     
/* 277 */     } else if (valueClass.getName().lastIndexOf('$') != -1 && valueClass.getDeclaringClass() == null) {
/*     */ 
/*     */       
/* 280 */       valueClass = valueClass.getSuperclass();
/*     */     } 
/* 282 */     return valueClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String pluralize(String name) {
/* 289 */     return name + "List";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> E peekAhead(Collection<E> collection) {
/* 298 */     Iterator<E> it = collection.iterator();
/* 299 */     if (!it.hasNext()) {
/* 300 */       throw new IllegalStateException("Unable to peek ahead in non-empty collection - no element found");
/*     */     }
/*     */     
/* 303 */     E value = it.next();
/* 304 */     if (value == null) {
/* 305 */       throw new IllegalStateException("Unable to peek ahead in non-empty collection - only null element found");
/*     */     }
/*     */     
/* 308 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\Conventions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */