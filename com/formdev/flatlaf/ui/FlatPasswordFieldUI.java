/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicPasswordFieldUI;
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
/*     */ public class FlatPasswordFieldUI
/*     */   extends BasicPasswordFieldUI
/*     */ {
/*     */   protected int minimumWidth;
/*     */   protected boolean isIntelliJTheme;
/*     */   protected Color placeholderForeground;
/*     */   protected boolean showCapsLock;
/*     */   protected Icon capsLockIcon;
/*     */   private FocusListener focusListener;
/*     */   private KeyListener capsLockListener;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  83 */     return new FlatPasswordFieldUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  88 */     super.installDefaults();
/*     */     
/*  90 */     String prefix = getPropertyPrefix();
/*  91 */     this.minimumWidth = UIManager.getInt("Component.minimumWidth");
/*  92 */     this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
/*  93 */     this.placeholderForeground = UIManager.getColor(prefix + ".placeholderForeground");
/*  94 */     this.showCapsLock = UIManager.getBoolean("PasswordField.showCapsLock");
/*  95 */     this.capsLockIcon = UIManager.getIcon("PasswordField.capsLockIcon");
/*     */     
/*  97 */     LookAndFeel.installProperty(getComponent(), "opaque", Boolean.valueOf(false));
/*     */     
/*  99 */     MigLayoutVisualPadding.install(getComponent());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults() {
/* 104 */     super.uninstallDefaults();
/*     */     
/* 106 */     this.placeholderForeground = null;
/* 107 */     this.capsLockIcon = null;
/*     */     
/* 109 */     MigLayoutVisualPadding.uninstall(getComponent());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installListeners() {
/* 114 */     super.installListeners();
/*     */     
/* 116 */     this.focusListener = new FlatUIUtils.RepaintFocusListener(getComponent());
/* 117 */     this.capsLockListener = new KeyAdapter()
/*     */       {
/*     */         public void keyPressed(KeyEvent e) {
/* 120 */           repaint(e);
/*     */         }
/*     */         
/*     */         public void keyReleased(KeyEvent e) {
/* 124 */           repaint(e);
/*     */         }
/*     */         private void repaint(KeyEvent e) {
/* 127 */           if (e.getKeyCode() == 20) {
/* 128 */             e.getComponent().repaint();
/*     */           }
/*     */         }
/*     */       };
/* 132 */     getComponent().addFocusListener(this.focusListener);
/* 133 */     getComponent().addKeyListener(this.capsLockListener);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallListeners() {
/* 138 */     super.uninstallListeners();
/*     */     
/* 140 */     getComponent().removeFocusListener(this.focusListener);
/* 141 */     getComponent().removeKeyListener(this.capsLockListener);
/* 142 */     this.focusListener = null;
/* 143 */     this.capsLockListener = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Caret createCaret() {
/* 148 */     return new FlatCaret(UIManager.getString("TextComponent.selectAllOnFocusPolicy"), 
/* 149 */         UIManager.getBoolean("TextComponent.selectAllOnMouseClick"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void propertyChange(PropertyChangeEvent e) {
/* 154 */     super.propertyChange(e);
/* 155 */     FlatTextFieldUI.propertyChange(getComponent(), e);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintSafely(Graphics g) {
/* 160 */     FlatTextFieldUI.paintBackground(g, getComponent(), this.isIntelliJTheme);
/* 161 */     FlatTextFieldUI.paintPlaceholder(g, getComponent(), this.placeholderForeground);
/* 162 */     paintCapsLock(g);
/*     */     
/* 164 */     super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D)g));
/*     */   }
/*     */   
/*     */   protected void paintCapsLock(Graphics g) {
/* 168 */     if (!this.showCapsLock) {
/*     */       return;
/*     */     }
/* 171 */     JTextComponent c = getComponent();
/* 172 */     if (!FlatUIUtils.isPermanentFocusOwner(c) || 
/* 173 */       !Toolkit.getDefaultToolkit().getLockingKeyState(20)) {
/*     */       return;
/*     */     }
/* 176 */     int y = (c.getHeight() - this.capsLockIcon.getIconHeight()) / 2;
/* 177 */     int x = c.getWidth() - this.capsLockIcon.getIconWidth() - y;
/* 178 */     this.capsLockIcon.paintIcon(c, g, x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintBackground(Graphics g) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 188 */     return FlatTextFieldUI.applyMinimumWidth(c, super.getPreferredSize(c), this.minimumWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 193 */     return FlatTextFieldUI.applyMinimumWidth(c, super.getMinimumSize(c), this.minimumWidth);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatPasswordFieldUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */