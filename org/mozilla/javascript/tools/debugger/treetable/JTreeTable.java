/*     */ package org.mozilla.javascript.tools.debugger.treetable;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.EventObject;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.table.TableCellEditor;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.DefaultTreeSelectionModel;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ import javax.swing.tree.TreeModel;
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
/*     */ public class JTreeTable
/*     */   extends JTable
/*     */ {
/*     */   private static final long serialVersionUID = -2103973006456695515L;
/*     */   protected TreeTableCellRenderer tree;
/*     */   
/*     */   public JTreeTable(TreeTableModel treeTableModel) {
/*  71 */     this.tree = new TreeTableCellRenderer(treeTableModel);
/*     */ 
/*     */     
/*  74 */     setModel(new TreeTableModelAdapter(treeTableModel, this.tree));
/*     */ 
/*     */     
/*  77 */     ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
/*     */     
/*  79 */     this.tree.setSelectionModel(selectionWrapper);
/*  80 */     setSelectionModel(selectionWrapper.getListSelectionModel());
/*     */ 
/*     */     
/*  83 */     setDefaultRenderer(TreeTableModel.class, this.tree);
/*  84 */     setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());
/*     */ 
/*     */     
/*  87 */     setShowGrid(false);
/*     */ 
/*     */     
/*  90 */     setIntercellSpacing(new Dimension(0, 0));
/*     */ 
/*     */ 
/*     */     
/*  94 */     if (this.tree.getRowHeight() < 1)
/*     */     {
/*  96 */       setRowHeight(18);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 107 */     super.updateUI();
/* 108 */     if (this.tree != null) {
/* 109 */       this.tree.updateUI();
/*     */     }
/*     */ 
/*     */     
/* 113 */     LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
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
/*     */   public int getEditingRow() {
/* 125 */     return (getColumnClass(this.editingColumn) == TreeTableModel.class) ? -1 : this.editingRow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRowHeight(int rowHeight) {
/* 134 */     super.setRowHeight(rowHeight);
/* 135 */     if (this.tree != null && this.tree.getRowHeight() != rowHeight) {
/* 136 */       this.tree.setRowHeight(getRowHeight());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JTree getTree() {
/* 144 */     return this.tree;
/*     */   }
/*     */ 
/*     */   
/*     */   public class TreeTableCellRenderer
/*     */     extends JTree
/*     */     implements TableCellRenderer
/*     */   {
/*     */     private static final long serialVersionUID = -193867880014600717L;
/*     */     protected int visibleRow;
/*     */     
/*     */     public TreeTableCellRenderer(TreeModel model) {
/* 156 */       super(model);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateUI() {
/* 165 */       super.updateUI();
/*     */ 
/*     */       
/* 168 */       TreeCellRenderer tcr = getCellRenderer();
/* 169 */       if (tcr instanceof DefaultTreeCellRenderer) {
/* 170 */         DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer)tcr;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 175 */         dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
/*     */         
/* 177 */         dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setRowHeight(int rowHeight) {
/* 188 */       if (rowHeight > 0) {
/* 189 */         super.setRowHeight(rowHeight);
/* 190 */         if (JTreeTable.this != null && JTreeTable.this.getRowHeight() != rowHeight)
/*     */         {
/* 192 */           JTreeTable.this.setRowHeight(getRowHeight());
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setBounds(int x, int y, int w, int h) {
/* 202 */       super.setBounds(x, 0, w, JTreeTable.this.getHeight());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void paint(Graphics g) {
/* 211 */       g.translate(0, -this.visibleRow * getRowHeight());
/* 212 */       super.paint(g);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 223 */       if (isSelected) {
/* 224 */         setBackground(table.getSelectionBackground());
/*     */       } else {
/* 226 */         setBackground(table.getBackground());
/*     */       } 
/* 228 */       this.visibleRow = row;
/* 229 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class TreeTableCellEditor
/*     */     extends AbstractCellEditor
/*     */     implements TableCellEditor
/*     */   {
/*     */     public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
/* 244 */       return JTreeTable.this.tree;
/*     */     }
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
/*     */     public boolean isCellEditable(EventObject e) {
/* 267 */       if (e instanceof MouseEvent) {
/* 268 */         for (int counter = JTreeTable.this.getColumnCount() - 1; counter >= 0; 
/* 269 */           counter--) {
/* 270 */           if (JTreeTable.this.getColumnClass(counter) == TreeTableModel.class) {
/* 271 */             MouseEvent me = (MouseEvent)e;
/* 272 */             MouseEvent newME = new MouseEvent(JTreeTable.this.tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - (JTreeTable.this.getCellRect(0, counter, true)).x, me.getY(), me.getClickCount(), me.isPopupTrigger());
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 277 */             JTreeTable.this.tree.dispatchEvent(newME);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/* 282 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class ListToTreeSelectionModelWrapper
/*     */     extends DefaultTreeSelectionModel
/*     */   {
/*     */     private static final long serialVersionUID = 8168140829623071131L;
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean updatingListSelectionModel;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ListToTreeSelectionModelWrapper() {
/* 303 */       getListSelectionModel().addListSelectionListener(createListSelectionListener());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ListSelectionModel getListSelectionModel() {
/* 313 */       return this.listSelectionModel;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void resetRowSelection() {
/* 323 */       if (!this.updatingListSelectionModel) {
/* 324 */         this.updatingListSelectionModel = true;
/*     */         try {
/* 326 */           super.resetRowSelection();
/*     */         } finally {
/*     */           
/* 329 */           this.updatingListSelectionModel = false;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected ListSelectionListener createListSelectionListener() {
/* 343 */       return new ListSelectionHandler();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void updateSelectedPathsFromSelectedRows() {
/* 352 */       if (!this.updatingListSelectionModel) {
/* 353 */         this.updatingListSelectionModel = true;
/*     */ 
/*     */         
/*     */         try {
/* 357 */           int min = this.listSelectionModel.getMinSelectionIndex();
/* 358 */           int max = this.listSelectionModel.getMaxSelectionIndex();
/*     */           
/* 360 */           clearSelection();
/* 361 */           if (min != -1 && max != -1) {
/* 362 */             for (int counter = min; counter <= max; counter++) {
/* 363 */               if (this.listSelectionModel.isSelectedIndex(counter)) {
/* 364 */                 TreePath selPath = JTreeTable.this.tree.getPathForRow(counter);
/*     */ 
/*     */                 
/* 367 */                 if (selPath != null) {
/* 368 */                   addSelectionPath(selPath);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } finally {
/*     */           
/* 375 */           this.updatingListSelectionModel = false;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     class ListSelectionHandler
/*     */       implements ListSelectionListener
/*     */     {
/*     */       public void valueChanged(ListSelectionEvent e) {
/* 386 */         JTreeTable.ListToTreeSelectionModelWrapper.this.updateSelectedPathsFromSelectedRows();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\debugger\treetable\JTreeTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */