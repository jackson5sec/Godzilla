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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WrappedException
/*    */   extends EvaluatorException
/*    */ {
/*    */   static final long serialVersionUID = -1551979216966520648L;
/*    */   private Throwable exception;
/*    */   
/*    */   public WrappedException(Throwable exception) {
/* 26 */     super("Wrapped " + exception.toString());
/* 27 */     this.exception = exception;
/* 28 */     Kit.initCause(this, exception);
/*    */     
/* 30 */     int[] linep = { 0 };
/* 31 */     String sourceName = Context.getSourcePositionFromStack(linep);
/* 32 */     int lineNumber = linep[0];
/* 33 */     if (sourceName != null) {
/* 34 */       initSourceName(sourceName);
/*    */     }
/* 36 */     if (lineNumber != 0) {
/* 37 */       initLineNumber(lineNumber);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable getWrappedException() {
/* 49 */     return this.exception;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public Object unwrap() {
/* 58 */     return getWrappedException();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\WrappedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */