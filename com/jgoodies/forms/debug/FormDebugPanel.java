/*     */ package com.jgoodies.forms.debug;
/*     */ 
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.LayoutManager;
/*     */ import javax.swing.JPanel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormDebugPanel
/*     */   extends JPanel
/*     */ {
/*     */   public static boolean paintRowsDefault = true;
/*  69 */   private static final Color DEFAULT_GRID_COLOR = Color.red;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean paintInBackground;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean paintDiagonals;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   private boolean paintRows = paintRowsDefault;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private Color gridColor = DEFAULT_GRID_COLOR;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormDebugPanel() {
/* 103 */     this((FormLayout)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormDebugPanel(FormLayout layout) {
/* 114 */     this(layout, false, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormDebugPanel(boolean paintInBackground, boolean paintDiagonals) {
/* 131 */     this((FormLayout)null, paintInBackground, paintDiagonals);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormDebugPanel(FormLayout layout, boolean paintInBackground, boolean paintDiagonals) {
/* 151 */     super((LayoutManager)layout);
/* 152 */     setPaintInBackground(paintInBackground);
/* 153 */     setPaintDiagonals(paintDiagonals);
/* 154 */     setGridColor(DEFAULT_GRID_COLOR);
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
/*     */   public void setPaintInBackground(boolean b) {
/* 166 */     this.paintInBackground = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPaintDiagonals(boolean b) {
/* 175 */     this.paintDiagonals = b;
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
/*     */   public void setPaintRows(boolean b) {
/* 187 */     this.paintRows = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGridColor(Color color) {
/* 196 */     this.gridColor = color;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/* 213 */     super.paintComponent(g);
/* 214 */     if (this.paintInBackground) {
/* 215 */       paintGrid(g);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void paint(Graphics g) {
/* 232 */     super.paint(g);
/* 233 */     if (!this.paintInBackground) {
/* 234 */       paintGrid(g);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void paintGrid(Graphics g) {
/* 245 */     if (!(getLayout() instanceof FormLayout)) {
/*     */       return;
/*     */     }
/* 248 */     FormLayout.LayoutInfo layoutInfo = FormDebugUtils.getLayoutInfo(this);
/* 249 */     int left = layoutInfo.getX();
/* 250 */     int top = layoutInfo.getY();
/* 251 */     int width = layoutInfo.getWidth();
/* 252 */     int height = layoutInfo.getHeight();
/*     */     
/* 254 */     g.setColor(this.gridColor);
/*     */ 
/*     */     
/* 257 */     int last = layoutInfo.columnOrigins.length - 1;
/* 258 */     for (int col = 0; col <= last; col++) {
/* 259 */       boolean firstOrLast = (col == 0 || col == last);
/* 260 */       int x = layoutInfo.columnOrigins[col];
/* 261 */       int start = firstOrLast ? 0 : top;
/* 262 */       int stop = firstOrLast ? getHeight() : (top + height);
/* 263 */       for (int i = start; i < stop; i += 5) {
/* 264 */         int length = Math.min(3, stop - i);
/* 265 */         g.fillRect(x, i, 1, length);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 270 */     last = layoutInfo.rowOrigins.length - 1;
/* 271 */     for (int row = 0; row <= last; row++) {
/* 272 */       boolean firstOrLast = (row == 0 || row == last);
/* 273 */       int y = layoutInfo.rowOrigins[row];
/* 274 */       int start = firstOrLast ? 0 : left;
/* 275 */       int stop = firstOrLast ? getWidth() : (left + width);
/* 276 */       if (firstOrLast || this.paintRows) {
/* 277 */         for (int i = start; i < stop; i += 5) {
/* 278 */           int length = Math.min(3, stop - i);
/* 279 */           g.fillRect(i, y, length, 1);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 284 */     if (this.paintDiagonals) {
/* 285 */       g.drawLine(left, top, left + width, top + height);
/* 286 */       g.drawLine(left, top + height, left + width, top);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\debug\FormDebugPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */