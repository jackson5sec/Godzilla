/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicHTML;
/*     */ import javax.swing.plaf.basic.BasicOptionPaneUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatOptionPaneUI
/*     */   extends BasicOptionPaneUI
/*     */ {
/*     */   protected int iconMessageGap;
/*     */   protected int messagePadding;
/*     */   protected int maxCharactersPerLine;
/*     */   private int focusWidth;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  93 */     return new FlatOptionPaneUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  98 */     super.installDefaults();
/*     */     
/* 100 */     this.iconMessageGap = UIManager.getInt("OptionPane.iconMessageGap");
/* 101 */     this.messagePadding = UIManager.getInt("OptionPane.messagePadding");
/* 102 */     this.maxCharactersPerLine = UIManager.getInt("OptionPane.maxCharactersPerLine");
/* 103 */     this.focusWidth = UIManager.getInt("Component.focusWidth");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installComponents() {
/* 108 */     super.installComponents();
/*     */     
/* 110 */     updateChildPanels(this.optionPane);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumOptionPaneSize() {
/* 115 */     return UIScale.scale(super.getMinimumOptionPaneSize());
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getMaxCharactersPerLineCount() {
/* 120 */     int max = super.getMaxCharactersPerLineCount();
/* 121 */     return (this.maxCharactersPerLine > 0 && max == Integer.MAX_VALUE) ? this.maxCharactersPerLine : max;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Container createMessageArea() {
/* 126 */     Container messageArea = super.createMessageArea();
/*     */ 
/*     */     
/* 129 */     if (this.iconMessageGap > 0) {
/* 130 */       Component iconMessageSeparator = findByName(messageArea, "OptionPane.separator");
/* 131 */       if (iconMessageSeparator != null) {
/* 132 */         iconMessageSeparator.setPreferredSize(new Dimension(UIScale.scale(this.iconMessageGap), 1));
/*     */       }
/*     */     } 
/* 135 */     return messageArea;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Container createButtonArea() {
/* 140 */     Container buttonArea = super.createButtonArea();
/*     */ 
/*     */     
/* 143 */     if (buttonArea.getLayout() instanceof BasicOptionPaneUI.ButtonAreaLayout) {
/* 144 */       BasicOptionPaneUI.ButtonAreaLayout layout = (BasicOptionPaneUI.ButtonAreaLayout)buttonArea.getLayout();
/* 145 */       layout.setPadding(UIScale.scale(layout.getPadding() - this.focusWidth * 2));
/*     */     } 
/*     */     
/* 148 */     return buttonArea;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addMessageComponents(Container container, GridBagConstraints cons, Object msg, int maxll, boolean internallyCreated) {
/* 156 */     if (this.messagePadding > 0) {
/* 157 */       cons.insets.bottom = UIScale.scale(this.messagePadding);
/*     */     }
/*     */     
/* 160 */     if (msg instanceof String && BasicHTML.isHTMLString((String)msg)) {
/* 161 */       maxll = Integer.MAX_VALUE;
/*     */     }
/* 163 */     super.addMessageComponents(container, cons, msg, maxll, internallyCreated);
/*     */   }
/*     */   
/*     */   private void updateChildPanels(Container c) {
/* 167 */     for (Component child : c.getComponents()) {
/* 168 */       if (child instanceof JPanel) {
/* 169 */         JPanel panel = (JPanel)child;
/*     */ 
/*     */         
/* 172 */         panel.setOpaque(false);
/*     */ 
/*     */         
/* 175 */         Border border = panel.getBorder();
/* 176 */         if (border instanceof javax.swing.plaf.UIResource) {
/* 177 */           panel.setBorder(new NonUIResourceBorder(border));
/*     */         }
/*     */       } 
/* 180 */       if (child instanceof Container) {
/* 181 */         updateChildPanels((Container)child);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private Component findByName(Container c, String name) {
/* 187 */     for (Component child : c.getComponents()) {
/* 188 */       if (name.equals(child.getName())) {
/* 189 */         return child;
/*     */       }
/* 191 */       if (child instanceof Container) {
/* 192 */         Component c2 = findByName((Container)child, name);
/* 193 */         if (c2 != null)
/* 194 */           return c2; 
/*     */       } 
/*     */     } 
/* 197 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class NonUIResourceBorder
/*     */     implements Border
/*     */   {
/*     */     private final Border delegate;
/*     */ 
/*     */     
/*     */     NonUIResourceBorder(Border delegate) {
/* 208 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 213 */       this.delegate.paintBorder(c, g, x, y, width, height);
/*     */     }
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c) {
/* 218 */       return this.delegate.getBorderInsets(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBorderOpaque() {
/* 223 */       return this.delegate.isBorderOpaque();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatOptionPaneUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */