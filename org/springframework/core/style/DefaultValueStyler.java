/*     */ package org.springframework.core.style;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.StringJoiner;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultValueStyler
/*     */   implements ValueStyler
/*     */ {
/*     */   private static final String EMPTY = "[[empty]]";
/*     */   private static final String NULL = "[null]";
/*     */   private static final String COLLECTION = "collection";
/*     */   private static final String SET = "set";
/*     */   private static final String LIST = "list";
/*     */   private static final String MAP = "map";
/*     */   private static final String EMPTY_MAP = "map[[empty]]";
/*     */   private static final String ARRAY = "array";
/*     */   
/*     */   public String style(@Nullable Object value) {
/*  55 */     if (value == null) {
/*  56 */       return "[null]";
/*     */     }
/*  58 */     if (value instanceof String) {
/*  59 */       return "'" + value + "'";
/*     */     }
/*  61 */     if (value instanceof Class) {
/*  62 */       return ClassUtils.getShortName((Class)value);
/*     */     }
/*  64 */     if (value instanceof Method) {
/*  65 */       Method method = (Method)value;
/*  66 */       return method.getName() + "@" + ClassUtils.getShortName(method.getDeclaringClass());
/*     */     } 
/*  68 */     if (value instanceof Map) {
/*  69 */       return style((Map<?, ?>)value);
/*     */     }
/*  71 */     if (value instanceof Map.Entry) {
/*  72 */       return style((Map.Entry<?, ?>)value);
/*     */     }
/*  74 */     if (value instanceof Collection) {
/*  75 */       return style((Collection)value);
/*     */     }
/*  77 */     if (value.getClass().isArray()) {
/*  78 */       return styleArray(ObjectUtils.toObjectArray(value));
/*     */     }
/*     */     
/*  81 */     return String.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   private <K, V> String style(Map<K, V> value) {
/*  86 */     if (value.isEmpty()) {
/*  87 */       return "map[[empty]]";
/*     */     }
/*     */     
/*  90 */     StringJoiner result = new StringJoiner(", ", "[", "]");
/*  91 */     for (Map.Entry<K, V> entry : value.entrySet()) {
/*  92 */       result.add(style(entry));
/*     */     }
/*  94 */     return "map" + result;
/*     */   }
/*     */   
/*     */   private String style(Map.Entry<?, ?> value) {
/*  98 */     return style(value.getKey()) + " -> " + style(value.getValue());
/*     */   }
/*     */   
/*     */   private String style(Collection<?> value) {
/* 102 */     String collectionType = getCollectionTypeString(value);
/*     */     
/* 104 */     if (value.isEmpty()) {
/* 105 */       return collectionType + "[[empty]]";
/*     */     }
/*     */     
/* 108 */     StringJoiner result = new StringJoiner(", ", "[", "]");
/* 109 */     for (Object o : value) {
/* 110 */       result.add(style(o));
/*     */     }
/* 112 */     return collectionType + result;
/*     */   }
/*     */   
/*     */   private String getCollectionTypeString(Collection<?> value) {
/* 116 */     if (value instanceof java.util.List) {
/* 117 */       return "list";
/*     */     }
/* 119 */     if (value instanceof java.util.Set) {
/* 120 */       return "set";
/*     */     }
/*     */     
/* 123 */     return "collection";
/*     */   }
/*     */ 
/*     */   
/*     */   private String styleArray(Object[] array) {
/* 128 */     if (array.length == 0) {
/* 129 */       return "array<" + ClassUtils.getShortName(array.getClass().getComponentType()) + '>' + "[[empty]]";
/*     */     }
/*     */     
/* 132 */     StringJoiner result = new StringJoiner(", ", "[", "]");
/* 133 */     for (Object o : array) {
/* 134 */       result.add(style(o));
/*     */     }
/* 136 */     return "array<" + ClassUtils.getShortName(array.getClass().getComponentType()) + '>' + result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\style\DefaultValueStyler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */