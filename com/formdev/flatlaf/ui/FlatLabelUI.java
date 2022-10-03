/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicHTML;
/*     */ import javax.swing.plaf.basic.BasicLabelUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatLabelUI
/*     */   extends BasicLabelUI
/*     */ {
/*     */   private Color disabledForeground;
/*     */   private boolean defaults_initialized = false;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  60 */     return FlatUIUtils.createSharedUI(FlatLabelUI.class, FlatLabelUI::new);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults(JLabel c) {
/*  65 */     super.installDefaults(c);
/*     */     
/*  67 */     if (!this.defaults_initialized) {
/*  68 */       this.disabledForeground = UIManager.getColor("Label.disabledForeground");
/*     */       
/*  70 */       this.defaults_initialized = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults(JLabel c) {
/*  76 */     super.uninstallDefaults(c);
/*  77 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installComponents(JLabel c) {
/*  82 */     super.installComponents(c);
/*     */ 
/*     */     
/*  85 */     updateHTMLRenderer(c, c.getText(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void propertyChange(PropertyChangeEvent e) {
/*  90 */     String name = e.getPropertyName();
/*  91 */     if (name == "text" || name == "font" || name == "foreground") {
/*  92 */       JLabel label = (JLabel)e.getSource();
/*  93 */       updateHTMLRenderer(label, label.getText(), true);
/*     */     } else {
/*  95 */       super.propertyChange(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void updateHTMLRenderer(JComponent c, String text, boolean always) {
/* 103 */     if (BasicHTML.isHTMLString(text) && c
/* 104 */       .getClientProperty("html.disable") != Boolean.TRUE && text
/* 105 */       .contains("<h") && (text
/* 106 */       .contains("<h1") || text.contains("<h2") || text.contains("<h3") || text
/* 107 */       .contains("<h4") || text.contains("<h5") || text.contains("<h6"))) {
/*     */       
/* 109 */       int headIndex = text.indexOf("<head>");
/*     */       
/* 111 */       String style = "<style>BASE_SIZE " + c.getFont().getSize() + "</style>";
/* 112 */       if (headIndex < 0) {
/* 113 */         style = "<head>" + style + "</head>";
/*     */       }
/* 115 */       int insertIndex = (headIndex >= 0) ? (headIndex + "<head>".length()) : "<html>".length();
/*     */ 
/*     */       
/* 118 */       text = text.substring(0, insertIndex) + style + text.substring(insertIndex);
/* 119 */     } else if (!always) {
/*     */       return;
/*     */     } 
/* 122 */     BasicHTML.updateRenderer(c, text);
/*     */   }
/*     */   
/*     */   static Graphics createGraphicsHTMLTextYCorrection(Graphics g, JComponent c) {
/* 126 */     return (c.getClientProperty("html") != null) ? 
/* 127 */       HiDPIUtils.createGraphicsTextYCorrection((Graphics2D)g) : g;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/* 133 */     super.paint(createGraphicsHTMLTextYCorrection(g, c), c);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
/* 138 */     int mnemIndex = FlatLaf.isShowMnemonics() ? l.getDisplayedMnemonicIndex() : -1;
/* 139 */     g.setColor(l.getForeground());
/* 140 */     FlatUIUtils.drawStringUnderlineCharAt(l, g, s, mnemIndex, textX, textY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY) {
/* 145 */     int mnemIndex = FlatLaf.isShowMnemonics() ? l.getDisplayedMnemonicIndex() : -1;
/* 146 */     g.setColor(this.disabledForeground);
/* 147 */     FlatUIUtils.drawStringUnderlineCharAt(l, g, s, mnemIndex, textX, textY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String layoutCL(JLabel label, FontMetrics fontMetrics, String text, Icon icon, Rectangle viewR, Rectangle iconR, Rectangle textR) {
/* 157 */     return SwingUtilities.layoutCompoundLabel(label, fontMetrics, text, icon, label
/* 158 */         .getVerticalAlignment(), label.getHorizontalAlignment(), label
/* 159 */         .getVerticalTextPosition(), label.getHorizontalTextPosition(), viewR, iconR, textR, 
/*     */         
/* 161 */         UIScale.scale(label.getIconTextGap()));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatLabelUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */