/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Color;
/*     */ import java.awt.MultipleGradientPaint;
/*     */ import java.awt.Paint;
/*     */ import java.awt.RadialGradientPaint;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RadialGradient
/*     */   extends Gradient
/*     */ {
/*     */   public static final String TAG_NAME = "radialgradient";
/*  55 */   float cx = 0.5F;
/*  56 */   float cy = 0.5F;
/*     */   boolean hasFocus = false;
/*  58 */   float fx = 0.0F;
/*  59 */   float fy = 0.0F;
/*  60 */   float r = 0.5F;
/*     */ 
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
/*  72 */     return "radialgradient";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  78 */     super.build();
/*     */     
/*  80 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  82 */     if (getPres(sty.setName("cx")))
/*     */     {
/*  84 */       this.cx = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  87 */     if (getPres(sty.setName("cy")))
/*     */     {
/*  89 */       this.cy = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  92 */     this.hasFocus = false;
/*  93 */     if (getPres(sty.setName("fx"))) {
/*     */       
/*  95 */       this.fx = sty.getFloatValueWithUnits();
/*  96 */       this.hasFocus = true;
/*     */     } 
/*     */     
/*  99 */     if (getPres(sty.setName("fy"))) {
/*     */       
/* 101 */       this.fy = sty.getFloatValueWithUnits();
/* 102 */       this.hasFocus = true;
/*     */     } 
/*     */     
/* 105 */     if (getPres(sty.setName("r")))
/*     */     {
/* 107 */       this.r = sty.getFloatValueWithUnits();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Paint getPaint(Rectangle2D bounds, AffineTransform xform) {
/*     */     MultipleGradientPaint.CycleMethod method;
/*     */     Paint paint;
/* 115 */     switch (this.spreadMethod) {
/*     */ 
/*     */       
/*     */       default:
/* 119 */         method = MultipleGradientPaint.CycleMethod.NO_CYCLE;
/*     */         break;
/*     */       case 1:
/* 122 */         method = MultipleGradientPaint.CycleMethod.REPEAT;
/*     */         break;
/*     */       case 2:
/* 125 */         method = MultipleGradientPaint.CycleMethod.REFLECT;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 130 */     Point2D.Float pt1 = new Point2D.Float(this.cx, this.cy);
/* 131 */     Point2D.Float pt2 = this.hasFocus ? new Point2D.Float(this.fx, this.fy) : pt1;
/* 132 */     float[] stopFractions = getStopFractions();
/* 133 */     Color[] stopColors = getStopColors();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (this.gradientUnits == 1) {
/*     */       
/* 142 */       paint = new RadialGradientPaint(pt1, this.r, pt2, stopFractions, stopColors, method, MultipleGradientPaint.ColorSpaceType.SRGB, this.gradientTransform);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 153 */       AffineTransform viewXform = new AffineTransform();
/* 154 */       viewXform.translate(bounds.getX(), bounds.getY());
/* 155 */       viewXform.scale(bounds.getWidth(), bounds.getHeight());
/*     */       
/* 157 */       viewXform.concatenate(this.gradientTransform);
/*     */       
/* 159 */       paint = new RadialGradientPaint(pt1, this.r, pt2, stopFractions, stopColors, method, MultipleGradientPaint.ColorSpaceType.SRGB, viewXform);
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
/* 170 */     return paint;
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
/* 184 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 187 */     StyleAttribute sty = new StyleAttribute();
/* 188 */     boolean shapeChange = false;
/*     */     
/* 190 */     if (getPres(sty.setName("cx"))) {
/*     */       
/* 192 */       float newVal = sty.getFloatValueWithUnits();
/* 193 */       if (newVal != this.cx) {
/*     */         
/* 195 */         this.cx = newVal;
/* 196 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 200 */     if (getPres(sty.setName("cy"))) {
/*     */       
/* 202 */       float newVal = sty.getFloatValueWithUnits();
/* 203 */       if (newVal != this.cy) {
/*     */         
/* 205 */         this.cy = newVal;
/* 206 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 210 */     if (getPres(sty.setName("fx"))) {
/*     */       
/* 212 */       float newVal = sty.getFloatValueWithUnits();
/* 213 */       if (newVal != this.fx) {
/*     */         
/* 215 */         this.fx = newVal;
/* 216 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 220 */     if (getPres(sty.setName("fy"))) {
/*     */       
/* 222 */       float newVal = sty.getFloatValueWithUnits();
/* 223 */       if (newVal != this.fy) {
/*     */         
/* 225 */         this.fy = newVal;
/* 226 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 230 */     if (getPres(sty.setName("r"))) {
/*     */       
/* 232 */       float newVal = sty.getFloatValueWithUnits();
/* 233 */       if (newVal != this.r) {
/*     */         
/* 235 */         this.r = newVal;
/* 236 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 240 */     return changeState;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\RadialGradient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */