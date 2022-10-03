/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.StartElement;
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
/*     */ class XMLEventStreamWriter
/*     */   implements XMLStreamWriter
/*     */ {
/*     */   private static final String DEFAULT_ENCODING = "UTF-8";
/*     */   private final XMLEventWriter eventWriter;
/*     */   private final XMLEventFactory eventFactory;
/*  49 */   private final List<EndElement> endElements = new ArrayList<>();
/*     */   
/*     */   private boolean emptyElement = false;
/*     */ 
/*     */   
/*     */   public XMLEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory) {
/*  55 */     this.eventWriter = eventWriter;
/*  56 */     this.eventFactory = eventFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
/*  62 */     this.eventWriter.setNamespaceContext(context);
/*     */   }
/*     */ 
/*     */   
/*     */   public NamespaceContext getNamespaceContext() {
/*  67 */     return this.eventWriter.getNamespaceContext();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix, String uri) throws XMLStreamException {
/*  72 */     this.eventWriter.setPrefix(prefix, uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPrefix(String uri) throws XMLStreamException {
/*  77 */     return this.eventWriter.getPrefix(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultNamespace(String uri) throws XMLStreamException {
/*  82 */     this.eventWriter.setDefaultNamespace(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/*  87 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeStartDocument() throws XMLStreamException {
/*  93 */     closeEmptyElementIfNecessary();
/*  94 */     this.eventWriter.add(this.eventFactory.createStartDocument());
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartDocument(String version) throws XMLStreamException {
/*  99 */     closeEmptyElementIfNecessary();
/* 100 */     this.eventWriter.add(this.eventFactory.createStartDocument("UTF-8", version));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartDocument(String encoding, String version) throws XMLStreamException {
/* 105 */     closeEmptyElementIfNecessary();
/* 106 */     this.eventWriter.add(this.eventFactory.createStartDocument(encoding, version));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartElement(String localName) throws XMLStreamException {
/* 111 */     closeEmptyElementIfNecessary();
/* 112 */     doWriteStartElement(this.eventFactory.createStartElement(new QName(localName), (Iterator)null, (Iterator)null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
/* 117 */     closeEmptyElementIfNecessary();
/* 118 */     doWriteStartElement(this.eventFactory.createStartElement(new QName(namespaceURI, localName), (Iterator)null, (Iterator)null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 123 */     closeEmptyElementIfNecessary();
/* 124 */     doWriteStartElement(this.eventFactory.createStartElement(new QName(namespaceURI, localName, prefix), (Iterator)null, (Iterator)null));
/*     */   }
/*     */   
/*     */   private void doWriteStartElement(StartElement startElement) throws XMLStreamException {
/* 128 */     this.eventWriter.add(startElement);
/* 129 */     this.endElements.add(this.eventFactory.createEndElement(startElement.getName(), startElement.getNamespaces()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEmptyElement(String localName) throws XMLStreamException {
/* 134 */     closeEmptyElementIfNecessary();
/* 135 */     writeStartElement(localName);
/* 136 */     this.emptyElement = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
/* 141 */     closeEmptyElementIfNecessary();
/* 142 */     writeStartElement(namespaceURI, localName);
/* 143 */     this.emptyElement = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
/* 148 */     closeEmptyElementIfNecessary();
/* 149 */     writeStartElement(prefix, localName, namespaceURI);
/* 150 */     this.emptyElement = true;
/*     */   }
/*     */   
/*     */   private void closeEmptyElementIfNecessary() throws XMLStreamException {
/* 154 */     if (this.emptyElement) {
/* 155 */       this.emptyElement = false;
/* 156 */       writeEndElement();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEndElement() throws XMLStreamException {
/* 162 */     closeEmptyElementIfNecessary();
/* 163 */     int last = this.endElements.size() - 1;
/* 164 */     EndElement lastEndElement = this.endElements.remove(last);
/* 165 */     this.eventWriter.add(lastEndElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeAttribute(String localName, String value) throws XMLStreamException {
/* 170 */     this.eventWriter.add(this.eventFactory.createAttribute(localName, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
/* 175 */     this.eventWriter.add(this.eventFactory.createAttribute(new QName(namespaceURI, localName), value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
/* 182 */     this.eventWriter.add(this.eventFactory.createAttribute(prefix, namespaceURI, localName, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
/* 187 */     doWriteNamespace(this.eventFactory.createNamespace(prefix, namespaceURI));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
/* 192 */     doWriteNamespace(this.eventFactory.createNamespace(namespaceURI));
/*     */   }
/*     */ 
/*     */   
/*     */   private void doWriteNamespace(Namespace namespace) throws XMLStreamException {
/* 197 */     int last = this.endElements.size() - 1;
/* 198 */     EndElement oldEndElement = this.endElements.get(last);
/* 199 */     Iterator<Namespace> oldNamespaces = oldEndElement.getNamespaces();
/* 200 */     List<Namespace> newNamespaces = new ArrayList<>();
/* 201 */     while (oldNamespaces.hasNext()) {
/* 202 */       Namespace oldNamespace = oldNamespaces.next();
/* 203 */       newNamespaces.add(oldNamespace);
/*     */     } 
/* 205 */     newNamespaces.add(namespace);
/* 206 */     EndElement newEndElement = this.eventFactory.createEndElement(oldEndElement.getName(), newNamespaces.iterator());
/* 207 */     this.eventWriter.add(namespace);
/* 208 */     this.endElements.set(last, newEndElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeCharacters(String text) throws XMLStreamException {
/* 213 */     closeEmptyElementIfNecessary();
/* 214 */     this.eventWriter.add(this.eventFactory.createCharacters(text));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
/* 219 */     closeEmptyElementIfNecessary();
/* 220 */     this.eventWriter.add(this.eventFactory.createCharacters(new String(text, start, len)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeCData(String data) throws XMLStreamException {
/* 225 */     closeEmptyElementIfNecessary();
/* 226 */     this.eventWriter.add(this.eventFactory.createCData(data));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeComment(String data) throws XMLStreamException {
/* 231 */     closeEmptyElementIfNecessary();
/* 232 */     this.eventWriter.add(this.eventFactory.createComment(data));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeProcessingInstruction(String target) throws XMLStreamException {
/* 237 */     closeEmptyElementIfNecessary();
/* 238 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, ""));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
/* 243 */     closeEmptyElementIfNecessary();
/* 244 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, data));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeDTD(String dtd) throws XMLStreamException {
/* 249 */     closeEmptyElementIfNecessary();
/* 250 */     this.eventWriter.add(this.eventFactory.createDTD(dtd));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityRef(String name) throws XMLStreamException {
/* 255 */     closeEmptyElementIfNecessary();
/* 256 */     this.eventWriter.add(this.eventFactory.createEntityReference(name, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEndDocument() throws XMLStreamException {
/* 261 */     closeEmptyElementIfNecessary();
/* 262 */     this.eventWriter.add(this.eventFactory.createEndDocument());
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws XMLStreamException {
/* 267 */     this.eventWriter.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/* 272 */     closeEmptyElementIfNecessary();
/* 273 */     this.eventWriter.close();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\XMLEventStreamWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */