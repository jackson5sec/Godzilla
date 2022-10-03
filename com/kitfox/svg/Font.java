/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.util.HashMap;
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
/*     */ public class Font
/*     */   extends SVGElement
/*     */ {
/*     */   public static final String TAG_NAME = "font";
/*  53 */   int horizOriginX = 0;
/*  54 */   int horizOriginY = 0;
/*  55 */   int horizAdvX = -1;
/*  56 */   int vertOriginX = -1;
/*  57 */   int vertOriginY = -1;
/*  58 */   int vertAdvY = -1;
/*  59 */   FontFace fontFace = null;
/*  60 */   MissingGlyph missingGlyph = null;
/*  61 */   final HashMap<String, SVGElement> glyphs = new HashMap<String, SVGElement>();
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
/*     */   public String getTagName() {
/*  73 */     return "font";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/*  83 */     super.loaderAddChild(helper, child);
/*     */     
/*  85 */     if (child instanceof Glyph) {
/*     */       
/*  87 */       this.glyphs.put(((Glyph)child).getUnicode(), child);
/*  88 */     } else if (child instanceof MissingGlyph) {
/*     */       
/*  90 */       this.missingGlyph = (MissingGlyph)child;
/*  91 */     } else if (child instanceof FontFace) {
/*     */       
/*  93 */       this.fontFace = (FontFace)child;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderEndElement(SVGLoaderHelper helper) throws SVGParseException {
/* 100 */     super.loaderEndElement(helper);
/*     */ 
/*     */ 
/*     */     
/* 104 */     helper.universe.registerFont(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/* 110 */     super.build();
/*     */     
/* 112 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 114 */     if (getPres(sty.setName("horiz-origin-x")))
/*     */     {
/* 116 */       this.horizOriginX = sty.getIntValue();
/*     */     }
/*     */     
/* 119 */     if (getPres(sty.setName("horiz-origin-y")))
/*     */     {
/* 121 */       this.horizOriginY = sty.getIntValue();
/*     */     }
/*     */     
/* 124 */     if (getPres(sty.setName("horiz-adv-x")))
/*     */     {
/* 126 */       this.horizAdvX = sty.getIntValue();
/*     */     }
/*     */     
/* 129 */     if (getPres(sty.setName("vert-origin-x")))
/*     */     {
/* 131 */       this.vertOriginX = sty.getIntValue();
/*     */     }
/*     */     
/* 134 */     if (getPres(sty.setName("vert-origin-y")))
/*     */     {
/* 136 */       this.vertOriginY = sty.getIntValue();
/*     */     }
/*     */     
/* 139 */     if (getPres(sty.setName("vert-adv-y")))
/*     */     {
/* 141 */       this.vertAdvY = sty.getIntValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public FontFace getFontFace() {
/* 147 */     return this.fontFace;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFontFace(FontFace face) {
/* 152 */     this.fontFace = face;
/*     */   }
/*     */ 
/*     */   
/*     */   public MissingGlyph getGlyph(String unicode) {
/* 157 */     Glyph retVal = (Glyph)this.glyphs.get(unicode);
/* 158 */     if (retVal == null)
/*     */     {
/* 160 */       return this.missingGlyph;
/*     */     }
/* 162 */     return retVal;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHorizOriginX() {
/* 167 */     return this.horizOriginX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHorizOriginY() {
/* 172 */     return this.horizOriginY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHorizAdvX() {
/* 177 */     return this.horizAdvX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVertOriginX() {
/* 182 */     if (this.vertOriginX != -1)
/*     */     {
/* 184 */       return this.vertOriginX;
/*     */     }
/* 186 */     this.vertOriginX = getHorizAdvX() / 2;
/* 187 */     return this.vertOriginX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVertOriginY() {
/* 192 */     if (this.vertOriginY != -1)
/*     */     {
/* 194 */       return this.vertOriginY;
/*     */     }
/* 196 */     this.vertOriginY = this.fontFace.getAscent();
/* 197 */     return this.vertOriginY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVertAdvY() {
/* 202 */     if (this.vertAdvY != -1)
/*     */     {
/* 204 */       return this.vertAdvY;
/*     */     }
/* 206 */     this.vertAdvY = this.fontFace.getUnitsPerEm();
/* 207 */     return this.vertAdvY;
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
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 221 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Font.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */