/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ final class CollectionToObjectConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public CollectionToObjectConverter(ConversionService conversionService) {
/* 39 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 44 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, Object.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 49 */     return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 55 */     if (source == null) {
/* 56 */       return null;
/*    */     }
/* 58 */     if (sourceType.isAssignableTo(targetType)) {
/* 59 */       return source;
/*    */     }
/* 61 */     Collection<?> sourceCollection = (Collection)source;
/* 62 */     if (sourceCollection.isEmpty()) {
/* 63 */       return null;
/*    */     }
/* 65 */     Object firstElement = sourceCollection.iterator().next();
/* 66 */     return this.conversionService.convert(firstElement, sourceType.elementTypeDescriptor(firstElement), targetType);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\CollectionToObjectConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */