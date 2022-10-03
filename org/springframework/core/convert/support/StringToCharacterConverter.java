/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ final class StringToCharacterConverter
/*    */   implements Converter<String, Character>
/*    */ {
/*    */   @Nullable
/*    */   public Character convert(String source) {
/* 33 */     if (source.isEmpty()) {
/* 34 */       return null;
/*    */     }
/* 36 */     if (source.length() > 1) {
/* 37 */       throw new IllegalArgumentException("Can only convert a [String] with length of 1 to a [Character]; string value '" + source + "'  has length of " + source
/* 38 */           .length());
/*    */     }
/* 40 */     return Character.valueOf(source.charAt(0));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToCharacterConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */