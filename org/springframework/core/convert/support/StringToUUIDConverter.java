/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.UUID;
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
/*    */ final class StringToUUIDConverter
/*    */   implements Converter<String, UUID>
/*    */ {
/*    */   @Nullable
/*    */   public UUID convert(String source) {
/* 37 */     return StringUtils.hasText(source) ? UUID.fromString(source.trim()) : null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToUUIDConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */