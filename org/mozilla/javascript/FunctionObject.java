/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ 
/*     */ 
/*     */ public class FunctionObject
/*     */   extends BaseFunction
/*     */ {
/*     */   static final long serialVersionUID = -5332312783643935019L;
/*     */   private static final short VARARGS_METHOD = -1;
/*     */   private static final short VARARGS_CTOR = -2;
/*     */   private static boolean sawSecurityException;
/*     */   public static final int JAVA_UNSUPPORTED_TYPE = 0;
/*     */   public static final int JAVA_STRING_TYPE = 1;
/*     */   public static final int JAVA_INT_TYPE = 2;
/*     */   public static final int JAVA_BOOLEAN_TYPE = 3;
/*     */   public static final int JAVA_DOUBLE_TYPE = 4;
/*     */   public static final int JAVA_SCRIPTABLE_TYPE = 5;
/*     */   public static final int JAVA_OBJECT_TYPE = 6;
/*     */   MemberBox member;
/*     */   private String functionName;
/*     */   private transient byte[] typeTags;
/*     */   private int parmsLength;
/*     */   private transient boolean hasVoidReturn;
/*     */   private transient int returnTypeTag;
/*     */   private boolean isStatic;
/*     */   
/*     */   public FunctionObject(String name, Member methodOrConstructor, Scriptable scope) {
/*  85 */     if (methodOrConstructor instanceof Constructor) {
/*  86 */       this.member = new MemberBox((Constructor)methodOrConstructor);
/*  87 */       this.isStatic = true;
/*     */     } else {
/*  89 */       this.member = new MemberBox((Method)methodOrConstructor);
/*  90 */       this.isStatic = this.member.isStatic();
/*     */     } 
/*  92 */     String methodName = this.member.getName();
/*  93 */     this.functionName = name;
/*  94 */     Class<?>[] types = this.member.argTypes;
/*  95 */     int arity = types.length;
/*  96 */     if (arity == 4 && (types[1].isArray() || types[2].isArray())) {
/*     */       
/*  98 */       if (types[1].isArray()) {
/*  99 */         if (!this.isStatic || types[0] != ScriptRuntime.ContextClass || types[1].getComponentType() != ScriptRuntime.ObjectClass || types[2] != ScriptRuntime.FunctionClass || types[3] != boolean.class)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 105 */           throw Context.reportRuntimeError1("msg.varargs.ctor", methodName);
/*     */         }
/*     */         
/* 108 */         this.parmsLength = -2;
/*     */       } else {
/* 110 */         if (!this.isStatic || types[0] != ScriptRuntime.ContextClass || types[1] != ScriptRuntime.ScriptableClass || types[2].getComponentType() != ScriptRuntime.ObjectClass || types[3] != ScriptRuntime.FunctionClass)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 116 */           throw Context.reportRuntimeError1("msg.varargs.fun", methodName);
/*     */         }
/*     */         
/* 119 */         this.parmsLength = -1;
/*     */       } 
/*     */     } else {
/* 122 */       this.parmsLength = arity;
/* 123 */       if (arity > 0) {
/* 124 */         this.typeTags = new byte[arity];
/* 125 */         for (int i = 0; i != arity; i++) {
/* 126 */           int tag = getTypeTag(types[i]);
/* 127 */           if (tag == 0) {
/* 128 */             throw Context.reportRuntimeError2("msg.bad.parms", types[i].getName(), methodName);
/*     */           }
/*     */           
/* 131 */           this.typeTags[i] = (byte)tag;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 136 */     if (this.member.isMethod()) {
/* 137 */       Method method = this.member.method();
/* 138 */       Class<?> returnType = method.getReturnType();
/* 139 */       if (returnType == void.class) {
/* 140 */         this.hasVoidReturn = true;
/*     */       } else {
/* 142 */         this.returnTypeTag = getTypeTag(returnType);
/*     */       } 
/*     */     } else {
/* 145 */       Class<?> ctorType = this.member.getDeclaringClass();
/* 146 */       if (!ScriptRuntime.ScriptableClass.isAssignableFrom(ctorType)) {
/* 147 */         throw Context.reportRuntimeError1("msg.bad.ctor.return", ctorType.getName());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 152 */     ScriptRuntime.setFunctionProtoAndParent(this, scope);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTypeTag(Class<?> type) {
/* 162 */     if (type == ScriptRuntime.StringClass)
/* 163 */       return 1; 
/* 164 */     if (type == ScriptRuntime.IntegerClass || type == int.class)
/* 165 */       return 2; 
/* 166 */     if (type == ScriptRuntime.BooleanClass || type == boolean.class)
/* 167 */       return 3; 
/* 168 */     if (type == ScriptRuntime.DoubleClass || type == double.class)
/* 169 */       return 4; 
/* 170 */     if (ScriptRuntime.ScriptableClass.isAssignableFrom(type))
/* 171 */       return 5; 
/* 172 */     if (type == ScriptRuntime.ObjectClass) {
/* 173 */       return 6;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 178 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object convertArg(Context cx, Scriptable scope, Object arg, int typeTag) {
/* 184 */     switch (typeTag) {
/*     */       case 1:
/* 186 */         if (arg instanceof String)
/* 187 */           return arg; 
/* 188 */         return ScriptRuntime.toString(arg);
/*     */       case 2:
/* 190 */         if (arg instanceof Integer)
/* 191 */           return arg; 
/* 192 */         return Integer.valueOf(ScriptRuntime.toInt32(arg));
/*     */       case 3:
/* 194 */         if (arg instanceof Boolean)
/* 195 */           return arg; 
/* 196 */         return ScriptRuntime.toBoolean(arg) ? Boolean.TRUE : Boolean.FALSE;
/*     */       
/*     */       case 4:
/* 199 */         if (arg instanceof Double)
/* 200 */           return arg; 
/* 201 */         return new Double(ScriptRuntime.toNumber(arg));
/*     */       case 5:
/* 203 */         return ScriptRuntime.toObjectOrNull(cx, arg, scope);
/*     */       case 6:
/* 205 */         return arg;
/*     */     } 
/* 207 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArity() {
/* 218 */     return (this.parmsLength < 0) ? 1 : this.parmsLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/* 226 */     return getArity();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFunctionName() {
/* 232 */     return (this.functionName == null) ? "" : this.functionName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMethodOrConstructor() {
/* 240 */     if (this.member.isMethod()) {
/* 241 */       return this.member.method();
/*     */     }
/* 243 */     return this.member.ctor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Method findSingleMethod(Method[] methods, String name) {
/* 249 */     Method found = null;
/* 250 */     for (int i = 0, N = methods.length; i != N; i++) {
/* 251 */       Method method = methods[i];
/* 252 */       if (method != null && name.equals(method.getName())) {
/* 253 */         if (found != null) {
/* 254 */           throw Context.reportRuntimeError2("msg.no.overload", name, method.getDeclaringClass().getName());
/*     */         }
/*     */ 
/*     */         
/* 258 */         found = method;
/*     */       } 
/*     */     } 
/* 261 */     return found;
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
/*     */   static Method[] getMethodList(Class<?> clazz) {
/* 273 */     Method[] methods = null;
/*     */ 
/*     */     
/*     */     try {
/* 277 */       if (!sawSecurityException)
/* 278 */         methods = clazz.getDeclaredMethods(); 
/* 279 */     } catch (SecurityException e) {
/*     */       
/* 281 */       sawSecurityException = true;
/*     */     } 
/* 283 */     if (methods == null) {
/* 284 */       methods = clazz.getMethods();
/*     */     }
/* 286 */     int count = 0;
/* 287 */     for (int i = 0; i < methods.length; i++) {
/* 288 */       if (sawSecurityException ? (methods[i].getDeclaringClass() != clazz) : !Modifier.isPublic(methods[i].getModifiers())) {
/*     */ 
/*     */ 
/*     */         
/* 292 */         methods[i] = null;
/*     */       } else {
/* 294 */         count++;
/*     */       } 
/*     */     } 
/* 297 */     Method[] result = new Method[count];
/* 298 */     int j = 0;
/* 299 */     for (int k = 0; k < methods.length; k++) {
/* 300 */       if (methods[k] != null)
/* 301 */         result[j++] = methods[k]; 
/*     */     } 
/* 303 */     return result;
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
/*     */   public void addAsConstructor(Scriptable scope, Scriptable prototype) {
/* 324 */     initAsConstructor(scope, prototype);
/* 325 */     defineProperty(scope, prototype.getClassName(), this, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void initAsConstructor(Scriptable scope, Scriptable prototype) {
/* 331 */     ScriptRuntime.setFunctionProtoAndParent(this, scope);
/* 332 */     setImmunePrototypeProperty(prototype);
/*     */     
/* 334 */     prototype.setParentScope(this);
/*     */     
/* 336 */     defineProperty(prototype, "constructor", this, 7);
/*     */ 
/*     */ 
/*     */     
/* 340 */     setParentScope(scope);
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
/*     */   public static Object convertArg(Context cx, Scriptable scope, Object arg, Class<?> desired) {
/* 352 */     int tag = getTypeTag(desired);
/* 353 */     if (tag == 0) {
/* 354 */       throw Context.reportRuntimeError1("msg.cant.convert", desired.getName());
/*     */     }
/*     */     
/* 357 */     return convertArg(cx, scope, arg, tag);
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
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*     */     Object object;
/* 374 */     boolean checkMethodResult = false;
/* 375 */     int argsLength = args.length;
/*     */     
/* 377 */     for (int i = 0; i < argsLength; i++) {
/*     */       
/* 379 */       if (args[i] instanceof ConsString) {
/* 380 */         args[i] = args[i].toString();
/*     */       }
/*     */     } 
/*     */     
/* 384 */     if (this.parmsLength < 0) {
/* 385 */       if (this.parmsLength == -1) {
/* 386 */         Object[] invokeArgs = { cx, thisObj, args, this };
/* 387 */         object = this.member.invoke(null, invokeArgs);
/* 388 */         checkMethodResult = true;
/*     */       } else {
/* 390 */         boolean inNewExpr = (thisObj == null);
/* 391 */         Boolean b = inNewExpr ? Boolean.TRUE : Boolean.FALSE;
/* 392 */         Object[] invokeArgs = { cx, args, this, b };
/* 393 */         object = this.member.isCtor() ? this.member.newInstance(invokeArgs) : this.member.invoke(null, invokeArgs);
/*     */       } 
/*     */     } else {
/*     */       Object[] invokeArgs;
/*     */ 
/*     */       
/* 399 */       if (!this.isStatic) {
/* 400 */         Class<?> clazz = this.member.getDeclaringClass();
/* 401 */         if (!clazz.isInstance(thisObj)) {
/* 402 */           boolean compatible = false;
/* 403 */           if (thisObj == scope) {
/* 404 */             Scriptable parentScope = getParentScope();
/* 405 */             if (scope != parentScope) {
/*     */ 
/*     */               
/* 408 */               compatible = clazz.isInstance(parentScope);
/* 409 */               if (compatible) {
/* 410 */                 thisObj = parentScope;
/*     */               }
/*     */             } 
/*     */           } 
/* 414 */           if (!compatible)
/*     */           {
/* 416 */             throw ScriptRuntime.typeError1("msg.incompat.call", this.functionName);
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 423 */       if (this.parmsLength == argsLength) {
/*     */ 
/*     */         
/* 426 */         invokeArgs = args;
/* 427 */         for (int j = 0; j != this.parmsLength; j++) {
/* 428 */           Object arg = args[j];
/* 429 */           Object converted = convertArg(cx, scope, arg, this.typeTags[j]);
/* 430 */           if (arg != converted) {
/* 431 */             if (invokeArgs == args) {
/* 432 */               invokeArgs = (Object[])args.clone();
/*     */             }
/* 434 */             invokeArgs[j] = converted;
/*     */           } 
/*     */         } 
/* 437 */       } else if (this.parmsLength == 0) {
/* 438 */         invokeArgs = ScriptRuntime.emptyArgs;
/*     */       } else {
/* 440 */         invokeArgs = new Object[this.parmsLength];
/* 441 */         for (int j = 0; j != this.parmsLength; j++) {
/* 442 */           Object arg = (j < argsLength) ? args[j] : Undefined.instance;
/*     */ 
/*     */           
/* 445 */           invokeArgs[j] = convertArg(cx, scope, arg, this.typeTags[j]);
/*     */         } 
/*     */       } 
/*     */       
/* 449 */       if (this.member.isMethod()) {
/* 450 */         object = this.member.invoke(thisObj, invokeArgs);
/* 451 */         checkMethodResult = true;
/*     */       } else {
/* 453 */         object = this.member.newInstance(invokeArgs);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 458 */     if (checkMethodResult) {
/* 459 */       if (this.hasVoidReturn) {
/* 460 */         object = Undefined.instance;
/* 461 */       } else if (this.returnTypeTag == 0) {
/* 462 */         object = cx.getWrapFactory().wrap(cx, scope, object, null);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 470 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable createObject(Context cx, Scriptable scope) {
/*     */     Scriptable result;
/* 481 */     if (this.member.isCtor() || this.parmsLength == -2) {
/* 482 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 486 */       result = (Scriptable)this.member.getDeclaringClass().newInstance();
/* 487 */     } catch (Exception ex) {
/* 488 */       throw Context.throwAsScriptRuntimeEx(ex);
/*     */     } 
/*     */     
/* 491 */     result.setPrototype(getClassPrototype());
/* 492 */     result.setParentScope(getParentScope());
/* 493 */     return result;
/*     */   }
/*     */   
/*     */   boolean isVarArgsMethod() {
/* 497 */     return (this.parmsLength == -1);
/*     */   }
/*     */   
/*     */   boolean isVarArgsConstructor() {
/* 501 */     return (this.parmsLength == -2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 507 */     in.defaultReadObject();
/* 508 */     if (this.parmsLength > 0) {
/* 509 */       Class<?>[] types = this.member.argTypes;
/* 510 */       this.typeTags = new byte[this.parmsLength];
/* 511 */       for (int i = 0; i != this.parmsLength; i++) {
/* 512 */         this.typeTags[i] = (byte)getTypeTag(types[i]);
/*     */       }
/*     */     } 
/* 515 */     if (this.member.isMethod()) {
/* 516 */       Method method = this.member.method();
/* 517 */       Class<?> returnType = method.getReturnType();
/* 518 */       if (returnType == void.class) {
/* 519 */         this.hasVoidReturn = true;
/*     */       } else {
/* 521 */         this.returnTypeTag = getTypeTag(returnType);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\FunctionObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */