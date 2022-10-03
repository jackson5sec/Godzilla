/*    */ package org.fife.rsta.ac.js.ast.parser;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*    */ import org.fife.rsta.ac.js.ast.CodeBlock;
/*    */ import org.fife.rsta.ac.js.ast.TypeDeclarationOptions;
/*    */ import org.fife.ui.autocomplete.Completion;
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
/*    */ public abstract class JavaScriptParser
/*    */ {
/*    */   protected SourceCompletionProvider provider;
/*    */   protected int dot;
/*    */   protected TypeDeclarationOptions options;
/*    */   
/*    */   public JavaScriptParser(SourceCompletionProvider provider, int dot, TypeDeclarationOptions options) {
/* 26 */     this.provider = provider;
/* 27 */     this.dot = dot;
/* 28 */     this.options = options;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract CodeBlock convertAstNodeToCodeBlock(AstRoot paramAstRoot, Set<Completion> paramSet, String paramString);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPreProcessing() {
/* 47 */     return (this.options != null && this.options.isPreProcessing());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\parser\JavaScriptParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */