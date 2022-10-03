/*     */ package com.formdev.flatlaf.icons;
/*     */ 
/*     */ import com.formdev.flatlaf.ui.FlatButtonUI;
/*     */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.Ellipse2D;
/*     */ import java.awt.geom.Path2D;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatHelpButtonIcon
/*     */   extends FlatAbstractIcon
/*     */ {
/*  51 */   protected final int focusWidth = UIManager.getInt("Component.focusWidth");
/*  52 */   protected final Color focusColor = UIManager.getColor("Component.focusColor");
/*     */   
/*  54 */   protected final Color borderColor = UIManager.getColor("HelpButton.borderColor");
/*  55 */   protected final Color disabledBorderColor = UIManager.getColor("HelpButton.disabledBorderColor");
/*  56 */   protected final Color focusedBorderColor = UIManager.getColor("HelpButton.focusedBorderColor");
/*  57 */   protected final Color hoverBorderColor = UIManager.getColor("HelpButton.hoverBorderColor");
/*  58 */   protected final Color background = UIManager.getColor("HelpButton.background");
/*  59 */   protected final Color disabledBackground = UIManager.getColor("HelpButton.disabledBackground");
/*  60 */   protected final Color focusedBackground = UIManager.getColor("HelpButton.focusedBackground");
/*  61 */   protected final Color hoverBackground = UIManager.getColor("HelpButton.hoverBackground");
/*  62 */   protected final Color pressedBackground = UIManager.getColor("HelpButton.pressedBackground");
/*  63 */   protected final Color questionMarkColor = UIManager.getColor("HelpButton.questionMarkColor");
/*  64 */   protected final Color disabledQuestionMarkColor = UIManager.getColor("HelpButton.disabledQuestionMarkColor");
/*     */   
/*  66 */   protected final int iconSize = 22 + this.focusWidth * 2;
/*     */   
/*     */   public FlatHelpButtonIcon() {
/*  69 */     super(0, 0, null);
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
/*     */   protected void paintIcon(Component c, Graphics2D g2) {
/*  84 */     boolean enabled = c.isEnabled();
/*  85 */     boolean focused = FlatUIUtils.isPermanentFocusOwner(c);
/*     */ 
/*     */     
/*  88 */     if (focused && FlatButtonUI.isFocusPainted(c)) {
/*  89 */       g2.setColor(this.focusColor);
/*  90 */       g2.fill(new Ellipse2D.Float(0.5F, 0.5F, (this.iconSize - 1), (this.iconSize - 1)));
/*     */     } 
/*     */ 
/*     */     
/*  94 */     g2.setColor(FlatButtonUI.buttonStateColor(c, this.borderColor, this.disabledBorderColor, this.focusedBorderColor, this.hoverBorderColor, null));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     g2.fill(new Ellipse2D.Float(this.focusWidth + 0.5F, this.focusWidth + 0.5F, 21.0F, 21.0F));
/*     */ 
/*     */     
/* 103 */     g2.setColor(FlatUIUtils.deriveColor(FlatButtonUI.buttonStateColor(c, this.background, this.disabledBackground, this.focusedBackground, this.hoverBackground, this.pressedBackground), this.background));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     g2.fill(new Ellipse2D.Float(this.focusWidth + 1.5F, this.focusWidth + 1.5F, 19.0F, 19.0F));
/*     */ 
/*     */     
/* 112 */     Path2D q = new Path2D.Float();
/* 113 */     q.moveTo(11.0D, 5.0D);
/* 114 */     q.curveTo(8.8D, 5.0D, 7.0D, 6.8D, 7.0D, 9.0D);
/* 115 */     q.lineTo(9.0D, 9.0D);
/* 116 */     q.curveTo(9.0D, 7.9D, 9.9D, 7.0D, 11.0D, 7.0D);
/* 117 */     q.curveTo(12.1D, 7.0D, 13.0D, 7.9D, 13.0D, 9.0D);
/* 118 */     q.curveTo(13.0D, 11.0D, 10.0D, 10.75D, 10.0D, 14.0D);
/* 119 */     q.lineTo(12.0D, 14.0D);
/* 120 */     q.curveTo(12.0D, 11.75D, 15.0D, 11.5D, 15.0D, 9.0D);
/* 121 */     q.curveTo(15.0D, 6.8D, 13.2D, 5.0D, 11.0D, 5.0D);
/* 122 */     q.closePath();
/*     */     
/* 124 */     g2.translate(this.focusWidth, this.focusWidth);
/* 125 */     g2.setColor(enabled ? this.questionMarkColor : this.disabledQuestionMarkColor);
/* 126 */     g2.fill(q);
/* 127 */     g2.fillRect(10, 15, 2, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIconWidth() {
/* 132 */     return UIScale.scale(this.iconSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIconHeight() {
/* 137 */     return UIScale.scale(this.iconSize);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatHelpButtonIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */