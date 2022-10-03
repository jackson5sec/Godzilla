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
/*     */ public class EcmaError
/*     */   extends RhinoException
/*     */ {
/*     */   static final long serialVersionUID = -6261226256957286699L;
/*     */   private String errorName;
/*     */   private String errorMessage;
/*     */   
/*     */   EcmaError(String errorName, String errorMessage, String sourceName, int lineNumber, String lineSource, int columnNumber) {
/*  39 */     recordErrorOrigin(sourceName, lineNumber, lineSource, columnNumber);
/*  40 */     this.errorName = errorName;
/*  41 */     this.errorMessage = errorMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EcmaError(Scriptable nativeError, String sourceName, int lineNumber, int columnNumber, String lineSource) {
/*  52 */     this("InternalError", ScriptRuntime.toString(nativeError), sourceName, lineNumber, lineSource, columnNumber);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String details() {
/*  59 */     return this.errorName + ": " + this.errorMessage;
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
/*     */   public String getName() {
/*  76 */     return this.errorName;
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
/*     */   public String getErrorMessage() {
/*  88 */     return this.errorMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getSourceName() {
/*  97 */     return sourceName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getLineNumber() {
/* 106 */     return lineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getColumnNumber() {
/* 115 */     return columnNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getLineSource() {
/* 123 */     return lineSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Scriptable getErrorObject() {
/* 133 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\EcmaError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */