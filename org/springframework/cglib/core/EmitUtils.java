/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.internal.CustomizerRegistry;
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
/*     */ public class EmitUtils
/*     */ {
/*  28 */   private static final Signature CSTRUCT_NULL = TypeUtils.parseConstructor("");
/*     */   
/*  30 */   private static final Signature CSTRUCT_THROWABLE = TypeUtils.parseConstructor("Throwable");
/*     */ 
/*     */   
/*  33 */   private static final Signature GET_NAME = TypeUtils.parseSignature("String getName()");
/*     */   
/*  35 */   private static final Signature HASH_CODE = TypeUtils.parseSignature("int hashCode()");
/*     */   
/*  37 */   private static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(Object)");
/*     */   
/*  39 */   private static final Signature STRING_LENGTH = TypeUtils.parseSignature("int length()");
/*     */   
/*  41 */   private static final Signature STRING_CHAR_AT = TypeUtils.parseSignature("char charAt(int)");
/*     */   
/*  43 */   private static final Signature FOR_NAME = TypeUtils.parseSignature("Class forName(String)");
/*     */   
/*  45 */   private static final Signature DOUBLE_TO_LONG_BITS = TypeUtils.parseSignature("long doubleToLongBits(double)");
/*     */   
/*  47 */   private static final Signature FLOAT_TO_INT_BITS = TypeUtils.parseSignature("int floatToIntBits(float)");
/*     */   
/*  49 */   private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
/*     */   
/*  51 */   private static final Signature APPEND_STRING = TypeUtils.parseSignature("StringBuffer append(String)");
/*     */   
/*  53 */   private static final Signature APPEND_INT = TypeUtils.parseSignature("StringBuffer append(int)");
/*     */   
/*  55 */   private static final Signature APPEND_DOUBLE = TypeUtils.parseSignature("StringBuffer append(double)");
/*     */   
/*  57 */   private static final Signature APPEND_FLOAT = TypeUtils.parseSignature("StringBuffer append(float)");
/*     */   
/*  59 */   private static final Signature APPEND_CHAR = TypeUtils.parseSignature("StringBuffer append(char)");
/*     */   
/*  61 */   private static final Signature APPEND_LONG = TypeUtils.parseSignature("StringBuffer append(long)");
/*     */   
/*  63 */   private static final Signature APPEND_BOOLEAN = TypeUtils.parseSignature("StringBuffer append(boolean)");
/*     */   
/*  65 */   private static final Signature LENGTH = TypeUtils.parseSignature("int length()");
/*     */   
/*  67 */   private static final Signature SET_LENGTH = TypeUtils.parseSignature("void setLength(int)");
/*     */   
/*  69 */   private static final Signature GET_DECLARED_METHOD = TypeUtils.parseSignature("java.lang.reflect.Method getDeclaredMethod(String, Class[])");
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static final ArrayDelimiters DEFAULT_DELIMITERS = new ArrayDelimiters("{", ", ", "}");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void factory_method(ClassEmitter ce, Signature sig) {
/*  79 */     CodeEmitter e = ce.begin_method(1, sig, null);
/*  80 */     e.new_instance_this();
/*  81 */     e.dup();
/*  82 */     e.load_args();
/*  83 */     e.invoke_constructor_this(TypeUtils.parseConstructor(sig.getArgumentTypes()));
/*  84 */     e.return_value();
/*  85 */     e.end_method();
/*     */   }
/*     */   
/*     */   public static void null_constructor(ClassEmitter ce) {
/*  89 */     CodeEmitter e = ce.begin_method(1, CSTRUCT_NULL, null);
/*  90 */     e.load_this();
/*  91 */     e.super_invoke_constructor();
/*  92 */     e.return_value();
/*  93 */     e.end_method();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void process_array(CodeEmitter e, Type type, ProcessArrayCallback callback) {
/* 104 */     Type componentType = TypeUtils.getComponentType(type);
/* 105 */     Local array = e.make_local();
/* 106 */     Local loopvar = e.make_local(Type.INT_TYPE);
/* 107 */     Label loopbody = e.make_label();
/* 108 */     Label checkloop = e.make_label();
/* 109 */     e.store_local(array);
/* 110 */     e.push(0);
/* 111 */     e.store_local(loopvar);
/* 112 */     e.goTo(checkloop);
/*     */     
/* 114 */     e.mark(loopbody);
/* 115 */     e.load_local(array);
/* 116 */     e.load_local(loopvar);
/* 117 */     e.array_load(componentType);
/* 118 */     callback.processElement(componentType);
/* 119 */     e.iinc(loopvar, 1);
/*     */     
/* 121 */     e.mark(checkloop);
/* 122 */     e.load_local(loopvar);
/* 123 */     e.load_local(array);
/* 124 */     e.arraylength();
/* 125 */     e.if_icmp(155, loopbody);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void process_arrays(CodeEmitter e, Type type, ProcessArrayCallback callback) {
/* 136 */     Type componentType = TypeUtils.getComponentType(type);
/* 137 */     Local array1 = e.make_local();
/* 138 */     Local array2 = e.make_local();
/* 139 */     Local loopvar = e.make_local(Type.INT_TYPE);
/* 140 */     Label loopbody = e.make_label();
/* 141 */     Label checkloop = e.make_label();
/* 142 */     e.store_local(array1);
/* 143 */     e.store_local(array2);
/* 144 */     e.push(0);
/* 145 */     e.store_local(loopvar);
/* 146 */     e.goTo(checkloop);
/*     */     
/* 148 */     e.mark(loopbody);
/* 149 */     e.load_local(array1);
/* 150 */     e.load_local(loopvar);
/* 151 */     e.array_load(componentType);
/* 152 */     e.load_local(array2);
/* 153 */     e.load_local(loopvar);
/* 154 */     e.array_load(componentType);
/* 155 */     callback.processElement(componentType);
/* 156 */     e.iinc(loopvar, 1);
/*     */     
/* 158 */     e.mark(checkloop);
/* 159 */     e.load_local(loopvar);
/* 160 */     e.load_local(array1);
/* 161 */     e.arraylength();
/* 162 */     e.if_icmp(155, loopbody);
/*     */   }
/*     */   
/*     */   public static void string_switch(CodeEmitter e, String[] strings, int switchStyle, ObjectSwitchCallback callback) {
/*     */     try {
/* 167 */       switch (switchStyle) {
/*     */         case 0:
/* 169 */           string_switch_trie(e, strings, callback);
/*     */           return;
/*     */         case 1:
/* 172 */           string_switch_hash(e, strings, callback, false);
/*     */           return;
/*     */         case 2:
/* 175 */           string_switch_hash(e, strings, callback, true);
/*     */           return;
/*     */       } 
/* 178 */       throw new IllegalArgumentException("unknown switch style " + switchStyle);
/*     */     }
/* 180 */     catch (RuntimeException ex) {
/* 181 */       throw ex;
/* 182 */     } catch (Error ex) {
/* 183 */       throw ex;
/* 184 */     } catch (Exception ex) {
/* 185 */       throw new CodeGenerationException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void string_switch_trie(final CodeEmitter e, String[] strings, final ObjectSwitchCallback callback) throws Exception {
/* 192 */     final Label def = e.make_label();
/* 193 */     final Label end = e.make_label();
/* 194 */     final Map buckets = CollectionUtils.bucket(Arrays.asList(strings), new Transformer() {
/*     */           public Object transform(Object value) {
/* 196 */             return new Integer(((String)value).length());
/*     */           }
/*     */         });
/* 199 */     e.dup();
/* 200 */     e.invoke_virtual(Constants.TYPE_STRING, STRING_LENGTH);
/* 201 */     e.process_switch(getSwitchKeys(buckets), new ProcessSwitchCallback() {
/*     */           public void processCase(int key, Label ignore_end) throws Exception {
/* 203 */             List bucket = (List)buckets.get(new Integer(key));
/* 204 */             EmitUtils.stringSwitchHelper(e, bucket, callback, def, end, 0);
/*     */           }
/*     */           public void processDefault() {
/* 207 */             e.goTo(def);
/*     */           }
/*     */         });
/* 210 */     e.mark(def);
/* 211 */     e.pop();
/* 212 */     callback.processDefault();
/* 213 */     e.mark(end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void stringSwitchHelper(final CodeEmitter e, List<String> strings, final ObjectSwitchCallback callback, final Label def, final Label end, final int index) throws Exception {
/* 222 */     final int len = ((String)strings.get(0)).length();
/* 223 */     final Map buckets = CollectionUtils.bucket(strings, new Transformer() {
/*     */           public Object transform(Object value) {
/* 225 */             return new Integer(((String)value).charAt(index));
/*     */           }
/*     */         });
/* 228 */     e.dup();
/* 229 */     e.push(index);
/* 230 */     e.invoke_virtual(Constants.TYPE_STRING, STRING_CHAR_AT);
/* 231 */     e.process_switch(getSwitchKeys(buckets), new ProcessSwitchCallback() {
/*     */           public void processCase(int key, Label ignore_end) throws Exception {
/* 233 */             List bucket = (List)buckets.get(new Integer(key));
/* 234 */             if (index + 1 == len) {
/* 235 */               e.pop();
/* 236 */               callback.processCase(bucket.get(0), end);
/*     */             } else {
/* 238 */               EmitUtils.stringSwitchHelper(e, bucket, callback, def, end, index + 1);
/*     */             } 
/*     */           }
/*     */           public void processDefault() {
/* 242 */             e.goTo(def);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   static int[] getSwitchKeys(Map buckets) {
/* 248 */     int[] keys = new int[buckets.size()];
/* 249 */     int index = 0;
/* 250 */     for (Iterator it = buckets.keySet().iterator(); it.hasNext();) {
/* 251 */       keys[index++] = ((Integer)it.next()).intValue();
/*     */     }
/* 253 */     Arrays.sort(keys);
/* 254 */     return keys;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void string_switch_hash(final CodeEmitter e, String[] strings, final ObjectSwitchCallback callback, final boolean skipEquals) throws Exception {
/* 261 */     final Map buckets = CollectionUtils.bucket(Arrays.asList(strings), new Transformer() {
/*     */           public Object transform(Object value) {
/* 263 */             return new Integer(value.hashCode());
/*     */           }
/*     */         });
/* 266 */     final Label def = e.make_label();
/* 267 */     final Label end = e.make_label();
/* 268 */     e.dup();
/* 269 */     e.invoke_virtual(Constants.TYPE_OBJECT, HASH_CODE);
/* 270 */     e.process_switch(getSwitchKeys(buckets), new ProcessSwitchCallback() {
/*     */           public void processCase(int key, Label ignore_end) throws Exception {
/* 272 */             List<String> bucket = (List)buckets.get(new Integer(key));
/* 273 */             Label next = null;
/* 274 */             if (skipEquals && bucket.size() == 1) {
/* 275 */               if (skipEquals)
/* 276 */                 e.pop(); 
/* 277 */               callback.processCase(bucket.get(0), end);
/*     */             } else {
/* 279 */               for (Iterator<String> it = bucket.iterator(); it.hasNext(); ) {
/* 280 */                 String string = it.next();
/* 281 */                 if (next != null) {
/* 282 */                   e.mark(next);
/*     */                 }
/* 284 */                 if (it.hasNext()) {
/* 285 */                   e.dup();
/*     */                 }
/* 287 */                 e.push(string);
/* 288 */                 e.invoke_virtual(Constants.TYPE_OBJECT, EmitUtils.EQUALS);
/* 289 */                 if (it.hasNext()) {
/* 290 */                   e.if_jump(153, next = e.make_label());
/* 291 */                   e.pop();
/*     */                 } else {
/* 293 */                   e.if_jump(153, def);
/*     */                 } 
/* 295 */                 callback.processCase(string, end);
/*     */               } 
/*     */             } 
/*     */           }
/*     */           public void processDefault() {
/* 300 */             e.pop();
/*     */           }
/*     */         });
/* 303 */     e.mark(def);
/* 304 */     callback.processDefault();
/* 305 */     e.mark(end);
/*     */   }
/*     */   
/*     */   public static void load_class_this(CodeEmitter e) {
/* 309 */     load_class_helper(e, e.getClassEmitter().getClassType());
/*     */   }
/*     */   
/*     */   public static void load_class(CodeEmitter e, Type type) {
/* 313 */     if (TypeUtils.isPrimitive(type)) {
/* 314 */       if (type == Type.VOID_TYPE) {
/* 315 */         throw new IllegalArgumentException("cannot load void type");
/*     */       }
/* 317 */       e.getstatic(TypeUtils.getBoxedType(type), "TYPE", Constants.TYPE_CLASS);
/*     */     } else {
/* 319 */       load_class_helper(e, type);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void load_class_helper(CodeEmitter e, Type type) {
/* 324 */     if (e.isStaticHook()) {
/*     */       
/* 326 */       e.push(TypeUtils.emulateClassGetName(type));
/* 327 */       e.invoke_static(Constants.TYPE_CLASS, FOR_NAME);
/*     */     } else {
/* 329 */       ClassEmitter ce = e.getClassEmitter();
/* 330 */       String typeName = TypeUtils.emulateClassGetName(type);
/*     */ 
/*     */       
/* 333 */       String fieldName = "CGLIB$load_class$" + TypeUtils.escapeType(typeName);
/* 334 */       if (!ce.isFieldDeclared(fieldName)) {
/* 335 */         ce.declare_field(26, fieldName, Constants.TYPE_CLASS, null);
/* 336 */         CodeEmitter hook = ce.getStaticHook();
/* 337 */         hook.push(typeName);
/* 338 */         hook.invoke_static(Constants.TYPE_CLASS, FOR_NAME);
/* 339 */         hook.putstatic(ce.getClassType(), fieldName, Constants.TYPE_CLASS);
/*     */       } 
/* 341 */       e.getfield(fieldName);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void push_array(CodeEmitter e, Object[] array) {
/* 346 */     e.push(array.length);
/* 347 */     e.newarray(Type.getType(remapComponentType(array.getClass().getComponentType())));
/* 348 */     for (int i = 0; i < array.length; i++) {
/* 349 */       e.dup();
/* 350 */       e.push(i);
/* 351 */       push_object(e, array[i]);
/* 352 */       e.aastore();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Class remapComponentType(Class componentType) {
/* 357 */     if (componentType.equals(Type.class))
/* 358 */       return Class.class; 
/* 359 */     return componentType;
/*     */   }
/*     */   
/*     */   public static void push_object(CodeEmitter e, Object obj) {
/* 363 */     if (obj == null) {
/* 364 */       e.aconst_null();
/*     */     } else {
/* 366 */       Class<?> type = obj.getClass();
/* 367 */       if (type.isArray()) {
/* 368 */         push_array(e, (Object[])obj);
/* 369 */       } else if (obj instanceof String) {
/* 370 */         e.push((String)obj);
/* 371 */       } else if (obj instanceof Type) {
/* 372 */         load_class(e, (Type)obj);
/* 373 */       } else if (obj instanceof Class) {
/* 374 */         load_class(e, Type.getType((Class)obj));
/* 375 */       } else if (obj instanceof java.math.BigInteger) {
/* 376 */         e.new_instance(Constants.TYPE_BIG_INTEGER);
/* 377 */         e.dup();
/* 378 */         e.push(obj.toString());
/* 379 */         e.invoke_constructor(Constants.TYPE_BIG_INTEGER);
/* 380 */       } else if (obj instanceof java.math.BigDecimal) {
/* 381 */         e.new_instance(Constants.TYPE_BIG_DECIMAL);
/* 382 */         e.dup();
/* 383 */         e.push(obj.toString());
/* 384 */         e.invoke_constructor(Constants.TYPE_BIG_DECIMAL);
/*     */       } else {
/* 386 */         throw new IllegalArgumentException("unknown type: " + obj.getClass());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void hash_code(CodeEmitter e, Type type, int multiplier, Customizer customizer) {
/* 396 */     hash_code(e, type, multiplier, CustomizerRegistry.singleton(customizer));
/*     */   }
/*     */   
/*     */   public static void hash_code(CodeEmitter e, Type type, int multiplier, CustomizerRegistry registry) {
/* 400 */     if (TypeUtils.isArray(type)) {
/* 401 */       hash_array(e, type, multiplier, registry);
/*     */     } else {
/* 403 */       e.swap(Type.INT_TYPE, type);
/* 404 */       e.push(multiplier);
/* 405 */       e.math(104, Type.INT_TYPE);
/* 406 */       e.swap(type, Type.INT_TYPE);
/* 407 */       if (TypeUtils.isPrimitive(type)) {
/* 408 */         hash_primitive(e, type);
/*     */       } else {
/* 410 */         hash_object(e, type, registry);
/*     */       } 
/* 412 */       e.math(96, Type.INT_TYPE);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void hash_array(final CodeEmitter e, Type type, final int multiplier, final CustomizerRegistry registry) {
/* 417 */     Label skip = e.make_label();
/* 418 */     Label end = e.make_label();
/* 419 */     e.dup();
/* 420 */     e.ifnull(skip);
/* 421 */     process_array(e, type, new ProcessArrayCallback() {
/*     */           public void processElement(Type type) {
/* 423 */             EmitUtils.hash_code(e, type, multiplier, registry);
/*     */           }
/*     */         });
/* 426 */     e.goTo(end);
/* 427 */     e.mark(skip);
/* 428 */     e.pop();
/* 429 */     e.mark(end);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void hash_object(CodeEmitter e, Type type, CustomizerRegistry registry) {
/* 434 */     Label skip = e.make_label();
/* 435 */     Label end = e.make_label();
/* 436 */     e.dup();
/* 437 */     e.ifnull(skip);
/* 438 */     boolean customHashCode = false;
/* 439 */     for (HashCodeCustomizer customizer : registry.get(HashCodeCustomizer.class)) {
/* 440 */       if (customizer.customize(e, type)) {
/* 441 */         customHashCode = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 445 */     if (!customHashCode) {
/* 446 */       for (Customizer customizer : registry.get(Customizer.class)) {
/* 447 */         customizer.customize(e, type);
/*     */       }
/* 449 */       e.invoke_virtual(Constants.TYPE_OBJECT, HASH_CODE);
/*     */     } 
/* 451 */     e.goTo(end);
/* 452 */     e.mark(skip);
/* 453 */     e.pop();
/* 454 */     e.push(0);
/* 455 */     e.mark(end);
/*     */   }
/*     */   
/*     */   private static void hash_primitive(CodeEmitter e, Type type) {
/* 459 */     switch (type.getSort()) {
/*     */       
/*     */       case 1:
/* 462 */         e.push(1);
/* 463 */         e.math(130, Type.INT_TYPE);
/*     */         break;
/*     */       
/*     */       case 6:
/* 467 */         e.invoke_static(Constants.TYPE_FLOAT, FLOAT_TO_INT_BITS);
/*     */         break;
/*     */       
/*     */       case 8:
/* 471 */         e.invoke_static(Constants.TYPE_DOUBLE, DOUBLE_TO_LONG_BITS);
/*     */       
/*     */       case 7:
/* 474 */         hash_long(e);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void hash_long(CodeEmitter e) {
/* 480 */     e.dup2();
/* 481 */     e.push(32);
/* 482 */     e.math(124, Type.LONG_TYPE);
/* 483 */     e.math(130, Type.LONG_TYPE);
/* 484 */     e.cast_numeric(Type.LONG_TYPE, Type.INT_TYPE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void not_equals(CodeEmitter e, Type type, Label notEquals, Customizer customizer) {
/* 496 */     not_equals(e, type, notEquals, CustomizerRegistry.singleton(customizer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void not_equals(final CodeEmitter e, Type type, final Label notEquals, final CustomizerRegistry registry) {
/* 507 */     (new ProcessArrayCallback() {
/*     */         public void processElement(Type type) {
/* 509 */           EmitUtils.not_equals_helper(e, type, notEquals, registry, this);
/*     */         }
/* 511 */       }).processElement(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void not_equals_helper(CodeEmitter e, Type type, Label notEquals, CustomizerRegistry registry, ProcessArrayCallback callback) {
/* 519 */     if (TypeUtils.isPrimitive(type)) {
/* 520 */       e.if_cmp(type, 154, notEquals);
/*     */     } else {
/* 522 */       Label end = e.make_label();
/* 523 */       nullcmp(e, notEquals, end);
/* 524 */       if (TypeUtils.isArray(type)) {
/* 525 */         Label checkContents = e.make_label();
/* 526 */         e.dup2();
/* 527 */         e.arraylength();
/* 528 */         e.swap();
/* 529 */         e.arraylength();
/* 530 */         e.if_icmp(153, checkContents);
/* 531 */         e.pop2();
/* 532 */         e.goTo(notEquals);
/* 533 */         e.mark(checkContents);
/* 534 */         process_arrays(e, type, callback);
/*     */       } else {
/* 536 */         List<Customizer> customizers = registry.get(Customizer.class);
/* 537 */         if (!customizers.isEmpty()) {
/* 538 */           for (Customizer customizer : customizers) {
/* 539 */             customizer.customize(e, type);
/*     */           }
/* 541 */           e.swap();
/* 542 */           for (Customizer customizer : customizers) {
/* 543 */             customizer.customize(e, type);
/*     */           }
/*     */         } 
/* 546 */         e.invoke_virtual(Constants.TYPE_OBJECT, EQUALS);
/* 547 */         e.if_jump(153, notEquals);
/*     */       } 
/* 549 */       e.mark(end);
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
/*     */   private static void nullcmp(CodeEmitter e, Label oneNull, Label bothNull) {
/* 561 */     e.dup2();
/* 562 */     Label nonNull = e.make_label();
/* 563 */     Label oneNullHelper = e.make_label();
/* 564 */     Label end = e.make_label();
/* 565 */     e.ifnonnull(nonNull);
/* 566 */     e.ifnonnull(oneNullHelper);
/* 567 */     e.pop2();
/* 568 */     e.goTo(bothNull);
/*     */     
/* 570 */     e.mark(nonNull);
/* 571 */     e.ifnull(oneNullHelper);
/* 572 */     e.goTo(end);
/*     */     
/* 574 */     e.mark(oneNullHelper);
/* 575 */     e.pop2();
/* 576 */     e.goTo(oneNull);
/*     */     
/* 578 */     e.mark(end);
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
/*     */   @Deprecated
/*     */   public static void append_string(CodeEmitter e, Type type, ArrayDelimiters delims, Customizer customizer) {
/* 603 */     append_string(e, type, delims, CustomizerRegistry.singleton(customizer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void append_string(final CodeEmitter e, Type type, ArrayDelimiters delims, final CustomizerRegistry registry) {
/* 610 */     final ArrayDelimiters d = (delims != null) ? delims : DEFAULT_DELIMITERS;
/* 611 */     ProcessArrayCallback callback = new ProcessArrayCallback() {
/*     */         public void processElement(Type type) {
/* 613 */           EmitUtils.append_string_helper(e, type, d, registry, this);
/* 614 */           e.push(d.inside);
/* 615 */           e.invoke_virtual(Constants.TYPE_STRING_BUFFER, EmitUtils.APPEND_STRING);
/*     */         }
/*     */       };
/* 618 */     append_string_helper(e, type, d, registry, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void append_string_helper(CodeEmitter e, Type type, ArrayDelimiters delims, CustomizerRegistry registry, ProcessArrayCallback callback) {
/* 626 */     Label skip = e.make_label();
/* 627 */     Label end = e.make_label();
/* 628 */     if (TypeUtils.isPrimitive(type)) {
/* 629 */       switch (type.getSort()) {
/*     */         case 3:
/*     */         case 4:
/*     */         case 5:
/* 633 */           e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_INT);
/*     */           break;
/*     */         case 8:
/* 636 */           e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_DOUBLE);
/*     */           break;
/*     */         case 6:
/* 639 */           e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_FLOAT);
/*     */           break;
/*     */         case 7:
/* 642 */           e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_LONG);
/*     */           break;
/*     */         case 1:
/* 645 */           e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_BOOLEAN);
/*     */           break;
/*     */         case 2:
/* 648 */           e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_CHAR);
/*     */           break;
/*     */       } 
/* 651 */     } else if (TypeUtils.isArray(type)) {
/* 652 */       e.dup();
/* 653 */       e.ifnull(skip);
/* 654 */       e.swap();
/* 655 */       if (delims != null && delims.before != null && !"".equals(delims.before)) {
/* 656 */         e.push(delims.before);
/* 657 */         e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
/* 658 */         e.swap();
/*     */       } 
/* 660 */       process_array(e, type, callback);
/* 661 */       shrinkStringBuffer(e, 2);
/* 662 */       if (delims != null && delims.after != null && !"".equals(delims.after)) {
/* 663 */         e.push(delims.after);
/* 664 */         e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
/*     */       } 
/*     */     } else {
/* 667 */       e.dup();
/* 668 */       e.ifnull(skip);
/* 669 */       for (Customizer customizer : registry.get(Customizer.class)) {
/* 670 */         customizer.customize(e, type);
/*     */       }
/* 672 */       e.invoke_virtual(Constants.TYPE_OBJECT, TO_STRING);
/* 673 */       e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
/*     */     } 
/* 675 */     e.goTo(end);
/* 676 */     e.mark(skip);
/* 677 */     e.pop();
/* 678 */     e.push("null");
/* 679 */     e.invoke_virtual(Constants.TYPE_STRING_BUFFER, APPEND_STRING);
/* 680 */     e.mark(end);
/*     */   }
/*     */   
/*     */   private static void shrinkStringBuffer(CodeEmitter e, int amt) {
/* 684 */     e.dup();
/* 685 */     e.dup();
/* 686 */     e.invoke_virtual(Constants.TYPE_STRING_BUFFER, LENGTH);
/* 687 */     e.push(amt);
/* 688 */     e.math(100, Type.INT_TYPE);
/* 689 */     e.invoke_virtual(Constants.TYPE_STRING_BUFFER, SET_LENGTH);
/*     */   }
/*     */   
/*     */   public static class ArrayDelimiters {
/*     */     private String before;
/*     */     private String inside;
/*     */     private String after;
/*     */     
/*     */     public ArrayDelimiters(String before, String inside, String after) {
/* 698 */       this.before = before;
/* 699 */       this.inside = inside;
/* 700 */       this.after = after;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void load_method(CodeEmitter e, MethodInfo method) {
/* 705 */     load_class(e, method.getClassInfo().getType());
/* 706 */     e.push(method.getSignature().getName());
/* 707 */     push_object(e, method.getSignature().getArgumentTypes());
/* 708 */     e.invoke_virtual(Constants.TYPE_CLASS, GET_DECLARED_METHOD);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void method_switch(CodeEmitter e, List methods, ObjectSwitchCallback callback) {
/* 718 */     member_switch_helper(e, methods, callback, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void constructor_switch(CodeEmitter e, List constructors, ObjectSwitchCallback callback) {
/* 724 */     member_switch_helper(e, constructors, callback, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void member_switch_helper(final CodeEmitter e, List members, final ObjectSwitchCallback callback, boolean useName) {
/*     */     try {
/* 732 */       final Map<Object, Object> cache = new HashMap<Object, Object>();
/* 733 */       final ParameterTyper cached = new ParameterTyper() {
/*     */           public Type[] getParameterTypes(MethodInfo member) {
/* 735 */             Type[] types = (Type[])cache.get(member);
/* 736 */             if (types == null) {
/* 737 */               cache.put(member, types = member.getSignature().getArgumentTypes());
/*     */             }
/* 739 */             return types;
/*     */           }
/*     */         };
/* 742 */       final Label def = e.make_label();
/* 743 */       final Label end = e.make_label();
/* 744 */       if (useName) {
/* 745 */         e.swap();
/* 746 */         final Map buckets = CollectionUtils.bucket(members, new Transformer() {
/*     */               public Object transform(Object value) {
/* 748 */                 return ((MethodInfo)value).getSignature().getName();
/*     */               }
/*     */             });
/* 751 */         String[] names = (String[])buckets.keySet().toArray((Object[])new String[buckets.size()]);
/* 752 */         string_switch(e, names, 1, new ObjectSwitchCallback() {
/*     */               public void processCase(Object key, Label dontUseEnd) throws Exception {
/* 754 */                 EmitUtils.member_helper_size(e, (List)buckets.get(key), callback, cached, def, end);
/*     */               }
/*     */               public void processDefault() throws Exception {
/* 757 */                 e.goTo(def);
/*     */               }
/*     */             });
/*     */       } else {
/* 761 */         member_helper_size(e, members, callback, cached, def, end);
/*     */       } 
/* 763 */       e.mark(def);
/* 764 */       e.pop();
/* 765 */       callback.processDefault();
/* 766 */       e.mark(end);
/* 767 */     } catch (RuntimeException ex) {
/* 768 */       throw ex;
/* 769 */     } catch (Error ex) {
/* 770 */       throw ex;
/* 771 */     } catch (Exception ex) {
/* 772 */       throw new CodeGenerationException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void member_helper_size(final CodeEmitter e, List members, final ObjectSwitchCallback callback, final ParameterTyper typer, final Label def, final Label end) throws Exception {
/* 782 */     final Map buckets = CollectionUtils.bucket(members, new Transformer() {
/*     */           public Object transform(Object value) {
/* 784 */             return new Integer((typer.getParameterTypes((MethodInfo)value)).length);
/*     */           }
/*     */         });
/* 787 */     e.dup();
/* 788 */     e.arraylength();
/* 789 */     e.process_switch(getSwitchKeys(buckets), new ProcessSwitchCallback() {
/*     */           public void processCase(int key, Label dontUseEnd) throws Exception {
/* 791 */             List bucket = (List)buckets.get(new Integer(key));
/* 792 */             EmitUtils.member_helper_type(e, bucket, callback, typer, def, end, new BitSet());
/*     */           }
/*     */           public void processDefault() throws Exception {
/* 795 */             e.goTo(def);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void member_helper_type(final CodeEmitter e, List<MethodInfo> members, final ObjectSwitchCallback callback, final ParameterTyper typer, final Label def, final Label end, final BitSet checked) throws Exception {
/* 807 */     if (members.size() == 1) {
/* 808 */       MethodInfo member = members.get(0);
/* 809 */       Type[] types = typer.getParameterTypes(member);
/*     */       
/* 811 */       for (int i = 0; i < types.length; i++) {
/* 812 */         if (checked == null || !checked.get(i)) {
/* 813 */           e.dup();
/* 814 */           e.aaload(i);
/* 815 */           e.invoke_virtual(Constants.TYPE_CLASS, GET_NAME);
/* 816 */           e.push(TypeUtils.emulateClassGetName(types[i]));
/* 817 */           e.invoke_virtual(Constants.TYPE_OBJECT, EQUALS);
/* 818 */           e.if_jump(153, def);
/*     */         } 
/*     */       } 
/* 821 */       e.pop();
/* 822 */       callback.processCase(member, end);
/*     */     } else {
/*     */       
/* 825 */       Type[] example = typer.getParameterTypes(members.get(0));
/* 826 */       Map buckets = null;
/* 827 */       int index = -1;
/* 828 */       for (int i = 0; i < example.length; i++) {
/* 829 */         final int j = i;
/* 830 */         Map test = CollectionUtils.bucket(members, new Transformer() {
/*     */               public Object transform(Object value) {
/* 832 */                 return TypeUtils.emulateClassGetName(typer.getParameterTypes((MethodInfo)value)[j]);
/*     */               }
/*     */             });
/* 835 */         if (buckets == null || test.size() > buckets.size()) {
/* 836 */           buckets = test;
/* 837 */           index = i;
/*     */         } 
/*     */       } 
/* 840 */       if (buckets == null || buckets.size() == 1) {
/*     */ 
/*     */         
/* 843 */         e.goTo(def);
/*     */       } else {
/* 845 */         checked.set(index);
/*     */         
/* 847 */         e.dup();
/* 848 */         e.aaload(index);
/* 849 */         e.invoke_virtual(Constants.TYPE_CLASS, GET_NAME);
/*     */         
/* 851 */         final Map fbuckets = buckets;
/* 852 */         String[] names = (String[])buckets.keySet().toArray((Object[])new String[buckets.size()]);
/* 853 */         string_switch(e, names, 1, new ObjectSwitchCallback() {
/*     */               public void processCase(Object key, Label dontUseEnd) throws Exception {
/* 855 */                 EmitUtils.member_helper_type(e, (List)fbuckets.get(key), callback, typer, def, end, checked);
/*     */               }
/*     */               public void processDefault() throws Exception {
/* 858 */                 e.goTo(def);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void wrap_throwable(Block block, Type wrapper) {
/* 866 */     CodeEmitter e = block.getCodeEmitter();
/* 867 */     e.catch_exception(block, Constants.TYPE_THROWABLE);
/* 868 */     e.new_instance(wrapper);
/* 869 */     e.dup_x1();
/* 870 */     e.swap();
/* 871 */     e.invoke_constructor(wrapper, CSTRUCT_THROWABLE);
/* 872 */     e.athrow();
/*     */   }
/*     */   
/*     */   public static void add_properties(ClassEmitter ce, String[] names, Type[] types) {
/* 876 */     for (int i = 0; i < names.length; i++) {
/* 877 */       String fieldName = "$cglib_prop_" + names[i];
/* 878 */       ce.declare_field(2, fieldName, types[i], null);
/* 879 */       add_property(ce, names[i], types[i], fieldName);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void add_property(ClassEmitter ce, String name, Type type, String fieldName) {
/* 884 */     String property = TypeUtils.upperFirst(name);
/*     */     
/* 886 */     CodeEmitter e = ce.begin_method(1, new Signature("get" + property, type, Constants.TYPES_EMPTY), null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 891 */     e.load_this();
/* 892 */     e.getfield(fieldName);
/* 893 */     e.return_value();
/* 894 */     e.end_method();
/*     */     
/* 896 */     e = ce.begin_method(1, new Signature("set" + property, Type.VOID_TYPE, new Type[] { type }), null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 901 */     e.load_this();
/* 902 */     e.load_arg(0);
/* 903 */     e.putfield(fieldName);
/* 904 */     e.return_value();
/* 905 */     e.end_method();
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
/*     */   public static void wrap_undeclared_throwable(CodeEmitter e, Block handler, Type[] exceptions, Type wrapper) {
/* 920 */     Set set = (exceptions == null) ? Collections.EMPTY_SET : new HashSet(Arrays.asList((Object[])exceptions));
/*     */     
/* 922 */     if (set.contains(Constants.TYPE_THROWABLE)) {
/*     */       return;
/*     */     }
/* 925 */     boolean needThrow = (exceptions != null);
/* 926 */     if (!set.contains(Constants.TYPE_RUNTIME_EXCEPTION)) {
/* 927 */       e.catch_exception(handler, Constants.TYPE_RUNTIME_EXCEPTION);
/* 928 */       needThrow = true;
/*     */     } 
/* 930 */     if (!set.contains(Constants.TYPE_ERROR)) {
/* 931 */       e.catch_exception(handler, Constants.TYPE_ERROR);
/* 932 */       needThrow = true;
/*     */     } 
/* 934 */     if (exceptions != null) {
/* 935 */       for (int i = 0; i < exceptions.length; i++) {
/* 936 */         e.catch_exception(handler, exceptions[i]);
/*     */       }
/*     */     }
/* 939 */     if (needThrow) {
/* 940 */       e.athrow();
/*     */     }
/*     */     
/* 943 */     e.catch_exception(handler, Constants.TYPE_THROWABLE);
/* 944 */     e.new_instance(wrapper);
/* 945 */     e.dup_x1();
/* 946 */     e.swap();
/* 947 */     e.invoke_constructor(wrapper, CSTRUCT_THROWABLE);
/* 948 */     e.athrow();
/*     */   }
/*     */   
/*     */   public static CodeEmitter begin_method(ClassEmitter e, MethodInfo method) {
/* 952 */     return begin_method(e, method, method.getModifiers());
/*     */   }
/*     */   
/*     */   public static CodeEmitter begin_method(ClassEmitter e, MethodInfo method, int access) {
/* 956 */     return e.begin_method(access, method
/* 957 */         .getSignature(), method
/* 958 */         .getExceptionTypes());
/*     */   }
/*     */   
/*     */   private static interface ParameterTyper {
/*     */     Type[] getParameterTypes(MethodInfo param1MethodInfo);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\EmitUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */