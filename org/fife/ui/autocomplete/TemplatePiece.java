/*    */ package org.fife.ui.autocomplete;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ interface TemplatePiece
/*    */ {
/*    */   String getText();
/*    */   
/*    */   public static class Text
/*    */     implements TemplatePiece
/*    */   {
/*    */     private String text;
/*    */     
/*    */     Text(String text) {
/* 34 */       this.text = text;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getText() {
/* 39 */       return this.text;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 44 */       return "[TemplatePiece.Text: text=" + this.text + "]";
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Param
/*    */     implements TemplatePiece
/*    */   {
/*    */     String text;
/*    */ 
/*    */ 
/*    */     
/*    */     Param(String text) {
/* 58 */       this.text = text;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getText() {
/* 63 */       return this.text;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 68 */       return "[TemplatePiece.Param: param=" + this.text + "]";
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class ParamCopy
/*    */     implements TemplatePiece
/*    */   {
/*    */     private String text;
/*    */ 
/*    */ 
/*    */     
/*    */     ParamCopy(String text) {
/* 82 */       this.text = text;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getText() {
/* 87 */       return this.text;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 92 */       return "[TemplatePiece.ParamCopy: param=" + this.text + "]";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\TemplatePiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */