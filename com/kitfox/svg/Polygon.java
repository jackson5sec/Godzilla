/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import com.kitfox.svg.xml.XMLParseUtil;
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
/*     */ public class Polygon
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "polygon";
/*  53 */   int fillRule = 1;
/*  54 */   String pointsStrn = "";
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
/*     */   public String getTagName() {
/*  67 */     return "polygon";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  73 */     super.build();
/*     */     
/*  75 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  77 */     if (getPres(sty.setName("points")))
/*     */     {
/*  79 */       this.pointsStrn = sty.getStringValue();
/*     */     }
/*     */     
/*  82 */     String fillRuleStrn = getStyle(sty.setName("fill-rule")) ? sty.getStringValue() : "nonzero";
/*  83 */     this.fillRule = fillRuleStrn.equals("evenodd") ? 0 : 1;
/*     */     
/*  85 */     buildPath();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void buildPath() {
/*  90 */     float[] points = XMLParseUtil.parseFloatList(this.pointsStrn);
/*  91 */     this.path = new GeneralPath(this.fillRule, points.length / 2);
/*     */     
/*  93 */     this.path.moveTo(points[0], points[1]);
/*  94 */     for (int i = 2; i < points.length; i += 2)
/*     */     {
/*  96 */       this.path.lineTo(points[i], points[i + 1]);
/*     */     }
/*  98 */     this.path.closePath();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 104 */     beginLayer(g);
/* 105 */     renderShape(g, this.path);
/* 106 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 112 */     return shapeToParent(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 118 */     return boundsToParent(includeStrokeInBounds(this.path.getBounds2D()));
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
/* 138 */     if (getStyle(sty.setName("fill-rule"))) {
/*     */       
/* 140 */       int newVal = sty.getStringValue().equals("evenodd") ? 0 : 1;
/*     */ 
/*     */       
/* 143 */       if (newVal != this.fillRule) {
/*     */         
/* 145 */         this.fillRule = newVal;
/* 146 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     if (getPres(sty.setName("points"))) {
/*     */       
/* 152 */       String newVal = sty.getStringValue();
/* 153 */       if (!newVal.equals(this.pointsStrn)) {
/*     */         
/* 155 */         this.pointsStrn = newVal;
/* 156 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 161 */     if (shapeChange)
/*     */     {
/* 163 */       build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 168 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Polygon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */