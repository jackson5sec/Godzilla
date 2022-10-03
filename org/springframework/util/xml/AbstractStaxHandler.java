/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
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
/*     */ abstract class AbstractStaxHandler
/*     */   implements ContentHandler, LexicalHandler
/*     */ {
/*  46 */   private final List<Map<String, String>> namespaceMappings = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private boolean inCData;
/*     */ 
/*     */   
/*     */   public final void startDocument() throws SAXException {
/*  53 */     removeAllNamespaceMappings();
/*  54 */     newNamespaceMapping();
/*     */     try {
/*  56 */       startDocumentInternal();
/*     */     }
/*  58 */     catch (XMLStreamException ex) {
/*  59 */       throw new SAXException("Could not handle startDocument: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void endDocument() throws SAXException {
/*  65 */     removeAllNamespaceMappings();
/*     */     try {
/*  67 */       endDocumentInternal();
/*     */     }
/*  69 */     catch (XMLStreamException ex) {
/*  70 */       throw new SAXException("Could not handle endDocument: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startPrefixMapping(String prefix, String uri) {
/*  76 */     currentNamespaceMapping().put(prefix, uri);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void endPrefixMapping(String prefix) {}
/*     */ 
/*     */   
/*     */   public final void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/*     */     try {
/*  86 */       startElementInternal(toQName(uri, qName), atts, currentNamespaceMapping());
/*  87 */       newNamespaceMapping();
/*     */     }
/*  89 */     catch (XMLStreamException ex) {
/*  90 */       throw new SAXException("Could not handle startElement: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void endElement(String uri, String localName, String qName) throws SAXException {
/*     */     try {
/*  97 */       endElementInternal(toQName(uri, qName), currentNamespaceMapping());
/*  98 */       removeNamespaceMapping();
/*     */     }
/* 100 */     catch (XMLStreamException ex) {
/* 101 */       throw new SAXException("Could not handle endElement: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void characters(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 108 */       String data = new String(ch, start, length);
/* 109 */       if (!this.inCData) {
/* 110 */         charactersInternal(data);
/*     */       } else {
/*     */         
/* 113 */         cDataInternal(data);
/*     */       }
/*     */     
/* 116 */     } catch (XMLStreamException ex) {
/* 117 */       throw new SAXException("Could not handle characters: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 124 */       ignorableWhitespaceInternal(new String(ch, start, length));
/*     */     }
/* 126 */     catch (XMLStreamException ex) {
/* 127 */       throw new SAXException("Could not handle ignorableWhitespace:" + ex
/* 128 */           .getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void processingInstruction(String target, String data) throws SAXException {
/*     */     try {
/* 135 */       processingInstructionInternal(target, data);
/*     */     }
/* 137 */     catch (XMLStreamException ex) {
/* 138 */       throw new SAXException("Could not handle processingInstruction: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void skippedEntity(String name) throws SAXException {
/*     */     try {
/* 145 */       skippedEntityInternal(name);
/*     */     }
/* 147 */     catch (XMLStreamException ex) {
/* 148 */       throw new SAXException("Could not handle skippedEntity: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startDTD(String name, @Nullable String publicId, String systemId) throws SAXException {
/*     */     try {
/* 155 */       StringBuilder builder = new StringBuilder("<!DOCTYPE ");
/* 156 */       builder.append(name);
/* 157 */       if (publicId != null) {
/* 158 */         builder.append(" PUBLIC \"");
/* 159 */         builder.append(publicId);
/* 160 */         builder.append("\" \"");
/*     */       } else {
/*     */         
/* 163 */         builder.append(" SYSTEM \"");
/*     */       } 
/* 165 */       builder.append(systemId);
/* 166 */       builder.append("\">");
/*     */       
/* 168 */       dtdInternal(builder.toString());
/*     */     }
/* 170 */     catch (XMLStreamException ex) {
/* 171 */       throw new SAXException("Could not handle startDTD: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void endDTD() throws SAXException {}
/*     */ 
/*     */   
/*     */   public final void startCDATA() throws SAXException {
/* 181 */     this.inCData = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void endCDATA() throws SAXException {
/* 186 */     this.inCData = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void comment(char[] ch, int start, int length) throws SAXException {
/*     */     try {
/* 192 */       commentInternal(new String(ch, start, length));
/*     */     }
/* 194 */     catch (XMLStreamException ex) {
/* 195 */       throw new SAXException("Could not handle comment: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startEntity(String name) throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endEntity(String name) throws SAXException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected QName toQName(String namespaceUri, String qualifiedName) {
/* 215 */     int idx = qualifiedName.indexOf(':');
/* 216 */     if (idx == -1) {
/* 217 */       return new QName(namespaceUri, qualifiedName);
/*     */     }
/*     */     
/* 220 */     String prefix = qualifiedName.substring(0, idx);
/* 221 */     String localPart = qualifiedName.substring(idx + 1);
/* 222 */     return new QName(namespaceUri, localPart, prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNamespaceDeclaration(QName qName) {
/* 227 */     String prefix = qName.getPrefix();
/* 228 */     String localPart = qName.getLocalPart();
/* 229 */     return (("xmlns".equals(localPart) && prefix.isEmpty()) || ("xmlns"
/* 230 */       .equals(prefix) && !localPart.isEmpty()));
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, String> currentNamespaceMapping() {
/* 235 */     return this.namespaceMappings.get(this.namespaceMappings.size() - 1);
/*     */   }
/*     */   
/*     */   private void newNamespaceMapping() {
/* 239 */     this.namespaceMappings.add(new HashMap<>());
/*     */   }
/*     */   
/*     */   private void removeNamespaceMapping() {
/* 243 */     this.namespaceMappings.remove(this.namespaceMappings.size() - 1);
/*     */   }
/*     */   
/*     */   private void removeAllNamespaceMappings() {
/* 247 */     this.namespaceMappings.clear();
/*     */   }
/*     */   
/*     */   protected abstract void startDocumentInternal() throws XMLStreamException;
/*     */   
/*     */   protected abstract void endDocumentInternal() throws XMLStreamException;
/*     */   
/*     */   protected abstract void startElementInternal(QName paramQName, Attributes paramAttributes, Map<String, String> paramMap) throws XMLStreamException;
/*     */   
/*     */   protected abstract void endElementInternal(QName paramQName, Map<String, String> paramMap) throws XMLStreamException;
/*     */   
/*     */   protected abstract void charactersInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void cDataInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void ignorableWhitespaceInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void processingInstructionInternal(String paramString1, String paramString2) throws XMLStreamException;
/*     */   
/*     */   protected abstract void skippedEntityInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void dtdInternal(String paramString) throws XMLStreamException;
/*     */   
/*     */   protected abstract void commentInternal(String paramString) throws XMLStreamException;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\AbstractStaxHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */