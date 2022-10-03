/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import java.util.StringJoiner;
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
/*    */ final class CollectionToStringConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private static final String DELIMITER = ",";
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public CollectionToStringConverter(ConversionService conversionService) {
/* 43 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 49 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Collection.class, String.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 54 */     return ConversionUtils.canConvertElements(sourceType
/* 55 */         .getElementTypeDescriptor(), targetType, this.conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 61 */     if (source == null) {
/* 62 */       return null;
/*    */     }
/* 64 */     Collection<?> sourceCollection = (Collection)source;
/* 65 */     if (sourceCollection.isEmpty()) {
/* 66 */       return "";
/*    */     }
/* 68 */     StringJoiner sj = new StringJoiner(",");
/* 69 */     for (Object sourceElement : sourceCollection) {
/* 70 */       Object targetElement = this.conversionService.convert(sourceElement, sourceType
/* 71 */           .elementTypeDescriptor(sourceElement), targetType);
/* 72 */       sj.add(String.valueOf(targetElement));
/*    */     } 
/* 74 */     return sj.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\CollectionToStringConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */