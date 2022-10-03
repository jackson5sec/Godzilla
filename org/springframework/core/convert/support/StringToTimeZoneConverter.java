/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.TimeZone;
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ class StringToTimeZoneConverter
/*    */   implements Converter<String, TimeZone>
/*    */ {
/*    */   public TimeZone convert(String source) {
/* 34 */     return StringUtils.parseTimeZoneString(source);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToTimeZoneConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */