/*     */ package org.springframework.core.type.classreading;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
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
/*     */ 
/*     */ @Deprecated
/*     */ abstract class AnnotationReadingVisitorUtils
/*     */ {
/*     */   public static AnnotationAttributes convertClassValues(Object annotatedElement, @Nullable ClassLoader classLoader, AnnotationAttributes original, boolean classValuesAsString) {
/*  54 */     AnnotationAttributes result = new AnnotationAttributes(original);
/*  55 */     AnnotationUtils.postProcessAnnotationAttributes(annotatedElement, result, classValuesAsString);
/*     */     
/*  57 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)result.entrySet()) {
/*     */       try {
/*  59 */         Object value = entry.getValue();
/*  60 */         if (value instanceof AnnotationAttributes) {
/*  61 */           value = convertClassValues(annotatedElement, classLoader, (AnnotationAttributes)value, classValuesAsString);
/*     */         
/*     */         }
/*  64 */         else if (value instanceof AnnotationAttributes[]) {
/*  65 */           AnnotationAttributes[] values = (AnnotationAttributes[])value;
/*  66 */           for (int i = 0; i < values.length; i++) {
/*  67 */             values[i] = convertClassValues(annotatedElement, classLoader, values[i], classValuesAsString);
/*     */           }
/*  69 */           value = values;
/*     */         }
/*  71 */         else if (value instanceof Type) {
/*     */           
/*  73 */           value = classValuesAsString ? ((Type)value).getClassName() : ClassUtils.forName(((Type)value).getClassName(), classLoader);
/*     */         }
/*  75 */         else if (value instanceof Type[]) {
/*  76 */           Type[] array = (Type[])value;
/*  77 */           Object[] convArray = classValuesAsString ? (Object[])new String[array.length] : (Object[])new Class[array.length];
/*     */           
/*  79 */           for (int i = 0; i < array.length; i++) {
/*  80 */             convArray[i] = classValuesAsString ? array[i].getClassName() : 
/*  81 */               ClassUtils.forName(array[i].getClassName(), classLoader);
/*     */           }
/*  83 */           value = convArray;
/*     */         }
/*  85 */         else if (classValuesAsString) {
/*  86 */           if (value instanceof Class) {
/*  87 */             value = ((Class)value).getName();
/*     */           }
/*  89 */           else if (value instanceof Class[]) {
/*  90 */             Class<?>[] clazzArray = (Class[])value;
/*  91 */             String[] newValue = new String[clazzArray.length];
/*  92 */             for (int i = 0; i < clazzArray.length; i++) {
/*  93 */               newValue[i] = clazzArray[i].getName();
/*     */             }
/*  95 */             value = newValue;
/*     */           } 
/*     */         } 
/*  98 */         entry.setValue(value);
/*     */       }
/* 100 */       catch (Throwable ex) {
/*     */         
/* 102 */         result.put(entry.getKey(), ex);
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static AnnotationAttributes getMergedAnnotationAttributes(LinkedMultiValueMap<String, AnnotationAttributes> attributesMap, Map<String, Set<String>> metaAnnotationMap, String annotationName) {
/* 131 */     List<AnnotationAttributes> attributesList = attributesMap.get(annotationName);
/* 132 */     if (CollectionUtils.isEmpty(attributesList)) {
/* 133 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     AnnotationAttributes result = new AnnotationAttributes(attributesList.get(0));
/*     */     
/* 141 */     Set<String> overridableAttributeNames = new HashSet<>(result.keySet());
/* 142 */     overridableAttributeNames.remove("value");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     List<String> annotationTypes = new ArrayList<>(attributesMap.keySet());
/* 148 */     Collections.reverse(annotationTypes);
/*     */ 
/*     */     
/* 151 */     annotationTypes.remove(annotationName);
/*     */     
/* 153 */     for (String currentAnnotationType : annotationTypes) {
/* 154 */       List<AnnotationAttributes> currentAttributesList = attributesMap.get(currentAnnotationType);
/* 155 */       if (!ObjectUtils.isEmpty(currentAttributesList)) {
/* 156 */         Set<String> metaAnns = metaAnnotationMap.get(currentAnnotationType);
/* 157 */         if (metaAnns != null && metaAnns.contains(annotationName)) {
/* 158 */           AnnotationAttributes currentAttributes = currentAttributesList.get(0);
/* 159 */           for (String overridableAttributeName : overridableAttributeNames) {
/* 160 */             Object value = currentAttributes.get(overridableAttributeName);
/* 161 */             if (value != null)
/*     */             {
/*     */               
/* 164 */               result.put(overridableAttributeName, value);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\classreading\AnnotationReadingVisitorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */