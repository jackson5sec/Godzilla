/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatRadioButtonMenuItemUI
/*    */   extends BasicRadioButtonMenuItemUI
/*    */ {
/*    */   private FlatMenuItemRenderer renderer;
/*    */   
/*    */   public static ComponentUI createUI(JComponent c) {
/* 61 */     return new FlatRadioButtonMenuItemUI();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void installDefaults() {
/* 66 */     super.installDefaults();
/*    */     
/* 68 */     LookAndFeel.installProperty(this.menuItem, "iconTextGap", Integer.valueOf(FlatUIUtils.getUIInt("MenuItem.iconTextGap", 4)));
/*    */     
/* 70 */     this.renderer = createRenderer();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void uninstallDefaults() {
/* 75 */     super.uninstallDefaults();
/*    */     
/* 77 */     this.renderer = null;
/*    */   }
/*    */   
/*    */   protected FlatMenuItemRenderer createRenderer() {
/* 81 */     return new FlatMenuItemRenderer(this.menuItem, this.checkIcon, this.arrowIcon, this.acceleratorFont, this.acceleratorDelimiter);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
/* 86 */     return this.renderer.getPreferredMenuItemSize();
/*    */   }
/*    */ 
/*    */   
/*    */   public void paint(Graphics g, JComponent c) {
/* 91 */     this.renderer.paintMenuItem(g, this.selectionBackground, this.selectionForeground, this.disabledForeground, this.acceleratorForeground, this.acceleratorSelectionForeground);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatRadioButtonMenuItemUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */