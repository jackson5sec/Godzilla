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
/*    */ public class QuadraticSmooth
/*    */   extends PathCommand
/*    */ {
/* 48 */   public float x = 0.0F;
/* 49 */   public float y = 0.0F;
/*    */ 
/*    */ 
/*    */   
/*    */   public QuadraticSmooth() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return "T " + this.x + " " + this.y;
/*    */   }
/*    */   
/*    */   public QuadraticSmooth(boolean isRelative, float x, float y) {
/* 62 */     super(isRelative);
/* 63 */     this.x = x;
/* 64 */     this.y = y;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void appendPath(GeneralPath path, BuildHistory hist) {
/* 71 */     float offx = this.isRelative ? hist.lastPoint.x : 0.0F;
/* 72 */     float offy = this.isRelative ? hist.lastPoint.y : 0.0F;
/*    */     
/* 74 */     float oldKx = hist.lastKnot.x;
/* 75 */     float oldKy = hist.lastKnot.y;
/* 76 */     float oldX = hist.lastPoint.x;
/* 77 */     float oldY = hist.lastPoint.y;
/*    */     
/* 79 */     float kx = oldX * 2.0F - oldKx;
/* 80 */     float ky = oldY * 2.0F - oldKy;
/*    */     
/* 82 */     path.quadTo(kx, ky, this.x + offx, this.y + offy);
/* 83 */     hist.setLastPoint(this.x + offx, this.y + offy);
/* 84 */     hist.setLastKnot(kx, ky);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumKnotsAdded() {
/* 90 */     return 4;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\QuadraticSmooth.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */