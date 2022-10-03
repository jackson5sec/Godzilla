/*     */ package org.mozilla.javascript.xmlimpl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.w3c.dom.UserDataHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ class XmlNode
/*     */   implements Serializable
/*     */ {
/*     */   private static final String XML_NAMESPACES_NAMESPACE_URI = "http://www.w3.org/2000/xmlns/";
/*  26 */   private static final String USER_DATA_XMLNODE_KEY = XmlNode.class.getName();
/*     */   
/*     */   private static final boolean DOM_LEVEL_3 = true;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   private static XmlNode getUserData(Node node) {
/*  32 */     return (XmlNode)node.getUserData(USER_DATA_XMLNODE_KEY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setUserData(Node node, XmlNode wrap) {
/*  39 */     node.setUserData(USER_DATA_XMLNODE_KEY, wrap, wrap.events);
/*     */   }
/*     */ 
/*     */   
/*     */   private static XmlNode createImpl(Node node) {
/*  44 */     if (node instanceof Document) throw new IllegalArgumentException(); 
/*  45 */     XmlNode rv = null;
/*  46 */     if (getUserData(node) == null) {
/*  47 */       rv = new XmlNode();
/*  48 */       rv.dom = node;
/*  49 */       setUserData(node, rv);
/*     */     } else {
/*  51 */       rv = getUserData(node);
/*     */     } 
/*  53 */     return rv;
/*     */   }
/*     */   
/*     */   static XmlNode newElementWithText(XmlProcessor processor, XmlNode reference, QName qname, String value) {
/*  57 */     if (reference instanceof Document) throw new IllegalArgumentException("Cannot use Document node as reference"); 
/*  58 */     Document document = null;
/*  59 */     if (reference != null) {
/*  60 */       document = reference.dom.getOwnerDocument();
/*     */     } else {
/*  62 */       document = processor.newDocument();
/*     */     } 
/*  64 */     Node referenceDom = (reference != null) ? reference.dom : null;
/*  65 */     Namespace ns = qname.getNamespace();
/*  66 */     Element e = (ns == null || ns.getUri().length() == 0) ? document.createElementNS(null, qname.getLocalName()) : document.createElementNS(ns.getUri(), qname.qualify(referenceDom));
/*     */ 
/*     */ 
/*     */     
/*  70 */     if (value != null) {
/*  71 */       e.appendChild(document.createTextNode(value));
/*     */     }
/*  73 */     return createImpl(e);
/*     */   }
/*     */   
/*     */   static XmlNode createText(XmlProcessor processor, String value) {
/*  77 */     return createImpl(processor.newDocument().createTextNode(value));
/*     */   }
/*     */   
/*     */   static XmlNode createElementFromNode(Node node) {
/*  81 */     if (node instanceof Document)
/*  82 */       node = ((Document)node).getDocumentElement(); 
/*  83 */     return createImpl(node);
/*     */   }
/*     */   
/*     */   static XmlNode createElement(XmlProcessor processor, String namespaceUri, String xml) throws SAXException {
/*  87 */     return createImpl(processor.toXml(namespaceUri, xml));
/*     */   }
/*     */   
/*     */   static XmlNode createEmpty(XmlProcessor processor) {
/*  91 */     return createText(processor, "");
/*     */   }
/*     */   
/*     */   private static XmlNode copy(XmlNode other) {
/*  95 */     return createImpl(other.dom.cloneNode(true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   private UserDataHandler events = new XmlNodeUserDataHandler();
/*     */ 
/*     */   
/*     */   private Node dom;
/*     */ 
/*     */   
/*     */   private XML xml;
/*     */ 
/*     */   
/*     */   String debug() {
/* 110 */     XmlProcessor raw = new XmlProcessor();
/* 111 */     raw.setIgnoreComments(false);
/* 112 */     raw.setIgnoreProcessingInstructions(false);
/* 113 */     raw.setIgnoreWhitespace(false);
/* 114 */     raw.setPrettyPrinting(false);
/* 115 */     return raw.ecmaToXmlString(this.dom);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     return "XmlNode: type=" + this.dom.getNodeType() + " dom=" + this.dom.toString();
/*     */   }
/*     */   
/*     */   XML getXml() {
/* 124 */     return this.xml;
/*     */   }
/*     */   
/*     */   void setXml(XML xml) {
/* 128 */     this.xml = xml;
/*     */   }
/*     */   
/*     */   int getChildCount() {
/* 132 */     return this.dom.getChildNodes().getLength();
/*     */   }
/*     */   
/*     */   XmlNode parent() {
/* 136 */     Node domParent = this.dom.getParentNode();
/* 137 */     if (domParent instanceof Document) return null; 
/* 138 */     if (domParent == null) return null; 
/* 139 */     return createImpl(domParent);
/*     */   }
/*     */   
/*     */   int getChildIndex() {
/* 143 */     if (isAttributeType()) return -1; 
/* 144 */     if (parent() == null) return -1; 
/* 145 */     NodeList siblings = this.dom.getParentNode().getChildNodes();
/* 146 */     for (int i = 0; i < siblings.getLength(); i++) {
/* 147 */       if (siblings.item(i) == this.dom) {
/* 148 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 152 */     throw new RuntimeException("Unreachable.");
/*     */   }
/*     */   
/*     */   void removeChild(int index) {
/* 156 */     this.dom.removeChild(this.dom.getChildNodes().item(index));
/*     */   }
/*     */   
/*     */   String toXmlString(XmlProcessor processor) {
/* 160 */     return processor.ecmaToXmlString(this.dom);
/*     */   }
/*     */ 
/*     */   
/*     */   String ecmaValue() {
/* 165 */     if (isTextType())
/* 166 */       return ((Text)this.dom).getData(); 
/* 167 */     if (isAttributeType())
/* 168 */       return ((Attr)this.dom).getValue(); 
/* 169 */     if (isProcessingInstructionType())
/* 170 */       return ((ProcessingInstruction)this.dom).getData(); 
/* 171 */     if (isCommentType())
/* 172 */       return ((Comment)this.dom).getNodeValue(); 
/* 173 */     if (isElementType()) {
/* 174 */       throw new RuntimeException("Unimplemented ecmaValue() for elements.");
/*     */     }
/* 176 */     throw new RuntimeException("Unimplemented for node " + this.dom);
/*     */   }
/*     */ 
/*     */   
/*     */   void deleteMe() {
/* 181 */     if (this.dom instanceof Attr) {
/* 182 */       Attr attr = (Attr)this.dom;
/* 183 */       attr.getOwnerElement().getAttributes().removeNamedItemNS(attr.getNamespaceURI(), attr.getLocalName());
/*     */     }
/* 185 */     else if (this.dom.getParentNode() != null) {
/* 186 */       this.dom.getParentNode().removeChild(this.dom);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void normalize() {
/* 195 */     this.dom.normalize();
/*     */   }
/*     */   
/*     */   void insertChildAt(int index, XmlNode node) {
/* 199 */     Node parent = this.dom;
/* 200 */     Node child = parent.getOwnerDocument().importNode(node.dom, true);
/* 201 */     if (parent.getChildNodes().getLength() < index)
/*     */     {
/* 203 */       throw new IllegalArgumentException("index=" + index + " length=" + parent.getChildNodes().getLength());
/*     */     }
/* 205 */     if (parent.getChildNodes().getLength() == index) {
/* 206 */       parent.appendChild(child);
/*     */     } else {
/* 208 */       parent.insertBefore(child, parent.getChildNodes().item(index));
/*     */     } 
/*     */   }
/*     */   
/*     */   void insertChildrenAt(int index, XmlNode[] nodes) {
/* 213 */     for (int i = 0; i < nodes.length; i++) {
/* 214 */       insertChildAt(index + i, nodes[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   XmlNode getChild(int index) {
/* 219 */     Node child = this.dom.getChildNodes().item(index);
/* 220 */     return createImpl(child);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasChildElement() {
/* 225 */     NodeList nodes = this.dom.getChildNodes();
/* 226 */     for (int i = 0; i < nodes.getLength(); i++) {
/* 227 */       if (nodes.item(i).getNodeType() == 1) return true; 
/*     */     } 
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isSameNode(XmlNode other) {
/* 234 */     return (this.dom == other.dom);
/*     */   }
/*     */   
/*     */   private String toUri(String ns) {
/* 238 */     return (ns == null) ? "" : ns;
/*     */   }
/*     */   
/*     */   private void addNamespaces(Namespaces rv, Element element) {
/* 242 */     if (element == null) throw new RuntimeException("element must not be null"); 
/* 243 */     String myDefaultNamespace = toUri(element.lookupNamespaceURI(null));
/* 244 */     String parentDefaultNamespace = "";
/* 245 */     if (element.getParentNode() != null) {
/* 246 */       parentDefaultNamespace = toUri(element.getParentNode().lookupNamespaceURI(null));
/*     */     }
/* 248 */     if (!myDefaultNamespace.equals(parentDefaultNamespace) || !(element.getParentNode() instanceof Element)) {
/* 249 */       rv.declare(Namespace.create("", myDefaultNamespace));
/*     */     }
/* 251 */     NamedNodeMap attributes = element.getAttributes();
/* 252 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 253 */       Attr attr = (Attr)attributes.item(i);
/* 254 */       if (attr.getPrefix() != null && attr.getPrefix().equals("xmlns")) {
/* 255 */         rv.declare(Namespace.create(attr.getLocalName(), attr.getValue()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private Namespaces getAllNamespaces() {
/* 261 */     Namespaces rv = new Namespaces();
/*     */     
/* 263 */     Node target = this.dom;
/* 264 */     if (target instanceof Attr) {
/* 265 */       target = ((Attr)target).getOwnerElement();
/*     */     }
/* 267 */     while (target != null) {
/* 268 */       if (target instanceof Element) {
/* 269 */         addNamespaces(rv, (Element)target);
/*     */       }
/* 271 */       target = target.getParentNode();
/*     */     } 
/*     */     
/* 274 */     rv.declare(Namespace.create("", ""));
/* 275 */     return rv;
/*     */   }
/*     */   
/*     */   Namespace[] getInScopeNamespaces() {
/* 279 */     Namespaces rv = getAllNamespaces();
/* 280 */     return rv.getNamespaces();
/*     */   }
/*     */ 
/*     */   
/*     */   Namespace[] getNamespaceDeclarations() {
/* 285 */     if (this.dom instanceof Element) {
/* 286 */       Namespaces rv = new Namespaces();
/* 287 */       addNamespaces(rv, (Element)this.dom);
/* 288 */       return rv.getNamespaces();
/*     */     } 
/* 290 */     return new Namespace[0];
/*     */   }
/*     */ 
/*     */   
/*     */   Namespace getNamespaceDeclaration(String prefix) {
/* 295 */     if (prefix.equals("") && this.dom instanceof Attr)
/*     */     {
/* 297 */       return Namespace.create("", "");
/*     */     }
/* 299 */     Namespaces rv = getAllNamespaces();
/* 300 */     return rv.getNamespace(prefix);
/*     */   }
/*     */   
/*     */   Namespace getNamespaceDeclaration() {
/* 304 */     if (this.dom.getPrefix() == null) return getNamespaceDeclaration(""); 
/* 305 */     return getNamespaceDeclaration(this.dom.getPrefix());
/*     */   }
/*     */   
/*     */   static class XmlNodeUserDataHandler
/*     */     implements UserDataHandler, Serializable {
/*     */     private static final long serialVersionUID = 4666895518900769588L;
/*     */     
/*     */     public void handle(short operation, String key, Object data, Node src, Node dest) {}
/*     */   }
/*     */   
/*     */   private static class Namespaces {
/* 316 */     private Map<String, String> map = new HashMap<String, String>();
/* 317 */     private Map<String, String> uriToPrefix = new HashMap<String, String>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void declare(XmlNode.Namespace n) {
/* 323 */       if (this.map.get(n.prefix) == null) {
/* 324 */         this.map.put(n.prefix, n.uri);
/*     */       }
/*     */ 
/*     */       
/* 328 */       if (this.uriToPrefix.get(n.uri) == null) {
/* 329 */         this.uriToPrefix.put(n.uri, n.prefix);
/*     */       }
/*     */     }
/*     */     
/*     */     XmlNode.Namespace getNamespaceByUri(String uri) {
/* 334 */       if (this.uriToPrefix.get(uri) == null) return null; 
/* 335 */       return XmlNode.Namespace.create(uri, this.uriToPrefix.get(uri));
/*     */     }
/*     */     
/*     */     XmlNode.Namespace getNamespace(String prefix) {
/* 339 */       if (this.map.get(prefix) == null) return null; 
/* 340 */       return XmlNode.Namespace.create(prefix, this.map.get(prefix));
/*     */     }
/*     */     
/*     */     XmlNode.Namespace[] getNamespaces() {
/* 344 */       ArrayList<XmlNode.Namespace> rv = new ArrayList<XmlNode.Namespace>();
/* 345 */       for (String prefix : this.map.keySet()) {
/* 346 */         String uri = this.map.get(prefix);
/* 347 */         XmlNode.Namespace n = XmlNode.Namespace.create(prefix, uri);
/* 348 */         if (!n.isEmpty()) {
/* 349 */           rv.add(n);
/*     */         }
/*     */       } 
/* 352 */       return rv.<XmlNode.Namespace>toArray(new XmlNode.Namespace[rv.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */   final XmlNode copy() {
/* 357 */     return copy(this);
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isParentType() {
/* 362 */     return isElementType();
/*     */   }
/*     */   
/*     */   final boolean isTextType() {
/* 366 */     return (this.dom.getNodeType() == 3 || this.dom.getNodeType() == 4);
/*     */   }
/*     */   
/*     */   final boolean isAttributeType() {
/* 370 */     return (this.dom.getNodeType() == 2);
/*     */   }
/*     */   
/*     */   final boolean isProcessingInstructionType() {
/* 374 */     return (this.dom.getNodeType() == 7);
/*     */   }
/*     */   
/*     */   final boolean isCommentType() {
/* 378 */     return (this.dom.getNodeType() == 8);
/*     */   }
/*     */   
/*     */   final boolean isElementType() {
/* 382 */     return (this.dom.getNodeType() == 1);
/*     */   }
/*     */   
/*     */   final void renameNode(QName qname) {
/* 386 */     this.dom = this.dom.getOwnerDocument().renameNode(this.dom, qname.getNamespace().getUri(), qname.qualify(this.dom));
/*     */   }
/*     */   
/*     */   void invalidateNamespacePrefix() {
/* 390 */     if (!(this.dom instanceof Element)) throw new IllegalStateException(); 
/* 391 */     String prefix = this.dom.getPrefix();
/* 392 */     QName after = QName.create(this.dom.getNamespaceURI(), this.dom.getLocalName(), null);
/* 393 */     renameNode(after);
/* 394 */     NamedNodeMap attrs = this.dom.getAttributes();
/* 395 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 396 */       if (attrs.item(i).getPrefix().equals(prefix)) {
/* 397 */         createImpl(attrs.item(i)).renameNode(QName.create(attrs.item(i).getNamespaceURI(), attrs.item(i).getLocalName(), null));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void declareNamespace(Element e, String prefix, String uri) {
/* 403 */     if (prefix.length() > 0) {
/* 404 */       e.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, uri);
/*     */     } else {
/* 406 */       e.setAttribute("xmlns", uri);
/*     */     } 
/*     */   }
/*     */   
/*     */   void declareNamespace(String prefix, String uri) {
/* 411 */     if (!(this.dom instanceof Element)) throw new IllegalStateException(); 
/* 412 */     if (this.dom.lookupNamespaceURI(uri) == null || !this.dom.lookupNamespaceURI(uri).equals(prefix)) {
/*     */ 
/*     */       
/* 415 */       Element e = (Element)this.dom;
/* 416 */       declareNamespace(e, prefix, uri);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Namespace getDefaultNamespace() {
/* 421 */     String prefix = "";
/* 422 */     String uri = (this.dom.lookupNamespaceURI(null) == null) ? "" : this.dom.lookupNamespaceURI(null);
/* 423 */     return Namespace.create(prefix, uri);
/*     */   }
/*     */   
/*     */   private String getExistingPrefixFor(Namespace namespace) {
/* 427 */     if (getDefaultNamespace().getUri().equals(namespace.getUri())) {
/* 428 */       return "";
/*     */     }
/* 430 */     return this.dom.lookupPrefix(namespace.getUri());
/*     */   }
/*     */   
/*     */   private Namespace getNodeNamespace() {
/* 434 */     String uri = this.dom.getNamespaceURI();
/* 435 */     String prefix = this.dom.getPrefix();
/* 436 */     if (uri == null) uri = ""; 
/* 437 */     if (prefix == null) prefix = ""; 
/* 438 */     return Namespace.create(prefix, uri);
/*     */   }
/*     */   
/*     */   Namespace getNamespace() {
/* 442 */     return getNodeNamespace();
/*     */   }
/*     */   
/*     */   void removeNamespace(Namespace namespace) {
/* 446 */     Namespace current = getNodeNamespace();
/*     */ 
/*     */     
/* 449 */     if (namespace.is(current))
/* 450 */       return;  NamedNodeMap attrs = this.dom.getAttributes();
/* 451 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 452 */       XmlNode attr = createImpl(attrs.item(i));
/* 453 */       if (namespace.is(attr.getNodeNamespace())) {
/*     */         return;
/*     */       }
/*     */     } 
/* 457 */     String existingPrefix = getExistingPrefixFor(namespace);
/* 458 */     if (existingPrefix != null) {
/* 459 */       if (namespace.isUnspecifiedPrefix()) {
/*     */ 
/*     */         
/* 462 */         declareNamespace(existingPrefix, getDefaultNamespace().getUri());
/*     */       }
/* 464 */       else if (existingPrefix.equals(namespace.getPrefix())) {
/* 465 */         declareNamespace(existingPrefix, getDefaultNamespace().getUri());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setProcessingInstructionName(String localName) {
/* 474 */     ProcessingInstruction pi = (ProcessingInstruction)this.dom;
/*     */     
/* 476 */     pi.getParentNode().replaceChild(pi, pi.getOwnerDocument().createProcessingInstruction(localName, pi.getData()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setLocalName(String localName) {
/* 483 */     if (this.dom instanceof ProcessingInstruction) {
/* 484 */       setProcessingInstructionName(localName);
/*     */     } else {
/* 486 */       String prefix = this.dom.getPrefix();
/* 487 */       if (prefix == null) prefix = ""; 
/* 488 */       this.dom = this.dom.getOwnerDocument().renameNode(this.dom, this.dom.getNamespaceURI(), QName.qualify(prefix, localName));
/*     */     } 
/*     */   }
/*     */   
/*     */   final QName getQname() {
/* 493 */     String uri = (this.dom.getNamespaceURI() == null) ? "" : this.dom.getNamespaceURI();
/* 494 */     String prefix = (this.dom.getPrefix() == null) ? "" : this.dom.getPrefix();
/* 495 */     return QName.create(uri, this.dom.getLocalName(), prefix);
/*     */   }
/*     */   
/*     */   void addMatchingChildren(XMLList result, Filter filter) {
/* 499 */     Node node = this.dom;
/* 500 */     NodeList children = node.getChildNodes();
/* 501 */     for (int i = 0; i < children.getLength(); i++) {
/* 502 */       Node childnode = children.item(i);
/* 503 */       XmlNode child = createImpl(childnode);
/* 504 */       if (filter.accept(childnode)) {
/* 505 */         result.addToList(child);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   XmlNode[] getMatchingChildren(Filter filter) {
/* 511 */     ArrayList<XmlNode> rv = new ArrayList<XmlNode>();
/* 512 */     NodeList nodes = this.dom.getChildNodes();
/* 513 */     for (int i = 0; i < nodes.getLength(); i++) {
/* 514 */       Node node = nodes.item(i);
/* 515 */       if (filter.accept(node)) {
/* 516 */         rv.add(createImpl(node));
/*     */       }
/*     */     } 
/* 519 */     return rv.<XmlNode>toArray(new XmlNode[rv.size()]);
/*     */   }
/*     */   
/*     */   XmlNode[] getAttributes() {
/* 523 */     NamedNodeMap attrs = this.dom.getAttributes();
/*     */     
/* 525 */     if (attrs == null) throw new IllegalStateException("Must be element."); 
/* 526 */     XmlNode[] rv = new XmlNode[attrs.getLength()];
/* 527 */     for (int i = 0; i < attrs.getLength(); i++) {
/* 528 */       rv[i] = createImpl(attrs.item(i));
/*     */     }
/* 530 */     return rv;
/*     */   }
/*     */   
/*     */   String getAttributeValue() {
/* 534 */     return ((Attr)this.dom).getValue();
/*     */   }
/*     */   
/*     */   void setAttribute(QName name, String value) {
/* 538 */     if (!(this.dom instanceof Element)) throw new IllegalStateException("Can only set attribute on elements."); 
/* 539 */     name.setAttribute((Element)this.dom, value);
/*     */   }
/*     */   
/*     */   void replaceWith(XmlNode other) {
/* 543 */     Node replacement = other.dom;
/* 544 */     if (replacement.getOwnerDocument() != this.dom.getOwnerDocument()) {
/* 545 */       replacement = this.dom.getOwnerDocument().importNode(replacement, true);
/*     */     }
/* 547 */     this.dom.getParentNode().replaceChild(replacement, this.dom);
/*     */   }
/*     */   
/*     */   String ecmaToXMLString(XmlProcessor processor) {
/* 551 */     if (isElementType()) {
/* 552 */       Element copy = (Element)this.dom.cloneNode(true);
/* 553 */       Namespace[] inScope = getInScopeNamespaces();
/* 554 */       for (int i = 0; i < inScope.length; i++) {
/* 555 */         declareNamespace(copy, inScope[i].getPrefix(), inScope[i].getUri());
/*     */       }
/* 557 */       return processor.ecmaToXmlString(copy);
/*     */     } 
/* 559 */     return processor.ecmaToXmlString(this.dom);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class Namespace
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 4073904386884677090L;
/*     */ 
/*     */     
/*     */     static Namespace create(String prefix, String uri) {
/* 571 */       if (prefix == null) {
/* 572 */         throw new IllegalArgumentException("Empty string represents default namespace prefix");
/*     */       }
/*     */       
/* 575 */       if (uri == null) {
/* 576 */         throw new IllegalArgumentException("Namespace may not lack a URI");
/*     */       }
/*     */       
/* 579 */       Namespace rv = new Namespace();
/* 580 */       rv.prefix = prefix;
/* 581 */       rv.uri = uri;
/* 582 */       return rv;
/*     */     }
/*     */     
/*     */     static Namespace create(String uri) {
/* 586 */       Namespace rv = new Namespace();
/* 587 */       rv.uri = uri;
/*     */ 
/*     */       
/* 590 */       if (uri == null || uri.length() == 0) {
/* 591 */         rv.prefix = "";
/*     */       }
/*     */       
/* 594 */       return rv;
/*     */     }
/*     */     
/* 597 */     static final Namespace GLOBAL = create("", "");
/*     */ 
/*     */     
/*     */     private String prefix;
/*     */ 
/*     */     
/*     */     private String uri;
/*     */ 
/*     */     
/*     */     public String toString() {
/* 607 */       if (this.prefix == null) return "XmlNode.Namespace [" + this.uri + "]"; 
/* 608 */       return "XmlNode.Namespace [" + this.prefix + "{" + this.uri + "}]";
/*     */     }
/*     */     
/*     */     boolean isUnspecifiedPrefix() {
/* 612 */       return (this.prefix == null);
/*     */     }
/*     */     
/*     */     boolean is(Namespace other) {
/* 616 */       return (this.prefix != null && other.prefix != null && this.prefix.equals(other.prefix) && this.uri.equals(other.uri));
/*     */     }
/*     */     
/*     */     boolean isEmpty() {
/* 620 */       return (this.prefix != null && this.prefix.equals("") && this.uri.equals(""));
/*     */     }
/*     */     
/*     */     boolean isDefault() {
/* 624 */       return (this.prefix != null && this.prefix.equals(""));
/*     */     }
/*     */     
/*     */     boolean isGlobal() {
/* 628 */       return (this.uri != null && this.uri.equals(""));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void setPrefix(String prefix) {
/* 634 */       if (prefix == null) throw new IllegalArgumentException(); 
/* 635 */       this.prefix = prefix;
/*     */     }
/*     */     
/*     */     String getPrefix() {
/* 639 */       return this.prefix;
/*     */     }
/*     */     
/*     */     String getUri() {
/* 643 */       return this.uri;
/*     */     }
/*     */   }
/*     */   
/*     */   static class QName
/*     */     implements Serializable {
/*     */     private static final long serialVersionUID = -6587069811691451077L;
/*     */     private XmlNode.Namespace namespace;
/*     */     private String localName;
/*     */     
/*     */     static QName create(XmlNode.Namespace namespace, String localName) {
/* 654 */       if (localName != null && localName.equals("*")) throw new RuntimeException("* is not valid localName"); 
/* 655 */       QName rv = new QName();
/* 656 */       rv.namespace = namespace;
/* 657 */       rv.localName = localName;
/* 658 */       return rv;
/*     */     }
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     static QName create(String uri, String localName, String prefix) {
/* 664 */       return create(XmlNode.Namespace.create(prefix, uri), localName);
/*     */     }
/*     */     
/*     */     static String qualify(String prefix, String localName) {
/* 668 */       if (prefix == null) throw new IllegalArgumentException("prefix must not be null"); 
/* 669 */       if (prefix.length() > 0) return prefix + ":" + localName; 
/* 670 */       return localName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 681 */       return "XmlNode.QName [" + this.localName + "," + this.namespace + "]";
/*     */     }
/*     */     
/*     */     private boolean equals(String one, String two) {
/* 685 */       if (one == null && two == null) return true; 
/* 686 */       if (one == null || two == null) return false; 
/* 687 */       return one.equals(two);
/*     */     }
/*     */     
/*     */     private boolean namespacesEqual(XmlNode.Namespace one, XmlNode.Namespace two) {
/* 691 */       if (one == null && two == null) return true; 
/* 692 */       if (one == null || two == null) return false; 
/* 693 */       return equals(one.getUri(), two.getUri());
/*     */     }
/*     */     
/*     */     final boolean equals(QName other) {
/* 697 */       if (!namespacesEqual(this.namespace, other.namespace)) return false; 
/* 698 */       if (!equals(this.localName, other.localName)) return false; 
/* 699 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 704 */       if (!(obj instanceof QName)) {
/* 705 */         return false;
/*     */       }
/* 707 */       return equals((QName)obj);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 712 */       return (this.localName == null) ? 0 : this.localName.hashCode();
/*     */     }
/*     */     
/*     */     void lookupPrefix(Node node) {
/* 716 */       if (node == null) throw new IllegalArgumentException("node must not be null"); 
/* 717 */       String prefix = node.lookupPrefix(this.namespace.getUri());
/* 718 */       if (prefix == null) {
/*     */         
/* 720 */         String defaultNamespace = node.lookupNamespaceURI(null);
/* 721 */         if (defaultNamespace == null) defaultNamespace = ""; 
/* 722 */         String nodeNamespace = this.namespace.getUri();
/* 723 */         if (nodeNamespace.equals(defaultNamespace)) {
/* 724 */           prefix = "";
/*     */         }
/*     */       } 
/* 727 */       int i = 0;
/* 728 */       while (prefix == null) {
/* 729 */         String generatedPrefix = "e4x_" + i++;
/* 730 */         String generatedUri = node.lookupNamespaceURI(generatedPrefix);
/* 731 */         if (generatedUri == null) {
/* 732 */           prefix = generatedPrefix;
/* 733 */           Node top = node;
/* 734 */           while (top.getParentNode() != null && top.getParentNode() instanceof Element) {
/* 735 */             top = top.getParentNode();
/*     */           }
/* 737 */           ((Element)top).setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, this.namespace.getUri());
/*     */         } 
/*     */       } 
/* 740 */       this.namespace.setPrefix(prefix);
/*     */     }
/*     */     
/*     */     String qualify(Node node) {
/* 744 */       if (this.namespace.getPrefix() == null) {
/* 745 */         if (node != null) {
/* 746 */           lookupPrefix(node);
/*     */         }
/* 748 */         else if (this.namespace.getUri().equals("")) {
/* 749 */           this.namespace.setPrefix("");
/*     */         }
/*     */         else {
/*     */           
/* 753 */           this.namespace.setPrefix("");
/*     */         } 
/*     */       }
/*     */       
/* 757 */       return qualify(this.namespace.getPrefix(), this.localName);
/*     */     }
/*     */     
/*     */     void setAttribute(Element element, String value) {
/* 761 */       if (this.namespace.getPrefix() == null) lookupPrefix(element); 
/* 762 */       element.setAttributeNS(this.namespace.getUri(), qualify(this.namespace.getPrefix(), this.localName), value);
/*     */     }
/*     */     
/*     */     XmlNode.Namespace getNamespace() {
/* 766 */       return this.namespace;
/*     */     }
/*     */     
/*     */     String getLocalName() {
/* 770 */       return this.localName;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class InternalList
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -3633151157292048978L;
/* 779 */     private List<XmlNode> list = new ArrayList<XmlNode>();
/*     */ 
/*     */     
/*     */     private void _add(XmlNode n) {
/* 783 */       this.list.add(n);
/*     */     }
/*     */     
/*     */     XmlNode item(int index) {
/* 787 */       return this.list.get(index);
/*     */     }
/*     */     
/*     */     void remove(int index) {
/* 791 */       this.list.remove(index);
/*     */     }
/*     */     
/*     */     void add(InternalList other) {
/* 795 */       for (int i = 0; i < other.length(); i++) {
/* 796 */         _add(other.item(i));
/*     */       }
/*     */     }
/*     */     
/*     */     void add(InternalList from, int startInclusive, int endExclusive) {
/* 801 */       for (int i = startInclusive; i < endExclusive; i++) {
/* 802 */         _add(from.item(i));
/*     */       }
/*     */     }
/*     */     
/*     */     void add(XmlNode node) {
/* 807 */       _add(node);
/*     */     }
/*     */ 
/*     */     
/*     */     void add(XML xml) {
/* 812 */       _add(xml.getAnnotation());
/*     */     }
/*     */ 
/*     */     
/*     */     void addToList(Object toAdd) {
/* 817 */       if (toAdd instanceof org.mozilla.javascript.Undefined) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 822 */       if (toAdd instanceof XMLList) {
/* 823 */         XMLList xmlSrc = (XMLList)toAdd;
/* 824 */         for (int i = 0; i < xmlSrc.length(); i++) {
/* 825 */           _add(xmlSrc.item(i).getAnnotation());
/*     */         }
/* 827 */       } else if (toAdd instanceof XML) {
/* 828 */         _add(((XML)toAdd).getAnnotation());
/* 829 */       } else if (toAdd instanceof XmlNode) {
/* 830 */         _add((XmlNode)toAdd);
/*     */       } 
/*     */     }
/*     */     
/*     */     int length() {
/* 835 */       return this.list.size();
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class Filter {
/* 840 */     static final Filter COMMENT = new Filter()
/*     */       {
/*     */         boolean accept(Node node) {
/* 843 */           return (node.getNodeType() == 8);
/*     */         }
/*     */       };
/* 846 */     static final Filter TEXT = new Filter()
/*     */       {
/*     */         boolean accept(Node node) {
/* 849 */           return (node.getNodeType() == 3);
/*     */         }
/*     */       };
/*     */     static Filter PROCESSING_INSTRUCTION(final XMLName name) {
/* 853 */       return new Filter()
/*     */         {
/*     */           boolean accept(Node node) {
/* 856 */             if (node.getNodeType() == 7) {
/* 857 */               ProcessingInstruction pi = (ProcessingInstruction)node;
/* 858 */               return name.matchesLocalName(pi.getTarget());
/*     */             } 
/* 860 */             return false;
/*     */           }
/*     */         };
/*     */     }
/* 864 */     static Filter ELEMENT = new Filter()
/*     */       {
/*     */         boolean accept(Node node) {
/* 867 */           return (node.getNodeType() == 1);
/*     */         }
/*     */       };
/* 870 */     static Filter TRUE = new Filter()
/*     */       {
/*     */         boolean accept(Node node) {
/* 873 */           return true;
/*     */         }
/*     */       };
/*     */     
/*     */     abstract boolean accept(Node param1Node);
/*     */   }
/*     */   
/*     */   Node toDomNode() {
/* 881 */     return this.dom;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\XmlNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */