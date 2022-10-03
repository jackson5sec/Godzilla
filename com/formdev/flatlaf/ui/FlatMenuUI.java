/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicMenuUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatMenuUI
/*     */   extends BasicMenuUI
/*     */ {
/*     */   private Color hoverBackground;
/*     */   private FlatMenuItemRenderer renderer;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  80 */     return new FlatMenuUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  85 */     super.installDefaults();
/*     */     
/*  87 */     LookAndFeel.installProperty(this.menuItem, "iconTextGap", Integer.valueOf(FlatUIUtils.getUIInt("MenuItem.iconTextGap", 4)));
/*     */     
/*  89 */     this.menuItem.setRolloverEnabled(true);
/*     */     
/*  91 */     this.hoverBackground = UIManager.getColor("MenuBar.hoverBackground");
/*  92 */     this.renderer = createRenderer();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/*  97 */     super.uninstallDefaults();
/*     */     
/*  99 */     this.hoverBackground = null;
/* 100 */     this.renderer = null;
/*     */   }
/*     */   
/*     */   protected FlatMenuItemRenderer createRenderer() {
/* 104 */     return new FlatMenuRenderer(this.menuItem, this.checkIcon, this.arrowIcon, this.acceleratorFont, this.acceleratorDelimiter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected MouseInputListener createMouseInputListener(JComponent c) {
/* 109 */     return new BasicMenuUI.MouseInputHandler()
/*     */       {
/*     */         public void mouseEntered(MouseEvent e) {
/* 112 */           super.mouseEntered(e);
/* 113 */           rollover(e, true);
/*     */         }
/*     */ 
/*     */         
/*     */         public void mouseExited(MouseEvent e) {
/* 118 */           super.mouseExited(e);
/* 119 */           rollover(e, false);
/*     */         }
/*     */         
/*     */         private void rollover(MouseEvent e, boolean rollover) {
/* 123 */           JMenu menu = (JMenu)e.getSource();
/* 124 */           if (menu.isTopLevelMenu() && menu.isRolloverEnabled()) {
/* 125 */             menu.getModel().setRollover(rollover);
/* 126 */             menu.repaint();
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 137 */     return ((JMenu)this.menuItem).isTopLevelMenu() ? c.getPreferredSize() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
/* 142 */     return this.renderer.getPreferredMenuItemSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/* 147 */     this.renderer.paintMenuItem(g, this.selectionBackground, this.selectionForeground, this.disabledForeground, this.acceleratorForeground, this.acceleratorSelectionForeground);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class FlatMenuRenderer
/*     */     extends FlatMenuItemRenderer
/*     */   {
/* 156 */     protected final Color menuBarUnderlineSelectionBackground = FlatUIUtils.getUIColor("MenuBar.underlineSelectionBackground", this.underlineSelectionBackground);
/* 157 */     protected final Color menuBarUnderlineSelectionColor = FlatUIUtils.getUIColor("MenuBar.underlineSelectionColor", this.underlineSelectionColor);
/* 158 */     protected final int menuBarUnderlineSelectionHeight = FlatUIUtils.getUIInt("MenuBar.underlineSelectionHeight", this.underlineSelectionHeight);
/*     */ 
/*     */ 
/*     */     
/*     */     protected FlatMenuRenderer(JMenuItem menuItem, Icon checkIcon, Icon arrowIcon, Font acceleratorFont, String acceleratorDelimiter) {
/* 163 */       super(menuItem, checkIcon, arrowIcon, acceleratorFont, acceleratorDelimiter);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void paintBackground(Graphics g, Color selectionBackground) {
/* 168 */       if (isUnderlineSelection() && ((JMenu)this.menuItem).isTopLevelMenu()) {
/* 169 */         selectionBackground = this.menuBarUnderlineSelectionBackground;
/*     */       }
/* 171 */       ButtonModel model = this.menuItem.getModel();
/* 172 */       if (model.isRollover() && !model.isArmed() && !model.isSelected() && model
/* 173 */         .isEnabled() && ((JMenu)this.menuItem).isTopLevelMenu()) {
/*     */         
/* 175 */         g.setColor(deriveBackground(FlatMenuUI.this.hoverBackground));
/* 176 */         g.fillRect(0, 0, this.menuItem.getWidth(), this.menuItem.getHeight());
/*     */       } else {
/* 178 */         super.paintBackground(g, selectionBackground);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void paintUnderlineSelection(Graphics g, Color underlineSelectionColor, int underlineSelectionHeight) {
/* 183 */       if (((JMenu)this.menuItem).isTopLevelMenu()) {
/* 184 */         underlineSelectionColor = this.menuBarUnderlineSelectionColor;
/* 185 */         underlineSelectionHeight = this.menuBarUnderlineSelectionHeight;
/*     */       } 
/*     */       
/* 188 */       super.paintUnderlineSelection(g, underlineSelectionColor, underlineSelectionHeight);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatMenuUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */