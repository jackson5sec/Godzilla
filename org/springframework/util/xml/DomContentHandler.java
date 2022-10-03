/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ class DomContentHandler
/*     */   implements ContentHandler
/*     */ {
/*     */   private final Document document;
/*  42 */   private final List<Element> elements = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Node node;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DomContentHandler(Node node) {
/*  52 */     this.node = node;
/*  53 */     if (node instanceof Document) {
/*  54 */       this.document = (Document)node;
/*     */     } else {
/*     */       
/*  57 */       this.document = node.getOwnerDocument();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Node getParent() {
/*  63 */     if (!this.elements.isEmpty()) {
/*  64 */       return this.elements.get(this.elements.size() - 1);
/*     */     }
/*     */     
/*  67 */     return this.node;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startElement(String uri, String localName, String qName, Attributes attributes) {
/*  73 */     Node parent = getParent();
/*  74 */     Element element = this.document.createElementNS(uri, qName);
/*  75 */     for (int i = 0; i < attributes.getLength(); i++) {
/*  76 */       String attrUri = attributes.getURI(i);
/*  77 */       String attrQname = attributes.getQName(i);
/*  78 */       String value = attributes.getValue(i);
/*  79 */       if (!attrQname.startsWith("xmlns")) {
/*  80 */         element.setAttributeNS(attrUri, attrQname, value);
/*     */       }
/*     */     } 
/*  83 */     element = (Element)parent.appendChild(element);
/*  84 */     this.elements.add(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endElement(String uri, String localName, String qName) {
/*  89 */     this.elements.remove(this.elements.size() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void characters(char[] ch, int start, int length) {
/*  94 */     String data = new String(ch, start, length);
/*  95 */     Node parent = getParent();
/*  96 */     Node lastChild = parent.getLastChild();
/*  97 */     if (lastChild != null && lastChild.getNodeType() == 3) {
/*  98 */       ((Text)lastChild).appendData(data);
/*     */     } else {
/*     */       
/* 101 */       Text text = this.document.createTextNode(data);
/* 102 */       parent.appendChild(text);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void processingInstruction(String target, String data) {
/* 108 */     Node parent = getParent();
/* 109 */     ProcessingInstruction pi = this.document.createProcessingInstruction(target, data);
/* 110 */     parent.appendChild(pi);
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator locator) {}
/*     */   
/*     */   public void startDocument() {}
/*     */   
/*     */   public void endDocument() {}
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) {}
/*     */   
/*     */   public void endPrefixMapping(String prefix) {}
/*     */   
/*     */   public void ignorableWhitespace(char[] ch, int start, int length) {}
/*     */   
/*     */   public void skippedEntity(String name) {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\DomContentHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */