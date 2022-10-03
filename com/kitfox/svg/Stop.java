/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Stop
/*     */   extends SVGElement
/*     */ {
/*     */   public static final String TAG_NAME = "stop";
/*  49 */   float offset = 0.0F;
/*  50 */   float opacity = 1.0F;
/*  51 */   Color color = Color.black;
/*     */ 
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
/*  63 */     return "stop";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  69 */     super.build();
/*     */     
/*  71 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  73 */     if (getPres(sty.setName("offset"))) {
/*     */       
/*  75 */       this.offset = sty.getFloatValue();
/*  76 */       String units = sty.getUnits();
/*  77 */       if (units != null && units.equals("%"))
/*     */       {
/*  79 */         this.offset /= 100.0F;
/*     */       }
/*  81 */       if (this.offset > 1.0F)
/*     */       {
/*  83 */         this.offset = 1.0F;
/*     */       }
/*  85 */       if (this.offset < 0.0F)
/*     */       {
/*  87 */         this.offset = 0.0F;
/*     */       }
/*     */     } 
/*     */     
/*  91 */     if (getStyle(sty.setName("stop-color")))
/*     */     {
/*  93 */       this.color = sty.getColorValue();
/*     */     }
/*     */     
/*  96 */     if (getStyle(sty.setName("stop-opacity")))
/*     */     {
/*  98 */       this.opacity = sty.getRatioValue();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 115 */     StyleAttribute sty = new StyleAttribute();
/* 116 */     boolean shapeChange = false;
/*     */     
/* 118 */     if (getPres(sty.setName("offset"))) {
/*     */       
/* 120 */       float newVal = sty.getFloatValue();
/* 121 */       if (newVal != this.offset) {
/*     */         
/* 123 */         this.offset = newVal;
/* 124 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     if (getStyle(sty.setName("stop-color"))) {
/*     */       
/* 130 */       Color newVal = sty.getColorValue();
/* 131 */       if (newVal != this.color) {
/*     */         
/* 133 */         this.color = newVal;
/* 134 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 138 */     if (getStyle(sty.setName("stop-opacity"))) {
/*     */       
/* 140 */       float newVal = sty.getFloatValue();
/* 141 */       if (newVal != this.opacity) {
/*     */         
/* 143 */         this.opacity = newVal;
/* 144 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 148 */     return shapeChange;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Stop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */