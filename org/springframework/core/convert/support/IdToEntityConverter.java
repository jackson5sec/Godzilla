/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ final class IdToEntityConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*     */   private final ConversionService conversionService;
/*     */   
/*     */   public IdToEntityConverter(ConversionService conversionService) {
/*  49 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/*  55 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  60 */     Method finder = getFinder(targetType.getType());
/*  61 */     return (finder != null && this.conversionService
/*  62 */       .canConvert(sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0])));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  68 */     if (source == null) {
/*  69 */       return null;
/*     */     }
/*  71 */     Method finder = getFinder(targetType.getType());
/*  72 */     Assert.state((finder != null), "No finder method");
/*  73 */     Object id = this.conversionService.convert(source, sourceType, 
/*  74 */         TypeDescriptor.valueOf(finder.getParameterTypes()[0]));
/*  75 */     return ReflectionUtils.invokeMethod(finder, source, new Object[] { id });
/*     */   } @Nullable
/*     */   private Method getFinder(Class<?> entityClass) {
/*     */     Method[] methods;
/*     */     boolean localOnlyFiltered;
/*  80 */     String finderMethod = "find" + getEntityName(entityClass);
/*     */ 
/*     */     
/*     */     try {
/*  84 */       methods = entityClass.getDeclaredMethods();
/*  85 */       localOnlyFiltered = true;
/*     */     }
/*  87 */     catch (SecurityException ex) {
/*     */ 
/*     */       
/*  90 */       methods = entityClass.getMethods();
/*  91 */       localOnlyFiltered = false;
/*     */     } 
/*  93 */     for (Method method : methods) {
/*  94 */       if (Modifier.isStatic(method.getModifiers()) && method.getName().equals(finderMethod) && method
/*  95 */         .getParameterCount() == 1 && method.getReturnType().equals(entityClass) && (localOnlyFiltered || method
/*  96 */         .getDeclaringClass().equals(entityClass))) {
/*  97 */         return method;
/*     */       }
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */   
/*     */   private String getEntityName(Class<?> entityClass) {
/* 104 */     String shortName = ClassUtils.getShortName(entityClass);
/* 105 */     int lastDot = shortName.lastIndexOf('.');
/* 106 */     if (lastDot != -1) {
/* 107 */       return shortName.substring(lastDot + 1);
/*     */     }
/*     */     
/* 110 */     return shortName;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\IdToEntityConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */