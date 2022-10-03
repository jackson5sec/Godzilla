/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Marker
/*     */   extends Group
/*     */ {
/*     */   public static final String TAG_NAME = "marker";
/*     */   AffineTransform viewXform;
/*     */   AffineTransform markerXform;
/*     */   Rectangle2D viewBox;
/*     */   float refX;
/*     */   float refY;
/*  58 */   float markerWidth = 1.0F;
/*  59 */   float markerHeight = 1.0F;
/*  60 */   float orient = Float.NaN; boolean markerUnitsStrokeWidth = true;
/*     */   public static final int MARKER_START = 0;
/*     */   public static final int MARKER_MID = 1;
/*     */   public static final int MARKER_END = 2;
/*     */   
/*     */   public String getTagName() {
/*  66 */     return "marker";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void build() throws SVGException {
/*  72 */     super.build();
/*     */     
/*  74 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/*  76 */     if (getPres(sty.setName("refX")))
/*     */     {
/*  78 */       this.refX = sty.getFloatValueWithUnits();
/*     */     }
/*  80 */     if (getPres(sty.setName("refY")))
/*     */     {
/*  82 */       this.refY = sty.getFloatValueWithUnits();
/*     */     }
/*  84 */     if (getPres(sty.setName("markerWidth")))
/*     */     {
/*  86 */       this.markerWidth = sty.getFloatValueWithUnits();
/*     */     }
/*  88 */     if (getPres(sty.setName("markerHeight")))
/*     */     {
/*  90 */       this.markerHeight = sty.getFloatValueWithUnits();
/*     */     }
/*     */     
/*  93 */     if (getPres(sty.setName("orient")))
/*     */     {
/*  95 */       if ("auto".equals(sty.getStringValue())) {
/*     */         
/*  97 */         this.orient = Float.NaN;
/*     */       } else {
/*     */         
/* 100 */         this.orient = sty.getFloatValue();
/*     */       } 
/*     */     }
/*     */     
/* 104 */     if (getPres(sty.setName("viewBox"))) {
/*     */       
/* 106 */       float[] dim = sty.getFloatList();
/* 107 */       this.viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
/*     */     } 
/*     */     
/* 110 */     if (this.viewBox == null)
/*     */     {
/* 112 */       this.viewBox = new Rectangle(0, 0, 1, 1);
/*     */     }
/*     */     
/* 115 */     if (getPres(sty.setName("markerUnits"))) {
/*     */       
/* 117 */       String markerUnits = sty.getStringValue();
/* 118 */       if (markerUnits != null && markerUnits.equals("userSpaceOnUse"))
/*     */       {
/* 120 */         this.markerUnitsStrokeWidth = false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 125 */     this.viewXform = new AffineTransform();
/* 126 */     this.viewXform.scale(1.0D / this.viewBox.getWidth(), 1.0D / this.viewBox.getHeight());
/* 127 */     this.viewXform.translate(-this.viewBox.getX(), -this.viewBox.getY());
/*     */     
/* 129 */     this.markerXform = new AffineTransform();
/* 130 */     this.markerXform.scale(this.markerWidth, this.markerHeight);
/* 131 */     this.markerXform.concatenate(this.viewXform);
/* 132 */     this.markerXform.translate(-this.refX, -this.refY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean outsideClip(Graphics2D g) throws SVGException {
/* 138 */     Shape clip = g.getClip();
/* 139 */     Rectangle2D rect = super.getBoundingBox();
/* 140 */     if (clip == null || clip.intersects(rect))
/*     */     {
/* 142 */       return false;
/*     */     }
/*     */     
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g) throws SVGException {
/* 152 */     AffineTransform oldXform = g.getTransform();
/* 153 */     g.transform(this.markerXform);
/*     */     
/* 155 */     super.render(g);
/*     */     
/* 157 */     g.setTransform(oldXform);
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(Graphics2D g, MarkerPos pos, float strokeWidth) throws SVGException {
/* 162 */     AffineTransform cacheXform = g.getTransform();
/*     */     
/* 164 */     g.translate(pos.x, pos.y);
/* 165 */     if (this.markerUnitsStrokeWidth)
/*     */     {
/* 167 */       g.scale(strokeWidth, strokeWidth);
/*     */     }
/*     */     
/* 170 */     g.rotate(Math.atan2(pos.dy, pos.dx));
/*     */     
/* 172 */     g.transform(this.markerXform);
/*     */     
/* 174 */     super.render(g);
/*     */     
/* 176 */     g.setTransform(cacheXform);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape getShape() {
/* 182 */     Shape shape = super.getShape();
/* 183 */     return this.markerXform.createTransformedShape(shape);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle2D getBoundingBox() throws SVGException {
/* 189 */     Rectangle2D rect = super.getBoundingBox();
/* 190 */     return this.markerXform.createTransformedShape(rect).getBounds2D();
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
/*     */   public boolean updateTime(double curTime) throws SVGException {
/* 203 */     boolean changeState = super.updateTime(curTime);
/*     */     
/* 205 */     build();
/*     */ 
/*     */     
/* 208 */     return changeState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MarkerPos
/*     */   {
/*     */     int type;
/*     */     
/*     */     double x;
/*     */     
/*     */     double y;
/*     */     
/*     */     double dx;
/*     */     
/*     */     double dy;
/*     */ 
/*     */     
/*     */     public MarkerPos(int type, double x, double y, double dx, double dy) {
/* 227 */       this.type = type;
/* 228 */       this.x = x;
/* 229 */       this.y = y;
/* 230 */       this.dx = dx;
/* 231 */       this.dy = dy;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class MarkerLayout
/*     */   {
/* 238 */     private ArrayList<Marker.MarkerPos> markerList = new ArrayList<Marker.MarkerPos>();
/*     */     
/*     */     boolean started = false;
/*     */     
/*     */     public void layout(Shape shape) {
/* 243 */       double px = 0.0D;
/* 244 */       double py = 0.0D;
/* 245 */       double[] coords = new double[6];
/* 246 */       PathIterator it = shape.getPathIterator(null);
/* 247 */       for (; !it.isDone(); it.next()) {
/*     */         double x; double k0x; double y; double k0y; double d1; double k1x; double d2; double k1y; double d3; double d4;
/* 249 */         switch (it.currentSegment(coords)) {
/*     */           
/*     */           case 0:
/* 252 */             px = coords[0];
/* 253 */             py = coords[1];
/* 254 */             this.started = false;
/*     */             break;
/*     */           case 4:
/* 257 */             this.started = false;
/*     */             break;
/*     */           
/*     */           case 1:
/* 261 */             x = coords[0];
/* 262 */             y = coords[1];
/* 263 */             markerIn(px, py, x - px, y - py);
/* 264 */             markerOut(x, y, x - px, y - py);
/* 265 */             px = x;
/* 266 */             py = y;
/*     */             break;
/*     */ 
/*     */           
/*     */           case 2:
/* 271 */             k0x = coords[0];
/* 272 */             k0y = coords[1];
/* 273 */             d1 = coords[2];
/* 274 */             d2 = coords[3];
/*     */ 
/*     */ 
/*     */             
/* 278 */             if (px != k0x || py != k0y) {
/*     */               
/* 280 */               markerIn(px, py, k0x - px, k0y - py);
/*     */             } else {
/*     */               
/* 283 */               markerIn(px, py, d1 - px, d2 - py);
/*     */             } 
/*     */ 
/*     */             
/* 287 */             if (d1 != k0x || d2 != k0y) {
/*     */               
/* 289 */               markerOut(d1, d2, d1 - k0x, d2 - k0y);
/*     */             } else {
/*     */               
/* 292 */               markerOut(d1, d2, d1 - px, d2 - py);
/*     */             } 
/*     */             
/* 295 */             markerIn(px, py, k0x - px, k0y - py);
/* 296 */             markerOut(d1, d2, d1 - k0x, d2 - k0y);
/* 297 */             px = d1;
/* 298 */             py = d2;
/*     */             break;
/*     */ 
/*     */           
/*     */           case 3:
/* 303 */             k0x = coords[0];
/* 304 */             k0y = coords[1];
/* 305 */             k1x = coords[2];
/* 306 */             k1y = coords[3];
/* 307 */             d3 = coords[4];
/* 308 */             d4 = coords[5];
/*     */ 
/*     */             
/* 311 */             if (px != k0x || py != k0y) {
/*     */               
/* 313 */               markerIn(px, py, k0x - px, k0y - py);
/* 314 */             } else if (px != k1x || py != k1y) {
/*     */               
/* 316 */               markerIn(px, py, k1x - px, k1y - py);
/*     */             } else {
/*     */               
/* 319 */               markerIn(px, py, d3 - px, d4 - py);
/*     */             } 
/*     */ 
/*     */             
/* 323 */             if (d3 != k1x || d4 != k1y) {
/*     */               
/* 325 */               markerOut(d3, d4, d3 - k1x, d4 - k1y);
/* 326 */             } else if (d3 != k0x || d4 != k0y) {
/*     */               
/* 328 */               markerOut(d3, d4, d3 - k0x, d4 - k0y);
/*     */             } else {
/*     */               
/* 331 */               markerOut(d3, d4, d3 - px, d4 - py);
/*     */             } 
/* 333 */             px = d3;
/* 334 */             py = d4;
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 340 */       for (int i = 1; i < this.markerList.size(); i++) {
/*     */         
/* 342 */         Marker.MarkerPos prev = this.markerList.get(i - 1);
/* 343 */         Marker.MarkerPos cur = this.markerList.get(i);
/*     */         
/* 345 */         if (cur.type == 0)
/*     */         {
/* 347 */           prev.type = 2;
/*     */         }
/*     */       } 
/* 350 */       Marker.MarkerPos last = this.markerList.get(this.markerList.size() - 1);
/* 351 */       last.type = 2;
/*     */     }
/*     */ 
/*     */     
/*     */     private void markerIn(double x, double y, double dx, double dy) {
/* 356 */       if (!this.started) {
/*     */         
/* 358 */         this.started = true;
/* 359 */         this.markerList.add(new Marker.MarkerPos(0, x, y, dx, dy));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void markerOut(double x, double y, double dx, double dy) {
/* 365 */       this.markerList.add(new Marker.MarkerPos(1, x, y, dx, dy));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ArrayList<Marker.MarkerPos> getMarkerList() {
/* 373 */       return this.markerList;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\Marker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */