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
/*    */ 
/*    */ final class StringToCollectionConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public StringToCollectionConverter(ConversionService conversionService) {
/* 45 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 51 */     return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, Collection.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 56 */     return (targetType.getElementTypeDescriptor() == null || this.conversionService
/* 57 */       .canConvert(sourceType, targetType.getElementTypeDescriptor()));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 63 */     if (source == null) {
/* 64 */       return null;
/*    */     }
/* 66 */     String string = (String)source;
/*    */     
/* 68 */     String[] fields = StringUtils.commaDelimitedListToStringArray(string);
/* 69 */     TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
/* 70 */     Collection<Object> target = CollectionFactory.createCollection(targetType.getType(), (elementDesc != null) ? elementDesc
/* 71 */         .getType() : null, fields.length);
/*    */     
/* 73 */     if (elementDesc == null) {
/* 74 */       for (String field : fields) {
/* 75 */         target.add(field.trim());
/*    */       }
/*    */     } else {
/*    */       
/* 79 */       for (String field : fields) {
/* 80 */         Object targetElement = this.conversionService.convert(field.trim(), sourceType, elementDesc);
/* 81 */         target.add(targetElement);
/*    */       } 
/*    */     } 
/* 84 */     return target;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToCollectionConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */