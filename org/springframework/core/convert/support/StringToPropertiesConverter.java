/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ final class StringToPropertiesConverter
/*    */   implements Converter<String, Properties>
/*    */ {
/*    */   public Properties convert(String source) {
/*    */     try {
/* 37 */       Properties props = new Properties();
/*    */       
/* 39 */       props.load(new ByteArrayInputStream(source.getBytes(StandardCharsets.ISO_8859_1)));
/* 40 */       return props;
/*    */     }
/* 42 */     catch (Exception ex) {
/*    */       
/* 44 */       throw new IllegalArgumentException("Failed to parse [" + source + "] into Properties", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StringToPropertiesConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */