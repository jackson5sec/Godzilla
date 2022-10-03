/*    */ package org.fife.rsta.ac.css;
/*    */ 
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JList;
/*    */ import org.fife.ui.autocomplete.Completion;
/*    */ import org.fife.ui.autocomplete.CompletionCellRenderer;
/*    */ import org.fife.ui.autocomplete.FunctionCompletion;
/*    */ import org.fife.ui.autocomplete.MarkupTagCompletion;
/*    */ import org.fife.ui.autocomplete.VariableCompletion;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CssCellRenderer
/*    */   extends CompletionCellRenderer
/*    */ {
/* 38 */   private Icon tagIcon = getIcon("../html/tag.png");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForFunctionCompletion(JList list, FunctionCompletion fc, int index, boolean selected, boolean hasFocus) {
/* 49 */     super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
/*    */     
/* 51 */     setIconWithDefault((Completion)fc);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForMarkupTagCompletion(JList list, MarkupTagCompletion c, int index, boolean selected, boolean hasFocus) {
/* 61 */     super.prepareForMarkupTagCompletion(list, c, index, selected, hasFocus);
/* 62 */     setIcon(this.tagIcon);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForOtherCompletion(JList list, Completion c, int index, boolean selected, boolean hasFocus) {
/* 73 */     super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
/* 74 */     setIconWithDefault(c);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForVariableCompletion(JList list, VariableCompletion vc, int index, boolean selected, boolean hasFocus) {
/* 85 */     super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
/*    */     
/* 87 */     setIcon(getEmptyIcon());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\css\CssCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */