/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
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
/*     */ public class NativeObject
/*     */   extends IdScriptableObject
/*     */   implements Map
/*     */ {
/*     */   static final long serialVersionUID = -6345305608474346996L;
/*  26 */   private static final Object OBJECT_TAG = "Object"; private static final int ConstructorId_getPrototypeOf = -1; private static final int ConstructorId_keys = -2; private static final int ConstructorId_getOwnPropertyNames = -3; private static final int ConstructorId_getOwnPropertyDescriptor = -4; private static final int ConstructorId_defineProperty = -5; private static final int ConstructorId_isExtensible = -6; private static final int ConstructorId_preventExtensions = -7; private static final int ConstructorId_defineProperties = -8; private static final int ConstructorId_create = -9; private static final int ConstructorId_isSealed = -10; private static final int ConstructorId_isFrozen = -11; private static final int ConstructorId_seal = -12;
/*     */   private static final int ConstructorId_freeze = -13;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  30 */     NativeObject obj = new NativeObject();
/*  31 */     obj.exportAsJSClass(12, scope, sealed);
/*     */   }
/*     */   private static final int Id_constructor = 1; private static final int Id_toString = 2; private static final int Id_toLocaleString = 3; private static final int Id_valueOf = 4; private static final int Id_hasOwnProperty = 5; private static final int Id_propertyIsEnumerable = 6; private static final int Id_isPrototypeOf = 7; private static final int Id_toSource = 8; private static final int Id___defineGetter__ = 9; private static final int Id___defineSetter__ = 10; private static final int Id___lookupGetter__ = 11; private static final int Id___lookupSetter__ = 12;
/*     */   private static final int MAX_PROTOTYPE_ID = 12;
/*     */   
/*     */   public String getClassName() {
/*  37 */     return "Object";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  43 */     return ScriptRuntime.defaultObjectToString(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fillConstructorProperties(IdFunctionObject ctor) {
/*  49 */     addIdFunctionProperty(ctor, OBJECT_TAG, -1, "getPrototypeOf", 1);
/*     */     
/*  51 */     addIdFunctionProperty(ctor, OBJECT_TAG, -2, "keys", 1);
/*     */     
/*  53 */     addIdFunctionProperty(ctor, OBJECT_TAG, -3, "getOwnPropertyNames", 1);
/*     */     
/*  55 */     addIdFunctionProperty(ctor, OBJECT_TAG, -4, "getOwnPropertyDescriptor", 2);
/*     */     
/*  57 */     addIdFunctionProperty(ctor, OBJECT_TAG, -5, "defineProperty", 3);
/*     */     
/*  59 */     addIdFunctionProperty(ctor, OBJECT_TAG, -6, "isExtensible", 1);
/*     */     
/*  61 */     addIdFunctionProperty(ctor, OBJECT_TAG, -7, "preventExtensions", 1);
/*     */     
/*  63 */     addIdFunctionProperty(ctor, OBJECT_TAG, -8, "defineProperties", 2);
/*     */     
/*  65 */     addIdFunctionProperty(ctor, OBJECT_TAG, -9, "create", 2);
/*     */     
/*  67 */     addIdFunctionProperty(ctor, OBJECT_TAG, -10, "isSealed", 1);
/*     */     
/*  69 */     addIdFunctionProperty(ctor, OBJECT_TAG, -11, "isFrozen", 1);
/*     */     
/*  71 */     addIdFunctionProperty(ctor, OBJECT_TAG, -12, "seal", 1);
/*     */     
/*  73 */     addIdFunctionProperty(ctor, OBJECT_TAG, -13, "freeze", 1);
/*     */     
/*  75 */     super.fillConstructorProperties(ctor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id)
/*     */   {
/*     */     String s;
/*     */     int arity;
/*  83 */     switch (id) { case 1:
/*  84 */         arity = 1; s = "constructor"; break;
/*  85 */       case 2: arity = 0; s = "toString"; break;
/*  86 */       case 3: arity = 0; s = "toLocaleString"; break;
/*  87 */       case 4: arity = 0; s = "valueOf"; break;
/*  88 */       case 5: arity = 1; s = "hasOwnProperty"; break;
/*     */       case 6:
/*  90 */         arity = 1; s = "propertyIsEnumerable"; break;
/*  91 */       case 7: arity = 1; s = "isPrototypeOf"; break;
/*  92 */       case 8: arity = 0; s = "toSource"; break;
/*     */       case 9:
/*  94 */         arity = 2; s = "__defineGetter__"; break;
/*     */       case 10:
/*  96 */         arity = 2; s = "__defineSetter__"; break;
/*     */       case 11:
/*  98 */         arity = 1; s = "__lookupGetter__"; break;
/*     */       case 12:
/* 100 */         arity = 1; s = "__lookupSetter__"; break;
/* 101 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 103 */     initPrototypeMethod(OBJECT_TAG, id, s, arity); } public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) { Object toString; boolean result; ScriptableObject so; Object arg; Callable fun; Object object1; String name; Scriptable scriptable2; ScriptableObject scriptableObject1; Scriptable scriptable1; ScriptableObject obj; String s; int index; Object ids[], nameArg, object2, propsObj; ScriptableObject newObject; Callable getterOrSetter; boolean isSetter; int i; String str1;
/*     */     Object descArg;
/*     */     Scriptable props;
/*     */     boolean bool1;
/*     */     Object gs;
/*     */     Scriptable scriptable3;
/*     */     ScriptableObject desc;
/* 110 */     if (!f.hasTag(OBJECT_TAG)) {
/* 111 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 113 */     int id = f.methodId();
/* 114 */     switch (id) {
/*     */       case 1:
/* 116 */         if (thisObj != null)
/*     */         {
/* 118 */           return f.construct(cx, scope, args);
/*     */         }
/* 120 */         if (args.length == 0 || args[0] == null || args[0] == Undefined.instance)
/*     */         {
/*     */           
/* 123 */           return new NativeObject();
/*     */         }
/* 125 */         return ScriptRuntime.toObject(cx, scope, args[0]);
/*     */ 
/*     */       
/*     */       case 3:
/* 129 */         toString = ScriptableObject.getProperty(thisObj, "toString");
/* 130 */         if (!(toString instanceof Callable)) {
/* 131 */           throw ScriptRuntime.notFunctionError(toString);
/*     */         }
/* 133 */         fun = (Callable)toString;
/* 134 */         return fun.call(cx, scope, thisObj, ScriptRuntime.emptyArgs);
/*     */ 
/*     */       
/*     */       case 2:
/* 138 */         if (cx.hasFeature(4)) {
/* 139 */           String str = ScriptRuntime.defaultObjectToSource(cx, scope, thisObj, args);
/*     */           
/* 141 */           int L = str.length();
/* 142 */           if (L != 0 && str.charAt(0) == '(' && str.charAt(L - 1) == ')')
/*     */           {
/* 144 */             str = str.substring(1, L - 1);
/*     */           }
/* 146 */           return str;
/*     */         } 
/* 148 */         return ScriptRuntime.defaultObjectToString(thisObj);
/*     */ 
/*     */       
/*     */       case 4:
/* 152 */         return thisObj;
/*     */ 
/*     */       
/*     */       case 5:
/* 156 */         object1 = (args.length < 1) ? Undefined.instance : args[0];
/* 157 */         s = ScriptRuntime.toStringIdOrIndex(cx, object1);
/* 158 */         if (s == null) {
/* 159 */           int j = ScriptRuntime.lastIndexResult(cx);
/* 160 */           result = thisObj.has(j, thisObj);
/*     */         } else {
/* 162 */           result = thisObj.has(s, thisObj);
/*     */         } 
/* 164 */         return ScriptRuntime.wrapBoolean(result);
/*     */ 
/*     */ 
/*     */       
/*     */       case 6:
/* 169 */         object1 = (args.length < 1) ? Undefined.instance : args[0];
/* 170 */         s = ScriptRuntime.toStringIdOrIndex(cx, object1);
/* 171 */         if (s == null) {
/* 172 */           int j = ScriptRuntime.lastIndexResult(cx);
/* 173 */           result = thisObj.has(j, thisObj);
/* 174 */           if (result && thisObj instanceof ScriptableObject) {
/* 175 */             ScriptableObject scriptableObject = (ScriptableObject)thisObj;
/* 176 */             int attrs = scriptableObject.getAttributes(j);
/* 177 */             result = ((attrs & 0x2) == 0);
/*     */           } 
/*     */         } else {
/* 180 */           result = thisObj.has(s, thisObj);
/* 181 */           if (result && thisObj instanceof ScriptableObject) {
/* 182 */             ScriptableObject scriptableObject = (ScriptableObject)thisObj;
/* 183 */             int attrs = scriptableObject.getAttributes(s);
/* 184 */             result = ((attrs & 0x2) == 0);
/*     */           } 
/*     */         } 
/* 187 */         return ScriptRuntime.wrapBoolean(result);
/*     */ 
/*     */       
/*     */       case 7:
/* 191 */         result = false;
/* 192 */         if (args.length != 0 && args[0] instanceof Scriptable) {
/* 193 */           Scriptable v = (Scriptable)args[0];
/*     */           do {
/* 195 */             v = v.getPrototype();
/* 196 */             if (v == thisObj) {
/* 197 */               result = true;
/*     */               break;
/*     */             } 
/* 200 */           } while (v != null);
/*     */         } 
/* 202 */         return ScriptRuntime.wrapBoolean(result);
/*     */ 
/*     */       
/*     */       case 8:
/* 206 */         return ScriptRuntime.defaultObjectToSource(cx, scope, thisObj, args);
/*     */ 
/*     */       
/*     */       case 9:
/*     */       case 10:
/* 211 */         if (args.length < 2 || !(args[1] instanceof Callable)) {
/* 212 */           Object badArg = (args.length >= 2) ? args[1] : Undefined.instance;
/*     */           
/* 214 */           throw ScriptRuntime.notFunctionError(badArg);
/*     */         } 
/* 216 */         if (!(thisObj instanceof ScriptableObject)) {
/* 217 */           throw Context.reportRuntimeError2("msg.extend.scriptable", thisObj.getClass().getName(), String.valueOf(args[0]));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 222 */         so = (ScriptableObject)thisObj;
/* 223 */         name = ScriptRuntime.toStringIdOrIndex(cx, args[0]);
/* 224 */         index = (name != null) ? 0 : ScriptRuntime.lastIndexResult(cx);
/*     */         
/* 226 */         getterOrSetter = (Callable)args[1];
/* 227 */         bool1 = (id == 10);
/* 228 */         so.setGetterOrSetter(name, index, getterOrSetter, bool1);
/* 229 */         if (so instanceof NativeArray) {
/* 230 */           ((NativeArray)so).setDenseOnly(false);
/*     */         }
/* 232 */         return Undefined.instance;
/*     */ 
/*     */       
/*     */       case 11:
/*     */       case 12:
/* 237 */         if (args.length < 1 || !(thisObj instanceof ScriptableObject))
/*     */         {
/* 239 */           return Undefined.instance;
/*     */         }
/* 241 */         so = (ScriptableObject)thisObj;
/* 242 */         name = ScriptRuntime.toStringIdOrIndex(cx, args[0]);
/* 243 */         index = (name != null) ? 0 : ScriptRuntime.lastIndexResult(cx);
/*     */         
/* 245 */         isSetter = (id == 12);
/*     */         
/*     */         while (true) {
/* 248 */           gs = so.getGetterOrSetter(name, index, isSetter);
/* 249 */           if (gs != null) {
/*     */             break;
/*     */           }
/*     */           
/* 253 */           Scriptable v = so.getPrototype();
/* 254 */           if (v == null)
/*     */             break; 
/* 256 */           if (v instanceof ScriptableObject) {
/* 257 */             so = (ScriptableObject)v; continue;
/*     */           } 
/*     */           break;
/*     */         } 
/* 261 */         if (gs != null) {
/* 262 */           return gs;
/*     */         }
/* 264 */         return Undefined.instance;
/*     */ 
/*     */       
/*     */       case -1:
/* 268 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 269 */         scriptable2 = ensureScriptable(arg);
/* 270 */         return scriptable2.getPrototype();
/*     */ 
/*     */       
/*     */       case -2:
/* 274 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 275 */         scriptable2 = ensureScriptable(arg);
/* 276 */         ids = scriptable2.getIds();
/* 277 */         for (i = 0; i < ids.length; i++) {
/* 278 */           ids[i] = ScriptRuntime.toString(ids[i]);
/*     */         }
/* 280 */         return cx.newArray(scope, ids);
/*     */ 
/*     */       
/*     */       case -3:
/* 284 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 285 */         scriptableObject1 = ensureScriptableObject(arg);
/* 286 */         ids = scriptableObject1.getAllIds();
/* 287 */         for (i = 0; i < ids.length; i++) {
/* 288 */           ids[i] = ScriptRuntime.toString(ids[i]);
/*     */         }
/* 290 */         return cx.newArray(scope, ids);
/*     */ 
/*     */       
/*     */       case -4:
/* 294 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/*     */ 
/*     */ 
/*     */         
/* 298 */         scriptableObject1 = ensureScriptableObject(arg);
/* 299 */         nameArg = (args.length < 2) ? Undefined.instance : args[1];
/* 300 */         str1 = ScriptRuntime.toString(nameArg);
/* 301 */         scriptable3 = scriptableObject1.getOwnPropertyDescriptor(cx, str1);
/* 302 */         return (scriptable3 == null) ? Undefined.instance : scriptable3;
/*     */ 
/*     */       
/*     */       case -5:
/* 306 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 307 */         scriptableObject1 = ensureScriptableObject(arg);
/* 308 */         object2 = (args.length < 2) ? Undefined.instance : args[1];
/* 309 */         descArg = (args.length < 3) ? Undefined.instance : args[2];
/* 310 */         desc = ensureScriptableObject(descArg);
/* 311 */         scriptableObject1.defineOwnProperty(cx, object2, desc);
/* 312 */         return scriptableObject1;
/*     */ 
/*     */       
/*     */       case -6:
/* 316 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 317 */         scriptableObject1 = ensureScriptableObject(arg);
/* 318 */         return Boolean.valueOf(scriptableObject1.isExtensible());
/*     */ 
/*     */       
/*     */       case -7:
/* 322 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 323 */         scriptableObject1 = ensureScriptableObject(arg);
/* 324 */         scriptableObject1.preventExtensions();
/* 325 */         return scriptableObject1;
/*     */ 
/*     */       
/*     */       case -8:
/* 329 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 330 */         scriptableObject1 = ensureScriptableObject(arg);
/* 331 */         propsObj = (args.length < 2) ? Undefined.instance : args[1];
/* 332 */         props = Context.toObject(propsObj, getParentScope());
/* 333 */         scriptableObject1.defineOwnProperties(cx, ensureScriptableObject(props));
/* 334 */         return scriptableObject1;
/*     */ 
/*     */       
/*     */       case -9:
/* 338 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 339 */         scriptable1 = (arg == null) ? null : ensureScriptable(arg);
/*     */         
/* 341 */         newObject = new NativeObject();
/* 342 */         newObject.setParentScope(getParentScope());
/* 343 */         newObject.setPrototype(scriptable1);
/*     */         
/* 345 */         if (args.length > 1 && args[1] != Undefined.instance) {
/* 346 */           props = Context.toObject(args[1], getParentScope());
/* 347 */           newObject.defineOwnProperties(cx, ensureScriptableObject(props));
/*     */         } 
/*     */         
/* 350 */         return newObject;
/*     */ 
/*     */       
/*     */       case -10:
/* 354 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 355 */         obj = ensureScriptableObject(arg);
/*     */         
/* 357 */         if (obj.isExtensible()) return Boolean.FALSE;
/*     */         
/* 359 */         for (Object object3 : obj.getAllIds()) {
/* 360 */           Object configurable = obj.getOwnPropertyDescriptor(cx, object3).get("configurable");
/* 361 */           if (Boolean.TRUE.equals(configurable)) {
/* 362 */             return Boolean.FALSE;
/*     */           }
/*     */         } 
/* 365 */         return Boolean.TRUE;
/*     */ 
/*     */       
/*     */       case -11:
/* 369 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 370 */         obj = ensureScriptableObject(arg);
/*     */         
/* 372 */         if (obj.isExtensible()) return Boolean.FALSE;
/*     */         
/* 374 */         for (Object object : obj.getAllIds()) {
/* 375 */           ScriptableObject scriptableObject = obj.getOwnPropertyDescriptor(cx, object);
/* 376 */           if (Boolean.TRUE.equals(scriptableObject.get("configurable")))
/* 377 */             return Boolean.FALSE; 
/* 378 */           if (isDataDescriptor(scriptableObject) && Boolean.TRUE.equals(scriptableObject.get("writable"))) {
/* 379 */             return Boolean.FALSE;
/*     */           }
/*     */         } 
/* 382 */         return Boolean.TRUE;
/*     */ 
/*     */       
/*     */       case -12:
/* 386 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 387 */         obj = ensureScriptableObject(arg);
/*     */         
/* 389 */         for (Object object : obj.getAllIds()) {
/* 390 */           ScriptableObject scriptableObject = obj.getOwnPropertyDescriptor(cx, object);
/* 391 */           if (Boolean.TRUE.equals(scriptableObject.get("configurable"))) {
/* 392 */             scriptableObject.put("configurable", scriptableObject, Boolean.FALSE);
/* 393 */             obj.defineOwnProperty(cx, object, scriptableObject, false);
/*     */           } 
/*     */         } 
/* 396 */         obj.preventExtensions();
/*     */         
/* 398 */         return obj;
/*     */ 
/*     */       
/*     */       case -13:
/* 402 */         arg = (args.length < 1) ? Undefined.instance : args[0];
/* 403 */         obj = ensureScriptableObject(arg);
/*     */         
/* 405 */         for (Object object : obj.getAllIds()) {
/* 406 */           ScriptableObject scriptableObject = obj.getOwnPropertyDescriptor(cx, object);
/* 407 */           if (isDataDescriptor(scriptableObject) && Boolean.TRUE.equals(scriptableObject.get("writable")))
/* 408 */             scriptableObject.put("writable", scriptableObject, Boolean.FALSE); 
/* 409 */           if (Boolean.TRUE.equals(scriptableObject.get("configurable")))
/* 410 */             scriptableObject.put("configurable", scriptableObject, Boolean.FALSE); 
/* 411 */           obj.defineOwnProperty(cx, object, scriptableObject, false);
/*     */         } 
/* 413 */         obj.preventExtensions();
/*     */         
/* 415 */         return obj;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 420 */     throw new IllegalArgumentException(String.valueOf(id)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 427 */     if (key instanceof String)
/* 428 */       return has((String)key, this); 
/* 429 */     if (key instanceof Number) {
/* 430 */       return has(((Number)key).intValue(), this);
/*     */     }
/* 432 */     return false;
/*     */   }
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 436 */     for (Object obj : values()) {
/* 437 */       if (value == obj || (value != null && value.equals(obj)))
/*     */       {
/* 439 */         return true;
/*     */       }
/*     */     } 
/* 442 */     return false;
/*     */   }
/*     */   
/*     */   public Object remove(Object key) {
/* 446 */     Object value = get(key);
/* 447 */     if (key instanceof String) {
/* 448 */       delete((String)key);
/* 449 */     } else if (key instanceof Number) {
/* 450 */       delete(((Number)key).intValue());
/*     */     } 
/* 452 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Object> keySet() {
/* 457 */     return new KeySet();
/*     */   }
/*     */   
/*     */   public Collection<Object> values() {
/* 461 */     return new ValueCollection();
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<Object, Object>> entrySet() {
/* 465 */     return new EntrySet();
/*     */   }
/*     */   
/*     */   public Object put(Object key, Object value) {
/* 469 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void putAll(Map m) {
/* 473 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 477 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   class EntrySet
/*     */     extends AbstractSet<Map.Entry<Object, Object>>
/*     */   {
/*     */     public Iterator<Map.Entry<Object, Object>> iterator() {
/* 484 */       return new Iterator<Map.Entry<Object, Object>>() {
/* 485 */           Object[] ids = NativeObject.this.getIds();
/* 486 */           Object key = null;
/* 487 */           int index = 0;
/*     */           
/*     */           public boolean hasNext() {
/* 490 */             return (this.index < this.ids.length);
/*     */           }
/*     */           
/*     */           public Map.Entry<Object, Object> next() {
/* 494 */             final Object ekey = this.key = this.ids[this.index++];
/* 495 */             final Object value = NativeObject.this.get(this.key);
/* 496 */             return new Map.Entry<Object, Object>() {
/*     */                 public Object getKey() {
/* 498 */                   return ekey;
/*     */                 }
/*     */                 
/*     */                 public Object getValue() {
/* 502 */                   return value;
/*     */                 }
/*     */                 
/*     */                 public Object setValue(Object value) {
/* 506 */                   throw new UnsupportedOperationException();
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public boolean equals(Object other) {
/* 511 */                   if (!(other instanceof Map.Entry)) {
/* 512 */                     return false;
/*     */                   }
/* 514 */                   Map.Entry<?, ?> e = (Map.Entry<?, ?>)other;
/* 515 */                   return (((ekey == null) ? (e.getKey() == null) : ekey.equals(e.getKey())) && ((value == null) ? (e.getValue() == null) : value.equals(e.getValue())));
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 public int hashCode() {
/* 521 */                   return ((ekey == null) ? 0 : ekey.hashCode()) ^ ((value == null) ? 0 : value.hashCode());
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 527 */                   return ekey + "=" + value;
/*     */                 }
/*     */               };
/*     */           }
/*     */           
/*     */           public void remove() {
/* 533 */             if (this.key == null) {
/* 534 */               throw new IllegalStateException();
/*     */             }
/* 536 */             NativeObject.this.remove(this.key);
/* 537 */             this.key = null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 544 */       return NativeObject.this.size();
/*     */     }
/*     */   }
/*     */   
/*     */   class KeySet
/*     */     extends AbstractSet<Object>
/*     */   {
/*     */     public boolean contains(Object key) {
/* 552 */       return NativeObject.this.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Object> iterator() {
/* 557 */       return new Iterator() {
/* 558 */           Object[] ids = NativeObject.this.getIds();
/*     */           Object key;
/* 560 */           int index = 0;
/*     */           
/*     */           public boolean hasNext() {
/* 563 */             return (this.index < this.ids.length);
/*     */           }
/*     */           
/*     */           public Object next() {
/*     */             try {
/* 568 */               return this.key = this.ids[this.index++];
/* 569 */             } catch (ArrayIndexOutOfBoundsException e) {
/* 570 */               this.key = null;
/* 571 */               throw new NoSuchElementException();
/*     */             } 
/*     */           }
/*     */           
/*     */           public void remove() {
/* 576 */             if (this.key == null) {
/* 577 */               throw new IllegalStateException();
/*     */             }
/* 579 */             NativeObject.this.remove(this.key);
/* 580 */             this.key = null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 587 */       return NativeObject.this.size();
/*     */     }
/*     */   }
/*     */   
/*     */   class ValueCollection
/*     */     extends AbstractCollection<Object>
/*     */   {
/*     */     public Iterator<Object> iterator() {
/* 595 */       return new Iterator() {
/* 596 */           Object[] ids = NativeObject.this.getIds();
/*     */           Object key;
/* 598 */           int index = 0;
/*     */           
/*     */           public boolean hasNext() {
/* 601 */             return (this.index < this.ids.length);
/*     */           }
/*     */           
/*     */           public Object next() {
/* 605 */             return NativeObject.this.get(this.key = this.ids[this.index++]);
/*     */           }
/*     */           
/*     */           public void remove() {
/* 609 */             if (this.key == null) {
/* 610 */               throw new IllegalStateException();
/*     */             }
/* 612 */             NativeObject.this.remove(this.key);
/* 613 */             this.key = null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 620 */       return NativeObject.this.size();
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
/*     */   protected int findPrototypeId(String s) {
/* 632 */     int c, id = 0; String X = null;
/* 633 */     switch (s.length()) { case 7:
/* 634 */         X = "valueOf"; id = 4; break;
/* 635 */       case 8: c = s.charAt(3);
/* 636 */         if (c == 111) { X = "toSource"; id = 8; break; }
/* 637 */          if (c == 116) { X = "toString"; id = 2; }  break;
/*     */       case 11:
/* 639 */         X = "constructor"; id = 1; break;
/* 640 */       case 13: X = "isPrototypeOf"; id = 7; break;
/* 641 */       case 14: c = s.charAt(0);
/* 642 */         if (c == 104) { X = "hasOwnProperty"; id = 5; break; }
/* 643 */          if (c == 116) { X = "toLocaleString"; id = 3; }  break;
/*     */       case 16:
/* 645 */         c = s.charAt(2);
/* 646 */         if (c == 100) {
/* 647 */           c = s.charAt(8);
/* 648 */           if (c == 71) { X = "__defineGetter__"; id = 9; break; }
/* 649 */            if (c == 83) { X = "__defineSetter__"; id = 10; }
/*     */            break;
/* 651 */         }  if (c == 108) {
/* 652 */           c = s.charAt(8);
/* 653 */           if (c == 71) { X = "__lookupGetter__"; id = 11; break; }
/* 654 */            if (c == 83) { X = "__lookupSetter__"; id = 12; } 
/*     */         }  break;
/*     */       case 20:
/* 657 */         X = "propertyIsEnumerable"; id = 6; break; }
/*     */     
/* 659 */     if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 663 */     return id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */