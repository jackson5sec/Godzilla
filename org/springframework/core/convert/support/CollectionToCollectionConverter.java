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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class CollectionToCollectionConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public CollectionToCollectionConverter(ConversionService conversionService) {
/* 47 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 53 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, Collection.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 58 */     return ConversionUtils.canConvertElements(sourceType
/* 59 */         .getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 65 */     if (source == null) {
/* 66 */       return null;
/*    */     }
/* 68 */     Collection<?> sourceCollection = (Collection)source;
/*    */ 
/*    */     
/* 71 */     boolean copyRequired = !targetType.getType().isInstance(source);
/* 72 */     if (!copyRequired && sourceCollection.isEmpty()) {
/* 73 */       return source;
/*    */     }
/* 75 */     TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
/* 76 */     if (elementDesc == null && !copyRequired) {
/* 77 */       return source;
/*    */     }
/*    */ 
/*    */     
/* 81 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), (elementDesc != null) ? elementDesc
/* 82 */         .getType() : null, sourceCollection.size());
/*    */     
/* 84 */     if (elementDesc == null) {
/* 85 */       target.addAll(sourceCollection);
/*    */     } else {
/*    */       
/* 88 */       for (Object sourceElement : sourceCollection) {
/* 89 */         Object targetElement = this.conversionService.convert(sourceElement, sourceType
/* 90 */             .elementTypeDescriptor(sourceElement), elementDesc);
/* 91 */         target.add(targetElement);
/* 92 */         if (sourceElement != targetElement) {
/* 93 */           copyRequired = true;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 98 */     return copyRequired ? target : source;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\CollectionToCollectionConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */