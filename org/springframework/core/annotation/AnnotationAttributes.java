/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationAttributes
/*     */   extends LinkedHashMap<String, Object>
/*     */ {
/*     */   private static final String UNKNOWN = "unknown";
/*     */   @Nullable
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   final String displayName;
/*     */   boolean validated = false;
/*     */   
/*     */   public AnnotationAttributes() {
/*  63 */     this.annotationType = null;
/*  64 */     this.displayName = "unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationAttributes(int initialCapacity) {
/*  73 */     super(initialCapacity);
/*  74 */     this.annotationType = null;
/*  75 */     this.displayName = "unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationAttributes(Map<String, Object> map) {
/*  85 */     super(map);
/*  86 */     this.annotationType = null;
/*  87 */     this.displayName = "unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationAttributes(AnnotationAttributes other) {
/*  97 */     super(other);
/*  98 */     this.annotationType = other.annotationType;
/*  99 */     this.displayName = other.displayName;
/* 100 */     this.validated = other.validated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationAttributes(Class<? extends Annotation> annotationType) {
/* 111 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 112 */     this.annotationType = annotationType;
/* 113 */     this.displayName = annotationType.getName();
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
/*     */   AnnotationAttributes(Class<? extends Annotation> annotationType, boolean validated) {
/* 126 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 127 */     this.annotationType = annotationType;
/* 128 */     this.displayName = annotationType.getName();
/* 129 */     this.validated = validated;
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
/*     */   public AnnotationAttributes(String annotationType, @Nullable ClassLoader classLoader) {
/* 142 */     Assert.notNull(annotationType, "'annotationType' must not be null");
/* 143 */     this.annotationType = getAnnotationType(annotationType, classLoader);
/* 144 */     this.displayName = annotationType;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Class<? extends Annotation> getAnnotationType(String annotationType, @Nullable ClassLoader classLoader) {
/* 150 */     if (classLoader != null) {
/*     */       try {
/* 152 */         return (Class)classLoader.loadClass(annotationType);
/*     */       }
/* 154 */       catch (ClassNotFoundException classNotFoundException) {}
/*     */     }
/*     */ 
/*     */     
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<? extends Annotation> annotationType() {
/* 169 */     return this.annotationType;
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
/*     */   public String getString(String attributeName) {
/* 181 */     return getRequiredAttribute(attributeName, String.class);
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
/*     */   public String[] getStringArray(String attributeName) {
/* 197 */     return getRequiredAttribute(attributeName, (Class)String[].class);
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
/*     */   public boolean getBoolean(String attributeName) {
/* 209 */     return ((Boolean)getRequiredAttribute(attributeName, Boolean.class)).booleanValue();
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
/*     */   public <N extends Number> N getNumber(String attributeName) {
/* 222 */     return (N)getRequiredAttribute(attributeName, Number.class);
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
/*     */   public <E extends Enum<?>> E getEnum(String attributeName) {
/* 235 */     return (E)getRequiredAttribute(attributeName, Enum.class);
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
/*     */   public <T> Class<? extends T> getClass(String attributeName) {
/* 248 */     return getRequiredAttribute(attributeName, (Class)Class.class);
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
/*     */   public Class<?>[] getClassArray(String attributeName) {
/* 263 */     return getRequiredAttribute(attributeName, (Class)Class[].class);
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
/*     */   public AnnotationAttributes getAnnotation(String attributeName) {
/* 278 */     return getRequiredAttribute(attributeName, AnnotationAttributes.class);
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
/*     */   public <A extends Annotation> A getAnnotation(String attributeName, Class<A> annotationType) {
/* 293 */     return (A)getRequiredAttribute(attributeName, annotationType);
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
/*     */   
/*     */   public AnnotationAttributes[] getAnnotationArray(String attributeName) {
/* 311 */     return getRequiredAttribute(attributeName, (Class)AnnotationAttributes[].class);
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
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A[] getAnnotationArray(String attributeName, Class<A> annotationType) {
/* 330 */     Object array = Array.newInstance(annotationType, 0);
/* 331 */     return (A[])getRequiredAttribute(attributeName, array.getClass());
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
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> T getRequiredAttribute(String attributeName, Class<T> expectedType) {
/* 351 */     Assert.hasText(attributeName, "'attributeName' must not be null or empty");
/* 352 */     Object value = get(attributeName);
/* 353 */     assertAttributePresence(attributeName, value);
/* 354 */     assertNotException(attributeName, value);
/* 355 */     if (!expectedType.isInstance(value) && expectedType.isArray() && expectedType
/* 356 */       .getComponentType().isInstance(value)) {
/* 357 */       Object array = Array.newInstance(expectedType.getComponentType(), 1);
/* 358 */       Array.set(array, 0, value);
/* 359 */       value = array;
/*     */     } 
/* 361 */     assertAttributeType(attributeName, value, expectedType);
/* 362 */     return (T)value;
/*     */   }
/*     */   
/*     */   private void assertAttributePresence(String attributeName, Object attributeValue) {
/* 366 */     Assert.notNull(attributeValue, () -> String.format("Attribute '%s' not found in attributes for annotation [%s]", new Object[] { attributeName, this.displayName }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertNotException(String attributeName, Object attributeValue) {
/* 372 */     if (attributeValue instanceof Throwable) {
/* 373 */       throw new IllegalArgumentException(String.format("Attribute '%s' for annotation [%s] was not resolvable due to exception [%s]", new Object[] { attributeName, this.displayName, attributeValue }), (Throwable)attributeValue);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertAttributeType(String attributeName, Object attributeValue, Class<?> expectedType) {
/* 380 */     if (!expectedType.isInstance(attributeValue)) {
/* 381 */       throw new IllegalArgumentException(String.format("Attribute '%s' is of type %s, but %s was expected in attributes for annotation [%s]", new Object[] { attributeName, attributeValue
/*     */               
/* 383 */               .getClass().getSimpleName(), expectedType.getSimpleName(), this.displayName }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 390 */     Iterator<Map.Entry<String, Object>> entries = entrySet().iterator();
/* 391 */     StringBuilder sb = new StringBuilder("{");
/* 392 */     while (entries.hasNext()) {
/* 393 */       Map.Entry<String, Object> entry = entries.next();
/* 394 */       sb.append(entry.getKey());
/* 395 */       sb.append('=');
/* 396 */       sb.append(valueToString(entry.getValue()));
/* 397 */       if (entries.hasNext()) {
/* 398 */         sb.append(", ");
/*     */       }
/*     */     } 
/* 401 */     sb.append('}');
/* 402 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private String valueToString(Object value) {
/* 406 */     if (value == this) {
/* 407 */       return "(this Map)";
/*     */     }
/* 409 */     if (value instanceof Object[]) {
/* 410 */       return "[" + StringUtils.arrayToDelimitedString((Object[])value, ", ") + "]";
/*     */     }
/* 412 */     return String.valueOf(value);
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
/*     */   @Nullable
/*     */   public static AnnotationAttributes fromMap(@Nullable Map<String, Object> map) {
/* 426 */     if (map == null) {
/* 427 */       return null;
/*     */     }
/* 429 */     if (map instanceof AnnotationAttributes) {
/* 430 */       return (AnnotationAttributes)map;
/*     */     }
/* 432 */     return new AnnotationAttributes(map);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AnnotationAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */