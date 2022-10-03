/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.ScaledImageIcon;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.LayoutManager;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatInternalFrameTitlePane
/*     */   extends BasicInternalFrameTitlePane
/*     */ {
/*     */   private JLabel titleLabel;
/*     */   private JPanel buttonPanel;
/*     */   
/*     */   public FlatInternalFrameTitlePane(JInternalFrame f) {
/*  51 */     super(f);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/*  56 */     super.installDefaults();
/*     */     
/*  58 */     LookAndFeel.installBorder(this, "InternalFrameTitlePane.border");
/*     */   }
/*     */ 
/*     */   
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/*  63 */     return new FlatPropertyChangeHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   protected LayoutManager createLayout() {
/*  68 */     return new BorderLayout(UIScale.scale(4), 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createButtons() {
/*  73 */     super.createButtons();
/*     */     
/*  75 */     this.iconButton.setContentAreaFilled(false);
/*  76 */     this.maxButton.setContentAreaFilled(false);
/*  77 */     this.closeButton.setContentAreaFilled(false);
/*     */     
/*  79 */     Border emptyBorder = BorderFactory.createEmptyBorder();
/*  80 */     this.iconButton.setBorder(emptyBorder);
/*  81 */     this.maxButton.setBorder(emptyBorder);
/*  82 */     this.closeButton.setBorder(emptyBorder);
/*     */     
/*  84 */     updateButtonsVisibility();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addSubComponents() {
/*  89 */     this.titleLabel = new JLabel(this.frame.getTitle());
/*  90 */     this.titleLabel.setFont(FlatUIUtils.nonUIResource(getFont()));
/*  91 */     this.titleLabel.setMinimumSize(new Dimension(UIScale.scale(32), 1));
/*  92 */     updateFrameIcon();
/*  93 */     updateColors();
/*     */     
/*  95 */     this.buttonPanel = new JPanel()
/*     */       {
/*     */         public Dimension getPreferredSize() {
/*  98 */           Dimension size = super.getPreferredSize();
/*  99 */           int height = size.height;
/*     */           
/* 101 */           if (!FlatInternalFrameTitlePane.this.iconButton.isVisible())
/* 102 */             height = Math.max(height, (FlatInternalFrameTitlePane.this.iconButton.getPreferredSize()).height); 
/* 103 */           if (!FlatInternalFrameTitlePane.this.maxButton.isVisible())
/* 104 */             height = Math.max(height, (FlatInternalFrameTitlePane.this.maxButton.getPreferredSize()).height); 
/* 105 */           if (!FlatInternalFrameTitlePane.this.closeButton.isVisible())
/* 106 */             height = Math.max(height, (FlatInternalFrameTitlePane.this.closeButton.getPreferredSize()).height); 
/* 107 */           return new Dimension(size.width, height);
/*     */         }
/*     */       };
/* 110 */     this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 2));
/* 111 */     this.buttonPanel.setOpaque(false);
/*     */     
/* 113 */     this.buttonPanel.add(this.iconButton);
/* 114 */     this.buttonPanel.add(this.maxButton);
/* 115 */     this.buttonPanel.add(this.closeButton);
/*     */     
/* 117 */     add(this.titleLabel, "Center");
/* 118 */     add(this.buttonPanel, "After");
/*     */   }
/*     */   protected void updateFrameIcon() {
/*     */     ScaledImageIcon scaledImageIcon;
/* 122 */     Icon frameIcon = this.frame.getFrameIcon();
/* 123 */     if (frameIcon != null && (frameIcon.getIconWidth() == 0 || frameIcon.getIconHeight() == 0)) {
/* 124 */       frameIcon = null;
/* 125 */     } else if (frameIcon instanceof ImageIcon) {
/* 126 */       scaledImageIcon = new ScaledImageIcon((ImageIcon)frameIcon);
/* 127 */     }  this.titleLabel.setIcon((Icon)scaledImageIcon);
/*     */   }
/*     */   
/*     */   protected void updateColors() {
/* 131 */     Color background = FlatUIUtils.nonUIResource(this.frame.isSelected() ? this.selectedTitleColor : this.notSelectedTitleColor);
/* 132 */     Color foreground = FlatUIUtils.nonUIResource(this.frame.isSelected() ? this.selectedTextColor : this.notSelectedTextColor);
/*     */     
/* 134 */     this.titleLabel.setForeground(foreground);
/* 135 */     this.iconButton.setBackground(background);
/* 136 */     this.iconButton.setForeground(foreground);
/* 137 */     this.maxButton.setBackground(background);
/* 138 */     this.maxButton.setForeground(foreground);
/* 139 */     this.closeButton.setBackground(background);
/* 140 */     this.closeButton.setForeground(foreground);
/*     */   }
/*     */   
/*     */   protected void updateButtonsVisibility() {
/* 144 */     this.iconButton.setVisible(this.frame.isIconifiable());
/* 145 */     this.maxButton.setVisible(this.frame.isMaximizable());
/* 146 */     this.closeButton.setVisible(this.frame.isClosable());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assembleSystemMenu() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void showSystemMenu() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintComponent(Graphics g) {
/* 165 */     paintTitleBackground(g);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class FlatPropertyChangeHandler
/*     */     extends BasicInternalFrameTitlePane.PropertyChangeHandler
/*     */   {
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 175 */       switch (e.getPropertyName()) {
/*     */         case "title":
/* 177 */           FlatInternalFrameTitlePane.this.titleLabel.setText(FlatInternalFrameTitlePane.this.frame.getTitle());
/*     */           break;
/*     */         
/*     */         case "frameIcon":
/* 181 */           FlatInternalFrameTitlePane.this.updateFrameIcon();
/*     */           break;
/*     */         
/*     */         case "selected":
/* 185 */           FlatInternalFrameTitlePane.this.updateColors();
/*     */           break;
/*     */         
/*     */         case "iconable":
/*     */         case "maximizable":
/*     */         case "closable":
/* 191 */           FlatInternalFrameTitlePane.this.updateButtonsVisibility();
/* 192 */           FlatInternalFrameTitlePane.this.enableActions();
/* 193 */           FlatInternalFrameTitlePane.this.revalidate();
/* 194 */           FlatInternalFrameTitlePane.this.repaint();
/*     */           return;
/*     */ 
/*     */ 
/*     */         
/*     */         case "componentOrientation":
/* 200 */           FlatInternalFrameTitlePane.this.applyComponentOrientation(FlatInternalFrameTitlePane.this.frame.getComponentOrientation());
/*     */           break;
/*     */       } 
/*     */       
/* 204 */       super.propertyChange(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatInternalFrameTitlePane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */