/*    */ package org.fife.rsta.ac.js.completion;
/*    */ 
/*    */ import javax.swing.Icon;
/*    */ import org.fife.rsta.ac.js.IconFactory;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.autocomplete.ShorthandCompletion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaScriptShorthandCompletion
/*    */   extends ShorthandCompletion
/*    */   implements JSCompletionUI
/*    */ {
/*    */   private static final String PREFIX = "<html><nobr>";
/*    */   
/*    */   public JavaScriptShorthandCompletion(CompletionProvider provider, String inputText, String replacementText) {
/* 18 */     super(provider, inputText, replacementText);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaScriptShorthandCompletion(CompletionProvider provider, String inputText, String replacementText, String shortDesc) {
/* 24 */     super(provider, inputText, replacementText, shortDesc);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaScriptShorthandCompletion(CompletionProvider provider, String inputText, String replacementText, String shortDesc, String summary) {
/* 31 */     super(provider, inputText, replacementText, shortDesc, summary);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Icon getIcon() {
/* 37 */     return IconFactory.getIcon("template");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRelevance() {
/* 43 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getShortDescriptionText() {
/* 48 */     StringBuilder sb = new StringBuilder("<html><nobr>");
/* 49 */     sb.append(getInputText());
/* 50 */     sb.append(" - ");
/* 51 */     sb.append(getShortDescription());
/* 52 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JavaScriptShorthandCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */