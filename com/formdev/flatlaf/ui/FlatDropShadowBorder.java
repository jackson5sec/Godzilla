/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.RadialGradientPaint;
/*     */ import java.awt.image.BufferedImage;
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
/*     */ public class FlatDropShadowBorder
/*     */   extends FlatEmptyBorder
/*     */ {
/*     */   private final Color shadowColor;
/*     */   private final Insets shadowInsets;
/*     */   private final float shadowOpacity;
/*     */   private final int shadowSize;
/*     */   private Image shadowImage;
/*     */   private Color lastShadowColor;
/*     */   private double lastSystemScaleFactor;
/*     */   private float lastUserScaleFactor;
/*     */   
/*     */   public FlatDropShadowBorder() {
/*  55 */     this(null);
/*     */   }
/*     */   
/*     */   public FlatDropShadowBorder(Color shadowColor) {
/*  59 */     this(shadowColor, 4, 0.5F);
/*     */   }
/*     */   
/*     */   public FlatDropShadowBorder(Color shadowColor, int shadowSize, float shadowOpacity) {
/*  63 */     this(shadowColor, new Insets(-shadowSize, -shadowSize, shadowSize, shadowSize), shadowOpacity);
/*     */   }
/*     */   
/*     */   public FlatDropShadowBorder(Color shadowColor, Insets shadowInsets, float shadowOpacity) {
/*  67 */     super(Math.max(shadowInsets.top, 0), Math.max(shadowInsets.left, 0), 
/*  68 */         Math.max(shadowInsets.bottom, 0), Math.max(shadowInsets.right, 0));
/*  69 */     this.shadowColor = shadowColor;
/*  70 */     this.shadowInsets = shadowInsets;
/*  71 */     this.shadowOpacity = shadowOpacity;
/*     */     
/*  73 */     this.shadowSize = Math.max(
/*  74 */         Math.max(shadowInsets.left, shadowInsets.right), 
/*  75 */         Math.max(shadowInsets.top, shadowInsets.bottom));
/*     */   }
/*     */ 
/*     */   
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*  80 */     if (this.shadowSize <= 0) {
/*     */       return;
/*     */     }
/*  83 */     HiDPIUtils.paintAtScale1x((Graphics2D)g, x, y, width, height, this::paintImpl);
/*     */   }
/*     */   
/*     */   private void paintImpl(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
/*  87 */     Color shadowColor = (this.shadowColor != null) ? this.shadowColor : g.getColor();
/*  88 */     int shadowSize = scale(this.shadowSize, scaleFactor);
/*     */ 
/*     */     
/*  91 */     float userScaleFactor = UIScale.getUserScaleFactor();
/*  92 */     if (this.shadowImage == null || 
/*  93 */       !shadowColor.equals(this.lastShadowColor) || this.lastSystemScaleFactor != scaleFactor || this.lastUserScaleFactor != userScaleFactor) {
/*     */ 
/*     */ 
/*     */       
/*  97 */       this.shadowImage = createShadowImage(shadowColor, shadowSize, this.shadowOpacity, (float)(scaleFactor * userScaleFactor));
/*     */       
/*  99 */       this.lastShadowColor = shadowColor;
/* 100 */       this.lastSystemScaleFactor = scaleFactor;
/* 101 */       this.lastUserScaleFactor = userScaleFactor;
/*     */     } 
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
/* 115 */     int left = scale(this.shadowInsets.left, scaleFactor);
/* 116 */     int right = scale(this.shadowInsets.right, scaleFactor);
/* 117 */     int top = scale(this.shadowInsets.top, scaleFactor);
/* 118 */     int bottom = scale(this.shadowInsets.bottom, scaleFactor);
/*     */ 
/*     */     
/* 121 */     int x1o = x - Math.min(left, 0);
/* 122 */     int y1o = y - Math.min(top, 0);
/* 123 */     int x2o = x + width + Math.min(right, 0);
/* 124 */     int y2o = y + height + Math.min(bottom, 0);
/*     */ 
/*     */     
/* 127 */     int x1i = x1o + shadowSize;
/* 128 */     int y1i = y1o + shadowSize;
/* 129 */     int x2i = x2o - shadowSize;
/* 130 */     int y2i = y2o - shadowSize;
/*     */     
/* 132 */     int wh = shadowSize * 2 - 1;
/* 133 */     int center = shadowSize - 1;
/*     */ 
/*     */     
/* 136 */     if (left > 0 || top > 0) {
/* 137 */       g.drawImage(this.shadowImage, x1o, y1o, x1i, y1i, 0, 0, center, center, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 142 */     if (top > 0) {
/* 143 */       g.drawImage(this.shadowImage, x1i, y1o, x2i, y1i, center, 0, center + 1, center, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 148 */     if (right > 0 || top > 0) {
/* 149 */       g.drawImage(this.shadowImage, x2i, y1o, x2o, y1i, center, 0, wh, center, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (left > 0) {
/* 155 */       g.drawImage(this.shadowImage, x1o, y1i, x1i, y2i, 0, center, center, center + 1, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 160 */     if (right > 0) {
/* 161 */       g.drawImage(this.shadowImage, x2i, y1i, x2o, y2i, center, center, wh, center + 1, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 166 */     if (left > 0 || bottom > 0) {
/* 167 */       g.drawImage(this.shadowImage, x1o, y2i, x1i, y2o, 0, center, center, wh, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 172 */     if (bottom > 0) {
/* 173 */       g.drawImage(this.shadowImage, x1i, y2i, x2i, y2o, center, center, center + 1, wh, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 178 */     if (right > 0 || bottom > 0) {
/* 179 */       g.drawImage(this.shadowImage, x2i, y2i, x2o, y2o, center, center, wh, wh, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private int scale(int value, double scaleFactor) {
/* 185 */     return (int)Math.ceil(UIScale.scale(value) * scaleFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static BufferedImage createShadowImage(Color shadowColor, int shadowSize, float shadowOpacity, float scaleFactor) {
/* 191 */     int shadowRGB = shadowColor.getRGB() & 0xFFFFFF;
/* 192 */     int shadowAlpha = (int)(255.0F * shadowOpacity);
/* 193 */     Color startColor = new Color(shadowRGB | (shadowAlpha & 0xFF) << 24, true);
/* 194 */     Color midColor = new Color(shadowRGB | (shadowAlpha / 2 & 0xFF) << 24, true);
/* 195 */     Color endColor = new Color(shadowRGB, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     int wh = shadowSize * 2 - 1;
/* 204 */     int center = shadowSize - 1;
/*     */     
/* 206 */     RadialGradientPaint p = new RadialGradientPaint(center, center, shadowSize - 0.75F * scaleFactor, new float[] { 0.0F, 0.35F, 1.0F }, new Color[] { startColor, midColor, endColor });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     BufferedImage image = new BufferedImage(wh, wh, 2);
/*     */     
/* 213 */     Graphics2D g = image.createGraphics();
/*     */     try {
/* 215 */       g.setPaint(p);
/* 216 */       g.fillRect(0, 0, wh, wh);
/*     */     } finally {
/* 218 */       g.dispose();
/*     */     } 
/*     */     
/* 221 */     return image;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatDropShadowBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */