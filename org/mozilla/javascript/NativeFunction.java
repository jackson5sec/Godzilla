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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NativeFunction
/*     */   extends BaseFunction
/*     */ {
/*     */   static final long serialVersionUID = 8713897114082216401L;
/*     */   
/*     */   public final void initScriptFunction(Context cx, Scriptable scope) {
/*  23 */     ScriptRuntime.setFunctionProtoAndParent(this, scope);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final String decompile(int indent, int flags) {
/*  34 */     String encodedSource = getEncodedSource();
/*  35 */     if (encodedSource == null) {
/*  36 */       return super.decompile(indent, flags);
/*     */     }
/*  38 */     UintMap properties = new UintMap(1);
/*  39 */     properties.put(1, indent);
/*  40 */     return Decompiler.decompile(encodedSource, flags, properties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/*  47 */     int paramCount = getParamCount();
/*  48 */     if (getLanguageVersion() != 120) {
/*  49 */       return paramCount;
/*     */     }
/*  51 */     Context cx = Context.getContext();
/*  52 */     NativeCall activation = ScriptRuntime.findFunctionActivation(cx, this);
/*  53 */     if (activation == null) {
/*  54 */       return paramCount;
/*     */     }
/*  56 */     return activation.originalArgs.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArity() {
/*  62 */     return getParamCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String jsGet_name() {
/*  73 */     return getFunctionName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncodedSource() {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public DebuggableScript getDebuggableView() {
/*  86 */     return null;
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
/*     */   public Object resumeGenerator(Context cx, Scriptable scope, int operation, Object state, Object value) {
/* 101 */     throw new EvaluatorException("resumeGenerator() not implemented");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int getLanguageVersion();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int getParamCount();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int getParamAndVarCount();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String getParamOrVarName(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getParamOrVarConst(int index) {
/* 136 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */