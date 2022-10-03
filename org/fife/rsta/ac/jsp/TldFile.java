/*     */ package org.fife.rsta.ac.jsp;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ public class TldFile
/*     */ {
/*     */   private JspCompletionProvider provider;
/*     */   private File jar;
/*     */   private List<TldElement> tldElems;
/*     */   
/*     */   public TldFile(JspCompletionProvider provider, File jar) throws IOException {
/*  42 */     this.provider = provider;
/*  43 */     this.jar = jar;
/*  44 */     this.tldElems = loadTldElems();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ParameterizedCompletion.Parameter> getAttributesForTag(String tagName) {
/*  50 */     for (TldElement elem : this.tldElems) {
/*  51 */       if (elem.getName().equals(tagName)) {
/*  52 */         return elem.getAttributes();
/*     */       }
/*     */     } 
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private String getChildText(Node elem) {
/*  60 */     StringBuilder sb = new StringBuilder();
/*  61 */     NodeList children = elem.getChildNodes();
/*  62 */     for (int i = 0; i < children.getLength(); i++) {
/*  63 */       Node child = children.item(i);
/*  64 */       if (child instanceof org.w3c.dom.Text) {
/*  65 */         sb.append(child.getNodeValue());
/*     */       }
/*     */     } 
/*  68 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public TldElement getElement(int index) {
/*  73 */     return this.tldElems.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getElementCount() {
/*  78 */     return this.tldElems.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<TldElement> loadTldElems() throws IOException {
/*  84 */     JarFile jar = new JarFile(this.jar);
/*  85 */     List<TldElement> elems = null;
/*     */     
/*  87 */     Enumeration<JarEntry> entries = jar.entries();
/*  88 */     while (entries.hasMoreElements()) {
/*  89 */       JarEntry entry = entries.nextElement();
/*  90 */       if (entry.getName().endsWith("tld")) {
/*     */         
/*  92 */         InputStream in = jar.getInputStream(entry);
/*  93 */         elems = parseTld(in);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  99 */         in.close();
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     jar.close();
/* 104 */     return elems;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<TldElement> parseTld(InputStream in) throws IOException {
/*     */     Document doc;
/* 111 */     List<TldElement> tldElems = new ArrayList<>();
/*     */     
/* 113 */     BufferedInputStream bin = new BufferedInputStream(in);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*     */     try {
/* 125 */       DocumentBuilder db = dbf.newDocumentBuilder();
/* 126 */       doc = db.parse(bin);
/* 127 */     } catch (Exception e) {
/* 128 */       throw new IOException(e.getMessage());
/*     */     } 
/* 130 */     Element root = doc.getDocumentElement();
/*     */     
/* 132 */     NodeList nl = root.getElementsByTagName("uri");
/* 133 */     if (nl.getLength() != 1) {
/* 134 */       throw new IOException("Expected 1 'uri' tag; found: " + nl.getLength());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 139 */     nl = root.getElementsByTagName("tag");
/* 140 */     for (int i = 0; i < nl.getLength(); i++) {
/* 141 */       Element elem = (Element)nl.item(i);
/* 142 */       String name = getChildText(elem.getElementsByTagName("name").item(0));
/* 143 */       String desc = getChildText(elem.getElementsByTagName("description").item(0));
/* 144 */       TldElement tldElem = new TldElement(this.provider, name, desc);
/* 145 */       tldElems.add(tldElem);
/* 146 */       NodeList attrNl = elem.getElementsByTagName("attribute");
/*     */       
/* 148 */       List<TldAttribute.TldAttributeParam> attrs = new ArrayList<>(attrNl.getLength());
/* 149 */       for (int j = 0; j < attrNl.getLength(); j++) {
/* 150 */         Element attrElem = (Element)attrNl.item(j);
/* 151 */         name = getChildText(attrElem.getElementsByTagName("name").item(0));
/* 152 */         desc = getChildText(attrElem.getElementsByTagName("description").item(0));
/* 153 */         boolean required = Boolean.parseBoolean(getChildText(attrElem.getElementsByTagName("required").item(0)));
/* 154 */         boolean rtexprValue = false;
/* 155 */         TldAttribute.TldAttributeParam param = new TldAttribute.TldAttributeParam(null, name, required, rtexprValue);
/*     */         
/* 157 */         param.setDescription(desc);
/* 158 */         attrs.add(param);
/*     */       } 
/* 160 */       tldElem.setAttributes(attrs);
/*     */     } 
/*     */     
/* 163 */     return tldElems;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\jsp\TldFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */