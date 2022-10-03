/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import org.mozilla.javascript.debug.DebuggableScript;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class InterpretedFunction
/*     */   extends NativeFunction
/*     */   implements Script
/*     */ {
/*     */   static final long serialVersionUID = 541475680333911468L;
/*     */   InterpreterData idata;
/*     */   SecurityController securityController;
/*     */   Object securityDomain;
/*     */   
/*     */   private InterpretedFunction(InterpreterData idata, Object staticSecurityDomain) {
/*     */     Object dynamicDomain;
/*  22 */     this.idata = idata;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  27 */     Context cx = Context.getContext();
/*  28 */     SecurityController sc = cx.getSecurityController();
/*     */     
/*  30 */     if (sc != null) {
/*  31 */       dynamicDomain = sc.getDynamicSecurityDomain(staticSecurityDomain);
/*     */     } else {
/*  33 */       if (staticSecurityDomain != null) {
/*  34 */         throw new IllegalArgumentException();
/*     */       }
/*  36 */       dynamicDomain = null;
/*     */     } 
/*     */     
/*  39 */     this.securityController = sc;
/*  40 */     this.securityDomain = dynamicDomain;
/*     */   }
/*     */ 
/*     */   
/*     */   private InterpretedFunction(InterpretedFunction parent, int index) {
/*  45 */     this.idata = parent.idata.itsNestedFunctions[index];
/*  46 */     this.securityController = parent.securityController;
/*  47 */     this.securityDomain = parent.securityDomain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static InterpretedFunction createScript(InterpreterData idata, Object staticSecurityDomain) {
/*  57 */     InterpretedFunction f = new InterpretedFunction(idata, staticSecurityDomain);
/*  58 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static InterpretedFunction createFunction(Context cx, Scriptable scope, InterpreterData idata, Object staticSecurityDomain) {
/*  69 */     InterpretedFunction f = new InterpretedFunction(idata, staticSecurityDomain);
/*  70 */     f.initScriptFunction(cx, scope);
/*  71 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static InterpretedFunction createFunction(Context cx, Scriptable scope, InterpretedFunction parent, int index) {
/*  81 */     InterpretedFunction f = new InterpretedFunction(parent, index);
/*  82 */     f.initScriptFunction(cx, scope);
/*  83 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFunctionName() {
/*  90 */     return (this.idata.itsName == null) ? "" : this.idata.itsName;
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
/* 106 */     if (!ScriptRuntime.hasTopCall(cx)) {
/* 107 */       return ScriptRuntime.doTopCall(this, cx, scope, thisObj, args);
/*     */     }
/* 109 */     return Interpreter.interpret(this, cx, scope, thisObj, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object exec(Context cx, Scriptable scope) {
/* 114 */     if (!isScript())
/*     */     {
/* 116 */       throw new IllegalStateException();
/*     */     }
/* 118 */     if (!ScriptRuntime.hasTopCall(cx))
/*     */     {
/* 120 */       return ScriptRuntime.doTopCall(this, cx, scope, scope, ScriptRuntime.emptyArgs);
/*     */     }
/*     */     
/* 123 */     return Interpreter.interpret(this, cx, scope, scope, ScriptRuntime.emptyArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isScript() {
/* 128 */     return (this.idata.itsFunctionType == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncodedSource() {
/* 134 */     return Interpreter.getEncodedSource(this.idata);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DebuggableScript getDebuggableView() {
/* 140 */     return this.idata;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object resumeGenerator(Context cx, Scriptable scope, int operation, Object state, Object value) {
/* 147 */     return Interpreter.resumeGenerator(cx, scope, operation, state, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLanguageVersion() {
/* 153 */     return this.idata.languageVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getParamCount() {
/* 159 */     return this.idata.argCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getParamAndVarCount() {
/* 165 */     return this.idata.argNames.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getParamOrVarName(int index) {
/* 171 */     return this.idata.argNames[index];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getParamOrVarConst(int index) {
/* 177 */     return this.idata.argIsConst[index];
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\InterpretedFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */