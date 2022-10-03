/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ final class AttributeMethods
/*     */ {
/*  39 */   static final AttributeMethods NONE = new AttributeMethods(null, new Method[0]);
/*     */ 
/*     */   
/*  42 */   private static final Map<Class<? extends Annotation>, AttributeMethods> cache = (Map<Class<? extends Annotation>, AttributeMethods>)new ConcurrentReferenceHashMap();
/*     */   
/*     */   static {
/*  45 */     methodComparator = ((m1, m2) -> 
/*  46 */       (m1 != null && m2 != null) ? m1.getName().compareTo(m2.getName()) : ((m1 != null) ? -1 : 1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Comparator<Method> methodComparator;
/*     */   
/*     */   @Nullable
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   
/*     */   private final Method[] attributeMethods;
/*     */   
/*     */   private final boolean[] canThrowTypeNotPresentException;
/*     */   
/*     */   private final boolean hasDefaultValueMethod;
/*     */   
/*     */   private final boolean hasNestedAnnotation;
/*     */ 
/*     */   
/*     */   private AttributeMethods(@Nullable Class<? extends Annotation> annotationType, Method[] attributeMethods) {
/*  66 */     this.annotationType = annotationType;
/*  67 */     this.attributeMethods = attributeMethods;
/*  68 */     this.canThrowTypeNotPresentException = new boolean[attributeMethods.length];
/*  69 */     boolean foundDefaultValueMethod = false;
/*  70 */     boolean foundNestedAnnotation = false;
/*  71 */     for (int i = 0; i < attributeMethods.length; i++) {
/*  72 */       Method method = this.attributeMethods[i];
/*  73 */       Class<?> type = method.getReturnType();
/*  74 */       if (method.getDefaultValue() != null) {
/*  75 */         foundDefaultValueMethod = true;
/*     */       }
/*  77 */       if (type.isAnnotation() || (type.isArray() && type.getComponentType().isAnnotation())) {
/*  78 */         foundNestedAnnotation = true;
/*     */       }
/*  80 */       ReflectionUtils.makeAccessible(method);
/*  81 */       this.canThrowTypeNotPresentException[i] = (type == Class.class || type == Class[].class || type.isEnum());
/*     */     } 
/*  83 */     this.hasDefaultValueMethod = foundDefaultValueMethod;
/*  84 */     this.hasNestedAnnotation = foundNestedAnnotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasOnlyValueAttribute() {
/*  94 */     return (this.attributeMethods.length == 1 && "value"
/*  95 */       .equals(this.attributeMethods[0].getName()));
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
/*     */   boolean isValid(Annotation annotation) {
/* 107 */     assertAnnotation(annotation);
/* 108 */     for (int i = 0; i < size(); i++) {
/* 109 */       if (canThrowTypeNotPresentException(i)) {
/*     */         try {
/* 111 */           get(i).invoke(annotation, new Object[0]);
/*     */         }
/* 113 */         catch (Throwable ex) {
/* 114 */           return false;
/*     */         } 
/*     */       }
/*     */     } 
/* 118 */     return true;
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
/*     */   void validate(Annotation annotation) {
/* 132 */     assertAnnotation(annotation);
/* 133 */     for (int i = 0; i < size(); i++) {
/* 134 */       if (canThrowTypeNotPresentException(i)) {
/*     */         try {
/* 136 */           get(i).invoke(annotation, new Object[0]);
/*     */         }
/* 138 */         catch (Throwable ex) {
/* 139 */           throw new IllegalStateException("Could not obtain annotation attribute value for " + 
/* 140 */               get(i).getName() + " declared on " + annotation.annotationType(), ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void assertAnnotation(Annotation annotation) {
/* 147 */     Assert.notNull(annotation, "Annotation must not be null");
/* 148 */     if (this.annotationType != null) {
/* 149 */       Assert.isInstanceOf(this.annotationType, annotation);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Method get(String name) {
/* 161 */     int index = indexOf(name);
/* 162 */     return (index != -1) ? this.attributeMethods[index] : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Method get(int index) {
/* 173 */     return this.attributeMethods[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean canThrowTypeNotPresentException(int index) {
/* 184 */     return this.canThrowTypeNotPresentException[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int indexOf(String name) {
/* 194 */     for (int i = 0; i < this.attributeMethods.length; i++) {
/* 195 */       if (this.attributeMethods[i].getName().equals(name)) {
/* 196 */         return i;
/*     */       }
/*     */     } 
/* 199 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int indexOf(Method attribute) {
/* 209 */     for (int i = 0; i < this.attributeMethods.length; i++) {
/* 210 */       if (this.attributeMethods[i].equals(attribute)) {
/* 211 */         return i;
/*     */       }
/*     */     } 
/* 214 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int size() {
/* 222 */     return this.attributeMethods.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasDefaultValueMethod() {
/* 230 */     return this.hasDefaultValueMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasNestedAnnotation() {
/* 239 */     return this.hasNestedAnnotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static AttributeMethods forAnnotationType(@Nullable Class<? extends Annotation> annotationType) {
/* 249 */     if (annotationType == null) {
/* 250 */       return NONE;
/*     */     }
/* 252 */     return cache.computeIfAbsent(annotationType, AttributeMethods::compute);
/*     */   }
/*     */   
/*     */   private static AttributeMethods compute(Class<? extends Annotation> annotationType) {
/* 256 */     Method[] methods = annotationType.getDeclaredMethods();
/* 257 */     int size = methods.length;
/* 258 */     for (int i = 0; i < methods.length; i++) {
/* 259 */       if (!isAttributeMethod(methods[i])) {
/* 260 */         methods[i] = null;
/* 261 */         size--;
/*     */       } 
/*     */     } 
/* 264 */     if (size == 0) {
/* 265 */       return NONE;
/*     */     }
/* 267 */     Arrays.sort(methods, methodComparator);
/* 268 */     Method[] attributeMethods = Arrays.<Method>copyOf(methods, size);
/* 269 */     return new AttributeMethods(annotationType, attributeMethods);
/*     */   }
/*     */   
/*     */   private static boolean isAttributeMethod(Method method) {
/* 273 */     return (method.getParameterCount() == 0 && method.getReturnType() != void.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String describe(@Nullable Method attribute) {
/* 283 */     if (attribute == null) {
/* 284 */       return "(none)";
/*     */     }
/* 286 */     return describe(attribute.getDeclaringClass(), attribute.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String describe(@Nullable Class<?> annotationType, @Nullable String attributeName) {
/* 297 */     if (attributeName == null) {
/* 298 */       return "(none)";
/*     */     }
/* 300 */     String in = (annotationType != null) ? (" in annotation [" + annotationType.getName() + "]") : "";
/* 301 */     return "attribute '" + attributeName + "'" + in;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AttributeMethods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */