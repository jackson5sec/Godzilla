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
/*    */ public class Quadratic
/*    */   extends PathCommand
/*    */ {
/* 48 */   public float kx = 0.0F;
/* 49 */   public float ky = 0.0F;
/* 50 */   public float x = 0.0F;
/* 51 */   public float y = 0.0F;
/*    */ 
/*    */ 
/*    */   
/*    */   public Quadratic() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return "Q " + this.kx + " " + this.ky + " " + this.x + " " + this.y;
/*    */   }
/*    */ 
/*    */   
/*    */   public Quadratic(boolean isRelative, float kx, float ky, float x, float y) {
/* 65 */     super(isRelative);
/* 66 */     this.kx = kx;
/* 67 */     this.ky = ky;
/* 68 */     this.x = x;
/* 69 */     this.y = y;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void appendPath(GeneralPath path, BuildHistory hist) {
/* 76 */     float offx = this.isRelative ? hist.lastPoint.x : 0.0F;
/* 77 */     float offy = this.isRelative ? hist.lastPoint.y : 0.0F;
/*    */     
/* 79 */     path.quadTo(this.kx + offx, this.ky + offy, this.x + offx, this.y + offy);
/* 80 */     hist.setLastPoint(this.x + offx, this.y + offy);
/* 81 */     hist.setLastKnot(this.kx + offx, this.ky + offy);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumKnotsAdded() {
/* 87 */     return 4;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\Quadratic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */