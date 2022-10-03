/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*    */ import org.springframework.core.convert.converter.GenericConverter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ final class ObjectToArrayConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ObjectToArrayConverter(ConversionService conversionService) {
/* 43 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 49 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Object[].class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 54 */     return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 61 */     if (source == null) {
/* 62 */       return null;
/*    */     }
/* 64 */     TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
/* 65 */     Assert.state((targetElementType != null), "No target element type");
/* 66 */     Object target = Array.newInstance(targetElementType.getType(), 1);
/* 67 */     Object targetElement = this.conversionService.convert(source, sourceType, targetElementType);
/* 68 */     Array.set(target, 0, targetElement);
/* 69 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ObjectToArrayConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */