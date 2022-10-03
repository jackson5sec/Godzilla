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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaScriptException
/*     */   extends RhinoException
/*     */ {
/*     */   static final long serialVersionUID = -7666130513694669293L;
/*     */   private Object value;
/*     */   
/*     */   @Deprecated
/*     */   public JavaScriptException(Object value) {
/*  29 */     this(value, "", 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaScriptException(Object value, String sourceName, int lineNumber) {
/*  39 */     recordErrorOrigin(sourceName, lineNumber, null, 0);
/*  40 */     this.value = value;
/*     */ 
/*     */     
/*  43 */     if (value instanceof NativeError && Context.getContext().hasFeature(10)) {
/*     */       
/*  45 */       NativeError error = (NativeError)value;
/*  46 */       if (!error.has("fileName", error)) {
/*  47 */         error.put("fileName", error, sourceName);
/*     */       }
/*  49 */       if (!error.has("lineNumber", error)) {
/*  50 */         error.put("lineNumber", error, Integer.valueOf(lineNumber));
/*     */       }
/*     */       
/*  53 */       error.setStackProvider(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String details() {
/*  60 */     if (this.value == null)
/*  61 */       return "null"; 
/*  62 */     if (this.value instanceof NativeError) {
/*  63 */       return this.value.toString();
/*     */     }
/*     */     try {
/*  66 */       return ScriptRuntime.toString(this.value);
/*  67 */     } catch (RuntimeException rte) {
/*     */       
/*  69 */       if (this.value instanceof Scriptable) {
/*  70 */         return ScriptRuntime.defaultObjectToString((Scriptable)this.value);
/*     */       }
/*  72 */       return this.value.toString();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue() {
/*  82 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getSourceName() {
/*  91 */     return sourceName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getLineNumber() {
/* 100 */     return lineNumber();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\JavaScriptException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */