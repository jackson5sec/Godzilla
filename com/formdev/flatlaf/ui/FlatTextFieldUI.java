/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.JavaCompatibility;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTextFieldUI;
/*     */ import javax.swing.text.Caret;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatTextFieldUI
/*     */   extends BasicTextFieldUI
/*     */ {
/*     */   protected int minimumWidth;
/*     */   protected boolean isIntelliJTheme;
/*     */   protected Color placeholderForeground;
/*     */   private FocusListener focusListener;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  82 */     return new FlatTextFieldUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  87 */     super.installDefaults();
/*     */     
/*  89 */     String prefix = getPropertyPrefix();
/*  90 */     this.minimumWidth = UIManager.getInt("Component.minimumWidth");
/*  91 */     this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
/*  92 */     this.placeholderForeground = UIManager.getColor(prefix + ".placeholderForeground");
/*     */     
/*  94 */     LookAndFeel.installProperty(getComponent(), "opaque", Boolean.valueOf(false));
/*     */     
/*  96 */     MigLayoutVisualPadding.install(getComponent());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/* 101 */     super.uninstallDefaults();
/*     */     
/* 103 */     this.placeholderForeground = null;
/*     */     
/* 105 */     MigLayoutVisualPadding.uninstall(getComponent());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installListeners() {
/* 110 */     super.installListeners();
/*     */     
/* 112 */     this.focusListener = new FlatUIUtils.RepaintFocusListener(getComponent());
/* 113 */     getComponent().addFocusListener(this.focusListener);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallListeners() {
/* 118 */     super.uninstallListeners();
/*     */     
/* 120 */     getComponent().removeFocusListener(this.focusListener);
/* 121 */     this.focusListener = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Caret createCaret() {
/* 126 */     return new FlatCaret(UIManager.getString("TextComponent.selectAllOnFocusPolicy"), 
/* 127 */         UIManager.getBoolean("TextComponent.selectAllOnMouseClick"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propertyChange(PropertyChangeEvent e) {
/* 132 */     super.propertyChange(e);
/* 133 */     propertyChange(getComponent(), e);
/*     */   }
/*     */   
/*     */   static void propertyChange(JTextComponent c, PropertyChangeEvent e) {
/* 137 */     switch (e.getPropertyName()) {
/*     */       case "JTextField.placeholderText":
/*     */       case "JComponent.roundRect":
/* 140 */         c.repaint();
/*     */         break;
/*     */       
/*     */       case "JComponent.minimumWidth":
/* 144 */         c.revalidate();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintSafely(Graphics g) {
/* 151 */     paintBackground(g, getComponent(), this.isIntelliJTheme);
/* 152 */     paintPlaceholder(g, getComponent(), this.placeholderForeground);
/*     */     
/* 154 */     super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D)g));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintBackground(Graphics g) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void paintBackground(Graphics g, JTextComponent c, boolean isIntelliJTheme) {
/* 168 */     if (!c.isOpaque() && FlatUIUtils.getOutsideFlatBorder(c) == null && FlatUIUtils.hasOpaqueBeenExplicitlySet(c)) {
/*     */       return;
/*     */     }
/* 171 */     float focusWidth = FlatUIUtils.getBorderFocusWidth(c);
/* 172 */     float arc = FlatUIUtils.getBorderArc(c);
/*     */ 
/*     */     
/* 175 */     if (c.isOpaque() && (focusWidth > 0.0F || arc > 0.0F)) {
/* 176 */       FlatUIUtils.paintParentBackground(g, c);
/*     */     }
/*     */     
/* 179 */     Graphics2D g2 = (Graphics2D)g.create();
/*     */     try {
/* 181 */       FlatUIUtils.setRenderingHints(g2);
/*     */       
/* 183 */       Color background = c.getBackground();
/* 184 */       g2.setColor(!(background instanceof javax.swing.plaf.UIResource) ? background : ((isIntelliJTheme && (
/*     */           
/* 186 */           !c.isEnabled() || !c.isEditable())) ? 
/* 187 */           FlatUIUtils.getParentBackground(c) : background));
/*     */       
/* 189 */       FlatUIUtils.paintComponentBackground(g2, 0, 0, c.getWidth(), c.getHeight(), focusWidth, arc);
/*     */     } finally {
/* 191 */       g2.dispose();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void paintPlaceholder(Graphics g, JTextComponent c, Color placeholderForeground) {
/* 197 */     if (c.getDocument().getLength() > 0) {
/*     */       return;
/*     */     }
/*     */     
/* 201 */     Container parent = c.getParent();
/* 202 */     JComponent jc = (parent instanceof JComboBox) ? (JComboBox)parent : c;
/*     */ 
/*     */     
/* 205 */     Object placeholder = jc.getClientProperty("JTextField.placeholderText");
/* 206 */     if (!(placeholder instanceof String)) {
/*     */       return;
/*     */     }
/*     */     
/* 210 */     Insets insets = c.getInsets();
/* 211 */     FontMetrics fm = c.getFontMetrics(c.getFont());
/* 212 */     int x = insets.left;
/* 213 */     int y = insets.top + fm.getAscent() + (c.getHeight() - insets.top - insets.bottom - fm.getHeight()) / 2;
/*     */ 
/*     */     
/* 216 */     g.setColor(placeholderForeground);
/* 217 */     String clippedPlaceholder = JavaCompatibility.getClippedString(jc, fm, (String)placeholder, c
/* 218 */         .getWidth() - insets.left - insets.right);
/* 219 */     FlatUIUtils.drawString(c, g, clippedPlaceholder, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 224 */     return applyMinimumWidth(c, super.getPreferredSize(c), this.minimumWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 229 */     return applyMinimumWidth(c, super.getMinimumSize(c), this.minimumWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   static Dimension applyMinimumWidth(JComponent c, Dimension size, int minimumWidth) {
/* 234 */     if (c instanceof JTextField && ((JTextField)c).getColumns() > 0) {
/* 235 */       return size;
/*     */     }
/*     */     
/* 238 */     Container parent = c.getParent();
/* 239 */     if (parent instanceof JComboBox || parent instanceof javax.swing.JSpinner || (parent != null && parent
/*     */       
/* 241 */       .getParent() instanceof javax.swing.JSpinner)) {
/* 242 */       return size;
/*     */     }
/* 244 */     minimumWidth = FlatUIUtils.minimumWidth(c, minimumWidth);
/* 245 */     float focusWidth = FlatUIUtils.getBorderFocusWidth(c);
/* 246 */     size.width = Math.max(size.width, UIScale.scale(minimumWidth) + Math.round(focusWidth * 2.0F));
/* 247 */     return size;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTextFieldUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */