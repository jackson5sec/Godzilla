/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.io.StringWriter;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class FallbackObjectToStringConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 48 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Object.class, String.class));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 53 */     Class<?> sourceClass = sourceType.getObjectType();
/* 54 */     if (String.class == sourceClass)
/*    */     {
/* 56 */       return false;
/*    */     }
/* 58 */     return (CharSequence.class.isAssignableFrom(sourceClass) || StringWriter.class
/* 59 */       .isAssignableFrom(sourceClass) || 
/* 60 */       ObjectToObjectConverter.hasConversionMethodOrConstructor(sourceClass, String.class));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 66 */     return (source != null) ? source.toString() : null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\FallbackObjectToStringConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */