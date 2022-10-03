/*     */ package com.kitfox.svg.pathcmd;
/*     */ 
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Arc2D;
/*     */ import java.awt.geom.GeneralPath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Arc
/*     */   extends PathCommand
/*     */ {
/*  55 */   public float rx = 0.0F;
/*  56 */   public float ry = 0.0F;
/*  57 */   public float xAxisRot = 0.0F;
/*     */   public boolean largeArc = false;
/*     */   public boolean sweep = false;
/*  60 */   public float x = 0.0F;
/*  61 */   public float y = 0.0F;
/*     */ 
/*     */   
/*     */   public Arc() {}
/*     */ 
/*     */   
/*     */   public Arc(boolean isRelative, float rx, float ry, float xAxisRot, boolean largeArc, boolean sweep, float x, float y) {
/*  68 */     super(isRelative);
/*  69 */     this.rx = rx;
/*  70 */     this.ry = ry;
/*  71 */     this.xAxisRot = xAxisRot;
/*  72 */     this.largeArc = largeArc;
/*  73 */     this.sweep = sweep;
/*  74 */     this.x = x;
/*  75 */     this.y = y;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendPath(GeneralPath path, BuildHistory hist) {
/*  82 */     float offx = this.isRelative ? hist.lastPoint.x : 0.0F;
/*  83 */     float offy = this.isRelative ? hist.lastPoint.y : 0.0F;
/*     */     
/*  85 */     arcTo(path, this.rx, this.ry, this.xAxisRot, this.largeArc, this.sweep, this.x + offx, this.y + offy, hist.lastPoint.x, hist.lastPoint.y);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     hist.setLastPoint(this.x + offx, this.y + offy);
/*  91 */     hist.setLastKnot(this.x + offx, this.y + offy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumKnotsAdded() {
/*  97 */     return 6;
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
/*     */   public void arcTo(GeneralPath path, float rx, float ry, float angle, boolean largeArcFlag, boolean sweepFlag, float x, float y, float x0, float y0) {
/* 135 */     if (rx == 0.0F || ry == 0.0F) {
/* 136 */       path.lineTo(x, y);
/*     */       
/*     */       return;
/*     */     } 
/* 140 */     if (x0 == x && y0 == y) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 146 */     Arc2D arc = computeArc(x0, y0, rx, ry, angle, largeArcFlag, sweepFlag, x, y);
/*     */     
/* 148 */     if (arc == null) {
/*     */       return;
/*     */     }
/* 151 */     AffineTransform t = AffineTransform.getRotateInstance(Math.toRadians(angle), arc.getCenterX(), arc.getCenterY());
/* 152 */     Shape s = t.createTransformedShape(arc);
/* 153 */     path.append(s, true);
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
/*     */   public static Arc2D computeArc(double x0, double y0, double rx, double ry, double angle, boolean largeArcFlag, boolean sweepFlag, double x, double y) {
/* 188 */     double dx2 = (x0 - x) / 2.0D;
/* 189 */     double dy2 = (y0 - y) / 2.0D;
/*     */     
/* 191 */     angle = Math.toRadians(angle % 360.0D);
/* 192 */     double cosAngle = Math.cos(angle);
/* 193 */     double sinAngle = Math.sin(angle);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     double x1 = cosAngle * dx2 + sinAngle * dy2;
/* 199 */     double y1 = -sinAngle * dx2 + cosAngle * dy2;
/*     */     
/* 201 */     rx = Math.abs(rx);
/* 202 */     ry = Math.abs(ry);
/* 203 */     double Prx = rx * rx;
/* 204 */     double Pry = ry * ry;
/* 205 */     double Px1 = x1 * x1;
/* 206 */     double Py1 = y1 * y1;
/*     */     
/* 208 */     double radiiCheck = Px1 / Prx + Py1 / Pry;
/* 209 */     if (radiiCheck > 1.0D) {
/* 210 */       rx = Math.sqrt(radiiCheck) * rx;
/* 211 */       ry = Math.sqrt(radiiCheck) * ry;
/* 212 */       Prx = rx * rx;
/* 213 */       Pry = ry * ry;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     double sign = (largeArcFlag == sweepFlag) ? -1.0D : 1.0D;
/* 220 */     double sq = (Prx * Pry - Prx * Py1 - Pry * Px1) / (Prx * Py1 + Pry * Px1);
/* 221 */     sq = (sq < 0.0D) ? 0.0D : sq;
/* 222 */     double coef = sign * Math.sqrt(sq);
/* 223 */     double cx1 = coef * rx * y1 / ry;
/* 224 */     double cy1 = coef * -(ry * x1 / rx);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     double sx2 = (x0 + x) / 2.0D;
/* 230 */     double sy2 = (y0 + y) / 2.0D;
/* 231 */     double cx = sx2 + cosAngle * cx1 - sinAngle * cy1;
/* 232 */     double cy = sy2 + sinAngle * cx1 + cosAngle * cy1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     double ux = (x1 - cx1) / rx;
/* 238 */     double uy = (y1 - cy1) / ry;
/* 239 */     double vx = (-x1 - cx1) / rx;
/* 240 */     double vy = (-y1 - cy1) / ry;
/*     */ 
/*     */     
/* 243 */     double n = Math.sqrt(ux * ux + uy * uy);
/* 244 */     double p = ux;
/* 245 */     sign = (uy < 0.0D) ? -1.0D : 1.0D;
/* 246 */     double angleStart = Math.toDegrees(sign * Math.acos(p / n));
/*     */ 
/*     */     
/* 249 */     n = Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
/* 250 */     p = ux * vx + uy * vy;
/* 251 */     sign = (ux * vy - uy * vx < 0.0D) ? -1.0D : 1.0D;
/* 252 */     double angleExtent = Math.toDegrees(sign * Math.acos(p / n));
/* 253 */     if (!sweepFlag && angleExtent > 0.0D) {
/* 254 */       angleExtent -= 360.0D;
/* 255 */     } else if (sweepFlag && angleExtent < 0.0D) {
/* 256 */       angleExtent += 360.0D;
/*     */     } 
/* 258 */     angleExtent %= 360.0D;
/* 259 */     angleStart %= 360.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 264 */     Arc2D.Double arc = new Arc2D.Double();
/* 265 */     arc.x = cx - rx;
/* 266 */     arc.y = cy - ry;
/* 267 */     arc.width = rx * 2.0D;
/* 268 */     arc.height = ry * 2.0D;
/* 269 */     arc.start = -angleStart;
/* 270 */     arc.extent = -angleExtent;
/*     */     
/* 272 */     return arc;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 278 */     return "A " + this.rx + " " + this.ry + " " + this.xAxisRot + " " + this.largeArc + " " + this.sweep + " " + this.x + " " + this.y;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\Arc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */