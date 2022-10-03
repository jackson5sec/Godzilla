/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalConverter;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ abstract class AbstractConditionalEnumConverter
/*    */   implements ConditionalConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   protected AbstractConditionalEnumConverter(ConversionService conversionService) {
/* 36 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 42 */     for (Class<?> interfaceType : (Iterable<Class<?>>)ClassUtils.getAllInterfacesForClassAsSet(sourceType.getType())) {
/* 43 */       if (this.conversionService.canConvert(TypeDescriptor.valueOf(interfaceType), targetType)) {
/* 44 */         return false;
/*    */       }
/*    */     } 
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\AbstractConditionalEnumConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */