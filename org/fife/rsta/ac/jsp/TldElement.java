/*    */ package org.fife.rsta.ac.jsp;
/*    */ 
/*    */ import org.fife.ui.autocomplete.CompletionProvider;
/*    */ import org.fife.ui.autocomplete.MarkupTagCompletion;
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
/*    */ class TldElement
/*    */   extends MarkupTagCompletion
/*    */ {
/*    */   public TldElement(JspCompletionProvider provider, String name, String desc) {
/* 26 */     super((CompletionProvider)provider, name);
/* 27 */     setDescription(desc);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\jsp\TldElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */