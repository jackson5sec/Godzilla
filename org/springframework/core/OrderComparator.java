/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OrderComparator
/*     */   implements Comparator<Object>
/*     */ {
/*  58 */   public static final OrderComparator INSTANCE = new OrderComparator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<Object> withSourceProvider(OrderSourceProvider sourceProvider) {
/*  68 */     return (o1, o2) -> doCompare(o1, o2, sourceProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compare(@Nullable Object o1, @Nullable Object o2) {
/*  73 */     return doCompare(o1, o2, null);
/*     */   }
/*     */   
/*     */   private int doCompare(@Nullable Object o1, @Nullable Object o2, @Nullable OrderSourceProvider sourceProvider) {
/*  77 */     boolean p1 = o1 instanceof PriorityOrdered;
/*  78 */     boolean p2 = o2 instanceof PriorityOrdered;
/*  79 */     if (p1 && !p2) {
/*  80 */       return -1;
/*     */     }
/*  82 */     if (p2 && !p1) {
/*  83 */       return 1;
/*     */     }
/*     */     
/*  86 */     int i1 = getOrder(o1, sourceProvider);
/*  87 */     int i2 = getOrder(o2, sourceProvider);
/*  88 */     return Integer.compare(i1, i2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getOrder(@Nullable Object obj, @Nullable OrderSourceProvider sourceProvider) {
/*  99 */     Integer order = null;
/* 100 */     if (obj != null && sourceProvider != null) {
/* 101 */       Object orderSource = sourceProvider.getOrderSource(obj);
/* 102 */       if (orderSource != null) {
/* 103 */         if (orderSource.getClass().isArray()) {
/* 104 */           for (Object source : ObjectUtils.toObjectArray(orderSource)) {
/* 105 */             order = findOrder(source);
/* 106 */             if (order != null) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } else {
/*     */           
/* 112 */           order = findOrder(orderSource);
/*     */         } 
/*     */       }
/*     */     } 
/* 116 */     return (order != null) ? order.intValue() : getOrder(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getOrder(@Nullable Object obj) {
/* 127 */     if (obj != null) {
/* 128 */       Integer order = findOrder(obj);
/* 129 */       if (order != null) {
/* 130 */         return order.intValue();
/*     */       }
/*     */     } 
/* 133 */     return Integer.MAX_VALUE;
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
/*     */   protected Integer findOrder(Object obj) {
/* 145 */     return (obj instanceof Ordered) ? Integer.valueOf(((Ordered)obj).getOrder()) : null;
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
/*     */   @Nullable
/*     */   public Integer getPriority(Object obj) {
/* 161 */     return null;
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
/* 173 */     if (list.size() > 1) {
/* 174 */       list.sort(INSTANCE);
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
/* 186 */     if (array.length > 1) {
/* 187 */       Arrays.sort(array, INSTANCE);
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
/* 200 */     if (value instanceof Object[]) {
/* 201 */       sort((Object[])value);
/*     */     }
/* 203 */     else if (value instanceof List) {
/* 204 */       sort((List)value);
/*     */     } 
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OrderSourceProvider {
/*     */     @Nullable
/*     */     Object getOrderSource(Object param1Object);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\OrderComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */