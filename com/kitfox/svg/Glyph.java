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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Glyph
/*     */   extends MissingGlyph
/*     */ {
/*     */   public static final String TAG_NAME = "missingglyph";
/*     */   String unicode;
/*     */   
/*     */   public String getTagName() {
/*  68 */     return "missingglyph";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  74 */     super.build();
/*     */     
/*  76 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  78 */     if (getPres(sty.setName("unicode")))
/*     */     {
/*  80 */       this.unicode = sty.getStringValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnicode() {
/*  86 */     return this.unicode;
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
/* 100 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Glyph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */