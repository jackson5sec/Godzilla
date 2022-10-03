/*    */ package org.fife.rsta.ac.js.tree;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.JTree;
/*    */ import javax.swing.tree.DefaultTreeCellRenderer;
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
/*    */ class JavaScriptTreeCellRenderer
/*    */   extends DefaultTreeCellRenderer
/*    */ {
/*    */   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
/* 31 */     super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
/*    */     
/* 33 */     if (value instanceof JavaScriptTreeNode) {
/* 34 */       JavaScriptTreeNode node = (JavaScriptTreeNode)value;
/* 35 */       setText(node.getText(sel));
/* 36 */       setIcon(node.getIcon());
/*    */     } 
/* 38 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\tree\JavaScriptTreeCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */