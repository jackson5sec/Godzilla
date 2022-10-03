/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
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
/*     */ public class Type
/*     */ {
/*     */   private final CtClass clazz;
/*     */   private final boolean special;
/*  43 */   private static final Map<CtClass, Type> prims = new IdentityHashMap<>();
/*     */   
/*  45 */   public static final Type DOUBLE = new Type(CtClass.doubleType);
/*     */   
/*  47 */   public static final Type BOOLEAN = new Type(CtClass.booleanType);
/*     */   
/*  49 */   public static final Type LONG = new Type(CtClass.longType);
/*     */   
/*  51 */   public static final Type CHAR = new Type(CtClass.charType);
/*     */   
/*  53 */   public static final Type BYTE = new Type(CtClass.byteType);
/*     */   
/*  55 */   public static final Type SHORT = new Type(CtClass.shortType);
/*     */   
/*  57 */   public static final Type INTEGER = new Type(CtClass.intType);
/*     */   
/*  59 */   public static final Type FLOAT = new Type(CtClass.floatType);
/*     */   
/*  61 */   public static final Type VOID = new Type(CtClass.voidType);
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
/*  72 */   public static final Type UNINIT = new Type(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final Type RETURN_ADDRESS = new Type(null, true);
/*     */ 
/*     */   
/*  81 */   public static final Type TOP = new Type(null, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public static final Type BOGUS = new Type(null, true);
/*     */ 
/*     */   
/*  93 */   public static final Type OBJECT = lookupType("java.lang.Object");
/*     */   
/*  95 */   public static final Type SERIALIZABLE = lookupType("java.io.Serializable");
/*     */   
/*  97 */   public static final Type CLONEABLE = lookupType("java.lang.Cloneable");
/*     */   
/*  99 */   public static final Type THROWABLE = lookupType("java.lang.Throwable");
/*     */   
/*     */   static {
/* 102 */     prims.put(CtClass.doubleType, DOUBLE);
/* 103 */     prims.put(CtClass.longType, LONG);
/* 104 */     prims.put(CtClass.charType, CHAR);
/* 105 */     prims.put(CtClass.shortType, SHORT);
/* 106 */     prims.put(CtClass.intType, INTEGER);
/* 107 */     prims.put(CtClass.floatType, FLOAT);
/* 108 */     prims.put(CtClass.byteType, BYTE);
/* 109 */     prims.put(CtClass.booleanType, BOOLEAN);
/* 110 */     prims.put(CtClass.voidType, VOID);
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
/*     */   public static Type get(CtClass clazz) {
/* 123 */     Type type = prims.get(clazz);
/* 124 */     return (type != null) ? type : new Type(clazz);
/*     */   }
/*     */   
/*     */   private static Type lookupType(String name) {
/*     */     try {
/* 129 */       return new Type(ClassPool.getDefault().get(name));
/* 130 */     } catch (NotFoundException e) {
/* 131 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   Type(CtClass clazz) {
/* 136 */     this(clazz, false);
/*     */   }
/*     */   
/*     */   private Type(CtClass clazz, boolean special) {
/* 140 */     this.clazz = clazz;
/* 141 */     this.special = special;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean popChanged() {
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 156 */     return (this.clazz == CtClass.doubleType || this.clazz == CtClass.longType || this == TOP) ? 2 : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getCtClass() {
/* 165 */     return this.clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReference() {
/* 174 */     return (!this.special && (this.clazz == null || !this.clazz.isPrimitive()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSpecial() {
/* 184 */     return this.special;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/* 193 */     return (this.clazz != null && this.clazz.isArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDimensions() {
/* 203 */     if (!isArray()) return 0;
/*     */     
/* 205 */     String name = this.clazz.getName();
/* 206 */     int pos = name.length() - 1;
/* 207 */     int count = 0;
/* 208 */     while (name.charAt(pos) == ']') {
/* 209 */       pos -= 2;
/* 210 */       count++;
/*     */     } 
/*     */     
/* 213 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getComponent() {
/*     */     CtClass component;
/* 223 */     if (this.clazz == null || !this.clazz.isArray()) {
/* 224 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 228 */       component = this.clazz.getComponentType();
/* 229 */     } catch (NotFoundException e) {
/* 230 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 233 */     Type type = prims.get(component);
/* 234 */     return (type != null) ? type : new Type(component);
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
/*     */   public boolean isAssignableFrom(Type type) {
/* 246 */     if (this == type) {
/* 247 */       return true;
/*     */     }
/* 249 */     if ((type == UNINIT && isReference()) || (this == UNINIT && type.isReference())) {
/* 250 */       return true;
/*     */     }
/* 252 */     if (type instanceof MultiType) {
/* 253 */       return ((MultiType)type).isAssignableTo(this);
/*     */     }
/* 255 */     if (type instanceof MultiArrayType) {
/* 256 */       return ((MultiArrayType)type).isAssignableTo(this);
/*     */     }
/*     */ 
/*     */     
/* 260 */     if (this.clazz == null || this.clazz.isPrimitive()) {
/* 261 */       return false;
/*     */     }
/*     */     try {
/* 264 */       return type.clazz.subtypeOf(this.clazz);
/* 265 */     } catch (Exception e) {
/* 266 */       throw new RuntimeException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type merge(Type type) {
/* 282 */     if (type == this)
/* 283 */       return this; 
/* 284 */     if (type == null)
/* 285 */       return this; 
/* 286 */     if (type == UNINIT)
/* 287 */       return this; 
/* 288 */     if (this == UNINIT) {
/* 289 */       return type;
/*     */     }
/*     */     
/* 292 */     if (!type.isReference() || !isReference()) {
/* 293 */       return BOGUS;
/*     */     }
/*     */     
/* 296 */     if (type instanceof MultiType) {
/* 297 */       return type.merge(this);
/*     */     }
/* 299 */     if (type.isArray() && isArray()) {
/* 300 */       return mergeArray(type);
/*     */     }
/*     */     try {
/* 303 */       return mergeClasses(type);
/* 304 */     } catch (NotFoundException e) {
/* 305 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   Type getRootComponent(Type type) {
/* 310 */     while (type.isArray()) {
/* 311 */       type = type.getComponent();
/*     */     }
/* 313 */     return type;
/*     */   }
/*     */   private Type createArray(Type rootComponent, int dims) {
/*     */     Type type;
/* 317 */     if (rootComponent instanceof MultiType) {
/* 318 */       return new MultiArrayType((MultiType)rootComponent, dims);
/*     */     }
/* 320 */     String name = arrayName(rootComponent.clazz.getName(), dims);
/*     */ 
/*     */     
/*     */     try {
/* 324 */       type = get(getClassPool(rootComponent).get(name));
/* 325 */     } catch (NotFoundException e) {
/* 326 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 329 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String arrayName(String component, int dims) {
/* 335 */     int i = component.length();
/* 336 */     int size = i + dims * 2;
/* 337 */     char[] string = new char[size];
/* 338 */     component.getChars(0, i, string, 0);
/* 339 */     while (i < size) {
/* 340 */       string[i++] = '[';
/* 341 */       string[i++] = ']';
/*     */     } 
/* 343 */     component = new String(string);
/* 344 */     return component;
/*     */   }
/*     */   
/*     */   private ClassPool getClassPool(Type rootComponent) {
/* 348 */     ClassPool pool = rootComponent.clazz.getClassPool();
/* 349 */     return (pool != null) ? pool : ClassPool.getDefault();
/*     */   } private Type mergeArray(Type type) {
/*     */     Type targetRoot;
/*     */     int targetDims;
/* 353 */     Type typeRoot = getRootComponent(type);
/* 354 */     Type thisRoot = getRootComponent(this);
/* 355 */     int typeDims = type.getDimensions();
/* 356 */     int thisDims = getDimensions();
/*     */ 
/*     */     
/* 359 */     if (typeDims == thisDims) {
/* 360 */       Type mergedComponent = thisRoot.merge(typeRoot);
/*     */ 
/*     */ 
/*     */       
/* 364 */       if (mergedComponent == BOGUS) {
/* 365 */         return OBJECT;
/*     */       }
/* 367 */       return createArray(mergedComponent, thisDims);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 373 */     if (typeDims < thisDims) {
/* 374 */       targetRoot = typeRoot;
/* 375 */       targetDims = typeDims;
/*     */     } else {
/* 377 */       targetRoot = thisRoot;
/* 378 */       targetDims = thisDims;
/*     */     } 
/*     */ 
/*     */     
/* 382 */     if (eq(CLONEABLE.clazz, targetRoot.clazz) || eq(SERIALIZABLE.clazz, targetRoot.clazz)) {
/* 383 */       return createArray(targetRoot, targetDims);
/*     */     }
/* 385 */     return createArray(OBJECT, targetDims);
/*     */   }
/*     */   
/*     */   private static CtClass findCommonSuperClass(CtClass one, CtClass two) throws NotFoundException {
/* 389 */     CtClass deep = one;
/* 390 */     CtClass shallow = two;
/* 391 */     CtClass backupShallow = shallow;
/* 392 */     CtClass backupDeep = deep;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 397 */       if (eq(deep, shallow) && deep.getSuperclass() != null) {
/* 398 */         return deep;
/*     */       }
/* 400 */       CtClass deepSuper = deep.getSuperclass();
/* 401 */       CtClass shallowSuper = shallow.getSuperclass();
/*     */       
/* 403 */       if (shallowSuper == null) {
/*     */         
/* 405 */         shallow = backupShallow;
/*     */         
/*     */         break;
/*     */       } 
/* 409 */       if (deepSuper == null) {
/*     */         
/* 411 */         deep = backupDeep;
/* 412 */         backupDeep = backupShallow;
/* 413 */         backupShallow = deep;
/*     */         
/* 415 */         deep = shallow;
/* 416 */         shallow = backupShallow;
/*     */         
/*     */         break;
/*     */       } 
/* 420 */       deep = deepSuper;
/* 421 */       shallow = shallowSuper;
/*     */     } 
/*     */ 
/*     */     
/*     */     while (true) {
/* 426 */       deep = deep.getSuperclass();
/* 427 */       if (deep == null) {
/*     */         break;
/*     */       }
/* 430 */       backupDeep = backupDeep.getSuperclass();
/*     */     } 
/*     */     
/* 433 */     deep = backupDeep;
/*     */ 
/*     */ 
/*     */     
/* 437 */     while (!eq(deep, shallow)) {
/* 438 */       deep = deep.getSuperclass();
/* 439 */       shallow = shallow.getSuperclass();
/*     */     } 
/*     */     
/* 442 */     return deep;
/*     */   }
/*     */   
/*     */   private Type mergeClasses(Type type) throws NotFoundException {
/* 446 */     CtClass superClass = findCommonSuperClass(this.clazz, type.clazz);
/*     */ 
/*     */     
/* 449 */     if (superClass.getSuperclass() == null) {
/* 450 */       Map<String, CtClass> interfaces = findCommonInterfaces(type);
/* 451 */       if (interfaces.size() == 1)
/* 452 */         return new Type(interfaces.values().iterator().next()); 
/* 453 */       if (interfaces.size() > 1) {
/* 454 */         return new MultiType(interfaces);
/*     */       }
/*     */       
/* 457 */       return new Type(superClass);
/*     */     } 
/*     */ 
/*     */     
/* 461 */     Map<String, CtClass> commonDeclared = findExclusiveDeclaredInterfaces(type, superClass);
/* 462 */     if (commonDeclared.size() > 0) {
/* 463 */       return new MultiType(commonDeclared, new Type(superClass));
/*     */     }
/*     */     
/* 466 */     return new Type(superClass);
/*     */   }
/*     */   
/*     */   private Map<String, CtClass> findCommonInterfaces(Type type) {
/* 470 */     Map<String, CtClass> typeMap = getAllInterfaces(type.clazz, null);
/* 471 */     Map<String, CtClass> thisMap = getAllInterfaces(this.clazz, null);
/*     */     
/* 473 */     return findCommonInterfaces(typeMap, thisMap);
/*     */   }
/*     */   
/*     */   private Map<String, CtClass> findExclusiveDeclaredInterfaces(Type type, CtClass exclude) {
/* 477 */     Map<String, CtClass> typeMap = getDeclaredInterfaces(type.clazz, null);
/* 478 */     Map<String, CtClass> thisMap = getDeclaredInterfaces(this.clazz, null);
/* 479 */     Map<String, CtClass> excludeMap = getAllInterfaces(exclude, null);
/*     */     
/* 481 */     for (String intf : excludeMap.keySet()) {
/* 482 */       typeMap.remove(intf);
/* 483 */       thisMap.remove(intf);
/*     */     } 
/*     */     
/* 486 */     return findCommonInterfaces(typeMap, thisMap);
/*     */   }
/*     */ 
/*     */   
/*     */   Map<String, CtClass> findCommonInterfaces(Map<String, CtClass> typeMap, Map<String, CtClass> alterMap) {
/* 491 */     if (alterMap == null) {
/* 492 */       alterMap = new HashMap<>();
/*     */     }
/* 494 */     if (typeMap == null || typeMap.isEmpty()) {
/* 495 */       alterMap.clear();
/*     */     }
/* 497 */     Iterator<String> it = alterMap.keySet().iterator();
/* 498 */     while (it.hasNext()) {
/* 499 */       String name = it.next();
/* 500 */       if (!typeMap.containsKey(name)) {
/* 501 */         it.remove();
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 507 */     Collection<CtClass> interfaces = new ArrayList<>();
/* 508 */     for (CtClass intf : alterMap.values()) {
/*     */       try {
/* 510 */         interfaces.addAll(Arrays.asList(intf.getInterfaces()));
/* 511 */       } catch (NotFoundException e) {
/* 512 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/* 515 */     for (CtClass c : interfaces) {
/* 516 */       alterMap.remove(c.getName());
/*     */     }
/* 518 */     return alterMap;
/*     */   }
/*     */   
/*     */   Map<String, CtClass> getAllInterfaces(CtClass clazz, Map<String, CtClass> map) {
/* 522 */     if (map == null) {
/* 523 */       map = new HashMap<>();
/*     */     }
/* 525 */     if (clazz.isInterface())
/* 526 */       map.put(clazz.getName(), clazz); 
/*     */     do {
/*     */       try {
/* 529 */         CtClass[] interfaces = clazz.getInterfaces();
/* 530 */         for (CtClass intf : interfaces) {
/* 531 */           map.put(intf.getName(), intf);
/* 532 */           getAllInterfaces(intf, map);
/*     */         } 
/*     */         
/* 535 */         clazz = clazz.getSuperclass();
/* 536 */       } catch (NotFoundException e) {
/* 537 */         throw new RuntimeException(e);
/*     */       } 
/* 539 */     } while (clazz != null);
/*     */     
/* 541 */     return map;
/*     */   }
/*     */   Map<String, CtClass> getDeclaredInterfaces(CtClass clazz, Map<String, CtClass> map) {
/*     */     CtClass[] interfaces;
/* 545 */     if (map == null) {
/* 546 */       map = new HashMap<>();
/*     */     }
/* 548 */     if (clazz.isInterface()) {
/* 549 */       map.put(clazz.getName(), clazz);
/*     */     }
/*     */     
/*     */     try {
/* 553 */       interfaces = clazz.getInterfaces();
/* 554 */     } catch (NotFoundException e) {
/* 555 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 558 */     for (CtClass intf : interfaces) {
/* 559 */       map.put(intf.getName(), intf);
/* 560 */       getDeclaredInterfaces(intf, map);
/*     */     } 
/*     */     
/* 563 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 568 */     return getClass().hashCode() + this.clazz.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 573 */     if (!(o instanceof Type)) {
/* 574 */       return false;
/*     */     }
/* 576 */     return (o.getClass() == getClass() && eq(this.clazz, ((Type)o).clazz));
/*     */   }
/*     */   
/*     */   static boolean eq(CtClass one, CtClass two) {
/* 580 */     return (one == two || (one != null && two != null && one.getName().equals(two.getName())));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 585 */     if (this == BOGUS)
/* 586 */       return "BOGUS"; 
/* 587 */     if (this == UNINIT)
/* 588 */       return "UNINIT"; 
/* 589 */     if (this == RETURN_ADDRESS)
/* 590 */       return "RETURN ADDRESS"; 
/* 591 */     if (this == TOP) {
/* 592 */       return "TOP";
/*     */     }
/* 594 */     return (this.clazz == null) ? "null" : this.clazz.getName();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\bytecode\analysis\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */