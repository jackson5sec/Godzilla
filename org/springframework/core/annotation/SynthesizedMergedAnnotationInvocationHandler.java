/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SynthesizedMergedAnnotationInvocationHandler<A extends Annotation>
/*     */   implements InvocationHandler
/*     */ {
/*     */   private final MergedAnnotation<?> annotation;
/*     */   private final Class<A> type;
/*     */   private final AttributeMethods attributes;
/*  56 */   private final Map<String, Object> valueCache = new ConcurrentHashMap<>(8);
/*     */   
/*     */   @Nullable
/*     */   private volatile Integer hashCode;
/*     */   
/*     */   @Nullable
/*     */   private volatile String string;
/*     */ 
/*     */   
/*     */   private SynthesizedMergedAnnotationInvocationHandler(MergedAnnotation<A> annotation, Class<A> type) {
/*  66 */     Assert.notNull(annotation, "MergedAnnotation must not be null");
/*  67 */     Assert.notNull(type, "Type must not be null");
/*  68 */     Assert.isTrue(type.isAnnotation(), "Type must be an annotation");
/*  69 */     this.annotation = annotation;
/*  70 */     this.type = type;
/*  71 */     this.attributes = AttributeMethods.forAnnotationType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) {
/*  77 */     if (ReflectionUtils.isEqualsMethod(method)) {
/*  78 */       return Boolean.valueOf(annotationEquals(args[0]));
/*     */     }
/*  80 */     if (ReflectionUtils.isHashCodeMethod(method)) {
/*  81 */       return Integer.valueOf(annotationHashCode());
/*     */     }
/*  83 */     if (ReflectionUtils.isToStringMethod(method)) {
/*  84 */       return annotationToString();
/*     */     }
/*  86 */     if (isAnnotationTypeMethod(method)) {
/*  87 */       return this.type;
/*     */     }
/*  89 */     if (this.attributes.indexOf(method.getName()) != -1) {
/*  90 */       return getAttributeValue(method);
/*     */     }
/*  92 */     throw new AnnotationConfigurationException(String.format("Method [%s] is unsupported for synthesized annotation type [%s]", new Object[] { method, this.type }));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isAnnotationTypeMethod(Method method) {
/*  97 */     return (method.getName().equals("annotationType") && method.getParameterCount() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean annotationEquals(Object other) {
/* 105 */     if (this == other) {
/* 106 */       return true;
/*     */     }
/* 108 */     if (!this.type.isInstance(other)) {
/* 109 */       return false;
/*     */     }
/* 111 */     for (int i = 0; i < this.attributes.size(); i++) {
/* 112 */       Method attribute = this.attributes.get(i);
/* 113 */       Object thisValue = getAttributeValue(attribute);
/* 114 */       Object otherValue = ReflectionUtils.invokeMethod(attribute, other);
/* 115 */       if (!ObjectUtils.nullSafeEquals(thisValue, otherValue)) {
/* 116 */         return false;
/*     */       }
/*     */     } 
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int annotationHashCode() {
/* 126 */     Integer hashCode = this.hashCode;
/* 127 */     if (hashCode == null) {
/* 128 */       hashCode = computeHashCode();
/* 129 */       this.hashCode = hashCode;
/*     */     } 
/* 131 */     return hashCode.intValue();
/*     */   }
/*     */   
/*     */   private Integer computeHashCode() {
/* 135 */     int hashCode = 0;
/* 136 */     for (int i = 0; i < this.attributes.size(); i++) {
/* 137 */       Method attribute = this.attributes.get(i);
/* 138 */       Object value = getAttributeValue(attribute);
/* 139 */       hashCode += 127 * attribute.getName().hashCode() ^ getValueHashCode(value);
/*     */     } 
/* 141 */     return Integer.valueOf(hashCode);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getValueHashCode(Object value) {
/* 147 */     if (value instanceof boolean[]) {
/* 148 */       return Arrays.hashCode((boolean[])value);
/*     */     }
/* 150 */     if (value instanceof byte[]) {
/* 151 */       return Arrays.hashCode((byte[])value);
/*     */     }
/* 153 */     if (value instanceof char[]) {
/* 154 */       return Arrays.hashCode((char[])value);
/*     */     }
/* 156 */     if (value instanceof double[]) {
/* 157 */       return Arrays.hashCode((double[])value);
/*     */     }
/* 159 */     if (value instanceof float[]) {
/* 160 */       return Arrays.hashCode((float[])value);
/*     */     }
/* 162 */     if (value instanceof int[]) {
/* 163 */       return Arrays.hashCode((int[])value);
/*     */     }
/* 165 */     if (value instanceof long[]) {
/* 166 */       return Arrays.hashCode((long[])value);
/*     */     }
/* 168 */     if (value instanceof short[]) {
/* 169 */       return Arrays.hashCode((short[])value);
/*     */     }
/* 171 */     if (value instanceof Object[]) {
/* 172 */       return Arrays.hashCode((Object[])value);
/*     */     }
/* 174 */     return value.hashCode();
/*     */   }
/*     */   
/*     */   private String annotationToString() {
/* 178 */     String string = this.string;
/* 179 */     if (string == null) {
/* 180 */       StringBuilder builder = (new StringBuilder("@")).append(this.type.getName()).append('(');
/* 181 */       for (int i = 0; i < this.attributes.size(); i++) {
/* 182 */         Method attribute = this.attributes.get(i);
/* 183 */         if (i > 0) {
/* 184 */           builder.append(", ");
/*     */         }
/* 186 */         builder.append(attribute.getName());
/* 187 */         builder.append('=');
/* 188 */         builder.append(toString(getAttributeValue(attribute)));
/*     */       } 
/* 190 */       builder.append(')');
/* 191 */       string = builder.toString();
/* 192 */       this.string = string;
/*     */     } 
/* 194 */     return string;
/*     */   }
/*     */   
/*     */   private String toString(Object value) {
/* 198 */     if (value instanceof Class) {
/* 199 */       return ((Class)value).getName();
/*     */     }
/* 201 */     if (value.getClass().isArray()) {
/* 202 */       StringBuilder builder = new StringBuilder("[");
/* 203 */       for (int i = 0; i < Array.getLength(value); i++) {
/* 204 */         if (i > 0) {
/* 205 */           builder.append(", ");
/*     */         }
/* 207 */         builder.append(toString(Array.get(value, i)));
/*     */       } 
/* 209 */       builder.append(']');
/* 210 */       return builder.toString();
/*     */     } 
/* 212 */     return String.valueOf(value);
/*     */   }
/*     */   
/*     */   private Object getAttributeValue(Method method) {
/* 216 */     Object value = this.valueCache.computeIfAbsent(method.getName(), attributeName -> {
/*     */           Class<?> type = ClassUtils.resolvePrimitiveIfNecessary(method.getReturnType());
/*     */ 
/*     */           
/*     */           return this.annotation.getValue(attributeName, type).orElseThrow(());
/*     */         });
/*     */ 
/*     */     
/* 224 */     if (value.getClass().isArray() && Array.getLength(value) > 0) {
/* 225 */       value = cloneArray(value);
/*     */     }
/*     */     
/* 228 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object cloneArray(Object array) {
/* 236 */     if (array instanceof boolean[]) {
/* 237 */       return ((boolean[])array).clone();
/*     */     }
/* 239 */     if (array instanceof byte[]) {
/* 240 */       return ((byte[])array).clone();
/*     */     }
/* 242 */     if (array instanceof char[]) {
/* 243 */       return ((char[])array).clone();
/*     */     }
/* 245 */     if (array instanceof double[]) {
/* 246 */       return ((double[])array).clone();
/*     */     }
/* 248 */     if (array instanceof float[]) {
/* 249 */       return ((float[])array).clone();
/*     */     }
/* 251 */     if (array instanceof int[]) {
/* 252 */       return ((int[])array).clone();
/*     */     }
/* 254 */     if (array instanceof long[]) {
/* 255 */       return ((long[])array).clone();
/*     */     }
/* 257 */     if (array instanceof short[]) {
/* 258 */       return ((short[])array).clone();
/*     */     }
/*     */ 
/*     */     
/* 262 */     return ((Object[])array).clone();
/*     */   }
/*     */ 
/*     */   
/*     */   static <A extends Annotation> A createProxy(MergedAnnotation<A> annotation, Class<A> type) {
/* 267 */     ClassLoader classLoader = type.getClassLoader();
/* 268 */     InvocationHandler<A> handler = new SynthesizedMergedAnnotationInvocationHandler<>(annotation, type);
/* 269 */     (new Class[2])[0] = type; (new Class[2])[1] = SynthesizedAnnotation.class; (new Class[1])[0] = type; Class<?>[] interfaces = isVisible(classLoader, SynthesizedAnnotation.class) ? new Class[2] : new Class[1];
/*     */     
/* 271 */     return (A)Proxy.newProxyInstance(classLoader, interfaces, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isVisible(ClassLoader classLoader, Class<?> interfaceClass) {
/* 276 */     if (classLoader == interfaceClass.getClassLoader()) {
/* 277 */       return true;
/*     */     }
/*     */     try {
/* 280 */       return (Class.forName(interfaceClass.getName(), false, classLoader) == interfaceClass);
/*     */     }
/* 282 */     catch (ClassNotFoundException ex) {
/* 283 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\SynthesizedMergedAnnotationInvocationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */