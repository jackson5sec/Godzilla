/*     */ package com.formdev.flatlaf.extras;
/*     */ 
/*     */ import com.kitfox.svg.SVGCache;
/*     */ import com.kitfox.svg.SVGDiagram;
/*     */ import com.kitfox.svg.SVGException;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public class FlatSVGUtils
/*     */ {
/*     */   public static List<Image> createWindowIconImages(String svgName) {
/*  50 */     SVGDiagram diagram = loadSVG(svgName);
/*     */     
/*  52 */     return Arrays.asList(new Image[] {
/*  53 */           svg2image(diagram, 16, 16), 
/*  54 */           svg2image(diagram, 24, 24), 
/*  55 */           svg2image(diagram, 32, 32), 
/*  56 */           svg2image(diagram, 48, 48), 
/*  57 */           svg2image(diagram, 64, 64)
/*     */         });
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
/*     */   public static BufferedImage svg2image(String svgName, int width, int height) {
/*  71 */     return svg2image(loadSVG(svgName), width, height);
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
/*     */   public static BufferedImage svg2image(String svgName, float scaleFactor) {
/*  83 */     SVGDiagram diagram = loadSVG(svgName);
/*  84 */     int width = (int)(diagram.getWidth() * scaleFactor);
/*  85 */     int height = (int)(diagram.getHeight() * scaleFactor);
/*  86 */     return svg2image(diagram, width, height);
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
/*     */   public static BufferedImage svg2image(SVGDiagram diagram, int width, int height) {
/*     */     try {
/* 100 */       BufferedImage image = new BufferedImage(width, height, 2);
/*     */       
/* 102 */       Graphics2D g = image.createGraphics();
/*     */       try {
/* 104 */         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 105 */         g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */         
/* 107 */         double sx = (width / diagram.getWidth());
/* 108 */         double sy = (height / diagram.getHeight());
/* 109 */         if (sx != 1.0D || sy != 1.0D) {
/* 110 */           g.scale(sx, sy);
/*     */         }
/* 112 */         diagram.setIgnoringClipHeuristic(true);
/*     */         
/* 114 */         diagram.render(g);
/*     */       } finally {
/* 116 */         g.dispose();
/*     */       } 
/* 118 */       return image;
/*     */     }
/* 120 */     catch (SVGException ex) {
/* 121 */       throw new RuntimeException(ex);
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
/*     */   private static SVGDiagram loadSVG(String svgName) {
/*     */     try {
/* 134 */       URL url = FlatSVGUtils.class.getResource(svgName);
/* 135 */       return SVGCache.getSVGUniverse().getDiagram(url.toURI());
/* 136 */     } catch (URISyntaxException ex) {
/* 137 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\FlatSVGUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */