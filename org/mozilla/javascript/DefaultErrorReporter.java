/*    */ package org.mozilla.javascript;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DefaultErrorReporter
/*    */   implements ErrorReporter
/*    */ {
/* 16 */   static final DefaultErrorReporter instance = new DefaultErrorReporter();
/*    */ 
/*    */   
/*    */   private boolean forEval;
/*    */   
/*    */   private ErrorReporter chainedReporter;
/*    */ 
/*    */   
/*    */   static ErrorReporter forEval(ErrorReporter reporter) {
/* 25 */     DefaultErrorReporter r = new DefaultErrorReporter();
/* 26 */     r.forEval = true;
/* 27 */     r.chainedReporter = reporter;
/* 28 */     return r;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void warning(String message, String sourceURI, int line, String lineText, int lineOffset) {
/* 34 */     if (this.chainedReporter != null) {
/* 35 */       this.chainedReporter.warning(message, sourceURI, line, lineText, lineOffset);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String message, String sourceURI, int line, String lineText, int lineOffset) {
/* 45 */     if (this.forEval) {
/*    */ 
/*    */ 
/*    */       
/* 49 */       String error = "SyntaxError";
/* 50 */       String TYPE_ERROR_NAME = "TypeError";
/* 51 */       String DELIMETER = ": ";
/* 52 */       String prefix = "TypeError: ";
/* 53 */       if (message.startsWith("TypeError: ")) {
/* 54 */         error = "TypeError";
/* 55 */         message = message.substring("TypeError: ".length());
/*    */       } 
/* 57 */       throw ScriptRuntime.constructError(error, message, sourceURI, line, lineText, lineOffset);
/*    */     } 
/*    */     
/* 60 */     if (this.chainedReporter != null) {
/* 61 */       this.chainedReporter.error(message, sourceURI, line, lineText, lineOffset);
/*    */     } else {
/*    */       
/* 64 */       throw runtimeError(message, sourceURI, line, lineText, lineOffset);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EvaluatorException runtimeError(String message, String sourceURI, int line, String lineText, int lineOffset) {
/* 73 */     if (this.chainedReporter != null) {
/* 74 */       return this.chainedReporter.runtimeError(message, sourceURI, line, lineText, lineOffset);
/*    */     }
/*    */     
/* 77 */     return new EvaluatorException(message, sourceURI, line, lineText, lineOffset);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\DefaultErrorReporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */