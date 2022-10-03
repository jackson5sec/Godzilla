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
/*     */ public class Circle
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "circle";
/*  52 */   float cx = 0.0F;
/*  53 */   float cy = 0.0F;
/*  54 */   float r = 0.0F;
/*  55 */   Ellipse2D.Float circle = new Ellipse2D.Float();
/*     */ 
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
/*  67 */     return "circle";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  73 */     super.build();
/*     */     
/*  75 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  77 */     if (getPres(sty.setName("cx")))
/*     */     {
/*  79 */       this.cx = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  82 */     if (getPres(sty.setName("cy")))
/*     */     {
/*  84 */       this.cy = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  87 */     if (getPres(sty.setName("r")))
/*     */     {
/*  89 */       this.r = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  92 */     this.circle.setFrame(this.cx - this.r, this.cy - this.r, this.r * 2.0F, this.r * 2.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/*  98 */     beginLayer(g);
/*  99 */     renderShape(g, this.circle);
/* 100 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 106 */     return shapeToParent(this.circle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 112 */     return boundsToParent(includeStrokeInBounds(this.circle.getBounds2D()));
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
/* 126 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 129 */     StyleAttribute sty = new StyleAttribute();
/* 130 */     boolean shapeChange = false;
/*     */     
/* 132 */     if (getPres(sty.setName("cx"))) {
/*     */       
/* 134 */       float newVal = sty.getFloatValueWithUnits();
/* 135 */       if (newVal != this.cx) {
/*     */         
/* 137 */         this.cx = newVal;
/* 138 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     if (getPres(sty.setName("cy"))) {
/*     */       
/* 144 */       float newVal = sty.getFloatValueWithUnits();
/* 145 */       if (newVal != this.cy) {
/*     */         
/* 147 */         this.cy = newVal;
/* 148 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 152 */     if (getPres(sty.setName("r"))) {
/*     */       
/* 154 */       float newVal = sty.getFloatValueWithUnits();
/* 155 */       if (newVal != this.r) {
/*     */         
/* 157 */         this.r = newVal;
/* 158 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 162 */     if (shapeChange)
/*     */     {
/* 164 */       build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 169 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Circle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */