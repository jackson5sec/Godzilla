/*     */ package com.kitfox.svg.animation;
/*     */ 
/*     */ import java.awt.geom.Point2D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Bezier
/*     */ {
/*     */   double length;
/*     */   double[] coord;
/*     */   
/*     */   public Bezier(double sx, double sy, double[] coords, int numCoords) {
/*  52 */     setCoords(sx, sy, coords, numCoords);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCoords(double sx, double sy, double[] coords, int numCoords) {
/*  57 */     this.coord = new double[numCoords * 2 + 2];
/*  58 */     this.coord[0] = sx;
/*  59 */     this.coord[1] = sy;
/*  60 */     for (int i = 0; i < numCoords; i++) {
/*     */       
/*  62 */       this.coord[i * 2 + 2] = coords[i * 2];
/*  63 */       this.coord[i * 2 + 3] = coords[i * 2 + 1];
/*     */     } 
/*     */     
/*  66 */     calcLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLength() {
/*  74 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   private void calcLength() {
/*  79 */     this.length = 0.0D;
/*  80 */     for (int i = 2; i < this.coord.length; i += 2)
/*     */     {
/*  82 */       this.length += lineLength(this.coord[i - 2], this.coord[i - 1], this.coord[i], this.coord[i + 1]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private double lineLength(double x1, double y1, double x2, double y2) {
/*  88 */     double dx = x2 - x1, dy = y2 - y1;
/*  89 */     return Math.sqrt(dx * dx + dy * dy);
/*     */   }
/*     */ 
/*     */   
/*     */   public Point2D.Double getFinalPoint(Point2D.Double point) {
/*  94 */     point.x = this.coord[this.coord.length - 2];
/*  95 */     point.y = this.coord[this.coord.length - 1];
/*  96 */     return point;
/*     */   }
/*     */ 
/*     */   
/*     */   public Point2D.Double eval(double param, Point2D.Double point) {
/* 101 */     point.x = 0.0D;
/* 102 */     point.y = 0.0D;
/* 103 */     int numKnots = this.coord.length / 2;
/*     */     
/* 105 */     for (int i = 0; i < numKnots; i++) {
/*     */       
/* 107 */       double scale = bernstein(numKnots - 1, i, param);
/* 108 */       point.x += this.coord[i * 2] * scale;
/* 109 */       point.y += this.coord[i * 2 + 1] * scale;
/*     */     } 
/*     */     
/* 112 */     return point;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double bernstein(int numKnots, int knotNo, double param) {
/* 123 */     double iParam = 1.0D - param;
/*     */     
/* 125 */     switch (numKnots) {
/*     */       
/*     */       case 0:
/* 128 */         return 1.0D;
/*     */       
/*     */       case 1:
/* 131 */         switch (knotNo) {
/*     */           
/*     */           case 0:
/* 134 */             return iParam;
/*     */           case 1:
/* 136 */             return param;
/*     */         } 
/*     */         
/*     */         break;
/*     */       
/*     */       case 2:
/* 142 */         switch (knotNo) {
/*     */           
/*     */           case 0:
/* 145 */             return iParam * iParam;
/*     */           case 1:
/* 147 */             return 2.0D * iParam * param;
/*     */           case 2:
/* 149 */             return param * param;
/*     */         } 
/*     */         
/*     */         break;
/*     */       
/*     */       case 3:
/* 155 */         switch (knotNo) {
/*     */           
/*     */           case 0:
/* 158 */             return iParam * iParam * iParam;
/*     */           case 1:
/* 160 */             return 3.0D * iParam * iParam * param;
/*     */           case 2:
/* 162 */             return 3.0D * iParam * param * param;
/*     */           case 3:
/* 164 */             return param * param * param;
/*     */         } 
/*     */ 
/*     */         
/*     */         break;
/*     */     } 
/*     */     
/* 171 */     double retVal = 1.0D; int i;
/* 172 */     for (i = 0; i < knotNo; i++)
/*     */     {
/* 174 */       retVal *= param;
/*     */     }
/* 176 */     for (i = 0; i < numKnots - knotNo; i++)
/*     */     {
/* 178 */       retVal *= iParam;
/*     */     }
/* 180 */     retVal *= choose(numKnots, knotNo);
/*     */     
/* 182 */     return retVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int choose(int num, int denom) {
/* 189 */     int denom2 = num - denom;
/* 190 */     if (denom < denom2) {
/*     */       
/* 192 */       int tmp = denom;
/* 193 */       denom = denom2;
/* 194 */       denom2 = tmp;
/*     */     } 
/*     */     
/* 197 */     int prod = 1; int i;
/* 198 */     for (i = num; i > denom; i--)
/*     */     {
/* 200 */       prod *= num;
/*     */     }
/*     */     
/* 203 */     for (i = 2; i <= denom2; i++)
/*     */     {
/* 205 */       prod /= i;
/*     */     }
/*     */     
/* 208 */     return prod;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\Bezier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */