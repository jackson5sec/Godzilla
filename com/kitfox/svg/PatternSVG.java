/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.pattern.PatternPaint;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.TexturePaint;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.net.URI;
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
/*     */ public class PatternSVG
/*     */   extends FillElement
/*     */ {
/*     */   public static final String TAG_NAME = "pattern";
/*     */   public static final int GU_OBJECT_BOUNDING_BOX = 0;
/*     */   public static final int GU_USER_SPACE_ON_USE = 1;
/*  62 */   int gradientUnits = 0;
/*     */   float x;
/*     */   float y;
/*     */   float width;
/*     */   float height;
/*  67 */   AffineTransform patternXform = new AffineTransform();
/*     */ 
/*     */ 
/*     */   
/*     */   Rectangle2D.Float viewBox;
/*     */ 
/*     */ 
/*     */   
/*     */   Paint texPaint;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  81 */     return "pattern";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/*  91 */     super.loaderAddChild(helper, child);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  97 */     super.build();
/*     */     
/*  99 */     StyleAttribute sty = new StyleAttribute();
/*     */ 
/*     */     
/* 102 */     String href = null;
/* 103 */     if (getPres(sty.setName("xlink:href")))
/*     */     {
/* 105 */       href = sty.getStringValue();
/*     */     }
/*     */ 
/*     */     
/* 109 */     if (href != null) {
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 114 */         URI src = getXMLBase().resolve(href);
/* 115 */         PatternSVG patSrc = (PatternSVG)this.diagram.getUniverse().getElement(src);
/*     */         
/* 117 */         this.gradientUnits = patSrc.gradientUnits;
/* 118 */         this.x = patSrc.x;
/* 119 */         this.y = patSrc.y;
/* 120 */         this.width = patSrc.width;
/* 121 */         this.height = patSrc.height;
/* 122 */         this.viewBox = patSrc.viewBox;
/* 123 */         this.patternXform.setTransform(patSrc.patternXform);
/* 124 */         this.children.addAll(patSrc.children);
/* 125 */       } catch (Exception e) {
/*     */         
/* 127 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse xlink:href", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 132 */     String gradientUnits = "";
/* 133 */     if (getPres(sty.setName("gradientUnits")))
/*     */     {
/* 135 */       gradientUnits = sty.getStringValue().toLowerCase();
/*     */     }
/* 137 */     if (gradientUnits.equals("userspaceonuse")) {
/*     */       
/* 139 */       this.gradientUnits = 1;
/*     */     } else {
/*     */       
/* 142 */       this.gradientUnits = 0;
/*     */     } 
/*     */     
/* 145 */     String patternTransform = "";
/* 146 */     if (getPres(sty.setName("patternTransform")))
/*     */     {
/* 148 */       patternTransform = sty.getStringValue();
/*     */     }
/* 150 */     this.patternXform = parseTransform(patternTransform);
/*     */ 
/*     */     
/* 153 */     if (getPres(sty.setName("x")))
/*     */     {
/* 155 */       this.x = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 158 */     if (getPres(sty.setName("y")))
/*     */     {
/* 160 */       this.y = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 163 */     if (getPres(sty.setName("width")))
/*     */     {
/* 165 */       this.width = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 168 */     if (getPres(sty.setName("height")))
/*     */     {
/* 170 */       this.height = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 173 */     if (getPres(sty.setName("viewBox"))) {
/*     */       
/* 175 */       float[] dim = sty.getFloatList();
/* 176 */       this.viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
/*     */     } 
/*     */     
/* 179 */     preparePattern();
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
/*     */   protected void preparePattern() throws SVGException {
/* 192 */     int tileWidth = (int)this.width;
/* 193 */     int tileHeight = (int)this.height;
/*     */     
/* 195 */     float stretchX = 1.0F, stretchY = 1.0F;
/* 196 */     if (!this.patternXform.isIdentity()) {
/*     */ 
/*     */       
/* 199 */       float xlateX = (float)this.patternXform.getTranslateX();
/* 200 */       float xlateY = (float)this.patternXform.getTranslateY();
/*     */       
/* 202 */       Point2D.Float pt = new Point2D.Float(), pt2 = new Point2D.Float();
/*     */       
/* 204 */       pt.setLocation(this.width, 0.0F);
/* 205 */       this.patternXform.transform(pt, pt2);
/* 206 */       pt2.x -= xlateX;
/* 207 */       pt2.y -= xlateY;
/* 208 */       stretchX = (float)Math.sqrt((pt2.x * pt2.x + pt2.y * pt2.y)) * 1.5F / this.width;
/*     */       
/* 210 */       pt.setLocation(this.height, 0.0F);
/* 211 */       this.patternXform.transform(pt, pt2);
/* 212 */       pt2.x -= xlateX;
/* 213 */       pt2.y -= xlateY;
/* 214 */       stretchY = (float)Math.sqrt((pt2.x * pt2.x + pt2.y * pt2.y)) * 1.5F / this.height;
/*     */       
/* 216 */       tileWidth = (int)(tileWidth * stretchX);
/* 217 */       tileHeight = (int)(tileHeight * stretchY);
/*     */     } 
/*     */     
/* 220 */     if (tileWidth == 0 || tileHeight == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 226 */     BufferedImage buf = new BufferedImage(tileWidth, tileHeight, 2);
/* 227 */     Graphics2D g = buf.createGraphics();
/* 228 */     g.setClip(0, 0, tileWidth, tileHeight);
/* 229 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */     
/* 231 */     for (SVGElement ele : this.children) {
/* 232 */       if (ele instanceof RenderableElement) {
/*     */         
/* 234 */         AffineTransform xform = new AffineTransform();
/*     */         
/* 236 */         if (this.viewBox == null) {
/*     */           
/* 238 */           xform.translate(-this.x, -this.y);
/*     */         } else {
/*     */           
/* 241 */           xform.scale((tileWidth / this.viewBox.width), (tileHeight / this.viewBox.height));
/* 242 */           xform.translate(-this.viewBox.x, -this.viewBox.y);
/*     */         } 
/*     */         
/* 245 */         g.setTransform(xform);
/* 246 */         ((RenderableElement)ele).render(g);
/*     */       } 
/*     */     } 
/*     */     
/* 250 */     g.dispose();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     if (this.patternXform.isIdentity()) {
/*     */       
/* 258 */       this.texPaint = new TexturePaint(buf, new Rectangle2D.Float(this.x, this.y, this.width, this.height));
/*     */     } else {
/*     */       
/* 261 */       this.patternXform.scale((1.0F / stretchX), (1.0F / stretchY));
/* 262 */       this.texPaint = (Paint)new PatternPaint(buf, this.patternXform);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Paint getPaint(Rectangle2D bounds, AffineTransform xform) {
/* 269 */     return this.texPaint;
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
/* 283 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\PatternSVG.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */