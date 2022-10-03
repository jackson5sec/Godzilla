/*    */ package org.fife.ui.rsyntaxtextarea;
/*    */ 
/*    */ import javax.swing.event.HyperlinkEvent;
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
/*    */ public class SelectRegionLinkGeneratorResult
/*    */   implements LinkGeneratorResult
/*    */ {
/*    */   private RSyntaxTextArea textArea;
/*    */   private int sourceOffset;
/*    */   private int selStart;
/*    */   private int selEnd;
/*    */   
/*    */   public SelectRegionLinkGeneratorResult(RSyntaxTextArea textArea, int sourceOffset, int selStart, int selEnd) {
/* 35 */     this.textArea = textArea;
/* 36 */     this.sourceOffset = sourceOffset;
/* 37 */     this.selStart = selStart;
/* 38 */     this.selEnd = selEnd;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HyperlinkEvent execute() {
/* 47 */     this.textArea.select(this.selStart, this.selEnd);
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSourceOffset() {
/* 54 */     return this.sourceOffset;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\SelectRegionLinkGeneratorResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */