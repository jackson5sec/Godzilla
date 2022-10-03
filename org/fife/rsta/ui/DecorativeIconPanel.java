/*     */ package org.fife.rsta.ui;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.ToolTipManager;
/*     */ import org.fife.ui.autocomplete.EmptyIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DecorativeIconPanel
/*     */   extends JPanel
/*     */ {
/*     */   private static final int DEFAULT_WIDTH = 8;
/*     */   private JLabel iconLabel;
/*     */   private boolean showIcon;
/*     */   private String tip;
/*     */   private EmptyIcon emptyIcon;
/*     */   
/*     */   public DecorativeIconPanel() {
/*  51 */     this(8);
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
/*     */   public DecorativeIconPanel(int iconWidth) {
/*  64 */     setLayout(new BorderLayout());
/*  65 */     this.emptyIcon = new EmptyIcon(iconWidth);
/*     */     
/*  67 */     this.iconLabel = new JLabel((Icon)this.emptyIcon)
/*     */       {
/*     */         public String getToolTipText(MouseEvent e) {
/*  70 */           return DecorativeIconPanel.this.showIcon ? DecorativeIconPanel.this.tip : null;
/*     */         }
/*     */       };
/*  73 */     this.iconLabel.setVerticalAlignment(1);
/*  74 */     ToolTipManager.sharedInstance().registerComponent(this.iconLabel);
/*  75 */     add(this.iconLabel, "North");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Icon getIcon() {
/*  86 */     return this.iconLabel.getIcon();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShowIcon() {
/*  97 */     return this.showIcon;
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
/*     */   public String getToolTipText() {
/* 110 */     return this.tip;
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
/*     */   protected void paintChildren(Graphics g) {
/* 123 */     if (this.showIcon) {
/* 124 */       super.paintChildren(g);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIcon(Icon icon) {
/*     */     EmptyIcon emptyIcon;
/* 136 */     if (icon == null) {
/* 137 */       emptyIcon = this.emptyIcon;
/*     */     }
/* 139 */     this.iconLabel.setIcon((Icon)emptyIcon);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShowIcon(boolean show) {
/* 150 */     if (show != this.showIcon) {
/* 151 */       this.showIcon = show;
/* 152 */       repaint();
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
/*     */   public void setToolTipText(String tip) {
/* 166 */     this.tip = tip;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\DecorativeIconPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */