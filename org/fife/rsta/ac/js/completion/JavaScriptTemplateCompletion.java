/*    */ package org.fife.rsta.ac.js.completion;
/*    */ 
/*    */ import org.fife.rsta.ac.java.JavaTemplateCompletion;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
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
/*    */ public class JavaScriptTemplateCompletion
/*    */   extends JavaTemplateCompletion
/*    */ {
/*    */   public JavaScriptTemplateCompletion(CompletionProvider provider, String inputText, String definitionString, String template) {
/* 28 */     this(provider, inputText, definitionString, template, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaScriptTemplateCompletion(CompletionProvider provider, String inputText, String definitionString, String template, String shortDesc) {
/* 35 */     this(provider, inputText, definitionString, template, shortDesc, null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaScriptTemplateCompletion(CompletionProvider provider, String inputText, String definitionString, String template, String shortDesc, String summary) {
/* 41 */     super(provider, inputText, definitionString, template, shortDesc, summary);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JavaScriptTemplateCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */