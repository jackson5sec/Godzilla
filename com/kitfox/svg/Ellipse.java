/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Ellipse2D;
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
/*     */ public class Ellipse
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "ellipse";
/*  52 */   float cx = 0.0F;
/*  53 */   float cy = 0.0F;
/*  54 */   float rx = 0.0F;
/*  55 */   float ry = 0.0F;
/*  56 */   Ellipse2D.Float ellipse = new Ellipse2D.Float();
/*     */ 
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
/*  68 */     return "ellipse";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  74 */     super.build();
/*     */     
/*  76 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  78 */     if (getPres(sty.setName("cx")))
/*     */     {
/*  80 */       this.cx = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  83 */     if (getPres(sty.setName("cy")))
/*     */     {
/*  85 */       this.cy = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  88 */     if (getPres(sty.setName("rx")))
/*     */     {
/*  90 */       this.rx = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  93 */     if (getPres(sty.setName("ry")))
/*     */     {
/*  95 */       this.ry = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  98 */     this.ellipse.setFrame(this.cx - this.rx, this.cy - this.ry, this.rx * 2.0F, this.ry * 2.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 104 */     beginLayer(g);
/* 105 */     renderShape(g, this.ellipse);
/* 106 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 112 */     return shapeToParent(this.ellipse);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 118 */     return boundsToParent(includeStrokeInBounds(this.ellipse.getBounds2D()));
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
/* 132 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 135 */     StyleAttribute sty = new StyleAttribute();
/* 136 */     boolean shapeChange = false;
/*     */     
/* 138 */     if (getPres(sty.setName("cx"))) {
/*     */       
/* 140 */       float newCx = sty.getFloatValueWithUnits();
/* 141 */       if (newCx != this.cx) {
/*     */         
/* 143 */         this.cx = newCx;
/* 144 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 148 */     if (getPres(sty.setName("cy"))) {
/*     */       
/* 150 */       float newCy = sty.getFloatValueWithUnits();
/* 151 */       if (newCy != this.cy) {
/*     */         
/* 153 */         this.cy = newCy;
/* 154 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 158 */     if (getPres(sty.setName("rx"))) {
/*     */       
/* 160 */       float newRx = sty.getFloatValueWithUnits();
/* 161 */       if (newRx != this.rx) {
/*     */         
/* 163 */         this.rx = newRx;
/* 164 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 168 */     if (getPres(sty.setName("ry"))) {
/*     */       
/* 170 */       float newRy = sty.getFloatValueWithUnits();
/* 171 */       if (newRy != this.ry) {
/*     */         
/* 173 */         this.ry = newRy;
/* 174 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     if (shapeChange)
/*     */     {
/* 180 */       build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 185 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Ellipse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */