/*    */ package org.fife.rsta.ac.js.completion;
/*    */ 
/*    */ import javax.swing.Icon;
/*    */ import org.fife.rsta.ac.js.IconFactory;
/*    */ import org.fife.ui.autocomplete.BasicCompletion;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavascriptBasicCompletion
/*    */   extends BasicCompletion
/*    */   implements JSCompletionUI
/*    */ {
/*    */   public JavascriptBasicCompletion(CompletionProvider provider, String replacementText, String shortDesc, String summary) {
/* 19 */     super(provider, replacementText, shortDesc, summary);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JavascriptBasicCompletion(CompletionProvider provider, String replacementText, String shortDesc) {
/* 25 */     super(provider, replacementText, shortDesc);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JavascriptBasicCompletion(CompletionProvider provider, String replacementText) {
/* 31 */     super(provider, replacementText);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Icon getIcon() {
/* 37 */     return IconFactory.getIcon(IconFactory.getEmptyIcon());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRelevance() {
/* 43 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JavascriptBasicCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */