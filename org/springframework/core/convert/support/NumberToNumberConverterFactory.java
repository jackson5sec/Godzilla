/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalConverter;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.convert.converter.ConverterFactory;
/*    */ import org.springframework.util.NumberUtils;
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
/*    */ 
/*    */ final class NumberToNumberConverterFactory
/*    */   implements ConverterFactory<Number, Number>, ConditionalConverter
/*    */ {
/*    */   public <T extends Number> Converter<Number, T> getConverter(Class<T> targetType) {
/* 47 */     return new NumberToNumber<>(targetType);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 52 */     return !sourceType.equals(targetType);
/*    */   }
/*    */   
/*    */   private static final class NumberToNumber<T extends Number>
/*    */     implements Converter<Number, T>
/*    */   {
/*    */     private final Class<T> targetType;
/*    */     
/*    */     NumberToNumber(Class<T> targetType) {
/* 61 */       this.targetType = targetType;
/*    */     }
/*    */ 
/*    */     
/*    */     public T convert(Number source) {
/* 66 */       return (T)NumberUtils.convertNumberToTargetClass(source, this.targetType);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\NumberToNumberConverterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */