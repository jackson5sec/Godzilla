/*     */ package com.formdev.flatlaf.demo;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.AbstractListModel;
/*     */ import javax.swing.DefaultCellEditor;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.DropMode;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.TransferHandler;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ class DataComponentsPanel extends JPanel {
/*     */   private JList<String> list1;
/*     */   private JList<String> list2;
/*     */   private JTree tree1;
/*     */   private JTree tree2;
/*     */   private JTable table1;
/*     */   private JCheckBox showHorizontalLinesCheckBox;
/*     */   
/*     */   DataComponentsPanel() {
/*  37 */     initComponents();
/*     */   }
/*     */   private JCheckBox showVerticalLinesCheckBox; private JCheckBox intercellSpacingCheckBox; private JCheckBox redGridColorCheckBox; private JCheckBox rowSelectionCheckBox; private JCheckBox columnSelectionCheckBox; private JCheckBox dndCheckBox;
/*     */   private void dndChanged() {
/*  41 */     boolean dnd = this.dndCheckBox.isSelected();
/*  42 */     this.list1.setDragEnabled(dnd);
/*  43 */     this.list2.setDragEnabled(dnd);
/*  44 */     this.tree1.setDragEnabled(dnd);
/*  45 */     this.tree2.setDragEnabled(dnd);
/*  46 */     this.table1.setDragEnabled(dnd);
/*     */     
/*  48 */     DropMode dropMode = dnd ? DropMode.ON_OR_INSERT : DropMode.USE_SELECTION;
/*  49 */     this.list1.setDropMode(dropMode);
/*  50 */     this.tree1.setDropMode(dropMode);
/*  51 */     this.table1.setDropMode(dropMode);
/*     */     
/*  53 */     String key = "FlatLaf.oldTransferHandler";
/*  54 */     if (dnd) {
/*  55 */       this.list1.putClientProperty(key, this.list1.getTransferHandler());
/*  56 */       this.list1.setTransferHandler(new DummyTransferHandler());
/*     */       
/*  58 */       this.tree1.putClientProperty(key, this.tree1.getTransferHandler());
/*  59 */       this.tree1.setTransferHandler(new DummyTransferHandler());
/*     */       
/*  61 */       this.table1.putClientProperty(key, this.table1.getTransferHandler());
/*  62 */       this.table1.setTransferHandler(new DummyTransferHandler());
/*     */     } else {
/*  64 */       this.list1.setTransferHandler((TransferHandler)this.list1.getClientProperty(key));
/*  65 */       this.tree1.setTransferHandler((TransferHandler)this.tree1.getClientProperty(key));
/*  66 */       this.table1.setTransferHandler((TransferHandler)this.table1.getClientProperty(key));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void rowSelectionChanged() {
/*  71 */     this.table1.setRowSelectionAllowed(this.rowSelectionCheckBox.isSelected());
/*     */   }
/*     */   
/*     */   private void columnSelectionChanged() {
/*  75 */     this.table1.setColumnSelectionAllowed(this.columnSelectionCheckBox.isSelected());
/*     */   }
/*     */   
/*     */   private void showHorizontalLinesChanged() {
/*  79 */     this.table1.setShowHorizontalLines(this.showHorizontalLinesCheckBox.isSelected());
/*     */   }
/*     */   
/*     */   private void showVerticalLinesChanged() {
/*  83 */     this.table1.setShowVerticalLines(this.showVerticalLinesCheckBox.isSelected());
/*     */   }
/*     */   
/*     */   private void intercellSpacingChanged() {
/*  87 */     this.table1.setIntercellSpacing(this.intercellSpacingCheckBox.isSelected() ? new Dimension(1, 1) : new Dimension());
/*     */   }
/*     */   
/*     */   private void redGridColorChanged() {
/*  91 */     this.table1.setGridColor(this.redGridColorCheckBox.isSelected() ? Color.red : UIManager.getColor("Table.gridColor"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateUI() {
/*  96 */     super.updateUI();
/*     */     
/*  98 */     EventQueue.invokeLater(() -> {
/*     */           showHorizontalLinesChanged();
/*     */           showVerticalLinesChanged();
/*     */           intercellSpacingChanged();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initComponents() {
/* 108 */     JLabel listLabel = new JLabel();
/* 109 */     JScrollPane scrollPane1 = new JScrollPane();
/* 110 */     this.list1 = new JList<>();
/* 111 */     JScrollPane scrollPane2 = new JScrollPane();
/* 112 */     this.list2 = new JList<>();
/* 113 */     JLabel treeLabel = new JLabel();
/* 114 */     JScrollPane scrollPane3 = new JScrollPane();
/* 115 */     this.tree1 = new JTree();
/* 116 */     JScrollPane scrollPane4 = new JScrollPane();
/* 117 */     this.tree2 = new JTree();
/* 118 */     JLabel tableLabel = new JLabel();
/* 119 */     JScrollPane scrollPane5 = new JScrollPane();
/* 120 */     this.table1 = new JTable();
/* 121 */     JPanel tableOptionsPanel = new JPanel();
/* 122 */     this.showHorizontalLinesCheckBox = new JCheckBox();
/* 123 */     this.showVerticalLinesCheckBox = new JCheckBox();
/* 124 */     this.intercellSpacingCheckBox = new JCheckBox();
/* 125 */     this.redGridColorCheckBox = new JCheckBox();
/* 126 */     this.rowSelectionCheckBox = new JCheckBox();
/* 127 */     this.columnSelectionCheckBox = new JCheckBox();
/* 128 */     this.dndCheckBox = new JCheckBox();
/* 129 */     JPopupMenu popupMenu2 = new JPopupMenu();
/* 130 */     JMenuItem menuItem3 = new JMenuItem();
/* 131 */     JMenuItem menuItem4 = new JMenuItem();
/* 132 */     JMenuItem menuItem5 = new JMenuItem();
/* 133 */     JMenuItem menuItem6 = new JMenuItem();
/*     */ 
/*     */     
/* 136 */     setLayout((LayoutManager)new MigLayout("insets dialog,hidemode 3", "[][200,fill][200,fill][fill]", "[150,grow,sizegroup 1,fill][150,grow,sizegroup 1,fill][150,grow,sizegroup 1,fill]"));
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
/* 149 */     listLabel.setText("JList:");
/* 150 */     add(listLabel, "cell 0 0,aligny top,growy 0");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     this.list1.setModel(new AbstractListModel<String>() {
/* 157 */           String[] values = new String[] { "item 1", "item 2", "item 3", "item 4", "item 5", "item 6", "item 7", "item 8", "item 9", "item 10", "item 11", "item 12", "item 13", "item 14", "item 15" };
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
/*     */           public int getSize() {
/* 175 */             return this.values.length;
/*     */           }
/* 177 */           public String getElementAt(int i) { return this.values[i]; }
/*     */         });
/* 179 */     this.list1.setComponentPopupMenu(popupMenu2);
/* 180 */     scrollPane1.setViewportView(this.list1);
/*     */     
/* 182 */     add(scrollPane1, "cell 1 0");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     this.list2.setModel(new AbstractListModel<String>() {
/* 189 */           String[] values = new String[] { "item 1", "item 2", "item 3", "item 4", "item 5", "item 6", "item 7", "item 8", "item 9", "item 10", "item 11", "item 12", "item 13", "item 14", "item 15" };
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
/*     */           public int getSize() {
/* 207 */             return this.values.length;
/*     */           }
/* 209 */           public String getElementAt(int i) { return this.values[i]; }
/*     */         });
/* 211 */     this.list2.setEnabled(false);
/* 212 */     scrollPane2.setViewportView(this.list2);
/*     */     
/* 214 */     add(scrollPane2, "cell 2 0");
/*     */ 
/*     */     
/* 217 */     treeLabel.setText("JTree:");
/* 218 */     add(treeLabel, "cell 0 1,aligny top,growy 0");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     this.tree1.setShowsRootHandles(true);
/* 225 */     this.tree1.setEditable(true);
/* 226 */     this.tree1.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("JTree")
/*     */           {
/*     */           
/*     */           }));
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
/* 252 */     this.tree1.setComponentPopupMenu(popupMenu2);
/* 253 */     scrollPane3.setViewportView(this.tree1);
/*     */     
/* 255 */     add(scrollPane3, "cell 1 1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     this.tree2.setEnabled(false);
/* 262 */     scrollPane4.setViewportView(this.tree2);
/*     */     
/* 264 */     add(scrollPane4, "cell 2 1");
/*     */ 
/*     */     
/* 267 */     tableLabel.setText("JTable:");
/* 268 */     add(tableLabel, "cell 0 2,aligny top,growy 0");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     this.table1.setModel(new DefaultTableModel(new Object[][] { { "item 1", "item 1b", "January", "July", 
/*     */               
/* 276 */               Integer.valueOf(123), null }, , { "item 2", "item 2b", "February", "August", 
/* 277 */               Integer.valueOf(456), Boolean.valueOf(true) }, , { "item 3", null, "March", null, null, null }, , { "item 4", null, "April", null, null, null }, , { "item 5", null, "May", null, null, null }, , { "item 6", null, "June", null, null, null }, , { "item 7", null, "July", null, null, null }, , { "item 8", null, "August", null, null, null }, , { "item 9", null, "September", null, null, null }, , { "item 10", null, "October", null, null, null }, , { "item 11", null, "November", null, null, null }, , { "item 12", null, "December", null, null, null },  }, (Object[])new String[] { "Not editable", "Text", "Combo", "Combo Editable", "Integer", "Boolean" })
/*     */         {
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
/* 293 */           Class<?>[] columnTypes = new Class[] { Object.class, Object.class, String.class, String.class, Integer.class, Boolean.class };
/*     */ 
/*     */           
/* 296 */           boolean[] columnEditable = new boolean[] { false, true, true, true, true, true };
/*     */ 
/*     */ 
/*     */           
/*     */           public Class<?> getColumnClass(int columnIndex) {
/* 301 */             return this.columnTypes[columnIndex];
/*     */           }
/*     */           
/*     */           public boolean isCellEditable(int rowIndex, int columnIndex) {
/* 305 */             return this.columnEditable[columnIndex];
/*     */           }
/*     */         });
/*     */     
/* 309 */     TableColumnModel cm = this.table1.getColumnModel();
/* 310 */     cm.getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox(new DefaultComboBoxModel((Object[])new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }))));
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
/* 325 */     cm.getColumn(3).setCellEditor(new DefaultCellEditor(new JComboBox(new DefaultComboBoxModel((Object[])new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }))));
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
/* 341 */     this.table1.setAutoCreateRowSorter(true);
/* 342 */     this.table1.setComponentPopupMenu(popupMenu2);
/* 343 */     scrollPane5.setViewportView(this.table1);
/*     */     
/* 345 */     add(scrollPane5, "cell 1 2 2 1,width 300");
/*     */ 
/*     */ 
/*     */     
/* 349 */     tableOptionsPanel.setLayout((LayoutManager)new MigLayout("insets 0,hidemode 3", "[]", "[]0[]0[]0[]0[]0[]0[]0"));
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
/* 363 */     this.showHorizontalLinesCheckBox.setText("show horizontal lines");
/* 364 */     this.showHorizontalLinesCheckBox.addActionListener(e -> showHorizontalLinesChanged());
/* 365 */     tableOptionsPanel.add(this.showHorizontalLinesCheckBox, "cell 0 0");
/*     */ 
/*     */     
/* 368 */     this.showVerticalLinesCheckBox.setText("show vertical lines");
/* 369 */     this.showVerticalLinesCheckBox.addActionListener(e -> showVerticalLinesChanged());
/* 370 */     tableOptionsPanel.add(this.showVerticalLinesCheckBox, "cell 0 1");
/*     */ 
/*     */     
/* 373 */     this.intercellSpacingCheckBox.setText("intercell spacing");
/* 374 */     this.intercellSpacingCheckBox.addActionListener(e -> intercellSpacingChanged());
/* 375 */     tableOptionsPanel.add(this.intercellSpacingCheckBox, "cell 0 2");
/*     */ 
/*     */     
/* 378 */     this.redGridColorCheckBox.setText("red grid color");
/* 379 */     this.redGridColorCheckBox.addActionListener(e -> redGridColorChanged());
/* 380 */     tableOptionsPanel.add(this.redGridColorCheckBox, "cell 0 3");
/*     */ 
/*     */     
/* 383 */     this.rowSelectionCheckBox.setText("row selection");
/* 384 */     this.rowSelectionCheckBox.setSelected(true);
/* 385 */     this.rowSelectionCheckBox.addActionListener(e -> rowSelectionChanged());
/* 386 */     tableOptionsPanel.add(this.rowSelectionCheckBox, "cell 0 4");
/*     */ 
/*     */     
/* 389 */     this.columnSelectionCheckBox.setText("column selection");
/* 390 */     this.columnSelectionCheckBox.addActionListener(e -> columnSelectionChanged());
/* 391 */     tableOptionsPanel.add(this.columnSelectionCheckBox, "cell 0 5");
/*     */ 
/*     */     
/* 394 */     this.dndCheckBox.setText("enable drag and drop");
/* 395 */     this.dndCheckBox.setMnemonic('D');
/* 396 */     this.dndCheckBox.addActionListener(e -> dndChanged());
/* 397 */     tableOptionsPanel.add(this.dndCheckBox, "cell 0 6");
/*     */     
/* 399 */     add(tableOptionsPanel, "cell 3 2");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 405 */     menuItem3.setText("Some Action");
/* 406 */     popupMenu2.add(menuItem3);
/*     */ 
/*     */     
/* 409 */     menuItem4.setText("More Action");
/* 410 */     popupMenu2.add(menuItem4);
/* 411 */     popupMenu2.addSeparator();
/*     */ 
/*     */     
/* 414 */     menuItem5.setText("No Action");
/* 415 */     popupMenu2.add(menuItem5);
/*     */ 
/*     */     
/* 418 */     menuItem6.setText("Noop Action");
/* 419 */     popupMenu2.add(menuItem6);
/*     */ 
/*     */ 
/*     */     
/* 423 */     ((JComboBox)((DefaultCellEditor)this.table1.getColumnModel().getColumn(3).getCellEditor()).getComponent()).setEditable(true);
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
/*     */   private static class DummyTransferHandler
/*     */     extends TransferHandler
/*     */   {
/*     */     private DummyTransferHandler() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Transferable createTransferable(JComponent c) {
/* 448 */       if (c instanceof JList && ((JList)c).isSelectionEmpty())
/* 449 */         return null; 
/* 450 */       if (c instanceof JTree && ((JTree)c).isSelectionEmpty())
/* 451 */         return null; 
/* 452 */       if (c instanceof JTable && ((JTable)c).getSelectionModel().isSelectionEmpty()) {
/* 453 */         return null;
/*     */       }
/* 455 */       return new StringSelection("dummy");
/*     */     }
/*     */ 
/*     */     
/*     */     public int getSourceActions(JComponent c) {
/* 460 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canImport(TransferHandler.TransferSupport support) {
/* 465 */       return support.isDataFlavorSupported(DataFlavor.stringFlavor);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean importData(TransferHandler.TransferSupport support) {
/* 470 */       String message = String.valueOf(support.getDropLocation());
/* 471 */       SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Drop", -1));
/*     */ 
/*     */       
/* 474 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\DataComponentsPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */