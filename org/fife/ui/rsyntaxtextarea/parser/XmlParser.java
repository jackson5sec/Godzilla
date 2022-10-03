/*     */ package org.fife.ui.rsyntaxtextarea.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.fife.io.DocumentReader;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlParser
/*     */   extends AbstractParser
/*     */ {
/*     */   private SAXParserFactory spf;
/*     */   private DefaultParseResult result;
/*     */   private EntityResolver entityResolver;
/*     */   
/*     */   public XmlParser() {
/*  68 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlParser(EntityResolver resolver) {
/*  79 */     this.entityResolver = resolver;
/*  80 */     this.result = new DefaultParseResult(this);
/*     */     try {
/*  82 */       this.spf = SAXParserFactory.newInstance();
/*  83 */     } catch (FactoryConfigurationError fce) {
/*  84 */       fce.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidating() {
/*  96 */     return this.spf.isValidating();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseResult parse(RSyntaxDocument doc, String style) {
/* 103 */     this.result.clearNotices();
/* 104 */     Element root = doc.getDefaultRootElement();
/* 105 */     this.result.setParsedLines(0, root.getElementCount() - 1);
/*     */     
/* 107 */     if (this.spf == null || doc.getLength() == 0) {
/* 108 */       return this.result;
/*     */     }
/*     */     
/*     */     try {
/* 112 */       SAXParser sp = this.spf.newSAXParser();
/* 113 */       Handler handler = new Handler((Document)doc);
/* 114 */       DocumentReader r = new DocumentReader((Document)doc);
/* 115 */       InputSource input = new InputSource((Reader)r);
/* 116 */       sp.parse(input, handler);
/* 117 */       r.close();
/* 118 */     } catch (SAXParseException sAXParseException) {
/*     */     
/* 120 */     } catch (Exception e) {
/*     */       
/* 122 */       this.result.addNotice(new DefaultParserNotice(this, "Error parsing XML: " + e
/* 123 */             .getMessage(), 0, -1, -1));
/*     */     } 
/*     */     
/* 126 */     return this.result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidating(boolean validating) {
/* 140 */     this.spf.setValidating(validating);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class Handler
/*     */     extends DefaultHandler
/*     */   {
/*     */     private Document doc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Handler(Document doc) {
/* 181 */       this.doc = doc;
/*     */     }
/*     */     
/*     */     private void doError(SAXParseException e, ParserNotice.Level level) {
/* 185 */       int line = e.getLineNumber() - 1;
/* 186 */       Element root = this.doc.getDefaultRootElement();
/* 187 */       Element elem = root.getElement(line);
/* 188 */       int offs = elem.getStartOffset();
/* 189 */       int len = elem.getEndOffset() - offs;
/* 190 */       if (line == root.getElementCount() - 1) {
/* 191 */         len++;
/*     */       }
/*     */       
/* 194 */       DefaultParserNotice pn = new DefaultParserNotice(XmlParser.this, e.getMessage(), line, offs, len);
/* 195 */       pn.setLevel(level);
/* 196 */       XmlParser.this.result.addNotice(pn);
/*     */     }
/*     */ 
/*     */     
/*     */     public void error(SAXParseException e) {
/* 201 */       doError(e, ParserNotice.Level.ERROR);
/*     */     }
/*     */ 
/*     */     
/*     */     public void fatalError(SAXParseException e) {
/* 206 */       doError(e, ParserNotice.Level.ERROR);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
/* 212 */       if (XmlParser.this.entityResolver != null) {
/* 213 */         return XmlParser.this.entityResolver.resolveEntity(publicId, systemId);
/*     */       }
/* 215 */       return super.resolveEntity(publicId, systemId);
/*     */     }
/*     */ 
/*     */     
/*     */     public void warning(SAXParseException e) {
/* 220 */       doError(e, ParserNotice.Level.WARNING);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\parser\XmlParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */