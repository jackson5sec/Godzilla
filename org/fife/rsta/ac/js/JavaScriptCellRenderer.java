/*    */ package org.fife.rsta.ac.js;
/*    */ 
/*    */ import javax.swing.JList;
/*    */ import org.fife.ui.autocomplete.Completion;
/*    */ import org.fife.ui.autocomplete.CompletionCellRenderer;
/*    */ import org.fife.ui.autocomplete.FunctionCompletion;
/*    */ import org.fife.ui.autocomplete.TemplateCompletion;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaScriptCellRenderer
/*    */   extends CompletionCellRenderer
/*    */ {
/*    */   protected void prepareForOtherCompletion(JList list, Completion c, int index, boolean selected, boolean hasFocus) {
/* 44 */     super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
/* 45 */     setIconWithDefault(c);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForTemplateCompletion(JList list, TemplateCompletion tc, int index, boolean selected, boolean hasFocus) {
/* 55 */     super.prepareForTemplateCompletion(list, tc, index, selected, hasFocus);
/* 56 */     setIconWithDefault((Completion)tc, IconFactory.getIcon("template"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForVariableCompletion(JList list, VariableCompletion vc, int index, boolean selected, boolean hasFocus) {
/* 66 */     super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
/* 67 */     setIconWithDefault((Completion)vc, IconFactory.getIcon("local_variable"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void prepareForFunctionCompletion(JList list, FunctionCompletion fc, int index, boolean selected, boolean hasFocus) {
/* 77 */     super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
/* 78 */     setIconWithDefault((Completion)fc, IconFactory.getIcon("default_function"));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JavaScriptCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */