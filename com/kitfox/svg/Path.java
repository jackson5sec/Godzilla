/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.GeneralPath;
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
/*     */ public class Path
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "path";
/*  53 */   int fillRule = 1;
/*  54 */   String d = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GeneralPath path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  68 */     return "path";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  74 */     super.build();
/*     */     
/*  76 */     StyleAttribute sty = new StyleAttribute();
/*     */ 
/*     */     
/*  79 */     String fillRuleStrn = getStyle(sty.setName("fill-rule")) ? sty.getStringValue() : "nonzero";
/*  80 */     this.fillRule = fillRuleStrn.equals("evenodd") ? 0 : 1;
/*     */     
/*  82 */     if (getPres(sty.setName("d")))
/*     */     {
/*  84 */       this.d = sty.getStringValue();
/*     */     }
/*     */     
/*  87 */     this.path = buildPath(this.d, this.fillRule);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/*  93 */     beginLayer(g);
/*  94 */     renderShape(g, this.path);
/*  95 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 101 */     return shapeToParent(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 107 */     return boundsToParent(includeStrokeInBounds(this.path.getBounds2D()));
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
/* 121 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 124 */     StyleAttribute sty = new StyleAttribute();
/* 125 */     boolean shapeChange = false;
/*     */     
/* 127 */     if (getStyle(sty.setName("fill-rule"))) {
/*     */       
/* 129 */       int newVal = sty.getStringValue().equals("evenodd") ? 0 : 1;
/*     */ 
/*     */       
/* 132 */       if (newVal != this.fillRule) {
/*     */         
/* 134 */         this.fillRule = newVal;
/* 135 */         changeState = true;
/*     */       } 
/*     */     } 
/*     */     
/* 139 */     if (getPres(sty.setName("d"))) {
/*     */       
/* 141 */       String newVal = sty.getStringValue();
/* 142 */       if (!newVal.equals(this.d)) {
/*     */         
/* 144 */         this.d = newVal;
/* 145 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 149 */     if (shapeChange)
/*     */     {
/* 151 */       build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 156 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Path.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */