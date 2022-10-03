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
/*    */ public class EvaluatorException
/*    */   extends RhinoException
/*    */ {
/*    */   static final long serialVersionUID = -8743165779676009808L;
/*    */   
/*    */   public EvaluatorException(String detail) {
/* 19 */     super(detail);
/*    */   }
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
/*    */   public EvaluatorException(String detail, String sourceName, int lineNumber) {
/* 35 */     this(detail, sourceName, lineNumber, (String)null, 0);
/*    */   }
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
/*    */   public EvaluatorException(String detail, String sourceName, int lineNumber, String lineSource, int columnNumber) {
/* 55 */     super(detail);
/* 56 */     recordErrorOrigin(sourceName, lineNumber, lineSource, columnNumber);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public String getSourceName() {
/* 65 */     return sourceName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public int getLineNumber() {
/* 74 */     return lineNumber();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public int getColumnNumber() {
/* 83 */     return columnNumber();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public String getLineSource() {
/* 92 */     return lineSource();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\EvaluatorException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */