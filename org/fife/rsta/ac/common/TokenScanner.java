/*    */ package org.fife.rsta.ac.common;
/*    */ 
/*    */ import javax.swing.text.Element;
/*    */ import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
/*    */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*    */ import org.fife.ui.rsyntaxtextarea.Token;
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
/*    */ public class TokenScanner
/*    */ {
/*    */   private RSyntaxDocument doc;
/*    */   private Element root;
/*    */   private Token t;
/*    */   private int line;
/*    */   
/*    */   public TokenScanner(RSyntaxTextArea textArea) {
/* 42 */     this((RSyntaxDocument)textArea.getDocument());
/*    */   }
/*    */ 
/*    */   
/*    */   public TokenScanner(RSyntaxDocument doc) {
/* 47 */     this.doc = doc;
/* 48 */     this.root = doc.getDefaultRootElement();
/* 49 */     this.line = 0;
/* 50 */     this.t = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RSyntaxDocument getDocument() {
/* 60 */     return this.doc;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Token next() {
/* 71 */     Token next = nextRaw();
/* 72 */     while (next != null && (next.isWhitespace() || next.isComment())) {
/* 73 */       next = nextRaw();
/*    */     }
/* 75 */     return next;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Token nextRaw() {
/* 86 */     if (this.t == null || !this.t.isPaintable()) {
/* 87 */       int lineCount = this.root.getElementCount();
/* 88 */       while (this.line < lineCount && (this.t == null || !this.t.isPaintable())) {
/* 89 */         this.t = this.doc.getTokenListForLine(this.line++);
/*    */       }
/* 91 */       if (this.line == lineCount) {
/* 92 */         return null;
/*    */       }
/*    */     } 
/* 95 */     Token next = this.t;
/* 96 */     this.t = this.t.getNextToken();
/* 97 */     return next;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\common\TokenScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */