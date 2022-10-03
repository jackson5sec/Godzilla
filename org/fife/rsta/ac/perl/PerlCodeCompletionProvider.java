/*    */ package org.fife.rsta.ac.perl;
/*    */ 
/*    */ import org.fife.ui.autocomplete.DefaultCompletionProvider;
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
/*    */ class PerlCodeCompletionProvider
/*    */   extends DefaultCompletionProvider
/*    */ {
/*    */   private PerlCompletionProvider parent;
/*    */   
/*    */   public PerlCodeCompletionProvider(PerlCompletionProvider parent) {
/* 28 */     this.parent = parent;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public char getParameterListEnd() {
/* 34 */     return this.parent.getParameterListEnd();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public char getParameterListStart() {
/* 40 */     return this.parent.getParameterListStart();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidChar(char ch) {
/* 49 */     return (super.isValidChar(ch) || ch == '@' || ch == '$' || ch == '%');
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\perl\PerlCodeCompletionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */