/*    */ package org.fife.rsta.ac.c;
/*    */ 
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JList;
/*    */ import org.fife.ui.autocomplete.Completion;
/*    */ import org.fife.ui.autocomplete.CompletionCellRenderer;
/*    */ import org.fife.ui.autocomplete.FunctionCompletion;
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
/*    */ 
/*    */ class CCellRenderer
/*    */   extends CompletionCellRenderer
/*    */ {
/* 38 */   private Icon variableIcon = getIcon("var.png");
/* 39 */   private Icon functionIcon = getIcon("function.png");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForOtherCompletion(JList list, Completion c, int index, boolean selected, boolean hasFocus) {
/* 49 */     super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
/* 50 */     setIcon(getEmptyIcon());
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
/* 61 */     super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
/*    */     
/* 63 */     setIcon(this.variableIcon);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForFunctionCompletion(JList list, FunctionCompletion fc, int index, boolean selected, boolean hasFocus) {
/* 74 */     super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
/*    */     
/* 76 */     setIcon(this.functionIcon);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\c\CCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */