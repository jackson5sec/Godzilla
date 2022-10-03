/*     */ package org.mozilla.javascript.xmlimpl;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.EcmaError;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.Ref;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ 
/*     */ class XMLName extends Ref {
/*     */   static final long serialVersionUID = 3832176310755686977L;
/*     */   private XmlNode.QName qname;
/*     */   
/*     */   private static boolean isNCNameStartChar(int c) {
/*  15 */     if ((c & 0xFFFFFF80) == 0) {
/*     */       
/*  17 */       if (c >= 97)
/*  18 */         return (c <= 122); 
/*  19 */       if (c >= 65) {
/*  20 */         if (c <= 90) {
/*  21 */           return true;
/*     */         }
/*  23 */         return (c == 95);
/*     */       } 
/*  25 */     } else if ((c & 0xFFFFE000) == 0) {
/*  26 */       return ((192 <= c && c <= 214) || (216 <= c && c <= 246) || (248 <= c && c <= 767) || (880 <= c && c <= 893) || 895 <= c);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     return ((8204 <= c && c <= 8205) || (8304 <= c && c <= 8591) || (11264 <= c && c <= 12271) || (12289 <= c && c <= 55295) || (63744 <= c && c <= 64975) || (65008 <= c && c <= 65533) || (65536 <= c && c <= 983039));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isAttributeName;
/*     */   
/*     */   private boolean isDescendants;
/*     */   private XMLObjectImpl xmlObject;
/*     */   
/*     */   private static boolean isNCNameChar(int c) {
/*  42 */     if ((c & 0xFFFFFF80) == 0) {
/*     */       
/*  44 */       if (c >= 97)
/*  45 */         return (c <= 122); 
/*  46 */       if (c >= 65) {
/*  47 */         if (c <= 90) {
/*  48 */           return true;
/*     */         }
/*  50 */         return (c == 95);
/*  51 */       }  if (c >= 48) {
/*  52 */         return (c <= 57);
/*     */       }
/*  54 */       return (c == 45 || c == 46);
/*     */     } 
/*  56 */     if ((c & 0xFFFFE000) == 0) {
/*  57 */       return (isNCNameStartChar(c) || c == 183 || (768 <= c && c <= 879));
/*     */     }
/*     */     
/*  60 */     return (isNCNameStartChar(c) || (8255 <= c && c <= 8256));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean accept(Object nameObj) {
/*     */     String name;
/*     */     try {
/*  68 */       name = ScriptRuntime.toString(nameObj);
/*  69 */     } catch (EcmaError ee) {
/*  70 */       if ("TypeError".equals(ee.getName())) {
/*  71 */         return false;
/*     */       }
/*  73 */       throw ee;
/*     */     } 
/*     */ 
/*     */     
/*  77 */     int length = name.length();
/*  78 */     if (length != 0 && 
/*  79 */       isNCNameStartChar(name.charAt(0))) {
/*  80 */       for (int i = 1; i != length; i++) {
/*  81 */         if (!isNCNameChar(name.charAt(i))) {
/*  82 */           return false;
/*     */         }
/*     */       } 
/*  85 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  89 */     return false;
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
/*     */   static XMLName formStar() {
/* 101 */     XMLName rv = new XMLName();
/* 102 */     rv.qname = XmlNode.QName.create(null, null);
/* 103 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   static XMLName formProperty(XmlNode.Namespace namespace, String localName) {
/* 109 */     if (localName != null && localName.equals("*")) localName = null; 
/* 110 */     XMLName rv = new XMLName();
/* 111 */     rv.qname = XmlNode.QName.create(namespace, localName);
/* 112 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   static XMLName formProperty(String uri, String localName) {
/* 117 */     return formProperty(XmlNode.Namespace.create(uri), localName);
/*     */   }
/*     */ 
/*     */   
/*     */   static XMLName create(String defaultNamespaceUri, String name) {
/* 122 */     if (name == null) {
/* 123 */       throw new IllegalArgumentException();
/*     */     }
/* 125 */     int l = name.length();
/* 126 */     if (l != 0) {
/* 127 */       char firstChar = name.charAt(0);
/* 128 */       if (firstChar == '*') {
/* 129 */         if (l == 1) {
/* 130 */           return formStar();
/*     */         }
/* 132 */       } else if (firstChar == '@') {
/* 133 */         XMLName xmlName = formProperty("", name.substring(1));
/* 134 */         xmlName.setAttributeName();
/* 135 */         return xmlName;
/*     */       } 
/*     */     } 
/*     */     
/* 139 */     return formProperty(defaultNamespaceUri, name);
/*     */   }
/*     */   
/*     */   static XMLName create(XmlNode.QName qname, boolean attribute, boolean descendants) {
/* 143 */     XMLName rv = new XMLName();
/* 144 */     rv.qname = qname;
/* 145 */     rv.isAttributeName = attribute;
/* 146 */     rv.isDescendants = descendants;
/* 147 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   static XMLName create(XmlNode.QName qname) {
/* 153 */     return create(qname, false, false);
/*     */   }
/*     */   
/*     */   void initXMLObject(XMLObjectImpl xmlObject) {
/* 157 */     if (xmlObject == null) throw new IllegalArgumentException(); 
/* 158 */     if (this.xmlObject != null) throw new IllegalStateException(); 
/* 159 */     this.xmlObject = xmlObject;
/*     */   }
/*     */   
/*     */   String uri() {
/* 163 */     if (this.qname.getNamespace() == null) return null; 
/* 164 */     return this.qname.getNamespace().getUri();
/*     */   }
/*     */   
/*     */   String localName() {
/* 168 */     if (this.qname.getLocalName() == null) return "*"; 
/* 169 */     return this.qname.getLocalName();
/*     */   }
/*     */   
/*     */   private void addDescendantChildren(XMLList list, XML target) {
/* 173 */     XMLName xmlName = this;
/* 174 */     if (target.isElement()) {
/* 175 */       XML[] children = target.getChildren();
/* 176 */       for (int i = 0; i < children.length; i++) {
/* 177 */         if (xmlName.matches(children[i])) {
/* 178 */           list.addToList(children[i]);
/*     */         }
/* 180 */         addDescendantChildren(list, children[i]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void addMatchingAttributes(XMLList list, XML target) {
/* 186 */     XMLName name = this;
/* 187 */     if (target.isElement()) {
/* 188 */       XML[] attributes = target.getAttributes();
/* 189 */       for (int i = 0; i < attributes.length; i++) {
/* 190 */         if (name.matches(attributes[i])) {
/* 191 */           list.addToList(attributes[i]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addDescendantAttributes(XMLList list, XML target) {
/* 198 */     if (target.isElement()) {
/* 199 */       addMatchingAttributes(list, target);
/* 200 */       XML[] children = target.getChildren();
/* 201 */       for (int i = 0; i < children.length; i++) {
/* 202 */         addDescendantAttributes(list, children[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   XMLList matchDescendantAttributes(XMLList rv, XML target) {
/* 208 */     rv.setTargets(target, null);
/* 209 */     addDescendantAttributes(rv, target);
/* 210 */     return rv;
/*     */   }
/*     */   
/*     */   XMLList matchDescendantChildren(XMLList rv, XML target) {
/* 214 */     rv.setTargets(target, null);
/* 215 */     addDescendantChildren(rv, target);
/* 216 */     return rv;
/*     */   }
/*     */   
/*     */   void addDescendants(XMLList rv, XML target) {
/* 220 */     XMLName xmlName = this;
/* 221 */     if (xmlName.isAttributeName()) {
/* 222 */       matchDescendantAttributes(rv, target);
/*     */     } else {
/* 224 */       matchDescendantChildren(rv, target);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addAttributes(XMLList rv, XML target) {
/* 229 */     addMatchingAttributes(rv, target);
/*     */   }
/*     */   
/*     */   void addMatches(XMLList rv, XML target) {
/* 233 */     if (isDescendants()) {
/* 234 */       addDescendants(rv, target);
/* 235 */     } else if (isAttributeName()) {
/* 236 */       addAttributes(rv, target);
/*     */     } else {
/* 238 */       XML[] children = target.getChildren();
/* 239 */       if (children != null) {
/* 240 */         for (int i = 0; i < children.length; i++) {
/* 241 */           if (matches(children[i])) {
/* 242 */             rv.addToList(children[i]);
/*     */           }
/*     */         } 
/*     */       }
/* 246 */       rv.setTargets(target, toQname());
/*     */     } 
/*     */   }
/*     */   
/*     */   XMLList getMyValueOn(XML target) {
/* 251 */     XMLList rv = target.newXMLList();
/* 252 */     addMatches(rv, target);
/* 253 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   void setMyValueOn(XML target, Object value) {
/* 258 */     if (value == null) {
/* 259 */       value = "null";
/* 260 */     } else if (value instanceof Undefined) {
/* 261 */       value = "undefined";
/*     */     } 
/*     */     
/* 264 */     XMLName xmlName = this;
/*     */     
/* 266 */     if (xmlName.isAttributeName()) {
/* 267 */       target.setAttribute(xmlName, value);
/* 268 */     } else if (xmlName.uri() == null && xmlName.localName().equals("*")) {
/* 269 */       target.setChildren(value);
/*     */     } else {
/*     */       
/* 272 */       XMLObjectImpl xmlValue = null;
/*     */       
/* 274 */       if (value instanceof XMLObjectImpl) {
/* 275 */         xmlValue = (XMLObjectImpl)value;
/*     */ 
/*     */         
/* 278 */         if (xmlValue instanceof XML && (
/* 279 */           (XML)xmlValue).isAttribute()) {
/* 280 */           xmlValue = target.makeXmlFromString(xmlName, xmlValue.toString());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 285 */         if (xmlValue instanceof XMLList) {
/* 286 */           for (int i = 0; i < xmlValue.length(); i++) {
/* 287 */             XML xml = ((XMLList)xmlValue).item(i);
/*     */             
/* 289 */             if (xml.isAttribute()) {
/* 290 */               ((XMLList)xmlValue).replace(i, target.makeXmlFromString(xmlName, xml.toString()));
/*     */             }
/*     */           } 
/*     */         }
/*     */       } else {
/* 295 */         xmlValue = target.makeXmlFromString(xmlName, ScriptRuntime.toString(value));
/*     */       } 
/*     */       
/* 298 */       XMLList matches = target.getPropertyList(xmlName);
/*     */       
/* 300 */       if (matches.length() == 0) {
/* 301 */         target.appendChild(xmlValue);
/*     */       } else {
/*     */         
/* 304 */         for (int i = 1; i < matches.length(); i++) {
/* 305 */           target.removeChild(matches.item(i).childIndex());
/*     */         }
/*     */ 
/*     */         
/* 309 */         XML firstMatch = matches.item(0);
/* 310 */         target.replace(firstMatch.childIndex(), xmlValue);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(Context cx) {
/* 317 */     if (this.xmlObject == null) {
/* 318 */       return false;
/*     */     }
/* 320 */     return this.xmlObject.hasXMLProperty(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(Context cx) {
/* 325 */     if (this.xmlObject == null) {
/* 326 */       throw ScriptRuntime.undefReadError(Undefined.instance, toString());
/*     */     }
/*     */     
/* 329 */     return this.xmlObject.getXMLProperty(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object set(Context cx, Object value) {
/* 334 */     if (this.xmlObject == null) {
/* 335 */       throw ScriptRuntime.undefWriteError(Undefined.instance, toString(), value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     if (this.isDescendants) throw Kit.codeBug(); 
/* 342 */     this.xmlObject.putXMLProperty(this, value);
/* 343 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean delete(Context cx) {
/* 348 */     if (this.xmlObject == null) {
/* 349 */       return true;
/*     */     }
/* 351 */     this.xmlObject.deleteXMLProperty(this);
/* 352 */     return !this.xmlObject.hasXMLProperty(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 358 */     StringBuilder buff = new StringBuilder();
/* 359 */     if (this.isDescendants) buff.append(".."); 
/* 360 */     if (this.isAttributeName) buff.append('@'); 
/* 361 */     if (uri() == null) {
/* 362 */       buff.append('*');
/* 363 */       if (localName().equals("*")) {
/* 364 */         return buff.toString();
/*     */       }
/*     */     } else {
/* 367 */       buff.append('"').append(uri()).append('"');
/*     */     } 
/* 369 */     buff.append(':').append(localName());
/* 370 */     return buff.toString();
/*     */   }
/*     */   
/*     */   final XmlNode.QName toQname() {
/* 374 */     return this.qname;
/*     */   }
/*     */   
/*     */   final boolean matchesLocalName(String localName) {
/* 378 */     return (localName().equals("*") || localName().equals(localName));
/*     */   }
/*     */   
/*     */   final boolean matchesElement(XmlNode.QName qname) {
/* 382 */     if ((uri() == null || uri().equals(qname.getNamespace().getUri())) && (
/* 383 */       localName().equals("*") || localName().equals(qname.getLocalName()))) {
/* 384 */       return true;
/*     */     }
/*     */     
/* 387 */     return false;
/*     */   }
/*     */   
/*     */   final boolean matches(XML node) {
/* 391 */     XmlNode.QName qname = node.getNodeQname();
/* 392 */     String nodeUri = null;
/* 393 */     if (qname.getNamespace() != null) {
/* 394 */       nodeUri = qname.getNamespace().getUri();
/*     */     }
/* 396 */     if (this.isAttributeName) {
/* 397 */       if (node.isAttribute()) {
/* 398 */         if ((uri() == null || uri().equals(nodeUri)) && (
/* 399 */           localName().equals("*") || localName().equals(qname.getLocalName()))) {
/* 400 */           return true;
/*     */         }
/*     */         
/* 403 */         return false;
/*     */       } 
/*     */ 
/*     */       
/* 407 */       return false;
/*     */     } 
/*     */     
/* 410 */     if (uri() == null || (node.isElement() && uri().equals(nodeUri))) {
/* 411 */       if (localName().equals("*")) return true; 
/* 412 */       if (node.isElement() && 
/* 413 */         localName().equals(qname.getLocalName())) return true;
/*     */     
/*     */     } 
/* 416 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isAttributeName() {
/* 422 */     return this.isAttributeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setAttributeName() {
/* 429 */     this.isAttributeName = true;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isDescendants() {
/* 434 */     return this.isDescendants;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   void setIsDescendants() {
/* 442 */     this.isDescendants = true;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\XMLName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */