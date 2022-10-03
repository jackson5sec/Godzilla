/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ class JavaMembers
/*     */ {
/*     */   private Class<?> cl;
/*     */   private Map<String, Object> members;
/*     */   private Map<String, FieldAndMethods> fieldAndMethods;
/*     */   private Map<String, Object> staticMembers;
/*     */   private Map<String, FieldAndMethods> staticFieldAndMethods;
/*     */   NativeJavaMethod ctors;
/*     */   
/*     */   JavaMembers(Scriptable scope, Class<?> cl) {
/*  26 */     this(scope, cl, false);
/*     */   }
/*     */ 
/*     */   
/*     */   JavaMembers(Scriptable scope, Class<?> cl, boolean includeProtected) {
/*     */     try {
/*  32 */       Context cx = ContextFactory.getGlobal().enterContext();
/*  33 */       ClassShutter shutter = cx.getClassShutter();
/*  34 */       if (shutter != null && !shutter.visibleToScripts(cl.getName())) {
/*  35 */         throw Context.reportRuntimeError1("msg.access.prohibited", cl.getName());
/*     */       }
/*     */       
/*  38 */       this.members = new HashMap<String, Object>();
/*  39 */       this.staticMembers = new HashMap<String, Object>();
/*  40 */       this.cl = cl;
/*  41 */       boolean includePrivate = cx.hasFeature(13);
/*     */       
/*  43 */       reflect(scope, includeProtected, includePrivate);
/*     */     } finally {
/*  45 */       Context.exit();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean has(String name, boolean isStatic) {
/*  51 */     Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
/*  52 */     Object obj = ht.get(name);
/*  53 */     if (obj != null) {
/*  54 */       return true;
/*     */     }
/*  56 */     return (findExplicitFunction(name, isStatic) != null);
/*     */   }
/*     */   
/*     */   Object get(Scriptable scope, String name, Object javaObject, boolean isStatic) {
/*     */     Object rval;
/*     */     Class<?> type;
/*  62 */     Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
/*  63 */     Object member = ht.get(name);
/*  64 */     if (!isStatic && member == null)
/*     */     {
/*  66 */       member = this.staticMembers.get(name);
/*     */     }
/*  68 */     if (member == null) {
/*  69 */       member = getExplicitFunction(scope, name, javaObject, isStatic);
/*     */       
/*  71 */       if (member == null)
/*  72 */         return Scriptable.NOT_FOUND; 
/*     */     } 
/*  74 */     if (member instanceof Scriptable) {
/*  75 */       return member;
/*     */     }
/*  77 */     Context cx = Context.getContext();
/*     */ 
/*     */     
/*     */     try {
/*  81 */       if (member instanceof BeanProperty) {
/*  82 */         BeanProperty bp = (BeanProperty)member;
/*  83 */         if (bp.getter == null)
/*  84 */           return Scriptable.NOT_FOUND; 
/*  85 */         rval = bp.getter.invoke(javaObject, Context.emptyArgs);
/*  86 */         type = bp.getter.method().getReturnType();
/*     */       } else {
/*  88 */         Field field = (Field)member;
/*  89 */         rval = field.get(isStatic ? null : javaObject);
/*  90 */         type = field.getType();
/*     */       } 
/*  92 */     } catch (Exception ex) {
/*  93 */       throw Context.throwAsScriptRuntimeEx(ex);
/*     */     } 
/*     */     
/*  96 */     scope = ScriptableObject.getTopLevelScope(scope);
/*  97 */     return cx.getWrapFactory().wrap(cx, scope, rval, type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void put(Scriptable scope, String name, Object javaObject, Object value, boolean isStatic) {
/* 103 */     Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
/* 104 */     Object member = ht.get(name);
/* 105 */     if (!isStatic && member == null)
/*     */     {
/* 107 */       member = this.staticMembers.get(name);
/*     */     }
/* 109 */     if (member == null)
/* 110 */       throw reportMemberNotFound(name); 
/* 111 */     if (member instanceof FieldAndMethods) {
/* 112 */       FieldAndMethods fam = (FieldAndMethods)ht.get(name);
/* 113 */       member = fam.field;
/*     */     } 
/*     */ 
/*     */     
/* 117 */     if (member instanceof BeanProperty) {
/* 118 */       BeanProperty bp = (BeanProperty)member;
/* 119 */       if (bp.setter == null) {
/* 120 */         throw reportMemberNotFound(name);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 125 */       if (bp.setters == null || value == null) {
/* 126 */         Class<?> setType = bp.setter.argTypes[0];
/* 127 */         Object[] args = { Context.jsToJava(value, setType) };
/*     */         try {
/* 129 */           bp.setter.invoke(javaObject, args);
/* 130 */         } catch (Exception ex) {
/* 131 */           throw Context.throwAsScriptRuntimeEx(ex);
/*     */         } 
/*     */       } else {
/* 134 */         Object[] args = { value };
/* 135 */         bp.setters.call(Context.getContext(), ScriptableObject.getTopLevelScope(scope), scope, args);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 141 */       if (!(member instanceof Field)) {
/* 142 */         String str = (member == null) ? "msg.java.internal.private" : "msg.java.method.assign";
/*     */         
/* 144 */         throw Context.reportRuntimeError1(str, name);
/*     */       } 
/* 146 */       Field field = (Field)member;
/* 147 */       Object javaValue = Context.jsToJava(value, field.getType());
/*     */       try {
/* 149 */         field.set(javaObject, javaValue);
/* 150 */       } catch (IllegalAccessException accessEx) {
/* 151 */         if ((field.getModifiers() & 0x10) != 0) {
/*     */           return;
/*     */         }
/*     */         
/* 155 */         throw Context.throwAsScriptRuntimeEx(accessEx);
/* 156 */       } catch (IllegalArgumentException argEx) {
/* 157 */         throw Context.reportRuntimeError3("msg.java.internal.field.type", value.getClass().getName(), field, javaObject.getClass().getName());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object[] getIds(boolean isStatic) {
/* 167 */     Map<String, Object> map = isStatic ? this.staticMembers : this.members;
/* 168 */     return map.keySet().toArray(new Object[map.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   static String javaSignature(Class<?> type) {
/* 173 */     if (!type.isArray()) {
/* 174 */       return type.getName();
/*     */     }
/* 176 */     int arrayDimension = 0;
/*     */     while (true) {
/* 178 */       arrayDimension++;
/* 179 */       type = type.getComponentType();
/* 180 */       if (!type.isArray()) {
/* 181 */         String name = type.getName();
/* 182 */         String suffix = "[]";
/* 183 */         if (arrayDimension == 1) {
/* 184 */           return name.concat(suffix);
/*     */         }
/* 186 */         int length = name.length() + arrayDimension * suffix.length();
/* 187 */         StringBuilder sb = new StringBuilder(length);
/* 188 */         sb.append(name);
/* 189 */         while (arrayDimension != 0) {
/* 190 */           arrayDimension--;
/* 191 */           sb.append(suffix);
/*     */         } 
/* 193 */         return sb.toString();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static String liveConnectSignature(Class<?>[] argTypes) {
/* 200 */     int N = argTypes.length;
/* 201 */     if (N == 0) return "()"; 
/* 202 */     StringBuilder sb = new StringBuilder();
/* 203 */     sb.append('(');
/* 204 */     for (int i = 0; i != N; i++) {
/* 205 */       if (i != 0) {
/* 206 */         sb.append(',');
/*     */       }
/* 208 */       sb.append(javaSignature(argTypes[i]));
/*     */     } 
/* 210 */     sb.append(')');
/* 211 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private MemberBox findExplicitFunction(String name, boolean isStatic) {
/* 216 */     int sigStart = name.indexOf('(');
/* 217 */     if (sigStart < 0) return null;
/*     */     
/* 219 */     Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
/* 220 */     MemberBox[] methodsOrCtors = null;
/* 221 */     boolean isCtor = (isStatic && sigStart == 0);
/*     */     
/* 223 */     if (isCtor) {
/*     */       
/* 225 */       methodsOrCtors = this.ctors.methods;
/*     */     } else {
/*     */       
/* 228 */       String trueName = name.substring(0, sigStart);
/* 229 */       Object obj = ht.get(trueName);
/* 230 */       if (!isStatic && obj == null)
/*     */       {
/* 232 */         obj = this.staticMembers.get(trueName);
/*     */       }
/* 234 */       if (obj instanceof NativeJavaMethod) {
/* 235 */         NativeJavaMethod njm = (NativeJavaMethod)obj;
/* 236 */         methodsOrCtors = njm.methods;
/*     */       } 
/*     */     } 
/*     */     
/* 240 */     if (methodsOrCtors != null) {
/* 241 */       for (MemberBox methodsOrCtor : methodsOrCtors) {
/* 242 */         Class<?>[] type = methodsOrCtor.argTypes;
/* 243 */         String sig = liveConnectSignature(type);
/* 244 */         if (sigStart + sig.length() == name.length() && name.regionMatches(sigStart, sig, 0, sig.length()))
/*     */         {
/*     */           
/* 247 */           return methodsOrCtor;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 252 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object getExplicitFunction(Scriptable scope, String name, Object javaObject, boolean isStatic) {
/* 258 */     Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
/* 259 */     Object member = null;
/* 260 */     MemberBox methodOrCtor = findExplicitFunction(name, isStatic);
/*     */     
/* 262 */     if (methodOrCtor != null) {
/* 263 */       Scriptable prototype = ScriptableObject.getFunctionPrototype(scope);
/*     */ 
/*     */       
/* 266 */       if (methodOrCtor.isCtor()) {
/* 267 */         NativeJavaConstructor fun = new NativeJavaConstructor(methodOrCtor);
/*     */         
/* 269 */         fun.setPrototype(prototype);
/* 270 */         member = fun;
/* 271 */         ht.put(name, fun);
/*     */       } else {
/* 273 */         String trueName = methodOrCtor.getName();
/* 274 */         member = ht.get(trueName);
/*     */         
/* 276 */         if (member instanceof NativeJavaMethod && ((NativeJavaMethod)member).methods.length > 1) {
/*     */           
/* 278 */           NativeJavaMethod fun = new NativeJavaMethod(methodOrCtor, name);
/*     */           
/* 280 */           fun.setPrototype(prototype);
/* 281 */           ht.put(name, fun);
/* 282 */           member = fun;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 287 */     return member;
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
/*     */   private static Method[] discoverAccessibleMethods(Class<?> clazz, boolean includeProtected, boolean includePrivate) {
/* 301 */     Map<MethodSignature, Method> map = new HashMap<MethodSignature, Method>();
/* 302 */     discoverAccessibleMethods(clazz, map, includeProtected, includePrivate);
/* 303 */     return (Method[])map.values().toArray((Object[])new Method[map.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void discoverAccessibleMethods(Class<?> clazz, Map<MethodSignature, Method> map, boolean includeProtected, boolean includePrivate) {
/* 310 */     if (Modifier.isPublic(clazz.getModifiers()) || includePrivate) {
/*     */       try {
/* 312 */         if (includeProtected || includePrivate) {
/* 313 */           while (clazz != null) {
/*     */             try {
/* 315 */               Method[] methods = clazz.getDeclaredMethods();
/* 316 */               for (Method method : methods) {
/* 317 */                 int mods = method.getModifiers();
/*     */                 
/* 319 */                 if (Modifier.isPublic(mods) || Modifier.isProtected(mods) || includePrivate) {
/*     */ 
/*     */                   
/* 322 */                   MethodSignature sig = new MethodSignature(method);
/* 323 */                   if (!map.containsKey(sig)) {
/* 324 */                     if (includePrivate && !method.isAccessible())
/* 325 */                       method.setAccessible(true); 
/* 326 */                     map.put(sig, method);
/*     */                   } 
/*     */                 } 
/*     */               } 
/* 330 */               clazz = clazz.getSuperclass(); continue;
/* 331 */             } catch (SecurityException e) {
/*     */ 
/*     */ 
/*     */               
/* 335 */               Method[] methods = clazz.getMethods();
/* 336 */               for (Method method : methods) {
/* 337 */                 MethodSignature sig = new MethodSignature(method);
/* 338 */                 if (!map.containsKey(sig)) {
/* 339 */                   map.put(sig, method);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             return;
/*     */           } 
/*     */         } else {
/* 346 */           Method[] methods = clazz.getMethods();
/* 347 */           for (Method method : methods) {
/* 348 */             MethodSignature sig = new MethodSignature(method);
/*     */             
/* 350 */             if (!map.containsKey(sig))
/* 351 */               map.put(sig, method); 
/*     */           } 
/*     */         } 
/*     */         return;
/* 355 */       } catch (SecurityException e) {
/* 356 */         Context.reportWarning("Could not discover accessible methods of class " + clazz.getName() + " due to lack of privileges, " + "attemping superclasses/interfaces.");
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 365 */     Class<?>[] interfaces = clazz.getInterfaces();
/* 366 */     for (Class<?> intface : interfaces) {
/* 367 */       discoverAccessibleMethods(intface, map, includeProtected, includePrivate);
/*     */     }
/*     */     
/* 370 */     Class<?> superclass = clazz.getSuperclass();
/* 371 */     if (superclass != null) {
/* 372 */       discoverAccessibleMethods(superclass, map, includeProtected, includePrivate);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class MethodSignature
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final Class<?>[] args;
/*     */     
/*     */     private MethodSignature(String name, Class<?>[] args) {
/* 384 */       this.name = name;
/* 385 */       this.args = args;
/*     */     }
/*     */ 
/*     */     
/*     */     MethodSignature(Method method) {
/* 390 */       this(method.getName(), method.getParameterTypes());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 396 */       if (o instanceof MethodSignature) {
/*     */         
/* 398 */         MethodSignature ms = (MethodSignature)o;
/* 399 */         return (ms.name.equals(this.name) && Arrays.equals((Object[])this.args, (Object[])ms.args));
/*     */       } 
/* 401 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 407 */       return this.name.hashCode() ^ this.args.length;
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
/*     */   private void reflect(Scriptable scope, boolean includeProtected, boolean includePrivate) {
/* 419 */     Method[] methods = discoverAccessibleMethods(this.cl, includeProtected, includePrivate);
/*     */     
/* 421 */     for (Method method : methods) {
/* 422 */       int mods = method.getModifiers();
/* 423 */       boolean isStatic = Modifier.isStatic(mods);
/* 424 */       Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
/* 425 */       String name = method.getName();
/* 426 */       Object value = ht.get(name);
/* 427 */       if (value == null) {
/* 428 */         ht.put(name, method);
/*     */       } else {
/*     */         ObjArray overloadedMethods;
/* 431 */         if (value instanceof ObjArray) {
/* 432 */           overloadedMethods = (ObjArray)value;
/*     */         } else {
/* 434 */           if (!(value instanceof Method)) Kit.codeBug();
/*     */ 
/*     */           
/* 437 */           overloadedMethods = new ObjArray();
/* 438 */           overloadedMethods.add(value);
/* 439 */           ht.put(name, overloadedMethods);
/*     */         } 
/* 441 */         overloadedMethods.add(method);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 447 */     for (int tableCursor = 0; tableCursor != 2; tableCursor++) {
/* 448 */       boolean isStatic = (tableCursor == 0);
/* 449 */       Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
/* 450 */       for (Map.Entry<String, Object> entry : ht.entrySet()) {
/*     */         MemberBox[] methodBoxes;
/* 452 */         Object value = entry.getValue();
/* 453 */         if (value instanceof Method) {
/* 454 */           methodBoxes = new MemberBox[1];
/* 455 */           methodBoxes[0] = new MemberBox((Method)value);
/*     */         } else {
/* 457 */           ObjArray overloadedMethods = (ObjArray)value;
/* 458 */           int N = overloadedMethods.size();
/* 459 */           if (N < 2) Kit.codeBug(); 
/* 460 */           methodBoxes = new MemberBox[N];
/* 461 */           for (int k = 0; k != N; k++) {
/* 462 */             Method method = (Method)overloadedMethods.get(k);
/* 463 */             methodBoxes[k] = new MemberBox(method);
/*     */           } 
/*     */         } 
/* 466 */         NativeJavaMethod fun = new NativeJavaMethod(methodBoxes);
/* 467 */         if (scope != null) {
/* 468 */           ScriptRuntime.setFunctionProtoAndParent(fun, scope);
/*     */         }
/* 470 */         ht.put(entry.getKey(), fun);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 475 */     Field[] fields = getAccessibleFields(includeProtected, includePrivate);
/* 476 */     for (Field field : fields) {
/* 477 */       String name = field.getName();
/* 478 */       int mods = field.getModifiers();
/*     */       try {
/* 480 */         boolean isStatic = Modifier.isStatic(mods);
/* 481 */         Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
/* 482 */         Object member = ht.get(name);
/* 483 */         if (member == null) {
/* 484 */           ht.put(name, field);
/* 485 */         } else if (member instanceof NativeJavaMethod) {
/* 486 */           NativeJavaMethod method = (NativeJavaMethod)member;
/* 487 */           FieldAndMethods fam = new FieldAndMethods(scope, method.methods, field);
/*     */           
/* 489 */           Map<String, FieldAndMethods> fmht = isStatic ? this.staticFieldAndMethods : this.fieldAndMethods;
/*     */           
/* 491 */           if (fmht == null) {
/* 492 */             fmht = new HashMap<String, FieldAndMethods>();
/* 493 */             if (isStatic) {
/* 494 */               this.staticFieldAndMethods = fmht;
/*     */             } else {
/* 496 */               this.fieldAndMethods = fmht;
/*     */             } 
/*     */           } 
/* 499 */           fmht.put(name, fam);
/* 500 */           ht.put(name, fam);
/* 501 */         } else if (member instanceof Field) {
/* 502 */           Field oldField = (Field)member;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 509 */           if (oldField.getDeclaringClass().isAssignableFrom(field.getDeclaringClass()))
/*     */           {
/*     */             
/* 512 */             ht.put(name, field);
/*     */           }
/*     */         } else {
/*     */           
/* 516 */           Kit.codeBug();
/*     */         } 
/* 518 */       } catch (SecurityException e) {
/*     */         
/* 520 */         Context.reportWarning("Could not access field " + name + " of class " + this.cl.getName() + " due to lack of privileges.");
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 528 */     for (int j = 0; j != 2; j++) {
/* 529 */       boolean isStatic = (j == 0);
/* 530 */       Map<String, Object> ht = isStatic ? this.staticMembers : this.members;
/*     */       
/* 532 */       Map<String, BeanProperty> toAdd = new HashMap<String, BeanProperty>();
/*     */ 
/*     */       
/* 535 */       for (String name : ht.keySet()) {
/*     */         
/* 537 */         boolean memberIsGetMethod = name.startsWith("get");
/* 538 */         boolean memberIsSetMethod = name.startsWith("set");
/* 539 */         boolean memberIsIsMethod = name.startsWith("is");
/* 540 */         if (memberIsGetMethod || memberIsIsMethod || memberIsSetMethod) {
/*     */ 
/*     */           
/* 543 */           String nameComponent = name.substring(memberIsIsMethod ? 2 : 3);
/*     */           
/* 545 */           if (nameComponent.length() == 0) {
/*     */             continue;
/*     */           }
/*     */           
/* 549 */           String beanPropertyName = nameComponent;
/* 550 */           char ch0 = nameComponent.charAt(0);
/* 551 */           if (Character.isUpperCase(ch0)) {
/* 552 */             if (nameComponent.length() == 1) {
/* 553 */               beanPropertyName = nameComponent.toLowerCase();
/*     */             } else {
/* 555 */               char ch1 = nameComponent.charAt(1);
/* 556 */               if (!Character.isUpperCase(ch1)) {
/* 557 */                 beanPropertyName = Character.toLowerCase(ch0) + nameComponent.substring(1);
/*     */               }
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 565 */           if (toAdd.containsKey(beanPropertyName))
/*     */             continue; 
/* 567 */           Object v = ht.get(beanPropertyName);
/* 568 */           if (v != null)
/*     */           {
/* 570 */             if (!includePrivate || !(v instanceof Member) || !Modifier.isPrivate(((Member)v).getModifiers())) {
/*     */               continue;
/*     */             }
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 580 */           MemberBox getter = null;
/* 581 */           getter = findGetter(isStatic, ht, "get", nameComponent);
/*     */           
/* 583 */           if (getter == null) {
/* 584 */             getter = findGetter(isStatic, ht, "is", nameComponent);
/*     */           }
/*     */ 
/*     */           
/* 588 */           MemberBox setter = null;
/* 589 */           NativeJavaMethod setters = null;
/* 590 */           String setterName = "set".concat(nameComponent);
/*     */           
/* 592 */           if (ht.containsKey(setterName)) {
/*     */             
/* 594 */             Object member = ht.get(setterName);
/* 595 */             if (member instanceof NativeJavaMethod) {
/* 596 */               NativeJavaMethod njmSet = (NativeJavaMethod)member;
/* 597 */               if (getter != null) {
/*     */ 
/*     */                 
/* 600 */                 Class<?> type = getter.method().getReturnType();
/* 601 */                 setter = extractSetMethod(type, njmSet.methods, isStatic);
/*     */               }
/*     */               else {
/*     */                 
/* 605 */                 setter = extractSetMethod(njmSet.methods, isStatic);
/*     */               } 
/*     */               
/* 608 */               if (njmSet.methods.length > 1) {
/* 609 */                 setters = njmSet;
/*     */               }
/*     */             } 
/*     */           } 
/*     */           
/* 614 */           BeanProperty bp = new BeanProperty(getter, setter, setters);
/*     */           
/* 616 */           toAdd.put(beanPropertyName, bp);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 621 */       for (String key : toAdd.keySet()) {
/* 622 */         Object value = toAdd.get(key);
/* 623 */         ht.put(key, value);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 628 */     Constructor[] arrayOfConstructor = (Constructor[])getAccessibleConstructors(includePrivate);
/* 629 */     MemberBox[] ctorMembers = new MemberBox[arrayOfConstructor.length];
/* 630 */     for (int i = 0; i != arrayOfConstructor.length; i++) {
/* 631 */       ctorMembers[i] = new MemberBox(arrayOfConstructor[i]);
/*     */     }
/* 633 */     this.ctors = new NativeJavaMethod(ctorMembers, this.cl.getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Constructor<?>[] getAccessibleConstructors(boolean includePrivate) {
/* 640 */     if (includePrivate && this.cl != ScriptRuntime.ClassClass) {
/*     */       try {
/* 642 */         Constructor[] arrayOfConstructor = (Constructor[])this.cl.getDeclaredConstructors();
/* 643 */         AccessibleObject.setAccessible((AccessibleObject[])arrayOfConstructor, true);
/*     */         
/* 645 */         return (Constructor<?>[])arrayOfConstructor;
/* 646 */       } catch (SecurityException e) {
/*     */         
/* 648 */         Context.reportWarning("Could not access constructor  of class " + this.cl.getName() + " due to lack of privileges.");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 653 */     return this.cl.getConstructors();
/*     */   }
/*     */ 
/*     */   
/*     */   private Field[] getAccessibleFields(boolean includeProtected, boolean includePrivate) {
/* 658 */     if (includePrivate || includeProtected) {
/*     */       try {
/* 660 */         List<Field> fieldsList = new ArrayList<Field>();
/* 661 */         Class<?> currentClass = this.cl;
/*     */         
/* 663 */         while (currentClass != null) {
/*     */ 
/*     */           
/* 666 */           Field[] declared = currentClass.getDeclaredFields();
/* 667 */           for (Field field : declared) {
/* 668 */             int mod = field.getModifiers();
/* 669 */             if (includePrivate || Modifier.isPublic(mod) || Modifier.isProtected(mod)) {
/* 670 */               if (!field.isAccessible())
/* 671 */                 field.setAccessible(true); 
/* 672 */               fieldsList.add(field);
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 677 */           currentClass = currentClass.getSuperclass();
/*     */         } 
/*     */         
/* 680 */         return fieldsList.<Field>toArray(new Field[fieldsList.size()]);
/* 681 */       } catch (SecurityException e) {}
/*     */     }
/*     */ 
/*     */     
/* 685 */     return this.cl.getFields();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MemberBox findGetter(boolean isStatic, Map<String, Object> ht, String prefix, String propertyName) {
/* 691 */     String getterName = prefix.concat(propertyName);
/* 692 */     if (ht.containsKey(getterName)) {
/*     */       
/* 694 */       Object member = ht.get(getterName);
/* 695 */       if (member instanceof NativeJavaMethod) {
/* 696 */         NativeJavaMethod njmGet = (NativeJavaMethod)member;
/* 697 */         return extractGetMethod(njmGet.methods, isStatic);
/*     */       } 
/*     */     } 
/* 700 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MemberBox extractGetMethod(MemberBox[] methods, boolean isStatic) {
/* 708 */     for (MemberBox method : methods) {
/*     */ 
/*     */       
/* 711 */       if (method.argTypes.length == 0 && (!isStatic || method.isStatic())) {
/* 712 */         Class<?> type = method.method().getReturnType();
/* 713 */         if (type != void.class) {
/* 714 */           return method;
/*     */         }
/*     */         break;
/*     */       } 
/*     */     } 
/* 719 */     return null;
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
/*     */   private static MemberBox extractSetMethod(Class<?> type, MemberBox[] methods, boolean isStatic) {
/* 733 */     for (int pass = 1; pass <= 2; pass++) {
/* 734 */       for (MemberBox method : methods) {
/* 735 */         if (!isStatic || method.isStatic()) {
/* 736 */           Class<?>[] params = method.argTypes;
/* 737 */           if (params.length == 1) {
/* 738 */             if (pass == 1) {
/* 739 */               if (params[0] == type) {
/* 740 */                 return method;
/*     */               }
/*     */             } else {
/* 743 */               if (pass != 2) Kit.codeBug(); 
/* 744 */               if (params[0].isAssignableFrom(type)) {
/* 745 */                 return method;
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 752 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MemberBox extractSetMethod(MemberBox[] methods, boolean isStatic) {
/* 759 */     for (MemberBox method : methods) {
/* 760 */       if ((!isStatic || method.isStatic()) && 
/* 761 */         method.method().getReturnType() == void.class && 
/* 762 */         method.argTypes.length == 1) {
/* 763 */         return method;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 768 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Map<String, FieldAndMethods> getFieldAndMethodsObjects(Scriptable scope, Object javaObject, boolean isStatic) {
/* 774 */     Map<String, FieldAndMethods> ht = isStatic ? this.staticFieldAndMethods : this.fieldAndMethods;
/* 775 */     if (ht == null)
/* 776 */       return null; 
/* 777 */     int len = ht.size();
/* 778 */     Map<String, FieldAndMethods> result = new HashMap<String, FieldAndMethods>(len);
/* 779 */     for (FieldAndMethods fam : ht.values()) {
/* 780 */       FieldAndMethods famNew = new FieldAndMethods(scope, fam.methods, fam.field);
/*     */       
/* 782 */       famNew.javaObject = javaObject;
/* 783 */       result.put(fam.field.getName(), famNew);
/*     */     } 
/* 785 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static JavaMembers lookupClass(Scriptable scope, Class<?> dynamicType, Class<?> staticType, boolean includeProtected) {
/*     */     JavaMembers members;
/* 792 */     ClassCache cache = ClassCache.get(scope);
/* 793 */     Map<Class<?>, JavaMembers> ct = cache.getClassCacheMap();
/*     */     
/* 795 */     Class<?> cl = dynamicType;
/*     */     while (true) {
/* 797 */       members = ct.get(cl);
/* 798 */       if (members != null) {
/* 799 */         if (cl != dynamicType)
/*     */         {
/*     */           
/* 802 */           ct.put(dynamicType, members);
/*     */         }
/* 804 */         return members;
/*     */       } 
/*     */       try {
/* 807 */         members = new JavaMembers(cache.getAssociatedScope(), cl, includeProtected);
/*     */         
/*     */         break;
/* 810 */       } catch (SecurityException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 815 */         if (staticType != null && staticType.isInterface()) {
/* 816 */           cl = staticType;
/* 817 */           staticType = null; continue;
/*     */         } 
/* 819 */         Class<?> parent = cl.getSuperclass();
/* 820 */         if (parent == null) {
/* 821 */           if (cl.isInterface()) {
/*     */             
/* 823 */             parent = ScriptRuntime.ObjectClass;
/*     */           } else {
/* 825 */             throw e;
/*     */           } 
/*     */         }
/* 828 */         cl = parent;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 833 */     if (cache.isCachingEnabled()) {
/* 834 */       ct.put(cl, members);
/* 835 */       if (cl != dynamicType)
/*     */       {
/*     */         
/* 838 */         ct.put(dynamicType, members);
/*     */       }
/*     */     } 
/* 841 */     return members;
/*     */   }
/*     */ 
/*     */   
/*     */   RuntimeException reportMemberNotFound(String memberName) {
/* 846 */     return Context.reportRuntimeError2("msg.java.member.not.found", this.cl.getName(), memberName);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\JavaMembers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */