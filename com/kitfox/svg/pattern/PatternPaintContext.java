/*     */ package com.kitfox.svg.pattern;
/*     */ 
/*     */ import java.awt.PaintContext;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public class PatternPaintContext
/*     */   implements PaintContext
/*     */ {
/*     */   BufferedImage source;
/*     */   Rectangle deviceBounds;
/*     */   AffineTransform xform;
/*     */   int sourceWidth;
/*     */   int sourceHeight;
/*     */   BufferedImage buf;
/*     */   
/*     */   public PatternPaintContext(BufferedImage source, Rectangle deviceBounds, AffineTransform userXform, AffineTransform distortXform) {
/*  69 */     this.source = source;
/*  70 */     this.deviceBounds = deviceBounds;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  77 */       this.xform = distortXform.createInverse();
/*  78 */       this.xform.concatenate(userXform.createInverse());
/*     */     }
/*  80 */     catch (Exception e) {
/*     */       
/*  82 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */     } 
/*     */     
/*  85 */     this.sourceWidth = source.getWidth();
/*  86 */     this.sourceHeight = source.getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {}
/*     */   
/*     */   public ColorModel getColorModel() {
/*  93 */     return this.source.getColorModel();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Raster getRaster(int x, int y, int w, int h) {
/*  99 */     if (this.buf == null || this.buf.getWidth() != w || this.buf.getHeight() != h)
/*     */     {
/* 101 */       this.buf = new BufferedImage(w, h, this.source.getType());
/*     */     }
/*     */ 
/*     */     
/* 105 */     Point2D.Float srcPt = new Point2D.Float(), destPt = new Point2D.Float();
/* 106 */     for (int j = 0; j < h; j++) {
/*     */       
/* 108 */       for (int i = 0; i < w; i++) {
/*     */         
/* 110 */         destPt.setLocation((i + x), (j + y));
/*     */         
/* 112 */         this.xform.transform(destPt, srcPt);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 117 */         int ii = (int)srcPt.x % this.sourceWidth;
/* 118 */         if (ii < 0) ii += this.sourceWidth; 
/* 119 */         int jj = (int)srcPt.y % this.sourceHeight;
/* 120 */         if (jj < 0) jj += this.sourceHeight;
/*     */         
/* 122 */         this.buf.setRGB(i, j, this.source.getRGB(ii, jj));
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     return this.buf.getData();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] argv) {
/* 131 */     int i = -4;
/* 132 */     System.err.println("Hello " + (i % 4));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pattern\PatternPaintContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */