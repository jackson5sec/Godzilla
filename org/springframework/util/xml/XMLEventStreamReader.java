/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Attribute;
/*     */ import javax.xml.stream.events.Comment;
/*     */ import javax.xml.stream.events.Namespace;
/*     */ import javax.xml.stream.events.ProcessingInstruction;
/*     */ import javax.xml.stream.events.StartDocument;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ class XMLEventStreamReader
/*     */   extends AbstractXMLStreamReader
/*     */ {
/*     */   private XMLEvent event;
/*     */   private final XMLEventReader eventReader;
/*     */   
/*     */   public XMLEventStreamReader(XMLEventReader eventReader) throws XMLStreamException {
/*  52 */     this.eventReader = eventReader;
/*  53 */     this.event = eventReader.nextEvent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public QName getName() {
/*  59 */     if (this.event.isStartElement()) {
/*  60 */       return this.event.asStartElement().getName();
/*     */     }
/*  62 */     if (this.event.isEndElement()) {
/*  63 */       return this.event.asEndElement().getName();
/*     */     }
/*     */     
/*  66 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Location getLocation() {
/*  72 */     return this.event.getLocation();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEventType() {
/*  77 */     return this.event.getEventType();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getVersion() {
/*  83 */     if (this.event.isStartDocument()) {
/*  84 */       return ((StartDocument)this.event).getVersion();
/*     */     }
/*     */     
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/*  93 */     return this.eventReader.getProperty(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStandalone() {
/*  98 */     if (this.event.isStartDocument()) {
/*  99 */       return ((StartDocument)this.event).isStandalone();
/*     */     }
/*     */     
/* 102 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean standaloneSet() {
/* 108 */     if (this.event.isStartDocument()) {
/* 109 */       return ((StartDocument)this.event).standaloneSet();
/*     */     }
/*     */     
/* 112 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEncoding() {
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getCharacterEncodingScheme() {
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPITarget() {
/* 130 */     if (this.event.isProcessingInstruction()) {
/* 131 */       return ((ProcessingInstruction)this.event).getTarget();
/*     */     }
/*     */     
/* 134 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPIData() {
/* 140 */     if (this.event.isProcessingInstruction()) {
/* 141 */       return ((ProcessingInstruction)this.event).getData();
/*     */     }
/*     */     
/* 144 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTextStart() {
/* 150 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText() {
/* 155 */     if (this.event.isCharacters()) {
/* 156 */       return this.event.asCharacters().getData();
/*     */     }
/* 158 */     if (this.event.getEventType() == 5) {
/* 159 */       return ((Comment)this.event).getText();
/*     */     }
/*     */     
/* 162 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributeCount() {
/* 169 */     if (!this.event.isStartElement()) {
/* 170 */       throw new IllegalStateException();
/*     */     }
/* 172 */     Iterator attributes = this.event.asStartElement().getAttributes();
/* 173 */     return countIterator(attributes);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttributeSpecified(int index) {
/* 178 */     return getAttribute(index).isSpecified();
/*     */   }
/*     */ 
/*     */   
/*     */   public QName getAttributeName(int index) {
/* 183 */     return getAttribute(index).getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeType(int index) {
/* 188 */     return getAttribute(index).getDTDType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeValue(int index) {
/* 193 */     return getAttribute(index).getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private Attribute getAttribute(int index) {
/* 198 */     if (!this.event.isStartElement()) {
/* 199 */       throw new IllegalStateException();
/*     */     }
/* 201 */     int count = 0;
/* 202 */     Iterator<Attribute> attributes = this.event.asStartElement().getAttributes();
/* 203 */     while (attributes.hasNext()) {
/* 204 */       Attribute attribute = attributes.next();
/* 205 */       if (count == index) {
/* 206 */         return attribute;
/*     */       }
/*     */       
/* 209 */       count++;
/*     */     } 
/*     */     
/* 212 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NamespaceContext getNamespaceContext() {
/* 217 */     if (this.event.isStartElement()) {
/* 218 */       return this.event.asStartElement().getNamespaceContext();
/*     */     }
/*     */     
/* 221 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNamespaceCount() {
/*     */     Iterator namespaces;
/* 229 */     if (this.event.isStartElement()) {
/* 230 */       namespaces = this.event.asStartElement().getNamespaces();
/*     */     }
/* 232 */     else if (this.event.isEndElement()) {
/* 233 */       namespaces = this.event.asEndElement().getNamespaces();
/*     */     } else {
/*     */       
/* 236 */       throw new IllegalStateException();
/*     */     } 
/* 238 */     return countIterator(namespaces);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespacePrefix(int index) {
/* 243 */     return getNamespace(index).getPrefix();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(int index) {
/* 248 */     return getNamespace(index).getNamespaceURI();
/*     */   }
/*     */ 
/*     */   
/*     */   private Namespace getNamespace(int index) {
/*     */     Iterator<Namespace> namespaces;
/* 254 */     if (this.event.isStartElement()) {
/* 255 */       namespaces = this.event.asStartElement().getNamespaces();
/*     */     }
/* 257 */     else if (this.event.isEndElement()) {
/* 258 */       namespaces = this.event.asEndElement().getNamespaces();
/*     */     } else {
/*     */       
/* 261 */       throw new IllegalStateException();
/*     */     } 
/* 263 */     int count = 0;
/* 264 */     while (namespaces.hasNext()) {
/* 265 */       Namespace namespace = namespaces.next();
/* 266 */       if (count == index) {
/* 267 */         return namespace;
/*     */       }
/*     */       
/* 270 */       count++;
/*     */     } 
/*     */     
/* 273 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int next() throws XMLStreamException {
/* 278 */     this.event = this.eventReader.nextEvent();
/* 279 */     return this.event.getEventType();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/* 284 */     this.eventReader.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int countIterator(Iterator iterator) {
/* 290 */     int count = 0;
/* 291 */     while (iterator.hasNext()) {
/* 292 */       iterator.next();
/* 293 */       count++;
/*     */     } 
/* 295 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\XMLEventStreamReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */