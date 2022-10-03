/*    */ package org.fife.rsta.ac.html;
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
/*    */ 
/*    */ public class HtmlCellRenderer
/*    */   extends CompletionCellRenderer
/*    */ {
/* 39 */   private Icon tagIcon = getIcon("tag.png");
/* 40 */   private Icon attrIcon = getIcon("attribute.png");
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
/* 51 */     super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
/*    */     
/* 53 */     setIcon(getEmptyIcon());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForMarkupTagCompletion(JList list, MarkupTagCompletion c, int index, boolean selected, boolean hasFocus) {
/* 63 */     super.prepareForMarkupTagCompletion(list, c, index, selected, hasFocus);
/* 64 */     setIcon(this.tagIcon);
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
/* 75 */     super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
/* 76 */     if (c instanceof AttributeCompletion) {
/* 77 */       setIcon(this.attrIcon);
/*    */     } else {
/*    */       
/* 80 */       setIcon(getEmptyIcon());
/*    */     } 
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
/* 92 */     super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
/*    */     
/* 94 */     setIcon(getEmptyIcon());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\html\HtmlCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */