/*    */ package org.fife.ui.rtextarea;
/*    */ 
/*    */ import javax.swing.text.BadLocationException;
/*    */ import javax.swing.text.GapContent;
/*    */ import javax.swing.text.PlainDocument;
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
/*    */ public class RDocument
/*    */   extends PlainDocument
/*    */ {
/*    */   public RDocument() {
/* 29 */     super(new RGapContent(null));
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
/*    */   public char charAt(int offset) throws BadLocationException {
/* 41 */     return ((RGapContent)getContent()).charAt(offset);
/*    */   }
/*    */ 
/*    */   
/*    */   private static class RGapContent
/*    */     extends GapContent
/*    */   {
/*    */     private RGapContent() {}
/*    */     
/*    */     public char charAt(int offset) throws BadLocationException {
/* 51 */       if (offset < 0 || offset >= length()) {
/* 52 */         throw new BadLocationException("Invalid offset", offset);
/*    */       }
/* 54 */       int g0 = getGapStart();
/* 55 */       char[] array = (char[])getArray();
/* 56 */       if (offset < g0) {
/* 57 */         return array[offset];
/*    */       }
/* 59 */       return array[getGapEnd() + offset - g0];
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */