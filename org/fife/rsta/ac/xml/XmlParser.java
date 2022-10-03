/*     */ package org.fife.rsta.ac.xml;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Segment;
/*     */ import javax.swing.tree.MutableTreeNode;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.fife.io.DocumentReader;
/*     */ import org.fife.rsta.ac.xml.tree.XmlTreeNode;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class XmlParser
/*     */   extends AbstractParser
/*     */ {
/*     */   public static final String PROPERTY_AST = "XmlAST";
/*     */   private XmlLanguageSupport xls;
/*     */   private PropertyChangeSupport support;
/*     */   private XmlTreeNode curElem;
/*     */   private XmlTreeNode root;
/*     */   private Locator locator;
/*     */   private SAXParserFactory spf;
/*     */   private SAXParser sp;
/*     */   private ValidationConfig validationConfig;
/*     */   private int elemCount;
/*     */   
/*     */   public XmlParser(XmlLanguageSupport xls) {
/*  83 */     this.xls = xls;
/*  84 */     this.support = new PropertyChangeSupport(this);
/*     */     try {
/*  86 */       this.spf = SAXParserFactory.newInstance();
/*  87 */     } catch (FactoryConfigurationError fce) {
/*  88 */       fce.printStackTrace();
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
/*     */ 
/*     */   
/*     */   public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
/* 102 */     this.support.addPropertyChangeListener(prop, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlTreeNode getAst() {
/* 113 */     return this.root;
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
/*     */   private String getMainAttribute(Attributes attributes) {
/* 126 */     int nameIndex = -1;
/* 127 */     int idIndex = -1;
/*     */     int i;
/* 129 */     for (i = 0; i < attributes.getLength(); i++) {
/* 130 */       String name = attributes.getLocalName(i);
/* 131 */       if ("id".equals(name)) {
/* 132 */         idIndex = i;
/*     */         break;
/*     */       } 
/* 135 */       if ("name".equals(name)) {
/* 136 */         nameIndex = i;
/*     */       }
/*     */     } 
/*     */     
/* 140 */     i = idIndex;
/* 141 */     if (i == -1) {
/* 142 */       i = nameIndex;
/* 143 */       if (i == -1) {
/* 144 */         i = 0;
/*     */       }
/*     */     } 
/*     */     
/* 148 */     return attributes.getLocalName(i) + "=" + attributes.getValue(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SAXParserFactory getSaxParserFactory() {
/* 154 */     return this.spf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseResult parse(RSyntaxDocument doc, String style) {
/* 164 */     (new ValidationConfigSniffer()).sniff(doc);
/*     */     
/* 166 */     DefaultParseResult result = new DefaultParseResult((Parser)this);
/* 167 */     this.curElem = this.root = new XmlTreeNode("Root");
/*     */     
/* 169 */     if (this.spf == null || doc.getLength() == 0) {
/* 170 */       return (ParseResult)result;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 175 */       if (this.sp == null) {
/* 176 */         this.sp = this.spf.newSAXParser();
/*     */       }
/* 178 */       Handler handler = new Handler(doc, result);
/* 179 */       if (this.validationConfig != null) {
/* 180 */         this.validationConfig.configureHandler(handler);
/*     */       }
/* 182 */       DocumentReader r = new DocumentReader((Document)doc);
/* 183 */       InputSource input = new InputSource((Reader)r);
/* 184 */       this.sp.parse(input, handler);
/* 185 */       r.close();
/* 186 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 194 */     if (this.locator != null) {
/*     */       try {
/* 196 */         this.root.setStartOffset(doc.createPosition(0));
/* 197 */         this.root.setEndOffset(doc.createPosition(doc.getLength()));
/* 198 */       } catch (BadLocationException ble) {
/* 199 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 203 */     this.support.firePropertyChange("XmlAST", (Object)null, this.root);
/* 204 */     return (ParseResult)result;
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
/*     */   public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
/* 217 */     this.support.removePropertyChangeListener(prop, l);
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
/*     */   public void setValidationConfig(ValidationConfig config) {
/* 229 */     this.validationConfig = config;
/* 230 */     if (this.validationConfig != null) {
/* 231 */       this.validationConfig.configureParser(this);
/*     */     }
/* 233 */     this.sp = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public class Handler
/*     */     extends DefaultHandler
/*     */   {
/*     */     private DefaultParseResult result;
/*     */     
/*     */     private RSyntaxDocument doc;
/*     */     
/*     */     private Segment s;
/*     */     
/*     */     private EntityResolver entityResolver;
/*     */     
/*     */     public Handler(RSyntaxDocument doc, DefaultParseResult result) {
/* 249 */       this.doc = doc;
/* 250 */       this.result = result;
/* 251 */       this.s = new Segment();
/*     */     }
/*     */ 
/*     */     
/*     */     private void doError(SAXParseException e, ParserNotice.Level level) {
/* 256 */       if (!XmlParser.this.xls.getShowSyntaxErrors()) {
/*     */         return;
/*     */       }
/* 259 */       int line = e.getLineNumber() - 1;
/* 260 */       Element root = this.doc.getDefaultRootElement();
/* 261 */       Element elem = root.getElement(line);
/* 262 */       int offs = elem.getStartOffset();
/* 263 */       int len = elem.getEndOffset() - offs;
/* 264 */       if (line == root.getElementCount() - 1) {
/* 265 */         len++;
/*     */       }
/*     */       
/* 268 */       DefaultParserNotice pn = new DefaultParserNotice((Parser)XmlParser.this, e.getMessage(), line, offs, len);
/* 269 */       pn.setLevel(level);
/* 270 */       this.result.addNotice((ParserNotice)pn);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void endElement(String uri, String localName, String qName) {
/* 277 */       XmlParser.this.curElem = (XmlTreeNode)XmlParser.this.curElem.getParent();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void error(SAXParseException e) {
/* 283 */       doError(e, ParserNotice.Level.ERROR);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void fatalError(SAXParseException e) {
/* 289 */       doError(e, ParserNotice.Level.ERROR);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private int getTagStart(int end) {
/* 295 */       Element root = this.doc.getDefaultRootElement();
/* 296 */       int line = root.getElementIndex(end);
/* 297 */       Element elem = root.getElement(line);
/* 298 */       int start = elem.getStartOffset();
/* 299 */       int lastCharOffs = -1;
/*     */       
/*     */       try {
/* 302 */         while (line >= 0) {
/* 303 */           this.doc.getText(start, end - start, this.s);
/* 304 */           for (int i = this.s.offset + this.s.count - 1; i >= this.s.offset; i--) {
/* 305 */             char ch = this.s.array[i];
/* 306 */             if (ch == '<') {
/* 307 */               return lastCharOffs;
/*     */             }
/* 309 */             if (Character.isLetterOrDigit(ch)) {
/* 310 */               lastCharOffs = start + i - this.s.offset;
/*     */             }
/*     */           } 
/* 313 */           if (--line >= 0) {
/* 314 */             elem = root.getElement(line);
/* 315 */             start = elem.getStartOffset();
/* 316 */             end = elem.getEndOffset();
/*     */           } 
/*     */         } 
/* 319 */       } catch (BadLocationException ble) {
/* 320 */         ble.printStackTrace();
/*     */       } 
/*     */       
/* 323 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
/* 331 */       if (this.entityResolver != null) {
/* 332 */         return this.entityResolver.resolveEntity(publicId, systemId);
/*     */       }
/*     */       
/* 335 */       return new InputSource(new StringReader(" "));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDocumentLocator(Locator l) {
/* 342 */       XmlParser.this.locator = l;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setEntityResolver(EntityResolver resolver) {
/* 347 */       this.entityResolver = resolver;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(String uri, String localName, String qName, Attributes attributes) {
/* 355 */       XmlTreeNode newElem = new XmlTreeNode(qName);
/* 356 */       if (attributes.getLength() > 0) {
/* 357 */         newElem.setMainAttribute(XmlParser.this.getMainAttribute(attributes));
/*     */       }
/* 359 */       if (XmlParser.this.locator != null) {
/* 360 */         int line = XmlParser.this.locator.getLineNumber();
/* 361 */         if (line != -1) {
/*     */           
/* 363 */           int offs = this.doc.getDefaultRootElement().getElement(line - 1).getStartOffset();
/* 364 */           int col = XmlParser.this.locator.getColumnNumber();
/* 365 */           if (col != -1) {
/* 366 */             offs += col - 1;
/*     */           }
/*     */           
/* 369 */           offs = getTagStart(offs);
/*     */           try {
/* 371 */             newElem.setStartOffset(this.doc.createPosition(offs));
/* 372 */             int endOffs = offs + qName.length();
/* 373 */             newElem.setEndOffset(this.doc.createPosition(endOffs));
/* 374 */           } catch (BadLocationException ble) {
/* 375 */             ble.printStackTrace();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 380 */       XmlParser.this.curElem.add((MutableTreeNode)newElem);
/* 381 */       XmlParser.this.curElem = newElem;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void warning(SAXParseException e) {
/* 388 */       doError(e, ParserNotice.Level.WARNING);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\xml\XmlParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */