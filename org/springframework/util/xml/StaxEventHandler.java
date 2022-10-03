/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
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
/*     */ class StaxEventHandler
/*     */   extends AbstractStaxHandler
/*     */ {
/*     */   private final XMLEventFactory eventFactory;
/*     */   private final XMLEventWriter eventWriter;
/*     */   
/*     */   public StaxEventHandler(XMLEventWriter eventWriter) {
/*  57 */     this.eventFactory = XMLEventFactory.newInstance();
/*  58 */     this.eventWriter = eventWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaxEventHandler(XMLEventWriter eventWriter, XMLEventFactory factory) {
/*  68 */     this.eventFactory = factory;
/*  69 */     this.eventWriter = eventWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDocumentLocator(@Nullable Locator locator) {
/*  75 */     if (locator != null) {
/*  76 */       this.eventFactory.setLocation(new LocatorLocationAdapter(locator));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startDocumentInternal() throws XMLStreamException {
/*  82 */     this.eventWriter.add(this.eventFactory.createStartDocument());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endDocumentInternal() throws XMLStreamException {
/*  87 */     this.eventWriter.add(this.eventFactory.createEndDocument());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startElementInternal(QName name, Attributes atts, Map<String, String> namespaceMapping) throws XMLStreamException {
/*  94 */     List<Attribute> attributes = getAttributes(atts);
/*  95 */     List<Namespace> namespaces = getNamespaces(namespaceMapping);
/*  96 */     this.eventWriter.add(this.eventFactory
/*  97 */         .createStartElement(name, attributes.iterator(), namespaces.iterator()));
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Namespace> getNamespaces(Map<String, String> namespaceMappings) {
/* 102 */     List<Namespace> result = new ArrayList<>(namespaceMappings.size());
/* 103 */     namespaceMappings.forEach((prefix, namespaceUri) -> result.add(this.eventFactory.createNamespace(prefix, namespaceUri)));
/*     */     
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   private List<Attribute> getAttributes(Attributes attributes) {
/* 109 */     int attrLength = attributes.getLength();
/* 110 */     List<Attribute> result = new ArrayList<>(attrLength);
/* 111 */     for (int i = 0; i < attrLength; i++) {
/* 112 */       QName attrName = toQName(attributes.getURI(i), attributes.getQName(i));
/* 113 */       if (!isNamespaceDeclaration(attrName)) {
/* 114 */         result.add(this.eventFactory.createAttribute(attrName, attributes.getValue(i)));
/*     */       }
/*     */     } 
/* 117 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endElementInternal(QName name, Map<String, String> namespaceMapping) throws XMLStreamException {
/* 122 */     List<Namespace> namespaces = getNamespaces(namespaceMapping);
/* 123 */     this.eventWriter.add(this.eventFactory.createEndElement(name, namespaces.iterator()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void charactersInternal(String data) throws XMLStreamException {
/* 128 */     this.eventWriter.add(this.eventFactory.createCharacters(data));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cDataInternal(String data) throws XMLStreamException {
/* 133 */     this.eventWriter.add(this.eventFactory.createCData(data));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ignorableWhitespaceInternal(String data) throws XMLStreamException {
/* 138 */     this.eventWriter.add(this.eventFactory.createIgnorableSpace(data));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processingInstructionInternal(String target, String data) throws XMLStreamException {
/* 143 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, data));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dtdInternal(String dtd) throws XMLStreamException {
/* 148 */     this.eventWriter.add(this.eventFactory.createDTD(dtd));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void commentInternal(String comment) throws XMLStreamException {
/* 153 */     this.eventWriter.add(this.eventFactory.createComment(comment));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void skippedEntityInternal(String name) {}
/*     */ 
/*     */   
/*     */   private static final class LocatorLocationAdapter
/*     */     implements Location
/*     */   {
/*     */     private final Locator locator;
/*     */ 
/*     */     
/*     */     public LocatorLocationAdapter(Locator locator) {
/* 167 */       this.locator = locator;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLineNumber() {
/* 172 */       return this.locator.getLineNumber();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnNumber() {
/* 177 */       return this.locator.getColumnNumber();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getCharacterOffset() {
/* 182 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPublicId() {
/* 187 */       return this.locator.getPublicId();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSystemId() {
/* 192 */       return this.locator.getSystemId();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\StaxEventHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */