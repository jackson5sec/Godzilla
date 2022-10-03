/*     */ package org.mozilla.javascript.xmlimpl;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ class XmlProcessor implements Serializable {
/*     */   private static final long serialVersionUID = 6903514433204808713L;
/*     */   private boolean ignoreComments;
/*     */   private boolean ignoreProcessingInstructions;
/*     */   private boolean ignoreWhitespace;
/*  39 */   private RhinoSAXErrorHandler errorHandler = new RhinoSAXErrorHandler(); private boolean prettyPrint; private int prettyIndent; private transient DocumentBuilderFactory dom; private transient TransformerFactory xform; private transient LinkedBlockingDeque<DocumentBuilder> documentBuilderPool;
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  42 */     stream.defaultReadObject();
/*  43 */     this.dom = DocumentBuilderFactory.newInstance();
/*  44 */     this.dom.setNamespaceAware(true);
/*  45 */     this.dom.setIgnoringComments(false);
/*  46 */     this.xform = TransformerFactory.newInstance();
/*  47 */     int poolSize = Runtime.getRuntime().availableProcessors() * 2;
/*  48 */     this.documentBuilderPool = new LinkedBlockingDeque<DocumentBuilder>(poolSize);
/*     */   }
/*     */   
/*     */   private static class RhinoSAXErrorHandler implements ErrorHandler, Serializable { private static final long serialVersionUID = 6918417235413084055L;
/*     */     
/*     */     private RhinoSAXErrorHandler() {}
/*     */     
/*     */     private void throwError(SAXParseException e) {
/*  56 */       throw ScriptRuntime.constructError("TypeError", e.getMessage(), e.getLineNumber() - 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void error(SAXParseException e) {
/*  61 */       throwError(e);
/*     */     }
/*     */     
/*     */     public void fatalError(SAXParseException e) {
/*  65 */       throwError(e);
/*     */     }
/*     */     
/*     */     public void warning(SAXParseException e) {
/*  69 */       Context.reportWarning(e.getMessage());
/*     */     } }
/*     */ 
/*     */   
/*     */   XmlProcessor() {
/*  74 */     setDefault();
/*  75 */     this.dom = DocumentBuilderFactory.newInstance();
/*  76 */     this.dom.setNamespaceAware(true);
/*  77 */     this.dom.setIgnoringComments(false);
/*  78 */     this.xform = TransformerFactory.newInstance();
/*  79 */     int poolSize = Runtime.getRuntime().availableProcessors() * 2;
/*  80 */     this.documentBuilderPool = new LinkedBlockingDeque<DocumentBuilder>(poolSize);
/*     */   }
/*     */   
/*     */   final void setDefault() {
/*  84 */     setIgnoreComments(true);
/*  85 */     setIgnoreProcessingInstructions(true);
/*  86 */     setIgnoreWhitespace(true);
/*  87 */     setPrettyPrinting(true);
/*  88 */     setPrettyIndent(2);
/*     */   }
/*     */   
/*     */   final void setIgnoreComments(boolean b) {
/*  92 */     this.ignoreComments = b;
/*     */   }
/*     */   
/*     */   final void setIgnoreWhitespace(boolean b) {
/*  96 */     this.ignoreWhitespace = b;
/*     */   }
/*     */   
/*     */   final void setIgnoreProcessingInstructions(boolean b) {
/* 100 */     this.ignoreProcessingInstructions = b;
/*     */   }
/*     */   
/*     */   final void setPrettyPrinting(boolean b) {
/* 104 */     this.prettyPrint = b;
/*     */   }
/*     */   
/*     */   final void setPrettyIndent(int i) {
/* 108 */     this.prettyIndent = i;
/*     */   }
/*     */   
/*     */   final boolean isIgnoreComments() {
/* 112 */     return this.ignoreComments;
/*     */   }
/*     */   
/*     */   final boolean isIgnoreProcessingInstructions() {
/* 116 */     return this.ignoreProcessingInstructions;
/*     */   }
/*     */   
/*     */   final boolean isIgnoreWhitespace() {
/* 120 */     return this.ignoreWhitespace;
/*     */   }
/*     */   
/*     */   final boolean isPrettyPrinting() {
/* 124 */     return this.prettyPrint;
/*     */   }
/*     */   
/*     */   final int getPrettyIndent() {
/* 128 */     return this.prettyIndent;
/*     */   }
/*     */   
/*     */   private String toXmlNewlines(String rv) {
/* 132 */     StringBuilder nl = new StringBuilder();
/* 133 */     for (int i = 0; i < rv.length(); i++) {
/* 134 */       if (rv.charAt(i) == '\r') {
/* 135 */         if (rv.charAt(i + 1) != '\n')
/*     */         {
/*     */ 
/*     */           
/* 139 */           nl.append('\n');
/*     */         }
/*     */       } else {
/* 142 */         nl.append(rv.charAt(i));
/*     */       } 
/*     */     } 
/* 145 */     return nl.toString();
/*     */   }
/*     */   
/*     */   private DocumentBuilderFactory getDomFactory() {
/* 149 */     return this.dom;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private DocumentBuilder getDocumentBuilderFromPool() throws ParserConfigurationException {
/* 155 */     DocumentBuilder builder = this.documentBuilderPool.pollFirst();
/* 156 */     if (builder == null) {
/* 157 */       builder = getDomFactory().newDocumentBuilder();
/*     */     }
/* 159 */     builder.setErrorHandler(this.errorHandler);
/* 160 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void returnDocumentBuilderToPool(DocumentBuilder db) {
/*     */     try {
/* 167 */       db.reset();
/* 168 */       this.documentBuilderPool.offerFirst(db);
/* 169 */     } catch (UnsupportedOperationException e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addProcessingInstructionsTo(List<Node> list, Node node) {
/* 175 */     if (node instanceof ProcessingInstruction) {
/* 176 */       list.add(node);
/*     */     }
/* 178 */     if (node.getChildNodes() != null) {
/* 179 */       for (int i = 0; i < node.getChildNodes().getLength(); i++) {
/* 180 */         addProcessingInstructionsTo(list, node.getChildNodes().item(i));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addCommentsTo(List<Node> list, Node node) {
/* 186 */     if (node instanceof Comment) {
/* 187 */       list.add(node);
/*     */     }
/* 189 */     if (node.getChildNodes() != null) {
/* 190 */       for (int i = 0; i < node.getChildNodes().getLength(); i++) {
/* 191 */         addProcessingInstructionsTo(list, node.getChildNodes().item(i));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void addTextNodesToRemoveAndTrim(List<Node> toRemove, Node node) {
/* 197 */     if (node instanceof Text) {
/* 198 */       Text text = (Text)node;
/* 199 */       boolean BUG_369394_IS_VALID = false;
/* 200 */       if (!BUG_369394_IS_VALID) {
/* 201 */         text.setData(text.getData().trim());
/*     */       }
/* 203 */       else if (text.getData().trim().length() == 0) {
/* 204 */         text.setData("");
/*     */       } 
/*     */       
/* 207 */       if (text.getData().length() == 0) {
/* 208 */         toRemove.add(node);
/*     */       }
/*     */     } 
/* 211 */     if (node.getChildNodes() != null) {
/* 212 */       for (int i = 0; i < node.getChildNodes().getLength(); i++) {
/* 213 */         addTextNodesToRemoveAndTrim(toRemove, node.getChildNodes().item(i));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   final Node toXml(String defaultNamespaceUri, String xml) throws SAXException {
/* 220 */     DocumentBuilder builder = null;
/*     */     try {
/* 222 */       String syntheticXml = "<parent xmlns=\"" + defaultNamespaceUri + "\">" + xml + "</parent>";
/*     */       
/* 224 */       builder = getDocumentBuilderFromPool();
/* 225 */       Document document = builder.parse(new InputSource(new StringReader(syntheticXml)));
/* 226 */       if (this.ignoreProcessingInstructions) {
/* 227 */         List<Node> list = new ArrayList<Node>();
/* 228 */         addProcessingInstructionsTo(list, document);
/* 229 */         for (Node node1 : list) {
/* 230 */           node1.getParentNode().removeChild(node1);
/*     */         }
/*     */       } 
/* 233 */       if (this.ignoreComments) {
/* 234 */         List<Node> list = new ArrayList<Node>();
/* 235 */         addCommentsTo(list, document);
/* 236 */         for (Node node1 : list) {
/* 237 */           node1.getParentNode().removeChild(node1);
/*     */         }
/*     */       } 
/* 240 */       if (this.ignoreWhitespace) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 246 */         List<Node> list = new ArrayList<Node>();
/* 247 */         addTextNodesToRemoveAndTrim(list, document);
/* 248 */         for (Node node1 : list) {
/* 249 */           node1.getParentNode().removeChild(node1);
/*     */         }
/*     */       } 
/* 252 */       NodeList rv = document.getDocumentElement().getChildNodes();
/* 253 */       if (rv.getLength() > 1)
/* 254 */         throw ScriptRuntime.constructError("SyntaxError", "XML objects may contain at most one node."); 
/* 255 */       if (rv.getLength() == 0) {
/* 256 */         Node node1 = document.createTextNode("");
/* 257 */         return node1;
/*     */       } 
/* 259 */       Node node = rv.item(0);
/* 260 */       document.getDocumentElement().removeChild(node);
/* 261 */       return node;
/*     */     }
/* 263 */     catch (IOException e) {
/* 264 */       throw new RuntimeException("Unreachable.");
/* 265 */     } catch (ParserConfigurationException e) {
/* 266 */       throw new RuntimeException(e);
/*     */     } finally {
/* 268 */       if (builder != null)
/* 269 */         returnDocumentBuilderToPool(builder); 
/*     */     } 
/*     */   }
/*     */   
/*     */   Document newDocument() {
/* 274 */     DocumentBuilder builder = null;
/*     */     
/*     */     try {
/* 277 */       builder = getDocumentBuilderFromPool();
/* 278 */       return builder.newDocument();
/* 279 */     } catch (ParserConfigurationException ex) {
/*     */       
/* 281 */       throw new RuntimeException(ex);
/*     */     } finally {
/* 283 */       if (builder != null) {
/* 284 */         returnDocumentBuilderToPool(builder);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private String toString(Node node) {
/* 290 */     DOMSource source = new DOMSource(node);
/* 291 */     StringWriter writer = new StringWriter();
/* 292 */     StreamResult result = new StreamResult(writer);
/*     */     try {
/* 294 */       Transformer transformer = this.xform.newTransformer();
/* 295 */       transformer.setOutputProperty("omit-xml-declaration", "yes");
/* 296 */       transformer.setOutputProperty("indent", "no");
/* 297 */       transformer.setOutputProperty("method", "xml");
/* 298 */       transformer.transform(source, result);
/* 299 */     } catch (TransformerConfigurationException ex) {
/*     */       
/* 301 */       throw new RuntimeException(ex);
/* 302 */     } catch (TransformerException ex) {
/*     */       
/* 304 */       throw new RuntimeException(ex);
/*     */     } 
/* 306 */     return toXmlNewlines(writer.toString());
/*     */   }
/*     */   
/*     */   String escapeAttributeValue(Object value) {
/* 310 */     String text = ScriptRuntime.toString(value);
/*     */     
/* 312 */     if (text.length() == 0) return "";
/*     */     
/* 314 */     Document dom = newDocument();
/* 315 */     Element e = dom.createElement("a");
/* 316 */     e.setAttribute("b", text);
/* 317 */     String elementText = toString(e);
/* 318 */     int begin = elementText.indexOf('"');
/* 319 */     int end = elementText.lastIndexOf('"');
/* 320 */     return elementText.substring(begin + 1, end);
/*     */   }
/*     */   
/*     */   String escapeTextValue(Object value) {
/* 324 */     if (value instanceof XMLObjectImpl) {
/* 325 */       return ((XMLObjectImpl)value).toXMLString();
/*     */     }
/*     */     
/* 328 */     String text = ScriptRuntime.toString(value);
/*     */     
/* 330 */     if (text.length() == 0) return text;
/*     */     
/* 332 */     Document dom = newDocument();
/* 333 */     Element e = dom.createElement("a");
/* 334 */     e.setTextContent(text);
/* 335 */     String elementText = toString(e);
/*     */     
/* 337 */     int begin = elementText.indexOf('>') + 1;
/* 338 */     int end = elementText.lastIndexOf('<');
/* 339 */     return (begin < end) ? elementText.substring(begin, end) : "";
/*     */   }
/*     */ 
/*     */   
/*     */   private String escapeElementValue(String s) {
/* 344 */     return escapeTextValue(s);
/*     */   }
/*     */ 
/*     */   
/*     */   private String elementToXmlString(Element element) {
/* 349 */     Element copy = (Element)element.cloneNode(true);
/* 350 */     if (this.prettyPrint) {
/* 351 */       beautifyElement(copy, 0);
/*     */     }
/* 353 */     return toString(copy);
/*     */   }
/*     */ 
/*     */   
/*     */   final String ecmaToXmlString(Node node) {
/* 358 */     StringBuilder s = new StringBuilder();
/* 359 */     int indentLevel = 0;
/* 360 */     if (this.prettyPrint) {
/* 361 */       for (int i = 0; i < indentLevel; i++) {
/* 362 */         s.append(' ');
/*     */       }
/*     */     }
/* 365 */     if (node instanceof Text) {
/* 366 */       String data = ((Text)node).getData();
/*     */       
/* 368 */       String v = this.prettyPrint ? data.trim() : data;
/* 369 */       s.append(escapeElementValue(v));
/* 370 */       return s.toString();
/*     */     } 
/* 372 */     if (node instanceof Attr) {
/* 373 */       String value = ((Attr)node).getValue();
/* 374 */       s.append(escapeAttributeValue(value));
/* 375 */       return s.toString();
/*     */     } 
/* 377 */     if (node instanceof Comment) {
/* 378 */       s.append("<!--" + ((Comment)node).getNodeValue() + "-->");
/* 379 */       return s.toString();
/*     */     } 
/* 381 */     if (node instanceof ProcessingInstruction) {
/* 382 */       ProcessingInstruction pi = (ProcessingInstruction)node;
/* 383 */       s.append("<?" + pi.getTarget() + " " + pi.getData() + "?>");
/* 384 */       return s.toString();
/*     */     } 
/* 386 */     s.append(elementToXmlString((Element)node));
/* 387 */     return s.toString();
/*     */   }
/*     */   
/*     */   private void beautifyElement(Element e, int indent) {
/* 391 */     StringBuilder s = new StringBuilder();
/* 392 */     s.append('\n');
/* 393 */     for (int i = 0; i < indent; i++) {
/* 394 */       s.append(' ');
/*     */     }
/* 396 */     String afterContent = s.toString();
/* 397 */     for (int j = 0; j < this.prettyIndent; j++) {
/* 398 */       s.append(' ');
/*     */     }
/* 400 */     String beforeContent = s.toString();
/*     */ 
/*     */ 
/*     */     
/* 404 */     ArrayList<Node> toIndent = new ArrayList<Node>();
/* 405 */     boolean indentChildren = false; int k;
/* 406 */     for (k = 0; k < e.getChildNodes().getLength(); k++) {
/* 407 */       if (k == 1) indentChildren = true; 
/* 408 */       if (e.getChildNodes().item(k) instanceof Text) {
/* 409 */         toIndent.add(e.getChildNodes().item(k));
/*     */       } else {
/* 411 */         indentChildren = true;
/* 412 */         toIndent.add(e.getChildNodes().item(k));
/*     */       } 
/*     */     } 
/* 415 */     if (indentChildren) {
/* 416 */       for (k = 0; k < toIndent.size(); k++) {
/* 417 */         e.insertBefore(e.getOwnerDocument().createTextNode(beforeContent), toIndent.get(k));
/*     */       }
/*     */     }
/*     */     
/* 421 */     NodeList nodes = e.getChildNodes();
/* 422 */     ArrayList<Element> list = new ArrayList<Element>();
/* 423 */     for (int m = 0; m < nodes.getLength(); m++) {
/* 424 */       if (nodes.item(m) instanceof Element) {
/* 425 */         list.add((Element)nodes.item(m));
/*     */       }
/*     */     } 
/* 428 */     for (Element elem : list) {
/* 429 */       beautifyElement(elem, indent + this.prettyIndent);
/*     */     }
/* 431 */     if (indentChildren)
/* 432 */       e.appendChild(e.getOwnerDocument().createTextNode(afterContent)); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\XmlProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */