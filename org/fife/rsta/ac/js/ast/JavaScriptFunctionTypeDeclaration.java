/*    */ package org.fife.rsta.ac.js.ast;
/*    */ 
/*    */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*    */ import org.mozilla.javascript.ast.AstNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaScriptFunctionTypeDeclaration
/*    */   extends JavaScriptVariableDeclaration
/*    */ {
/*    */   private AstNode typeNode;
/*    */   
/*    */   public JavaScriptFunctionTypeDeclaration(String name, int offset, SourceCompletionProvider provider, CodeBlock block) {
/* 15 */     super(name, offset, provider, block);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setTypeDeclaration(AstNode typeNode) {
/* 20 */     this.typeNode = typeNode;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeDeclaration getTypeDeclaration() {
/* 26 */     return this.provider.getJavaScriptEngine().getJavaScriptResolver(this.provider).resolveNode(this.typeNode);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\JavaScriptFunctionTypeDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */