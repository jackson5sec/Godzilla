/*    */ package org.mozilla.javascript.ast;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.mozilla.javascript.EvaluatorException;
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
/*    */ public class ErrorCollector
/*    */   implements IdeErrorReporter
/*    */ {
/* 24 */   private List<ParseProblem> errors = new ArrayList<ParseProblem>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
/* 33 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void warning(String message, String sourceName, int offset, int length) {
/* 41 */     this.errors.add(new ParseProblem(ParseProblem.Type.Warning, message, sourceName, offset, length));
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
/*    */   public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
/* 54 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void error(String message, String sourceName, int fileOffset, int length) {
/* 63 */     this.errors.add(new ParseProblem(ParseProblem.Type.Error, message, sourceName, fileOffset, length));
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
/*    */   public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
/* 75 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ParseProblem> getErrors() {
/* 82 */     return this.errors;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 87 */     StringBuilder sb = new StringBuilder(this.errors.size() * 100);
/* 88 */     for (ParseProblem pp : this.errors) {
/* 89 */       sb.append(pp.toString()).append("\n");
/*    */     }
/* 91 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ErrorCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */