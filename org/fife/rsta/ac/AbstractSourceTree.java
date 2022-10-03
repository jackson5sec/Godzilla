/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.event.TreeExpansionListener;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.TreeNode;
/*     */ import javax.swing.tree.TreePath;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
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
/*     */ public abstract class AbstractSourceTree
/*     */   extends JTree
/*     */ {
/*     */   protected RSyntaxTextArea textArea;
/*     */   private boolean sorted;
/*     */   private Pattern pattern;
/*     */   private boolean gotoSelectedElementOnClick;
/*     */   private boolean showMajorElementsOnly;
/*     */   
/*     */   public AbstractSourceTree() {
/*  64 */     getSelectionModel().setSelectionMode(1);
/*     */     
/*  66 */     this.gotoSelectedElementOnClick = true;
/*  67 */     this.showMajorElementsOnly = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void expandInitialNodes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean fastExpandAll(TreePath parent, boolean expand) {
/*  88 */     TreeExpansionListener[] listeners = getTreeExpansionListeners();
/*  89 */     for (TreeExpansionListener listener : listeners) {
/*  90 */       removeTreeExpansionListener(listener);
/*     */     }
/*     */     
/*  93 */     boolean result = fastExpandAllImpl(parent, expand);
/*     */     
/*  95 */     for (TreeExpansionListener listener : listeners) {
/*  96 */       addTreeExpansionListener(listener);
/*     */     }
/*     */ 
/*     */     
/* 100 */     collapsePath(parent);
/* 101 */     expandPath(parent);
/*     */     
/* 103 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean fastExpandAllImpl(TreePath parent, boolean expand) {
/* 109 */     TreeNode node = (TreeNode)parent.getLastPathComponent();
/* 110 */     if (node.getChildCount() > 0) {
/* 111 */       boolean childExpandCalled = false;
/* 112 */       for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
/* 113 */         TreeNode n = (TreeNode)e.nextElement();
/* 114 */         TreePath path = parent.pathByAddingChild(n);
/*     */ 
/*     */         
/* 117 */         childExpandCalled = (fastExpandAllImpl(path, expand) || childExpandCalled);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 122 */       if (!childExpandCalled)
/*     */       {
/*     */         
/* 125 */         if (expand) {
/* 126 */           expandPath(parent);
/*     */         } else {
/*     */           
/* 129 */           collapsePath(parent);
/*     */         } 
/*     */       }
/* 132 */       return true;
/*     */     } 
/*     */     
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void filter(String pattern) {
/* 147 */     if ((pattern == null && this.pattern != null) || (pattern != null && this.pattern == null) || (pattern != null && 
/*     */       
/* 149 */       !pattern.equals(this.pattern.pattern()))) {
/* 150 */       this
/* 151 */         .pattern = (pattern == null || pattern.length() == 0) ? null : RSyntaxUtilities.wildcardToPattern("^" + pattern, false, false);
/* 152 */       Object root = getModel().getRoot();
/* 153 */       if (root instanceof SourceTreeNode) {
/* 154 */         ((SourceTreeNode)root).filter(this.pattern);
/*     */       }
/* 156 */       ((DefaultTreeModel)getModel()).reload();
/* 157 */       expandInitialNodes();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getGotoSelectedElementOnClick() {
/* 170 */     return this.gotoSelectedElementOnClick;
/*     */   }
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
/*     */   public boolean getShowMajorElementsOnly() {
/* 183 */     return this.showMajorElementsOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean gotoSelectedElement();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSorted() {
/* 202 */     return this.sorted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void listenTo(RSyntaxTextArea paramRSyntaxTextArea);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void refresh() {
/* 224 */     DefaultTreeModel model = (DefaultTreeModel)getModel();
/* 225 */     Object root = model.getRoot();
/* 226 */     if (root instanceof SourceTreeNode) {
/* 227 */       SourceTreeNode node = (SourceTreeNode)root;
/* 228 */       node.refresh();
/*     */       
/* 230 */       model.reload();
/* 231 */       expandInitialNodes();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectFirstNodeMatchingFilter() {
/* 241 */     if (this.pattern == null) {
/*     */       return;
/*     */     }
/*     */     
/* 245 */     DefaultTreeModel model = (DefaultTreeModel)getModel();
/* 246 */     DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
/* 247 */     Enumeration<?> en = root.depthFirstEnumeration();
/*     */     
/* 249 */     while (en.hasMoreElements()) {
/* 250 */       SourceTreeNode stn = (SourceTreeNode)en.nextElement();
/*     */       
/* 252 */       JLabel renderer = (JLabel)getCellRenderer().getTreeCellRendererComponent(this, stn, true, true, stn
/* 253 */           .isLeaf(), 0, true);
/* 254 */       String text = renderer.getText();
/* 255 */       if (text != null && this.pattern.matcher(text).find()) {
/* 256 */         setSelectionPath(new TreePath((Object[])model.getPathToRoot(stn)));
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectNextVisibleRow() {
/* 270 */     int currentRow = getLeadSelectionRow();
/* 271 */     if (++currentRow < getRowCount()) {
/* 272 */       TreePath path = getPathForRow(currentRow);
/* 273 */       setSelectionPath(path);
/* 274 */       scrollPathToVisible(path);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void selectPreviousVisibleRow() {
/* 285 */     int currentRow = getLeadSelectionRow();
/* 286 */     if (--currentRow >= 0) {
/* 287 */       TreePath path = getPathForRow(currentRow);
/* 288 */       setSelectionPath(path);
/* 289 */       scrollPathToVisible(path);
/*     */     } 
/*     */   }
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
/*     */   public void setGotoSelectedElementOnClick(boolean gotoSelectedElement) {
/* 303 */     this.gotoSelectedElementOnClick = gotoSelectedElement;
/*     */   }
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
/*     */   public void setShowMajorElementsOnly(boolean show) {
/* 316 */     this.showMajorElementsOnly = show;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSorted(boolean sorted) {
/* 327 */     if (this.sorted != sorted) {
/* 328 */       this.sorted = sorted;
/* 329 */       Object root = getModel().getRoot();
/* 330 */       if (root instanceof SourceTreeNode) {
/* 331 */         ((SourceTreeNode)root).setSorted(sorted);
/*     */       }
/* 333 */       ((DefaultTreeModel)getModel()).reload();
/* 334 */       expandInitialNodes();
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract void uninstall();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\AbstractSourceTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */