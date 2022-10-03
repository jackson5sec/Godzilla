/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NativeJavaMethod
/*     */   extends BaseFunction
/*     */ {
/*     */   static final long serialVersionUID = -3440381785576412928L;
/*     */   private static final int PREFERENCE_EQUAL = 0;
/*     */   private static final int PREFERENCE_FIRST_ARG = 1;
/*     */   private static final int PREFERENCE_SECOND_ARG = 2;
/*     */   private static final int PREFERENCE_AMBIGUOUS = 3;
/*     */   private static final boolean debug = false;
/*     */   MemberBox[] methods;
/*     */   private String functionName;
/*     */   private transient CopyOnWriteArrayList<ResolvedOverload> overloadCache;
/*     */   
/*     */   NativeJavaMethod(MemberBox[] methods) {
/*  29 */     this.functionName = methods[0].getName();
/*  30 */     this.methods = methods;
/*     */   }
/*     */ 
/*     */   
/*     */   NativeJavaMethod(MemberBox[] methods, String name) {
/*  35 */     this.functionName = name;
/*  36 */     this.methods = methods;
/*     */   }
/*     */ 
/*     */   
/*     */   NativeJavaMethod(MemberBox method, String name) {
/*  41 */     this.functionName = name;
/*  42 */     this.methods = new MemberBox[] { method };
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeJavaMethod(Method method, String name) {
/*  47 */     this(new MemberBox(method), name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFunctionName() {
/*  53 */     return this.functionName;
/*     */   }
/*     */ 
/*     */   
/*     */   static String scriptSignature(Object[] values) {
/*  58 */     StringBuilder sig = new StringBuilder();
/*  59 */     for (int i = 0; i != values.length; i++) {
/*  60 */       String s; Object value = values[i];
/*     */ 
/*     */       
/*  63 */       if (value == null) {
/*  64 */         s = "null";
/*  65 */       } else if (value instanceof Boolean) {
/*  66 */         s = "boolean";
/*  67 */       } else if (value instanceof String) {
/*  68 */         s = "string";
/*  69 */       } else if (value instanceof Number) {
/*  70 */         s = "number";
/*  71 */       } else if (value instanceof Scriptable) {
/*  72 */         if (value instanceof Undefined) {
/*  73 */           s = "undefined";
/*  74 */         } else if (value instanceof Wrapper) {
/*  75 */           Object wrapped = ((Wrapper)value).unwrap();
/*  76 */           s = wrapped.getClass().getName();
/*  77 */         } else if (value instanceof Function) {
/*  78 */           s = "function";
/*     */         } else {
/*  80 */           s = "object";
/*     */         } 
/*     */       } else {
/*  83 */         s = JavaMembers.javaSignature(value.getClass());
/*     */       } 
/*     */       
/*  86 */       if (i != 0) {
/*  87 */         sig.append(',');
/*     */       }
/*  89 */       sig.append(s);
/*     */     } 
/*  91 */     return sig.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String decompile(int indent, int flags) {
/*  97 */     StringBuilder sb = new StringBuilder();
/*  98 */     boolean justbody = (0 != (flags & 0x1));
/*  99 */     if (!justbody) {
/* 100 */       sb.append("function ");
/* 101 */       sb.append(getFunctionName());
/* 102 */       sb.append("() {");
/*     */     } 
/* 104 */     sb.append("/*\n");
/* 105 */     sb.append(toString());
/* 106 */     sb.append(justbody ? "*/\n" : "*/}\n");
/* 107 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     StringBuilder sb = new StringBuilder();
/* 114 */     for (int i = 0, N = this.methods.length; i != N; i++) {
/*     */       
/* 116 */       if (this.methods[i].isMethod()) {
/* 117 */         Method method = this.methods[i].method();
/* 118 */         sb.append(JavaMembers.javaSignature(method.getReturnType()));
/* 119 */         sb.append(' ');
/* 120 */         sb.append(method.getName());
/*     */       } else {
/* 122 */         sb.append(this.methods[i].getName());
/*     */       } 
/* 124 */       sb.append(JavaMembers.liveConnectSignature((this.methods[i]).argTypes));
/* 125 */       sb.append('\n');
/*     */     } 
/* 127 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*     */     Object javaObject;
/* 135 */     if (this.methods.length == 0) {
/* 136 */       throw new RuntimeException("No methods defined for call");
/*     */     }
/*     */     
/* 139 */     int index = findCachedFunction(cx, args);
/* 140 */     if (index < 0) {
/* 141 */       Class<?> c = this.methods[0].method().getDeclaringClass();
/* 142 */       String sig = c.getName() + '.' + getFunctionName() + '(' + scriptSignature(args) + ')';
/*     */       
/* 144 */       throw Context.reportRuntimeError1("msg.java.no_such_method", sig);
/*     */     } 
/*     */     
/* 147 */     MemberBox meth = this.methods[index];
/* 148 */     Class<?>[] argTypes = meth.argTypes;
/*     */     
/* 150 */     if (meth.vararg) {
/*     */       
/* 152 */       Object varArgs, newArgs[] = new Object[argTypes.length];
/* 153 */       for (int i = 0; i < argTypes.length - 1; i++) {
/* 154 */         newArgs[i] = Context.jsToJava(args[i], argTypes[i]);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 161 */       if (args.length == argTypes.length && (args[args.length - 1] == null || args[args.length - 1] instanceof NativeArray || args[args.length - 1] instanceof NativeJavaArray)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 167 */         varArgs = Context.jsToJava(args[args.length - 1], argTypes[argTypes.length - 1]);
/*     */       }
/*     */       else {
/*     */         
/* 171 */         Class<?> componentType = argTypes[argTypes.length - 1].getComponentType();
/*     */         
/* 173 */         varArgs = Array.newInstance(componentType, args.length - argTypes.length + 1);
/*     */         
/* 175 */         for (int j = 0; j < Array.getLength(varArgs); j++) {
/* 176 */           Object value = Context.jsToJava(args[argTypes.length - 1 + j], componentType);
/*     */           
/* 178 */           Array.set(varArgs, j, value);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 183 */       newArgs[argTypes.length - 1] = varArgs;
/*     */       
/* 185 */       args = newArgs;
/*     */     } else {
/*     */       
/* 188 */       Object[] origArgs = args;
/* 189 */       for (int i = 0; i < args.length; i++) {
/* 190 */         Object arg = args[i];
/* 191 */         Object coerced = Context.jsToJava(arg, argTypes[i]);
/* 192 */         if (coerced != arg) {
/* 193 */           if (origArgs == args) {
/* 194 */             args = (Object[])args.clone();
/*     */           }
/* 196 */           args[i] = coerced;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 201 */     if (meth.isStatic()) {
/* 202 */       javaObject = null;
/*     */     } else {
/* 204 */       Scriptable o = thisObj;
/* 205 */       Class<?> c = meth.getDeclaringClass();
/*     */       while (true) {
/* 207 */         if (o == null) {
/* 208 */           throw Context.reportRuntimeError3("msg.nonjava.method", getFunctionName(), ScriptRuntime.toString(thisObj), c.getName());
/*     */         }
/*     */ 
/*     */         
/* 212 */         if (o instanceof Wrapper) {
/* 213 */           javaObject = ((Wrapper)o).unwrap();
/* 214 */           if (c.isInstance(javaObject)) {
/*     */             break;
/*     */           }
/*     */         } 
/* 218 */         o = o.getPrototype();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     Object retval = meth.invoke(javaObject, args);
/* 226 */     Class<?> staticType = meth.method().getReturnType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     Object wrapped = cx.getWrapFactory().wrap(cx, scope, retval, staticType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     if (wrapped == null && staticType == void.class) {
/* 246 */       wrapped = Undefined.instance;
/*     */     }
/* 248 */     return wrapped;
/*     */   }
/*     */   
/*     */   int findCachedFunction(Context cx, Object[] args) {
/* 252 */     if (this.methods.length > 1) {
/* 253 */       if (this.overloadCache != null) {
/* 254 */         for (ResolvedOverload ovl : this.overloadCache) {
/* 255 */           if (ovl.matches(args)) {
/* 256 */             return ovl.index;
/*     */           }
/*     */         } 
/*     */       } else {
/* 260 */         this.overloadCache = new CopyOnWriteArrayList<ResolvedOverload>();
/*     */       } 
/* 262 */       int index = findFunction(cx, this.methods, args);
/*     */ 
/*     */       
/* 265 */       if (this.overloadCache.size() < this.methods.length * 2) {
/* 266 */         synchronized (this.overloadCache) {
/* 267 */           ResolvedOverload ovl = new ResolvedOverload(args, index);
/* 268 */           if (!this.overloadCache.contains(ovl)) {
/* 269 */             this.overloadCache.add(0, ovl);
/*     */           }
/*     */         } 
/*     */       }
/* 273 */       return index;
/*     */     } 
/* 275 */     return findFunction(cx, this.methods, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int findFunction(Context cx, MemberBox[] methodsOrCtors, Object[] args) {
/* 286 */     if (methodsOrCtors.length == 0)
/* 287 */       return -1; 
/* 288 */     if (methodsOrCtors.length == 1) {
/* 289 */       MemberBox member = methodsOrCtors[0];
/* 290 */       Class<?>[] argTypes = member.argTypes;
/* 291 */       int alength = argTypes.length;
/*     */       
/* 293 */       if (member.vararg) {
/* 294 */         alength--;
/* 295 */         if (alength > args.length) {
/* 296 */           return -1;
/*     */         }
/*     */       }
/* 299 */       else if (alength != args.length) {
/* 300 */         return -1;
/*     */       } 
/*     */       
/* 303 */       for (int k = 0; k != alength; k++) {
/* 304 */         if (!NativeJavaObject.canConvert(args[k], argTypes[k]))
/*     */         {
/*     */           
/* 307 */           return -1;
/*     */         }
/*     */       } 
/*     */       
/* 311 */       return 0;
/*     */     } 
/*     */     
/* 314 */     int firstBestFit = -1;
/* 315 */     int[] extraBestFits = null;
/* 316 */     int extraBestFitsCount = 0;
/*     */     
/*     */     int i;
/* 319 */     label104: for (i = 0; i < methodsOrCtors.length; i++) {
/* 320 */       MemberBox member = methodsOrCtors[i];
/* 321 */       Class<?>[] argTypes = member.argTypes;
/* 322 */       int alength = argTypes.length;
/*     */       
/* 324 */       alength--;
/* 325 */       if (member.vararg ? (alength > args.length) : (
/*     */ 
/*     */ 
/*     */         
/* 329 */         alength != args.length)) {
/*     */ 
/*     */ 
/*     */         
/* 333 */         for (int k = 0; k < alength; k++) {
/* 334 */           if (!NativeJavaObject.canConvert(args[k], argTypes[k])) {
/*     */             continue label104;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 340 */         if (firstBestFit < 0) {
/*     */           
/* 342 */           firstBestFit = i;
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 348 */           int betterCount = 0;
/*     */           
/* 350 */           int worseCount = 0;
/*     */           
/* 352 */           for (int m = -1; m != extraBestFitsCount; m++) {
/*     */             int bestFitIndex;
/* 354 */             if (m == -1) {
/* 355 */               bestFitIndex = firstBestFit;
/*     */             } else {
/* 357 */               bestFitIndex = extraBestFits[m];
/*     */             } 
/* 359 */             MemberBox bestFit = methodsOrCtors[bestFitIndex];
/* 360 */             if (cx.hasFeature(13) && (bestFit.member().getModifiers() & 0x1) != (member.member().getModifiers() & 0x1))
/*     */             
/*     */             { 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 367 */               if ((bestFit.member().getModifiers() & 0x1) == 0) {
/* 368 */                 betterCount++;
/*     */               } else {
/* 370 */                 worseCount++;
/*     */               }  }
/* 372 */             else { int preference = preferSignature(args, argTypes, member.vararg, bestFit.argTypes, bestFit.vararg);
/*     */ 
/*     */ 
/*     */               
/* 376 */               if (preference == 3)
/*     */                 break; 
/* 378 */               if (preference == 1) {
/* 379 */                 betterCount++;
/* 380 */               } else if (preference == 2) {
/* 381 */                 worseCount++;
/*     */               } else {
/* 383 */                 if (preference != 0) Kit.codeBug();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 389 */                 if (bestFit.isStatic()) { if (bestFit.getDeclaringClass().isAssignableFrom(member.getDeclaringClass())) {
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
/* 400 */                     if (m == -1) {
/* 401 */                       firstBestFit = i; continue label104;
/*     */                     } 
/* 403 */                     extraBestFits[m] = i;
/*     */                     
/*     */                     continue label104;
/*     */                   } 
/*     */                   
/*     */                   continue label104; }
/*     */                 
/*     */                 continue label104;
/*     */               }  }
/*     */           
/*     */           } 
/* 414 */           if (betterCount == 1 + extraBestFitsCount) {
/*     */ 
/*     */ 
/*     */             
/* 418 */             firstBestFit = i;
/* 419 */             extraBestFitsCount = 0;
/* 420 */           } else if (worseCount != 1 + extraBestFitsCount) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 428 */             if (extraBestFits == null)
/*     */             {
/* 430 */               extraBestFits = new int[methodsOrCtors.length - 1];
/*     */             }
/* 432 */             extraBestFits[extraBestFitsCount] = i;
/* 433 */             extraBestFitsCount++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 438 */     if (firstBestFit < 0)
/*     */     {
/* 440 */       return -1; } 
/* 441 */     if (extraBestFitsCount == 0)
/*     */     {
/* 443 */       return firstBestFit;
/*     */     }
/*     */ 
/*     */     
/* 447 */     StringBuilder buf = new StringBuilder();
/* 448 */     for (int j = -1; j != extraBestFitsCount; j++) {
/*     */       int bestFitIndex;
/* 450 */       if (j == -1) {
/* 451 */         bestFitIndex = firstBestFit;
/*     */       } else {
/* 453 */         bestFitIndex = extraBestFits[j];
/*     */       } 
/* 455 */       buf.append("\n    ");
/* 456 */       buf.append(methodsOrCtors[bestFitIndex].toJavaDeclaration());
/*     */     } 
/*     */     
/* 459 */     MemberBox firstFitMember = methodsOrCtors[firstBestFit];
/* 460 */     String memberName = firstFitMember.getName();
/* 461 */     String memberClass = firstFitMember.getDeclaringClass().getName();
/*     */     
/* 463 */     if (methodsOrCtors[0].isCtor()) {
/* 464 */       throw Context.reportRuntimeError3("msg.constructor.ambiguous", memberName, scriptSignature(args), buf.toString());
/*     */     }
/*     */ 
/*     */     
/* 468 */     throw Context.reportRuntimeError4("msg.method.ambiguous", memberClass, memberName, scriptSignature(args), buf.toString());
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
/*     */   private static int preferSignature(Object[] args, Class<?>[] sig1, boolean vararg1, Class<?>[] sig2, boolean vararg2) {
/* 492 */     int totalPreference = 0;
/* 493 */     for (int j = 0; j < args.length; j++) {
/* 494 */       Class<?> type1 = (vararg1 && j >= sig1.length) ? sig1[sig1.length - 1] : sig1[j];
/* 495 */       Class<?> type2 = (vararg2 && j >= sig2.length) ? sig2[sig2.length - 1] : sig2[j];
/* 496 */       if (type1 != type2) {
/*     */         int preference;
/*     */         
/* 499 */         Object arg = args[j];
/*     */ 
/*     */ 
/*     */         
/* 503 */         int rank1 = NativeJavaObject.getConversionWeight(arg, type1);
/* 504 */         int rank2 = NativeJavaObject.getConversionWeight(arg, type2);
/*     */ 
/*     */         
/* 507 */         if (rank1 < rank2) {
/* 508 */           preference = 1;
/* 509 */         } else if (rank1 > rank2) {
/* 510 */           preference = 2;
/*     */         
/*     */         }
/* 513 */         else if (rank1 == 0) {
/* 514 */           if (type1.isAssignableFrom(type2)) {
/* 515 */             preference = 2;
/* 516 */           } else if (type2.isAssignableFrom(type1)) {
/* 517 */             preference = 1;
/*     */           } else {
/* 519 */             preference = 3;
/*     */           } 
/*     */         } else {
/* 522 */           preference = 3;
/*     */         } 
/*     */ 
/*     */         
/* 526 */         totalPreference |= preference;
/*     */         
/* 528 */         if (totalPreference == 3)
/*     */           break; 
/*     */       } 
/*     */     } 
/* 532 */     return totalPreference;
/*     */   }
/*     */   
/*     */   private static void printDebug(String msg, MemberBox member, Object[] args) {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeJavaMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */