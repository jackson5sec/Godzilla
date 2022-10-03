/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RenderableElement
/*     */   extends TransformableElement
/*     */ {
/*  56 */   AffineTransform cachedXform = null;
/*     */   
/*  58 */   Shape cachedClip = null;
/*     */ 
/*     */   
/*     */   public static final int VECTOR_EFFECT_NONE = 0;
/*     */   
/*     */   public static final int VECTOR_EFFECT_NON_SCALING_STROKE = 1;
/*     */   
/*     */   int vectorEffect;
/*     */ 
/*     */   
/*     */   public RenderableElement() {}
/*     */ 
/*     */   
/*     */   public RenderableElement(String id, SVGElement parent) {
/*  72 */     super(id, parent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  78 */     super.build();
/*     */     
/*  80 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  82 */     if (getPres(sty.setName("vector-effect"))) {
/*     */       
/*  84 */       if ("non-scaling-stroke".equals(sty.getStringValue())) {
/*     */         
/*  86 */         this.vectorEffect = 1;
/*     */       } else {
/*     */         
/*  89 */         this.vectorEffect = 0;
/*     */       } 
/*     */     } else {
/*     */       
/*  93 */       this.vectorEffect = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void render(Graphics2D paramGraphics2D) throws SVGException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void pick(Point2D paramPoint2D, boolean paramBoolean, List<List<SVGElement>> paramList) throws SVGException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void pick(Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, boolean paramBoolean, List<List<SVGElement>> paramList) throws SVGException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Rectangle2D getBoundingBox() throws SVGException;
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beginLayer(Graphics2D g) throws SVGException {
/* 120 */     if (this.xform != null) {
/*     */       
/* 122 */       this.cachedXform = g.getTransform();
/* 123 */       g.transform(this.xform);
/*     */     } 
/*     */     
/* 126 */     StyleAttribute styleAttrib = new StyleAttribute();
/*     */ 
/*     */ 
/*     */     
/* 130 */     Shape clipPath = null;
/* 131 */     int clipPathUnits = 0;
/* 132 */     if (getStyle(styleAttrib.setName("clip-path"), false) && 
/* 133 */       !"none".equals(styleAttrib.getStringValue())) {
/*     */       
/* 135 */       URI uri = styleAttrib.getURIValue(getXMLBase());
/* 136 */       if (uri != null) {
/*     */         
/* 138 */         ClipPath ele = (ClipPath)this.diagram.getUniverse().getElement(uri);
/* 139 */         clipPath = ele.getClipPathShape();
/* 140 */         clipPathUnits = ele.getClipPathUnits();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 145 */     if (clipPath != null) {
/*     */       
/* 147 */       if (clipPathUnits == 1 && this instanceof ShapeElement) {
/*     */         
/* 149 */         Rectangle2D rect = ((ShapeElement)this).getBoundingBox();
/* 150 */         AffineTransform at = new AffineTransform();
/* 151 */         at.scale(rect.getWidth(), rect.getHeight());
/* 152 */         clipPath = at.createTransformedShape(clipPath);
/*     */       } 
/*     */       
/* 155 */       this.cachedClip = g.getClip();
/* 156 */       if (this.cachedClip == null) {
/*     */         
/* 158 */         g.setClip(clipPath);
/*     */       } else {
/*     */         
/* 161 */         Area newClip = new Area(this.cachedClip);
/* 162 */         newClip.intersect(new Area(clipPath));
/* 163 */         g.setClip(newClip);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finishLayer(Graphics2D g) {
/* 175 */     if (this.cachedClip != null)
/*     */     {
/* 177 */       g.setClip(this.cachedClip);
/*     */     }
/*     */     
/* 180 */     if (this.cachedXform != null)
/*     */     {
/* 182 */       g.setTransform(this.cachedXform);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\RenderableElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */