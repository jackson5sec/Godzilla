/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Properties;
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
/*    */ 
/*    */ final class PropertiesToStringConverter
/*    */   implements Converter<Properties, String>
/*    */ {
/*    */   public String convert(Properties source) {
/*    */     try {
/* 37 */       ByteArrayOutputStream os = new ByteArrayOutputStream(256);
/* 38 */       source.store(os, (String)null);
/* 39 */       return os.toString("ISO-8859-1");
/*    */     }
/* 41 */     catch (IOException ex) {
/*    */       
/* 43 */       throw new IllegalArgumentException("Failed to store [" + source + "] into String", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\PropertiesToStringConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */