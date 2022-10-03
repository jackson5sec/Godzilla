/*    */ package org.fife.rsta.ac.ts;
/*    */ 
/*    */ import org.fife.rsta.ac.js.JsDocCompletionProvider;
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
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
/*    */ public class TypeScriptCompletionProvider
/*    */   extends LanguageAwareCompletionProvider
/*    */ {
/*    */   private TypeScriptLanguageSupport languageSupport;
/*    */   
/*    */   public TypeScriptCompletionProvider(TypeScriptLanguageSupport languageSupport) {
/* 30 */     super((CompletionProvider)new SourceCompletionProvider());
/*    */     
/* 32 */     this.languageSupport = languageSupport;
/*    */     
/* 34 */     setDocCommentCompletionProvider((CompletionProvider)new JsDocCompletionProvider());
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeScriptLanguageSupport getLanguageSupport() {
/* 39 */     return this.languageSupport;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\ts\TypeScriptCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */