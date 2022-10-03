/*    */ package com.jediterm.terminal.util;
/*    */ 
/*    */ import javax.swing.text.AttributeSet;
/*    */ import javax.swing.text.BadLocationException;
/*    */ import javax.swing.text.PlainDocument;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JTextFieldLimit
/*    */   extends PlainDocument
/*    */ {
/*    */   private int limit;
/*    */   
/*    */   public JTextFieldLimit(int limit) {
/* 15 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
/* 19 */     if (str == null)
/*    */       return; 
/* 21 */     if (getLength() + str.length() <= this.limit)
/* 22 */       super.insertString(offset, str, attr); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\util\JTextFieldLimit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */