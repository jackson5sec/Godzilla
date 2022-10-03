/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NativeJavaObject
/*     */   implements Scriptable, Wrapper, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -6948590651130498591L;
/*     */   private static final int JSTYPE_UNDEFINED = 0;
/*     */   private static final int JSTYPE_NULL = 1;
/*     */   private static final int JSTYPE_BOOLEAN = 2;
/*     */   private static final int JSTYPE_NUMBER = 3;
/*     */   private static final int JSTYPE_STRING = 4;
/*     */   private static final int JSTYPE_JAVA_CLASS = 5;
/*     */   private static final int JSTYPE_JAVA_OBJECT = 6;
/*     */   private static final int JSTYPE_JAVA_ARRAY = 7;
/*     */   private static final int JSTYPE_OBJECT = 8;
/*     */   
/*     */   public NativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType) {
/*  34 */     this(scope, javaObject, staticType, false);
/*     */   }
/*     */   static final byte CONVERSION_TRIVIAL = 1; static final byte CONVERSION_NONTRIVIAL = 0; static final byte CONVERSION_NONE = 99; protected Scriptable prototype; protected Scriptable parent; protected transient Object javaObject; protected transient Class<?> staticType; protected transient JavaMembers members; private transient Map<String, FieldAndMethods> fieldAndMethods; protected transient boolean isAdapter;
/*     */   public NativeJavaObject() {}
/*     */   
/*     */   public NativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType, boolean isAdapter) {
/*  40 */     this.parent = scope;
/*  41 */     this.javaObject = javaObject;
/*  42 */     this.staticType = staticType;
/*  43 */     this.isAdapter = isAdapter;
/*  44 */     initMembers();
/*     */   }
/*     */   
/*     */   protected void initMembers() {
/*     */     Class<?> dynamicType;
/*  49 */     if (this.javaObject != null) {
/*  50 */       dynamicType = this.javaObject.getClass();
/*     */     } else {
/*  52 */       dynamicType = this.staticType;
/*     */     } 
/*  54 */     this.members = JavaMembers.lookupClass(this.parent, dynamicType, this.staticType, this.isAdapter);
/*     */     
/*  56 */     this.fieldAndMethods = this.members.getFieldAndMethodsObjects(this, this.javaObject, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(String name, Scriptable start) {
/*  61 */     return this.members.has(name, false);
/*     */   }
/*     */   
/*     */   public boolean has(int index, Scriptable start) {
/*  65 */     return false;
/*     */   }
/*     */   
/*     */   public Object get(String name, Scriptable start) {
/*  69 */     if (this.fieldAndMethods != null) {
/*  70 */       Object result = this.fieldAndMethods.get(name);
/*  71 */       if (result != null) {
/*  72 */         return result;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  77 */     return this.members.get(this, name, this.javaObject, false);
/*     */   }
/*     */   
/*     */   public Object get(int index, Scriptable start) {
/*  81 */     throw this.members.reportMemberNotFound(Integer.toString(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String name, Scriptable start, Object value) {
/*  88 */     if (this.prototype == null || this.members.has(name, false)) {
/*  89 */       this.members.put(this, name, this.javaObject, value, false);
/*     */     } else {
/*  91 */       this.prototype.put(name, this.prototype, value);
/*     */     } 
/*     */   }
/*     */   public void put(int index, Scriptable start, Object value) {
/*  95 */     throw this.members.reportMemberNotFound(Integer.toString(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasInstance(Scriptable value) {
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(String name) {}
/*     */ 
/*     */   
/*     */   public void delete(int index) {}
/*     */   
/*     */   public Scriptable getPrototype() {
/* 110 */     if (this.prototype == null && this.javaObject instanceof String) {
/* 111 */       return TopLevel.getBuiltinPrototype(ScriptableObject.getTopLevelScope(this.parent), TopLevel.Builtins.String);
/*     */     }
/*     */ 
/*     */     
/* 115 */     return this.prototype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrototype(Scriptable m) {
/* 122 */     this.prototype = m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable getParentScope() {
/* 129 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentScope(Scriptable m) {
/* 136 */     this.parent = m;
/*     */   }
/*     */   
/*     */   public Object[] getIds() {
/* 140 */     return this.members.getIds(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Object wrap(Scriptable scope, Object obj, Class<?> staticType) {
/* 150 */     Context cx = Context.getContext();
/* 151 */     return cx.getWrapFactory().wrap(cx, scope, obj, staticType);
/*     */   }
/*     */   
/*     */   public Object unwrap() {
/* 155 */     return this.javaObject;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/* 159 */     return "JavaObject";
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getDefaultValue(Class<?> hint) {
/*     */     Object value;
/* 165 */     if (hint == null && 
/* 166 */       this.javaObject instanceof Boolean) {
/* 167 */       hint = ScriptRuntime.BooleanClass;
/*     */     }
/*     */     
/* 170 */     if (hint == null || hint == ScriptRuntime.StringClass) {
/* 171 */       value = this.javaObject.toString();
/*     */     } else {
/*     */       String converterName;
/* 174 */       if (hint == ScriptRuntime.BooleanClass) {
/* 175 */         converterName = "booleanValue";
/* 176 */       } else if (hint == ScriptRuntime.NumberClass) {
/* 177 */         converterName = "doubleValue";
/*     */       } else {
/* 179 */         throw Context.reportRuntimeError0("msg.default.value");
/*     */       } 
/* 181 */       Object converterObject = get(converterName, this);
/* 182 */       if (converterObject instanceof Function) {
/* 183 */         Function f = (Function)converterObject;
/* 184 */         value = f.call(Context.getContext(), f.getParentScope(), this, ScriptRuntime.emptyArgs);
/*     */       
/*     */       }
/* 187 */       else if (hint == ScriptRuntime.NumberClass && this.javaObject instanceof Boolean) {
/*     */ 
/*     */         
/* 190 */         boolean b = ((Boolean)this.javaObject).booleanValue();
/* 191 */         value = ScriptRuntime.wrapNumber(b ? 1.0D : 0.0D);
/*     */       } else {
/* 193 */         value = this.javaObject.toString();
/*     */       } 
/*     */     } 
/*     */     
/* 197 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canConvert(Object fromObj, Class<?> to) {
/* 206 */     int weight = getConversionWeight(fromObj, to);
/*     */     
/* 208 */     return (weight < 99);
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
/*     */   static int getConversionWeight(Object fromObj, Class<?> to) {
/*     */     Object javaObj;
/* 235 */     int fromCode = getJSTypeCode(fromObj);
/*     */     
/* 237 */     switch (fromCode) {
/*     */       
/*     */       case 0:
/* 240 */         if (to == ScriptRuntime.StringClass || to == ScriptRuntime.ObjectClass)
/*     */         {
/* 242 */           return 1;
/*     */         }
/*     */         break;
/*     */       
/*     */       case 1:
/* 247 */         if (!to.isPrimitive()) {
/* 248 */           return 1;
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 254 */         if (to == boolean.class) {
/* 255 */           return 1;
/*     */         }
/* 257 */         if (to == ScriptRuntime.BooleanClass) {
/* 258 */           return 2;
/*     */         }
/* 260 */         if (to == ScriptRuntime.ObjectClass) {
/* 261 */           return 3;
/*     */         }
/* 263 */         if (to == ScriptRuntime.StringClass) {
/* 264 */           return 4;
/*     */         }
/*     */         break;
/*     */       
/*     */       case 3:
/* 269 */         if (to.isPrimitive()) {
/* 270 */           if (to == double.class) {
/* 271 */             return 1;
/*     */           }
/* 273 */           if (to != boolean.class) {
/* 274 */             return 1 + getSizeRank(to);
/*     */           }
/*     */           break;
/*     */         } 
/* 278 */         if (to == ScriptRuntime.StringClass)
/*     */         {
/* 280 */           return 9;
/*     */         }
/* 282 */         if (to == ScriptRuntime.ObjectClass) {
/* 283 */           return 10;
/*     */         }
/* 285 */         if (ScriptRuntime.NumberClass.isAssignableFrom(to))
/*     */         {
/* 287 */           return 2;
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 293 */         if (to == ScriptRuntime.StringClass) {
/* 294 */           return 1;
/*     */         }
/* 296 */         if (to.isInstance(fromObj)) {
/* 297 */           return 2;
/*     */         }
/* 299 */         if (to.isPrimitive()) {
/* 300 */           if (to == char.class)
/* 301 */             return 3; 
/* 302 */           if (to != boolean.class) {
/* 303 */             return 4;
/*     */           }
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 5:
/* 309 */         if (to == ScriptRuntime.ClassClass) {
/* 310 */           return 1;
/*     */         }
/* 312 */         if (to == ScriptRuntime.ObjectClass) {
/* 313 */           return 3;
/*     */         }
/* 315 */         if (to == ScriptRuntime.StringClass) {
/* 316 */           return 4;
/*     */         }
/*     */         break;
/*     */       
/*     */       case 6:
/*     */       case 7:
/* 322 */         javaObj = fromObj;
/* 323 */         if (javaObj instanceof Wrapper) {
/* 324 */           javaObj = ((Wrapper)javaObj).unwrap();
/*     */         }
/* 326 */         if (to.isInstance(javaObj)) {
/* 327 */           return 0;
/*     */         }
/* 329 */         if (to == ScriptRuntime.StringClass) {
/* 330 */           return 2;
/*     */         }
/* 332 */         if (to.isPrimitive() && to != boolean.class) {
/* 333 */           return (fromCode == 7) ? 99 : (2 + getSizeRank(to));
/*     */         }
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 340 */         if (to != ScriptRuntime.ObjectClass && to.isInstance(fromObj))
/*     */         {
/* 342 */           return 1;
/*     */         }
/* 344 */         if (to.isArray()) {
/* 345 */           if (fromObj instanceof NativeArray)
/*     */           {
/*     */ 
/*     */             
/* 349 */             return 2; } 
/*     */           break;
/*     */         } 
/* 352 */         if (to == ScriptRuntime.ObjectClass) {
/* 353 */           return 3;
/*     */         }
/* 355 */         if (to == ScriptRuntime.StringClass) {
/* 356 */           return 4;
/*     */         }
/* 358 */         if (to == ScriptRuntime.DateClass) {
/* 359 */           if (fromObj instanceof NativeDate)
/*     */           {
/* 361 */             return 1; } 
/*     */           break;
/*     */         } 
/* 364 */         if (to.isInterface()) {
/* 365 */           if (fromObj instanceof NativeObject || fromObj instanceof NativeFunction)
/*     */           {
/* 367 */             return 1;
/*     */           }
/* 369 */           return 12;
/*     */         } 
/* 371 */         if (to.isPrimitive() && to != boolean.class) {
/* 372 */           return 4 + getSizeRank(to);
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 377 */     return 99;
/*     */   }
/*     */   
/*     */   static int getSizeRank(Class<?> aType) {
/* 381 */     if (aType == double.class) {
/* 382 */       return 1;
/*     */     }
/* 384 */     if (aType == float.class) {
/* 385 */       return 2;
/*     */     }
/* 387 */     if (aType == long.class) {
/* 388 */       return 3;
/*     */     }
/* 390 */     if (aType == int.class) {
/* 391 */       return 4;
/*     */     }
/* 393 */     if (aType == short.class) {
/* 394 */       return 5;
/*     */     }
/* 396 */     if (aType == char.class) {
/* 397 */       return 6;
/*     */     }
/* 399 */     if (aType == byte.class) {
/* 400 */       return 7;
/*     */     }
/* 402 */     if (aType == boolean.class) {
/* 403 */       return 99;
/*     */     }
/*     */     
/* 406 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getJSTypeCode(Object value) {
/* 411 */     if (value == null) {
/* 412 */       return 1;
/*     */     }
/* 414 */     if (value == Undefined.instance) {
/* 415 */       return 0;
/*     */     }
/* 417 */     if (value instanceof CharSequence) {
/* 418 */       return 4;
/*     */     }
/* 420 */     if (value instanceof Number) {
/* 421 */       return 3;
/*     */     }
/* 423 */     if (value instanceof Boolean) {
/* 424 */       return 2;
/*     */     }
/* 426 */     if (value instanceof Scriptable) {
/* 427 */       if (value instanceof NativeJavaClass) {
/* 428 */         return 5;
/*     */       }
/* 430 */       if (value instanceof NativeJavaArray) {
/* 431 */         return 7;
/*     */       }
/* 433 */       if (value instanceof Wrapper) {
/* 434 */         return 6;
/*     */       }
/*     */       
/* 437 */       return 8;
/*     */     } 
/*     */     
/* 440 */     if (value instanceof Class) {
/* 441 */       return 5;
/*     */     }
/*     */     
/* 444 */     Class<?> valueClass = value.getClass();
/* 445 */     if (valueClass.isArray()) {
/* 446 */       return 7;
/*     */     }
/*     */     
/* 449 */     return 6;
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
/*     */   @Deprecated
/*     */   public static Object coerceType(Class<?> type, Object value) {
/* 463 */     return coerceTypeImpl(type, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object coerceTypeImpl(Class<?> type, Object value) {
/* 472 */     if (value != null && value.getClass() == type) {
/* 473 */       return value;
/*     */     }
/*     */     
/* 476 */     switch (getJSTypeCode(value)) {
/*     */ 
/*     */       
/*     */       case 1:
/* 480 */         if (type.isPrimitive()) {
/* 481 */           reportConversionError(value, type);
/*     */         }
/* 483 */         return null;
/*     */       
/*     */       case 0:
/* 486 */         if (type == ScriptRuntime.StringClass || type == ScriptRuntime.ObjectClass)
/*     */         {
/* 488 */           return "undefined";
/*     */         }
/*     */         
/* 491 */         reportConversionError("undefined", type);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 497 */         if (type == boolean.class || type == ScriptRuntime.BooleanClass || type == ScriptRuntime.ObjectClass)
/*     */         {
/*     */           
/* 500 */           return value;
/*     */         }
/* 502 */         if (type == ScriptRuntime.StringClass) {
/* 503 */           return value.toString();
/*     */         }
/*     */         
/* 506 */         reportConversionError(value, type);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 511 */         if (type == ScriptRuntime.StringClass) {
/* 512 */           return ScriptRuntime.toString(value);
/*     */         }
/* 514 */         if (type == ScriptRuntime.ObjectClass) {
/* 515 */           return coerceToNumber(double.class, value);
/*     */         }
/* 517 */         if ((type.isPrimitive() && type != boolean.class) || ScriptRuntime.NumberClass.isAssignableFrom(type))
/*     */         {
/* 519 */           return coerceToNumber(type, value);
/*     */         }
/*     */         
/* 522 */         reportConversionError(value, type);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 527 */         if (type == ScriptRuntime.StringClass || type.isInstance(value)) {
/* 528 */           return value.toString();
/*     */         }
/* 530 */         if (type == char.class || type == ScriptRuntime.CharacterClass) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 537 */           if (((CharSequence)value).length() == 1) {
/* 538 */             return Character.valueOf(((CharSequence)value).charAt(0));
/*     */           }
/*     */           
/* 541 */           return coerceToNumber(type, value);
/*     */         } 
/*     */         
/* 544 */         if ((type.isPrimitive() && type != boolean.class) || ScriptRuntime.NumberClass.isAssignableFrom(type))
/*     */         {
/*     */           
/* 547 */           return coerceToNumber(type, value);
/*     */         }
/*     */         
/* 550 */         reportConversionError(value, type);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 555 */         if (value instanceof Wrapper) {
/* 556 */           value = ((Wrapper)value).unwrap();
/*     */         }
/*     */         
/* 559 */         if (type == ScriptRuntime.ClassClass || type == ScriptRuntime.ObjectClass)
/*     */         {
/* 561 */           return value;
/*     */         }
/* 563 */         if (type == ScriptRuntime.StringClass) {
/* 564 */           return value.toString();
/*     */         }
/*     */         
/* 567 */         reportConversionError(value, type);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/*     */       case 7:
/* 573 */         if (value instanceof Wrapper) {
/* 574 */           value = ((Wrapper)value).unwrap();
/*     */         }
/* 576 */         if (type.isPrimitive()) {
/* 577 */           if (type == boolean.class) {
/* 578 */             reportConversionError(value, type);
/*     */           }
/* 580 */           return coerceToNumber(type, value);
/*     */         } 
/*     */         
/* 583 */         if (type == ScriptRuntime.StringClass) {
/* 584 */           return value.toString();
/*     */         }
/*     */         
/* 587 */         if (type.isInstance(value)) {
/* 588 */           return value;
/*     */         }
/*     */         
/* 591 */         reportConversionError(value, type);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 598 */         if (type == ScriptRuntime.StringClass) {
/* 599 */           return ScriptRuntime.toString(value);
/*     */         }
/* 601 */         if (type.isPrimitive()) {
/* 602 */           if (type == boolean.class) {
/* 603 */             reportConversionError(value, type);
/*     */           }
/* 605 */           return coerceToNumber(type, value);
/*     */         } 
/* 607 */         if (type.isInstance(value)) {
/* 608 */           return value;
/*     */         }
/* 610 */         if (type == ScriptRuntime.DateClass && value instanceof NativeDate) {
/*     */ 
/*     */           
/* 613 */           double time = ((NativeDate)value).getJSTimeValue();
/*     */           
/* 615 */           return new Date((long)time);
/*     */         } 
/* 617 */         if (type.isArray() && value instanceof NativeArray) {
/*     */ 
/*     */           
/* 620 */           NativeArray array = (NativeArray)value;
/* 621 */           long length = array.getLength();
/* 622 */           Class<?> arrayType = type.getComponentType();
/* 623 */           Object Result = Array.newInstance(arrayType, (int)length);
/* 624 */           for (int i = 0; i < length; i++) {
/*     */             try {
/* 626 */               Array.set(Result, i, coerceTypeImpl(arrayType, array.get(i, array)));
/*     */             
/*     */             }
/* 629 */             catch (EvaluatorException ee) {
/* 630 */               reportConversionError(value, type);
/*     */             } 
/*     */           } 
/*     */           
/* 634 */           return Result;
/*     */         } 
/* 636 */         if (value instanceof Wrapper) {
/* 637 */           value = ((Wrapper)value).unwrap();
/* 638 */           if (type.isInstance(value))
/* 639 */             return value; 
/* 640 */           reportConversionError(value, type); break;
/*     */         } 
/* 642 */         if (type.isInterface() && (value instanceof NativeObject || value instanceof NativeFunction))
/*     */         {
/*     */           
/* 645 */           return createInterfaceAdapter(type, (ScriptableObject)value);
/*     */         }
/* 647 */         reportConversionError(value, type);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 652 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Object createInterfaceAdapter(Class<?> type, ScriptableObject so) {
/* 662 */     Object key = Kit.makeHashKeyFromPair(COERCED_INTERFACE_KEY, type);
/* 663 */     Object old = so.getAssociatedValue(key);
/* 664 */     if (old != null)
/*     */     {
/* 666 */       return old;
/*     */     }
/* 668 */     Context cx = Context.getContext();
/* 669 */     Object glue = InterfaceAdapter.create(cx, type, so);
/*     */     
/* 671 */     glue = so.associateValue(key, glue);
/* 672 */     return glue;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object coerceToNumber(Class<?> type, Object value) {
/* 677 */     Class<?> valueClass = value.getClass();
/*     */ 
/*     */     
/* 680 */     if (type == char.class || type == ScriptRuntime.CharacterClass) {
/* 681 */       if (valueClass == ScriptRuntime.CharacterClass) {
/* 682 */         return value;
/*     */       }
/* 684 */       return Character.valueOf((char)(int)toInteger(value, ScriptRuntime.CharacterClass, 0.0D, 65535.0D));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 691 */     if (type == ScriptRuntime.ObjectClass || type == ScriptRuntime.DoubleClass || type == double.class)
/*     */     {
/* 693 */       return (valueClass == ScriptRuntime.DoubleClass) ? value : new Double(toDouble(value));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 698 */     if (type == ScriptRuntime.FloatClass || type == float.class) {
/* 699 */       if (valueClass == ScriptRuntime.FloatClass) {
/* 700 */         return value;
/*     */       }
/*     */       
/* 703 */       double number = toDouble(value);
/* 704 */       if (Double.isInfinite(number) || Double.isNaN(number) || number == 0.0D)
/*     */       {
/* 706 */         return new Float((float)number);
/*     */       }
/*     */       
/* 709 */       double absNumber = Math.abs(number);
/* 710 */       if (absNumber < 1.401298464324817E-45D) {
/* 711 */         return new Float((number > 0.0D) ? 0.0D : -0.0D);
/*     */       }
/* 713 */       if (absNumber > 3.4028234663852886E38D) {
/* 714 */         return new Float((number > 0.0D) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 719 */       return new Float((float)number);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 726 */     if (type == ScriptRuntime.IntegerClass || type == int.class) {
/* 727 */       if (valueClass == ScriptRuntime.IntegerClass) {
/* 728 */         return value;
/*     */       }
/*     */       
/* 731 */       return Integer.valueOf((int)toInteger(value, ScriptRuntime.IntegerClass, -2.147483648E9D, 2.147483647E9D));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 738 */     if (type == ScriptRuntime.LongClass || type == long.class) {
/* 739 */       if (valueClass == ScriptRuntime.LongClass) {
/* 740 */         return value;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 749 */       double max = Double.longBitsToDouble(4890909195324358655L);
/* 750 */       double min = Double.longBitsToDouble(-4332462841530417152L);
/* 751 */       return Long.valueOf(toInteger(value, ScriptRuntime.LongClass, min, max));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 758 */     if (type == ScriptRuntime.ShortClass || type == short.class) {
/* 759 */       if (valueClass == ScriptRuntime.ShortClass) {
/* 760 */         return value;
/*     */       }
/*     */       
/* 763 */       return Short.valueOf((short)(int)toInteger(value, ScriptRuntime.ShortClass, -32768.0D, 32767.0D));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 770 */     if (type == ScriptRuntime.ByteClass || type == byte.class) {
/* 771 */       if (valueClass == ScriptRuntime.ByteClass) {
/* 772 */         return value;
/*     */       }
/*     */       
/* 775 */       return Byte.valueOf((byte)(int)toInteger(value, ScriptRuntime.ByteClass, -128.0D, 127.0D));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 782 */     return new Double(toDouble(value));
/*     */   }
/*     */ 
/*     */   
/*     */   private static double toDouble(Object value) {
/*     */     Method method;
/* 788 */     if (value instanceof Number) {
/* 789 */       return ((Number)value).doubleValue();
/*     */     }
/* 791 */     if (value instanceof String) {
/* 792 */       return ScriptRuntime.toNumber((String)value);
/*     */     }
/* 794 */     if (value instanceof Scriptable) {
/* 795 */       if (value instanceof Wrapper)
/*     */       {
/* 797 */         return toDouble(((Wrapper)value).unwrap());
/*     */       }
/*     */       
/* 800 */       return ScriptRuntime.toNumber(value);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 806 */       method = value.getClass().getMethod("doubleValue", (Class[])null);
/*     */     
/*     */     }
/* 809 */     catch (NoSuchMethodException e) {
/* 810 */       method = null;
/*     */     }
/* 812 */     catch (SecurityException e) {
/* 813 */       method = null;
/*     */     } 
/* 815 */     if (method != null) {
/*     */       try {
/* 817 */         return ((Number)method.invoke(value, (Object[])null)).doubleValue();
/*     */       
/*     */       }
/* 820 */       catch (IllegalAccessException e) {
/*     */         
/* 822 */         reportConversionError(value, double.class);
/*     */       }
/* 824 */       catch (InvocationTargetException e) {
/*     */         
/* 826 */         reportConversionError(value, double.class);
/*     */       } 
/*     */     }
/* 829 */     return ScriptRuntime.toNumber(value.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long toInteger(Object value, Class<?> type, double min, double max) {
/* 836 */     double d = toDouble(value);
/*     */     
/* 838 */     if (Double.isInfinite(d) || Double.isNaN(d))
/*     */     {
/* 840 */       reportConversionError(ScriptRuntime.toString(value), type);
/*     */     }
/*     */     
/* 843 */     if (d > 0.0D) {
/* 844 */       d = Math.floor(d);
/*     */     } else {
/*     */       
/* 847 */       d = Math.ceil(d);
/*     */     } 
/*     */     
/* 850 */     if (d < min || d > max)
/*     */     {
/* 852 */       reportConversionError(ScriptRuntime.toString(value), type);
/*     */     }
/* 854 */     return (long)d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void reportConversionError(Object value, Class<?> type) {
/* 861 */     throw Context.reportRuntimeError2("msg.conversion.not.allowed", String.valueOf(value), JavaMembers.javaSignature(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 870 */     out.defaultWriteObject();
/*     */     
/* 872 */     out.writeBoolean(this.isAdapter);
/* 873 */     if (this.isAdapter) {
/* 874 */       if (adapter_writeAdapterObject == null) {
/* 875 */         throw new IOException();
/*     */       }
/* 877 */       Object[] args = { this.javaObject, out };
/*     */       try {
/* 879 */         adapter_writeAdapterObject.invoke(null, args);
/* 880 */       } catch (Exception ex) {
/* 881 */         throw new IOException();
/*     */       } 
/*     */     } else {
/* 884 */       out.writeObject(this.javaObject);
/*     */     } 
/*     */     
/* 887 */     if (this.staticType != null) {
/* 888 */       out.writeObject(this.staticType.getClass().getName());
/*     */     } else {
/* 890 */       out.writeObject(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 897 */     in.defaultReadObject();
/*     */     
/* 899 */     this.isAdapter = in.readBoolean();
/* 900 */     if (this.isAdapter) {
/* 901 */       if (adapter_readAdapterObject == null)
/* 902 */         throw new ClassNotFoundException(); 
/* 903 */       Object[] args = { this, in };
/*     */       try {
/* 905 */         this.javaObject = adapter_readAdapterObject.invoke(null, args);
/* 906 */       } catch (Exception ex) {
/* 907 */         throw new IOException();
/*     */       } 
/*     */     } else {
/* 910 */       this.javaObject = in.readObject();
/*     */     } 
/*     */     
/* 913 */     String className = (String)in.readObject();
/* 914 */     if (className != null) {
/* 915 */       this.staticType = Class.forName(className);
/*     */     } else {
/* 917 */       this.staticType = null;
/*     */     } 
/*     */     
/* 920 */     initMembers();
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
/* 940 */   private static final Object COERCED_INTERFACE_KEY = "Coerced Interface";
/*     */   
/*     */   private static Method adapter_writeAdapterObject;
/*     */   private static Method adapter_readAdapterObject;
/*     */   
/*     */   static {
/* 946 */     Class<?>[] sig2 = new Class[2];
/* 947 */     Class<?> cl = Kit.classOrNull("org.mozilla.javascript.JavaAdapter");
/* 948 */     if (cl != null)
/*     */       try {
/* 950 */         sig2[0] = ScriptRuntime.ObjectClass;
/* 951 */         sig2[1] = Kit.classOrNull("java.io.ObjectOutputStream");
/* 952 */         adapter_writeAdapterObject = cl.getMethod("writeAdapterObject", sig2);
/*     */ 
/*     */         
/* 955 */         sig2[0] = ScriptRuntime.ScriptableClass;
/* 956 */         sig2[1] = Kit.classOrNull("java.io.ObjectInputStream");
/* 957 */         adapter_readAdapterObject = cl.getMethod("readAdapterObject", sig2);
/*     */       
/*     */       }
/* 960 */       catch (NoSuchMethodException e) {
/* 961 */         adapter_writeAdapterObject = null;
/* 962 */         adapter_readAdapterObject = null;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeJavaObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */