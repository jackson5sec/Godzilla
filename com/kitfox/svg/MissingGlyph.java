/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.pathcmd.BuildHistory;
/*     */ import com.kitfox.svg.pathcmd.PathCommand;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MissingGlyph
/*     */   extends ShapeElement
/*     */ {
/*     */   public static final String TAG_NAME = "missingglyph";
/*  61 */   private Shape path = null;
/*     */   
/*  63 */   private float horizAdvX = -1.0F;
/*  64 */   private float vertOriginX = -1.0F;
/*  65 */   private float vertOriginY = -1.0F;
/*  66 */   private float vertAdvY = -1.0F;
/*     */ 
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
/*  78 */     return "missingglyph";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/*  88 */     super.loaderAddChild(helper, child);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  94 */     super.build();
/*     */     
/*  96 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  98 */     String commandList = "";
/*  99 */     if (getPres(sty.setName("d")))
/*     */     {
/* 101 */       commandList = sty.getStringValue();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 106 */     if (commandList != null) {
/*     */       
/* 108 */       String fillRule = getStyle(sty.setName("fill-rule")) ? sty.getStringValue() : "nonzero";
/*     */       
/* 110 */       PathCommand[] commands = parsePathList(commandList);
/*     */ 
/*     */       
/* 113 */       GeneralPath buildPath = new GeneralPath(fillRule.equals("evenodd") ? 0 : 1, commands.length);
/*     */ 
/*     */       
/* 116 */       BuildHistory hist = new BuildHistory();
/*     */       
/* 118 */       for (int i = 0; i < commands.length; i++) {
/*     */         
/* 120 */         PathCommand cmd = commands[i];
/* 121 */         cmd.appendPath(buildPath, hist);
/*     */       } 
/*     */ 
/*     */       
/* 125 */       AffineTransform at = new AffineTransform();
/* 126 */       at.scale(1.0D, -1.0D);
/* 127 */       this.path = at.createTransformedShape(buildPath);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 132 */     if (getPres(sty.setName("horiz-adv-x")))
/*     */     {
/* 134 */       this.horizAdvX = sty.getFloatValue();
/*     */     }
/*     */     
/* 137 */     if (getPres(sty.setName("vert-origin-x")))
/*     */     {
/* 139 */       this.vertOriginX = sty.getFloatValue();
/*     */     }
/*     */     
/* 142 */     if (getPres(sty.setName("vert-origin-y")))
/*     */     {
/* 144 */       this.vertOriginY = sty.getFloatValue();
/*     */     }
/*     */     
/* 147 */     if (getPres(sty.setName("vert-adv-y")))
/*     */     {
/* 149 */       this.vertAdvY = sty.getFloatValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Shape getPath() {
/* 155 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 163 */     if (this.path != null)
/*     */     {
/* 165 */       renderShape(g, this.path);
/*     */     }
/*     */     
/* 168 */     Iterator<SVGElement> it = this.children.iterator();
/* 169 */     while (it.hasNext()) {
/*     */       
/* 171 */       SVGElement ele = it.next();
/* 172 */       if (ele instanceof RenderableElement)
/*     */       {
/* 174 */         ((RenderableElement)ele).render(g);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getHorizAdvX() {
/* 183 */     if (this.horizAdvX == -1.0F)
/*     */     {
/* 185 */       this.horizAdvX = ((Font)this.parent).getHorizAdvX();
/*     */     }
/* 187 */     return this.horizAdvX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVertOriginX() {
/* 192 */     if (this.vertOriginX == -1.0F)
/*     */     {
/* 194 */       this.vertOriginX = getHorizAdvX() / 2.0F;
/*     */     }
/* 196 */     return this.vertOriginX;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVertOriginY() {
/* 201 */     if (this.vertOriginY == -1.0F)
/*     */     {
/* 203 */       this.vertOriginY = ((Font)this.parent).getFontFace().getAscent();
/*     */     }
/* 205 */     return this.vertOriginY;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVertAdvY() {
/* 210 */     if (this.vertAdvY == -1.0F)
/*     */     {
/* 212 */       this.vertAdvY = ((Font)this.parent).getFontFace().getUnitsPerEm();
/*     */     }
/* 214 */     return this.vertAdvY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 221 */     if (this.path != null)
/*     */     {
/* 223 */       return shapeToParent(this.path);
/*     */     }
/* 225 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 231 */     if (this.path != null)
/*     */     {
/* 233 */       return boundsToParent(includeStrokeInBounds(this.path.getBounds2D()));
/*     */     }
/* 235 */     return null;
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
/* 249 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPath(Shape path) {
/* 257 */     this.path = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHorizAdvX(float horizAdvX) {
/* 265 */     this.horizAdvX = horizAdvX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVertOriginX(float vertOriginX) {
/* 273 */     this.vertOriginX = vertOriginX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVertOriginY(float vertOriginY) {
/* 281 */     this.vertOriginY = vertOriginY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVertAdvY(float vertAdvY) {
/* 289 */     this.vertAdvY = vertAdvY;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\MissingGlyph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */