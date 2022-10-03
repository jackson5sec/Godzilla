/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ class StaxResult
/*     */   extends SAXResult
/*     */ {
/*     */   @Nullable
/*     */   private XMLEventWriter eventWriter;
/*     */   @Nullable
/*     */   private XMLStreamWriter streamWriter;
/*     */   
/*     */   public StaxResult(XMLEventWriter eventWriter) {
/*  63 */     StaxEventHandler handler = new StaxEventHandler(eventWriter);
/*  64 */     super.setHandler(handler);
/*  65 */     super.setLexicalHandler(handler);
/*  66 */     this.eventWriter = eventWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StaxResult(XMLStreamWriter streamWriter) {
/*  74 */     StaxStreamHandler handler = new StaxStreamHandler(streamWriter);
/*  75 */     super.setHandler(handler);
/*  76 */     super.setLexicalHandler(handler);
/*  77 */     this.streamWriter = streamWriter;
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
/*     */   @Nullable
/*     */   public XMLEventWriter getXMLEventWriter() {
/*  90 */     return this.eventWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public XMLStreamWriter getXMLStreamWriter() {
/* 102 */     return this.streamWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandler(ContentHandler handler) {
/* 112 */     throw new UnsupportedOperationException("setHandler is not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLexicalHandler(LexicalHandler handler) {
/* 121 */     throw new UnsupportedOperationException("setLexicalHandler is not supported");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\StaxResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */