/*     */ package org.mozilla.javascript.tools.debugger.treetable;
/*     */ 
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.TreeExpansionEvent;
/*     */ import javax.swing.event.TreeExpansionListener;
/*     */ import javax.swing.event.TreeModelEvent;
/*     */ import javax.swing.event.TreeModelListener;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeTableModelAdapter
/*     */   extends AbstractTableModel
/*     */ {
/*     */   private static final long serialVersionUID = 48741114609209052L;
/*     */   JTree tree;
/*     */   TreeTableModel treeTableModel;
/*     */   
/*     */   public TreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree) {
/*  62 */     this.tree = tree;
/*  63 */     this.treeTableModel = treeTableModel;
/*     */     
/*  65 */     tree.addTreeExpansionListener(new TreeExpansionListener()
/*     */         {
/*     */           public void treeExpanded(TreeExpansionEvent event)
/*     */           {
/*  69 */             TreeTableModelAdapter.this.fireTableDataChanged();
/*     */           }
/*     */           public void treeCollapsed(TreeExpansionEvent event) {
/*  72 */             TreeTableModelAdapter.this.fireTableDataChanged();
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     treeTableModel.addTreeModelListener(new TreeModelListener() {
/*     */           public void treeNodesChanged(TreeModelEvent e) {
/*  82 */             TreeTableModelAdapter.this.delayedFireTableDataChanged();
/*     */           }
/*     */           
/*     */           public void treeNodesInserted(TreeModelEvent e) {
/*  86 */             TreeTableModelAdapter.this.delayedFireTableDataChanged();
/*     */           }
/*     */           
/*     */           public void treeNodesRemoved(TreeModelEvent e) {
/*  90 */             TreeTableModelAdapter.this.delayedFireTableDataChanged();
/*     */           }
/*     */           
/*     */           public void treeStructureChanged(TreeModelEvent e) {
/*  94 */             TreeTableModelAdapter.this.delayedFireTableDataChanged();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() {
/* 102 */     return this.treeTableModel.getColumnCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColumnName(int column) {
/* 107 */     return this.treeTableModel.getColumnName(column);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getColumnClass(int column) {
/* 112 */     return this.treeTableModel.getColumnClass(column);
/*     */   }
/*     */   
/*     */   public int getRowCount() {
/* 116 */     return this.tree.getRowCount();
/*     */   }
/*     */   
/*     */   protected Object nodeForRow(int row) {
/* 120 */     TreePath treePath = this.tree.getPathForRow(row);
/* 121 */     return treePath.getLastPathComponent();
/*     */   }
/*     */   
/*     */   public Object getValueAt(int row, int column) {
/* 125 */     return this.treeTableModel.getValueAt(nodeForRow(row), column);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCellEditable(int row, int column) {
/* 130 */     return this.treeTableModel.isCellEditable(nodeForRow(row), column);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValueAt(Object value, int row, int column) {
/* 135 */     this.treeTableModel.setValueAt(value, nodeForRow(row), column);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void delayedFireTableDataChanged() {
/* 143 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 145 */             TreeTableModelAdapter.this.fireTableDataChanged();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\debugger\treetable\TreeTableModelAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */