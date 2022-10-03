/*     */ package org.mozilla.javascript.xmlimpl;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.IdFunctionCall;
/*     */ import org.mozilla.javascript.IdFunctionObject;
/*     */ import org.mozilla.javascript.Kit;
/*     */ import org.mozilla.javascript.NativeWith;
/*     */ import org.mozilla.javascript.Ref;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ import org.mozilla.javascript.xml.XMLObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class XMLObjectImpl
/*     */   extends XMLObject
/*     */ {
/*  19 */   private static final Object XMLOBJECT_TAG = "XMLObject"; private XMLLibImpl lib; private boolean prototypeFlag; private static final int Id_constructor = 1; private static final int Id_addNamespace = 2; private static final int Id_appendChild = 3; private static final int Id_attribute = 4; private static final int Id_attributes = 5; private static final int Id_child = 6; private static final int Id_childIndex = 7; private static final int Id_children = 8; private static final int Id_comments = 9; private static final int Id_contains = 10; private static final int Id_copy = 11; private static final int Id_descendants = 12; private static final int Id_elements = 13; private static final int Id_inScopeNamespaces = 14; private static final int Id_insertChildAfter = 15; private static final int Id_insertChildBefore = 16;
/*     */   private static final int Id_hasOwnProperty = 17;
/*     */   private static final int Id_hasComplexContent = 18;
/*     */   private static final int Id_hasSimpleContent = 19;
/*     */   private static final int Id_length = 20;
/*     */   
/*     */   protected XMLObjectImpl(XMLLibImpl lib, Scriptable scope, XMLObject prototype) {
/*  26 */     initialize(lib, scope, prototype);
/*     */   }
/*     */   private static final int Id_localName = 21; private static final int Id_name = 22; private static final int Id_namespace = 23; private static final int Id_namespaceDeclarations = 24; private static final int Id_nodeKind = 25; private static final int Id_normalize = 26; private static final int Id_parent = 27; private static final int Id_prependChild = 28; private static final int Id_processingInstructions = 29; private static final int Id_propertyIsEnumerable = 30;
/*     */   private static final int Id_removeNamespace = 31;
/*     */   
/*     */   final void initialize(XMLLibImpl lib, Scriptable scope, XMLObject prototype) {
/*  32 */     setParentScope(scope);
/*  33 */     setPrototype((Scriptable)prototype);
/*  34 */     this.prototypeFlag = (prototype == null);
/*  35 */     this.lib = lib;
/*     */   }
/*     */   private static final int Id_replace = 32; private static final int Id_setChildren = 33; private static final int Id_setLocalName = 34; private static final int Id_setName = 35; private static final int Id_setNamespace = 36; private static final int Id_text = 37; private static final int Id_toString = 38; private static final int Id_toSource = 39; private static final int Id_toXMLString = 40; private static final int Id_valueOf = 41; private static final int MAX_PROTOTYPE_ID = 41;
/*     */   final boolean isPrototype() {
/*  39 */     return this.prototypeFlag;
/*     */   }
/*     */   
/*     */   XMLLibImpl getLib() {
/*  43 */     return this.lib;
/*     */   }
/*     */   
/*     */   final XML newXML(XmlNode node) {
/*  47 */     return this.lib.newXML(node);
/*     */   }
/*     */   
/*     */   XML xmlFromNode(XmlNode node) {
/*  51 */     if (node.getXml() == null) {
/*  52 */       node.setXml(newXML(node));
/*     */     }
/*  54 */     return node.getXml();
/*     */   }
/*     */   
/*     */   final XMLList newXMLList() {
/*  58 */     return this.lib.newXMLList();
/*     */   }
/*     */   
/*     */   final XMLList newXMLListFrom(Object o) {
/*  62 */     return this.lib.newXMLListFrom(o);
/*     */   }
/*     */   
/*     */   final XmlProcessor getProcessor() {
/*  66 */     return this.lib.getProcessor();
/*     */   }
/*     */   
/*     */   final QName newQName(String uri, String localName, String prefix) {
/*  70 */     return this.lib.newQName(uri, localName, prefix);
/*     */   }
/*     */   
/*     */   final QName newQName(XmlNode.QName name) {
/*  74 */     return this.lib.newQName(name);
/*     */   }
/*     */   
/*     */   final Namespace createNamespace(XmlNode.Namespace declaration) {
/*  78 */     if (declaration == null) return null; 
/*  79 */     return this.lib.createNamespaces(new XmlNode.Namespace[] { declaration })[0];
/*     */   }
/*     */   
/*     */   final Namespace[] createNamespaces(XmlNode.Namespace[] declarations) {
/*  83 */     return this.lib.createNamespaces(declarations);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Scriptable getPrototype() {
/*  89 */     return super.getPrototype();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setPrototype(Scriptable prototype) {
/*  94 */     super.setPrototype(prototype);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Scriptable getParentScope() {
/*  99 */     return super.getParentScope();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setParentScope(Scriptable parent) {
/* 104 */     super.setParentScope(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object getDefaultValue(Class<?> hint) {
/* 109 */     return toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasInstance(Scriptable scriptable) {
/* 114 */     return super.hasInstance(scriptable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean hasXMLProperty(XMLName paramXMLName);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Object getXMLProperty(XMLName paramXMLName);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void putXMLProperty(XMLName paramXMLName, Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void deleteXMLProperty(XMLName paramXMLName);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract boolean equivalentXml(Object paramObject);
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void addMatches(XMLList paramXMLList, XMLName paramXMLName);
/*     */ 
/*     */ 
/*     */   
/*     */   private XMLList getMatches(XMLName name) {
/* 149 */     XMLList rv = newXMLList();
/* 150 */     addMatches(rv, name);
/* 151 */     return rv;
/*     */   }
/*     */   abstract XML getXML();
/*     */   abstract XMLList child(int paramInt);
/*     */   abstract XMLList child(XMLName paramXMLName);
/*     */   abstract XMLList children();
/*     */   abstract XMLList comments();
/*     */   
/*     */   abstract boolean contains(Object paramObject);
/*     */   
/*     */   abstract XMLObjectImpl copy();
/*     */   
/*     */   abstract XMLList elements(XMLName paramXMLName);
/*     */   
/*     */   abstract boolean hasOwnProperty(XMLName paramXMLName);
/*     */   
/*     */   abstract boolean hasComplexContent();
/*     */   
/*     */   abstract boolean hasSimpleContent();
/*     */   
/*     */   abstract int length();
/*     */   
/*     */   abstract void normalize();
/*     */   
/*     */   abstract Object parent();
/*     */   
/*     */   abstract XMLList processingInstructions(XMLName paramXMLName);
/*     */   
/*     */   abstract boolean propertyIsEnumerable(Object paramObject);
/*     */   
/*     */   abstract XMLList text();
/*     */   
/*     */   public abstract String toString();
/*     */   
/*     */   abstract String toSource(int paramInt);
/*     */   
/*     */   abstract String toXMLString();
/*     */   
/*     */   abstract Object valueOf();
/*     */   
/*     */   protected abstract Object jsConstructor(Context paramContext, boolean paramBoolean, Object[] paramArrayOfObject);
/*     */   
/*     */   protected final Object equivalentValues(Object value) {
/* 194 */     boolean result = equivalentXml(value);
/* 195 */     return result ? Boolean.TRUE : Boolean.FALSE;
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
/*     */   public final boolean has(Context cx, Object id) {
/* 209 */     if (cx == null) cx = Context.getCurrentContext(); 
/* 210 */     XMLName xmlName = this.lib.toXMLNameOrIndex(cx, id);
/* 211 */     if (xmlName == null) {
/* 212 */       long index = ScriptRuntime.lastUint32Result(cx);
/*     */       
/* 214 */       return has((int)index, (Scriptable)this);
/*     */     } 
/* 216 */     return hasXMLProperty(xmlName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(String name, Scriptable start) {
/* 221 */     Context cx = Context.getCurrentContext();
/* 222 */     return hasXMLProperty(this.lib.toXMLNameFromString(cx, name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object get(Context cx, Object id) {
/* 229 */     if (cx == null) cx = Context.getCurrentContext(); 
/* 230 */     XMLName xmlName = this.lib.toXMLNameOrIndex(cx, id);
/* 231 */     if (xmlName == null) {
/* 232 */       long index = ScriptRuntime.lastUint32Result(cx);
/*     */       
/* 234 */       Object result = get((int)index, (Scriptable)this);
/* 235 */       if (result == Scriptable.NOT_FOUND) {
/* 236 */         result = Undefined.instance;
/*     */       }
/* 238 */       return result;
/*     */     } 
/* 240 */     return getXMLProperty(xmlName);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(String name, Scriptable start) {
/* 245 */     Context cx = Context.getCurrentContext();
/* 246 */     return getXMLProperty(this.lib.toXMLNameFromString(cx, name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void put(Context cx, Object id, Object value) {
/* 253 */     if (cx == null) cx = Context.getCurrentContext(); 
/* 254 */     XMLName xmlName = this.lib.toXMLNameOrIndex(cx, id);
/* 255 */     if (xmlName == null) {
/* 256 */       long index = ScriptRuntime.lastUint32Result(cx);
/*     */       
/* 258 */       put((int)index, (Scriptable)this, value);
/*     */       return;
/*     */     } 
/* 261 */     putXMLProperty(xmlName, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String name, Scriptable start, Object value) {
/* 266 */     Context cx = Context.getCurrentContext();
/* 267 */     putXMLProperty(this.lib.toXMLNameFromString(cx, name), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean delete(Context cx, Object id) {
/* 274 */     if (cx == null) cx = Context.getCurrentContext(); 
/* 275 */     XMLName xmlName = this.lib.toXMLNameOrIndex(cx, id);
/* 276 */     if (xmlName == null) {
/* 277 */       long index = ScriptRuntime.lastUint32Result(cx);
/*     */       
/* 279 */       delete((int)index);
/* 280 */       return true;
/*     */     } 
/* 282 */     deleteXMLProperty(xmlName);
/* 283 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(String name) {
/* 289 */     Context cx = Context.getCurrentContext();
/* 290 */     deleteXMLProperty(this.lib.toXMLNameFromString(cx, name));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getFunctionProperty(Context cx, int id) {
/* 295 */     if (isPrototype()) {
/* 296 */       return get(id, (Scriptable)this);
/*     */     }
/* 298 */     Scriptable proto = getPrototype();
/* 299 */     if (proto instanceof XMLObject) {
/* 300 */       return ((XMLObject)proto).getFunctionProperty(cx, id);
/*     */     }
/*     */     
/* 303 */     return NOT_FOUND;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getFunctionProperty(Context cx, String name) {
/* 308 */     if (isPrototype()) {
/* 309 */       return super.get(name, (Scriptable)this);
/*     */     }
/* 311 */     Scriptable proto = getPrototype();
/* 312 */     if (proto instanceof XMLObject) {
/* 313 */       return ((XMLObject)proto).getFunctionProperty(cx, name);
/*     */     }
/*     */     
/* 316 */     return NOT_FOUND;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Ref memberRef(Context cx, Object elem, int memberTypeFlags) {
/* 322 */     boolean attribute = ((memberTypeFlags & 0x2) != 0);
/* 323 */     boolean descendants = ((memberTypeFlags & 0x4) != 0);
/* 324 */     if (!attribute && !descendants)
/*     */     {
/*     */ 
/*     */       
/* 328 */       throw Kit.codeBug();
/*     */     }
/* 330 */     XmlNode.QName qname = this.lib.toNodeQName(cx, elem, attribute);
/* 331 */     XMLName rv = XMLName.create(qname, attribute, descendants);
/* 332 */     rv.initXMLObject(this);
/* 333 */     return rv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ref memberRef(Context cx, Object namespace, Object elem, int memberTypeFlags) {
/* 343 */     boolean attribute = ((memberTypeFlags & 0x2) != 0);
/* 344 */     boolean descendants = ((memberTypeFlags & 0x4) != 0);
/* 345 */     XMLName rv = XMLName.create(this.lib.toNodeQName(cx, namespace, elem), attribute, descendants);
/*     */     
/* 347 */     rv.initXMLObject(this);
/* 348 */     return rv;
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeWith enterWith(Scriptable scope) {
/* 353 */     return new XMLWithScope(this.lib, scope, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeWith enterDotQuery(Scriptable scope) {
/* 358 */     XMLWithScope xws = new XMLWithScope(this.lib, scope, this);
/* 359 */     xws.initAsDotQuery();
/* 360 */     return xws;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object addValues(Context cx, boolean thisIsLeft, Object value) {
/* 366 */     if (value instanceof XMLObject) {
/*     */       XMLObject v1; XMLObject v2;
/* 368 */       if (thisIsLeft) {
/* 369 */         v1 = this;
/* 370 */         v2 = (XMLObject)value;
/*     */       } else {
/* 372 */         v1 = (XMLObject)value;
/* 373 */         v2 = this;
/*     */       } 
/* 375 */       return this.lib.addXMLObjects(cx, v1, v2);
/*     */     } 
/* 377 */     if (value == Undefined.instance)
/*     */     {
/* 379 */       return ScriptRuntime.toString(this);
/*     */     }
/*     */     
/* 382 */     return super.addValues(cx, thisIsLeft, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void exportAsJSClass(boolean sealed) {
/* 392 */     this.prototypeFlag = true;
/* 393 */     exportAsJSClass(41, getParentScope(), sealed);
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
/*     */   protected int findPrototypeId(String s) {
/* 447 */     int c, id = 0; String X = null;
/* 448 */     switch (s.length()) { case 4:
/* 449 */         c = s.charAt(0);
/* 450 */         if (c == 99) { X = "copy"; id = 11; break; }
/* 451 */          if (c == 110) { X = "name"; id = 22; break; }
/* 452 */          if (c == 116) { X = "text"; id = 37; }  break;
/*     */       case 5:
/* 454 */         X = "child"; id = 6; break;
/* 455 */       case 6: c = s.charAt(0);
/* 456 */         if (c == 108) { X = "length"; id = 20; break; }
/* 457 */          if (c == 112) { X = "parent"; id = 27; }  break;
/*     */       case 7:
/* 459 */         c = s.charAt(0);
/* 460 */         if (c == 114) { X = "replace"; id = 32; break; }
/* 461 */          if (c == 115) { X = "setName"; id = 35; break; }
/* 462 */          if (c == 118) { X = "valueOf"; id = 41; }  break;
/*     */       case 8:
/* 464 */         switch (s.charAt(2)) { case 'S':
/* 465 */             c = s.charAt(7);
/* 466 */             if (c == 101) { X = "toSource"; id = 39; break; }
/* 467 */              if (c == 103) { X = "toString"; id = 38; }  break;
/*     */           case 'd':
/* 469 */             X = "nodeKind"; id = 25; break;
/* 470 */           case 'e': X = "elements"; id = 13; break;
/* 471 */           case 'i': X = "children"; id = 8; break;
/* 472 */           case 'm': X = "comments"; id = 9; break;
/* 473 */           case 'n': X = "contains"; id = 10; break; }  break;
/*     */       case 9:
/* 475 */         switch (s.charAt(2)) { case 'c':
/* 476 */             X = "localName"; id = 21; break;
/* 477 */           case 'm': X = "namespace"; id = 23; break;
/* 478 */           case 'r': X = "normalize"; id = 26; break;
/* 479 */           case 't': X = "attribute"; id = 4; break; }  break;
/*     */       case 10:
/* 481 */         c = s.charAt(0);
/* 482 */         if (c == 97) { X = "attributes"; id = 5; break; }
/* 483 */          if (c == 99) { X = "childIndex"; id = 7; }  break;
/*     */       case 11:
/* 485 */         switch (s.charAt(0)) { case 'a':
/* 486 */             X = "appendChild"; id = 3; break;
/* 487 */           case 'c': X = "constructor"; id = 1; break;
/* 488 */           case 'd': X = "descendants"; id = 12; break;
/* 489 */           case 's': X = "setChildren"; id = 33; break;
/* 490 */           case 't': X = "toXMLString"; id = 40; break; }  break;
/*     */       case 12:
/* 492 */         c = s.charAt(0);
/* 493 */         if (c == 97) { X = "addNamespace"; id = 2; break; }
/* 494 */          if (c == 112) { X = "prependChild"; id = 28; break; }
/* 495 */          if (c == 115) {
/* 496 */           c = s.charAt(3);
/* 497 */           if (c == 76) { X = "setLocalName"; id = 34; break; }
/* 498 */            if (c == 78) { X = "setNamespace"; id = 36; } 
/*     */         }  break;
/*     */       case 14:
/* 501 */         X = "hasOwnProperty"; id = 17; break;
/* 502 */       case 15: X = "removeNamespace"; id = 31; break;
/* 503 */       case 16: c = s.charAt(0);
/* 504 */         if (c == 104) { X = "hasSimpleContent"; id = 19; break; }
/* 505 */          if (c == 105) { X = "insertChildAfter"; id = 15; }  break;
/*     */       case 17:
/* 507 */         c = s.charAt(3);
/* 508 */         if (c == 67) { X = "hasComplexContent"; id = 18; break; }
/* 509 */          if (c == 99) { X = "inScopeNamespaces"; id = 14; break; }
/* 510 */          if (c == 101) { X = "insertChildBefore"; id = 16; }  break;
/*     */       case 20:
/* 512 */         X = "propertyIsEnumerable"; id = 30; break;
/* 513 */       case 21: X = "namespaceDeclarations"; id = 24; break;
/* 514 */       case 22: X = "processingInstructions"; id = 29; break; }
/*     */     
/* 516 */     if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 520 */     return id;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/*     */     IdFunctionObject ctor;
/* 528 */     switch (id) {
/*     */       
/*     */       case 1:
/* 531 */         if (this instanceof XML) {
/* 532 */           ctor = new XMLCtor((XML)this, XMLOBJECT_TAG, id, 1);
/*     */         } else {
/* 534 */           ctor = new IdFunctionObject((IdFunctionCall)this, XMLOBJECT_TAG, id, 1);
/*     */         } 
/* 536 */         initPrototypeConstructor(ctor);
/*     */         return;
/*     */       
/*     */       case 2:
/* 540 */         arity = 1; s = "addNamespace"; break;
/* 541 */       case 3: arity = 1; s = "appendChild"; break;
/* 542 */       case 4: arity = 1; s = "attribute"; break;
/* 543 */       case 5: arity = 0; s = "attributes"; break;
/* 544 */       case 6: arity = 1; s = "child"; break;
/* 545 */       case 7: arity = 0; s = "childIndex"; break;
/* 546 */       case 8: arity = 0; s = "children"; break;
/* 547 */       case 9: arity = 0; s = "comments"; break;
/* 548 */       case 10: arity = 1; s = "contains"; break;
/* 549 */       case 11: arity = 0; s = "copy"; break;
/* 550 */       case 12: arity = 1; s = "descendants"; break;
/* 551 */       case 13: arity = 1; s = "elements"; break;
/* 552 */       case 18: arity = 0; s = "hasComplexContent"; break;
/* 553 */       case 17: arity = 1; s = "hasOwnProperty"; break;
/* 554 */       case 19: arity = 0; s = "hasSimpleContent"; break;
/* 555 */       case 14: arity = 0; s = "inScopeNamespaces"; break;
/* 556 */       case 15: arity = 2; s = "insertChildAfter"; break;
/* 557 */       case 16: arity = 2; s = "insertChildBefore"; break;
/* 558 */       case 20: arity = 0; s = "length"; break;
/* 559 */       case 21: arity = 0; s = "localName"; break;
/* 560 */       case 22: arity = 0; s = "name"; break;
/* 561 */       case 23: arity = 1; s = "namespace"; break;
/*     */       case 24:
/* 563 */         arity = 0; s = "namespaceDeclarations"; break;
/* 564 */       case 25: arity = 0; s = "nodeKind"; break;
/* 565 */       case 26: arity = 0; s = "normalize"; break;
/* 566 */       case 27: arity = 0; s = "parent"; break;
/* 567 */       case 28: arity = 1; s = "prependChild"; break;
/*     */       case 29:
/* 569 */         arity = 1; s = "processingInstructions"; break;
/*     */       case 30:
/* 571 */         arity = 1; s = "propertyIsEnumerable"; break;
/* 572 */       case 31: arity = 1; s = "removeNamespace"; break;
/* 573 */       case 32: arity = 2; s = "replace"; break;
/* 574 */       case 33: arity = 1; s = "setChildren"; break;
/* 575 */       case 34: arity = 1; s = "setLocalName"; break;
/* 576 */       case 35: arity = 1; s = "setName"; break;
/* 577 */       case 36: arity = 1; s = "setNamespace"; break;
/* 578 */       case 37: arity = 0; s = "text"; break;
/* 579 */       case 38: arity = 0; s = "toString"; break;
/* 580 */       case 39: arity = 1; s = "toSource"; break;
/* 581 */       case 40: arity = 1; s = "toXMLString"; break;
/* 582 */       case 41: arity = 0; s = "valueOf"; break;
/*     */       default:
/* 584 */         throw new IllegalArgumentException(String.valueOf(id));
/*     */     } 
/* 586 */     initPrototypeMethod(XMLOBJECT_TAG, id, s, arity);
/*     */   }
/*     */   
/*     */   private Object[] toObjectArray(Object[] typed) {
/* 590 */     Object[] rv = new Object[typed.length];
/* 591 */     for (int i = 0; i < rv.length; i++) {
/* 592 */       rv[i] = typed[i];
/*     */     }
/* 594 */     return rv;
/*     */   }
/*     */   
/*     */   private void xmlMethodNotFound(Object object, String name) {
/* 598 */     throw ScriptRuntime.notFunctionError(object, name); } public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) { Namespace namespace2; Object arg0; String prefix; Namespace array[], namespace1; XMLName xMLName2; String localName; Object arg; Namespace ns; XMLName xMLName1;
/*     */     XmlNode.QName qname;
/*     */     XMLName xmlName;
/*     */     int indent;
/*     */     Namespace rv;
/*     */     Object arg1, object1;
/*     */     QName qName;
/* 605 */     if (!f.hasTag(XMLOBJECT_TAG)) {
/* 606 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 608 */     int id = f.methodId();
/* 609 */     if (id == 1) {
/* 610 */       return jsConstructor(cx, (thisObj == null), args);
/*     */     }
/*     */ 
/*     */     
/* 614 */     if (!(thisObj instanceof XMLObjectImpl))
/* 615 */       throw incompatibleCallError(f); 
/* 616 */     XMLObjectImpl realThis = (XMLObjectImpl)thisObj;
/*     */     
/* 618 */     XML xml = realThis.getXML();
/* 619 */     switch (id) {
/*     */       case 3:
/* 621 */         if (xml == null) xmlMethodNotFound(realThis, "appendChild"); 
/* 622 */         return xml.appendChild(arg(args, 0));
/*     */       
/*     */       case 2:
/* 625 */         if (xml == null) xmlMethodNotFound(realThis, "addNamespace"); 
/* 626 */         namespace2 = this.lib.castToNamespace(cx, arg(args, 0));
/* 627 */         return xml.addNamespace(namespace2);
/*     */       
/*     */       case 7:
/* 630 */         if (xml == null) xmlMethodNotFound(realThis, "childIndex"); 
/* 631 */         return ScriptRuntime.wrapInt(xml.childIndex());
/*     */       
/*     */       case 14:
/* 634 */         if (xml == null) xmlMethodNotFound(realThis, "inScopeNamespaces"); 
/* 635 */         return cx.newArray(scope, toObjectArray((Object[])xml.inScopeNamespaces()));
/*     */       
/*     */       case 15:
/* 638 */         if (xml == null) xmlMethodNotFound(realThis, "insertChildAfter"); 
/* 639 */         arg0 = arg(args, 0);
/* 640 */         if (arg0 == null || arg0 instanceof XML) {
/* 641 */           return xml.insertChildAfter((XML)arg0, arg(args, 1));
/*     */         }
/* 643 */         return Undefined.instance;
/*     */       
/*     */       case 16:
/* 646 */         if (xml == null) xmlMethodNotFound(realThis, "insertChildBefore"); 
/* 647 */         arg0 = arg(args, 0);
/* 648 */         if (arg0 == null || arg0 instanceof XML) {
/* 649 */           return xml.insertChildBefore((XML)arg0, arg(args, 1));
/*     */         }
/* 651 */         return Undefined.instance;
/*     */       
/*     */       case 21:
/* 654 */         if (xml == null) xmlMethodNotFound(realThis, "localName"); 
/* 655 */         return xml.localName();
/*     */       
/*     */       case 22:
/* 658 */         if (xml == null) xmlMethodNotFound(realThis, "name"); 
/* 659 */         return xml.name();
/*     */       
/*     */       case 23:
/* 662 */         if (xml == null) xmlMethodNotFound(realThis, "namespace"); 
/* 663 */         prefix = (args.length > 0) ? ScriptRuntime.toString(args[0]) : null;
/* 664 */         rv = xml.namespace(prefix);
/* 665 */         if (rv == null) {
/* 666 */           return Undefined.instance;
/*     */         }
/* 668 */         return rv;
/*     */ 
/*     */       
/*     */       case 24:
/* 672 */         if (xml == null) xmlMethodNotFound(realThis, "namespaceDeclarations"); 
/* 673 */         array = xml.namespaceDeclarations();
/* 674 */         return cx.newArray(scope, toObjectArray((Object[])array));
/*     */       
/*     */       case 25:
/* 677 */         if (xml == null) xmlMethodNotFound(realThis, "nodeKind"); 
/* 678 */         return xml.nodeKind();
/*     */       
/*     */       case 28:
/* 681 */         if (xml == null) xmlMethodNotFound(realThis, "prependChild"); 
/* 682 */         return xml.prependChild(arg(args, 0));
/*     */       
/*     */       case 31:
/* 685 */         if (xml == null) xmlMethodNotFound(realThis, "removeNamespace"); 
/* 686 */         namespace1 = this.lib.castToNamespace(cx, arg(args, 0));
/* 687 */         return xml.removeNamespace(namespace1);
/*     */       
/*     */       case 32:
/* 690 */         if (xml == null) xmlMethodNotFound(realThis, "replace"); 
/* 691 */         xMLName2 = this.lib.toXMLNameOrIndex(cx, arg(args, 0));
/* 692 */         arg1 = arg(args, 1);
/* 693 */         if (xMLName2 == null) {
/*     */           
/* 695 */           int index = (int)ScriptRuntime.lastUint32Result(cx);
/* 696 */           return xml.replace(index, arg1);
/*     */         } 
/* 698 */         return xml.replace(xMLName2, arg1);
/*     */ 
/*     */       
/*     */       case 33:
/* 702 */         if (xml == null) xmlMethodNotFound(realThis, "setChildren"); 
/* 703 */         return xml.setChildren(arg(args, 0));
/*     */       
/*     */       case 34:
/* 706 */         if (xml == null) xmlMethodNotFound(realThis, "setLocalName");
/*     */         
/* 708 */         object1 = arg(args, 0);
/* 709 */         if (object1 instanceof QName) {
/* 710 */           localName = ((QName)object1).localName();
/*     */         } else {
/* 712 */           localName = ScriptRuntime.toString(object1);
/*     */         } 
/* 714 */         xml.setLocalName(localName);
/* 715 */         return Undefined.instance;
/*     */       
/*     */       case 35:
/* 718 */         if (xml == null) xmlMethodNotFound(realThis, "setName"); 
/* 719 */         arg = (args.length != 0) ? args[0] : Undefined.instance;
/* 720 */         qName = this.lib.constructQName(cx, arg);
/* 721 */         xml.setName(qName);
/* 722 */         return Undefined.instance;
/*     */       
/*     */       case 36:
/* 725 */         if (xml == null) xmlMethodNotFound(realThis, "setNamespace"); 
/* 726 */         ns = this.lib.castToNamespace(cx, arg(args, 0));
/* 727 */         xml.setNamespace(ns);
/* 728 */         return Undefined.instance;
/*     */ 
/*     */       
/*     */       case 4:
/* 732 */         xMLName1 = XMLName.create(this.lib.toNodeQName(cx, arg(args, 0), true), true, false);
/* 733 */         return realThis.getMatches(xMLName1);
/*     */       
/*     */       case 5:
/* 736 */         return realThis.getMatches(XMLName.create(XmlNode.QName.create(null, null), true, false));
/*     */       case 6:
/* 738 */         xMLName1 = this.lib.toXMLNameOrIndex(cx, arg(args, 0));
/* 739 */         if (xMLName1 == null) {
/*     */           
/* 741 */           int index = (int)ScriptRuntime.lastUint32Result(cx);
/* 742 */           return realThis.child(index);
/*     */         } 
/* 744 */         return realThis.child(xMLName1);
/*     */ 
/*     */       
/*     */       case 8:
/* 748 */         return realThis.children();
/*     */       case 9:
/* 750 */         return realThis.comments();
/*     */       case 10:
/* 752 */         return ScriptRuntime.wrapBoolean(realThis.contains(arg(args, 0)));
/*     */       
/*     */       case 11:
/* 755 */         return realThis.copy();
/*     */       case 12:
/* 757 */         qname = (args.length == 0) ? XmlNode.QName.create(null, null) : this.lib.toNodeQName(cx, args[0], false);
/* 758 */         return realThis.getMatches(XMLName.create(qname, false, true));
/*     */       
/*     */       case 13:
/* 761 */         xmlName = (args.length == 0) ? XMLName.formStar() : this.lib.toXMLName(cx, args[0]);
/*     */ 
/*     */         
/* 764 */         return realThis.elements(xmlName);
/*     */       
/*     */       case 17:
/* 767 */         xmlName = this.lib.toXMLName(cx, arg(args, 0));
/* 768 */         return ScriptRuntime.wrapBoolean(realThis.hasOwnProperty(xmlName));
/*     */ 
/*     */       
/*     */       case 18:
/* 772 */         return ScriptRuntime.wrapBoolean(realThis.hasComplexContent());
/*     */       case 19:
/* 774 */         return ScriptRuntime.wrapBoolean(realThis.hasSimpleContent());
/*     */       case 20:
/* 776 */         return ScriptRuntime.wrapInt(realThis.length());
/*     */       case 26:
/* 778 */         realThis.normalize();
/* 779 */         return Undefined.instance;
/*     */       case 27:
/* 781 */         return realThis.parent();
/*     */       case 29:
/* 783 */         xmlName = (args.length > 0) ? this.lib.toXMLName(cx, args[0]) : XMLName.formStar();
/*     */ 
/*     */         
/* 786 */         return realThis.processingInstructions(xmlName);
/*     */       
/*     */       case 30:
/* 789 */         return ScriptRuntime.wrapBoolean(realThis.propertyIsEnumerable(arg(args, 0)));
/*     */ 
/*     */       
/*     */       case 37:
/* 793 */         return realThis.text();
/*     */       case 38:
/* 795 */         return realThis.toString();
/*     */       case 39:
/* 797 */         indent = ScriptRuntime.toInt32(args, 0);
/* 798 */         return realThis.toSource(indent);
/*     */       case 40:
/* 800 */         return realThis.toXMLString();
/*     */       
/*     */       case 41:
/* 803 */         return realThis.valueOf();
/*     */     } 
/* 805 */     throw new IllegalArgumentException(String.valueOf(id)); }
/*     */ 
/*     */   
/*     */   private static Object arg(Object[] args, int i) {
/* 809 */     return (i < args.length) ? args[i] : Undefined.instance;
/*     */   }
/*     */   
/*     */   final XML newTextElementXML(XmlNode reference, XmlNode.QName qname, String value) {
/* 813 */     return this.lib.newTextElementXML(reference, qname, value);
/*     */   }
/*     */ 
/*     */   
/*     */   final XML newXMLFromJs(Object inputObject) {
/* 818 */     return this.lib.newXMLFromJs(inputObject);
/*     */   }
/*     */   
/*     */   final XML ecmaToXml(Object object) {
/* 822 */     return this.lib.ecmaToXml(object);
/*     */   }
/*     */ 
/*     */   
/*     */   final String ecmaEscapeAttributeValue(String s) {
/* 827 */     String quoted = this.lib.escapeAttributeValue(s);
/* 828 */     return quoted.substring(1, quoted.length() - 1);
/*     */   }
/*     */   
/*     */   final XML createEmptyXML() {
/* 832 */     return newXML(XmlNode.createEmpty(getProcessor()));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\xmlimpl\XMLObjectImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */