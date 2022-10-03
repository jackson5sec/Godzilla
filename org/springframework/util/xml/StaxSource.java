/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.XMLReader;
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
/*     */ 
/*     */ 
/*     */ class StaxSource
/*     */   extends SAXSource
/*     */ {
/*     */   @Nullable
/*     */   private XMLEventReader eventReader;
/*     */   @Nullable
/*     */   private XMLStreamReader streamReader;
/*     */   
/*     */   StaxSource(XMLEventReader eventReader) {
/*  65 */     super(new StaxEventXMLReader(eventReader), new InputSource());
/*  66 */     this.eventReader = eventReader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StaxSource(XMLStreamReader streamReader) {
/*  77 */     super(new StaxStreamXMLReader(streamReader), new InputSource());
/*  78 */     this.streamReader = streamReader;
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
/*     */   XMLEventReader getXMLEventReader() {
/*  91 */     return this.eventReader;
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
/*     */   XMLStreamReader getXMLStreamReader() {
/* 103 */     return this.streamReader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputSource(InputSource inputSource) {
/* 113 */     throw new UnsupportedOperationException("setInputSource is not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXMLReader(XMLReader reader) {
/* 122 */     throw new UnsupportedOperationException("setXMLReader is not supported");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\StaxSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */