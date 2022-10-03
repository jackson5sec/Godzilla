/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Area;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClipPath
/*     */   extends SVGElement
/*     */ {
/*     */   public static final String TAG_NAME = "clippath";
/*     */   public static final int CP_USER_SPACE_ON_USE = 0;
/*     */   public static final int CP_OBJECT_BOUNDING_BOX = 1;
/*  52 */   int clipPathUnits = 0;
/*     */ 
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
/*  64 */     return "clippath";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/*  74 */     super.loaderAddChild(helper, child);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  80 */     super.build();
/*     */     
/*  82 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  84 */     this
/*  85 */       .clipPathUnits = (getPres(sty.setName("clipPathUnits")) && sty.getStringValue().equals("objectBoundingBox")) ? 1 : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getClipPathUnits() {
/*  92 */     return this.clipPathUnits;
/*     */   }
/*     */ 
/*     */   
/*     */   public Shape getClipPathShape() {
/*  97 */     if (this.children.isEmpty())
/*     */     {
/*  99 */       return null;
/*     */     }
/* 101 */     if (this.children.size() == 1)
/*     */     {
/* 103 */       return ((ShapeElement)this.children.get(0)).getShape();
/*     */     }
/*     */     
/* 106 */     Area clipArea = null;
/* 107 */     for (SVGElement svgElement : this.children) {
/* 108 */       ShapeElement se = (ShapeElement)svgElement;
/*     */       
/* 110 */       if (clipArea == null) {
/*     */         
/* 112 */         Shape shape1 = se.getShape();
/* 113 */         if (shape1 != null)
/*     */         {
/* 115 */           clipArea = new Area(se.getShape());
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 120 */       Shape shape = se.getShape();
/* 121 */       if (shape != null)
/*     */       {
/* 123 */         clipArea.intersect(new Area(shape));
/*     */       }
/*     */     } 
/*     */     
/* 127 */     return clipArea;
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
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 143 */     StyleAttribute sty = new StyleAttribute();
/* 144 */     boolean shapeChange = false;
/*     */ 
/*     */     
/* 147 */     if (getPres(sty.setName("clipPathUnits"))) {
/*     */       
/* 149 */       String newUnitsStrn = sty.getStringValue();
/* 150 */       int newUnits = newUnitsStrn.equals("objectBoundingBox") ? 1 : 0;
/*     */ 
/*     */ 
/*     */       
/* 154 */       if (newUnits != this.clipPathUnits) {
/*     */         
/* 156 */         this.clipPathUnits = newUnits;
/* 157 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 161 */     if (shapeChange)
/*     */     {
/* 163 */       build();
/*     */     }
/*     */     
/* 166 */     for (int i = 0; i < this.children.size(); i++) {
/*     */       
/* 168 */       SVGElement ele = this.children.get(i);
/* 169 */       ele.updateTime(curTime);
/*     */     } 
/*     */     
/* 172 */     return shapeChange;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\ClipPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */