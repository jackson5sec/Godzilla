/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.SystemColor;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.ListModel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.basic.BasicListUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FastListUI
/*     */   extends BasicListUI
/*     */ {
/*     */   private boolean overriddenBackground;
/*     */   private boolean overriddenForeground;
/*     */   private static final int ESTIMATION_THRESHOLD = 200;
/*     */   
/*     */   private Color determineSelectionBackground() {
/*  58 */     Color c = UIManager.getColor("List.selectionBackground");
/*  59 */     if (c == null) {
/*  60 */       c = UIManager.getColor("nimbusSelectionBackground");
/*  61 */       if (c == null) {
/*  62 */         c = UIManager.getColor("textHighlight");
/*  63 */         if (c == null) {
/*  64 */           c = SystemColor.textHighlight;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  73 */     return new Color(c.getRGB());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Color determineSelectionForeground() {
/*  79 */     Color c = UIManager.getColor("List.selectionForeground");
/*  80 */     if (c == null) {
/*  81 */       c = UIManager.getColor("nimbusSelectedText");
/*  82 */       if (c == null) {
/*  83 */         c = UIManager.getColor("textHighlightText");
/*  84 */         if (c == null) {
/*  85 */           c = SystemColor.textHighlightText;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  91 */     return new Color(c.getRGB());
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
/*     */   protected void installDefaults() {
/* 104 */     super.installDefaults();
/*     */     
/* 106 */     if (this.list.getSelectionBackground() == null) {
/* 107 */       this.list.setSelectionBackground(determineSelectionBackground());
/* 108 */       this.overriddenBackground = true;
/*     */     } 
/*     */     
/* 111 */     if (this.list.getSelectionForeground() == null) {
/* 112 */       this.list.setSelectionForeground(determineSelectionForeground());
/* 113 */       this.overriddenForeground = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/* 124 */     super.uninstallDefaults();
/*     */     
/* 126 */     if (this.overriddenBackground) {
/* 127 */       this.list.setSelectionBackground((Color)null);
/*     */     }
/*     */     
/* 130 */     if (this.overriddenForeground) {
/* 131 */       this.list.setSelectionForeground((Color)null);
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
/*     */   protected void updateLayoutState() {
/* 147 */     ListModel model = this.list.getModel();
/* 148 */     int itemCount = model.getSize();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     if (itemCount < 200) {
/* 154 */       super.updateLayoutState();
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 160 */     ListCellRenderer<Object> renderer = (ListCellRenderer)this.list.getCellRenderer();
/*     */     
/* 162 */     this.cellWidth = this.list.getWidth();
/* 163 */     if (this.list.getParent() instanceof javax.swing.JViewport) {
/* 164 */       this.cellWidth = this.list.getParent().getWidth();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 169 */     this.cellHeights = null;
/*     */     
/* 171 */     if (renderer != null) {
/* 172 */       Object value = model.getElementAt(0);
/* 173 */       Component c = renderer.getListCellRendererComponent(this.list, value, 0, false, false);
/*     */       
/* 175 */       this.rendererPane.add(c);
/* 176 */       Dimension cellSize = c.getPreferredSize();
/* 177 */       this.cellHeight = cellSize.height;
/* 178 */       this.cellWidth = Math.max(this.cellWidth, cellSize.width);
/*     */     } else {
/*     */       
/* 181 */       this.cellHeight = 20;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\FastListUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */