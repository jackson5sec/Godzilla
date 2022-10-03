/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FilterEffects
/*     */   extends SVGElement
/*     */ {
/*     */   public static final String TAG_NAME = "filtereffects";
/*     */   public static final int FP_SOURCE_GRAPHIC = 0;
/*     */   public static final int FP_SOURCE_ALPHA = 1;
/*     */   public static final int FP_BACKGROUND_IMAGE = 2;
/*     */   public static final int FP_BACKGROUND_ALPHA = 3;
/*     */   public static final int FP_FILL_PAINT = 4;
/*     */   public static final int FP_STROKE_PAINT = 5;
/*     */   public static final int FP_CUSTOM = 5;
/*     */   private int filterPrimitiveTypeIn;
/*     */   private String filterPrimitiveRefIn;
/*  59 */   float x = 0.0F;
/*  60 */   float y = 0.0F;
/*  61 */   float width = 1.0F;
/*  62 */   float height = 1.0F;
/*  63 */   String result = "defaultFilterName";
/*  64 */   URL href = null;
/*     */ 
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
/*  76 */     return "filtereffects";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/*  86 */     super.loaderAddChild(helper, child);
/*     */     
/*  88 */     if (child instanceof FilterEffects);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  97 */     super.build();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getX() {
/* 140 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/* 145 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 150 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 155 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 164 */     StyleAttribute sty = new StyleAttribute();
/* 165 */     boolean stateChange = false;
/*     */     
/* 167 */     if (getPres(sty.setName("x"))) {
/*     */       
/* 169 */       float newVal = sty.getFloatValueWithUnits();
/* 170 */       if (newVal != this.x) {
/*     */         
/* 172 */         this.x = newVal;
/* 173 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 177 */     if (getPres(sty.setName("y"))) {
/*     */       
/* 179 */       float newVal = sty.getFloatValueWithUnits();
/* 180 */       if (newVal != this.y) {
/*     */         
/* 182 */         this.y = newVal;
/* 183 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 187 */     if (getPres(sty.setName("width"))) {
/*     */       
/* 189 */       float newVal = sty.getFloatValueWithUnits();
/* 190 */       if (newVal != this.width) {
/*     */         
/* 192 */         this.width = newVal;
/* 193 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 197 */     if (getPres(sty.setName("height"))) {
/*     */       
/* 199 */       float newVal = sty.getFloatValueWithUnits();
/* 200 */       if (newVal != this.height) {
/*     */         
/* 202 */         this.height = newVal;
/* 203 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 209 */       if (getPres(sty.setName("xlink:href"))) {
/*     */         
/* 211 */         URI src = sty.getURIValue(getXMLBase());
/* 212 */         URL newVal = src.toURL();
/*     */         
/* 214 */         if (!newVal.equals(this.href)) {
/*     */           
/* 216 */           this.href = newVal;
/* 217 */           stateChange = true;
/*     */         } 
/*     */       } 
/* 220 */     } catch (Exception e) {
/*     */       
/* 222 */       throw new SVGException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     return stateChange;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\FilterEffects.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */