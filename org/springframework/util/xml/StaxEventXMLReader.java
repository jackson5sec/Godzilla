/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.Comment;
/*     */ import javax.xml.stream.events.DTD;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.EntityDeclaration;
/*     */ import javax.xml.stream.events.EntityReference;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.NotationDeclaration;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
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
/*     */ 
/*     */ 
/*     */ class StaxEventXMLReader
/*     */   extends AbstractStaxXMLReader
/*     */ {
/*     */   private static final String DEFAULT_XML_VERSION = "1.0";
/*     */   private final XMLEventReader reader;
/*  68 */   private String xmlVersion = "1.0";
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
/*     */   StaxEventXMLReader(XMLEventReader reader) {
/*     */     try {
/*  83 */       XMLEvent event = reader.peek();
/*  84 */       if (event != null && !event.isStartDocument() && !event.isStartElement()) {
/*  85 */         throw new IllegalStateException("XMLEventReader not at start of document or element");
/*     */       }
/*     */     }
/*  88 */     catch (XMLStreamException ex) {
/*  89 */       throw new IllegalStateException("Could not read first element: " + ex.getMessage());
/*     */     } 
/*  91 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseInternal() throws SAXException, XMLStreamException {
/*  97 */     boolean documentStarted = false;
/*  98 */     boolean documentEnded = false;
/*  99 */     int elementDepth = 0;
/* 100 */     while (this.reader.hasNext() && elementDepth >= 0) {
/* 101 */       XMLEvent event = this.reader.nextEvent();
/* 102 */       if (!event.isStartDocument() && !event.isEndDocument() && !documentStarted) {
/* 103 */         handleStartDocument(event);
/* 104 */         documentStarted = true;
/*     */       } 
/* 106 */       switch (event.getEventType()) {
/*     */         case 7:
/* 108 */           handleStartDocument(event);
/* 109 */           documentStarted = true;
/*     */         
/*     */         case 1:
/* 112 */           elementDepth++;
/* 113 */           handleStartElement(event.asStartElement());
/*     */         
/*     */         case 2:
/* 116 */           elementDepth--;
/* 117 */           if (elementDepth >= 0) {
/* 118 */             handleEndElement(event.asEndElement());
/*     */           }
/*     */         
/*     */         case 3:
/* 122 */           handleProcessingInstruction((ProcessingInstruction)event);
/*     */         
/*     */         case 4:
/*     */         case 6:
/*     */         case 12:
/* 127 */           handleCharacters(event.asCharacters());
/*     */         
/*     */         case 8:
/* 130 */           handleEndDocument();
/* 131 */           documentEnded = true;
/*     */         
/*     */         case 14:
/* 134 */           handleNotationDeclaration((NotationDeclaration)event);
/*     */         
/*     */         case 15:
/* 137 */           handleEntityDeclaration((EntityDeclaration)event);
/*     */         
/*     */         case 5:
/* 140 */           handleComment((Comment)event);
/*     */         
/*     */         case 11:
/* 143 */           handleDtd((DTD)event);
/*     */         
/*     */         case 9:
/* 146 */           handleEntityReference((EntityReference)event);
/*     */       } 
/*     */     
/*     */     } 
/* 150 */     if (documentStarted && !documentEnded) {
/* 151 */       handleEndDocument();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleStartDocument(XMLEvent event) throws SAXException {
/* 157 */     if (event.isStartDocument()) {
/* 158 */       StartDocument startDocument = (StartDocument)event;
/* 159 */       String xmlVersion = startDocument.getVersion();
/* 160 */       if (StringUtils.hasLength(xmlVersion)) {
/* 161 */         this.xmlVersion = xmlVersion;
/*     */       }
/* 163 */       if (startDocument.encodingSet()) {
/* 164 */         this.encoding = startDocument.getCharacterEncodingScheme();
/*     */       }
/*     */     } 
/*     */     
/* 168 */     ContentHandler contentHandler = getContentHandler();
/* 169 */     if (contentHandler != null) {
/* 170 */       final Location location = event.getLocation();
/* 171 */       contentHandler.setDocumentLocator(new Locator2()
/*     */           {
/*     */             public int getColumnNumber() {
/* 174 */               return (location != null) ? location.getColumnNumber() : -1;
/*     */             }
/*     */             
/*     */             public int getLineNumber() {
/* 178 */               return (location != null) ? location.getLineNumber() : -1;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getPublicId() {
/* 183 */               return (location != null) ? location.getPublicId() : null;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getSystemId() {
/* 188 */               return (location != null) ? location.getSystemId() : null;
/*     */             }
/*     */             
/*     */             public String getXMLVersion() {
/* 192 */               return StaxEventXMLReader.this.xmlVersion;
/*     */             }
/*     */             
/*     */             @Nullable
/*     */             public String getEncoding() {
/* 197 */               return StaxEventXMLReader.this.encoding;
/*     */             }
/*     */           });
/* 200 */       contentHandler.startDocument();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleStartElement(StartElement startElement) throws SAXException {
/* 205 */     if (getContentHandler() != null) {
/* 206 */       QName qName = startElement.getName();
/* 207 */       if (hasNamespacesFeature()) {
/* 208 */         for (Iterator<Namespace> iterator = startElement.getNamespaces(); iterator.hasNext(); ) {
/* 209 */           Namespace namespace = iterator.next();
/* 210 */           startPrefixMapping(namespace.getPrefix(), namespace.getNamespaceURI());
/*     */         } 
/* 212 */         for (Iterator<Attribute> i = startElement.getAttributes(); i.hasNext(); ) {
/* 213 */           Attribute attribute = i.next();
/* 214 */           QName attributeName = attribute.getName();
/* 215 */           startPrefixMapping(attributeName.getPrefix(), attributeName.getNamespaceURI());
/*     */         } 
/*     */         
/* 218 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), 
/* 219 */             getAttributes(startElement));
/*     */       } else {
/*     */         
/* 222 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes(startElement));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleCharacters(Characters characters) throws SAXException {
/* 228 */     char[] data = characters.getData().toCharArray();
/* 229 */     if (getContentHandler() != null && characters.isIgnorableWhiteSpace()) {
/* 230 */       getContentHandler().ignorableWhitespace(data, 0, data.length);
/*     */       return;
/*     */     } 
/* 233 */     if (characters.isCData() && getLexicalHandler() != null) {
/* 234 */       getLexicalHandler().startCDATA();
/*     */     }
/* 236 */     if (getContentHandler() != null) {
/* 237 */       getContentHandler().characters(data, 0, data.length);
/*     */     }
/* 239 */     if (characters.isCData() && getLexicalHandler() != null) {
/* 240 */       getLexicalHandler().endCDATA();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEndElement(EndElement endElement) throws SAXException {
/* 245 */     if (getContentHandler() != null) {
/* 246 */       QName qName = endElement.getName();
/* 247 */       if (hasNamespacesFeature()) {
/* 248 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 249 */         for (Iterator<Namespace> i = endElement.getNamespaces(); i.hasNext(); ) {
/* 250 */           Namespace namespace = i.next();
/* 251 */           endPrefixMapping(namespace.getPrefix());
/*     */         } 
/*     */       } else {
/*     */         
/* 255 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleEndDocument() throws SAXException {
/* 262 */     if (getContentHandler() != null) {
/* 263 */       getContentHandler().endDocument();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleNotationDeclaration(NotationDeclaration declaration) throws SAXException {
/* 268 */     if (getDTDHandler() != null) {
/* 269 */       getDTDHandler().notationDecl(declaration.getName(), declaration.getPublicId(), declaration.getSystemId());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleEntityDeclaration(EntityDeclaration entityDeclaration) throws SAXException {
/* 274 */     if (getDTDHandler() != null) {
/* 275 */       getDTDHandler().unparsedEntityDecl(entityDeclaration.getName(), entityDeclaration.getPublicId(), entityDeclaration
/* 276 */           .getSystemId(), entityDeclaration.getNotationName());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleProcessingInstruction(ProcessingInstruction pi) throws SAXException {
/* 281 */     if (getContentHandler() != null) {
/* 282 */       getContentHandler().processingInstruction(pi.getTarget(), pi.getData());
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleComment(Comment comment) throws SAXException {
/* 287 */     if (getLexicalHandler() != null) {
/* 288 */       char[] ch = comment.getText().toCharArray();
/* 289 */       getLexicalHandler().comment(ch, 0, ch.length);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleDtd(DTD dtd) throws SAXException {
/* 294 */     if (getLexicalHandler() != null) {
/* 295 */       Location location = dtd.getLocation();
/* 296 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/*     */     } 
/* 298 */     if (getLexicalHandler() != null) {
/* 299 */       getLexicalHandler().endDTD();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleEntityReference(EntityReference reference) throws SAXException {
/* 305 */     if (getLexicalHandler() != null) {
/* 306 */       getLexicalHandler().startEntity(reference.getName());
/*     */     }
/* 308 */     if (getLexicalHandler() != null) {
/* 309 */       getLexicalHandler().endEntity(reference.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Attributes getAttributes(StartElement event) {
/* 315 */     AttributesImpl attributes = new AttributesImpl();
/* 316 */     for (Iterator<Attribute> i = event.getAttributes(); i.hasNext(); ) {
/* 317 */       Attribute attribute = i.next();
/* 318 */       QName qName = attribute.getName();
/* 319 */       String namespace = qName.getNamespaceURI();
/* 320 */       if (namespace == null || !hasNamespacesFeature()) {
/* 321 */         namespace = "";
/*     */       }
/* 323 */       String type = attribute.getDTDType();
/* 324 */       if (type == null) {
/* 325 */         type = "CDATA";
/*     */       }
/* 327 */       attributes.addAttribute(namespace, qName.getLocalPart(), toQualifiedName(qName), type, attribute.getValue());
/*     */     } 
/* 329 */     if (hasNamespacePrefixesFeature()) {
/* 330 */       for (Iterator<Namespace> iterator = event.getNamespaces(); iterator.hasNext(); ) {
/* 331 */         String qName; Namespace namespace = iterator.next();
/* 332 */         String prefix = namespace.getPrefix();
/* 333 */         String namespaceUri = namespace.getNamespaceURI();
/*     */         
/* 335 */         if (StringUtils.hasLength(prefix)) {
/* 336 */           qName = "xmlns:" + prefix;
/*     */         } else {
/*     */           
/* 339 */           qName = "xmlns";
/*     */         } 
/* 341 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/*     */       } 
/*     */     }
/*     */     
/* 345 */     return attributes;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\StaxEventXMLReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */