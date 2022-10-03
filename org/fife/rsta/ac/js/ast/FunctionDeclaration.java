/*    */ package org.fife.rsta.ac.js.ast;
/*    */ 
/*    */ import org.fife.ui.autocomplete.FunctionCompletion;
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
/*    */ public class FunctionDeclaration
/*    */ {
/*    */   private FunctionCompletion fc;
/*    */   private int offset;
/*    */   
/*    */   public FunctionDeclaration(FunctionCompletion fc, int offset) {
/* 23 */     this.fc = fc;
/* 24 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public FunctionCompletion getFunction() {
/* 29 */     return this.fc;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOffset() {
/* 34 */     return this.offset;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\FunctionDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */