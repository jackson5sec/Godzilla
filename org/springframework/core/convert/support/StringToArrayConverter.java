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
/*    */ import org.springframework.util.StringUtils;
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
/*    */ final class StringToArrayConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public StringToArrayConverter(ConversionService conversionService) {
/* 44 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 50 */     return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, Object[].class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 55 */     return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 62 */     if (source == null) {
/* 63 */       return null;
/*    */     }
/* 65 */     String string = (String)source;
/* 66 */     String[] fields = StringUtils.commaDelimitedListToStringArray(string);
/* 67 */     TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
/* 68 */     Assert.state((targetElementType != null), "No target element type");
/* 69 */     Object target = Array.newInstance(targetElementType.getType(), fields.length);
/* 70 */     for (int i = 0; i < fields.length; i++) {
/* 71 */       String sourceElement = fields[i];
/* 72 */       Object targetElement = this.conversionService.convert(sourceElement.trim(), sourceType, targetElementType);
/* 73 */       Array.set(target, i, targetElement);
/*    */     } 
/* 75 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToArrayConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */