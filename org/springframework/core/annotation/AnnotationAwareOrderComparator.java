/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.core.OrderComparator;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationAwareOrderComparator
/*     */   extends OrderComparator
/*     */ {
/*  52 */   public static final AnnotationAwareOrderComparator INSTANCE = new AnnotationAwareOrderComparator();
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
/*     */   protected Integer findOrder(Object obj) {
/*  64 */     Integer order = super.findOrder(obj);
/*  65 */     if (order != null) {
/*  66 */       return order;
/*     */     }
/*  68 */     return findOrderFromAnnotation(obj);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Integer findOrderFromAnnotation(Object obj) {
/*  73 */     AnnotatedElement element = (obj instanceof AnnotatedElement) ? (AnnotatedElement)obj : obj.getClass();
/*  74 */     MergedAnnotations annotations = MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
/*  75 */     Integer order = OrderUtils.getOrderFromAnnotations(element, annotations);
/*  76 */     if (order == null && obj instanceof DecoratingProxy) {
/*  77 */       return findOrderFromAnnotation(((DecoratingProxy)obj).getDecoratedClass());
/*     */     }
/*  79 */     return order;
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
/*     */   public Integer getPriority(Object obj) {
/*  91 */     if (obj instanceof Class) {
/*  92 */       return OrderUtils.getPriority((Class)obj);
/*     */     }
/*  94 */     Integer priority = OrderUtils.getPriority(obj.getClass());
/*  95 */     if (priority == null && obj instanceof DecoratingProxy) {
/*  96 */       return getPriority(((DecoratingProxy)obj).getDecoratedClass());
/*     */     }
/*  98 */     return priority;
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
/*     */   public static void sort(List<?> list) {
/* 110 */     if (list.size() > 1) {
/* 111 */       list.sort((Comparator<?>)INSTANCE);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(Object[] array) {
/* 123 */     if (array.length > 1) {
/* 124 */       Arrays.sort(array, (Comparator<? super Object>)INSTANCE);
/*     */     }
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
/*     */   public static void sortIfNecessary(Object value) {
/* 137 */     if (value instanceof Object[]) {
/* 138 */       sort((Object[])value);
/*     */     }
/* 140 */     else if (value instanceof List) {
/* 141 */       sort((List)value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AnnotationAwareOrderComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */