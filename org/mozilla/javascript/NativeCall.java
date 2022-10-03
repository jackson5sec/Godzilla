/*     */ package org.mozilla.javascript;
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
/*     */ public final class NativeCall
/*     */   extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = -7471457301304454454L;
/*  21 */   private static final Object CALL_TAG = "Call"; private static final int Id_constructor = 1;
/*     */   private static final int MAX_PROTOTYPE_ID = 1;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  25 */     NativeCall obj = new NativeCall();
/*  26 */     obj.exportAsJSClass(1, scope, sealed);
/*     */   }
/*     */   NativeFunction function; Object[] originalArgs; transient NativeCall parentActivationCall;
/*     */   
/*     */   NativeCall() {}
/*     */   
/*     */   NativeCall(NativeFunction function, Scriptable scope, Object[] args) {
/*  33 */     this.function = function;
/*     */     
/*  35 */     setParentScope(scope);
/*     */ 
/*     */     
/*  38 */     this.originalArgs = (args == null) ? ScriptRuntime.emptyArgs : args;
/*     */ 
/*     */     
/*  41 */     int paramAndVarCount = function.getParamAndVarCount();
/*  42 */     int paramCount = function.getParamCount();
/*  43 */     if (paramAndVarCount != 0) {
/*  44 */       for (int i = 0; i < paramCount; i++) {
/*  45 */         String name = function.getParamOrVarName(i);
/*  46 */         Object val = (i < args.length) ? args[i] : Undefined.instance;
/*     */         
/*  48 */         defineProperty(name, val, 4);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  54 */     if (!has("arguments", this)) {
/*  55 */       defineProperty("arguments", new Arguments(this), 4);
/*     */     }
/*     */     
/*  58 */     if (paramAndVarCount != 0) {
/*  59 */       for (int i = paramCount; i < paramAndVarCount; i++) {
/*  60 */         String name = function.getParamOrVarName(i);
/*  61 */         if (!has(name, this)) {
/*  62 */           if (function.getParamOrVarConst(i)) {
/*  63 */             defineProperty(name, Undefined.instance, 13);
/*     */           } else {
/*  65 */             defineProperty(name, Undefined.instance, 4);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  74 */     return "Call";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/*  80 */     return s.equals("constructor") ? 1 : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/*  88 */     if (id == 1) {
/*  89 */       arity = 1; s = "constructor";
/*     */     } else {
/*  91 */       throw new IllegalArgumentException(String.valueOf(id));
/*     */     } 
/*  93 */     initPrototypeMethod(CALL_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 100 */     if (!f.hasTag(CALL_TAG)) {
/* 101 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 103 */     int id = f.methodId();
/* 104 */     if (id == 1) {
/* 105 */       if (thisObj != null) {
/* 106 */         throw Context.reportRuntimeError1("msg.only.from.new", "Call");
/*     */       }
/* 108 */       ScriptRuntime.checkDeprecated(cx, "Call");
/* 109 */       NativeCall result = new NativeCall();
/* 110 */       result.setPrototype(getObjectPrototype(scope));
/* 111 */       return result;
/*     */     } 
/* 113 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeCall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */