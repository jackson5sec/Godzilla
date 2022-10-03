/*     */ package com.kitfox.svg.util;
/*     */ 
/*     */ import com.kitfox.svg.Font;
/*     */ import com.kitfox.svg.FontFace;
/*     */ import com.kitfox.svg.Glyph;
/*     */ import com.kitfox.svg.MissingGlyph;
/*     */ import java.awt.Canvas;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.GlyphMetrics;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
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
/*     */ public class FontSystem
/*     */   extends Font
/*     */ {
/*     */   Font sysFont;
/*     */   FontMetrics fm;
/*     */   HashMap<String, Glyph> glyphCache;
/*  65 */   static HashSet<String> sysFontNames = new HashSet<String>();
/*     */ 
/*     */   
/*     */   public static boolean checkIfSystemFontExists(String fontName) {
/*  69 */     if (sysFontNames.isEmpty())
/*     */     {
/*  71 */       for (String name : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH))
/*     */       {
/*  73 */         sysFontNames.add(name);
/*     */       }
/*     */     }
/*     */     
/*  77 */     return sysFontNames.contains(fontName);
/*     */   }
/*     */ 
/*     */   
/*     */   public static FontSystem createFont(String fontFamily, int fontStyle, int fontWeight, float fontSize) {
/*  82 */     String[] families = fontFamily.split(",");
/*  83 */     for (String fontName : families) {
/*     */       
/*  85 */       String javaFontName = mapJavaFontName(fontName);
/*  86 */       if (checkIfSystemFontExists(javaFontName))
/*     */       {
/*  88 */         return new FontSystem(javaFontName, fontStyle, fontWeight, fontSize);
/*     */       }
/*     */     } 
/*     */     
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String mapJavaFontName(String fontName) {
/*  97 */     if ("serif".equals(fontName))
/*     */     {
/*  99 */       return "Serif";
/*     */     }
/* 101 */     if ("sans-serif".equals(fontName))
/*     */     {
/* 103 */       return "SansSerif";
/*     */     }
/* 105 */     if ("monospace".equals(fontName))
/*     */     {
/* 107 */       return "Monospaced";
/*     */     }
/* 109 */     return fontName;
/*     */   }
/*     */   
/*     */   private FontSystem(String fontFamily, int fontStyle, int fontWeight, float fontSize) {
/*     */     int style, weight;
/*     */     this.glyphCache = new HashMap<String, Glyph>();
/* 115 */     switch (fontStyle) {
/*     */       
/*     */       case 1:
/* 118 */         style = 2;
/*     */         break;
/*     */       default:
/* 121 */         style = 0;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 126 */     switch (fontWeight) {
/*     */       
/*     */       case 1:
/*     */       case 2:
/* 130 */         weight = 1;
/*     */         break;
/*     */       default:
/* 133 */         weight = 0;
/*     */         break;
/*     */     } 
/*     */     
/* 137 */     this.sysFont = (new Font(fontFamily, style | weight, 1)).deriveFont(fontSize);
/*     */     
/* 139 */     Canvas c = new Canvas();
/* 140 */     this.fm = c.getFontMetrics(this.sysFont);
/*     */     
/* 142 */     FontFace face = new FontFace();
/* 143 */     face.setAscent(this.fm.getAscent());
/* 144 */     face.setDescent(this.fm.getDescent());
/* 145 */     face.setUnitsPerEm(this.fm.charWidth('M'));
/* 146 */     setFontFace(face);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MissingGlyph getGlyph(String unicode) {
/* 152 */     FontRenderContext frc = new FontRenderContext(null, true, true);
/* 153 */     GlyphVector vec = this.sysFont.createGlyphVector(frc, unicode);
/*     */     
/* 155 */     Glyph glyph = this.glyphCache.get(unicode);
/* 156 */     if (glyph == null) {
/*     */       
/* 158 */       glyph = new Glyph();
/* 159 */       glyph.setPath(vec.getGlyphOutline(0));
/*     */       
/* 161 */       GlyphMetrics gm = vec.getGlyphMetrics(0);
/* 162 */       glyph.setHorizAdvX(gm.getAdvanceX());
/* 163 */       glyph.setVertAdvY(gm.getAdvanceY());
/* 164 */       glyph.setVertOriginX(0.0F);
/* 165 */       glyph.setVertOriginY(0.0F);
/*     */       
/* 167 */       this.glyphCache.put(unicode, glyph);
/*     */     } 
/*     */     
/* 170 */     return (MissingGlyph)glyph;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\sv\\util\FontSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */