/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.convert.converter.ConverterFactory;
/*    */ import org.springframework.core.convert.converter.ConverterRegistry;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ConversionServiceFactory
/*    */ {
/*    */   public static void registerConverters(@Nullable Set<?> converters, ConverterRegistry registry) {
/* 49 */     if (converters != null)
/* 50 */       for (Object converter : converters) {
/* 51 */         if (converter instanceof GenericConverter) {
/* 52 */           registry.addConverter((GenericConverter)converter); continue;
/*    */         } 
/* 54 */         if (converter instanceof Converter) {
/* 55 */           registry.addConverter((Converter)converter); continue;
/*    */         } 
/* 57 */         if (converter instanceof ConverterFactory) {
/* 58 */           registry.addConverterFactory((ConverterFactory)converter);
/*    */           continue;
/*    */         } 
/* 61 */         throw new IllegalArgumentException("Each converter object must implement one of the Converter, ConverterFactory, or GenericConverter interfaces");
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ConversionServiceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */