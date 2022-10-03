/*    */ package org.mozilla.javascript;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public final class ScriptStackElement
/*    */   implements Serializable
/*    */ {
/*    */   static final long serialVersionUID = -6416688260860477449L;
/*    */   public final String fileName;
/*    */   public final String functionName;
/*    */   public final int lineNumber;
/*    */   
/*    */   public ScriptStackElement(String fileName, String functionName, int lineNumber) {
/* 24 */     this.fileName = fileName;
/* 25 */     this.functionName = functionName;
/* 26 */     this.lineNumber = lineNumber;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 31 */     StringBuilder sb = new StringBuilder();
/* 32 */     renderMozillaStyle(sb);
/* 33 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void renderJavaStyle(StringBuilder sb) {
/* 42 */     sb.append("\tat ").append(this.fileName);
/* 43 */     if (this.lineNumber > -1) {
/* 44 */       sb.append(':').append(this.lineNumber);
/*    */     }
/* 46 */     if (this.functionName != null) {
/* 47 */       sb.append(" (").append(this.functionName).append(')');
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void renderMozillaStyle(StringBuilder sb) {
/* 57 */     if (this.functionName != null) {
/* 58 */       sb.append(this.functionName).append("()");
/*    */     }
/* 60 */     sb.append('@').append(this.fileName);
/* 61 */     if (this.lineNumber > -1) {
/* 62 */       sb.append(':').append(this.lineNumber);
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
/*    */   public void renderV8Style(StringBuilder sb) {
/* 74 */     sb.append("    at ");
/*    */     
/* 76 */     if (this.functionName == null || "anonymous".equals(this.functionName) || "undefined".equals(this.functionName)) {
/*    */       
/* 78 */       appendV8Location(sb);
/*    */     } else {
/*    */       
/* 81 */       sb.append(this.functionName).append(" (");
/* 82 */       appendV8Location(sb);
/* 83 */       sb.append(')');
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void appendV8Location(StringBuilder sb) {
/* 89 */     sb.append(this.fileName);
/* 90 */     if (this.lineNumber > -1)
/* 91 */       sb.append(':').append(this.lineNumber); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ScriptStackElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */