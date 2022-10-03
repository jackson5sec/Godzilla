/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FontFace
/*     */   extends SVGElement
/*     */ {
/*     */   public static final String TAG_NAME = "fontface";
/*     */   String fontFamily;
/*  56 */   private int unitsPerEm = 1000;
/*  57 */   private int ascent = -1;
/*  58 */   private int descent = -1;
/*  59 */   private int accentHeight = -1;
/*  60 */   private int underlinePosition = -1;
/*  61 */   private int underlineThickness = -1;
/*  62 */   private int strikethroughPosition = -1;
/*  63 */   private int strikethroughThickness = -1;
/*  64 */   private int overlinePosition = -1;
/*  65 */   private int overlineThickness = -1;
/*     */ 
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
/*  77 */     return "fontface";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  83 */     super.build();
/*     */     
/*  85 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  87 */     if (getPres(sty.setName("font-family")))
/*     */     {
/*  89 */       this.fontFamily = sty.getStringValue();
/*     */     }
/*     */     
/*  92 */     if (getPres(sty.setName("units-per-em")))
/*     */     {
/*  94 */       this.unitsPerEm = sty.getIntValue();
/*     */     }
/*  96 */     if (getPres(sty.setName("ascent")))
/*     */     {
/*  98 */       this.ascent = sty.getIntValue();
/*     */     }
/* 100 */     if (getPres(sty.setName("descent")))
/*     */     {
/* 102 */       this.descent = sty.getIntValue();
/*     */     }
/* 104 */     if (getPres(sty.setName("accent-height")))
/*     */     {
/* 106 */       this.accentHeight = sty.getIntValue();
/*     */     }
/*     */     
/* 109 */     if (getPres(sty.setName("underline-position")))
/*     */     {
/* 111 */       this.underlinePosition = sty.getIntValue();
/*     */     }
/* 113 */     if (getPres(sty.setName("underline-thickness")))
/*     */     {
/* 115 */       this.underlineThickness = sty.getIntValue();
/*     */     }
/* 117 */     if (getPres(sty.setName("strikethrough-position")))
/*     */     {
/* 119 */       this.strikethroughPosition = sty.getIntValue();
/*     */     }
/* 121 */     if (getPres(sty.setName("strikethrough-thickenss")))
/*     */     {
/* 123 */       this.strikethroughThickness = sty.getIntValue();
/*     */     }
/* 125 */     if (getPres(sty.setName("overline-position")))
/*     */     {
/* 127 */       this.overlinePosition = sty.getIntValue();
/*     */     }
/* 129 */     if (getPres(sty.setName("overline-thickness")))
/*     */     {
/* 131 */       this.overlineThickness = sty.getIntValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFontFamily() {
/* 137 */     return this.fontFamily;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnitsPerEm() {
/* 142 */     return this.unitsPerEm;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAscent() {
/* 147 */     if (this.ascent == -1)
/*     */     {
/* 149 */       this.ascent = this.unitsPerEm - ((Font)this.parent).getVertOriginY();
/*     */     }
/* 151 */     return this.ascent;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDescent() {
/* 156 */     if (this.descent == -1)
/*     */     {
/* 158 */       this.descent = ((Font)this.parent).getVertOriginY();
/*     */     }
/* 160 */     return this.descent;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAccentHeight() {
/* 165 */     if (this.accentHeight == -1)
/*     */     {
/* 167 */       this.accentHeight = getAscent();
/*     */     }
/* 169 */     return this.accentHeight;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnderlinePosition() {
/* 174 */     if (this.underlinePosition == -1)
/*     */     {
/* 176 */       this.underlinePosition = this.unitsPerEm * 5 / 6;
/*     */     }
/* 178 */     return this.underlinePosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnderlineThickness() {
/* 183 */     if (this.underlineThickness == -1)
/*     */     {
/* 185 */       this.underlineThickness = this.unitsPerEm / 20;
/*     */     }
/* 187 */     return this.underlineThickness;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrikethroughPosition() {
/* 192 */     if (this.strikethroughPosition == -1)
/*     */     {
/* 194 */       this.strikethroughPosition = this.unitsPerEm * 3 / 6;
/*     */     }
/* 196 */     return this.strikethroughPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrikethroughThickness() {
/* 201 */     if (this.strikethroughThickness == -1)
/*     */     {
/* 203 */       this.strikethroughThickness = this.unitsPerEm / 20;
/*     */     }
/* 205 */     return this.strikethroughThickness;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOverlinePosition() {
/* 210 */     if (this.overlinePosition == -1)
/*     */     {
/* 212 */       this.overlinePosition = this.unitsPerEm * 5 / 6;
/*     */     }
/* 214 */     return this.overlinePosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOverlineThickness() {
/* 219 */     if (this.overlineThickness == -1)
/*     */     {
/* 221 */       this.overlineThickness = this.unitsPerEm / 20;
/*     */     }
/* 223 */     return this.overlineThickness;
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
/*     */   public boolean updateTime(double curTime) {
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnitsPerEm(int unitsPerEm) {
/* 245 */     this.unitsPerEm = unitsPerEm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAscent(int ascent) {
/* 253 */     this.ascent = ascent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescent(int descent) {
/* 261 */     this.descent = descent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccentHeight(int accentHeight) {
/* 269 */     this.accentHeight = accentHeight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnderlinePosition(int underlinePosition) {
/* 277 */     this.underlinePosition = underlinePosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnderlineThickness(int underlineThickness) {
/* 285 */     this.underlineThickness = underlineThickness;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStrikethroughPosition(int strikethroughPosition) {
/* 293 */     this.strikethroughPosition = strikethroughPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStrikethroughThickness(int strikethroughThickness) {
/* 301 */     this.strikethroughThickness = strikethroughThickness;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverlinePosition(int overlinePosition) {
/* 309 */     this.overlinePosition = overlinePosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverlineThickness(int overlineThickness) {
/* 317 */     this.overlineThickness = overlineThickness;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\FontFace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */