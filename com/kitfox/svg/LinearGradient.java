/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Color;
/*     */ import java.awt.LinearGradientPaint;
/*     */ import java.awt.MultipleGradientPaint;
/*     */ import java.awt.Paint;
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
/*     */ public class LinearGradient
/*     */   extends Gradient
/*     */ {
/*     */   public static final String TAG_NAME = "lineargradient";
/*  55 */   float x1 = 0.0F;
/*  56 */   float y1 = 0.0F;
/*  57 */   float x2 = 1.0F;
/*  58 */   float y2 = 0.0F;
/*     */ 
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
/*  70 */     return "lineargradient";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  76 */     super.build();
/*     */     
/*  78 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  80 */     if (getPres(sty.setName("x1")))
/*     */     {
/*  82 */       this.x1 = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  85 */     if (getPres(sty.setName("y1")))
/*     */     {
/*  87 */       this.y1 = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  90 */     if (getPres(sty.setName("x2")))
/*     */     {
/*  92 */       this.x2 = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  95 */     if (getPres(sty.setName("y2")))
/*     */     {
/*  97 */       this.y2 = sty.getFloatValueWithUnits();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Paint getPaint(Rectangle2D bounds, AffineTransform xform) {
/*     */     MultipleGradientPaint.CycleMethod method;
/*     */     Paint paint;
/* 105 */     switch (this.spreadMethod) {
/*     */ 
/*     */       
/*     */       default:
/* 109 */         method = MultipleGradientPaint.CycleMethod.NO_CYCLE;
/*     */         break;
/*     */       case 1:
/* 112 */         method = MultipleGradientPaint.CycleMethod.REPEAT;
/*     */         break;
/*     */       case 2:
/* 115 */         method = MultipleGradientPaint.CycleMethod.REFLECT;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 120 */     Point2D.Float pt1 = new Point2D.Float(this.x1, this.y1);
/* 121 */     Point2D.Float pt2 = new Point2D.Float(this.x2, this.y2);
/* 122 */     if (pt1.equals(pt2)) {
/*     */       
/* 124 */       Color[] colors = getStopColors();
/* 125 */       paint = (colors.length > 0) ? colors[0] : Color.black;
/* 126 */     } else if (this.gradientUnits == 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       paint = new LinearGradientPaint(pt1, pt2, getStopFractions(), getStopColors(), method, MultipleGradientPaint.ColorSpaceType.SRGB, (this.gradientTransform == null) ? new AffineTransform() : this.gradientTransform);
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 140 */       AffineTransform viewXform = new AffineTransform();
/* 141 */       viewXform.translate(bounds.getX(), bounds.getY());
/*     */ 
/*     */       
/* 144 */       double width = Math.max(1.0D, bounds.getWidth());
/* 145 */       double height = Math.max(1.0D, bounds.getHeight());
/* 146 */       viewXform.scale(width, height);
/*     */       
/* 148 */       if (this.gradientTransform != null)
/*     */       {
/* 150 */         viewXform.concatenate(this.gradientTransform);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 157 */       paint = new LinearGradientPaint(pt1, pt2, getStopFractions(), getStopColors(), method, MultipleGradientPaint.ColorSpaceType.SRGB, viewXform);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     return paint;
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
/* 177 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 180 */     StyleAttribute sty = new StyleAttribute();
/* 181 */     boolean shapeChange = false;
/*     */     
/* 183 */     if (getPres(sty.setName("x1"))) {
/*     */       
/* 185 */       float newVal = sty.getFloatValueWithUnits();
/* 186 */       if (newVal != this.x1) {
/*     */         
/* 188 */         this.x1 = newVal;
/* 189 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 193 */     if (getPres(sty.setName("y1"))) {
/*     */       
/* 195 */       float newVal = sty.getFloatValueWithUnits();
/* 196 */       if (newVal != this.y1) {
/*     */         
/* 198 */         this.y1 = newVal;
/* 199 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 203 */     if (getPres(sty.setName("x2"))) {
/*     */       
/* 205 */       float newVal = sty.getFloatValueWithUnits();
/* 206 */       if (newVal != this.x2) {
/*     */         
/* 208 */         this.x2 = newVal;
/* 209 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     if (getPres(sty.setName("y2"))) {
/*     */       
/* 215 */       float newVal = sty.getFloatValueWithUnits();
/* 216 */       if (newVal != this.y2) {
/*     */         
/* 218 */         this.y2 = newVal;
/* 219 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 223 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\LinearGradient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */