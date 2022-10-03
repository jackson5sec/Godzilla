/*     */ package org.mozilla.javascript.xmlimpl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.mozilla.javascript.Callable;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.Function;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.ScriptableObject;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ import org.mozilla.javascript.xml.XMLObject;
/*     */ 
/*     */ class XMLList
/*     */   extends XMLObjectImpl implements Function {
/*     */   static final long serialVersionUID = -4543618751670781135L;
/*     */   private XmlNode.InternalList _annos;
/*  17 */   private XMLObjectImpl targetObject = null;
/*  18 */   private XmlNode.QName targetProperty = null;
/*     */   
/*     */   XMLList(XMLLibImpl lib, Scriptable scope, XMLObject prototype) {
/*  21 */     super(lib, scope, prototype);
/*  22 */     this._annos = new XmlNode.InternalList();
/*     */   }
/*     */ 
/*     */   
/*     */   XmlNode.InternalList getNodeList() {
/*  27 */     return this._annos;
/*     */   }
/*     */ 
/*     */   
/*     */   void setTargets(XMLObjectImpl object, XmlNode.QName property) {
/*  32 */     this.targetObject = object;
/*  33 */     this.targetProperty = property;
/*     */   }
/*     */ 
/*     */   
/*     */   private XML getXmlFromAnnotation(int index) {
/*  38 */     return getXML(this._annos, index);
/*     */   }
/*     */ 
/*     */   
/*     */   XML getXML() {
/*  43 */     if (length() == 1) return getXmlFromAnnotation(0); 
/*  44 */     return null;
/*     */   }
/*     */   
/*     */   private void internalRemoveFromList(int index) {
/*  48 */     this._annos.remove(index);
/*     */   }
/*     */   
/*     */   void replace(int index, XML xml) {
/*  52 */     if (index < length()) {
/*  53 */       XmlNode.InternalList newAnnoList = new XmlNode.InternalList();
/*  54 */       newAnnoList.add(this._annos, 0, index);
/*  55 */       newAnnoList.add(xml);
/*  56 */       newAnnoList.add(this._annos, index + 1, length());
/*  57 */       this._annos = newAnnoList;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void insert(int index, XML xml) {
/*  62 */     if (index < length()) {
/*  63 */       XmlNode.InternalList newAnnoList = new XmlNode.InternalList();
/*  64 */       newAnnoList.add(this._annos, 0, index);
/*  65 */       newAnnoList.add(xml);
/*  66 */       newAnnoList.add(this._annos, index, length());
/*  67 */       this._annos = newAnnoList;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  79 */     return "XMLList";
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
/*     */   public Object get(int index, Scriptable start) {
/*  92 */     if (index >= 0 && index < length()) {
/*  93 */       return getXmlFromAnnotation(index);
/*     */     }
/*  95 */     return Scriptable.NOT_FOUND;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasXMLProperty(XMLName xmlName) {
/* 102 */     return (getPropertyList(xmlName).length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(int index, Scriptable start) {
/* 107 */     return (0 <= index && index < length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void putXMLProperty(XMLName xmlName, Object value) {
/* 115 */     if (value == null) {
/* 116 */       value = "null";
/* 117 */     } else if (value instanceof Undefined) {
/* 118 */       value = "undefined";
/*     */     } 
/*     */     
/* 121 */     if (length() > 1) {
/* 122 */       throw ScriptRuntime.typeError("Assignment to lists with more than one item is not supported");
/*     */     }
/* 124 */     if (length() == 0) {
/*     */ 
/*     */       
/* 127 */       if (this.targetObject != null && this.targetProperty != null && this.targetProperty.getLocalName() != null && this.targetProperty.getLocalName().length() > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 133 */         XML xmlValue = newTextElementXML(null, this.targetProperty, null);
/* 134 */         addToList(xmlValue);
/*     */         
/* 136 */         if (xmlName.isAttributeName()) {
/* 137 */           setAttribute(xmlName, value);
/*     */         } else {
/* 139 */           XML xml = item(0);
/* 140 */           xml.putXMLProperty(xmlName, value);
/*     */ 
/*     */           
/* 143 */           replace(0, item(0));
/*     */         } 
/*     */ 
/*     */         
/* 147 */         XMLName name2 = XMLName.formProperty(this.targetProperty.getNamespace().getUri(), this.targetProperty.getLocalName());
/*     */ 
/*     */         
/* 150 */         this.targetObject.putXMLProperty(name2, this);
/* 151 */         replace(0, this.targetObject.getXML().getLastXmlChild());
/*     */       } else {
/* 153 */         throw ScriptRuntime.typeError("Assignment to empty XMLList without targets not supported");
/*     */       }
/*     */     
/* 156 */     } else if (xmlName.isAttributeName()) {
/* 157 */       setAttribute(xmlName, value);
/*     */     } else {
/* 159 */       XML xml = item(0);
/* 160 */       xml.putXMLProperty(xmlName, value);
/*     */ 
/*     */       
/* 163 */       replace(0, item(0));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   Object getXMLProperty(XMLName name) {
/* 169 */     return getPropertyList(name);
/*     */   }
/*     */   
/*     */   private void replaceNode(XML xml, XML with) {
/* 173 */     xml.replaceWith(with);
/*     */   }
/*     */   
/*     */   public void put(int index, Scriptable start, Object value) {
/*     */     XMLObject xmlValue;
/* 178 */     Object parent = Undefined.instance;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     if (value == null) {
/* 184 */       value = "null";
/* 185 */     } else if (value instanceof Undefined) {
/* 186 */       value = "undefined";
/*     */     } 
/*     */     
/* 189 */     if (value instanceof XMLObject) {
/* 190 */       xmlValue = (XMLObject)value;
/*     */     }
/* 192 */     else if (this.targetProperty == null) {
/* 193 */       xmlValue = newXMLFromJs(value.toString());
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 199 */       xmlValue = item(index);
/* 200 */       if (xmlValue == null) {
/* 201 */         XML x = item(0);
/* 202 */         xmlValue = (x == null) ? newTextElementXML(null, this.targetProperty, null) : x.copy();
/*     */       } 
/*     */ 
/*     */       
/* 206 */       ((XML)xmlValue).setChildren(value);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 211 */     if (index < length()) {
/* 212 */       parent = item(index).parent();
/* 213 */     } else if (length() == 0) {
/* 214 */       parent = (this.targetObject != null) ? this.targetObject.getXML() : parent();
/*     */     } else {
/*     */       
/* 217 */       parent = parent();
/*     */     } 
/*     */     
/* 220 */     if (parent instanceof XML) {
/*     */       
/* 222 */       XML xmlParent = (XML)parent;
/*     */       
/* 224 */       if (index < length()) {
/*     */         
/* 226 */         XML xmlNode = getXmlFromAnnotation(index);
/*     */         
/* 228 */         if (xmlValue instanceof XML) {
/* 229 */           replaceNode(xmlNode, (XML)xmlValue);
/* 230 */           replace(index, xmlNode);
/* 231 */         } else if (xmlValue instanceof XMLList) {
/*     */           
/* 233 */           XMLList list = (XMLList)xmlValue;
/*     */           
/* 235 */           if (list.length() > 0) {
/* 236 */             int lastIndexAdded = xmlNode.childIndex();
/* 237 */             replaceNode(xmlNode, list.item(0));
/* 238 */             replace(index, list.item(0));
/*     */             
/* 240 */             for (int i = 1; i < list.length(); i++) {
/* 241 */               xmlParent.insertChildAfter(xmlParent.getXmlChild(lastIndexAdded), list.item(i));
/* 242 */               lastIndexAdded++;
/* 243 */               insert(index + i, list.item(i));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 249 */         xmlParent.appendChild(xmlValue);
/* 250 */         addToList(xmlParent.getLastXmlChild());
/*     */       }
/*     */     
/*     */     }
/* 254 */     else if (index < length()) {
/* 255 */       XML xmlNode = getXML(this._annos, index);
/*     */       
/* 257 */       if (xmlValue instanceof XML) {
/* 258 */         replaceNode(xmlNode, (XML)xmlValue);
/* 259 */         replace(index, xmlNode);
/* 260 */       } else if (xmlValue instanceof XMLList) {
/*     */         
/* 262 */         XMLList list = (XMLList)xmlValue;
/*     */         
/* 264 */         if (list.length() > 0) {
/* 265 */           replaceNode(xmlNode, list.item(0));
/* 266 */           replace(index, list.item(0));
/*     */           
/* 268 */           for (int i = 1; i < list.length(); i++) {
/* 269 */             insert(index + i, list.item(i));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/* 274 */       addToList(xmlValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private XML getXML(XmlNode.InternalList _annos, int index) {
/* 280 */     if (index >= 0 && index < length()) {
/* 281 */       return xmlFromNode(_annos.item(index));
/*     */     }
/* 283 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void deleteXMLProperty(XMLName name) {
/* 289 */     for (int i = 0; i < length(); i++) {
/* 290 */       XML xml = getXmlFromAnnotation(i);
/*     */       
/* 292 */       if (xml.isElement()) {
/* 293 */         xml.deleteXMLProperty(name);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(int index) {
/* 300 */     if (index >= 0 && index < length()) {
/* 301 */       XML xml = getXmlFromAnnotation(index);
/*     */       
/* 303 */       xml.remove();
/*     */       
/* 305 */       internalRemoveFromList(index);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getIds() {
/*     */     Object[] enumObjs;
/* 313 */     if (isPrototype()) {
/* 314 */       enumObjs = new Object[0];
/*     */     } else {
/* 316 */       enumObjs = new Object[length()];
/*     */       
/* 318 */       for (int i = 0; i < enumObjs.length; i++) {
/* 319 */         enumObjs[i] = Integer.valueOf(i);
/*     */       }
/*     */     } 
/*     */     
/* 323 */     return enumObjs;
/*     */   }
/*     */   
/*     */   public Object[] getIdsForDebug() {
/* 327 */     return getIds();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void remove() {
/* 333 */     int nLen = length();
/* 334 */     for (int i = nLen - 1; i >= 0; i--) {
/* 335 */       XML xml = getXmlFromAnnotation(i);
/* 336 */       if (xml != null) {
/* 337 */         xml.remove();
/* 338 */         internalRemoveFromList(i);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   XML item(int index) {
/* 344 */     return (this._annos != null) ? getXmlFromAnnotation(index) : createEmptyXML();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setAttribute(XMLName xmlName, Object value) {
/* 349 */     for (int i = 0; i < length(); i++) {
/* 350 */       XML xml = getXmlFromAnnotation(i);
/* 351 */       xml.setAttribute(xmlName, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   void addToList(Object toAdd) {
/* 356 */     this._annos.addToList(toAdd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XMLList child(int index) {
/* 367 */     XMLList result = newXMLList();
/*     */     
/* 369 */     for (int i = 0; i < length(); i++) {
/* 370 */       result.addToList(getXmlFromAnnotation(i).child(index));
/*     */     }
/*     */     
/* 373 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList child(XMLName xmlName) {
/* 378 */     XMLList result = newXMLList();
/*     */     
/* 380 */     for (int i = 0; i < length(); i++) {
/* 381 */       result.addToList(getXmlFromAnnotation(i).child(xmlName));
/*     */     }
/*     */     
/* 384 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   void addMatches(XMLList rv, XMLName name) {
/* 389 */     for (int i = 0; i < length(); i++) {
/* 390 */       getXmlFromAnnotation(i).addMatches(rv, name);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList children() {
/* 396 */     ArrayList<XML> list = new ArrayList<XML>();
/*     */     
/* 398 */     for (int i = 0; i < length(); i++) {
/* 399 */       XML xml = getXmlFromAnnotation(i);
/*     */       
/* 401 */       if (xml != null) {
/* 402 */         XMLList childList = xml.children();
/*     */         
/* 404 */         int cChildren = childList.length();
/* 405 */         for (int k = 0; k < cChildren; k++) {
/* 406 */           list.add(childList.item(k));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 411 */     XMLList allChildren = newXMLList();
/* 412 */     int sz = list.size();
/*     */     
/* 414 */     for (int j = 0; j < sz; j++) {
/* 415 */       allChildren.addToList(list.get(j));
/*     */     }
/*     */     
/* 418 */     return allChildren;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList comments() {
/* 423 */     XMLList result = newXMLList();
/*     */     
/* 425 */     for (int i = 0; i < length(); i++) {
/* 426 */       XML xml = getXmlFromAnnotation(i);
/* 427 */       result.addToList(xml.comments());
/*     */     } 
/*     */     
/* 430 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList elements(XMLName name) {
/* 435 */     XMLList rv = newXMLList();
/* 436 */     for (int i = 0; i < length(); i++) {
/* 437 */       XML xml = getXmlFromAnnotation(i);
/* 438 */       rv.addToList(xml.elements(name));
/*     */     } 
/* 440 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean contains(Object xml) {
/* 445 */     boolean result = false;
/*     */     
/* 447 */     for (int i = 0; i < length(); i++) {
/* 448 */       XML member = getXmlFromAnnotation(i);
/*     */       
/* 450 */       if (member.equivalentXml(xml)) {
/* 451 */         result = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 456 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLObjectImpl copy() {
/* 461 */     XMLList result = newXMLList();
/*     */     
/* 463 */     for (int i = 0; i < length(); i++) {
/* 464 */       XML xml = getXmlFromAnnotation(i);
/* 465 */       result.addToList(xml.copy());
/*     */     } 
/*     */     
/* 468 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasOwnProperty(XMLName xmlName) {
/* 473 */     if (isPrototype()) {
/* 474 */       String property = xmlName.localName();
/* 475 */       return (findPrototypeId(property) != 0);
/*     */     } 
/* 477 */     return (getPropertyList(xmlName).length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasComplexContent() {
/*     */     boolean complexContent;
/* 484 */     int length = length();
/*     */     
/* 486 */     if (length == 0) {
/* 487 */       complexContent = false;
/* 488 */     } else if (length == 1) {
/* 489 */       complexContent = getXmlFromAnnotation(0).hasComplexContent();
/*     */     } else {
/* 491 */       complexContent = false;
/*     */       
/* 493 */       for (int i = 0; i < length; i++) {
/* 494 */         XML nextElement = getXmlFromAnnotation(i);
/* 495 */         if (nextElement.isElement()) {
/* 496 */           complexContent = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 502 */     return complexContent;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasSimpleContent() {
/* 507 */     if (length() == 0)
/* 508 */       return true; 
/* 509 */     if (length() == 1) {
/* 510 */       return getXmlFromAnnotation(0).hasSimpleContent();
/*     */     }
/* 512 */     for (int i = 0; i < length(); i++) {
/* 513 */       XML nextElement = getXmlFromAnnotation(i);
/* 514 */       if (nextElement.isElement()) {
/* 515 */         return false;
/*     */       }
/*     */     } 
/* 518 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int length() {
/* 524 */     int result = 0;
/*     */     
/* 526 */     if (this._annos != null) {
/* 527 */       result = this._annos.length();
/*     */     }
/*     */     
/* 530 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   void normalize() {
/* 535 */     for (int i = 0; i < length(); i++) {
/* 536 */       getXmlFromAnnotation(i).normalize();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object parent() {
/* 546 */     if (length() == 0) return Undefined.instance;
/*     */     
/* 548 */     XML candidateParent = null;
/*     */     
/* 550 */     for (int i = 0; i < length(); i++) {
/* 551 */       Object currParent = getXmlFromAnnotation(i).parent();
/* 552 */       if (!(currParent instanceof XML)) return Undefined.instance; 
/* 553 */       XML xml = (XML)currParent;
/* 554 */       if (i == 0) {
/*     */         
/* 556 */         candidateParent = xml;
/*     */       }
/* 558 */       else if (!candidateParent.is(xml)) {
/*     */ 
/*     */         
/* 561 */         return Undefined.instance;
/*     */       } 
/*     */     } 
/*     */     
/* 565 */     return candidateParent;
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList processingInstructions(XMLName xmlName) {
/* 570 */     XMLList result = newXMLList();
/*     */     
/* 572 */     for (int i = 0; i < length(); i++) {
/* 573 */       XML xml = getXmlFromAnnotation(i);
/*     */       
/* 575 */       result.addToList(xml.processingInstructions(xmlName));
/*     */     } 
/*     */     
/* 578 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean propertyIsEnumerable(Object name) {
/*     */     long index;
/* 584 */     if (name instanceof Integer) {
/* 585 */       index = ((Integer)name).intValue();
/* 586 */     } else if (name instanceof Number) {
/* 587 */       double x = ((Number)name).doubleValue();
/* 588 */       index = (long)x;
/* 589 */       if (index != x) {
/* 590 */         return false;
/*     */       }
/* 592 */       if (index == 0L && 1.0D / x < 0.0D)
/*     */       {
/* 594 */         return false;
/*     */       }
/*     */     } else {
/* 597 */       String s = ScriptRuntime.toString(name);
/* 598 */       index = ScriptRuntime.testUint32String(s);
/*     */     } 
/* 600 */     return (0L <= index && index < length());
/*     */   }
/*     */ 
/*     */   
/*     */   XMLList text() {
/* 605 */     XMLList result = newXMLList();
/*     */     
/* 607 */     for (int i = 0; i < length(); i++) {
/* 608 */       result.addToList(getXmlFromAnnotation(i).text());
/*     */     }
/*     */     
/* 611 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 617 */     if (hasSimpleContent()) {
/* 618 */       StringBuilder sb = new StringBuilder();
/*     */       
/* 620 */       for (int i = 0; i < length(); i++) {
/* 621 */         XML next = getXmlFromAnnotation(i);
/* 622 */         if (!next.isComment() && !next.isProcessingInstruction())
/*     */         {
/*     */           
/* 625 */           sb.append(next.toString());
/*     */         }
/*     */       } 
/*     */       
/* 629 */       return sb.toString();
/*     */     } 
/* 631 */     return toXMLString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String toSource(int indent) {
/* 637 */     return toXMLString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String toXMLString() {
/* 643 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 645 */     for (int i = 0; i < length(); i++) {
/* 646 */       if (getProcessor().isPrettyPrinting() && i != 0) {
/* 647 */         sb.append('\n');
/*     */       }
/* 649 */       sb.append(getXmlFromAnnotation(i).toXMLString());
/*     */     } 
/* 651 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   Object valueOf() {
/* 656 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean equivalentXml(Object target) {
/* 665 */     boolean result = false;
/*     */ 
/*     */     
/* 668 */     if (target instanceof Undefined && length() == 0) {
/* 669 */       result = true;
/* 670 */     } else if (length() == 1) {
/* 671 */       result = getXmlFromAnnotation(0).equivalentXml(target);
/* 672 */     } else if (target instanceof XMLList) {
/* 673 */       XMLList otherList = (XMLList)target;
/*     */       
/* 675 */       if (otherList.length() == length()) {
/* 676 */         result = true;
/*     */         
/* 678 */         for (int i = 0; i < length(); i++) {
/* 679 */           if (!getXmlFromAnnotation(i).equivalentXml(otherList.getXmlFromAnnotation(i))) {
/* 680 */             result = false;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 687 */     return result;
/*     */   }
/*     */   
/*     */   private XMLList getPropertyList(XMLName name) {
/* 691 */     XMLList propertyList = newXMLList();
/* 692 */     XmlNode.QName qname = null;
/*     */     
/* 694 */     if (!name.isDescendants() && !name.isAttributeName())
/*     */     {
/*     */       
/* 697 */       qname = name.toQname();
/*     */     }
/*     */     
/* 700 */     propertyList.setTargets(this, qname);
/*     */     
/* 702 */     for (int i = 0; i < length(); i++) {
/* 703 */       propertyList.addToList(getXmlFromAnnotation(i).getPropertyList(name));
/*     */     }
/*     */ 
/*     */     
/* 707 */     return propertyList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object applyOrCall(boolean isApply, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 713 */     String methodName = isApply ? "apply" : "call";
/* 714 */     if (!(thisObj instanceof XMLList) || ((XMLList)thisObj).targetProperty == null)
/*     */     {
/* 716 */       throw ScriptRuntime.typeError1("msg.isnt.function", methodName);
/*     */     }
/*     */     
/* 719 */     return ScriptRuntime.applyOrCall(isApply, cx, scope, thisObj, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object jsConstructor(Context cx, boolean inNewExpr, Object[] args) {
/* 726 */     if (args.length == 0) {
/* 727 */       return newXMLList();
/*     */     }
/* 729 */     Object arg0 = args[0];
/* 730 */     if (!inNewExpr && arg0 instanceof XMLList)
/*     */     {
/* 732 */       return arg0;
/*     */     }
/* 734 */     return newXMLListFrom(arg0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable getExtraMethodSource(Context cx) {
/* 743 */     if (length() == 1) {
/* 744 */       return (Scriptable)getXmlFromAnnotation(0);
/*     */     }
/* 746 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 753 */     if (this.targetProperty == null) {
/* 754 */       throw ScriptRuntime.notFunctionError(this);
/*     */     }
/* 756 */     String methodName = this.targetProperty.getLocalName();
/*     */     
/* 758 */     boolean isApply = methodName.equals("apply");
/* 759 */     if (isApply || methodName.equals("call")) {
/* 760 */       return applyOrCall(isApply, cx, scope, thisObj, args);
/*     */     }
/* 762 */     if (!(thisObj instanceof XMLObject)) {
/* 763 */       throw ScriptRuntime.typeError1("msg.incompat.call", methodName);
/*     */     }
/* 765 */     Object func = null;
/* 766 */     Scriptable sobj = thisObj;
/*     */     
/* 768 */     while (sobj instanceof XMLObject) {
/* 769 */       XMLObject xmlObject = (XMLObject)sobj;
/* 770 */       func = xmlObject.getFunctionProperty(cx, methodName);
/* 771 */       if (func != Scriptable.NOT_FOUND) {
/*     */         break;
/*     */       }
/* 774 */       sobj = xmlObject.getExtraMethodSource(cx);
/* 775 */       if (sobj != null) {
/* 776 */         thisObj = sobj;
/* 777 */         if (!(sobj instanceof XMLObject)) {
/* 778 */           func = ScriptableObject.getProperty(sobj, methodName);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 783 */     if (!(func instanceof Callable)) {
/* 784 */       throw ScriptRuntime.notFunctionError(thisObj, func, methodName);
/*     */     }
/* 786 */     return ((Callable)func).call(cx, scope, thisObj, args);
/*     */   }
/*     */   
/*     */   public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
/* 790 */     throw ScriptRuntime.typeError1("msg.not.ctor", "XMLList");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\XMLList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */