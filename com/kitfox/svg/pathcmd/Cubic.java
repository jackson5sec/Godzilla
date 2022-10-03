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
/*    */ public class Cubic
/*    */   extends PathCommand
/*    */ {
/* 48 */   public float k1x = 0.0F;
/* 49 */   public float k1y = 0.0F;
/* 50 */   public float k2x = 0.0F;
/* 51 */   public float k2y = 0.0F;
/* 52 */   public float x = 0.0F;
/* 53 */   public float y = 0.0F;
/*    */ 
/*    */ 
/*    */   
/*    */   public Cubic() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return "C " + this.k1x + " " + this.k1y + " " + this.k2x + " " + this.k2y + " " + this.x + " " + this.y;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Cubic(boolean isRelative, float k1x, float k1y, float k2x, float k2y, float x, float y) {
/* 68 */     super(isRelative);
/* 69 */     this.k1x = k1x;
/* 70 */     this.k1y = k1y;
/* 71 */     this.k2x = k2x;
/* 72 */     this.k2y = k2y;
/* 73 */     this.x = x;
/* 74 */     this.y = y;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void appendPath(GeneralPath path, BuildHistory hist) {
/* 81 */     float offx = this.isRelative ? hist.lastPoint.x : 0.0F;
/* 82 */     float offy = this.isRelative ? hist.lastPoint.y : 0.0F;
/*    */     
/* 84 */     path.curveTo(this.k1x + offx, this.k1y + offy, this.k2x + offx, this.k2y + offy, this.x + offx, this.y + offy);
/*    */ 
/*    */ 
/*    */     
/* 88 */     hist.setLastPoint(this.x + offx, this.y + offy);
/* 89 */     hist.setLastKnot(this.k2x + offx, this.k2y + offy);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumKnotsAdded() {
/* 95 */     return 6;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\Cubic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */