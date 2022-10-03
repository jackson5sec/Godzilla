/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class BufferedImageBackgroundPainterStrategy
/*     */   extends ImageBackgroundPainterStrategy
/*     */ {
/*     */   private BufferedImage bgImage;
/*     */   
/*     */   public BufferedImageBackgroundPainterStrategy(RTextAreaBase ta) {
/*  57 */     super(ta);
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
/*     */   protected void paintImage(Graphics g, int x, int y) {
/*  72 */     if (this.bgImage != null) {
/*  73 */       g.drawImage(this.bgImage, x, y, null);
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
/*     */   
/*     */   protected void rescaleImage(int width, int height, int hint) {
/*  88 */     Image master = getMasterImage();
/*  89 */     if (master != null) {
/*     */       
/*  91 */       Map<RenderingHints.Key, Object> hints = new HashMap<>();
/*     */       
/*  93 */       switch (hint) {
/*     */       
/*     */       } 
/*     */       
/*  97 */       hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
/*     */       
/*  99 */       hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*     */       
/* 101 */       hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */ 
/*     */ 
/*     */       
/* 105 */       this.bgImage = createAcceleratedImage(width, height);
/* 106 */       Graphics2D g = this.bgImage.createGraphics();
/* 107 */       g.addRenderingHints(hints);
/* 108 */       g.drawImage(master, 0, 0, width, height, null);
/* 109 */       g.dispose();
/*     */     }
/*     */     else {
/*     */       
/* 113 */       this.bgImage = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private BufferedImage createAcceleratedImage(int width, int height) {
/* 119 */     GraphicsConfiguration gc = getRTextAreaBase().getGraphicsConfiguration();
/* 120 */     BufferedImage image = gc.createCompatibleImage(width, height);
/* 121 */     return image;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\BufferedImageBackgroundPainterStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */