/*    */ package org.fife.rsta.ac.xml;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ import javax.xml.validation.Schema;
/*    */ import javax.xml.validation.SchemaFactory;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public class SchemaValidationConfig
/*    */   implements ValidationConfig
/*    */ {
/*    */   private Schema schema;
/*    */   
/*    */   public SchemaValidationConfig(String language, InputStream in) throws IOException {
/* 37 */     SchemaFactory sf = SchemaFactory.newInstance(language);
/*    */     
/* 39 */     try (BufferedInputStream bis = new BufferedInputStream(in)) {
/* 40 */       this.schema = sf.newSchema(new StreamSource(bis));
/* 41 */     } catch (SAXException se) {
/* 42 */       se.printStackTrace();
/* 43 */       throw new IOException(se.toString());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void configureParser(XmlParser parser) {
/* 51 */     SAXParserFactory spf = parser.getSaxParserFactory();
/* 52 */     spf.setValidating(false);
/* 53 */     if (this.schema != null) {
/* 54 */       spf.setSchema(this.schema);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void configureHandler(XmlParser.Handler handler) {
/* 61 */     handler.setEntityResolver(null);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\xml\SchemaValidationConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */