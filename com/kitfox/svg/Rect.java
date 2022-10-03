/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.RectangularShape;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Rect
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "rect";
/*  56 */   float x = 0.0F;
/*  57 */   float y = 0.0F;
/*  58 */   float width = 0.0F;
/*  59 */   float height = 0.0F;
/*  60 */   float rx = 0.0F;
/*  61 */   float ry = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RectangularShape rect;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTagName() {
/*  74 */     return "rect";
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/*  79 */     out.writeFloat(this.x);
/*  80 */     out.writeFloat(this.y);
/*  81 */     out.writeFloat(this.width);
/*  82 */     out.writeFloat(this.height);
/*  83 */     out.writeFloat(this.rx);
/*  84 */     out.writeFloat(this.ry);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException {
/*  89 */     this.x = in.readFloat();
/*  90 */     this.y = in.readFloat();
/*  91 */     this.width = in.readFloat();
/*  92 */     this.height = in.readFloat();
/*  93 */     this.rx = in.readFloat();
/*  94 */     this.ry = in.readFloat();
/*     */     
/*  96 */     if (this.rx == 0.0F && this.ry == 0.0F) {
/*     */       
/*  98 */       this.rect = new Rectangle2D.Float(this.x, this.y, this.width, this.height);
/*     */     } else {
/*     */       
/* 101 */       this.rect = new RoundRectangle2D.Float(this.x, this.y, this.width, this.height, this.rx * 2.0F, this.ry * 2.0F);
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
/*     */   protected void build() throws SVGException {
/* 138 */     super.build();
/*     */     
/* 140 */     StyleAttribute sty = new StyleAttribute();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (getPres(sty.setName("x")))
/*     */     {
/* 153 */       this.x = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 156 */     if (getPres(sty.setName("y")))
/*     */     {
/* 158 */       this.y = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 161 */     if (getPres(sty.setName("width")))
/*     */     {
/* 163 */       this.width = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 166 */     if (getPres(sty.setName("height")))
/*     */     {
/* 168 */       this.height = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/* 171 */     boolean rxSet = false;
/* 172 */     if (getPres(sty.setName("rx"))) {
/*     */       
/* 174 */       this.rx = sty.getFloatValueWithUnits();
/* 175 */       rxSet = true;
/*     */     } 
/*     */     
/* 178 */     boolean rySet = false;
/* 179 */     if (getPres(sty.setName("ry"))) {
/*     */       
/* 181 */       this.ry = sty.getFloatValueWithUnits();
/* 182 */       rySet = true;
/*     */     } 
/*     */     
/* 185 */     if (!rxSet)
/*     */     {
/* 187 */       this.rx = this.ry;
/*     */     }
/* 189 */     if (!rySet)
/*     */     {
/* 191 */       this.ry = this.rx;
/*     */     }
/*     */ 
/*     */     
/* 195 */     if (this.rx == 0.0F && this.ry == 0.0F) {
/*     */       
/* 197 */       this.rect = new Rectangle2D.Float(this.x, this.y, this.width, this.height);
/*     */     } else {
/*     */       
/* 200 */       this.rect = new RoundRectangle2D.Float(this.x, this.y, this.width, this.height, this.rx * 2.0F, this.ry * 2.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 207 */     beginLayer(g);
/* 208 */     renderShape(g, this.rect);
/* 209 */     finishLayer(g);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 215 */     return shapeToParent(this.rect);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 221 */     return boundsToParent(includeStrokeInBounds(this.rect.getBounds2D()));
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
/* 235 */     boolean changeState = super.updateTime(curTime);
/*     */ 
/*     */     
/* 238 */     StyleAttribute sty = new StyleAttribute();
/* 239 */     boolean shapeChange = false;
/*     */     
/* 241 */     if (getPres(sty.setName("x"))) {
/*     */       
/* 243 */       float newVal = sty.getFloatValueWithUnits();
/* 244 */       if (newVal != this.x) {
/*     */         
/* 246 */         this.x = newVal;
/* 247 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 251 */     if (getPres(sty.setName("y"))) {
/*     */       
/* 253 */       float newVal = sty.getFloatValueWithUnits();
/* 254 */       if (newVal != this.y) {
/*     */         
/* 256 */         this.y = newVal;
/* 257 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 261 */     if (getPres(sty.setName("width"))) {
/*     */       
/* 263 */       float newVal = sty.getFloatValueWithUnits();
/* 264 */       if (newVal != this.width) {
/*     */         
/* 266 */         this.width = newVal;
/* 267 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 271 */     if (getPres(sty.setName("height"))) {
/*     */       
/* 273 */       float newVal = sty.getFloatValueWithUnits();
/* 274 */       if (newVal != this.height) {
/*     */         
/* 276 */         this.height = newVal;
/* 277 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 281 */     if (getPres(sty.setName("rx"))) {
/*     */       
/* 283 */       float newVal = sty.getFloatValueWithUnits();
/* 284 */       if (newVal != this.rx) {
/*     */         
/* 286 */         this.rx = newVal;
/* 287 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 291 */     if (getPres(sty.setName("ry"))) {
/*     */       
/* 293 */       float newVal = sty.getFloatValueWithUnits();
/* 294 */       if (newVal != this.ry) {
/*     */         
/* 296 */         this.ry = newVal;
/* 297 */         shapeChange = true;
/*     */       } 
/*     */     } 
/*     */     
/* 301 */     if (shapeChange)
/*     */     {
/* 303 */       build();
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
/* 315 */     return (changeState || shapeChange);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Rect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */