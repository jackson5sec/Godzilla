/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Currency;
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
/*    */ 
/*    */ 
/*    */ class StringToCurrencyConverter
/*    */   implements Converter<String, Currency>
/*    */ {
/*    */   public Currency convert(String source) {
/* 33 */     return Currency.getInstance(source);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToCurrencyConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */