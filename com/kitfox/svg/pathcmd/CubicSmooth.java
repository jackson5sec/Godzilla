/*    */ package com.kitfox.svg.pathcmd;
/*    */ 
/*    */ import java.awt.geom.GeneralPath;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CubicSmooth
/*    */   extends PathCommand
/*    */ {
/* 48 */   public float x = 0.0F;
/* 49 */   public float y = 0.0F;
/* 50 */   public float k2x = 0.0F;
/* 51 */   public float k2y = 0.0F;
/*    */ 
/*    */   
/*    */   public CubicSmooth() {}
/*    */ 
/*    */   
/*    */   public CubicSmooth(boolean isRelative, float k2x, float k2y, float x, float y) {
/* 58 */     super(isRelative);
/* 59 */     this.k2x = k2x;
/* 60 */     this.k2y = k2y;
/* 61 */     this.x = x;
/* 62 */     this.y = y;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void appendPath(GeneralPath path, BuildHistory hist) {
/* 69 */     float offx = this.isRelative ? hist.lastPoint.x : 0.0F;
/* 70 */     float offy = this.isRelative ? hist.lastPoint.y : 0.0F;
/*    */     
/* 72 */     float oldKx = hist.lastKnot.x;
/* 73 */     float oldKy = hist.lastKnot.y;
/* 74 */     float oldX = hist.lastPoint.x;
/* 75 */     float oldY = hist.lastPoint.y;
/*    */     
/* 77 */     float k1x = oldX * 2.0F - oldKx;
/* 78 */     float k1y = oldY * 2.0F - oldKy;
/*    */     
/* 80 */     path.curveTo(k1x, k1y, this.k2x + offx, this.k2y + offy, this.x + offx, this.y + offy);
/* 81 */     hist.setLastPoint(this.x + offx, this.y + offy);
/* 82 */     hist.setLastKnot(this.k2x + offx, this.k2y + offy);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumKnotsAdded() {
/* 88 */     return 6;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return "S " + this.k2x + " " + this.k2y + " " + this.x + " " + this.y;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\CubicSmooth.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */