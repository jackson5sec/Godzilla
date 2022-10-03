/*     */ package com.formdev.flatlaf.util;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
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
/*     */ public class ScaledImageIcon
/*     */   implements Icon
/*     */ {
/*     */   private final ImageIcon imageIcon;
/*     */   private final int iconWidth;
/*     */   private final int iconHeight;
/*     */   private double lastSystemScaleFactor;
/*     */   private float lastUserScaleFactor;
/*     */   private Image lastImage;
/*     */   
/*     */   public ScaledImageIcon(ImageIcon imageIcon) {
/*  48 */     this(imageIcon, imageIcon.getIconWidth(), imageIcon.getIconHeight());
/*     */   }
/*     */   
/*     */   public ScaledImageIcon(ImageIcon imageIcon, int iconWidth, int iconHeight) {
/*  52 */     this.imageIcon = imageIcon;
/*  53 */     this.iconWidth = iconWidth;
/*  54 */     this.iconHeight = iconHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIconWidth() {
/*  59 */     return UIScale.scale(this.iconWidth);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIconHeight() {
/*  64 */     return UIScale.scale(this.iconHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintIcon(Component c, Graphics g, int x, int y) {
/*  75 */     double systemScaleFactor = UIScale.getSystemScaleFactor((Graphics2D)g);
/*  76 */     float userScaleFactor = UIScale.getUserScaleFactor();
/*  77 */     double scaleFactor = systemScaleFactor * userScaleFactor;
/*     */ 
/*     */     
/*  80 */     if (scaleFactor == 1.0D && this.iconWidth == this.imageIcon.getIconWidth() && this.iconHeight == this.imageIcon.getIconHeight()) {
/*  81 */       this.imageIcon.paintIcon(c, g, x, y);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  86 */     if (systemScaleFactor == this.lastSystemScaleFactor && userScaleFactor == this.lastUserScaleFactor && this.lastImage != null) {
/*     */ 
/*     */ 
/*     */       
/*  90 */       paintLastImage(g, x, y);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  95 */     int destImageWidth = (int)Math.round(this.iconWidth * scaleFactor);
/*  96 */     int destImageHeight = (int)Math.round(this.iconHeight * scaleFactor);
/*     */ 
/*     */     
/*  99 */     Image image = getResolutionVariant(destImageWidth, destImageHeight);
/*     */ 
/*     */     
/* 102 */     int imageWidth = image.getWidth(null);
/* 103 */     int imageHeight = image.getHeight(null);
/*     */ 
/*     */     
/* 106 */     if (imageWidth != destImageWidth || imageHeight != destImageHeight) {
/*     */       
/* 108 */       Object scalingInterpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
/* 109 */       float imageScaleFactor = destImageWidth / imageWidth;
/* 110 */       if ((int)imageScaleFactor == imageScaleFactor && imageScaleFactor > 1.0F && imageWidth <= 16 && imageHeight <= 16)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 117 */         scalingInterpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
/*     */       }
/*     */ 
/*     */       
/* 121 */       BufferedImage bufferedImage = image2bufferedImage(image);
/* 122 */       image = scaleImage(bufferedImage, destImageWidth, destImageHeight, scalingInterpolation);
/*     */     } 
/*     */ 
/*     */     
/* 126 */     this.lastSystemScaleFactor = systemScaleFactor;
/* 127 */     this.lastUserScaleFactor = userScaleFactor;
/* 128 */     this.lastImage = image;
/*     */ 
/*     */     
/* 131 */     paintLastImage(g, x, y);
/*     */   }
/*     */   
/*     */   protected Image getResolutionVariant(int destImageWidth, int destImageHeight) {
/* 135 */     return MultiResolutionImageSupport.getResolutionVariant(this.imageIcon
/* 136 */         .getImage(), destImageWidth, destImageHeight);
/*     */   }
/*     */   
/*     */   private void paintLastImage(Graphics g, int x, int y) {
/* 140 */     if (this.lastSystemScaleFactor > 1.0D) {
/* 141 */       HiDPIUtils.paintAtScale1x((Graphics2D)g, x, y, 100, 100, (g2, x2, y2, width2, height2, scaleFactor2) -> g2.drawImage(this.lastImage, x2, y2, (ImageObserver)null));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 146 */       g.drawImage(this.lastImage, x, y, null);
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
/*     */   private BufferedImage scaleImage(BufferedImage image, int targetWidth, int targetHeight, Object scalingInterpolation) {
/* 158 */     BufferedImage bufferedImage = new BufferedImage(targetWidth, targetHeight, 2);
/* 159 */     Graphics2D g = bufferedImage.createGraphics();
/*     */     try {
/* 161 */       g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, scalingInterpolation);
/* 162 */       g.drawImage(image, 0, 0, targetWidth, targetHeight, null);
/*     */     } finally {
/* 164 */       g.dispose();
/*     */     } 
/* 166 */     return bufferedImage;
/*     */   }
/*     */ 
/*     */   
/*     */   private BufferedImage image2bufferedImage(Image image) {
/* 171 */     if (image instanceof BufferedImage) {
/* 172 */       return (BufferedImage)image;
/*     */     }
/*     */     
/* 175 */     BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
/* 176 */     Graphics2D g = bufferedImage.createGraphics();
/*     */     try {
/* 178 */       g.drawImage(image, 0, 0, (ImageObserver)null);
/*     */     } finally {
/* 180 */       g.dispose();
/*     */     } 
/* 182 */     return bufferedImage;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\util\ScaledImageIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */