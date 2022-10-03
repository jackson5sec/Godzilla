/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.convert.converter.ConverterFactory;
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
/*    */ final class StringToEnumConverterFactory
/*    */   implements ConverterFactory<String, Enum>
/*    */ {
/*    */   public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
/* 35 */     return new StringToEnum<>((Class)ConversionUtils.getEnumType(targetType));
/*    */   }
/*    */   
/*    */   private static class StringToEnum<T extends Enum>
/*    */     implements Converter<String, T>
/*    */   {
/*    */     private final Class<T> enumType;
/*    */     
/*    */     StringToEnum(Class<T> enumType) {
/* 44 */       this.enumType = enumType;
/*    */     }
/*    */ 
/*    */     
/*    */     @Nullable
/*    */     public T convert(String source) {
/* 50 */       if (source.isEmpty())
/*    */       {
/* 52 */         return null;
/*    */       }
/* 54 */       return (T)Enum.valueOf(this.enumType, source.trim());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToEnumConverterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */