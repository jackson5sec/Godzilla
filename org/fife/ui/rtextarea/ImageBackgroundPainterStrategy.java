/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.net.URL;
/*     */ import javax.imageio.ImageIO;
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
/*     */ public abstract class ImageBackgroundPainterStrategy
/*     */   implements BackgroundPainterStrategy
/*     */ {
/*     */   protected MediaTracker tracker;
/*     */   private RTextAreaBase textArea;
/*     */   private Image master;
/*     */   private int oldWidth;
/*     */   private int oldHeight;
/*     */   private int scalingHint;
/*     */   
/*     */   public ImageBackgroundPainterStrategy(RTextAreaBase textArea) {
/*  55 */     this.textArea = textArea;
/*  56 */     this.tracker = new MediaTracker(textArea);
/*  57 */     this.scalingHint = 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RTextAreaBase getRTextAreaBase() {
/*  67 */     return this.textArea;
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
/*     */   public Image getMasterImage() {
/*  79 */     return this.master;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScalingHint() {
/*  90 */     return this.scalingHint;
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
/*     */   public final void paint(Graphics g, Rectangle bounds) {
/* 103 */     if (bounds.width != this.oldWidth || bounds.height != this.oldHeight) {
/* 104 */       rescaleImage(bounds.width, bounds.height, getScalingHint());
/* 105 */       this.oldWidth = bounds.width;
/* 106 */       this.oldHeight = bounds.height;
/*     */     } 
/* 108 */     paintImage(g, bounds.x, bounds.y);
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
/*     */   protected abstract void paintImage(Graphics paramGraphics, int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void rescaleImage(int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImage(URL imageURL) {
/* 141 */     BufferedImage image = null;
/*     */     try {
/* 143 */       image = ImageIO.read(imageURL);
/* 144 */     } catch (Exception e) {
/* 145 */       e.printStackTrace();
/*     */     } 
/* 147 */     setImage(image);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImage(Image image) {
/* 156 */     this.master = image;
/* 157 */     this.oldWidth = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScalingHint(int hint) {
/* 168 */     this.scalingHint = hint;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\ImageBackgroundPainterStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */