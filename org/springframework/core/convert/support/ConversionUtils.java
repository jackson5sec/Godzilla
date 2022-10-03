/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionFailedException;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.GenericConverter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class ConversionUtils
/*    */ {
/*    */   @Nullable
/*    */   public static Object invokeConverter(GenericConverter converter, @Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*    */     try {
/* 41 */       return converter.convert(source, sourceType, targetType);
/*    */     }
/* 43 */     catch (ConversionFailedException ex) {
/* 44 */       throw ex;
/*    */     }
/* 46 */     catch (Throwable ex) {
/* 47 */       throw new ConversionFailedException(sourceType, targetType, source, ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean canConvertElements(@Nullable TypeDescriptor sourceElementType, @Nullable TypeDescriptor targetElementType, ConversionService conversionService) {
/* 54 */     if (targetElementType == null)
/*    */     {
/* 56 */       return true;
/*    */     }
/* 58 */     if (sourceElementType == null)
/*    */     {
/* 60 */       return true;
/*    */     }
/* 62 */     if (conversionService.canConvert(sourceElementType, targetElementType))
/*    */     {
/* 64 */       return true;
/*    */     }
/* 66 */     if (ClassUtils.isAssignable(sourceElementType.getType(), targetElementType.getType()))
/*    */     {
/* 68 */       return true;
/*    */     }
/*    */     
/* 71 */     return false;
/*    */   }
/*    */   
/*    */   public static Class<?> getEnumType(Class<?> targetType) {
/* 75 */     Class<?> enumType = targetType;
/* 76 */     while (enumType != null && !enumType.isEnum()) {
/* 77 */       enumType = enumType.getSuperclass();
/*    */     }
/* 79 */     Assert.notNull(enumType, () -> "The target type " + targetType.getName() + " does not refer to an enum");
/* 80 */     return enumType;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ConversionUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */