/*     */ package com.formdev.flatlaf.util;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatSystemProperties;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import javax.swing.JComponent;
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
/*     */ public class HiDPIUtils
/*     */ {
/*     */   private static Boolean useTextYCorrection;
/*     */   
/*     */   public static void paintAtScale1x(Graphics2D g, JComponent c, Painter painter) {
/*  38 */     paintAtScale1x(g, 0, 0, c.getWidth(), c.getHeight(), painter);
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
/*     */   public static void paintAtScale1x(Graphics2D g, int x, int y, int width, int height, Painter painter) {
/*  51 */     AffineTransform transform = g.getTransform();
/*     */ 
/*     */     
/*  54 */     if (transform.getScaleX() == 1.0D && transform.getScaleY() == 1.0D) {
/*  55 */       painter.paint(g, x, y, width, height, 1.0D);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  60 */     Rectangle2D.Double scaledRect = scale(transform, x, y, width, height);
/*     */ 
/*     */     
/*     */     try {
/*  64 */       g.setTransform(new AffineTransform(1.0D, 0.0D, 0.0D, 1.0D, 
/*  65 */             Math.floor(scaledRect.x), Math.floor(scaledRect.y)));
/*     */       
/*  67 */       int swidth = (int)scaledRect.width;
/*  68 */       int sheight = (int)scaledRect.height;
/*     */ 
/*     */       
/*  71 */       painter.paint(g, 0, 0, swidth, sheight, transform.getScaleX());
/*     */     } finally {
/*     */       
/*  74 */       g.setTransform(transform);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Rectangle2D.Double scale(AffineTransform transform, int x, int y, int width, int height) {
/*  84 */     double dx1 = transform.getScaleX();
/*  85 */     double dy2 = transform.getScaleY();
/*  86 */     double px = x * dx1 + transform.getTranslateX();
/*  87 */     double py = y * dy2 + transform.getTranslateY();
/*  88 */     dx1 *= width;
/*  89 */     dy2 *= height;
/*     */     
/*  91 */     double newx = normalize(px);
/*  92 */     double newy = normalize(py);
/*  93 */     dx1 = normalize(px + dx1) - newx;
/*  94 */     dy2 = normalize(py + dy2) - newy;
/*     */     
/*  96 */     return new Rectangle2D.Double(newx, newy, dx1, dy2);
/*     */   }
/*     */   
/*     */   private static double normalize(double value) {
/* 100 */     return Math.floor(value + 0.25D) + 0.25D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean useTextYCorrection() {
/* 107 */     if (useTextYCorrection == null)
/* 108 */       useTextYCorrection = Boolean.valueOf(FlatSystemProperties.getBoolean("flatlaf.useTextYCorrection", true)); 
/* 109 */     return useTextYCorrection.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float computeTextYCorrection(Graphics2D g) {
/* 120 */     if (!useTextYCorrection() || !SystemInfo.isWindows) {
/* 121 */       return 0.0F;
/*     */     }
/* 123 */     if (!SystemInfo.isJava_9_orLater) {
/* 124 */       return (UIScale.getUserScaleFactor() > 1.0F) ? -UIScale.scale(0.625F) : 0.0F;
/*     */     }
/* 126 */     AffineTransform t = g.getTransform();
/* 127 */     double scaleY = t.getScaleY();
/* 128 */     if (scaleY < 1.25D) {
/* 129 */       return 0.0F;
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
/* 141 */     if (scaleY <= 1.25D)
/* 142 */       return -0.875F; 
/* 143 */     if (scaleY <= 1.5D)
/* 144 */       return -0.625F; 
/* 145 */     if (scaleY <= 1.75D)
/* 146 */       return -0.875F; 
/* 147 */     if (scaleY <= 2.0D)
/* 148 */       return -0.75F; 
/* 149 */     if (scaleY <= 2.25D)
/* 150 */       return -0.875F; 
/* 151 */     if (scaleY <= 3.5D)
/* 152 */       return -0.75F; 
/* 153 */     return -0.875F;
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
/*     */   public static void drawStringWithYCorrection(JComponent c, Graphics2D g, String text, int x, int y) {
/* 165 */     drawStringUnderlineCharAtWithYCorrection(c, g, text, -1, x, y);
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
/*     */   public static void drawStringUnderlineCharAtWithYCorrection(JComponent c, Graphics2D g, String text, int underlinedIndex, int x, int y) {
/* 177 */     float yCorrection = computeTextYCorrection(g);
/* 178 */     if (yCorrection != 0.0F) {
/* 179 */       g.translate(0.0D, yCorrection);
/* 180 */       JavaCompatibility.drawStringUnderlineCharAt(c, g, text, underlinedIndex, x, y);
/* 181 */       g.translate(0.0D, -yCorrection);
/*     */     } else {
/* 183 */       JavaCompatibility.drawStringUnderlineCharAt(c, g, text, underlinedIndex, x, y);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Graphics2D createGraphicsTextYCorrection(Graphics2D g) {
/* 191 */     final float yCorrection = computeTextYCorrection(g);
/* 192 */     if (yCorrection == 0.0F) {
/* 193 */       return g;
/*     */     }
/* 195 */     return new Graphics2DProxy(g)
/*     */       {
/*     */         public void drawString(String str, int x, int y) {
/* 198 */           super.drawString(str, x, y + yCorrection);
/*     */         }
/*     */ 
/*     */         
/*     */         public void drawString(String str, float x, float y) {
/* 203 */           super.drawString(str, x, y + yCorrection);
/*     */         }
/*     */ 
/*     */         
/*     */         public void drawString(AttributedCharacterIterator iterator, int x, int y) {
/* 208 */           super.drawString(iterator, x, y + yCorrection);
/*     */         }
/*     */ 
/*     */         
/*     */         public void drawString(AttributedCharacterIterator iterator, float x, float y) {
/* 213 */           super.drawString(iterator, x, y + yCorrection);
/*     */         }
/*     */ 
/*     */         
/*     */         public void drawChars(char[] data, int offset, int length, int x, int y) {
/* 218 */           super.drawChars(data, offset, length, x, Math.round(y + yCorrection));
/*     */         }
/*     */ 
/*     */         
/*     */         public void drawBytes(byte[] data, int offset, int length, int x, int y) {
/* 223 */           super.drawBytes(data, offset, length, x, Math.round(y + yCorrection));
/*     */         }
/*     */ 
/*     */         
/*     */         public void drawGlyphVector(GlyphVector g, float x, float y) {
/* 228 */           super.drawGlyphVector(g, x, y + yCorrection);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static interface Painter {
/*     */     void paint(Graphics2D param1Graphics2D, int param1Int1, int param1Int2, int param1Int3, int param1Int4, double param1Double);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\util\HiDPIUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */