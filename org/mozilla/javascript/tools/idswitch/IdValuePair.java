/*    */ package org.mozilla.javascript.tools.idswitch;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IdValuePair
/*    */ {
/*    */   public final int idLength;
/*    */   public final String id;
/*    */   public final String value;
/*    */   private int lineNumber;
/*    */   
/*    */   public IdValuePair(String id, String value) {
/* 17 */     this.idLength = id.length();
/* 18 */     this.id = id;
/* 19 */     this.value = value;
/*    */   }
/*    */   public int getLineNumber() {
/* 22 */     return this.lineNumber;
/*    */   } public void setLineNumber(int value) {
/* 24 */     this.lineNumber = value;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\idswitch\IdValuePair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */