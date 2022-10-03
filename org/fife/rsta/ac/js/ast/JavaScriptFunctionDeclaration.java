/*    */ package org.fife.rsta.ac.js.ast;
/*    */ 
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaScriptFunctionDeclaration
/*    */   extends JavaScriptDeclaration
/*    */ {
/*    */   private TypeDeclaration typeDec;
/*    */   private String functionName;
/*    */   
/*    */   public JavaScriptFunctionDeclaration(String name, int offset, CodeBlock block, TypeDeclaration typeDec) {
/* 14 */     super(name, offset, block);
/* 15 */     this.typeDec = typeDec;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeDeclaration getTypeDeclaration() {
/* 20 */     return this.typeDec;
/*    */   }
/*    */   
/*    */   public void setFunctionName(String functionName) {
/* 24 */     this.functionName = functionName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFunctionName() {
/* 29 */     return this.functionName;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\JavaScriptFunctionDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */