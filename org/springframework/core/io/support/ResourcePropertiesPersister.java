/*    */ package org.springframework.core.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.util.Properties;
/*    */ import org.springframework.core.SpringProperties;
/*    */ import org.springframework.util.DefaultPropertiesPersister;
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
/*    */ public class ResourcePropertiesPersister
/*    */   extends DefaultPropertiesPersister
/*    */ {
/* 45 */   public static final ResourcePropertiesPersister INSTANCE = new ResourcePropertiesPersister();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   private static final boolean shouldIgnoreXml = SpringProperties.getFlag("spring.xml.ignore");
/*    */ 
/*    */ 
/*    */   
/*    */   public void loadFromXml(Properties props, InputStream is) throws IOException {
/* 57 */     if (shouldIgnoreXml) {
/* 58 */       throw new UnsupportedOperationException("XML support disabled");
/*    */     }
/* 60 */     super.loadFromXml(props, is);
/*    */   }
/*    */ 
/*    */   
/*    */   public void storeToXml(Properties props, OutputStream os, String header) throws IOException {
/* 65 */     if (shouldIgnoreXml) {
/* 66 */       throw new UnsupportedOperationException("XML support disabled");
/*    */     }
/* 68 */     super.storeToXml(props, os, header);
/*    */   }
/*    */ 
/*    */   
/*    */   public void storeToXml(Properties props, OutputStream os, String header, String encoding) throws IOException {
/* 73 */     if (shouldIgnoreXml) {
/* 74 */       throw new UnsupportedOperationException("XML support disabled");
/*    */     }
/* 76 */     super.storeToXml(props, os, header, encoding);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\ResourcePropertiesPersister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */