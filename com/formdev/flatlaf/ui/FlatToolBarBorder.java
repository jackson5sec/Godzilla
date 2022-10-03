/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JToolBar;
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
/*     */ public class FlatToolBarBorder
/*     */   extends FlatMarginBorder
/*     */ {
/*     */   private static final int DOT_COUNT = 4;
/*     */   private static final int DOT_SIZE = 2;
/*     */   private static final int GRIP_SIZE = 6;
/*  45 */   protected final Color gripColor = UIManager.getColor("ToolBar.gripColor");
/*     */   
/*     */   public FlatToolBarBorder() {
/*  48 */     super(UIManager.getInsets("ToolBar.borderMargins"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*  54 */     if (c instanceof JToolBar && ((JToolBar)c).isFloatable()) {
/*  55 */       Graphics2D g2 = (Graphics2D)g.create();
/*     */       try {
/*  57 */         FlatUIUtils.setRenderingHints(g2);
/*     */         
/*  59 */         g2.setColor(this.gripColor);
/*  60 */         paintGrip(c, g2, x, y, width, height);
/*     */       } finally {
/*  62 */         g2.dispose();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void paintGrip(Component c, Graphics g, int x, int y, int width, int height) {
/*  68 */     Rectangle r = calculateGripBounds(c, x, y, width, height);
/*  69 */     FlatUIUtils.paintGrip(g, r.x, r.y, r.width, r.height, 
/*  70 */         (((JToolBar)c).getOrientation() == 1), 4, 2, 2, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Rectangle calculateGripBounds(Component c, int x, int y, int width, int height) {
/*  76 */     Insets insets = super.getBorderInsets(c, new Insets(0, 0, 0, 0));
/*  77 */     Rectangle r = FlatUIUtils.subtractInsets(new Rectangle(x, y, width, height), insets);
/*     */ 
/*     */     
/*  80 */     int gripSize = UIScale.scale(6);
/*  81 */     if (((JToolBar)c).getOrientation() == 0) {
/*  82 */       if (!c.getComponentOrientation().isLeftToRight())
/*  83 */         r.x = r.x + r.width - gripSize; 
/*  84 */       r.width = gripSize;
/*     */     } else {
/*  86 */       r.height = gripSize;
/*     */     } 
/*  88 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public Insets getBorderInsets(Component c, Insets insets) {
/*  93 */     insets = super.getBorderInsets(c, insets);
/*     */ 
/*     */     
/*  96 */     if (c instanceof JToolBar && ((JToolBar)c).isFloatable()) {
/*  97 */       int gripInset = UIScale.scale(6);
/*  98 */       if (((JToolBar)c).getOrientation() == 0)
/*  99 */       { if (c.getComponentOrientation().isLeftToRight()) {
/* 100 */           insets.left += gripInset;
/*     */         } else {
/* 102 */           insets.right += gripInset;
/*     */         }  }
/* 104 */       else { insets.top += gripInset; }
/*     */     
/*     */     } 
/* 107 */     return insets;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatToolBarBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */