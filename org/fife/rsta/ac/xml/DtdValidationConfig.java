/*    */ package org.fife.rsta.ac.xml;
/*    */ 
/*    */ import javax.xml.parsers.SAXParserFactory;
/*    */ import org.xml.sax.EntityResolver;
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
/*    */ public class DtdValidationConfig
/*    */   implements ValidationConfig
/*    */ {
/*    */   private EntityResolver entityResolver;
/*    */   
/*    */   public DtdValidationConfig(EntityResolver entityResolver) {
/* 29 */     this.entityResolver = entityResolver;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void configureParser(XmlParser parser) {
/* 35 */     SAXParserFactory spf = parser.getSaxParserFactory();
/* 36 */     spf.setValidating(true);
/* 37 */     spf.setSchema(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void configureHandler(XmlParser.Handler handler) {
/* 43 */     handler.setEntityResolver(this.entityResolver);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\xml\DtdValidationConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */