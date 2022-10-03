/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.app.data.Handler;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImageSVG
/*     */   extends RenderableElement
/*     */ {
/*     */   public static final String TAG_NAME = "image";
/*  63 */   float x = 0.0F;
/*  64 */   float y = 0.0F;
/*  65 */   float width = 0.0F;
/*  66 */   float height = 0.0F;
/*     */   
/*  68 */   URL imageSrc = null;
/*     */ 
/*     */ 
/*     */   
/*     */   AffineTransform xform;
/*     */ 
/*     */ 
/*     */   
/*     */   Rectangle2D bounds;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  82 */     return "image";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  88 */     super.build();
/*     */     
/*  90 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  92 */     if (getPres(sty.setName("x")))
/*     */     {
/*  94 */       this.x = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  97 */     if (getPres(sty.setName("y")))
/*     */     {
/*  99 */       this.y = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 102 */     if (getPres(sty.setName("width")))
/*     */     {
/* 104 */       this.width = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 107 */     if (getPres(sty.setName("height")))
/*     */     {
/* 109 */       this.height = sty.getFloatValueWithUnits();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 114 */       if (getPres(sty.setName("xlink:href"))) {
/*     */         
/* 116 */         URI src = sty.getURIValue(getXMLBase());
/* 117 */         if ("data".equals(src.getScheme())) {
/*     */           
/* 119 */           this.imageSrc = new URL(null, src.toASCIIString(), (URLStreamHandler)new Handler());
/*     */         }
/* 121 */         else if (!this.diagram.getUniverse().isImageDataInlineOnly()) {
/*     */ 
/*     */           
/*     */           try {
/* 125 */             this.imageSrc = src.toURL();
/* 126 */           } catch (Exception e) {
/*     */             
/* 128 */             Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse xlink:href " + src, e);
/*     */             
/* 130 */             this.imageSrc = null;
/*     */           } 
/*     */         } 
/*     */       } 
/* 134 */     } catch (Exception e) {
/*     */       
/* 136 */       throw new SVGException(e);
/*     */     } 
/*     */     
/* 139 */     if (this.imageSrc != null) {
/*     */       
/* 141 */       this.diagram.getUniverse().registerImage(this.imageSrc);
/*     */ 
/*     */       
/* 144 */       BufferedImage img = this.diagram.getUniverse().getImage(this.imageSrc);
/* 145 */       if (img == null) {
/*     */         
/* 147 */         this.xform = new AffineTransform();
/* 148 */         this.bounds = new Rectangle2D.Float();
/*     */         
/*     */         return;
/*     */       } 
/* 152 */       if (this.width == 0.0F)
/*     */       {
/* 154 */         this.width = img.getWidth();
/*     */       }
/* 156 */       if (this.height == 0.0F)
/*     */       {
/* 158 */         this.height = img.getHeight();
/*     */       }
/*     */ 
/*     */       
/* 162 */       this.xform = new AffineTransform();
/* 163 */       this.xform.translate(this.x, this.y);
/* 164 */       this.xform.scale((this.width / img.getWidth()), (this.height / img.getHeight()));
/*     */     } 
/*     */     
/* 167 */     this.bounds = new Rectangle2D.Float(this.x, this.y, this.width, this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getX() {
/* 172 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getY() {
/* 177 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 182 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 187 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void pick(Point2D point, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/* 193 */     if (getBoundingBox().contains(point))
/*     */     {
/* 195 */       retVec.add(getPath(null));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void pick(Rectangle2D pickArea, AffineTransform ltw, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/* 202 */     if (ltw.createTransformedShape(getBoundingBox()).intersects(pickArea))
/*     */     {
/* 204 */       retVec.add(getPath(null));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 211 */     StyleAttribute styleAttrib = new StyleAttribute();
/* 212 */     if (getStyle(styleAttrib.setName("visibility")))
/*     */     {
/* 214 */       if (!styleAttrib.getStringValue().equals("visible")) {
/*     */         return;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 220 */     if (getStyle(styleAttrib.setName("display")))
/*     */     {
/* 222 */       if (styleAttrib.getStringValue().equals("none")) {
/*     */         return;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 228 */     beginLayer(g);
/*     */     
/* 230 */     float opacity = 1.0F;
/* 231 */     if (getStyle(styleAttrib.setName("opacity")))
/*     */     {
/* 233 */       opacity = styleAttrib.getRatioValue();
/*     */     }
/*     */     
/* 236 */     if (opacity <= 0.0F) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 241 */     Composite oldComp = null;
/*     */     
/* 243 */     if (opacity < 1.0F) {
/*     */       
/* 245 */       oldComp = g.getComposite();
/* 246 */       Composite comp = AlphaComposite.getInstance(3, opacity);
/* 247 */       g.setComposite(comp);
/*     */     } 
/*     */     
/* 250 */     BufferedImage img = this.diagram.getUniverse().getImage(this.imageSrc);
/* 251 */     if (img == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 256 */     AffineTransform curXform = g.getTransform();
/* 257 */     g.transform(this.xform);
/*     */     
/* 259 */     g.drawImage(img, 0, 0, (ImageObserver)null);
/*     */     
/* 261 */     g.setTransform(curXform);
/* 262 */     if (oldComp != null)
/*     */     {
/* 264 */       g.setComposite(oldComp);
/*     */     }
/*     */     
/* 267 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() {
/* 273 */     return boundsToParent(this.bounds);
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
/* 287 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 290 */     StyleAttribute sty = new StyleAttribute();
/* 291 */     boolean shapeChange = false;
/*     */     
/* 293 */     if (getPres(sty.setName("x"))) {
/*     */       
/* 295 */       float newVal = sty.getFloatValueWithUnits();
/* 296 */       if (newVal != this.x) {
/*     */         
/* 298 */         this.x = newVal;
/* 299 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 303 */     if (getPres(sty.setName("y"))) {
/*     */       
/* 305 */       float newVal = sty.getFloatValueWithUnits();
/* 306 */       if (newVal != this.y) {
/*     */         
/* 308 */         this.y = newVal;
/* 309 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 313 */     if (getPres(sty.setName("width"))) {
/*     */       
/* 315 */       float newVal = sty.getFloatValueWithUnits();
/* 316 */       if (newVal != this.width) {
/*     */         
/* 318 */         this.width = newVal;
/* 319 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 323 */     if (getPres(sty.setName("height"))) {
/*     */       
/* 325 */       float newVal = sty.getFloatValueWithUnits();
/* 326 */       if (newVal != this.height) {
/*     */         
/* 328 */         this.height = newVal;
/* 329 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 335 */       if (getPres(sty.setName("xlink:href"))) {
/*     */         
/* 337 */         URI src = sty.getURIValue(getXMLBase());
/*     */         
/* 339 */         URL newVal = null;
/* 340 */         if ("data".equals(src.getScheme())) {
/*     */           
/* 342 */           newVal = new URL(null, src.toASCIIString(), (URLStreamHandler)new Handler());
/* 343 */         } else if (!this.diagram.getUniverse().isImageDataInlineOnly()) {
/*     */           
/* 345 */           newVal = src.toURL();
/*     */         } 
/*     */         
/* 348 */         if (newVal != null && !newVal.equals(this.imageSrc)) {
/*     */           
/* 350 */           this.imageSrc = newVal;
/* 351 */           shapeChange = true;
/*     */         } 
/*     */       } 
/* 354 */     } catch (IllegalArgumentException ie) {
/*     */       
/* 356 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Image provided with illegal value for href: \"" + sty
/*     */           
/* 358 */           .getStringValue() + '"', ie);
/* 359 */     } catch (Exception e) {
/*     */       
/* 361 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse xlink:href", e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 366 */     if (shapeChange)
/*     */     {
/* 368 */       build();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 396 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\ImageSVG.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */