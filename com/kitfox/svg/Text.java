/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.util.FontSystem;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Text
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "text";
/*  62 */   float x = 0.0F;
/*  63 */   float y = 0.0F;
/*  64 */   AffineTransform transform = null;
/*     */   
/*     */   String fontFamily;
/*     */   float fontSize;
/*  68 */   LinkedList<Serializable> content = new LinkedList<Serializable>();
/*     */   Shape textShape;
/*     */   public static final int TXAN_START = 0;
/*     */   public static final int TXAN_MIDDLE = 1;
/*     */   public static final int TXAN_END = 2;
/*  73 */   int textAnchor = 0;
/*     */   
/*     */   public static final int TXST_NORMAL = 0;
/*     */   public static final int TXST_ITALIC = 1;
/*     */   public static final int TXST_OBLIQUE = 2;
/*     */   int fontStyle;
/*     */   public static final int TXWE_NORMAL = 0;
/*     */   public static final int TXWE_BOLD = 1;
/*     */   public static final int TXWE_BOLDER = 2;
/*     */   public static final int TXWE_LIGHTER = 3;
/*     */   public static final int TXWE_100 = 4;
/*     */   public static final int TXWE_200 = 5;
/*     */   public static final int TXWE_300 = 6;
/*     */   public static final int TXWE_400 = 7;
/*     */   public static final int TXWE_500 = 8;
/*     */   public static final int TXWE_600 = 9;
/*     */   public static final int TXWE_700 = 10;
/*     */   public static final int TXWE_800 = 11;
/*     */   public static final int TXWE_900 = 12;
/*     */   int fontWeight;
/*  93 */   float textLength = -1.0F;
/*  94 */   String lengthAdjust = "spacing";
/*     */ 
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
/* 106 */     return "text";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearContent() {
/* 114 */     this.content.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendText(String text) {
/* 119 */     this.content.addLast(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendTspan(Tspan tspan) throws SVGElementException {
/* 124 */     super.loaderAddChild(null, tspan);
/* 125 */     this.content.addLast(tspan);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rebuild() throws SVGException {
/* 133 */     build();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Serializable> getContent() {
/* 138 */     return this.content;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/* 148 */     super.loaderAddChild(helper, child);
/*     */     
/* 150 */     this.content.addLast(child);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddText(SVGLoaderHelper helper, String text) {
/* 159 */     Matcher matchWs = Pattern.compile("\\s*").matcher(text);
/* 160 */     if (!matchWs.matches())
/*     */     {
/* 162 */       this.content.addLast(text);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void build() throws SVGException {
/* 169 */     super.build();
/*     */     
/* 171 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 173 */     if (getPres(sty.setName("x")))
/*     */     {
/* 175 */       this.x = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 178 */     if (getPres(sty.setName("y")))
/*     */     {
/* 180 */       this.y = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 183 */     if (getStyle(sty.setName("font-family"))) {
/*     */       
/* 185 */       this.fontFamily = sty.getStringValue();
/*     */     }
/*     */     else {
/*     */       
/* 189 */       this.fontFamily = "SansSerif";
/*     */     } 
/*     */     
/* 192 */     if (getStyle(sty.setName("font-size"))) {
/*     */       
/* 194 */       this.fontSize = sty.getFloatValueWithUnits();
/*     */     }
/*     */     else {
/*     */       
/* 198 */       this.fontSize = 12.0F;
/*     */     } 
/*     */     
/* 201 */     if (getStyle(sty.setName("textLength"))) {
/*     */       
/* 203 */       this.textLength = sty.getFloatValueWithUnits();
/*     */     }
/*     */     else {
/*     */       
/* 207 */       this.textLength = -1.0F;
/*     */     } 
/*     */     
/* 210 */     if (getStyle(sty.setName("lengthAdjust"))) {
/*     */       
/* 212 */       this.lengthAdjust = sty.getStringValue();
/*     */     }
/*     */     else {
/*     */       
/* 216 */       this.lengthAdjust = "spacing";
/*     */     } 
/*     */     
/* 219 */     if (getStyle(sty.setName("font-style"))) {
/*     */       
/* 221 */       String s = sty.getStringValue();
/* 222 */       if ("normal".equals(s)) {
/*     */         
/* 224 */         this.fontStyle = 0;
/* 225 */       } else if ("italic".equals(s)) {
/*     */         
/* 227 */         this.fontStyle = 1;
/* 228 */       } else if ("oblique".equals(s)) {
/*     */         
/* 230 */         this.fontStyle = 2;
/*     */       } 
/*     */     } else {
/*     */       
/* 234 */       this.fontStyle = 0;
/*     */     } 
/*     */     
/* 237 */     if (getStyle(sty.setName("font-weight"))) {
/*     */       
/* 239 */       String s = sty.getStringValue();
/* 240 */       if ("normal".equals(s)) {
/*     */         
/* 242 */         this.fontWeight = 0;
/* 243 */       } else if ("bold".equals(s)) {
/*     */         
/* 245 */         this.fontWeight = 1;
/*     */       } 
/*     */     } else {
/*     */       
/* 249 */       this.fontWeight = 0;
/*     */     } 
/*     */     
/* 252 */     if (getStyle(sty.setName("text-anchor"))) {
/*     */       
/* 254 */       String s = sty.getStringValue();
/* 255 */       if (s.equals("middle")) {
/*     */         
/* 257 */         this.textAnchor = 1;
/* 258 */       } else if (s.equals("end")) {
/*     */         
/* 260 */         this.textAnchor = 2;
/*     */       } else {
/*     */         
/* 263 */         this.textAnchor = 0;
/*     */       } 
/*     */     } else {
/*     */       
/* 267 */       this.textAnchor = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     buildText();
/*     */   }
/*     */   
/*     */   protected void buildText() throws SVGException {
/*     */     FontSystem fontSystem;
/*     */     AffineTransform at;
/* 280 */     String[] families = this.fontFamily.split(",");
/* 281 */     Font font = null;
/* 282 */     for (int i = 0; i < families.length; i++) {
/*     */       
/* 284 */       font = this.diagram.getUniverse().getFont(families[i]);
/* 285 */       if (font != null) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 291 */     if (font == null)
/*     */     {
/*     */       
/* 294 */       fontSystem = FontSystem.createFont(this.fontFamily, this.fontStyle, this.fontWeight, this.fontSize);
/*     */     }
/*     */     
/* 297 */     if (fontSystem == null) {
/*     */       
/* 299 */       Logger.getLogger(Text.class.getName()).log(Level.WARNING, "Could not create font " + this.fontFamily);
/* 300 */       fontSystem = FontSystem.createFont("Serif", this.fontStyle, this.fontWeight, this.fontSize);
/*     */     } 
/*     */     
/* 303 */     GeneralPath textPath = new GeneralPath();
/* 304 */     this.textShape = textPath;
/*     */     
/* 306 */     float cursorX = this.x, cursorY = this.y;
/*     */ 
/*     */     
/* 309 */     AffineTransform xform = new AffineTransform();
/*     */     
/* 311 */     for (Serializable obj : this.content) {
/* 312 */       if (obj instanceof String) {
/*     */         
/* 314 */         String text = (String)obj;
/* 315 */         if (text != null)
/*     */         {
/* 317 */           text = text.trim();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 322 */         for (int j = 0; j < text.length(); j++) {
/*     */           
/* 324 */           xform.setToIdentity();
/* 325 */           xform.setToTranslation(cursorX, cursorY);
/*     */ 
/*     */ 
/*     */           
/* 329 */           String unicode = text.substring(j, j + 1);
/* 330 */           MissingGlyph glyph = fontSystem.getGlyph(unicode);
/*     */           
/* 332 */           Shape path = glyph.getPath();
/* 333 */           if (path != null) {
/*     */             
/* 335 */             path = xform.createTransformedShape(path);
/* 336 */             textPath.append(path, false);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 341 */           cursorX += glyph.getHorizAdvX();
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 346 */         this.strokeWidthScalar = 1.0F; continue;
/*     */       } 
/* 348 */       if (obj instanceof Tspan) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 366 */         Tspan tspan = (Tspan)obj;
/* 367 */         Point2D cursor = new Point2D.Float(cursorX, cursorY);
/*     */ 
/*     */         
/* 370 */         tspan.appendToShape(textPath, cursor);
/*     */ 
/*     */         
/* 373 */         cursorX = (float)cursor.getX();
/* 374 */         cursorY = (float)cursor.getY();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 380 */     switch (this.textAnchor) {
/*     */ 
/*     */       
/*     */       case 1:
/* 384 */         at = new AffineTransform();
/* 385 */         at.translate(-textPath.getBounds().getWidth() / 2.0D, 0.0D);
/* 386 */         textPath.transform(at);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 391 */         at = new AffineTransform();
/* 392 */         at.translate(-textPath.getBounds().getWidth(), 0.0D);
/* 393 */         textPath.transform(at);
/*     */         break;
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 484 */     beginLayer(g);
/* 485 */     renderShape(g, this.textShape);
/* 486 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 492 */     return shapeToParent(this.textShape);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 498 */     return boundsToParent(includeStrokeInBounds(this.textShape.getBounds2D()));
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
/* 512 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 515 */     StyleAttribute sty = new StyleAttribute();
/* 516 */     boolean shapeChange = false;
/*     */     
/* 518 */     if (getPres(sty.setName("x"))) {
/*     */       
/* 520 */       float newVal = sty.getFloatValueWithUnits();
/* 521 */       if (newVal != this.x) {
/*     */         
/* 523 */         this.x = newVal;
/* 524 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 528 */     if (getPres(sty.setName("y"))) {
/*     */       
/* 530 */       float newVal = sty.getFloatValueWithUnits();
/* 531 */       if (newVal != this.y) {
/*     */         
/* 533 */         this.y = newVal;
/* 534 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 538 */     if (getStyle(sty.setName("textLength"))) {
/*     */       
/* 540 */       this.textLength = sty.getFloatValueWithUnits();
/*     */     }
/*     */     else {
/*     */       
/* 544 */       this.textLength = -1.0F;
/*     */     } 
/*     */     
/* 547 */     if (getStyle(sty.setName("lengthAdjust"))) {
/*     */       
/* 549 */       this.lengthAdjust = sty.getStringValue();
/*     */     }
/*     */     else {
/*     */       
/* 553 */       this.lengthAdjust = "spacing";
/*     */     } 
/*     */     
/* 556 */     if (getPres(sty.setName("font-family"))) {
/*     */       
/* 558 */       String newVal = sty.getStringValue();
/* 559 */       if (!newVal.equals(this.fontFamily)) {
/*     */         
/* 561 */         this.fontFamily = newVal;
/* 562 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 566 */     if (getPres(sty.setName("font-size"))) {
/*     */       
/* 568 */       float newVal = sty.getFloatValueWithUnits();
/* 569 */       if (newVal != this.fontSize) {
/*     */         
/* 571 */         this.fontSize = newVal;
/* 572 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 577 */     if (getStyle(sty.setName("font-style"))) {
/*     */       
/* 579 */       String s = sty.getStringValue();
/* 580 */       int newVal = this.fontStyle;
/* 581 */       if ("normal".equals(s)) {
/*     */         
/* 583 */         newVal = 0;
/* 584 */       } else if ("italic".equals(s)) {
/*     */         
/* 586 */         newVal = 1;
/* 587 */       } else if ("oblique".equals(s)) {
/*     */         
/* 589 */         newVal = 2;
/*     */       } 
/* 591 */       if (newVal != this.fontStyle) {
/*     */         
/* 593 */         this.fontStyle = newVal;
/* 594 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 598 */     if (getStyle(sty.setName("font-weight"))) {
/*     */       
/* 600 */       String s = sty.getStringValue();
/* 601 */       int newVal = this.fontWeight;
/* 602 */       if ("normal".equals(s)) {
/*     */         
/* 604 */         newVal = 0;
/* 605 */       } else if ("bold".equals(s)) {
/*     */         
/* 607 */         newVal = 1;
/*     */       } 
/* 609 */       if (newVal != this.fontWeight) {
/*     */         
/* 611 */         this.fontWeight = newVal;
/* 612 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 616 */     if (shapeChange)
/*     */     {
/* 618 */       build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 623 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Text.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */