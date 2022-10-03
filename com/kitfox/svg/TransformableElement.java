/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
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
/*     */ 
/*     */ public abstract class TransformableElement
/*     */   extends SVGElement
/*     */ {
/*  51 */   AffineTransform xform = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransformableElement() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransformableElement(String id, SVGElement parent) {
/*  62 */     super(id, parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AffineTransform getXForm() {
/*  73 */     return (this.xform == null) ? null : new AffineTransform(this.xform);
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
/*     */   protected void build() throws SVGException {
/*  92 */     super.build();
/*     */     
/*  94 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  96 */     if (getPres(sty.setName("transform")))
/*     */     {
/*  98 */       this.xform = parseTransform(sty.getStringValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Shape shapeToParent(Shape shape) {
/* 104 */     if (this.xform == null)
/*     */     {
/* 106 */       return shape;
/*     */     }
/* 108 */     return this.xform.createTransformedShape(shape);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Rectangle2D boundsToParent(Rectangle2D rect) {
/* 113 */     if (this.xform == null || rect == null)
/*     */     {
/* 115 */       return rect;
/*     */     }
/* 117 */     return this.xform.createTransformedShape(rect).getBounds2D();
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
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 130 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 132 */     if (getPres(sty.setName("transform"))) {
/*     */       
/* 134 */       AffineTransform newXform = parseTransform(sty.getStringValue());
/* 135 */       if (!newXform.equals(this.xform)) {
/*     */         
/* 137 */         this.xform = newXform;
/* 138 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\TransformableElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */