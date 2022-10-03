/*    */ package org.fife.rsta.ac.js;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.StringReader;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.fife.rsta.ac.js.ast.CodeBlock;
/*    */ import org.fife.rsta.ac.js.ast.TypeDeclarationOptions;
/*    */ import org.fife.ui.autocomplete.Completion;
/*    */ import org.mozilla.javascript.CompilerEnvirons;
/*    */ import org.mozilla.javascript.Parser;
/*    */ import org.mozilla.javascript.ast.AstRoot;
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
/*    */ public class PreProcessingScripts
/*    */ {
/*    */   private SourceCompletionProvider provider;
/* 26 */   private Set<Completion> preProcessingCompletions = new HashSet<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public PreProcessingScripts(SourceCompletionProvider provider) {
/* 31 */     this.provider = provider;
/*    */   }
/*    */ 
/*    */   
/*    */   public void parseScript(String scriptText, TypeDeclarationOptions options) {
/* 36 */     if (scriptText != null && scriptText.length() > 0) {
/*    */       
/* 38 */       CompilerEnvirons env = JavaScriptParser.createCompilerEnvironment(new JavaScriptParser.JSErrorReporter(), this.provider.getLanguageSupport());
/* 39 */       Parser parser = new Parser(env);
/* 40 */       StringReader r = new StringReader(scriptText);
/*    */       try {
/* 42 */         AstRoot root = parser.parse(r, null, 0);
/* 43 */         CodeBlock block = this.provider.iterateAstRoot(root, this.preProcessingCompletions, "", 2147483647, options);
/* 44 */         this.provider.recursivelyAddLocalVars(this.preProcessingCompletions, block, 0, null, false, true);
/*    */       }
/* 46 */       catch (IOException iOException) {}
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void reset() {
/* 55 */     this.preProcessingCompletions.clear();
/*    */     
/* 57 */     this.provider.getVariableResolver().resetPreProcessingVariables(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<Completion> getCompletions() {
/* 62 */     return this.preProcessingCompletions;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\PreProcessingScripts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */