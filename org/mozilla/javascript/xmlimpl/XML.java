/*     */ package org.mozilla.javascript.xmlimpl;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ import org.mozilla.javascript.xml.XMLObject;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ 
/*     */ class XML
/*     */   extends XMLObjectImpl
/*     */ {
/*     */   static final long serialVersionUID = -630969919086449092L;
/*     */   private XmlNode node;
/*     */   
/*     */   XML(XMLLibImpl lib, Scriptable scope, XMLObject prototype, XmlNode node) {
/*  18 */     super(lib, scope, prototype);
/*  19 */     initialize(node);
/*     */   }
/*     */   
/*     */   void initialize(XmlNode node) {
/*  23 */     this.node = node;
/*  24 */     this.node.setXml(this);
/*     */   }
/*     */ 
/*     */   
/*     */   final XML getXML() {
/*  29 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void replaceWith(XML value) {
/*  36 */     if (this.node.parent() == null) {
/*     */ 
/*     */       
/*  39 */       initialize(value.node);
/*     */       return;
/*     */     } 
/*     */     this.node.replaceWith(value.node);
/*     */   }
/*     */   XML makeXmlFromString(XMLName name, String value) {
/*     */     try {
/*  46 */       return newTextElementXML(this.node, name.toQname(), value);
/*  47 */     } catch (Exception e) {
/*  48 */       throw ScriptRuntime.typeError(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   XmlNode getAnnotation() {
/*  54 */     return this.node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(int index, Scriptable start) {
/*  65 */     if (index == 0) {
/*  66 */       return this;
/*     */     }
/*  68 */     return Scriptable.NOT_FOUND;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean has(int index, Scriptable start) {
/*  74 */     return (index == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(int index, Scriptable start, Object value) {
/*  81 */     throw ScriptRuntime.typeError("Assignment to indexed XML is not allowed");
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getIds() {
/*  86 */     if (isPrototype()) {
/*  87 */       return new Object[0];
/*     */     }
/*  89 */     return new Object[] { Integer.valueOf(0) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(int index) {
/*  96 */     if (index == 0) {
/*  97 */       remove();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasXMLProperty(XMLName xmlName) {
/* 107 */     return (getPropertyList(xmlName).length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   Object getXMLProperty(XMLName xmlName) {
/* 112 */     return getPropertyList(xmlName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XmlNode.QName getNodeQname() {
/* 122 */     return this.node.getQname();
/*     */   }
/*     */   
/*     */   XML[] getChildren() {
/* 126 */     if (!isElement()) return null; 
/* 127 */     XmlNode[] children = this.node.getMatchingChildren(XmlNode.Filter.TRUE);
/* 128 */     XML[] rv = new XML[children.length];
/* 129 */     for (int i = 0; i < rv.length; i++) {
/* 130 */       rv[i] = toXML(children[i]);
/*     */     }
/* 132 */     return rv;
/*     */   }
/*     */   
/*     */   XML[] getAttributes() {
/* 136 */     XmlNode[] attributes = this.node.getAttributes();
/* 137 */     XML[] rv = new XML[attributes.length];
/* 138 */     for (int i = 0; i < rv.length; i++) {
/* 139 */       rv[i] = toXML(attributes[i]);
/*     */     }
/* 141 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList getPropertyList(XMLName name) {
/* 146 */     return name.getMyValueOn(this);
/*     */   }
/*     */ 
/*     */   
/*     */   void deleteXMLProperty(XMLName name) {
/* 151 */     XMLList list = getPropertyList(name);
/* 152 */     for (int i = 0; i < list.length(); i++) {
/* 153 */       (list.item(i)).node.deleteMe();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void putXMLProperty(XMLName xmlName, Object value) {
/* 159 */     if (!isPrototype())
/*     */     {
/*     */       
/* 162 */       xmlName.setMyValueOn(this, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasOwnProperty(XMLName xmlName) {
/* 168 */     boolean hasProperty = false;
/*     */     
/* 170 */     if (isPrototype()) {
/* 171 */       String property = xmlName.localName();
/* 172 */       hasProperty = (0 != findPrototypeId(property));
/*     */     } else {
/* 174 */       hasProperty = (getPropertyList(xmlName).length() > 0);
/*     */     } 
/*     */     
/* 177 */     return hasProperty;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object jsConstructor(Context cx, boolean inNewExpr, Object[] args) {
/* 182 */     if (args.length == 0 || args[0] == null || args[0] == Undefined.instance) {
/* 183 */       args = new Object[] { "" };
/*     */     }
/*     */     
/* 186 */     XML toXml = ecmaToXml(args[0]);
/* 187 */     if (inNewExpr) {
/* 188 */       return toXml.copy();
/*     */     }
/* 190 */     return toXml;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable getExtraMethodSource(Context cx) {
/* 197 */     if (hasSimpleContent()) {
/* 198 */       String src = toString();
/* 199 */       return ScriptRuntime.toObjectOrNull(cx, src);
/*     */     } 
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeChild(int index) {
/* 209 */     this.node.removeChild(index);
/*     */   }
/*     */ 
/*     */   
/*     */   void normalize() {
/* 214 */     this.node.normalize();
/*     */   }
/*     */   
/*     */   private XML toXML(XmlNode node) {
/* 218 */     if (node.getXml() == null) {
/* 219 */       node.setXml(newXML(node));
/*     */     }
/* 221 */     return node.getXml();
/*     */   }
/*     */   
/*     */   void setAttribute(XMLName xmlName, Object value) {
/* 225 */     if (!isElement()) throw new IllegalStateException("Can only set attributes on elements.");
/*     */     
/* 227 */     if (xmlName.uri() == null && xmlName.localName().equals("*")) {
/* 228 */       throw ScriptRuntime.typeError("@* assignment not supported.");
/*     */     }
/* 230 */     this.node.setAttribute(xmlName.toQname(), ScriptRuntime.toString(value));
/*     */   }
/*     */   
/*     */   void remove() {
/* 234 */     this.node.deleteMe();
/*     */   }
/*     */ 
/*     */   
/*     */   void addMatches(XMLList rv, XMLName name) {
/* 239 */     name.addMatches(rv, this);
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList elements(XMLName name) {
/* 244 */     XMLList rv = newXMLList();
/* 245 */     rv.setTargets(this, name.toQname());
/*     */     
/* 247 */     XmlNode[] elements = this.node.getMatchingChildren(XmlNode.Filter.ELEMENT);
/* 248 */     for (int i = 0; i < elements.length; i++) {
/* 249 */       if (name.matches(toXML(elements[i]))) {
/* 250 */         rv.addToList(toXML(elements[i]));
/*     */       }
/*     */     } 
/* 253 */     return rv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XMLList child(XMLName xmlName) {
/* 260 */     XMLList rv = newXMLList();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     XmlNode[] elements = this.node.getMatchingChildren(XmlNode.Filter.ELEMENT);
/* 266 */     for (int i = 0; i < elements.length; i++) {
/* 267 */       if (xmlName.matchesElement(elements[i].getQname())) {
/* 268 */         rv.addToList(toXML(elements[i]));
/*     */       }
/*     */     } 
/* 271 */     rv.setTargets(this, xmlName.toQname());
/* 272 */     return rv;
/*     */   }
/*     */   
/*     */   XML replace(XMLName xmlName, Object xml) {
/* 276 */     putXMLProperty(xmlName, xml);
/* 277 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList children() {
/* 282 */     XMLList rv = newXMLList();
/* 283 */     XMLName all = XMLName.formStar();
/* 284 */     rv.setTargets(this, all.toQname());
/* 285 */     XmlNode[] children = this.node.getMatchingChildren(XmlNode.Filter.TRUE);
/* 286 */     for (int i = 0; i < children.length; i++) {
/* 287 */       rv.addToList(toXML(children[i]));
/*     */     }
/* 289 */     return rv;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   XMLList child(int index) {
/* 295 */     XMLList result = newXMLList();
/* 296 */     result.setTargets(this, null);
/* 297 */     if (index >= 0 && index < this.node.getChildCount()) {
/* 298 */       result.addToList(getXmlChild(index));
/*     */     }
/* 300 */     return result;
/*     */   }
/*     */   
/*     */   XML getXmlChild(int index) {
/* 304 */     XmlNode child = this.node.getChild(index);
/* 305 */     if (child.getXml() == null) {
/* 306 */       child.setXml(newXML(child));
/*     */     }
/* 308 */     return child.getXml();
/*     */   }
/*     */ 
/*     */   
/*     */   XML getLastXmlChild() {
/* 313 */     int pos = this.node.getChildCount() - 1;
/* 314 */     if (pos < 0) return null; 
/* 315 */     return getXmlChild(pos);
/*     */   }
/*     */   
/*     */   int childIndex() {
/* 319 */     return this.node.getChildIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean contains(Object xml) {
/* 324 */     if (xml instanceof XML) {
/* 325 */       return equivalentXml(xml);
/*     */     }
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean equivalentXml(Object target) {
/* 334 */     boolean result = false;
/*     */     
/* 336 */     if (target instanceof XML)
/*     */     {
/* 338 */       return this.node.toXmlString(getProcessor()).equals(((XML)target).node.toXmlString(getProcessor())); } 
/* 339 */     if (target instanceof XMLList) {
/*     */       
/* 341 */       XMLList otherList = (XMLList)target;
/*     */       
/* 343 */       if (otherList.length() == 1) {
/* 344 */         result = equivalentXml(otherList.getXML());
/*     */       }
/* 346 */     } else if (hasSimpleContent()) {
/* 347 */       String otherStr = ScriptRuntime.toString(target);
/*     */       
/* 349 */       result = toString().equals(otherStr);
/*     */     } 
/*     */     
/* 352 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLObjectImpl copy() {
/* 357 */     return newXML(this.node.copy());
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasSimpleContent() {
/* 362 */     if (isComment() || isProcessingInstruction()) return false; 
/* 363 */     if (isText() || this.node.isAttributeType()) return true; 
/* 364 */     return !this.node.hasChildElement();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasComplexContent() {
/* 369 */     return !hasSimpleContent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int length() {
/* 376 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean is(XML other) {
/* 381 */     return this.node.isSameNode(other.node);
/*     */   }
/*     */   
/*     */   Object nodeKind() {
/* 385 */     return ecmaClass();
/*     */   }
/*     */ 
/*     */   
/*     */   Object parent() {
/* 390 */     XmlNode parent = this.node.parent();
/* 391 */     if (parent == null) return null; 
/* 392 */     return newXML(this.node.parent());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean propertyIsEnumerable(Object name) {
/*     */     boolean result;
/* 399 */     if (name instanceof Integer) {
/* 400 */       result = (((Integer)name).intValue() == 0);
/* 401 */     } else if (name instanceof Number) {
/* 402 */       double x = ((Number)name).doubleValue();
/*     */       
/* 404 */       result = (x == 0.0D && 1.0D / x > 0.0D);
/*     */     } else {
/* 406 */       result = ScriptRuntime.toString(name).equals("0");
/*     */     } 
/* 408 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   Object valueOf() {
/* 413 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XMLList comments() {
/* 422 */     XMLList rv = newXMLList();
/* 423 */     this.node.addMatchingChildren(rv, XmlNode.Filter.COMMENT);
/* 424 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList text() {
/* 429 */     XMLList rv = newXMLList();
/* 430 */     this.node.addMatchingChildren(rv, XmlNode.Filter.TEXT);
/* 431 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList processingInstructions(XMLName xmlName) {
/* 436 */     XMLList rv = newXMLList();
/* 437 */     this.node.addMatchingChildren(rv, XmlNode.Filter.PROCESSING_INSTRUCTION(xmlName));
/* 438 */     return rv;
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
/*     */   
/*     */   private XmlNode[] getNodesForInsert(Object value) {
/* 452 */     if (value instanceof XML)
/* 453 */       return new XmlNode[] { ((XML)value).node }; 
/* 454 */     if (value instanceof XMLList) {
/* 455 */       XMLList list = (XMLList)value;
/* 456 */       XmlNode[] rv = new XmlNode[list.length()];
/* 457 */       for (int i = 0; i < list.length(); i++) {
/* 458 */         rv[i] = (list.item(i)).node;
/*     */       }
/* 460 */       return rv;
/*     */     } 
/* 462 */     return new XmlNode[] { XmlNode.createText(getProcessor(), ScriptRuntime.toString(value)) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XML replace(int index, Object xml) {
/* 469 */     XMLList xlChildToReplace = child(index);
/* 470 */     if (xlChildToReplace.length() > 0) {
/*     */       
/* 472 */       XML childToReplace = xlChildToReplace.item(0);
/* 473 */       insertChildAfter(childToReplace, xml);
/* 474 */       removeChild(index);
/*     */     } 
/* 476 */     return this;
/*     */   }
/*     */   
/*     */   XML prependChild(Object xml) {
/* 480 */     if (this.node.isParentType()) {
/* 481 */       this.node.insertChildrenAt(0, getNodesForInsert(xml));
/*     */     }
/* 483 */     return this;
/*     */   }
/*     */   
/*     */   XML appendChild(Object xml) {
/* 487 */     if (this.node.isParentType()) {
/* 488 */       XmlNode[] nodes = getNodesForInsert(xml);
/* 489 */       this.node.insertChildrenAt(this.node.getChildCount(), nodes);
/*     */     } 
/* 491 */     return this;
/*     */   }
/*     */   
/*     */   private int getChildIndexOf(XML child) {
/* 495 */     for (int i = 0; i < this.node.getChildCount(); i++) {
/* 496 */       if (this.node.getChild(i).isSameNode(child.node)) {
/* 497 */         return i;
/*     */       }
/*     */     } 
/* 500 */     return -1;
/*     */   }
/*     */   
/*     */   XML insertChildBefore(XML child, Object xml) {
/* 504 */     if (child == null) {
/*     */       
/* 506 */       appendChild(xml);
/*     */     } else {
/* 508 */       XmlNode[] toInsert = getNodesForInsert(xml);
/* 509 */       int index = getChildIndexOf(child);
/* 510 */       if (index != -1) {
/* 511 */         this.node.insertChildrenAt(index, toInsert);
/*     */       }
/*     */     } 
/*     */     
/* 515 */     return this;
/*     */   }
/*     */   
/*     */   XML insertChildAfter(XML child, Object xml) {
/* 519 */     if (child == null) {
/*     */       
/* 521 */       prependChild(xml);
/*     */     } else {
/* 523 */       XmlNode[] toInsert = getNodesForInsert(xml);
/* 524 */       int index = getChildIndexOf(child);
/* 525 */       if (index != -1) {
/* 526 */         this.node.insertChildrenAt(index + 1, toInsert);
/*     */       }
/*     */     } 
/*     */     
/* 530 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   XML setChildren(Object xml) {
/* 535 */     if (!isElement()) return this;
/*     */     
/* 537 */     while (this.node.getChildCount() > 0) {
/* 538 */       this.node.removeChild(0);
/*     */     }
/* 540 */     XmlNode[] toInsert = getNodesForInsert(xml);
/*     */     
/* 542 */     this.node.insertChildrenAt(0, toInsert);
/*     */     
/* 544 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addInScopeNamespace(Namespace ns) {
/* 552 */     if (!isElement()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 557 */     if (ns.prefix() != null) {
/* 558 */       if (ns.prefix().length() == 0 && ns.uri().length() == 0) {
/*     */         return;
/*     */       }
/* 561 */       if (this.node.getQname().getNamespace().getPrefix().equals(ns.prefix())) {
/* 562 */         this.node.invalidateNamespacePrefix();
/*     */       }
/* 564 */       this.node.declareNamespace(ns.prefix(), ns.uri());
/*     */     } else {
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   Namespace[] inScopeNamespaces() {
/* 571 */     XmlNode.Namespace[] inScope = this.node.getInScopeNamespaces();
/* 572 */     return createNamespaces(inScope);
/*     */   }
/*     */   
/*     */   private XmlNode.Namespace adapt(Namespace ns) {
/* 576 */     if (ns.prefix() == null) {
/* 577 */       return XmlNode.Namespace.create(ns.uri());
/*     */     }
/* 579 */     return XmlNode.Namespace.create(ns.prefix(), ns.uri());
/*     */   }
/*     */ 
/*     */   
/*     */   XML removeNamespace(Namespace ns) {
/* 584 */     if (!isElement()) return this; 
/* 585 */     this.node.removeNamespace(adapt(ns));
/* 586 */     return this;
/*     */   }
/*     */   
/*     */   XML addNamespace(Namespace ns) {
/* 590 */     addInScopeNamespace(ns);
/* 591 */     return this;
/*     */   }
/*     */   
/*     */   QName name() {
/* 595 */     if (isText() || isComment()) return null; 
/* 596 */     if (isProcessingInstruction()) return newQName("", this.node.getQname().getLocalName(), null); 
/* 597 */     return newQName(this.node.getQname());
/*     */   }
/*     */   
/*     */   Namespace[] namespaceDeclarations() {
/* 601 */     XmlNode.Namespace[] declarations = this.node.getNamespaceDeclarations();
/* 602 */     return createNamespaces(declarations);
/*     */   }
/*     */   
/*     */   Namespace namespace(String prefix) {
/* 606 */     if (prefix == null) {
/* 607 */       return createNamespace(this.node.getNamespaceDeclaration());
/*     */     }
/* 609 */     return createNamespace(this.node.getNamespaceDeclaration(prefix));
/*     */   }
/*     */ 
/*     */   
/*     */   String localName() {
/* 614 */     if (name() == null) return null; 
/* 615 */     return name().localName();
/*     */   }
/*     */ 
/*     */   
/*     */   void setLocalName(String localName) {
/* 620 */     if (isText() || isComment())
/* 621 */       return;  this.node.setLocalName(localName);
/*     */   }
/*     */ 
/*     */   
/*     */   void setName(QName name) {
/* 626 */     if (isText() || isComment())
/* 627 */       return;  if (isProcessingInstruction()) {
/*     */ 
/*     */       
/* 630 */       this.node.setLocalName(name.localName());
/*     */       return;
/*     */     } 
/* 633 */     this.node.renameNode(name.getDelegate());
/*     */   }
/*     */ 
/*     */   
/*     */   void setNamespace(Namespace ns) {
/* 638 */     if (isText() || isComment() || isProcessingInstruction())
/* 639 */       return;  setName(newQName(ns.uri(), localName(), ns.prefix()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final String ecmaClass() {
/* 647 */     if (this.node.isTextType())
/* 648 */       return "text"; 
/* 649 */     if (this.node.isAttributeType())
/* 650 */       return "attribute"; 
/* 651 */     if (this.node.isCommentType())
/* 652 */       return "comment"; 
/* 653 */     if (this.node.isProcessingInstructionType())
/* 654 */       return "processing-instruction"; 
/* 655 */     if (this.node.isElementType()) {
/* 656 */       return "element";
/*     */     }
/* 658 */     throw new RuntimeException("Unrecognized type: " + this.node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 667 */     return "XML";
/*     */   }
/*     */   
/*     */   private String ecmaValue() {
/* 671 */     return this.node.ecmaValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private String ecmaToString() {
/* 676 */     if (isAttribute() || isText()) {
/* 677 */       return ecmaValue();
/*     */     }
/* 679 */     if (hasSimpleContent()) {
/* 680 */       StringBuilder rv = new StringBuilder();
/* 681 */       for (int i = 0; i < this.node.getChildCount(); i++) {
/* 682 */         XmlNode child = this.node.getChild(i);
/* 683 */         if (!child.isProcessingInstructionType() && !child.isCommentType()) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 688 */           XML x = new XML(getLib(), getParentScope(), (XMLObject)getPrototype(), child);
/*     */           
/* 690 */           rv.append(x.toString());
/*     */         } 
/*     */       } 
/* 693 */       return rv.toString();
/*     */     } 
/* 695 */     return toXMLString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 700 */     return ecmaToString();
/*     */   }
/*     */ 
/*     */   
/*     */   String toSource(int indent) {
/* 705 */     return toXMLString();
/*     */   }
/*     */ 
/*     */   
/*     */   String toXMLString() {
/* 710 */     return this.node.ecmaToXMLString(getProcessor());
/*     */   }
/*     */   
/*     */   final boolean isAttribute() {
/* 714 */     return this.node.isAttributeType();
/*     */   }
/*     */   
/*     */   final boolean isComment() {
/* 718 */     return this.node.isCommentType();
/*     */   }
/*     */   
/*     */   final boolean isText() {
/* 722 */     return this.node.isTextType();
/*     */   }
/*     */   
/*     */   final boolean isElement() {
/* 726 */     return this.node.isElementType();
/*     */   }
/*     */   
/*     */   final boolean isProcessingInstruction() {
/* 730 */     return this.node.isProcessingInstructionType();
/*     */   }
/*     */ 
/*     */   
/*     */   Node toDomNode() {
/* 735 */     return this.node.toDomNode();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\XML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */