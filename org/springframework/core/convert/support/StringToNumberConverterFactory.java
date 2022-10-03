/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.convert.converter.ConverterFactory;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ final class StringToNumberConverterFactory
/*    */   implements ConverterFactory<String, Number>
/*    */ {
/*    */   public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
/* 46 */     return new StringToNumber<>(targetType);
/*    */   }
/*    */   
/*    */   private static final class StringToNumber<T extends Number>
/*    */     implements Converter<String, T>
/*    */   {
/*    */     private final Class<T> targetType;
/*    */     
/*    */     public StringToNumber(Class<T> targetType) {
/* 55 */       this.targetType = targetType;
/*    */     }
/*    */ 
/*    */     
/*    */     @Nullable
/*    */     public T convert(String source) {
/* 61 */       if (source.isEmpty()) {
/* 62 */         return null;
/*    */       }
/* 64 */       return (T)NumberUtils.parseNumber(source, this.targetType);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToNumberConverterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */