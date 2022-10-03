/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Line2D;
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
/*     */ public class Line
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "line";
/*  52 */   float x1 = 0.0F;
/*  53 */   float y1 = 0.0F;
/*  54 */   float x2 = 0.0F;
/*  55 */   float y2 = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Line2D.Float line;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  68 */     return "line";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  74 */     super.build();
/*     */     
/*  76 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  78 */     if (getPres(sty.setName("x1")))
/*     */     {
/*  80 */       this.x1 = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  83 */     if (getPres(sty.setName("y1")))
/*     */     {
/*  85 */       this.y1 = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  88 */     if (getPres(sty.setName("x2")))
/*     */     {
/*  90 */       this.x2 = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  93 */     if (getPres(sty.setName("y2")))
/*     */     {
/*  95 */       this.y2 = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  98 */     this.line = new Line2D.Float(this.x1, this.y1, this.x2, this.y2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 104 */     beginLayer(g);
/* 105 */     renderShape(g, this.line);
/* 106 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 112 */     return shapeToParent(this.line);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 118 */     return boundsToParent(includeStrokeInBounds(this.line.getBounds2D()));
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
/* 138 */     if (getPres(sty.setName("x1"))) {
/*     */       
/* 140 */       float newVal = sty.getFloatValueWithUnits();
/* 141 */       if (newVal != this.x1) {
/*     */         
/* 143 */         this.x1 = newVal;
/* 144 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 148 */     if (getPres(sty.setName("y1"))) {
/*     */       
/* 150 */       float newVal = sty.getFloatValueWithUnits();
/* 151 */       if (newVal != this.y1) {
/*     */         
/* 153 */         this.y1 = newVal;
/* 154 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 158 */     if (getPres(sty.setName("x2"))) {
/*     */       
/* 160 */       float newVal = sty.getFloatValueWithUnits();
/* 161 */       if (newVal != this.x2) {
/*     */         
/* 163 */         this.x2 = newVal;
/* 164 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 168 */     if (getPres(sty.setName("y2"))) {
/*     */       
/* 170 */       float newVal = sty.getFloatValueWithUnits();
/* 171 */       if (newVal != this.y2) {
/*     */         
/* 173 */         this.y2 = newVal;
/* 174 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     if (shapeChange)
/*     */     {
/* 180 */       build();
/*     */     }
/*     */     
/* 183 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Line.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */