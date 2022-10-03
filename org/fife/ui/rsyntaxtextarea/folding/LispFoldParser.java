/*    */ package org.fife.ui.rsyntaxtextarea.folding;
/*    */ 
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
/*    */ public class LispFoldParser
/*    */   extends CurlyFoldParser
/*    */ {
/*    */   public boolean isLeftCurly(Token t) {
/* 25 */     return t.isSingleChar(22, '(');
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isRightCurly(Token t) {
/* 31 */     return t.isSingleChar(22, ')');
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\LispFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */