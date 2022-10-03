/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.Locator2;
/*     */ import org.xml.sax.helpers.AttributesImpl;
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
/*     */ class StaxStreamXMLReader
/*     */   extends AbstractStaxXMLReader
/*     */ {
/*     */   private static final String DEFAULT_XML_VERSION = "1.0";
/*     */   private final XMLStreamReader reader;
/*  52 */   private String xmlVersion = "1.0";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StaxStreamXMLReader(XMLStreamReader reader) {
/*  66 */     int event = reader.getEventType();
/*  67 */     if (event != 7 && event != 1) {
/*  68 */       throw new IllegalStateException("XMLEventReader not at start of document or element");
/*     */     }
/*  70 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseInternal() throws SAXException, XMLStreamException {
/*  76 */     boolean documentStarted = false;
/*  77 */     boolean documentEnded = false;
/*  78 */     int elementDepth = 0;
/*  79 */     int eventType = this.reader.getEventType();
/*     */     while (true) {
/*  81 */       if (eventType != 7 && eventType != 8 && !documentStarted) {
/*     */         
/*  83 */         handleStartDocument();
/*  84 */         documentStarted = true;
/*     */       } 
/*  86 */       switch (eventType) {
/*     */         case 1:
/*  88 */           elementDepth++;
/*  89 */           handleStartElement();
/*     */           break;
/*     */         case 2:
/*  92 */           elementDepth--;
/*  93 */           if (elementDepth >= 0) {
/*  94 */             handleEndElement();
/*     */           }
/*     */           break;
/*     */         case 3:
/*  98 */           handleProcessingInstruction();
/*     */           break;
/*     */         case 4:
/*     */         case 6:
/*     */         case 12:
/* 103 */           handleCharacters();
/*     */           break;
/*     */         case 7:
/* 106 */           handleStartDocument();
/* 107 */           documentStarted = true;
/*     */           break;
/*     */         case 8:
/* 110 */           handleEndDocument();
/* 111 */           documentEnded = true;
/*     */           break;
/*     */         case 5:
/* 114 */           handleComment();
/*     */           break;
/*     */         case 11:
/* 117 */           handleDtd();
/*     */           break;
/*     */         case 9:
/* 120 */           handleEntityReference();
/*     */           break;
/*     */       } 
/* 123 */       if (this.reader.hasNext() && elementDepth >= 0) {
/* 124 */         eventType = this.reader.next();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 130 */     if (!documentEnded) {
/* 131 */       handleEndDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleStartDocument() throws SAXException {
/* 136 */     if (7 == this.reader.getEventType()) {
/* 137 */       String xmlVersion = this.reader.getVersion();
/* 138 */       if (StringUtils.hasLength(xmlVersion)) {
/* 139 */         this.xmlVersion = xmlVersion;
/*     */       }
/* 141 */       this.encoding = this.reader.getCharacterEncodingScheme();
/*     */     } 
/*     */     
/* 144 */     ContentHandler contentHandler = getContentHandler();
/* 145 */     if (contentHandler != null) {
/* 146 */       final Location location = this.reader.getLocation();
/* 147 */       contentHandler.setDocumentLocator(new Locator2()
/*     */           {
/*     */             public int getColumnNumber() {
/* 150 */               return (location != null) ? location.getColumnNumber() : -1;
/*     */             }
/*     */             
/*     */             public int getLineNumber() {
/* 154 */               return (location != null) ? location.getLineNumber() : -1;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getPublicId() {
/* 159 */               return (location != null) ? location.getPublicId() : null;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getSystemId() {
/* 164 */               return (location != null) ? location.getSystemId() : null;
/*     */             }
/*     */             
/*     */             public String getXMLVersion() {
/* 168 */               return StaxStreamXMLReader.this.xmlVersion;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getEncoding() {
/* 173 */               return StaxStreamXMLReader.this.encoding;
/*     */             }
/*     */           });
/* 176 */       contentHandler.startDocument();
/* 177 */       if (this.reader.standaloneSet()) {
/* 178 */         setStandalone(this.reader.isStandalone());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleStartElement() throws SAXException {
/* 184 */     if (getContentHandler() != null) {
/* 185 */       QName qName = this.reader.getName();
/* 186 */       if (hasNamespacesFeature()) {
/* 187 */         int i; for (i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 188 */           startPrefixMapping(this.reader.getNamespacePrefix(i), this.reader.getNamespaceURI(i));
/*     */         }
/* 190 */         for (i = 0; i < this.reader.getAttributeCount(); i++) {
/* 191 */           String prefix = this.reader.getAttributePrefix(i);
/* 192 */           String namespace = this.reader.getAttributeNamespace(i);
/* 193 */           if (StringUtils.hasLength(namespace)) {
/* 194 */             startPrefixMapping(prefix, namespace);
/*     */           }
/*     */         } 
/* 197 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), 
/* 198 */             toQualifiedName(qName), getAttributes());
/*     */       } else {
/*     */         
/* 201 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleEndElement() throws SAXException {
/* 207 */     if (getContentHandler() != null) {
/* 208 */       QName qName = this.reader.getName();
/* 209 */       if (hasNamespacesFeature()) {
/* 210 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 211 */         for (int i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 212 */           String prefix = this.reader.getNamespacePrefix(i);
/* 213 */           if (prefix == null) {
/* 214 */             prefix = "";
/*     */           }
/* 216 */           endPrefixMapping(prefix);
/*     */         } 
/*     */       } else {
/*     */         
/* 220 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleCharacters() throws SAXException {
/* 226 */     if (12 == this.reader.getEventType() && getLexicalHandler() != null) {
/* 227 */       getLexicalHandler().startCDATA();
/*     */     }
/* 229 */     if (getContentHandler() != null) {
/* 230 */       getContentHandler().characters(this.reader.getTextCharacters(), this.reader
/* 231 */           .getTextStart(), this.reader.getTextLength());
/*     */     }
/* 233 */     if (12 == this.reader.getEventType() && getLexicalHandler() != null) {
/* 234 */       getLexicalHandler().endCDATA();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleComment() throws SAXException {
/* 239 */     if (getLexicalHandler() != null) {
/* 240 */       getLexicalHandler().comment(this.reader.getTextCharacters(), this.reader
/* 241 */           .getTextStart(), this.reader.getTextLength());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleDtd() throws SAXException {
/* 246 */     if (getLexicalHandler() != null) {
/* 247 */       Location location = this.reader.getLocation();
/* 248 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/*     */     } 
/* 250 */     if (getLexicalHandler() != null) {
/* 251 */       getLexicalHandler().endDTD();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEntityReference() throws SAXException {
/* 256 */     if (getLexicalHandler() != null) {
/* 257 */       getLexicalHandler().startEntity(this.reader.getLocalName());
/*     */     }
/* 259 */     if (getLexicalHandler() != null) {
/* 260 */       getLexicalHandler().endEntity(this.reader.getLocalName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndDocument() throws SAXException {
/* 265 */     if (getContentHandler() != null) {
/* 266 */       getContentHandler().endDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleProcessingInstruction() throws SAXException {
/* 271 */     if (getContentHandler() != null) {
/* 272 */       getContentHandler().processingInstruction(this.reader.getPITarget(), this.reader.getPIData());
/*     */     }
/*     */   }
/*     */   
/*     */   private Attributes getAttributes() {
/* 277 */     AttributesImpl attributes = new AttributesImpl(); int i;
/* 278 */     for (i = 0; i < this.reader.getAttributeCount(); i++) {
/* 279 */       String namespace = this.reader.getAttributeNamespace(i);
/* 280 */       if (namespace == null || !hasNamespacesFeature()) {
/* 281 */         namespace = "";
/*     */       }
/* 283 */       String type = this.reader.getAttributeType(i);
/* 284 */       if (type == null) {
/* 285 */         type = "CDATA";
/*     */       }
/* 287 */       attributes.addAttribute(namespace, this.reader.getAttributeLocalName(i), 
/* 288 */           toQualifiedName(this.reader.getAttributeName(i)), type, this.reader.getAttributeValue(i));
/*     */     } 
/* 290 */     if (hasNamespacePrefixesFeature()) {
/* 291 */       for (i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 292 */         String qName, prefix = this.reader.getNamespacePrefix(i);
/* 293 */         String namespaceUri = this.reader.getNamespaceURI(i);
/*     */         
/* 295 */         if (StringUtils.hasLength(prefix)) {
/* 296 */           qName = "xmlns:" + prefix;
/*     */         } else {
/*     */           
/* 299 */           qName = "xmlns";
/*     */         } 
/* 301 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/*     */       } 
/*     */     }
/*     */     
/* 305 */     return attributes;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\StaxStreamXMLReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */