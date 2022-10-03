/*    */ package org.springframework.util.xml;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public class SimpleSaxErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/*    */   private final Log logger;
/*    */   
/*    */   public SimpleSaxErrorHandler(Log logger) {
/* 42 */     this.logger = logger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void warning(SAXParseException ex) throws SAXException {
/* 48 */     this.logger.warn("Ignored XML validation warning", ex);
/*    */   }
/*    */ 
/*    */   
/*    */   public void error(SAXParseException ex) throws SAXException {
/* 53 */     throw ex;
/*    */   }
/*    */ 
/*    */   
/*    */   public void fatalError(SAXParseException ex) throws SAXException {
/* 58 */     throw ex;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\SimpleSaxErrorHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */