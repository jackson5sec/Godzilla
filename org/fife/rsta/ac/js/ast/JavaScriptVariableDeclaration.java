/*     */ package org.fife.rsta.ac.js.ast;
/*     */ 
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaScriptVariableDeclaration
/*     */   extends JavaScriptDeclaration
/*     */ {
/*     */   protected TypeDeclaration typeDec;
/*     */   protected SourceCompletionProvider provider;
/*     */   private boolean reassigned;
/*     */   private TypeDeclaration originalTypeDec;
/*     */   
/*     */   public JavaScriptVariableDeclaration(String name, int offset, SourceCompletionProvider provider, CodeBlock block) {
/*  38 */     super(name, offset, block);
/*  39 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeDeclaration(AstNode typeNode) {
/*  49 */     this
/*  50 */       .typeDec = this.provider.getJavaScriptEngine().getJavaScriptResolver(this.provider).resolveNode(typeNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeDeclaration(AstNode typeNode, boolean overrideOriginal) {
/*  61 */     if (!this.reassigned) {
/*  62 */       this.originalTypeDec = this.typeDec;
/*     */     }
/*     */     
/*  65 */     setTypeDeclaration(typeNode);
/*     */     
/*  67 */     if (overrideOriginal) {
/*  68 */       this.originalTypeDec = this.typeDec;
/*     */     }
/*  70 */     this.reassigned = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetVariableToOriginalType() {
/*  78 */     if (this.reassigned) {
/*  79 */       this.reassigned = false;
/*  80 */       this.typeDec = this.originalTypeDec;
/*     */     } 
/*  82 */     this.originalTypeDec = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeDeclaration(TypeDeclaration typeDec) {
/*  92 */     this.typeDec = typeDec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeDeclaration getTypeDeclaration() {
/* 100 */     return this.typeDec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJavaScriptTypeName() {
/* 108 */     TypeDeclaration dec = getTypeDeclaration();
/* 109 */     return (dec != null) ? dec.getJSName() : this.provider.getTypesFactory()
/* 110 */       .getDefaultTypeDeclaration().getJSName();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\JavaScriptVariableDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */