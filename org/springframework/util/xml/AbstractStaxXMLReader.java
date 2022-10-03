/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
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
/*     */ abstract class AbstractStaxXMLReader
/*     */   extends AbstractXMLReader
/*     */ {
/*     */   private static final String NAMESPACES_FEATURE_NAME = "http://xml.org/sax/features/namespaces";
/*     */   private static final String NAMESPACE_PREFIXES_FEATURE_NAME = "http://xml.org/sax/features/namespace-prefixes";
/*     */   private static final String IS_STANDALONE_FEATURE_NAME = "http://xml.org/sax/features/is-standalone";
/*     */   private boolean namespacesFeature = true;
/*     */   private boolean namespacePrefixesFeature = false;
/*     */   @Nullable
/*     */   private Boolean isStandalone;
/*  63 */   private final Map<String, String> namespaces = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  68 */     switch (name) {
/*     */       case "http://xml.org/sax/features/namespaces":
/*  70 */         return this.namespacesFeature;
/*     */       case "http://xml.org/sax/features/namespace-prefixes":
/*  72 */         return this.namespacePrefixesFeature;
/*     */       case "http://xml.org/sax/features/is-standalone":
/*  74 */         if (this.isStandalone != null) {
/*  75 */           return this.isStandalone.booleanValue();
/*     */         }
/*     */         
/*  78 */         throw new SAXNotSupportedException("startDocument() callback not completed yet");
/*     */     } 
/*     */     
/*  81 */     return super.getFeature(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  87 */     if ("http://xml.org/sax/features/namespaces".equals(name)) {
/*  88 */       this.namespacesFeature = value;
/*     */     }
/*  90 */     else if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
/*  91 */       this.namespacePrefixesFeature = value;
/*     */     } else {
/*     */       
/*  94 */       super.setFeature(name, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setStandalone(boolean standalone) {
/*  99 */     this.isStandalone = Boolean.valueOf(standalone);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasNamespacesFeature() {
/* 106 */     return this.namespacesFeature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasNamespacePrefixesFeature() {
/* 113 */     return this.namespacePrefixesFeature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String toQualifiedName(QName qName) {
/* 124 */     String prefix = qName.getPrefix();
/* 125 */     if (!StringUtils.hasLength(prefix)) {
/* 126 */       return qName.getLocalPart();
/*     */     }
/*     */     
/* 129 */     return prefix + ":" + qName.getLocalPart();
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
/*     */   
/*     */   public final void parse(InputSource ignored) throws SAXException {
/* 142 */     parse();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void parse(String ignored) throws SAXException {
/* 153 */     parse();
/*     */   }
/*     */   
/*     */   private void parse() throws SAXException {
/*     */     try {
/* 158 */       parseInternal();
/*     */     }
/* 160 */     catch (XMLStreamException ex) {
/* 161 */       Locator locator = null;
/* 162 */       if (ex.getLocation() != null) {
/* 163 */         locator = new StaxLocator(ex.getLocation());
/*     */       }
/* 165 */       SAXParseException saxException = new SAXParseException(ex.getMessage(), locator, ex);
/* 166 */       if (getErrorHandler() != null) {
/* 167 */         getErrorHandler().fatalError(saxException);
/*     */       } else {
/*     */         
/* 170 */         throw saxException;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void parseInternal() throws SAXException, XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startPrefixMapping(@Nullable String prefix, String namespace) throws SAXException {
/* 186 */     if (getContentHandler() != null && StringUtils.hasLength(namespace)) {
/* 187 */       if (prefix == null) {
/* 188 */         prefix = "";
/*     */       }
/* 190 */       if (!namespace.equals(this.namespaces.get(prefix))) {
/* 191 */         getContentHandler().startPrefixMapping(prefix, namespace);
/* 192 */         this.namespaces.put(prefix, namespace);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void endPrefixMapping(String prefix) throws SAXException {
/* 202 */     if (getContentHandler() != null && this.namespaces.containsKey(prefix)) {
/* 203 */       getContentHandler().endPrefixMapping(prefix);
/* 204 */       this.namespaces.remove(prefix);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StaxLocator
/*     */     implements Locator
/*     */   {
/*     */     private final Location location;
/*     */ 
/*     */ 
/*     */     
/*     */     public StaxLocator(Location location) {
/* 219 */       this.location = location;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPublicId() {
/* 224 */       return this.location.getPublicId();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSystemId() {
/* 229 */       return this.location.getSystemId();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLineNumber() {
/* 234 */       return this.location.getLineNumber();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnNumber() {
/* 239 */       return this.location.getColumnNumber();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\AbstractStaxXMLReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */