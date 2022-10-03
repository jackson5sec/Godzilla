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
/*     */ public class Polyline
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "polyline";
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
/*  67 */     return "polyline";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void build() throws SVGException {
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
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 103 */     beginLayer(g);
/* 104 */     renderShape(g, this.path);
/* 105 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 111 */     return shapeToParent(this.path);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 117 */     return boundsToParent(includeStrokeInBounds(this.path.getBounds2D()));
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
/* 131 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 134 */     StyleAttribute sty = new StyleAttribute();
/* 135 */     boolean shapeChange = false;
/*     */     
/* 137 */     if (getStyle(sty.setName("fill-rule"))) {
/*     */       
/* 139 */       int newVal = sty.getStringValue().equals("evenodd") ? 0 : 1;
/*     */ 
/*     */       
/* 142 */       if (newVal != this.fillRule) {
/*     */         
/* 144 */         this.fillRule = newVal;
/* 145 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 149 */     if (getPres(sty.setName("points"))) {
/*     */       
/* 151 */       String newVal = sty.getStringValue();
/* 152 */       if (!newVal.equals(this.pointsStrn)) {
/*     */         
/* 154 */         this.pointsStrn = newVal;
/* 155 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 160 */     if (shapeChange)
/*     */     {
/* 162 */       build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 167 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Polyline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */