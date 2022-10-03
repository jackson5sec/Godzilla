/*    */ package org.fife.rsta.ac.js.ast;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class JavaScriptDeclaration
/*    */ {
/*    */   private String name;
/*    */   private int offset;
/*    */   private int start;
/*    */   private int end;
/*    */   private CodeBlock block;
/*    */   private TypeDeclarationOptions options;
/*    */   
/*    */   public JavaScriptDeclaration(String name, int offset, CodeBlock block) {
/* 16 */     this.name = name;
/* 17 */     this.offset = offset;
/* 18 */     this.block = block;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 25 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOffset() {
/* 33 */     return this.offset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getEndOffset() {
/* 42 */     return this.end;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setEndOffset(int end) {
/* 52 */     this.end = end;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setStartOffset(int start) {
/* 62 */     this.start = start;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getStartOffSet() {
/* 72 */     return this.start;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CodeBlock getCodeBlock() {
/* 79 */     return this.block;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTypeDeclarationOptions(TypeDeclarationOptions options) {
/* 88 */     this.options = options;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeDeclarationOptions getTypeDeclarationOptions() {
/* 96 */     return this.options;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\JavaScriptDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */