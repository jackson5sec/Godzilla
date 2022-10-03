/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*    */ import org.springframework.core.convert.converter.GenericConverter;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ final class ArrayToArrayConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final CollectionToArrayConverter helperConverter;
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ArrayToArrayConverter(ConversionService conversionService) {
/* 47 */     this.helperConverter = new CollectionToArrayConverter(conversionService);
/* 48 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 54 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object[].class, Object[].class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 59 */     return this.helperConverter.matches(sourceType, targetType);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 65 */     if (this.conversionService instanceof GenericConversionService) {
/* 66 */       TypeDescriptor targetElement = targetType.getElementTypeDescriptor();
/* 67 */       if (targetElement != null && ((GenericConversionService)this.conversionService)
/* 68 */         .canBypassConvert(sourceType
/* 69 */           .getElementTypeDescriptor(), targetElement)) {
/* 70 */         return source;
/*    */       }
/*    */     } 
/* 73 */     List<Object> sourceList = Arrays.asList(ObjectUtils.toObjectArray(source));
/* 74 */     return this.helperConverter.convert(sourceList, sourceType, targetType);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ArrayToArrayConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */