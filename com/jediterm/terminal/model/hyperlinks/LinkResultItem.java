/*    */ package com.jediterm.terminal.model.hyperlinks;
/*    */ 
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LinkResultItem
/*    */ {
/*    */   private int myStartOffset;
/*    */   private int myEndOffset;
/*    */   private LinkInfo myLinkInfo;
/*    */   
/*    */   public LinkResultItem(int startOffset, int endOffset, @NotNull LinkInfo linkInfo) {
/* 15 */     this.myStartOffset = startOffset;
/* 16 */     this.myEndOffset = endOffset;
/* 17 */     this.myLinkInfo = linkInfo;
/*    */   }
/*    */   
/*    */   public int getStartOffset() {
/* 21 */     return this.myStartOffset;
/*    */   }
/*    */   
/*    */   public int getEndOffset() {
/* 25 */     return this.myEndOffset;
/*    */   }
/*    */   
/*    */   public LinkInfo getLinkInfo() {
/* 29 */     return this.myLinkInfo;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\hyperlinks\LinkResultItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */