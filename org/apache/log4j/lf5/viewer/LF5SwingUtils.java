/*     */ package org.apache.log4j.lf5.viewer;
/*     */ 
/*     */ import java.awt.Adjustable;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.table.TableModel;
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
/*     */ public class LF5SwingUtils
/*     */ {
/*     */   public static void selectRow(int row, JTable table, JScrollPane pane) {
/*  67 */     if (table == null || pane == null) {
/*     */       return;
/*     */     }
/*  70 */     if (!contains(row, table.getModel())) {
/*     */       return;
/*     */     }
/*  73 */     moveAdjustable(row * table.getRowHeight(), pane.getVerticalScrollBar());
/*  74 */     selectRow(row, table.getSelectionModel());
/*     */ 
/*     */ 
/*     */     
/*  78 */     repaintLater(table);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeScrollBarTrack(Adjustable scrollBar) {
/*  86 */     if (scrollBar == null) {
/*     */       return;
/*     */     }
/*  89 */     scrollBar.addAdjustmentListener(new TrackingAdjustmentListener());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeVerticalScrollBarTrack(JScrollPane pane) {
/*  98 */     if (pane == null) {
/*     */       return;
/*     */     }
/* 101 */     makeScrollBarTrack(pane.getVerticalScrollBar());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean contains(int row, TableModel model) {
/* 108 */     if (model == null) {
/* 109 */       return false;
/*     */     }
/* 111 */     if (row < 0) {
/* 112 */       return false;
/*     */     }
/* 114 */     if (row >= model.getRowCount()) {
/* 115 */       return false;
/*     */     }
/* 117 */     return true;
/*     */   }
/*     */   
/*     */   protected static void selectRow(int row, ListSelectionModel model) {
/* 121 */     if (model == null) {
/*     */       return;
/*     */     }
/* 124 */     model.setSelectionInterval(row, row);
/*     */   }
/*     */   
/*     */   protected static void moveAdjustable(int location, Adjustable scrollBar) {
/* 128 */     if (scrollBar == null) {
/*     */       return;
/*     */     }
/* 131 */     scrollBar.setValue(location);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void repaintLater(final JComponent component) {
/* 139 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 141 */             component.repaint();
/*     */           }
/*     */           
/*     */           private final JComponent val$component;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\lf5\viewer\LF5SwingUtils.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */