/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
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
/*     */ public abstract class IdScriptableObject
/*     */   extends ScriptableObject
/*     */   implements IdFunctionCall
/*     */ {
/*     */   private transient PrototypeValues prototypeValues;
/*     */   
/*     */   private static final class PrototypeValues
/*     */     implements Serializable
/*     */   {
/*     */     static final long serialVersionUID = 3038645279153854371L;
/*     */     private static final int NAME_SLOT = 1;
/*     */     private static final int SLOT_SPAN = 2;
/*     */     private IdScriptableObject obj;
/*     */     private int maxId;
/*     */     private Object[] valueArray;
/*     */     private short[] attributeArray;
/*     */     int constructorId;
/*     */     private IdFunctionObject constructor;
/*     */     private short constructorAttrs;
/*     */     
/*     */     PrototypeValues(IdScriptableObject obj, int maxId) {
/*  55 */       if (obj == null) throw new IllegalArgumentException(); 
/*  56 */       if (maxId < 1) throw new IllegalArgumentException(); 
/*  57 */       this.obj = obj;
/*  58 */       this.maxId = maxId;
/*     */     }
/*     */ 
/*     */     
/*     */     final int getMaxId() {
/*  63 */       return this.maxId;
/*     */     }
/*     */ 
/*     */     
/*     */     final void initValue(int id, String name, Object value, int attributes) {
/*  68 */       if (1 > id || id > this.maxId)
/*  69 */         throw new IllegalArgumentException(); 
/*  70 */       if (name == null)
/*  71 */         throw new IllegalArgumentException(); 
/*  72 */       if (value == Scriptable.NOT_FOUND)
/*  73 */         throw new IllegalArgumentException(); 
/*  74 */       ScriptableObject.checkValidAttributes(attributes);
/*  75 */       if (this.obj.findPrototypeId(name) != id) {
/*  76 */         throw new IllegalArgumentException(name);
/*     */       }
/*  78 */       if (id == this.constructorId) {
/*  79 */         if (!(value instanceof IdFunctionObject)) {
/*  80 */           throw new IllegalArgumentException("consructor should be initialized with IdFunctionObject");
/*     */         }
/*  82 */         this.constructor = (IdFunctionObject)value;
/*  83 */         this.constructorAttrs = (short)attributes;
/*     */         
/*     */         return;
/*     */       } 
/*  87 */       initSlot(id, name, value, attributes);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void initSlot(int id, String name, Object value, int attributes) {
/*  93 */       Object[] array = this.valueArray;
/*  94 */       if (array == null) {
/*  95 */         throw new IllegalStateException();
/*     */       }
/*  97 */       if (value == null) {
/*  98 */         value = UniqueTag.NULL_VALUE;
/*     */       }
/* 100 */       int index = (id - 1) * 2;
/* 101 */       synchronized (this) {
/* 102 */         Object value2 = array[index];
/* 103 */         if (value2 == null) {
/* 104 */           array[index] = value;
/* 105 */           array[index + 1] = name;
/* 106 */           this.attributeArray[id - 1] = (short)attributes;
/*     */         }
/* 108 */         else if (!name.equals(array[index + 1])) {
/* 109 */           throw new IllegalStateException();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final IdFunctionObject createPrecachedConstructor() {
/* 116 */       if (this.constructorId != 0) throw new IllegalStateException(); 
/* 117 */       this.constructorId = this.obj.findPrototypeId("constructor");
/* 118 */       if (this.constructorId == 0) {
/* 119 */         throw new IllegalStateException("No id for constructor property");
/*     */       }
/*     */       
/* 122 */       this.obj.initPrototypeId(this.constructorId);
/* 123 */       if (this.constructor == null) {
/* 124 */         throw new IllegalStateException(this.obj.getClass().getName() + ".initPrototypeId() did not " + "initialize id=" + this.constructorId);
/*     */       }
/*     */ 
/*     */       
/* 128 */       this.constructor.initFunction(this.obj.getClassName(), ScriptableObject.getTopLevelScope(this.obj));
/*     */       
/* 130 */       this.constructor.markAsConstructor(this.obj);
/* 131 */       return this.constructor;
/*     */     }
/*     */ 
/*     */     
/*     */     final int findId(String name) {
/* 136 */       return this.obj.findPrototypeId(name);
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean has(int id) {
/* 141 */       Object[] array = this.valueArray;
/* 142 */       if (array == null)
/*     */       {
/* 144 */         return true;
/*     */       }
/* 146 */       int valueSlot = (id - 1) * 2;
/* 147 */       Object value = array[valueSlot];
/* 148 */       if (value == null)
/*     */       {
/* 150 */         return true;
/*     */       }
/* 152 */       return (value != Scriptable.NOT_FOUND);
/*     */     }
/*     */ 
/*     */     
/*     */     final Object get(int id) {
/* 157 */       Object value = ensureId(id);
/* 158 */       if (value == UniqueTag.NULL_VALUE) {
/* 159 */         value = null;
/*     */       }
/* 161 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     final void set(int id, Scriptable start, Object value) {
/* 166 */       if (value == Scriptable.NOT_FOUND) throw new IllegalArgumentException(); 
/* 167 */       ensureId(id);
/* 168 */       int attr = this.attributeArray[id - 1];
/* 169 */       if ((attr & 0x1) == 0) {
/* 170 */         if (start == this.obj) {
/* 171 */           if (value == null) {
/* 172 */             value = UniqueTag.NULL_VALUE;
/*     */           }
/* 174 */           int valueSlot = (id - 1) * 2;
/* 175 */           synchronized (this) {
/* 176 */             this.valueArray[valueSlot] = value;
/*     */           } 
/*     */         } else {
/*     */           
/* 180 */           int nameSlot = (id - 1) * 2 + 1;
/* 181 */           String name = (String)this.valueArray[nameSlot];
/* 182 */           start.put(name, start, value);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     final void delete(int id) {
/* 189 */       ensureId(id);
/* 190 */       int attr = this.attributeArray[id - 1];
/* 191 */       if ((attr & 0x4) == 0) {
/* 192 */         int valueSlot = (id - 1) * 2;
/* 193 */         synchronized (this) {
/* 194 */           this.valueArray[valueSlot] = Scriptable.NOT_FOUND;
/* 195 */           this.attributeArray[id - 1] = 0;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final int getAttributes(int id) {
/* 202 */       ensureId(id);
/* 203 */       return this.attributeArray[id - 1];
/*     */     }
/*     */ 
/*     */     
/*     */     final void setAttributes(int id, int attributes) {
/* 208 */       ScriptableObject.checkValidAttributes(attributes);
/* 209 */       ensureId(id);
/* 210 */       synchronized (this) {
/* 211 */         this.attributeArray[id - 1] = (short)attributes;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final Object[] getNames(boolean getAll, Object[] extraEntries) {
/* 217 */       Object[] names = null;
/* 218 */       int count = 0;
/* 219 */       for (int id = 1; id <= this.maxId; id++) {
/* 220 */         Object value = ensureId(id);
/* 221 */         if ((getAll || (this.attributeArray[id - 1] & 0x2) == 0) && 
/* 222 */           value != Scriptable.NOT_FOUND) {
/* 223 */           int nameSlot = (id - 1) * 2 + 1;
/* 224 */           String name = (String)this.valueArray[nameSlot];
/* 225 */           if (names == null) {
/* 226 */             names = new Object[this.maxId];
/*     */           }
/* 228 */           names[count++] = name;
/*     */         } 
/*     */       } 
/*     */       
/* 232 */       if (count == 0)
/* 233 */         return extraEntries; 
/* 234 */       if (extraEntries == null || extraEntries.length == 0) {
/* 235 */         if (count != names.length) {
/* 236 */           Object[] arrayOfObject = new Object[count];
/* 237 */           System.arraycopy(names, 0, arrayOfObject, 0, count);
/* 238 */           names = arrayOfObject;
/*     */         } 
/* 240 */         return names;
/*     */       } 
/* 242 */       int extra = extraEntries.length;
/* 243 */       Object[] tmp = new Object[extra + count];
/* 244 */       System.arraycopy(extraEntries, 0, tmp, 0, extra);
/* 245 */       System.arraycopy(names, 0, tmp, extra, count);
/* 246 */       return tmp;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Object ensureId(int id) {
/* 252 */       Object[] array = this.valueArray;
/* 253 */       if (array == null) {
/* 254 */         synchronized (this) {
/* 255 */           array = this.valueArray;
/* 256 */           if (array == null) {
/* 257 */             array = new Object[this.maxId * 2];
/* 258 */             this.valueArray = array;
/* 259 */             this.attributeArray = new short[this.maxId];
/*     */           } 
/*     */         } 
/*     */       }
/* 263 */       int valueSlot = (id - 1) * 2;
/* 264 */       Object value = array[valueSlot];
/* 265 */       if (value == null) {
/* 266 */         if (id == this.constructorId) {
/* 267 */           initSlot(this.constructorId, "constructor", this.constructor, this.constructorAttrs);
/*     */           
/* 269 */           this.constructor = null;
/*     */         } else {
/* 271 */           this.obj.initPrototypeId(id);
/*     */         } 
/* 273 */         value = array[valueSlot];
/* 274 */         if (value == null) {
/* 275 */           throw new IllegalStateException(this.obj.getClass().getName() + ".initPrototypeId(int id) " + "did not initialize id=" + id);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 280 */       return value;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IdScriptableObject() {}
/*     */ 
/*     */   
/*     */   public IdScriptableObject(Scriptable scope, Scriptable prototype) {
/* 290 */     super(scope, prototype);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean defaultHas(String name) {
/* 295 */     return super.has(name, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Object defaultGet(String name) {
/* 300 */     return super.get(name, this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void defaultPut(String name, Object value) {
/* 305 */     super.put(name, this, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean has(String name, Scriptable start) {
/* 311 */     int info = findInstanceIdInfo(name);
/* 312 */     if (info != 0) {
/* 313 */       int attr = info >>> 16;
/* 314 */       if ((attr & 0x4) != 0) {
/* 315 */         return true;
/*     */       }
/* 317 */       int id = info & 0xFFFF;
/* 318 */       return (NOT_FOUND != getInstanceIdValue(id));
/*     */     } 
/* 320 */     if (this.prototypeValues != null) {
/* 321 */       int id = this.prototypeValues.findId(name);
/* 322 */       if (id != 0) {
/* 323 */         return this.prototypeValues.has(id);
/*     */       }
/*     */     } 
/* 326 */     return super.has(name, start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(String name, Scriptable start) {
/* 334 */     Object value = super.get(name, start);
/* 335 */     if (value != NOT_FOUND) {
/* 336 */       return value;
/*     */     }
/* 338 */     int info = findInstanceIdInfo(name);
/* 339 */     if (info != 0) {
/* 340 */       int id = info & 0xFFFF;
/* 341 */       value = getInstanceIdValue(id);
/* 342 */       if (value != NOT_FOUND) return value; 
/*     */     } 
/* 344 */     if (this.prototypeValues != null) {
/* 345 */       int id = this.prototypeValues.findId(name);
/* 346 */       if (id != 0) {
/* 347 */         value = this.prototypeValues.get(id);
/* 348 */         if (value != NOT_FOUND) return value; 
/*     */       } 
/*     */     } 
/* 351 */     return NOT_FOUND;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String name, Scriptable start, Object value) {
/* 357 */     int info = findInstanceIdInfo(name);
/* 358 */     if (info != 0) {
/* 359 */       if (start == this && isSealed()) {
/* 360 */         throw Context.reportRuntimeError1("msg.modify.sealed", name);
/*     */       }
/*     */       
/* 363 */       int attr = info >>> 16;
/* 364 */       if ((attr & 0x1) == 0) {
/* 365 */         if (start == this) {
/* 366 */           int id = info & 0xFFFF;
/* 367 */           setInstanceIdValue(id, value);
/*     */         } else {
/*     */           
/* 370 */           start.put(name, start, value);
/*     */         } 
/*     */       }
/*     */       return;
/*     */     } 
/* 375 */     if (this.prototypeValues != null) {
/* 376 */       int id = this.prototypeValues.findId(name);
/* 377 */       if (id != 0) {
/* 378 */         if (start == this && isSealed()) {
/* 379 */           throw Context.reportRuntimeError1("msg.modify.sealed", name);
/*     */         }
/*     */         
/* 382 */         this.prototypeValues.set(id, start, value);
/*     */         return;
/*     */       } 
/*     */     } 
/* 386 */     super.put(name, start, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(String name) {
/* 392 */     int info = findInstanceIdInfo(name);
/* 393 */     if (info != 0)
/*     */     {
/* 395 */       if (!isSealed()) {
/* 396 */         int attr = info >>> 16;
/* 397 */         if ((attr & 0x4) == 0) {
/* 398 */           int id = info & 0xFFFF;
/* 399 */           setInstanceIdValue(id, NOT_FOUND);
/*     */         } 
/*     */         return;
/*     */       } 
/*     */     }
/* 404 */     if (this.prototypeValues != null) {
/* 405 */       int id = this.prototypeValues.findId(name);
/* 406 */       if (id != 0) {
/* 407 */         if (!isSealed()) {
/* 408 */           this.prototypeValues.delete(id);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/* 413 */     super.delete(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAttributes(String name) {
/* 419 */     int info = findInstanceIdInfo(name);
/* 420 */     if (info != 0) {
/* 421 */       int attr = info >>> 16;
/* 422 */       return attr;
/*     */     } 
/* 424 */     if (this.prototypeValues != null) {
/* 425 */       int id = this.prototypeValues.findId(name);
/* 426 */       if (id != 0) {
/* 427 */         return this.prototypeValues.getAttributes(id);
/*     */       }
/*     */     } 
/* 430 */     return super.getAttributes(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttributes(String name, int attributes) {
/* 436 */     ScriptableObject.checkValidAttributes(attributes);
/* 437 */     int info = findInstanceIdInfo(name);
/* 438 */     if (info != 0) {
/* 439 */       int id = info & 0xFFFF;
/* 440 */       int currentAttributes = info >>> 16;
/* 441 */       if (attributes != currentAttributes) {
/* 442 */         setInstanceIdAttributes(id, attributes);
/*     */       }
/*     */       return;
/*     */     } 
/* 446 */     if (this.prototypeValues != null) {
/* 447 */       int id = this.prototypeValues.findId(name);
/* 448 */       if (id != 0) {
/* 449 */         this.prototypeValues.setAttributes(id, attributes);
/*     */         return;
/*     */       } 
/*     */     } 
/* 453 */     super.setAttributes(name, attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object[] getIds(boolean getAll) {
/* 459 */     Object[] result = super.getIds(getAll);
/*     */     
/* 461 */     if (this.prototypeValues != null) {
/* 462 */       result = this.prototypeValues.getNames(getAll, result);
/*     */     }
/*     */     
/* 465 */     int maxInstanceId = getMaxInstanceId();
/* 466 */     if (maxInstanceId != 0) {
/* 467 */       Object[] ids = null;
/* 468 */       int count = 0;
/*     */       
/* 470 */       for (int id = maxInstanceId; id != 0; id--) {
/* 471 */         String name = getInstanceIdName(id);
/* 472 */         int info = findInstanceIdInfo(name);
/* 473 */         if (info != 0) {
/* 474 */           int attr = info >>> 16;
/* 475 */           if ((attr & 0x4) != 0 || 
/* 476 */             NOT_FOUND != getInstanceIdValue(id))
/*     */           {
/*     */ 
/*     */             
/* 480 */             if (getAll || (attr & 0x2) == 0) {
/* 481 */               if (count == 0)
/*     */               {
/* 483 */                 ids = new Object[id];
/*     */               }
/* 485 */               ids[count++] = name;
/*     */             }  } 
/*     */         } 
/*     */       } 
/* 489 */       if (count != 0) {
/* 490 */         if (result.length == 0 && ids.length == count) {
/* 491 */           result = ids;
/*     */         } else {
/*     */           
/* 494 */           Object[] tmp = new Object[result.length + count];
/* 495 */           System.arraycopy(result, 0, tmp, 0, result.length);
/* 496 */           System.arraycopy(ids, 0, tmp, result.length, count);
/* 497 */           result = tmp;
/*     */         } 
/*     */       }
/*     */     } 
/* 501 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMaxInstanceId() {
/* 509 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static int instanceIdInfo(int attributes, int id) {
/* 514 */     return attributes << 16 | id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findInstanceIdInfo(String name) {
/* 524 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getInstanceIdName(int id) {
/* 531 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInstanceIdValue(int id) {
/* 542 */     throw new IllegalStateException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInstanceIdValue(int id, Object value) {
/* 551 */     throw new IllegalStateException(String.valueOf(id));
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
/*     */   protected void setInstanceIdAttributes(int id, int attr) {
/* 563 */     throw ScriptRuntime.constructError("InternalError", "Changing attributes not supported for " + getClassName() + " " + getInstanceIdName(id) + " property");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 573 */     throw f.unknown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IdFunctionObject exportAsJSClass(int maxPrototypeId, Scriptable scope, boolean sealed) {
/* 581 */     if (scope != this && scope != null) {
/* 582 */       setParentScope(scope);
/* 583 */       setPrototype(getObjectPrototype(scope));
/*     */     } 
/*     */     
/* 586 */     activatePrototypeMap(maxPrototypeId);
/* 587 */     IdFunctionObject ctor = this.prototypeValues.createPrecachedConstructor();
/* 588 */     if (sealed) {
/* 589 */       sealObject();
/*     */     }
/* 591 */     fillConstructorProperties(ctor);
/* 592 */     if (sealed) {
/* 593 */       ctor.sealObject();
/*     */     }
/* 595 */     ctor.exportAsScopeProperty();
/* 596 */     return ctor;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasPrototypeMap() {
/* 601 */     return (this.prototypeValues != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void activatePrototypeMap(int maxPrototypeId) {
/* 606 */     PrototypeValues values = new PrototypeValues(this, maxPrototypeId);
/* 607 */     synchronized (this) {
/* 608 */       if (this.prototypeValues != null)
/* 609 */         throw new IllegalStateException(); 
/* 610 */       this.prototypeValues = values;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void initPrototypeMethod(Object tag, int id, String name, int arity) {
/* 617 */     Scriptable scope = ScriptableObject.getTopLevelScope(this);
/* 618 */     IdFunctionObject f = newIdFunction(tag, id, name, arity, scope);
/* 619 */     this.prototypeValues.initValue(id, name, f, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void initPrototypeConstructor(IdFunctionObject f) {
/* 624 */     int id = this.prototypeValues.constructorId;
/* 625 */     if (id == 0)
/* 626 */       throw new IllegalStateException(); 
/* 627 */     if (f.methodId() != id)
/* 628 */       throw new IllegalArgumentException(); 
/* 629 */     if (isSealed()) f.sealObject(); 
/* 630 */     this.prototypeValues.initValue(id, "constructor", f, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void initPrototypeValue(int id, String name, Object value, int attributes) {
/* 636 */     this.prototypeValues.initValue(id, name, value, attributes);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/* 641 */     throw new IllegalStateException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String name) {
/* 646 */     throw new IllegalStateException(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fillConstructorProperties(IdFunctionObject ctor) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addIdFunctionProperty(Scriptable obj, Object tag, int id, String name, int arity) {
/* 656 */     Scriptable scope = ScriptableObject.getTopLevelScope(obj);
/* 657 */     IdFunctionObject f = newIdFunction(tag, id, name, arity, scope);
/* 658 */     f.addAsProperty(obj);
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
/*     */   protected static EcmaError incompatibleCallError(IdFunctionObject f) {
/* 684 */     throw ScriptRuntime.typeError1("msg.incompat.call", f.getFunctionName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IdFunctionObject newIdFunction(Object tag, int id, String name, int arity, Scriptable scope) {
/* 691 */     IdFunctionObject f = new IdFunctionObject(this, tag, id, name, arity, scope);
/*     */     
/* 693 */     if (isSealed()) f.sealObject(); 
/* 694 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   public void defineOwnProperty(Context cx, Object key, ScriptableObject desc) {
/* 699 */     if (key instanceof String) {
/* 700 */       String name = (String)key;
/* 701 */       int info = findInstanceIdInfo(name);
/* 702 */       if (info != 0) {
/* 703 */         int id = info & 0xFFFF;
/* 704 */         if (isAccessorDescriptor(desc)) {
/* 705 */           delete(id);
/*     */         } else {
/* 707 */           checkPropertyDefinition(desc);
/* 708 */           ScriptableObject current = getOwnPropertyDescriptor(cx, key);
/* 709 */           checkPropertyChange(name, current, desc);
/* 710 */           int attr = info >>> 16;
/* 711 */           Object value = getProperty(desc, "value");
/* 712 */           if (value != NOT_FOUND && (attr & 0x1) == 0) {
/* 713 */             Object currentValue = getInstanceIdValue(id);
/* 714 */             if (!sameValue(value, currentValue)) {
/* 715 */               setInstanceIdValue(id, value);
/*     */             }
/*     */           } 
/* 718 */           setAttributes(name, applyDescriptorToAttributeBitset(attr, desc));
/*     */           return;
/*     */         } 
/*     */       } 
/* 722 */       if (this.prototypeValues != null) {
/* 723 */         int id = this.prototypeValues.findId(name);
/* 724 */         if (id != 0) {
/* 725 */           if (isAccessorDescriptor(desc)) {
/* 726 */             this.prototypeValues.delete(id);
/*     */           } else {
/* 728 */             checkPropertyDefinition(desc);
/* 729 */             ScriptableObject current = getOwnPropertyDescriptor(cx, key);
/* 730 */             checkPropertyChange(name, current, desc);
/* 731 */             int attr = this.prototypeValues.getAttributes(id);
/* 732 */             Object value = getProperty(desc, "value");
/* 733 */             if (value != NOT_FOUND && (attr & 0x1) == 0) {
/* 734 */               Object currentValue = this.prototypeValues.get(id);
/* 735 */               if (!sameValue(value, currentValue)) {
/* 736 */                 this.prototypeValues.set(id, this, value);
/*     */               }
/*     */             } 
/* 739 */             this.prototypeValues.setAttributes(id, applyDescriptorToAttributeBitset(attr, desc));
/*     */             return;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 745 */     super.defineOwnProperty(cx, key, desc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ScriptableObject getOwnPropertyDescriptor(Context cx, Object id) {
/* 751 */     ScriptableObject desc = super.getOwnPropertyDescriptor(cx, id);
/* 752 */     if (desc == null && id instanceof String) {
/* 753 */       desc = getBuiltInDescriptor((String)id);
/*     */     }
/* 755 */     return desc;
/*     */   }
/*     */   
/*     */   private ScriptableObject getBuiltInDescriptor(String name) {
/* 759 */     Object value = null;
/* 760 */     int attr = 0;
/*     */     
/* 762 */     Scriptable scope = getParentScope();
/* 763 */     if (scope == null) {
/* 764 */       scope = this;
/*     */     }
/*     */     
/* 767 */     int info = findInstanceIdInfo(name);
/* 768 */     if (info != 0) {
/* 769 */       int id = info & 0xFFFF;
/* 770 */       value = getInstanceIdValue(id);
/* 771 */       attr = info >>> 16;
/* 772 */       return buildDataDescriptor(scope, value, attr);
/*     */     } 
/* 774 */     if (this.prototypeValues != null) {
/* 775 */       int id = this.prototypeValues.findId(name);
/* 776 */       if (id != 0) {
/* 777 */         value = this.prototypeValues.get(id);
/* 778 */         attr = this.prototypeValues.getAttributes(id);
/* 779 */         return buildDataDescriptor(scope, value, attr);
/*     */       } 
/*     */     } 
/* 782 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 788 */     stream.defaultReadObject();
/* 789 */     int maxPrototypeId = stream.readInt();
/* 790 */     if (maxPrototypeId != 0) {
/* 791 */       activatePrototypeMap(maxPrototypeId);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 798 */     stream.defaultWriteObject();
/* 799 */     int maxPrototypeId = 0;
/* 800 */     if (this.prototypeValues != null) {
/* 801 */       maxPrototypeId = this.prototypeValues.getMaxId();
/*     */     }
/* 803 */     stream.writeInt(maxPrototypeId);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\IdScriptableObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */