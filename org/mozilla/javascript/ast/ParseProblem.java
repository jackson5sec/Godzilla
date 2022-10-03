/*    */ package org.mozilla.javascript.ast;
/*    */ 
/*    */ 
/*    */ public class ParseProblem
/*    */ {
/*    */   private Type type;
/*    */   private String message;
/*    */   private String sourceName;
/*    */   private int offset;
/*    */   private int length;
/*    */   
/*    */   public enum Type
/*    */   {
/* 14 */     Error, Warning;
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
/*    */   public ParseProblem(Type type, String message, String sourceName, int offset, int length) {
/* 27 */     setType(type);
/* 28 */     setMessage(message);
/* 29 */     setSourceName(sourceName);
/* 30 */     setFileOffset(offset);
/* 31 */     setLength(length);
/*    */   }
/*    */   
/*    */   public Type getType() {
/* 35 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(Type type) {
/* 39 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 43 */     return this.message;
/*    */   }
/*    */   
/*    */   public void setMessage(String msg) {
/* 47 */     this.message = msg;
/*    */   }
/*    */   
/*    */   public String getSourceName() {
/* 51 */     return this.sourceName;
/*    */   }
/*    */   
/*    */   public void setSourceName(String name) {
/* 55 */     this.sourceName = name;
/*    */   }
/*    */   
/*    */   public int getFileOffset() {
/* 59 */     return this.offset;
/*    */   }
/*    */   
/*    */   public void setFileOffset(int offset) {
/* 63 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 67 */     return this.length;
/*    */   }
/*    */   
/*    */   public void setLength(int length) {
/* 71 */     this.length = length;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     StringBuilder sb = new StringBuilder(200);
/* 77 */     sb.append(this.sourceName).append(":");
/* 78 */     sb.append("offset=").append(this.offset).append(",");
/* 79 */     sb.append("length=").append(this.length).append(",");
/* 80 */     sb.append((this.type == Type.Error) ? "error: " : "warning: ");
/* 81 */     sb.append(this.message);
/* 82 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ast\ParseProblem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */