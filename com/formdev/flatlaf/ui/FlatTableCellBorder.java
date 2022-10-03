/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
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
/*     */ public class FlatTableCellBorder
/*     */   extends FlatLineBorder
/*     */ {
/*  36 */   final boolean showCellFocusIndicator = UIManager.getBoolean("Table.showCellFocusIndicator");
/*     */   
/*     */   protected FlatTableCellBorder() {
/*  39 */     super(UIManager.getInsets("Table.cellMargins"), UIManager.getColor("Table.cellFocusColor"));
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
/*     */   public static class Default
/*     */     extends FlatTableCellBorder
/*     */   {
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Focused
/*     */     extends FlatTableCellBorder {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Selected
/*     */     extends FlatTableCellBorder
/*     */   {
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*  77 */       if (!this.showCellFocusIndicator) {
/*  78 */         JTable table = (JTable)SwingUtilities.getAncestorOfClass(JTable.class, c);
/*  79 */         if (table != null && !isSelectionEditable(table)) {
/*     */           return;
/*     */         }
/*     */       } 
/*  83 */       super.paintBorder(c, g, x, y, width, height);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean isSelectionEditable(JTable table) {
/*  90 */       if (table.getRowSelectionAllowed()) {
/*  91 */         int columnCount = table.getColumnCount();
/*  92 */         int[] selectedRows = table.getSelectedRows();
/*  93 */         for (int selectedRow : selectedRows) {
/*  94 */           for (int column = 0; column < columnCount; column++) {
/*  95 */             if (table.isCellEditable(selectedRow, column)) {
/*  96 */               return true;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 101 */       if (table.getColumnSelectionAllowed()) {
/* 102 */         int rowCount = table.getRowCount();
/* 103 */         int[] selectedColumns = table.getSelectedColumns();
/* 104 */         for (int selectedColumn : selectedColumns) {
/* 105 */           for (int row = 0; row < rowCount; row++) {
/* 106 */             if (table.isCellEditable(row, selectedColumn)) {
/* 107 */               return true;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 112 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTableCellBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */