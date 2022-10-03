/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ public abstract class ShapeElement
/*     */   extends RenderableElement
/*     */ {
/*  72 */   protected float strokeWidthScalar = 1.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void render(Graphics2D paramGraphics2D) throws SVGException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void pick(Point2D point, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/*  93 */     if ((boundingBox ? getBoundingBox() : getShape()).contains(point))
/*     */     {
/*  95 */       retVec.add(getPath(null));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void pick(Rectangle2D pickArea, AffineTransform ltw, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/* 104 */     if (ltw.createTransformedShape(boundingBox ? getBoundingBox() : getShape()).intersects(pickArea))
/*     */     {
/* 106 */       retVec.add(getPath(null));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private Paint handleCurrentColor(StyleAttribute styleAttrib) throws SVGException {
/* 112 */     if (styleAttrib.getStringValue().equals("currentColor")) {
/*     */       
/* 114 */       StyleAttribute currentColorAttrib = new StyleAttribute();
/* 115 */       if (getStyle(currentColorAttrib.setName("color")))
/*     */       {
/* 117 */         if (!currentColorAttrib.getStringValue().equals("none"))
/*     */         {
/* 119 */           return currentColorAttrib.getColorValue();
/*     */         }
/*     */       }
/* 122 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 126 */     return styleAttrib.getColorValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderShape(Graphics2D g, Shape shape) throws SVGException {
/* 134 */     StyleAttribute styleAttrib = new StyleAttribute();
/*     */ 
/*     */     
/* 137 */     if (getStyle(styleAttrib.setName("visibility")))
/*     */     {
/* 139 */       if (!styleAttrib.getStringValue().equals("visible"))
/*     */         return; 
/*     */     }
/* 142 */     if (getStyle(styleAttrib.setName("display")))
/*     */     {
/* 144 */       if (styleAttrib.getStringValue().equals("none")) {
/*     */         return;
/*     */       }
/*     */     }
/* 148 */     Paint fillPaint = Color.black;
/* 149 */     if (getStyle(styleAttrib.setName("fill")))
/*     */     {
/* 151 */       if (styleAttrib.getStringValue().equals("none")) { fillPaint = null; }
/*     */       else
/*     */       
/* 154 */       { fillPaint = handleCurrentColor(styleAttrib);
/* 155 */         if (fillPaint == null) {
/*     */           
/* 157 */           URI uri = styleAttrib.getURIValue(getXMLBase());
/* 158 */           if (uri != null) {
/*     */             
/* 160 */             Rectangle2D bounds = shape.getBounds2D();
/* 161 */             AffineTransform xform = g.getTransform();
/*     */             
/* 163 */             SVGElement ele = this.diagram.getUniverse().getElement(uri);
/* 164 */             if (ele != null) {
/*     */               
/*     */               try {
/* 167 */                 fillPaint = ((FillElement)ele).getPaint(bounds, xform);
/* 168 */               } catch (IllegalArgumentException e) {
/* 169 */                 throw new SVGException(e);
/*     */               } 
/*     */             }
/*     */           } 
/*     */         }  }
/*     */     
/*     */     }
/*     */ 
/*     */     
/* 178 */     float opacity = 1.0F;
/* 179 */     if (getStyle(styleAttrib.setName("opacity")))
/*     */     {
/* 181 */       opacity = styleAttrib.getRatioValue();
/*     */     }
/*     */     
/* 184 */     float fillOpacity = opacity;
/* 185 */     if (getStyle(styleAttrib.setName("fill-opacity")))
/*     */     {
/* 187 */       fillOpacity *= styleAttrib.getRatioValue();
/*     */     }
/*     */ 
/*     */     
/* 191 */     Paint strokePaint = null;
/* 192 */     if (getStyle(styleAttrib.setName("stroke")))
/*     */     {
/* 194 */       if (styleAttrib.getStringValue().equals("none")) { strokePaint = null; }
/*     */       else
/*     */       
/* 197 */       { strokePaint = handleCurrentColor(styleAttrib);
/* 198 */         if (strokePaint == null) {
/*     */           
/* 200 */           URI uri = styleAttrib.getURIValue(getXMLBase());
/* 201 */           if (uri != null) {
/*     */             
/* 203 */             Rectangle2D bounds = shape.getBounds2D();
/* 204 */             AffineTransform xform = g.getTransform();
/*     */             
/* 206 */             SVGElement ele = this.diagram.getUniverse().getElement(uri);
/* 207 */             if (ele != null)
/*     */             {
/* 209 */               strokePaint = ((FillElement)ele).getPaint(bounds, xform);
/*     */             }
/*     */           } 
/*     */         }  }
/*     */     
/*     */     }
/*     */     
/* 216 */     float[] strokeDashArray = null;
/* 217 */     if (getStyle(styleAttrib.setName("stroke-dasharray"))) {
/*     */       
/* 219 */       strokeDashArray = styleAttrib.getFloatList();
/* 220 */       if (strokeDashArray.length == 0) strokeDashArray = null;
/*     */     
/*     */     } 
/* 223 */     float strokeDashOffset = 0.0F;
/* 224 */     if (getStyle(styleAttrib.setName("stroke-dashoffset")))
/*     */     {
/* 226 */       strokeDashOffset = styleAttrib.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 229 */     int strokeLinecap = 0;
/* 230 */     if (getStyle(styleAttrib.setName("stroke-linecap"))) {
/*     */       
/* 232 */       String val = styleAttrib.getStringValue();
/* 233 */       if (val.equals("round")) {
/*     */         
/* 235 */         strokeLinecap = 1;
/*     */       }
/* 237 */       else if (val.equals("square")) {
/*     */         
/* 239 */         strokeLinecap = 2;
/*     */       } 
/*     */     } 
/*     */     
/* 243 */     int strokeLinejoin = 0;
/* 244 */     if (getStyle(styleAttrib.setName("stroke-linejoin"))) {
/*     */       
/* 246 */       String val = styleAttrib.getStringValue();
/* 247 */       if (val.equals("round")) {
/*     */         
/* 249 */         strokeLinejoin = 1;
/*     */       }
/* 251 */       else if (val.equals("bevel")) {
/*     */         
/* 253 */         strokeLinejoin = 2;
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     float strokeMiterLimit = 4.0F;
/* 258 */     if (getStyle(styleAttrib.setName("stroke-miterlimit")))
/*     */     {
/* 260 */       strokeMiterLimit = Math.max(styleAttrib.getFloatValueWithUnits(), 1.0F);
/*     */     }
/*     */     
/* 263 */     float strokeOpacity = opacity;
/* 264 */     if (getStyle(styleAttrib.setName("stroke-opacity")))
/*     */     {
/* 266 */       strokeOpacity *= styleAttrib.getRatioValue();
/*     */     }
/*     */     
/* 269 */     float strokeWidth = 1.0F;
/* 270 */     if (getStyle(styleAttrib.setName("stroke-width")))
/*     */     {
/* 272 */       strokeWidth = styleAttrib.getFloatValueWithUnits();
/*     */     }
/*     */ 
/*     */     
/* 276 */     strokeWidth *= this.strokeWidthScalar;
/*     */ 
/*     */     
/* 279 */     Marker markerStart = null;
/* 280 */     if (getStyle(styleAttrib.setName("marker-start")))
/*     */     {
/* 282 */       if (!styleAttrib.getStringValue().equals("none")) {
/*     */         
/* 284 */         URI uri = styleAttrib.getURIValue(getXMLBase());
/* 285 */         markerStart = (Marker)this.diagram.getUniverse().getElement(uri);
/*     */       } 
/*     */     }
/*     */     
/* 289 */     Marker markerMid = null;
/* 290 */     if (getStyle(styleAttrib.setName("marker-mid")))
/*     */     {
/* 292 */       if (!styleAttrib.getStringValue().equals("none")) {
/*     */         
/* 294 */         URI uri = styleAttrib.getURIValue(getXMLBase());
/* 295 */         markerMid = (Marker)this.diagram.getUniverse().getElement(uri);
/*     */       } 
/*     */     }
/*     */     
/* 299 */     Marker markerEnd = null;
/* 300 */     if (getStyle(styleAttrib.setName("marker-end")))
/*     */     {
/* 302 */       if (!styleAttrib.getStringValue().equals("none")) {
/*     */         
/* 304 */         URI uri = styleAttrib.getURIValue(getXMLBase());
/* 305 */         markerEnd = (Marker)this.diagram.getUniverse().getElement(uri);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 311 */     if (fillPaint != null && fillOpacity != 0.0F)
/*     */     {
/* 313 */       if (fillOpacity > 0.0F)
/*     */       {
/*     */ 
/*     */         
/* 317 */         if (fillOpacity < 1.0F) {
/*     */           
/* 319 */           Composite cachedComposite = g.getComposite();
/* 320 */           g.setComposite(AlphaComposite.getInstance(3, fillOpacity));
/*     */           
/* 322 */           g.setPaint(fillPaint);
/* 323 */           g.fill(shape);
/*     */           
/* 325 */           g.setComposite(cachedComposite);
/*     */         }
/*     */         else {
/*     */           
/* 329 */           g.setPaint(fillPaint);
/* 330 */           g.fill(shape);
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/* 335 */     if (strokePaint != null && strokeOpacity != 0.0F) {
/*     */       BasicStroke stroke;
/*     */       Shape strokeShape;
/* 338 */       if (strokeDashArray == null) {
/*     */         
/* 340 */         stroke = new BasicStroke(strokeWidth, strokeLinecap, strokeLinejoin, strokeMiterLimit);
/*     */       }
/*     */       else {
/*     */         
/* 344 */         stroke = new BasicStroke(strokeWidth, strokeLinecap, strokeLinejoin, strokeMiterLimit, strokeDashArray, strokeDashOffset);
/*     */       } 
/*     */ 
/*     */       
/* 348 */       AffineTransform cacheXform = g.getTransform();
/* 349 */       if (this.vectorEffect == 1) {
/*     */         
/* 351 */         strokeShape = cacheXform.createTransformedShape(shape);
/* 352 */         strokeShape = stroke.createStrokedShape(strokeShape);
/*     */       }
/*     */       else {
/*     */         
/* 356 */         strokeShape = stroke.createStrokedShape(shape);
/*     */       } 
/*     */       
/* 359 */       if (strokeOpacity > 0.0F) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 365 */         Composite cachedComposite = g.getComposite();
/*     */         
/* 367 */         if (strokeOpacity < 1.0F)
/*     */         {
/* 369 */           g.setComposite(AlphaComposite.getInstance(3, strokeOpacity));
/*     */         }
/*     */         
/* 372 */         if (this.vectorEffect == 1)
/*     */         {
/*     */           
/* 375 */           g.setTransform(new AffineTransform());
/*     */         }
/*     */         
/* 378 */         g.setPaint(strokePaint);
/* 379 */         g.fill(strokeShape);
/*     */         
/* 381 */         if (this.vectorEffect == 1)
/*     */         {
/*     */           
/* 384 */           g.setTransform(cacheXform);
/*     */         }
/*     */         
/* 387 */         if (strokeOpacity < 1.0F)
/*     */         {
/* 389 */           g.setComposite(cachedComposite);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 394 */     if (markerStart != null || markerMid != null || markerEnd != null) {
/*     */       
/* 396 */       Marker.MarkerLayout layout = new Marker.MarkerLayout();
/* 397 */       layout.layout(shape);
/*     */       
/* 399 */       ArrayList<Marker.MarkerPos> list = layout.getMarkerList();
/* 400 */       for (int i = 0; i < list.size(); i++) {
/*     */         
/* 402 */         Marker.MarkerPos pos = list.get(i);
/*     */         
/* 404 */         switch (pos.type) {
/*     */           
/*     */           case 0:
/* 407 */             if (markerStart != null)
/*     */             {
/* 409 */               markerStart.render(g, pos, strokeWidth);
/*     */             }
/*     */             break;
/*     */           case 1:
/* 413 */             if (markerMid != null)
/*     */             {
/* 415 */               markerMid.render(g, pos, strokeWidth);
/*     */             }
/*     */             break;
/*     */           case 2:
/* 419 */             if (markerEnd != null)
/*     */             {
/* 421 */               markerEnd.render(g, pos, strokeWidth);
/*     */             }
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Shape getShape();
/*     */   
/*     */   protected Rectangle2D includeStrokeInBounds(Rectangle2D rect) throws SVGException {
/* 433 */     StyleAttribute styleAttrib = new StyleAttribute();
/* 434 */     if (!getStyle(styleAttrib.setName("stroke"))) return rect;
/*     */     
/* 436 */     double strokeWidth = 1.0D;
/* 437 */     if (getStyle(styleAttrib.setName("stroke-width"))) strokeWidth = styleAttrib.getDoubleValue();
/*     */     
/* 439 */     rect.setRect(rect
/* 440 */         .getX() - strokeWidth / 2.0D, rect
/* 441 */         .getY() - strokeWidth / 2.0D, rect
/* 442 */         .getWidth() + strokeWidth, rect
/* 443 */         .getHeight() + strokeWidth);
/*     */     
/* 445 */     return rect;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\ShapeElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */