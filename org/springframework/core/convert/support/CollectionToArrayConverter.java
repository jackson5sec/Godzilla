/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class CollectionToArrayConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public CollectionToArrayConverter(ConversionService conversionService) {
/* 48 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 54 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, Object[].class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 59 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType
/* 60 */         .getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 66 */     if (source == null) {
/* 67 */       return null;
/*    */     }
/* 69 */     Collection<?> sourceCollection = (Collection)source;
/* 70 */     TypeDescriptor targetElementType = targetType.getElementTypeDescriptor();
/* 71 */     Assert.state((targetElementType != null), "No target element type");
/* 72 */     Object array = Array.newInstance(targetElementType.getType(), sourceCollection.size());
/* 73 */     int i = 0;
/* 74 */     for (Object sourceElement : sourceCollection) {
/* 75 */       Object targetElement = this.conversionService.convert(sourceElement, sourceType
/* 76 */           .elementTypeDescriptor(sourceElement), targetElementType);
/* 77 */       Array.set(array, i++, targetElement);
/*    */     } 
/* 79 */     return array;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\CollectionToArrayConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */