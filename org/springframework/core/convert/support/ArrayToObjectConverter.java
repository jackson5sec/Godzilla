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
/*    */ final class ArrayToObjectConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ArrayToObjectConverter(ConversionService conversionService) {
/* 41 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 47 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object[].class, Object.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 52 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 58 */     if (source == null) {
/* 59 */       return null;
/*    */     }
/* 61 */     if (sourceType.isAssignableTo(targetType)) {
/* 62 */       return source;
/*    */     }
/* 64 */     if (Array.getLength(source) == 0) {
/* 65 */       return null;
/*    */     }
/* 67 */     Object firstElement = Array.get(source, 0);
/* 68 */     return this.conversionService.convert(firstElement, sourceType.elementTypeDescriptor(firstElement), targetType);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ArrayToObjectConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */