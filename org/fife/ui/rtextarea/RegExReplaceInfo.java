/*    */ package org.fife.ui.rtextarea;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class RegExReplaceInfo
/*    */ {
/*    */   private String matchedText;
/*    */   private int startIndex;
/*    */   private int endIndex;
/*    */   private String replacement;
/*    */   
/*    */   RegExReplaceInfo(String matchedText, int start, int end, String replacement) {
/* 39 */     this.matchedText = matchedText;
/* 40 */     this.startIndex = start;
/* 41 */     this.endIndex = end;
/* 42 */     this.replacement = replacement;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getEndIndex() {
/* 53 */     return this.endIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMatchedText() {
/* 62 */     return this.matchedText;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getReplacement() {
/* 71 */     return this.replacement;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getStartIndex() {
/* 82 */     return this.startIndex;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RegExReplaceInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */