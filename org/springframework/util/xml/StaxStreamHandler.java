/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
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
/*     */ class StaxStreamHandler
/*     */   extends AbstractStaxHandler
/*     */ {
/*     */   private final XMLStreamWriter streamWriter;
/*     */   
/*     */   public StaxStreamHandler(XMLStreamWriter streamWriter) {
/*  44 */     this.streamWriter = streamWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startDocumentInternal() throws XMLStreamException {
/*  50 */     this.streamWriter.writeStartDocument();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endDocumentInternal() throws XMLStreamException {
/*  55 */     this.streamWriter.writeEndDocument();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startElementInternal(QName name, Attributes attributes, Map<String, String> namespaceMapping) throws XMLStreamException {
/*  62 */     this.streamWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
/*     */     
/*  64 */     for (Map.Entry<String, String> entry : namespaceMapping.entrySet()) {
/*  65 */       String prefix = entry.getKey();
/*  66 */       String namespaceUri = entry.getValue();
/*  67 */       this.streamWriter.writeNamespace(prefix, namespaceUri);
/*  68 */       if ("".equals(prefix)) {
/*  69 */         this.streamWriter.setDefaultNamespace(namespaceUri);
/*     */         continue;
/*     */       } 
/*  72 */       this.streamWriter.setPrefix(prefix, namespaceUri);
/*     */     } 
/*     */     
/*  75 */     for (int i = 0; i < attributes.getLength(); i++) {
/*  76 */       QName attrName = toQName(attributes.getURI(i), attributes.getQName(i));
/*  77 */       if (!isNamespaceDeclaration(attrName)) {
/*  78 */         this.streamWriter.writeAttribute(attrName.getPrefix(), attrName.getNamespaceURI(), attrName
/*  79 */             .getLocalPart(), attributes.getValue(i));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void endElementInternal(QName name, Map<String, String> namespaceMapping) throws XMLStreamException {
/*  86 */     this.streamWriter.writeEndElement();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void charactersInternal(String data) throws XMLStreamException {
/*  91 */     this.streamWriter.writeCharacters(data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cDataInternal(String data) throws XMLStreamException {
/*  96 */     this.streamWriter.writeCData(data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ignorableWhitespaceInternal(String data) throws XMLStreamException {
/* 101 */     this.streamWriter.writeCharacters(data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processingInstructionInternal(String target, String data) throws XMLStreamException {
/* 106 */     this.streamWriter.writeProcessingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dtdInternal(String dtd) throws XMLStreamException {
/* 111 */     this.streamWriter.writeDTD(dtd);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void commentInternal(String comment) throws XMLStreamException {
/* 116 */     this.streamWriter.writeComment(comment);
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {}
/*     */   
/*     */   public void startEntity(String name) throws SAXException {}
/*     */   
/*     */   public void endEntity(String name) throws SAXException {}
/*     */   
/*     */   protected void skippedEntityInternal(String name) throws XMLStreamException {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\StaxStreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */