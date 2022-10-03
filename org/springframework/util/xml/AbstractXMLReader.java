/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
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
/*     */ abstract class AbstractXMLReader
/*     */   implements XMLReader
/*     */ {
/*     */   @Nullable
/*     */   private DTDHandler dtdHandler;
/*     */   @Nullable
/*     */   private ContentHandler contentHandler;
/*     */   @Nullable
/*     */   private EntityResolver entityResolver;
/*     */   @Nullable
/*     */   private ErrorHandler errorHandler;
/*     */   @Nullable
/*     */   private LexicalHandler lexicalHandler;
/*     */   
/*     */   public void setContentHandler(@Nullable ContentHandler contentHandler) {
/*  62 */     this.contentHandler = contentHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ContentHandler getContentHandler() {
/*  68 */     return this.contentHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDTDHandler(@Nullable DTDHandler dtdHandler) {
/*  73 */     this.dtdHandler = dtdHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DTDHandler getDTDHandler() {
/*  79 */     return this.dtdHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntityResolver(@Nullable EntityResolver entityResolver) {
/*  84 */     this.entityResolver = entityResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public EntityResolver getEntityResolver() {
/*  90 */     return this.entityResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setErrorHandler(@Nullable ErrorHandler errorHandler) {
/*  95 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ErrorHandler getErrorHandler() {
/* 101 */     return this.errorHandler;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected LexicalHandler getLexicalHandler() {
/* 106 */     return this.lexicalHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 117 */     if (name.startsWith("http://xml.org/sax/features/")) {
/* 118 */       return false;
/*     */     }
/*     */     
/* 121 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 132 */     if (name.startsWith("http://xml.org/sax/features/")) {
/* 133 */       if (value) {
/* 134 */         throw new SAXNotSupportedException(name);
/*     */       }
/*     */     } else {
/*     */       
/* 138 */       throw new SAXNotRecognizedException(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 149 */     if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 150 */       return this.lexicalHandler;
/*     */     }
/*     */     
/* 153 */     throw new SAXNotRecognizedException(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 163 */     if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/* 164 */       this.lexicalHandler = (LexicalHandler)value;
/*     */     } else {
/*     */       
/* 167 */       throw new SAXNotRecognizedException(name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\AbstractXMLReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */