/*    */ package org.apache.log4j.lf5.viewer.categoryexplorer;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.JCheckBox;
/*    */ import javax.swing.JTree;
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
/*    */ public class CategoryNodeEditorRenderer
/*    */   extends CategoryNodeRenderer
/*    */ {
/*    */   private static final long serialVersionUID = -6094804684259929574L;
/*    */   
/*    */   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
/* 61 */     Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
/*    */ 
/*    */ 
/*    */     
/* 65 */     return c;
/*    */   }
/*    */   
/*    */   public JCheckBox getCheckBox() {
/* 69 */     return this._checkBox;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\lf5\viewer\categoryexplorer\CategoryNodeEditorRenderer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */