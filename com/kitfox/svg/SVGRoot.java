/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.NumberWithUnits;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import com.kitfox.svg.xml.StyleSheet;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.NoninvertibleTransformException;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
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
/*     */ public class SVGRoot
/*     */   extends Group
/*     */ {
/*     */   public static final String TAG_NAME = "svg";
/*     */   NumberWithUnits x;
/*     */   NumberWithUnits y;
/*     */   NumberWithUnits width;
/*     */   NumberWithUnits height;
/*  66 */   Rectangle2D.Float viewBox = null;
/*     */   
/*     */   public static final int PA_X_NONE = 0;
/*     */   
/*     */   public static final int PA_X_MIN = 1;
/*     */   
/*     */   public static final int PA_X_MID = 2;
/*     */   
/*     */   public static final int PA_X_MAX = 3;
/*     */   public static final int PA_Y_NONE = 0;
/*     */   public static final int PA_Y_MIN = 1;
/*     */   public static final int PA_Y_MID = 2;
/*     */   public static final int PA_Y_MAX = 3;
/*     */   public static final int PS_MEET = 0;
/*     */   public static final int PS_SLICE = 1;
/*  81 */   int parSpecifier = 0;
/*  82 */   int parAlignX = 2;
/*  83 */   int parAlignY = 2;
/*     */   
/*  85 */   final AffineTransform viewXform = new AffineTransform();
/*  86 */   final Rectangle2D.Float clipRect = new Rectangle2D.Float();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StyleSheet styleSheet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  98 */     return "svg";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void build() throws SVGException {
/* 104 */     super.build();
/*     */     
/* 106 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 108 */     if (getPres(sty.setName("x")))
/*     */     {
/* 110 */       this.x = sty.getNumberWithUnits();
/*     */     }
/*     */     
/* 113 */     if (getPres(sty.setName("y")))
/*     */     {
/* 115 */       this.y = sty.getNumberWithUnits();
/*     */     }
/*     */     
/* 118 */     if (getPres(sty.setName("width")))
/*     */     {
/* 120 */       this.width = sty.getNumberWithUnits();
/*     */     }
/*     */     
/* 123 */     if (getPres(sty.setName("height")))
/*     */     {
/* 125 */       this.height = sty.getNumberWithUnits();
/*     */     }
/*     */     
/* 128 */     if (getPres(sty.setName("viewBox"))) {
/*     */       
/* 130 */       float[] coords = sty.getFloatList();
/* 131 */       this.viewBox = new Rectangle2D.Float(coords[0], coords[1], coords[2], coords[3]);
/*     */     } 
/*     */     
/* 134 */     if (getPres(sty.setName("preserveAspectRatio"))) {
/*     */       
/* 136 */       String preserve = sty.getStringValue();
/*     */       
/* 138 */       if (contains(preserve, "none")) { this.parAlignX = 0; this.parAlignY = 0; }
/* 139 */       else if (contains(preserve, "xMinYMin")) { this.parAlignX = 1; this.parAlignY = 1; }
/* 140 */       else if (contains(preserve, "xMidYMin")) { this.parAlignX = 2; this.parAlignY = 1; }
/* 141 */       else if (contains(preserve, "xMaxYMin")) { this.parAlignX = 3; this.parAlignY = 1; }
/* 142 */       else if (contains(preserve, "xMinYMid")) { this.parAlignX = 1; this.parAlignY = 2; }
/* 143 */       else if (contains(preserve, "xMidYMid")) { this.parAlignX = 2; this.parAlignY = 2; }
/* 144 */       else if (contains(preserve, "xMaxYMid")) { this.parAlignX = 3; this.parAlignY = 2; }
/* 145 */       else if (contains(preserve, "xMinYMax")) { this.parAlignX = 1; this.parAlignY = 3; }
/* 146 */       else if (contains(preserve, "xMidYMax")) { this.parAlignX = 2; this.parAlignY = 3; }
/* 147 */       else if (contains(preserve, "xMaxYMax")) { this.parAlignX = 3; this.parAlignY = 3; }
/*     */       
/* 149 */       if (contains(preserve, "meet")) {
/*     */         
/* 151 */         this.parSpecifier = 0;
/*     */       }
/* 153 */       else if (contains(preserve, "slice")) {
/*     */         
/* 155 */         this.parSpecifier = 1;
/*     */       } 
/*     */     } 
/*     */     
/* 159 */     prepareViewport();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean contains(String text, String find) {
/* 164 */     return (text.indexOf(find) != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGRoot getRoot() {
/* 170 */     return this;
/*     */   }
/*     */   protected void prepareViewport() {
/*     */     Rectangle2D defaultBounds;
/*     */     float xx, yy, ww, hh;
/* 175 */     Rectangle deviceViewport = this.diagram.getDeviceViewport();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 180 */       defaultBounds = getBoundingBox();
/*     */     }
/* 182 */     catch (SVGException ex) {
/*     */       
/* 184 */       defaultBounds = new Rectangle2D.Float();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 189 */     if (this.width != null) {
/*     */       
/* 191 */       xx = (this.x == null) ? 0.0F : StyleAttribute.convertUnitsToPixels(this.x.getUnits(), this.x.getValue());
/* 192 */       if (this.width.getUnits() == 9)
/*     */       {
/* 194 */         ww = this.width.getValue() * deviceViewport.width;
/*     */       }
/*     */       else
/*     */       {
/* 198 */         ww = StyleAttribute.convertUnitsToPixels(this.width.getUnits(), this.width.getValue());
/*     */       }
/*     */     
/* 201 */     } else if (this.viewBox != null) {
/*     */       
/* 203 */       xx = this.viewBox.x;
/* 204 */       ww = this.viewBox.width;
/* 205 */       this.width = new NumberWithUnits(ww, 1);
/* 206 */       this.x = new NumberWithUnits(xx, 1);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 211 */       xx = (float)defaultBounds.getX();
/* 212 */       ww = (float)defaultBounds.getWidth();
/* 213 */       this.width = new NumberWithUnits(ww, 1);
/* 214 */       this.x = new NumberWithUnits(xx, 1);
/*     */     } 
/*     */     
/* 217 */     if (this.height != null) {
/*     */       
/* 219 */       yy = (this.y == null) ? 0.0F : StyleAttribute.convertUnitsToPixels(this.y.getUnits(), this.y.getValue());
/* 220 */       if (this.height.getUnits() == 9)
/*     */       {
/* 222 */         hh = this.height.getValue() * deviceViewport.height;
/*     */       }
/*     */       else
/*     */       {
/* 226 */         hh = StyleAttribute.convertUnitsToPixels(this.height.getUnits(), this.height.getValue());
/*     */       }
/*     */     
/* 229 */     } else if (this.viewBox != null) {
/*     */       
/* 231 */       yy = this.viewBox.y;
/* 232 */       hh = this.viewBox.height;
/* 233 */       this.height = new NumberWithUnits(hh, 1);
/* 234 */       this.y = new NumberWithUnits(yy, 1);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 239 */       yy = (float)defaultBounds.getY();
/* 240 */       hh = (float)defaultBounds.getHeight();
/* 241 */       this.height = new NumberWithUnits(hh, 1);
/* 242 */       this.y = new NumberWithUnits(yy, 1);
/*     */     } 
/*     */     
/* 245 */     this.clipRect.setRect(xx, yy, ww, hh);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderToViewport(Graphics2D g) throws SVGException {
/* 250 */     render(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 256 */     prepareViewport();
/*     */     
/* 258 */     Rectangle targetViewport = g.getClipBounds();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 268 */     Rectangle deviceViewport = this.diagram.getDeviceViewport();
/* 269 */     if (this.width != null && this.height != null) {
/*     */ 
/*     */ 
/*     */       
/* 273 */       float ww, hh, xx = (this.x == null) ? 0.0F : StyleAttribute.convertUnitsToPixels(this.x.getUnits(), this.x.getValue());
/* 274 */       if (this.width.getUnits() == 9) {
/*     */         
/* 276 */         ww = this.width.getValue() * deviceViewport.width;
/*     */       }
/*     */       else {
/*     */         
/* 280 */         ww = StyleAttribute.convertUnitsToPixels(this.width.getUnits(), this.width.getValue());
/*     */       } 
/*     */       
/* 283 */       float yy = (this.y == null) ? 0.0F : StyleAttribute.convertUnitsToPixels(this.y.getUnits(), this.y.getValue());
/* 284 */       if (this.height.getUnits() == 9) {
/*     */         
/* 286 */         hh = this.height.getValue() * deviceViewport.height;
/*     */       }
/*     */       else {
/*     */         
/* 290 */         hh = StyleAttribute.convertUnitsToPixels(this.height.getUnits(), this.height.getValue());
/*     */       } 
/*     */       
/* 293 */       targetViewport = new Rectangle((int)xx, (int)yy, (int)ww, (int)hh);
/*     */     }
/*     */     else {
/*     */       
/* 297 */       targetViewport = new Rectangle(deviceViewport);
/*     */     } 
/* 299 */     this.clipRect.setRect(targetViewport);
/*     */     
/* 301 */     this.viewXform.setTransform(calcViewportTransform(targetViewport));
/*     */     
/* 303 */     AffineTransform cachedXform = g.getTransform();
/* 304 */     g.transform(this.viewXform);
/*     */     
/* 306 */     super.render(g);
/*     */     
/* 308 */     g.setTransform(cachedXform);
/*     */   }
/*     */ 
/*     */   
/*     */   public AffineTransform calcViewportTransform(Rectangle targetViewport) {
/* 313 */     AffineTransform xform = new AffineTransform();
/*     */     
/* 315 */     if (this.viewBox == null) {
/*     */       
/* 317 */       xform.setToIdentity();
/*     */     }
/*     */     else {
/*     */       
/* 321 */       xform.setToIdentity();
/* 322 */       xform.setToTranslation(targetViewport.x, targetViewport.y);
/* 323 */       xform.scale(targetViewport.width, targetViewport.height);
/* 324 */       xform.scale((1.0F / this.viewBox.width), (1.0F / this.viewBox.height));
/* 325 */       xform.translate(-this.viewBox.x, -this.viewBox.y);
/*     */     } 
/*     */     
/* 328 */     return xform;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void pick(Rectangle2D pickArea, AffineTransform ltw, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/* 334 */     if (this.viewXform != null) {
/*     */       
/* 336 */       ltw = new AffineTransform(ltw);
/* 337 */       ltw.concatenate(this.viewXform);
/*     */     } 
/*     */     
/* 340 */     super.pick(pickArea, ltw, boundingBox, retVec);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void pick(Point2D point, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
/* 346 */     Point2D xPoint = new Point2D.Double(point.getX(), point.getY());
/* 347 */     if (this.viewXform != null) {
/*     */       
/*     */       try {
/*     */         
/* 351 */         this.viewXform.inverseTransform(point, xPoint);
/* 352 */       } catch (NoninvertibleTransformException ex) {
/*     */         
/* 354 */         throw new SVGException(ex);
/*     */       } 
/*     */     }
/*     */     
/* 358 */     super.pick(xPoint, boundingBox, retVec);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 364 */     Shape shape = super.getShape();
/* 365 */     return this.viewXform.createTransformedShape(shape);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 371 */     Rectangle2D bbox = super.getBoundingBox();
/* 372 */     return this.viewXform.createTransformedShape(bbox).getBounds2D();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDeviceWidth() {
/* 377 */     return this.clipRect.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDeviceHeight() {
/* 382 */     return this.clipRect.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle2D getDeviceRect(Rectangle2D rect) {
/* 387 */     rect.setRect(this.clipRect);
/* 388 */     return rect;
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
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 400 */     boolean changeState = super.updateTime(curTime);
/*     */     
/* 402 */     StyleAttribute sty = new StyleAttribute();
/* 403 */     boolean shapeChange = false;
/*     */     
/* 405 */     if (getPres(sty.setName("x"))) {
/*     */       
/* 407 */       NumberWithUnits newVal = sty.getNumberWithUnits();
/* 408 */       if (!newVal.equals(this.x)) {
/*     */         
/* 410 */         this.x = newVal;
/* 411 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 415 */     if (getPres(sty.setName("y"))) {
/*     */       
/* 417 */       NumberWithUnits newVal = sty.getNumberWithUnits();
/* 418 */       if (!newVal.equals(this.y)) {
/*     */         
/* 420 */         this.y = newVal;
/* 421 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 425 */     if (getPres(sty.setName("width"))) {
/*     */       
/* 427 */       NumberWithUnits newVal = sty.getNumberWithUnits();
/* 428 */       if (!newVal.equals(this.width)) {
/*     */         
/* 430 */         this.width = newVal;
/* 431 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 435 */     if (getPres(sty.setName("height"))) {
/*     */       
/* 437 */       NumberWithUnits newVal = sty.getNumberWithUnits();
/* 438 */       if (!newVal.equals(this.height)) {
/*     */         
/* 440 */         this.height = newVal;
/* 441 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 445 */     if (getPres(sty.setName("viewBox"))) {
/*     */       
/* 447 */       float[] coords = sty.getFloatList();
/* 448 */       Rectangle2D.Float newViewBox = new Rectangle2D.Float(coords[0], coords[1], coords[2], coords[3]);
/* 449 */       if (!newViewBox.equals(this.viewBox)) {
/*     */         
/* 451 */         this.viewBox = newViewBox;
/* 452 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 456 */     if (shapeChange)
/*     */     {
/* 458 */       build();
/*     */     }
/*     */     
/* 461 */     return (changeState || shapeChange);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StyleSheet getStyleSheet() {
/* 469 */     if (this.styleSheet == null)
/*     */     {
/* 471 */       for (int i = 0; i < getNumChildren(); i++) {
/*     */         
/* 473 */         SVGElement ele = getChild(i);
/* 474 */         if (ele instanceof Style)
/*     */         {
/* 476 */           return ((Style)ele).getStyleSheet();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 481 */     return this.styleSheet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStyleSheet(StyleSheet styleSheet) {
/* 489 */     this.styleSheet = styleSheet;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\SVGRoot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */