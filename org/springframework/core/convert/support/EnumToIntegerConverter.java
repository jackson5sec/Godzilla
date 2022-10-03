/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ final class EnumToIntegerConverter
/*    */   extends AbstractConditionalEnumConverter
/*    */   implements Converter<Enum<?>, Integer>
/*    */ {
/*    */   public EnumToIntegerConverter(ConversionService conversionService) {
/* 32 */     super(conversionService);
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer convert(Enum<?> source) {
/* 37 */     return Integer.valueOf(source.ordinal());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\EnumToIntegerConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */