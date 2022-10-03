/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.Graphics2DProxy;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTableUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatTableUI
/*     */   extends BasicTableUI
/*     */ {
/*     */   protected boolean showHorizontalLines;
/*     */   protected boolean showVerticalLines;
/*     */   protected Dimension intercellSpacing;
/*     */   protected Color selectionBackground;
/*     */   protected Color selectionForeground;
/*     */   protected Color selectionInactiveBackground;
/*     */   protected Color selectionInactiveForeground;
/*     */   private boolean oldShowHorizontalLines;
/*     */   private boolean oldShowVerticalLines;
/*     */   private Dimension oldIntercellSpacing;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/* 103 */     return new FlatTableUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/* 108 */     super.installDefaults();
/*     */     
/* 110 */     this.showHorizontalLines = UIManager.getBoolean("Table.showHorizontalLines");
/* 111 */     this.showVerticalLines = UIManager.getBoolean("Table.showVerticalLines");
/* 112 */     this.intercellSpacing = UIManager.getDimension("Table.intercellSpacing");
/*     */     
/* 114 */     this.selectionBackground = UIManager.getColor("Table.selectionBackground");
/* 115 */     this.selectionForeground = UIManager.getColor("Table.selectionForeground");
/* 116 */     this.selectionInactiveBackground = UIManager.getColor("Table.selectionInactiveBackground");
/* 117 */     this.selectionInactiveForeground = UIManager.getColor("Table.selectionInactiveForeground");
/*     */     
/* 119 */     toggleSelectionColors();
/*     */     
/* 121 */     int rowHeight = FlatUIUtils.getUIInt("Table.rowHeight", 16);
/* 122 */     if (rowHeight > 0) {
/* 123 */       LookAndFeel.installProperty(this.table, "rowHeight", Integer.valueOf(UIScale.scale(rowHeight)));
/*     */     }
/* 125 */     if (!this.showHorizontalLines) {
/* 126 */       this.oldShowHorizontalLines = this.table.getShowHorizontalLines();
/* 127 */       this.table.setShowHorizontalLines(false);
/*     */     } 
/* 129 */     if (!this.showVerticalLines) {
/* 130 */       this.oldShowVerticalLines = this.table.getShowVerticalLines();
/* 131 */       this.table.setShowVerticalLines(false);
/*     */     } 
/*     */     
/* 134 */     if (this.intercellSpacing != null) {
/* 135 */       this.oldIntercellSpacing = this.table.getIntercellSpacing();
/* 136 */       this.table.setIntercellSpacing(this.intercellSpacing);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/* 142 */     super.uninstallDefaults();
/*     */     
/* 144 */     this.selectionBackground = null;
/* 145 */     this.selectionForeground = null;
/* 146 */     this.selectionInactiveBackground = null;
/* 147 */     this.selectionInactiveForeground = null;
/*     */ 
/*     */     
/* 150 */     if (!this.showHorizontalLines && this.oldShowHorizontalLines && !this.table.getShowHorizontalLines())
/* 151 */       this.table.setShowHorizontalLines(true); 
/* 152 */     if (!this.showVerticalLines && this.oldShowVerticalLines && !this.table.getShowVerticalLines()) {
/* 153 */       this.table.setShowVerticalLines(true);
/*     */     }
/*     */     
/* 156 */     if (this.intercellSpacing != null && this.table.getIntercellSpacing().equals(this.intercellSpacing)) {
/* 157 */       this.table.setIntercellSpacing(this.oldIntercellSpacing);
/*     */     }
/*     */   }
/*     */   
/*     */   protected FocusListener createFocusListener() {
/* 162 */     return new BasicTableUI.FocusHandler()
/*     */       {
/*     */         public void focusGained(FocusEvent e) {
/* 165 */           super.focusGained(e);
/* 166 */           FlatTableUI.this.toggleSelectionColors();
/*     */         }
/*     */ 
/*     */         
/*     */         public void focusLost(FocusEvent e) {
/* 171 */           super.focusLost(e);
/*     */ 
/*     */           
/* 174 */           EventQueue.invokeLater(() -> FlatTableUI.this.toggleSelectionColors());
/*     */         }
/*     */       };
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
/*     */   private void toggleSelectionColors() {
/* 191 */     if (this.table == null) {
/*     */       return;
/*     */     }
/* 194 */     if (FlatUIUtils.isPermanentFocusOwner(this.table)) {
/* 195 */       if (this.table.getSelectionBackground() == this.selectionInactiveBackground)
/* 196 */         this.table.setSelectionBackground(this.selectionBackground); 
/* 197 */       if (this.table.getSelectionForeground() == this.selectionInactiveForeground)
/* 198 */         this.table.setSelectionForeground(this.selectionForeground); 
/*     */     } else {
/* 200 */       if (this.table.getSelectionBackground() == this.selectionBackground)
/* 201 */         this.table.setSelectionBackground(this.selectionInactiveBackground); 
/* 202 */       if (this.table.getSelectionForeground() == this.selectionForeground)
/* 203 */         this.table.setSelectionForeground(this.selectionInactiveForeground); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/*     */     Graphics2DProxy graphics2DProxy;
/* 209 */     final boolean horizontalLines = this.table.getShowHorizontalLines();
/* 210 */     final boolean verticalLines = this.table.getShowVerticalLines();
/* 211 */     if (horizontalLines || verticalLines) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 217 */       final boolean hideLastVerticalLine = hideLastVerticalLine();
/* 218 */       final int tableWidth = this.table.getWidth();
/*     */       
/* 220 */       double systemScaleFactor = UIScale.getSystemScaleFactor((Graphics2D)g);
/* 221 */       final double lineThickness = 1.0D / systemScaleFactor * (int)systemScaleFactor;
/*     */ 
/*     */ 
/*     */       
/* 225 */       graphics2DProxy = new Graphics2DProxy((Graphics2D)g)
/*     */         {
/*     */           public void drawLine(int x1, int y1, int x2, int y2)
/*     */           {
/* 229 */             if (hideLastVerticalLine && verticalLines && x1 == x2 && y1 == 0 && x1 == tableWidth - 1 && 
/*     */               
/* 231 */               wasInvokedFromPaintGrid()) {
/*     */               return;
/*     */             }
/* 234 */             super.drawLine(x1, y1, x2, y2);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void fillRect(int x, int y, int width, int height) {
/* 240 */             if (hideLastVerticalLine && verticalLines && width == 1 && y == 0 && x == tableWidth - 1 && 
/*     */               
/* 242 */               wasInvokedFromPaintGrid()) {
/*     */               return;
/*     */             }
/*     */             
/* 246 */             if (lineThickness != 1.0D) {
/* 247 */               if (horizontalLines && height == 1 && wasInvokedFromPaintGrid()) {
/* 248 */                 fill(new Rectangle2D.Double(x, y, width, lineThickness));
/*     */                 return;
/*     */               } 
/* 251 */               if (verticalLines && width == 1 && y == 0 && wasInvokedFromPaintGrid()) {
/* 252 */                 fill(new Rectangle2D.Double(x, y, lineThickness, height));
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/* 257 */             super.fillRect(x, y, width, height);
/*     */           }
/*     */           
/*     */           private boolean wasInvokedFromPaintGrid() {
/* 261 */             StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
/* 262 */             for (int i = 0; i < 10 || i < stackTrace.length; i++) {
/* 263 */               if ("javax.swing.plaf.basic.BasicTableUI".equals(stackTrace[i].getClassName()) && "paintGrid"
/* 264 */                 .equals(stackTrace[i].getMethodName()))
/* 265 */                 return true; 
/*     */             } 
/* 267 */             return false;
/*     */           }
/*     */         };
/*     */     } 
/*     */     
/* 272 */     super.paint((Graphics)graphics2DProxy, c);
/*     */   }
/*     */   
/*     */   protected boolean hideLastVerticalLine() {
/* 276 */     Container viewport = SwingUtilities.getUnwrappedParent(this.table);
/* 277 */     Container viewportParent = (viewport != null) ? viewport.getParent() : null;
/* 278 */     if (!(viewportParent instanceof JScrollPane)) {
/* 279 */       return false;
/*     */     }
/*     */     
/* 282 */     if (this.table.getX() + this.table.getWidth() < viewport.getWidth()) {
/* 283 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     JScrollPane scrollPane = (JScrollPane)viewportParent;
/* 291 */     JViewport rowHeader = scrollPane.getRowHeader();
/* 292 */     return scrollPane.getComponentOrientation().isLeftToRight() ? ((viewport != rowHeader)) : ((viewport == rowHeader || rowHeader == null));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTableUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */