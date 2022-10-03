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
/*     */ public class FeDistantLight
/*     */   extends FeLight
/*     */ {
/*     */   public static final String TAG_NAME = "fedistantlight";
/*  48 */   float azimuth = 0.0F;
/*  49 */   float elevation = 0.0F;
/*     */ 
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
/*  61 */     return "fedistantlight";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  67 */     super.build();
/*     */     
/*  69 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  71 */     if (getPres(sty.setName("azimuth")))
/*     */     {
/*  73 */       this.azimuth = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  76 */     if (getPres(sty.setName("elevation")))
/*     */     {
/*  78 */       this.elevation = sty.getFloatValueWithUnits();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAzimuth() {
/*  84 */     return this.azimuth;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getElevation() {
/*  89 */     return this.elevation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean updateTime(double curTime) throws SVGException {
/*  98 */     StyleAttribute sty = new StyleAttribute();
/*  99 */     boolean stateChange = false;
/*     */     
/* 101 */     if (getPres(sty.setName("azimuth"))) {
/*     */       
/* 103 */       float newVal = sty.getFloatValueWithUnits();
/* 104 */       if (newVal != this.azimuth) {
/*     */         
/* 106 */         this.azimuth = newVal;
/* 107 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     if (getPres(sty.setName("elevation"))) {
/*     */       
/* 113 */       float newVal = sty.getFloatValueWithUnits();
/* 114 */       if (newVal != this.elevation) {
/*     */         
/* 116 */         this.elevation = newVal;
/* 117 */         stateChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     return stateChange;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\FeDistantLight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */