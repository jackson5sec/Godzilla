/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.net.URI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Use
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "use";
/*  53 */   float x = 0.0F;
/*  54 */   float y = 0.0F;
/*  55 */   float width = 1.0F;
/*  56 */   float height = 1.0F;
/*     */   
/*  58 */   URI href = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AffineTransform refXform;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  71 */     return "use";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  77 */     super.build();
/*     */     
/*  79 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  81 */     if (getPres(sty.setName("x")))
/*     */     {
/*  83 */       this.x = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  86 */     if (getPres(sty.setName("y")))
/*     */     {
/*  88 */       this.y = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  91 */     if (getPres(sty.setName("width")))
/*     */     {
/*  93 */       this.width = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  96 */     if (getPres(sty.setName("height")))
/*     */     {
/*  98 */       this.height = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 101 */     if (getPres(sty.setName("xlink:href"))) {
/*     */       
/* 103 */       URI src = sty.getURIValue(getXMLBase());
/* 104 */       this.href = src;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 109 */     this.refXform = new AffineTransform();
/* 110 */     this.refXform.translate(this.x, this.y);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 116 */     beginLayer(g);
/*     */ 
/*     */     
/* 119 */     AffineTransform oldXform = g.getTransform();
/* 120 */     g.transform(this.refXform);
/*     */     
/* 122 */     SVGElement ref = this.diagram.getUniverse().getElement(this.href);
/*     */     
/* 124 */     if (ref == null || !(ref instanceof RenderableElement)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 129 */     RenderableElement rendEle = (RenderableElement)ref;
/* 130 */     rendEle.pushParentContext(this);
/* 131 */     rendEle.render(g);
/* 132 */     rendEle.popParentContext();
/*     */     
/* 134 */     g.setTransform(oldXform);
/*     */     
/* 136 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 142 */     SVGElement ref = this.diagram.getUniverse().getElement(this.href);
/* 143 */     if (ref instanceof ShapeElement) {
/*     */       
/* 145 */       Shape shape = ((ShapeElement)ref).getShape();
/* 146 */       shape = this.refXform.createTransformedShape(shape);
/* 147 */       shape = shapeToParent(shape);
/* 148 */       return shape;
/*     */     } 
/*     */     
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 157 */     SVGElement ref = this.diagram.getUniverse().getElement(this.href);
/* 158 */     if (ref instanceof ShapeElement) {
/*     */       
/* 160 */       ShapeElement shapeEle = (ShapeElement)ref;
/* 161 */       shapeEle.pushParentContext(this);
/* 162 */       Rectangle2D bounds = shapeEle.getBoundingBox();
/* 163 */       shapeEle.popParentContext();
/*     */       
/* 165 */       bounds = this.refXform.createTransformedShape(bounds).getBounds2D();
/* 166 */       bounds = boundsToParent(bounds);
/*     */       
/* 168 */       return bounds;
/*     */     } 
/*     */     
/* 171 */     return null;
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
/* 185 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 188 */     StyleAttribute sty = new StyleAttribute();
/* 189 */     boolean shapeChange = false;
/*     */     
/* 191 */     if (getPres(sty.setName("x"))) {
/*     */       
/* 193 */       float newVal = sty.getFloatValueWithUnits();
/* 194 */       if (newVal != this.x) {
/*     */         
/* 196 */         this.x = newVal;
/* 197 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 201 */     if (getPres(sty.setName("y"))) {
/*     */       
/* 203 */       float newVal = sty.getFloatValueWithUnits();
/* 204 */       if (newVal != this.y) {
/*     */         
/* 206 */         this.y = newVal;
/* 207 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     if (getPres(sty.setName("width"))) {
/*     */       
/* 213 */       float newVal = sty.getFloatValueWithUnits();
/* 214 */       if (newVal != this.width) {
/*     */         
/* 216 */         this.width = newVal;
/* 217 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 221 */     if (getPres(sty.setName("height"))) {
/*     */       
/* 223 */       float newVal = sty.getFloatValueWithUnits();
/* 224 */       if (newVal != this.height) {
/*     */         
/* 226 */         this.height = newVal;
/* 227 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     if (getPres(sty.setName("xlink:href"))) {
/*     */       
/* 233 */       URI src = sty.getURIValue(getXMLBase());
/*     */       
/* 235 */       if (!src.equals(this.href)) {
/*     */         
/* 237 */         this.href = src;
/* 238 */         shapeChange = true;
/*     */       } 
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
/* 253 */     if (shapeChange)
/*     */     {
/* 255 */       build();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Use.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */