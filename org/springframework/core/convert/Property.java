/*     */ package org.springframework.core.convert;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public final class Property
/*     */ {
/*  50 */   private static Map<Property, Annotation[]> annotationCache = (Map<Property, Annotation[]>)new ConcurrentReferenceHashMap();
/*     */   
/*     */   private final Class<?> objectType;
/*     */   
/*     */   @Nullable
/*     */   private final Method readMethod;
/*     */   
/*     */   @Nullable
/*     */   private final Method writeMethod;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final MethodParameter methodParameter;
/*     */   
/*     */   @Nullable
/*     */   private Annotation[] annotations;
/*     */ 
/*     */   
/*     */   public Property(Class<?> objectType, @Nullable Method readMethod, @Nullable Method writeMethod) {
/*  69 */     this(objectType, readMethod, writeMethod, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Property(Class<?> objectType, @Nullable Method readMethod, @Nullable Method writeMethod, @Nullable String name) {
/*  75 */     this.objectType = objectType;
/*  76 */     this.readMethod = readMethod;
/*  77 */     this.writeMethod = writeMethod;
/*  78 */     this.methodParameter = resolveMethodParameter();
/*  79 */     this.name = (name != null) ? name : resolveName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/*  87 */     return this.objectType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  94 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getType() {
/* 101 */     return this.methodParameter.getParameterType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getReadMethod() {
/* 109 */     return this.readMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Method getWriteMethod() {
/* 117 */     return this.writeMethod;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MethodParameter getMethodParameter() {
/* 124 */     return this.methodParameter;
/*     */   }
/*     */   
/*     */   Annotation[] getAnnotations() {
/* 128 */     if (this.annotations == null) {
/* 129 */       this.annotations = resolveAnnotations();
/*     */     }
/* 131 */     return this.annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String resolveName() {
/* 138 */     if (this.readMethod != null) {
/* 139 */       int index = this.readMethod.getName().indexOf("get");
/* 140 */       if (index != -1) {
/* 141 */         index += 3;
/*     */       } else {
/*     */         
/* 144 */         index = this.readMethod.getName().indexOf("is");
/* 145 */         if (index != -1) {
/* 146 */           index += 2;
/*     */         }
/*     */         else {
/*     */           
/* 150 */           index = 0;
/*     */         } 
/*     */       } 
/* 153 */       return StringUtils.uncapitalize(this.readMethod.getName().substring(index));
/*     */     } 
/* 155 */     if (this.writeMethod != null) {
/* 156 */       int index = this.writeMethod.getName().indexOf("set");
/* 157 */       if (index == -1) {
/* 158 */         throw new IllegalArgumentException("Not a setter method");
/*     */       }
/* 160 */       index += 3;
/* 161 */       return StringUtils.uncapitalize(this.writeMethod.getName().substring(index));
/*     */     } 
/*     */     
/* 164 */     throw new IllegalStateException("Property is neither readable nor writeable");
/*     */   }
/*     */ 
/*     */   
/*     */   private MethodParameter resolveMethodParameter() {
/* 169 */     MethodParameter read = resolveReadMethodParameter();
/* 170 */     MethodParameter write = resolveWriteMethodParameter();
/* 171 */     if (write == null) {
/* 172 */       if (read == null) {
/* 173 */         throw new IllegalStateException("Property is neither readable nor writeable");
/*     */       }
/* 175 */       return read;
/*     */     } 
/* 177 */     if (read != null) {
/* 178 */       Class<?> readType = read.getParameterType();
/* 179 */       Class<?> writeType = write.getParameterType();
/* 180 */       if (!writeType.equals(readType) && writeType.isAssignableFrom(readType)) {
/* 181 */         return read;
/*     */       }
/*     */     } 
/* 184 */     return write;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private MethodParameter resolveReadMethodParameter() {
/* 189 */     if (getReadMethod() == null) {
/* 190 */       return null;
/*     */     }
/* 192 */     return (new MethodParameter(getReadMethod(), -1)).withContainingClass(getObjectType());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private MethodParameter resolveWriteMethodParameter() {
/* 197 */     if (getWriteMethod() == null) {
/* 198 */       return null;
/*     */     }
/* 200 */     return (new MethodParameter(getWriteMethod(), 0)).withContainingClass(getObjectType());
/*     */   }
/*     */   
/*     */   private Annotation[] resolveAnnotations() {
/* 204 */     Annotation[] annotations = annotationCache.get(this);
/* 205 */     if (annotations == null) {
/* 206 */       Map<Class<? extends Annotation>, Annotation> annotationMap = new LinkedHashMap<>();
/* 207 */       addAnnotationsToMap(annotationMap, getReadMethod());
/* 208 */       addAnnotationsToMap(annotationMap, getWriteMethod());
/* 209 */       addAnnotationsToMap(annotationMap, getField());
/* 210 */       annotations = (Annotation[])annotationMap.values().toArray((Object[])new Annotation[0]);
/* 211 */       annotationCache.put(this, annotations);
/*     */     } 
/* 213 */     return annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAnnotationsToMap(Map<Class<? extends Annotation>, Annotation> annotationMap, @Nullable AnnotatedElement object) {
/* 219 */     if (object != null) {
/* 220 */       for (Annotation annotation : object.getAnnotations()) {
/* 221 */         annotationMap.put(annotation.annotationType(), annotation);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Field getField() {
/* 228 */     String name = getName();
/* 229 */     if (!StringUtils.hasLength(name)) {
/* 230 */       return null;
/*     */     }
/* 232 */     Field field = null;
/* 233 */     Class<?> declaringClass = declaringClass();
/* 234 */     if (declaringClass != null) {
/* 235 */       field = ReflectionUtils.findField(declaringClass, name);
/* 236 */       if (field == null) {
/*     */         
/* 238 */         field = ReflectionUtils.findField(declaringClass, StringUtils.uncapitalize(name));
/* 239 */         if (field == null) {
/* 240 */           field = ReflectionUtils.findField(declaringClass, StringUtils.capitalize(name));
/*     */         }
/*     */       } 
/*     */     } 
/* 244 */     return field;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Class<?> declaringClass() {
/* 249 */     if (getReadMethod() != null) {
/* 250 */       return getReadMethod().getDeclaringClass();
/*     */     }
/* 252 */     if (getWriteMethod() != null) {
/* 253 */       return getWriteMethod().getDeclaringClass();
/*     */     }
/*     */     
/* 256 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 263 */     if (this == other) {
/* 264 */       return true;
/*     */     }
/* 266 */     if (!(other instanceof Property)) {
/* 267 */       return false;
/*     */     }
/* 269 */     Property otherProperty = (Property)other;
/* 270 */     return (ObjectUtils.nullSafeEquals(this.objectType, otherProperty.objectType) && 
/* 271 */       ObjectUtils.nullSafeEquals(this.name, otherProperty.name) && 
/* 272 */       ObjectUtils.nullSafeEquals(this.readMethod, otherProperty.readMethod) && 
/* 273 */       ObjectUtils.nullSafeEquals(this.writeMethod, otherProperty.writeMethod));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 278 */     return ObjectUtils.nullSafeHashCode(this.objectType) * 31 + ObjectUtils.nullSafeHashCode(this.name);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */