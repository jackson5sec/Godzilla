/*    */ package org.fife.rsta.ac.java.tree;
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
/*    */ class AstTreeCellRenderer
/*    */   extends DefaultTreeCellRenderer
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
/* 33 */     super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
/*    */     
/* 35 */     if (value instanceof JavaTreeNode) {
/* 36 */       JavaTreeNode node = (JavaTreeNode)value;
/* 37 */       setText(node.getText(sel));
/* 38 */       setIcon(node.getIcon());
/*    */     } 
/* 40 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\tree\AstTreeCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */