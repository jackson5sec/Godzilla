/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.util.Map;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OrderUtils
/*     */ {
/*  39 */   private static final Object NOT_ANNOTATED = new Object();
/*     */ 
/*     */   
/*     */   private static final String JAVAX_PRIORITY_ANNOTATION = "javax.annotation.Priority";
/*     */   
/*  44 */   private static final Map<AnnotatedElement, Object> orderCache = (Map<AnnotatedElement, Object>)new ConcurrentReferenceHashMap(64);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getOrder(Class<?> type, int defaultOrder) {
/*  57 */     Integer order = getOrder(type);
/*  58 */     return (order != null) ? order.intValue() : defaultOrder;
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
/*     */   @Nullable
/*     */   public static Integer getOrder(Class<?> type, @Nullable Integer defaultOrder) {
/*  71 */     Integer order = getOrder(type);
/*  72 */     return (order != null) ? order : defaultOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Integer getOrder(Class<?> type) {
/*  84 */     return getOrder(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Integer getOrder(AnnotatedElement element) {
/*  96 */     return getOrderFromAnnotations(element, MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY));
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
/*     */   @Nullable
/*     */   static Integer getOrderFromAnnotations(AnnotatedElement element, MergedAnnotations annotations) {
/* 109 */     if (!(element instanceof Class)) {
/* 110 */       return findOrder(annotations);
/*     */     }
/* 112 */     Object cached = orderCache.get(element);
/* 113 */     if (cached != null) {
/* 114 */       return (cached instanceof Integer) ? (Integer)cached : null;
/*     */     }
/* 116 */     Integer result = findOrder(annotations);
/* 117 */     orderCache.put(element, (result != null) ? result : NOT_ANNOTATED);
/* 118 */     return result;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static Integer findOrder(MergedAnnotations annotations) {
/* 123 */     MergedAnnotation<Order> orderAnnotation = annotations.get(Order.class);
/* 124 */     if (orderAnnotation.isPresent()) {
/* 125 */       return Integer.valueOf(orderAnnotation.getInt("value"));
/*     */     }
/* 127 */     MergedAnnotation<?> priorityAnnotation = annotations.get("javax.annotation.Priority");
/* 128 */     if (priorityAnnotation.isPresent()) {
/* 129 */       return Integer.valueOf(priorityAnnotation.getInt("value"));
/*     */     }
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Integer getPriority(Class<?> type) {
/* 142 */     return MergedAnnotations.from(type, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).<Annotation>get("javax.annotation.Priority")
/* 143 */       .getValue("value", Integer.class).orElse(null);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\OrderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */