/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Symbol
/*     */   extends Group
/*     */ {
/*     */   public static final String TAG_NAME = "symbol";
/*     */   AffineTransform viewXform;
/*     */   Rectangle2D viewBox;
/*     */   
/*     */   public String getTagName() {
/*  66 */     return "symbol";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  72 */     super.build();
/*     */     
/*  74 */     StyleAttribute sty = new StyleAttribute();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     if (getPres(sty.setName("viewBox"))) {
/*     */       
/*  82 */       float[] dim = sty.getFloatList();
/*  83 */       this.viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
/*     */     } 
/*     */     
/*  86 */     if (this.viewBox == null)
/*     */     {
/*     */       
/*  89 */       this.viewBox = new Rectangle(0, 0, 1, 1);
/*     */     }
/*     */ 
/*     */     
/*  93 */     this.viewXform = new AffineTransform();
/*  94 */     this.viewXform.scale(1.0D / this.viewBox.getWidth(), 1.0D / this.viewBox.getHeight());
/*  95 */     this.viewXform.translate(-this.viewBox.getX(), -this.viewBox.getY());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean outsideClip(Graphics2D g) throws SVGException {
/* 101 */     Shape clip = g.getClip();
/*     */     
/* 103 */     Rectangle2D rect = super.getBoundingBox();
/* 104 */     if (clip == null || clip.intersects(rect))
/*     */     {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 116 */     AffineTransform oldXform = g.getTransform();
/* 117 */     g.transform(this.viewXform);
/*     */     
/* 119 */     super.render(g);
/*     */     
/* 121 */     g.setTransform(oldXform);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 127 */     Shape shape = super.getShape();
/* 128 */     return this.viewXform.createTransformedShape(shape);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 134 */     Rectangle2D rect = super.getBoundingBox();
/* 135 */     return this.viewXform.createTransformedShape(rect).getBounds2D();
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
/* 149 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */ 
/*     */     
/* 153 */     return changeState;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Symbol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */