/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.geom.NoninvertibleTransformException;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Group
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "group";
/*     */   Rectangle2D boundingBox;
/*     */   Shape cachedShape;
/*     */   
/*     */   public String getTagName() {
/*  71 */     return "group";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/*  81 */     super.loaderAddChild(helper, child);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean outsideClip(Graphics2D g) throws SVGException {
/*  86 */     Shape clip = g.getClip();
/*  87 */     if (clip == null)
/*     */     {
/*  89 */       return false;
/*     */     }
/*     */     
/*  92 */     Rectangle2D rect = getBoundingBox();
/*     */     
/*  94 */     if (clip.intersects(rect))
/*     */     {
/*  96 */       return false;
/*     */     }
/*     */     
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void pick(Point2D point, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/* 105 */     Point2D xPoint = new Point2D.Double(point.getX(), point.getY());
/* 106 */     if (this.xform != null) {
/*     */       
/*     */       try {
/*     */         
/* 110 */         this.xform.inverseTransform(point, xPoint);
/* 111 */       } catch (NoninvertibleTransformException ex) {
/*     */         
/* 113 */         throw new SVGException(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 118 */     for (SVGElement ele : this.children) {
/* 119 */       if (ele instanceof RenderableElement) {
/*     */         
/* 121 */         RenderableElement rendEle = (RenderableElement)ele;
/*     */         
/* 123 */         rendEle.pick(xPoint, boundingBox, retVec);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void pick(Rectangle2D pickArea, AffineTransform ltw, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/* 131 */     if (this.xform != null) {
/*     */       
/* 133 */       ltw = new AffineTransform(ltw);
/* 134 */       ltw.concatenate(this.xform);
/*     */     } 
/*     */ 
/*     */     
/* 138 */     for (SVGElement ele : this.children) {
/* 139 */       if (ele instanceof RenderableElement) {
/*     */         
/* 141 */         RenderableElement rendEle = (RenderableElement)ele;
/*     */         
/* 143 */         rendEle.pick(pickArea, ltw, boundingBox, retVec);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 152 */     StyleAttribute styleAttrib = new StyleAttribute();
/*     */ 
/*     */     
/* 155 */     if (getStyle(styleAttrib.setName("display")))
/*     */     {
/* 157 */       if (styleAttrib.getStringValue().equals("none")) {
/*     */         return;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 164 */     boolean ignoreClip = this.diagram.ignoringClipHeuristic();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 170 */     beginLayer(g);
/*     */     
/* 172 */     Iterator<SVGElement> it = this.children.iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     Shape clip = g.getClip();
/* 186 */     while (it.hasNext()) {
/*     */       
/* 188 */       SVGElement ele = it.next();
/* 189 */       if (ele instanceof RenderableElement) {
/*     */         
/* 191 */         RenderableElement rendEle = (RenderableElement)ele;
/*     */ 
/*     */ 
/*     */         
/* 195 */         if (!(ele instanceof Group))
/*     */         {
/*     */           
/* 198 */           if (!ignoreClip && clip != null && 
/* 199 */             !clip.intersects(rendEle.getBoundingBox())) {
/*     */             continue;
/*     */           }
/*     */         }
/*     */ 
/*     */         
/* 205 */         rendEle.render(g);
/*     */       } 
/*     */     } 
/*     */     
/* 209 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 218 */     if (this.cachedShape == null)
/*     */     {
/* 220 */       calcShape();
/*     */     }
/* 222 */     return this.cachedShape;
/*     */   }
/*     */ 
/*     */   
/*     */   public void calcShape() {
/* 227 */     Area retShape = new Area();
/*     */     
/* 229 */     for (SVGElement ele : this.children) {
/* 230 */       if (ele instanceof ShapeElement) {
/*     */         
/* 232 */         ShapeElement shpEle = (ShapeElement)ele;
/* 233 */         Shape shape = shpEle.getShape();
/* 234 */         if (shape != null)
/*     */         {
/* 236 */           retShape.add(new Area(shape));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 241 */     this.cachedShape = shapeToParent(retShape);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 250 */     if (this.boundingBox == null)
/*     */     {
/* 252 */       calcBoundingBox();
/*     */     }
/*     */     
/* 255 */     return this.boundingBox;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void calcBoundingBox() throws SVGException {
/* 265 */     Rectangle2D retRect = null;
/*     */     
/* 267 */     for (SVGElement ele : this.children) {
/* 268 */       if (ele instanceof RenderableElement) {
/*     */         
/* 270 */         RenderableElement rendEle = (RenderableElement)ele;
/* 271 */         Rectangle2D bounds = rendEle.getBoundingBox();
/* 272 */         if (bounds != null && (bounds.getWidth() != 0.0D || bounds.getHeight() != 0.0D)) {
/*     */           
/* 274 */           if (retRect == null) {
/*     */             
/* 276 */             retRect = bounds;
/*     */             
/*     */             continue;
/*     */           } 
/* 280 */           if (retRect.getWidth() != 0.0D || retRect.getHeight() != 0.0D)
/*     */           {
/* 282 */             retRect = retRect.createUnion(bounds);
/*     */           }
/*     */         } 
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
/* 295 */     if (retRect == null)
/*     */     {
/* 297 */       retRect = new Rectangle2D.Float();
/*     */     }
/*     */     
/* 300 */     this.boundingBox = boundsToParent(retRect);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 306 */     boolean changeState = super.updateTime(curTime);
/* 307 */     Iterator<SVGElement> it = this.children.iterator();
/*     */ 
/*     */     
/* 310 */     while (it.hasNext()) {
/*     */       
/* 312 */       SVGElement ele = it.next();
/* 313 */       boolean updateVal = ele.updateTime(curTime);
/*     */       
/* 315 */       changeState = (changeState || updateVal);
/*     */ 
/*     */       
/* 318 */       if (ele instanceof ShapeElement)
/*     */       {
/* 320 */         this.cachedShape = null;
/*     */       }
/* 322 */       if (ele instanceof RenderableElement)
/*     */       {
/* 324 */         this.boundingBox = null;
/*     */       }
/*     */     } 
/*     */     
/* 328 */     return changeState;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Group.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */