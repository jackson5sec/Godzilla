/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.lang.reflect.Array;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ArrayToCollectionConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ArrayToCollectionConverter(ConversionService conversionService) {
/* 48 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 54 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object[].class, Collection.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 59 */     return ConversionUtils.canConvertElements(sourceType
/* 60 */         .getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 66 */     if (source == null) {
/* 67 */       return null;
/*    */     }
/*    */     
/* 70 */     int length = Array.getLength(source);
/* 71 */     TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
/* 72 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), (elementDesc != null) ? elementDesc
/* 73 */         .getType() : null, length);
/*    */     
/* 75 */     if (elementDesc == null) {
/* 76 */       for (int i = 0; i < length; i++) {
/* 77 */         Object sourceElement = Array.get(source, i);
/* 78 */         target.add(sourceElement);
/*    */       } 
/*    */     } else {
/*    */       
/* 82 */       for (int i = 0; i < length; i++) {
/* 83 */         Object sourceElement = Array.get(source, i);
/* 84 */         Object targetElement = this.conversionService.convert(sourceElement, sourceType
/* 85 */             .elementTypeDescriptor(sourceElement), elementDesc);
/* 86 */         target.add(targetElement);
/*    */       } 
/*    */     } 
/* 89 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ArrayToCollectionConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */