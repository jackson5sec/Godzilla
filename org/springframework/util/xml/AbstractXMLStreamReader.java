/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
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
/*     */ abstract class AbstractXMLStreamReader
/*     */   implements XMLStreamReader
/*     */ {
/*     */   public String getElementText() throws XMLStreamException {
/*  36 */     if (getEventType() != 1) {
/*  37 */       throw new XMLStreamException("Parser must be on START_ELEMENT to read next text", getLocation());
/*     */     }
/*  39 */     int eventType = next();
/*  40 */     StringBuilder builder = new StringBuilder();
/*  41 */     while (eventType != 2) {
/*  42 */       if (eventType == 4 || eventType == 12 || eventType == 6 || eventType == 9) {
/*     */         
/*  44 */         builder.append(getText());
/*     */       }
/*  46 */       else if (eventType != 3 && eventType != 5) {
/*     */ 
/*     */ 
/*     */         
/*  50 */         if (eventType == 8) {
/*  51 */           throw new XMLStreamException("Unexpected end of document when reading element text content", 
/*  52 */               getLocation());
/*     */         }
/*  54 */         if (eventType == 1) {
/*  55 */           throw new XMLStreamException("Element text content may not contain START_ELEMENT", getLocation());
/*     */         }
/*     */         
/*  58 */         throw new XMLStreamException("Unexpected event type " + eventType, getLocation());
/*     */       } 
/*  60 */       eventType = next();
/*     */     } 
/*  62 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeLocalName(int index) {
/*  67 */     return getAttributeName(index).getLocalPart();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeNamespace(int index) {
/*  72 */     return getAttributeName(index).getNamespaceURI();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributePrefix(int index) {
/*  77 */     return getAttributeName(index).getPrefix();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI() {
/*  82 */     int eventType = getEventType();
/*  83 */     if (eventType == 1 || eventType == 2) {
/*  84 */       return getName().getNamespaceURI();
/*     */     }
/*     */     
/*  87 */     throw new IllegalStateException("Parser must be on START_ELEMENT or END_ELEMENT state");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(String prefix) {
/*  93 */     return getNamespaceContext().getNamespaceURI(prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasText() {
/*  98 */     int eventType = getEventType();
/*  99 */     return (eventType == 6 || eventType == 4 || eventType == 5 || eventType == 12 || eventType == 9);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/* 106 */     int eventType = getEventType();
/* 107 */     if (eventType == 1 || eventType == 2) {
/* 108 */       return getName().getPrefix();
/*     */     }
/*     */     
/* 111 */     throw new IllegalStateException("Parser must be on START_ELEMENT or END_ELEMENT state");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasName() {
/* 117 */     int eventType = getEventType();
/* 118 */     return (eventType == 1 || eventType == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWhiteSpace() {
/* 123 */     return (getEventType() == 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStartElement() {
/* 128 */     return (getEventType() == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndElement() {
/* 133 */     return (getEventType() == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCharacters() {
/* 138 */     return (getEventType() == 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextTag() throws XMLStreamException {
/* 143 */     int eventType = next();
/* 144 */     while ((eventType == 4 && isWhiteSpace()) || (eventType == 12 && 
/* 145 */       isWhiteSpace()) || eventType == 6 || eventType == 3 || eventType == 5)
/*     */     {
/* 147 */       eventType = next();
/*     */     }
/* 149 */     if (eventType != 1 && eventType != 2) {
/* 150 */       throw new XMLStreamException("expected start or end tag", getLocation());
/*     */     }
/* 152 */     return eventType;
/*     */   }
/*     */ 
/*     */   
/*     */   public void require(int expectedType, String namespaceURI, String localName) throws XMLStreamException {
/* 157 */     int eventType = getEventType();
/* 158 */     if (eventType != expectedType) {
/* 159 */       throw new XMLStreamException("Expected [" + expectedType + "] but read [" + eventType + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getAttributeValue(@Nullable String namespaceURI, String localName) {
/* 166 */     for (int i = 0; i < getAttributeCount(); i++) {
/* 167 */       QName name = getAttributeName(i);
/* 168 */       if (name.getLocalPart().equals(localName) && (namespaceURI == null || name
/* 169 */         .getNamespaceURI().equals(namespaceURI))) {
/* 170 */         return getAttributeValue(i);
/*     */       }
/*     */     } 
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 178 */     return (getEventType() != 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLocalName() {
/* 183 */     return getName().getLocalPart();
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getTextCharacters() {
/* 188 */     return getText().toCharArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) {
/* 193 */     char[] source = getTextCharacters();
/* 194 */     length = Math.min(length, source.length);
/* 195 */     System.arraycopy(source, sourceStart, target, targetStart, length);
/* 196 */     return length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTextLength() {
/* 201 */     return getText().length();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\AbstractXMLStreamReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */