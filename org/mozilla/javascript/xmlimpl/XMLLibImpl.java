/*     */ package org.mozilla.javascript.xmlimpl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.Ref;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ import org.mozilla.javascript.Wrapper;
/*     */ import org.mozilla.javascript.xml.XMLLib;
/*     */ import org.mozilla.javascript.xml.XMLObject;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class XMLLibImpl
/*     */   extends XMLLib
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private Scriptable globalScope;
/*     */   private XML xmlPrototype;
/*     */   private XMLList xmlListPrototype;
/*     */   private Namespace namespacePrototype;
/*     */   private QName qnamePrototype;
/*     */   
/*     */   public static Node toDomNode(Object xmlObject) {
/*  28 */     if (xmlObject instanceof XML) {
/*  29 */       return ((XML)xmlObject).toDomNode();
/*     */     }
/*  31 */     throw new IllegalArgumentException("xmlObject is not an XML object in JavaScript.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  37 */     XMLLibImpl lib = new XMLLibImpl(scope);
/*  38 */     XMLLib bound = lib.bindToScope(scope);
/*  39 */     if (bound == lib) {
/*  40 */       lib.exportToScope(sealed);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIgnoreComments(boolean b) {
/*  46 */     this.options.setIgnoreComments(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIgnoreWhitespace(boolean b) {
/*  51 */     this.options.setIgnoreWhitespace(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIgnoreProcessingInstructions(boolean b) {
/*  56 */     this.options.setIgnoreProcessingInstructions(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPrettyPrinting(boolean b) {
/*  61 */     this.options.setPrettyPrinting(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPrettyIndent(int i) {
/*  66 */     this.options.setPrettyIndent(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnoreComments() {
/*  71 */     return this.options.isIgnoreComments();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnoreProcessingInstructions() {
/*  76 */     return this.options.isIgnoreProcessingInstructions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isIgnoreWhitespace() {
/*  81 */     return this.options.isIgnoreWhitespace();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrettyPrinting() {
/*  86 */     return this.options.isPrettyPrinting();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPrettyIndent() {
/*  91 */     return this.options.getPrettyIndent();
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
/* 102 */   private XmlProcessor options = new XmlProcessor();
/*     */   
/*     */   private XMLLibImpl(Scriptable globalScope) {
/* 105 */     this.globalScope = globalScope;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   QName qnamePrototype() {
/* 111 */     return this.qnamePrototype;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   Scriptable globalScope() {
/* 117 */     return this.globalScope;
/*     */   }
/*     */   
/*     */   XmlProcessor getProcessor() {
/* 121 */     return this.options;
/*     */   }
/*     */   
/*     */   private void exportToScope(boolean sealed) {
/* 125 */     this.xmlPrototype = newXML(XmlNode.createText(this.options, ""));
/* 126 */     this.xmlListPrototype = newXMLList();
/* 127 */     this.namespacePrototype = Namespace.create(this.globalScope, null, XmlNode.Namespace.GLOBAL);
/*     */     
/* 129 */     this.qnamePrototype = QName.create(this, this.globalScope, null, XmlNode.QName.create(XmlNode.Namespace.create(""), ""));
/*     */ 
/*     */     
/* 132 */     this.xmlPrototype.exportAsJSClass(sealed);
/* 133 */     this.xmlListPrototype.exportAsJSClass(sealed);
/* 134 */     this.namespacePrototype.exportAsJSClass(sealed);
/* 135 */     this.qnamePrototype.exportAsJSClass(sealed);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   XMLName toAttributeName(Context cx, Object nameValue) {
/* 141 */     if (nameValue instanceof XMLName)
/*     */     {
/* 143 */       return (XMLName)nameValue; } 
/* 144 */     if (nameValue instanceof QName)
/* 145 */       return XMLName.create(((QName)nameValue).getDelegate(), true, false); 
/* 146 */     if (nameValue instanceof Boolean || nameValue instanceof Number || nameValue == Undefined.instance || nameValue == null)
/*     */     {
/*     */ 
/*     */       
/* 150 */       throw badXMLName(nameValue);
/*     */     }
/*     */     
/* 153 */     String localName = null;
/* 154 */     if (nameValue instanceof String) {
/* 155 */       localName = (String)nameValue;
/*     */     } else {
/* 157 */       localName = ScriptRuntime.toString(nameValue);
/*     */     } 
/* 159 */     if (localName != null && localName.equals("*")) localName = null; 
/* 160 */     return XMLName.create(XmlNode.QName.create(XmlNode.Namespace.create(""), localName), true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RuntimeException badXMLName(Object value) {
/*     */     String msg;
/* 168 */     if (value instanceof Number) {
/* 169 */       msg = "Can not construct XML name from number: ";
/* 170 */     } else if (value instanceof Boolean) {
/* 171 */       msg = "Can not construct XML name from boolean: ";
/* 172 */     } else if (value == Undefined.instance || value == null) {
/* 173 */       msg = "Can not construct XML name from ";
/*     */     } else {
/* 175 */       throw new IllegalArgumentException(value.toString());
/*     */     } 
/* 177 */     return (RuntimeException)ScriptRuntime.typeError(msg + ScriptRuntime.toString(value));
/*     */   }
/*     */   
/*     */   XMLName toXMLNameFromString(Context cx, String name) {
/* 181 */     return XMLName.create(getDefaultNamespaceURI(cx), name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   XMLName toXMLName(Context cx, Object nameValue) {
/*     */     XMLName result;
/* 188 */     if (nameValue instanceof XMLName)
/* 189 */     { result = (XMLName)nameValue; }
/* 190 */     else if (nameValue instanceof QName)
/* 191 */     { QName qname = (QName)nameValue;
/* 192 */       result = XMLName.formProperty(qname.uri(), qname.localName()); }
/* 193 */     else if (nameValue instanceof String)
/* 194 */     { result = toXMLNameFromString(cx, (String)nameValue); }
/* 195 */     else { if (nameValue instanceof Boolean || nameValue instanceof Number || nameValue == Undefined.instance || nameValue == null)
/*     */       {
/*     */ 
/*     */         
/* 199 */         throw badXMLName(nameValue);
/*     */       }
/* 201 */       String name = ScriptRuntime.toString(nameValue);
/* 202 */       result = toXMLNameFromString(cx, name); }
/*     */ 
/*     */     
/* 205 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XMLName toXMLNameOrIndex(Context cx, Object value) {
/*     */     XMLName result;
/* 217 */     if (value instanceof XMLName) {
/* 218 */       result = (XMLName)value;
/* 219 */     } else if (value instanceof String) {
/* 220 */       String str = (String)value;
/* 221 */       long test = ScriptRuntime.testUint32String(str);
/* 222 */       if (test >= 0L) {
/* 223 */         ScriptRuntime.storeUint32Result(cx, test);
/* 224 */         result = null;
/*     */       } else {
/* 226 */         result = toXMLNameFromString(cx, str);
/*     */       } 
/* 228 */     } else if (value instanceof Number) {
/* 229 */       double d = ((Number)value).doubleValue();
/* 230 */       long l = (long)d;
/* 231 */       if (l == d && 0L <= l && l <= 4294967295L) {
/* 232 */         ScriptRuntime.storeUint32Result(cx, l);
/* 233 */         result = null;
/*     */       } else {
/* 235 */         throw badXMLName(value);
/*     */       } 
/* 237 */     } else if (value instanceof QName) {
/* 238 */       QName qname = (QName)value;
/* 239 */       String uri = qname.uri();
/* 240 */       boolean number = false;
/* 241 */       result = null;
/* 242 */       if (uri != null && uri.length() == 0) {
/*     */         
/* 244 */         long test = ScriptRuntime.testUint32String(uri);
/* 245 */         if (test >= 0L) {
/* 246 */           ScriptRuntime.storeUint32Result(cx, test);
/* 247 */           number = true;
/*     */         } 
/*     */       } 
/* 250 */       if (!number)
/* 251 */         result = XMLName.formProperty(uri, qname.localName()); 
/*     */     } else {
/* 253 */       if (value instanceof Boolean || value == Undefined.instance || value == null)
/*     */       {
/*     */ 
/*     */         
/* 257 */         throw badXMLName(value);
/*     */       }
/* 259 */       String str = ScriptRuntime.toString(value);
/* 260 */       long test = ScriptRuntime.testUint32String(str);
/* 261 */       if (test >= 0L) {
/* 262 */         ScriptRuntime.storeUint32Result(cx, test);
/* 263 */         result = null;
/*     */       } else {
/* 265 */         result = toXMLNameFromString(cx, str);
/*     */       } 
/*     */     } 
/*     */     
/* 269 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   Object addXMLObjects(Context cx, XMLObject obj1, XMLObject obj2) {
/* 274 */     XMLList listToAdd = newXMLList();
/*     */     
/* 276 */     if (obj1 instanceof XMLList) {
/* 277 */       XMLList list1 = (XMLList)obj1;
/* 278 */       if (list1.length() == 1) {
/* 279 */         listToAdd.addToList(list1.item(0));
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 284 */         listToAdd = newXMLListFrom(obj1);
/*     */       } 
/*     */     } else {
/* 287 */       listToAdd.addToList(obj1);
/*     */     } 
/*     */     
/* 290 */     if (obj2 instanceof XMLList) {
/* 291 */       XMLList list2 = (XMLList)obj2;
/* 292 */       for (int i = 0; i < list2.length(); i++) {
/* 293 */         listToAdd.addToList(list2.item(i));
/*     */       }
/* 295 */     } else if (obj2 instanceof XML) {
/* 296 */       listToAdd.addToList(obj2);
/*     */     } 
/*     */     
/* 299 */     return listToAdd;
/*     */   }
/*     */ 
/*     */   
/*     */   private Ref xmlPrimaryReference(Context cx, XMLName xmlName, Scriptable scope) {
/* 304 */     XMLObjectImpl xmlObj, firstXml = null;
/*     */ 
/*     */     
/*     */     while (true) {
/* 308 */       if (scope instanceof XMLWithScope) {
/* 309 */         xmlObj = (XMLObjectImpl)scope.getPrototype();
/* 310 */         if (xmlObj.hasXMLProperty(xmlName)) {
/*     */           break;
/*     */         }
/* 313 */         if (firstXml == null) {
/* 314 */           firstXml = xmlObj;
/*     */         }
/*     */       } 
/* 317 */       scope = scope.getParentScope();
/* 318 */       if (scope == null) {
/* 319 */         xmlObj = firstXml;
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 326 */     if (xmlObj != null) {
/* 327 */       xmlName.initXMLObject(xmlObj);
/*     */     }
/* 329 */     return xmlName;
/*     */   }
/*     */   
/*     */   Namespace castToNamespace(Context cx, Object namespaceObj) {
/* 333 */     return this.namespacePrototype.castToNamespace(namespaceObj);
/*     */   }
/*     */   
/*     */   private String getDefaultNamespaceURI(Context cx) {
/* 337 */     return getDefaultNamespace(cx).uri();
/*     */   }
/*     */   
/*     */   Namespace newNamespace(String uri) {
/* 341 */     return this.namespacePrototype.newNamespace(uri);
/*     */   }
/*     */   
/*     */   Namespace getDefaultNamespace(Context cx) {
/* 345 */     if (cx == null) {
/* 346 */       cx = Context.getCurrentContext();
/* 347 */       if (cx == null) {
/* 348 */         return this.namespacePrototype;
/*     */       }
/*     */     } 
/*     */     
/* 352 */     Object ns = ScriptRuntime.searchDefaultNamespace(cx);
/* 353 */     if (ns == null) {
/* 354 */       return this.namespacePrototype;
/*     */     }
/* 356 */     if (ns instanceof Namespace) {
/* 357 */       return (Namespace)ns;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 362 */     return this.namespacePrototype;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Namespace[] createNamespaces(XmlNode.Namespace[] declarations) {
/* 368 */     Namespace[] rv = new Namespace[declarations.length];
/* 369 */     for (int i = 0; i < declarations.length; i++) {
/* 370 */       rv[i] = this.namespacePrototype.newNamespace(declarations[i].getPrefix(), declarations[i].getUri());
/*     */     }
/*     */     
/* 373 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   QName constructQName(Context cx, Object namespace, Object name) {
/* 378 */     return this.qnamePrototype.constructQName(this, cx, namespace, name);
/*     */   }
/*     */   
/*     */   QName newQName(String uri, String localName, String prefix) {
/* 382 */     return this.qnamePrototype.newQName(this, uri, localName, prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   QName constructQName(Context cx, Object nameValue) {
/* 387 */     return this.qnamePrototype.constructQName(this, cx, nameValue);
/*     */   }
/*     */   
/*     */   QName castToQName(Context cx, Object qnameValue) {
/* 391 */     return this.qnamePrototype.castToQName(this, cx, qnameValue);
/*     */   }
/*     */   
/*     */   QName newQName(XmlNode.QName qname) {
/* 395 */     return QName.create(this, this.globalScope, this.qnamePrototype, qname);
/*     */   }
/*     */   
/*     */   XML newXML(XmlNode node) {
/* 399 */     return new XML(this, this.globalScope, this.xmlPrototype, node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final XML newXMLFromJs(Object inputObject) {
/*     */     String frag;
/* 407 */     if (inputObject == null || inputObject == Undefined.instance) {
/* 408 */       frag = "";
/* 409 */     } else if (inputObject instanceof XMLObjectImpl) {
/*     */       
/* 411 */       frag = ((XMLObjectImpl)inputObject).toXMLString();
/*     */     } else {
/* 413 */       frag = ScriptRuntime.toString(inputObject);
/*     */     } 
/*     */     
/* 416 */     if (frag.trim().startsWith("<>")) {
/* 417 */       throw ScriptRuntime.typeError("Invalid use of XML object anonymous tags <></>.");
/*     */     }
/*     */     
/* 420 */     if (frag.indexOf("<") == -1)
/*     */     {
/* 422 */       return newXML(XmlNode.createText(this.options, frag));
/*     */     }
/* 424 */     return parse(frag);
/*     */   }
/*     */   
/*     */   private XML parse(String frag) {
/*     */     try {
/* 429 */       return newXML(XmlNode.createElement(this.options, getDefaultNamespaceURI(Context.getCurrentContext()), frag));
/*     */     }
/* 431 */     catch (SAXException e) {
/* 432 */       throw ScriptRuntime.typeError("Cannot parse XML: " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   final XML ecmaToXml(Object object) {
/* 438 */     if (object == null || object == Undefined.instance) {
/* 439 */       throw ScriptRuntime.typeError("Cannot convert " + object + " to XML");
/*     */     }
/* 441 */     if (object instanceof XML) return (XML)object; 
/* 442 */     if (object instanceof XMLList) {
/* 443 */       XMLList list = (XMLList)object;
/* 444 */       if (list.getXML() != null) {
/* 445 */         return list.getXML();
/*     */       }
/* 447 */       throw ScriptRuntime.typeError("Cannot convert list of >1 element to XML");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 454 */     if (object instanceof Wrapper) {
/* 455 */       object = ((Wrapper)object).unwrap();
/*     */     }
/* 457 */     if (object instanceof Node) {
/* 458 */       Node node = (Node)object;
/* 459 */       return newXML(XmlNode.createElementFromNode(node));
/*     */     } 
/*     */     
/* 462 */     String s = ScriptRuntime.toString(object);
/*     */     
/* 464 */     if (s.length() > 0 && s.charAt(0) == '<') {
/* 465 */       return parse(s);
/*     */     }
/* 467 */     return newXML(XmlNode.createText(this.options, s));
/*     */   }
/*     */ 
/*     */   
/*     */   final XML newTextElementXML(XmlNode reference, XmlNode.QName qname, String value) {
/* 472 */     return newXML(XmlNode.newElementWithText(this.options, reference, qname, value));
/*     */   }
/*     */   
/*     */   XMLList newXMLList() {
/* 476 */     return new XMLList(this, this.globalScope, this.xmlListPrototype);
/*     */   }
/*     */   
/*     */   final XMLList newXMLListFrom(Object inputObject) {
/* 480 */     XMLList rv = newXMLList();
/*     */     
/* 482 */     if (inputObject == null || inputObject instanceof Undefined)
/* 483 */       return rv; 
/* 484 */     if (inputObject instanceof XML) {
/* 485 */       XML xml = (XML)inputObject;
/* 486 */       rv.getNodeList().add(xml);
/* 487 */       return rv;
/* 488 */     }  if (inputObject instanceof XMLList) {
/* 489 */       XMLList xmll = (XMLList)inputObject;
/* 490 */       rv.getNodeList().add(xmll.getNodeList());
/* 491 */       return rv;
/*     */     } 
/* 493 */     String frag = ScriptRuntime.toString(inputObject).trim();
/*     */     
/* 495 */     if (!frag.startsWith("<>")) {
/* 496 */       frag = "<>" + frag + "</>";
/*     */     }
/*     */     
/* 499 */     frag = "<fragment>" + frag.substring(2);
/* 500 */     if (!frag.endsWith("</>")) {
/* 501 */       throw ScriptRuntime.typeError("XML with anonymous tag missing end anonymous tag");
/*     */     }
/*     */     
/* 504 */     frag = frag.substring(0, frag.length() - 3) + "</fragment>";
/*     */     
/* 506 */     XML orgXML = newXMLFromJs(frag);
/*     */ 
/*     */     
/* 509 */     XMLList children = orgXML.children();
/*     */     
/* 511 */     for (int i = 0; i < children.getNodeList().length(); i++)
/*     */     {
/* 513 */       rv.getNodeList().add((XML)children.item(i).copy());
/*     */     }
/* 515 */     return rv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XmlNode.QName toNodeQName(Context cx, Object namespaceValue, Object nameValue) {
/*     */     String localName;
/*     */     XmlNode.Namespace ns;
/* 525 */     if (nameValue instanceof QName) {
/* 526 */       QName qname = (QName)nameValue;
/* 527 */       localName = qname.localName();
/*     */     } else {
/* 529 */       localName = ScriptRuntime.toString(nameValue);
/*     */     } 
/*     */ 
/*     */     
/* 533 */     if (namespaceValue == Undefined.instance) {
/* 534 */       if ("*".equals(localName)) {
/* 535 */         ns = null;
/*     */       } else {
/* 537 */         ns = getDefaultNamespace(cx).getDelegate();
/*     */       } 
/* 539 */     } else if (namespaceValue == null) {
/* 540 */       ns = null;
/* 541 */     } else if (namespaceValue instanceof Namespace) {
/* 542 */       ns = ((Namespace)namespaceValue).getDelegate();
/*     */     } else {
/* 544 */       ns = this.namespacePrototype.constructNamespace(namespaceValue).getDelegate();
/*     */     } 
/*     */     
/* 547 */     if (localName != null && localName.equals("*")) localName = null; 
/* 548 */     return XmlNode.QName.create(ns, localName);
/*     */   }
/*     */   
/*     */   XmlNode.QName toNodeQName(Context cx, String name, boolean attribute) {
/* 552 */     XmlNode.Namespace defaultNamespace = getDefaultNamespace(cx).getDelegate();
/* 553 */     if (name != null && name.equals("*")) {
/* 554 */       return XmlNode.QName.create(null, null);
/*     */     }
/* 556 */     if (attribute) {
/* 557 */       return XmlNode.QName.create(XmlNode.Namespace.GLOBAL, name);
/*     */     }
/* 559 */     return XmlNode.QName.create(defaultNamespace, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XmlNode.QName toNodeQName(Context cx, Object nameValue, boolean attribute) {
/* 569 */     if (nameValue instanceof XMLName)
/* 570 */       return ((XMLName)nameValue).toQname(); 
/* 571 */     if (nameValue instanceof QName) {
/* 572 */       QName qname = (QName)nameValue;
/* 573 */       return qname.getDelegate();
/* 574 */     }  if (nameValue instanceof Boolean || nameValue instanceof Number || nameValue == Undefined.instance || nameValue == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 580 */       throw badXMLName(nameValue);
/*     */     }
/* 582 */     String local = null;
/* 583 */     if (nameValue instanceof String) {
/* 584 */       local = (String)nameValue;
/*     */     } else {
/* 586 */       local = ScriptRuntime.toString(nameValue);
/*     */     } 
/* 588 */     return toNodeQName(cx, local, attribute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isXMLName(Context _cx, Object nameObj) {
/* 598 */     return XMLName.accept(nameObj);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object toDefaultXmlNamespace(Context cx, Object uriValue) {
/* 603 */     return this.namespacePrototype.constructNamespace(uriValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public String escapeTextValue(Object o) {
/* 608 */     return this.options.escapeTextValue(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public String escapeAttributeValue(Object o) {
/* 613 */     return this.options.escapeAttributeValue(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public Ref nameRef(Context cx, Object name, Scriptable scope, int memberTypeFlags) {
/* 618 */     if ((memberTypeFlags & 0x2) == 0)
/*     */     {
/* 620 */       throw Kit.codeBug();
/*     */     }
/* 622 */     XMLName xmlName = toAttributeName(cx, name);
/* 623 */     return xmlPrimaryReference(cx, xmlName, scope);
/*     */   }
/*     */ 
/*     */   
/*     */   public Ref nameRef(Context cx, Object namespace, Object name, Scriptable scope, int memberTypeFlags) {
/* 628 */     XMLName xmlName = XMLName.create(toNodeQName(cx, namespace, name), false, false);
/*     */ 
/*     */     
/* 631 */     if ((memberTypeFlags & 0x2) != 0 && 
/* 632 */       !xmlName.isAttributeName()) {
/* 633 */       xmlName.setAttributeName();
/*     */     }
/*     */ 
/*     */     
/* 637 */     return xmlPrimaryReference(cx, xmlName, scope);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\XMLLibImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */