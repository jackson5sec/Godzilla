/*    */ package org.fife.ui.rsyntaxtextarea.modes;
/*    */ 
/*    */ import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;
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
/*    */ public abstract class AbstractMarkupTokenMaker
/*    */   extends AbstractJFlexTokenMaker
/*    */ {
/*    */   public abstract boolean getCompleteCloseTags();
/*    */   
/*    */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 34 */     return new String[] { "<!--", "-->" };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isMarkupLanguage() {
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\AbstractMarkupTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */