/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.CollectionFactory;
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
/*    */ 
/*    */ final class ObjectToCollectionConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ObjectToCollectionConverter(ConversionService conversionService) {
/* 43 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 49 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, Collection.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 54 */     return ConversionUtils.canConvertElements(sourceType, targetType.getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 60 */     if (source == null) {
/* 61 */       return null;
/*    */     }
/*    */     
/* 64 */     TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
/* 65 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), (elementDesc != null) ? elementDesc
/* 66 */         .getType() : null, 1);
/*    */     
/* 68 */     if (elementDesc == null || elementDesc.isCollection()) {
/* 69 */       target.add(source);
/*    */     } else {
/*    */       
/* 72 */       Object singleElement = this.conversionService.convert(source, sourceType, elementDesc);
/* 73 */       target.add(singleElement);
/*    */     } 
/* 75 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ObjectToCollectionConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */