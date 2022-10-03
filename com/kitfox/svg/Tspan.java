/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.util.FontSystem;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Point2D;
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
/*     */ public class Tspan
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "tspan";
/*  56 */   float[] x = null;
/*  57 */   float[] y = null;
/*  58 */   float[] dx = null;
/*  59 */   float[] dy = null;
/*  60 */   float[] rotate = null;
/*  61 */   private String text = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  76 */     return "tspan";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddText(SVGLoaderHelper helper, String text) {
/* 129 */     this.text += text;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/* 135 */     super.build();
/*     */     
/* 137 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 139 */     if (getPres(sty.setName("x")))
/*     */     {
/* 141 */       this.x = sty.getFloatList();
/*     */     }
/*     */     
/* 144 */     if (getPres(sty.setName("y")))
/*     */     {
/* 146 */       this.y = sty.getFloatList();
/*     */     }
/*     */     
/* 149 */     if (getPres(sty.setName("dx")))
/*     */     {
/* 151 */       this.dx = sty.getFloatList();
/*     */     }
/*     */     
/* 154 */     if (getPres(sty.setName("dy")))
/*     */     {
/* 156 */       this.dy = sty.getFloatList();
/*     */     }
/*     */     
/* 159 */     if (getPres(sty.setName("rotate"))) {
/*     */       
/* 161 */       this.rotate = sty.getFloatList();
/* 162 */       for (int i = 0; i < this.rotate.length; i++)
/*     */       {
/* 164 */         this.rotate[i] = (float)Math.toRadians(this.rotate[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendToShape(GeneralPath addShape, Point2D cursor) throws SVGException {
/*     */     FontSystem fontSystem;
/* 172 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 174 */     String fontFamily = null;
/* 175 */     if (getStyle(sty.setName("font-family")))
/*     */     {
/* 177 */       fontFamily = sty.getStringValue();
/*     */     }
/*     */ 
/*     */     
/* 181 */     float fontSize = 12.0F;
/* 182 */     if (getStyle(sty.setName("font-size")))
/*     */     {
/* 184 */       fontSize = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 187 */     float letterSpacing = 0.0F;
/* 188 */     if (getStyle(sty.setName("letter-spacing")))
/*     */     {
/* 190 */       letterSpacing = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 193 */     int fontStyle = 0;
/* 194 */     if (getStyle(sty.setName("font-style"))) {
/*     */       
/* 196 */       String s = sty.getStringValue();
/* 197 */       if ("normal".equals(s)) {
/*     */         
/* 199 */         fontStyle = 0;
/* 200 */       } else if ("italic".equals(s)) {
/*     */         
/* 202 */         fontStyle = 1;
/* 203 */       } else if ("oblique".equals(s)) {
/*     */         
/* 205 */         fontStyle = 2;
/*     */       } 
/*     */     } else {
/*     */       
/* 209 */       fontStyle = 0;
/*     */     } 
/*     */     
/* 212 */     int fontWeight = 0;
/* 213 */     if (getStyle(sty.setName("font-weight"))) {
/*     */       
/* 215 */       String s = sty.getStringValue();
/* 216 */       if ("normal".equals(s)) {
/*     */         
/* 218 */         fontWeight = 0;
/* 219 */       } else if ("bold".equals(s)) {
/*     */         
/* 221 */         fontWeight = 1;
/*     */       } 
/*     */     } else {
/*     */       
/* 225 */       fontWeight = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 230 */     Font font = this.diagram.getUniverse().getFont(fontFamily);
/* 231 */     if (font == null && fontFamily != null)
/*     */     {
/* 233 */       fontSystem = FontSystem.createFont(fontFamily, fontStyle, fontWeight, fontSize);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 238 */     if (fontSystem == null)
/*     */     {
/* 240 */       fontSystem = FontSystem.createFont("Serif", fontStyle, fontWeight, fontSize);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     AffineTransform xform = new AffineTransform();
/*     */ 
/*     */ 
/*     */     
/* 251 */     float cursorX = (float)cursor.getX();
/* 252 */     float cursorY = (float)cursor.getY();
/*     */ 
/*     */ 
/*     */     
/* 256 */     String drawText = this.text;
/* 257 */     drawText = drawText.trim();
/* 258 */     for (int i = 0; i < drawText.length(); i++) {
/*     */       
/* 260 */       if (this.x != null && i < this.x.length) {
/*     */         
/* 262 */         cursorX = this.x[i];
/* 263 */       } else if (this.dx != null && i < this.dx.length) {
/*     */         
/* 265 */         cursorX += this.dx[i];
/*     */       } 
/*     */       
/* 268 */       if (this.y != null && i < this.y.length) {
/*     */         
/* 270 */         cursorY = this.y[i];
/* 271 */       } else if (this.dy != null && i < this.dy.length) {
/*     */         
/* 273 */         cursorY += this.dy[i];
/*     */       } 
/*     */ 
/*     */       
/* 277 */       xform.setToIdentity();
/* 278 */       xform.setToTranslation(cursorX, cursorY);
/*     */       
/* 280 */       if (this.rotate != null)
/*     */       {
/* 282 */         xform.rotate(this.rotate[i]);
/*     */       }
/*     */       
/* 285 */       String unicode = drawText.substring(i, i + 1);
/* 286 */       MissingGlyph glyph = fontSystem.getGlyph(unicode);
/*     */       
/* 288 */       Shape path = glyph.getPath();
/* 289 */       if (path != null) {
/*     */         
/* 291 */         path = xform.createTransformedShape(path);
/* 292 */         addShape.append(path, false);
/*     */       } 
/*     */ 
/*     */       
/* 296 */       cursorX += glyph.getHorizAdvX() + letterSpacing;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 301 */     cursor.setLocation(cursorX, cursorY);
/* 302 */     this.strokeWidthScalar = 1.0F;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 364 */     float cursorX = 0.0F;
/* 365 */     float cursorY = 0.0F;
/*     */     
/* 367 */     if (this.x != null) {
/*     */       
/* 369 */       cursorX = this.x[0];
/* 370 */       cursorY = this.y[0];
/* 371 */     } else if (this.dx != null) {
/*     */       
/* 373 */       cursorX += this.dx[0];
/* 374 */       cursorY += this.dy[0];
/*     */     } 
/*     */     
/* 377 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 379 */     String fontFamily = null;
/* 380 */     if (getPres(sty.setName("font-family")))
/*     */     {
/* 382 */       fontFamily = sty.getStringValue();
/*     */     }
/*     */ 
/*     */     
/* 386 */     float fontSize = 12.0F;
/* 387 */     if (getPres(sty.setName("font-size")))
/*     */     {
/* 389 */       fontSize = sty.getFloatValueWithUnits();
/*     */     }
/*     */ 
/*     */     
/* 393 */     Font font = this.diagram.getUniverse().getFont(fontFamily);
/* 394 */     if (font == null) {
/*     */       
/* 396 */       System.err.println("Could not load font");
/* 397 */       Font sysFont = new Font(fontFamily, 0, (int)fontSize);
/* 398 */       renderSysFont(g, sysFont);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 403 */     FontFace fontFace = font.getFontFace();
/* 404 */     int ascent = fontFace.getAscent();
/* 405 */     float fontScale = fontSize / ascent;
/*     */     
/* 407 */     AffineTransform oldXform = g.getTransform();
/* 408 */     AffineTransform xform = new AffineTransform();
/*     */     
/* 410 */     this.strokeWidthScalar = 1.0F / fontScale;
/*     */     
/* 412 */     int posPtr = 1;
/*     */     
/* 414 */     for (int i = 0; i < this.text.length(); i++) {
/*     */       
/* 416 */       xform.setToTranslation(cursorX, cursorY);
/* 417 */       xform.scale(fontScale, fontScale);
/* 418 */       g.transform(xform);
/*     */       
/* 420 */       String unicode = this.text.substring(i, i + 1);
/* 421 */       MissingGlyph glyph = font.getGlyph(unicode);
/*     */       
/* 423 */       Shape path = glyph.getPath();
/* 424 */       if (path != null) {
/*     */         
/* 426 */         renderShape(g, path);
/*     */       } else {
/*     */         
/* 429 */         glyph.render(g);
/*     */       } 
/*     */       
/* 432 */       if (this.x != null && posPtr < this.x.length) {
/*     */         
/* 434 */         cursorX = this.x[posPtr];
/* 435 */         cursorY = this.y[posPtr++];
/* 436 */       } else if (this.dx != null && posPtr < this.dx.length) {
/*     */         
/* 438 */         cursorX += this.dx[posPtr];
/* 439 */         cursorY += this.dy[posPtr++];
/*     */       } 
/*     */       
/* 442 */       cursorX += fontScale * glyph.getHorizAdvX();
/*     */       
/* 444 */       g.setTransform(oldXform);
/*     */     } 
/*     */     
/* 447 */     this.strokeWidthScalar = 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderSysFont(Graphics2D g, Font font) throws SVGException {
/* 452 */     float cursorX = 0.0F;
/* 453 */     float cursorY = 0.0F;
/*     */     
/* 455 */     FontRenderContext frc = g.getFontRenderContext();
/*     */     
/* 457 */     Shape textShape = font.createGlyphVector(frc, this.text).getOutline(cursorX, cursorY);
/* 458 */     renderShape(g, textShape);
/* 459 */     Rectangle2D rect = font.getStringBounds(this.text, frc);
/* 460 */     cursorX += (float)rect.getWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 466 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() {
/* 473 */     return null;
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
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 488 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getText() {
/* 493 */     return this.text;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setText(String text) {
/* 498 */     this.text = text;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Tspan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */