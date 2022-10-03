/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JToolTip;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicDesktopIconUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatDesktopIconUI
/*     */   extends BasicDesktopIconUI
/*     */ {
/*     */   private Dimension iconSize;
/*     */   private Dimension closeSize;
/*     */   private JLabel dockIcon;
/*     */   private JButton closeButton;
/*     */   private JToolTip titleTip;
/*     */   private ActionListener closeListener;
/*     */   private MouseInputListener mouseInputListener;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  80 */     return new FlatDesktopIconUI();
/*     */   }
/*     */ 
/*     */   
/*     */   public void uninstallUI(JComponent c) {
/*  85 */     super.uninstallUI(c);
/*     */     
/*  87 */     this.dockIcon = null;
/*  88 */     this.closeButton = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installComponents() {
/*  93 */     this.dockIcon = new JLabel();
/*  94 */     this.dockIcon.setHorizontalAlignment(0);
/*     */     
/*  96 */     this.closeButton = new JButton();
/*  97 */     this.closeButton.setIcon(UIManager.getIcon("DesktopIcon.closeIcon"));
/*  98 */     this.closeButton.setFocusable(false);
/*  99 */     this.closeButton.setBorder(BorderFactory.createEmptyBorder());
/* 100 */     this.closeButton.setOpaque(true);
/* 101 */     this.closeButton.setBackground(FlatUIUtils.nonUIResource(this.desktopIcon.getBackground()));
/* 102 */     this.closeButton.setForeground(FlatUIUtils.nonUIResource(this.desktopIcon.getForeground()));
/* 103 */     this.closeButton.setVisible(false);
/*     */     
/* 105 */     this.desktopIcon.setLayout(new FlatDesktopIconLayout());
/* 106 */     this.desktopIcon.add(this.closeButton);
/* 107 */     this.desktopIcon.add(this.dockIcon);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallComponents() {
/* 112 */     hideTitleTip();
/*     */     
/* 114 */     this.desktopIcon.remove(this.dockIcon);
/* 115 */     this.desktopIcon.remove(this.closeButton);
/* 116 */     this.desktopIcon.setLayout((LayoutManager)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/* 121 */     super.installDefaults();
/*     */     
/* 123 */     LookAndFeel.installColors(this.desktopIcon, "DesktopIcon.background", "DesktopIcon.foreground");
/*     */     
/* 125 */     this.iconSize = UIManager.getDimension("DesktopIcon.iconSize");
/* 126 */     this.closeSize = UIManager.getDimension("DesktopIcon.closeSize");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installListeners() {
/* 131 */     super.installListeners();
/*     */     
/* 133 */     this.closeListener = (e -> {
/*     */         if (this.frame.isClosable())
/*     */           this.frame.doDefaultCloseAction(); 
/*     */       });
/* 137 */     this.closeButton.addActionListener(this.closeListener);
/* 138 */     this.closeButton.addMouseListener(this.mouseInputListener);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallListeners() {
/* 143 */     super.uninstallListeners();
/*     */     
/* 145 */     this.closeButton.removeActionListener(this.closeListener);
/* 146 */     this.closeButton.removeMouseListener(this.mouseInputListener);
/* 147 */     this.closeListener = null;
/* 148 */     this.mouseInputListener = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MouseInputListener createMouseInputListener() {
/* 153 */     this.mouseInputListener = new MouseInputAdapter()
/*     */       {
/*     */         public void mouseReleased(MouseEvent e) {
/* 156 */           if (FlatDesktopIconUI.this.frame.isIcon() && FlatDesktopIconUI.this.desktopIcon.contains(e.getX(), e.getY())) {
/* 157 */             FlatDesktopIconUI.this.hideTitleTip();
/* 158 */             FlatDesktopIconUI.this.closeButton.setVisible(false);
/*     */             
/*     */             try {
/* 161 */               FlatDesktopIconUI.this.frame.setIcon(false);
/* 162 */             } catch (PropertyVetoException propertyVetoException) {}
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void mouseEntered(MouseEvent e) {
/* 170 */           FlatDesktopIconUI.this.showTitleTip();
/* 171 */           if (FlatDesktopIconUI.this.frame.isClosable()) {
/* 172 */             FlatDesktopIconUI.this.closeButton.setVisible(true);
/*     */           }
/*     */         }
/*     */         
/*     */         public void mouseExited(MouseEvent e) {
/* 177 */           FlatDesktopIconUI.this.hideTitleTip();
/* 178 */           FlatDesktopIconUI.this.closeButton.setVisible(false);
/*     */         }
/*     */       };
/* 181 */     return this.mouseInputListener;
/*     */   }
/*     */   
/*     */   private void showTitleTip() {
/* 185 */     JRootPane rootPane = SwingUtilities.getRootPane(this.desktopIcon);
/* 186 */     if (rootPane == null) {
/*     */       return;
/*     */     }
/* 189 */     if (this.titleTip == null) {
/* 190 */       this.titleTip = new JToolTip();
/* 191 */       rootPane.getLayeredPane().add(this.titleTip, JLayeredPane.POPUP_LAYER);
/*     */     } 
/* 193 */     this.titleTip.setTipText(this.frame.getTitle());
/* 194 */     this.titleTip.setSize(this.titleTip.getPreferredSize());
/*     */     
/* 196 */     int tx = (this.desktopIcon.getWidth() - this.titleTip.getWidth()) / 2;
/* 197 */     int ty = -(this.titleTip.getHeight() + UIScale.scale(4));
/* 198 */     Point pt = SwingUtilities.convertPoint(this.desktopIcon, tx, ty, this.titleTip.getParent());
/* 199 */     if (pt.x + this.titleTip.getWidth() > rootPane.getWidth())
/* 200 */       pt.x = rootPane.getWidth() - this.titleTip.getWidth(); 
/* 201 */     if (pt.x < 0)
/* 202 */       pt.x = 0; 
/* 203 */     this.titleTip.setLocation(pt);
/* 204 */     this.titleTip.repaint();
/*     */   }
/*     */   
/*     */   private void hideTitleTip() {
/* 208 */     if (this.titleTip == null) {
/*     */       return;
/*     */     }
/* 211 */     this.titleTip.setVisible(false);
/* 212 */     this.titleTip.getParent().remove(this.titleTip);
/* 213 */     this.titleTip = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 218 */     return UIScale.scale(this.iconSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 223 */     return getPreferredSize(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMaximumSize(JComponent c) {
/* 228 */     return getPreferredSize(c);
/*     */   }
/*     */ 
/*     */   
/*     */   void updateDockIcon() {
/* 233 */     EventQueue.invokeLater(() -> {
/*     */           if (this.dockIcon != null) {
/*     */             updateDockIconLater();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void updateDockIconLater() {
/* 241 */     if (this.frame.isSelected()) {
/*     */       try {
/* 243 */         this.frame.setSelected(false);
/* 244 */       } catch (PropertyVetoException propertyVetoException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 250 */     int frameWidth = Math.max(this.frame.getWidth(), 1);
/* 251 */     int frameHeight = Math.max(this.frame.getHeight(), 1);
/* 252 */     BufferedImage frameImage = new BufferedImage(frameWidth, frameHeight, 2);
/* 253 */     Graphics2D g = frameImage.createGraphics();
/*     */     
/*     */     try {
/* 256 */       this.frame.paint(g);
/*     */     } finally {
/* 258 */       g.dispose();
/*     */     } 
/*     */ 
/*     */     
/* 262 */     Insets insets = this.desktopIcon.getInsets();
/* 263 */     int previewWidth = UIScale.scale(this.iconSize.width) - insets.left - insets.right;
/* 264 */     int previewHeight = UIScale.scale(this.iconSize.height) - insets.top - insets.bottom;
/* 265 */     float frameRatio = frameHeight / frameWidth;
/* 266 */     if (previewWidth / frameWidth > previewHeight / frameHeight) {
/* 267 */       previewWidth = Math.round(previewHeight / frameRatio);
/*     */     } else {
/* 269 */       previewHeight = Math.round(previewWidth * frameRatio);
/*     */     } 
/*     */     
/* 272 */     Image previewImage = frameImage.getScaledInstance(previewWidth, previewHeight, 4);
/* 273 */     this.dockIcon.setIcon(new ImageIcon(previewImage));
/*     */   }
/*     */   
/*     */   private class FlatDesktopIconLayout
/*     */     implements LayoutManager
/*     */   {
/*     */     private FlatDesktopIconLayout() {}
/*     */     
/*     */     public void addLayoutComponent(String name, Component comp) {}
/*     */     
/*     */     public void removeLayoutComponent(Component comp) {}
/*     */     
/*     */     public Dimension preferredLayoutSize(Container parent) {
/* 286 */       return FlatDesktopIconUI.this.dockIcon.getPreferredSize();
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension minimumLayoutSize(Container parent) {
/* 291 */       return FlatDesktopIconUI.this.dockIcon.getMinimumSize();
/*     */     }
/*     */ 
/*     */     
/*     */     public void layoutContainer(Container parent) {
/* 296 */       Insets insets = parent.getInsets();
/*     */ 
/*     */       
/* 299 */       FlatDesktopIconUI.this.dockIcon.setBounds(insets.left, insets.top, parent
/* 300 */           .getWidth() - insets.left - insets.right, parent
/* 301 */           .getHeight() - insets.top - insets.bottom);
/*     */ 
/*     */       
/* 304 */       Dimension cSize = UIScale.scale(FlatDesktopIconUI.this.closeSize);
/* 305 */       FlatDesktopIconUI.this.closeButton.setBounds(parent.getWidth() - cSize.width, 0, cSize.width, cSize.height);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatDesktopIconUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */