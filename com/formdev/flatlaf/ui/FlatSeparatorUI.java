/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicSeparatorUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatSeparatorUI
/*     */   extends BasicSeparatorUI
/*     */ {
/*     */   protected int height;
/*     */   protected int stripeWidth;
/*     */   protected int stripeIndent;
/*     */   private boolean defaults_initialized = false;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  56 */     return FlatUIUtils.createSharedUI(FlatSeparatorUI.class, FlatSeparatorUI::new);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults(JSeparator s) {
/*  61 */     super.installDefaults(s);
/*     */     
/*  63 */     if (!this.defaults_initialized) {
/*  64 */       String prefix = getPropertyPrefix();
/*  65 */       this.height = UIManager.getInt(prefix + ".height");
/*  66 */       this.stripeWidth = UIManager.getInt(prefix + ".stripeWidth");
/*  67 */       this.stripeIndent = UIManager.getInt(prefix + ".stripeIndent");
/*     */       
/*  69 */       this.defaults_initialized = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults(JSeparator s) {
/*  75 */     super.uninstallDefaults(s);
/*  76 */     this.defaults_initialized = false;
/*     */   }
/*     */   
/*     */   protected String getPropertyPrefix() {
/*  80 */     return "Separator";
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/*  85 */     Graphics2D g2 = (Graphics2D)g.create();
/*     */     
/*  87 */     try { FlatUIUtils.setRenderingHints(g2);
/*  88 */       g2.setColor(c.getForeground());
/*     */       
/*  90 */       float width = UIScale.scale(this.stripeWidth);
/*  91 */       float indent = UIScale.scale(this.stripeIndent);
/*     */       
/*  93 */       if (((JSeparator)c).getOrientation() == 1) {
/*  94 */         g2.fill(new Rectangle2D.Float(indent, 0.0F, width, c.getHeight()));
/*     */       } else {
/*  96 */         g2.fill(new Rectangle2D.Float(0.0F, indent, c.getWidth(), width));
/*     */       }  }
/*  98 */     finally { g2.dispose(); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 104 */     if (((JSeparator)c).getOrientation() == 1) {
/* 105 */       return new Dimension(UIScale.scale(this.height), 0);
/*     */     }
/* 107 */     return new Dimension(0, UIScale.scale(this.height));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatSeparatorUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */