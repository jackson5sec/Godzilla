/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ final class StringToLocaleConverter
/*    */   implements Converter<String, Locale>
/*    */ {
/*    */   @Nullable
/*    */   public Locale convert(String source) {
/* 41 */     return StringUtils.parseLocale(source);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToLocaleConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */