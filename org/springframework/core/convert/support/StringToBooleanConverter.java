/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ final class StringToBooleanConverter
/*    */   implements Converter<String, Boolean>
/*    */ {
/* 34 */   private static final Set<String> trueValues = new HashSet<>(8);
/*    */   
/* 36 */   private static final Set<String> falseValues = new HashSet<>(8);
/*    */   
/*    */   static {
/* 39 */     trueValues.add("true");
/* 40 */     trueValues.add("on");
/* 41 */     trueValues.add("yes");
/* 42 */     trueValues.add("1");
/*    */     
/* 44 */     falseValues.add("false");
/* 45 */     falseValues.add("off");
/* 46 */     falseValues.add("no");
/* 47 */     falseValues.add("0");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Boolean convert(String source) {
/* 54 */     String value = source.trim();
/* 55 */     if (value.isEmpty()) {
/* 56 */       return null;
/*    */     }
/* 58 */     value = value.toLowerCase();
/* 59 */     if (trueValues.contains(value)) {
/* 60 */       return Boolean.TRUE;
/*    */     }
/* 62 */     if (falseValues.contains(value)) {
/* 63 */       return Boolean.FALSE;
/*    */     }
/*    */     
/* 66 */     throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToBooleanConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */