/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatButtonUI;
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import com.formdev.flatlaf.util.HiDPIUtils;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics2D;
/*    */ import javax.swing.UIManager;
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
/*    */ public abstract class FlatWindowAbstractIcon
/*    */   extends FlatAbstractIcon
/*    */ {
/*    */   private final Color hoverBackground;
/*    */   private final Color pressedBackground;
/*    */   
/*    */   public FlatWindowAbstractIcon() {
/* 44 */     this(UIManager.getDimension("TitlePane.buttonSize"), 
/* 45 */         UIManager.getColor("TitlePane.buttonHoverBackground"), 
/* 46 */         UIManager.getColor("TitlePane.buttonPressedBackground"));
/*    */   }
/*    */   
/*    */   public FlatWindowAbstractIcon(Dimension size, Color hoverBackground, Color pressedBackground) {
/* 50 */     super(size.width, size.height, null);
/* 51 */     this.hoverBackground = hoverBackground;
/* 52 */     this.pressedBackground = pressedBackground;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintIcon(Component c, Graphics2D g) {
/* 57 */     paintBackground(c, g);
/*    */     
/* 59 */     g.setColor(getForeground(c));
/* 60 */     HiDPIUtils.paintAtScale1x(g, 0, 0, this.width, this.height, this::paintIconAt1x);
/*    */   }
/*    */   
/*    */   protected abstract void paintIconAt1x(Graphics2D paramGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble);
/*    */   
/*    */   protected void paintBackground(Component c, Graphics2D g) {
/* 66 */     Color background = FlatButtonUI.buttonStateColor(c, null, null, null, this.hoverBackground, this.pressedBackground);
/* 67 */     if (background != null) {
/* 68 */       g.setColor(FlatUIUtils.deriveColor(background, c.getBackground()));
/* 69 */       g.fillRect(0, 0, this.width, this.height);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected Color getForeground(Component c) {
/* 74 */     return c.getForeground();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatWindowAbstractIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */